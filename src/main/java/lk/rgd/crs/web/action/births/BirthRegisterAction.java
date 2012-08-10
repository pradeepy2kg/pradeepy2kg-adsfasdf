package lk.rgd.crs.web.action.births;


import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.*;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.common.util.MarriedStatusUtil;
import lk.rgd.common.util.NameFormatUtil;
import lk.rgd.common.util.PinAndNicUtils;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.AssignmentDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.GNDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.api.service.BirthAlterationService;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.CommonUtil;
import lk.rgd.crs.web.util.DateState;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * EntryAction is a struts action class  responsible for  data capture for a birth declaration and the persistence of the same.
 * Data capture forms (4) will be kept in session until persistence at the end of 4th page.
 */
public class BirthRegisterAction extends ActionSupport implements SessionAware {
    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);
    private static final String BIRTH_ADVANCE_SEARCH = "advanceSearch";

    private final BirthRegistrationService service;
    private final BirthAlterationService birthAlterationService;
    private final AdoptionOrderService adoptionService;
    private final DistrictDAO districtDAO;
    private final CountryDAO countryDAO;
    private final RaceDAO raceDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final CommonUtil commonUtil;
    private final AppParametersDAO appParametersDAO;
    private final UserLocationDAO userLocationDAO;
    private final LocationDAO locationDAO;
    private final AssignmentDAO assignmentDAO;
    private final GNDivisionDAO gnDivisionDAO;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PopulationRegistry ecivilService;

    private Map<Integer, String> districtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> gnDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> allDistrictList;
    private Map<Integer, String> allDSDivisionList;
    private Map<Integer, String> locationList;
    private Map<String, String> userList;

    private List<UserWarning> warnings;
    private List<BirthDeclaration> archivedEntryList;

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
    private OldBDInfo oldBDInfo;
    private BitSet changedFields;
    private Person person;


    private int pageNo; //pageNo is used to decide the current pageNo of the Birth Registration Form
    private long bdId;   // If present, it should be used to fetch a new BD instead of creating a new one (we are in edit mode)
    private long oldBdId;    // bdId of previously persisted birth declaration, used in add new entry in batch mode
    private long adoptionId; // adoption order id, used in registering an adopted child
    private boolean confirmationSearchFlag;//if true request to search an entry based on serialNo
    private int rowNumber;
    private int counter;

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    private int birthDistrictId;
    private int birthDivisionId;
    private int gnDivisionId;
    private int fatherCountry;
    private int motherCountry;
    private int fatherRace;
    private int motherRace;
    private int dsDivisionId;
    private int motherDistrictId;
    private int motherDSDivisionId;
    private int motherGNDivisionId;
    private int bdfLateOrBelated;
    private String caseFileNumber;
    private String newComment;
    private String language;
    private long idUKey;

    private long serialNo; //to be used in the case where search is performed from confirmation 1 page.
    private boolean addNewMode;
    private boolean back;
    private boolean allowApproveBDF;
    private boolean allowPrintCertificate;
    private boolean editMode;
    private boolean advanceSearch;

    private BirthDeclaration.BirthType birthType;
    private boolean directPrint;
    private boolean directPrintBirthCertificate;
    private int rgdErrorCode;
    private boolean certificateSearch;
    private boolean skipConfirmationChages;


    private String gender;
    private String genderEn;
    private String childDistrict;
    private String childDistrictEn;
    private String childDsDivision;
    private String childDsDivisionEn;
    private String fatherRacePrint;
    private String fatherRacePrintEn;
    private String motherRacePrint;
    private String motherRacePrintEn;
    private String marriedStatus;
    private String marriedStatusEn;
    private String returnAddress;
    private String unknownFieldPref;
    private String unknownFieldEn;

    public String welcome() {
        return SUCCESS;
    }

    public BirthRegisterAction(BirthRegistrationService service, AdoptionOrderService adoptionService, DistrictDAO districtDAO,
        CountryDAO countryDAO, RaceDAO raceDAO, BDDivisionDAO bdDivisionDAO, DSDivisionDAO dsDivisionDAO,
        AppParametersDAO appParametersDAO, UserLocationDAO userLocationDAO, LocationDAO locationDAO,
        AssignmentDAO assignmentDAO, BirthAlterationService birthAlterationService, CommonUtil commonUtil, GNDivisionDAO gnDivisionDAO, PopulationRegistry ecivilService, UserDAO userDAO, RoleDAO roleDAO) {
        this.service = service;
        this.adoptionService = adoptionService;
        this.districtDAO = districtDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.appParametersDAO = appParametersDAO;
        this.userLocationDAO = userLocationDAO;
        this.locationDAO = locationDAO;
        this.assignmentDAO = assignmentDAO;
        this.birthAlterationService = birthAlterationService;
        this.commonUtil = commonUtil;
        this.gnDivisionDAO = gnDivisionDAO;
        this.ecivilService = ecivilService;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;

        dsDivisionList = new HashMap<Integer, String>();
        bdDivisionList = new HashMap<Integer, String>();
    }

    /**
     * This method is responsible for loading and capture data for all 4 BDF pages as well
     * as their persistence. pageNo hidden variable which is passed to the action (empty=0 for the
     * very first form page) is used to decide which state of the process we are in. at the last step
     * only we do a persistence, until then all data will be in the session. This is a design decision
     * to limit DB writes. Master data population will be done before displaying every page.
     * This will have no performance impact as they will be cached in the back end.
     */
    public String birthDeclaration() {
        logger.debug("Step {} of 4 ", pageNo);
        BirthDeclaration bdf;
        if (back) {
            populate((BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
            if (pageNo == 1) {
                populateMotherFullLists((BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
            }
            return "form" + pageNo;
        }
        if (pageNo < 1) {
            return ERROR;
        }
        bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        switch (pageNo) {
            case 1:
                // checking serial number is already used and skip this in edit mode
                BirthDeclaration bd = service.getActiveRecordByBDDivisionAndSerialNo(register.getBirthDivision(),
                    register.getBdfSerialNo(), user);
                if (bd != null && bd.getIdUKey() != bdf.getIdUKey()) {
                    addFieldError("duplicateSerialNumberError", getText("p1.duplicateSerialNumber.label"));
                    pageNo = 0;
                }

                birthType = bdf.getRegister().getBirthType();
                bdf.setChild(child);
                register.setStatus(bdf.getRegister().getStatus());
                register.setCaseFileNumber(bdf.getRegister().getCaseFileNumber());
                register.setComments(bdf.getRegister().getComments());
                register.setAdoptionUKey(bdf.getRegister().getAdoptionUKey());
                bdf.setRegister(register);
                bdf.getRegister().setBirthType(birthType);
                // populate all district, DS and GN lists
                populateMotherFullLists(bdf);
                break;
            case 2:
                birthType = bdf.getRegister().getBirthType();
                bdf.setParent(parent);
                // Populate marriage information (if any)
                if (bdf.getIdUKey() == 0 && bdf.getMarriage().getDateOfMarriage() == null && bdf.getMarriage().getPlaceOfMarriage() == null) {
                    populateMarriageInfo(bdf);
                }
                break;
            case 3:
                birthType = bdf.getRegister().getBirthType();
                bdf.setMarriage(marriage);
                bdf.setGrandFather(grandFather);
                bdf.setInformant(informant);
                if (BirthDeclaration.BirthType.STILL != birthType && BirthDeclaration.BirthType.ADOPTION != birthType) {
                    bdfLateOrBelated = checkDateLateOrBelated(bdf);
                }
                if (bdf.getIdUKey() == 0) {
                    populateRegistrars(bdf);
                }
                break;
            case 4:
                birthType = bdf.getRegister().getBirthType();
                bdf.setNotifyingAuthority(notifyingAuthority);

                if (BirthDeclaration.BirthType.STILL != birthType && BirthDeclaration.BirthType.ADOPTION != birthType) {
                    bdfLateOrBelated = checkDateLateOrBelated(bdf);
                    if (bdfLateOrBelated == 1 || bdfLateOrBelated == 2) {
                        bdf.getRegister().setComments(register.getComments());
                        bdf.getRegister().setCaseFileNumber(register.getCaseFileNumber());
                    }
                    if (bdfLateOrBelated == 2) {
                        birthType = BirthDeclaration.BirthType.BELATED;
                        bdf.getRegister().setBirthType(birthType);
                    }
                }

                // all pages captured, proceed to persist after validations
                // todo data validations, exception handling and error reporting
                bdId = bdf.getIdUKey();
                boolean noError = true;

                if (bdId == 0) {
                    if (birthType == BirthDeclaration.BirthType.LIVE) {
                        service.addLiveBirthDeclaration(bdf, true, user);
                    } else if (birthType == BirthDeclaration.BirthType.STILL) {
                        service.addStillBirthDeclaration(bdf, true, user);
                    } else if (birthType == BirthDeclaration.BirthType.ADOPTION) {
                        service.addAdoptionBirthDeclaration(bdf, true, user);
                    } else if (birthType == BirthDeclaration.BirthType.BELATED) {
                        service.addBelatedBirthDeclaration(bdf, true, user);
                    }
                    bdId = bdf.getIdUKey();  // JPA is nice to us. it will populate this field after a new add.
                    addActionMessage(getText("saveSuccess.label"));
                } else {
                    //check before update
                    BirthDeclaration exBdf = service.getActiveRecordByBDDivisionAndSerialNo(
                        bdf.getRegister().getBirthDivision(), bdf.getRegister().getBdfSerialNo(), user);
                    if (exBdf != null && exBdf.getIdUKey() == 0) {
                        //trying to duplicate
                        addFieldError("duplicateSerialNumberError", getText("p1.duplicateSerialNumber.label"));
                        pageNo = 0;
                    }
                    try {
                        if (birthType == BirthDeclaration.BirthType.LIVE) {
                            service.editLiveBirthDeclaration(bdf, true, user);
                        } else if (birthType == BirthDeclaration.BirthType.STILL) {
                            service.editStillBirthDeclaration(bdf, true, user);
                        } else if (birthType == BirthDeclaration.BirthType.ADOPTION) {
                            service.editAdoptionBirthDeclaration(bdf, true, user);
                        } else if (birthType == BirthDeclaration.BirthType.BELATED) {
                            service.editBelatedBirthDeclaration(bdf, true, user);
                        }
                        addActionMessage(getText("edit.Data.Save.Success.label"));
                    } catch (CRSRuntimeException e) {
                        switch (e.getErrorCode()) {
                            case ErrorCodes.ILLEGAL_STATE:
                                addActionError(getText("error.cannot.edit.approved"));
                                noError = false;
                                break;
                            default:
                                logger.error("Unhandled exception");
                                return ERROR;
                        }
                    } catch (NullPointerException e) {
                        addActionError(getText("error.cannot.edit.deleted"));
                    }
                }
                session.remove(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
                session.remove(WebConstants.SESSION_OLD_BD_FOR_ADOPTION);

                // used to check user have approval authority and passed to BirthRegistrationFormDetails jsp
                if (noError) {
                    if (BirthDeclaration.BirthType.BELATED != birthType) {
                        allowApproveBDF = user.isAuthorized(Permission.APPROVE_BDF);
                    } else {
                        allowApproveBDF = user.isAuthorized(Permission.APPROVE_BDF_BELATED);
                    }
                }
        }
        if (!addNewMode && (pageNo != 4)) {
            session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("DistrictId: " + birthDistrictId + " ,BDDivisionId: " + birthDivisionId + " ,DSDivisionId: " +
                dsDivisionId);
        }
        populate(bdf);

        logger.debug("Birth Declaration: PageNo=" + pageNo);
        return "form" + pageNo;
    }

    /**
     * This method is responsible for loading and capture data for all 3 BDC pages as well
     * as their persistence. pageNo hidden variable which is passed to the action (empty=0 for the
     * very first form page) is used to decide which state of the process we are in. bdId field should be used to
     * determine the particular birth declaration entity on the initial visit to action. (after then it will be kept in the session)
     */
    public String birthConfirmation() {
        logger.debug("Step {} of 3 ", pageNo);
        BirthDeclaration bdf;
        if (back) {
            BirthDeclaration bdfSession = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
            populate(bdfSession);
            populateMotherFullLists(bdfSession);
            return "form" + pageNo;
        }
        if (pageNo < 1) {
            return ERROR;
        }
        bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
        switch (pageNo) {
            case 1:
                logger.debug("Step {} of 3 ", pageNo);
                bdf.getRegister().setBirthDivision(register.getBirthDivision());

                bdf.getChild().setDateOfBirth(child.getDateOfBirth());
                bdf.getChild().setChildGender(child.getChildGender());
                bdf.getRegister().setPreferredLanguage(register.getPreferredLanguage());
                bdf.getChild().setPlaceOfBirth(child.getPlaceOfBirth());
                bdf.getChild().setPlaceOfBirthEnglish(child.getPlaceOfBirthEnglish());

                bdf.getParent().setFatherNICorPIN(parent.getFatherNICorPIN());
                bdf.getParent().setFatherRace(parent.getFatherRace());
                bdf.getParent().setMotherNICorPIN(parent.getMotherNICorPIN());
                bdf.getParent().setMotherRace(parent.getMotherRace());
                bdf.getParent().setMotherGNDivision(parent.getMotherGNDivision());
                bdf.getParent().setMotherDSDivision(parent.getMotherDSDivision());
                bdf.getMarriage().setParentsMarried(marriage.getParentsMarried());
                break;
            case 2:
                logger.debug("Step {} of 3 ", pageNo);
                bdf.getChild().setChildFullNameOfficialLang(child.getChildFullNameOfficialLang());
                bdf.getChild().setChildFullNameEnglish(child.getChildFullNameEnglish());

                bdf.getParent().setFatherFullName(parent.getFatherFullName());
                bdf.getParent().setMotherFullName(parent.getMotherFullName());

                bdf.getParent().setFatherFullNameInEnglish(parent.getFatherFullNameInEnglish());
                bdf.getParent().setMotherFullNameInEnglish(parent.getMotherFullNameInEnglish());
                break;
            case 3:
                logger.debug("Step {} of 3 ", pageNo);
                bdf.getConfirmant().setConfirmantNICorPIN(confirmant.getConfirmantNICorPIN());
                bdf.getConfirmant().setConfirmantFullName(confirmant.getConfirmantFullName());
                bdf.getConfirmant().setConfirmantSignDate(confirmant.getConfirmantSignDate());
                bdf.getConfirmant().setConfirmantType(confirmant.getConfirmantType());

                logger.debug("skipConfirmationChanges {}", skipConfirmationChages);
                //todo exception handling, validations and error reporting
                if (skipConfirmationChages) {
                    //no confirmation changes by skipping 2 of 3BDC
                    if (bdf.getRegister().getBirthDivision() == null) {
                        //skip without selecting a valid Birth Declaration
                        addActionError(getText("cp3.confirmation.changes.invalid.operation"));
                        return ERROR;
                    } else {
                        if (bdf.getRegister().getStatus() == BirthDeclaration.State.CONFIRMATION_PRINTED) {
                            service.markLiveBirthDeclarationAsConfirmedWithoutChanges(bdf, user);
                            addActionMessage(getText("cp3.no.confirmation.changes.success"));
                            //todo make sure we don't have to approve now.
//                        } else {
//                            service.captureLiveBirthConfirmationChanges(bdf, user);
//                            allowApproveBDF = user.isAuthorized(Permission.APPROVE_BDF_CONFIRMATION);
//                            addActionMessage(getText("cp3.confirmation.changes.success"));
//                            //this is set to false because not to allow directly print the certificate it should have to approve
//                            skipConfirmationChages = false;
                        }
                    }
                } else {
                    try {
                        service.captureLiveBirthConfirmationChanges(bdf, user);
                        logger.debug("Mother name : {} ", bdf.getParent().getMotherFullName());
                        //setting permission for BirthConfirmationDetails page
                        allowApproveBDF = user.isAuthorized(Permission.APPROVE_BDF_CONFIRMATION);
                        addActionMessage(getText("cp3.confirmation.changes.success"));
                    } catch (CRSRuntimeException e) {
                        switch (e.getErrorCode()) {
                            case ErrorCodes.INVALID_STATE_FOR_CONFIRMATION_CHANGES:
                                logger.debug("invalid state for capture confirmation changes for " +
                                    "birth record idUKey : {}", bdf.getIdUKey());
                                addActionError(getText("error.invalid.state.for.capture.changes", new String[]{Long.toString(bdf.getIdUKey())}));
                                break;
                            default:
                                logger.debug("unable to capture confirmation changes for " +
                                    "birth record idUKey : {}", bdf.getIdUKey());
                                addActionError(getText("error.unable.to.capture.changes", new String[]{Long.toString(bdf.getIdUKey())}));
                        }
                    }
                }

                //jpa gives the newly added entries bdId instead of archived entry
                bdId = bdf.getIdUKey();
                session.remove(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
                logger.debug("Ds division : {} ", bdf.getRegister().getBirthDivision().getDsDivision().getDivisionId());
        }

        if (pageNo != 3) {
            session.put(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN, bdf);
        }
        populate(bdf);
        return "form" + pageNo;
    }

    //todo move following method to PrintAction

    /**
     * Load  List page which needs changes by parents
     *
     * @return pageLoad
     */
    public String confirmationPrintPageLoad() {
        try {
            BirthDeclaration bdf = service.getById(bdId, user);
            Calendar cal1 = new GregorianCalendar();
            cal1.add(Calendar.DATE, appParametersDAO.getIntParameter(AppParameter.CRS_BIRTH_CONFIRMATION_DAYS_PRINTED));
            bdf.getRegister().setLastDayForConfirmation(cal1.getTime());
            logger.debug("Set last day for confirmation as : {} for record : {}", cal1.getTime(), bdf.getIdUKey());

            bdf = service.loadValuesForPrint(bdf, user);
            bdId = bdf.getIdUKey();
            populate(bdf);
            //populate location list
            String language = bdf.getRegister().getPreferredLanguage();
            locationList = commonUtil.populateActiveUserLocations(user, language);
            if (locationList.size() > 0) {
                populateReturnAddress(null);
            }
            if (!(bdf.getRegister().getStatus() == BirthDeclaration.State.CONFIRMATION_PRINTED ||
                bdf.getRegister().getStatus() == BirthDeclaration.State.APPROVED)) {
                return ERROR;
            } else {
                beanPopulate(bdf);
                addActionMessage(getText("error.print.success"));
                return "pageLoad";
            }
        } catch (Exception e) {
            handleErrors(e);
            addActionError(getText("error.print.notSuccess"));
            return ERROR;
        }

    }

    private void populateReturnAddress(Location location) {
        if (location == null) {
            int locationId = locationList.keySet().iterator().next();
            location = locationDAO.getLocation(locationId);
        }
        if (AppConstants.SINHALA.equals(language)) {
            returnAddress = location.getSiLocationMailingAddress();
        }
        if (AppConstants.TAMIL.equals(language)) {
            returnAddress = location.getTaLocationMailingAddress();
        }
        if (AppConstants.ENGLISH.equals(language)) {
            returnAddress = location.getEnLocationMailingAddress();
        }
    }

    public String initBirthRegistration() {
        return SUCCESS;
    }

    public String initStillBirth() {
        return SUCCESS;
    }

    /**
     * Used in direct direct printing birth confirmation and still birth certificate page redirecting
     *
     * @return
     */
    public String pageLoad() {
        BirthDeclaration bdf = service.getById(bdId, user);
        bdId = bdf.getIdUKey();
        populate(bdf);
        return SUCCESS;
    }

    /**
     * This method is responsible for loading 1 of 4 BDF pages of Live and Still Birth Declaration.
     * if bdId is 0 it is a fresh birth declaration, if addNewMode is true it is in the batch mode switches to editable
     * mode. if bdId is greater than 0. for editing checks whether requested birthDeclaration is editable. If it is not
     * in the editable mode directed to error page
     *
     * @return
     */
    public String birthDeclarationInit() {
        BirthDeclaration bdf;
        logger.debug("Birth type is : {}", birthType);
        session.remove(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
        if (bdId == 0) {
            if (!addNewMode) {
                session.remove(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
            }
            bdf = new BirthDeclaration();
            bdf.getRegister().setBirthType(birthType);
        } else {
            editMode = true;
            bdf = service.getById(bdId, user);
            if (bdf.getRegister().getStatus() != BirthDeclaration.State.DATA_ENTRY) {  // edit not allowed
                addActionError(getText("p1.editNotAllowed"));
                return ERROR;
            }
        }
        session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);
        populate(bdf);
        return "form0";
    }

    /**
     * This method is used when adding a Birth Declaration for adopted child
     *
     * @return
     */
    public String adoptionDeclarationInit() {
        BirthDeclaration bdf;
        BirthDeclaration existingBDF = null;
        AdoptionOrder ao;
        logger.debug("Adding BDF of an adopted child. Birth Type : {}", birthType);
        session.remove(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        session.remove(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);

        if (adoptionId == 0) {
            addActionError(getText("adoption_order_id_invalid.label"));
            return ERROR;
        }
        ao = adoptionService.getById(adoptionId, user);
        if (ao == null) {
            addActionError(getText("adoption_order_id_invalid.label"));
            return ERROR;
        }
        if (ao.getStatus() != AdoptionOrder.State.ADOPTION_CERTIFICATE_PRINTED) {
            addActionError(getText("adoption_invalid_state.label"));
            return ERROR;
        }
        bdf = new BirthDeclaration();
        bdf.getRegister().setBirthType(birthType);
        bdf.getRegister().setAdoptionUKey(adoptionId);

        // population fields in adoption order to birth declaration
        bdf.getRegister().setAdoptionUKey(ao.getIdUKey());
        long existBDUKey = ao.getBirthCertificateNumber();

        if (existBDUKey != 0) {
            try {
                existingBDF = service.getById(existBDUKey, user);
            } catch (NullPointerException e) {
                logger.error("failed to find requested BDF :", e);
                addActionError(getText("adoption_invalid_birth_certificate_number.label"));
                return ERROR;
            }
        } else {
            long existSerial = ao.getBirthRegistrationSerial();
            int existBDivisionId = ao.getBirthDivisionId();

            if (existSerial != 0 && existBDivisionId != 0) {
                existingBDF = service.getActiveRecordByBDDivisionAndSerialNo(
                    bdDivisionDAO.getBDDivisionByPK(existBDivisionId), existSerial, user);
            } else {
                addActionError(getText("adoption_invalid_BDivision_or_serialNo.label"));
            }
        }
        logger.debug("Existing birth declaration IDUKey : {}", existBDUKey);
        if (existingBDF != null) {
            oldBDInfo = new OldBDInfo();
            populateOldBD(oldBDInfo, existingBDF);
            session.put(WebConstants.SESSION_OLD_BD_FOR_ADOPTION, oldBDInfo);
        }

        bdf.getChild().setDateOfBirth(ao.getChildBirthDate());
        bdf.getChild().setChildFullNameOfficialLang(ao.getChildNewName() != null ? ao.getChildNewName() : ao.getChildExistingName());
        bdf.getChild().setChildGender(ao.getChildGender());

        if (ao.isApplicantMother()) {
            bdf.getParent().setMotherNICorPIN(ao.getApplicantPINorNIC());
            bdf.getParent().setMotherFullName(ao.getApplicantName());
            bdf.getParent().setMotherAddress(ao.getApplicantAddress());
            if (ao.getApplicantCountryId() > 0) {
                bdf.getParent().setMotherCountry(countryDAO.getCountry(ao.getApplicantCountryId()));
            }
            bdf.getParent().setMotherPassportNo(ao.getApplicantPassport());
        } else {
            bdf.getParent().setFatherNICorPIN(ao.getApplicantPINorNIC());
            bdf.getParent().setFatherFullName(ao.getApplicantName());
            if (ao.getApplicantCountryId() > 0) {
                bdf.getParent().setFatherCountry(countryDAO.getCountry(ao.getApplicantCountryId()));
            }
            bdf.getParent().setFatherPassportNo(ao.getApplicantPassport());

            bdf.getParent().setMotherNICorPIN(ao.getWifePINorNIC());
            bdf.getParent().setMotherFullName(ao.getWifeName());
            if (ao.getWifeCountryId() > 0) {
                bdf.getParent().setMotherCountry(countryDAO.getCountry(ao.getWifeCountryId()));
            }
            bdf.getParent().setMotherPassportNo(ao.getWifePassport());
        }

        session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);
        populate(bdf);
        return "form0";
    }

    /**
     * Populate oldBDInfo bean used in adding birth declaration for adopted child in 1 of 4BDF pages.
     *
     * @param oldBDInfo  bean used to hold old birth declaration data
     * @param existingBD existing birth declaration
     */
    private void populateOldBD(OldBDInfo oldBDInfo, BirthDeclaration existingBD) {
        BDDivision existBdDivision = existingBD.getRegister().getBirthDivision();
        oldBDInfo.setSerialNumber(existingBD.getRegister().getBdfSerialNo());
        oldBDInfo.setBdDivisionName(bdDivisionDAO.getNameByPK(existBdDivision.getBdDivisionUKey(), language));
        oldBDInfo.setDistrictName(districtDAO.getNameByPK(existBdDivision.getDistrict().getDistrictUKey(), language));
        oldBDInfo.setDsDivisionName(dsDivisionDAO.getNameByPK(existBdDivision.getDsDivision().getDsDivisionUKey(), language));
        oldBDInfo.setGnDivisionName(gnDivisionDAO.getNameByPK(existingBD.getParent().getMotherGNDivision().getGnDivisionUKey(), language));
    }

    /**
     * Responsible for loading the 4BDF in non editable mode if the requested BirthDeclaration is a belated or Still
     * Birth related those will also be processed by this method
     *
     * @return
     */
    public String viewInNonEditableMode() {
        //todo add support for belated registration after finishing the backend
        //logger.debug("Non Editable Mode Step {} of 4 ", pageNo);
        BirthDeclaration bdf;
        logger.debug("initializing non editable mode for bdId {}", bdId);
        try {
            bdf = service.getById(bdId, user);
            if (bdf != null) {
                birthType = bdf.getRegister().getBirthType();
                bdfLateOrBelated = checkDateLateOrBelated(bdf);
                if (bdf.getRegister().getStatus().ordinal() == 5) {
                    logger.debug("searching revisions for bdId {} ", bdId);
                    archivedEntryList = service.getArchivedCorrectedEntriesForGivenSerialNo(
                        bdf.getRegister().getBirthDivision(), bdf.getRegister().getBdfSerialNo(), user);
                }
                if (BirthDeclaration.BirthType.ADOPTION == birthType) {
                    oldBDInfo = new OldBDInfo();
                    populateOldBD(oldBDInfo, bdf);
                }

                register = bdf.getRegister();
                child = bdf.getChild();
                parent = bdf.getParent();
                marriage = bdf.getMarriage();
                grandFather = bdf.getGrandFather();
                grandFather = bdf.getGrandFather();
                informant = bdf.getInformant();
                notifyingAuthority = bdf.getNotifyingAuthority();
            } else {
                addActionError(getText("message.birthRecord_null"));
                if (advanceSearch) {
                    return BIRTH_ADVANCE_SEARCH;
                }
            }
        } catch (Exception e) {
            handleErrors(e);
            addActionError(getText("p1.invalid.Entry"));
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * This method is responsible for loading 1 of the 3 BDC pages.
     * If bdId is 0 it is a fresh birth confirmation, else it is for
     * edit. In edit mode checks whether requested birthDeclaration
     * is in the editable. If it is not in the editable mode directed
     * to error page
     *
     * @return
     */
    public String birthConfirmationInit() {
        BirthDeclaration bdf, bcf;
        session.remove(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        session.remove(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
        if (bdId != 0) {
            //edit mode of confirmation
            try {
                bdf = service.getById(bdId, user);
                //get most recently archived record
                //on this stage of BD there cant be more than 1 archived record the only archive add when changes captured
                List<BirthDeclaration> archivedRecords = service.getArchivedCorrectedEntriesForGivenSerialNo(bdf.getRegister().
                    getBirthDivision(), bdf.getRegister().getBdfSerialNo(), user);
                if (archivedRecords.isEmpty()) {
                    //fist time capturing
                    bcf = bdf;
                } else {
                    //editing captured record
                    bcf = archivedRecords.get(0);
                }
                logger.debug("bdId is {} ", bdId);
                logger.debug("value of the status of bdf is :{}", bdf.getRegister().getStatus() == BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED);
                if (!(bdf.getRegister().getStatus() == BirthDeclaration.State.CONFIRMATION_PRINTED ||
                    bdf.getRegister().getStatus() == BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED)) {
                    addActionError(getText("cp1.error.editNotAllowed"));
                    //otherwise it will populate details while giving error message cannot edit
                    bdf = new BirthDeclaration();
                    bcf = new BirthDeclaration();
                    bdId = 0;
                }
            } catch (Exception e) {
                handleErrors(e);
                addActionError(getText("cp1.error.entryNotAvailable"));
                bdf = new BirthDeclaration();
                bcf = new BirthDeclaration();
                bdId = 0;
            }
        } else {
            bdf = new BirthDeclaration(); // just go to the confirmation 1 page
            bcf = new BirthDeclaration();
        }
        session.put(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN, bdf);
        populateMotherFullLists(bcf);
        session.put(WebConstants.SESSION_BIRTH_CONFIRMATION_DB_BEAN, bcf);
        populate(bdf);
        return "form0";
    }

    private void populateMotherFullLists(BirthDeclaration birthDeclaration) {
        ParentInfo parent = birthDeclaration.getParent();
        DSDivision motherDS = parent.getMotherDSDivision();
        if (motherDS != null) {
            GNDivision motherGN = (parent.getMotherGNDivision() != null) ?
                gnDivisionDAO.getGNDivisionByPK(parent.getMotherGNDivision().getGnDivisionUKey()) : null;
            String districtPrint = null, dsPrint = null, gnPrint = null;
            if (language.equals(AppConstants.SINHALA)) {
                gnPrint = (motherGN != null) ? motherGN.getSiGNDivisionName() : null;
                dsPrint = motherDS.getSiDivisionName();
                districtPrint = motherDS.getDistrict().getSiDistrictName();
            } else if (language.equals(AppConstants.TAMIL)) {
                gnPrint = (motherGN != null) ? motherGN.getTaGNDivisionName() : null;
                dsPrint = motherDS.getTaDivisionName();
                districtPrint = motherDS.getDistrict().getTaDistrictName();
            } else {
                gnPrint = (motherGN != null) ? motherGN.getEnGNDivisionName() : null;
                dsPrint = motherDS.getEnDivisionName();
                districtPrint = motherDS.getDistrict().getEnDistrictName();
            }
            birthDeclaration.getParent().setMotherDistrictPrint(districtPrint);
            birthDeclaration.getParent().setMotherDsDivisionPrint(dsPrint);
            birthDeclaration.getParent().setMotherGNDivisionPrint(gnPrint);
            motherDistrictId = motherDS.getDistrict().getDistrictUKey();
            motherDSDivisionId = motherDS.getDsDivisionUKey();
            motherGNDivisionId = (motherGN != null) ? motherGN.getGnDivisionUKey() : 0;
            //populate lists
            allDistrictList = districtDAO.getAllDistrictNames(language, user);
            populateAllDSDivisionList(motherDistrictId, language);
            try {
                gnDivisionList = gnDivisionDAO.getGNDivisionNames(motherDSDivisionId, language, user);
            } catch (RGDRuntimeException e) {
                gnDivisionList = Collections.emptyMap();
            }
        } else {
            /** getting full district list and DS list for mother */
            allDistrictList = districtDAO.getAllDistrictNames(language, user);
            if (!allDistrictList.isEmpty()) {
                int selectedDistrictId = allDistrictList.keySet().iterator().next();
                allDSDivisionList = dsDivisionDAO.getAllDSDivisionNames(selectedDistrictId, language, user);
                try {
                    gnDivisionList = gnDivisionDAO.getGNDivisionNames(selectedDistrictId, language, user);
                } catch (RGDRuntimeException e) {
                    gnDivisionList = Collections.emptyMap();
                }
            }
        }
    }

    /**
     * This is responsible for skipping the 2 of 3BDC
     * if there is no confirmation changes received.
     *
     * @return
     */
    public String skipConfirmationChanges() {
        BirthDeclaration bdf;
        logger.debug("Skipping Confirmation Changes for bdId {}", bdId);
        bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
        populate(bdf);
        return "form2";
    }

    /**
     * Load Birth Certificate List Page
     *
     * @return pageLoad
     */
    public String birthCertificatePrint() {
        try {
            BirthDeclaration bdf = service.getWithRelationshipsById(bdId, user);

            bdf = service.loadValuesForPrint(bdf, user);
            birthType = bdf.getRegister().getBirthType();

            if (!(bdf.getRegister().getStatus() == BirthDeclaration.State.ARCHIVED_CERT_GENERATED ||
                bdf.getRegister().getStatus() == BirthDeclaration.State.ARCHIVED_CERT_PRINTED ||
                bdf.getRegister().getStatus() == BirthDeclaration.State.ARCHIVED_ALTERED)) {
                return ERROR;
            } else {

                beanPopulate(bdf);

                locationList = commonUtil.populateActiveUserLocations(user, language);
                if (!locationList.isEmpty()) {
                    logger.debug("Location list size: {}", locationList.size());
                    userList = new HashMap<String, String>();
                    for (int i = 0; i < locationList.size(); i++) {
                        int currentLocationId = locationList.keySet().iterator().next();
                        for (User u : userLocationDAO.getBirthCertSignUsersByLocationId(currentLocationId, true)) {
                            userList.put(u.getUserId(), NameFormatUtil.getDisplayName(u.getUserName(), 50));
                        }
                    }
                    logger.debug("User list size: {}", userList.size());
                }
                if ((bdf.getRegister().getOriginalBCIssueUser() == null &&
                    bdf.getRegister().getOriginalBCPlaceOfIssue() == null &&
                    BirthDeclaration.State.ARCHIVED_CERT_GENERATED == bdf.getRegister().getStatus()) || (certificateSearch)) {
                    //TODO use primary location to find User location
                    UserLocation userLocation = userLocationDAO.getUserLocation(
                        userList.keySet().iterator().next(), locationList.keySet().iterator().next());
                    String prefLang = bdf.getRegister().getPreferredLanguage();

                    bdf.getRegister().setOriginalBCIssueUserSignPrint(userLocation.getUser().getUserSignature(prefLang));
                    bdf.getRegister().setOriginalBCPlaceOfIssueSignPrint(userLocation.getLocation().getLocationSignature(prefLang));
                    bdf.getRegister().setOriginalBCPlaceOfIssuePrint(userLocation.getLocation().getLocationName(prefLang));
                    populateReturnAddress(null);
                } else {
                    populateReturnAddress(bdf.getRegister().getOriginalBCPlaceOfIssue());
                }

                gender = child.getChildGenderPrint();
                genderEn = GenderUtil.getGender(child.getChildGender(), AppConstants.ENGLISH);
                childDistrict = register.getDistrictPrint();
                childDistrictEn = register.getBirthDistrict().getEnDistrictName();
                childDsDivision = register.getDsDivisionPrint();
                childDsDivisionEn = register.getDsDivision().getEnDivisionName();
                fatherRacePrint = parent.getFatherRacePrint();
                unknownFieldPref = lk.rgd.common.util.CommonUtil.getUnknownForCertificate(register.getPreferredLanguage());
                unknownFieldEn = lk.rgd.common.util.CommonUtil.getUnknownForCertificate(AppConstants.ENGLISH);

                if (parent.getFatherRace() != null) {
                    fatherRacePrintEn = raceDAO.getNameByPK(parent.getFatherRace().getRaceId(), AppConstants.ENGLISH);
                }
                motherRacePrint = parent.getMotherRacePrint();
                if (parent.getMotherRace() != null) {
                    motherRacePrintEn = raceDAO.getNameByPK(parent.getMotherRace().getRaceId(), AppConstants.ENGLISH);
                }
                marriedStatus = marriage.getParentsMarriedPrint();
                if (marriedStatus != null) {
                    marriedStatusEn = MarriedStatusUtil.getMarriedStatus(marriage.getParentsMarried(), AppConstants.ENGLISH);
                }

                addActionMessage("message.print.success");
                allowPrintCertificate = user.isAuthorized(Permission.PRINT_BIRTH_CERTIFICATE);
                if (!certificateSearch) {
                    //loading alterations done to this certificate
                    archivedEntryList = service.getHistoricalBirthDeclarationRecordForBDDivisionAndSerialNo(bdf.getRegister().getBirthDivision(),
                        bdf.getRegister().getBdfSerialNo(), bdf.getIdUKey(), user);
                    //create changed bit set to display *  marks
                    if (archivedEntryList.size() > 0) {
                        displayChangesInStarMark(archivedEntryList);
                    }
                }
                if (BirthDeclaration.BirthType.STILL != bdf.getRegister().getBirthType()) {
                    person = ecivilService.findPersonByPIN(bdf.getChild().getPin(), user);
                }
                return "pageLoad";
            }
        } catch (Exception e) {
            handleErrors(e);
            addActionError(getText("error.print.notSuccess"));
            return ERROR;
        }
    }

    private void displayChangesInStarMark(List<BirthDeclaration> birthDeclarations) {
        changedFields = new BitSet();
        for (int i = 0; i < birthDeclarations.size(); i++) {
            List<BirthAlteration> birthAlterations = birthAlterationService.getBirthAlterationByBirthCertificateNumber(birthDeclarations.get(i).getIdUKey(), user);
            if (birthAlterations.size() == 1) {
                BirthAlteration ba = birthAlterations.get(0);
                changedFields.or(ba.getApprovalStatuses());
            }

        }
        logger.debug("bit sets merge and final bit set : {}", changedFields);
    }

    /**
     * Populate BDF for pages
     */
    private void beanPopulate(BirthDeclaration bdf) {
        child = bdf.getChild();
        parent = bdf.getParent();
        grandFather = bdf.getGrandFather();
        marriage = bdf.getMarriage();
        informant = bdf.getInformant();
        confirmant = bdf.getConfirmant();
        register = bdf.getRegister();
        notifyingAuthority = bdf.getNotifyingAuthority();
    }

    private void handleErrors(Exception e) {
        logger.error("Handle Error  ", e);
        //todo pass the error to the error.jsp page
    }

    private void populateRegistrars(BirthDeclaration bdf) {
        if (!addNewMode && (bdf.getRegister().getBirthDivision() == null ||
            bdf.getRegister().getBirthDivision().getBdDivisionUKey() != birthDivisionId)) {
            String checkString = String.valueOf(bdf.getRegister().getBdfSerialNo());
            if ("0".equals(checkString.substring(4, 5))) {
                // Load ADR information as Notifying Officer.
                List<User> adrList = userDAO.getUsersByRoleAndAssignedBDDSDivision(roleDAO.getRole(Role.ROLE_ADR), bdf.getRegister().getDsDivision(), user);
                if (adrList != null && adrList.size() > 0) {
                    User adr = adrList.iterator().next();
                    notifyingAuthority = new NotifyingAuthorityInfo();
                    // Set Notifying officer name to Name of the ADR
                    notifyingAuthority.setNotifyingAuthorityName(adr.getUserName());
                    if (String.valueOf(adr.getPin()) != null) {
                        // Set Notifying officer PIN to be the ADR PIN
                        notifyingAuthority.setNotifyingAuthorityPIN(String.valueOf(adr.getPin()));
                    }

                    String locationCode = bdf.getRegister().getDsDivision().getDistrict().getDistrictId() + "-" + bdf.getRegister().getDsDivision().getDivisionId();
                    // Load the location according to the Birth Record entry division.
                    Location location = locationDAO.getLocationByCode(locationCode).iterator().next();
                    // Set Notifying officer address to be the location address.
                    if (location != null) {
                        if (AppConstants.ENGLISH.equals(language)) {
                            notifyingAuthority.setNotifyingAuthorityAddress(location.getEnLocationMailingAddress());
                        } else if (AppConstants.SINHALA.equals(language)) {
                            notifyingAuthority.setNotifyingAuthorityAddress(location.getSiLocationMailingAddress());
                        } else if (AppConstants.TAMIL.equals(language)) {
                            notifyingAuthority.setNotifyingAuthorityAddress(location.getTaLocationMailingAddress());
                        }
                    }
                    bdf.setNotifyingAuthority(notifyingAuthority);
                    logger.debug("ADR info populated for ADR userID : {}", adr.getUserId());
                }
            } else if ("1".equals(checkString.substring(4, 5))) {
                // Load Registrar information as Notifying Officer.
                // Registrar data populated to as Notifying authority considering the birthDivision
                List<Assignment> registrarAssigns = assignmentDAO.getAllAssignmentsByBDorMRDivisionAndType(
                    bdf.getRegister().getBirthDivision().getBdDivisionUKey(), Assignment.Type.BIRTH, true, false);
                // only the first registrar in the assigned list is loaded as the notifying authority
                if (registrarAssigns.size() > 0) {
                    Registrar registrar = registrarAssigns.get(0).getRegistrar();
                    notifyingAuthority = new NotifyingAuthorityInfo();
                    notifyingAuthority.setNotifyingAuthorityName(registrar.getFullNameInOfficialLanguage());
                    if (registrar.getNic() != null) {
                        notifyingAuthority.setNotifyingAuthorityPIN(registrar.getNic());
                    }
                    if (registrar.getCurrentAddress() != null) {
                        notifyingAuthority.setNotifyingAuthorityAddress(registrar.getCurrentAddress());
                    }
                    bdf.setNotifyingAuthority(notifyingAuthority);
                    logger.debug("Registrar info populated for RegistrarUKey : {}", registrar.getRegistrarUKey());
                }
            }
        }
    }

    private void populateMarriageInfo(BirthDeclaration bdf) {
        logger.debug("Populate Marriage Information");
        if (bdf.getParent() != null) {
            if (bdf.getParent().getFatherNICorPIN() != null) {
                String PINOrNIC = bdf.getParent().getFatherNICorPIN();
                Person father = null;
                marriage = new MarriageInfo();
                if(PinAndNicUtils.isValidPIN(PINOrNIC)){
                    // PIN, There is only 1 record for a PIN.
                    father = ecivilService.findPersonByPIN(Long.parseLong(PINOrNIC), user);
                }
                if (father != null && father.getLastMarriage() != null && father.getLastMarriage().getDateOfMarriage() != null && father.getLastMarriage().getPlaceOfMarriage() != null) {
                    marriage.setDateOfMarriage(father.getLastMarriage().getDateOfMarriage());
                    marriage.setPlaceOfMarriage(father.getLastMarriage().getPlaceOfMarriage());
                    logger.debug("Set marriage date {} and place of marriage {}", marriage.getDateOfMarriage(), marriage.getPlaceOfMarriage());
                }
                bdf.setMarriage(marriage);
            }else if(bdf.getParent().getMotherNICorPIN() != null){
                String PINOrNIC = bdf.getParent().getMotherNICorPIN();
                Person mother = null;
                marriage = new MarriageInfo();
                if(PinAndNicUtils.isValidPIN(PINOrNIC)){
                    // PIN, There is only 1 record for a PIN.
                    mother = ecivilService.findPersonByPIN(Long.parseLong(PINOrNIC), user);
                }
                if (mother != null && mother.getLastMarriage() != null && mother.getLastMarriage().getDateOfMarriage() != null && mother.getLastMarriage().getPlaceOfMarriage() != null) {
                    marriage.setDateOfMarriage(mother.getLastMarriage().getDateOfMarriage());
                    marriage.setPlaceOfMarriage(mother.getLastMarriage().getPlaceOfMarriage());
                    logger.debug("Set marriage date {} and place of marriage {}", marriage.getDateOfMarriage(), marriage.getPlaceOfMarriage());
                }
                bdf.setMarriage(marriage);
            }
        }
    }

    /**
     * Populate master data to the UIs
     */
    private void populate(BirthDeclaration bdf) {
        populateBasicLists(language);
        birthType = bdf.getRegister().getBirthType();

        if (bdf.getIdUKey() != 0) {
            editMode = true;
        }
        /**
         *  under "Add another mode", few special values need to be3 preserved from last entry .
         *  Pre setting serial, dateOfRegistration, district, division and notifyAuthority in batch mode data entry.
         *  serial number incrementing by one.
         */
        if (addNewMode) {
            BirthDeclaration oldBdf = service.getById(oldBdId, user);
            register = new BirthRegisterInfo();
            register.setBdfSerialNo(oldBdf.getRegister().getBdfSerialNo() + 1);
            register.setDateOfRegistration(oldBdf.getRegister().getDateOfRegistration());
            register.setBirthDivision(oldBdf.getRegister().getBirthDivision());
            register.setBirthType(BirthDeclaration.BirthType.LIVE);

            child = new ChildInfo();
            //set gender to male
            child.setChildGender(0);

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
            bdf.setChild(child);
            session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);
            logger.debug("Districts, DS and BD divisions set from earlier (AddNewMode) info : {} {}", birthDistrictId, dsDivisionId);
            return;  // end of populating fields for this mode.
        }

        beanPopulate(bdf);

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

        if (!idsPopulated) {         // populate district and ds div Ids with user preferences or set to 0 temporarily
            if (user.getPrefBDDistrict() != null) {
                birthDistrictId = user.getPrefBDDistrict().getDistrictUKey();
                logger.debug("Preferred district {} set in user {}", birthDistrictId, user.getUserId());
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

        // following painful null checks are needed because the DB may have incomplete data
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

            DSDivision ds = parent.getMotherDSDivision();
            if (ds != null) {
                motherDSDivisionId = ds.getDsDivisionUKey();
                motherDistrictId = ds.getDistrict().getDistrictUKey();
            }
            gnDivisionId = (parent.getMotherGNDivision() != null) ? parent.getMotherGNDivision().getGnDivisionUKey() : 0;
            //todo remove this assignment both gnDivisionId and motherGNDivision id is used for same purposes so duplicate variable
            motherGNDivisionId = gnDivisionId;
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
        //Populate division lists and set the first item as default
        birthDistrictId = commonUtil.findDefaultListValue(districtList, birthDistrictId);
        dsDivisionId = commonUtil.findDivisionList(dsDivisionList, dsDivisionId, birthDistrictId, AppConstants.DS_DIVISION, user, language);
        birthDivisionId = commonUtil.findDivisionList(bdDivisionList, birthDivisionId, dsDivisionId, AppConstants.BIRTH, user, language);
    }

    private void populateBasicLists(String language) {
        countryList = countryDAO.getCountries(language);
        districtList = districtDAO.getDistrictNames(language, user);
        raceList = raceDAO.getRaces(language);
    }

    private void populateAllDSDivisionList(int districtID, String language) {
        allDSDivisionList = dsDivisionDAO.getAllDSDivisionNames(districtID, language, user);
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
        logger.debug("setting User: {} and UserLanguage : {}", user.getUserName(), language);
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

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
        logger.debug("setting DS Division: {}", dsDivisionId);
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

    public String getCaseFileNumber() {
        return caseFileNumber;
    }

    public void setCaseFileNumber(String caseFileNumber) {
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

    public boolean isAllowApproveBDF() {
        return allowApproveBDF;
    }

    public void setAllowApproveBDF(boolean allowApproveBDF) {
        this.allowApproveBDF = allowApproveBDF;
    }

    public long getOldBdId() {
        return oldBdId;
    }

    public void setOldBdId(long oldBdId) {
        this.oldBdId = oldBdId;
    }

    public BirthDeclaration.BirthType getBirthType() {
        return birthType;
    }

    public void setBirthType(BirthDeclaration.BirthType birthType) {
        this.birthType = birthType;
    }

    public int getRgdErrorCode() {
        return rgdErrorCode;
    }

    public void setRgdErrorCode(int rgdErrorCode) {
        this.rgdErrorCode = rgdErrorCode;
    }

    public boolean isDirectPrint() {
        return directPrint;
    }

    public void setDirectPrint(boolean directPrint) {
        this.directPrint = directPrint;
    }

    public boolean isSkipConfirmationChages() {
        return skipConfirmationChages;
    }

    public void setSkipConfirmationChages(boolean skipConfirmationChages) {
        this.skipConfirmationChages = skipConfirmationChages;
    }


    public String getGender() {
        return gender;
    }

    public boolean isDirectPrintBirthCertificate() {
        return directPrintBirthCertificate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDirectPrintBirthCertificate(boolean directPrintBirthCertificate) {
        this.directPrintBirthCertificate = directPrintBirthCertificate;
    }

    public String getChildDistrict() {
        return childDistrict;
    }

    public void setChildDistrict(String childDistrict) {
        this.childDistrict = childDistrict;
    }

    public String getChildDsDivision() {
        return childDsDivision;
    }

    public void setChildDSDivision(String childDsDivision) {
        this.setChildDsDivision(childDsDivision);
    }

    public String getFatherRacePrint() {
        return fatherRacePrint;
    }

    public void setFatherRacePrint(String fatherRacePrint) {
        this.fatherRacePrint = fatherRacePrint;
    }

    public String getMotherRacePrint() {
        return motherRacePrint;
    }

    public void setMotherRacePrint(String motherRacePrint) {
        this.motherRacePrint = motherRacePrint;
    }

    public String getMarriedStatusEn() {
        return marriedStatusEn;
    }

    public void setMarriedStatusEn(String marriedStatusEn) {
        this.marriedStatusEn = marriedStatusEn;
    }

    public String getGenderEn() {
        return genderEn;
    }

    public void setGenderEn(String genderEn) {
        this.genderEn = genderEn;
    }

    public String getChildDistrictEn() {
        return childDistrictEn;
    }

    public void setChildDistrictEn(String childDistrictEn) {
        this.childDistrictEn = childDistrictEn;
    }

    public void setChildDsDivision(String childDsDivision) {
        this.childDsDivision = childDsDivision;
    }

    public String getChildDsDivisionEn() {
        return childDsDivisionEn;
    }

    public void setChildDsDivisionEn(String childDsDivisionEn) {
        this.childDsDivisionEn = childDsDivisionEn;
    }

    public String getFatherRacePrintEn() {
        return fatherRacePrintEn;
    }

    public void setFatherRacePrintEn(String fatherRacePrintEn) {
        this.fatherRacePrintEn = fatherRacePrintEn;
    }

    public String getMotherRacePrintEn() {
        return motherRacePrintEn;
    }

    public void setMotherRacePrintEn(String motherRacePrintEn) {
        this.motherRacePrintEn = motherRacePrintEn;
    }

    public List<BirthDeclaration> getArchivedEntryList() {
        return archivedEntryList;
    }

    public void setArchivedEntryList(List<BirthDeclaration> archivedEntryList) {
        this.archivedEntryList = archivedEntryList;
    }

    public BirthRegistrationService getService() {
        return this.service;
    }

    public BDDivisionDAO getBDDivisionDAO() {
        return this.bdDivisionDAO;
    }

    public long getAdoptionId() {
        return adoptionId;
    }

    public void setAdoptionId(long adoptionId) {
        this.adoptionId = adoptionId;
    }

    public OldBDInfo getOldBDInfo() {
        return oldBDInfo;
    }

    public void setOldBDInfo(OldBDInfo oldBDInfo) {
        this.oldBDInfo = oldBDInfo;
    }

    public String getMarriedStatus() {
        return marriedStatus;
    }

    public void setMarriedStatus(String marriedStatus) {
        this.marriedStatus = marriedStatus;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public boolean isAllowPrintCertificate() {
        return allowPrintCertificate;
    }

    public void setAllowPrintCertificate(boolean allowPrintCertificate) {
        this.allowPrintCertificate = allowPrintCertificate;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isAdvanceSearch() {
        return advanceSearch;
    }

    public void setAdvanceSearch(boolean advanceSearch) {
        this.advanceSearch = advanceSearch;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public boolean isCertificateSearch() {
        return certificateSearch;
    }

    public void setCertificateSearch(boolean certificateSearch) {
        this.certificateSearch = certificateSearch;
    }

    public Map<Integer, String> getLocationList() {
        return locationList;
    }

    public void setLocationList(Map<Integer, String> locationList) {
        this.locationList = locationList;
    }

    public Map<String, String> getUserList() {
        return userList;
    }

    public void setUserList(Map<String, String> userList) {
        this.userList = userList;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public BitSet getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(BitSet changedFields) {
        this.changedFields = changedFields;
    }

    public Map<Integer, String> getGnDivisionList() {
        return gnDivisionList;
    }

    public void setGnDivisionList(Map<Integer, String> gnDivisionList) {
        this.gnDivisionList = gnDivisionList;
    }

    public int getGnDivisionId() {
        return gnDivisionId;
    }

    public void setGnDivisionId(int gnDivisionId) {
        this.gnDivisionId = gnDivisionId;
        if (parent == null) {
            parent = new ParentInfo();
        }
        parent.setMotherGNDivision(gnDivisionDAO.getGNDivisionByPK(gnDivisionId));
        logger.debug("setting GNDivision: {}", parent.getMotherGNDivision().getEnGNDivisionName());
    }

    public int getMotherGNDivisionId() {
        return motherGNDivisionId;
    }

    public void setMotherGNDivisionId(int motherGNDivisionId) {
        this.motherGNDivisionId = motherGNDivisionId;
        if (parent == null) {
            parent = new ParentInfo();
        }
        parent.setMotherGNDivision(gnDivisionDAO.getGNDivisionByPK(motherGNDivisionId));
        logger.debug("setting Mother GNDivision: {}", parent.getMotherGNDivision().getEnGNDivisionName());
    }

    public String getUnknownFieldPref() {
        return unknownFieldPref;
    }

    public void setUnknownFieldPref(String unknownFieldPref) {
        this.unknownFieldPref = unknownFieldPref;
    }

    public String getUnknownFieldEn() {
        return unknownFieldEn;
    }

    public void setUnknownFieldEn(String unknownFieldEn) {
        this.unknownFieldEn = unknownFieldEn;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}