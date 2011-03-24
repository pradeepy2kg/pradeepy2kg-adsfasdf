package lk.rgd.crs.web;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import lk.rgd.AppConstants;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.dao.UserLocationDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.UserLocation;
import lk.rgd.common.util.NameFormatUtil;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.MarriageRegister;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Chathuranga Withana
 */
public class JSONCertSignUserLookupService extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(JSONCertSignUserLookupService.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private UserLocationDAO userLocationDAO;
    private UserDAO userDAO;
    private DistrictDAO districtDAO;
    private DSDivisionDAO dsDivisionDAO;
    private BirthDeclarationDAO birthDeclarationDAO;
    private MarriageRegistrationDAO marriageRegistrationDAO;
    private static final String TYPE_MARRIAGE = "marriage";
    private static final String TYPE_BIRTH = "birth";
    private static final String TYPE_DEATH = "death";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context =
            WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        userLocationDAO = (UserLocationDAO) context.getBean("userLocationDAOImpl");
        userDAO = (UserDAO) context.getBean("userDAOImpl");
        birthDeclarationDAO = (BirthDeclarationDAO) context.getBean("birthDeclarationDAOImpl");
        marriageRegistrationDAO = (MarriageRegistrationDAO) context.getBean("marriageRegistrationDAOImpl");
        districtDAO = (DistrictDAO) context.getBean("districtDAOImpl");
        dsDivisionDAO = (DSDivisionDAO) context.getBean("dsDivisionDAOImpl");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userLocationId = request.getParameter(WebConstants.USER_LOCATION_ID);
        String mode = request.getParameter(WebConstants.MODE);
        String userId = request.getParameter(WebConstants.USER_ID);
        String certId = request.getParameter(WebConstants.CERTIFICATE_ID);
        String type = request.getParameter(WebConstants.TYPE);

        int locationId = Integer.parseInt(userLocationId);
        long certificateId = Long.parseLong(certId);


        if (logger.isDebugEnabled()) {
            logger.debug("Received UserLocationId : " + userLocationId + ", Mode : " + mode + ", UserId : " + userId +
                ", CertificateId : " + certId);
        }
        if (userLocationId == null || mode == null || certId == null) {
            logger.warn("Required parameters passed to this service is not valid");
            return;
        }

        HashMap<String, Object> optionList = new HashMap<String, Object>();

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.isNew()) {
                logger.warn("User has not logged on to the system to invoke this service");
                return;
            } else {
                User user = (User) session.getAttribute(WebConstants.SESSION_USER_BEAN);
                if (user == null) {
                    logger.warn("Unexpected - User object is not present in the session");
                    return;
                }
            }
            if ("1".equals(mode)) {
                optionList.put("authorizedUsers", getBirthCertSignUsers(locationId));
            } else if ("2".equals(mode)) {
                optionList.put("authorizedUsers", getDeathCertSignUsers(locationId));
            } else if ("3".equals(mode)) {
                optionList.put("authorizedUsers", getAdoptionCertSignUsers(locationId));
            } else if ("4".equals(mode)) {
                optionList.put("authorizedUsers", getMarriageCertSignUsers(locationId));
            } else {
                String lang = getUserPrefLang(certificateId, type);
                if (userId == null) {
                    User user = (User) session.getAttribute(WebConstants.SESSION_USER_BEAN);
                    userId = user.getUserId();
                }
                UserLocation userLocation = userLocationDAO.getUserLocation(userId, locationId);

                DSDivision ds = dsDivisionDAO.getDSDivisionByPK(userLocation.getLocation().getDsDivisionId());
                District district = ds.getDistrict();

                if (AppConstants.SINHALA.equals(lang) || AppConstants.TAMIL.equals(lang)) {
                    optionList.put("officerSignature", userLocation.getUser().getUserSignature(lang));
                    optionList.put("locationSignature", userLocation.getLocation().getLocationSignature(lang));
                    optionList.put("locationName", userLocation.getLocation().getLocationName(lang));
                    if (AppConstants.SINHALA.equals(lang)) {
                        optionList.put("locationAddress", userLocation.getLocation().getSiLocationMailingAddress());
                        //district and division
                        optionList.put("locationDistrictInOl", district.getSiDistrictName());
                        optionList.put("locationDivisionInOl", ds.getSiDivisionName());
                    }
                    if (AppConstants.TAMIL.equals(lang)) {
                        optionList.put("locationAddress", userLocation.getLocation().getTaLocationMailingAddress());
                        //district and division
                        optionList.put("locationDistrictInOl", district.getTaDistrictName());
                        optionList.put("locationDivisionInOl", ds.getTaDivisionName());
                    }
                    optionList.put("locationDivisionInEn", ds.getEnDivisionName());
                    optionList.put("locationDistrictInEn", district.getEnDistrictName());
                } else {
                    logger.warn("Unexpected language passed to the service");
                    return;
                }
            }

        } catch (NumberFormatException e) {
            logger.error("Invalid number used for locationId or certificateId", e);
        } catch (Exception e) {
            logger.error("Unexpected Exception encountered ", e);
        }

        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();

        mapper.writeValue(out, optionList);
        out.flush();
    }

    private List<SelectOption> getBirthCertSignUsers(int locationId) {
        List<User> users = userLocationDAO.getBirthCertSignUsersByLocationId(locationId, true);
        return getList(users);
    }

    private List<SelectOption> getDeathCertSignUsers(int locationId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private List<SelectOption> getAdoptionCertSignUsers(int locationId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private List<SelectOption> getMarriageCertSignUsers(int locationId) {
        List<User> users = userLocationDAO.getMarriageCertificateSignUsersByLocationId(locationId, true);
        return getList(users);
    }

    private String getUserPrefLang(long certIdUKey, String type) {
        if (TYPE_MARRIAGE.equals(type)) {
            MarriageRegister mr = marriageRegistrationDAO.getByIdUKey(certIdUKey);
            return mr.getPreferredLanguage();
        } else if (TYPE_DEATH.equals(type)) {
            //get preferred language for death certificate
        } else {
            BirthDeclaration bd = birthDeclarationDAO.getById(certIdUKey);
            return bd.getRegister().getPreferredLanguage();
        }
        //default
        return AppConstants.SINHALA;
    }

    private List getList(List<User> users) {
        List<SelectOption> ds = new ArrayList<SelectOption>();

        for (User u : users) {
            SelectOption option = new SelectOption();
            option.setOptionValue(u.getUserId());
            option.setOptionDisplay(NameFormatUtil.getDisplayName(u.getUserName(), 50));
            ds.add(option);
        }
        logger.debug("Select option list size : {}", ds.size());
        return ds;
    }
}