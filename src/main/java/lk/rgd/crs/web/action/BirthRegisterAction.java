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

    private Map<Integer, String> districtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> allDistrictList;
    private Map<Integer, String> allDSDivisionList;

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
    private BirthRegisterInfo register;

    private int pageNo; //pageNo is used to decide the current pageNo of the Birth Registration Form
    private long bdId;   // If present, it should be used to fetch a new BD instead of creating a new one (we are in edit mode)

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    private int birthDistrictId;
    private int birthDivisionId;
    private int fatherCountry;
    private int motherCountry;
    private int fatherRace;
    private int motherRace;
    private int dsDivisionId;
    private int motherDistrictId;
    private int motherDSDivisionId;

    private String serialNo; //to be used in the case where search is performed from confirmation 1 page.

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
        child = new ChildInfo();
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
                    bdf.getRegister().setStatus(BirthDeclaration.State.DATA_ENTRY);
                } else {
                    bdf = service.getById(bdId);
                    BirthDeclaration.State status = bdf.getRegister().getStatus();
                    logger.debug("inside birthDeclaration - bdId : {}, status : {} ", bdId, status);
                    if (status != BirthDeclaration.State.DATA_ENTRY) {  // edit not allowed
                        return "error";   // todo pass error info
                    }
                    //todo check permissions to operate on this birthdivision 
                }
            } else {
                bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
                switch (pageNo) {
                    case 1:
                        bdf.setChild(child);
                        bdf.setRegister(register);
                        break;
                    case 2:
                        bdf.setParent(parent);
                        break;
                    case 3:
                       logger.debug("Postal Address of informent  :" + informant.getInformantAddress());
                        bdf.setMarriage(marriage);
                        bdf.setGrandFather(grandFather);
                        bdf.setInformant(informant);
                        break;
                    case 4:
                        bdf.setNotifyingAuthority(notifyingAuthority);
                        // all pages captured, proceed to persist after validations
                        // todo business validations and persiatance

                        service.addNormalBirthDeclaration(bdf, true, (User) session.get(WebConstants.SESSION_USER_BEAN));
                }
            }
            session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);

            populate(bdf);
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
                    bdf = new BirthDeclaration(); // just go to the confirmation 1 page
                }
            } else {
                bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
                switch (pageNo) {
                    case 1:
                        bdf.getRegister().setBdfSerialNo(register.getBdfSerialNo());
                        bdf.getRegister().setDateOfRegistration(register.getDateOfRegistration());
                        bdf.getRegister().setBirthDivision(register.getBirthDivision());

                        bdf.getChild().setDateOfBirth(child.getDateOfBirth());
                        bdf.getChild().setChildGender(child.getChildGender());
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
                        break;
                    case 3:
                        bdf.getConfirmant().setConfirmantNICorPIN(confirmant.getConfirmantNICorPIN());
                        bdf.getConfirmant().setConfirmantFullName(confirmant.getConfirmantFullName());
                        bdf.getConfirmant().setConfirmantSignDate(confirmant.getConfirmantSignDate());

                        logger.debug("Birth Confirmation Persist : {}", confirmant.getConfirmantSignDate());
                        service.addNormalBirthDeclaration(bdf, true, (User) session.get(WebConstants.SESSION_USER_BEAN));
                        break;
                }
            }
            session.put(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN, bdf);

            populate(bdf);
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
                    //logger.debug("inside birthConfirmation : bdKey {}", getBdKey());
                    bdf = new BirthDeclaration(); // just go to the confirmation 1 page
                }
            } else {
                bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
            }
            session.put(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN, bdf);
            populate(bdf);
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
    private void populate(BirthDeclaration bdf) {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);

        logger.debug("inside populate : {} observed.", language);

        countryList = countryDAO.getCountries(language);
        districtList = districtDAO.getDistrictNames(language, user);
        /** getting full district list */
        allDistrictList = districtDAO.getDistrictNames(language, null);
        /** getting full DSDivision list */
        // TODO currently getting DSDivision by hard coding district id
        if (!allDistrictList.isEmpty()) {
            int selectedDistrictId = allDistrictList.keySet().iterator().next();
            allDSDivisionList = dsDivisionDAO.getDSDivisionNames(selectedDistrictId, language, null);
        }

        if (!districtList.isEmpty()) {
            // int selectedDistrictId = districtList.keySet().iterator().next();
            int birthDistrictId = districtList.keySet().iterator().next();
            dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, language, user);
            bdDivisionList = bdDivisionDAO.getBDDivisionNames(birthDistrictId, language, user);
            raceList = raceDAO.getRaces(language);
            logger.debug("inside populate : districts , dsdivisions, countries and races populated.");
        }

        child = bdf.getChild();
        parent = bdf.getParent();
        grandFather = bdf.getGrandFather();
        marriage = bdf.getMarriage();
        informant = bdf.getInformant();
        confirmant = bdf.getConfirmant();
        register = bdf.getRegister();

