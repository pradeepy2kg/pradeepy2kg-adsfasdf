package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.Statistics;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.StatisticsManager;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.web.WebConstants;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

/**
 * This action is used for statistics
 *
 * @author Chathuranga Withana
 */
public class CRSStatisticsAction extends ActionSupport implements SessionAware {
    private static final Logger logger = LoggerFactory.getLogger(CRSStatisticsAction.class);

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> divisionList;

    private User user;          // logged in user
    private String role;          // logged in user role
    private Statistics statistics;

    private String language;    // preferred language of logged in user
    private String startDate;
    private String endDate;
    private int districtId;
    private int dsDivisionId;

    private final UserDAO userDAO;
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final StatisticsManager statService;

    public CRSStatisticsAction(StatisticsManager statService, UserDAO userDAO, DSDivisionDAO dsDivisionDAO,
        DistrictDAO districtDAO) {
        this.statService = statService;
        this.userDAO = userDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.districtDAO = districtDAO;
    }

    public String showStatistics() {

        logger.debug("Logged user's UserName : {} and Role : {}", user.getUserId(), user.getRole().getName());
        User statUser;
        // for logged in Admin users stat page similar to RG's stat page
        if (Role.ROLE_ADMIN.equalsIgnoreCase(user.getUserId())) {
            statUser = userDAO.getUsersByRole(Role.ROLE_RG).get(0);
        } else {
            statUser = user;
        }
        role = user.getRole().getRoleId();

        statistics = statService.getStatisticsForUser(statUser, null, null);
        if (statistics != null) {
            if (!statService.existsStatisticsForUser(user)) {
                statService.addStatistics(user, statistics);
            } else {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR, -(cal.get(Calendar.HOUR_OF_DAY) + 1));
                if (statistics.getCreatedTimestamp().before(cal.getTime())) {
                    statService.updateStatistics(user.getUserId(), statistics);
                }
            }
        } else {
            statistics = new Statistics();
        }

        populateDateRange();
        populateDivision();

        return SUCCESS;
    }

    private void populateDivision() {
        // load district list according to user
        districtList = districtDAO.getDistrictNames(language, user);
        if (districtList.size() > 0) {
            districtId = districtList.keySet().iterator().next();
        }

        divisionList = dsDivisionDAO.getDSDivisionNames(districtId, user.getPrefLanguage(), user);
        if (divisionList.size() > 0) {
            dsDivisionId = divisionList.keySet().iterator().next();
        }
        // TODO else parts need to be implemented. i.e. all district and dsDiviosn selected
    }

    private void populateDateRange() {
        if (startDate == null || endDate == null) {
            // set first day of current year
            Calendar cal1 = Calendar.getInstance();
            int currentYear = cal1.get(Calendar.YEAR);
            cal1.clear();
            cal1.set(currentYear, Calendar.JANUARY, 1, 0, 0, 0);
            startDate = DateTimeUtils.getISO8601FormattedString(cal1.getTime());

            // set end date to today midnight
            Calendar cal2 = Calendar.getInstance();
            cal2.set(Calendar.HOUR_OF_DAY, 23);
            cal2.set(Calendar.MINUTE, 59);
            cal2.set(Calendar.SECOND, 59);
            endDate = DateTimeUtils.getISO8601FormattedString(cal2.getTime());
        }
    }

    public void setSession(Map map) {
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
}
