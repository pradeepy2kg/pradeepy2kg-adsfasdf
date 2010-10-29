package lk.rgd.crs.web.action.deaths;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.AppConstants;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.common.util.GenderUtil;
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

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> userLocations;
    private Map<Integer, List> approvalFieldList = new HashMap<Integer, List>();

    private List<DeathAlteration> approvalList;

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

    public enum Type {

        STRING,//0
        INTEGER,//1
        LONG,//2
        BOOLEAN,//3
        COUNTRY,//4
        DATE, //5
        RACE //6
    }


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
        if (!((deathAlteration.getStatus().equals(DeathAlteration.State.DATA_ENTRY)) |
                (deathAlteration.getStatus().equals(DeathAlteration.State.PARTIALY_APPROVED)))) {
            logger.error("cannot approve death alteration : id {} :invalid alteration state", deathAlteration.getIdUKey());
            addActionError(getText("cannot.approve.nt.in.correct.state"));
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            return ERROR;
        }
        //todo check sudden death   gender is print with 0 and 1 convert them 
        /**
         * getting existing death recode and compare it with death alteration
         */
        deathRegister = deathRegistrationService.getById(deathAlteration.getDeathRegisterIDUkey(), user);

        String dateEx = null;
        String dateAlt = null;
        if (deathRegister.getDeath().getDateOfDeath() != null) {
            dateEx = DateTimeUtils.getISO8601FormattedString(deathRegister.getDeath().getDateOfDeath());
        }
        if (deathAlteration.getDeathInfo().getDateOfDeath() != null) {
            dateAlt = DateTimeUtils.getISO8601FormattedString(deathAlteration.getDeathInfo().getDateOfDeath());
        }

        if (deathAlteration.getDeathInfo() != null) {
            getDisplayList(DeathAlteration.DATE_OF_DEATH, dateEx, dateAlt, Type.STRING.ordinal());
            getDisplayList(DeathAlteration.TIME_OF_DEATH, deathRegister.getDeath().getTimeOfDeath(),
                    deathAlteration.getDeathInfo().getTimeOfDeath(), Type.STRING.ordinal());
            getDisplayList(DeathAlteration.PLACE_OF_DEATH, deathRegister.getDeath().getPlaceOfDeath(),
                    deathAlteration.getDeathInfo().getPlaceOfDeath(), Type.STRING.ordinal());
            getDisplayList(DeathAlteration.PLACE_OF_DEATH_ENGLISH, deathRegister.getDeath().getPlaceOfDeathInEnglish(),
                    deathAlteration.getDeathInfo().getPlaceOfDeathInEnglish(), Type.STRING.ordinal());
            getDisplayList(DeathAlteration.CAUSE_OF_DEATH_ESTABLISHED, deathRegister.getDeath().isCauseOfDeathEstablished(),
                    deathAlteration.getDeathInfo().isCauseOfDeathEstablished(), Type.BOOLEAN.ordinal());
            getDisplayList(DeathAlteration.CAUSE_OF_DEATH, deathRegister.getDeath().getCauseOfDeath(),
                    deathAlteration.getDeathInfo().getCauseOfDeath(), Type.STRING.ordinal());
            getDisplayList(DeathAlteration.ICD_CODE, deathRegister.getDeath().getIcdCodeOfCause(),
                    deathAlteration.getDeathInfo().getIcdCodeOfCause(), Type.STRING.ordinal());
            getDisplayList(DeathAlteration.BURIAL_PLACE, deathRegister.getDeath().getPlaceOfBurial(),
                    deathAlteration.getDeathInfo().getPlaceOfBurial(), Type.STRING.ordinal());
        }
        if (deathAlteration.getDeathPerson() != null) {
            getDisplayList(DeathAlteration.COUNTRY, deathRegister.getDeathPerson().getDeathPersonCountry(),
                    deathAlteration.getDeathPerson().getDeathPersonCountry(), Type.COUNTRY.ordinal());
            getDisplayList(DeathAlteration.PASSPORT, deathRegister.getDeathPerson().getDeathPersonPassportNo(),
                    deathAlteration.getDeathPerson().getDeathPersonPassportNo(), Type.STRING.ordinal());
            getDisplayList(DeathAlteration.AGE, deathRegister.getDeathPerson().getDeathPersonAge(),
                    deathAlteration.getDeathPerson().getDeathPersonAge(), Type.INTEGER.ordinal());
            getDisplayList(DeathAlteration.GENDER, deathRegister.getDeathPerson().getDeathPersonGender(),
                    deathAlteration.getDeathPerson().getDeathPersonGender(), Type.INTEGER.ordinal());
            getDisplayList(DeathAlteration.RACE, deathRegister.getDeathPerson().getDeathPersonRace(),
                    deathAlteration.getDeathPerson().getDeathPersonRace(), Type.RACE.ordinal());
            getDisplayList(DeathAlteration.NAME, deathRegister.getDeathPerson().getDeathPersonNameOfficialLang(),
                    deathAlteration.getDeathPerson().getDeathPersonNameOfficialLang(), Type.STRING.ordinal());
            getDisplayList(DeathAlteration.NAME_ENGLISH, deathRegister.getDeathPerson().getDeathPersonNameInEnglish(),
                    deathAlteration.getDeathPerson().getDeathPersonNameInEnglish(), Type.STRING.ordinal());
            getDisplayList(DeathAlteration.ADDRESS, deathRegister.getDeathPerson().getDeathPersonPermanentAddress(),
                    deathAlteration.getDeathPerson().getDeathPersonPermanentAddress(), Type.STRING.ordinal());
            getDisplayList(DeathAlteration.PIN_FATHER, deathRegister.getDeathPerson().getDeathPersonFatherPINorNIC(),
                    deathAlteration.getDeathPerson().getDeathPersonFatherPINorNIC(), Type.STRING.ordinal());
            getDisplayList(DeathAlteration.NAME_FATHER, deathRegister.getDeathPerson().getDeathPersonFatherFullName(),
                    deathAlteration.getDeathPerson().getDeathPersonFatherFullName(), Type.STRING.ordinal());
            getDisplayList(DeathAlteration.PIN_MOTHER, deathRegister.getDeathPerson().getDeathPersonMotherPINorNIC(),
                    deathAlteration.getDeathPerson().getDeathPersonMotherPINorNIC(), Type.STRING.ordinal());
            getDisplayList(DeathAlteration.NAME_MOTHER, deathRegister.getDeathPerson().getDeathPersonMotherFullName(),
                    deathAlteration.getDeathPerson().getDeathPersonMotherFullName(), Type.STRING.ordinal());
        }

        //applying requested bit set
        if (deathAlteration.getRequestedAlterations().size() == 0) {
            //this mean first time comparison
            BitSet requested = deathAlteration.getRequestedAlterations();
            for (Map.Entry e : approvalFieldList.entrySet()) {
                requested.set((Integer) e.getKey(), true);
            }
            deathAlteration.setRequestedAlterations(requested);
            //updating death alteration
            deathAlterationService.updateDeathAlteration(deathAlteration, user);
        }
        populatePrimaryLists(districtUKey, dsDivisionId, language, user);
        return SUCCESS;
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
        BitSet requested = deathAlteration.getRequestedAlterations();
        BitSet approvedBitset = deathAlteration.getApprovalStatuses();
        List values = null;

