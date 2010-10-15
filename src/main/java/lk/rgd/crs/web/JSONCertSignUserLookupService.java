package lk.rgd.crs.web;

import lk.rgd.AppConstants;
import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.dao.UserLocationDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.UserLocation;
import lk.rgd.common.util.NameFormatUtil;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BirthDeclaration;
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
    private BirthDeclarationDAO birthDeclarationDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context =
                WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        userLocationDAO = (UserLocationDAO) context.getBean("userLocationDAOImpl");
        userDAO = (UserDAO) context.getBean("userDAOImpl");
        birthDeclarationDAO = (BirthDeclarationDAO) context.getBean("birthDeclarationDAOImpl");
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
        logger.debug("Received User Location Id : {} and Mode : {}", userLocationId, mode);

        HashMap<String, Object> optionList = new HashMap<String, Object>();

        try {
            // TODO
            HttpSession session = request.getSession();

            int locationId = Integer.parseInt(userLocationId);
            long certificateId = Long.parseLong(certId);

            if ("1".equals(mode)) {
                optionList.put("authorizedUsers", getBirthCertSignUsers(locationId));
            } else if ("2".equals(mode)) {
                optionList.put("authorizedUsers", getDeathCertSignUsers(locationId));
            } else if ("3".equals(mode)) {
                optionList.put("authorizedUsers", getAdoptionCertSignUsers(locationId));
            } else if ("4".equals(mode)) {
                optionList.put("authorizedUsers", getMarriageCertSignUsers(locationId));
            } else if ("5".equals(mode)) {
            } else {
                if (userId == null) {
                    //get user id from session
                    User user = (User) session.getAttribute(WebConstants.SESSION_USER_BEAN);
                    userId = user.getUserId();
                }
                UserLocation userLocation = userLocationDAO.getUserLocation(userId, locationId);
                String lang = getUserPrefLang(certificateId);

                if (AppConstants.SINHALA.equals(lang) || AppConstants.TAMIL.equals(lang)) {
                    optionList.put("officerSignature", userLocation.getUser().getUserSignature(lang));
                    optionList.put("locationSignature", userLocation.getLocation().getLocationSignature(lang));
                    optionList.put("locationName", userLocation.getLocation().getLocationName(lang));
                    if (AppConstants.SINHALA.equals(lang))
                        optionList.put("locationAddress", userLocation.getLocation().getSiLocationMailingAddress());
                    if (AppConstants.TAMIL.equals(lang))
                        optionList.put("locationAddress", userLocation.getLocation().getTaLocationMailingAddress());

                } else {
                    // TODO throw exception
                }
            }

        } catch (NumberFormatException e) {
            logger.error("Invalid number used for location id", e);
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
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private List<SelectOption> getAdoptionCertSignUsers(int locationId) {
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private List<SelectOption> getMarriageCertSignUsers(int locationId) {
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // TODO
    private String getUserPrefLang(long certIdUKey) {
        BirthDeclaration bd = birthDeclarationDAO.getById(certIdUKey);
        return bd.getRegister().getPreferredLanguage();
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