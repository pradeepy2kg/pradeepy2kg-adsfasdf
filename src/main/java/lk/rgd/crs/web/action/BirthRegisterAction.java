package lk.rgd.crs.web.action;


import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.domain.AppParameter;
import org.apache.struts2.interceptor.SessionAware;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Locale;
import java.util.List;

import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.User;

import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.bean.UserWarning;

import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.DateState;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.Permission;

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
    private final AppParametersDAO appParametersDAO;

    private Map<Integer, String> districtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> allDistrictList;
    private Map<Integer, String> allDSDivisionList;
    private List<UserWarning> warnings;

    private String scopeKey;
    private Map session;

    private ChildInfo child;
    private ParentInfo parent;
    private GrandFatherInfo grandFather;
    private MarriageInfo marriage;
    private InformantInfo informant;
    private NotifyingAuthorityInfo notifyingAuthority;
    private ConfirmantInfo confirmant;
    private BirthRegisterInfo register;
    private User user;

    private int pageNo; //pageNo is used to decide the current pageNo of the Birth Registration Form
    private long bdId;   // If present, it should be used to fetch a new BD instead of creating a new one (we are in edit mode)
    private boolean confirmationSearchFlag;//if true request to search an entry based on serialNo

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
    private int bdfLateOrBelated;
    private int caseFileNumber;
    private String newComment;

    private long serialNo; //to be used in the case where search is performed from confirmation 1 page.
    private boolean addNewMode;
    private boolean back;

    public String welcome() {
        return "success";
    }

    public BirthRegisterAction(BirthRegistrationService service, DistrictDAO districtDAO, CountryDAO countryDAO, RaceDAO raceDAO, BDDivisionDAO bdDivisionDAO, DSDivisionDAO dsDivisionDAO, AppParametersDAO appParametersDAO) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.appParametersDAO = appParametersDAO;
        child = new ChildInfo();
    }

    public String loadDSDivList() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        populateBasicLists(language);
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, language, user);
        logger.debug("DS division list set from Ajax : {} {}", birthDistrictId, dsDivisionId);
        return "DSDivList";
    }

    public String loadBDDivList() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        if (dsDivisionId != 0) { // in case UI does not return a ID, (at page load) use the existing list
            bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        } else {
            populateBasicLists(language);
            populateDynamicLists(language);
        }
        logger.debug("BD division list set from Ajax : {} {}", dsDivisionId, bdDivisionList);
        return "BDDivList";
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
            if (back) {
                populate((BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
                return "form" + pageNo;
            }

            if (pageNo == 0) {
                if (bdId == 0) {
                    bdf = new BirthDeclaration();
                    bdf.getRegister().setStatus(BirthDeclaration.State.DATA_ENTRY);
                } else {
                    bdf = service.getById(bdId, user);
                    if (bdf.getRegister().getStatus() != BirthDeclaration.State.DATA_ENTRY) {  // edit not allowed
                        return "error";   // todo pass error info
                    }
                    //todo check permissions to operate on this birthdivision 
                }
            } else {
                bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
                switch (pageNo) {
                    case 1:
                        bdf.setChild(child);
                        register.setStatus(bdf.getRegister().getStatus());
                        register.setComments(bdf.getRegister().getComments());
                        bdf.setRegister(register);
                        break;
                    case 2:
                        bdf.setParent(parent);
                        break;
                    case 3:
                        bdf.setMarriage(marriage);
                        bdf.setGrandFather(grandFather);
                        bdf.setInformant(informant);
                        bdfLateOrBelated = checkDateLateOrBelated(bdf);
                        break;
                    case 4:
                        bdf.setNotifyingAuthority(notifyingAuthority);

                        logger.debug("caseFileNum: {}, newComment: {}", caseFileNumber, newComment);
                        // all pages captured, proceed to persist after validations
                        // todo data validations
                        service.addNormalBirthDeclaration(bdf, true, (User) session.get(WebConstants.SESSION_USER_BEAN), new Integer(caseFileNumber).toString() /** FIXME */, newComment);

                        // TODO remove this section, can access this in jsp
                        // used to check user have aproval authority and passed to BirthRegistationFormDetails jsp
                        boolean allowApproveBDF = user.isAuthorized(Permission.APPROVE_BDF);
                        session.put("allowApproveBDF", allowApproveBDF);
                }
            }
            if (!addNewMode) {
                session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("DistrictId: " + birthDistrictId + " ,BDDivisionId: " + birthDivisionId + " ,DSDivisionId: " + dsDivisionId);
            }

            populate(bdf);
            logger.debug("Birth Declaration: PageNo=" + pageNo);
            return "form" + pageNo;
        }
    }

    /**
     * This method is responsible for loading and capture data for all 3 BDC pages as well
     * as their persistance. pageNo hidden variable which is passed to the action (empty=0 for the
     * very first form page) is used to decide which state of the process we are in. bdId field should be used to
     * determine the particular birth declarion entity on the initial visit to action. (after then it will be kept in the session)
     */
    public String birthConfirmation() {
        logger.debug("Step {} of 3 ", pageNo);
        if ((pageNo > 3) || (pageNo < 0)) {
            return "error";
        } else {
            BirthDeclaration bdf;
            if (back) {
                populate((BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN));
                return "form" + pageNo;
            }

            if (pageNo == 0) {
                if (bdId != 0) {
                    try {
                        bdf = service.getById(bdId, user);
                        if (!(bdf.getRegister().getStatus() == BirthDeclaration.State.CONFIRMATION_PRINTED ||
                            bdf.getRegister().getStatus() == BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED)) {
                            addActionError(getText("cp1.error.editNotAllowed"));
                            return "error";
                        }
                    } catch (Exception e) {
                        handleErrors(e);
                        addActionError(getText("cp1.error.entryNotAvailable"));
                        return "error";
                    }
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
                        //todo archive the old entry
                        service.addNormalBirthDeclaration(bdf, true, (User) session.get(WebConstants.SESSION_USER_BEAN), new Integer(caseFileNumber).toString() /** FIXME */, newComment);
                }
            }
            session.put(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN, bdf);

            populate(bdf);
            return "form" + pageNo;
        }
    }

    public String ConfirmationPrintPageLoad(){
        boolean caughtException = false;
        BirthDeclaration bdf;
        bdf = service.getById(bdId, user);
        if (user.isAuthorized(Permission.APPROVE_BDF)){
            logger.debug("BDID {} : ",bdId );

            try {
                warnings = service.approveBirthDeclaration(bdf, false, user);
            } catch (CRSRuntimeException e) {
                addActionError(getText("brapproval.approval.error." + e.getErrorCode()));
                caughtException = true;
            }

            if(caughtException ||(warnings != null && warnings.isEmpty())) {
                return "pageLoad";
            } else {
                return "error";
            }
        } else {
            return "error";
        } 
    }

    public String birthConfirmationPrint() {
        return "success";
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
        populateBasicLists(language);

        /**
         *  under "Add another mode", few special values need to be3 preserved from last entry .
         *  Pre setting serial, dateOfRegistrtion, district, division and notifyAuthority in batch mode data entry.
         *  serial number incerementing by one.
         */
        if (addNewMode) {
            BirthDeclaration oldBdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
            register = new BirthRegisterInfo();
            register.setBdfSerialNo(oldBdf.getRegister().getBdfSerialNo() + 1);
            register.setDateOfRegistration(oldBdf.getRegister().getDateOfRegistration());
            birthDistrictId = oldBdf.getRegister().getBirthDistrict().getDistrictUKey();
            birthDivisionId = oldBdf.getRegister().getBirthDivision().getBdDivisionUKey();
            dsDivisionId = oldBdf.getRegister().getDsDivision().getDsDivisionUKey();
            populateDynamicLists(language);

            notifyingAuthority = new NotifyingAuthorityInfo();
            notifyingAuthority.setNotifyingAuthorityPIN(oldBdf.getNotifyingAuthority().getNotifyingAuthorityPIN());
            notifyingAuthority.setNotifyingAuthorityName(oldBdf.getNotifyingAuthority().getNotifyingAuthorityName());
            notifyingAuthority.setNotifyingAuthorityAddress(oldBdf.getNotifyingAuthority().getNotifyingAuthorityAddress());
            notifyingAuthority.setNotifyingAuthoritySignDate(oldBdf.getNotifyingAuthority().getNotifyingAuthoritySignDate());

            bdf.setNotifyingAuthority(notifyingAuthority);
            bdf.setRegister(register);
            session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);
            logger.debug("Districts, DS and BD divisions set from earlier (AddNewMode) info : {} {}", birthDistrictId, dsDivisionId);
            return;  // end of populating fields for this mode.
        }

        child = bdf.getChild();
        parent = bdf.getParent();
        grandFather = bdf.getGrandFather();
        marriage = bdf.getMarriage();
        informant = bdf.getInformant();
        confirmant = bdf.getConfirmant();
        register = bdf.getRegister();
        notifyingAuthority = bdf.getNotifyingAuthority();

        boolean idsPopulated = false;
        if (register != null) {
            if (register.getBirthDivision() != null) {  //if data present, populate with existing values
                birthDistrictId = register.getBirthDistrict().getDistrictUKey();
                birthDivisionId = register.getBirthDivision().getBdDivisionUKey();
                dsDivisionId = register.getDsDivision().getDsDivisionUKey();
                idsPopulated = true;
            }
            logger.debug("Districts, DS and BD divisions set from RegisterInfo : {} {}", birthDistrictId, dsDivisionId);
        }

        if (!idsPopulated) {         // populate distric and ds div Ids with user preferences or set to 0 temporarily
            if (user.getPrefBDDistrict() != null) {
                birthDistrictId = user.getPrefBDDistrict().getDistrictUKey();
                logger.debug("Prefered district {} set in user {}", birthDistrictId, user.getUserId());
            } else {
                birthDistrictId = 0;
                logger.debug("First district in the list {} was set in user {}", birthDistrictId, user.getUserId());
            }

            if (user.getPrefBDDSDivision() != null) {
                dsDivisionId = user.getPrefBDDSDivision().getDsDivisionUKey();
            } else {
                dsDivisionId = 0;
            }
            logger.debug("Districts, DS and BD divisions set from defaults : {} {}", birthDistrictId, dsDivisionId);
        }

        populateDynamicLists(language);

        // following painful null checks are needed b'cos the DB may have incomplete data
        if (parent != null) {
            if (parent.getFatherCountry() != null) {
                fatherCountry = parent.getFatherCountry().getCountryId();
            }

            if (parent.getMotherCountry() != null) {
                motherCountry = parent.getMotherCountry().getCountryId();
            }

            if (parent.getFatherRace() != null) {
                fatherRace = parent.getFatherRace().getRaceId();
            }

            if (parent.getMotherRace() != null) {
                motherRace = parent.getMotherRace().getRaceId();
            }

            if (parent.getMotherDSDivision() != null) {
                motherDistrictId = parent.getMotherDSDivision().getDsDivisionUKey();
            }

            if (parent.getMotherDSDivision() != null) {
                motherDSDivisionId = parent.getMotherDSDivision().getDsDivisionUKey();
            }
        }
    }

    /**
     * Check whether BirthDeclarationForm is late registration or belated registration
     *
     * @param bdf
     * @return int id of specific item in DateState
     */
    private int checkDateLateOrBelated(BirthDeclaration bdf) {
        long maxLateDays = appParametersDAO.getIntParameter(AppParameter.CRS_BIRTH_LATE_REG_DAYS);
        long maxBelatedDays = appParametersDAO.getIntParameter(AppParameter.CRS_BELATED_MAX_DAYS);
        long registerDate = bdf.getRegister().getDateOfRegistration().getTime();
        long birthDate = bdf.getChild().getDateOfBirth().getTime();
        long milliSecPerDay = 1000 * 60 * 60 * 24;

        long dateDiff = (registerDate - birthDate) / milliSecPerDay;

        if (dateDiff >= 0) {
            if (dateDiff >= maxBelatedDays) {
                return DateState.BD_BELATED.getStateId();
            } else if (dateDiff >= maxLateDays) {
                return DateState.BD_LATE.getStateId();
            } else {
                return DateState.BD_OK.getStateId();
            }
        } else {
            return DateState.ERROR.getStateId();
        }
    }

    private void populateDynamicLists(String language) {
        if (birthDistrictId == 0) {
            if (!districtList.isEmpty()) {
                birthDistrictId = districtList.keySet().iterator().next();
                logger.debug("first allowed district in the list {} was set", birthDistrictId);
            }
        }
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, language, user);

        if (dsDivisionId == 0) {
            if (!dsDivisionList.isEmpty()) {
                dsDivisionId = dsDivisionList.keySet().iterator().next();
                logger.debug("first allowed DS Div in the list {} was set", dsDivisionId);
            }
        }

        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        if (birthDivisionId == 0) {
            birthDivisionId = bdDivisionList.keySet().iterator().next();
            logger.debug("first allowed BD Div in the list {} was set", birthDivisionId);
        }
    }

    private void populateBasicLists(String language) {
        countryList = countryDAO.getCountries(language);
        districtList = districtDAO.getDistrictNames(language, user);
        raceList = raceDAO.getRaces(language);

        /** getting full district list and DS list for mother info on page 4 */
        allDistrictList = districtDAO.getDistrictNames(language, null);
        if (!allDistrictList.isEmpty()) {
            int selectedDistrictId = allDistrictList.keySet().iterator().next();
            allDSDivisionList = dsDivisionDAO.getDSDivisionNames(selectedDistrictId, language, null);
        }
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setSession(Map map) {
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("setting User: {}", user.getUserName());
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

//    public void setBdDivisionList(Map<Integer, String> bdDivisionList) {
//        this.bdDivisionList = bdDivisionList;
//    }

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
        logger.debug("setting BirthDivision: {}", register.getBirthDivision().getEnDivisionName());
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
        logger.debug("setting Father Country: {}", parent.getFatherCountry().getEnCountryName());
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

//    public void setDsDivisionList(Map<Integer, String> dsDivisionList) {
//        this.dsDivisionList = dsDivisionList;
//    }
//
//    public DSDivisionDAO getDsDivisionDAO() {
//        return dsDivisionDAO;
//    }

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

    public long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(long serialNo) {
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
        logger.debug("setting Father Race: {}", parent.getFatherRace().getEnRaceName());
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
        logger.debug("setting Mother Race: {}", parent.getMotherRace().getEnRaceName());
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
        logger.debug("setting Mother DSDivision: {}", parent.getMotherDSDivision().getEnDivisionName());
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

    public boolean isAddNewMode() {
        return addNewMode;
    }

    public void setAddNewMode(boolean addNewMode) {
        this.addNewMode = addNewMode;
    }

    public boolean isBack() {
        return back;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    public boolean isConfirmationSearchFlag() {
        return confirmationSearchFlag;
    }

    public void setConfirmationSearchFlag(boolean confirmationSearchFlag) {
        this.confirmationSearchFlag = confirmationSearchFlag;
    }

    public int getBdfLateOrBelated() {
        return bdfLateOrBelated;
    }

    public void setBdfLateOrBelated(int bdfLateOrBelated) {
        this.bdfLateOrBelated = bdfLateOrBelated;
    }

    public int getCaseFileNumber() {
        return caseFileNumber;
    }

    public void setCaseFileNumber(int caseFileNumber) {
        this.caseFileNumber = caseFileNumber;
    }

    public String getNewComment() {
        return newComment;
    }

    public void setNewComment(String newComment) {
        this.newComment = newComment;
    }
    public List<UserWarning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<UserWarning> warnings) {
        this.warnings = warnings;
    }
}