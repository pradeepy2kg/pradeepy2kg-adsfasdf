/*@author
* amith jayasekara
* chathuranga
* */
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
import lk.rgd.common.api.domain.DSDivision;

import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
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
    private final BirthDeclarationDAO birthDeclarationDAO;

    private Map<Integer, String> districtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> bdDivisionList;

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
    private int birthDistrictId;
    private int birthDivisionId;
    private int fatherCountry;
    private int motherCountry;
    private int fatherRace;
    private int motherRace;
    private int dsDivisionId;

    private String serialNo; //to be used in the case where search is performed from confirmation 1 page.
    /**
     * confirmationFlag is set to 1 if
     * request is from birth registration
     * approval jsp else it is set to 0
     */
    private int confirmationFlag;
    /**
     * key is used to identify a particular
     * entity of BirthDeclaration bean
     */
    private long bdKey;

    public String welcome() {
        return "success";
    }

    public BirthRegisterAction(BirthRegistrationService service, DistrictDAO districtDAO, CountryDAO countryDAO, RaceDAO raceDAO, BDDivisionDAO bdDivisionDAO, DSDivisionDAO dsDivisionDAO,BirthDeclarationDAO birthDeclarationDAO) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.birthDeclarationDAO=birthDeclarationDAO;
        child = new ChildInfo();
        parent = new ParentInfo();
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
                    bdf = service.getById(bdId);
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
                        logger.debug("Father Country: {}", fatherCountry);
                        logger.debug("father new country  {} ", parent.getFatherCountry());
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

                        service.addNormalBirthDeclaration(bdf, true, (User) session.get(WebConstants.SESSION_USER_BEAN));

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
                if (bdId != 0) {
                    bdf = service.getById(bdId);
                } else if ((serialNo != null) && !(serialNo.equals(""))) {
                    bdf = service.getBySerialNo(serialNo);
                } else {
                    logger.debug("inside birthConfirmation : bdKey {} observed", bdKey);
                    bdf=new BirthDeclaration(); // just go to the confirmation 1 page
                    if (bdKey != 0) {
                        /**
                         * request is from Birth registration approval 
                         */
                        bdf = birthDeclarationDAO.getById(bdKey);
                        session.put("birthRegister", bdf);
                    }
                }
            } else {
                bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
                switch (pageNo) {
                    case 1:
                        bdf.getChild().setBdfSerialNo(child.getBdfSerialNo());
                        bdf.getChild().setDateOfRegistration(child.getDateOfRegistration());
                        bdf.getChild().setDateOfBirth(child.getDateOfBirth());
                        bdf.getChild().setChildGender(child.getChildGender());
                        bdf.getChild().setBirthDivision(child.getBirthDivision());
                        bdf.getChild().setPlaceOfBirth(child.getPlaceOfBirth());

                        bdf.getParent().setFatherNICorPIN(parent.getFatherNICorPIN());
                        bdf.getParent().setFatherRace(parent.getFatherRace());
                        bdf.getParent().setMotherNICorPIN(parent.getMotherNICorPIN());
                        bdf.getParent().setMotherRace(parent.getMotherRace());

                        bdf.getMarriage().setParentsMarried(marriage.getParentsMarried());
                        break;
                    case 2:
                        logger.debug("inside birthConfirmation : confiramationFlag {}", confirmationFlag);
                        if (confirmationFlag != 1) {
                            bdf.getChild().setChildFullNameOfficialLang(child.getChildFullNameOfficialLang());
                            bdf.getChild().setChildFullNameEnglish(child.getChildFullNameEnglish());

                            bdf.getParent().setFatherFullName(parent.getFatherFullName());
                            bdf.getParent().setMotherFullName(parent.getMotherFullName());
                        }
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

    public String birthConfirmationPrint() {

        logger.debug("Step {} of 3 ", pageNo);
        if ((pageNo > 3) || (pageNo < 0)) {
            return "error";
        } else {
            BirthDeclaration bdf;
            if (pageNo == 0) {
                if (bdId != 0) {
                    bdf = service.getById(bdId);
                } else if ((serialNo != null) && !(serialNo.equals(""))) {
                    bdf = service.getBySerialNo(serialNo);
                } else {
                    logger.debug("inside birthConfirmation : bdKey {}", getBdKey());
                    bdf = new BirthDeclaration(); // just go to the confirmation 1 page
                }
            } else {
                bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
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

        logger.debug("inside populate : {} observed.", language);

        countryList = countryDAO.getCountries(language);
        districtList = districtDAO.getDistrictNames(language, user);
        if (!districtList.isEmpty()) {
            int selectedDistrictId = districtList.keySet().iterator().next();
            dsDivisionList = dsDivisionDAO.getDSDivisionNames(selectedDistrictId, language, user);
            bdDivisionList = bdDivisionDAO.getBDDivisionNames(selectedDistrictId, language, user);
            raceList = raceDAO.getRaces(language);
            logger.debug("inside populate : districts , dsdivisions, countries and races populated.");
        }
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
    public int getBirthDistrictId() {
        return birthDistrictId;
    }

    public void setBirthDistrictId(int birthDistrictId) {
        this.birthDistrictId = birthDistrictId;
    }

    public int getBirthDivisionId() {
        return birthDivisionId;
    }

    public void setBirthDivisionId(int birthDivisionId) {
        this.birthDivisionId = birthDivisionId;
        logger.debug("BirthDivision : {}, district {}", birthDivisionId, birthDistrictId);
        child.setBirthDivision(bdDivisionDAO.getBDDivision(birthDistrictId, birthDivisionId));
        logger.debug("BirthDivision object : {}", child.getBirthDivision());
    }

    public int getFatherCountry() {
        return fatherCountry;
    }

    public void setFatherCountry(int fatherCountry) {
        this.fatherCountry = fatherCountry;
        parent.setFatherCountry(countryDAO.getCountry(fatherCountry));
        logger.debug("Father Country: {} From DAO :{}", parent.getFatherCountry().getEnCountryName());
    }

    public int getMotherCountry() {
        return motherCountry;
    }

    public void setMotherCountry(int motherCountry) {
        this.motherCountry = motherCountry;
        parent.setMotherCountry(countryDAO.getCountry(motherCountry));
        logger.debug("Mother Country: {}", parent.getMotherCountry().getEnCountryName());
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

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
        logger.debug("DS Division: {}", dsDivisionId);
        // TODO
    }

    public ConfirmantInfo getConfirmant() {
        return confirmant;
    }

    public void setConfirmant(ConfirmantInfo confirmant) {
        this.confirmant = confirmant;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public int getConfirmationFlag() {
        return confirmationFlag;
    }

    public void setConfirmationFlag(int confirmationFlag) {
        this.confirmationFlag = confirmationFlag;
    }

    public long getBdKey() {
        return bdKey;
    }

    public void setBdKey(long bdKey) {
        this.bdKey = bdKey;
    }

    public int getFatherRace() {
        return fatherRace;
    }

    public void setFatherRace(int fatherRace) {
        this.fatherRace = fatherRace;
    }

    public int getMotherRace() {
        return motherRace;
    }

    public void setMotherRace(int motherRace) {
        this.motherRace = motherRace;
    }
}