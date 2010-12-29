package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.AppConstants;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthAlterationService;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.core.service.BirthAlterationValidator;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.CommonUtil;
import lk.rgd.crs.web.util.FieldValue;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
//todo remove unused variables

/**
 * @author tharanga
 */
public class BirthAlterationAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthAlterationAction.class);
    private static final String BA_APPROVAL_ROWS_PER_PAGE = "crs.br_approval_rows_per_page";

    private BirthRegistrationService service;
    private BirthAlterationService alterationService;
    private DistrictDAO districtDAO;
    private CountryDAO countryDAO;
    private RaceDAO raceDAO;
    private BDDivisionDAO bdDivisionDAO;
    private DSDivisionDAO dsDivisionDAO;
    private AppParametersDAO appParametersDAO;
    private BirthAlterationValidator birthAlterationValidator;
    private final CommonUtil commonUtil;

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> allDistrictList;
    private Map<Integer, String> allDsDivisionList;
    private Map<Integer, String> allBdDivisionList;
    private Map<Integer, String> userLocations;
    private Map<Integer, Boolean> alterationApprovalPermission;

    private List<String[]> birthAlterationApprovalList;           //no usage remove amith
    private List<FieldValue> changesList = new LinkedList<FieldValue>();
    private List birthAlterationApprovedList;    //no usage remove amith

    private User user;
    private Alteration27 alt27;
    private Alteration27A alt27A;
    private Alteration52_1 alt52_1;
    private DeclarantInfo declarant;
    private ParentInfo parent;
    private int[] index;   //no usage remove amith
    private BitSet indexCheck; //no usage remove amith
    private HashMap approveStatus = new HashMap();  //no usage remove amith
    private int numberOfAppPending; //no usage remove amith

    private ChildInfo child;
    private FatherInfo father;
    private GrandFatherInfo grandFather;
    private MarriageInfo marriage;
    private AlterationInformatInfo informant;
    private NotifyingAuthorityInfo notifyingAuthority;
    private ConfirmantInfo confirmant;
    private BirthRegisterInfo register;
    private BirthAlteration birthAlteration;
    private BirthDeclaration birthDeclaration;


    private int pageNo;
    private int pageType;     //todo no need any more remove
    private int noOfRows;
    private int[] approvedIndex;
    private long bdId;   // If present, it should be used to fetch a new BD instead of creating a new one (we are in edit mode)
    private Long nicOrPin;
    private String districtName;
    private String dsDivisionName;
    private String bdDivisionName;
    private String originalName;
    private String comment;

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    private int birthDistrictId;
    private int birthDivisionId;
    private int fatherCountryId;
    private int motherCountryId;
    private int fatherRaceId;
    private int motherRaceId;
    private int dsDivisionId;
    private int sectionOfAct;
    private int locationUKey;

    private long idUKey;
    private long serialNo; //to be used in the case where search is performed from confirmation 1 page.
    private boolean allowApproveAlteration;
    private boolean nextFlag;
    private boolean previousFlag;
    private List<BirthAlteration> birthAlterationPendingApprovalList;
    private int divisionAltaration;
    private Date alterationRecivedDate;
    private BirthAlteration.AlterationType alterationType;
    private long birthCertificateNumber;


    private String language;
    private boolean applyChanges;
    private boolean approveRightsToUser;
    private boolean editChildInfo;
    private boolean editMotherInfo;
    private boolean editInformantInfo;
    private boolean editFatherInfo;
    private boolean editMarriageInfo;
    private boolean editMothersNameAfterMarriageInfo;
    private boolean editGrandFatherInfo;
    private boolean editMode;


    public BirthAlterationAction(BirthRegistrationService service, DistrictDAO districtDAO, CountryDAO countryDAO,
        RaceDAO raceDAO, BDDivisionDAO bdDivisionDAO, DSDivisionDAO dsDivisionDAO, BirthAlterationService alterationService,
        AppParametersDAO appParametersDAO, CommonUtil commonUtil, BirthAlterationValidator birthAlterationValidator) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.alterationService = alterationService;
        this.appParametersDAO = appParametersDAO;
        this.commonUtil = commonUtil;
        this.birthAlterationValidator = birthAlterationValidator;
    }


    public String initBirthAlteration() {
        pageNo = 0;
        populateBasicLists();
        return SUCCESS;
    }

    //load birth alteration home page

    public String birthAlterationHome() {
        return SUCCESS;
    }

    /**
     * searching a birth certificate for and alteration
     */
    //todo change method name to searchBirthCertificateForAlteration
    public String birthAlterationSearch() {
        BirthDeclaration bdf = null;
        birthAlteration = new BirthAlteration();
        populateBasicLists();
        if (birthCertificateNumber != 0) {
            logger.debug("attempt to search birth certificate by birth certificate number : {}", birthCertificateNumber);
            bdf = service.getById(birthCertificateNumber);
        } else if (nicOrPin != null) {
            logger.debug("attempt to search birth certificate by identification number : {}", nicOrPin);
            bdf = service.getByPINorNIC(nicOrPin, user);
        } else if (birthDivisionId != 0 && serialNo != 0) {
            logger.debug("attempt to search birth certificate by BD division : {} and Serial number : {} ",
                birthDivisionId, serialNo);
            bdf = service.getActiveRecordByBDDivisionAndSerialNo(bdDivisionDAO.getBDDivisionByPK(birthDivisionId),
                serialNo, user);
        }
        if (bdf == null) {
            logger.debug("unable to found birth record for alteration ");
            addActionError(getText("cp1.error.entryNotAvailable"));
            populateBasicLists();
            return ERROR;
        } else {
            try {
                birthAlterationValidator.checkOnGoingAlterationOnThisSection(bdf.getIdUKey(), alterationType, user);
            }
            catch (CRSRuntimeException e) {
                addActionError(getText("error.ongoing.alteration.on.this.section", new String[]{"" + idUKey}));
                populateBasicLists();
                return ERROR;
            }
            if (bdf.getRegister() != null) {
                if (!(bdf.getRegister().getStatus() == BirthDeclaration.State.ARCHIVED_CERT_PRINTED)) {
                    addActionError(getText("cp1.error.entryNotPrinted"));
                    populateBasicLists();
                    return ERROR;
                } else {
                    getBirthCertificateInfo(bdf);
                }
            }
            switch (alterationType) {
                case TYPE_27: {
                    parent = bdf.getParent();
                    Alteration27 alt27 = new Alteration27();
                    alt27.setChildFullNameOfficialLang(bdf.getChild().getChildFullNameOfficialLang());
                    alt27.setChildFullNameEnglish(bdf.getChild().getChildFullNameEnglish());
                    birthAlteration.setAlt27(alt27);
                }
                break;
                case TYPE_27A:
                    populateAlt27A(bdf);
                    break;
                default: {
                    alt52_1 = new Alteration52_1();
                    populateAlt52_1(bdf);
                }
            }
            populateBasicLists();
            populateCountryRacesAndAllDSDivisions();
        }
        idUKey = 0;
        return SUCCESS;
    }

    /**
     * adding birth alteration
     */
    public String addBirthAlteration() {
        logger.debug("attempt to add birth alteration ");
        //todo check existing serial number
        switch (alterationType) {
            case TYPE_27: {
            }
            break;
            case TYPE_27A: {
                //TYPE 27A alteration
                if (fatherCountryId > 0) {
                    birthAlteration.getAlt27A().getFather().setFatherCountry(countryDAO.getCountry(fatherCountryId));
                }
                if (fatherRaceId > 0) {
                    birthAlteration.getAlt27A().getFather().setFatherRace(raceDAO.getRace(fatherRaceId));
                }
            }
            break;
            case TYPE_52_1_A:
            case TYPE_52_1_B:
            case TYPE_52_1_E:
            case TYPE_52_1_H:
            case TYPE_52_1_I:
            case TYPE_52_1_D: {
                //populate 52_1 object
                if (divisionAltaration > 0) {
                    birthAlteration.getAlt52_1().setBirthDivision(bdDivisionDAO.getBDDivisionByPK(divisionAltaration));
                }
                if (motherCountryId > 0) {
                    birthAlteration.getAlt52_1().getMother().setMotherCountry(countryDAO.getCountry(motherCountryId));
                }
                if (motherRaceId > 0) {
                    birthAlteration.getAlt52_1().getMother().setMotherRace(raceDAO.getRace(motherRaceId));
                }
            }
        }
        birthAlteration.setType(alterationType);
        if (birthDivisionId > 0) {
            birthAlteration.setBirthRecordDivision(bdDivisionDAO.getBDDivisionByPK(birthDivisionId));
        }
        birthAlteration.setBdfIDUKey(bdId);
        //todo remove           from here
        alterationService.addBirthAlteration(birthAlteration, user);
        approveRightsToUser = user.isAllowedAccessToBDDSDivision(birthAlteration.getBirthRecordDivision().
            getDsDivision().getDsDivisionUKey());
        idUKey = birthAlteration.getIdUKey();
        bdId = birthAlteration.getBdfIDUKey();
        pageType = 1;
        //todo remove           to here        
        logger.debug("successfully add birth alteration ");
        return SUCCESS;
    }

    /**
     * init editing birth alteration
     */
    //todo remove BDID which is passing through the URL it is no need to this
    public String editBirthAlterationInit() {
        logger.debug("attempt to load edit page for  birth alteration idUKey : {}", idUKey);
        if (idUKey != 0) {
            birthAlteration = alterationService.getByIDUKey(idUKey, user);
        }
        if (birthAlteration != null) {
            bdId = birthAlteration.getBdfIDUKey();
            BirthDeclaration bdf = service.getById(bdId);
            pageType = 1;  // what is this variable
            //information for search option
            if (bdf != null) {
                getBirthCertificateInfo(bdf);
                birthAlteration.setStatus(BirthAlteration.State.DATA_ENTRY);
            }
            if (birthAlteration.getType() == BirthAlteration.AlterationType.TYPE_27A) {
                populateAlt27A(bdf);
                sectionOfAct = 3;
            } else if (birthAlteration.getType() == BirthAlteration.AlterationType.TYPE_27) {
                alt27 = birthAlteration.getAlt27();
                sectionOfAct = 1;
            } else if (birthAlteration.getType() != BirthAlteration.AlterationType.TYPE_27
                && birthAlteration.getType() != BirthAlteration.AlterationType.TYPE_27A) {
                sectionOfAct = 2;
                populateAlt52_1(bdf);
            }
        }
        populateBasicLists();
        populateCountryRacesAndAllDSDivisions();
        alterationType = birthAlteration.getType();
        editMode = true;
        return SUCCESS;
        //todo what happen if no record found to edit       no error massage
    }

    /**
     * edit birth alteration
     */
    public String editBirthAlteration() {
        logger.debug("attempt to edit birth alteration for idUKey : {} ", idUKey);
        //loading existing alteration object
        BirthAlteration existingBirthAlteration = null;
        if (idUKey != 0) {
            existingBirthAlteration = alterationService.getByIDUKey(idUKey, user);
        }
        if (existingBirthAlteration != null) {
            try {
                //now we found existing alteration now we populate existing alteration with updated values
                populateBirthAlterationForUpdate(existingBirthAlteration, birthAlteration);
                //todo call to a edit service method amith following is temporary solution
                alterationService.updateBirthAlteration(existingBirthAlteration, user);
                logger.debug("birth alteration update success fully : {}", idUKey);
            }
            catch (CRSRuntimeException exception) {
                logger.debug("unable to update birth alteration idUKey : {}", idUKey);
                addActionError(getText("error.unable.to.edit", new String[]{"" + idUKey}));
                return ERROR;
            }
        } else {
            logger.debug("unable to find birth alteration record for the alteration idUKey : {}", idUKey);
            addActionError(getText("error.unable.to.edit", new String[]{"" + idUKey}));
            return ERROR;
        }
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
        logger.debug("attempt to load pending list for birth alterations");
        populateDistrictAndDSDivision();
        noOfRows = appParametersDAO.getIntParameter(BA_APPROVAL_ROWS_PER_PAGE);
        pageNo = 1;
        populateBasicLists();
        //   initPermission();   todo remove no usage those check are has to be done at service level
        filterBirthAlteration();
        logger.debug("successfully load the pending list for birth alterations");
        return SUCCESS;
    }

    /**
     * searching birth alterations
     */
    private void filterBirthAlteration() {
        setPageNo(1);
        noOfRows = appParametersDAO.getIntParameter(BA_APPROVAL_ROWS_PER_PAGE);
        try {
            if (birthCertificateNumber != 0) {
                BirthAlteration baApprovalPending = alterationService.getApprovalPendingByIdUKey
                    (idUKey, pageNo, noOfRows, user);
                if (birthAlterationPendingApprovalList == null) {
                    birthAlterationPendingApprovalList = new ArrayList<BirthAlteration>();
                }
                birthAlterationPendingApprovalList.add(baApprovalPending);
                logger.debug("filter Birth Alteration to approve by Birth Alteration Serial Number :{}", idUKey);
            } else if (locationUKey != 0) {
                logger.debug("filter Birth Alteration to approve by idUKey of the location :{}", locationUKey);
                birthAlterationPendingApprovalList = alterationService.getApprovalPendingByUserLocationIdUKey(
                    locationUKey, pageNo, noOfRows, user);
            } else if (birthDivisionId != 0) {
                logger.debug("filter Birth Alteration to approve by Birth Serial Number :{}", serialNo);
                birthAlterationPendingApprovalList = alterationService.getApprovalPendingByBDDivision(
                    bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows);

            }
        } catch (Exception CRSRuntimeException) {
            addActionError(getText("cp1.error.entryNotAvailable"));
            logger.debug("User {} can not filter birth alteration with idUKey :{}", user.getUserId(), idUKey);
        }

        if (birthAlterationPendingApprovalList != null) {
            paginationHandler(birthAlterationPendingApprovalList.size());
            logger.debug("number of rows in Birth Alteration Approval List is :{}",
                birthAlterationPendingApprovalList.size());
        } else {
            logger.info("The Birth Alteration List is empty");
        }
    }

    /**
     * responsible for filtering requested birth alteration by its
     * birth division or DS division
     *
     * @return
     */
    /*   public String filter() {
        logger.debug("attempt to filter birth alteration pending list ");
        setPageNo(1);
        noOfRows = appParametersDAO.getIntParameter(BA_APPROVAL_ROWS_PER_PAGE);
        filterBirthAlteration();
        if (birthAlterationPendingApprovalList != null) {
            paginationHandler(birthAlterationPendingApprovalList.size());
            logger.debug("number of rows in Birth Alteration Approval List is :{}",
                birthAlterationPendingApprovalList.size());
        } else {
            logger.info("The Birth Alteration List is empty");
        }
        //   checkPermissionToApproval();      todo remove
        // initPermission();
        populateBasicLists();
        return SUCCESS;
    }*/

    /**
     * init approval of a birth alteration
     */
    //todo remove bdid parameter is unused here
    public String approveInit() {
        logger.debug("attempt to init approve  birth alteration : idUKey {} ", idUKey);
        BirthDeclaration bdf = new BirthDeclaration();
        birthAlteration = alterationService.getByIDUKey(idUKey, user);
        if (birthAlteration != null) {
            logger.debug("attempt to compare alteration with alteration type : {} ", birthAlteration.getType());
            //find original birth record for the alteration
            bdf = service.getById(birthAlteration.getBdfIDUKey());
            String language = bdf.getRegister().getPreferredLanguage();
            switch (birthAlteration.getType()) {
                case TYPE_27:
                    alt27 = birthAlteration.getAlt27();
                    //only compare child name in 27
                    changesOfAlt27(birthDeclaration, language);
                    break;
                case TYPE_27A:
                    alt27A = birthAlteration.getAlt27A();
                    changesOfAlt27A(bdf, language);
                    break;
                case TYPE_52_1_A:
                case TYPE_52_1_B:
                case TYPE_52_1_D:
                case TYPE_52_1_E:
                case TYPE_52_1_H:
                case TYPE_52_1_I: {
                    alt52_1 = birthAlteration.getAlt52_1();
                    changesOfAlt52_1(bdf, language);
                }
            }
        } else {
            logger.debug("unable to found a birth alteration for alteration idUKey : {} ", idUKey);
            populateBasicLists();
            filterBirthAlteration();
            addActionError(getText("error.unable.to.find.birth.alteration.for.approval"));
            return ERROR;
        }
        populateBasicLists();

        return SUCCESS;
    }
    //todo use a one private method for above and below two methods       amith

    /**
     * load birth alteration notice
     */
    public String printBirthAlterationNotice() {
        logger.debug("attempt to print birth alteration notice ,alteration idUKey : {}", idUKey);
        birthAlteration = alterationService.getByIDUKey(idUKey, user);
        if (birthAlteration != null) {
            BirthDeclaration birthRegister = service.getById(birthAlteration.getBdfIDUKey());
            String preferedLan = birthRegister.getRegister().getPreferredLanguage();
            switch (birthAlteration.getType()) {
                case TYPE_27:
                    alt27 = birthAlteration.getAlt27();
                    //only compare child name in 27
                    changesOfAlt27(birthRegister, preferedLan);
                    break;
                case TYPE_27A:
                    alt27A = birthAlteration.getAlt27A();
                    changesOfAlt27A(birthRegister, preferedLan);
                    break;
                default: {
                    alt52_1 = birthAlteration.getAlt52_1();
                    changesOfAlt52_1(birthRegister, preferedLan);
                }
            }
        } else {
            logger.debug("unable to found birth alteration for print notice idUKey : {} ", idUKey);
            populateBasicLists();
            filterBirthAlteration();
            addActionError(getText("error.unable.to.find.birth.alteration.for.print.notice"));
            return ERROR;
        }
        populateBirthAlterationNotice(birthAlteration);
        addActionMessage(getText("message.success.print.birth.alteration.notice"));
        logger.debug("successfully printed alteration notice for alteration idUKey : {} ", idUKey);
        return SUCCESS;
    }


    /**
     * approving birth alteration
     */
    public String approveBirthAlteration() {
        logger.debug("attempt to approve birth alteration with approval bit set : {} ", approvedIndex);
        birthAlteration = alterationService.getByIDUKey(idUKey, user);
        if (birthAlteration != null) {
            try {
                Hashtable<Integer, Boolean> approveBitset = new Hashtable<Integer, Boolean>();
                if (approvedIndex != null) {
                    for (int i = 0; i < approvedIndex.length; i++) {
                        int bit = approvedIndex[i];
                        approveBitset.put(bit, true);
                    }
                }
                alterationService.approveBirthAlteration(birthAlteration, approveBitset, user);
            }
            catch (CRSRuntimeException e) {
                logger.error("cannot set bit set for birth alteration : {}", idUKey);
                populateBasicLists();
                filterBirthAlteration();
                return ERROR;
            }
        } else {
            logger.debug("unable to find birth alteration for approving : {} ", idUKey);
            populateBasicLists();
            filterBirthAlteration();
            return ERROR;
        }

        logger.debug("apply changes to birth alteration : alteration id  {}", idUKey);
        populateBasicLists();
        filterBirthAlteration();
        return SUCCESS;
    }


    /**
     * init birth alteration reject page
     */
    public String rejectBirthAlterationInit() {
        logger.debug("attempt load birth alteration get comment page for rejecting birth alteration idUKey : {}", idUKey);
        //do nothing just load the page
        if (idUKey != 0) {
            birthAlteration = alterationService.getByIDUKey(idUKey, user);
            if (birthAlteration != null) {
                originalName = service.getById(birthAlteration.getBdfIDUKey(), user).
                    getChild().getChildFullNameOfficialLang();
                return "pageLoad";
            }
        }
        logger.debug("unable to find birth alteration for reject idUKey : {}", idUKey);
        //todo forward to list page  amith
        return ERROR;
    }

    /**
     * rejecting birth alteration
     */
    public String rejectAlteration() {
        logger.debug("attempt to reject birth alteration idUKey : {}", idUKey);
        try {
            alterationService.rejectBirthAlteration(idUKey, comment, user);
            logger.debug("successfully rejected birth alteration idUKey : {}", idUKey);
        }
        catch (CRSRuntimeException e) {
            logger.debug("unable to reject birth alteration idUKey : {}", idUKey);
            addActionError(getText("error.unable.to.reject", new String[]{"" + idUKey}));
            //todo forward to list page  amith
            return ERROR;
        }
        return SUCCESS;
    }
