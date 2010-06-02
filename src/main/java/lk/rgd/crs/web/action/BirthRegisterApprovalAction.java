package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.DistrictDAO;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
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
    private int division;
    private int district;
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
        int selectedDistrict = user.getInitialDistrict();
        int selectedDivision = user.getInitialBDDivision();
        birthRegisterApproval = birthDeclarationDAO.getConfirmationApprovalPending(
            bdDivisionDAO.getBDDivision(selectedDistrict, selectedDivision));
        session.put("ApprovalData", birthRegisterApproval);
        return "pageLoad";
    }

    /**
     * filters birth register approval data
     * by user selected district and division
     *
     * @return String
     */
    public String filter() {
        logger.debug("inside filter : district {} and division {} observed", district, division);
        birthRegisterApproval = birthDeclarationDAO.getConfirmationApprovalPending(bdDivisionDAO.getBDDivision(district, division));
        session.put("ApprovalData", birthRegisterApproval);
        populate();
        return "success";
    }

    /**
     * All the selected Birth Registration Approval data
     * will be persisted without directing to the birth
     * confirmation
     *
     * @return String
     */
    public String ApproveSelected() {
        //todo this has to be implemented
        logger.debug("inside ApprovalSelected :");
        populate();
        return "success";
    }

    /**
     * Populate master data to the UIs
     */
    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("inside populate : {} observed.", language);
        int selectedDistrict = user.getInitialDistrict();
        districtList = districtDAO.getDistricts(language, user);
        divisionList = bdDivisionDAO.getDivisions(language, selectedDistrict, user);

        logger.debug("inside populate : districts , countries and races populated.");
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
        birthRegisterApproval = (List<BirthDeclaration>) session.get("ApprovalData");

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

    public void setBirthRegisterApproval(List<BirthDeclaration> birthRegisterApproval) {
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

    public BirthDeclarationDAO getBirthDeclarationDAO() {
        return birthDeclarationDAO;
    }

    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }
}
