package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.AppParametersDAO;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.RequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Locale;
import java.util.List;

import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.common.api.domain.User;


/**
 * @author Indunil Moremada
 *         struts action class which is used for the birth registration approval purposes
 */

public class BirthRegisterApprovalAction extends ActionSupport implements SessionAware, RequestAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);

    private static final String BR_APPROVAL_ROWS_PER_PAGE = "crs.br_approval_rows_per_page";
    private Map<String, Object> request;
    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final AppParametersDAO appParametersDAO;
    private final BirthRegistrationService service;
    private Map<Integer, String> divisionList;
    private Map<Integer, String> districtList;
    private int division;
    private int district;
    private Map session;
    private List<BirthDeclaration> birthRegisterApproval;
    private long[] index;
    private User user;
    private long bdId;
    private boolean ignoreWarning;

    public BirthRegisterApprovalAction(DistrictDAO districtDAO, BDDivisionDAO bdDivisionDAO, AppParametersDAO appParametersDAO, BirthRegistrationService service) {
        this.districtDAO = districtDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.appParametersDAO = appParametersDAO;
        this.service = service;
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
        if (selectedDistrict == -1 && !districtList.isEmpty()) {
            selectedDistrict = districtList.keySet().iterator().next();
        }
        int selectedDivision = user.getInitialBDDivision();
        if (selectedDivision == -1 && !divisionList.isEmpty()) {
            selectedDivision = divisionList.keySet().iterator().next();
        }
        logger.debug("inside birthRegisterApproval: district {} division {} ", selectedDistrict, selectedDivision);
        session.put("selectedDistrict", selectedDistrict);
        session.put("selectedDivision", selectedDivision);
        /**
         * initially pageNo is set to 1
         */
        int pageNo = 1;
        birthRegisterApproval = service.getConfirmationApprovalPending(
                bdDivisionDAO.getBDDivisionByPK(selectedDivision), pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        paginationHandler(birthRegisterApproval.size());
        session.put("previousFlag", 0);
        session.put("selectedFlag", 0);
        session.put("ApprovalData", birthRegisterApproval);
        session.put("pageNo", pageNo);
        session.put("rowCounter", 0);
        return "pageLoad";
    }

    /**
     * filters birth register approval data
     * by user selected district and division
     *
     * @return String
     */
    public String filter() {
        int pageNo = 1;
        session.put("pageNo", pageNo);
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
        birthRegisterApproval = service.getConfirmationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division), pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("ApprovalData", birthRegisterApproval);
        paginationHandler(birthRegisterApproval.size());
        session.put("ApprovalStart", 0);
        populate();
        return "success";
    }

    /**
     * delete the selected pending birth declaration if there
     * are any warnings redirected to warning displying page
     *
     * @return String
     */
    public String approveBirthDeclaration() {
        //todo set status of the Declaration
        /* logger.debug("inside approveBirthDeclaration : bdId {} observed ", bdId);
    BirthDeclaration bd = service.getById(bdId);
    User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
    List<UserWarning> warnings = service.approveBirthDeclaration(bd, false, user);
    if (warnings != null) {
        Integer pageNo = (Integer) session.get("pageNo");
        district = (Integer) session.get("selectedDistrict");
        division = (Integer) session.get("selectedDivision");
        birthRegisterApproval = service.getConfirmationApprovalPending(bdDivisionDAO.getBDDivision(
            district, division), pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("ApprovalData", birthRegisterApproval);
        paginationHandler(birthRegisterApproval.size());
        populate();
        return "success";
    } else {
        request.put("bdId",bdId);
        request.put("warnings",warnings);
        populate();*/
        request.put("bdId", bdId);//for testing
        return "approvalRejected";
    }
//    }

    public String approveIgnoringWorning() {
        //todo
        logger.debug("inside approveIgnoringWorning bdId {} observed ignoreWarning is {}  ", bdId, ignoreWarning);
        /*if (ignoreWarning) {
            BirthDeclaration bd = service.getById(bdId);
            User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
            service.approveBirthDeclaration(bd, true, user);
        } else {

        }*/
        populate();
        return "success";
    }

    /**
     * @return String which desides the next page
     */
    public String approveAllSelected() {
        //todo
        logger.debug("inside approveAllSelected :");
        /*User user=(User)session.get(WebConstants.SESSION_USER_BEAN);
        service.approveBirthDeclarationIdList(index,user);*/
        return "success";
    }

    /**
     * delete the selected Birth Approval record from
     * the database
     *
     * @return String
     */
    public String deleteBirthDeclaration() {
        //todo display warnings
        logger.debug("inside deleteApprovalPending : bdId {} observed ", bdId);
        BirthDeclaration bd = service.getById(bdId);
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        service.deleteNormalBirthDeclaration(bd, false, user);
        Integer pageNo = (Integer) session.get("pageNo");
        district = (Integer) session.get("selectedDistrict");
        division = (Integer) session.get("selectedDivision");
        birthRegisterApproval = service.getConfirmationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division), pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("ApprovalData", birthRegisterApproval);
        paginationHandler(birthRegisterApproval.size());
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
        districtList = districtDAO.getDistrictNames(language, user);
        divisionList = bdDivisionDAO.getBDDivisionNames(selectedDistrict, language, user);

        logger.debug("inside populate : districts , countriees and races populated.");
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
        district = (Integer) session.get("selectedDistrict");
        division = (Integer) session.get("selectedDivision");
        logger.debug("inside nextPage : district {} division {} observed", district, division);
        /**
         * gets the user selected district to get the records
         * variable nextFlag is used to handle the pagination link
         * in the jsp page 
         */
        birthRegisterApproval = service.getConfirmationApprovalPending(
                bdDivisionDAO.getBDDivisionByPK(division), pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("ApprovalData", birthRegisterApproval);
        paginationHandler(birthRegisterApproval.size());
        /**
         * when page refreshes birthRegisterApproval list
         * does not get any value pageNo is not incremented
         */
        if (birthRegisterApproval.size() == 0) {
            session.put("pageNo", --pageNo);
        } else {
            session.put("pageNo", pageNo);
        }

        session.put("previousFlag", 1);
        session.put("ApprovalStart", ((Integer) session.get("ApprovalStart") + 10));
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
        district = (Integer) session.get("selectedDistrict");
        division = (Integer) session.get("selectedDivision");
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
        birthRegisterApproval = service.getConfirmationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division), pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("ApprovalData", birthRegisterApproval);
        Integer approvalStart = (Integer) session.get("ApprovalStart");
        if (approvalStart > 0) {
            approvalStart = approvalStart - 10;
        }
        session.put("ApprovalStart", approvalStart);
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

    public long[] getIndex() {
        return index;
    }

    public void setIndex(long[] index) {
        this.index = index;
    }

    public long getBdId() {
        return bdId;
    }

    public void setBdId(long bdId) {
        this.bdId = bdId;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    public boolean isIgnoreWarning() {
        return ignoreWarning;
    }

    public void setIgnoreWarning(boolean ignoreWarning) {
        this.ignoreWarning = ignoreWarning;
    }
}
