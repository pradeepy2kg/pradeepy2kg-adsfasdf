package lk.rgd.crs.web.action.deaths;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.AppConstants;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.common.util.WebUtils;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.DeathAlteration;
import lk.rgd.crs.api.domain.DeathAlterationInfo;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.service.DeathAlterationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.CommonUtil;
import lk.rgd.crs.web.util.FieldValue;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * @author amith jayasekara   action class for death alterations
 */
public class DeathAlterationAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(DeathAlterationAction.class);
    private static final String DA_APPROVAL_ROWS_PER_PAGE = "crs.br_approval_rows_per_page";

    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final RaceDAO raceDAO;
    private final CountryDAO countryDAO;
    private final AppParametersDAO appParametersDAO;
    private final CommonUtil commonUtil;
    private DeathAlterationService deathAlterationService;
    private DeathRegistrationService deathRegistrationService;

    private User user;
    private Date toDay;

    private DeathAlteration deathAlteration;
    private DeathRegister deathRegister;

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> userLocations;

    private List<DeathAlteration> approvalList;
    private List<FieldValue> changesList = new LinkedList<FieldValue>();

    private int[] approvedIndex;

    private int dsDivisionId;
    private int birthDivisionId;
    private int pageNumber; //use to track alteration capture page load and add or edit
    private int districtUKey;
    private int divisionUKey;
    private int pageNo;  //to capture data table pagination
    private int rowNo;
    private int deathPersonCountry;
    private int deathPersonRace;
    private int deathCountryId;
    private int deathRaceId;
    private int locationUKey;

    private long idUKey;
    private long serialNumber;
    private long alterationSerialNo;
    private long deathId;
    private long deathAlterationId;

    private String language;
    private String district;
    private String dsDivision;
    private String deathDivision;
    private String rejectComment;
    private String pin;

    private boolean editMode;
    private boolean editDeathInfo;
    private boolean editDeathPerson;
    private boolean approvalPage;

    public DeathAlterationAction(DeathAlterationService deathAlterationService,
        DeathRegistrationService deathRegistrationService, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO,
        BDDivisionDAO bdDivisionDAO, RaceDAO raceDAO, CountryDAO countryDAO, AppParametersDAO appParametersDAO,
        CommonUtil commonUtil) {
        this.deathAlterationService = deathAlterationService;
        this.deathRegistrationService = deathRegistrationService;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.raceDAO = raceDAO;
        this.countryDAO = countryDAO;
        this.appParametersDAO = appParametersDAO;
        this.commonUtil = commonUtil;
    }

    /**
     * loading death alteration search page
     */
    public String deathAlterationSearch() {
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("loading death alteration search page");
        populatePrimaryLists(districtUKey, dsDivisionId, language, user);
        return SUCCESS;
    }

    private void findDeathCertificateForDeathAlteration() {
        //search by certificate number
        if (idUKey != 0) {
            logger.debug("attempt to load death register by certificate number : {}", idUKey);
            deathRegister = deathRegistrationService.getById(idUKey);
        }
        //search by pin
        else if (pin != null && Long.parseLong(pin) != 0) {
            //only get first record others ignored  because there can be NIC duplications
            //TODO amith very important use try catch here :D there can be invalid  user access then it throws an exceptions
            logger.debug("attempt to load death register by pin number : {}", pin);
            List<DeathRegister> deathRegisterList = deathRegistrationService.getByPinOrNic(Long.parseLong(pin), user);
            if (deathRegisterList != null) {
                deathRegister = deathRegisterList.get(0);
            }
        }
        //search by  serial and death division
        else if (serialNumber != 0 && divisionUKey != 0) {
            logger.debug("attempt to load death register by serial number : {} and death division : {}",
                serialNumber, divisionUKey);
            BDDivision deathDivision = bdDivisionDAO.getBDDivisionByPK(divisionUKey);
            deathRegister = deathRegistrationService.getByBDDivisionAndDeathSerialNo(deathDivision, serialNumber, user);
        }
    }

    /**
     * load death alteration capture page for searched death certificate
     */
    public String deathAlterationCaptureInit() {
        logger.debug("attempting to load death alteration capture page");
        findDeathCertificateForDeathAlteration();
        if (deathRegister == null) {
            logger.debug("can not find a death registrations for alterations :serial {} or :idUKey : {}", serialNumber, idUKey);
            addActionError(getText("error.cannot.find.death.registration"));
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            return SUCCESS;
        } else {
            //check is there a ongoing alteration for this certificate
            //check death register is not null and in data approved state
            List<DeathAlteration> existingAlterations =
                deathAlterationService.getAlterationByDeathCertificateNumber(deathRegister.getIdUKey(), user);
            Iterator<DeathAlteration> itr = existingAlterations.iterator();
            while (itr.hasNext()) {
                DeathAlteration da = itr.next();
                if (!da.getStatus().equals(DeathAlteration.State.FULLY_APPROVED)) {
                    logger.error("there is a ongoing alteration so cannot add a new");
                    addActionError(getText("error.existing.alterations.data.entry",
                        new String[]{Long.toString(deathRegister.getIdUKey())}));
                    populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                    return ERROR;
                }
            }
            if (!deathRegister.getStatus().equals(DeathRegister.State.ARCHIVED_CERT_GENERATED)) {
                logger.error("cannot capture alterations certificate is not in correct state for alteration");
                addActionError(getText("error.death.certificate.must.print.before"));
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return ERROR;
            }
        }
        populateOtherLists();
        deathAlteration = populateAlterationObject(deathRegister);
        //setting up death district    ds and death division
        district = districtDAO.getNameByPK(deathRegister.getDeath().getDeathDistrict().getDistrictUKey(), language);
        DSDivision division = deathRegister.getDeath().getDeathDivision().getDsDivision();
        dsDivision = dsDivisionDAO.getNameByPK(division.getDsDivisionUKey(), language);
        deathDivision = bdDivisionDAO.getNameByPK(deathRegister.getDeath().getDeathDivision().getBdDivisionUKey(), language);
        Country country = deathRegister.getDeathPerson().getDeathPersonCountry();
        serialNumber = deathRegister.getDeath().getDeathSerialNo();
        if (country != null) {
            deathCountryId = country.getCountryId();
        }
        Race race = deathRegister.getDeathPerson().getDeathPersonRace();
        if (race != null) {
            deathRaceId = race.getRaceId();
        }
        toDay = new Date();
        return "pageLoad";
    }

    /**
     * capture death alterations and edit death alteration
     */
    public String captureDeathAlterations() {
        logger.debug("capturing death alteration");
        try {
            DeathRegister dr = deathRegistrationService.getById(deathId);
            deathAlteration.setDeathRegisterIDUkey(deathId);
            deathAlteration.setStatus(DeathAlteration.State.DATA_ENTRY);
            deathAlteration.setDeathRecordDivision(dr.getDeath().getDeathDivision());
            deathAlteration.setDeathPersonPin(dr.getDeathPerson().getDeathPersonPINorNIC());
            Country deathCountry;
            if (deathPersonCountry > 0) {
                deathCountry = countryDAO.getCountry(deathPersonCountry);
                deathAlteration.getDeathPerson().setDeathPersonCountry(deathCountry);
            }
            Race deathRace;
            if (deathPersonRace > 0) {
                deathRace = raceDAO.getRace(deathPersonRace);
                deathAlteration.getDeathPerson().setDeathPersonRace(deathRace);
            }
            if (!editDeathInfo) {
                deathAlteration.setDeathInfo(null);
            }
            if (!editDeathPerson) {
                deathAlteration.setDeathPerson(null);
            }
            deathAlterationService.addDeathAlteration(deathAlteration, user);
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            addActionMessage(getText("alt.message.success"));
            logger.debug("capturing alteration success ");
            return SUCCESS;

        } catch (CRSRuntimeException e) {
            //todo both cases(both object null and declerant info is null gives same error message if need two add here)
            logger.error("CRS exception while adding death alteration ");
            addActionMessage(getText("alt.message.cannot.add.alteration.validation.failed"));
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            return ERROR;
        }
    }

    public String deathAlterationEdit() {
        logger.debug("attempt to edit death alteration : idUKey : {}", deathAlteration.getIdUKey());
        DeathAlteration existing = deathAlterationService.getByIDUKey(deathAlterationId, user);
        try {
            deathRegister = deathRegistrationService.getById(existing.getDeathRegisterIDUkey(), user);
            if (editDeathInfo) {
                existing.setDeathInfo(deathAlteration.getDeathInfo());
            }
            if (editDeathPerson) {
                existing.setDeathPerson(deathAlteration.getDeathPerson());
            }
            existing.setDeclarant(deathAlteration.getDeclarant());
            deathAlterationService.updateDeathAlteration(existing, user);

            addActionMessage(getText("alt.edit.message.success",
                new String[]{Long.toString(deathAlteration.getIdUKey())}));
            logger.debug("editing death alteration : idUKey : {} success", deathAlterationId);
            populatePrimaryLists(deathRegister.getDeath().getDeathDistrict().getDistrictUKey(),
                deathRegister.getDeath().getDeathDivision().getBdDivisionUKey(), language, user);
            userLocations = commonUtil.populateActiveUserLocations(user, language);
            return SUCCESS;
        } catch (CRSRuntimeException e) {
            logger.debug("cannot edit death alteration idUKey {}", existing.getIdUKey());
            addActionError(getText("alt.message.cannot.edit.alteration.validation.failed",
                new String[]{Long.toString(deathAlteration.getIdUKey())}));
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            userLocations = commonUtil.populateActiveUserLocations(user, language);
            return ERROR;
        }
    }


    public String editDeathAlterationInit() {
        logger.debug("attempt to edit a death alteration : idUKey : {} ", deathAlterationId);
        deathAlteration = deathAlterationService.getByIDUKey(deathAlterationId, user);
        if (deathAlteration != null) {
            if (deathAlteration.getStatus().equals(DeathAlteration.State.DATA_ENTRY)) {
                //populate death person info at alteration
                deathRegister = deathRegistrationService.getById(deathAlteration.getDeathRegisterIDUkey(), user);
                district = districtDAO.getNameByPK(deathRegister.getDeath().getDeathDistrict().getDistrictUKey(), language);
                DSDivision division = deathRegister.getDeath().getDeathDivision().getDsDivision();
                dsDivision = dsDivisionDAO.getNameByPK(division.getDsDivisionUKey(), language);
                deathDivision = bdDivisionDAO.getNameByPK(deathRegister.getDeath().getDeathDivision().getBdDivisionUKey(), language);
                Country country = (deathAlteration.getDeathPerson() != null) ?
                    deathAlteration.getDeathPerson().getDeathPersonCountry() : null;
                serialNumber = deathRegister.getDeath().getDeathSerialNo();
                if (country != null) {
                    deathCountryId = country.getCountryId();
                }
                Race race = (deathAlteration.getDeathPerson() != null) ?
                    deathAlteration.getDeathPerson().getDeathPersonRace() : null;
                if (race != null) {
                    deathRaceId = race.getRaceId();
                }
                editMode = true;
                toDay = deathAlteration.getDateReceived();
                alterationSerialNo = deathAlteration.getIdUKey();
            } else {
                logger.debug("cannot edit death alteration idUKey : {} : not in DATA_ENTRY mode", deathAlterationId);
                addActionError(getText("error.cannot.edit.death.alteration",
                    new String[]{Long.toString(deathAlteration.getIdUKey())}));
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return ERROR;
            }
        } else {
            logger.debug("unable to find death alteration : idUKey : {} : for edit", deathAlterationId);
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            return ERROR;
        }
        populateOtherLists();
        return SUCCESS;
    }

    /**
     * searching death alterations for approvals/rejection/delete and edit
     */
    private void findDeathAlterationForApproval() {
        //search by pin
        rowNo = appParametersDAO.getIntParameter(DA_APPROVAL_ROWS_PER_PAGE);
        if (pin != null) {
            approvalList = deathAlterationService.getAlterationByDeathPersonPin(pin, user);
        } else if (locationUKey > 0) {
            //search by user location
            approvalList = deathAlterationService.getDeathAlterationByUserLocation(locationUKey, user);
        } else if (divisionUKey > 0) {
            approvalList = deathAlterationService.getAlterationApprovalListByDeathDivision(pageNo, rowNo, divisionUKey, user);
        } else {
            approvalList = deathAlterationService.getAlterationApprovalListByDeathDivision(1, rowNo,
                user.getPrimaryLocation().getDsDivisionId(), user);
        }
    }


    public String deathAlterationApprovalListInit() {
        //todo fixe here
        logger.debug("loading death alteration approval list page ");
        populatePrimaryLists(districtUKey, dsDivisionId, language, user);
        divisionUKey = user.getPrimaryLocation().getDsDivisionId();
        pageNo = 1;
        findDeathAlterationForApproval();
        userLocations = commonUtil.populateActiveUserLocations(user, language);
        locationUKey = 0;
        logger.debug("success fully loaded death alteration approval page");
        return SUCCESS;
    }

    public String deathAlterationApprovalList() {
        pageNo = 1;
        rowNo = appParametersDAO.getIntParameter(DA_APPROVAL_ROWS_PER_PAGE);
        logger.debug("attempt to get death alteration pending list");
        if (pageNumber > 0) {
            try {
                findDeathAlterationForApproval();
            }
            catch (CRSRuntimeException e) {
                logger.debug("unable to death alteration pending list");
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                userLocations = commonUtil.populateActiveUserLocations(user, language);
                locationUKey = 0;
                findDeathAlterationForApproval();
                addActionError(getText("error.while.searching"));
                return ERROR;
            }
            if (approvalList.size() < 1) {
                logger.debug("no pending list found ");
                addActionError(getText("no.pending.alterations"));
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                userLocations = commonUtil.populateActiveUserLocations(user, language);
                locationUKey = 0;
                findDeathAlterationForApproval();
                return ERROR;
            }
        }
        populatePrimaryLists(districtUKey, dsDivisionId, language, user);
        userLocations = commonUtil.populateActiveUserLocations(user, language);
        locationUKey = 0;
        return SUCCESS;
    }

    public String displayChangesForApproval() {
        logger.debug("approving direct death alteration : {}", deathAlterationId);

        if (deathAlterationId > 0) {
            //approving from death alteration approval list
            deathAlteration = deathAlterationService.getByIDUKey(deathAlterationId, user);
        }
        //todo remove follow else block
        else {
            addActionError(getText("cannot.find.death.alteration.for.approval"));
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            return ERROR;
        }
        //checking in data entry
        if (!(deathAlteration.getStatus().equals(DeathAlteration.State.DATA_ENTRY))) {
            logger.error("cannot approve death alteration : id {} :invalid alteration state", deathAlteration.getIdUKey());
            addActionError(getText("cannot.approve.nt.in.correct.state"));
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            return ERROR;
        }

        deathRegister = deathRegistrationService.getById(deathAlteration.getDeathRegisterIDUkey(), user);
        generateChangesList(deathRegister, deathAlteration, deathRegister.getDeath().getPreferredLanguage());
        approvalPage = true;
        return SUCCESS;
    }

    public String approveAlteration() {
        logger.debug("setting bit set : {}", approvedIndex);
        try {
            Hashtable<Integer, Boolean> approveBitset = new Hashtable<Integer, Boolean>();
            deathAlteration = deathAlterationService.getByIDUKey(deathAlterationId, user);
            if (approvedIndex != null) {
                for (int i = 0; i < approvedIndex.length; i++) {
                    int bit = approvedIndex[i];
                    approveBitset.put(bit, true);
                }
            }
            deathAlterationService.approveDeathAlteration(deathAlteration, approveBitset, user);
            addActionMessage(getText("message.approve.death.alteration.success",
                new String[]{Long.toString(deathAlterationId)}));
            populateListAfterSuccessOrFail();
            findDeathAlterationForApproval();
            logger.debug("apply changes to death alteration : alteration id  {}", deathAlterationId);
        } catch (CRSRuntimeException e) {
            logger.error("cannot set bit set for death alteration : {}", deathAlterationId);
            addActionError(getText("error.approve.death.alteration", new String[]{Long.toString(deathAlterationId)}));
            populateListAfterSuccessOrFail();
            return ERROR;
        }
        return SUCCESS;
    }


    public String deleteDeathAlteration() {
        logger.debug("attempt to delete death alteration : idUKey :{} by User : {}", deathAlterationId, user.getUserName());
        deathAlterationService.deleteDeathAlteration(deathAlterationId, user);
        populatePrimaryLists(districtUKey, dsDivisionId, language, user);
        logger.debug("death alteration deleted success : alteration id :{}", deathAlterationId);
        return SUCCESS;
    }

    public String rejectDeathAlteration() {
        logger.debug("attempt to load death alteration  rejection page for get comment : for death alteration id  {} ", deathAlterationId);
        if (pageNumber > 0) {
            logger.debug("attempt to reject death alteration : idUKey : {} by User : {}", deathAlterationId, user.getUserName());
            try {
                deathAlterationService.rejectDeathAlteration(deathAlterationId, user, rejectComment);
            }
            catch (CRSRuntimeException e) {
                logger.debug("error while reject death alteration : idUKey {} ", deathAlterationId);
                addActionMessage(getText("error.failed.reject.death.alteration",
                    new String[]{Long.toString(deathAlterationId)}));
                populateListAfterSuccessOrFail();
            }
            populateListAfterSuccessOrFail();
            addActionMessage(getText("message.successfully.reject.death.alteration",
                new String[]{Long.toString(deathAlterationId)}));
            logger.debug("death alteration rejected success : alteration id :{}", deathAlterationId);
            return SUCCESS;
        }
        deathAlteration = deathAlterationService.getByIDUKey(deathAlterationId, user);
        return "pageLoad";
    }

    private void populateListAfterSuccessOrFail() {
        populatePrimaryLists(districtUKey, dsDivisionId, language, user);
        userLocations = commonUtil.populateActiveUserLocations(user, language);
        locationUKey = 0;
        findDeathAlterationForApproval();
    }

    /**
     * printing confirmation letter to declarent who requested changes
     */
    public String printAlterationLetter() {
        //todo check the state before load page to print       todo what is BA is null ? handle that error
        logger.debug("generating printing letter for death alteration id : {}", deathAlterationId);
        //loading requested alterations
        deathAlteration = deathAlterationService.getByIDUKey(deathAlterationId, user);
        deathRegister = deathRegistrationService.getById(deathAlteration.getDeathRegisterIDUkey());
        String preferedLan = deathRegister.getDeath().getPreferredLanguage();
        generateChangesList(deathRegister, deathAlteration, preferedLan);
        logger.debug("complete generating printing letter for death alteration id : {}", deathAlterationId);
        return "pageLoad";
    }

    private void generateChangesList(DeathRegister deathRegister, DeathAlteration deathAlteration, String preferedLan) {
        logger.debug("begin generating changes list");

        if (deathAlteration.getDeathInfo() != null && deathRegister.getDeath() != null) {
            //todo check sudden death
            String dateEx = null;
            String dateAlt = null;
            if (deathRegister.getDeath().getDateOfDeath() != null) {
                dateEx = DateTimeUtils.getISO8601FormattedString(deathRegister.getDeath().getDateOfDeath());
            }
            if (deathAlteration.getDeathInfo().getDateOfDeath() != null) {
                dateAlt = DateTimeUtils.getISO8601FormattedString(deathAlteration.getDeathInfo().getDateOfDeath());
            }

            compareStringValues(deathRegister.getDeath().getPlaceOfBurial(),
                deathAlteration.getDeathInfo().getPlaceOfBurial(), DeathAlteration.BURIAL_PLACE, preferedLan);

            compareStringValues(deathRegister.getDeath().getIcdCodeOfCause(),
                deathAlteration.getDeathInfo().getIcdCodeOfCause(), DeathAlteration.ICD_CODE, preferedLan);

            compareStringValues(deathRegister.getDeath().getCauseOfDeath(),
                deathAlteration.getDeathInfo().getCauseOfDeath(), DeathAlteration.CAUSE_OF_DEATH, preferedLan);

            compareStringValues(deathRegister.getDeath().getPlaceOfDeathInEnglish(),
                deathAlteration.getDeathInfo().getPlaceOfDeathInEnglish(), DeathAlteration.PLACE_OF_DEATH_ENGLISH, preferedLan);

            compareStringValues(deathRegister.getDeath().getPlaceOfDeath(),
                deathAlteration.getDeathInfo().getPlaceOfDeath(), DeathAlteration.PLACE_OF_DEATH, preferedLan);

            compareStringValues(deathRegister.getDeath().getTimeOfDeath(),
                deathAlteration.getDeathInfo().getTimeOfDeath(), DeathAlteration.TIME_OF_DEATH, preferedLan);

            compareStringValues(dateEx, dateAlt, DeathAlteration.DATE_OF_DEATH, preferedLan);

            if (!(deathRegister.getDeath().isCauseOfDeathEstablished() ==
                deathAlteration.getDeathInfo().isCauseOfDeathEstablished())) {
                changesList.add(new FieldValue(lk.rgd.common.util.CommonUtil.getYesOrNo(deathRegister.getDeath().isCauseOfDeathEstablished(), preferedLan),
                    lk.rgd.common.util.CommonUtil.getYesOrNo(deathAlteration.getDeathInfo().isCauseOfDeathEstablished(), preferedLan),
                    DeathAlteration.CAUSE_OF_DEATH_ESTABLISHED,
                    lk.rgd.common.util.CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.CAUSE_OF_DEATH_ESTABLISHED), preferedLan)
                ));
            }

        }

        if (deathAlteration.getDeathPerson() != null && deathRegister.getDeathPerson() != null) {

            compareStringValues(deathRegister.getDeathPerson().getDeathPersonPINorNIC(), deathAlteration.getDeathPerson().
                getDeathPersonPINorNIC(), DeathAlteration.PIN, preferedLan);

            compareStringValues(deathRegister.getDeathPerson().getDeathPersonNameOfficialLang(),
                deathAlteration.getDeathPerson().getDeathPersonNameOfficialLang(), DeathAlteration.NAME, preferedLan);

            compareStringValues(deathRegister.getDeathPerson().getDeathPersonNameInEnglish(),
                deathAlteration.getDeathPerson().getDeathPersonNameInEnglish(), DeathAlteration.NAME_ENGLISH, preferedLan);

            compareStringValues(deathRegister.getDeathPerson().getDeathPersonPermanentAddress(),
                deathAlteration.getDeathPerson().getDeathPersonPermanentAddress(), DeathAlteration.ADDRESS, preferedLan);

            compareStringValues(deathRegister.getDeathPerson().getDeathPersonFatherPINorNIC(),
                deathAlteration.getDeathPerson().getDeathPersonFatherPINorNIC(), DeathAlteration.PIN_FATHER, preferedLan);

            compareStringValues(deathRegister.getDeathPerson().getDeathPersonFatherFullName(),
                deathAlteration.getDeathPerson().getDeathPersonFatherFullName(), DeathAlteration.NAME_FATHER, preferedLan);

            compareStringValues(deathRegister.getDeathPerson().getDeathPersonMotherPINorNIC(),
                deathAlteration.getDeathPerson().getDeathPersonMotherPINorNIC(), DeathAlteration.PIN_MOTHER, preferedLan);

            compareStringValues(deathRegister.getDeathPerson().getDeathPersonMotherFullName(),
                deathAlteration.getDeathPerson().getDeathPersonMotherFullName(), DeathAlteration.NAME_MOTHER, preferedLan);

            compareStringValues(deathRegister.getDeathPerson().getDeathPersonPassportNo(),
                deathAlteration.getDeathPerson().getDeathPersonPassportNo(), DeathAlteration.PASSPORT, preferedLan);

            if (!(deathRegister.getDeathPerson().getDeathPersonAge() == deathAlteration.getDeathPerson().getDeathPersonAge())) {
                changesList.add(new FieldValue(
                    deathRegister.getDeathPerson().getDeathPersonAge() != null ? Integer.toString(deathRegister.getDeathPerson().getDeathPersonAge()) : null,
                    deathAlteration.getDeathPerson().getDeathPersonAge() != null ? Integer.toString(deathAlteration.getDeathPerson().getDeathPersonAge()) : null,
                    DeathAlteration.AGE, lk.rgd.common.util.CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.AGE), preferedLan)));
            }

            if (!(deathAlteration.getDeathPerson().getDeathPersonGender() == deathAlteration.getDeathPerson().getDeathPersonGender())) {
                changesList.add(new FieldValue(GenderUtil.getGender(deathAlteration.getDeathPerson().getDeathPersonGender(),
                    preferedLan), GenderUtil.getGender(deathAlteration.getDeathPerson().getDeathPersonGender(), preferedLan),
                    DeathAlteration.GENDER, lk.rgd.common.util.CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.GENDER), preferedLan)));
            }

            //checking death country
            FieldValue fv = compareCountry(deathRegister.getDeathPerson().getDeathPersonCountry(),
                deathAlteration.getDeathPerson().getDeathPersonCountry(), preferedLan);
            if (fv != null) {
                changesList.add(fv);
            }

            fv = compareRace(deathRegister.getDeathPerson().getDeathPersonRace(),
                deathAlteration.getDeathPerson().getDeathPersonRace(), preferedLan);
            if (fv != null) {
                changesList.add(fv);
            }
        }
        logger.debug("ending generating changes list");
    }

    private void compareStringValues(String registerValue, String alterationValue, int constantValue, String preferedLan) {
        if (!(registerValue == null && alterationValue == null)) {
            boolean x = registerValue != null ? !(registerValue.equals(alterationValue)) : true;
            if (x) {
                changesList.add(new FieldValue(registerValue, alterationValue, constantValue,
                    lk.rgd.common.util.CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(constantValue), preferedLan)));
            }
        }
    }

    private FieldValue compareCountry(Country registerValue, Country alterationValue, String preferedLan) {
        logger.debug("compare country for generating death alteration changes list");
        //one case null or both null in both null no need to add ,in other case need to add with out comparison
        //not both case null
        //case 1 : death register death country is null
        final FieldValue fv = new FieldValue(null, null, DeathAlteration.COUNTRY,
            lk.rgd.common.util.CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.COUNTRY), preferedLan));
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
        if (fv.getExistingValue() == null && fv.getAlterationValue() == null) {
            return null;
        }
        logger.debug("compare country for generating changes list completed");
        return fv;
    }

    private FieldValue compareRace(Race registerValue, Race alterationValue, String preferedLan) {
        logger.debug("compare race for generating death alteration changes list");
        //one case null or both null in both null no need to add ,in other case need to add with out comparison
        //not both case null
        //case 1 : death register death country is null
        final FieldValue fv = new FieldValue(null, null, DeathAlteration.RACE,
            lk.rgd.common.util.CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.RACE), preferedLan));

        if (registerValue != null && alterationValue != null) {
            if (registerValue.getRaceId() !=
                deathAlteration.getDeathPerson().getDeathPersonRace().getRaceId()) {
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
            if (deathRegister.getDeathPerson().getDeathPersonRace() != null) {
                fv.setExistingValue(null);
                if (AppConstants.SINHALA.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getSiRaceName());

                }
                if (AppConstants.TAMIL.equals(preferedLan)) {
                    fv.setExistingValue(registerValue.getTaRaceName());
                }
            } else if (deathAlteration.getDeathPerson().getDeathPersonRace() != null) {
                if (AppConstants.SINHALA.equals(preferedLan)) {
                    fv.setAlterationValue(alterationValue.getSiRaceName());
                }
                if (AppConstants.TAMIL.equals(preferedLan)) {
                    fv.setAlterationValue(alterationValue.getTaRaceName());
                }
            }
        }
        if (fv.getExistingValue() == null && fv.getAlterationValue() == null) {
            return null;
        }
        logger.debug("complete comparing race");
        return fv;
    }

    private DeathAlteration populateAlterationObject(DeathRegister deathRegister) {
        DeathAlteration da = new DeathAlteration();
        //populate death alteration info object
        DeathAlterationInfo daf = new DeathAlterationInfo();
        daf.setCauseOfDeath(deathRegister.getDeath().getCauseOfDeath());
        daf.setCauseOfDeathEstablished(deathRegister.getDeath().isCauseOfDeathEstablished());
        daf.setDateOfDeath(deathRegister.getDeath().getDateOfDeath());
        daf.setIcdCodeOfCause(deathRegister.getDeath().getIcdCodeOfCause());
        daf.setPlaceOfBurial(deathRegister.getDeath().getPlaceOfBurial());
        daf.setPlaceOfDeath(deathRegister.getDeath().getPlaceOfDeath());
        daf.setPlaceOfDeathInEnglish(deathRegister.getDeath().getPlaceOfDeathInEnglish());
        daf.setTimeOfDeath(deathRegister.getDeath().getTimeOfDeath());
        //populate other fields
        da.setDeathRegisterIDUkey(deathRegister.getIdUKey());
        da.setDeathPersonPin(deathRegister.getDeathPerson().getDeathPersonPINorNIC());
        da.setDeathRecordDivision(deathRegister.getDeath().getDeathDivision());
        //merging objects
        da.setDeathInfo(daf);
        da.setDeathPerson(deathRegister.getDeathPerson());
        return da;
    }

    /**
     * basic list district/divisions/DS
     */
    private void populatePrimaryLists(int districtUKey, int dsDivisionId, String language, User user) {
        districtList = districtDAO.getDistrictNames(language, user);
        districtUKey = districtList.keySet().iterator().next();
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(districtUKey, language, user);
        dsDivisionId = dsDivisionList.keySet().iterator().next();
        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        logger.debug("basic lists (district list ,D.S Division list,death division list) are populated");
    }

    /**
     * other lists race/country
     */
    private void populateOtherLists() {
        raceList = raceDAO.getRaces(language);
        countryList = countryDAO.getCountries(language);
        logger.debug("other lists (race list,country list) are populated ");
    }

    //todo paginations next and previous
    /*  public String nextPage() {
    if (logger.isDebugEnabled()) {
        logger.debug("inside nextPage() : current birthDistrictId {}, birthDivisionId {}", deathAlterationId, birthDivisionId +
                " requested from pageNo " + pageNo);
    }
    setPageNo(getPageNo() + 1);

    rowNo = appParametersDAO.getIntParameter(DA_APPROVAL_ROWS_PER_PAGE);
    */

    /**
     * gets the user selected district to get the records
     * variable nextFlag is used to handle the pagination link
     * in the jsp page
     *//*
        if (birthDivisionId != 0) {
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByBDDivision(
                    bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, user);
        } else {
            birthAlterationPendingApprovalList = alterationService.getApprovalPendingByDSDivision(
                    dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        }
     //   paginationHandler(birthAlterationPendingApprovalList.size());
       // setPreviousFlag(true);
        populatePrimaryLists();
        return SUCCESS;
    }
     */
    public void setSession(Map map) {
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("setting User: {}", user.getUserName());
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
    }

    public Map getSession() {
        return session;
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

    public Map<Integer, String> getBdDivisionList() {
        return bdDivisionList;
    }

    public void setBdDivisionList(Map<Integer, String> bdDivisionList) {
        this.bdDivisionList = bdDivisionList;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public int getBirthDivisionId() {
        return birthDivisionId;
    }

    public void setBirthDivisionId(int birthDivisionId) {
        this.birthDivisionId = birthDivisionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public DeathAlteration getDeathAlteration() {
        return deathAlteration;
    }

    public void setDeathAlteration(DeathAlteration deathAlteration) {
        this.deathAlteration = deathAlteration;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public DeathRegister getDeathRegister() {
        return deathRegister;
    }

    public void setDeathRegister(DeathRegister deathRegister) {
        this.deathRegister = deathRegister;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDeathDivision() {
        return deathDivision;
    }

    public void setDeathDivision(String deathDivision) {
        this.deathDivision = deathDivision;
    }

    public String getDsDivision() {
        return dsDivision;
    }

    public void setDsDivision(String dsDivision) {
        this.dsDivision = dsDivision;
    }

    public Map<Integer, String> getRaceList() {
        return raceList;
    }

    public void setRaceList(Map<Integer, String> raceList) {
        this.raceList = raceList;
    }

    public long getAlterationSerialNo() {
        return alterationSerialNo;
    }

    public void setAlterationSerialNo(long alterationSerialNo) {
        this.alterationSerialNo = alterationSerialNo;
    }

    public Map<Integer, String> getCountryList() {
        return countryList;
    }

    public void setCountryList(Map<Integer, String> countryList) {
        this.countryList = countryList;
    }

    public long getDeathId() {
        return deathId;
    }

    public void setDeathId(long deathId) {
        this.deathId = deathId;
    }

    public int getDistrictUKey() {
        return districtUKey;
    }

    public void setDistrictUKey(int districtUKey) {
        this.districtUKey = districtUKey;
    }

    public int getDivisionUKey() {
        return divisionUKey;
    }

    public void setDivisionUKey(int divisionUKey) {
        this.divisionUKey = divisionUKey;
    }

    public Date getToDay() {
        return toDay;
    }

    public void setToDay(Date toDay) {
        this.toDay = toDay;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = WebUtils.filterBlanks(pin);
    }

    public List<DeathAlteration> getApprovalList() {
        return approvalList;
    }

    public void setApprovalList(List<DeathAlteration> approvalList) {
        this.approvalList = approvalList;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public long getDeathAlterationId() {
        return deathAlterationId;
    }

    public void setDeathAlterationId(long deathAlterationId) {
        this.deathAlterationId = deathAlterationId;
    }

    public int[] getApprovedIndex() {
        return approvedIndex;
    }

    public void setApprovedIndex(int[] approvedIndex) {
        this.approvedIndex = approvedIndex;
    }


    public int getDeathPersonCountry() {
        return deathPersonCountry;
    }

    public void setDeathPersonCountry(int deathPersonCountry) {
        this.deathPersonCountry = deathPersonCountry;
    }

    public int getDeathPersonRace() {
        return deathPersonRace;
    }

    public void setDeathPersonRace(int deathPersonRace) {
        this.deathPersonRace = deathPersonRace;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getRejectComment() {
        return rejectComment;
    }

    public void setRejectComment(String rejectComment) {
        this.rejectComment = rejectComment;
    }

    public int getDeathRaceId() {
        return deathRaceId;
    }

    public void setDeathRaceId(int deathRaceId) {
        this.deathRaceId = deathRaceId;
    }

    public int getDeathCountryId() {
        return deathCountryId;
    }

    public void setDeathCountryId(int deathCountryId) {
        this.deathCountryId = deathCountryId;
    }

    public int getLocationUKey() {
        return locationUKey;
    }

    public void setLocationUKey(int locationUKey) {
        this.locationUKey = locationUKey;
    }

    public Map<Integer, String> getUserLocations() {
        return userLocations;
    }

    public void setUserLocations(Map<Integer, String> userLocations) {
        this.userLocations = userLocations;
    }

    public boolean isEditDeathPerson() {
        return editDeathPerson;
    }

    public void setEditDeathPerson(boolean editDeathPerson) {
        this.editDeathPerson = editDeathPerson;
    }

    public boolean isEditDeathInfo() {
        return editDeathInfo;
    }

    public void setEditDeathInfo(boolean editDeathInfo) {
        this.editDeathInfo = editDeathInfo;
    }

    public boolean isApprovalPage() {
        return approvalPage;
    }

    public void setApprovalPage(boolean approvalPage) {
        this.approvalPage = approvalPage;
    }

    public List<FieldValue> getChangesList() {
        return changesList;
    }

    public void setChangesList(List<FieldValue> changesList) {
        this.changesList = changesList;
    }

}
