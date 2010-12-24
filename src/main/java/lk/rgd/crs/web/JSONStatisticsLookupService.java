package lk.rgd.crs.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import lk.rgd.common.api.domain.CommonStatistics;
import lk.rgd.common.api.service.StatisticsCollectorService;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.service.DeathAlterationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author shan
 */
public class JSONStatisticsLookupService extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(JSONStatisticsLookupService.class);
    private final ObjectMapper mapper = new ObjectMapper();
    //private StatisticsCollectorService statisticsCollectorService;
    private DeathRegistrationService deathRegistrationService;
    private BirthRegistrationService birthRegistrationService;
    private MarriageRegistrationService marriageRegistrationService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context =
            WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        deathRegistrationService = (DeathRegistrationService) context.getBean("deathRegisterService");
        birthRegistrationService = (BirthRegistrationService) context.getBean("manageBirthService");
        marriageRegistrationService = (MarriageRegistrationService) context.getBean("marriageRegistrationService");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userType = request.getParameter(WebConstants.USER_TYPE);
        String statType = request.getParameter(WebConstants.STAT_TYPE);
        String deoUserId = request.getParameter(WebConstants.USER_DEO);
        String mode = request.getParameter("mode");

        logger.debug("Received Division userType and statType : {} {} ", userType, statType);
        logger.debug("Received Division mode and user id : {} {}", mode, deoUserId);
        HashMap<String, Object> optionLists = new HashMap<String, Object>();
        CommonStatistics cs;

        try {
            if (mode.equals("drStatInfo")) {
                //cs = birthRegistrationService.getBirthStatisticsForADR(deoUserId);
            } else if (mode.equals("adrStatInfo")) {

                cs = birthRegistrationService.getBirthStatisticsForDEO(deoUserId);
                if (cs != null) {
                    optionLists = populateBirthStatistics(cs);
                }

                cs = deathRegistrationService.getDeathStatisticsForDEO(deoUserId);
                if (cs != null) {
                    optionLists = populateDeathStatistics(cs);
                }

                cs = marriageRegistrationService.getMarriageStatisticsForDEO(deoUserId);
                if (cs != null) {
                    optionLists = populateMarriageStatistics(cs);
                }

            } else if (mode.equals("commonStatInfo")) {
                if (userType.equals(WebConstants.USER_ADR)) {
                    if (statType.equals(WebConstants.STAT_ALL)) {
                        cs = populateBirthStatistics(WebConstants.USER_ADR);
                        if (cs != null) {
                            optionLists = populateBirthStatistics(cs);
                        }

                        cs = populateDeathStatistics(WebConstants.USER_ADR);
                        if (cs != null) {
                            optionLists = populateDeathStatistics(cs);
                        }

                        cs = populateMarriageStatistics(WebConstants.USER_ADR);
                        if (cs != null) {
                            optionLists = populateMarriageStatistics(cs);
                        }

                    }

                } else if (userType.equals(WebConstants.USER_DEO)) {
                    if (statType.equals(WebConstants.STAT_ALL)) {
                        cs = populateBirthStatistics(WebConstants.USER_DEO);
                        if (cs != null) {
                            optionLists = populateBirthStatistics(cs);
                        }

                        logger.debug("Births Total Submissions for DEO {}", cs.getTotalSubmissions());

                        cs = populateDeathStatistics(WebConstants.USER_DEO);
                        if (cs != null) {
                            optionLists = populateDeathStatistics(cs);
                        }

                        cs = populateMarriageStatistics(WebConstants.USER_DEO);
                        if (cs != null) {
                            optionLists = populateMarriageStatistics(cs);
                        }

                    }
                } else if (userType.equals(WebConstants.USER_ARG)) {
                    if (statType.equals(WebConstants.STAT_ALL)) {
                        cs = populateBirthStatistics(WebConstants.USER_ARG);
                        if (cs != null) {
                            optionLists = populateBirthStatistics(cs);
                        }

                        cs = populateDeathStatistics(WebConstants.USER_ARG);
                        if (cs != null) {
                            optionLists = populateDeathStatistics(cs);
                        }

                        cs = populateMarriageStatistics(WebConstants.USER_ARG);
                        if (cs != null) {
                            optionLists = populateMarriageStatistics(cs);
                        }

                    }
                } else if (userType.equals(WebConstants.USER_DR)) {
                    if (statType.equals(WebConstants.STAT_ALL)) {
                        cs = populateBirthStatistics(WebConstants.USER_ARG);
                        if (cs != null) {
                            optionLists = populateBirthStatistics(cs);
                        }

                        cs = populateDeathStatistics(WebConstants.USER_ARG);
                        if (cs != null) {
                            optionLists = populateDeathStatistics(cs);
                        }

                        cs = populateMarriageStatistics(WebConstants.USER_ARG);
                        if (cs != null) {
                            optionLists = populateMarriageStatistics(cs);
                        }

                    }
                } else if (userType.equals(WebConstants.USER_RG)) {
                    if (statType.equals(WebConstants.STAT_ALL)) {
                        cs = populateBirthStatistics(WebConstants.USER_ARG);
                        if (cs != null) {
                            optionLists = populateBirthStatistics(cs);
                        }

                        cs = populateDeathStatistics(WebConstants.USER_ARG);
                        if (cs != null) {
                            optionLists = populateDeathStatistics(cs);
                        }

                        cs = populateMarriageStatistics(WebConstants.USER_ARG);
                        if (cs != null) {
                            optionLists = populateMarriageStatistics(cs);
                        }
                    }
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

    public CommonStatistics populateBirthStatistics(String user) {
        // TODO User Specific Functions
        CommonStatistics commonStat;

        if (user.equals(WebConstants.USER_ADR)) {
            commonStat = birthRegistrationService.getCommonBirthCertificateCount(user);
        } else if (user.equals(WebConstants.USER_DEO)) {
            commonStat = birthRegistrationService.getCommonBirthCertificateCount(user);
        } else if (user.equals(WebConstants.USER_ARG)) {
            commonStat = birthRegistrationService.getCommonBirthCertificateCount(user);
        } else if (user.equals(WebConstants.USER_DR)) {
            commonStat = birthRegistrationService.getCommonBirthCertificateCount(user);
        } else if (user.equals(WebConstants.USER_RG)) {
            commonStat = birthRegistrationService.getCommonBirthCertificateCount(user);
        } else {
            commonStat = new CommonStatistics();
        }

        return commonStat;
    }

    public CommonStatistics populateDeathStatistics(String user) {
        // TODO User Specific Functions
        CommonStatistics commonStat;

        if (user.equals(WebConstants.USER_ADR)) {
            commonStat = deathRegistrationService.getCommonDeathCertificateCount(user);
        } else if (user.equals(WebConstants.USER_DEO)) {
            commonStat = deathRegistrationService.getCommonDeathCertificateCount(user);
        } else if (user.equals(WebConstants.USER_ARG)) {
            commonStat = deathRegistrationService.getCommonDeathCertificateCount(user);
        } else if (user.equals(WebConstants.USER_DR)) {
            commonStat = deathRegistrationService.getCommonDeathCertificateCount(user);
        } else if (user.equals(WebConstants.USER_RG)) {
            commonStat = deathRegistrationService.getCommonDeathCertificateCount(user);
        } else {
            commonStat = new CommonStatistics();
        }
        return commonStat;
    }

    public CommonStatistics populateMarriageStatistics(String user) {
        // TODO User Specific Functions
        CommonStatistics commonStat;

        if (user.equals(WebConstants.USER_ADR)) {
            commonStat = marriageRegistrationService.getCommonMarriageCertificateCount(user);
        } else if (user.equals(WebConstants.USER_DEO)) {
            commonStat = marriageRegistrationService.getCommonMarriageCertificateCount(user);
        } else if (user.equals(WebConstants.USER_ARG)) {
            commonStat = marriageRegistrationService.getCommonMarriageCertificateCount(user);
        } else if (user.equals(WebConstants.USER_DR)) {
            commonStat = marriageRegistrationService.getCommonMarriageCertificateCount(user);
        } else if (user.equals(WebConstants.USER_RG)) {
            commonStat = marriageRegistrationService.getCommonMarriageCertificateCount(user);
        } else {
            commonStat = new CommonStatistics();
        }
        return commonStat;
    }

    public HashMap<String, Object> populateBirthStatistics(CommonStatistics cs) {
        HashMap<String, Object> optionLists = new HashMap<String, Object>();
        optionLists.put("approved_b", cs.getApprovedItems());
        optionLists.put("rejected_b", cs.getRejectedItems());
        optionLists.put("this_month_b", cs.getThisMonthPendingItems());
        optionLists.put("arrears_b", cs.getArrearsPendingItems());
        optionLists.put("normal_b", cs.getNormalSubmissions());
        optionLists.put("late_b", cs.getLateSubmissions());
        return optionLists;
    }

    public HashMap<String, Object> populateDeathStatistics(CommonStatistics cs) {
        HashMap<String, Object> optionLists = new HashMap<String, Object>();
        optionLists.put("approved_d", /*cs.getApprovedItems()*/12);
        optionLists.put("rejected_d", /*cs.getRejectedItems()*/4);
        optionLists.put("this_month_d",/* cs.getThisMonthPendingItems()*/5);
        optionLists.put("arrears_d", /*cs.getArrearsPendingItems()*/8);
        optionLists.put("normal_d", /*cs.getNormalSubmissions()*/1);
        optionLists.put("late_d", /*cs.getLateSubmissions()*/2);
        return optionLists;
    }

    public HashMap<String, Object> populateMarriageStatistics(CommonStatistics cs) {
        HashMap<String, Object> optionLists = new HashMap<String, Object>();
        optionLists.put("approved_m", /*cs.getApprovedItems()*/21);
        optionLists.put("rejected_m", /*cs.getRejectedItems()*/9);
        optionLists.put("this_month_m", /*cs.getThisMonthPendingItems()*/8);
        optionLists.put("arrears_m", /*cs.getArrearsPendingItems()*/5);
        optionLists.put("normal_m", /*cs.getNormalSubmissions()*/4);
        optionLists.put("late_m", /*cs.getLateSubmissions()*/2);
        return optionLists;
    }
}
