package lk.rgd.crs.web.action.deaths;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.AppConstants;
import lk.rgd.common.util.CommonUtil;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.crs.web.util.FieldValue;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import lk.rgd.common.api.domain.*;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.util.WebUtils;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.service.DeathAlterationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.CRSRuntimeException;


/**
 * @author amith jayasekara   action class for death alterations
 */
public class DeathAlterationAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(DeathAlterationAction.class);
    private static final String DA_APPROVAL_ROWS_PER_PAGE = "crs.br_approval_rows_per_page";

    private static final String FIELD_SUDDEN_DEATH = "";
    private static final String FIELD_DATE_DEATH = "";
    private static final String FIELD_TIME_DEATH = "";
    private static final String FIELD_PLACE_OF_DEATH = "";
    private static final String FIELD_PLACE_OF_DEATH_ENGLISH = "";
    private static final String FIELD_CAUSE_OF_DEATH_ESTABLISHED = "";
    private static final String FIELD_CAUSE_OF_DEATH = "";
    private static final String FIELD_ICD_CODE_OF_DEATH = "";
    private static final String FIELD_BURIAL_PLACE = "";
    private static final String FIELD_IDENTIFICATION_NUMBER = "";
    private static final String FIELD_COUNTRY = "";
    private static final String FIELD_PASSPORT = "";
    private static final String FIELD_AGE = "";
    private static final String FIELD_GENDER = "";
    private static final String FIELD_RACE = "";
    private static final String FIELD_NAME = "";
    private static final String FIELD_NAME_ENGLISH = "";
    private static final String FIELD_ADDRESS = "";
    private static final String FIELD_IDENTIFICATION_FATHER = "";
    private static final String FIELD_NAME_FATHER = "";
    private static final String FIELD_IDENTIFICATION_MOTHER = "";
    private static final String FIELD_NAME_MOTHER = "";

    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final RaceDAO raceDAO;
    private final CountryDAO countryDAO;
    private final AppParametersDAO appParametersDAO;

    private User user;
    private Date toDay;
    private DeathAlterationService deathAlterationService;
    private DeathRegistrationService deathRegistrationService;
    private DeathAlteration deathAlteration;
    private DeathRegister deathRegister;

    private BitSet requested;
    private BitSet approvedBitset;

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> userLocations;
    private Map<Integer, List> approvalFieldList = new HashMap<Integer, List>();

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
    private int pendingListSize;       //todo remove
    private int deathPersonCountry;
    private int deathPersonRace;
    private int deathCountryId;
    private int deathRaceId;
    private int locationUKey;

    private long certificateNumber;
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
                                 DeathRegistrationService deathRegistrationService, DistrictDAO districtDAO,
                                 DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO, RaceDAO raceDAO,
                                 CountryDAO countryDAO, AppParametersDAO appParametersDAO) {
        this.deathAlterationService = deathAlterationService;
        this.deathRegistrationService = deathRegistrationService;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.raceDAO = raceDAO;
        this.countryDAO = countryDAO;
        this.appParametersDAO = appParametersDAO;
    }

    /**
     * loading death alteration search page
     */
    public String deathAlterationSearch() {
        logger.debug("loading death alteration search page");
        populatePrimaryLists(districtUKey, dsDivisionId, language, user);
        return SUCCESS;
    }


    /**
     * capture death alterations      and edit death alteration
     */
    public String captureDeathAlterations() {
        if (!editMode) {
            logger.debug("capturing death alteration alteration");
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
                addActionMessage(getText("alt.massage.success"));
                logger.debug("capturing alteration success ");
                return SUCCESS;

            } catch (CRSRuntimeException e) {
                //todo both cases(both object null and declerant info is null gives same error massage if need two add here)
                logger.error("CRS exception while adding death alteration ");
                addActionMessage(getText("alt.massage.cannot.add.alteration.validation.failed"));
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return ERROR;
            }
/*            catch (Exception e) {
                logger.error("error accorded while adding death alteration ");
                addActionMessage(getText("unknown.error"));
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return ERROR;
            }*/
        } else {
            logger.debug("attempt to edit death alteration : idUKey : {}", deathAlteration.getIdUKey());
            try {
                DeathAlteration existing = deathAlterationService.getByIDUKey(deathAlterationId, user);
                //todo check check box
                existing.setDeathInfo(deathAlteration.getDeathInfo());
                existing.setDeathPerson(deathAlteration.getDeathPerson());
                existing.setDeclarant(deathAlteration.getDeclarant());
                deathAlterationService.updateDeathAlteration(existing, user);

                addActionMessage(getText("alt.edit.massage.success"));
                logger.debug("editing death alteration : idUKey : {} success", deathAlterationId);
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return SUCCESS;
            } catch (CRSRuntimeException e) {
                logger.debug("cannot edit death alteration");
                addActionMessage(getText("alt.massage.cannot.edit.alteration.validation.failed"));
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return ERROR;
            }
        }
    }

    /**
     * load death alteration capture page for searched death certificate
     */
    public String deathAlterationCaptureInit() {
        logger.debug("attempting to load death alteration capture page");
        deathAlteration = new DeathAlteration();
        //search by certificate number
        if (certificateNumber != 0) {
            logger.debug("attempt to load death register by certificate number : {}", certificateNumber);
            deathRegister = deathRegistrationService.getById(certificateNumber);

        }
        //search by pin
        if (pin != null && Long.parseLong(pin) != 0) {
            //only get first recode others ignored  because there can be NIC duplications
            logger.debug("attempt to load death register by pin number : {}", pin);
            List<DeathRegister> deathRegisterList = deathRegistrationService.getByPinOrNic(Long.parseLong(pin), user);
            if (deathRegisterList != null)
                deathRegister = deathRegisterList.get(0);
        }
        //search by  serial and death division
        if (serialNumber != 0 && divisionUKey != 0) {
            logger.debug("attempt to load death register by serial number : {} and death division : {}",
                serialNumber, divisionUKey);
            BDDivision deathDivision = bdDivisionDAO.getBDDivisionByPK(divisionUKey);
            deathRegister = deathRegistrationService.getByBDDivisionAndDeathSerialNo(deathDivision, serialNumber, user);
        }
        if (deathRegister == null) {
            logger.debug("can not find a death registrations for alterations :serial {} or :certificateNumber : {}",
                serialNumber, certificateNumber);
            addActionError(getText("error.cannot.find.death.registration"));
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            return ERROR;
        }
        //check is there a ongoing alteration for this certificate
        List<DeathAlteration> existingAlterations =
            deathAlterationService.getAlterationByDeathCertificateNumber(deathRegister.getIdUKey(), user);
        Iterator<DeathAlteration> itr = existingAlterations.iterator();
        while (itr.hasNext()) {
            DeathAlteration da = itr.next();
            if (!da.getStatus().equals(DeathAlteration.State.FULLY_APPROVED)) {
                logger.error("there is a ongoing alteration so cannot add a new");
                addActionError("error.existing.alterations.data.entry");
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return ERROR;
            }
        }
        //check death register is not null and in data approved state
        if (deathRegister != null) {
            if (!deathRegister.getStatus().equals(DeathRegister.State.ARCHIVED_CERT_GENERATED)) {
                logger.error("cannot capture alterations certificate is not in correct state for alteration");
                addActionError(getText("error.death.certificate.must.print.before"));
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return ERROR;
            }
            populateOtherLists();
            //setting up death district    ds and death division
            district = districtDAO.getNameByPK(deathRegister.getDeath().getDeathDistrict().getDistrictUKey(), language);
            DSDivision division = deathRegister.getDeath().getDeathDivision().getDsDivision();
            dsDivision = dsDivisionDAO.getNameByPK(division.getDsDivisionUKey(), language);
            deathDivision = bdDivisionDAO.getNameByPK(deathRegister.getDeath().getDeathDivision().getBdDivisionUKey(), language);
            Country country = deathRegister.getDeathPerson().getDeathPersonCountry();
            if (country != null) {
                deathCountryId = country.getCountryId();
            }
            Race race = deathRegister.getDeathPerson().getDeathPersonRace();
            if (race != null) {
                deathRaceId = race.getRaceId();
            }
            //todo remove use java scripts setting receiving date to today
            toDay = new Date();
        }
        return "pageLoad";
    }

    public String editDeathAlterationInit() {
        logger.debug("attempt to edit a death alteration : idUKey : {} ", deathAlterationId);
        deathAlteration = deathAlterationService.getByIDUKey(deathAlterationId, user);
        if (deathAlteration != null) {
            if (deathAlteration.getStatus().equals(DeathAlteration.State.DATA_ENTRY)) {
                //populate death person info at alteration
                deathRegister = deathRegistrationService.getById(deathAlteration.getDeathRegisterIDUkey(), user);
                //this is no use bt cannot set for null object so populate it as well
                deathRegister.setDeathPerson(deathAlteration.getDeathPerson());
                //populate death info
                deathRegister.setDeath(populateDeathInfo(deathAlteration.getDeathInfo(), deathRegister.getDeath()));
                editMode = true;
                toDay = deathAlteration.getDateReceived();
                alterationSerialNo = deathAlteration.getIdUKey();
            } else {
                logger.debug("cannot edit death alteration idUKey : {} : not in DATA_ENTRY mode", deathAlterationId);
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
    public String deathAlterationApproval() {
        pageNo = 1;
        rowNo = appParametersDAO.getIntParameter(DA_APPROVAL_ROWS_PER_PAGE);
        logger.debug("attempt to get death alteration pending list");
        if (pageNumber > 0) {
            //search by pin
            if (pin != null) {
                try {
                    approvalList = deathAlterationService.getAlterationByDeathPersonPin(pin, user);
                } catch (CRSRuntimeException e) {
                    logger.error("cannot find a death alteration for pin : {}", pin);
                    addActionError(getText("no.pending.alterations"));
                    populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                    userLocations = user.getActiveLocations(language);
                    locationUKey = 0;
                    return ERROR;
                }
            } else if (locationUKey > 0) {
                //search by user location
                approvalList = deathAlterationService.getDeathAlterationByUserLocation(locationUKey, user);
            } else {
                //search by division
                if (divisionUKey > 0) {
                    approvalList = deathAlterationService.getAlterationApprovalListByDeathDivision(pageNo, rowNo, divisionUKey, user);
                }
            }
            if (approvalList.size() < 1) {
                logger.debug("no pending list found ");
                addActionError(getText("no.pending.alterations"));
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                userLocations = user.getActiveLocations(language);
                locationUKey = 0;
                return ERROR;
            }
        }
        populatePrimaryLists(districtUKey, dsDivisionId, language, user);
        userLocations = user.getActiveLocations(language);
        locationUKey = 0;
        return SUCCESS;
    }

    public String directApprove() {
        logger.debug("approving direct death alteration : {}", deathAlterationId);

        if (deathAlterationId > 0) {
            //approving from death alteration approval list
            deathAlteration = deathAlterationService.getByIDUKey(deathAlterationId, user);
        } else {
            List<DeathAlteration> alterations = deathAlterationService.getAlterationByDeathId(deathId, user);
            Iterator<DeathAlteration> itr = alterations.iterator();
            while (itr.hasNext()) {
                DeathAlteration da = itr.next();
                if (da.getStatus().equals(DeathAlteration.State.DATA_ENTRY)) {
                    deathAlteration = da;
                    break;
                }
            }
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

            if (!(deathRegister.getDeath().isCauseOfDeathEstablished() ==
                deathAlteration.getDeathInfo().isCauseOfDeathEstablished())) {
                changesList.add(new FieldValue(CommonUtil.getYesOrNo(deathRegister.getDeath().isCauseOfDeathEstablished(), preferedLan),
                    CommonUtil.getYesOrNo(deathAlteration.getDeathInfo().isCauseOfDeathEstablished(), preferedLan),
                    DeathAlteration.CAUSE_OF_DEATH_ESTABLISHED,
                    CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.CAUSE_OF_DEATH_ESTABLISHED), preferedLan)
                ));
            }

            if (!((deathRegister.getDeath().getPlaceOfBurial() == null) || deathRegister.getDeath().getPlaceOfBurial()
                .equals(deathAlteration.getDeathInfo().getPlaceOfBurial()))) {
                changesList.add(new FieldValue(deathRegister.getDeath().getPlaceOfBurial(),
                    deathAlteration.getDeathInfo().getPlaceOfBurial(), DeathAlteration.BURIAL_PLACE,
                    CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.BURIAL_PLACE), preferedLan)));
            }

            if (!((deathRegister.getDeath().getIcdCodeOfCause() == null) || deathRegister.getDeath().getIcdCodeOfCause().
                equals(deathAlteration.getDeathInfo().getIcdCodeOfCause()))) {
                changesList.add(new FieldValue(deathRegister.getDeath().getIcdCodeOfCause(),
                    deathAlteration.getDeathInfo().getIcdCodeOfCause(), DeathAlteration.BURIAL_PLACE,
                    CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.BURIAL_PLACE), preferedLan)));
            }

            if (!((deathRegister.getDeath().getCauseOfDeath() == null) || deathRegister.getDeath().getCauseOfDeath().
                equals(deathAlteration.getDeathInfo().getCauseOfDeath()))) {
                changesList.add(new FieldValue(deathRegister.getDeath().getCauseOfDeath(),
                    deathAlteration.getDeathInfo().getCauseOfDeath(), DeathAlteration.CAUSE_OF_DEATH,
                    CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.CAUSE_OF_DEATH), preferedLan)));
            }

            if (!((deathRegister.getDeath().getPlaceOfDeathInEnglish() == null) || deathRegister.getDeath().getPlaceOfDeathInEnglish().
                equals(deathAlteration.getDeathInfo().getPlaceOfDeathInEnglish()))) {
                changesList.add(new FieldValue(deathRegister.getDeath().getCauseOfDeath(),
                    deathAlteration.getDeathInfo().getCauseOfDeath(), DeathAlteration.PLACE_OF_DEATH_ENGLISH,
                    CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.PLACE_OF_DEATH_ENGLISH), preferedLan)));
            }

            if (!((deathRegister.getDeath().getPlaceOfDeath() == null) || deathRegister.getDeath().getPlaceOfDeath().equals(deathAlteration.getDeathInfo().getPlaceOfDeath()))) {
                changesList.add(new FieldValue(deathRegister.getDeath().getPlaceOfDeath(),
                    deathAlteration.getDeathInfo().getPlaceOfDeath(),
                    DeathAlteration.PLACE_OF_DEATH, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.PLACE_OF_DEATH), preferedLan)));
            }

            if (!((deathRegister.getDeath().getTimeOfDeath() == null) || deathRegister.getDeath().getTimeOfDeath().equals(deathAlteration.getDeathInfo().getTimeOfDeath()))) {
                changesList.add(new FieldValue(deathRegister.getDeath().getTimeOfDeath(),
                    deathAlteration.getDeathInfo().getTimeOfDeath(),
                    DeathAlteration.TIME_OF_DEATH, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.TIME_OF_DEATH), preferedLan)));
            }

            if (!((dateEx == null) || dateEx.equals(dateAlt))) {  //todo compare date object
                changesList.add(new FieldValue(dateEx, dateAlt,
                    DeathAlteration.DATE_OF_DEATH, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.DATE_OF_DEATH), preferedLan)));
            }
        }

        //todo remove jsp approval
        if (deathAlteration.getDeathPerson() != null && deathRegister.getDeathPerson() != null) {

            if (!((deathRegister.getDeathPerson().getDeathPersonPINorNIC() == null) || deathRegister.getDeathPerson().getDeathPersonPINorNIC().
                equals(deathAlteration.getDeathPerson().getDeathPersonPINorNIC()))) {
                changesList.add(new FieldValue(deathRegister.getDeathPerson().getDeathPersonPINorNIC(),
                    deathAlteration.getDeathPerson().getDeathPersonPINorNIC(),
                    DeathAlteration.PIN, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.PIN), preferedLan)));
            }
            //todo check this :(
            //checking death country
            FieldValue fv = compareCountry(deathRegister, deathAlteration, preferedLan);
            if (fv != null) {
                changesList.add(fv);
            }

            if (!((deathRegister.getDeathPerson().getDeathPersonPassportNo() == null) || deathRegister.getDeathPerson().getDeathPersonPassportNo().
                equals(deathAlteration.getDeathPerson().getDeathPersonPassportNo()))) {
                changesList.add(new FieldValue(deathRegister.getDeathPerson().getDeathPersonPassportNo(),
                    deathAlteration.getDeathPerson().getDeathPersonPassportNo(),
                    DeathAlteration.PASSPORT, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.PASSPORT), preferedLan)));
            }

            if (!(deathRegister.getDeathPerson().getDeathPersonAge() ==
                deathAlteration.getDeathPerson().getDeathPersonAge())) {
                changesList.add(new FieldValue(Integer.toString(deathRegister.getDeathPerson().getDeathPersonAge()),
                    Integer.toString(deathAlteration.getDeathPerson().getDeathPersonAge()),
                    DeathAlteration.AGE, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.AGE), preferedLan)));
            }

            if (!(deathAlteration.getDeathPerson().getDeathPersonGender() ==
                deathAlteration.getDeathPerson().getDeathPersonGender())) {
                changesList.add(new FieldValue(GenderUtil.getGender(deathAlteration.getDeathPerson().getDeathPersonGender(),
                    preferedLan), GenderUtil.getGender(deathAlteration.getDeathPerson().getDeathPersonGender(), preferedLan),
                    DeathAlteration.GENDER, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.GENDER), preferedLan)));
            }

            fv = compareRace(deathRegister, deathAlteration, preferedLan);
            if (fv != null) {
                changesList.add(fv);
            }
            if (!((deathRegister.getDeathPerson().getDeathPersonNameOfficialLang() == null) || deathRegister.getDeathPerson().getDeathPersonNameOfficialLang().
                equals(deathAlteration.getDeathPerson().getDeathPersonNameOfficialLang()))) {
                changesList.add(new FieldValue(deathRegister.getDeathPerson().getDeathPersonNameOfficialLang(),
                    deathAlteration.getDeathPerson().getDeathPersonNameOfficialLang(),
                    DeathAlteration.NAME, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.NAME), preferedLan)));
            }

            if (!((deathRegister.getDeathPerson().getDeathPersonNameInEnglish() == null) || deathRegister.getDeathPerson().getDeathPersonNameInEnglish().
                equals(deathAlteration.getDeathPerson().getDeathPersonNameInEnglish()))) {
                changesList.add(new FieldValue(deathRegister.getDeathPerson().getDeathPersonNameInEnglish(),
                    deathAlteration.getDeathPerson().getDeathPersonNameInEnglish(),
                    DeathAlteration.NAME_ENGLISH, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.NAME_ENGLISH), preferedLan)));
            }

            if (!((deathRegister.getDeathPerson().getDeathPersonPermanentAddress() == null) || deathRegister.getDeathPerson().getDeathPersonPermanentAddress().
                equals(deathAlteration.getDeathPerson().getDeathPersonPermanentAddress()))) {
                changesList.add(new FieldValue(deathRegister.getDeathPerson().getDeathPersonPermanentAddress(),
                    deathAlteration.getDeathPerson().getDeathPersonPermanentAddress(),
                    DeathAlteration.ADDRESS, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.ADDRESS), preferedLan)));
            }

            if (!((deathRegister.getDeathPerson().getDeathPersonFatherPINorNIC() == null) || deathRegister.getDeathPerson().getDeathPersonFatherPINorNIC().
                equals(deathAlteration.getDeathPerson().getDeathPersonFatherPINorNIC()))) {
                changesList.add(new FieldValue(deathRegister.getDeathPerson().getDeathPersonFatherPINorNIC(),
                    deathAlteration.getDeathPerson().getDeathPersonFatherPINorNIC(),
                    DeathAlteration.PIN_FATHER, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.PIN_FATHER), preferedLan)));
            }

            if (!((deathRegister.getDeathPerson().getDeathPersonFatherFullName() == null) || deathRegister.getDeathPerson().getDeathPersonFatherFullName().
                equals(deathAlteration.getDeathPerson().getDeathPersonFatherFullName()))) {
                changesList.add(new FieldValue(deathRegister.getDeathPerson().getDeathPersonFatherFullName(),
                    deathAlteration.getDeathPerson().getDeathPersonFatherFullName(),
                    DeathAlteration.NAME_FATHER, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.NAME_FATHER), preferedLan)));
            }

            if (!((deathRegister.getDeathPerson().getDeathPersonMotherPINorNIC() == null) || deathRegister.getDeathPerson().getDeathPersonMotherPINorNIC().
                equals(deathAlteration.getDeathPerson().getDeathPersonMotherPINorNIC()))) {
                changesList.add(new FieldValue(deathRegister.getDeathPerson().getDeathPersonMotherPINorNIC(),
                    deathAlteration.getDeathPerson().getDeathPersonMotherPINorNIC(),
                    DeathAlteration.PIN_MOTHER, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.PIN_MOTHER), preferedLan)));
            }

            if (!((deathRegister.getDeathPerson().getDeathPersonMotherFullName() == null) || deathRegister.getDeathPerson().getDeathPersonMotherFullName().
                equals(deathAlteration.getDeathPerson().getDeathPersonMotherFullName()))) {
                changesList.add(new FieldValue(deathRegister.getDeathPerson().getDeathPersonMotherFullName(),
                    deathAlteration.getDeathPerson().getDeathPersonMotherFullName(),
                    DeathAlteration.NAME_MOTHER, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.NAME_MOTHER), preferedLan)));
            }

        }
        logger.debug("ending generating changes list");
    }

    private FieldValue compareCountry(DeathRegister deathRegister, DeathAlteration deathAlteration, String preferedLan) {
        //one case null or both null in both null no need to add ,in other case need to add with out comparison
        //not both case null
        //case 1 : death register death country is null
        final FieldValue fv = new FieldValue(null, null, DeathAlteration.COUNTRY, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.COUNTRY), preferedLan));
        if (deathRegister.getDeathPerson().getDeathPersonCountry() != null && deathAlteration.getDeathPerson().getDeathPersonCountry() != null && deathRegister.getDeathPerson().getDeathPersonCountry().getCountryId() ==
            deathAlteration.getDeathPerson().getDeathPersonCountry().getCountryId()) {
            if (AppConstants.SINHALA.equals(preferedLan)) {
                fv.setExistingValue(deathRegister.getDeathPerson().getDeathPersonCountry().getSiCountryName());
                fv.setAlterationValue(deathAlteration.getDeathPerson().getDeathPersonCountry().getSiCountryName());
            }
            if (AppConstants.TAMIL.equals(preferedLan)) {
                fv.setExistingValue(deathRegister.getDeathPerson().getDeathPersonCountry().getTaCountryName());
                fv.setAlterationValue(deathAlteration.getDeathPerson().getDeathPersonCountry().getTaCountryName());
            }

        } else if (deathRegister.getDeathPerson().getDeathPersonCountry() != null) {
            fv.setExistingValue(null);
            if (AppConstants.SINHALA.equals(preferedLan)) {
                fv.setAlterationValue(deathAlteration.getDeathPerson().getDeathPersonCountry().getSiCountryName());
            }
            if (AppConstants.TAMIL.equals(preferedLan)) {
                fv.setAlterationValue(deathAlteration.getDeathPerson().getDeathPersonCountry().getTaCountryName());
            }
        } else if (deathAlteration.getDeathPerson().getDeathPersonCountry() != null) {
            if (AppConstants.SINHALA.equals(preferedLan)) {
                fv.setExistingValue(deathRegister.getDeathPerson().getDeathPersonCountry().getSiCountryName());
            }

            if (AppConstants.TAMIL.equals(preferedLan)) {
                fv.setExistingValue(deathRegister.getDeathPerson().getDeathPersonCountry().getTaCountryName());
            }
        } else {
            return null;
        }

        return fv;
    }

    private FieldValue compareRace(DeathRegister deathRegister, DeathAlteration deathAlteration, String preferedLan) {
        //one case null or both null in both null no need to add ,in other case need to add with out comparison
        //not both case null
        //case 1 : death register death country is null
        final FieldValue fv = new FieldValue(null, null, DeathAlteration.RACE, CommonUtil.getYesOrNo(deathAlteration.getApprovalStatuses().get(DeathAlteration.RACE), preferedLan));
        if (deathRegister.getDeathPerson().getDeathPersonRace() != null && deathAlteration.getDeathPerson().getDeathPersonRace() != null && deathRegister.getDeathPerson().getDeathPersonRace().getRaceId() ==
            deathAlteration.getDeathPerson().getDeathPersonRace().getRaceId()) {
            if (AppConstants.SINHALA.equals(preferedLan)) {
                fv.setExistingValue(deathRegister.getDeathPerson().getDeathPersonRace().getSiRaceName());
                fv.setAlterationValue(deathAlteration.getDeathPerson().getDeathPersonRace().getSiRaceName());
            }
            if (AppConstants.TAMIL.equals(preferedLan)) {
                fv.setExistingValue(deathRegister.getDeathPerson().getDeathPersonRace().getTaRaceName());
                fv.setAlterationValue(deathAlteration.getDeathPerson().getDeathPersonRace().getTaRaceName());
            }

        } else if (deathRegister.getDeathPerson().getDeathPersonRace() != null) {
            fv.setExistingValue(null);
            if (AppConstants.SINHALA.equals(preferedLan)) {
                fv.setAlterationValue(deathAlteration.getDeathPerson().getDeathPersonRace().getSiRaceName());
            }
            if (AppConstants.TAMIL.equals(preferedLan)) {
                fv.setAlterationValue(deathAlteration.getDeathPerson().getDeathPersonRace().getTaRaceName());
            }
        } else if (deathAlteration.getDeathPerson().getDeathPersonRace() != null) {
            if (AppConstants.SINHALA.equals(preferedLan)) {
                fv.setExistingValue(deathRegister.getDeathPerson().getDeathPersonRace().getSiRaceName());
            }

            if (AppConstants.TAMIL.equals(preferedLan)) {
                fv.setExistingValue(deathRegister.getDeathPerson().getDeathPersonRace().getTaRaceName());
            }
        } else {
            return null;
        }
        return fv;
    }


    public String setBitset() {
        logger.info("setting bit set : {}", approvedIndex);
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
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            logger.debug("apply changes to death alteration : alteration id  {}", deathAlterationId);
        }
        catch (CRSRuntimeException e) {
            logger.error("cannot set bit set for death alteration : {}", deathAlterationId);
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
        if (pageNumber > 0) {
            logger.debug("attempt to reject death alteration : idUKey : {} by User : {}", deathAlterationId, user.getUserName());
            deathAlterationService.rejectDeathAlteration(deathAlterationId, user, rejectComment);
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            logger.debug("death alteration rejected success : alteration id :{}", deathAlterationId);
            return SUCCESS;
        }
        deathAlteration = deathAlterationService.getByIDUKey(deathAlterationId, user);
        return "pageLoad";
    }

    /**
     * printing confirmation letter to declarent who requested changes
     */
    public String printAlterationLetter() {
        //loading requested alterations
        deathAlteration = deathAlterationService.getByIDUKey(deathAlterationId, user);
        deathRegister = deathRegistrationService.getById(deathAlteration.getDeathRegisterIDUkey());
        requested = deathAlteration.getRequestedAlterations();
        approvedBitset = deathAlteration.getApprovalStatuses();
        return "pageLoad";
    }

    private DeathInfo populateDeathInfo(DeathAlterationInfo dai, DeathInfo di) {

        di.setDateOfDeath(dai.getDateOfDeath());
        di.setCauseOfDeath(dai.getCauseOfDeath());
        di.setIcdCodeOfCause(dai.getIcdCodeOfCause());
        di.setPlaceOfBurial(dai.getPlaceOfBurial());
        di.setPlaceOfDeath(dai.getPlaceOfDeath());
        di.setPlaceOfDeathInEnglish(dai.getPlaceOfDeathInEnglish());
        di.setTimeOfDeath(dai.getTimeOfDeath());
        di.setCauseOfDeathEstablished(dai.isCauseOfDeathEstablished());

        return di;
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
        logger.debug("basic lists are populated");
    }

    /**
     * other lists race/country
     */
    private void populateOtherLists() {
        raceList = raceDAO.getRaces(language);
        countryList = countryDAO.getCountries(language);
        logger.debug("other lists are populated ");
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

    public long getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(long certificateNumber) {
        this.certificateNumber = certificateNumber;
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

    public int getPendingListSize() {
        return pendingListSize;
    }

    public void setPendingListSize(int pendingListSize) {
        this.pendingListSize = pendingListSize;
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

    public Map<Integer, List> getApprovalFieldList() {
        return approvalFieldList;
    }

    public void setApprovalFieldList(Map<Integer, List> approvalFieldList) {
        this.approvalFieldList = approvalFieldList;
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

    public BitSet getRequested() {
        return requested;
    }

    public void setRequested(BitSet requested) {
        this.requested = requested;
    }

    public BitSet getApprovedBitset() {
        return approvedBitset;
    }

    public void setApprovedBitset(BitSet approvedBitset) {
        this.approvedBitset = approvedBitset;
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
