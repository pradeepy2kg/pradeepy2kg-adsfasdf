package lk.rgd.prs.web;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.common.util.HashUtil;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.DeathPersonInfo;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.impl.datamatrix.SymbolShapeHint;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Generates and serves a barcode image of a person registered in PRS
 *
 * @author: Ashoka Ekanayaka
 */
public class BarCodeImageServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(BarCodeImageServlet.class);

    private PopulationRegistry ecivil;
    private BirthRegistrationService birthRegistrationService;
    private DeathRegistrationService deathRegistrationService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context =
            WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        ecivil = (PopulationRegistry) context.getBean("ecivilService");
        birthRegistrationService = (BirthRegistrationService) context.getBean("manageBirthService");
        deathRegistrationService = (DeathRegistrationService) context.getBean("deathRegisterService");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        try {
            long personId = Long.parseLong(request.getParameter(WebConstants.PERSON_ID));
            String certificateType = request.getParameter(WebConstants.CERTIFICATE_TYPE);

            logger.debug("Person Id received {} and the certificate type {}", personId, certificateType);

            User user = null;
            HttpSession session = request.getSession(false);
            if (session == null || session.isNew()) {
                logger.warn("User has not logged onto the system to invoke this service");
                return;
            } else {
                user = (User) session.getAttribute(WebConstants.SESSION_USER_BEAN);
                if (user == null) {
                    logger.warn("Unexpected - User object is not present in the session");
                    return;
                }
            }

            // Refactored barcode begins here...

            // Hard-coded for a GIF image.
            response.setContentType("image/gif");
            //response.setContentLength(length);

            // Get the output stream from our response object, so we
            // can write our image data to the client:
            ServletOutputStream out = response.getOutputStream();

            try {
                //Create the barcode bean
                DataMatrixBean bean = new DataMatrixBean();

                //Configure the barcode generator
                final int dpi = 300;
                bean.setModuleWidth(0.330); //UnitConv.in2mm(2.0f / dpi));
                bean.doQuietZone(true);
                bean.setShape(SymbolShapeHint.FORCE_SQUARE);

                //Set up the canvas provider for monochrome gif output
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                    out, "image/gif", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
                canvas.establishDimensions(new BarcodeDimension(25, 25));

                //prepare data
                StringBuffer buffer = new StringBuffer();
                BirthDeclaration birthDeclaration;
                DeathRegister deathRegister;

                if (WebConstants.PRS_CERTIFICATE.equals(certificateType) || WebConstants.BIRTH_CERTIFICATE.equals(certificateType)) {
                    Person person = ecivil.getByUKey(personId, user);
                    List<DeathRegister> deathRegisterList;
                    if (person != null) {
                        if (person.getPin() != null) {
                            birthDeclaration = birthRegistrationService.getByPINorNIC(person.getPin(), user);
                            deathRegisterList = deathRegistrationService.getByPinOrNic(person.getPin().toString(), user);
                            if (deathRegisterList != null && deathRegisterList.size() > 0) {
                                deathRegister = deathRegisterList.iterator().next();
                            } else {
                                deathRegister = null;
                            }
                        } else {
                            birthDeclaration = null;
                            deathRegister = null;
                        }

                        // Data for the PRS Certificate and Birth Certificate BarCode
                        buffer.append(person.getFullNameInEnglishLanguage());                           // NAME
                        buffer.append("|");
                        buffer.append(person.getPin());                                                 // PIN
                        buffer.append("|");
                        buffer.append(person.getDateOfBirth());                                         // Date of Birth
                        buffer.append("|");
                        buffer.append(person.getGender());                                              // Gender
                        buffer.append("|");
                        buffer.append(person.getPersonUKey());                                          // PRS Certificate Number
                        buffer.append("|");
                        if (birthDeclaration != null) {                                                 // Birth Certificate Number
                            buffer.append(birthDeclaration.getIdUKey());
                        } else {
                            buffer.append(" - ");
                        }
                        buffer.append("|");
                        if (deathRegister != null) {                                                    // Death Certificate Number
                            buffer.append(deathRegister.getIdUKey());
                        } else {
                            buffer.append(" - ");
                        }
                        buffer.append("|");
                        if (WebConstants.PRS_CERTIFICATE.equals(certificateType)) {
                            if (person.getDateOfRegistration() != null) {
                                buffer.append(person.getDateOfRegistration());                          // Date of Registration PRS
                            } else {
                                buffer.append(" - ");
                            }
                        } else if (WebConstants.BIRTH_CERTIFICATE.equals(certificateType)) {
                            if (birthDeclaration.getRegister().getDateOfRegistration() != null) {
                                buffer.append(birthDeclaration.getRegister().getDateOfRegistration());      // Date of Registration Birth
                            } else {
                                buffer.append(" - ");
                            }
                        } else {
                            buffer.append(" - ");
                        }
                        buffer.append("|");
                        buffer.append(DateTimeUtils.getISO8601FormattedString(new Date()));         // Date of Issue
                        buffer.append("|");
                        String hash = HashUtil.hashString(buffer.toString());
                        buffer.append(hash);                                                      // TODO Hash
                    } else {
                        logger.debug("Person {} not found.", personId);
                    }
                } else if (WebConstants.DEATH_CERTIFICATE.equals(certificateType)) {
                    // personID in Death is the idUKey of Death Certificate.
                    deathRegister = deathRegistrationService.getById(personId, user);

                    if (deathRegister != null) {
                        DeathPersonInfo deathPersonInfo = deathRegister.getDeathPerson();
                        Person person;
                        String PINorNIC = deathPersonInfo.getDeathPersonPINorNIC();

                        if (deathPersonInfo.getDeathPersonNameInEnglish() != null) {
                            buffer.append(deathPersonInfo.getDeathPersonNameInEnglish());               // NAME
                        } else {
                            buffer.append(" - ");
                        }
                        buffer.append("|");
                        if (PINorNIC != null) {
                            /* Find the person only if the PIN is given. As there can be more than one record for a NIC. */
                            if (PINorNIC.length() == 12) {
                                person = ecivil.getPersonByPIN(Long.parseLong(PINorNIC), user);
                                birthDeclaration = birthRegistrationService.getByPINorNIC(Long.parseLong(PINorNIC), user);
                            } else {
                                person = null;
                                birthDeclaration = null;
                            }
                            buffer.append(PINorNIC);                                                    // PIN or NIC
                        } else {
                            person = null;
                            birthDeclaration = null;
                            buffer.append(" - ");
                        }
                        buffer.append("|");
                        if (deathRegister.getDeath().getDateOfDeath() != null) {
                            buffer.append(deathRegister.getDeath().getDateOfDeath());                   // Date of Death
                        } else {
                            buffer.append(" - ");
                        }
                        buffer.append("|");
                        buffer.append(deathPersonInfo.getDeathPersonGender());                          // Gender
                        buffer.append("|");
                        if (person != null) {
                            buffer.append(person.getPersonUKey());                                      // PRS Certificate Number
                        } else {
                            buffer.append(" - ");
                        }
                        buffer.append("|");
                        if (birthDeclaration != null) {
                            buffer.append(birthDeclaration.getIdUKey());                                // Birth Certificate Number
                        } else {
                            buffer.append(" - ");
                        }
                        buffer.append("|");
                        buffer.append(deathRegister.getIdUKey());                                       // Death Certificate Number
                        buffer.append("|");
                        if (deathRegister.getDeath().getDateOfRegistration() != null) {
                            buffer.append(deathRegister.getDeath().getDateOfRegistration());            // Date of Registration Death
                        } else {
                            buffer.append(" - ");
                        }
                        buffer.append("|");
                        buffer.append(DateTimeUtils.getISO8601FormattedString(new Date()));             // Date of Issue
                        buffer.append("|");
                        String hash = HashUtil.hashString(buffer.toString());
                        buffer.append(hash);                                                          // TODO Hash
                    } else {
                        logger.debug("Death {} not found.", personId);
                    }
                }
                logger.debug("Barcode data size : {}", buffer.length());

                //Generate the barcode
                bean.generateBarcode(canvas, buffer.toString());

                //Signal end of generation
                canvas.finish();
            } finally {
                out.flush();
                out.close();
            }
            // Refactored barcode ends here...
        } catch (Exception e) {
            logger.error("Unexpected error generating the bar code : " + e.getLocalizedMessage());
        }
    }

    protected void doPost(
        HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        // Pass through to the doPost method:
        doGet(request, response);
    }
}
