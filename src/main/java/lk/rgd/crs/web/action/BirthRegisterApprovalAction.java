package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.AppParametersDAO;
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

    private static final String BR_APPROVAL_ROWS_PER_PAGE = "crs.br_approval_rows_per_page";

    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final AppParametersDAO appParametersDAO;
    private Map<Integer, String> divisionList;
    private Map<Integer, String> districtList;
    private int division;
    private int district;
    private Map session;
    private List<BirthDeclaration> birthRegisterApproval;

    private User user;
    private String bdId;

    public BirthRegisterApprovalAction(DistrictDAO districtDAO, BDDivisionDAO bdDivisionDAO,
                                       BirthDeclarationDAO birthDeclarationDAO, AppParametersDAO appParametersDAO) {
        this.districtDAO = districtDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.appParametersDAO = appParametersDAO;
    }

    /**
     * gets the BirthRegisterApproval data which is to be displayed
     * and store them in the session to get later in the jsp
     * page naviagation can be done only if previousFlag is set to 1
     * or nextFlag is set to 1  selectedFlag is to capture whether
     * user selects a particular district or division if district
     * or division is selected selectedFlag is set to 1 else it
     * is set to 0
     *
     * @return String
     */
    public String birthRegisterApproval() {
        populate();
        int selectedDistrict = user.getInitialDistrict();
        int selectedDivision = user.getInitialBDDivision();
        /**
         * initially pageNo is set to 1
         */
        int pageNo = 1;
        birthRegisterApproval = birthDeclarationDAO.getConfirmationApprovalPending(
            bdDivisionDAO.getBDDivision(selectedDistrict, selectedDivision), pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        paginationHandler(birthRegisterApproval.size());
        session.put("previousFlag", 0);
        session.put("selectedFlag", 0);
        session.put("ApprovalData", birthRegisterApproval);
        session.put("pageNo", pageNo);
        return "pageLoad";
    }

    /**
     * filters birth register approval data
     * by user selected district and division
     *
     * @return String
     */
    public String filter() {
        //todo pagination for the filtering
        Integer pageNo = (Integer) session.get("pageNo");
        if (logger.isDebugEnabled()) {
            logger.debug("inside filter : district {} division {} observed ", district, division + " Page number " + pageNo);
        }
        /**
         * following session variables are assigned for
         * pagination purposes  
         */
        session.put("selectedDistrict", district);
        session.put("selectedDivision", division);
        session.put("selectedFlag", 1);
        birthRegisterApproval = birthDeclarationDAO.getConfirmationApprovalPending(bdDivisionDAO.getBDDivision(
            district, division), pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("ApprovalData", birthRegisterApproval);
        paginationHandler(birthRegisterApproval.size());
        session.put("previousFlag", 0);
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
        logger.debug("inside ApprovalSelected :", session.get("index"));
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
     *
     * @return String
     */
    public String nextPage() {
        Integer pageNo = (Integer) session.get("pageNo");
        Integer selectedFlag = (Integer) session.get("selectedFlag");
        logger.debug("inside nextPage : pageNo {} and selectedFlag {} observed", pageNo, selectedFlag);
        pageNo++;
        if (selectedFlag == 1) {
            district = (Integer) session.get("selectedDistrict");
            division = (Integer) session.get("selectedDivision");
            session.put("selectedFlag", 0);
        } else {
            user = (User) session.get(WebConstants.SESSION_USER_BEAN);
            district = user.getInitialDistrict();
            division = user.getInitialBDDivision();
        }
        /**
         * gets the user selected district to get the records
         * variable nextFlag is used to handle the pagination link
         * in the jsp page 
         */
        birthRegisterApproval = birthDeclarationDAO.getConfirmationApprovalPending(
            bdDivisionDAO.getBDDivision(district, division), pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("ApprovalData", birthRegisterApproval);
        paginationHandler(birthRegisterApproval.size());
        session.put("previousFlag", 1);
        session.put("pageNo", pageNo);
        populate();
        return "success";
    }

    /**
     * handles pagination of birth registration approval data
     *
     * @return String
     */
    public String previousPage() {
        Integer pageNo = (Integer) session.get("pageNo");
        Integer selectedFlag = (Integer) session.get("selectedFlag");
        if (logger.isDebugEnabled()) {
            logger.debug("inside filter : district {} division {} observed ", district, division +
                " Page number " + pageNo.toString() + " selectedFlag " + selectedFlag.toString());
        }
        /**
         * UI related handle whether to display the
         * next link and previous link
         */
        Integer previousFlag = (Integer) session.get("previousFlag");
        if (previousFlag == 1 && pageNo == 2) {
            /**
             * request is comming backword(calls previous
             * to load the very first page
             */
            session.put("previousFlag", 0);
        } else if (pageNo == 1) {
            /**
             * if request is from page one
             * in the next page previous link
             * should be displayed
             */
            session.put("previousFlag", 0);
        } else {
            session.put("previousFlag", 1);
        }
        session.put("nextFlag", 1);
        if (pageNo > 1) {
            pageNo--;
        }
        session.put("pageNo", pageNo);
        /**
         * decides district and division whether it is
         * user selected or not
         */
        if (selectedFlag == 1) {
            district = (Integer) session.get("selectedDistrict");
            division = (Integer) session.get("selectedDivision");
            session.put("selectedFlag", 1);
        } else {
            user = (User) session.get(WebConstants.SESSION_USER_BEAN);
            district = user.getInitialDistrict();
            division = user.getInitialBDDivision();
        }
        birthRegisterApproval = birthDeclarationDAO.getConfirmationApprovalPending(bdDivisionDAO.getBDDivision(
            district, division), pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("ApprovalData", birthRegisterApproval);
        populate();
        return "success";
    }

    /**
     * responsible for whether to display the next link in
     * the birthRegisterApproval jsp or not and handles the
     * page number
     *
     * @param recordsFound no of birth register approval pending
     *                     records found
     */
    public void paginationHandler(int recordsFound) {
        if (recordsFound == appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE)) {
            session.put("nextFlag", 1);
        } else {
            session.put("nextFlag", 0);
        }
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

    public String getBdId() {
        return bdId;
    }

    public void setBdId(String bdId) {
        this.bdId = bdId;
    }
}
