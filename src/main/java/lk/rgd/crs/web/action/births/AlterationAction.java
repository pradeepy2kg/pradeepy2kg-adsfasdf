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

import java.util.*;

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
    private AppParametersDAO appParametersDAO;
    private static final String BA_APPROVAL_ROWS_PER_PAGE = "crs.br_approval_rows_per_page";

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
    private Date dateReceived;
    private Long alterationSerialNo;


    private int pageNo;
    private int noOfRows;
    private long bdId;   // If present, it should be used to fetch a new BD instead of creating a new one (we are in edit mode)
    private Long nicOrPin;
    private String districtName;
    private String dsDivisionName;
    private String bdDivisionName;

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    private int birthDistrictId;
    private int birthDivisionId;
    private int fatherCountry;
    private int motherCountry;
    private int fatherRace;
    private int motherRace;
    private int dsDivisionId;
    private int sectionOfAct;
    private Long idUKey;
    private long serialNo; //to be used in the case where search is performed from confirmation 1 page.
    private boolean allowApproveAlteration;
    private boolean nextFlag;
    private boolean previousFlag;
    private List<BirthAlteration> birthAlterationPendingApprovalList;

    private String language;

    public AlterationAction(BirthRegistrationService service, DistrictDAO districtDAO, CountryDAO countryDAO, RaceDAO raceDAO, BDDivisionDAO bdDivisionDAO,
                            DSDivisionDAO dsDivisionDAO, BirthAlterationService alterationService, AppParametersDAO appParametersDAO) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.alterationService = alterationService;
        this.appParametersDAO = appParametersDAO;
    }


    public String initBirthAlteration() {
        pageNo = 0;
        populateBasicLists();

        return SUCCESS;
    }

    public String birthAlterationSearch() {
        BirthDeclaration bdf = new BirthDeclaration();
        populateBasicLists();
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
        try {
            idUKey = bdf.getIdUKey();
            nicOrPin = bdf.getChild().getPin();
            serialNo = bdf.getRegister().getBdfSerialNo();
            String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
            districtName = districtDAO.getNameByPK(birthDistrictId, language);
            dsDivisionName = dsDivisionDAO.getNameByPK(dsDivisionId, language);
            bdDivisionName = bdDivisionDAO.getNameByPK(birthDivisionId, language);
        }
        catch (Exception e) {
            handleErrors(e);
            addActionError(getText("cp1.error.entryNotAvailable"));
            pageNo = 0;
            return SUCCESS;
        }
        // check that birth Certificate is printed

        populateAlteration(bdf);
        pageNo = 1;
        populateBasicLists();
        populateCountryRacesAndAllDSDivisions();
        return SUCCESS;
    }

    private void populateAlteration(BirthDeclaration bdf) {
        alt27 = new Alteration27();
        alt27A = new Alteration27A();
        alt52_1 = new Alteration52_1();
        switch (sectionOfAct) {
            //set alt27
            case 1:
                alt27.setChildFullNameOfficialLang(bdf.getChild().getChildFullNameOfficialLang());
                alt27.setChildFullNameEnglish(bdf.getChild().getChildFullNameEnglish());
                break;
            //set alt52_1
            case 2:
                InformantInfo bdfInformant = bdf.getInformant();
                alt52_1.setInformant(new AlterationInformatInfo(bdfInformant.getInformantType(),
                        bdfInformant.getInformantName(), bdfInformant.getInformantNICorPIN(), bdfInformant.getInformantAddress()));
                alt52_1.setChildGender(bdf.getChild().getChildGender());
                alt52_1.setDateOfBirth(bdf.getChild().getDateOfBirth());
                alt52_1.setPlaceOfBirth(bdf.getChild().getPlaceOfBirth());
                alt52_1.setPlaceOfBirthEnglish(bdf.getChild().getPlaceOfBirthEnglish());
                MotherInfo mother = new MotherInfo();
                mother.setMotherAddress(bdf.getParent().getMotherAddress());
                mother.setMotherAgeAtBirth(bdf.getParent().getMotherAgeAtBirth());
                mother.setMotherDOB(bdf.getParent().getMotherDOB());
                mother.setMotherFullName(bdf.getParent().getMotherFullName());
                mother.setMotherNICorPIN(bdf.getParent().getMotherNICorPIN());
                mother.setMotherPassportNo(bdf.getParent().getMotherPassportNo());
                alt52_1.setMother(mother);
                logger.debug("Loaded  Mother NIC or PIN Number of the {} is :{} ",
                        alt52_1.getMother().getMotherFullName(), alt52_1.getMother().getMotherNICorPIN());
                if (bdf.getParent().getMotherCountry() != null) {
                    motherCountry = bdf.getParent().getMotherCountry().getCountryId();
                }
                if (bdf.getParent().getMotherRace() != null) {
                    motherRace = bdf.getParent().getMotherRace().getRaceId();
                }
                break;
            case 3:
                //set alt27A
                FatherInfo father = new FatherInfo();
                father.setFatherDOB(bdf.getParent().getFatherDOB());
                father.setFatherFullName(bdf.getParent().getFatherFullName());
                father.setFatherNICorPIN(bdf.getParent().getFatherNICorPIN());
                father.setFatherPassportNo(bdf.getParent().getFatherPassportNo());
                father.setFatherPlaceOfBirth(bdf.getParent().getFatherPlaceOfBirth());
                father.setFatherRace(bdf.getParent().getFatherRace());
                if (bdf.getParent().getFatherCountry() != null) {
                    fatherCountry = bdf.getParent().getFatherCountry().getCountryId();
                }
                if (bdf.getParent().getFatherRace() != null) {
                    fatherRace = bdf.getParent().getFatherRace().getRaceId();
                }
                alt27A.setFather(father);
                alt27A.setGrandFather(bdf.getGrandFather());
                alt27A.setMarriage(bdf.getMarriage());
                break;

        }
        birthDistrictId = bdf.getRegister().getBirthDistrict().getDistrictUKey();
        birthDivisionId = bdf.getRegister().getBirthDivision().getBdDivisionUKey();
        dsDivisionId = bdf.getRegister().getDsDivision().getDsDivisionUKey();
    }


    public String birthAlteration() {
        BirthAlteration ba = new BirthAlteration();
        BitSet bitSet = new BitSet();
        switch (sectionOfAct) {
            //case 1 is used to set alteration27
            case 1:
                alt27.setFullNameOfficialLangApproved(false);
                alt27.setFullNameEnglishApproved(false);
                ba.setAlt27(alt27);
                break;
            //case 2 is used to set alteration52_1
            case 2:
                alt52_1.setBirthDivision(bdDivisionDAO.getBDDivisionByPK(birthDivisionId));
                alt52_1.setApprovalStatuses(bitSet);
                ba.setAlt52_1(alt52_1);
                break;
            //case 2 is used to set alteration27A
            case 3:
                alt27A.setApprovalStatuses(bitSet);
                ba.setAlt27A(alt27A);
                break;
        }
        logger.debug("value of the idUkey  :{}", idUKey);
        ba.setBdId(idUKey);
        ba.setDeclarant(declarant);
        ba.setDateReceived(dateReceived);
        ba.setAlterationSerialNo(alterationSerialNo);
        alterationService.addBirthAlteration(ba, user);
        return SUCCESS;
    }

    /**
     * this is responsible for loading the birth alteration
     * which are still in the pending state to be approved
     * by the ARG or higher authority
     *
     * @return
     */
    public String initBirthAlterationPendingApprovalList() {
        populateDistrictAndDSDivision();
        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        noOfRows = appParametersDAO.getIntParameter(BA_APPROVAL_ROWS_PER_PAGE);
        setPageNo(1);
        /*birthAlterationPendingApprovalList = alterationService.getApprovalPendingByDSDivision(
                dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);*/
        initPermission();
        return SUCCESS;
    }

    /**
     * responsible for filtering requested birth alteration by its
     * birth division or DS division
     *
     * @return
     */
    public String filter() {
        setPageNo(1);
        noOfRows = appParametersDAO.getIntParameter(BA_APPROVAL_ROWS_PER_PAGE);
        if (birthDivisionId != 0) {
            logger.debug("requested to filter birth alterations by birthDivisionId : {} ", birthDivisionId);
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByBDDivision(
                    bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, user);
        } else {
            logger.debug("requested to filter birth alterations by dsDivisionId : {}", dsDivisionId);
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByDSDivision(
                    dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        }
        paginationHandler(birthAlterationPendingApprovalList.size());
        initPermission();
        populateBasicLists();
        return SUCCESS;
    }

    /**
     * responsible for approving a requested birth alteration field
     *
     * @return
     */
    public String approve() {
        //todo has to be implemented
        initPermission();
        populateBasicLists();
        return SUCCESS;
    }

    private void initPermission() {
        setAllowApproveAlteration(user.isAuthorized(Permission.APPROVE_BIRTH_ALTERATION));
    }

    /**
     * handles pagination of BirthAlterations which are to be displayed in jsp
     *
     * @return String
     */
    public String nextPage() {
        if (logger.isDebugEnabled()) {
            logger.debug("inside nextPage() : current birthDistrictId {}, birthDivisionId {}", birthDistrictId, birthDivisionId +
                    " requested from pageNo " + pageNo);
        }
        setPageNo(getPageNo() + 1);

        noOfRows = appParametersDAO.getIntParameter(BA_APPROVAL_ROWS_PER_PAGE);
        /**
         * gets the user selected district to get the records
         * variable nextFlag is used to handle the pagination link
         * in the jsp page
         */
        if (birthDivisionId != 0) {
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByBDDivision(
                    bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, user);
        } else {
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByDSDivision(
                    dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        }
        paginationHandler(birthAlterationPendingApprovalList.size());
        setPreviousFlag(true);
        populateBasicLists();
        initPermission();
        return SUCCESS;
    }

    /**
     * handles pagination of BirthAlteration approval pending data
     *
     * @return String
     */
    public String previousPage() {

        if (logger.isDebugEnabled()) {
            logger.debug("inside previousPage() : current birthDistrictId {}, birthDivisionId {} ", birthDistrictId, birthDivisionId
                    + " requested from pageNo " + pageNo);
        }
        /**
         * UI related. decides whether to display
         * next and previous links
         */
        if (previousFlag && getPageNo() == 2) {
            /**
             * request is comming backword(calls previous
             * to load the very first page
             */
            setPreviousFlag(false);
        } else if (getPageNo() == 1) {
            /**
             * if request is from page one
             * in the next page previous link
             * should be displayed
             */
            setPreviousFlag(false);
        } else {
            setPreviousFlag(true);
        }
        setNextFlag(true);
        if (getPageNo() > 1) {
            setPageNo(getPageNo() - 1);
        }
        noOfRows = appParametersDAO.getIntParameter(BA_APPROVAL_ROWS_PER_PAGE);

        if (birthDivisionId != 0) {
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByBDDivision(
                    bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, user);
        } else {
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByDSDivision(
                    dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        }
        populateBasicLists();
        initPermission();
        return SUCCESS;
    }

    private void paginationHandler(int recordsFound) {
        if (recordsFound == appParametersDAO.getIntParameter(BA_APPROVAL_ROWS_PER_PAGE)) {
            setNextFlag(true);
        } else {
            setNextFlag(false);
        }
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

    private void populateCountryRacesAndAllDSDivisions() {
        countryList = countryDAO.getCountries(language);
        districtList = districtDAO.getDistrictNames(language, user);
        raceList = raceDAO.getRaces(language);

        /** getting full district list and DS list */
        allDistrictList = districtDAO.getAllDistrictNames(language, user);
    }

    private void handleErrors(Exception e) {
        logger.error("Handle Error  ", e);
        //todo pass the error to the error.jsp page
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

    public Long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(Long idUKey) {
        this.idUKey = idUKey;
    }

    public Long getNicOrPin() {
        return nicOrPin;
    }

    public void setNicOrPin(Long nicOrPin) {
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

    public List<BirthAlteration> getBirthAlterationPendingApprovalList() {
        return birthAlterationPendingApprovalList;
    }

    public void setBirthAlterationPendingApprovalList(List<BirthAlteration> birthAlterationPendingApprovalList) {
        this.birthAlterationPendingApprovalList = birthAlterationPendingApprovalList;
    }

    public int getNoOfRows() {
        return noOfRows;
    }

    public void setNoOfRows(int noOfRows) {
        this.noOfRows = noOfRows;
    }

    public boolean isNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(boolean nextFlag) {
        this.nextFlag = nextFlag;
    }

    public boolean isPreviousFlag() {
        return previousFlag;
    }

    public void setPreviousFlag(boolean previousFlag) {
        this.previousFlag = previousFlag;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDsDivisionName() {
        return dsDivisionName;
    }

    public void setDsDivisionName(String dsDivisionName) {
        this.dsDivisionName = dsDivisionName;
    }

    public String getBdDivisionName() {
        return bdDivisionName;
    }

    public void setBdDivisionName(String bdDivisionName) {
        this.bdDivisionName = bdDivisionName;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public Long getAlterationSerialNo() {
        return alterationSerialNo;
    }

    public void setAlterationSerialNo(Long alterationSerialNo) {
        this.alterationSerialNo = alterationSerialNo;
    }
}
