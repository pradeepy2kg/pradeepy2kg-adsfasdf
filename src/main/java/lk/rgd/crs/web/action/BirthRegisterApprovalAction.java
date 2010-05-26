package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.ArrayList;
import java.util.Locale;
import java.sql.Date;

import lk.rgd.crs.api.domain.BirthRegisterApproval;
import lk.rgd.crs.api.dao.DistrictDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.web.WebConstants;


/**
 * @author Duminda Dharmakeerthi.
 * @author Indunil Moremada
 *         struts action class which is used for the birth registration approval purposes
 */

public class BirthRegisterApprovalAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);

    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private Map<Integer, String> divisionList;
    private Map<Integer, String> districtList;

    private boolean expired;
    private Map session;
    private ArrayList<BirthRegisterApproval> birthRegisterApproval = new ArrayList();
    private ArrayList<BirthRegisterApproval> birthRegisterApprovalExpired = new ArrayList();

    public BirthRegisterApprovalAction(DistrictDAO districtDAO, BDDivisionDAO bdDivisionDAO) {
        this.districtDAO = districtDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        if (birthRegisterApproval.isEmpty())
            this.createArrayList();
    }

    /**
     * tempory data for testing purposes
     */
    private void createArrayList() {
        BirthRegisterApproval BRA = new BirthRegisterApproval();
        BirthRegisterApproval BRA1 = new BirthRegisterApproval();
        BirthRegisterApproval BRA2 = new BirthRegisterApproval();
        BirthRegisterApproval BRA3 = new BirthRegisterApproval();
        BirthRegisterApproval BRA4 = new BirthRegisterApproval();
        BirthRegisterApproval BRA5 = new BirthRegisterApproval();
        BirthRegisterApproval BRA6 = new BirthRegisterApproval();
        BirthRegisterApproval BRA7 = new BirthRegisterApproval();
        BirthRegisterApproval BRA8 = new BirthRegisterApproval();


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
        BRA2.setRecievedDate(new Date(108, 03, 02));
        BRA2.setActions("Expired");
        birthRegisterApproval.add(BRA2);

        BRA3.setSerial(5);
        BRA3.setName("kamal");
        BRA3.setChanges(true);
        BRA3.setRecievedDate(new Date(110, 10, 24));
        BRA3.setActions("Expired");
        birthRegisterApproval.add(BRA3);

        BRA4.setSerial(4);
        BRA4.setName("Nimal");
        BRA4.setChanges(true);
        BRA4.setRecievedDate(new Date(107, 01, 01));
        BRA4.setActions("Approve");
        birthRegisterApproval.add(BRA4);

        BRA5.setSerial(8);
        BRA5.setName("sunil");
        BRA5.setChanges(true);
        BRA5.setRecievedDate(new Date(108, 10, 5));
        BRA5.setActions("Expired");
        birthRegisterApproval.add(BRA5);

        BRA6.setSerial(17);
        BRA6.setName("jagath");
        BRA6.setChanges(true);
        BRA6.setRecievedDate(new Date(110, 04, 20));
        BRA6.setActions("Expired");
        birthRegisterApproval.add(BRA6);

        BRA7.setSerial(16);
        BRA7.setName("sampath");
        BRA7.setChanges(true);
        BRA7.setRecievedDate(new Date(110, 02, 19));
        BRA7.setActions("Approve");
        birthRegisterApproval.add(BRA7);

        BRA8.setSerial(21);
        BRA8.setName("nuwan");
        BRA8.setChanges(false);
        BRA8.setRecievedDate(new Date(108, 10, 5));
        BRA8.setActions("Approve");
        birthRegisterApproval.add(BRA8);
    }

    public String birthRegisterApproval() {
        populate();

        session.put("ApprovelData", birthRegisterApproval);
        session.put("page_title", "birth register approval");
        return "pageLoad";
    }

    /**
     * Populate master data to the UIs
     */
    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("inside populate : {} observed.", language);

        divisionList = bdDivisionDAO.getDivisions(language, 11);
        districtList = districtDAO.getDistricts(language);
        logger.debug("inside populte : districts , countries and races populated.");
    }

    /**
     * getExpiredList method is used to get expired
     * BR approval data then store them in the session
     * for further usage
     *
     * @return success is returned if expired checkbox
     *         is selected by the user else redirect is returned
     *         redirect causes to redirect the action to the
     *         birthRegisterApproval() action
     */
    public String getExpiredList() {
        populate();
        if (isExpired()) {
            birthRegisterApproval = (ArrayList<BirthRegisterApproval>) session.get("ApprovelData");
            for (BirthRegisterApproval bra : birthRegisterApproval) {
                if (bra.getActions().equals("Expired")) {
                    birthRegisterApprovalExpired.add(bra);
                }
            }
            session.put("ApprovelData", birthRegisterApprovalExpired);
            return "success";
        } else {
            birthRegisterApproval();
            return "redirect";
        }
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

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public Map<Integer, String> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(Map<Integer, String> divisionList) {
        this.divisionList = divisionList;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
