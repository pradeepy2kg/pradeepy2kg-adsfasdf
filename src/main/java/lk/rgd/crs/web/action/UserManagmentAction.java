package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import lk.rgd.common.api.domain.User;

/**
 * @author amith jayasekara
 */
public class UserManagmentAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);
    private Map session;
    private User user = new User();
    private int pageNo = 0;


    public String creatUser() {
        logger.info("creat user called");
        if(pageNo == 1)  {
            // to do the code.....
            return "sucess";
        }
        return "pageLoad";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageNo() {

        return pageNo;
    }
    
    public void setSession(Map map) {
        this.session = map;
    }
}
