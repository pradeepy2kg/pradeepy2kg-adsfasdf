package lk.rgd.crs.web.action.marriages;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.WebUtils;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.MarriageNotice;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.CommonUtil;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Chathuranga Withana
 * @author amith jayasekara
 */
public class MarriageRegisterSearchAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(MarriageRegisterSearchAction.class);
    private static final String MR_APPROVAL_ROWS_PER_PAGE = "crs.mr_approval_rows_per_page";

    // services and DAOs
    private final MarriageRegistrationService service;
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final MRDivisionDAO mrDivisionDAO;
    private final AppParametersDAO appParametersDAO;
    private final CommonUtil commonUtil;

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

    public MarriageRegisterSearchAction(MarriageRegistrationService service, DistrictDAO districtDAO,
        DSDivisionDAO dsDivisionDAO, MRDivisionDAO mrDivisionDAO, AppParametersDAO appParametersDAO,
        CommonUtil commonUtil) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.mrDivisionDAO = mrDivisionDAO;
        this.appParametersDAO = appParametersDAO;
        this.commonUtil = commonUtil;

    }

    /**
     * loading search page for marriage notice search
     */
    public String marriageNoticeSearchInit() {
        logger.debug("Marriage notice search page loaded");
        populateBasicLists();

        pageNo += 1;
        noOfRows = appParametersDAO.getIntParameter(MR_APPROVAL_ROWS_PER_PAGE);

        if (noticeSerialNo != null) {
            if (mrDivisionId != 0) {
                searchList = WebUtils.populateNoticeList(service.getMarriageNoticePendingApprovalByMRDivisionAndSerial(
                    mrDivisionDAO.getMRDivisionByPK(mrDivisionId), noticeSerialNo, user));
            }
        } else {
            if (isEmpty(pinOrNic) && noticeSerialNo == null) {
                if (mrDivisionId == 0) {
                    searchList = WebUtils.populateNoticeList(service.getMarriageNoticePendingApprovalByDSDivision(
                        dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, true, user));
                } else {
                    searchList = WebUtils.populateNoticeList(service.getMarriageNoticePendingApprovalByMRDivision(
                        mrDivisionDAO.getMRDivisionByPK(mrDivisionId), pageNo, noOfRows, true, user));
                }
            } else {
                searchList = WebUtils.populateNoticeList(
                    service.getMarriageNoticePendingApprovalByPINorNIC(pinOrNic, true, user));
            }
        }
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
     * loading searching page for marriage register searching
     * by default this method loads marriage register records for first element if the ds  division list
     */
    //TODO: to be removed
    public String marriageRegisterSearch() {
        //todo complete **amith
        logger.debug("loading marriage register search page");
        populateBasicLists();
        pageNo += 1;
        //number of results rows that fetch at a time
        noOfRows = appParametersDAO.getIntParameter(MR_APPROVAL_ROWS_PER_PAGE);
        if (noticeSerialNo != null) {
            //search by serial number and MR division u Key
            logger.debug("attempt to search marriage register list by serial number :{} and mr division idUKey : {}",
                noticeSerialNo, mrDivisionId);
            if (mrDivisionId != 0) {
                marriageRegisterSearchList = service.getMarriageRegisterPendingApprovalBySerialAndMRDivision
                    (noticeSerialNo, mrDivisionDAO.getMRDivisionByPK(mrDivisionId), pageNo, noOfRows, true, user);
            }
        } else {
            if (isEmpty(pinOrNic) && noticeSerialNo == null) {
                if (mrDivisionId == 0) {
                    //default search option use when page is loaded(search all the marriage records in DS division)
                    marriageRegisterSearchList = service.getMarriageRegisterPendingApprovalByDSDivision
                        (dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, true, user);
                } else {
                    searchList = WebUtils.populateNoticeList(service.getMarriageNoticePendingApprovalByMRDivision(
                        mrDivisionDAO.getMRDivisionByPK(mrDivisionId), pageNo, noOfRows, true, user));
                }
            } else {
                searchList = WebUtils.populateNoticeList(
                    service.getMarriageNoticePendingApprovalByPINorNIC(pinOrNic, true, user));
            }
        }
        if (marriageRegisterSearchList.size() == 0) {
            addActionMessage(getText("noItemMsg.label"));
        }
        logger.debug("Marriage notice search list loaded with size : {}", marriageRegisterSearchList.size());

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
}
