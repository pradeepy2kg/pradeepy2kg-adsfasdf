package lk.rgd.crs.web;

import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.domain.CommonStatistics;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.Statistics;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.StatisticsManager;
import lk.rgd.common.util.DateTimeUtils;
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
import java.util.Date;
import java.util.HashMap;

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
        commonStat.setCertificateGeneratedItems(stat.getBirthCertificateGenerated());
        commonStat.setCertificatePrintedItems(stat.getBirthCertificatePrinted());
        commonStat.setDeletedItems(stat.getBirthDeletedItems());
        commonStat.setConfirmationApprovalPendingItems(stat.getBirthConfirmationApprovalPendingItems());
        commonStat.setConfirmationApprovedItems(stat.getBirthConfirmationApprovedItems());
        commonStat.setConfirmationPrintedItems(stat.getBirthConfirmationPrintedItems());
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
        commonStat.setDeletedItems(stat.getDeathsDeletedItems());
        commonStat.setCertificatePrintedItems(stat.getDeathCertificatePrintedItems());
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
        int districtId = Integer.parseInt(request.getParameter("districtId"));
        int dsDivisionId = Integer.parseInt(request.getParameter("dsDivisionId"));

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
        Statistics statistics = statisticsService.getStatisticsForUser(statUser, startDate, endDate, districtId, dsDivisionId);
//        Statistics statistics = statisticsService.getStatisticsForUser(statUser, startDate, endDate);

        CommonStatistics cs_b = new CommonStatistics();
        CommonStatistics cs_d = new CommonStatistics();
        CommonStatistics cs_m = new CommonStatistics();

        birthDataTransfer(statistics, cs_b, userRole);
        deathDataTransfer(statistics, cs_d, userRole);
        marriageDataTransfer(statistics, cs_m, userRole);

        populateBirthStatistics(cs_b);
        populateDeathStatistics(cs_d);
        populateMarriageStatistics(cs_m);

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
        optionLists.put("confirmation_printed_b", cs.getConfirmationPrintedItems());
        optionLists.put("confirmation_approval_pending_b", cs.getConfirmationApprovalPendingItems());
        optionLists.put("confirmation_approved_b", cs.getConfirmationApprovedItems());
        optionLists.put("certificate_generated_b", cs.getCertificateGeneratedItems());
        optionLists.put("certificate_printed_b", cs.getCertificatePrintedItems());
        optionLists.put("deleted_b", cs.getDeletedItems());
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
        optionLists.put("certificate_printed_d", cs.getCertificatePrintedItems());
        optionLists.put("deleted_d", cs.getDeletedItems());
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
