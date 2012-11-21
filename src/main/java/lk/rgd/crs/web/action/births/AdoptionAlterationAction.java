package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.web.WebConstants;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

/**
 * @author Duminda Dharmakeerthi
 */
public class AdoptionAlterationAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionAlterationAction.class);

    private Map session;
    private User user;
    private String language;

    private AdoptionOrder adoptionOrder;

    public String pageLoad(){
        return SUCCESS;
    }

    public String addAdoptionAlteration(){
        return SUCCESS;
    }

    public String updateAdoptionAlteration(){
        return SUCCESS;
    }

    public String approveOrRejectAdoptionAlteration(){
        return SUCCESS;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public AdoptionOrder getAdoptionOrder() {
        return adoptionOrder;
    }

    public void setAdoptionOrder(AdoptionOrder adoptionOrder) {
        this.adoptionOrder = adoptionOrder;
    }

    public void setSession(Map map) {
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("setting User: {} and language : {}", user.getUserName(), language);
    }

    public Map getSession() {
        return session;
    }
}
