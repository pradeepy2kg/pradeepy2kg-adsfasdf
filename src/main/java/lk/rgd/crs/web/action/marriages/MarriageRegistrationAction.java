package lk.rgd.crs.web.action.marriages;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.WebConstants;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;

import java.util.Map;

/**
 * @author amith
 *         action class for marriage registration
 */
public class MarriageRegistrationAction extends ActionSupport implements SessionAware {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MarriageRegistrationAction.class);

    private User user;

    private Map session;

    /**
     * loading marriage notice page
     */
    public String marriageNoticeInit() {
        logger.debug("attempt to load marriage notice page");
        return "pageLoad";
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("setting User: {}", user.getUserName());
    }
}
