package lk.rgd.crs.web;

import lk.rgd.common.util.PinAndNicUtils;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.Registrar;
import lk.rgd.crs.api.service.RegistrarManagementService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @author Mahesha
 * @author amith jayasekara
 */
public class JSONRegistrarLookupService extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(JSONRegistrarLookupService.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private RegistrarManagementService registrarManager;
    private PopulationRegistry populationRegistry;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context =
            WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        registrarManager = (RegistrarManagementService) context.getBean("registrarManagementService");
        populationRegistry = (PopulationRegistry) context.getBean("ecivilService");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pinOrNic = request.getParameter(WebConstants.REQUEST_PIN_NIC);
        logger.debug("Received Pin/NIC : " + pinOrNic);
        HashMap<String, Object> untyped = new HashMap<String, Object>();
        User user;
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

        try {
            Registrar registrar = null;
            if (PinAndNicUtils.isValidNIC(pinOrNic)) {
                registrar = registrarManager.getRegistrarByNIC(pinOrNic, user);
            } else if (PinAndNicUtils.isValidPIN(pinOrNic)) {
                registrar = registrarManager.getRegistrarByPin(Long.parseLong(pinOrNic), user);
            }
            if (registrar != null) {
                logger.debug("registrar found for pin or nic :{}", pinOrNic);
                untyped.put("fullNameInOfficialLanguage", registrar.getFullNameInOfficialLanguage());
                untyped.put("fullNameInEnglishLanguage", registrar.getFullNameInEnglishLanguage());
                untyped.put("address", registrar.getCurrentAddress());
            }else{
                //find minister in PRS there is no way to validate given person is a minister
                Person minister = populationRegistry.findUniquePersonByPINorNIC(pinOrNic, user);
                if (minister != null) {
                    logger.debug("person found as a minister , pin or nic : {}", pinOrNic);
                    untyped.put("fullNameInOfficialLanguage", minister.getFullNameInOfficialLanguage());
                    untyped.put("fullNameInEnglishLanguage", minister.getFullNameInEnglishLanguage());
                    untyped.put("address", minister.getCurrentAddress());
                }
            }
        } catch (NumberFormatException e) {
            //find minister in PRS there is no way to validate given person is a minister
            Person minister = populationRegistry.findUniquePersonByPINorNIC(pinOrNic, user);
            if (minister != null) {
                logger.debug("person found as a minister , pin or nic : {}", pinOrNic);
                untyped.put("fullNameInOfficialLanguage", minister.getFullNameInOfficialLanguage());
                untyped.put("fullNameInEnglishLanguage", minister.getFullNameInEnglishLanguage());
                untyped.put("address", minister.getCurrentAddress());
            }
        }

        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        mapper.writeValue(out, untyped);
        out.flush();

    }
}
