package lk.rgd.crs.web.action;


import com.opensymphony.xwork2.ActionSupport;
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

import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.User;

import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;

import lk.rgd.crs.web.WebConstants;

/**
 * EntryAction is a struts action class  responsible for  data capture for a birth declaration and the persistance of the same.
 * Data capture forms (4) will be kept in session until persistance at the end of 4th page.
 */
public class BirthRegisterAction extends ActionSupport implements SessionAware {
    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);

    private final BirthRegistrationService service;
    private final DistrictDAO districtDAO;
    private final CountryDAO countryDAO;
    private final RaceDAO raceDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;

    private Map<Integer, String> districtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> dsdivisionList;
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

    private int pageNo; //pageNo is used to decide the current pageNo of the Birth Registration Form

    private int bdId;   // If present, it should used to fetch a new BD instead of creating a new one (we are in edit mode)

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    // TODO country,district,division not populating
    //private int birthDistrict;
    //private int birthDivision;
    private int fatherCountry;
    private int motherCountry;

    public String welcome() {
        return "success";
    }

    public BirthRegisterAction(BirthRegistrationService service, DistrictDAO districtDAO, CountryDAO countryDAO, RaceDAO raceDAO, BDDivisionDAO bdDivisionDAO, DSDivisionDAO dsDivisionDAO) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
    }

    /**
     * This method is responsible for loading and capture data for all 4 BDF pages as well
     * as their persistance. pageNo hidden variable which is passed to the action (empty=0 for the
     * very first form page) is used to decide which state of the process we are in. at the last step
     * only we do a persistance, until then all data will be in the session. This is a design decision
     * to limit DB writes. Masterdata population will be done before displaying every page.
     * This will have no performace impact as they will be cached in the backend.
     */
    public String birthDeclaration() {
        logger.debug("Step {} of 4 ", pageNo);
        if ((pageNo > 4) || (pageNo < 0)) {
            return "error";
        } else {
            BirthDeclaration bdf;
            if (pageNo == 0) {
                if (bdId == 0) {
                    bdf = new BirthDeclaration();
                } else {
                    bdf = new BirthDeclaration();
                    //todo replace with get a new BD by id
                    //bdf = BirthDeclarionDAO.getById(bdId); 
                }
            } else {
                bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
                switch (pageNo) {
                    case 1:
                        bdf.setChild(child);
                        if (logger.isDebugEnabled()) {
                            logger.debug("BRF 1: serial=" + bdf.getChild().getBdfSerialNo() + ",submitDate=" + bdf.getChild().getDateOfRegistration() + ",dob=" + bdf.getChild().getDateOfBirth() + ",district=******" +
                                    ",division=******" + ",hospCode=" + bdf.getChild().getHospitalCode() + ",gnCode=" + bdf.getChild().getGnCode() + ",name=" + bdf.getChild().getChildFullNameOfficialLang() + "," +
                                    bdf.getChild().getChildFullNameEnglish() + ",gender=" + bdf.getChild().getChildGender() + ",weight=" + bdf.getChild().getChildBirthWeight() + ",rank=" + bdf.getChild().getChildRank() + ",children=" + bdf.getChild().getNumberOfChildrenBorn());
                        }
                        break;
                    case 2:
                        bdf.setParent(parent);
                        if (logger.isDebugEnabled()) {
                            logger.debug("BRF 2: father- nic=" + bdf.getParent().getFatherNICorPIN() + ",country=" + bdf.getParent().getFatherCountry() + ",passport" + bdf.getParent().getFatherPassportNo() + ",name=" +
                                    bdf.getParent().getFatherFullName() + ",dob=" + bdf.getParent().getFatherDOB() + ",place=" + bdf.getParent().getFatherPlaceOfBirth() + ",race=" + bdf.getParent().getFatherRace());
                            logger.debug("BRF 2: mother- nic=" + bdf.getParent().getMotherNICorPIN() + ",country=" + bdf.getParent().getMotherCountry() + ",passport" + bdf.getParent().getMotherPassportNo() + ",name=" +
                                    bdf.getParent().getMotherFullName() + ",dob=" + bdf.getParent().getMotherDOB() + ",place=" + bdf.getParent().getMotherPlaceOfBirth() + ",race=" + bdf.getParent().getMotherRace() + ",age=" + bdf.getParent().getMotherAgeAtBirth() +
                                    ",address=" + bdf.getParent().getMotherAddress() + ",admitted=" + bdf.getParent().getMotherAdmissionNo() + "," + bdf.getParent().getMotherAdmissionDate() + ",tel=" + bdf.getParent().getMotherPhoneNo() + ",email=" + bdf.getParent().getMotherEmail());

                        }
                        break;
                    case 3:
                        bdf.setMarriage(marriage);
                        bdf.setGrandFather(grandFather);
                        bdf.setInformant(informant);
                        if (logger.isDebugEnabled()) {
                            logger.debug("BRF 3: married=" + bdf.getMarriage().getParentsMarried() + ", place=" + bdf.getMarriage().getPlaceOfMarriage() + ", date=" +
                                    bdf.getMarriage().getDateOfMarriage() + ", motherSign=" + bdf.getMarriage().isMotherSigned() + " ,fatherSign=" + bdf.getMarriage().isFatherSigned() +
                                    ", grandFather=" + bdf.getGrandFather().getGrandFatherFullName() + "," + bdf.getGrandFather().getGrandFatherBirthYear() + "," + bdf.getGrandFather().getGrandFatherBirthPlace() +
                                    ", GreatGrand=" + bdf.getGrandFather().getGreatGrandFatherFullName() + "," + bdf.getGrandFather().getGreatGrandFatherBirthYear() + "," + bdf.getGrandFather().getGreatGrandFatherBirthPlace() +
                                    ", InformantType" + bdf.getInformant().getInformantType() + ", InfoName=" + bdf.getInformant().getInformantName() + ", infoId=" + bdf.getInformant().getInformantNICorPIN() + ", infoAdd=" +
                                    bdf.getInformant().getInformantAddress() + ", infoTel=" + bdf.getInformant().getInformantPhoneNo() + ", infoEmail=" + bdf.getInformant().getInformantEmail() + " ,infoSingnDate=" + bdf.getInformant().getInformantSignDate());
                        }

                        break;
                    case 4:
                        bdf.setNotifyingAuthority(notifyingAuthority);
                        // all pages captured, proceed to persist after validations
                        // todo business validations and persiatance
                        logger.debug("Birth Register : {},{}", bdf.getChild().getChildFullNameEnglish(), bdf.getParent().getFatherFullName());
                        logger.debug("Birth Register : {}.", bdf.getParent().getMotherFullName());

                        if (logger.isDebugEnabled()) {
                            logger.debug("BRF 4: authName=" + bdf.getNotifyingAuthority().getNotifyingAuthorityPIN() + " ," + bdf.getNotifyingAuthority().getNotifyingAuthorityName() + " ," +
                                    bdf.getNotifyingAuthority().getNotifyingAuthorityAddress() + " ,signDate=" + bdf.getNotifyingAuthority().getNotifyingAuthoritySignDate());
                        }
                }
            }
            session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);
               
            populate();
            return "form" + pageNo;
        }
    }

    /**
     * This method is responsible for loading and capture data for all 3 BDC pages as well
     * as their persistance. pageNo hidden variable which is passed to the action (empty=0 for the
     * very first form page) is used to decide which state of the process we are in. bdId field should be used to
     * determoine the particular birth declarion entity on the initial visit to action. (after then it will be kept in the session)
     */
    public String birthConfirmation() {
        logger.debug("Step {} of 3 ", pageNo);
        if ((pageNo > 3) || (pageNo < 0)) {
            return "error";
        } else {
            BirthDeclaration bdf;
            if (pageNo == 0) {
                bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
                //todo replace with get a new BD by id
                //bdf = BirthDeclarionDAO.getById(bdId);
            } else {
                bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
                switch (pageNo) {
                    case 1:
                        bdf.setChild(child);
                        bdf.setParent(parent);
                        break;
                    case 2:
                        bdf.setChild(child);    //todo merge needed
                        bdf.setParent(parent);  //todo merge needed
                        bdf.setMarriage(marriage);
                        break;
                    case 3:
                        bdf.setInformant(informant);
                        break;
                }
            }
            session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);

            populate();
            return "form" + pageNo;
        }
    }

    /**
     * update the bean in session with the values of local bean
     */
    private BirthDeclaration beanMerge(Object o) throws Exception {
        BirthDeclaration target = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
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

        session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, target);

        return target;
    }

    private void handleErrors(Exception e) {
        logger.error("Handle Error {} : {}", e.getMessage(), e);
        //todo pass the error to the error.jsp page
    }

    /**
     * Populate master data to the UIs
     */
    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("inside populate : {} observed.", language);

        districtList = districtDAO.getDistricts(language, user);
        dsdivisionList = dsDivisionDAO.getDSDivisionNames(user.getPrefDistrict(), language);
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

