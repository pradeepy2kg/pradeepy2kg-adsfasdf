package lk.rgd.prs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.dao.LocationDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.CommonUtil;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private List<Person> approvalPendingList;
    private List<UserWarning> warnings;

    private int locationId;
    private int pageNo;
    private int noOfRows;
    private long personUKey;
    private String language;

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
    public String loadPersonApprovalPendingList() {
        logger.debug("Loading approval pending person list");
        populateLocations();
        getApprovalPendingPersons();

        logger.debug("Loaded approval pending person list with size : {} for LocationId : {} ",
            approvalPendingList.size(), locationId);
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
            getApprovalPendingList();
            final Person existing = service.getByUKey(personUKey, user);
            addActionMessage(getText("message.approval.success", new String[]{existing.getPin().toString()}));
            return SUCCESS;
        } else {
            return WARNING;
        }
    }

    /**
     * This method used to approve persons pending approval by ignoring warnings
     */
    public String approveIgnoreWarnings() {
        return SUCCESS;
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
     * This method used to load approval pending person list for specified locationId, pageNo and noOfRows
     */
    private void getApprovalPendingPersons() {
        pageNo += 1;
        noOfRows = appParametersDAO.getIntParameter(PRS_APPROVAL_ROWS_PER_PAGE);
        approvalPendingList = service.getPRSPendingApprovalByLocation(locationDAO.getLocation(locationId), pageNo, noOfRows, user);
        if (approvalPendingList.size() == 0) {
            addActionMessage(getText("noitemMsg.label"));
        }
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

    public List<Person> getApprovalPendingList() {
        return approvalPendingList;
    }

    public void setApprovalPendingList(List<Person> approvalPendingList) {
        this.approvalPendingList = approvalPendingList;
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

    public long getPersonUKey() {
        return personUKey;
    }

    public void setPersonUKey(long personUKey) {
        this.personUKey = personUKey;
    }
}
