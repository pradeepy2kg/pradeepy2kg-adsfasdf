package lk.rgd.crs.web.action;


import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.crs.api.domain.BirthRegister;
import lk.rgd.crs.api.domain.Person;
import lk.rgd.crs.api.service.BirthRegisterService;
import lk.rgd.crs.api.dao.DistrictDAO;
import lk.rgd.crs.api.dao.CountryDAO;
import lk.rgd.crs.api.dao.RaceDAO;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.EPopDate;
import org.apache.struts2.interceptor.SessionAware;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;


/**
 * EntryAction is a struts action class
 *
 * @author indunil Moremada
 * @author Chathranga
 * @author Amith
 * @author Duminda
 */

public class BirthRegisterAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);
    private final BirthRegisterService service;
    private final DistrictDAO districtDAO;
    private final CountryDAO countryDAO;
    private final RaceDAO raceDAO;

    private BirthRegister birthRegister;

    /*pageNo is used to decide the current pageNo of the Birth Registration Form*/
    private int pageNo;
    private String childDOB;
    private String motherDOB;
    private String fatherDOB;
    private List<Person> myList;
    private Map session;

    private String scopeKey;
    private Map<Integer, String> districtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> raceList;

    public String welcome() {
        return "success";
    }

    public BirthRegisterAction(BirthRegisterService service, DistrictDAO districtDAO, CountryDAO countryDAO, RaceDAO raceDAO) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        logger.debug("inside birth register action constructor");
    }

    /**
     * This method is responsible for loading and capture data for all 4 BDF pages as well
     * as their persistance. pageNo hidden variable which is passed to the action (empty=0 for the
     * very first form page) is used to decide which state of the process we are in. at the last step
     * only we do a persistance, until then all data will be in the session. This is a design decision
     * to limit DB writes. Masterdata population will be done before displaying every page.
     * This will have no performace impact as they will be cached in the backend.
     */
    public String birthRegistration() {
        logger.debug("Step {} of 4 ", pageNo);
        if (pageNo > 4) {
            return "error";
        } else if (pageNo == 4) {
            // all pages captured, proceed to persist after validations
            BirthRegister register = (BirthRegister) session.get("birthRegister");
            // todo business validations and persiatance
            logger.debug("Birth Register : {},{}", register.getChildFullNameEnglish(), register.getFatherFullName());
            logger.debug("Birth Register : {}.", register.getMotherFullName());
            return "success";
        }

        populate();
        if (pageNo == 0) {
            initForm();
        } else {
            if (pageNo == 1) {
                birthRegister.setChildDOB(new EPopDate().getDate(childDOB));
            } else if (pageNo == 2) {
                birthRegister.setFatherDOB(new EPopDate().getDate(fatherDOB));
                birthRegister.setMotherDOB(new EPopDate().getDate(motherDOB));
            }
        }

            // submissions of pages 1 - 4
            try {
                beanMerge();
            } catch (Exception e) {
                handleErrors(e);
                return "error";
            }
        }

        session.put("page_title", "birth registration form : "+ (pageNo+1));
        return "form" + pageNo;
    }

    private void handleErrors(Exception e) {
        logger.error(e.getLocalizedMessage());
        //todo pass the error to the error.jsp page
    }

    /**
     *  initialises the birthRegister bean with proper initial values (depending on user, date etc) and
     * store it in session
     */
    private void initForm() {
        birthRegister = new BirthRegister();
        //todo set fields to proper initial values based on user and date
        session.put(WebConstants.SESSION_BIRTH_REGISTER_BEAN, birthRegister);
    }

    /**
     * birthRegisterFinalizer is called by the fourth jsp page of the Birth
     * Registration Form it finalize the birthRegister Entity bean and send
     * it to the Business delegate
     *
     * @return String
     */
    public String birthRegisterFinalizer() {
        birthRegister = (BirthRegister) session.get(WebConstants.SESSION_BIRTH_REGISTER_BEAN);
        //birthRegister.setChildDOB(new EPopDate().getDate(childDOB));
        return "success";
    }

    /**
     * Populate master data to the UIs
     */
    private void populate() {
        String language = (String) (session.get(WebConstants.SESSION_USER_LANG));
        logger.debug("inside populate : {} observed.", language);

        districtList = districtDAO.getDistricts(language);
        countryList = countryDAO.getCountries(language);
        raceList = raceDAO.getRaces(language);

        //todo temporary solution until use a method to show Map in UI
        session.put("districtList", districtList);
        session.put("countryList", countryList);
        session.put("raceList", raceList);

        logger.debug("inside populte : districts , countries and races populated.");
    }

    public BirthRegister getBirthRegister() {
        return birthRegister;
    }

    public void setBirthRegister(BirthRegister birthRegister) {
        this.birthRegister = birthRegister;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<Person> getMyList() {
        return myList;
    }

    public void setMyList(List<Person> myList) {
        this.myList = myList;
    }

    public void setSession(Map map) {
        this.session = map;
    }

    public Map getSession() {
        return session;
    }

    public void setModel(BirthRegister o) {
        birthRegister = o;
    }

    public BirthRegister getModel() {
        return birthRegister;
    }

    public void setScopeKey(String s) {
        this.scopeKey = s;
    }

    public String getScopeKey() {
        return scopeKey;
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public Map<Integer, String> getCountryList() {
        return countryList;
    }

    public void setCountryList(Map<Integer, String> countryList) {
        this.countryList = countryList;
    }

    public Map<Integer, String> getRaceList() {
        return raceList;
    }

    public void setRaceList(Map<Integer, String> raceList) {
        this.raceList = raceList;
    }

    public String getChildDOB() {
        return childDOB;
    }

    public void setChildDOB(String childDOB) {
        this.childDOB = childDOB;
    }

    public String getMotherDOB() {
        return motherDOB;
    }

    public void setMotherDOB(String motherDOB) {
        this.motherDOB = motherDOB;
    }

    public String getFatherDOB() {
        return fatherDOB;
    }

    public void setFatherDOB(String fatherDOB) {
        this.fatherDOB = fatherDOB;
    }
}