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
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.common.api.domain.User;
import lk.rgd.Permission;


/**
 * @author Indunil Moremada
 *         struts action class which is used for the BirthDeclaration approval purposes
 */

public class BirthRegisterApprovalAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);


    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final AppParametersDAO appParametersDAO;
    private final BirthRegistrationService service;
    private static final String BR_APPROVAL_ROWS_PER_PAGE = "crs.br_approval_rows_per_page";

    private Map session;

    private Map<Integer, String> divisionList;
    private Map<Integer, String> districtList;
    private List<UserWarning> warnings;
    private List<BirthDeclaration> birthDeclarationPendingList;
    private int division;
    private int district;
    private long[] index;
    private User user;
    private long bdId;
    private int pageNo;
    private int recordCounter;
    private boolean nextFlag;
    private boolean previousFlag;
    private boolean ignoreWarning;
    private boolean allowEditBDF;
    private boolean allowApproveBDF;

    public BirthRegisterApprovalAction(DistrictDAO districtDAO, BDDivisionDAO bdDivisionDAO, AppParametersDAO appParametersDAO, BirthRegistrationService service) {
        this.districtDAO = districtDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.appParametersDAO = appParametersDAO;
        this.service = service;
    }

    /**
     * gets the BirthDeclarations which are not
     * confirmed yet by the parents and set them in the
     * session to get later in the jsp page naviagation
     * can be done only if previousFlag is set to 1 or
     * nextFlag is set to 1 initial data are loaded based
     * on the first district of the allowed district
     * and the first division of the allowed division
     *
     * @return String
     */
    public String birthDeclarationApproval() {
        initPermission();
        populate();
        setInitialDistrict();
        if (!divisionList.isEmpty()) {
            division = divisionList.keySet().iterator().next();
        }
        //todo set following after changing the backend
        logger.debug("inside birthDeclarationPendingList: district {} division {} selected ", district, division);
        /**
         * initially pageNo is set to 1
         */
        setPageNo(1);
        birthDeclarationPendingList = service.getDeclarationApprovalPending(
            bdDivisionDAO.getBDDivisionByPK(division), getPageNo(), appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        paginationHandler(birthDeclarationPendingList.size());
        setPreviousFlag(false);
        return "pageLoad";
    }

    /**
     * handles the approval, edit, delete and reject permission
     * of the user
     */
    private void initPermission() {
        setAllowApproveBDF(user.isAuthorized(Permission.APPROVE_BDF));
        setAllowEditBDF(user.isAuthorized(Permission.EDIT_BDF));
    }

    /**
     * filters BirthDeclarations which are not
     * yet approved by user selected district
     * and division
     *
     * @return String
     */
    public String filter() {
        setPageNo(1);
        initPermission();
        if (logger.isDebugEnabled()) {
            logger.debug("inside filter : district {} division {} observed ", district, division + " Page number " + pageNo);
        }
        birthDeclarationPendingList = service.getDeclarationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division),
            pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        paginationHandler(birthDeclarationPendingList.size());
        setRecordCounter(0);
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
        initPermission();
        logger.debug("inside approveBirthDeclaration : bdId {} observed ", bdId);
        BirthDeclaration bd = service.getById(bdId, user);
        boolean caughtException = false;
        try {
            warnings = service.approveBirthDeclaration(bd, false, user);
        }
        catch (CRSRuntimeException e) {
            logger.error("inside approveBirthDeclaration : {} , {} ", e.getErrorCode(), e);
            addActionError(getText("brapproval.approval.error." + e.getErrorCode()));
            caughtException = true;
        }

        if (caughtException || (warnings != null && warnings.isEmpty())) {
            birthDeclarationPendingList = service.getDeclarationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division),
                pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
            if (birthDeclarationPendingList.size() > 0) {
                paginationHandler(birthDeclarationPendingList.size());
            } else {
                setPageNo(getPageNo() - 1);
            }
            populate();
            return "success";
        } else {
            //todo check whether warnings are working fine
            populate();
            return "approvalRejected";
        }
    }

    /**
     * Used for direct Birth Declaration Registration after filling BDFs
     *
     * @return
     */
    public String approveBirthDeclarationForm() {
        initPermission();
        BirthDeclaration bd = service.getById(bdId, user);
        boolean caughtException = false;

        try {
            warnings = service.approveBirthDeclaration(bd, false, user);
        }
        catch (CRSRuntimeException e) {
            logger.error("inside approveBirthDeclarationForm : {} , {} ", e.getErrorCode(), e);
            addActionError(Integer.toString(e.getErrorCode()));
            caughtException = true;
        }

        if (caughtException || (warnings != null && warnings.isEmpty())) {
            populate();
        }
        return "success";
    }

    public String approveIgnoringWorning() {
        //todo has to be checked with the backend
        logger.debug("inside approveIgnoringWorning bdId {} received ignoreWarning is {}  ", bdId, ignoreWarning);
        initPermission();
        if (ignoreWarning) {
            BirthDeclaration bd = service.getById(bdId, user);
            try {
                service.approveBirthDeclaration(bd, true, user);
            } catch (CRSRuntimeException e) {
                logger.error("inside approveIgnoringWorning : {} , {} ", e.getErrorCode(), e);
                addActionError(getText("brapproval.ignoreWarningApproval.error." + e.getErrorCode()));
            }
            populate();
        } else {
            birthDeclarationApproval();
        }
        return "success";
    }

    /**
     * @return String which desides the next page
     */
    public String approveAllSelectedBirthDeclaration() {
        //todo warning handling has to be checked when rejectin some records from the requested list
        initPermission();
        if (index != null) {
            logger.debug("inside approveAllSelectedBirthDeclaration : {} records are requested to approve " + index.length);
            try {
                warnings = service.approveBirthDeclarationIdList(index, user);
            }
            catch (CRSRuntimeException e) {
                //todo identify the number of faild entities to handle the record counter
                addActionError(getText("brapproval.approval.error." + e.getErrorCode()));
                logger.error("inside approveAllSelectedBirthDeclaration : {} , {} ", e.getErrorCode(), e);
            }
        }
        birthDeclarationPendingList = service.getDeclarationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division),
            getPageNo(), appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        paginationHandler(birthDeclarationPendingList.size());
        populate();
        return "success";
    }

    /**
     * reject BirthDeclaration based on received bdId
     *
     * @return String
     */
    public String rejectBirthDeclaration() {
        //todo has to be checked after getting the backend support
        logger.debug("inside rejectBirthDeclaration : bdId {} received", bdId);
        initPermission();
        BirthDeclaration bd = service.getById(bdId, user);
        birthDeclarationPendingList = service.getDeclarationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division),
            getPageNo(), appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        paginationHandler(birthDeclarationPendingList.size());
        populate();
        return "success";
    }

    /**
     * request to delete the selected Birth Approval record from the database
     *
     * @return String
     */
    public String deleteBirthDeclaration() {
        logger.debug("inside deleteApprovalPending : bdId {} received ", bdId);
        initPermission();
        BirthDeclaration bd = service.getById(bdId, user);
        try {
            service.deleteNormalBirthDeclaration(bd, false, user);
        }
        catch (CRSRuntimeException e) {
            addActionError(getText("brapproval.delete.error." + e.getErrorCode()));
            logger.error("inside deleteBirthDeclaration: {} , {} ", e.getErrorCode(), e);
        }
        birthDeclarationPendingList = service.getDeclarationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division),
            getPageNo(), appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        paginationHandler(birthDeclarationPendingList.size());
        populate();
        return "success";
    }

    /**
     * Populate master data to the UIs
     */
    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("inside populate : {} observed.", language);
        districtList = districtDAO.getDistrictNames(language, user);
        setInitialDistrict();
        divisionList = bdDivisionDAO.getBDDivisionNames(district, language, user);
        logger.debug("inside populate : districts , countriees and races populated.");
    }

    /**
     * handles pagination of BirthDeclaration pending approval data
     *
     * @return String
     */
    public String nextPage() {
        initPermission();
        logger.debug("inside nextPage : pageNo {} received", getPageNo());
        setPageNo(getPageNo() + 1);
        logger.debug("inside nextPage : district {} division {} observed", district, division);
        /**
         * gets the user selected district to get the records
         * variable nextFlag is used to handle the pagination link
         * in the jsp page
         */
        birthDeclarationPendingList = service.getDeclarationApprovalPending(
            bdDivisionDAO.getBDDivisionByPK(division), getPageNo(), appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        paginationHandler(birthDeclarationPendingList.size());
        /**
         * when page refreshes BirthDeclaration approval pending list
         * does not get any value pageNo is not incremented
         */
        if (birthDeclarationPendingList.size() == 0) {
            setPageNo(getPageNo() - 1);
        }
        setPreviousFlag(true);
        setRecordCounter(getRecordCounter() + appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        populate();
        return "success";
    }

    /**
     * handles pagination of BirthDeclaration approval pending data
     *
     * @return String
     */
    public String previousPage() {
        initPermission();
        if (logger.isDebugEnabled()) {
            logger.debug("inside filter : district {} division {} observed ", district, division +
                " Page number " + getPageNo());
        }
        /**
         * UI related handle whether to display the
         * next link and previous link
         */
        if (previousFlag && getPageNo() == 2) {
            /**
             * request is comming backword(calls previous
             * to load the very first page
             */
            setPreviousFlag(false);
        } else if (getPageNo() == 1) {
            /**
             * if request is from page one
             * in the next page previous link
             * should be displayed
             */
            setPreviousFlag(false);
        } else {
            setPreviousFlag(true);
        }
        setNextFlag(true);
        if (getPageNo() > 1) {
            setPageNo(getPageNo() - 1);
        }
        birthDeclarationPendingList = service.getConfirmationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division),
            getPageNo(), appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        if (recordCounter > 0) {
            setRecordCounter(getRecordCounter() - appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        }
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
            setNextFlag(true);
        } else {
            setNextFlag(false);
        }
    }

    /**
     * initial district is set to the
     * first district of the allowed
     * district list of a perticular
     * user
     */
    public void setInitialDistrict() {
        if (!districtList.isEmpty()) {
            district = districtList.keySet().iterator().next();
        }
    }

    public void setBirthDeclarationPendingList(List<BirthDeclaration> birthDeclarationPendingList) {
        this.birthDeclarationPendingList = birthDeclarationPendingList;
    }

    public List<BirthDeclaration> getBirthDeclarationPendingList() {
        return birthDeclarationPendingList;
    }

    public void setSession(Map map) {
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
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

    public boolean isIgnoreWarning() {
        return ignoreWarning;
    }

    public void setIgnoreWarning(boolean ignoreWarning) {
        this.ignoreWarning = ignoreWarning;
    }

    public boolean isNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(boolean nextFlag) {
        this.nextFlag = nextFlag;
    }

    public boolean isPreviousFlag() {
        return previousFlag;
    }

    public void setPreviousFlag(boolean previousFlag) {
        this.previousFlag = previousFlag;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public boolean isAllowEditBDF() {
        return allowEditBDF;
    }

    public void setAllowEditBDF(boolean allowEditBDF) {
        this.allowEditBDF = allowEditBDF;
    }

    public boolean isAllowApproveBDF() {
        return allowApproveBDF;
    }

    public void setAllowApproveBDF(boolean allowApproveBDF) {
        this.allowApproveBDF = allowApproveBDF;
    }

    public List<UserWarning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<UserWarning> warnings) {
        this.warnings = warnings;
    }

    public int getRecordCounter() {
        return recordCounter;
    }

    public void setRecordCounter(int recordCounter) {
        this.recordCounter = recordCounter;
    }
}