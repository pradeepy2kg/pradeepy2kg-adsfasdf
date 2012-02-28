package lk.rgd.crs.web;

import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.domain.*;
import lk.rgd.common.api.service.StatisticsManager;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author shan
 * @author Chathuranga Withana
 */
public class JSONStatisticsLookupService extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(JSONStatisticsLookupService.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private StatisticsManager statisticsService;
    private UserDAO userDAO;
    HashMap<String, Object> optionLists;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context =
            WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        statisticsService = (StatisticsManager) context.getBean("statisticsManagerService");
        userDAO = (UserDAO) context.getBean("userDAOImpl");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private CommonStatistics birthDataTransfer(Statistics stat, CommonStatistics commonStat, String userRole) {
        commonStat.setApprovedItems(stat.getBirthsApprovedItems());
        commonStat.setArrearsPendingItems(stat.getBirthsArrearsPendingItems());
        commonStat.setLateSubmissions(stat.getBirthsLateSubmissions());
        commonStat.setNormalSubmissions(stat.getBirthsNormalSubmissions());
        commonStat.setRejectedItems(stat.getBirthsRejectedItems());
        commonStat.setUserRole(userRole);
        commonStat.setThisMonthPendingItems(stat.getBirthsThisMonthPendingItems());
        commonStat.setTotalPendingItems(stat.getBirthsTotalPendingItems());
        commonStat.setTotalSubmissions(stat.getBirthsTotalSubmissions());
        return commonStat;
    }

    private CommonStatistics deathDataTransfer(Statistics stat, CommonStatistics commonStat, String userRole) {
        commonStat.setApprovedItems(stat.getDeathsApprovedItems());
        commonStat.setArrearsPendingItems(stat.getDeathsArrearsPendingItems());
        commonStat.setLateSubmissions(stat.getDeathsLateSubmissions());
        commonStat.setNormalSubmissions(stat.getDeathsNormalSubmissions());
        commonStat.setRejectedItems(stat.getDeathsRejectedItems());
        commonStat.setUserRole(userRole);
        commonStat.setThisMonthPendingItems(stat.getDeathsThisMonthPendingItems());
        commonStat.setTotalPendingItems(stat.getDeathsTotalPendingItems());
        commonStat.setTotalSubmissions(stat.getDeathsTotalSubmissions());
        return commonStat;
    }

    private CommonStatistics marriageDataTransfer(Statistics stat, CommonStatistics commonStat, String userRole) {
        commonStat.setApprovedItems(stat.getMrgApprovedItems());
        commonStat.setArrearsPendingItems(stat.getMrgArrearsPendingItems());
        commonStat.setLateSubmissions(stat.getMrgLateSubmissions());
        commonStat.setNormalSubmissions(stat.getMrgNormalSubmissions());
        commonStat.setRejectedItems(stat.getMrgRejectedItems());
        commonStat.setUserRole(userRole);
        commonStat.setThisMonthPendingItems(stat.getMrgThisMonthPendingItems());
        commonStat.setTotalPendingItems(stat.getMrgTotalPendingItems());
        commonStat.setTotalSubmissions(stat.getMrgTotalSubmissions());
        return commonStat;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String districtId = request.getParameter("districtId");
        String dsDivisionId = request.getParameter("dsDivisionId");

        String startD = request.getParameter("startDate");
        String endD = request.getParameter("endDate");
        Date startDate = DateTimeUtils.getDateFromISO8601String(startD);
        Date endDate = DateTimeUtils.getDateFromISO8601String(endD);

        if (logger.isDebugEnabled()) {
            logger.debug("Request stats for districtId:{}, DSDivisionId:{} from {} to {} by {}",
                new Object[]{districtId, dsDivisionId, startD, endD, userId});
        }

        optionLists = new HashMap<String, Object>();

        User loggedUser = null;
        HttpSession session = request.getSession(false);
        if (session == null || session.isNew()) {
            logger.warn("User has not logged on to the system to invoke this service");
            return;
        } else {
            loggedUser = (User) session.getAttribute(WebConstants.SESSION_USER_BEAN);
            if (loggedUser == null) {
                logger.warn("Unexpected - User object is not present in the session");
                return;
            } else {
                if (!loggedUser.getUserId().equals(userId)) {
                    logger.warn("Logged in userId and request sent userId mismatch");
                    return;
                }
            }
        }

        User statUser = null;
        if (Role.ROLE_ADMIN.equals(loggedUser.getRole().getRoleId())) {
            // because currently Admin view statistics of RG view
            statUser = userDAO.getUsersByRole(Role.ROLE_RG).get(0);
        } else {
            statUser = loggedUser;
        }
        String userRole = statUser.getRole().getRoleId();
        Statistics statistics = statisticsService.getStatisticsForUser(statUser, startDate, endDate);

        CommonStatistics cs_b = new CommonStatistics();
        CommonStatistics cs_d = new CommonStatistics();
        CommonStatistics cs_m = new CommonStatistics();

        birthDataTransfer(statistics, cs_b, userRole);
        deathDataTransfer(statistics, cs_d, userRole);
        marriageDataTransfer(statistics, cs_m, userRole);

        populateBirthStatistics(cs_b);
        populateDeathStatistics(cs_d);
        populateMarriageStatistics(cs_m);


        // TODO refactor this part
        /*try {
            if (Role.ROLE_DR.equals(userRole)) {  // DR selects ADR
                cs_b = new CommonStatistics();
                cs_d = new CommonStatistics();
                cs_m = new CommonStatistics();
                cs = new CommonStatistics();    // default
                List<User> temp = new ArrayList<User>();
                User user = userDAO.getUserByPK(userName);

                if (user != null) {
                    List<User> allUsers = userDAO.getUsersByRole("DEO");
                    Set<DSDivision> set = user.getAssignedBDDSDivisions();

                    for (User selected : allUsers) {
                        for (DSDivision dsd : set) {
                            if (selected.getAssignedBDDSDivisions().contains(dsd)) {
                                temp.add(selected);
                            }
                        }
                    }
                    if (temp.size() > 0) {
                        // adding together all the birth statistics objects which belongs to 'temp' list members
                        for (User one : temp) {
                            cs_b.add(birthRegistrationService.getBirthStatisticsForUser(one.getUserId()));
                            cs_d.add(deathRegistrationService.getDeathStatisticsForUser(one.getUserId()));
                            cs_m.add(marriageRegistrationService.getMarriageStatisticsForUser(one.getUserId()));
                        }
                        // adding combined statistics object to response object
                        populateBirthStatistics(cs_b);
                        populateDeathStatistics(cs_d);
                        populateMarriageStatistics(cs_m);
                    }
                } else {
                    // if user is null, populate default(0) values
                    populateBirthStatistics(cs);
                }

            } else if (Role.ROLE_ADR.equals(userRole) || Role.ROLE_DEO.equals(userRole)) {  // ADR selects DEO
                cs = new CommonStatistics();

                if (userDAO.getUserByPK(userId) != null) {
                    cs = birthRegistrationService.getBirthStatisticsForUser(userId);
                    populateBirthStatistics(cs);

                    cs = deathRegistrationService.getDeathStatisticsForUser(userId);
                    populateDeathStatistics(cs);

                    cs = marriageRegistrationService.getMarriageStatisticsForUser(userId);
                    populateMarriageStatistics(cs);
                } else {
                    // if user is null, populate default(0) values
                    populateBirthStatistics(cs);
                }

            } else if (Role.ROLE_RG.equals(userRole) || Role.ROLE_ARG.equals(userRole)) { // RG selects District or DsDivision

                logger.debug("District = {} DSDivision = {}", districtId, dsDivisionId);
                cs_b = new CommonStatistics();
                cs_d = new CommonStatistics();
                cs_m = new CommonStatistics();
                cs = new CommonStatistics();
                List<User> allUsers = userDAO.getUsersByRole("DEO");
                List<User> temp = new ArrayList<User>();

                if (districtId != null) {
                    District district = districtDAO.getDistrict(Integer.parseInt(districtId));
                    for (User user : allUsers) {
                        if (user.getAssignedBDDistricts().contains(district)) {
                            temp.add(user);
                        }
                    }
                    if (temp.size() > 0) {
                        // adding together all the birth statistics objects which belongs to 'temp' list members
                        for (User user : temp) {
                            cs_b.add(birthRegistrationService.getBirthStatisticsForUser(user.getUserId()));
                            cs_d.add(deathRegistrationService.getDeathStatisticsForUser(user.getUserId()));
                            cs_m.add(marriageRegistrationService.getMarriageStatisticsForUser(user.getUserId()));
                        }
                        // adding combined statistics object to response object
                        populateBirthStatistics(cs_b);
                        populateDeathStatistics(cs_d);
                        populateMarriageStatistics(cs_m);
                    }
                } else if (dsDivisionId != null) {
                    DSDivision dsDivision = dsDivisionDAO.getDSDivisionByPK(Integer.parseInt(dsDivisionId));
                    for (User user : allUsers) {
                        if (user.getAssignedBDDSDivisions().contains(dsDivision)) {
                            temp.add(user);
                        }
                    }
                    if (temp.size() > 0) {
                        // adding together all the birth statistics objects which belongs to 'temp' list members
                        for (User user : temp) {
                            cs_b.add(birthRegistrationService.getBirthStatisticsForUser(user.getUserId()));
                            cs_d.add(deathRegistrationService.getDeathStatisticsForUser(user.getUserId()));
                            cs_m.add(marriageRegistrationService.getMarriageStatisticsForUser(user.getUserId()));
                        }
                        // adding combined statistics object to response object
                        populateBirthStatistics(cs_b);
                        populateDeathStatistics(cs_d);
                        populateMarriageStatistics(cs_m);
                    }

                } else {
                    // if user is null, populate default(0) values
                    populateBirthStatistics(cs);
                }

                logger.debug("temp.size = {}", temp.size());

            }


        } catch (Exception e) {
            logger.error("[JSONStatisticsLookupService] Fatal Error : {}", e);
            return;
        }*/

        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        logger.debug("OptionList size = {}", optionLists.size());
        mapper.writeValue(out, optionLists);
        out.flush();

    }

    public void populateBirthStatistics(CommonStatistics cs) {
        optionLists.put("approved_b", cs.getApprovedItems());
        optionLists.put("rejected_b", cs.getRejectedItems());
        optionLists.put("thismonth_pend_b", cs.getThisMonthPendingItems());
        optionLists.put("arrears_pend_b", cs.getArrearsPendingItems());
        optionLists.put("total_submitted_b", cs.getTotalSubmissions());
        optionLists.put("normal_b", cs.getNormalSubmissions());
        optionLists.put("late_b", cs.getLateSubmissions());
        optionLists.put("userRole", cs.getUserRole());
    }

    public void populateDeathStatistics(CommonStatistics cs) {
        optionLists.put("approved_d", cs.getApprovedItems());
        optionLists.put("rejected_d", cs.getRejectedItems());
        optionLists.put("thismonth_pend_d", cs.getThisMonthPendingItems());
        optionLists.put("arrears_pend_d", cs.getArrearsPendingItems());
        optionLists.put("total_submitted_d", cs.getTotalSubmissions());
        optionLists.put("normal_d", cs.getNormalSubmissions());
        optionLists.put("late_d", cs.getLateSubmissions());
        optionLists.put("userRole", cs.getUserRole());
    }

    public void populateMarriageStatistics(CommonStatistics cs) {
        optionLists.put("approved_m", cs.getApprovedItems());
        optionLists.put("rejected_m", cs.getRejectedItems());
        optionLists.put("thismonth_pend_m", cs.getThisMonthPendingItems());
        optionLists.put("arrears_pend_m", cs.getArrearsPendingItems());
        optionLists.put("total_submitted_m", cs.getTotalSubmissions());
        optionLists.put("normal_m", cs.getNormalSubmissions());
        optionLists.put("late_m", cs.getLateSubmissions());
        optionLists.put("userRole", cs.getUserRole());
    }
}
