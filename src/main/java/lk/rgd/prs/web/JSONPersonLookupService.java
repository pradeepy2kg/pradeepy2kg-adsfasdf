package lk.rgd.prs.web;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import lk.rgd.crs.web.WebConstants;
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
import java.util.HashMap;

/**
 * @author asankha
 */
public class JSONPersonLookupService extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(JSONPersonLookupService.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private PopulationRegistry ecivil;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context =
            WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        ecivil = (PopulationRegistry) context.getBean("ecivilService");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pinOrNic = request.getParameter(WebConstants.REQUEST_PIN_NIC);
        logger.debug("Received Pin/NIC : " + pinOrNic);

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

        Person person = ecivil.findPersonByPINorNIC(pinOrNic, user);
        if (person != null) {
            logger.debug("Loaded person : " + person.getFullNameInOfficialLanguage());

            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();

            HashMap<String, Object> untyped = new HashMap<String, Object>();

            final String nameInOfficialLanguage = person.getFullNameInOfficialLanguage();
            // For any record in the PRS the name should be available at least one language (e.g. for migrated data)
            if (nameInOfficialLanguage == null || nameInOfficialLanguage.trim().length() == 0) {
                untyped.put("fullNameInOfficialLanguage", person.getFullNameInEnglishLanguage());
            } else {
                untyped.put("fullNameInOfficialLanguage", nameInOfficialLanguage);
            }
            untyped.put("gender", person.getGender());
            untyped.put("dateOfBirth", person.getDateOfBirth());
            untyped.put("placeOfBirth", person.getPlaceOfBirth());
            int raceId = (person.getRace() != null) ? person.getRace().getRaceId() : 0;
            untyped.put("race", raceId);

            if (person.getLastAddress() != null && person.getLastAddress().isPermanent()) {
                untyped.put("address", person.getLastAddress().getLine1());
            }
            if (person.getLastAddress() != null) {
                untyped.put("lastAddress", person.getLastAddress().toString());
            } else {
                untyped.put("lastAddress", "");
            }

            mapper.writeValue(out, untyped);
            out.flush();
        }
    }
}
