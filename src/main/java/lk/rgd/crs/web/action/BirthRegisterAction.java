package lk.rgd.crs.web.action;


import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.Person;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.dao.DistrictDAO;
import lk.rgd.crs.api.dao.CountryDAO;
import lk.rgd.crs.api.dao.RaceDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.model.ChildInfo;
import lk.rgd.crs.web.model.ParentInfo;
import lk.rgd.crs.web.model.OtherInfo;
import lk.rgd.crs.web.model.NotifyingAuthorityInfo;
import lk.rgd.crs.web.util.EPopDate;
import lk.rgd.common.api.domain.User;
import org.apache.struts2.interceptor.SessionAware;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Locale;
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
    private final BirthRegistrationService service;
    private final DistrictDAO districtDAO;
    private final CountryDAO countryDAO;
    private final RaceDAO raceDAO;
    private final BDDivisionDAO bdDivisionDAO;

    private BirthDeclaration birthRegister;
    private ChildInfo child;
    private ParentInfo parent;
    private OtherInfo other;
    private NotifyingAuthorityInfo notifyingAuthority;

    /*pageNo is used to decide the current pageNo of the Birth Registration Form*/
    private int pageNo;
    private List<Person> myList;
    private Map session;

    private String scopeKey;
    private Map<Integer, String> districtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> divisionList;
    private String childDOB;
    private String motherDOB;
    private String fatherDOB;

    public String welcome() {
        return "success";
    }

    public BirthRegisterAction(BirthRegistrationService service, DistrictDAO districtDAO, CountryDAO countryDAO, RaceDAO raceDAO, BDDivisionDAO bdDivisionDAO) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.bdDivisionDAO = bdDivisionDAO;
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
        if ((pageNo > 4) || (pageNo < 0)) {
            return "error";
        }

        populate();
        try {
            switch (pageNo) {
                case 0: initForm(); break;
                /*case 1: beanMerge(child); break;
                case 2: beanMerge(parent); break;
                case 3: beanMerge(other); break;                                            */
                case 4: BirthDeclaration register = beanMerge(notifyingAuthority);
                        // all pages captured, proceed to persist after validations
                        // todo business validations and persiatance
                        logger.debug("Birth Register : {},{}", register.getChildFullNameEnglish(), register.getFatherFullName());
                        logger.debug("Birth Register : {}.", register.getMotherFullName());
                        return "success";
            }
        } catch (Exception e) {
            handleErrors(e);
            return "error";
        }

        return "form" + pageNo;
    }

    /**
     * update the bean in session with the values of local bean
     */
    private BirthDeclaration beanMerge(Object o) throws Exception {
        BirthDeclaration target = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_REGISTER_BEAN);
        BeanInfo beanInfo = Introspector.getBeanInfo(o.getClass());

        // Iterate over all the attributes of form bean and copy them into the target
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            Object newValue = descriptor.getReadMethod().invoke(o);
            logger.debug("processing : {}, value is : {}", descriptor.getReadMethod(), newValue);

            if (descriptor.getWriteMethod() != null) {
                logger.debug("field merged");
                descriptor.getWriteMethod().invoke(target, newValue);
            } else {
                logger.debug("field not merged");
            }
        }

        session.put(WebConstants.SESSION_BIRTH_REGISTER_BEAN, target);

        return target;
    }

    private void handleErrors(Exception e) {
        logger.error("{} : {}", e.getMessage(), e.toString());
        //todo pass the error to the error.jsp page
    }


    /**
     * initialises the birthRegister bean with proper initial values (depending on user, date etc) and
     * store it in session
     */
    private void initForm() {
        birthRegister = new BirthDeclaration();
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
        birthRegister = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_REGISTER_BEAN);
        //birthRegister.setChildDOB(new EPopDate().getDate(childDOB));
        return "success";
    }

    /**
     * Populate master data to the UIs
     */
    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("inside populate : {} observed.", language);

        districtList = districtDAO.getDistricts(language, user);
        countryList = countryDAO.getCountries(language);
        raceList = raceDAO.getRaces(language);
        // TODO division hard coded for the moment
        divisionList = bdDivisionDAO.getDivisions(language, 11, user);

        logger.debug("inside populte : districts , countries and races populated.");
    }

    public BirthDeclaration getBirthRegister() {
        return birthRegister;
    }

    public void setBirthRegister(BirthDeclaration birthRegister) {
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

    public void setModel(BirthDeclaration o) {
        birthRegister = o;
    }

    public BirthDeclaration getModel() {
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

    public Map<Integer, String> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(Map<Integer, String> divisionList) {
        this.divisionList = divisionList;
    }
}