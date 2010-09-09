package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.service.BirthAlterationService;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.User;
import lk.rgd.Permission;

import java.util.Map;
import java.util.Date;
import java.util.BitSet;
import java.util.Locale;

/**
 * @author tharanga
 */
public class AlterationAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(AlterationAction.class);
    private BirthRegistrationService service;
    private DistrictDAO districtDAO;
    private CountryDAO countryDAO;
    private RaceDAO raceDAO;
    private BDDivisionDAO bdDivisionDAO;
    private DSDivisionDAO dsDivisionDAO;
    private BirthAlterationService alterationService;
    private Map session;

    private Map<Integer, String> districtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> allDistrictList;
    private Map<Integer, String> allDSDivisionList;

    private User user;
    private Alteration27 alt27;
    private Alteration27A alt27A;
    private Alteration52_1 alt52_1;
    private DeclarantInfo declarant;
    private BirthRegisterInfo register;


    private int pageNo; //pageNo is used to decide the current pageNo of the Birth Registration Form
    private long bdId;   // If present, it should be used to fetch a new BD instead of creating a new one (we are in edit mode)
    private long nicOrPin;

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    private int birthDistrictId;
    private int birthDivisionId;
    private int fatherCountry;
    private int motherCountry;
    private int fatherRace;
    private int motherRace;
    private int dsDivisionId;
    private int sectionOfAct;
    private long idUKey;
    private long serialNo; //to be used in the case where search is performed from confirmation 1 page.
    private boolean allowApproveAlteration;

    private String language;

    public AlterationAction(BirthRegistrationService service, DistrictDAO districtDAO, CountryDAO countryDAO, RaceDAO raceDAO, BDDivisionDAO bdDivisionDAO, DSDivisionDAO dsDivisionDAO
        , BirthAlterationService alterationService) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.alterationService = alterationService;
    }


    public String initBirthAlteration() {
        pageNo = 0;
        populateBasicLists();

        return SUCCESS;
    }

    public String birthAlterationSearch() {
        BirthDeclaration bdf = new BirthDeclaration();
        switch (pageNo) {
            case 1:
                bdf = service.getById(idUKey, user);
                break;
            case 2:
                bdf = service.getByPINorNIC(nicOrPin, user);
                break;
            case 3:
                bdf = service.getActiveRecordByBDDivisionAndSerialNo(bdDivisionDAO.getBDDivisionByPK(birthDivisionId),
                    serialNo, user);
                break;
        }

        populateAlteration(bdf);
        pageNo = 1;
        populateBasicLists();
        return SUCCESS;
    }

    private void populateAlteration(BirthDeclaration bdf) {
        alt27 = new Alteration27();
        alt27A = new Alteration27A();
        alt52_1 = new Alteration52_1();

        //set alt27
        alt27.setChildFullNameOfficialLang(bdf.getChild().getChildFullNameOfficialLang());
        alt27.setChildFullNameEnglish(bdf.getChild().getChildFullNameEnglish());
        //set alt27A
        FatherInfo father = new FatherInfo();
        father.setFatherDOB(bdf.getParent().getFatherDOB());
        father.setFatherFullName(bdf.getParent().getFatherFullName());
        father.setFatherNICorPIN(bdf.getParent().getFatherNICorPIN());
        father.setFatherPassportNo(bdf.getParent().getFatherPassportNo());
        father.setFatherPlaceOfBirth(bdf.getParent().getFatherPlaceOfBirth());
        father.setFatherRace(bdf.getParent().getFatherRace());
        alt27A.setFather(father);
        alt27A.setGrandFather(bdf.getGrandFather());
        alt27A.setMarriage(bdf.getMarriage());
        alt52_1.setInformant(bdf.getInformant());
        alt52_1.setChildGender(bdf.getChild().getChildGender());
        alt52_1.setDateOfBirth(bdf.getChild().getDateOfBirth());
        alt52_1.setPlaceOfBirth(bdf.getChild().getPlaceOfBirth());

        //set alt52_1
        alt52_1.setPlaceOfBirthEnglish(bdf.getChild().getPlaceOfBirthEnglish());
        MotherInfo mother = new MotherInfo();
        mother.setMotherAddress(bdf.getParent().getMotherAddress());
        mother.setMotherAgeAtBirth(bdf.getParent().getMotherAgeAtBirth());
        mother.setMotherDOB(bdf.getParent().getMotherDOB());
        mother.setMotherFullName(bdf.getParent().getMotherFullName());
        mother.setMotherNICorPIN(bdf.getParent().getMotherNICorPIN());
        mother.setMotherPassportNo(bdf.getParent().getMotherPassportNo());
        alt52_1.setMother(mother);
        if (bdf.getParent().getFatherCountry() != null) {
            fatherCountry = bdf.getParent().getFatherCountry().getCountryId();
        }
        if (bdf.getParent().getFatherRace() != null) {
            fatherRace = bdf.getParent().getFatherRace().getRaceId();
        }
        if (bdf.getParent().getMotherCountry() != null) {
            motherCountry = bdf.getParent().getMotherCountry().getCountryId();
        }
        if (bdf.getParent().getMotherRace() != null) {
            motherRace = bdf.getParent().getMotherRace().getRaceId();
        }
        birthDistrictId = bdf.getRegister().getBirthDistrict().getDistrictUKey();
        birthDivisionId = bdf.getRegister().getBirthDivision().getBdDivisionUKey();
        dsDivisionId = bdf.getRegister().getDsDivision().getDsDivisionUKey();
    }


    public String birthAlteration() {
        //todo tharanga >> remove country and race DAOs if they not wanted and also allDistrictList and AllDSDivisionList
        BirthAlteration ba = new BirthAlteration();
        alt27.setFullNameOfficialLangApproved(false);
        alt52_1.setBirthDivision(bdDivisionDAO.getBDDivisionByPK(birthDivisionId));
        Date d = new Date();
        alt52_1.getInformant().setInformantSignDate(d);
        BitSet bitSet = new BitSet();
        alt27A.setApprovalStatuses(bitSet);
        alt52_1.setApprovalStatuses(bitSet);
        ba.setAlt27(alt27);
        ba.setAlt27A(alt27A);
        ba.setAlt52_1(alt52_1);
        ba.setDeclarant(declarant);
        alterationService.addBirthAlteration(ba, user);
        logger.debug("");
        return SUCCESS;
    }

    /**
     * this is responsible for loading the birth alteration
     * which are still in the pending state to be approved
     * by the ARG or higher authority
     *
     * @return
     */
    public String initBirthAlterationPendingList() {
        populateDistrictAndDSDivision();
        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        initPermission();
        return SUCCESS;
    }

    private void initPermission() {
        setAllowApproveAlteration(user.isAuthorized(Permission.APPROVE_BIRTH_ALTERATION));
    }

    private void populateBasicLists() {
        populateDistrictAndDSDivision();
        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, getLanguage(), user);
        if (birthDivisionId == 0) {
            birthDivisionId = bdDivisionList.keySet().iterator().next();
            logger.debug("first allowed BD Div in the list {} was set", birthDivisionId);
        }
    }

    private void populateDistrictAndDSDivision() {
        setLanguage(((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage());

        districtList = districtDAO.getDistrictNames(language, user);
        if (birthDistrictId == 0) {
            if (!districtList.isEmpty()) {
                birthDistrictId = districtList.keySet().iterator().next();
                logger.debug("first allowed district in the list {} was set", birthDistrictId);
            }
        }

        dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, getLanguage(), user);
        if (dsDivisionId == 0) {
            if (!dsDivisionList.isEmpty()) {
                dsDivisionId = dsDivisionList.keySet().iterator().next();
                logger.debug("first allowed DS Division in the list {} was set", dsDivisionId);
            }
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

    public int getFatherCountry() {
        return fatherCountry;
    }

    public int getMotherCountry() {
        return motherCountry;
    }

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
        logger.debug("setting DS Division: {}", dsDivisionId);
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

    public int getMotherRace() {
        return motherRace;
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


    public BirthRegistrationService getService() {
        return this.service;
    }

    public BDDivisionDAO getBDDivisionDAO() {
        return this.bdDivisionDAO;
    }

    public int getSectionOfAct() {
        return sectionOfAct;
    }

    public void setSectionOfAct(int sectionOfAct) {
        this.sectionOfAct = sectionOfAct;
    }

    public Alteration27 getAlt27() {
        return alt27;
    }

    public void setAlt27(Alteration27 alt27) {
        this.alt27 = alt27;
    }

    public Alteration27A getAlt27A() {
        return alt27A;
    }

    public void setAlt27A(Alteration27A alt27A) {
        this.alt27A = alt27A;
    }

    public Alteration52_1 getAlt52_1() {
        return alt52_1;
    }

    public void setAlt52_1(Alteration52_1 alt52_1) {
        this.alt52_1 = alt52_1;
    }

    public DeclarantInfo getDeclarant() {
        return declarant;
    }

    public void setDeclarant(DeclarantInfo declarant) {
        this.declarant = declarant;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public long getNicOrPin() {
        return nicOrPin;
    }

    public void setNicOrPin(long nicOrPin) {
        this.nicOrPin = nicOrPin;
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
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

    public boolean isAllowApproveAlteration() {
        return allowApproveAlteration;
    }

    public void setAllowApproveAlteration(boolean allowApproveAlteration) {
        this.allowApproveAlteration = allowApproveAlteration;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
