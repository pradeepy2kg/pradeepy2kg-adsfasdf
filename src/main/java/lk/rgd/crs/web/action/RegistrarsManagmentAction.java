package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * action class for managing registras
 *
 * @author amith jayasekara
 */
public class RegistrarsManagmentAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(RegistrarsManagmentAction.class);
    private Map session;

    public String registrarsManagmentHome() {
        return SUCCESS;
    }

    public void setSession(Map map) {
        logger.debug("Set session {}", map);
        this.session = map;
    }

    public Map getSession() {
        return session;
    }


}
