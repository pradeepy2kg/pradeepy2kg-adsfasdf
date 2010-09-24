package lk.rgd.crs.web;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
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
 * @author asankha
 */
public class JSONDivisionLookupService extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(JSONDivisionLookupService.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private DSDivisionDAO dsDivisionDAO;
    private BDDivisionDAO bdDivisionDAO;
    private DistrictDAO districtDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        dsDivisionDAO = (DSDivisionDAO) context.getBean("dsDivisionDAOImpl");
        bdDivisionDAO = (BDDivisionDAO) context.getBean("bdDivisionDAOImpl");
        districtDAO = (DistrictDAO) context.getBean("districtDAOImpl");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter(WebConstants.DIVISION_ID);
        String mode = request.getParameter(WebConstants.MODE);
        logger.debug("Received Division Id and mode : {} {} ", id, mode);
        User user;
        String lang;
        HashMap<String, Object> optionLists;

        try {
            HttpSession session = request.getSession();
            user = (User) session.getAttribute(WebConstants.SESSION_USER_BEAN);
            lang = ((Locale) session.getAttribute(WebConstants.SESSION_USER_LANG)).getLanguage();
            int divisionId = Integer.parseInt(id);
            optionLists = new HashMap<String, Object>();
            logger.info("selected district : {}", id);

            if ("1".equals(mode)) {
                // passing districtId, return only the DS list
                optionLists.put("dsDivisionList", getDSDivisions(lang, divisionId, user));
            } else if ("2".equals(mode)) {
                // passing dsDivisionId, return the BD list
                optionLists.put("bdDivisionList", getBDDivisions(lang, divisionId, user));
            } else if ("3".equals(mode)) {
                // passing districtId, return all DSDivision list.
                optionLists.put("dsDivisionList", getAllDSDivisions(lang, divisionId, user));
            } else if ("4".equals(mode)) {
                optionLists.put("districtList", getAllDisList(lang, user));
            } else {
                // passing districtId, return DS List and the BD List for the 1st DS division
                List ds = getAllDSDivisions(lang, divisionId, user);
                int dsDivisionId = ((SelectOption) ds.get(0)).getOptionValue();
                List bd = getBDDivisions(lang, dsDivisionId, user);
                optionLists.put("dsDivisionList", ds);
                optionLists.put("bdDivisionList", bd);
            }
        } catch (Exception e) {
            logger.error("Fatal Error : {}", e);
            return;
        }

        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();

        mapper.writeValue(out, optionLists);
        out.flush();
    }

    private List getBDDivisions(String language, int dsDivisionId, User user) {
        Map<Integer, String> bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        logger.debug("Loaded BD list : {}", bdDivisionList);

        return getList(bdDivisionList);
    }

    private List getAllDSDivisions(String language, int BDId, User user) {
        Map<Integer, String> dsDivisionList = dsDivisionDAO.getAllDSDivisionNames(BDId, language, user);
        logger.debug("Loaded DS list : {}", dsDivisionList);

        return getList(dsDivisionList);
    }

    private List getAllDisList(String language, User user) {
        Map<Integer, String> districtList = districtDAO.getAllDistrictNames(language, user);
        return getList(districtList);
    }

    private List getDSDivisions(String language, int BDId, User user) {
        Map<Integer, String> dsDivisionList = dsDivisionDAO.getDSDivisionNames(BDId, language, user);
        logger.debug("Loaded DS list : {}", dsDivisionList);

        return getList(dsDivisionList);
    }


    private List getList(Map<Integer, String> map) {
        List<SelectOption> ds = new ArrayList<SelectOption>();

        for (Map.Entry<Integer, String> e : map.entrySet()) {
            SelectOption option = new SelectOption();
            option.setOptionValue(e.getKey());
            option.setOptionDisplay(e.getValue());
            ds.add(option);
        }

        return ds;
    }
}