//        if (register != null) {
//            birthDistrictId = register.getBirthDistrict().getDistrictId();
//            birthDivisionId = register.getBirthDivision().getDivisionId();
//            dsDivisionId = register.getDsDivision().getDivisionId();
//        }
//
//        if (parent != null) {
//            fatherCountry = parent.getFatherCountry().getCountryId();
//            motherCountry = parent.getMotherCountry().getCountryId();
//            fatherRace = parent.getFatherRace().getRaceId();
//            motherRace = parent.getMotherRace().getRaceId();
//            motherDistrictId = parent.getMotherDSDivision().getDistrictId();
//            motherDSDivisionId = parent.getMotherDSDivision().getDivisionId();
//        }
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
        if (register == null) {
            register = new BirthRegisterInfo();
        }
        register.setBirthDivision(bdDivisionDAO.getBDDivisionByPK(birthDivisionId));
        logger.debug("setting BirthDivision : {}", register.getBirthDivision().getEnDivisionName());
    }

    public int getFatherCountry() {
        return fatherCountry;
    }

    public void setFatherCountry(int fatherCountry) {
        this.fatherCountry = fatherCountry;
        if (parent == null) {
            parent = new ParentInfo();
        }
        parent.setFatherCountry(countryDAO.getCountry(fatherCountry));
        logger.debug("setting Father Country: {} From DAO: {}", parent.getFatherCountry().getEnCountryName());
    }

    public int getMotherCountry() {
        return motherCountry;
    }

    public void setMotherCountry(int motherCountry) {
        this.motherCountry = motherCountry;
        if (parent == null) {
            parent = new ParentInfo();
        }
        parent.setMotherCountry(countryDAO.getCountry(motherCountry));
        logger.debug("setting Mother Country: {}", parent.getMotherCountry().getEnCountryName());
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

    public int getFatherRace() {
        return fatherRace;
    }

    public void setFatherRace(int fatherRace) {
        this.fatherRace = fatherRace;
        if (parent == null) {
            parent = new ParentInfo();
        }
        parent.setFatherRace(raceDAO.getRace(fatherRace));
    }

    public int getMotherRace() {
        return motherRace;
    }

    public void setMotherRace(int motherRace) {
        this.motherRace = motherRace;
        if (parent == null) {
            parent = new ParentInfo();
        }
        parent.setMotherRace(raceDAO.getRace(motherRace));
    }

    public int getMotherDSDivisionId() {
        return motherDSDivisionId;
    }

    public void setMotherDSDivisionId(int motherDSDivisionId) {
        this.motherDSDivisionId = motherDSDivisionId;
        if (parent == null) {
            parent = new ParentInfo();
        }
        parent.setMotherDSDivision(dsDivisionDAO.getDSDivisionByPK(motherDSDivisionId));
    }

    public BirthRegisterInfo getRegister() {
        return register;
    }

    public void setRegister(BirthRegisterInfo register) {
        this.register = register;
    }

    public long getBdId() {
        return bdId;
    }

    public void setBdId(long bdId) {
        this.bdId = bdId;
    }

    public Map<Integer, String> getAllDistrictList() {
        return allDistrictList;
    }

    public void setAllDistrictList(Map<Integer, String> allDistrictList) {
        this.allDistrictList = allDistrictList;
    }

    public Map<Integer, String> getAllDSDivisionList() {
        return allDSDivisionList;
    }

    public void setAllDSDivisionList(Map<Integer, String> allDSDivisionList) {
        this.allDSDivisionList = allDSDivisionList;
    }

    public int getMotherDistrictId() {
        return motherDistrictId;
    }

    public void setMotherDistrictId(int motherDistrictId) {
        this.motherDistrictId = motherDistrictId;
    }
}