package lk.rgd.crs.web.action.marriages;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.CivilStatusUtil;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.CommonUtil;
import lk.rgd.crs.web.util.MarriageType;
import lk.rgd.crs.web.util.TypeOfMarriagePlace;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.AppConstants;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.io.File;

/**
 * @author amith
 *         action class for marriage registration
 *         //TODO remove bothSubmitted variable (actual meaning is there is only one or two notices)
 */
public class MarriageRegistrationAction extends ActionSupport implements SessionAware {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MarriageRegistrationAction.class);

    private final MarriageRegistrationService marriageRegistrationService;

    private final MRDivisionDAO mrDivisionDAO;
    private final RaceDAO raceDAO;
    private final CountryDAO countryDAO;
    private final CommonUtil commonUtil;

    private User user;
    private MarriageRegister marriage;

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> mrDivisionList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> raceList;
    private Map<Person.CivilStatus, String> civilStatusMale;
    private Map<Person.CivilStatus, String> civilStatusFemale;

    private List<UserWarning> userWarnings;

    private int marriageDistrictId;           //use for both male /female and both cases
    private int dsDivisionId;
    private int mrDivisionId;             //use for both male /female and both cases
    private int raceIdMale;
    private int raceIdFemale;
    private int countryIdMale;
    private int countryIdFemale;

    private long idUKey;

    private boolean male;
    private boolean secondNotice;
    private boolean editMode;
    private boolean ignoreWarnings;
    private String mode;

    private String language;

    private Long serialNumber;

    private Date noticeReceivedDate;

    private MarriageType[] marriageTypeList;
    private TypeOfMarriagePlace[] typeOfMarriagePlaceList;
    private Map<String, String> languageList;

    private MarriageNotice.Type noticeType;

    private File scannedImage;
    private String scannedImageContentType; // The content type of the file
    private String scannedImageFileName; // The uploaded file name and path

    public MarriageRegistrationAction(MarriageRegistrationService marriageRegistrationService,
        MRDivisionDAO mrDivisionDAO, RaceDAO raceDAO, CountryDAO countryDAO,
        CommonUtil commonUtil) {
        this.marriageRegistrationService = marriageRegistrationService;
        this.mrDivisionDAO = mrDivisionDAO;
        this.raceDAO = raceDAO;
        this.countryDAO = countryDAO;
        this.commonUtil = commonUtil;

        districtList = new HashMap<Integer, String>();
        dsDivisionList = new HashMap<Integer, String>();
        mrDivisionList = new HashMap<Integer, String>();
        raceList = new HashMap<Integer, String>();
        countryList = new HashMap<Integer, String>();
    }

    public String selectMarriageNoticeType() {
        //nothing to do use to load the notice type selection JSP
        return SUCCESS;
    }

    /**
     * loading marriage notice page
     */
    public String marriageNoticeInit() {
        //loading notice page to adding a new notice
        logger.debug("attempt to load marriage notice page");
        if (idUKey > 0) {
            //loading existing marriage notice from the list page
            logger.debug("load existing marriage notice : idUKey {}", idUKey);
            marriage = marriageRegistrationService.getByIdUKey(idUKey, user);
        }
        //populating lists
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
            marriageDistrictId, dsDivisionId, mrDivisionId, AppConstants.MARRIAGE, user, language);
        commonUtil.populateCountryAndRaceLists(countryList, raceList, language);
        logger.debug("successfully loaded the page marriage notice");
        return "pageLoad";
    }

    /**
     * adding a new marriage notice     if serial number is not unique redirect to notice add page
     */
    public String addMarriageNotice() {
        logger.debug("attempt to add marriage notice serial number : {} ");
        try {
            populateNoticeForPersists();
            //add race,country to  male party and female party
            populatePartyObjectsForPersisting(marriage);
            marriageRegistrationService.addMarriageNotice(marriage, noticeType, user);
            addActionMessage(getText("message.notice.successfully.add"));
            logger.debug("successfully added marriage notice serial number: {}");
            idUKey = marriage.getIdUKey();
            return SUCCESS;
        }
        catch (CRSRuntimeException e) {
            switch (e.getErrorCode()) {
                case ErrorCodes.INVALID_SERIAL_NUMBER:
                    addFieldError("duplicateSerialNumberError", getText("message.invalid.serialNumber.found"));
                    break;
                case ErrorCodes.POSSIBLE_MARRIAGE_NOTICE_SERIAL_NUMBER_DUPLICATION:
                    addFieldError("duplicateSerialNumberError", getText("message.duplicate.serialNumber.found"));
            }
            logger.debug("invalid marriage notice : male serial number : {} : female serial number : {}",
                marriage.getSerialOfMaleNotice(), marriage.getSerialOfFemaleNotice());
            //populating lists
            commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
                marriageDistrictId, dsDivisionId, mrDivisionId, "Marriage", user, language);
            commonUtil.populateCountryAndRaceLists(countryList, raceList, language);
            return "pageLoad";
        }
    }

    public String editMarriageNoticeInit() {
        //todo check state for editing record
        logger.debug("attempt to edit marriage notice :idUKey {}", idUKey);
        marriage = marriageRegistrationService.getByIdUKey(idUKey, user);
        if (marriage == null) {
            logger.debug("cannot find marriage register record to edit : idUKey {}", idUKey);
            addActionError(getText("error.cannot.find.record.for.edit"));
            commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
                marriageDistrictId, dsDivisionId, mrDivisionId, AppConstants.MARRIAGE, user, language);
            return ERROR;
        }
        populateNoticeForInitEdit(marriage, noticeType);
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
            marriageDistrictId, dsDivisionId, mrDivisionId, AppConstants.MARRIAGE, user, language);
        commonUtil.populateCountryAndRaceLists(countryList, raceList, language);
        editMode = true;
        return "pageLoad";
    }


    /**
     * editing(updating) a marriage notice (register)
     */
    public String editMarriageNotice() {
        //todo if notice is approved cannot edit in notice stage
        logger.debug("attempt to edit marriage notice : idUKey {}", idUKey);
        MarriageRegister existingNotice = marriageRegistrationService.getByIdUKey(idUKey, user);
        if (existingNotice != null) {
            populatePartyObjectsForPersisting(marriage);
            populateNoticeForEdit(marriage, existingNotice, noticeType);
            //todo use edit method instead of this update
/*
            marriageRegistrationService.updateMarriageRegister(existingNotice, user);
*/
            userWarnings = marriageRegistrationService.editMarriageNotice(existingNotice, noticeType, ignoreWarnings, user);
            if (userWarnings.size() > 0) {
                logger.debug("warnings found while editing notice");
                editMode = true;
                addNoticeToSession(existingNotice);
                return "warn";
            }
            addActionMessage(getText("marriage.notice.updated.success", new String[]{(noticeType ==
                MarriageNotice.Type.FEMALE_NOTICE) ? Long.toString(existingNotice.getSerialOfFemaleNotice()) :
                Long.toString(existingNotice.getSerialOfMaleNotice())}));
        } else {
            logger.debug("marriage notice : idUKey {} : update fails");
            addActionError(getText("marriage.notice.update.fails"));
            return ERROR;
        }
        logger.debug("marriage notice : idUKey {} : edited successfully", marriage.getIdUKey());
        return SUCCESS;
    }

    /**
     * special case submit notices to two locations and second notice about to be add(actually updating same record)
     * if isBoth submitted is true there cannot be a second notice
     * else if existing notice is male second is female vise versa
     * when warn found forward to warning page
     */
    public String addSecondNotice() {
        logger.debug("attempt to add second notice : idUKey of the record : {}", idUKey);
        MarriageRegister existingNotice = marriageRegistrationService.getByIdUKey(idUKey, user);
        if (existingNotice != null) {
            populatePartyObjectsForPersisting(existingNotice);
            //check who submit the second notice
            if (!ignoreWarnings) {
                if (!existingNotice.isSingleNotice() && existingNotice.getSerialOfMaleNotice() == null) {
                    noticeType = MarriageNotice.Type.MALE_NOTICE;
                } else {
                    noticeType = MarriageNotice.Type.FEMALE_NOTICE;
                }
            } else {
                //getting previous notice related data from the session and remove from the session
                getAndRemoveNoticeFromSession();
                //check idUKey and sessions idUKey is same
                if (marriage.getIdUKey() != idUKey) {
                    //todo throw an error                check serial is not setting in OP
                    //that mean session value ans idUKey is not sync
                }
                ///setting to license collection party tp prev
                marriage.setLicenseCollectType(existingNotice.getLicenseCollectType());
            }
            populateNoticeForAddingSecondNotice(existingNotice, marriage);
            try {
                userWarnings = marriageRegistrationService.addSecondMarriageNotice(existingNotice, noticeType, ignoreWarnings,
                    false, user);
            }
            catch (CRSRuntimeException e) {
                switch (e.getErrorCode()) {
                    case ErrorCodes.POSSIBLE_MARRIAGE_NOTICE_SERIAL_NUMBER_DUPLICATION: {
                        addFieldError("duplicateSerialNumber", getText("error.duplicate.serial.number"));
                        //populating lists
                        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
                            marriageDistrictId, dsDivisionId, mrDivisionId, "Marriage", user, language);
                        commonUtil.populateCountryAndRaceLists(countryList, raceList, language);
                    }
                    return "pageLoad";
                    default:
                        addActionError(getText("error.unable.to.add.marriage.notice",
                            new String[]{(noticeType == MarriageNotice.Type.FEMALE_NOTICE) ?
                                Long.toString(existingNotice.getSerialOfMaleNotice()) :
                                Long.toString(existingNotice.getSerialOfFemaleNotice())}));
                }
                logger.debug("unable to add second notice : idUKey {}", idUKey);

            }
            if (userWarnings.size() > 0) {
                //no need to null check we returning empty set if no warnings
                logger.debug("user warnings found for adding second notice for existing notice idUKey : {}", idUKey);
                addNoticeToSession(existingNotice);
                return "warn";
            }
        } else {
            logger.debug("cannot add second notice to idUKey : {} first does not exist", idUKey);
            addActionError(getText("cannot.add.second,notice.first.does.not.exist"));
            return ERROR;
        }
        logger.debug("added second notice to idUKey : {}", idUKey);
        addActionMessage(getText("message.add.successfully.notice", new String[]{Long.toString(
            (noticeType == MarriageNotice.Type.FEMALE_NOTICE) ?
                existingNotice.getSerialOfFemaleNotice() : existingNotice.getSerialOfMaleNotice())}));
        return SUCCESS;
    }

    /**
     * this is the scenario that can be happen in many rare cases and in can only encounter when there are two notice
     * submitted by two parties
     * <p/>
     * assume Male party is submitting the notice first and he declare female party as the license owner
     * and before female party submit the notice Male notice is being approved by the ADR
     * this scenario happens now
     * assume now female is submitting the notice and she declare male party as the license owner but the approval process
     * says  LP (license party) can only be approved iff OP get approved
     * but now Male party is being approved first so This party cannot hold the license
     * in that case DEO is asked to choose two options
     * 1>ask female party to keep to be the license owner as the previous party declare
     * or
     * 2>if female party does not want to be the license party and if she said male must get the license ,in that case we
     * roll back the approval of the female party and allow female to declare male as the license owner in that case
     * male notice has to approve again by the ADR after female party get approved
     * and vise versa
     * <p/>
     * note in funny situations male party (im referring to the above story) may refuse and he may declare female as the
     * owner again this process can be repeating over and over again and we cannot avoid that pragmatically so it should resolve
     * manually
     */
    public String rollBackApprovedToNonApproved() {
        logger.debug("attempt to roll back the approval of the marriage notice idUKey : {} :and the notice type : {}",
            idUKey, noticeType);
        getAndRemoveNoticeFromSession();
        MarriageRegister existingNotice = marriageRegistrationService.getByIdUKey(idUKey, user);
        populateNoticeForAddingSecondNotice(existingNotice, marriage);
        marriageRegistrationService.addSecondMarriageNotice(existingNotice, noticeType, ignoreWarnings, true, user);
        addActionMessage(getText("message.successfully.roll.back.to.prev.and.add.second.notice"));
        logger.debug("roll back success for marriage notice idUKey : {}", idUKey);
        return SUCCESS;
    }

    public String rollBackAndEditOrProceed() {
        logger.debug("attempt to roll back to previouse state and edit or proceed to edit");
        getAndRemoveNoticeFromSession();
        if (editMode) {
            //this means user select to proceed  and dropping values
            MarriageRegister originalNotice = marriageRegistrationService.getByIdUKey(marriage.getIdUKey(), user);
            marriage.setLicenseCollectType(originalNotice.getLicenseCollectType());
        } else {
            //user select to roll back
            marriage.setState(MarriageRegister.State.DATA_ENTRY);
        }
        marriageRegistrationService.editMarriageNotice(marriage, noticeType, ignoreWarnings, user);
        addActionMessage("message.edit.success");
        return SUCCESS;
    }

    //TODO : to be removed

    public String marriageCertificateInit() {
        logger.debug("loading marriage certificate : idUKey : {}", idUKey);
        //TODO all loading stuffs
        return "pageLoad";
    }

    /**
     * this private method populate existing marriage notice record when adding second notice
     * if second notice is male updating marriage notice with new male notice related data and vise-versa
     * note:notice type "BOTH" doesn't have second notice so this method does not handle that scenario
     */
    private void populateNoticeForAddingSecondNotice(MarriageRegister noticeExisting, MarriageRegister noticeEdited) {
        //BOTH type does not have a second notice  so we are not handling it
        //todo license request by when add second notice
        MRDivision mr = mrDivisionDAO.getMRDivisionByPK(mrDivisionId);
        if (noticeType == MarriageNotice.Type.MALE_NOTICE) {
            noticeExisting.setSerialOfMaleNotice(serialNumber);
            noticeExisting.setDateOfMaleNotice(noticeReceivedDate);
            noticeExisting.setMale(noticeEdited.getMale());
            noticeExisting.setMrDivisionOfMaleNotice(mr);
        } else {
            noticeExisting.setSerialOfFemaleNotice(serialNumber);
            noticeExisting.setDateOfFemaleNotice(noticeReceivedDate);
            noticeExisting.setFemale(noticeEdited.getFemale());
            noticeExisting.setMrDivisionOfFemaleNotice(mr);
        }
        noticeExisting.setLicenseCollectType(noticeEdited.getLicenseCollectType());
        noticeExisting.setTypeOfMarriage(noticeEdited.getTypeOfMarriage());
    }

    /**
     * this private method populate notice(marriage register object)for edit mode
     * if editing notice is female notice we only chane female notice related data and vise versa
     * the special case notice type "BOTH" including both male and female party information and holds
     * male notice related data as well
     */
    private void populateNoticeForEdit(MarriageRegister marriageRegister, MarriageRegister existing, MarriageNotice.Type type) {
        MRDivision mrDivision = mrDivisionDAO.getMRDivisionByPK(mrDivisionId);
        switch (type) {
            case BOTH_NOTICE: {
                existing.setFemale(marriageRegister.getFemale());
            }
            case MALE_NOTICE: {
                existing.setMale(marriageRegister.getMale());//BOTH also executing this block
                existing.setSerialOfMaleNotice(serialNumber);
                existing.setDateOfMaleNotice(noticeReceivedDate);
                existing.setMrDivisionOfMaleNotice(mrDivision);
            }
            break;
            case FEMALE_NOTICE: {
                existing.setFemale(marriageRegister.getFemale());
                existing.setSerialOfFemaleNotice(serialNumber);
                existing.setDateOfFemaleNotice(noticeReceivedDate);
                existing.setMrDivisionOfFemaleNotice(mrDivision);
            }
        }
        existing.setTypeOfMarriage(marriageRegister.getTypeOfMarriage());
        existing.setLicenseCollectType(marriageRegister.getLicenseCollectType());
        existing.setPreferredLanguage(marriageRegister.getPreferredLanguage());
    }

    private void populateNoticeForInitEdit(MarriageRegister marriage, MarriageNotice.Type type) {
        if (secondNotice) {
            logger.debug("attempt to add second notice for marriage notice idUKey : {}", idUKey);
            //notice type BOTH cannot have a second notice
            if (noticeType == MarriageNotice.Type.MALE_NOTICE) {
                //that means first notice is male notice so second must ne female notice so we have to init female notice page
                noticeType = MarriageNotice.Type.FEMALE_NOTICE;
            } else {
                noticeType = MarriageNotice.Type.MALE_NOTICE;
            }
        } else {
            logger.debug("init edit page for notice idUKey : {}", idUKey);
            MRDivision mrDivision = null;
            DSDivision dsDivision = null;
            District district = null;
            switch (type) {
                case BOTH_NOTICE:
                    countryIdFemale = (marriage.getFemale().getCountry() != null) ?
                        marriage.getFemale().getCountry().getCountryId() : 0;
                    //race cannot be null
                    raceIdFemale = marriage.getFemale().getFemaleRace().getRaceId();
                    //only both related populations
                case MALE_NOTICE:
                    countryIdMale = (marriage.getMale().getCountry() != null) ?
                        marriage.getMale().getCountry().getCountryId() : 0;
                    raceIdMale = (marriage.getMale().getMaleRace() != null) ?
                        marriage.getMale().getMaleRace().getRaceId() : 0;
                    serialNumber = marriage.getSerialOfMaleNotice();
                    noticeReceivedDate = marriage.getDateOfMaleNotice();
                    mrDivision = marriage.getMrDivisionOfMaleNotice();
                    break;
                case FEMALE_NOTICE:
                    countryIdFemale = (marriage.getFemale().getCountry() != null) ?
                        marriage.getFemale().getCountry().getCountryId() : 0;
                    raceIdFemale = (marriage.getFemale().getFemaleRace() != null) ?
                        marriage.getFemale().getFemaleRace().getRaceId() : 0;
                    serialNumber = marriage.getSerialOfFemaleNotice();
                    noticeReceivedDate = marriage.getDateOfFemaleNotice();
                    mrDivision = marriage.getMrDivisionOfFemaleNotice();
            }
            mrDivisionId = mrDivision.getMrDivisionUKey();
            dsDivision = mrDivision.getDsDivision();
            dsDivisionId = dsDivision.getDsDivisionUKey();
            district = mrDivision.getDistrict();
            marriageDistrictId = district.getDistrictUKey();
        }
    }

    /**
     * populate additional fields for persisting notice
     * setting notice serial numbers and receiving dates
     */
    private void populateNoticeForPersists() {
        //both NOTICE_MALE and BOTH are treated as male notices and populate male notice related fields
        MRDivision mr = mrDivisionDAO.getMRDivisionByPK(mrDivisionId);
        if (noticeType == MarriageNotice.Type.BOTH_NOTICE || noticeType == MarriageNotice.Type.MALE_NOTICE) {
            marriage.setMrDivisionOfMaleNotice(mr);
            marriage.setSerialOfMaleNotice(serialNumber);
            marriage.setDateOfMaleNotice(noticeReceivedDate);
        } else {
            //FEMALE notice
            marriage.setMrDivisionOfFemaleNotice(mr);
            marriage.setSerialOfFemaleNotice(serialNumber);
            marriage.setDateOfFemaleNotice(noticeReceivedDate);
        }
    }

    /**
     * populate male/female race,country(if foreign)
     */
    private void populatePartyObjectsForPersisting(MarriageRegister marriage) {
//todo simplify  amith
        if (raceIdMale != 0) {
            marriage.getMale().setMaleRace(raceDAO.getRace(raceIdMale));
        }
        if (raceIdFemale != 0) {
            marriage.getFemale().setFemaleRace(raceDAO.getRace(raceIdFemale));
        }
        if (countryIdMale != 0) {
            marriage.getMale().setCountry(countryDAO.getCountry(countryIdMale));
        }
        if (countryIdFemale != 0) {
            marriage.getFemale().setCountry(countryDAO.getCountry(countryIdFemale));
        }
    }

    /**
     * Marriage Registration -Marriage Details page load
     */
    public String marriageRegistrationInit() {
        logger.debug("Marriage Details - idUKey : {}", idUKey);
        populateLists();
        if (idUKey != 0) {
            marriage = marriageRegistrationService.getByIdUKey(idUKey, user);
            if ("register".equals(mode)) {
                marriage.setTypeOfMarriagePlace(TypeOfMarriagePlace.REGISTRAR_OFFICE);
            }
        } else {
            marriage = new MarriageRegister();
            marriage.setTypeOfMarriagePlace(TypeOfMarriagePlace.REGISTRAR_OFFICE);
            marriage.setTypeOfMarriage(MarriageType.GENERAL);
            marriage.setMale(new MaleParty());
            marriage.getMale().setCivilStatusMale(Person.CivilStatus.NEVER_MARRIED);
            marriage.setFemale(new FemaleParty());
            marriage.getFemale().setCivilStatusFemale(Person.CivilStatus.NEVER_MARRIED);
        }
        return INPUT;
    }

    /**
     * Marriage Registration - persist new marriage entry through the page for muslim type marrige
     */
    public String registerNewMarriage() {
        populateMuslimMarriageDetails();
        try {
            marriageRegistrationService.addMarriageRegister(marriage, user, scannedImage, scannedImageFileName);
            addActionMessage("uploaded file name " + scannedImageFileName);
            addActionMessage("uploaded content type " + scannedImageContentType);
        } catch (CRSRuntimeException e) {
            addActionError(getText("error.marriageregister.registrationfailed"));
            return marriageRegistrationInit();
        }
        addActionMessage(getText("message.marriageregister.registered"));
        return SUCCESS;
    }

    /**
     * Register and approve muslim marriages
     */
    public String registerAndApproveNewMarriage() {
        //TODO: implement a single mothod allowing access only for adr and higher
        populateMuslimMarriageDetails();
        try {
            marriageRegistrationService.addMarriageRegister(marriage, user, scannedImage, scannedImageFileName);
        } catch (CRSRuntimeException e) {
            addActionError(getText("error.marriageregister.registrationfailed"));
            return marriageRegistrationInit();
        }
        try {
            marriageRegistrationService.approveMarriageRegister(marriage.getIdUKey(), user);
        } catch (CRSRuntimeException e) {
            addActionError(getText("error.marriageregister.approvalfailed"));
            return marriageRegistrationInit();
        }
        addActionMessage(getText("message.marriageregister.registeredandapproved"));
        return SUCCESS;
    }

    /**
     * Update and approve muslim marriages
     */
    public String updateAndApproveMuslimMarriage() {
        //TODO: implement a single mothod allowing access only for adr and higher
        MarriageRegister marriageRegister = marriageRegistrationService.getByIdUKey(idUKey, user);
        if (marriageRegister == null) {
            addActionError(getText("error.marriageregister.notfound"));
            return marriageRegistrationInit();
        }
        populateRegistrationDetails(marriageRegister);
        populateMaleFemaleDetails(marriageRegister);
        try {
            marriageRegistrationService.updateMarriageRegister(marriageRegister, user, scannedImage, scannedImageFileName);
        } catch (CRSRuntimeException e) {
            addActionError(getText("error.marriageregister.failedtoupdate"));
            return marriageRegistrationInit();
        }
        try {
            marriageRegistrationService.approveMarriageRegister(marriageRegister.getIdUKey(), user);
        } catch (CRSRuntimeException e) {
            addActionError(getText("error.marriageregister.approvalfailed"));
            return marriageRegistrationInit();
        }
        addActionMessage(getText("message.marriageregister.updatedandapproved"));
        return SUCCESS;
    }

    private void populateMuslimMarriageDetails() {
        MRDivision mrDivision = mrDivisionDAO.getMRDivisionByPK(mrDivisionId);
        marriage.setMrDivision(mrDivision);
        marriage.setMrDivisionOfMaleNotice(mrDivision);
        marriage.setMrDivisionOfFemaleNotice(mrDivision);
        marriage.setState(MarriageRegister.State.REG_DATA_ENTRY);
        //TODO to be removed if Race is mandatory.
        /*if (marriage.getMale().getMaleRace().getRaceId() == 0) {
            marriage.getMale().setMaleRace(null);
        }
        if (marriage.getFemale().getFemaleRace().getRaceId() == 0) {
            marriage.getFemale().setFemaleRace(null);
        }*/
    }

    public String displayScannedImage() {
        return SUCCESS;
    }

    /**
     * Marriage Registration - update Muslim Marriage Details
     */
    public String updateMarriageDetails() {
        MarriageRegister marriageRegister = marriageRegistrationService.getByIdUKey(idUKey, user);
        if (marriageRegister == null) {
            addActionError(getText("error.marriageregister.notfound"));
            return marriageRegistrationInit();
        }
        populateRegistrationDetails(marriageRegister);
        populateMaleFemaleDetails(marriageRegister);
        try {
            marriageRegistrationService.updateMarriageRegister(marriageRegister, user, scannedImage, scannedImageFileName);
        } catch (CRSRuntimeException e) {
            addActionError(getText("error.marriageregister.failedtoupdate"));
            return marriageRegistrationInit();
        }
        addActionMessage(getText("message.marriageregister.updated"));
        return SUCCESS;
    }

    /**
     * Marriage Registration - Update existing licensed marriage entry with the registrar details, registration place and the date
     */
    public String registerNoticedMarriage() {
        MarriageRegister marriageRegister = marriageRegistrationService.getByIdUKey(idUKey, user);
        if (marriageRegister == null) {
            addActionError(getText("error.marriageregister.notfound"));
            return marriageRegistrationInit();
        }
        populateRegistrationDetails(marriageRegister);
        try {
            marriageRegistrationService.updateMarriageRegister(marriageRegister, user, scannedImage, scannedImageFileName);
        } catch (CRSRuntimeException e) {
            addActionError(getText("error.marriageregister.registrationfailed"));
            return marriageRegistrationInit();
        }
        addActionMessage(getText("message.marriageregister.registered"));
        return SUCCESS;
    }

    private void getAndRemoveNoticeFromSession() {
        marriage = (MarriageRegister) session.get(WebConstants.SESSION_NOTICE_WARNINGS);
        mrDivisionId = (Integer) session.get(WebConstants.SESSION_NOTICE_MR_DIVISION_KEY);
        noticeReceivedDate = (Date) session.get(WebConstants.SESSION_NOTICE_RECEIVED_DATE);
        serialNumber = (Long) session.get(WebConstants.SESSION_NOTICE_SERIAL);
        session.remove(WebConstants.SESSION_NOTICE_WARNINGS);
        session.remove(WebConstants.SESSION_NOTICE_MR_DIVISION_KEY);
        session.remove(WebConstants.SESSION_NOTICE_RECEIVED_DATE);
        session.remove(WebConstants.SESSION_NOTICE_SERIAL);
    }

    private void addNoticeToSession(MarriageRegister existingNotice) {
        session.put(WebConstants.SESSION_NOTICE_WARNINGS, existingNotice);
        session.put(WebConstants.SESSION_NOTICE_MR_DIVISION_KEY, mrDivisionId);
        session.put(WebConstants.SESSION_NOTICE_RECEIVED_DATE, noticeReceivedDate);
        session.put(WebConstants.SESSION_NOTICE_SERIAL, serialNumber);
    }

    /**
     * Marriage Registration - populate MarriageRegister object with the registrar details, registration place and the date
     */
    private void populateRegistrationDetails(MarriageRegister marriageRegister) {
        marriageRegister.setDateOfMarriage(marriage.getDateOfMarriage());
        marriageRegister.setRegistrarOrMinisterPIN(marriage.getRegistrarOrMinisterPIN());
        marriageRegister.setTypeOfMarriagePlace(marriage.getTypeOfMarriagePlace());
        marriageRegister.setMrDivision(mrDivisionDAO.getMRDivisionByPK(mrDivisionId));
        marriageRegister.setRegPlaceInEnglishLang(marriage.getRegPlaceInEnglishLang());
        marriageRegister.setRegPlaceInOfficialLang(marriage.getRegPlaceInOfficialLang());
        marriageRegister.setRegNameInEnglishLang(marriage.getRegNameInEnglishLang());
        marriageRegister.setRegNameInOfficialLang(marriage.getRegNameInOfficialLang());
        marriageRegister.setRegistrationDate(marriage.getRegistrationDate());
        marriageRegister.setPreferredLanguage(marriage.getPreferredLanguage());
        marriageRegister.setTypeOfMarriage(marriage.getTypeOfMarriage());
        marriageRegister.setSerialNumber(marriage.getSerialNumber());
        marriageRegister.setState(MarriageRegister.State.REG_DATA_ENTRY);
    }

    /**
     * Marriage Registration - populate MarriageRegister object with the details of Male party and the Female party
     */
    private void populateMaleFemaleDetails(MarriageRegister marriageRegister) {
        //populate the details of Male party
        marriageRegister.getMale().setIdentificationNumberMale(marriage.getMale().getIdentificationNumberMale());
        marriageRegister.getMale().setDateOfBirthMale(marriage.getMale().getDateOfBirthMale());
        marriageRegister.getMale().setAgeAtLastBirthDayMale(marriage.getMale().getAgeAtLastBirthDayMale());
        marriageRegister.getMale().setMaleRace(marriage.getMale().getMaleRace());
        marriageRegister.getMale().setCivilStatusMale(marriage.getMale().getCivilStatusMale());
        marriageRegister.getMale().setNameInEnglishMale(marriage.getMale().getNameInEnglishMale());
        marriageRegister.getMale().setNameInOfficialLanguageMale(marriage.getMale().getNameInOfficialLanguageMale());
        marriageRegister.getMale().setResidentAddressMaleInOfficialLang(marriage.getMale().getResidentAddressMaleInOfficialLang());
        //populate the details of Female party
        marriageRegister.getFemale().setIdentificationNumberFemale(marriage.getFemale().getIdentificationNumberFemale());
        marriageRegister.getFemale().setDateOfBirthFemale(marriage.getFemale().getDateOfBirthFemale());
        marriageRegister.getFemale().setAgeAtLastBirthDayFemale(marriage.getFemale().getAgeAtLastBirthDayFemale());
        marriageRegister.getFemale().setFemaleRace(marriage.getFemale().getFemaleRace());
        marriageRegister.getFemale().setCivilStatusFemale(marriage.getFemale().getCivilStatusFemale());
        marriageRegister.getFemale().setNameInEnglishFemale(marriage.getFemale().getNameInEnglishFemale());
        marriageRegister.getFemale().setNameInOfficialLanguageFemale(marriage.getFemale().getNameInOfficialLanguageFemale());
        marriageRegister.getFemale().setResidentAddressFemaleInOfficialLang(marriage.getFemale().getResidentAddressFemaleInOfficialLang());
    }

    /**
     * Marriage Registration - Populate basic lists at page load
     */
    private void populateLists() {
        marriageTypeList = MarriageType.values();
        typeOfMarriagePlaceList = TypeOfMarriagePlace.values();
        civilStatusMale = CivilStatusUtil.populateCivilStatus();
        civilStatusFemale = CivilStatusUtil.populateCivilStatus();
        languageList = commonUtil.findLanguageList();
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
            marriageDistrictId, dsDivisionId, mrDivisionId, AppConstants.MARRIAGE, user, language);
        raceList = raceDAO.getRaces(language);
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("setting User: {}", user.getUserName());
        Locale userLocale = (Locale) session.get(WebConstants.SESSION_USER_LANG);
        language = userLocale.getLanguage();
        logger.debug("setting language : {}", language);
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public void setDsDivisionList(Map<Integer, String> dsDivisionList) {
        this.dsDivisionList = dsDivisionList;
    }

    public Map<Integer, String> getMrDivisionList() {
        return mrDivisionList;
    }

    public void setMrDivisionList(Map<Integer, String> mrDivisionList) {
        this.mrDivisionList = mrDivisionList;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public MarriageRegister getMarriage() {
        return marriage;
    }

    public void setMarriage(MarriageRegister marriage) {
        this.marriage = marriage;
    }

    public int getMarriageDistrictId() {
        return marriageDistrictId;
    }

    public void setMarriageDistrictId(int marriageDistrictId) {
        this.marriageDistrictId = marriageDistrictId;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public int getMrDivisionId() {
        return mrDivisionId;
    }

    public void setMrDivisionId(int mrDivisionId) {
        this.mrDivisionId = mrDivisionId;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public Date getNoticeReceivedDate() {
        return noticeReceivedDate;
    }

    public void setNoticeReceivedDate(Date noticeReceivedDate) {
        this.noticeReceivedDate = noticeReceivedDate;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Map<Integer, String> getRaceList() {
        return raceList;
    }

    public void setRaceList(Map<Integer, String> raceList) {
        this.raceList = raceList;
    }

    public Map<Integer, String> getCountryList() {
        return countryList;
    }

    public void setCountryList(Map<Integer, String> countryList) {
        this.countryList = countryList;
    }

    public int getRaceIdMale() {
        return raceIdMale;
    }

    public void setRaceIdMale(int raceIdMale) {
        this.raceIdMale = raceIdMale;
    }

    public int getRaceIdFemale() {
        return raceIdFemale;
    }

    public void setRaceIdFemale(int raceIdFemale) {
        this.raceIdFemale = raceIdFemale;
    }

    public int getCountryIdMale() {
        return countryIdMale;
    }

    public void setCountryIdMale(int countryIdMale) {
        this.countryIdMale = countryIdMale;
    }

    public int getCountryIdFemale() {
        return countryIdFemale;
    }

    public void setCountryIdFemale(int countryIdFemale) {
        this.countryIdFemale = countryIdFemale;
    }

    public boolean isSecondNotice() {
        return secondNotice;
    }

    public void setSecondNotice(boolean secondNotice) {
        this.secondNotice = secondNotice;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public Map<Person.CivilStatus, String> getCivilStatusMale() {
        return civilStatusMale;
    }

    public void setCivilStatusMale(Map<Person.CivilStatus, String> civilStatusMale) {
        this.civilStatusMale = civilStatusMale;
    }

    public Map<Person.CivilStatus, String> getCivilStatusFemale() {
        return civilStatusFemale;
    }

    public void setCivilStatusFemale(Map<Person.CivilStatus, String> civilStatusFemale) {
        this.civilStatusFemale = civilStatusFemale;
    }

    public MarriageNotice.Type getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(MarriageNotice.Type noticeType) {
        this.noticeType = noticeType;
    }

    public MarriageType[] getMarriageTypeList() {
        return marriageTypeList;
    }

    public void setMarriageTypeList(MarriageType[] marriageTypeList) {
        this.marriageTypeList = marriageTypeList;
    }

    public TypeOfMarriagePlace[] getTypeOfMarriagePlaceList() {
        return typeOfMarriagePlaceList;
    }

    public void setTypeOfMarriagePlaceList(TypeOfMarriagePlace[] typeOfMarriagePlaceList) {
        this.typeOfMarriagePlaceList = typeOfMarriagePlaceList;
    }

    public List<UserWarning> getUserWarnings() {
        return userWarnings;
    }

    public void setUserWarnings(List<UserWarning> userWarnings) {
        this.userWarnings = userWarnings;
    }

    public boolean isIgnoreWarnings() {
        return ignoreWarnings;
    }

    public void setIgnoreWarnings(boolean ignoreWarnings) {
        this.ignoreWarnings = ignoreWarnings;
    }

    public Map<String, String> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(Map<String, String> languageList) {
        this.languageList = languageList;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public File getScannedImage() {
        return scannedImage;
    }

    public void setScannedImage(File scannedImage) {
        this.scannedImage = scannedImage;
    }

    public String getScannedImageContentType() {
        return scannedImageContentType;
    }

    public void setScannedImageContentType(String scannedImageContentType) {
        this.scannedImageContentType = scannedImageContentType;
    }

    public String getScannedImageFileName() {
        return scannedImageFileName;
    }

    public void setScannedImageFileName(String scannedImageFileName) {
        this.scannedImageFileName = scannedImageFileName;
    }
}

