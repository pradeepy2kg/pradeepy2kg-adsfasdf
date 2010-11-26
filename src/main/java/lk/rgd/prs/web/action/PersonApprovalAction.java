package lk.rgd.prs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.dao.LocationDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.DivisionUtil;
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

    // services and DAOs
    private PopulationRegistry service;
    private LocationDAO locationDAO;
    private AppParametersDAO appParametersDAO;
    private final DivisionUtil divisionUtil;

    private Map session;
    private User user;

    private Map<Integer, String> locationList;
    private List<Person> approvalPendingList;

    private String language;
    private int locationId;
    private int pageNo;
    private int noOfRows;

    public PersonApprovalAction(PopulationRegistry service, LocationDAO locationDAO, AppParametersDAO appParametersDAO,
        DivisionUtil divisionUtil) {
        this.service = service;
        this.locationDAO = locationDAO;
        this.appParametersDAO = appParametersDAO;
        this.divisionUtil = divisionUtil;
    }

    /**
     * This method used to load person pending approval list by specified location id (only for page number 1)
     */
    public String loadPersonApprovalPendingList() {
        logger.debug("Loading approval pending person list");
        locationList = divisionUtil.populateActiveUserLocations(user, language);
        // use primary location for locationId
        if (locationId == 0) {
            locationId = user.getPrimaryLocation().getLocationUKey();
        }
        pageNo = 1;
        getApprovalPendingPersons();

        logger.debug("Loaded approval pending person list with size : {} for LocationId : {} ",
            approvalPendingList.size(), locationId);
        return SUCCESS;
    }

    /**
     * This method used to load approval pending person list for specified locationId, pageNo and noOfRows
     */
    private void getApprovalPendingPersons() {
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
}
