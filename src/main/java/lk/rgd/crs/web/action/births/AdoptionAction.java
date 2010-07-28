package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: tharanga
 * Date: Jul 27, 2010
 * Time: 3:49:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdoptionAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionAction.class);

    private Map session;

    public String adoptionDeclaration() {
        return SUCCESS;
    }

    public String initAdoptionRegistration() {
        return SUCCESS;
    }

    public String adoptionReRegistration() {
        return SUCCESS;
    }

    public String adoptionApprovalAndPrint() {
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
