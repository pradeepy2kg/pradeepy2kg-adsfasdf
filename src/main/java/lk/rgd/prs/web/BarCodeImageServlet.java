package lk.rgd.prs.web;

import lk.rgd.common.api.domain.User;
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

/**
 * Generates and serves a barcode image of a person registered in PRS
 *
 * @author: Ashoka Ekanayaka
 */
public class BarCodeImageServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(BarCodeImageServlet.class);

    private PopulationRegistry ecivil;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context =
            WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        ecivil = (PopulationRegistry) context.getBean("ecivilService");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        try {
            long personId = Long.parseLong(request.getParameter(WebConstants.PERSON_ID));
            logger.debug("Person Id received {}", personId);

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

            Person person = ecivil.getByUKey(personId, user);
            if (person != null) {
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
                    //buffer.append(URLEncoder.encode(person.getFullNameInOfficialLanguage(), "UTF-8"));
                    //buffer.append("|");
                    buffer.append(person.getFullNameInEnglishLanguage());
                    buffer.append("|");
                    //buffer.append("NIC=");
                    //buffer.append(person.getNic());
                    //buffer.append("|");
                    buffer.append(person.getPin());
                    buffer.append("|");
                    buffer.append(person.getGender());
                    buffer.append("|");
                    buffer.append(person.getPersonUKey());
                    buffer.append("|");
                    buffer.append(person.getDateOfBirth());
                    logger.debug("Barcode data size : {}", buffer.length());

                    //Generate the barcode
                    bean.generateBarcode(canvas, buffer.toString());

                    //Signal end of generation
                    canvas.finish();
                } finally {
                    out.flush();
                    out.close();
                }
            } else {
                logger.debug("person not found {}", personId);
            }
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
