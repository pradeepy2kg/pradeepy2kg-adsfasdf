package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.*;
import lk.rgd.crs.api.domain.BDDivision;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Locale;
import java.util.List;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.CourtDAO;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.Permission;
import lk.rgd.AppConstants;

/**
 * @author Duminda Dharmakeerthi
 * @author amith jayasekara
 * @author Indunil Moremada
 */
@SuppressWarnings({"ALL"})
public class AdoptionAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionAction.class);
    private static final String ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE = "crs.br_approval_rows_per_page";

    private final AdoptionOrderService service;
    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final CountryDAO countryDAO;
    private final CourtDAO courtDAO;
    private final AppParametersDAO appParametersDAO;
    private final ZonalOfficesDAO zonalOfficesDAO;
    private final BirthRegistrationService birthRegistrationService;

    private AdoptionOrder adoption;
    private User user;
    private Map session;

    private AdoptionOrder.State state;
    private AdoptionOrder.ApplicantType certificateApplicantType;

    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> allDSDivisionList;
    private Map<Integer, String> courtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> zonalOfficeList;
    private List<AdoptionOrder> adoptionApprovalAndPrintList;

    private int birthDistrictId;
    private int birthDivisionId;
    private int courtId;
    private int dsDivisionId;
    private int noOfRows;
    private int currentStatus;
    private int pageNo;
    private int zonalOfficeId;

    private long idUKey;
    private long adoptionId;

    private boolean allowEditAdoption;
    private boolean allowApproveAdoption;
    private boolean nextFlag;
    private boolean previousFlag;
    private boolean alreadyPrinted;
    private boolean approved;
    private boolean printed;

    private String courtOrderNo;
    private String dsDivisionName;
    private String birthDivisionName;
    private String applicantCountryName;
    private String courtName;
    private String wifeCountryName;
    private String birthDistrictName;
    private String certificateApplicantAddress;
    private String certificateApplicantPINorNIC;
    private String certificateApplicantName;
    private String placeOfIssue;
    private String genderEn;
    private String genderSi;
    private String language;

    public AdoptionAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO,
        AdoptionOrderService service, CountryDAO countryDAO, AppParametersDAO appParametersDAO,
        BirthRegistrationService birthRegistrationService, CourtDAO courtDAO, ZonalOfficesDAO zonalOfficesDAO) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.countryDAO = countryDAO;
        this.appParametersDAO = appParametersDAO;
        this.birthRegistrationService = birthRegistrationService;
        this.courtDAO = courtDAO;
        this.zonalOfficesDAO = zonalOfficesDAO;
    }

    public String initAdoptionRegistrationOrCancelPrintAdoptionNotice() {
        return SUCCESS;
    }

    public String addOrEditAdoption() {
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        adoption.setStatus(AdoptionOrder.State.DATA_ENTRY);
        //  populateBasicLists(language);         bug 2202
        //    populateAllDSDivisionList();
        long birthCertificateNo = 0;
        //check applicant type
        if (adoption.isApplicantMother()) {
            //check wife details are already filled if so give action error
            if (adoption.getWifeName() != null || adoption.getWifePassport() != null
                || adoption.getWifePINorNIC() != null) {
                addActionError(getText("er.if.applicant.type.mother.wife.detail.null"));
                basicLists();
                populate();
                populateAllDSDivisionList();
                return "invalidBirthCertificateNumber";
            }
        }

        if (idUKey > 0) {
            AdoptionOrder existingOrder = service.getById(idUKey, user);
            adoption.setIdUKey(idUKey);
            birthCertificateNo = adoption.getBirthCertificateNumber();
            if (birthCertificateNo > 0) {
                logger.info("checking the given birth Certificate for birth certificate number : {}", birthCertificateNo);
                BirthDeclaration bdf = birthRegistrationService.getByIdForAdoptionLookup(birthCertificateNo, user);
                if (bdf == null) {
                    addActionError(getText("er.invalid.birth.certificate.number"));
                    populate();
                    populateAllDSDivisionList();
                    return "invalidBirthCertificateNumber";
                }
            }
            populateAdoptionObjectForEdit(adoption, existingOrder);
            service.updateAdoptionOrder(adoption, user);
            addActionMessage(getText("message.successfully.edited.adoption.order",
                new String[]{adoption.getCourtOrderNumber()}));
        } else {
            birthCertificateNo = adoption.getBirthCertificateNumber();
            if (birthCertificateNo > 0) {
                logger.info("checking the given birth Certificate for birth certificate number : {}", birthCertificateNo);
                BirthDeclaration bdf = birthRegistrationService.getByIdForAdoptionLookup(birthCertificateNo, user);
                if (bdf == null) {
                    addActionError(getText("er.invalid.birth.certificate.number"));
                    basicLists();
                    return "invalidBirthCertificateNumber";
                }
            }
            try {
                service.addAdoptionOrder(adoption, user);
            } catch (CRSRuntimeException e) {
                basicLists();
                addFieldError("duplicateCourtOrderNumberError", getText("er.court.order.number.not.unique"));
                logger.error("error with adding adoption order :: court order number :{}", adoption.getCourtOrderNumber());
                return "invalidBirthCertificateNumber";
            }
            logger.debug("added an adoption successfully with idUKey : {}", adoption.getIdUKey());
            setIdUKey(adoption.getIdUKey());
            setAllowApproveAdoption(user.isAuthorized(Permission.APPROVE_ADOPTION));
            addActionMessage(getText("message.add.successfully.adoption", new String[]{adoption.getCourtOrderNumber()}));
        }

        return SUCCESS;
    }

    private void populateAdoptionObjectForEdit(AdoptionOrder adoption, AdoptionOrder existingOrder) {
//populating by default disabled fields other wise data already entered to them getting lost due to method use
//  to edit adoption
        adoption.setLifeCycleInfo(existingOrder.getLifeCycleInfo());
        adoption.setBirthCertificateNumber((adoption.getBirthCertificateNumber() == 0) ?
            adoption.getBirthCertificateNumber() : existingOrder.getBirthCertificateNumber());
        adoption.setBirthRegistrationSerial((adoption.getBirthRegistrationSerial() == 0) ?
            adoption.getBirthRegistrationSerial() : existingOrder.getBirthRegistrationSerial());
        if (birthDivisionId == 0) {
            adoption.setBirthDivisionId(existingOrder.getBirthDivisionId());
        }
//todo amith bug 2202 populate others
    }


    public String adoptionDeclaration() {
        logger.debug("initializing adoption registration");
        if (idUKey == 0) {
            adoption = new AdoptionOrder();
        }

        populateBasicLists(language);
        populateAllDSDivisionList();
        return SUCCESS;
    }

    public String initApplicantInfo() {
        logger.debug("initializing applicant info");
        return SUCCESS;
    }

    private void basicLists() {
        populate();
        populateBasicLists(language);
        populateAllDSDivisionList();
    }

    /**
     * responsible for loading the AdoptionOrder based
     * on requested idUKey. Error will be thrown if it
     * is not in the DATA_ENTRY mode
     *
     * @return
     */
    public String adoptionDeclarationEditMode() {
        logger.debug("requested to edit AdoptionOrder with idUKey : {}", idUKey);
        adoption = service.getById(idUKey, user);
        if (adoption.getStatus() != AdoptionOrder.State.DATA_ENTRY) {
            //not in data entry mode
            addActionError(getText("adoption.error.editNotAllowed"));
            return ERROR;
        }
        populate();
        populateAllDSDivisionList();
        if (adoption.getBirthDivisionId() != 0) {
            BDDivision bdDivision = bdDivisionDAO.getBDDivisionByPK(adoption.getBirthDivisionId());
            birthDistrictId = bdDivision.getDistrict().getDistrictUKey();
            birthDivisionId = bdDivision.getBdDivisionUKey();
            dsDivisionId = bdDivision.getDsDivision().getDsDivisionUKey();
            //get DSDivision list for the district
            allDSDivisionList = dsDivisionDAO.getAllDSDivisionNames(birthDistrictId, language, user);
            //get bd divisions for edit list
            bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);

        }
        //populate court
        courtId = adoption.getCourt().getCourtUKey();
        return SUCCESS;
    }


    private boolean isApplicantMother(AdoptionOrder adoption) {

        return false;
    }

    /**
     * responsible for loading the AdoptionOrder based
     * on requested idUKey which is then redirected to
     * non editable page
     *
     * @return
     */
    public String adoptionDeclarationViewMode() {
        logger.debug("initializing view mode for idUKey : {}", idUKey);
        adoption = service.getById(idUKey, user);
        if (adoption == null) {
            addActionError(getText("er.invalid.Entry"));
            populateApprovalAndPrintList();
            return "skip";
        }
        // todo remove
        // String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        if (adoption.getBirthDivisionId() > 0) {
            birthDivisionName = bdDivisionDAO.getNameByPK(adoption.getBirthDivisionId(), language);
            dsDivisionName = dsDivisionDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(
                adoption.getBirthDivisionId()).getDsDivision().getDsDivisionUKey(), language);
            birthDistrictName = districtDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(
                adoption.getBirthDivisionId()).getDistrict().getDistrictUKey(), language);
        }
        if (adoption.getApplicantCountryId() > 0) {
            applicantCountryName = countryDAO.getNameByPK(adoption.getApplicantCountryId(), language);
        }
        if (adoption.getWifeCountryId() > 0) {
            wifeCountryName = countryDAO.getNameByPK(adoption.getWifeCountryId(), language);
        }
        if (adoption.getCourt().getCourtId() > 0) {
            courtName = courtDAO.getNameByPK(adoption.getCourt().getCourtId(), language);
        }

        return SUCCESS;
    }

    /**
     *  view adoption order details
     * @return
     */
    public String eprAdoptionOrderDetailsViewMode(){
        logger.debug("initializing view mode for idUKey : {}", idUKey);
        adoption = service.getById(idUKey, user);
        if (adoption == null) {
            addActionError(getText("er.invalid.Entry"));
            populateApprovalAndPrintList();
            return "skip";
        }
        // todo remove
        // String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        if (adoption.getBirthDivisionId() > 0) {
            birthDivisionName = bdDivisionDAO.getNameByPK(adoption.getBirthDivisionId(), language);
            dsDivisionName = dsDivisionDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(
                adoption.getBirthDivisionId()).getDsDivision().getDsDivisionUKey(), language);
            birthDistrictName = districtDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(
                adoption.getBirthDivisionId()).getDistrict().getDistrictUKey(), language);
        }
        if (adoption.getApplicantCountryId() > 0) {
            applicantCountryName = countryDAO.getNameByPK(adoption.getApplicantCountryId(), language);
        }
        if (adoption.getWifeCountryId() > 0) {
            wifeCountryName = countryDAO.getNameByPK(adoption.getWifeCountryId(), language);
        }
        if (adoption.getCourt().getCourtId() > 0) {
            courtName = courtDAO.getNameByPK(adoption.getCourt().getCourtId(), language);
        }
        return SUCCESS;
    }

    /**
     * loading the AdoptionRegistration form
     *
     * @return
     */
    public String loadAdoptionNotice() {
        adoption = service.getById(idUKey, user);
        if (adoption == null) {
            addActionError(getText("er.invalid.Entry"));
            populateApprovalAndPrintList();
            return "skip";
        }
        if (!(adoption.getStatus() == AdoptionOrder.State.APPROVED || adoption.getStatus() == AdoptionOrder.State.NOTICE_LETTER_PRINTED)) {
            addActionError(getText("adoption.not.permited.operation"));
            logger.debug("Current state of adoption order : {}", adoption.getStatus());
            return ERROR;
        } else {
            logger.debug("Current state of adoption order : {}", adoption.getStatus());
            genderEn = GenderUtil.getGender(adoption.getChildGender(), AppConstants.ENGLISH);
            genderSi = GenderUtil.getGender(adoption.getChildGender(), AppConstants.SINHALA);
            courtName = courtDAO.getNameByPK(adoption.getCourt().getCourtUKey(),
                adoption.getLanguageToTransliterate());
            zonalOfficeList = zonalOfficesDAO.getActiveZonalOffices(language);
            return SUCCESS;
        }
    }

    /**
     * marks requested AdoptionOrder as its Adoption notice
     * as printed then loads the adoption list page where it
     * started to print the adoption Notice
     *
     * @return
     */
    public String markAdoptionNoticeAsPrinted() {
        logger.debug("requested to mark Adoption Notice as printed for idUKey : {} ", idUKey);
        adoption = service.getById(idUKey, user);
        if (adoption != null && adoption.getStatus() == AdoptionOrder.State.APPROVED) {
            service.setStatusToPrintedNotice(idUKey, user);
        }
        populate();
        initPermissionForApprovalAndPrint();
        noOfRows = appParametersDAO.getIntParameter(ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (currentStatus != 0) {
            adoptionApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, state, user);
        } else {
            adoptionApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        }
        return SUCCESS;
    }

    /**
     * this method is responsible for mariking adoption notice
     * as printed. then directed to the adoption summary page
     *
     * @return
     */
    public String markDirectlyAdoptionNoticeAsPrinted() {
        logger.debug("initiating Adoption marking as its notice as printed for the idUKey : {}", idUKey);
        service.setStatusToPrintedNotice(idUKey, user);
        setPrinted(true);
        return SUCCESS;
    }

    /**
     * responsible for loading the Adoption Certificate
     *
     * @return
     */
    public String loadAdoptionCertificate() {
        adoption = service.getWithRelationshipsById(idUKey, user);
        if (adoption == null) {
            addActionError(getText("er.invalid.Entry"));
            populateApprovalAndPrintList();
            return "skip";
        }
        if ((adoption.getStatus() != AdoptionOrder.State.CERTIFICATE_ISSUE_REQUEST_CAPTURED) &&
            (adoption.getStatus() != AdoptionOrder.State.ADOPTION_CERTIFICATE_PRINTED) &&
            adoption.getStatus() != AdoptionOrder.State.RE_REGISTRATION_REQUESTED &&
            adoption.getStatus() != AdoptionOrder.State.RE_REGISTERED) {
            addActionError(getText("adoption.not.permited.operation"));
            logger.debug("Current state of adoption certificate : {}", adoption.getStatus());
            return ERROR;
        } else {
            logger.debug("Current state of adoption certificate : {}", adoption.getStatus());
            String certificatePrifLang = adoption.getLanguageToTransliterate();
            courtName = courtDAO.getNameByPK(adoption.getCourt().getCourtUKey(), certificatePrifLang);
            birthDivisionId = adoption.getBirthDivisionId();
            genderEn = GenderUtil.getGender(adoption.getChildGender(), AppConstants.ENGLISH);
            genderSi = GenderUtil.getGender(adoption.getChildGender(), AppConstants.SINHALA);
            //place of issue in prefered language
            User issuedUser = adoption.getLifeCycleInfo().getApprovalOrRejectUser();

            //certifacate preferd language
            String lang = adoption.getLanguageToTransliterate();
            placeOfIssue = dsDivisionDAO.getNameByPK(issuedUser.getPrefBDDSDivision().getDsDivisionUKey(), lang);


            BirthDeclaration bdf = null;
            if (adoption.getBirthCertificateNumber() > 0) {
                bdf = birthRegistrationService.getByIdForAdoptionLookup(adoption.getBirthCertificateNumber(), user);
            }

            if (bdf != null) {
                birthDistrictId = bdf.getRegister().getBirthDistrict().getDistrictUKey();
                birthDivisionId = bdf.getRegister().getBirthDivision().getBdDivisionUKey();
                dsDivisionId = bdf.getRegister().getDsDivision().getDsDivisionUKey();
                birthDistrictName = districtDAO.getNameByPK(birthDistrictId, language);
                birthDivisionName = bdDivisionDAO.getNameByPK(birthDivisionId, language);
                dsDivisionName = dsDivisionDAO.getNameByPK(dsDivisionId, language);
            } else if (birthDivisionId > 0) {
                birthDivisionName = bdDivisionDAO.getNameByPK(birthDivisionId, language);
                birthDistrictName = districtDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(birthDivisionId).getDistrict().getDistrictUKey(), language);
                dsDivisionName = dsDivisionDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(birthDivisionId).getDsDivision().getDsDivisionUKey(), language);
            }
            return SUCCESS;
        }
    }

    /**
     * marks adoption certificate as printed only if
     * it is already not printed. then loads the
     * adoption list page where it started to print
     * the adoption certificate
     *
     * @return
     */
    public String markAdoptionCertificateAsPrinted() {
        logger.debug("requested to mark adoption certificate as printed with idUKey : {} alreadyPrinted : {} ", idUKey, alreadyPrinted);
        try {
            service.setStatusToPrintedCertificate(idUKey, user);
        }
        catch (CRSRuntimeException e) {
            logger.debug("invalide state for mark as print adoption order idUKey : {}", idUKey);
        }
        populate();
        initPermissionForApprovalAndPrint();
        noOfRows = appParametersDAO.getIntParameter(ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (currentStatus != 0) {
            adoptionApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, state, user);
        } else {
            adoptionApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        }
        logger.debug("marked as print successfully adoption order idUKey : {}", idUKey);
        return SUCCESS;
    }

    /**
     * get the adoption which are to be approved and printed
     *
     * @return
     */
    public String adoptionApprovalAndPrint() {
        setPageNo(1);
        birthDistrictId = user.getPrefBDDistrict().getDistrictUKey();
        populate();
        initPermissionForApprovalAndPrint();
        noOfRows = appParametersDAO.getIntParameter(ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (currentStatus != 0) {
            adoptionApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, state, user);
        } else {
            adoptionApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        }
        paginationHandler(adoptionApprovalAndPrintList.size());
        previousFlag = false;
        return SUCCESS;
    }

    /**
     * method is responsible for loading the previous records
     * after viewing and printing a particular adoption
     *
     * @return
     */
    public String adoptionBackToPreviouseState() {
        logger.debug("loading previous page : currentStatus {} , pageNo  {}", currentStatus, pageNo);
        populateApprovalAndPrintList();
        return SUCCESS;
    }

    private void populateApprovalAndPrintList() {
        populate();
        initPermissionForApprovalAndPrint();
        noOfRows = appParametersDAO.getIntParameter(ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (state != null) {
            adoptionApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, state, user);
        } else {
            adoptionApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        }
    }

    /**
     * filtering based on the user selected state
     *
     * @return
     */
    public String filterByStatus() {
        setPageNo(1);
        logger.debug("requested to filter by : {}", currentStatus);
        populate();
        initPermissionForApprovalAndPrint();
        noOfRows = appParametersDAO.getIntParameter(ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (currentStatus == 0) {
            //no state is selected
            adoptionApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        } else {
            adoptionApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, state, user);
        }
        paginationHandler(adoptionApprovalAndPrintList.size());
        return SUCCESS;
    }

    /**
     * responsible whether to display the next link in
     * the jsp or not and handles the page number
     *
     * @param recordsFound no of AdoptionOrders found
     */
    public void paginationHandler(int recordsFound) {
        if (recordsFound == appParametersDAO.getIntParameter(ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE)) {
            setNextFlag(true);
        } else {
            setNextFlag(false);
        }
    }

    /**
     * handles pagination of AdoptionOrders approval and print data
     *
     * @return String
     */
    public String loadPreviousRecords() {
        logger.debug("requested previous records current pageNo : {} ", pageNo);
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
        populateApprovalAndPrintList();
        return SUCCESS;
    }

    public String loadNextRecords() {
        logger.debug("requested next records current pageNo : {} ", pageNo);
        setPageNo(getPageNo() + 1);
        populateApprovalAndPrintList();
        paginationHandler(adoptionApprovalAndPrintList.size());
        setPreviousFlag(true);
        return SUCCESS;
    }

    /**
     * responsible for approving requested adoption
     * based on the idUKey
     *
     * @return
     */
    public String approveAdoption() {
        logger.debug("requested to approve AdoptionOrder with idUKey : {}", idUKey);
        try {
            service.approveAdoptionOrder(getIdUKey(), user);
            addActionMessage(getText("message.successfully.approved.adoption.order"));
        } catch (CRSRuntimeException e) {
            addActionError(getText("adoption.error.no.permission"));
        }
        populateApprovalAndPrintList();
        return SUCCESS;
    }

    /**
     * responsible for approving an adoption immediately after
     * adding to the system. if the approval is success approved
     * boolean is set to true in order to makesure it is approved
     * for printing its Notice letter.
     *
     * @return
     */
    public String directApproveAdoption() {
        logger.debug("initiating adoption direct approval with idUKey : {}", idUKey);
        try {
            service.approveAdoptionOrder(idUKey, user);
            setApproved(true);
        } catch (CRSRuntimeException e) {
            addActionError(getText("adoption.error.no.permission"));
        }
        return SUCCESS;
    }

    /**
     * reject the AdoptionOrder based on requested
     * idUKey
     *
     * @return
     */
    public String rejectAdoption() {
        logger.debug("requested to reject AdoptionOrder with idUKey : {}", idUKey);
        try {
            service.rejectAdoptionOrder(idUKey, user);
            addActionMessage(getText("message.successfully.reject.adoption"));
        } catch (CRSRuntimeException e) {
            addActionError(getText("adoption.error.no.permission"));
        }
        populateApprovalAndPrintList();
        return SUCCESS;
    }

    /**
     * delete the AdoptionOrder based on requested
     * idUKey
     *
     * @return
     */

    public String deleteAdoption() {
        logger.debug("requested to delete AdoptionOrder with idUKey : {}", idUKey);
        service.deleteAdoptionOrder(idUKey, user);
        addActionMessage(getText("message.successfully.deleted"));
        populateApprovalAndPrintList();
        return SUCCESS;
    }

    /**
     * capture certificate applicant data and chage adoption order state in to CERTIFICATE_ISSUE_REQUEST_CAPTURED
     *
     * @return SUCCESS if successfully captured data
     */
    public String adoptionApplicantInfo() {
        //todo check pageNo value
        if (pageNo == 1) {
            //set type
            adoption = (AdoptionOrder) session.get(WebConstants.SESSION_ADOPTION_ORDER);
            //cannot capture data if it is not approved
            if (adoption.getStatus().equals(AdoptionOrder.State.NOTICE_LETTER_PRINTED)) {
                adoption.setCertificateApplicantAddress(certificateApplicantAddress);
                adoption.setCertificateApplicantName(certificateApplicantName);
                adoption.setCertificateApplicantPINorNIC(certificateApplicantPINorNIC);
                adoption.setCertificateApplicantType(certificateApplicantType);
                service.setApplicantInfo(adoption, user);
            } else {
                addActionError("er.label.cannot_capture_data");
            }
            session.remove(WebConstants.SESSION_ADOPTION_ORDER);
        }
        return SUCCESS;
    }

    public String reRegistrationInit() {
        return SUCCESS;
    }

    public String loadReRegistrationRecord() {
        adoption = service.getById(idUKey, user);
        if (adoption == null) {
            addActionError(getText("er.adoption.re.registration.invalid.entry"));
            return "invalidData";
        }
        if (AdoptionOrder.State.ADOPTION_CERTIFICATE_PRINTED != adoption.getStatus()) {
            addActionError(getText("er.adoption.re.registration.invalid.state"));
            return "invalidData";
        } else {
            return SUCCESS;
        }
    }

    public void initPermissionForApprovalAndPrint() {

        allowApproveAdoption = user.isAuthorized(Permission.APPROVE_ADOPTION);
        allowEditAdoption = user.isAuthorized(Permission.EDIT_ADOPTION);

    }

    private void populate() {
        // todo remove
        // String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        populateBasicLists(language);
        populateDynamicLists(language);


    }

    public String populateAdoption() {
        //adoption = service.getByCourtAndCourtOrderNumber(0 /* TODO FIX ME*/, courtOrderNo, user);
        adoption = service.getById(idUKey, user);
        if (adoption != null) {
            courtName = courtDAO.getNameByPK(adoption.getCourt().getCourtUKey(),
                ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage());
            if (adoption.getStatus().equals(AdoptionOrder.State.NOTICE_LETTER_PRINTED)) {
                session.put(WebConstants.SESSION_ADOPTION_ORDER, adoption);

            } else {
                if (adoption.getStatus().equals(AdoptionOrder.State.APPROVED)) {
                    addActionError(getText("er.label.notice.not.printed.cannot_capture_data"));
                }
                adoption = null;
                //simple fix for bug 2184
                courtName = "";
                addActionError(getText("er.label.invalid.data"));
            }
        } else {
            addActionError(getText("adoption_order_notfound.message"));
        }
        return SUCCESS;
    }

    private void populateBasicLists(String language) {
        countryList = countryDAO.getCountries(language);
        districtList = districtDAO.getAllDistrictNames(language, user);
        courtList = (courtDAO.getCourtNames(language));
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

    private void populateAllDSDivisionList() {
        if (birthDistrictId == 0) {
            if (!districtList.isEmpty()) {
                birthDistrictId = districtList.keySet().iterator().next();
                logger.debug("first allowed district in the list {} was set", birthDistrictId);
            }
        }

        allDSDivisionList = dsDivisionDAO.getAllDSDivisionNames(birthDistrictId, language, user);

        if (dsDivisionId == 0) {
            if (!allDSDivisionList.isEmpty()) {
                dsDivisionId = allDSDivisionList.keySet().iterator().next();
                logger.debug("first allowed DS Div in the list {} was set", dsDivisionId);
            }
        }
        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        if (birthDivisionId == 0) {
            birthDivisionId = bdDivisionList.keySet().iterator().next();
            logger.debug("first allowed BD Div in the list {} was set", birthDivisionId);
        }
    }

    public void setBirthDistrictId(int birthDistrictId) {
        this.birthDistrictId = birthDistrictId;
    }

    public void setBirthDivisionId(int birthDivisionId) {
        this.birthDivisionId = birthDivisionId;
        if (adoption != null && birthDivisionId > 0) {
            logger.debug("setting the birth Division for the birthDivisionId : {}", birthDivisionId);
            adoption.setBirthDivisionId(bdDivisionDAO.getBDDivisionByPK(birthDivisionId).getBdDivisionUKey());
        }
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public void setDsDivisionList(Map<Integer, String> dsDivisionList) {
        this.dsDivisionList = dsDivisionList;
    }

    public void setBdDivisionList(Map<Integer, String> bdDivisionList) {
        this.bdDivisionList = bdDivisionList;
    }

    public int getBirthDistrictId() {
        return birthDistrictId;
    }

    public int getBirthDivisionId() {
        return birthDivisionId;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public Map<Integer, String> getBdDivisionList() {
        return bdDivisionList;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setSession(Map map) {
        logger.debug("Set session {}", map);
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("setting User: {}", user.getUserName());
    }

    public Map getSession() {
        return session;
    }

    public AdoptionOrder getAdoption() {
        return adoption;
    }

    public void setAdoption(AdoptionOrder adoption) {
        this.adoption = adoption;
    }

    public String getCourtOrderNo() {
        return courtOrderNo;
    }

    public void setCourtOrderNo(String courtOrderNo) {
        this.courtOrderNo = courtOrderNo;
    }


    public List<AdoptionOrder> getAdoptionApprovalAndPrintList() {
        return adoptionApprovalAndPrintList;
    }

    public void setAdoptionApprovalAndPrintList(List<AdoptionOrder> adoptionApprovalAndPrintList) {
        this.adoptionApprovalAndPrintList = adoptionApprovalAndPrintList;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<Integer, String> getCountryList() {
        return countryList;
    }

    public void setCountryList(Map<Integer, String> countryList) {
        this.countryList = countryList;
    }

    public int getPageNumber() {
        return pageNo;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNo = pageNumber;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public boolean isAllowEditAdoption() {
        return allowEditAdoption;
    }

    public void setAllowEditAdoption(boolean allowEditAdoption) {
        this.allowEditAdoption = allowEditAdoption;
    }

    public boolean isAllowApproveAdoption() {
        return allowApproveAdoption;
    }

    public void setAllowApproveAdoption(boolean allowApproveAdoption) {
        this.allowApproveAdoption = allowApproveAdoption;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public String getDsDivisionName() {
        return dsDivisionName;
    }

    public void setDsDivisionName(String dsDivisionName) {
        this.dsDivisionName = dsDivisionName;
    }

    public String getBirthDivisionName() {
        return birthDivisionName;
    }

    public void setBirthDivisionName(String birthDivisionName) {
        this.birthDivisionName = birthDivisionName;
    }

    public String getApplicantCountryName() {
        return applicantCountryName;
    }

    public void setApplicantCountryName(String applicantCountryName) {
        this.applicantCountryName = applicantCountryName;
    }

    public String getWifeCountryName() {
        return wifeCountryName;
    }

    public void setWifeCountryName(String wifeCountryName) {
        this.wifeCountryName = wifeCountryName;
    }

    public String getBirthDistrictName() {
        return birthDistrictName;
    }

    public void setBirthDistrictName(String birthDistrictName) {
        this.birthDistrictName = birthDistrictName;
    }

    public String getCertificateApplicantAddress() {
        return certificateApplicantAddress;
    }

    public void setCertificateApplicantAddress(String certificateApplicantAddress) {
        this.certificateApplicantAddress = certificateApplicantAddress;
    }

    public String getCertificateApplicantPINorNIC() {
        return certificateApplicantPINorNIC;
    }

    public void setCertificateApplicantPINorNIC(String certificateApplicantPINorNIC) {
        this.certificateApplicantPINorNIC = certificateApplicantPINorNIC;
    }

    public String getCertificateApplicantName() {
        return certificateApplicantName;
    }

    public void setCertificateApplicantName(String certificateApplicantName) {
        this.certificateApplicantName = certificateApplicantName;
    }

    public AdoptionOrder.ApplicantType getCertificateApplicantType() {
        return certificateApplicantType;
    }

    public void setCertificateApplicantType(AdoptionOrder.ApplicantType certificateApplicantType) {
        this.certificateApplicantType = certificateApplicantType;
    }

    public boolean isAlreadyPrinted() {
        return alreadyPrinted;
    }

    public void setAlreadyPrinted(boolean alreadyPrinted) {
        this.alreadyPrinted = alreadyPrinted;
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

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
        switch (currentStatus) {
            case 1:
                this.state = AdoptionOrder.State.DATA_ENTRY;
                break;
            case 2:
                this.state = AdoptionOrder.State.APPROVED;
                break;
            case 3:
                this.state = AdoptionOrder.State.NOTICE_LETTER_PRINTED;
                break;
            case 4:
                this.state = AdoptionOrder.State.REJECTED;
                break;
            case 5:
                this.state = AdoptionOrder.State.CERTIFICATE_ISSUE_REQUEST_CAPTURED;
                break;
            case 6:
                this.state = AdoptionOrder.State.ADOPTION_CERTIFICATE_PRINTED;
                //speacial case 0 all status
        }
    }

    public String getGenderEn() {
        return genderEn;
    }

    public void setGenderEn(String genderEn) {
        this.genderEn = genderEn;
    }

    public String getGenderSi() {
        return genderSi;
    }

    public void setGenderSi(String genderSi) {
        this.genderSi = genderSi;
    }

    public AdoptionOrder.State getState() {
        return state;
    }

    public void setState(AdoptionOrder.State state) {
        this.state = state;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isPrinted() {
        return printed;
    }

    public void setPrinted(boolean printed) {
        this.printed = printed;
    }

    public long getAdoptionId() {
        return adoptionId;
    }

    public void setAdoptionId(long adoptionId) {
        this.adoptionId = adoptionId;
    }

    public Map<Integer, String> getCourtList() {
        return courtList;
    }

    public void setCourtList(Map<Integer, String> courtList) {
        this.courtList = courtList;
    }

    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
        this.birthDivisionId = birthDivisionId;
        if (adoption == null) {
            adoption = new AdoptionOrder();
        }
        adoption.setCourt(courtDAO.getCourt(courtId));
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public Map<Integer, String> getAllDSDivisionList() {
        return allDSDivisionList;
    }

    public void setAllDSDivisionList(Map<Integer, String> allDSDivisionList) {
        this.allDSDivisionList = allDSDivisionList;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public Map<Integer, String> getZonalOfficeList() {
        return zonalOfficeList;
    }

    public void setZonalOfficeList(Map<Integer, String> zonalOfficeList) {
        this.zonalOfficeList = zonalOfficeList;
    }

    public int getZonalOfficeId() {
        return zonalOfficeId;
    }

    public void setZonalOfficeId(int zonalOfficeId) {
        this.zonalOfficeId = zonalOfficeId;
    }
}
