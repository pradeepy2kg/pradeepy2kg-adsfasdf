package lk.rgd.crs.web.action.marriages;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.WebUtils;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.MarriageNotice;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.CommonUtil;
import lk.rgd.AppConstants;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Chathuranga Withana
 * @author amith jayasekara
 */
public class MarriageRegisterSearchAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(MarriageRegisterSearchAction.class);
    private static final String MR_APPROVAL_ROWS_PER_PAGE = "crs.mr_approval_rows_per_page";

    // services and DAOs
    private final MarriageRegistrationService marriageRegistrationService;
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final MRDivisionDAO mrDivisionDAO;
    private final AppParametersDAO appParametersDAO;
    private final CommonUtil commonUtil;

    private MarriageRegister marriage;

    private User user;

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> mrDivisionList;
    private List<MarriageNotice> searchList;
    private List<MarriageRegister> marriageRegisterSearchList;

    private Date searchStartDate;
    private Date searchEndDate;

    private String language;
    private String pinOrNic;
    private Long noticeSerialNo;

    private int districtId;
    private int dsDivisionId;
    private int mrDivisionId;
    private int pageNo;
    private int noOfRows;
    private long idUKey;

    private String comment;

    private MarriageNotice.Type noticeType;

    public MarriageRegisterSearchAction(MarriageRegistrationService marriageRegistrationService, DistrictDAO districtDAO,
        DSDivisionDAO dsDivisionDAO, MRDivisionDAO mrDivisionDAO, AppParametersDAO appParametersDAO,
        CommonUtil commonUtil) {
        this.marriageRegistrationService = marriageRegistrationService;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.mrDivisionDAO = mrDivisionDAO;
        this.appParametersDAO = appParametersDAO;
        this.commonUtil = commonUtil;

        districtList = new HashMap<Integer, String>();
        dsDivisionList = new HashMap<Integer, String>();
        mrDivisionList = new HashMap<Integer, String>();

    }

    /**
     * loading search page for marriage notice search
     */
    public String marriageNoticeSearchInit() {
        logger.debug("Marriage notice search page loaded");
        populateBasicLists();
        getApprovalPendingNotices();

        if (searchList.size() == 0) {
            addActionMessage(getText("noItemMsg.label"));
        }
        logger.debug("Marriage notice search list loaded with size : {}", searchList.size());

        // by doing following previously user entered values will be removed in jsp page
        noticeSerialNo = null;
        pinOrNic = null;

        return SUCCESS;
    }

    /**
     * loading search page for marriage register
     */
    public String marriageRegisterSearchInit() {
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId, dsDivisionId, mrDivisionId, "Marriage", user, language);
        return SUCCESS;
    }

    /**
     * loading search result page for marriage register
     */
    public String marriageRegisterSearchResult() {
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId, dsDivisionId, mrDivisionId, "Marriage", user, language);
        pageNo += 1;
        noOfRows = appParametersDAO.getIntParameter(MR_APPROVAL_ROWS_PER_PAGE);
        if (noticeSerialNo != null) {
            //TODO: search by serial number, clear tabs
        } else {
            if (mrDivisionId == 0) {
                //default search option use when page is loaded(search all the marriage records in DS division)
                marriageRegisterSearchList = marriageRegistrationService.getMarriageRegistersByDSDivision
                    (dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, true, user);
            } else {
                marriageRegisterSearchList = marriageRegistrationService.getMarriageRegisterByMRDivision
                    (mrDivisionDAO.getMRDivisionByPK(mrDivisionId), pageNo, noOfRows, true, user);
            }
        }
        if (marriageRegisterSearchList.size() == 0) {
            addActionMessage(getText("error.marriageregister.norecords"));
        }
        return SUCCESS;
    }

    public String approveMarriageRegister() {
        marriageRegistrationService.approveMarriageRegister(idUKey, user);
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId, dsDivisionId,
            mrDivisionId, AppConstants.MARRIAGE, user, language);
        addActionMessage(getText("message.marriageregister.approved"));
        return marriageRegisterSearchInit();
    }

    public String rejectMarriageRegister() {
        marriageRegistrationService.rejectMarriageRegister(idUKey, "rejected", user);
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId, dsDivisionId,
            mrDivisionId, AppConstants.MARRIAGE, user, language);
        addActionMessage(getText("message.marriageregister.rejected"));
        return marriageRegisterSearchInit();
    }

    /**
     * action method use to approve a notice this could be male notice or female notice or a single notice type(BOTH)
     */
    public String approveMarriageNotice() {
        logger.debug("approving marriage notice idUKey : {} and notice type : {}", idUKey, noticeType);
        try {
            marriageRegistrationService.approveMarriageNotice(idUKey, noticeType, user);
            addActionMessage(getText("message.approve.success", new String[]{Long.toString(idUKey), noticeType.toString()}));
            logger.debug("successfully approved marriage notice idUKey : {} and notice type :{ }", idUKey, noticeType);
        } catch (CRSRuntimeException e) {
            //error happens when approving marriage notice
            addActionError(getText("error.approval.failed", new String[]{Long.toString(idUKey), noticeType.toString()}));
            commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId,
                dsDivisionId, mrDivisionId, "Marriage", user, language);
            getApprovalPendingNotices();
            return ERROR;
        }
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId, dsDivisionId,
            mrDivisionId, "Marriage", user, language);
        getApprovalPendingNotices();
        addActionMessage(getText("message.approve.successfully"));
        logger.debug("successfully approved :idUKey : {}", idUKey);
        return SUCCESS;
    }


    /**
     * deleting a marriage notice
     * notes:
     * when removing a notice(have 2 notices) it just updating the data row
     * if there is only one notice (BOTH) delete the row
     */
    public String deleteMarriageNotice() {
        logger.debug("attempt to delete marriage notice : idUKey {} : notice type : {}", idUKey, noticeType);
        try {
            marriageRegistrationService.deleteMarriageNotice(idUKey, noticeType, user);
        }
        catch (CRSRuntimeException e) {
            commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId,
                dsDivisionId, mrDivisionId, "Marriage", user, language);
            getApprovalPendingNotices();
            addActionError(getText("error.delete.notice"));
            return ERROR;
        }
        addActionMessage(getText("massage.delete.successfully"));
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId,
            dsDivisionId, mrDivisionId, "Marriage", user, language);
        getApprovalPendingNotices();
        return SUCCESS;
    }

    public String rejectInit() {
        logger.debug("loading commenting page for rejecting marriage notice");
        //do nothing just load get comment page for rejecting marriage notice
        marriage = marriageRegistrationService.getByIdUKey(idUKey, user);
        return SUCCESS;
    }

    public String rejectMarriageNotice() {
        logger.debug("attempt to reject marriage notice : idUKey : {} :notice type : {}", idUKey, noticeType);
        try {
            marriageRegistrationService.rejectMarriageNotice(idUKey, noticeType, comment, user);
        }
        catch (CRSRuntimeException e) {
            switch (e.getErrorCode()) {
                case ErrorCodes.UNABLE_TO_REJECT_FEMALE_NOTICE:
                    addActionError(getText("error.unable.to.reject.notice", new String[]{"label.female", idUKey + ""}));
                    break;
                case ErrorCodes.UNABLE_TO_REJECT_MALE_NOTICE:
                    addActionError(getText("error.unable.to.reject.notice", new String[]{"label.male", idUKey + ""}));
                    break;
                case ErrorCodes.INVALID_NOTICE_STATE_FOR_REJECT:
                    addActionError(getText("error.unable.to.reject.notice.invalid.state", new String[]{idUKey + ""}));
                    break;
            }
            commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId,
                dsDivisionId, mrDivisionId, "Marriage", user, language);
            getApprovalPendingNotices();
            return ERROR;
        }
        //todo check table load after success or error  amith
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId,
            dsDivisionId, mrDivisionId, "Marriage", user, language);
        getApprovalPendingNotices();
        return SUCCESS;
    }

    /**
     * printing license to marriage
     */
    public String licenseToMarriagePrintInit() {
        logger.debug("attempt to print license to marriage for marriage notice :idUKey : {} and notice type : {}",
            idUKey, noticeType);
        //todo load notice
        return SUCCESS;
    }

    /**
     * This method used to load approval pending Marriage Notices list
     */
    private void getApprovalPendingNotices() {
        pageNo += 1;
        noOfRows = appParametersDAO.getIntParameter(MR_APPROVAL_ROWS_PER_PAGE);

        if (noticeSerialNo != null) {
            if (mrDivisionId != 0) {
                searchList = WebUtils.populateNoticeList(marriageRegistrationService.getMarriageNoticePendingApprovalByMRDivisionAndSerial(
                    mrDivisionDAO.getMRDivisionByPK(mrDivisionId), noticeSerialNo, true, user));
            } else {
                searchList = Collections.emptyList();
            }
        } else {
            if (isEmpty(pinOrNic) && noticeSerialNo == null) {
                if (mrDivisionId == 0) {
                    searchList = WebUtils.populateNoticeList(marriageRegistrationService.getMarriageNoticePendingApprovalByDSDivision(
                        dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, true, user));
                } else {
                    searchList = WebUtils.populateNoticeList(marriageRegistrationService.getMarriageNoticePendingApprovalByMRDivision(
                        mrDivisionDAO.getMRDivisionByPK(mrDivisionId), pageNo, noOfRows, true, user));
                }
            } else {
                searchList = WebUtils.populateNoticeList(
                    marriageRegistrationService.getMarriageNoticePendingApprovalByPINorNIC(pinOrNic, true, user));
            }
        }
    }

    /**
     * populate basic list such as districts, DSDivisions and MRDivision
     */
    private void populateBasicLists() {
        // TODO chathuranga change following
        districtList = districtDAO.getDistrictNames(language, user);
        if (districtId == 0) {
            if (!districtList.isEmpty()) {
                districtId = districtList.keySet().iterator().next();
                logger.debug("first allowed district in the list {} was set", districtId);
            }
        }
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(districtId, language, user);

        if (dsDivisionId == 0) {
            if (!dsDivisionList.isEmpty()) {
                dsDivisionId = dsDivisionList.keySet().iterator().next();
                logger.debug("first allowed DS Div in the list {} was set", dsDivisionId);
            }
        }

        mrDivisionList = mrDivisionDAO.getMRDivisionNames(dsDivisionId, language, user);
        // TODO
        /*if (mrDivisionId == 0) {
            mrDivisionId = mrDivisionList.keySet().iterator().next();
            logger.debug("first allowed BD Div in the list {} was set", mrDivisionId);
        }*/
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().length() != 10;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("setting User: {} and Language : {}", user.getUserName(), language);
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public void setDsDivisionList(Map<Integer, String> dsDivisionList) {
        this.dsDivisionList = dsDivisionList;
    }

    public Map<Integer, String> getMrDivisionList() {
        return mrDivisionList;
    }

    public void setMrDivisionList(Map<Integer, String> mrDivisionList) {
        this.mrDivisionList = mrDivisionList;
    }

    public List<MarriageNotice> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<MarriageNotice> searchList) {
        this.searchList = searchList;
    }

    public Date getSearchStartDate() {
        return searchStartDate;
    }

    public void setSearchStartDate(Date searchStartDate) {
        this.searchStartDate = searchStartDate;
    }

    public Date getSearchEndDate() {
        return searchEndDate;
    }

    public void setSearchEndDate(Date searchEndDate) {
        this.searchEndDate = searchEndDate;
    }

    public String getPinOrNic() {
        return pinOrNic;
    }

    public void setPinOrNic(String pinOrNic) {
        this.pinOrNic = WebUtils.filterBlanks(pinOrNic);
    }

    public Long getNoticeSerialNo() {
        return noticeSerialNo;
    }

    public void setNoticeSerialNo(Long noticeSerialNo) {
        this.noticeSerialNo = noticeSerialNo;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public int getMrDivisionId() {
        return mrDivisionId;
    }

    public void setMrDivisionId(int mrDivisionId) {
        this.mrDivisionId = mrDivisionId;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getNoOfRows() {
        return noOfRows;
    }

    public void setNoOfRows(int noOfRows) {
        this.noOfRows = noOfRows;
    }

    public List<MarriageRegister> getMarriageRegisterSearchList() {
        return marriageRegisterSearchList;
    }

    public void setMarriageRegisterSearchList(List<MarriageRegister> marriageRegisterSearchList) {
        this.marriageRegisterSearchList = marriageRegisterSearchList;
    }

    public MarriageNotice.Type getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(MarriageNotice.Type noticeType) {
        this.noticeType = noticeType;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public MarriageRegister getMarriage() {
        return marriage;
    }

    public void setMarriage(MarriageRegister marriage) {
        this.marriage = marriage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
