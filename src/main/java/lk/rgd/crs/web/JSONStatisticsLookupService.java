package lk.rgd.crs.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.domain.CommonStatistics;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.service.BirthRegistrationService;
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
    private DeathRegistrationService deathRegistrationService;          //todo to be removed
    private BirthRegistrationService birthRegistrationService;          //todo to be removed
    private MarriageRegistrationService marriageRegistrationService;    //todo to be removed
    private UserDAO userDAO;
    private DistrictDAO districtDAO;
    private DSDivisionDAO dsDivisionDAO;
    HashMap<String, Object> optionLists;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context =
            WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        deathRegistrationService = (DeathRegistrationService) context.getBean("deathRegisterService");
        birthRegistrationService = (BirthRegistrationService) context.getBean("manageBirthService");
        marriageRegistrationService = (MarriageRegistrationService) context.getBean("marriageRegistrationService");
        userDAO = (UserDAO) context.getBean("userDAOImpl");
        dsDivisionDAO = (DSDivisionDAO) context.getBean("dsDivisionDAOImpl");
        districtDAO = (DistrictDAO) context.getBean("districtDAOImpl");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userType = request.getParameter(WebConstants.USER_TYPE);
        String statType = request.getParameter(WebConstants.STAT_TYPE);
        String deoUserId = request.getParameter(WebConstants.USER_DEO);
        String userName = request.getParameter("userName");
        String mode = request.getParameter("mode");
        String districtId = request.getParameter("districtId");
        String dsDivisionId = request.getParameter("dsDivisionId");

        logger.debug("Received Division userType and statType : {} {} ", userType, statType);
        logger.debug("Received Division mode and user id : {} {}", mode, deoUserId);

        optionLists = new HashMap<String, Object>();

        try {
            if (mode.equals("drStatInfo")) {  // DR selects ADR
                CommonStatistics cs_b = new CommonStatistics();
                CommonStatistics cs_d = new CommonStatistics();
                CommonStatistics cs_m = new CommonStatistics();
                CommonStatistics cs = new CommonStatistics();    // default
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
                        /* adding together all the birth statistics objects which belongs to 'temp' list members */
                        for (User one : temp) {
                            cs_b.add(birthRegistrationService.getBirthStatisticsForUser(one.getUserId()));
                            cs_d.add(deathRegistrationService.getDeathStatisticsForUser(one.getUserId()));
                            cs_m.add(marriageRegistrationService.getMarriageStatisticsForUser(one.getUserId()));
                        }
                        /* adding combined statistics object to response object */
                        populateBirthStatistics(cs_b);
                        populateDeathStatistics(cs_d);
                        populateMarriageStatistics(cs_m);
                    }
                } else {
                    /* if user is null, populate default(0) values */
                    populateBirthStatistics(cs);
                }

            } else if (mode.equals("adrStatInfo") || mode.equals("deoStatInfo")) {  // ADR selects DEO
                CommonStatistics cs = new CommonStatistics();

                if (userDAO.getUserByPK(deoUserId) != null) {
                    cs = birthRegistrationService.getBirthStatisticsForUser(deoUserId);
                    populateBirthStatistics(cs);

                    cs = deathRegistrationService.getDeathStatisticsForUser(deoUserId);
                    populateDeathStatistics(cs);

                    cs = marriageRegistrationService.getMarriageStatisticsForUser(deoUserId);
                    populateMarriageStatistics(cs);
                } else {
                    /* if user is null, populate default(0) values */
                    populateBirthStatistics(cs);
                }

            } else if (mode.equals("rgStatInfo") || mode.equals("argStatInfo")) { // RG selects District or DsDivision

                logger.debug("District = {} DSDivision = {}", districtId, dsDivisionId);
                CommonStatistics cs_b = new CommonStatistics();
                CommonStatistics cs_d = new CommonStatistics();
                CommonStatistics cs_m = new CommonStatistics();
                CommonStatistics cs = new CommonStatistics();
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
                        /* adding together all the birth statistics objects which belongs to 'temp' list members */
                        for (User user : temp) {
                            cs_b.add(birthRegistrationService.getBirthStatisticsForUser(user.getUserId()));
                            cs_d.add(deathRegistrationService.getDeathStatisticsForUser(user.getUserId()));
                            cs_m.add(marriageRegistrationService.getMarriageStatisticsForUser(user.getUserId()));
                        }
                        /* adding combined statistics object to response object */
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
                        /* adding together all the birth statistics objects which belongs to 'temp' list members */
                        for (User user : temp) {
                            cs_b.add(birthRegistrationService.getBirthStatisticsForUser(user.getUserId()));
                            cs_d.add(deathRegistrationService.getDeathStatisticsForUser(user.getUserId()));
                            cs_m.add(marriageRegistrationService.getMarriageStatisticsForUser(user.getUserId()));
                        }
                        /* adding combined statistics object to response object */
                        populateBirthStatistics(cs_b);
                        populateDeathStatistics(cs_d);
                        populateMarriageStatistics(cs_m);
                    }

                } else {
                    /* if user is null, populate default(0) values */
                    populateBirthStatistics(cs);
                }

                logger.debug("temp.size = {}", temp.size());

            }

        } catch (Exception e) {
            logger.error("[JSONStatisticsLookupService] Fatal Error : {}", e);
            return;
        }

        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        logger.debug("OptionList size = {}", optionLists.size());
        mapper.writeValue(out, optionLists);
        out.flush();

    }

    public void populateBirthStatistics(CommonStatistics cs) {
        optionLists.put("approved_b", cs.getApprovedItems());
        optionLists.put("rejected_b", cs.getRejectedItems());
        optionLists.put("this_month_b", cs.getThisMonthPendingItems());
        optionLists.put("arrears_b", cs.getArrearsPendingItems());
        optionLists.put("normal_b", cs.getNormalSubmissions());
        optionLists.put("late_b", cs.getLateSubmissions());
    }

    public void populateDeathStatistics(CommonStatistics cs) {
        optionLists.put("approved_d", cs.getApprovedItems());
        optionLists.put("rejected_d", cs.getRejectedItems());
        optionLists.put("this_month_d", cs.getThisMonthPendingItems());
        optionLists.put("arrears_d", cs.getArrearsPendingItems());
        optionLists.put("normal_d", cs.getNormalSubmissions());
        optionLists.put("late_d", cs.getLateSubmissions());
    }

    public void populateMarriageStatistics(CommonStatistics cs) {
        optionLists.put("approved_m", cs.getApprovedItems());
        optionLists.put("rejected_m", cs.getRejectedItems());
        optionLists.put("this_month_m", cs.getThisMonthPendingItems());
        optionLists.put("arrears_m", cs.getArrearsPendingItems());
        optionLists.put("normal_m", cs.getNormalSubmissions());
        optionLists.put("late_m", cs.getLateSubmissions());
    }
}
