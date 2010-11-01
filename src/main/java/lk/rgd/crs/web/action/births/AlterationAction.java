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
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.util.GenderUtil;
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
    private Map<Integer, String> allDsDivisionList;
    private Map<Integer, String> allBdDivisionList;
    private Map<Integer, String> userLocations;
    private Map<Integer, Boolean> alterationApprovalPermission;
    private List<String[]> birthAlterationApprovalList;
    private List birthAlterationApprovedList;

    private User user;
    private Alteration27 alt27;
    private Alteration27A alt27A;
    private Alteration52_1 alt52_1;
    private DeclarantInfo declarant;
    private ParentInfo parent;
    private Date dateReceived;
    private int[] index;
    private BitSet indexCheck;
    private HashMap approveStatus = new HashMap();
    private int numberOfAppPending;

    private ChildInfo child;
    private FatherInfo father;
    private GrandFatherInfo grandFather;
    private MarriageInfo marriage;
    private AlterationInformatInfo informant;
    private NotifyingAuthorityInfo notifyingAuthority;
    private ConfirmantInfo confirmant;
    private BirthRegisterInfo register;


    private int pageNo;
    private int pageType;
    private int noOfRows;
    private long bdId;   // If present, it should be used to fetch a new BD instead of creating a new one (we are in edit mode)
    private Long nicOrPin;
    private String districtName;
    private String dsDivisionName;
    private String bdDivisionName;

    private boolean bcOfFather;
    private boolean bcOfMother;
    private boolean mcOfParents;
    private String otherDocuments;
    private String comments;

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
    private Long idUKey;
    private long serialNo; //to be used in the case where search is performed from confirmation 1 page.
    private boolean allowApproveAlteration;
    private boolean nextFlag;
    private boolean previousFlag;
    private List<BirthAlteration> birthAlterationPendingApprovalList;
    private int divisionAltaration;
    private Date alterationRecivedDate;
    private Date dateReceivedFrom;
    private Date dateReceivedTo;
    private BirthAlteration.AlterationType alterationType;


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

    //load birth alteration home page

    public String birthAlterationHome() {
        return SUCCESS;
    }

    public String birthAlterationSearch() {
        BirthDeclaration bdf = new BirthDeclaration();
        populateBasicLists();
        pageType = 1;

        if (idUKey != null) {
            bdf = service.getById(idUKey);
            logger.debug("load birth with idUKey :{}", bdf.getIdUKey());
        } else if (nicOrPin != null) {
            bdf = service.getByPINorNIC(nicOrPin, user);
        } else if (birthDivisionId != 0 && serialNo != 0) {
            bdf = service.getActiveRecordByBDDivisionAndSerialNo(bdDivisionDAO.getBDDivisionByPK
                    (birthDivisionId), serialNo, user);
        }

        if (bdf == null) {
            addActionError(getText("cp1.error.entryNotAvailable"));
            pageType = 0;
        } else {

            try {
                if (!(bdf.getRegister().getStatus() == BirthDeclaration.State.ARCHIVED_CERT_PRINTED)) {
                    addActionError(getText("cp1.error.entryNotPrinted"));
                    pageType = 0;
                }
                getBirthCertificateInfo(bdf);
            }
            catch (Exception e) {
                handleErrors(e);
                addActionError(getText("cp1.error.entryNotAvailable"));
                pageType = 0;
            }
            if (pageType == 1) {
                if (sectionOfAct == 3) {
                    alt27A = new Alteration27A();
                    populateAlt27A(bdf);
                } else if (sectionOfAct == 2) {
                    alt52_1 = new Alteration52_1();
                    populateAlt52_1(bdf);
                }
                parent = bdf.getParent();
                alt27 = new Alteration27();
                alt27.setChildFullNameOfficialLang(bdf.getChild().getChildFullNameOfficialLang());
                alt27.setChildFullNameEnglish(bdf.getChild().getChildFullNameEnglish());
                logger.debug("populate child in information of child :{}", alt27.getChildFullNameEnglish());
            }
            populateBasicLists();
            populateCountryRacesAndAllDSDivisions();
        }

        idUKey = null;
        return SUCCESS;
    }

    private void populateAlt52_1(BirthDeclaration bdf) {
        parent = bdf.getParent();
        child = bdf.getChild();
        InformantInfo bdfInformant = bdf.getInformant();
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

    }

    private void populateAlt27A(BirthDeclaration bdf) {
        if (bdf != null) {
            parent = bdf.getParent();
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
    }

    public String birthAlteration() {
        BirthAlteration ba;
        if (idUKey != null) {
            ba = alterationService.getByIDUKey(idUKey, user);
        } else {
            ba = new BirthAlteration();
        }
        ba.setAlt27(alt27); /*Child's full name is save in any act*/
        switch (sectionOfAct) {
            //case 2 is used to set alteration52_1
            case 1:
                ba.setType(BirthAlteration.AlterationType.TYPE_27);
                ba.setApprovalStatuses(new BitSet(WebConstants.BIRTH_ALTERATION_APPROVE_ALT27));
                break;
            //case 2 is used to set alteration27A
            case 2:
                ba.setType(alterationType);
                if (divisionAltaration > 0) {
                    alt52_1.setBirthDivision(bdDivisionDAO.getBDDivisionByPK(divisionAltaration));
                }
                if (motherCountryId > 0) {
                    alt52_1.getMother().setMotherCountry(countryDAO.getCountry(motherCountryId));
                }
                if (motherRaceId > 0) {
                    alt52_1.getMother().setMotherRace(raceDAO.getRace(motherRaceId));
                }
                ba.setAlt52_1(alt52_1);
                ba.setApprovalStatuses(new BitSet(WebConstants.BIRTH_ALTERATION_APPROVE_ALT52_1));
                break;
            case 3:
                ba.setType(BirthAlteration.AlterationType.TYPE_27A);
                logger.debug("father country id is :{}", fatherCountryId);
                if (fatherCountryId > 0) {
                    alt27A.getFather().setFatherCountry(countryDAO.getCountry(fatherCountryId));
                }
                if (fatherRaceId > 0) alt27A.getFather().setFatherRace(raceDAO.getRace(fatherRaceId));
                ba.setAlt27A(alt27A);
                ba.setAlt52_1(null);
                ba.setApprovalStatuses(new BitSet(WebConstants.BIRTH_ALTERATION_APPROVE_ALT27A));
                break;

        }
        if (birthDivisionId > 0) {
            ba.setBirthRecordDivision(bdDivisionDAO.getBDDivisionByPK(birthDivisionId));
        }
        ba.setBcOfFather(bcOfFather);
        ba.setBcOfMother(bcOfMother);
        ba.setMcOfParents(mcOfParents);
        ba.setComments(comments);
        ba.setOtherDocuments(otherDocuments);
        ba.setStatus(BirthAlteration.State.DATA_ENTRY);
        ba.setBdfIDUKey(bdId);
        ba.setDeclarant(declarant);
        ba.setDateReceived(dateReceived);
/*        logger.debug("ba value is :{}",ba.getBdfIDUKey());*/
        if (idUKey != null) {
            alterationService.updateBirthAlteration(ba, user);
            logger.debug("Updated a  Birth Alteration with Alteration idUKey  :{}", ba.getIdUKey());
        } else {
            alterationService.addBirthAlteration(ba, user);
            logger.debug("Add a new Birth Alteration with Alteration idUKey  :{}", ba.getIdUKey());
        }
        /*check permission to approval birth alteration  */
        approveRightsToUser = user.isAllowedAccessToBDDSDivision(ba.getBirthRecordDivision().
                getDsDivision().getDsDivisionUKey());
        idUKey = ba.getIdUKey();
        bdId = ba.getBdfIDUKey();
        pageType = 1;
        initPermission();
        return SUCCESS;
    }

    public String editBirthAlteration() {
        BirthAlteration ba = alterationService.getByIDUKey(idUKey, user);
        if (ba != null) {
            bdId = ba.getBdfIDUKey();
            BirthDeclaration bdf = service.getById(bdId);
            alt27 = ba.getAlt27();
            alt27A = ba.getAlt27A();
            alt52_1 = ba.getAlt52_1();
            dateReceived = ba.getDateReceived();
            declarant = ba.getDeclarant();
            bcOfFather = ba.isBcOfFather();
            bcOfMother = ba.isBcOfMother();
            mcOfParents = ba.isMcOfParents();
            comments = ba.getComments();
            pageType = 1;
            //information for search option
            if (bdf != null) {
                getBirthCertificateInfo(bdf);
                ba.setStatus(BirthAlteration.State.DATA_ENTRY);
            }
            if (ba.getType() == BirthAlteration.AlterationType.TYPE_27A) {
                populateAlt27A(bdf);
                sectionOfAct = 3;
            } else if (ba.getType() == BirthAlteration.AlterationType.TYPE_27) {
                alt27 = ba.getAlt27();
                sectionOfAct = 1;
            } else if (ba.getType() != BirthAlteration.AlterationType.TYPE_27
                    && ba.getType() != BirthAlteration.AlterationType.TYPE_27A) {
                sectionOfAct = 2;
                populateAlt52_1(bdf);
            }

        }


        populateBasicLists();
        populateCountryRacesAndAllDSDivisions();
        return SUCCESS;
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
            String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
            districtName = districtDAO.getNameByPK(birthDistrictId, language);
            dsDivisionName = dsDivisionDAO.getNameByPK(dsDivisionId, language);
            bdDivisionName = bdDivisionDAO.getNameByPK(birthDivisionId, language);
        }

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
        noOfRows = appParametersDAO.getIntParameter(BA_APPROVAL_ROWS_PER_PAGE);
        pageNo = 1;
        populateBasicLists();
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

        try {
            if (idUKey != null) {
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
            logger.info("The Birth Alteration List is Null");
        }
        checkPermissionToApproval();
        initPermission();
        populateBasicLists();
        return SUCCESS;
    }

    private void checkPermissionToApproval() {
        alterationApprovalPermission = new HashMap<Integer, Boolean>();
        if (birthAlterationPendingApprovalList != null) {
            BDDivision birthDivision;
            int birthDSDivsion;
            for (int i = 0; i < birthAlterationPendingApprovalList.size(); i++) {
                birthDivision = birthAlterationPendingApprovalList.get(i).getBirthRecordDivision();
                birthDSDivsion = birthDivision.getDsDivision().getDsDivisionUKey();
                boolean approveRights = user.isAllowedAccessToBDDSDivision(birthDSDivsion);
                alterationApprovalPermission.put(i, approveRights);
            }
        }
    }

    /**
     * responsible for approving a requested birth alteration field
     *
     * @return
     */

    public String approveInit() {
        numberOfAppPending = 0;
        logger.debug("bdId :{} ,idUKey :{}", bdId, idUKey);
        BirthDeclaration bdf = service.getById(bdId);
        BirthAlteration ba = alterationService.getByIDUKey(idUKey, user);
        setLanguage(((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage());
        if (ba == null || bdf == null) {
            return ERROR;
        } else {
            logger.debug("Loaded Birth Alteration with idUKey : {}", ba.getIdUKey());
            alt27 = ba.getAlt27();
            alt27A = ba.getAlt27A();
            alt52_1 = ba.getAlt52_1();
            String language = bdf.getRegister().getPreferredLanguage();
            child = bdf.getChild();
            if (child != null && alt27 != null) {
                if (child.getChildFullNameEnglish().equals(alt27.getChildFullNameEnglish()) &&
                        child.getChildFullNameOfficialLang().equals(alt27.getChildFullNameOfficialLang())) {
                    alt27 = null;
                }
            }
            birthAlterationApprovalList = new ArrayList();
            birthAlterationApprovedList = new ArrayList();
            alterationType = ba.getType();
            if (alterationType == BirthAlteration.AlterationType.TYPE_27) {
                logger.debug("loading birth alteration record of alt27 of idUKey  :{}", ba.getIdUKey());
                compareAndAdd(Alteration27.CHILD_FULL_NAME_OFFICIAL_LANG, bdf.getChild().
                        getChildFullNameOfficialLang(), alt27.getChildFullNameOfficialLang(), 0);
                compareAndAdd(Alteration27.CHILD_FULL_NAME_ENGLISH, bdf.getChild().getChildFullNameEnglish(),
                        alt27.getChildFullNameEnglish(), 0);
                sectionOfAct = 1;
                logger.debug("Child full name in English is :{}", alt27.getChildFullNameEnglish());
            } else if (alterationType != BirthAlteration.AlterationType.TYPE_27A &&
                    alterationType != BirthAlteration.AlterationType.TYPE_27) {
                logger.debug("loading birth alteration record of alt52_1 of idUKey  :{}", ba.getIdUKey());
                sectionOfAct = 2;
                changesOfAlt52_1(bdf, language);
            } else if (alterationType == BirthAlteration.AlterationType.TYPE_27A) {
                logger.debug("loading birth alteration record of alt27A of idUKey  :{}", ba.getIdUKey());
                sectionOfAct = 3;
                changesOfAlt27A(bdf, language);
            }
            if (birthAlterationApprovalList != null) {
                logger.debug("number of changes in birth alteration of idUKey {} is :{}", idUKey,
                        birthAlterationApprovalList.size());
            }
            int countApproved = 0;
            BitSet approvalsBitSet = ba.getApprovalStatuses();
            index = new int[approvalsBitSet.length()];
            for (int i = 0; i < approvalsBitSet.length(); i++) {
                if (approvalsBitSet.get(i)) {
                    index[i] = i;
                    countApproved++;
                } else {
                    index[i] = 0;
                }
            }
            if (pageType == 2) {
                applyChanges = true;
            }

            if (countApproved == 0 && birthAlterationApprovalList != null) {
                BitSet changedFields = new BitSet();
                int a = approvalsBitSet.length();
                for (int i = 0; i < a; i++) {
                    changedFields.set(i, false);
                }
                for (int i = 0; i < birthAlterationApprovalList.size(); i++) {
                    String[] ChangeField = birthAlterationApprovalList.get(i);
                    int fieldIndex = Integer.parseInt(ChangeField[0]);
                    changedFields.set(fieldIndex, true);
                }
                ba.setChangedfields(changedFields);
                alterationService.updateBirthAlteration(ba, user);
            }

            initPermission();
            populateBasicLists();
            return SUCCESS;
        }
    }

    private void changesOfAlt27A(BirthDeclaration bdf, String language) {
        father = alt27A.getFather();
        ParentInfo parent;
        parent = bdf.getParent();
        String[] compareChanges;
        grandFather = alt27A.getGrandFather();
        GrandFatherInfo grandFatherOriginal = bdf.getGrandFather();
        if (grandFather != null && grandFatherOriginal != null) {
            compareAndAdd(Alteration27A.GRAND_FATHER_FULLNAME, grandFatherOriginal.getGrandFatherFullName(),
                    grandFather.getGrandFatherFullName(), 0);
            compareAndAdd(Alteration27A.GRAND_FATHER_NIC_OR_PIN, grandFatherOriginal.getGrandFatherNICorPIN(),
                    grandFather.getGrandFatherNICorPIN(), 0);
            compareAndAdd(Alteration27A.GRAND_FATHER_BIRTH_YEAR, grandFatherOriginal.getGrandFatherBirthYear(),
                    grandFather.getGrandFatherBirthYear(), 0);
            compareAndAdd(Alteration27A.GRAND_FATHER_BIRTH_PLACE, grandFatherOriginal.getGrandFatherBirthPlace(),
                    grandFather.getGrandFatherBirthPlace(), 0);
            compareAndAdd(Alteration27A.GREAT_GRAND_FATHER_FULLNAME, grandFatherOriginal.getGreatGrandFatherFullName(),
                    grandFather.getGreatGrandFatherFullName(), 0);
            compareAndAdd(Alteration27A.GREAT_GRAND_FATHER_NIC_OR_PIN, grandFatherOriginal.getGreatGrandFatherNICorPIN(),
                    grandFather.getGreatGrandFatherNICorPIN(), 0);
            compareAndAdd(Alteration27A.GREAT_GRAND_FATHER_BIRTH_YEAR, grandFatherOriginal.getGreatGrandFatherBirthYear(),
                    grandFather.getGreatGrandFatherBirthYear(), 0);
            compareAndAdd(Alteration27A.GREAT_GRAND_FATHER_BIRTH_PLACE, grandFatherOriginal.getGreatGrandFatherBirthPlace(),
                    grandFather.getGreatGrandFatherBirthPlace(), 0);
            logger.debug("Check and add to approval list all information of {} (GrandFather) of idUKey :{}",
                    grandFather.getGrandFatherFullName(), idUKey);

        }
        if (father != null && parent != null) {
            compareAndAdd(Alteration27A.FATHER_FULLNAME, parent.getFatherFullName(), father.getFatherFullName(), 0);
            compareAndAdd(Alteration27A.FATHER_NIC_OR_PIN, parent.getFatherNICorPIN(), father.getFatherNICorPIN(), 0);
            compareAndAdd(Alteration27A.FATHER_BIRTH_DATE, parent.getFatherDOB(), father.getFatherDOB(), 0);
            compareAndAdd(Alteration27A.FATHER_BIRTH_PLACE, parent.getFatherPlaceOfBirth(), father.getFatherPlaceOfBirth(), 0);
            compareAndAdd(Alteration27A.FATHER_COUNTRY, parent.getFatherCountry(), father.getFatherCountry(), 1);
            compareAndAdd(Alteration27A.FATHER_PASSPORT, parent.getFatherPassportNo(), father.getFatherPassportNo(), 0);
            compareAndAdd(Alteration27A.FATHER_RACE, parent.getFatherRace(), father.getFatherRace(), 2);
            logger.debug("Check and add to approval list all information of {} (Father) of idUKey :{}",
                    father.getFatherFullName(), idUKey);
        }
        marriage = alt27A.getMarriage();
        MarriageInfo marriageOriginal = bdf.getMarriage();
        if (marriage != null && marriageOriginal != null) {
            compareAndAdd(Alteration27A.WERE_PARENTS_MARRIED, marriageOriginal.getParentsMarried(), marriage.getParentsMarried(), 0);
            compareAndAdd(Alteration27A.PLACE_OF_MARRIAGE, marriageOriginal.getPlaceOfMarriage(), marriage.getPlaceOfMarriage(), 0);
            compareAndAdd(Alteration27A.DATE_OF_MARRIAGE, marriageOriginal.getDateOfMarriage(), marriage.getDateOfMarriage(), 0);
            logger.debug("Check and add to all field of Marriage to approval list of idUKey :{}", idUKey);
        }
        if (alt27A.getMothersNameAfterMarriage() != null) {
            compareAndAdd(Alteration27A.MOTHER_NAME_AFTER_MARRIAGE, parent.getMotherFullName(),
                    alt27A.getMothersNameAfterMarriage(), 0);
        }

    }

    private void changesOfAlt52_1(BirthDeclaration bdf, String language) {
        child = bdf.getChild();
        register = bdf.getRegister();
        if (child != null) {
            compareAndAdd(Alteration52_1.DATE_OF_BIRTH, child.getDateOfBirth(), alt52_1.getDateOfBirth(), 0);
            compareAndAdd(Alteration52_1.PLACE_OF_BIRTH, child.getPlaceOfBirth(), alt52_1.getPlaceOfBirth(), 0);
            compareAndAdd(Alteration52_1.PLACE_OF_BIRTH_ENGLISH, child.getPlaceOfBirthEnglish(),
                    alt52_1.getPlaceOfBirthEnglish(), 0);
            compareAndAdd(Alteration52_1.GENDER, child.getChildGender(), alt52_1.getChildGender(), 4);
        }
        compareAndAdd(Alteration52_1.BIRTH_DIVISION, register.getBirthDivision(), alt52_1.getBirthDivision(), 3);
        MotherInfo mother = alt52_1.getMother();
        ParentInfo parent = bdf.getParent();
        if (mother != null && parent != null) {
            compareAndAdd(Alteration52_1.MOTHER_FULLNAME, parent.getMotherFullName(), mother.getMotherFullName(), 0);
            compareAndAdd(Alteration52_1.MOTHER_BIRTH_PLACE, parent.getMotherPlaceOfBirth(), mother.getMotherPlaceOfBirth(), 0);
            compareAndAdd(Alteration52_1.MOTHER_PASSPORT, parent.getMotherPassportNo(), mother.getMotherPassportNo(), 0);
            compareAndAdd(Alteration52_1.MOTHER_AGE_AT_BIRTH, parent.getMotherAgeAtBirth(), mother.getMotherAgeAtBirth(), 0);
            compareAndAdd(Alteration52_1.MOTHER_ADDRESS, parent.getMotherAddress(), mother.getMotherAddress(), 0);
            compareAndAdd(Alteration52_1.MOTHER_BIRTH_DATE, parent.getMotherDOB(), mother.getMotherDOB(), 0);
            compareAndAdd(Alteration52_1.MOTHER_COUNTRY, parent.getMotherCountry(), mother.getMotherCountry(), 1);
            compareAndAdd(Alteration52_1.MOTHER_RACE, parent.getMotherRace(), mother.getMotherRace(), 2);
        }
        //compare the informant information
        informant = alt52_1.getInformant();
        InformantInfo informantOriginal = bdf.getInformant();
        if (informant != null && informantOriginal != null) {
            compareAndAdd(Alteration52_1.INFORMANT_TYPE, informantOriginal.getInformantType().name(), informant.getInformantType().name(), 0);
            compareAndAdd(Alteration52_1.INFORMANT_NIC_OR_PIN, informantOriginal.getInformantNICorPIN(), informant.getInformantNICorPIN(), 0);
            compareAndAdd(Alteration52_1.INFORMANT_NAME, informantOriginal.getInformantName(), informant.getInformantName(), 0);
            compareAndAdd(Alteration52_1.INFORMANT_ADDRESS, informantOriginal.getInformantAddress(), informant.getInformantAddress(), 0);
        }
        logger.debug("length of the alteration :{}", birthAlterationApprovalList.size());

    }

    private void compareAndAdd(int index, Object bdfName, Object baName, int type) {
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

    public String alterationApproval() {
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
    }

    public String rejectAlteration() {
        BirthAlteration ba = alterationService.getByIDUKey(idUKey, user);
        ba.setStatus(BirthAlteration.State.REJECT);
        alterationService.updateBirthAlteration(ba, user);
        return SUCCESS;
    }

    public String birthAlterationApplyChanges() {
        BirthAlteration ba = new BirthAlteration();
        Hashtable approvalsBitSet = new Hashtable();
        if (idUKey != null) {
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


    public String printBirthAlterationNotice() {
        birthAlterationApprovalList = new ArrayList();
        birthAlterationApprovedList = new ArrayList();
        setLanguage(((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage());
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
    }

    private void addToNoticeAlt27A(BirthDeclaration bdf, BitSet approvedFields, BitSet rejectedFields) {
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

    private void addToNoticeAlt52_1(BirthDeclaration bdf, BitSet approvedFields, BitSet rejectedFields) {
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
    }

    private void addToApprovalList(int index, Object bdfName, Object baName, boolean app, boolean rej, int type) {
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


    /* private boolean checkApprovalOrReject(boolean app ,boolean rej){
        if(app){
            return true;
        }               else if(rej){

        }
    }*/

    private void paginationHandler(int recordsFound) {
        if (recordsFound == appParametersDAO.getIntParameter(BA_APPROVAL_ROWS_PER_PAGE)) {
            setNextFlag(true);
        } else {
            setNextFlag(false);
        }
    }

    private void populateBasicLists() {
        userLocations = user.getActiveLocations(language);
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

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
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

    public boolean isBcOfFather() {
        return bcOfFather;
    }

    public void setBcOfFather(boolean bcOfFather) {
        this.bcOfFather = bcOfFather;
    }

    public boolean isBcOfMother() {
        return bcOfMother;
    }

    public void setBcOfMother(boolean bcOfMother) {
        this.bcOfMother = bcOfMother;
    }

    public boolean isMcOfParents() {
        return mcOfParents;
    }

    public void setMcOfParents(boolean mcOfParents) {
        this.mcOfParents = mcOfParents;
    }

    public String getOtherDocuments() {
        return otherDocuments;
    }

    public void setOtherDocuments(String otherDocuments) {
        this.otherDocuments = otherDocuments;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public Date getDateReceivedTo() {
        return dateReceivedTo;
    }

    public void setDateReceivedTo(Date dateReceivedTo) {
        this.dateReceivedTo = dateReceivedTo;
    }

    public Date getDateReceivedFrom() {
        return dateReceivedFrom;
    }

    public void setDateReceivedFrom(Date dateReceivedFrom) {
        this.dateReceivedFrom = dateReceivedFrom;
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
}
