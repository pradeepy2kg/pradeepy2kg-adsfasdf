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
    private static final String MALE = "M";
    private static final String FEMALE = "F";
    private static final String UNKNOWN = "U";

    private static final int SINHALA_UN_MIN = 3458;
    private static final int SINHALA_UN_MAX = 3572;
    private static final int TAMIL_UN_MIN = 2946;
    private static final int TAMIL_UN_MAX = 3066;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String text = request.getParameter(WebConstants.TEXT_TO_TRANS).trim();
        String genderStr = request.getParameter(WebConstants.GENDER).trim();

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

        if (text == null || text.isEmpty()) {
            logger.warn("Input text for transliteration service is empty");
            return;
        }

        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();

        HashMap<String, Object> untyped = new HashMap<String, Object>();

        int source = checkInputTextLanguage(text);
        int gender = checkGender(genderStr);
        String transText = null;

        switch (source) {
            case Transliterate.ENGLISH:
                break;
            default:
                transText = Transliterate.translateLine(text, source, Transliterate.ENGLISH, gender);
        }

        untyped.put(WebConstants.TEXT_TRANSLATED, transText);

        mapper.writeValue(out, untyped);
        out.flush();
    }

    private int checkInputTextLanguage(String text) {
        // TODO take 3 texts and compare
        int i = text.charAt(0);
        if (i >= SINHALA_UN_MIN && i <= SINHALA_UN_MAX) {
            return Transliterate.SINHALA;
        } else if (i >= TAMIL_UN_MIN && i <= TAMIL_UN_MAX) {
            return Transliterate.TAMIL;
        } else {
            return Transliterate.ENGLISH;
        }
    }

    private int checkGender(String gender) {
        if (gender.equals(MALE)) {
            return Transliterate.MALE;
        } else if (gender.equals(FEMALE)) {
            return Transliterate.FEMALE;
        } else {
            return Transliterate.UNKNOWN;
        }
    }
}
