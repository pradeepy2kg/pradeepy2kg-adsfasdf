package lk.rgd.prs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.dao.LocationDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.WebConstants;
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

    private Map session;
    private User user;

    private Map<Integer, String> locationList;
    private List<Person> approvalPendingList;

    private String language;
    private int locationId;
    private int pageNo;
    private int noOfRows;

    public PersonApprovalAction(PopulationRegistry service, LocationDAO locationDAO, AppParametersDAO appParametersDAO) {
        this.service = service;
        this.locationDAO = locationDAO;
        this.appParametersDAO = appParametersDAO;
    }

    /**
     * This method used to load person pending approval list
     */
    public String loadPersonApprovalPendingList() {
        logger.debug("Loading approval pending person list");
        locationList = user.getActiveLocations(language);
        // TODO
        noOfRows = appParametersDAO.getIntParameter(PRS_APPROVAL_ROWS_PER_PAGE);
        if (locationId == 0) {
            logger.debug("Load approval pending list for users all available locations");
            // TODO
        } else {
            pageNo = 1;
            approvalPendingList = service.getPRSPendingApprovalByLocation(locationDAO.getLocation(locationId), pageNo, noOfRows, user);
        }
        logger.debug("Loaded approval pending person list");
        return SUCCESS;
    }

    /**
     * This method used to redirect to person edit process in PersonRegisterAction from person pending approval list
     * page
     */
    public String loadEditPage() {
        return SUCCESS;
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
