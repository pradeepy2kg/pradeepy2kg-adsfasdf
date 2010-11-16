package lk.rgd.prs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

/**
 * @author Chathuranga Withana
 */
public class PersonApprovalAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(PersonApprovalAction.class);

    // services and DAOs
    private PopulationRegistry service;

    private Map session;
    private User user;

    private Map<Integer, String> locationList;

    private String language;

    public PersonApprovalAction(PopulationRegistry service) {
        this.service = service;
    }

    /**
     * This method used to load person pending approval list
     */
    public String loadPersonApprovalPendingList() {
        logger.debug("Loading person list pending approval");
        locationList = user.getActiveLocations(language);
        return SUCCESS;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("setting User : {} and Language : {}", user.getUserName(), language);
    }

    public Map<Integer, String> getLocationList() {
        return locationList;
    }

    public void setLocationList(Map<Integer, String> locationList) {
        this.locationList = locationList;
    }
}
