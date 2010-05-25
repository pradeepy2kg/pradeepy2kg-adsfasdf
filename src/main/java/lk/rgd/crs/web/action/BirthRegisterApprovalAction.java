package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.dispatcher.ApplicationMap;

import java.util.Map;
import java.util.ArrayList;
import java.sql.Date;

import lk.rgd.crs.api.domain.BirthRegisterApproval;


/**
 * @author Duminda Dharmakeerthi.
 * @author Indunil Moremada
 * struts action class which is used for the birth registration approval purposes
 */

public class BirthRegisterApprovalAction extends ActionSupport implements SessionAware {

    private Map session;
    private static ArrayList<BirthRegisterApproval> birthRegisterApproval = new ArrayList();


    public BirthRegisterApprovalAction() {
        if (birthRegisterApproval.isEmpty())
            this.createArrayList();
    }

    /**
     * Tempory code for testing.
     */
    private void createArrayList() {
        BirthRegisterApproval BRA = new BirthRegisterApproval();
        BirthRegisterApproval BRA1 = new BirthRegisterApproval();
        BirthRegisterApproval BRA2 = new BirthRegisterApproval();

        BRA.setSerial(10);
        BRA.setName("Duminda");
        BRA.setChanges(true);
        BRA.setRecievedDate(new Date(110, 10, 24));
        BRA.setActions("Expired");
        birthRegisterApproval.add(BRA);

        BRA1.setSerial(2);
        BRA1.setName("Indunil");
        BRA1.setChanges(true);
        BRA1.setRecievedDate(new Date(110, 05, 12));
        BRA1.setActions("Approve");
        birthRegisterApproval.add(BRA1);

        BRA2.setSerial(3);
        BRA2.setName("Amith");
        BRA2.setChanges(false);
        BRA2.setRecievedDate(new Date(108, 10, 5));
        BRA2.setActions("Approve");
        birthRegisterApproval.add(BRA2);
    }

    public String birthRegisterApproval() {
        session.put("ApprovelData", birthRegisterApproval);
        session.put("page_title", "birth register approval");
        return "pageLoad";
    }

    public void setBirthRegisterApproval(ArrayList<BirthRegisterApproval> birthRegisterApproval) {
        this.birthRegisterApproval = birthRegisterApproval;
    }

    public ArrayList<BirthRegisterApproval> getBirthRegisterApproval() {
        return birthRegisterApproval;
    }

    public void setSession(Map map) {
        this.session = map;
    }
}