/*   todo remove
    public String birthAlterationApplyChanges() {
        BirthAlteration ba = new BirthAlteration();
        Hashtable approvalsBitSet = new Hashtable();
        if (idUKey != 0) {
            ba = alterationService.getByIDUKey(idUKey, user);
        }
        int lengthOfBitSet = 0;
        if (ba != null) {
            indexCheck = ba.getApprovalStatuses();
            lengthOfBitSet = ba.getApprovalStatuses().length();
        }

        for (int i = 0; i < lengthOfBitSet + 1; i++) {
            if (indexCheck.get(i)) {
                approvalsBitSet.put(i, true);
            } else {
                approvalsBitSet.put(i, false);
            }
        }
        return SUCCESS;
    }*/


    private void compareStringValues(String registerValue, String alterationValue, int constantValue, String preferedLan) {
        if (!(registerValue == null && alterationValue == null)) {
            boolean x = registerValue != null ? !(registerValue.equals(alterationValue)) : true;
            if (x) {
                changesList.add(new FieldValue(registerValue, alterationValue, constantValue,
                    lk.rgd.common.util.CommonUtil.getYesOrNo(birthAlteration.getApprovalStatuses().get(constantValue), preferedLan)));
            }
        }
    }


    private void populateBirthAlterationNotice(BirthAlteration birthAlteration) {
        birthDeclaration = service.getById(birthAlteration.getBdfIDUKey(), user);
        BDDivision bdDivision = birthDeclaration.getRegister().getBirthDivision();
        DSDivision dsDivision = birthDeclaration.getRegister().getDsDivision();
        if (AppConstants.SINHALA.equals(birthDeclaration.getRegister().getPreferredLanguage())) {
            districtName = districtDAO.getNameByPK(dsDivision.getDistrict().getDistrictUKey(), AppConstants.SINHALA);
            dsDivisionName = dsDivision.getSiDivisionName();
            bdDivisionName = bdDivision.getSiDivisionName();
        } else if (AppConstants.TAMIL.equals(birthDeclaration.getRegister().getPreferredLanguage())) {
            districtName = districtDAO.getNameByPK(dsDivision.getDistrict().getDistrictUKey(), AppConstants.TAMIL);
            dsDivisionName = dsDivision.getTaDivisionName();
            bdDivisionName = bdDivision.getTaDivisionName();
        }
    }

    private void changesOfAlt27A(BirthDeclaration bdf, String language) {
        father = alt27A.getFather();
        ParentInfo parent = bdf.getParent();
        grandFather = alt27A.getGrandFather();
        GrandFatherInfo grandFatherOriginal = bdf.getGrandFather();
        if (grandFather != null && grandFatherOriginal != null) {
            compareStringValues(grandFatherOriginal.getGrandFatherFullName(), grandFather.getGrandFatherFullName(),
                Alteration27A.GRAND_FATHER_FULLNAME, language);

            compareStringValues(grandFatherOriginal.getGrandFatherNICorPIN(), grandFather.getGrandFatherNICorPIN(),
                Alteration27A.GRAND_FATHER_NIC_OR_PIN, language);

            if (!(grandFatherOriginal.getGrandFatherBirthYear() == grandFather.getGrandFatherBirthYear())) {
                changesList.add(new FieldValue(
                    grandFatherOriginal.getGrandFatherBirthYear() != null ? Integer.toString(grandFatherOriginal.getGrandFatherBirthYear()) : null,
                    grandFather.getGrandFatherBirthYear() != null ? Integer.toString(grandFather.getGrandFatherBirthYear()) : null,
                    Alteration27A.GRAND_FATHER_BIRTH_YEAR, lk.rgd.common.util.CommonUtil.getYesOrNo(birthAlteration.getApprovalStatuses().get(Alteration27A.GRAND_FATHER_BIRTH_YEAR), language)));
            }

            compareStringValues(grandFatherOriginal.getGrandFatherBirthPlace(), grandFather.getGrandFatherBirthPlace(),
                Alteration27A.GRAND_FATHER_BIRTH_PLACE, language);

            compareStringValues(grandFatherOriginal.getGreatGrandFatherFullName(), grandFather.getGreatGrandFatherFullName(),
                Alteration27A.GREAT_GRAND_FATHER_FULLNAME, language);

            compareStringValues(grandFatherOriginal.getGreatGrandFatherNICorPIN(), grandFather.getGreatGrandFatherNICorPIN(),
                Alteration27A.GREAT_GRAND_FATHER_NIC_OR_PIN, language);

            compareStringValues(grandFatherOriginal.getGreatGrandFatherBirthPlace(), grandFather.getGreatGrandFatherBirthPlace(),
                Alteration27A.GREAT_GRAND_FATHER_BIRTH_PLACE, language);
        }
        if (father != null && parent != null) {

            compareStringValues(parent.getFatherFullName(), father.getFatherFullName(),
                Alteration27A.FATHER_FULLNAME, language);

            compareStringValues(parent.getFatherNICorPIN(), father.getFatherNICorPIN(),
                Alteration27A.FATHER_NIC_OR_PIN, language);

            compareDate(parent.getFatherDOB(), father.getFatherDOB(), language, Alteration27A.FATHER_BIRTH_DATE);

            compareStringValues(parent.getFatherPlaceOfBirth(), father.getFatherPlaceOfBirth(),
                Alteration27A.FATHER_BIRTH_PLACE, language);

            FieldValue fv = compareCountry(parent.getFatherCountry(), father.getFatherCountry(),
                language, Alteration27A.FATHER_COUNTRY);
            if (fv != null) {
                changesList.add(fv);
            }

            compareStringValues(parent.getFatherPassportNo(), father.getFatherPassportNo(),
                Alteration27A.FATHER_BIRTH_PLACE, language);

            fv = compareRace(parent.getFatherRace(), father.getFatherRace(), language, Alteration27A.FATHER_RACE);
            if (fv != null) {
                changesList.add(fv);
            }
        }
        marriage = alt27A.getMarriage();
        MarriageInfo marriageOriginal = bdf.getMarriage();
        if (marriage != null && marriageOriginal != null) {

            compareStringValues(marriageOriginal.getParentsMarried().name(), marriage.getParentsMarried().name(),
                Alteration27A.WERE_PARENTS_MARRIED, language);

            compareStringValues(marriageOriginal.getPlaceOfMarriage(), marriage.getPlaceOfMarriage(),
                Alteration27A.PLACE_OF_MARRIAGE, language);

            compareDate(marriageOriginal.getDateOfMarriage(), marriage.getDateOfMarriage(), language,
                Alteration27A.DATE_OF_MARRIAGE);
        }
        compareStringValues(parent.getMotherFullName(), alt27A.getMothersNameAfterMarriage(),
            Alteration27A.MOTHER_NAME_AFTER_MARRIAGE, language);
    }

    private void changesOfAlt27(BirthDeclaration birthDeclaration, String language) {

        compareStringValues(birthDeclaration.getChild().getChildFullNameOfficialLang(),
            alt27.getChildFullNameOfficialLang(), Alteration27.CHILD_FULL_NAME_OFFICIAL_LANG, language);

        compareStringValues(birthDeclaration.getChild().getChildFullNameEnglish(),
            alt27.getChildFullNameEnglish(), Alteration27.CHILD_FULL_NAME_ENGLISH, language);
    }

    private void changesOfAlt52_1(BirthDeclaration bdf, String language) {
        child = bdf.getChild();
        register = bdf.getRegister();
        if (child != null) {

            compareDate(child.getDateOfBirth(), alt52_1.getDateOfBirth(), language,
                Alteration52_1.DATE_OF_BIRTH);

            compareStringValues(child.getPlaceOfBirth(), alt52_1.getPlaceOfBirth(),
                Alteration52_1.PLACE_OF_BIRTH, language);

            compareStringValues(child.getPlaceOfBirthEnglish(), alt52_1.getPlaceOfBirthEnglish(),
                Alteration52_1.PLACE_OF_BIRTH_ENGLISH, language);

            if (!(child.getChildGender() == alt52_1.getChildGender())) {
                changesList.add(new FieldValue(
                    child.getChildGender() != 0 ? Integer.toString(child.getChildGender()) : null,
                    alt52_1.getChildGender() != 0 ? Integer.toString(alt52_1.getChildGender()) : null,
                    Alteration52_1.GENDER, lk.rgd.common.util.CommonUtil.
                        getYesOrNo(birthAlteration.getApprovalStatuses().get(Alteration52_1.GENDER), language)));
            }
        }
        FieldValue fv = compareBDDivision(register.getBirthDivision(), alt52_1.getBirthDivision(),
            language, Alteration52_1.BIRTH_DIVISION);
        if (fv != null) {
            changesList.add(fv);
        }

        MotherInfo mother = alt52_1.getMother();
        ParentInfo parent = bdf.getParent();
        if (mother != null && parent != null) {

            compareStringValues(parent.getMotherFullName(), mother.getMotherFullName(),
                Alteration52_1.MOTHER_FULLNAME, language);

            compareStringValues(parent.getMotherPlaceOfBirth(), mother.getMotherPlaceOfBirth(),
                Alteration52_1.MOTHER_BIRTH_PLACE, language);

            compareStringValues(parent.getMotherPassportNo(), mother.getMotherPassportNo(),
                Alteration52_1.MOTHER_PASSPORT, language);

            if (!(parent.getMotherAgeAtBirth() == mother.getMotherAgeAtBirth())) {
                changesList.add(new FieldValue(
                    parent.getMotherAgeAtBirth() != 0 ? Integer.toString(parent.getMotherAgeAtBirth()) : null,
                    mother.getMotherAgeAtBirth() != 0 ? Integer.toString(mother.getMotherAgeAtBirth()) : null,
                    Alteration52_1.MOTHER_AGE_AT_BIRTH, lk.rgd.common.util.CommonUtil.
                        getYesOrNo(birthAlteration.getApprovalStatuses().get(Alteration52_1.MOTHER_AGE_AT_BIRTH), language)));
            }

            compareStringValues(parent.getMotherAddress(), mother.getMotherAddress(),
                Alteration52_1.MOTHER_ADDRESS, language);

            compareDate(parent.getMotherDOB(), mother.getMotherDOB(), language,
                Alteration52_1.MOTHER_BIRTH_DATE);

            fv = compareCountry(parent.getMotherCountry(), mother.getMotherCountry(),
                language, Alteration52_1.MOTHER_COUNTRY);
            if (fv != null) {
                changesList.add(fv);
            }

            fv = compareRace(parent.getMotherRace(), mother.getMotherRace(), language, Alteration52_1.MOTHER_RACE);
            if (fv != null) {
                changesList.add(fv);
            }
        }
        //compare the informant information
        informant = alt52_1.getInformant();
        InformantInfo informantOriginal = bdf.getInformant();
        if (informant != null && informantOriginal != null) {

            compareStringValues(informantOriginal.getInformantType().name(), informant.getInformantType().name(),
                Alteration52_1.INFORMANT_TYPE, language);

            compareStringValues(informantOriginal.getInformantNICorPIN(), informant.getInformantNICorPIN(),
                Alteration52_1.INFORMANT_NIC_OR_PIN, language);

            compareStringValues(informantOriginal.getInformantName(), informant.getInformantName(),
                Alteration52_1.INFORMANT_NAME, language);

            compareStringValues(informantOriginal.getInformantAddress(), informant.getInformantAddress(),
                Alteration52_1.INFORMANT_ADDRESS, language);
        }
    }

    private void compareDate(Date registerValue, Date alterationValue, String preferedLan, int constant) {
        String dateEx = null;
        String dateAlt = null;
        if (registerValue != null) {
            dateEx = DateTimeUtils.getISO8601FormattedString(registerValue);
        }
        if (alterationValue != null) {
            dateAlt = DateTimeUtils.getISO8601FormattedString(alterationValue);
        }
        compareStringValues(dateEx, dateAlt, constant, preferedLan);
    }

    private FieldValue compareBDDivision(BDDivision registerValue, BDDivision alterationValue, String preferedLan, int constant) {
        logger.debug("compare country for generating death alteration changes list");
        //one case null or both null in both null no need to add ,in other case need to add with out comparison
        //not both case null
        //case 1 : death register death country is null
        final FieldValue fv = new FieldValue(null, null, constant,
            lk.rgd.common.util.CommonUtil.getYesOrNo(birthAlteration.getApprovalStatuses().get(constant), preferedLan));
        if (registerValue != null && alterationValue != null) {
            if (registerValue.getBdDivisionUKey() != alterationValue.getBdDivisionUKey()) {
                if (AppConstants.SINHALA.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getSiDivisionName());
                    fv.setAlterationValue(alterationValue.getSiDivisionName());
                }
                if (AppConstants.TAMIL.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getTaDivisionName());
                    fv.setAlterationValue(alterationValue.getTaDivisionName());
                }
            } else {
                return null;
            }
        } else {
            if (registerValue != null) {
                fv.setExistingValue(null);
                if (AppConstants.SINHALA.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getSiDivisionName());
                }
                if (AppConstants.TAMIL.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getTaDivisionName());
                }
            } else if (alterationValue != null) {
                if (AppConstants.SINHALA.equals(preferedLan)) {
                    fv.setAlterationValue(alterationValue.getSiDivisionName());
                }
                if (AppConstants.TAMIL.equals(preferedLan)) {
                    fv.setAlterationValue(alterationValue.getTaDivisionName());
                }
            }
        }
        logger.debug("compare country for generating changes list completed");
        if (fv.getExistingValue() == null && fv.getAlterationValue() == null) {
            return null;
        }
        return fv;
    }

    //todo remove following

    /* private void compareAndAdd(int index, Object bdfName, Object baName, int type) {
            String[] compareChanges = new String[3];
            compareChanges[0] = Integer.toString(index);


            switch (type) {
                case 0:
                    if (bdfName != null) {
                        compareChanges[1] = bdfName.toString();
                        compareChanges[1] = compareChanges[1].trim();
                        if (compareChanges[1].length() == 0) {
                            compareChanges[1] = null;
                        }
                    } else {
                        compareChanges[1] = null;
                    }
                    if (baName != null) {
                        compareChanges[2] = baName.toString();
                        compareChanges[2] = compareChanges[2].trim();
                        if (compareChanges[2].length() == 0) {
                            compareChanges[2] = null;
                        }
                    } else {
                        compareChanges[2] = null;
                    }
                    break;
                //for add Country
                case 1:
                    Country countryBdf = (Country) bdfName;
                    Country countryBa = (Country) baName;
                    if (countryBdf != null) {
                        compareChanges[1] = countryDAO.getNameByPK(countryBdf.getCountryId(), language);
                    } else {
                        compareChanges[1] = null;
                    }
                    if (countryBa != null) {
                        compareChanges[2] = countryDAO.getNameByPK(countryBa.getCountryId(), language);
                    } else {
                        compareChanges[2] = null;
                    }
                    break;
                // for add Race
                case 2:
                    Race raceBdf = (Race) bdfName;
                    Race raceBa = (Race) baName;
                    if (raceBdf != null) {
                        compareChanges[1] = raceDAO.getNameByPK(raceBdf.getRaceId(), language);
                    } else {
                        compareChanges[1] = null;
                    }
                    if (raceBa != null) {
                        compareChanges[2] = raceDAO.getNameByPK(raceBa.getRaceId(), language);
                    } else {
                        compareChanges[2] = null;
                    }
                    break;
                case 3:
                    BDDivision bdDivisionBdf = (BDDivision) bdfName;
                    BDDivision bdDivisionBa = (BDDivision) baName;
                    if (bdDivisionBdf != null) {
                        compareChanges[1] = bdDivisionDAO.getNameByPK(bdDivisionBdf.getDivisionId(), language);
                    } else {
                        compareChanges[1] = null;
                    }
                    if (bdDivisionBa != null) {
                        compareChanges[2] = bdDivisionDAO.getNameByPK(bdDivisionBa.getDivisionId(), language);
                    } else {
                        compareChanges[2] = null;
                    }
                    break;
                case 4:
                    int genderBdf = (Integer) bdfName;
                    int genderBa = (Integer) baName;
                    if (genderBdf < 3) {
                        compareChanges[1] = GenderUtil.getGender(genderBdf, language);
                    } else {
                        compareChanges[1] = null;
                    }
                    if (genderBa < 3) {
                        compareChanges[2] = GenderUtil.getGender(genderBa, language);
                    } else {
                        compareChanges[2] = null;
                    }
            }

            if (compareChanges[1] != null && compareChanges[2] != null) {
                if (!compareChanges[2].equals(compareChanges[1])) {
                    birthAlterationApprovalList.add(compareChanges);
                    numberOfAppPending++;
                }
            }
            if (!(compareChanges == null && compareChanges[2] == null)) {
                if ((compareChanges[2] == null && compareChanges[1] != null) || (compareChanges[2] != null && compareChanges[1] == null)) {
                    birthAlterationApprovalList.add(compareChanges);
                    logger.debug("null property is ,{},value bdf :{} ", compareChanges[0], compareChanges[1]);
                    logger.debug("null property is ,{},value ba : {}", compareChanges[0], compareChanges[2]);
                    numberOfAppPending++;
                }
            }

        }
    */
    /*   public String alterationApproval() {
        BirthAlteration ba = alterationService.getByIDUKey(idUKey, user);
        int lengthOfBitSet = 0;
        Hashtable approvalsBitSet = new Hashtable();
        if (ba != null) {
            indexCheck = ba.getApprovalStatuses();
        }
        switch (sectionOfAct) {
            case 1:
                lengthOfBitSet = WebConstants.BIRTH_ALTERATION_APPROVE_ALT27;
                logger.debug("Change The alt27 bit set of the Birth Alteration idUKey :{}", idUKey);
                break;
            case 2:
                lengthOfBitSet = WebConstants.BIRTH_ALTERATION_APPROVE_ALT27A;
                logger.debug("Change The alt27A bit set of the Birth Alteration idUKey :{}", idUKey);
                break;
            case 3:
                lengthOfBitSet = WebConstants.BIRTH_ALTERATION_APPROVE_ALT52_1;
                logger.debug("Change The alt52_1 bit set of the Birth Alteration idUKey :{}", idUKey);
                break;

        }
        int check = 0;
        if (index != null) {
            for (int i = 0; i < lengthOfBitSet + 1; i++) {
                if (check < index.length) {
                    if (i == index[check]) {
                        logger.debug("index {}  is :{}", i, index[check]);
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
        }
        alterationService.approveBirthAlteration(ba, approvalsBitSet, user);
        ba = alterationService.getByIDUKey(idUKey, user);
        logger.debug("New Bit Set After Approval  :{}", ba.getApprovalStatuses());
        pageType = 2;
        bdId = ba.getBdfIDUKey();
        return SUCCESS;
    }

    private void initPermission() {
        setAllowApproveAlteration(user.isAuthorized(Permission.APPROVE_BIRTH_ALTERATION));
    }*/

    /**
     * handles pagination of BirthAlterations which are to be displayed in jsp
     *
     * @return String
     */

