package lk.rgd.prs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.dao.LocationDAO;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.WebUtils;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.CommonUtil;
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

    // services and DAOs
    private final PopulationRegistry service;
    private final LocationDAO locationDAO;
    private final AppParametersDAO appParametersDAO;
    private final CommonUtil commonUtil;

    private Map session;
    private User user;

    private Map<Integer, String> locationList;
    private List<Person> searchResultList;
    private List<UserWarning> warnings;

    private int locationId;
    private int pageNo;
    private int noOfRows;
    private int printStart;

    private long personUKey;
    private Long searchPin;
    private Long searchTempPin;

    private String language;
    private String searchNic;

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
     * This method used to approve person pending approval
     */
    public String approveSelectedPerson() {
        logger.debug("Approving Person with PersonUKey : {}", personUKey);
        warnings = service.approvePerson(personUKey, false, user);

        if (warnings.isEmpty()) {
            populateLocations();
            getSearchResultsPage();
            addActionMessage(getText("message.approval.success"));
            return SUCCESS;
        } else {
            return WARNING;
        }
    }

    /**
     * This method used to approve persons pending approval by ignoring warnings
     */
    public String approveIgnoreWarnings() {
        // TODO
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
}