package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.DistrictDAO;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.common.api.domain.User;


/**
 * @author Duminda Dharmakeerthi.
 * @author Indunil Moremada
 *         struts action class which is used for the birth registration approval purposes
 */

public class BirthRegisterApprovalAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);

    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final BirthDeclarationDAO birthDeclarationDAO;
    private Map<Integer, String> divisionList;
    private Map<Integer, String> districtList;

    private boolean expired;
    private Map session;
    private List<BirthDeclaration> birthRegisterApproval;

    private User user;
    public BirthRegisterApprovalAction(DistrictDAO districtDAO, BDDivisionDAO bdDivisionDAO, BirthDeclarationDAO birthDeclarationDAO) {
        this.districtDAO = districtDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.birthDeclarationDAO = birthDeclarationDAO;
    }

    /**
     * gets the BirthRegisterApproval data which is to be displayed
     * and store them in the session to get later in the jsp
     *
     * @return String
     */
    public String birthRegisterApproval() {
        populate();
        //todo if it wants to get the user selected district this has to be modified
        birthRegisterApproval = birthDeclarationDAO.getBirthRegistrationPending(user.getAssignedBDDivision(), false);
        session.put("ApprovalData", birthRegisterApproval);
        return "pageLoad";
    }

    /**
     * Populate master data to the UIs
     */
    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("inside populate : {} observed.", language);
        //todo division id should be the user selection division
        divisionList = bdDivisionDAO.getDivisions(language, 11, user);
        districtList = districtDAO.getDistricts(language, user);
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
        if (expired) {
            logger.debug("insid getExpiredList: checked {} ", expired);
            birthRegisterApproval = birthDeclarationDAO.getBirthRegistrationPending(user.getAssignedBDDivision(), true);
            /** here it replacess the session variable ApprovalData with the expiredApprovalData */
            session.put("ApprovalData", birthRegisterApproval);
            if (birthRegisterApproval.size() < 10) {
                session.put("flag", 0);
            }
            return "success";
        } else {
            session.put("flag", 1);
            birthRegisterApproval();
            return "redirect";
        }
    }

    /**
     * handles pagination of birth registration approval data
     * sets the session variable to load the next ten records
     * flag variable is decide whether to display the Next link
     * or not.
     *
     * @return String
     */
    public String nextPage() {
        Integer i = (Integer) session.get("approvalStart");
        Integer counter = (Integer) session.get("counter");
        birthRegisterApproval = (ArrayList<BirthDeclaration>) session.get("ApprovalData");

        logger.debug("Next Page: Count {} , ApprovalArrayList Size {}", counter, birthRegisterApproval.size());
        int boundary = birthRegisterApproval.size();
        if (i != null && boundary > counter) {
            if (boundary > (counter + 10)) {
                i = i + 10;
                session.put("flag", 1);
            } else {
                i = counter;
                session.put("flag", 0);
            }
            session.put("approvalStart", i);
        }
        populate();
        return "success";
    }

    /**
     * handles pagination of birth registration approval data
     *
     * @return String
     */
    public String previousPage() {
        Integer i = (Integer) session.get("approvalStart");
        if (i != null && i != 0) {
            session.put("approvalStart", i - 10);
            session.put("flag", 1);
        }
        populate();
        return "success";
    }

    public void setBirthRegisterApproval(ArrayList<BirthDeclaration> birthRegisterApproval) {
        this.birthRegisterApproval = birthRegisterApproval;
    }

    public List<BirthDeclaration> getBirthRegisterApproval() {
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

    public BirthDeclarationDAO getBirthDeclarationDAO() {
        return birthDeclarationDAO;
    }
}