/*    public String nextPage() {
        if (logger.isDebugEnabled()) {
            logger.debug("inside nextPage() : current birthDistrictId {}, birthDivisionId {}", birthDistrictId, birthDivisionId +
                " requested from pageNo " + pageNo);
        }
        setPageNo(getPageNo() + 1);

        noOfRows = appParametersDAO.getIntParameter(BA_APPROVAL_ROWS_PER_PAGE);
        *//**
     * gets the user selected district to get the records
     * variable nextFlag is used to handle the pagination link
     * in the jsp page
     *//*
        if (idUKey != 0) {
            BirthAlteration baApprovalPending = alterationService.getApprovalPendingByIdUKey
                (idUKey, pageNo, noOfRows, user);
            if (birthAlterationPendingApprovalList == null) {
                birthAlterationPendingApprovalList = new ArrayList<BirthAlteration>();
            }
            birthAlterationPendingApprovalList.add(baApprovalPending);
            logger.debug("filter Birth Alteration to approve by Birth Alteration Serial Number :{}", idUKey);
        } else if (locationUKey != 0) {
            logger.debug("filter Birth Alteration to approve by idUKey of the location :{}", locationUKey);
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByUserLocationIdUKey(
                locationUKey, pageNo, noOfRows, user);
        } else if (birthDivisionId != 0) {
            logger.debug("filter Birth Alteration to approve by Birth Serial Number :{}", serialNo);
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByBDDivision(
                bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows);

        }
        paginationHandler(birthAlterationPendingApprovalList.size());
        setPreviousFlag(true);
        populateBasicLists();
           return SUCCESS;
    }*/

    /**
     * handles pagination of BirthAlteration approval pending data
     *
     * @return String
     */
    /*public String previousPage() {

        if (logger.isDebugEnabled()) {
            logger.debug("inside previousPage() : current birthDistrictId {}, birthDivisionId {} ", birthDistrictId, birthDivisionId
                + " requested from pageNo " + pageNo);
        }
        *//**
     * UI related. decides whether to display
     * next and previous links
     *//*
        if (previousFlag && getPageNo() == 2) {
            *//**
     * request is comming backword(calls previous
     * to load the very first page
     *//*
            setPreviousFlag(false);
        } else if (getPageNo() == 1) {
            */

    /**
     * if request is from page one
     * in the next page previous link
     * should be displayed
     *//*
            setPreviousFlag(false);
        } else {
            setPreviousFlag(true);
        }
        setNextFlag(true);
        if (getPageNo() > 1) {
            setPageNo(getPageNo() - 1);
        }
        noOfRows = appParametersDAO.getIntParameter(BA_APPROVAL_ROWS_PER_PAGE);

        if (idUKey != 0) {
            BirthAlteration baApprovalPending = alterationService.getApprovalPendingByIdUKey
                (idUKey, pageNo, noOfRows, user);
            if (birthAlterationPendingApprovalList == null) {
                birthAlterationPendingApprovalList = new ArrayList<BirthAlteration>();
            }
            birthAlterationPendingApprovalList.add(baApprovalPending);
            logger.debug("filter Birth Alteration to approve by Birth Alteration Serial Number :{}", idUKey);
        } else if (locationUKey != 0) {
            logger.debug("filter Birth Alteration to approve by idUKey of the location :{}", locationUKey);
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByUserLocationIdUKey(
                locationUKey, pageNo, noOfRows, user);
        } else if (birthDivisionId != 0) {
            logger.debug("filter Birth Alteration to approve by Birth Serial Number :{}", serialNo);
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByBDDivision(
                bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows);

        }
        populateBasicLists();
        initPermission();
        return SUCCESS;
    }

*/
    /* todo remove 
  public String printBirthAlterationNotice() {
        birthAlterationApprovalList = new ArrayList();
        birthAlterationApprovedList = new ArrayList();
        BirthAlteration ba = alterationService.getByIDUKey(idUKey, user);
        BirthDeclaration bdf = service.getById(bdId);
        if (ba != null) {
            BitSet changedFields = ba.getChangedfields();
            BitSet approveStatus = ba.getApprovalStatuses();
            BitSet approvedFields = new BitSet();
            BitSet rejectedFields = new BitSet();
            //get approved fields in birth alteration
            for (int i = 0; i < changedFields.length(); i++) {
                if (changedFields.get(i) && approveStatus.get(i)) {
                    approvedFields.set(i, true);
                } else {
                    approvedFields.set(i, false);
                }
            }
            //to get rejected fields in birth alteration 
            for (int i = 0; i < changedFields.length(); i++) {
                if (changedFields.get(i) && !(approveStatus.get(i))) {
                    rejectedFields.set(i, true);
                } else {
                    rejectedFields.set(i, false);
                }
            }
            BirthAlteration.AlterationType checkAlterationType = ba.getType();
            if (checkAlterationType == BirthAlteration.AlterationType.TYPE_27) {
                alt27 = ba.getAlt27();
                addToNoticeAlt27(bdf, approvedFields, rejectedFields);
                sectionOfAct = 1;
            }
            if (checkAlterationType != BirthAlteration.AlterationType.TYPE_27 && checkAlterationType !=
                BirthAlteration.AlterationType.TYPE_27A) {
                alt52_1 = ba.getAlt52_1();
                addToNoticeAlt52_1(bdf, approvedFields, rejectedFields);
                sectionOfAct = 2;
            }
            if (checkAlterationType == BirthAlteration.AlterationType.TYPE_27A) {
                alt27A = ba.getAlt27A();
                addToNoticeAlt27A(bdf, approvedFields, rejectedFields);
                sectionOfAct = 3;
            }
            declarant = ba.getDeclarant();

        }
        if (bdf != null) {
            getBirthCertificateInfo(bdf);
        }
        return SUCCESS;
    }*/
    /* private void addToNoticeAlt27A(BirthDeclaration bdf, BitSet approvedFields, BitSet rejectedFields) {
            GrandFatherInfo grandFatherBa = alt27A.getGrandFather();
            GrandFatherInfo grandFatherBdf = bdf.getGrandFather();

            if (grandFatherBa != null && grandFatherBdf != null) {
                //check Grand father information
                if (approvedFields.get(0) || rejectedFields.get(0)) {
                    addToApprovalList(Alteration27A.GRAND_FATHER_FULLNAME, grandFatherBdf.getGrandFatherFullName()
                        , grandFatherBa.getGrandFatherFullName(), approvedFields.get(0), rejectedFields.get(0), 0);
                }
                if (approvedFields.get(1) || rejectedFields.get(1)) {
                    addToApprovalList(Alteration27A.GRAND_FATHER_NIC_OR_PIN, grandFatherBdf.getGrandFatherNICorPIN(),
                        grandFatherBa.getGrandFatherNICorPIN(), approvedFields.get(1), rejectedFields.get(1), 0);
                }
                if (approvedFields.get(2) || rejectedFields.get(2)) {
                    logger.debug("grand father birth year ");
                    addToApprovalList(Alteration27A.GRAND_FATHER_BIRTH_YEAR, grandFatherBdf.getGreatGrandFatherBirthYear()
                        , grandFatherBa.getGreatGrandFatherBirthYear(), approvedFields.get(2), rejectedFields.get(2), 0);
                }
                if (approvedFields.get(3) || rejectedFields.get(3)) {
                    addToApprovalList(Alteration27A.GRAND_FATHER_BIRTH_PLACE, grandFatherBdf.getGrandFatherBirthPlace()
                        , grandFatherBa.getGrandFatherBirthPlace(), approvedFields.get(3), rejectedFields.get(3), 0);
                }
                //check Grand grand father information
                if (approvedFields.get(4) || rejectedFields.get(4)) {
                    addToApprovalList(Alteration27A.GREAT_GRAND_FATHER_FULLNAME, grandFatherBdf.getGreatGrandFatherFullName()
                        , grandFatherBa.getGreatGrandFatherFullName(), approvedFields.get(4), rejectedFields.get(4), 0);
                }
                if (approvedFields.get(5) || rejectedFields.get(5)) {
                    addToApprovalList(Alteration27A.GREAT_GRAND_FATHER_NIC_OR_PIN, grandFatherBdf.getGreatGrandFatherNICorPIN(),
                        grandFatherBa.getGreatGrandFatherNICorPIN(), approvedFields.get(5), rejectedFields.get(5), 0);
                }
                if (approvedFields.get(6) || rejectedFields.get(6)) {
                    addToApprovalList(Alteration27A.GREAT_GRAND_FATHER_BIRTH_YEAR, grandFatherBdf.getGreatGrandFatherBirthYear(),
                        grandFatherBa.getGreatGrandFatherBirthYear(), approvedFields.get(6), rejectedFields.get(6), 0);
                }
                if (approvedFields.get(7) || rejectedFields.get(7)) {
                    addToApprovalList(Alteration27A.GREAT_GRAND_FATHER_BIRTH_PLACE, grandFatherBdf.getGreatGrandFatherBirthPlace(),
                        grandFatherBa.getGreatGrandFatherBirthPlace(), approvedFields.get(7), rejectedFields.get(7), 0);
                }
            }
            ParentInfo fatherBdf = bdf.getParent();
            FatherInfo fatherBa = alt27A.getFather();
            if (fatherBdf != null && fatherBa != null) {
                //check father information
                if (approvedFields.get(8) || rejectedFields.get(8)) {
                    addToApprovalList(Alteration27A.FATHER_FULLNAME, fatherBdf.getFatherFullName(), fatherBa.getFatherFullName()
                        , approvedFields.get(8), rejectedFields.get(8), 0);
                }
                if (approvedFields.get(9) || rejectedFields.get(9)) {
                    addToApprovalList(Alteration27A.FATHER_NIC_OR_PIN, fatherBdf.getFatherNICorPIN(), fatherBa.getFatherNICorPIN(),
                        approvedFields.get(9), rejectedFields.get(9), 0);
                }
                if (approvedFields.get(10) || rejectedFields.get(10)) {
                    addToApprovalList(Alteration27A.FATHER_BIRTH_DATE, fatherBdf.getFatherDOB(), fatherBa.getFatherDOB(),
                        approvedFields.get(10), rejectedFields.get(10), 0);
                }
                if (approvedFields.get(11) || rejectedFields.get(11)) {
                    addToApprovalList(Alteration27A.FATHER_BIRTH_PLACE, fatherBdf.getFatherPlaceOfBirth(),
                        fatherBa.getFatherPlaceOfBirth(), approvedFields.get(11), rejectedFields.get(11), 0);
                }
                if (approvedFields.get(12) || rejectedFields.get(12)) {
                    addToApprovalList(Alteration27A.FATHER_COUNTRY, fatherBdf.getFatherCountry(), fatherBa.getFatherCountry(),
                        approvedFields.get(12), rejectedFields.get(12), 1);
                }
                if (approvedFields.get(13) || rejectedFields.get(13)) {
                    addToApprovalList(Alteration27A.FATHER_PASSPORT, fatherBdf.getFatherPassportNo(), fatherBa.getFatherPassportNo(),
                        approvedFields.get(13), rejectedFields.get(13), 0);
                }
                if (approvedFields.get(14) || rejectedFields.get(14)) {
                    addToApprovalList(Alteration27A.FATHER_RACE, fatherBdf.getFatherRace(), fatherBa.getFatherRace(),
                        approvedFields.get(14), rejectedFields.get(14), 2);
                }

            }
            MarriageInfo marriageBdf = bdf.getMarriage();
            MarriageInfo marriageBa = alt27A.getMarriage();
            if (marriageBdf != null && marriageBa != null) {
                if (approvedFields.get(15) || rejectedFields.get(15)) {
                    addToApprovalList(Alteration27A.WERE_PARENTS_MARRIED, marriageBdf.getParentsMarried(),
                        marriageBa.getParentsMarried(), approvedFields.get(15), rejectedFields.get(15), 0);
                }
                if (approvedFields.get(16) || rejectedFields.get(16)) {
                    addToApprovalList(Alteration27A.PLACE_OF_MARRIAGE, marriageBdf.getPlaceOfMarriage(), marriageBa.getParentsMarried(),
                        approvedFields.get(16), rejectedFields.get(16), 0);
                }
                if (approvedFields.get(17) || rejectedFields.get(17)) {
                    addToApprovalList(Alteration27A.DATE_OF_MARRIAGE, marriageBdf.getDateOfMarriage(), marriageBa.getDateOfMarriage(),
                        approvedFields.get(17), rejectedFields.get(17), 0);
                }
            }
            if (bdf.getParent() != null) {
                if (approvedFields.get(18) || rejectedFields.get(18)) {
                    addToApprovalList(Alteration27A.MOTHER_NAME_AFTER_MARRIAGE, bdf.getParent().getMotherFullName(),
                        alt27A.getMothersNameAfterMarriage(), approvedFields.get(18), rejectedFields.get(18), 0);
                }
            }

        }
    */
    /*private void addToNoticeAlt52_1(BirthDeclaration bdf, BitSet approvedFields, BitSet rejectedFields) {
        child = bdf.getChild();
        if (child != null) {
            if (approvedFields.get(0) || rejectedFields.get(0)) {
                addToApprovalList(Alteration52_1.DATE_OF_BIRTH, child.getDateOfBirth(), alt52_1.getDateOfBirth(),
                    approvedFields.get(0), rejectedFields.get(0), 0);
            }
            if (approvedFields.get(1) || rejectedFields.get(1)) {
                addToApprovalList(Alteration52_1.PLACE_OF_BIRTH, child.getPlaceOfBirth(), alt52_1.getPlaceOfBirth(),
                    approvedFields.get(1), rejectedFields.get(1), 0);
            }
            if (approvedFields.get(2) || rejectedFields.get(2)) {
                addToApprovalList(Alteration52_1.PLACE_OF_BIRTH_ENGLISH, child.getPlaceOfBirthEnglish(), alt52_1.getPlaceOfBirthEnglish(),
                    approvedFields.get(2), rejectedFields.get(2), 0);
            }

        }
        register = bdf.getRegister();
        if (register != null) {
            if (approvedFields.get(3) || rejectedFields.get(3)) {
                addToApprovalList(Alteration52_1.BIRTH_DIVISION, register.getBirthDivision(), alt52_1.getBirthDivision(),
                    approvedFields.get(3), rejectedFields.get(3), 3);
            }
        }
        if (child != null) {
            if (approvedFields.get(4) || rejectedFields.get(4)) {
                addToApprovalList(Alteration52_1.GENDER, child.getChildGender(), alt52_1.getChildGender(),
                    approvedFields.get(4), rejectedFields.get(4), 4);
            }
        }
        ParentInfo motherBdf = bdf.getParent();
        MotherInfo motherBa = alt52_1.getMother();
        if (motherBdf != null && motherBa != null) {
            if (approvedFields.get(5) || rejectedFields.get(5)) {
                addToApprovalList(Alteration52_1.MOTHER_FULLNAME, motherBdf.getFatherFullName(), motherBa.getMotherFullName(),
                    approvedFields.get(5), rejectedFields.get(5), 0);
            }
            if (approvedFields.get(6) || rejectedFields.get(6)) {
                addToApprovalList(Alteration52_1.MOTHER_NIC_OR_PIN, motherBdf.getMotherNICorPIN(), motherBa.getMotherNICorPIN(),
                    approvedFields.get(6), rejectedFields.get(6), 0);
            }
            if (approvedFields.get(7) || rejectedFields.get(7)) {
                addToApprovalList(Alteration52_1.MOTHER_BIRTH_DATE, motherBdf.getMotherDOB(), motherBa.getMotherDOB(),
                    approvedFields.get(7), rejectedFields.get(7), 0);
            }
            if (approvedFields.get(8) || rejectedFields.get(8)) {
                addToApprovalList(Alteration52_1.MOTHER_BIRTH_PLACE, motherBdf.getMotherPlaceOfBirth(), motherBa.getMotherPlaceOfBirth(),
                    approvedFields.get(8), rejectedFields.get(8), 0);
            }
            if (approvedFields.get(9) || rejectedFields.get(9)) {
                addToApprovalList(Alteration52_1.MOTHER_COUNTRY, motherBdf.getMotherCountry(), motherBa.getMotherCountry(),
                    approvedFields.get(9), rejectedFields.get(9), 1);
            }
            if (approvedFields.get(10) || rejectedFields.get(10)) {
                addToApprovalList(Alteration52_1.MOTHER_PASSPORT, motherBdf.getMotherPassportNo(), motherBa.getMotherPassportNo(),
                    approvedFields.get(10), rejectedFields.get(10), 0);
            }
            if (approvedFields.get(11) || rejectedFields.get(11)) {
                addToApprovalList(Alteration52_1.MOTHER_RACE, motherBdf.getMotherRace(), motherBa.getMotherRace(),
                    approvedFields.get(11), rejectedFields.get(11), 2);
            }
            if (approvedFields.get(12) || rejectedFields.get(12)) {
                addToApprovalList(Alteration52_1.MOTHER_AGE_AT_BIRTH, motherBdf.getMotherAgeAtBirth(), motherBa.getMotherAgeAtBirth(),
                    approvedFields.get(12), rejectedFields.get(12), 0);
            }
            if (approvedFields.get(13) || rejectedFields.get(13)) {
                addToApprovalList(Alteration52_1.MOTHER_ADDRESS, motherBdf.getMotherAddress(), motherBa.getMotherAddress(),
                    approvedFields.get(13), rejectedFields.get(13), 0);
            }
        }
        InformantInfo informantBdf = bdf.getInformant();
        AlterationInformatInfo informantBa = alt52_1.getInformant();
        if (informantBdf != null && informantBa != null) {
            if (approvedFields.get(14) || rejectedFields.get(14)) {
                addToApprovalList(Alteration52_1.INFORMANT_TYPE, informantBdf.getInformantType(), informantBa.getInformantType(),
                    approvedFields.get(14), rejectedFields.get(14), 0);
            }
            if (approvedFields.get(15) || rejectedFields.get(15)) {
                addToApprovalList(Alteration52_1.INFORMANT_NIC_OR_PIN, informantBdf.getInformantNICorPIN(), informantBa.getInformantNICorPIN(),
                    approvedFields.get(15), rejectedFields.get(15), 0);
            }
            if (approvedFields.get(16) || rejectedFields.get(16)) {
                addToApprovalList(Alteration52_1.INFORMANT_NAME, informantBdf.getInformantName(), informantBa.getInformantName(),
                    approvedFields.get(16), rejectedFields.get(16), 0);
            }
            if (approvedFields.get(17) || rejectedFields.get(17)) {
                addToApprovalList(Alteration52_1.INFORMANT_ADDRESS, informantBdf.getInformantAddress(), informantBa.getInformantAddress(),
                    approvedFields.get(17), rejectedFields.get(17), 0);
            }
        }
    }

    private void addToNoticeAlt27(BirthDeclaration bdf, BitSet approvedFields, BitSet rejectedFields) {
        child = bdf.getChild();
        if (child != null) {
            if (approvedFields.get(0) || rejectedFields.get(0)) {
                addToApprovalList(Alteration27.CHILD_FULL_NAME_OFFICIAL_LANG, child.getChildFullNameOfficialLang(),
                    alt27.getChildFullNameOfficialLang(), approvedFields.get(0), rejectedFields.get(0), 0);
            }
            if (approvedFields.get(1) || rejectedFields.get(1)) {
                addToApprovalList(Alteration27.CHILD_FULL_NAME_ENGLISH, child.getChildFullNameEnglish(), alt27.getChildFullNameEnglish(),
                    approvedFields.get(1), rejectedFields.get(1), 0);
            }
        }
    }    */
    /*  private void addToApprovalList(int index, Object bdfName, Object baName, boolean app, boolean rej, int type) {
            String[] compareChanges = new String[3];
            compareChanges[0] = Integer.toString(index);
            switch (type) {
                case 0:
                    if (bdfName != null) {
                        compareChanges[1] = bdfName.toString();
                    } else {
                        compareChanges[1] = null;
                    }
                    if (baName != null) {
                        compareChanges[2] = baName.toString();
                    } else {
                        compareChanges[2] = null;
                    }
                    break;
                //for add Country
                case 1:
                    Country countryBdf = (Country) bdfName;
                    Country countryBa = (Country) baName;
                    if (countryBdf != null) {
                        compareChanges[1] = countryDAO.getNameByPK(countryBdf.getCountryId(), language);
                    } else {
                        compareChanges[1] = null;
                    }
                    if (countryBa != null) {
                        compareChanges[2] = countryDAO.getNameByPK(countryBa.getCountryId(), language);
                    } else {
                        compareChanges[2] = null;
                    }
                    break;
                // for add Race
                case 2:
                    Race raceBdf = (Race) bdfName;
                    Race raceBa = (Race) baName;
                    if (raceBdf != null) {
                        compareChanges[1] = raceDAO.getNameByPK(raceBdf.getRaceId(), language);
                    } else {
                        compareChanges[1] = null;
                    }
                    if (raceBa != null) {
                        compareChanges[2] = raceDAO.getNameByPK(raceBa.getRaceId(), language);
                    } else {
                        compareChanges[2] = null;
                    }
                    break;
                case 3:
                    BDDivision bdDivisionBdf = (BDDivision) bdfName;
                    BDDivision bdDivisionBa = (BDDivision) baName;
                    if (bdDivisionBdf != null) {
                        compareChanges[1] = bdDivisionDAO.getNameByPK(bdDivisionBdf.getDivisionId(), language);
                    } else {
                        compareChanges[1] = null;
                    }
                    if (bdDivisionBa != null) {
                        compareChanges[2] = bdDivisionDAO.getNameByPK(bdDivisionBa.getDivisionId(), language);
                    } else {
                        compareChanges[2] = null;
                    }
                    break;
                case 4:
                    int genderBdf = (Integer) bdfName;
                    int genderBa = (Integer) baName;
                    if (genderBdf < 3) {
                        compareChanges[1] = GenderUtil.getGender(genderBdf, language);
                    } else {
                        compareChanges[1] = null;
                    }
                    if (genderBa < 3) {
                        compareChanges[2] = GenderUtil.getGender(genderBa, language);
                    } else {
                        compareChanges[2] = null;
                    }
                    break;
            }

            if (app)

            {
                birthAlterationApprovedList.add(compareChanges);
            } else if (rej)

            {
                birthAlterationApprovalList.add(compareChanges);
            }

        }
    */
    //todo use one method for follow 3 amith
    private FieldValue compareCountry(Country registerValue, Country alterationValue, String preferedLan, int constant) {
        logger.debug("compare country for generating death alteration changes list");
        //one case null or both null in both null no need to add ,in other case need to add with out comparison
        //not both case null
        //case 1 : death register death country is null
        final FieldValue fv = new FieldValue(null, null, constant,
            lk.rgd.common.util.CommonUtil.getYesOrNo(birthAlteration.getApprovalStatuses().get(constant), preferedLan));
        if (registerValue != null && alterationValue != null) {
            if (registerValue.getCountryId() != alterationValue.getCountryId()) {
                if (AppConstants.SINHALA.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getSiCountryName());
                    fv.setAlterationValue(alterationValue.getSiCountryName());
                }
                if (AppConstants.TAMIL.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getTaCountryName());
                    fv.setAlterationValue(alterationValue.getTaCountryName());
                }
            } else {
                return null;
            }
        } else {
            if (registerValue != null) {
                fv.setExistingValue(null);
                if (AppConstants.SINHALA.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getSiCountryName());
                }
                if (AppConstants.TAMIL.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getTaCountryName());
                }
            } else if (alterationValue != null) {
                if (AppConstants.SINHALA.equals(preferedLan)) {
                    fv.setAlterationValue(alterationValue.getSiCountryName());
                }
                if (AppConstants.TAMIL.equals(preferedLan)) {
                    fv.setAlterationValue(alterationValue.getTaCountryName());
                }
            }
        }
        logger.debug("compare country for generating changes list completed");
        if (fv.getExistingValue() == null && fv.getAlterationValue() == null) {
            return null;
        }
        return fv;
    }

    private FieldValue compareRace(Race registerValue, Race alterationValue, String preferedLan, int constant) {
        logger.debug("compare race for generating death alteration changes list");
        //one case null or both null in both null no need to add ,in other case need to add with out comparison
        //not both case null
        //case 1 : death register death country is null
        final FieldValue fv = new FieldValue(null, null, constant,
            lk.rgd.common.util.CommonUtil.getYesOrNo(birthAlteration.getApprovalStatuses().get(constant), preferedLan));

        if (registerValue != null && alterationValue != null) {
            if (registerValue.getRaceId() != alterationValue.getRaceId()) {
                if (AppConstants.SINHALA.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getSiRaceName());
                    fv.setAlterationValue(alterationValue.getSiRaceName());
                }
                if (AppConstants.TAMIL.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getTaRaceName());
                    fv.setAlterationValue(alterationValue.getTaRaceName());
                }
            } else {
                return null;
            }
        } else {
            if (registerValue != null) {
                fv.setExistingValue(null);
                if (AppConstants.SINHALA.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getSiRaceName());

                }
                if (AppConstants.TAMIL.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getTaRaceName());
                }
            } else if (alterationValue != null) {
                if (AppConstants.SINHALA.equals(preferedLan)) {
                    fv.setAlterationValue(alterationValue.getSiRaceName());
                }
                if (AppConstants.TAMIL.equals(preferedLan)) {
                    fv.setAlterationValue(alterationValue.getTaRaceName());
                }
            }
        }
        logger.debug("complete comparing race");
        if (fv.getExistingValue() == null && fv.getAlterationValue() == null) {
            return null;
        }
        return fv;
    }

    private void paginationHandler(int recordsFound) {
        if (recordsFound == appParametersDAO.getIntParameter(BA_APPROVAL_ROWS_PER_PAGE)) {
            setNextFlag(true);
        } else {
            setNextFlag(false);
        }
    }

    private void populateBasicLists() {
        userLocations = commonUtil.populateActiveUserLocations(user, language);
        if (userLocations != null) {
            logger.debug("Loaded {} user locations", userLocations.size());
        }
        populateDistrictAndDSDivision();
        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, getLanguage(), user);
        if (birthDivisionId == 0) {
            birthDivisionId = bdDivisionList.keySet().iterator().next();
            logger.debug("first allowed BD Div in the list {} was set", birthDivisionId);
        }
    }

    private void populateDistrictAndDSDivision() {
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
        if (birthDistrictId == 0) {
            if (!districtList.isEmpty()) {
                birthDistrictId = districtList.keySet().iterator().next();
                logger.debug("first allowed district in the list {} was set", birthDistrictId);
            }
        }
        allDsDivisionList = dsDivisionDAO.getAllDSDivisionNames(birthDistrictId, language, user);
        if (dsDivisionId == 0) {
            if (!dsDivisionList.isEmpty()) {
                dsDivisionId = dsDivisionList.keySet().iterator().next();
                logger.debug("first allowed DS Division in the list {} was set", dsDivisionId);
            }
        }
        allBdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
    }


    private void getBirthCertificateInfo(BirthDeclaration bdf) {
        if (bdf != null) {
            register = bdf.getRegister();
            birthDistrictId = register.getBirthDistrict().getDistrictUKey();
            birthDivisionId = register.getBirthDivision().getBdDivisionUKey();
            dsDivisionId = register.getDsDivision().getDsDivisionUKey();

            bdId = bdf.getIdUKey();
            nicOrPin = bdf.getChild().getPin();
            serialNo = bdf.getRegister().getBdfSerialNo();
            districtName = districtDAO.getNameByPK(birthDistrictId, language);
            dsDivisionName = dsDivisionDAO.getNameByPK(dsDivisionId, language);
            bdDivisionName = bdDivisionDAO.getNameByPK(birthDivisionId, language);
        }

    }


    private void populateAlt52_1(BirthDeclaration bdf) {
        Alteration52_1 alt52_1 = birthAlteration.getAlt52_1();
        if (alt52_1 == null) {
            alt52_1 = new Alteration52_1();
        }
        ParentInfo parent = bdf.getParent();
        child = bdf.getChild();
        InformantInfo bdfInformant = bdf.getInformant();
        /*if informant is null then populate informant from bdf
        *in the edit mode populate informant information if  informant information were not changed in first time
        * */
        if (alt52_1.getInformant() == null) {
            if (bdfInformant != null) {
                alt52_1.setInformant(new AlterationInformatInfo(bdfInformant.getInformantType(),
                    bdfInformant.getInformantName(), bdfInformant.getInformantNICorPIN(), bdfInformant.getInformantAddress()));
            }
        } else {
            editInformantInfo = true;
        }
        if (child != null) {
            if (alt52_1.getDateOfBirth() == null) {
                alt52_1.setDateOfBirth(child.getDateOfBirth());
            }
            if (alt52_1.getPlaceOfBirth() == null) {
                alt52_1.setPlaceOfBirth(child.getPlaceOfBirth());
            }
            if (alt52_1.getPlaceOfBirthEnglish() == null) {
                alt52_1.setPlaceOfBirthEnglish(child.getPlaceOfBirthEnglish());
            }
            if (alt52_1.getChildGender() == 0) {
                alt52_1.setChildGender(child.getChildGender());
            }
            if (alt52_1.getBirthDivision() != null) {
                editChildInfo = true;
                birthDivisionId = alt52_1.getBirthDivision().getDivisionId();
            }

        }
        MotherInfo mother = alt52_1.getMother();
        if (mother != null) {
            editMotherInfo = true;
        }
        if (mother == null && parent != null) {
            mother = new MotherInfo();
            mother.setMotherAddress(parent.getMotherAddress());
            mother.setMotherAgeAtBirth(parent.getMotherAgeAtBirth());
            mother.setMotherDOB(parent.getMotherDOB());
            mother.setMotherFullName(parent.getMotherFullName());
            mother.setMotherNICorPIN(parent.getMotherNICorPIN());
            mother.setMotherPassportNo(parent.getMotherPassportNo());
            if (parent.getMotherCountry() != null) {
                motherCountryId = bdf.getParent().getMotherCountry().getCountryId();
            }
            if (parent.getMotherRace() != null) {
                motherRaceId = bdf.getParent().getMotherRace().getRaceId();
            }
        }
        alt52_1.setMother(mother);
        logger.debug("Loaded  Mother NIC or PIN Number of the {} is :{} ",
            alt52_1.getMother().getMotherFullName(), alt52_1.getMother().getMotherNICorPIN());
        birthAlteration.setAlt52_1(alt52_1);

    }

    private void populateAlt27A(BirthDeclaration bdf) {
        Alteration27A alt27A = birthAlteration.getAlt27A();
        if (alt27A == null) {
            alt27A = new Alteration27A();
        }
        if (bdf != null) {
            ParentInfo parent = bdf.getParent();
            FatherInfo father = alt27A.getFather();
            if (father != null) {
                //if father is not null,check box of the father information will check
                editFatherInfo = true;
            }
            if (father == null && parent != null) {
                /*if father in null then father information will populate from bdf*/
                father = new FatherInfo();
                father.setFatherDOB(parent.getFatherDOB());
                father.setFatherFullName(parent.getFatherFullName());
                father.setFatherNICorPIN(parent.getFatherNICorPIN());
                father.setFatherPassportNo(parent.getFatherPassportNo());
                father.setFatherPlaceOfBirth(parent.getFatherPlaceOfBirth());
                father.setFatherRace(parent.getFatherRace());
                if (parent.getFatherCountry() != null) {
                    fatherCountryId = parent.getFatherCountry().getCountryId();
                }
                if (parent.getFatherRace() != null) {
                    fatherRaceId = parent.getFatherRace().getRaceId();
                }
            } else {
                if (father.getFatherCountry() != null) {
                    fatherCountryId = father.getFatherCountry().getCountryId();
                    logger.debug("populated {} father country id is :{}", father.getFatherFullName(), fatherCountryId);
                }
                if (father.getFatherRace() != null) {
                    fatherRaceId = father.getFatherRace().getRaceId();
                }
            }
            alt27A.setFather(father);
            if (alt27A.getGrandFather() == null) {
                alt27A.setGrandFather(bdf.getGrandFather());
            } else {
                editGrandFatherInfo = true;
            }
            if (alt27A.getMarriage() == null) {
                alt27A.setMarriage(bdf.getMarriage());
            } else {
                editMarriageInfo = true;
            }
            if (parent != null) {
                if (alt27A.getMothersNameAfterMarriage() == null) {
                    alt27A.setMothersNameAfterMarriage(parent.getMotherFullName());
                } else {
                    editMothersNameAfterMarriageInfo = true;
                }
            }
        }
        birthAlteration.setAlt27A(alt27A);
    }


    private void populateBirthAlterationForUpdate(BirthAlteration existing, BirthAlteration updated) {
        //there are 3 acts in birth alteration and one and only one get populated for on alteration
        switch (existing.getType()) {
            case TYPE_27: {
                //only populate ALT 27 A object
                existing.setAlt27(updated.getAlt27());
            }
            break;
            case TYPE_27A: {
                existing.setAlt27A(updated.getAlt27A());
            }
            break;
            //all following cases are treat as same only different is how the approve
            case TYPE_52_1_A:
            case TYPE_52_1_B:
            case TYPE_52_1_D:
            case TYPE_52_1_E:
            case TYPE_52_1_H:
            case TYPE_52_1_I: {
                existing.setAlt52_1(updated.getAlt52_1());
            }
        }
        //populate other fields that can be edit
        existing.setDateReceived(updated.getDateReceived());
        existing.setDeclarant(updated.getDeclarant());
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
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("setting User: {} and language : {}", user.getUserName(), language);
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

    public int getFatherCountryId() {
        return fatherCountryId;
    }

    public void setFatherCountryId(int fatherCountryId) {
        this.fatherCountryId = fatherCountryId;
    }

    public void setMotherCountryId(int motherCountryId) {
        this.motherCountryId = motherCountryId;
    }

    public int getMotherCountryId() {
        return motherCountryId;
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

    public void setFatherRaceId(int fatherRaceId) {
        this.fatherRaceId = fatherRaceId;
    }

    public int getFatherRaceId() {
        return fatherRaceId;
    }

    public void setMotherRaceId(int motherRaceId) {
        this.motherRaceId = motherRaceId;
    }

    public int getMotherRaceId() {
        return motherRaceId;
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

    public List<String[]> getBirthAlterationApprovalList() {
        return birthAlterationApprovalList;
    }

    public void setBirthAlterationApprovalList(List<String[]> birthAlterationApprovalList) {
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

    public BitSet getIndexCheck() {
        return indexCheck;
    }

    public void setIndexCheck(BitSet indexCheck) {
        this.indexCheck = indexCheck;
    }

    public HashMap getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(HashMap approveStatus) {
        this.approveStatus = approveStatus;
    }

    public int getNumberOfAppPending() {
        return numberOfAppPending;
    }

    public void setNumberOfAppPending(int numberOfAppPending) {
        this.numberOfAppPending = numberOfAppPending;
    }

    public ParentInfo getParent() {
        return parent;
    }

    public void setParent(ParentInfo parent) {
        this.parent = parent;
    }


    public int getPageType() {
        return pageType;
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    public Date getAlterationRecivedDate() {
        return alterationRecivedDate;
    }

    public void setAlterationRecivedDate(Date alterationRecivedDate) {
        this.alterationRecivedDate = alterationRecivedDate;
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

    public BirthAlteration.AlterationType getAlterationType() {
        return alterationType;
    }

    public void setAlterationType(BirthAlteration.AlterationType alterationType) {
        this.alterationType = alterationType;
    }

    public Map<Integer, String> getUserLocations() {
        return userLocations;
    }

    public void setUserLocations(Map<Integer, String> userLocations) {
        this.userLocations = userLocations;
    }

    public int getLocationUKey() {
        return locationUKey;
    }

    public void setLocationUKey(int locationUKey) {
        this.locationUKey = locationUKey;
    }

    public Map<Integer, Boolean> getAlterationApprovalPermission() {
        return alterationApprovalPermission;
    }

    public void setAlterationApprovalPermission(Map<Integer, Boolean> alterationApprovalPermission) {
        this.alterationApprovalPermission = alterationApprovalPermission;
    }

    public boolean isApplyChanges() {
        return applyChanges;
    }

    public void setApplyChanges(boolean applyChanges) {
        this.applyChanges = applyChanges;
    }

    public List getBirthAlterationApprovedList() {
        return birthAlterationApprovedList;
    }

    public void setBirthAlterationApprovedList(List birthAlterationApprovedList) {
        this.birthAlterationApprovedList = birthAlterationApprovedList;
    }

    public int getDivisionAltaration() {
        return divisionAltaration;
    }

    public void setDivisionAltaration(int divisionAltaration) {
        this.divisionAltaration = divisionAltaration;
    }

    public boolean isApproveRightsToUser() {
        return approveRightsToUser;
    }

    public void setApproveRightsToUser(boolean approveRightsToUser) {
        this.approveRightsToUser = approveRightsToUser;
    }

    public DistrictDAO getDistrictDAO() {
        return districtDAO;
    }

    public void setDistrictDAO(DistrictDAO districtDAO) {
        this.districtDAO = districtDAO;
    }

    public Map<Integer, String> getAllBdDivisionList() {
        return allBdDivisionList;
    }

    public void setAllBdDivisionList(Map<Integer, String> allBdDivisionList) {
        this.allBdDivisionList = allBdDivisionList;
    }

    public Map<Integer, String> getAllDsDivisionList() {
        return allDsDivisionList;
    }

    public void setAllDsDivisionList(Map<Integer, String> allDsDivisionList) {
        this.allDsDivisionList = allDsDivisionList;
    }

    public BirthAlteration getBirthAlteration() {
        return birthAlteration;
    }

    public void setBirthAlteration(BirthAlteration birthAlteration) {
        this.birthAlteration = birthAlteration;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getBirthCertificateNumber() {
        return birthCertificateNumber;
    }

    public void setBirthCertificateNumber(long birthCertificateNumber) {
        this.birthCertificateNumber = birthCertificateNumber;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public List<FieldValue> getChangesList() {
        return changesList;
    }

    public void setChangesList(List<FieldValue> changesList) {
        this.changesList = changesList;
    }

    public int[] getApprovedIndex() {
        return approvedIndex;
    }

    public void setApprovedIndex(int[] approvedIndex) {
        this.approvedIndex = approvedIndex;
    }

    public BirthDeclaration getBirthDeclaration() {
        return birthDeclaration;
    }

    public void setBirthDeclaration(BirthDeclaration birthDeclaration) {
        this.birthDeclaration = birthDeclaration;
    }
}
