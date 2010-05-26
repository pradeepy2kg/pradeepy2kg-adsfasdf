package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.dispatcher.ApplicationMap;
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

    private String district;
    private String division;
    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private Map<Integer, String> divisionList;
    private Map<Integer, String> districtList;
    private Map session;
    private ArrayList<BirthRegisterApproval> birthRegisterApproval = new ArrayList();
    private ArrayList<BirthRegisterApproval> birthRegisterApprovalExpired=new ArrayList();

    public BirthRegisterApprovalAction(DistrictDAO districtDAO, BDDivisionDAO bdDivisionDAO) {
        this.districtDAO = districtDAO;
        this.bdDivisionDAO = bdDivisionDAO;
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
        BRA2.setActions("Expired");
        birthRegisterApproval.add(BRA2);
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

    public String getExpiredList(){

        return "sucess";
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
