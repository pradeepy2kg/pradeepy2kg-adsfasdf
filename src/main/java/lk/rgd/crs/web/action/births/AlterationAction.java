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
    private Map<Integer, String> allDSDivisionList;
    private Map<Integer, String> userLocations;
    private Map<Integer, Boolean> alterationApprovalPermission;
    private List birthAlterationApprovalList;

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
    private int divisionAlt;
    private Date alterationRecivedDate;
    private Date dateReceivedFrom;
    private Date dateReceivedTo;
    private BirthAlteration.AlterationType alterationType;


    private String language;


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
            // check that birth Certificate is printed
            //   populateAlteration(bdf);
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
                birthDivisionId = 2;// alt52_1.getBirthDivision().getDivisionId();
                logger.debug("birth id :{}", birthDivisionId);
            } else {
                editChildInfo = true;
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
        // TODO fix this to lookup using idUKey
        BirthAlteration ba = new BirthAlteration();
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
                if (birthDivisionId > 0) {
                    alt52_1.setBirthDivision(bdDivisionDAO.getBDDivisionByPK(birthDivisionId));
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
            ba.setBirthRecodDivision(bdDivisionDAO.getBDDivisionByPK(birthDivisionId));
        }
        ba.setBcOfFather(bcOfFather);
        ba.setBcOfMother(bcOfMother);
        ba.setMcOfParents(mcOfParents);
        ba.setComments(comments);
        ba.setOtherDocuments(otherDocuments);
        ba.setStatus(BirthAlteration.State.DATA_ENTRY);
        ba.setBdfIDUKey(idUKey);
        ba.setDeclarant(declarant);
        ba.setDateReceived(dateReceived);
        alterationService.addBirthAlteration(ba, user);
        logger.debug("Add a new Birth Alteration with Alteration idUKey  :{}", ba.getIdUKey());
        idUKey = ba.getIdUKey();
        bdId = ba.getBdfIDUKey();
        pageType = 1;
        initPermission();
        return SUCCESS;
    }

    public String editbirthAlteration() {
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
            }
            if (ba.getType() == BirthAlteration.AlterationType.TYPE_27A) {
                populateAlt27A(bdf);
                sectionOfAct = 3;
            } else if (ba.getType() != BirthAlteration.AlterationType.TYPE_27
                    || ba.getType() != BirthAlteration.AlterationType.TYPE_27A) {
                sectionOfAct = 2;
                populateAlt52_1(bdf);
            } else if (ba.getType() != BirthAlteration.AlterationType.TYPE_27) {
                sectionOfAct = 1;
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

            idUKey = bdf.getIdUKey();
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
        if (idUKey != null) {
            try {
                BirthAlteration baApprovalPending = alterationService.getByIDUKey(idUKey, user);
                if (birthAlterationPendingApprovalList == null) {
                    birthAlterationPendingApprovalList = new ArrayList<BirthAlteration>();
                }
                birthAlterationPendingApprovalList.add(baApprovalPending);
                logger.debug("filter Birth Alteration to approve by Birth Alteration Serial Number :{}", idUKey);
            } catch (Exception CRSRuntimeException) {
                addActionError(getText("permission.to.search.Requested.Entry"));
                logger.debug("User {} can not filter birth alteration with idUKey :{}", user.getUserId(), idUKey);
            }
        } else if (locationUKey != 0) {
            // TODO - we cannot give a list awaiting reply only on received date - it has to be on DS division and date
            logger.debug("filter Birth Alteration to approve by idUKey of the location :{}", locationUKey);
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByUserLocationIdUKey(
                    locationUKey, pageNo, noOfRows, user);
        } else if (birthDivisionId != 0) {
            logger.debug("filter Birth Alteration to approve by Birth Serial Number :{}", serialNo);
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByBDDivision(
                    bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows);

        }
        if (birthAlterationPendingApprovalList != null) {
            paginationHandler(birthAlterationPendingApprovalList.size());
            logger.debug("number of rows in Birth Alteration Approval List is :{}", birthAlterationPendingApprovalList.size());
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
                birthDivision = birthAlterationPendingApprovalList.get(i).getBirthRecodDivision();
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
        //todo has to be implemented
        numberOfAppPending = 0;
        BirthDeclaration bdf = service.getById(bdId);
        BirthAlteration ba = alterationService.getByIDUKey(idUKey, user);
        if (ba == null || bdf == null) {
            return ERROR;
        } else {
            logger.debug("Loaded Birth Alteration with idUKey : {}", ba.getIdUKey());
            indexCheck = ba.getApprovalStatuses();
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
            alterationType = ba.getType();
            if (alterationType == BirthAlteration.AlterationType.TYPE_27) {
                logger.debug("loading birth alteration record of alt27 of idUKey  :{}", ba.getIdUKey());
                compareAndAdd(Alteration27.CHILD_FULL_NAME_OFFICIAL_LANG, bdf.getChild().
                        getChildFullNameOfficialLang(), alt27.getChildFullNameOfficialLang());
                compareAndAdd(Alteration27.CHILD_FULL_NAME_ENGLISH, bdf.getChild().getChildFullNameEnglish(),
                        alt27.getChildFullNameEnglish());
                sectionOfAct = 1;
                logger.debug("Child full name in English is :{}", alt27.getChildFullNameEnglish());
            } else if (!(alterationType != BirthAlteration.AlterationType.TYPE_27A ||
                    alterationType != BirthAlteration.AlterationType.TYPE_27)) {
                logger.debug("loading birth alteration record of alt52_1 of idUKey  :{}", ba.getIdUKey());
                sectionOfAct = 2;
                changesOfAlt52_1(bdf, language);
            } else if (alterationType == BirthAlteration.AlterationType.TYPE_27A) {
                logger.debug("loading birth alteration record of alt27A of idUKey  :{}", ba.getIdUKey());
                sectionOfAct = 3;
                changesOfAlt27A(bdf, language);
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
            compareAndAdd(Alteration27A.GRAND_FATHER_FULLNAME, grandFatherOriginal.getGrandFatherFullName(), grandFather.getGrandFatherFullName());
            compareAndAdd(Alteration27A.GRAND_FATHER_NIC_OR_PIN,
                    grandFatherOriginal.getGrandFatherNICorPIN(), grandFather.getGrandFatherNICorPIN());
            String grandFatherDOBinBA = null;
            String grandFatherDOBinBDF = null;
            if (grandFatherOriginal.getGrandFatherBirthYear() != null)
                grandFatherDOBinBDF = grandFatherOriginal.getGrandFatherBirthYear().toString();
            if (grandFather.getGrandFatherBirthYear() != null)
                grandFatherDOBinBA = grandFather.getGrandFatherBirthYear().toString();
            compareAndAdd(Alteration27A.GRAND_FATHER_BIRTH_YEAR, grandFatherDOBinBDF, grandFatherDOBinBA);
            compareAndAdd(Alteration27A.GRAND_FATHER_BIRTH_PLACE, grandFatherOriginal.getGrandFatherBirthPlace(), grandFather.getGrandFatherBirthPlace());
            compareAndAdd(Alteration27A.GREAT_GRAND_FATHER_FULLNAME,
                    grandFatherOriginal.getGreatGrandFatherFullName(), grandFather.getGreatGrandFatherFullName());
            compareAndAdd(Alteration27A.GREAT_GRAND_FATHER_NIC_OR_PIN,
                    grandFatherOriginal.getGreatGrandFatherNICorPIN(), grandFather.getGreatGrandFatherNICorPIN());
            String grandGrandFatherDOBinBA = null;
            String grandGrandFatherDOBinBDF = null;
            if (grandFatherOriginal.getGreatGrandFatherBirthYear() != null) {
                grandGrandFatherDOBinBDF = grandFatherOriginal.getGreatGrandFatherBirthYear().toString();
            }
            if (grandFather.getGreatGrandFatherBirthYear() != null) {
                grandGrandFatherDOBinBA = grandFather.getGreatGrandFatherBirthYear().toString();
            }
            compareAndAdd(Alteration27A.GREAT_GRAND_FATHER_BIRTH_YEAR, grandGrandFatherDOBinBDF, grandGrandFatherDOBinBA);
            compareAndAdd(Alteration27A.GREAT_GRAND_FATHER_BIRTH_PLACE, grandFatherOriginal.getGreatGrandFatherBirthPlace(), grandFather.getGreatGrandFatherBirthPlace());
            logger.debug("Check and add to approval list all information of {} (GrandFather) of idUKey :{}", grandFather.getGrandFatherFullName(), idUKey);

        }
        if (father != null && parent != null) {
            compareAndAdd(Alteration27A.FATHER_FULLNAME, parent.getFatherFullName(), father.getFatherFullName());
            compareAndAdd(Alteration27A.FATHER_NIC_OR_PIN, parent.getFatherNICorPIN(), father.getFatherNICorPIN());
            String fatherDOBinBA = null;
            String fatherDOBinBDF = null;
            if (parent.getFatherDOB() != null) {
                fatherDOBinBDF = parent.getFatherDOB().toString();
            }
            if (father.getFatherDOB() != null) {
                fatherDOBinBA = father.getFatherDOB().toString();
            }
            compareAndAdd(Alteration27A.FATHER_BIRTH_DATE, fatherDOBinBDF, fatherDOBinBA);
            compareAndAdd(Alteration27A.FATHER_BIRTH_PLACE, parent.getFatherPlaceOfBirth(), father.getFatherPlaceOfBirth());
            //if father country is not null in both ba and bd
            if (father.getFatherCountry() != null && parent.getFatherCountry() != null) {
                compareAndAdd(Alteration27A.FATHER_COUNTRY, countryDAO.getNameByPK(parent.getFatherCountry().getCountryId(), language),
                        countryDAO.getNameByPK(father.getFatherCountry().getCountryId(), language));
                logger.debug("change the father country to :{}", countryDAO.getNameByPK(father.getFatherCountry().getCountryId(), language));
            }
            //if father country in not null in ba but null in bdf
            if (father.getFatherCountry() != null && parent.getFatherCountry() == null) {
                compareAndAdd(Alteration27A.FATHER_COUNTRY, null, countryDAO.getNameByPK(father.getFatherCountry().getCountryId(), language));

            }
            //if father country in  null in ba but not null in bdf
            if (father.getFatherCountry() == null && parent.getFatherCountry() != null) {
                compareAndAdd(Alteration27A.FATHER_COUNTRY, countryDAO.getNameByPK(parent.getFatherCountry().getCountryId(), language), "");
                logger.debug("Delete the country of the {} child's father  :{}", bdf.getChild().getChildFullNameOfficialLang(),
                        parent.getFatherCountry().getSiCountryName());
            }
            compareAndAdd(Alteration27A.FATHER_PASSPORT, parent.getFatherPassportNo(), father.getFatherPassportNo());
            if (father.getFatherRace() != null && parent.getFatherRace() != null) {
                compareAndAdd(Alteration27A.FATHER_RACE, raceDAO.getNameByPK(parent.getFatherRace().getRaceId(), language),
                        raceDAO.getNameByPK(father.getFatherRace().getRaceId(), language));
            }
            if (father.getFatherRace() != null && parent.getFatherRace() == null) {
                compareAndAdd(Alteration27A.FATHER_RACE, "", raceDAO.getNameByPK(father.getFatherRace().getRaceId(), language));
            }
            if (father.getFatherRace() == null && parent.getFatherRace() != null) {
                compareAndAdd(Alteration27A.FATHER_RACE, raceDAO.getNameByPK(parent.getFatherRace().getRaceId(), language), "");
            }
            logger.debug("Check and add to approval list all information of {} (Father) of idUKey :{}", father.getFatherFullName(), idUKey);
        }
        marriage = alt27A.getMarriage();
        MarriageInfo marriageOriginal = bdf.getMarriage();
        if (marriage != null && marriageOriginal != null) {
            String originalParentsMarriage = null;
            String marriageParents = null;
            if (marriageOriginal.getParentsMarried() != null) {
                originalParentsMarriage = marriageOriginal.getParentsMarried().toString();
            }
            if (marriage.getParentsMarried() != null) {
                marriageParents = marriage.getParentsMarried().toString();
            }
            compareAndAdd(Alteration27A.WERE_PARENTS_MARRIED, originalParentsMarriage, marriageParents);
            compareAndAdd(Alteration27A.PLACE_OF_MARRIAGE, marriageOriginal.getPlaceOfMarriage(), marriage.getPlaceOfMarriage());
            String originalMarriageDate = null;
            String marriageDate = null;
            if (marriageOriginal.getDateOfMarriage() != null) {
                originalMarriageDate = marriageOriginal.getDateOfMarriage().toString();
            }
            if (marriage.getDateOfMarriage() != null) {
                marriageDate = marriage.getDateOfMarriage().toString();
            }
            compareAndAdd(Alteration27A.DATE_OF_MARRIAGE, originalMarriageDate, marriageDate);
            logger.debug("Check and add to all field of Marriage to approval list of idUKey :{}", idUKey);

        }
        if (alt27A.getMothersNameAfterMarriage() != null) {
            compareChanges = new String[3];
            compareChanges[0] = Integer.toString(Alteration27A.MOTHER_NAME_AFTER_MARRIAGE);
            compareChanges[1] = parent.getMotherFullName();
            compareChanges[2] = alt27A.getMothersNameAfterMarriage();
            birthAlterationApprovalList.add(compareChanges);
            numberOfAppPending++;
            logger.debug("Check {} mother name after Marriage if idUKey :{}", compareChanges[0], idUKey);
        }

    }

    private void changesOfAlt52_1(BirthDeclaration bdf, String language) {
        child = bdf.getChild();
        register = bdf.getRegister();
        if (child != null) {
            if (alt52_1.getDateOfBirth() != null) {
                compareAndAdd(Alteration52_1.DATE_OF_BIRTH, child.getDateOfBirth().
                        toString(), alt52_1.getDateOfBirth().toString());
            }
            compareAndAdd(Alteration52_1.PLACE_OF_BIRTH, child.getPlaceOfBirth(), alt52_1.getPlaceOfBirth());
            compareAndAdd(Alteration52_1.PLACE_OF_BIRTH_ENGLISH, child.getPlaceOfBirthEnglish(), alt52_1.getPlaceOfBirthEnglish());
            compareAndAdd(Alteration52_1.GENDER, GenderUtil.getGender(child.getChildGender(), language),
                    GenderUtil.getGender(alt52_1.getChildGender(), language));
        }
        if (alt52_1.getBirthDivision() != null && register.getBirthDivision() != null) {
            compareAndAdd(Alteration52_1.BIRTH_DIVISION, bdDivisionDAO.getNameByPK(register.getBirthDivision().getBdDivisionUKey(),
                    language), bdDivisionDAO.getNameByPK(alt52_1.getBirthDivision().getBdDivisionUKey(), language));
        }
        MotherInfo mother = alt52_1.getMother();
        ParentInfo parent = bdf.getParent();
        if (mother != null && parent != null) {
            compareAndAdd(Alteration52_1.MOTHER_FULLNAME, parent.getMotherFullName(), mother.getMotherFullName());
            compareAndAdd(Alteration52_1.MOTHER_BIRTH_PLACE, parent.getMotherPlaceOfBirth(), mother.getMotherPlaceOfBirth());
            compareAndAdd(Alteration52_1.MOTHER_PASSPORT, parent.getMotherPassportNo(), mother.getMotherPassportNo());
            compareAndAdd(Alteration52_1.MOTHER_AGE_AT_BIRTH, parent.getMotherAgeAtBirth().toString(), mother.getMotherAgeAtBirth().toString());
            compareAndAdd(Alteration52_1.MOTHER_ADDRESS, parent.getMotherAddress(), mother.getMotherAddress());
            Date bdfDate = parent.getMotherDOB();
            Date baDate = mother.getMotherDOB();
            if (bdfDate != baDate) {
                String bdfMotherDOB = null, baMotherDOB = null;
                if (bdfDate != null) {
                    bdfMotherDOB = bdfDate.toString();
                }
                if (baDate != null) {
                    baMotherDOB = baDate.toString();
                }
                compareAndAdd(Alteration52_1.MOTHER_BIRTH_DATE, bdfMotherDOB, baMotherDOB);
            }
            Country bdfCountry = parent.getMotherCountry();
            Country baCountry = mother.getMotherCountry();
            if (bdfCountry != baCountry) {
                String bdfMotherCountry = null, baMotherCountry = null;
                if (bdfCountry != null) {
                    bdfMotherCountry = countryDAO.getNameByPK(bdfCountry.getCountryId(), language);
                }
                if (baCountry != null) {
                    baMotherCountry = countryDAO.getNameByPK(baCountry.getCountryId(), language);
                }
                compareAndAdd(Alteration52_1.MOTHER_COUNTRY, bdfMotherCountry, baMotherCountry);
            }
            //if mother race is not null in both bdf and ba
            Race bdfRace = parent.getMotherRace();
            Race baRace = mother.getMotherRace();
            if (bdfRace != baRace) {
                String bdfMotherRace = null, baMotherRace = null;
                if (bdfRace != null) {
                    bdfMotherRace = raceDAO.getNameByPK(bdfRace.getRaceId(), language);
                }
                if (baRace != null) {
                    baMotherRace = raceDAO.getNameByPK(baRace.getRaceId(), language);
                }
                compareAndAdd(Alteration52_1.MOTHER_RACE, bdfMotherRace, baMotherRace);
            }
        }
        //compare the informant information
        informant = alt52_1.getInformant();
        InformantInfo informantOriginal = bdf.getInformant();
        if (informant != null && informantOriginal != null) {
            compareAndAdd(Alteration52_1.INFORMANT_TYPE, informantOriginal.getInformantType().name(), informant.getInformantType().name());
            compareAndAdd(Alteration52_1.INFORMANT_NIC_OR_PIN, informantOriginal.getInformantNICorPIN(), informant.getInformantNICorPIN());
            compareAndAdd(Alteration52_1.INFORMANT_NAME, informantOriginal.getInformantName(), informant.getInformantName());
            compareAndAdd(Alteration52_1.INFORMANT_ADDRESS, informantOriginal.getInformantAddress(), informant.getInformantAddress());
        }


    }

    private void compareAndAdd(int index, String bdfName, String baName) {
        String[] compareChanges = new String[3];
        compareChanges[0] = Integer.toString(index);
        if (bdfName != null) {
            if (bdfName.trim().length() == 0) compareChanges[1] = null;
            else compareChanges[1] = bdfName.trim();
        } else compareChanges[1] = null;
        if (baName != null) {
            if (baName.trim().length() == 0) compareChanges[2] = null;
            else compareChanges[2] = baName.trim();
        } else compareChanges[2] = null;
        boolean checkApp = true;
        if (!(indexCheck.get(index))) {
            if (compareChanges[1] != null && compareChanges[2] != null) {
                if (!compareChanges[2].equals(compareChanges[1])) {
                    birthAlterationApprovalList.add(compareChanges);
                    numberOfAppPending++;
                }
            }
            if ((compareChanges[2] == null && compareChanges[1] != null) || (compareChanges[2] != null && compareChanges[1] == null)) {
                logger.debug("value :{}", compareChanges[0]);
                birthAlterationApprovalList.add(compareChanges);
                numberOfAppPending++;
            }
        }
    }

    public String alterationApproval() {
        BirthAlteration ba = alterationService.getByIDUKey(idUKey, user);
        int lengthOfBitSet = 0;
        Hashtable approvalsBitSet = new Hashtable();
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
        boolean appStatus = false;
        if (index.length == numberOfAppPending) {
            appStatus = true;
            logger.debug("The Alteration of {} child is fully approved ", ba.getAlt27().getChildFullNameOfficialLang());
        }
        logger.debug("length of the apprrovals list is  :{}", approvalsBitSet.size());
        alterationService.approveBirthAlteration(ba, approvalsBitSet, appStatus, user);
        ba = alterationService.getByIDUKey(idUKey, user);
        logger.debug("New Bit Set After Approval  :{}", ba.getApprovalStatuses());
        pageType = 2;
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

    public int getDivisionAlt() {
        return divisionAlt;
    }

    public void setDivisionAlt(int divisionAlt) {
        this.divisionAlt = divisionAlt;
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
}
