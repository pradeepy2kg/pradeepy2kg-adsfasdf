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
import lk.rgd.Permission;


/**
 * @author Indunil Moremada
 *         struts action class which is used for the BirthDeclaration approval purposes
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
    private List<BirthDeclaration> birthDeclarationPendingList;
    private long[] index;
    private User user;
    private long bdId;
    private boolean ignoreWarning;
    int pageNo;
    int initialDistrict;
    int initialDivision;

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
        populate();
        setInitialDistrict();
        initialDivision = -1;
        if (!divisionList.isEmpty()) {
            initialDivision = divisionList.keySet().iterator().next();
        }
        boolean allowApproveBDF = user.isAuthorized(Permission.APPROVE_BDF);
        boolean allowEditBDF = user.isAuthorized(Permission.EDIT_BDF);
        session.put("allowApproveBDF", allowApproveBDF);
        session.put("allowEditBDF", allowEditBDF);
        //todo set following after changing the backend
        session.put("allowReject", true);
        logger.debug("inside birthDeclarationPendingList: district {} division {} selected ", initialDistrict, initialDivision);
        session.put("selectedDistrict", initialDistrict);
        session.put("initialDivision", initialDivision);
        /**
         * initially pageNo is set to 1
         */
        pageNo = 1;
        birthDeclarationPendingList = service.getDeclarationApprovalPending(
            bdDivisionDAO.getBDDivisionByPK(initialDivision), pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        paginationHandler(birthDeclarationPendingList.size());
        session.put("previousFlag", 0);
        session.put("districtOrDivisionSelectedFlag", 0);
        session.put("BirthDeclarationApprovalPending", birthDeclarationPendingList);
        session.put("pageNo", pageNo);
        return "pageLoad";
    }

    /**
     * filters BirthDeclarations which are not
     * yet approved by user selected district
     * and division
     *
     * @return String
     */
    public String filter() {
        pageNo = 1;
        session.put("pageNo", pageNo);
        if (logger.isDebugEnabled()) {
            logger.debug("inside filter : district {} division {} observed ", district, division + " Page number " + pageNo);
        }
        session.put("selectedDistrict", district);
        session.put("initialDivision", division);
        session.put("selectedFlag", 1);
        birthDeclarationPendingList = service.getDeclarationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division),
            pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("BirthDeclarationApprovalPending", birthDeclarationPendingList);
        paginationHandler(birthDeclarationPendingList.size());
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
        logger.debug("inside approveBirthDeclaration : bdId {} observed ", bdId);
        BirthDeclaration bd = service.getById(bdId);
        List<UserWarning> warnings = null;
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        boolean caughtException = false;
        try {
            warnings = service.approveBirthDeclaration(bd, false, user);
        }
        catch (CRSRuntimeException e) {
            logger.error("inside approveBirthDeclaration : {} , {} ", e.getErrorCode(), e);
            addActionError(Integer.toString(e.getErrorCode()));
            caughtException = true;
        }
        if (caughtException || (warnings != null && warnings.isEmpty())) {
            setStatus();
            birthDeclarationPendingList = service.getDeclarationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division),
                pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
            session.put("BirthDeclarationApprovalPending", birthDeclarationPendingList);
            if (birthDeclarationPendingList.size() > 0) {
                paginationHandler(birthDeclarationPendingList.size());
            } else {
                pageNo--;
                session.put("pageNo", pageNo);
            }
            populate();
            return "success";
        } else {
            request.put("bdId", bdId);
            request.put("warnings", warnings);
            populate();
            request.put("bdId", bdId);//for testing
            return "approvalRejected";
        }
    }

    public String approveIgnoringWorning() {
        //todo has to be checked with the backend
        logger.debug("inside approveIgnoringWorning bdId {} received ignoreWarning is {}  ", bdId, ignoreWarning);
        if (ignoreWarning) {
            BirthDeclaration bd = service.getById(bdId);
            User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
            try {
                service.approveBirthDeclaration(bd, true, user);
            } catch (CRSRuntimeException e) {
                logger.error("inside approveIgnoringWorning : {} , {} ", e.getErrorCode(), e);
                addActionError(Integer.toString(e.getErrorCode()));
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
        //todo warning handling has to be checked
        logger.debug("inside approveAllSelectedBirthDeclaration : {} Declarations is/are requested to remove ", index.length);
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        List<UserWarning> warnings = null;
        try {
            warnings = service.approveBirthDeclarationIdList(index, user);
            if (warnings != null && !warnings.isEmpty()) {
                request.put("warnings", warnings);
            }
        }
        catch (CRSRuntimeException e) {
            addActionError(Integer.toString(e.getErrorCode()));
            logger.error("inside approveAllSelectedBirthDeclaration : {} , {} ", e.getErrorCode(), e);
        }
        setStatus();
        birthDeclarationPendingList = service.getDeclarationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division),
            pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("BirthDeclarationApprovalPending", birthDeclarationPendingList);
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
        BirthDeclaration bd = service.getById(bdId);
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        setStatus();
        birthDeclarationPendingList = service.getDeclarationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division),
            pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("BirthDeclarationApprovalPending", birthDeclarationPendingList);
        paginationHandler(birthDeclarationPendingList.size());
        populate();
        return "success";
    }

    /**
     * delete the selected Birth Approval record from
     * the database
     *
     * @return String
     */
    public String deleteBirthDeclaration() {
        logger.debug("inside deleteApprovalPending : bdId {} received ", bdId);
        BirthDeclaration bd = service.getById(bdId);
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        try {
            service.deleteNormalBirthDeclaration(bd, false, user);
        }
        catch (CRSRuntimeException e) {
            addActionError(Integer.toString(e.getErrorCode()));
            logger.error("inside deleteBirthDeclaration: {} , {} ", e.getErrorCode(), e);
        }
        setStatus();
        birthDeclarationPendingList = service.getDeclarationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division),
            pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("BirthDeclarationApprovalPending", birthDeclarationPendingList);
        paginationHandler(birthDeclarationPendingList.size());
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
        districtList = districtDAO.getDistrictNames(language, user);
        setInitialDistrict();
        divisionList = bdDivisionDAO.getBDDivisionNames(initialDistrict, language, user);
        logger.debug("inside populate : districts , countriees and races populated.");
    }

    /**
     * handles pagination of BirthDeclaration pending approval data
     * sets the session variable to load the next ten records
     *
     * @return String
     */
    public String nextPage() {
        setStatus();
        logger.debug("inside nextPage : pageNo {} received", pageNo);
        pageNo++;
        logger.debug("inside nextPage : district {} division {} observed", district, division);
        /**
         * gets the user selected district to get the records
         * variable nextFlag is used to handle the pagination link
         * in the jsp page
         */
        birthDeclarationPendingList = service.getDeclarationApprovalPending(
            bdDivisionDAO.getBDDivisionByPK(division), pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("BirthDeclarationApprovalPending", birthDeclarationPendingList);
        paginationHandler(birthDeclarationPendingList.size());
        /**
         * when page refreshes BirthDeclaration approval pending list
         * does not get any value pageNo is not incremented
         */
        if (birthDeclarationPendingList.size() == 0) {
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
     * handles pagination of BirthDeclaration approval pending data
     *
     * @return String
     */
    public String previousPage() {
        setStatus();
        if (logger.isDebugEnabled()) {
            logger.debug("inside filter : district {} division {} observed ", district, division +
                " Page number " + pageNo);
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
        birthDeclarationPendingList = service.getConfirmationApprovalPending(bdDivisionDAO.getBDDivisionByPK(division),
            pageNo, appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE));
        session.put("BirthDeclarationApprovalPending", birthDeclarationPendingList);
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

    /**
     * populate district, division and pageNo
     * required data will be loaded based on
     * these values
     */
    public void setStatus() {
        pageNo = (Integer) session.get("pageNo");
        district = (Integer) session.get("selectedDistrict");
        division = (Integer) session.get("initialDivision");
    }

    /**
     * initial district is set to the
     * first district of the allowed
     * district list of a perticular
     * user
     */
    public void setInitialDistrict() {
        if (!districtList.isEmpty()) {
            initialDistrict = districtList.keySet().iterator().next();
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