//    TODO district and division retrieval should be done
//    public int getBirthDistrict() {
//        return birthDistrict;
//    }
//
//    public void setBirthDistrict(int birthDistrict) {
//        this.birthDistrict = birthDistrict;
//    }
//
//    public int getBirthDivision() {
//        return birthDivision;
//    }
//
//    public void setBirthDivision(int birthDivision) {
//        this.birthDivision = birthDivision;
//        child.setBirthDivision(bdDivisionDAO.getBDDivision(birthDistrict, birthDivision));
//    }

    public int getFatherCountry() {
        return fatherCountry;
    }

    public void setFatherCountry(int fatherCountry) {
        if (parent != null) {
            this.fatherCountry = fatherCountry;
            parent.setFatherCountry(countryDAO.getCountry(fatherCountry));
        }
    }

    public int getMotherCountry() {
        return motherCountry;
    }

    public void setMotherCountry(int motherCountry) {
        if (parent != null) {
            this.motherCountry = motherCountry;
            logger.debug("Mother Country:{}", motherCountry);
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

    public GrandFatherInfo getGrandFather() {
        return grandFather;
    }

    public void setGrandFather(GrandFatherInfo grandFather) {
        this.grandFather = grandFather;
    }

    public Map<Integer, String> getDsdivisionList() {
        return dsdivisionList;
    }

    public void setDsdivisionList(Map<Integer, String> dsdivisionList) {
        this.dsdivisionList = dsdivisionList;
    }
}