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
import java.lang.reflect.Array;

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
    private List birthAlterationApprovalList;

    private User user;
    private Alteration27 alt27;
    private Alteration27A alt27A;
    private Alteration52_1 alt52_1;
    private DeclarantInfo declarant;
    private Date dateReceived;
    private Long alterationSerialNo;
    private int[] index;

    private ChildInfo child;
    private FatherInfo father;
    private GrandFatherInfo grandFather;
    private MarriageInfo marriage;
    private AlterationInformatInfo informant;
    private NotifyingAuthorityInfo notifyingAuthority;
    private ConfirmantInfo confirmant;
    private BirthRegisterInfo register;


    private int pageNo;
    private int noOfRows;
    private long bdId;   // If present, it should be used to fetch a new BD instead of creating a new one (we are in edit mode)
    private Long nicOrPin;
    private String districtName;
    private String dsDivisionName;
    private String bdDivisionName;

    /*boolean values to    */
    private boolean editChildInfo;
    private boolean editMotherInfo;
    private boolean editInformantInfo;
    private boolean editFatherInfo;
    private boolean editMarriageInfo;
    private boolean editMothersNameAfterMarriageInfo;
    private boolean editGrandFatherInfo;


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
        alt27.setChildFullNameOfficialLang(bdf.getChild().getChildFullNameOfficialLang());
        alt27.setChildFullNameEnglish(bdf.getChild().getChildFullNameEnglish());
        switch (sectionOfAct) {
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
        ba.setAlt27(alt27); /*Child's full name is save in any act*/
        switch (sectionOfAct) {
            //case 2 is used to set alteration52_1
            case 2:
                alt52_1.setBirthDivision(bdDivisionDAO.getBDDivisionByPK(birthDivisionId));
                ba.setAlt52_1(alt52_1);
                break;
            //case 2 is used to set alteration27A
            case 3:
                ba.setAlt27A(alt27A); 
                break;
        }
        //set the idUKey of the bdf in alteration as bdId
        ba.setBdId(idUKey);
        ba.setDeclarant(declarant);
        ba.setDateReceived(dateReceived);
        ba.setAlterationSerialNo(alterationSerialNo);
        alterationService.addBirthAlteration(ba, user);
        logger.debug("Add a new Birth Alteration with Alteration Serial No  :{}", alterationSerialNo);
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
        /* birthAlterationPendingApprovalList = alterationService.getApprovalPendingByDSDivision(
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
        logger.debug("number of rows in Birth Alteration Approval List is :{}", birthAlterationPendingApprovalList.size());
        initPermission();
        populateBasicLists();
        return SUCCESS;
    }

    /**
     * responsible for approving a requested birth alteration field
     *
     * @return
     */
    public String approveInit() {
        //todo has to be implemented
        BirthDeclaration bdf = service.getById(bdId, user);
        BirthAlteration ba = alterationService.getById(idUKey, user);
        alt27 = ba.getAlt27();
        alt27A = ba.getAlt27A();
        alt52_1 = ba.getAlt52_1();

        child = bdf.getChild();

        if (child.getChildFullNameEnglish().equals(alt27.getChildFullNameEnglish()) &&
                child.getChildFullNameOfficialLang().equals(alt27.getChildFullNameOfficialLang())) {
            alt27 = null;
        }
        birthAlterationApprovalList = new ArrayList();
        if (alt27 != null) {
            birthAlterationApprovalList.add(alt27.getChildFullNameOfficialLang());
            birthAlterationApprovalList.add(alt27.getChildFullNameEnglish());
            sectionOfAct = 1;
            logger.debug("Child full name in English is :{}", alt27.getChildFullNameEnglish());
        }
        if (alt27A != null) {
            sectionOfAct = 2;
            changesOfAlt27A(bdf);
        }
        if (alt52_1 != null) {
            sectionOfAct = 3;
            changesOfAlt52_1(bdf);
        }
        initPermission();
        populateBasicLists();
        return SUCCESS;
    }

    private void changesOfAlt27A(BirthDeclaration bdf) {
        father = alt27A.getFather();
        ParentInfo parent;
        parent = bdf.getParent();
        String[] compareChanges;
        grandFather = alt27A.getGrandFather();
        if (grandFather != null) {
            GrandFatherInfo grandFatherOriginal = bdf.getGrandFather();
            compareAndAdd(Alteration27A.GRAND_FATHER_FULLNAME, grandFatherOriginal.getGrandFatherFullName(), grandFather.getGrandFatherFullName());
            compareAndAdd(Alteration27A.GRAND_FATHER_NIC_OR_PIN,
                    grandFatherOriginal.getGrandFatherNICorPIN(), grandFather.getGrandFatherNICorPIN());
            compareAndAdd(Alteration27A.GRAND_FATHER_BIRTH_YEAR, grandFatherOriginal.getGrandFatherBirthYear().toString(), grandFather.getGrandFatherBirthYear().toString());
            compareAndAdd(Alteration27A.GRAND_FATHER_BIRTH_PLACE, grandFatherOriginal.getGrandFatherBirthPlace(), grandFather.getGrandFatherBirthPlace());
            compareAndAdd(Alteration27A.GREAT_GRAND_FATHER_FULLNAME,
                    grandFatherOriginal.getGreatGrandFatherFullName(), grandFather.getGreatGrandFatherFullName());
            compareAndAdd(Alteration27A.GREAT_GRAND_FATHER_NIC_OR_PIN,
                    grandFatherOriginal.getGreatGrandFatherNICorPIN(), grandFather.getGreatGrandFatherNICorPIN());
            compareAndAdd(Alteration27A.GREAT_GRAND_FATHER_BIRTH_YEAR,
                    grandFatherOriginal.getGreatGrandFatherBirthYear().toString(), grandFather.getGreatGrandFatherBirthYear().toString());
            compareAndAdd(Alteration27A.GREAT_GRAND_FATHER_BIRTH_PLACE, grandFatherOriginal.getGreatGrandFatherBirthPlace(), grandFather.getGreatGrandFatherBirthPlace());
            logger.debug("Check and add to approval list all information of {} (GrandFather) of idUKey :{}", grandFather.getGrandFatherFullName(), idUKey);

        }
        if (father != null) {
            compareAndAdd(Alteration27A.FATHER_FULLNAME, parent.getFatherFullName(), father.getFatherFullName());
            compareAndAdd(Alteration27A.FATHER_NIC_OR_PIN, parent.getFatherNICorPIN(), father.getFatherNICorPIN());
            compareAndAdd(Alteration27A.FATHER_BIRTH_YEAR, parent.getFatherDOB().toString(), father.getFatherDOB().toString());
            compareAndAdd(Alteration27A.FATHER_BIRTH_PLACE, parent.getFatherPlaceOfBirth(), father.getFatherPlaceOfBirth());
            //if father country is not null in both ba and bdf
            if (father.getFatherCountry() != null && parent.getFatherCountry() != null) {
                compareAndAdd(Alteration27A.FATHER_COUNTRY, parent.getFatherCountry().getSiCountryName(), father.getFatherCountry().getSiCountryName());
            }
            //if father country in not null in ba but null in bdf
            if (father.getFatherCountry() != null && parent.getFatherCountry() == null) {
                compareAndAdd(Alteration27A.FATHER_COUNTRY, "", father.getFatherCountry().getSiCountryName());

            }
            //if father country in  null in ba but not null in bdf
            if (father.getFatherCountry() == null && parent.getFatherCountry() != null) {
                compareAndAdd(Alteration27A.FATHER_COUNTRY, parent.getFatherCountry().getSiCountryName(), "");
                logger.debug("Delete the country of the {} child's father  :{}", bdf.getChild().getChildFullNameOfficialLang(),
                        parent.getFatherCountry().getSiCountryName());
            }
            compareAndAdd(Alteration27A.FATHER_PASSPORT, parent.getFatherPassportNo(), father.getFatherPassportNo());
            if (father.getFatherRace() != null && parent.getFatherRace() != null) {
                compareAndAdd(Alteration27A.FATHER_RACE, father.getFatherRace().getEnRaceName(), parent.getFatherRace().getEnRaceName());
            }
            logger.debug("Check and add to approval list all information of {} (Father) of idUKey :{}", father.getFatherFullName(), idUKey);
        }
        marriage = alt27A.getMarriage();
        MarriageInfo marriageOriginal = bdf.getMarriage();
        if (marriage != null) {
            compareAndAdd(Alteration27A.WERE_PARENTS_MARRIED, marriageOriginal.getParentsMarried().toString(), marriage.getParentsMarried().toString());
            compareAndAdd(Alteration27A.PLACE_OF_MARRIAGE, marriageOriginal.getPlaceOfMarriage(), marriage.getPlaceOfMarriage());
            compareAndAdd(Alteration27A.DATE_OF_MARRIAGE, marriageOriginal.getDateOfMarriage().toString(), marriage.getDateOfMarriage().toString());
            logger.debug("Check and add to all field of Marriage to approval list of idUKey :{}", idUKey);

        }
        if (alt27A.getMothersNameAfterMarriage() != null) {
            compareChanges = new String[3];
            compareChanges[0] = Integer.toString(Alteration27A.MOTHER_NAME_AFTER_MARRIAGE);
            compareChanges[0] = parent.getMotherFullName();
            compareChanges[2] = alt27A.getMothersNameAfterMarriage();
            birthAlterationApprovalList.add(compareChanges);
            logger.debug("Check {} mother name after Marriage if idUKey :{}", compareChanges[0], idUKey);
        }

    }

    private void changesOfAlt52_1(BirthDeclaration bdf) {
        child = bdf.getChild();
        register = bdf.getRegister();
        if (alt52_1.getDateOfBirth() != null)
            compareAndAdd(Alteration52_1.DATE_OF_BIRTH, alt52_1.getDateOfBirth().toString(), child.getDateOfBirth().toString());
        if (alt52_1.getPlaceOfBirth() != null)
            compareAndAdd(Alteration52_1.PLACE_OF_BIRTH, alt52_1.getPlaceOfBirth(), child.getPlaceOfBirth());
        compareAndAdd(Alteration52_1.PLACE_OF_BIRTH_ENGLISH, alt52_1.getPlaceOfBirthEnglish(), child.getPlaceOfBirthEnglish());
        if (alt52_1.getBirthDivision() != null && register.getBirthDivision() != null)
            compareAndAdd(Alteration52_1.BIRTH_DIVISION, alt52_1.getBirthDivision().getEnDivisionName(), register.getBirthDivision().getEnDivisionName());
        if (alt52_1.getChildGender() > 0 && child.getChildGender() > 0)
            compareAndAdd(Alteration52_1.GENDER, Integer.toString(alt52_1.getChildGender()), Integer.toString(child.getChildGender()));
        if (alt52_1.getChildGender() > 0 && child.getChildGender() < 0) {
            String[] compareChanges = new String[3];
            compareChanges[0] = Integer.toString(Alteration52_1.GENDER);
            compareChanges[1] = "";
            compareChanges[2] = Integer.toString(alt52_1.getChildGender());
            birthAlterationApprovalList.add(compareChanges);
        }
        MotherInfo mother = alt52_1.getMother();
        if (mother != null) {
            ParentInfo parent = bdf.getParent();
            compareAndAdd(Alteration52_1.MOTHER_FULLNAME, parent.getMotherFullName(), mother.getMotherFullName());
            compareAndAdd(Alteration52_1.MOTHER_BIRTH_YEAR, parent.getMotherDOB().toString(), mother.getMotherDOB().toString());
            compareAndAdd(Alteration52_1.MOTHER_BIRTH_PLACE, parent.getMotherPlaceOfBirth(), mother.getMotherPlaceOfBirth());
            //if mother country is not null in bdf and ba
            if (mother.getMotherCountry() != null && parent.getMotherCountry() != null) {
                compareAndAdd(Alteration52_1.MOTHER_COUNTRY
                        , countryDAO.getNameByPK(parent.getMotherCountry().getCountryId(), language),
                        countryDAO.getNameByPK(mother.getMotherCountry().getCountryId(), language));
            }
            //todo if mother country is not null in bdf but null in ba
            compareAndAdd(Alteration52_1.MOTHER_PASSPORT, parent.getMotherPassportNo(), mother.getMotherPassportNo());
            //if mother race is not null in both bdf and ba
            if (mother.getMotherRace() != null && parent.getMotherRace() != null) {
                compareAndAdd(Alteration52_1.MOTHER_RACE, raceDAO.getRace(parent.getMotherRace().getRaceId()).getSiRaceName(),
                        raceDAO.getRace(mother.getMotherRace().getRaceId()).getSiRaceName());
            }
            compareAndAdd(Alteration52_1.MOTHER_AGE_AT_BIRTH, parent.getMotherAgeAtBirth().toString(), mother.getMotherAgeAtBirth().toString());
            compareAndAdd(Alteration52_1.MOTHER_ADDRESS, parent.getMotherAddress(), mother.getMotherAddress());

            //compare the informant information
            informant = alt52_1.getInformant();
            if (informant != null) {
                InformantInfo informantOriginal = bdf.getInformant();
                compareAndAdd(Alteration52_1.INFORMANT_TYPE, informantOriginal.getInformantType().name(), informant.getInformantType().name());
                compareAndAdd(Alteration52_1.INFORMANT_NIC_OR_PIN, informantOriginal.getInformantNICorPIN(), informant.getInformantNICorPIN());
                compareAndAdd(Alteration52_1.INFORMANT_NAME, informantOriginal.getInformantName(), informant.getInformantName());
                compareAndAdd(Alteration52_1.INFORMANT_ADDRESS, informantOriginal.getInformantAddress(), informant.getInformantAddress());
            }


        }
    }

    private void compareAndAdd(int index, String bdfName, String baName) {
        String[] compareChanges = new String[3];
        compareChanges[0] = Integer.toString(index);
        compareChanges[1] = bdfName;
        compareChanges[2] = baName;
        if (!compareChanges[2].equals(compareChanges[1])) {
            birthAlterationApprovalList.add(compareChanges);
        }
    }

    public String alterationApproval() {
        BirthAlteration ba = alterationService.getById(idUKey, user);
        boolean isAct27A = false;
        Hashtable approvalsBitSet = new Hashtable();
        if (sectionOfAct == 2) {
            isAct27A = true;
            int check = 0;
            for (int i = 1; i < WebConstants.BIRTH_ALTERATION_APPROVE_ALT27A; i++) {
                if (check < index.length) {
                    if (i == index[check]) {
                        //if a field is approved bit set to true
                        approvalsBitSet.put(i, true);
                        check++;
                    } else {
                        approvalsBitSet.put(i, false);
                    }
                } else {
                    approvalsBitSet.put(i, false);
                }
            }
            logger.debug("Change The alt27A bit set of the Birth Alteration idUKey :{}", idUKey);
        }
        logger.debug("length of the apprrovals list is  :{}", approvalsBitSet.size());
        alterationService.approveBirthAlteration(ba, isAct27A, approvalsBitSet, user);
        ba = alterationService.getById(idUKey, user);
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
        if (getRegister() == null) {
            setRegister(new BirthRegisterInfo());
        }
        getRegister().setBirthDivision(bdDivisionDAO.getBDDivisionByPK(birthDivisionId));
        logger.debug("setting BirthDivision: {}", getRegister().getBirthDivision().getEnDivisionName());
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

    public boolean isEditChildInfo() {
        return editChildInfo;
    }

    public void setEditChildInfo(boolean editChildInfo) {
        this.editChildInfo = editChildInfo;
    }

    public boolean isEditMotherInfo() {
        return editMotherInfo;
    }

    public void setEditMotherInfo(boolean editMotherInfo) {
        this.editMotherInfo = editMotherInfo;
    }

    public boolean isEditInformantInfo() {
        return editInformantInfo;
    }

    public void setEditInformantInfo(boolean editInformantInfo) {
        this.editInformantInfo = editInformantInfo;
    }

    public boolean isEditFatherInfo() {
        return editFatherInfo;
    }

    public void setEditFatherInfo(boolean editFatherInfo) {
        this.editFatherInfo = editFatherInfo;
    }

    public boolean isEditMarriageInfo() {
        return editMarriageInfo;
    }

    public void setEditMarriageInfo(boolean editMarriageInfo) {
        this.editMarriageInfo = editMarriageInfo;
    }

    public boolean isEditMothersNameAfterMarriageInfo() {
        return editMothersNameAfterMarriageInfo;
    }

    public void setEditMothersNameAfterMarriageInfo(boolean editMothersNameAfterMarriageInfo) {
        this.editMothersNameAfterMarriageInfo = editMothersNameAfterMarriageInfo;
    }

    public boolean isEditGrandFatherInfo() {
        return editGrandFatherInfo;
    }

    public void setEditGrandFatherInfo(boolean editGrandFatherInfo) {
        this.editGrandFatherInfo = editGrandFatherInfo;
    }

    public ChildInfo getChild() {
        return child;
    }

    public void setChild(ChildInfo child) {
        this.child = child;
    }

    public GrandFatherInfo getGrandFather() {
        return grandFather;
    }

    public void setGrandFather(GrandFatherInfo grandFather) {
        this.grandFather = grandFather;
    }

    public MarriageInfo getMarriage() {
        return marriage;
    }

    public void setMarriage(MarriageInfo marriage) {
        this.marriage = marriage;
    }

    public AlterationInformatInfo getInformant() {
        return informant;
    }

    public void setInformant(AlterationInformatInfo informant) {
        this.informant = informant;
    }

    public NotifyingAuthorityInfo getNotifyingAuthority() {
        return notifyingAuthority;
    }

    public void setNotifyingAuthority(NotifyingAuthorityInfo notifyingAuthority) {
        this.notifyingAuthority = notifyingAuthority;
    }

    public ConfirmantInfo getConfirmant() {
        return confirmant;
    }

    public void setConfirmant(ConfirmantInfo confirmant) {
        this.confirmant = confirmant;
    }

    public BirthRegisterInfo getRegister() {
        return register;
    }

    public void setRegister(BirthRegisterInfo register) {
        this.register = register;
    }

    public List getBirthAlterationApprovalList() {
        return birthAlterationApprovalList;
    }

    public void setBirthAlterationApprovalList(List birthAlterationApprovalList) {
        this.birthAlterationApprovalList = birthAlterationApprovalList;
    }

    public FatherInfo getFather() {
        return father;
    }

    public void setFather(FatherInfo father) {
        this.father = father;
    }

    public int[] getIndex() {
        return index;
    }

    public void setIndex(int[] index) {
        this.index = index;
    }
}
