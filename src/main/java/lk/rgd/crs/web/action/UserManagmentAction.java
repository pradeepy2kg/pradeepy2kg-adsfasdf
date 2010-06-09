package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author amith jayasekara
 */
public class UserManagmentAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);
    private Map session;


    public String creatUser() {
         logger.info("creat user called");
        return "pageLoad";
    }


    public void setSession(Map<String, Object> stringObjectMap) {

    }
}
