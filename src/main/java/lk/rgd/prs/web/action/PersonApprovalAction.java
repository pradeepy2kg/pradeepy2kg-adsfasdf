package lk.rgd.prs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.dao.LocationDAO;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.WebUtils;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.CommonUtil;
import lk.rgd.prs.PRSRuntimeException;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Chathuranga Withana
 */
public class PersonApprovalAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(PersonApprovalAction.class);
    private static final String PRS_APPROVAL_ROWS_PER_PAGE = "prs.prs_approval_rows_per_page";
    private static final String WARNING = "warning";
    private static final String PAGE_LOAD = "pageLoad";

    // services and DAOs
    private final PopulationRegistry service;
    private final LocationDAO locationDAO;
    private final AppParametersDAO appParametersDAO;
    private final CommonUtil commonUtil;

    private Map session;
    private User user;
    private Person person;

    private Map<Integer, String> locationList;
    private List<Person> searchResultList;
    private List<UserWarning> warnings;

    private boolean direct;         // used to identify actions performed directly, without using the list
    private boolean allowApprove;
    private boolean allowPrint;
    private boolean pageLoad;
    private boolean delete;
    private boolean ignoreWarning;

    private int locationId;
    private int pageNo;
    private int noOfRows;
    private int printStart;

    private long personUKey;
    private Long searchPin;
    private Long searchTempPin;

    private String language;
    private String searchNic;
    private String comments;

    public PersonApprovalAction(PopulationRegistry service, LocationDAO locationDAO, AppParametersDAO appParametersDAO,
        CommonUtil commonUtil) {
        this.service = service;
        this.locationDAO = locationDAO;
        this.appParametersDAO = appParametersDAO;
        this.commonUtil = commonUtil;
    }

    /**
     * This method used to load person pending approval list by specified location id (only for page number 1)
     */
    public String initResultsPage() {
        logger.debug("Loading approval pending person list");
        populateLocations();
        pageNo += 1;
        getSearchResultsPage();
        displayResultSize();

        clearSearchFieldValues();
        return SUCCESS;
    }

    /**
     * This method used to approve person pending approval from direct mode or list and ignore warnings
     */
    public String approveSelectedPerson() {
        logger.debug("Approving Person with personUKey : {} by ignoring warnings : {}", personUKey, ignoreWarning);

        try {
            warnings = service.approvePerson(personUKey, ignoreWarning, user);
        } catch (PRSRuntimeException e) {
            switch (e.getErrorCode()) {
                case ErrorCodes.INVALID_DATA:
                    addActionError(getText("message.approveData.invalid"));
                    loadPRSManagementPage();
                    return SUCCESS;
                case ErrorCodes.INVALID_STATE_FOR_PRS_APPROVAL:
                case ErrorCodes.PERMISSION_DENIED:
                    addActionError(getText("message.noPermission"));
                    break;
            }
        }

        if (warnings.isEmpty()) {
            if (direct) {
                initPermissions();
            } else {
                loadPRSManagementPage();
            }
            addActionMessage(getText("message.approval.success"));
            logger.debug("Approving person with personUKey : {} approved successfully", personUKey);
            return SUCCESS;
        } else {
            logger.debug("Approving person with personUKey : {} gives warning list with : {} items", personUKey,
                warnings.size());
            return WARNING;
        }
    }

    /**
     * This method is used to reject person registrations
     */
    public String rejectSelectedPerson() {
        if (pageLoad) {
            logger.debug("Loading Person with personUKey : {} for rejection", personUKey);
            person = service.getByUKey(personUKey, user);
            return PAGE_LOAD;
        } else {
            logger.debug("Rejecting Person with personUKey : {}", personUKey);
            try {
                service.rejectPersonBeforeApproval(personUKey, comments, user);
                addActionMessage(getText("message.reject.success"));

            } catch (PRSRuntimeException e) {
                switch (e.getErrorCode()) {
                    case ErrorCodes.COMMENT_REQUIRED_PRS_REJECT:
                        addActionError(getText("enter.comment.label"));
                        break;
                    case ErrorCodes.PRS_REJECT_RECORD_DENIED:
                    case ErrorCodes.PERMISSION_DENIED:
                        addActionError(getText("message.noPermission"));
                        break;
                }
            }

            loadPRSManagementPage();
            return SUCCESS;
        }
    }

    /**
     * This method is used to delete person registrations
     */
    public String deleteSelectedPerson() {
        if (pageLoad) {
            logger.debug("Loading Person with personUKey : {} for deletion", personUKey);
            person = service.getByUKey(personUKey, user);
            return PAGE_LOAD;
        } else {
            logger.debug("Deleting Person with personUKey : {}", personUKey);
            try {
                service.deletePersonBeforeApproval(personUKey, comments, user);
                addActionMessage(getText("message.delete.success"));

            } catch (PRSRuntimeException e) {
                switch (e.getErrorCode()) {
                    case ErrorCodes.COMMENT_REQUIRED_PRS_DELETE:
                        addActionError(getText("enter.comment.label"));
                        break;
                    case ErrorCodes.PRS_DELETE_RECORD_DENIED:
                    case ErrorCodes.PERMISSION_DENIED:
                        addActionError(getText("message.noPermission"));
                        break;
                }
            }

            loadPRSManagementPage();
            return SUCCESS;
        }
    }

    /**
     * This method is used to mark PRS certificate as printed for the first time from direct and search list page
     */
    public String markPRSCertificateAsPrinted() {
        logger.debug("Mark PRS certificate as printed for personUKey : {} , in direct mode : {}", personUKey, direct);
        try {
            service.markPRSCertificateAsPrinted(personUKey, user);
            logger.debug("PRS certificate with personUKey : {} marked as printed successfully", personUKey);
            addActionMessage(getText("message.certPrint.success"));

            if (direct) {
                initPermissions();
            } else {
                loadPRSManagementPage();
            }

        } catch (RGDRuntimeException e) {
            switch (e.getErrorCode()) {
                case ErrorCodes.PERMISSION_DENIED:
                case ErrorCodes.PRS_CERT_MARK_AS_PRINTED_DENIED:
                case ErrorCodes.ILLEGAL_STATE:
                    addActionError(getText("message.noPermission"));
                    break;
            }
        }
        return SUCCESS;
    }

    /**
     * This method is used to return back from PRS certificate to search list or details page
     */
    public String backFromPRSCertificate() {
        logger.debug("Return back from PRS certificate page in direct mode : {}", direct);
        if (direct) {
            // nothing to do
        } else {
            loadPRSManagementPage();
        }
        return SUCCESS;
    }

    /**
     * This method is used for pagination, to move backward in PRS search results list page
     */
    public String previousResultsPage() {
        logger.debug("Previous page of PRS search result list page loaded");
        noOfRows = appParametersDAO.getIntParameter(PRS_APPROVAL_ROWS_PER_PAGE);
        pageNo = printStart / noOfRows;

        filterAndLoadResults();

        printStart -= noOfRows;
        populateLocations();
        clearSearchFieldValues();
        displayResultSize();
        return SUCCESS;
    }

    /**
     * This method is used for pagination, to move forward in PRS search results list page
     */
    public String nextResultsPage() {
        logger.debug("Next page of PRS search result list page loaded");
        noOfRows = appParametersDAO.getIntParameter(PRS_APPROVAL_ROWS_PER_PAGE);
        pageNo = ((printStart + noOfRows) / noOfRows) + 1;

        filterAndLoadResults();

        printStart += noOfRows;
        populateLocations();
        clearSearchFieldValues();
        displayResultSize();
        return SUCCESS;
    }

    /**
     * This method is used to load PRS management list page
     */
    private void loadPRSManagementPage() {
        pageNo = (pageNo == 0) ? 1 : pageNo;
        populateLocations();
        getSearchResultsPage();
    }

    /**
     * This method used to load approval pending person list for specified locationId, pageNo and noOfRows
     */
    private void getSearchResultsPage() {
        noOfRows = appParametersDAO.getIntParameter(PRS_APPROVAL_ROWS_PER_PAGE);
        filterAndLoadResults();
    }

    /**
     * This method is used to load PRS result list according to the specified searching criteria
     */
    private void filterAndLoadResults() {
        if (locationId != 0) {
            Location selectedLocation = locationDAO.getLocation(locationId);
            if (searchPin == null && searchNic == null && searchTempPin == null) {
                // Search by Location
                searchResultList = service.getPersonsByLocation(selectedLocation, pageNo, noOfRows, user);
            } else {
                if (searchPin != null) {
                    // Search by Location and PIN
                    searchResultList = service.getPersonByLocationAndPIN(selectedLocation, searchPin, user);
                } else if (!isEmpty(searchNic)) {
                    // Search by Location and NIC
                    searchResultList = service.getPersonsByLocationAndNIC(selectedLocation, searchNic, user);
                } else if (searchTempPin != null) {
                    // Search by Location and Temporary PIN
                    searchResultList = service.getPersonByLocationAndTemporaryPIN(selectedLocation, searchTempPin, user);
                } else {
                    Collections.emptyList();
                    logger.warn("PRS searching cannot occur this search scenario");
                }
            }
        } else {
            searchResultList = Collections.emptyList();
            logger.warn("Searching location cannot be 0, LocationId mandatory for PRS searching");
        }
    }

    /**
     * This method is used to show action message if the search result list is empty
     */
    private void displayResultSize() {
        if (searchResultList.size() == 0) {
            addActionMessage(getText("noItemMsg.label"));
        }
        logger.debug("Loaded approval pending person list with size : {} for LocationId : {} ",
            searchResultList.size(), locationId);
    }

    /**
     * This method used to populate users active user location list
     */
    private void populateLocations() {
        locationList = commonUtil.populateActiveUserLocations(user, language);
        // use primary location for locationId
        if (locationId == 0) {
            locationId = user.getPrimaryLocation().getLocationUKey();
        }
    }

    /**
     * This method used to show/hide specific links in JSPs according to user permissions
     */
    private void initPermissions() {
        allowPrint = user.isAuthorized(Permission.PRS_PRINT_CERT);
        if (logger.isDebugEnabled()) {
            logger.debug("User : " + user.getUserId() + " is allowed to print PRS certificate : " + allowPrint);
        }
    }

    /**
     * This method used to set searching fields back to initial state
     */
    private void clearSearchFieldValues() {
        searchPin = null;
        searchNic = null;
        searchTempPin = null;
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
        logger.debug("setting User : {} and Language : {}", user.getUserName(), language);
    }

    public Map<Integer, String> getLocationList() {
        return locationList;
    }

    public void setLocationList(Map<Integer, String> locationList) {
        this.locationList = locationList;
    }

    public List<Person> getSearchResultList() {
        return searchResultList;
    }

    public void setSearchResultList(List<Person> searchResultList) {
        this.searchResultList = searchResultList;
    }

    public List<UserWarning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<UserWarning> warnings) {
        this.warnings = warnings;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public boolean isAllowApprove() {
        return allowApprove;
    }

    public void setAllowApprove(boolean allowApprove) {
        this.allowApprove = allowApprove;
    }

    public boolean isAllowPrint() {
        return allowPrint;
    }

    public void setAllowPrint(boolean allowPrint) {
        this.allowPrint = allowPrint;
    }

    public boolean isPageLoad() {
        return pageLoad;
    }

    public void setPageLoad(boolean pageLoad) {
        this.pageLoad = pageLoad;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isIgnoreWarning() {
        return ignoreWarning;
    }

    public void setIgnoreWarning(boolean ignoreWarning) {
        this.ignoreWarning = ignoreWarning;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
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

    public int getPrintStart() {
        return printStart;
    }

    public void setPrintStart(int printStart) {
        this.printStart = printStart;
    }

    public long getPersonUKey() {
        return personUKey;
    }

    public void setPersonUKey(long personUKey) {
        this.personUKey = personUKey;
    }

    public Long getSearchPin() {
        return searchPin;
    }

    public void setSearchPin(Long searchPin) {
        this.searchPin = searchPin;
    }

    public Long getSearchTempPin() {
        return searchTempPin;
    }

    public void setSearchTempPin(Long searchTempPin) {
        this.searchTempPin = searchTempPin;
    }

    public String getSearchNic() {
        return searchNic;
    }

    public void setSearchNic(String searchNic) {
        this.searchNic = WebUtils.filterBlanks(searchNic);
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}