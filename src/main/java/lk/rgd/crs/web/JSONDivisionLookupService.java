package lk.rgd.crs.web;

import lk.rgd.AppConstants;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.LocaleUtil;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.GNDivisionDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;
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
    private MRDivisionDAO mrDivisionDAO;
    private GNDivisionDAO gnDivisionDAO;
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private HospitalDAO hospitalDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        dsDivisionDAO = (DSDivisionDAO) context.getBean("dsDivisionDAOImpl");
        bdDivisionDAO = (BDDivisionDAO) context.getBean("bdDivisionDAOImpl");
        districtDAO = (DistrictDAO) context.getBean("districtDAOImpl");
        mrDivisionDAO = (MRDivisionDAO) context.getBean("mrDivisionDAOImpl");
        userDAO = (UserDAO) context.getBean("userDAOImpl");
        roleDAO = (RoleDAO) context.getBean("roleDAOImpl");
        gnDivisionDAO = (GNDivisionDAO) context.getBean("gnDivisionDAOImpl");
        hospitalDAO = (HospitalDAO) context.getBean("hospitalDAOImpl");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean withAll = false;
        String id = request.getParameter(WebConstants.DIVISION_ID);
        String mode = request.getParameter(WebConstants.MODE);
        String withAllOption = request.getParameter(WebConstants.WITHALL);
        if (withAllOption != null && !withAllOption.isEmpty()) {
            withAll = Boolean.parseBoolean(withAllOption);
            logger.debug("Received with All Option : {}", withAllOption);
        }
        logger.debug("Received Division Id and mode : {} {}", id, mode);
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
                //TODO: to be removed safely. generalized 9
                // passing dsDivisionId, return the BD list
                optionLists.put("bdDivisionList", getBDDivisions(lang, divisionId, user));
                optionLists.put("gnDivisionList", getGNDivisions(lang, divisionId, user));
            } else if ("3".equals(mode)) {
                // passing districtId, return all DSDivision list.
                optionLists.put("dsDivisionList", getAllDSDivisions(lang, divisionId, user));
            } else if ("4".equals(mode)) {
                optionLists.put("districtList", getAllDisList(lang, user));
            } else if ("5".equals(mode)) { //TODO: tobe removed - generalized 16
                // passing districtId, return DS List and the BD List for the 1st DS division
                List ds = getAllDSDivisions(lang, divisionId, user);
                int dsDivisionId = Integer.parseInt(((SelectOption) ds.get(0)).getOptionValue());
                List bd = getBDDivisions(lang, dsDivisionId, user);
                optionLists.put("dsDivisionList", ds);
                optionLists.put("bdDivisionList", bd);
            } else if ("6".equals(mode)) { //TODO: tobe removed - generalized 17
                //passing district list and return ds division list and mr division list for 1st ds division.
                List ds = getAllDSDivisions(lang, divisionId, user);
                int dsDivisionId = Integer.parseInt(((SelectOption) ds.get(0)).getOptionValue());
                List bd = getMRDivision(lang, dsDivisionId, user);
                optionLists.put("dsDivisionList", ds);
                optionLists.put("bdDivisionList", bd);

            } else if ("7".equals(mode)) {
                //TODO: to be removed safely - generalized 10
                // passing dsDivisionId, return the MR list
                optionLists.put("mrDivisionList", getMRDivision(lang, divisionId, user));
            } else if ("8".equals(mode)) { //TODO : tobe removed
                // passing district id and , return the MR list and DS list
                List ds = getDSDivisions(lang, divisionId, user);
                int dsDivisionId = Integer.parseInt(((SelectOption) ds.get(0)).getOptionValue());
                List mr = getMRDivision(lang, dsDivisionId, user);
                optionLists.put("dsDivisionList", ds);
                optionLists.put("mrDivisionList", mr);
            } else if ("9".equals(mode)) { // used for generalized dynamic list population in division.js
                //return the BD list
                optionLists.put("divisionList", getBDDivisionList(lang, divisionId, user, withAll));
            } else if ("10".equals(mode)) {// used for generalized dynamic list population in division.js
                //return the MR list
                optionLists.put("divisionList", getMRDivisionList(lang, divisionId, user, withAll));
            } else if ("11".equals(mode)) {// used for generalized dynamic list population in division.js
                //return DS List and the BD List for the 1st DS division
                List ds = getDSDivisionList(lang, divisionId, user, withAll);
                int dsDivisionId = Integer.parseInt(((SelectOption) ds.get(0)).getOptionValue());
                List bd = getBDDivisionList(lang, dsDivisionId, user, withAll);
                optionLists.put("dsDivisionList", ds);
                optionLists.put("divisionList", bd);
            } else if ("12".equals(mode)) {// used for generalized dynamic list population in division.js
                //return the MR list and DS list
                List ds = getDSDivisionList(lang, divisionId, user, withAll);
                int dsDivisionId = Integer.parseInt(((SelectOption) ds.get(0)).getOptionValue());
                List mr = getMRDivisionList(lang, dsDivisionId, user, withAll);
                optionLists.put("dsDivisionList", ds);
                optionLists.put("divisionList", mr);

            } else if ("13".equals(mode)) {
                List ds = getAllDSDivisions(lang, divisionId, user);
                logger.debug("DSDivision List size : {}", ds.size());
                int dsDivisionId = Integer.parseInt(((SelectOption) ds.get(0)).getOptionValue());
                List deoList = getList(
                    userDAO.getDEOsByDSDivision(
                        user.getPrefLanguage(),
                        user,
                        dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                        roleDAO.getRole("DEO")
                    )
                );
                optionLists.put("dsDivisionList", ds);
                optionLists.put("deoList", deoList);

            } else if ("14".equals(mode)) {
                int dsDivisionId = Integer.parseInt(id);
                List deoList = getList(
                    userDAO.getDEOsByDSDivision(
                        user.getPrefLanguage(),
                        user,
                        dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                        roleDAO.getRole("DEO")
                    )
                );
                optionLists.put("deoList", deoList);
            } else if ("15".equals(mode)) {
                List ds = getAllDSDivisions(lang, divisionId, user);
                List adrList = getList(
                    userDAO.getADRsByDistrictId(
                        districtDAO.getDistrict(divisionId), roleDAO.getRole("ADR")
                    )
                );
                optionLists.put("dsDivisionList", ds);
                optionLists.put("adrList", adrList);
            } else if ("16".equals(mode)) { // used for generalized dynamic list population in division.js
                //return DS List and the BD List
                List ds = getAllDSDivisionList(lang, divisionId, user, withAll);
                int dsDivisionId = Integer.parseInt(((SelectOption) ds.get(0)).getOptionValue());
                List bd = getBDDivisionList(lang, dsDivisionId, user, withAll);
                List gn = getGNDivisions(lang, dsDivisionId, user);
                optionLists.put("dsDivisionList", ds);
                optionLists.put("divisionList", bd);
                optionLists.put("gnDivisionList", gn);
            } else if ("17".equals(mode)) { // used for generalized dynamic list population in division.js
                //return ds division list and mr division list
                List ds = getAllDSDivisionList(lang, divisionId, user, withAll);
                int dsDivisionId = Integer.parseInt(((SelectOption) ds.get(0)).getOptionValue());
                List bd = getMRDivisionList(lang, dsDivisionId, user, withAll);
                optionLists.put("dsDivisionList", ds);
                optionLists.put("divisionList", bd);
            } else if ("18".equals(mode)) {
                List gn = getGNDivisions(lang, divisionId, user);
                optionLists.put("gnDivisionList", gn);
            }else if ("19".equals(mode)){
                List districts = getDistrictsByProvince(lang, divisionId, user);
                optionLists.put("districtList", districts);

            }else if("20".equals(mode)){
                List hospitals = getHospitalsbyDSDivision(lang, divisionId,user);
                optionLists.put("hospitalList", hospitals);
            /*}else if("21".equals(mode)){
                List hospitals = getHospitalsbyBdDivision(lang, divisionId,user);
                optionLists.put("hospitalList", hospitals);*/
            }else if("22".equals(mode)){
                List hospitals = getHospitalsbyDistrict(lang, divisionId,user);
                optionLists.put("hospitalList", hospitals);
            } else {
                // passing districtId, return DS List and the BD List for the 1st DS division
                List ds = getDSDivisions(lang, divisionId, user);
                int dsDivisionId = Integer.parseInt(((SelectOption) ds.get(0)).getOptionValue());
                List bd = getBDDivisions(lang, dsDivisionId, user);
                List gn = getGNDivisions(lang, dsDivisionId, user);
                List hospitals = getHospitals(lang,dsDivisionId, user);
                optionLists.put("dsDivisionList", ds);
                optionLists.put("bdDivisionList", bd);
                optionLists.put("gnDivisionList", gn);
                optionLists.put("hospitalList", hospitals);
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

    private List getHospitalsbyDistrict(String language, int districtId, User user) {
        Map<Integer, String> hospitalList = hospitalDAO.getHospitalsbyDistrict(language,districtId, user);
        logger.debug("Loaded hospital list : {}", hospitalList);
        return getList(hospitalList);
    }

    private List getHospitalsbyDSDivision(String language, int dsDivisionId, User user) {
        Map<Integer, String> hospitalList = hospitalDAO.getHospitalsbyDSDivision(language, dsDivisionId, user);
        logger.debug("Loaded hospital list : {}", hospitalList);
        return getList(hospitalList);
    }

    private List getHospitals(String language, int dsDivisionId, User user) {
             Map<Integer, String> hospitaList = null;
        try {
            hospitaList = hospitalDAO.getHospitalsbyDSDivision(language, dsDivisionId, user);
            logger.debug("Loaded GN list : {}", hospitaList);
        } catch (RGDRuntimeException e) {
            hospitaList = Collections.emptyMap();
        }
        return getList(hospitaList);

    }

 /*   private List getHospitalsbyBdDivision(String language, int bdDivisionId, User user) {
        Map<Integer, String> hospitalList = hospitalDAO.getHospitalsNamesbyBdDivision(bdDivisionId, language);
        logger.debug("Loaded hospital list : {}", hospitalList);
        return getList(hospitalList);
    }*/

    private List getDistrictsByProvince(String language, int provinceUKey, User user){
        Map<Integer, String> districtList = districtDAO.getDistrictNamesByProvince(language, provinceUKey, user);
        logger.debug("Loaded District list size: {}", districtList.size());
        return getList(districtList);
    }
    //TODO : tobe removed

    private List getBDDivisions(String language, int dsDivisionId, User user) {
        Map<Integer, String> bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        logger.debug("Loaded BD list : {}", bdDivisionList);

        return getList(bdDivisionList);
    }

    private List getGNDivisions(String language, int dsDivisionId, User user) {
        Map<Integer, String> gnDivisionList = null;
        try {
            gnDivisionList = gnDivisionDAO.getGNDivisionNames(dsDivisionId, language, user);
            logger.debug("Loaded GN list : {}", gnDivisionList);
        } catch (RGDRuntimeException e) {
            gnDivisionList = Collections.emptyMap();
        }
        return getList(gnDivisionList);
    }

    //TODO : tobe removed

    private List getMRDivision(String language, int dsDivision, User user) {
        Map<Integer, String> bdDivisionList = mrDivisionDAO.getMRDivisionNames(dsDivision, language, user);
        logger.debug("Loaded MR list : {}", bdDivisionList);

        return getList(bdDivisionList);
    }

    //todo:  to be removed

    private List getAllDSDivisions(String language, int BDId, User user) {
        Map<Integer, String> dsDivisionList = dsDivisionDAO.getAllDSDivisionNames(BDId, language, user);
        logger.debug("Loaded DS list : {}", dsDivisionList);

        return getList(dsDivisionList);
    }

    private List getAllDisList(String language, User user) {
        Map<Integer, String> districtList = districtDAO.getAllDistrictNames(language, user);
        return getList(districtList);
    }

    //TODO : tobe removed

    private List getDSDivisions(String language, int BDId, User user) {
        Map<Integer, String> dsDivisionList = dsDivisionDAO.getDSDivisionNames(BDId, language, user);
        logger.debug("Loaded DS list : {}", dsDivisionList);

        return getList(dsDivisionList);
    }

    private List getAllDSDivisionList(String language, int districtId, User user, boolean withAll) {
        if (withAll & districtId == 0) {
            return createListWithAllOption(language);
        }
        Map<Integer, String> dsDivisionList = dsDivisionDAO.getAllDSDivisionNames(districtId, language, user);
        logger.debug("Loaded All DS list : {}", dsDivisionList);

        return getList(dsDivisionList);
    }

    private List getBDDivisionList(String language, int dsDivisionId, User user, boolean withAll) {
        if (withAll & dsDivisionId == 0) {
            return createListWithAllOption(language);
        }
        Map<Integer, String> bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        logger.debug("Loaded BD list : {}", bdDivisionList);

        return getList(bdDivisionList, language, withAll);
    }

    private List getMRDivisionList(String language, int dsDivisionId, User user, boolean withAll) {
        if (withAll & dsDivisionId == 0) {
            return createListWithAllOption(language);
        }
        Map<Integer, String> bdDivisionList = mrDivisionDAO.getMRDivisionNames(dsDivisionId, language, user);
        logger.debug("Loaded MR list : {}", bdDivisionList);

        return getList(bdDivisionList, language, withAll);
    }

    private List getDSDivisionList(String language, int districtId, User user, boolean withAll) {
        if (withAll & districtId == 0) {
            return createListWithAllOption(language);
        }
        Map<Integer, String> dsDivisionList = dsDivisionDAO.getDSDivisionNames(districtId, language, user);
        logger.debug("Loaded DS list : {}", dsDivisionList);

        return getList(dsDivisionList, language, withAll);
    }

    //TODO : tobe removed

    private List getList(Map<Integer, String> map) {
        List<SelectOption> ds = new ArrayList<SelectOption>();

        for (Map.Entry<Integer, String> e : map.entrySet()) {
            SelectOption option = new SelectOption();
            option.setOptionValue(e.getKey().toString());
            option.setOptionDisplay(e.getValue());
            ds.add(option);
        }
        return ds;
    }

    private List<SelectOption> createListWithAllOption(String language) {
        List<SelectOption> ds = new ArrayList<SelectOption>();
        SelectOption headerOption = new SelectOption();
        headerOption.setOptionValue("0");
        headerOption.setOptionDisplay(LocaleUtil.getLocalizedString(language, AppConstants.ALL));
        ds.add(headerOption);
        return ds;
    }

    private List getList(Map<Integer, String> map, String language, boolean withAll) {

        List<SelectOption> ds;

        if (withAll) {
            ds = createListWithAllOption(language);
        } else {
            ds = new ArrayList<SelectOption>();
        }
        for (Map.Entry<Integer, String> e : map.entrySet()) {
            SelectOption option = new SelectOption();
            option.setOptionValue(e.getKey().toString());
            option.setOptionDisplay(e.getValue());
            ds.add(option);
        }
        return ds;
    }

    private List getList(List<String> l) {
        List<SelectOption> ds = new ArrayList<SelectOption>();
        Iterator<String> i = l.iterator();
        while (i.hasNext()) {
            String s = i.next();
            SelectOption option = new SelectOption();
            option.setOptionValue(s);
            option.setOptionDisplay(s);
            ds.add(option);
        }
        return ds;
    }
}