/*todo sudden death
   if (requested.get(DeathAlteration.SUDDEN_DEATH)) {
        approvalFieldList.put(DeathAlteration.indexMap.get(DeathAlteration.SUDDEN_DEATH),deathRegister.getDeathType(),deathAlteration.get)
        }*/
        if (requested.get(DeathAlteration.DATE_OF_DEATH)) {
            values = new LinkedList();
            String dateEx = null;
            String dateAlt = null;
            if (deathRegister.getDeath().getDateOfDeath() != null)
                dateEx = DateTimeUtils.getISO8601FormattedString(deathRegister.getDeath().getDateOfDeath());
            if (deathAlteration.getDeathInfo().getDateOfDeath() != null)
                dateAlt = DateTimeUtils.getISO8601FormattedString(deathAlteration.getDeathInfo().getDateOfDeath());
            values.add(0, dateEx);
            values.add(1, dateAlt);
            values.add(2, approvedBitset.get(DeathAlteration.DATE_OF_DEATH));
            approvalFieldList.put(DeathAlteration.DATE_OF_DEATH, values);
        }

        if (requested.get(DeathAlteration.TIME_OF_DEATH)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeath().getTimeOfDeath());
            values.add(1, deathAlteration.getDeathInfo().getTimeOfDeath());
            values.add(2, approvedBitset.get(DeathAlteration.TIME_OF_DEATH));
            approvalFieldList.put(DeathAlteration.TIME_OF_DEATH, values);
        }

        if (requested.get(DeathAlteration.PLACE_OF_DEATH)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeath().getPlaceOfDeath());
            values.add(1, deathAlteration.getDeathInfo().getPlaceOfDeath());
            values.add(2, approvedBitset.get(DeathAlteration.PLACE_OF_DEATH));
            approvalFieldList.put(DeathAlteration.PLACE_OF_DEATH, values);
        }

        if (requested.get(DeathAlteration.PLACE_OF_DEATH_ENGLISH)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeath().getPlaceOfDeathInEnglish());
            values.add(1, deathAlteration.getDeathInfo().getPlaceOfDeathInEnglish());
            values.add(2, approvedBitset.get(DeathAlteration.PLACE_OF_DEATH_ENGLISH));
            approvalFieldList.put(DeathAlteration.PLACE_OF_DEATH_ENGLISH, values);
        }

        if (requested.get(DeathAlteration.CAUSE_OF_DEATH_ESTABLISHED)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeath().isCauseOfDeathEstablished());
            values.add(1, deathAlteration.getDeathInfo().isCauseOfDeathEstablished());
            values.add(2, approvedBitset.get(DeathAlteration.CAUSE_OF_DEATH_ESTABLISHED));
            approvalFieldList.put(DeathAlteration.CAUSE_OF_DEATH_ESTABLISHED, values);
        }

        if (requested.get(DeathAlteration.CAUSE_OF_DEATH)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeath().getCauseOfDeath());
            values.add(1, deathAlteration.getDeathInfo().getCauseOfDeath());
            values.add(2, approvedBitset.get(DeathAlteration.CAUSE_OF_DEATH));
            approvalFieldList.put(DeathAlteration.CAUSE_OF_DEATH, values);
        }

        if (requested.get(DeathAlteration.ICD_CODE)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeath().getIcdCodeOfCause());
            values.add(1, deathAlteration.getDeathInfo().getIcdCodeOfCause());
            values.add(2, approvedBitset.get(DeathAlteration.ICD_CODE));
            approvalFieldList.put(DeathAlteration.ICD_CODE, values);
        }

        if (requested.get(DeathAlteration.BURIAL_PLACE)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeath().getPlaceOfBurial());
            values.add(1, deathAlteration.getDeathInfo().getPlaceOfBurial());
            values.add(2, approvedBitset.get(DeathAlteration.BURIAL_PLACE));
            approvalFieldList.put(DeathAlteration.BURIAL_PLACE, values);
        }

        if (requested.get(DeathAlteration.PIN)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeathPerson().getDeathPersonPINorNIC());
            values.add(1, deathAlteration.getDeathPerson().getDeathPersonPINorNIC());
            values.add(2, approvedBitset.get(DeathAlteration.PIN));
            approvalFieldList.put(DeathAlteration.PIN, values);
        }

        if (requested.get(DeathAlteration.COUNTRY)) {
            values = new LinkedList();
            Country existingCountry = deathRegister.getDeathPerson().getDeathPersonCountry();
            Country alteredCountry = deathAlteration.getDeathPerson().getDeathPersonCountry();
            String existingCountryName = null;
            String alteredCountryName = null;

            if (deathRegister.getDeath().getPreferredLanguage().equals(AppConstants.SINHALA)) {
                if (existingCountry != null) {
                    existingCountryName = deathRegister.getDeathPerson().getDeathPersonCountry().getSiCountryName();
                }
                if (alteredCountry != null) {
                    alteredCountryName = deathAlteration.getDeathPerson().getDeathPersonCountry().getSiCountryName();
                }
            }
            if (deathRegister.getDeath().getPreferredLanguage().equals(AppConstants.TAMIL)) {
                if (existingCountry != null) {
                    existingCountryName = deathRegister.getDeathPerson().getDeathPersonCountry().getTaCountryName();
                }
                if (alteredCountry != null) {
                    alteredCountryName = deathAlteration.getDeathPerson().getDeathPersonCountry().getTaCountryName();
                }
            }
            values.add(0, existingCountryName);
            values.add(1, alteredCountryName);
            values.add(2, approvedBitset.get(DeathAlteration.COUNTRY));
            approvalFieldList.put(DeathAlteration.COUNTRY, values);
        }

        if (requested.get(DeathAlteration.PASSPORT)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeathPerson().getDeathPersonPassportNo());
            values.add(1, deathAlteration.getDeathPerson().getDeathPersonPassportNo());
            values.add(2, approvedBitset.get(DeathAlteration.PASSPORT));
            approvalFieldList.put(DeathAlteration.PASSPORT, values);
        }

        if (requested.get(DeathAlteration.AGE)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeathPerson().getDeathPersonAge());
            values.add(1, deathAlteration.getDeathPerson().getDeathPersonAge());
            values.add(2, approvedBitset.get(DeathAlteration.AGE));
            approvalFieldList.put(DeathAlteration.AGE, values);
        }

        if (requested.get(DeathAlteration.GENDER)) {
            values = new LinkedList();
            values.add(0, GenderUtil.getGender(deathRegister.getDeathPerson().getDeathPersonGender(),
                    deathRegister.getDeath().getPreferredLanguage()));
            values.add(1, GenderUtil.getGender(deathAlteration.getDeathPerson().getDeathPersonGender(),
                    deathRegister.getDeath().getPreferredLanguage()));
            values.add(2, approvedBitset.get(DeathAlteration.GENDER));
            approvalFieldList.put(DeathAlteration.GENDER, values);
        }

        if (requested.get(DeathAlteration.RACE)) {
            values = new LinkedList();
            Race existingRace = deathRegister.getDeathPerson().getDeathPersonRace();
            Race alteredRace = deathAlteration.getDeathPerson().getDeathPersonRace();
            String existingRaceName = null;
            String alteredRaceName = null;
            if (deathRegister.getDeath().getPreferredLanguage().equals(AppConstants.SINHALA)) {
                if (existingRace != null) {
                    existingRaceName = deathRegister.getDeathPerson().getDeathPersonRace().getSiRaceName();
                }
                if (alteredRace != null) {
                    alteredRaceName = deathAlteration.getDeathPerson().getDeathPersonRace().getSiRaceName();
                }
            }
            if (deathRegister.getDeath().getPreferredLanguage().equals(AppConstants.TAMIL)) {
                if (existingRace != null) {
                    existingRaceName = deathRegister.getDeathPerson().getDeathPersonRace().getTaRaceName();
                }
                if (alteredRace != null) {
                    alteredRaceName = deathAlteration.getDeathPerson().getDeathPersonRace().getTaRaceName();
                }
            }
            values.add(0, existingRaceName);
            values.add(1, alteredRaceName);
            values.add(2, approvedBitset.get(DeathAlteration.AGE));
            approvalFieldList.put(DeathAlteration.AGE, values);
        }

        if (requested.get(DeathAlteration.NAME)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeathPerson().getDeathPersonNameOfficialLang());
            values.add(1, deathAlteration.getDeathPerson().getDeathPersonNameOfficialLang());
            values.add(2, approvedBitset.get(DeathAlteration.NAME));
            approvalFieldList.put(DeathAlteration.NAME, values);
        }

        if (requested.get(DeathAlteration.NAME_ENGLISH)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeathPerson().getDeathPersonNameInEnglish());
            values.add(1, deathAlteration.getDeathPerson().getDeathPersonNameInEnglish());
            values.add(2, approvedBitset.get(DeathAlteration.NAME_ENGLISH));
            approvalFieldList.put(DeathAlteration.NAME_ENGLISH, values);
        }

        if (requested.get(DeathAlteration.ADDRESS)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeathPerson().getDeathPersonPermanentAddress());
            values.add(1, deathAlteration.getDeathPerson().getDeathPersonPermanentAddress());
            values.add(2, approvedBitset.get(DeathAlteration.ADDRESS));
            approvalFieldList.put(DeathAlteration.ADDRESS, values);
        }

        if (requested.get(DeathAlteration.PIN_FATHER)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeathPerson().getDeathPersonFatherPINorNIC());
            values.add(1, deathAlteration.getDeathPerson().getDeathPersonFatherPINorNIC());
            values.add(2, approvedBitset.get(DeathAlteration.PIN_FATHER));
            approvalFieldList.put(DeathAlteration.PIN_FATHER, values);
        }

        if (requested.get(DeathAlteration.NAME_FATHER)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeathPerson().getDeathPersonFatherFullName());
            values.add(1, deathAlteration.getDeathPerson().getDeathPersonFatherFullName());
            values.add(2, approvedBitset.get(DeathAlteration.NAME_FATHER));
            approvalFieldList.put(DeathAlteration.NAME_FATHER, values);
        }

        if (requested.get(DeathAlteration.PIN_MOTHER)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeathPerson().getDeathPersonMotherPINorNIC());
            values.add(1, deathAlteration.getDeathPerson().getDeathPersonMotherPINorNIC());
            values.add(2, approvedBitset.get(DeathAlteration.PIN_MOTHER));
            approvalFieldList.put(DeathAlteration.PIN_MOTHER, values);
        }

        if (requested.get(DeathAlteration.NAME_MOTHER)) {
            values = new LinkedList();
            values.add(0, deathRegister.getDeathPerson().getDeathPersonMotherFullName());
            values.add(1, deathAlteration.getDeathPerson().getDeathPersonMotherFullName());
            values.add(2, approvedBitset.get(DeathAlteration.NAME_MOTHER));
            approvalFieldList.put(DeathAlteration.NAME_MOTHER, values);
        }
        return "pageLoad";
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


    private void getDisplayList(int index, Object deathRegisterValue, Object deathAlterationValue, int type) {
        List stringList = new ArrayList();
        stringList.add(0, deathRegisterValue);
        stringList.add(1, deathAlterationValue);
        switch (type) {
            case 0:
                if (deathAlterationValue != null & deathRegisterValue != null) {
                    if (compareStiring((String) deathRegisterValue, (String) deathAlterationValue)) {
                        approvalFieldList.put(index, stringList);
                    }
                } else {
                    if (!((deathAlterationValue == null || deathAlterationValue.toString().length() == 0)
                            & (deathRegisterValue == null || deathRegisterValue.toString().length() == 0)))
                        approvalFieldList.put(index, stringList);
                }
                break;
            case 1:
                if (deathAlterationValue != null & deathRegisterValue != null) {
                    if (compareInteger((Integer) deathRegisterValue, (Integer) deathAlterationValue)) {
                        approvalFieldList.put(index, stringList);
                    }
                } else {
                    if (!(deathAlterationValue == null & deathRegisterValue == null))
                        approvalFieldList.put(index, stringList);
                }
                break;
            case 2:
                if (deathAlterationValue != null & deathRegisterValue != null) {
                    if (compareLong((Long) deathRegisterValue, (Long) deathAlterationValue)) {
                        approvalFieldList.put(index, stringList);
                    }
                } else {
                    if (!(deathAlterationValue == null & deathRegisterValue == null))
                        approvalFieldList.put(index, stringList);
                }
                break;
            case 3:
                if (deathAlterationValue != null & deathRegisterValue != null) {
                    if (compareBoolean((Boolean) deathRegisterValue, (Boolean) deathAlterationValue)) {
                        approvalFieldList.put(index, stringList);
                    }
                } else {
                    if (!(deathAlterationValue == null & deathRegisterValue == null))
                        approvalFieldList.put(index, stringList);
                }
                break;
            case 4:
                List<String> countryList = new ArrayList<String>();
                Country existing = (Country) deathRegisterValue;
                Country current = (Country) deathAlterationValue;
                if (existing != null & current != null) {
                    countryList.add(0, existing.getEnCountryName());
                    countryList.add(1, current.getEnCountryName());
                    if (compareCountry(existing, current)) {
                        approvalFieldList.put(index, countryList);
                    }
                } else {
                    if (!(existing == null & current == null)) {
                        if (existing != null) {
                            countryList.add(0, existing.getEnCountryName());
                            countryList.add(1, null);
                        }
                        if (current != null) {
                            countryList.add(0, null);
                            countryList.add(1, current.getEnCountryName());
                        }
                        approvalFieldList.put(index, countryList);
                    }
                }
                break;
            case 6:
                List<String> raceList = new ArrayList<String>();
                Race exRace = (Race) deathRegisterValue;
                Race cuRace = (Race) deathAlterationValue;
                if (exRace != null & cuRace != null) {
                    raceList.add(0, exRace.getEnRaceName());
                    raceList.add(1, cuRace.getEnRaceName());
                    if (compareRaces((Race) deathRegisterValue, (Race) deathAlterationValue)) {
                        approvalFieldList.put(index, raceList);
                    }
                } else {
                    if (!(deathAlterationValue == null & deathRegisterValue == null)) {
                        if (exRace != null) {
                            raceList.add(0, exRace.getEnRaceName());
                            raceList.add(1, null);
                        }
                        if (cuRace != null) {
                            raceList.add(0, null);
                            raceList.add(1, cuRace.getEnRaceName()); //due to bug array index out of bound cause by adding to 1 position when 0 position is empty
                        }
                        approvalFieldList.put(index, raceList);
                    }
                }
                break;
            default:
        }
    }

    private boolean compareStiring(String exsisting, String current) {
        if (current != null) {
            int value = exsisting.compareTo(current.trim());
            if (value == 0) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private boolean compareInteger(Integer ex, Integer cu) {
        if (ex.compareTo(cu) == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean compareBoolean(Boolean ex, Boolean cu) {
        if (ex.compareTo(cu) == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean compareLong(Long ex, Long cu) {
        if (ex.compareTo(cu) == 0)
            return false;
        else {
            return true;
        }
    }

    private boolean compareCountry(Country ex, Country cu) {
        if (ex.getCountryId() == cu.getCountryId())
            return false;
        return true;
    }

    private boolean compareRaces(Race ex, Race cu) {
        if (ex.getRaceId() == cu.getRaceId())
            return false;
        return true;
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
}
