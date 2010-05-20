package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;


/**
 * @author Duminda Dharmakeerthi.
 * @author Indunil Moremada.
 * @author Amith Jayasekara.
 * @author Chathuranga Withana.
 */

public class BirthRegisterApprovalAction extends ActionSupport implements SessionAware {

    private Map session;

    public String birthRegisterApproval(){
        session.put("info", "IN birthregisterApproval");
        return "pageLoad";
    }

    public String getList(){
        session.put("info", "IN the correct method");
        return "pageLoad";
    }

    public void setSession(Map map) {
        this.session = map;
    }
}
