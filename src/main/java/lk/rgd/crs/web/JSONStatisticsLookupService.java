package lk.rgd.crs.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import lk.rgd.common.api.domain.CommonStatistics;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shan
 */
public class JSONStatisticsLookupService extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(JSONStatisticsLookupService.class);
    private final ObjectMapper mapper = new ObjectMapper();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userType = request.getParameter(WebConstants.USER_TYPE);
        String statType = request.getParameter(WebConstants.STAT_TYPE);

        logger.debug("Received Division userType and statType : {} {} ", userType, statType);
        HashMap<String, Object> optionLists = new HashMap<String, Object>();
        CommonStatistics cs;

        try {
            if (userType.equals(WebConstants.USER_ADR)) {
                if (statType.equals(WebConstants.STAT_ALL)) {

                    cs = populateCommonStat(WebConstants.STAT_BIRTH, WebConstants.USER_ADR);

                    optionLists.put("approved_b", cs.getApprovedItems());
                    optionLists.put("rejected_b", cs.getRejectedItems());
                    optionLists.put("this_month_b", cs.getThisMonthPendingItems());
                    optionLists.put("arrears_b", cs.getArrearsPendingItems());
                    optionLists.put("normal_b", cs.getNormalSubmissions());
                    optionLists.put("late_b", cs.getLateSubmissions());

                    cs = populateCommonStat(WebConstants.STAT_DEATH, WebConstants.USER_ADR);

                    optionLists.put("approved_d", cs.getApprovedItems());
                    optionLists.put("rejected_d", cs.getRejectedItems());
                    optionLists.put("this_month_d", cs.getThisMonthPendingItems());
                    optionLists.put("arrears_d", cs.getArrearsPendingItems());
                    optionLists.put("normal_d", cs.getNormalSubmissions());
                    optionLists.put("late_d", cs.getLateSubmissions());

                    cs = populateCommonStat(WebConstants.STAT_MARRIAGE, WebConstants.USER_ADR);

                    /*
                    * TODO populate CommonStatistics object
                    */

                } else if (statType.equals(WebConstants.STAT_BIRTH)) {

                    cs = populateCommonStat(WebConstants.STAT_BIRTH, WebConstants.USER_ADR);

                    optionLists.put("approved_b", cs.getApprovedItems());
                    optionLists.put("rejected_b", cs.getRejectedItems());
                    optionLists.put("this_month_b", cs.getThisMonthPendingItems());
                    optionLists.put("arrears_b", cs.getArrearsPendingItems());
                    optionLists.put("normal_b", cs.getNormalSubmissions());
                    optionLists.put("late_b", cs.getLateSubmissions());

                } else if (statType.equals(WebConstants.STAT_DEATH)) {

                    cs = populateCommonStat(WebConstants.STAT_DEATH, WebConstants.USER_ADR);

                    optionLists.put("approved_d", cs.getApprovedItems());
                    optionLists.put("rejected_d", cs.getRejectedItems());
                    optionLists.put("this_month_d", cs.getThisMonthPendingItems());
                    optionLists.put("arrears_d", cs.getArrearsPendingItems());
                    optionLists.put("normal_d", cs.getNormalSubmissions());
                    optionLists.put("late_d", cs.getLateSubmissions());

                } else if (statType.equals(WebConstants.STAT_MARRIAGE)) {
                    /*
                    * TODO populate CommonStatistics object
                    */
                }
            } else if (userType.equals(WebConstants.USER_DEO)) {
                if (statType.equals(WebConstants.STAT_ALL)) {
                    optionLists.put("submitted_b", 1);
                    optionLists.put("approved_b", 5);
                    optionLists.put("rejected_b", 1);
                    optionLists.put("pending_b", 8);

                    optionLists.put("submitted_d", 2);
                    optionLists.put("approved_d", 8);
                    optionLists.put("rejected_d", 4);
                    optionLists.put("pending_d", 1);

                    optionLists.put("this_month_b", 8);
                    optionLists.put("arrears_b", 6);
                    optionLists.put("normal_b", 2);
                    optionLists.put("late_b", 6);

                    optionLists.put("this_month_d", 3);
                    optionLists.put("arrears_d", 4);
                    optionLists.put("normal_d", 6);
                    optionLists.put("late_d", 9);

                } else if (statType.equals(WebConstants.STAT_BIRTH)) {

                } else if (statType.equals(WebConstants.STAT_DEATH)) {

                } else if (statType.equals(WebConstants.STAT_MARRIAGE)) {

                }
            } else if (userType.equals(WebConstants.USER_ARG)) {
                if (statType.equals(WebConstants.STAT_ALL)) {
                    optionLists.put("submitted_b", 1);
                    optionLists.put("approved_b", 5);
                    optionLists.put("rejected_b", 1);
                    optionLists.put("pending_b", 8);

                    optionLists.put("submitted_d", 2);
                    optionLists.put("approved_d", 8);
                    optionLists.put("rejected_d", 4);
                    optionLists.put("pending_d", 1);

                    optionLists.put("this_month_b", 8);
                    optionLists.put("arrears_b", 6);
                    optionLists.put("normal_b", 2);
                    optionLists.put("late_b", 6);

                    optionLists.put("this_month_d", 3);
                    optionLists.put("arrears_d", 4);
                    optionLists.put("normal_d", 6);
                    optionLists.put("late_d", 9);

                } else if (statType.equals(WebConstants.STAT_BIRTH)) {

                } else if (statType.equals(WebConstants.STAT_DEATH)) {

                } else if (statType.equals(WebConstants.STAT_MARRIAGE)) {

                }
            } else if (userType.equals(WebConstants.USER_DR)) {
                if (statType.equals(WebConstants.STAT_ALL)) {
                    optionLists.put("submitted_b", 1);
                    optionLists.put("approved_b", 5);
                    optionLists.put("rejected_b", 1);
                    optionLists.put("pending_b", 8);

                    optionLists.put("submitted_d", 2);
                    optionLists.put("approved_d", 8);
                    optionLists.put("rejected_d", 4);
                    optionLists.put("pending_d", 1);

                    optionLists.put("this_month_b", 8);
                    optionLists.put("arrears_b", 6);
                    optionLists.put("normal_b", 2);
                    optionLists.put("late_b", 6);

                    optionLists.put("this_month_d", 3);
                    optionLists.put("arrears_d", 4);
                    optionLists.put("normal_d", 6);
                    optionLists.put("late_d", 9);

                } else if (statType.equals(WebConstants.STAT_BIRTH)) {

                } else if (statType.equals(WebConstants.STAT_DEATH)) {

                } else if (statType.equals(WebConstants.STAT_MARRIAGE)) {

                }
            } else if (userType.equals(WebConstants.USER_RG)) {
                if (statType.equals(WebConstants.STAT_ALL)) {
                    optionLists.put("submitted_b", 4);
                    optionLists.put("approved_b", 5);
                    optionLists.put("rejected_b", 3);
                    optionLists.put("pending_b", 6);

                    optionLists.put("submitted_d", 1);
                    optionLists.put("approved_d", 1);
                    optionLists.put("rejected_d", 4);
                    optionLists.put("pending_d", 1);

                    optionLists.put("this_month_b", 3);
                    optionLists.put("arrears_b", 4);
                    optionLists.put("normal_b", 1);
                    optionLists.put("late_b", 4);

                    optionLists.put("this_month_d", 3);
                    optionLists.put("arrears_d", 9);
                    optionLists.put("normal_d", 2);
                    optionLists.put("late_d", 4);

                } else if (statType.equals(WebConstants.STAT_BIRTH)) {

                } else if (statType.equals(WebConstants.STAT_DEATH)) {

                } else if (statType.equals(WebConstants.STAT_MARRIAGE)) {

                }
            }

        } catch (Exception e) {
            logger.error("[JSONStatisticsLookupService] Fatal Error : {}", e);
            return;
        }


        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();

        mapper.writeValue(out, optionLists);
        out.flush();

    }

    public CommonStatistics populateCommonStat(String type, String user) {
        CommonStatistics commonStat = new CommonStatistics();

        commonStat.setStatType(type);

        if (type.equals(WebConstants.STAT_BIRTH)) {

            // TODO get CommonStat object for Births

            commonStat.setApprovedItems(30);
            commonStat.setArrearsPendingItems(20);
            commonStat.setLateSubmissions(50);
            commonStat.setNormalSubmissions(30);
            commonStat.setRejectedItems(20);
            commonStat.setThisMonthPendingItems(60);
            commonStat.setTotalPendingItems(80);
            commonStat.setTotalSubmissions(80);
        }
        if (type.equals(WebConstants.STAT_DEATH)) {

            // TODO get CommonStat object for Births

            commonStat.setApprovedItems(30);
            commonStat.setArrearsPendingItems(20);
            commonStat.setLateSubmissions(50);
            commonStat.setNormalSubmissions(30);
            commonStat.setRejectedItems(20);
            commonStat.setThisMonthPendingItems(60);
            commonStat.setTotalPendingItems(80);
            commonStat.setTotalSubmissions(80);
        }
        if (type.equals(WebConstants.STAT_MARRIAGE)) {

            // TODO get CommonStat object for Births

            commonStat.setApprovedItems(30);
            commonStat.setArrearsPendingItems(20);
            commonStat.setLateSubmissions(50);
            commonStat.setNormalSubmissions(30);
            commonStat.setRejectedItems(20);
            commonStat.setThisMonthPendingItems(60);
            commonStat.setTotalPendingItems(80);
            commonStat.setTotalSubmissions(80);
        }

        return commonStat;
    }


}
