package lk.rgd.crs.web.action.deaths;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * @author Duminda Dharmakeerthi
 */
public class DeathRegisterAction extends ActionSupport implements SessionAware {

    private Map session;
    private int pageNo;


    public String welcome(){
        return SUCCESS;
    }

    public String deathDeclaration(){
        switch (pageNo){
            case 0:
                return "form0";
            case 1:
                return "form1";
            case 2:
                return "form2";
            case 3:
                return SUCCESS;
        }
        return ERROR;
    }

    public String deathCertificate(){
        return SUCCESS;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageNo() {
        return pageNo;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map map) {
        this.session = map;
    }
}
