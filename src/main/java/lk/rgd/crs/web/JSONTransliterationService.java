package lk.rgd.crs.web;

import lk.rgd.common.api.domain.User;
import lk.transliterate.Transliterate;
import org.apache.derby.impl.sql.compile.JavaToSQLValueNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @author Chathuranga Withana
 */
public class JSONTransliterationService extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(JSONTransliterationService.class);
    private final ObjectMapper mapper = new ObjectMapper();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String text = request.getParameter(WebConstants.TEXT_TO_TRANS);
        String sourceLang = request.getParameter("");
        String destLang = request.getParameter("");
        String gender = request.getParameter(WebConstants.GENDER);

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

        if (text == null) {
            logger.warn("");
            return;
        }

        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();

        HashMap<String, Object> untyped = new HashMap<String, Object>();

        String transText = Transliterate.translateLine(text, Transliterate.SINHALA, Transliterate.ENGLISH, Transliterate.MALE);
        untyped.put(WebConstants.TEXT_TRANSLATED, transText);

        mapper.writeValue(out, untyped);
        out.flush();
    }
}
