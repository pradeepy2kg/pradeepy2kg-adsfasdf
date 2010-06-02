package lk.rgd.crs.web.action;


import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.Person;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.EPopDate;
import lk.rgd.crs.web.model.*;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Country;
import org.apache.struts2.interceptor.SessionAware;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;


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

    private Map<Integer, String> districtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> divisionList;

    private String scopeKey;
    private Map session;

    private List<Person> myList;

    private ChildInfo child;
    private ParentInfo parent;
    private GrandFatherInfo grandFather;
    private MarriageInfo marriage;
    private InformantInfo informant;
    private NotifyingAuthorityInfo notifyingAuthority;

    /*pageNo is used to decide the current pageNo of the Birth Registration Form*/
    private int pageNo;

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    private int birthDistrict;
    private int birthDivision;
    private String childDOB;
    private String motherDOB;
    private String fatherDOB;
  	private String motherAdmissionDate;
    private String dateOfRegistration;
    private String dateOfMarriage;
    private int fatherCountry;
    private int motherCountry;
    private String informantSignDate;

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
                case 0:
                    initForm();
                    break;
                case 1:
                    beanMerge(child);
                    break;
                case 2:
                    beanMerge(parent);
                    break;
                case 3:
                    beanMerge(marriage);
                    beanMerge(grandFather);
                    beanMerge(informant);
                    break;
                case 4:
                    BirthDeclaration register = beanMerge(notifyingAuthority);
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
        BeanInfo targetInfo = Introspector.getBeanInfo(BirthDeclaration.class);
        PropertyDescriptor[] targetDescriptors = targetInfo.getPropertyDescriptors();

        // Iterate over all the attributes of form bean and copy them into the target
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            Object newValue = descriptor.getReadMethod().invoke(o);
            //logger.debug("processing : {}, value is : {}", descriptor.getReadMethod(), newValue);

            Method beanMethod = descriptor.getWriteMethod();
            if (beanMethod != null) {
                String methodName = descriptor.getWriteMethod().getName();
                for (PropertyDescriptor targetDescriptor : targetDescriptors) {
                    Method targetMethod = targetDescriptor.getWriteMethod();
                    if (targetMethod == null) {
                        continue;
                    }
                    //logger.debug("looking for a match with target method {}", targetMethod);
                    if (methodName.equals(targetMethod.getName())) {
                        targetMethod.invoke(target, newValue);
                        logger.debug("field merged ");
                        break;
                    }
                }
            }
        }

        session.put(WebConstants.SESSION_BIRTH_REGISTER_BEAN, target);

        return target;
    }

    private void handleErrors(Exception e) {
        logger.error("Handle Error {} : {}", e.getMessage(), e);
        //todo pass the error to the error.jsp page
    }


    /**
     * initialises the birthRegister bean with proper initial values (depending on user, date etc) and
     * store it in session
     */
    private void initForm() {
        BirthDeclaration birthRegister = new BirthDeclaration();
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
        //birthRegister = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_REGISTER_BEAN);
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
        logger.debug("Child DOB String: {}", childDOB);
//        logger.debug("Child DOB Date: {}", EPopDate.getDate(childDOB));
        child.setDateOfBirth(EPopDate.getDate(childDOB));
        logger.debug("ChildDOB in ChildInfo : {}", child.getDateOfBirth());
    }

    public String getMotherDOB() {
        return motherDOB;
    }

    public void setMotherDOB(String motherDOB) {
        if (parent != null) {
            this.motherDOB = motherDOB;
            logger.debug("Mother DOB String: {}", motherDOB);
            logger.debug("Mother DOB Date: {}", EPopDate.getDate(motherDOB));
            parent.setMotherDOB(EPopDate.getDate(motherDOB));
            logger.debug("Mother DOB in Parent: {}", parent.getMotherDOB());
            logger.error("Mother DOB in Parent: {}", parent.getMotherDOB());
        }
    }

    public String getFatherDOB() {
        return fatherDOB;
    }

    public void setFatherDOB(String fatherDOB) {
        if (parent != null) {
            this.fatherDOB = fatherDOB;
            logger.debug("Father DOB String: {} {}", fatherDOB, parent);
            logger.debug("Father DOB Date: {}", EPopDate.getDate(fatherDOB));
            parent.setFatherDOB(EPopDate.getDate(fatherDOB));
            //logger.debug("Father DOB in Parent: {}", parent.getFatherDOB());
            //logger.error("Father DOB in Parent: {}", parent.getFatherDOB());
        }
    }

    public Map<Integer, String> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(Map<Integer, String> divisionList) {
        this.divisionList = divisionList;
    }

    public ChildInfo getChild() {
        return child;
    }

    public void setChild(ChildInfo child) {
        this.child = child;
    }

    public ParentInfo getParent() {
        return parent;
    }

    public void setParent(ParentInfo parent) {
        this.parent = parent;
    }

    public NotifyingAuthorityInfo getNotifyingAuthority() {
        return notifyingAuthority;
    }

    public void setNotifyingAuthority(NotifyingAuthorityInfo notifyingAuthority) {
        this.notifyingAuthority = notifyingAuthority;
    }

    public String getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(String dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
        child.setDateOfRegistration(EPopDate.getDate(dateOfRegistration));
        logger.debug("Child Registration in Child: {}", child.getDateOfRegistration());
    }

    public String getMotherAdmissionDate() {
        return motherAdmissionDate;
    }

    public void setMotherAdmissionDate(String motherAdmissionDate) {
        if (parent != null) {
            this.motherAdmissionDate = motherAdmissionDate;
            logger.debug("Mother Admission String: {}", motherAdmissionDate);
            logger.debug("Mother Admission Date: {}", EPopDate.getDate(motherAdmissionDate));
            parent.setMotherAdmissionDate(EPopDate.getDate(motherAdmissionDate));
            logger.debug("Mother Admission in Parent: {}", parent.getMotherAdmissionDate());
            logger.error("Mother Admission in Parent: {}", parent.getMotherAdmissionDate());
        }
    }

    public String getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(String dateOfMarriage) {
        if (marriage != null) {
            this.dateOfMarriage = dateOfMarriage;
            marriage.setDateOfMarriage(EPopDate.getDate(dateOfMarriage));
            logger.debug("DateOfMarriage : {}", marriage.getDateOfMarriage());
        }
    }

    public int getBirthDistrict() {
        return birthDistrict;
    }

    public void setBirthDistrict(int birthDistrict) {
        this.birthDistrict = birthDistrict;
        child.setBirthDistrict(districtDAO.getDistrict(birthDistrict));
    }

    public int getBirthDivision() {
        return birthDivision;
    }

    public void setBirthDivision(int birthDivision) {
        this.birthDivision = birthDivision;
        child.setBirthDivision(bdDivisionDAO.getBDDivision(birthDistrict, birthDivision));
    }

    public int getFatherCountry() {
        return fatherCountry;
    }

    public void setFatherCountry(int fatherCountry) {
        if (parent != null) {
            this.fatherCountry = fatherCountry;
            //TODO use countryDAO to get Country when id given
            parent.setFatherCountry(new Country());
        }
    }

    public int getMotherCountry() {
        return motherCountry;
    }

    public void setMotherCountry(int motherCountry) {
        if (parent != null) {
            this.motherCountry = motherCountry;
            //TODO use countryDAO to get Country when id given
            parent.setMotherCountry(countryDAO.getCountry(motherCountry));
        }
    }

    public MarriageInfo getMarriage() {
        return marriage;
    }

    public void setMarriage(MarriageInfo marriage) {
        this.marriage = marriage;
    }

    public InformantInfo getInformant() {
        return informant;
    }

    public void setInformant(InformantInfo informant) {
        this.informant = informant;
    }

    public String getInformantSignDate() {
        return informantSignDate;
    }

    public void setInformantSignDate(String informantSignDate) {
        if (informant != null) {
            this.informantSignDate = informantSignDate;
            informant.setInformantSignDate(EPopDate.getDate(informantSignDate));
        }
    }

    public GrandFatherInfo getGrandFather() {
        return grandFather;
    }

    public void setGrandFather(GrandFatherInfo grandFather) {
        this.grandFather = grandFather;
    }
}