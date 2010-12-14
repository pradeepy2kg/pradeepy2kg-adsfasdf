package lk.rgd.crs.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

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
        HashMap<String, Object> optionLists;

        try {
            optionLists = new HashMap<String, Object>();
            if (userType.equals(WebConstants.USER_ADR)) {
                if (statType.equals(WebConstants.STAT_ALL)) {
                    optionLists.put("submitted_b", 3);
                    optionLists.put("approved_b", 5);
                    optionLists.put("rejected_b", 14);
                    optionLists.put("pending_b", 8);

                    optionLists.put("submitted_d", 2);
                    optionLists.put("approved_d", 8);
                    optionLists.put("rejected_d", 6);
                    optionLists.put("pending_d", 18);
                } else if(statType.equals(WebConstants.STAT_BIRTH)) {
                    // TODO [shan]
                } else if(statType.equals(WebConstants.STAT_DEATH)) {

                } else if(statType.equals(WebConstants.STAT_MARRIAGE)) {

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
                } else if(statType.equals(WebConstants.STAT_BIRTH)) {

                } else if(statType.equals(WebConstants.STAT_DEATH)) {

                } else if(statType.equals(WebConstants.STAT_MARRIAGE)) {

                }
            } else if (userType.equals(WebConstants.USER_ARG)) {

            } else if (userType.equals(WebConstants.USER_DR)) {

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
                } else if(statType.equals(WebConstants.STAT_BIRTH)) {

                } else if(statType.equals(WebConstants.STAT_DEATH)) {

                } else if(statType.equals(WebConstants.STAT_MARRIAGE)) {

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
}
