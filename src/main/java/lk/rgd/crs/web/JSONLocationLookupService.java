package lk.rgd.crs.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * @author Chathuranga Withana
 */
public class JSONLocationLookupService extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(JSONLocationLookupService.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        // TODO inject DAOs
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userLocationId = request.getParameter(WebConstants.USER_LOCATION_ID);
        logger.debug("Received User Location Id : {}", userLocationId);

        HashMap<String, Object> optionList = new HashMap<String, Object>();

        HttpSession session = request.getSession();

        // TODO load from DAOs
        if ("1".equals(userLocationId)) {
            optionList.put("authorizedUsers", loadListForLocation1());

        } else {
            optionList.put("authorizedUsers", loadListForLocation2());

        }

        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();

        mapper.writeValue(out, optionList);
        out.flush();
    }

    private List getList(Map<Integer, String> map) {
        // TODO still implementing
        return null;
    }

    private List<SelectOption> loadListForLocation1() {
        List<SelectOption> list = new ArrayList<SelectOption>();

        for (int i = 1; i < 10; i++) {
            SelectOption option = new SelectOption();
            option.setOptionValue(i);
            option.setOptionDisplay("Person 1 Value " + i);
            list.add(option);
        }
        return list;
    }

    private List<SelectOption> loadListForLocation2() {
        List<SelectOption> list = new ArrayList<SelectOption>();

        for (int i = 1; i < 10; i++) {
            SelectOption option = new SelectOption();
            option.setOptionValue(i);
            option.setOptionDisplay("Person 2 Value " + i);
            list.add(option);
        }
        return list;
    }
}
