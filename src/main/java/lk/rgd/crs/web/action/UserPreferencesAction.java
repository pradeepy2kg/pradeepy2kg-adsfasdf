package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Locale;

import lk.rgd.crs.web.WebConstants;
import lk.rgd.AppConstants;

/**
 * @author Duminda
 * 
 */
public class UserPreferencesAction extends ActionSupport implements SessionAware {

    private String language;
    private Map session;

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);

    /**
     * Set the Language that the user preffered to work
     * And set preffered language to the session
     */
    public String selectLanguage() {
        logger.debug("inside selectLanguage : {} passed.", language);
        String country = "LK";
        if (language.equals(AppConstants.ENGLISH)) {
            country = "US";
        }

        session.put(WebConstants.SESSION_USER_LANG, new Locale(language, country));
        session.put("page_title", "home");
        return "success";
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setSession(Map map) {
        this.session = map;
    }
}
