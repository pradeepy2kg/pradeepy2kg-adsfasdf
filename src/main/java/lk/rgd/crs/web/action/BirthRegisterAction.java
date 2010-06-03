package lk.rgd.crs.web.action;


import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Locale;

import lk.rgd.common.api.dao.*;
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
    private final GNDivisionDAO gnDivisionDAO;

    private Map<Integer, String> districtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> gnDivisionList;

    private String scopeKey;
    private Map session;

    private List<Person> myList;

    private ChildInfo child;
    private ParentInfo parent;
    private GrandFatherInfo grandFather;
    private MarriageInfo marriage;
    private InformantInfo informant;
    private NotifyingAuthorityInfo notifyingAuthority;
    private ConfirmantInfo confirmant;


    private int pageNo; //pageNo is used to decide the current pageNo of the Birth Registration Form

    private int bdId;   // If present, it should used to fetch a new BD instead of creating a new one (we are in edit mode)

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    // TODO country,district,division not populating
    private int birthDistrict;
    private int birthDivision;
    private int fatherCountry;
    private int motherCountry;
    private int dsDivision;
    private int gnDivision;


    public String welcome() {
        return "success";
    }

    public BirthRegisterAction(BirthRegistrationService service, DistrictDAO districtDAO, CountryDAO countryDAO, RaceDAO raceDAO, BDDivisionDAO bdDivisionDAO, DSDivisionDAO dsDivisionDAO, GNDivisionDAO gnDivisionDAO) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.gnDivisionDAO = gnDivisionDAO;
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
                        /*if (logger.isDebugEnabled()) {
                            logger.debug("BRF 1: serial=" + bdf.getChild().getBdfSerialNo() + ",submitDate=" + bdf.getChild().getDateOfRegistration() + ",dob=" + bdf.getChild().getDateOfBirth() + ",district=" + bdf.getChild().getBirthDistrict().getSiDistrictName() +
                                    ",DSDivision=" + bdf.getChild().getDsDivision().getSiDivisionName() + ",birthDivision=" + bdf.getChild().getBirthDivision().getSiDivisionName() + ",place=" + bdf.getChild().getPlaceOfBirth() + ",GNDivision=" + bdf.getChild().getGnDivision().getSiDivisionName() + ",name=" + bdf.getChild().getChildFullNameOfficialLang() + "," +
                                    bdf.getChild().getChildFullNameEnglish() + ",gender=" + bdf.getChild().getChildGender() + ",weight=" + bdf.getChild().getChildBirthWeight() + ",rank=" + bdf.getChild().getChildRank() + ",children=" + bdf.getChild().getNumberOfChildrenBorn());
                        }*/
                        break;
                    case 2:
                        bdf.setParent(parent);
                        /*if (logger.isDebugEnabled()) {
                            logger.debug("BRF 2: father- nic=" + bdf.getParent().getFatherNICorPIN() + ",country=" + bdf.getParent().getFatherCountry() + ",passport" + bdf.getParent().getFatherPassportNo() + ",name=" +
                                    bdf.getParent().getFatherFullName() + ",dob=" + bdf.getParent().getFatherDOB() + ",place=" + bdf.getParent().getFatherPlaceOfBirth() + ",race=" + bdf.getParent().getFatherRace());
                            logger.debug("BRF 2: mother- nic=" + bdf.getParent().getMotherNICorPIN() + ",country=" + bdf.getParent().getMotherCountry() + ",passport" + bdf.getParent().getMotherPassportNo() + ",name=" +
                                    bdf.getParent().getMotherFullName() + ",dob=" + bdf.getParent().getMotherDOB() + ",place=" + bdf.getParent().getMotherPlaceOfBirth() + ",race=" + bdf.getParent().getMotherRace() + ",age=" + bdf.getParent().getMotherAgeAtBirth() +
                                    ",address=" + bdf.getParent().getMotherAddress() + ",admitted=" + bdf.getParent().getMotherAdmissionNo() + "," + bdf.getParent().getMotherAdmissionDate() + ",tel=" + bdf.getParent().getMotherPhoneNo() + ",email=" + bdf.getParent().getMotherEmail());
                        }*/
                        break;
                    case 3:
                        bdf.setMarriage(marriage);
                        bdf.setGrandFather(grandFather);
                        bdf.setInformant(informant);
                        /*if (logger.isDebugEnabled()) {
                            logger.debug("BRF 3: married=" + bdf.getMarriage().getParentsMarried() + ", place=" + bdf.getMarriage().getPlaceOfMarriage() + ", date=" +
                                    bdf.getMarriage().getDateOfMarriage() + ", motherSign=" + bdf.getMarriage().isMotherSigned() + " ,fatherSign=" + bdf.getMarriage().isFatherSigned() +
                                    ", grandFather=" + bdf.getGrandFather().getGrandFatherFullName() + "," + bdf.getGrandFather().getGrandFatherBirthYear() + "," + bdf.getGrandFather().getGrandFatherBirthPlace() +
                                    ", GreatGrand=" + bdf.getGrandFather().getGreatGrandFatherFullName() + "," + bdf.getGrandFather().getGreatGrandFatherBirthYear() + "," + bdf.getGrandFather().getGreatGrandFatherBirthPlace() +
                                    ", InformantType" + bdf.getInformant().getInformantType() + ", InfoName=" + bdf.getInformant().getInformantName() + ", infoId=" + bdf.getInformant().getInformantNICorPIN() + ", infoAdd=" +
                                    bdf.getInformant().getInformantAddress() + ", infoTel=" + bdf.getInformant().getInformantPhoneNo() + ", infoEmail=" + bdf.getInformant().getInformantEmail() + " ,infoSingnDate=" + bdf.getInformant().getInformantSignDate());
                        }*/

                        break;
                    case 4:
                        bdf.setNotifyingAuthority(notifyingAuthority);
                        // all pages captured, proceed to persist after validations
                        // todo business validations and persiatance
                        logger.debug("Birth Register : {},{}", bdf.getChild().getChildFullNameEnglish(), bdf.getParent().getFatherFullName());
                        logger.debug("Birth Register : {}.", bdf.getParent().getMotherFullName());

                        /*if (logger.isDebugEnabled()) {
                            logger.debug("BRF 4: authName=" + bdf.getNotifyingAuthority().getNotifyingAuthorityPIN() + " ," + bdf.getNotifyingAuthority().getNotifyingAuthorityName() + " ," +
                                    bdf.getNotifyingAuthority().getNotifyingAuthorityAddress() + " ,signDate=" + bdf.getNotifyingAuthority().getNotifyingAuthoritySignDate());
                        }*/
                }
            }
            session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);

            populate();
            logger.debug("Birth Declaration: PageNo=" + pageNo);
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
                        bdf.getChild().setBdfSerialNo(child.getBdfSerialNo());
                        bdf.getChild().setDateOfRegistration(child.getDateOfRegistration());
                        bdf.getChild().setDateOfBirth(child.getDateOfBirth());
                        bdf.getChild().setChildGender(child.getChildGender());
                        bdf.getChild().setGnDivision(child.getGnDivision());
                        bdf.getChild().setBirthDivision(child.getBirthDivision());
                        bdf.getChild().setPlaceOfBirth(child.getPlaceOfBirth());

                        bdf.getParent().setFatherNICorPIN(parent.getFatherNICorPIN());
                        bdf.getParent().setFatherRace(parent.getFatherRace());
                        bdf.getParent().setMotherNICorPIN(parent.getMotherNICorPIN());
                        bdf.getParent().setMotherRace(parent.getMotherRace());

                        bdf.getMarriage().setParentsMarried(marriage.getParentsMarried());
                        break;
                    case 2:
                        bdf.getChild().setChildFullNameOfficialLang(child.getChildFullNameOfficialLang());
                        bdf.getChild().setChildFullNameEnglish(child.getChildFullNameEnglish());

                        bdf.getParent().setFatherFullName(parent.getFatherFullName());
                        bdf.getParent().setMotherFullName(parent.getMotherFullName());
                          //logger.debug("check parent name ,{},{}.",bdf.getParent().getFatherFullName());
                        break;
                    case 3:
                         logger.debug("Birth Confirmation Persist : {}", confirmant.getConfirmantSignDate());
                        bdf.getConfirmant().setConfirmantNICorPIN(confirmant.getConfirmantNICorPIN());
                        bdf.getConfirmant().setConfirmantFullName(confirmant.getConfirmantFullName());
                        bdf.getConfirmant().setConfirmantSignDate(confirmant.getConfirmantSignDate());
                        break;
                }
            }
            session.put(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN, bdf);

            populate();
            return "form" + pageNo;
        }
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

        int selectedDistrictId = user.getInitialDistrict();
        int selectedBDDivisionId = user.getInitialBDDivision();
        int selectedDSDivisionId = bdDivisionDAO.getBDDivision(selectedDistrictId, selectedBDDivisionId).
                getDsDivision().getDivisionId();

        logger.debug("inside populate : {} observed.", language);
        districtList = districtDAO.getDistricts(language, user);
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(selectedDistrictId, language);

        districtList = districtDAO.getDistricts(language, user);
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(selectedDistrictId, language);

        countryList = countryDAO.getCountries(language);
        raceList = raceDAO.getRaces(language);
        // TODO division hard coded for the moment
        bdDivisionList = bdDivisionDAO.getDivisions(language, selectedDistrictId, user);
        gnDivisionList = gnDivisionDAO.getGNDivisionNames(selectedDistrictId, selectedDSDivisionId, language);

        logger.debug("inside populte : districts , dsdivisions, countries and races populated.");
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

    public Map<Integer, String> getBdDivisionList() {
        return bdDivisionList;
    }

    public void setBdDivisionList(Map<Integer, String> bdDivisionList) {
        this.bdDivisionList = bdDivisionList;
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
    public int getBirthDistrict() {
        return birthDistrict;
    }

    public void setBirthDistrict(int birthDistrict) {
        this.birthDistrict = birthDistrict;
    }

    public int getBirthDivision() {
        return birthDivision;
    }

    public void setBirthDivision(int birthDivision) {
        this.birthDivision = birthDivision;
        logger.debug("BirthDivision : {}, district {}", birthDivision, birthDistrict);
        child.setBirthDivision(bdDivisionDAO.getBDDivision(birthDistrict, birthDivision));
    }

    public int getFatherCountry() {
        return fatherCountry;
    }

    public void setFatherCountry(int fatherCountry) {
        if (parent != null) {
            this.fatherCountry = fatherCountry;
            logger.debug("Father Country: {}", fatherCountry);
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

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public void setDsDivisionList(Map<Integer, String> dsDivisionList) {
        this.dsDivisionList = dsDivisionList;
    }

    public DSDivisionDAO getDsDivisionDAO() {
        return dsDivisionDAO;
    }

    public Map<Integer, String> getGnDivisionList() {
        return gnDivisionList;
    }

    public void setGnDivisionList(Map<Integer, String> gnDivisionList) {
        this.gnDivisionList = gnDivisionList;
    }

    public int getDsDivision() {
        return dsDivision;
    }

    public void setDsDivision(int dsDivision) {
        this.dsDivision = dsDivision;
        logger.debug("DS Division: {}", dsDivision);
        // TODO
    }

    public int getGnDivision() {
        return gnDivision;
    }

    public void setGnDivision(int gnDivision) {
        this.gnDivision = gnDivision;
        logger.debug("GN Division: {}", gnDivision);
        // TODO
    }

    public ConfirmantInfo getConfirmant() {
        return confirmant;
    }

    public void setConfirmant(ConfirmantInfo confirmant) {
        this.confirmant = confirmant;
    }
}