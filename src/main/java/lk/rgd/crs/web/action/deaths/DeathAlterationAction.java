package lk.rgd.crs.web.action.deaths;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import lk.rgd.common.api.domain.*;
import lk.rgd.common.api.dao.*;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.service.DeathAlterationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.dao.BDDivisionDAO;


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
    private Date startDate;
    private Date endDate;
    private DeathAlterationService deathAlterationService;
    private DeathRegistrationService deathRegistrationService;
    private DeathAlteration deathAlteration;
    private DeathRegister deathRegister;
    private District deathDistrict;

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> countryList;
    private Map<Integer, List<String>> pendingList = new HashMap<Integer, List<String>>();

    private List<DeathAlteration> approvalList;

    private int[] approvedIndex;

    private int dsDivisionId;
    private int birthDivisionId;
    private int pageNumber; //use to track alteration capture page load and add or edit
    private int districtUKey;
    private int divisionUKey;
    private int pageNo;  //to capture data table paginatins
    private int rowNo;
    private int pendingListSize;
    private int deathPersonCountry;
    private int deathPersonRace;

    private long certificateNumber;
    private long serialNumber;
    private long alterationSerialNo;
    private long deathId;
    private long pin;
    private long deathAlterationId;

    private String language;
    private String district;
    private String dsDivision;
    private String deathDivision;
    private String rejectComment;

    private boolean editDeathInfo;     //todo remove follow two variables no usage
    private boolean editDeathPerson;
    private boolean editMode;

    public DeathAlterationAction(DeathAlterationService deathAlterationService, DeathRegistrationService deathRegistrationService
            , DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO, RaceDAO raceDAO, CountryDAO countryDAO, AppParametersDAO appParametersDAO) {
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
     * loding death alteration search page
     */
    public String deathAlterationSearch() {
        logger.debug("loading death alteration search page");
        populatePrimaryLists(districtUKey, dsDivisionId, language, user);
        return SUCCESS;
    }

    //todo check already addedd data before add
    /**
     * loading death alteration page and capture death alterations
     */
    public String captureDeathAlterations() {
        if (pageNumber > 0) {
            if (!editMode) {
                logger.debug("capturing death alteration alteration serial number : {}", alterationSerialNo);
                try {
                    DeathRegister dr = deathRegistrationService.getById(deathId, user);

                    deathAlteration.setAlterationSerialNo(alterationSerialNo);
                    deathAlteration.setDeathId(deathId);
                    deathAlteration.setDeclarant(deathRegister.getDeclarant());
                    deathAlteration.setDeathPerson(deathRegister.getDeathPerson());
                    deathAlteration.setStatus(DeathAlteration.State.DATA_ENTRY);
                    deathAlteration.setDeathDivision(dr.getDeath().getDeathDivision());
                    deathAlterationService.addDeathAlteration(deathAlteration, user);

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

                    addActionMessage(getText("alt.massage.success"));
                    populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                    logger.debug("capturing alteration serial number : {} success ", serialNumber);
                    return SUCCESS;
                }
                catch (Exception e) {
                    logger.error("error accoured while adding death alteration : serial number : {}", serialNumber);
                    populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                    return ERROR;
                }
            } else {
                logger.debug("attempt to edit death alteration : idUKey : {}", deathAlteration.getIdUKey());
                DeathAlteration exsisting = deathAlterationService.getById(deathAlterationId, user);

                deathAlteration.setDeclarant(deathRegister.getDeclarant());
                deathAlteration.setDeathPerson(deathRegister.getDeathPerson());
                deathAlteration.setIdUKey(deathAlterationId);
                deathAlteration.setDeathId(deathId);
                deathAlteration.setStatus(DeathAlteration.State.DATA_ENTRY);
                deathAlteration.setAlterationSerialNo(alterationSerialNo);
                deathAlteration.setLifeCycleInfo(exsisting.getLifeCycleInfo());

                deathAlterationService.updateDeathAlteration(deathAlteration, user);

                addActionMessage(getText("alt.edit.massage.success"));
                logger.debug("editing death alteration : idUKey : {} success", deathAlterationId);
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return SUCCESS;
            }
        } else {
            logger.debug("attempting to load death alteration capture page");
            deathAlteration = new DeathAlteration();
            //search by certificate number
            if (certificateNumber != 0)
                deathRegister = deathRegistrationService.getById(certificateNumber, user);
            //search by pin
            if (pin != 0) {
                //only get firts recode others ignored  because there can be NIC duplications
                List<DeathRegister> deathAltList = deathRegistrationService.getByPinOrNic(pin, user);
                if (deathAltList != null)
                    deathRegister = deathAltList.get(0);
            }
            //search by  serial and death division
            if (serialNumber != 0 && divisionUKey != 0) {
                BDDivision deathDivision = bdDivisionDAO.getBDDivisionByPK(divisionUKey);
                deathRegister = deathRegistrationService.getByBDDivisionAndDeathSerialNo(deathDivision, serialNumber, user);
            }
            if (deathRegister == null) {
                logger.debug("can not find a death registretion for alterations : serial {} or :certificatenumber : {}", serialNumber, certificateNumber);
                addActionError(getText("error.cannot.find.death.registration"));
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return ERROR;
            }
            //check is there a ongoing alteration for this cartificate
            List<DeathAlteration> exsistingAlterations = deathAlterationService.getAlterationByDeathCertificateNumber(deathRegister.getIdUKey(), user);
            Iterator<DeathAlteration> itr = exsistingAlterations.iterator();
            while (itr.hasNext()) {
                DeathAlteration da = itr.next();
                if (da.getStatus().equals(DeathAlteration.State.DATA_ENTRY)) {
                    logger.error("exsisting recode with DATA_ENTRY mode found");
                    addActionError("error.exsisting.alteratios.data.entry");
                    populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                    return ERROR;
                }
            }
            //check death register is not null and in data approved state
            if (deathRegister != null) {
                if (!deathRegister.getStatus().equals(DeathRegister.State.DEATH_CERTIFICATE_PRINTED)) {
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
                //setting reciving date to today
                toDay = new Date();
            } else {
                logger.error("unknown error");
                addActionError(getText("error.unknown"));
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return ERROR;
            }
        }
        return "pageload";
    }

    /**
     * searching death alterations for approvals/rejection/delete and edit
     */
    public String deathAlterationApproval() {
        logger.debug("attemp to get death alteration pending list");
        if (pageNumber > 0) {
            pageNo = 1;
            rowNo = appParametersDAO.getIntParameter(DA_APPROVAL_ROWS_PER_PAGE);

            //search by date frame
            if (startDate != null & endDate != null) {
                approvalList = deathAlterationService.getDeathAlterationByTimePeriod(startDate, endDate, user);
            } else {
                if (!(startDate == null & endDate == null)) {
                    addActionError(getText("invalide.searching.schema.data.enter.both.dates"));
                    startDate = null;
                    endDate = null;
                    populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                    return ERROR;
                }
            }
            //search by division
            if (divisionUKey > 0) {
                approvalList = deathAlterationService.getAlterationApprovalListByDeathDivision(pageNo, rowNo, divisionUKey);
            }
            if (approvalList.size() < 1) {
                logger.debug("no pending list found ");
                addActionError(getText("no.pending.alterations"));
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return ERROR;
            }
        }
        populatePrimaryLists(districtUKey, dsDivisionId, language, user);
        return SUCCESS;
    }

    public String directApprove() {
        logger.debug("approving direct death alteration : {}", deathAlterationId);

        if (deathAlterationId > 0) {
            //approving from deaht alteration approval list
            deathAlteration = deathAlterationService.getById(deathAlterationId, user);
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
        if (!((deathAlteration.getStatus().equals(DeathAlteration.State.DATA_ENTRY)) | (deathAlteration.getStatus().equals(DeathAlteration.State.PARTIALY_APPROVED)))) {
            logger.error("cannot approve death alteration : id {} :in valide alteration state", deathAlteration.getIdUKey());
            addActionError(getText("cannot.approve.nt.in.correct.state"));
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            return ERROR;
        }
        //todo check sudden death
        /**
         * getting exsisting death recode and compare it with death alteration
         */
        deathRegister = deathRegistrationService.getById(deathAlteration.getDeathId(), user);

        DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        String dateEx = null;
        String dateAlt = null;
        if (deathRegister.getDeath().getDateOfDeath() != null)
            dateEx = df.format(deathRegister.getDeath().getDateOfDeath());
        if (deathAlteration.getDeathInfo().getDateOfDeath() != null)
            dateAlt = df.format(deathAlteration.getDeathInfo().getDateOfDeath());

        getDisplayList(DeathAlteration.DATE_OF_DEATH, dateEx, dateAlt, 0);
        getDisplayList(DeathAlteration.TIME_OF_DEATH, deathRegister.getDeath().getTimeOfDeath(), deathAlteration.getDeathInfo().getTimeOfDeath(), 0);
        getDisplayList(DeathAlteration.PLACE_OF_DEATH_OFFICIAL, deathRegister.getDeath().getPlaceOfDeath(), deathAlteration.getDeathInfo().getPlaceOfDeath(), 0);
        getDisplayList(DeathAlteration.PLACE_OF_DEATH_ENGLISH, deathRegister.getDeath().getPlaceOfDeathInEnglish(), deathAlteration.getDeathInfo().getPlaceOfDeathInEnglish(), 0);
        getDisplayList(DeathAlteration.CAUSE_OF_DEATH_ESTABLISHED, deathRegister.getDeath().isCauseOfDeathEstablished(), deathAlteration.getDeathInfo().isCauseOfDeathEstablished(), 3);
        getDisplayList(DeathAlteration.CAUSE_OF_DEATH, deathRegister.getDeath().getCauseOfDeath(), deathAlteration.getDeathInfo().getCauseOfDeath(), 0);
        getDisplayList(DeathAlteration.ICD_CODE, deathRegister.getDeath().getIcdCodeOfCause(), deathAlteration.getDeathInfo().getIcdCodeOfCause(), 0);
        getDisplayList(DeathAlteration.BURIAL_PLACE, deathRegister.getDeath().getPlaceOfBurial(), deathAlteration.getDeathInfo().getPlaceOfBurial(), 0);
        //todo possible defect with ICD code and cause of death

        getDisplayList(DeathAlteration.DEATH_PERSON_COUNTRY, deathRegister.getDeathPerson().getDeathPersonCountry(), deathAlteration.getDeathPerson().getDeathPersonCountry(), 4);
        getDisplayList(DeathAlteration.DEATH_PERSON_PASSPORT, deathRegister.getDeathPerson().getDeathPersonPassportNo(), deathAlteration.getDeathPerson().getDeathPersonPassportNo(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_AGE, deathRegister.getDeathPerson().getDeathPersonAge(), deathAlteration.getDeathPerson().getDeathPersonAge(), 1);
        getDisplayList(DeathAlteration.DEATH_PERSON_GENDER, deathRegister.getDeathPerson().getDeathPersonGender(), deathAlteration.getDeathPerson().getDeathPersonGender(), 1);
        getDisplayList(DeathAlteration.DEATH_PERSON_RACE, deathRegister.getDeathPerson().getDeathPersonRace(), deathAlteration.getDeathPerson().getDeathPersonRace(), 6);
        getDisplayList(DeathAlteration.DEATH_PERSON_NAME_OFFICIAL, deathRegister.getDeathPerson().getDeathPersonNameOfficialLang(), deathAlteration.getDeathPerson().getDeathPersonNameOfficialLang(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_NAME_ENGLISH, deathRegister.getDeathPerson().getDeathPersonNameInEnglish(), deathAlteration.getDeathPerson().getDeathPersonNameInEnglish(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_ADDRESS, deathRegister.getDeathPerson().getDeathPersonPermanentAddress(), deathAlteration.getDeathPerson().getDeathPersonPermanentAddress(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_FATHER_PIN, deathRegister.getDeathPerson().getDeathPersonFatherPINorNIC(), deathAlteration.getDeathPerson().getDeathPersonFatherPINorNIC(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_FATHER_NAME, deathRegister.getDeathPerson().getDeathPersonFatherFullName(), deathAlteration.getDeathPerson().getDeathPersonFatherFullName(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_MOTHER_PIN, deathRegister.getDeathPerson().getDeathPersonMotherPINorNIC(), deathAlteration.getDeathPerson().getDeathPersonMotherPINorNIC(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_MOTHER_NAME, deathRegister.getDeathPerson().getDeathPersonMotherFullName(), deathAlteration.getDeathPerson().getDeathPersonMotherFullName(), 0);

        /**
         *retrimming death alteration pending list for partially approvals
         */
        if (deathAlteration.getStatus().equals(DeathAlteration.State.PARTIALY_APPROVED)) {
            List<Integer> currentList = new ArrayList<Integer>();
            BitSet current = deathAlteration.getApprovalStatuses();
            for (Map.Entry e : pendingList.entrySet()) {
                int key = (Integer) e.getKey();
                boolean available = current.get(key);
                if (available) {
                    currentList.add(key);
                }
            }
            for (int i = 0; i < currentList.size(); i++) {
                pendingList.remove(currentList.get(i));
            }
        }
        populatePrimaryLists(districtUKey, dsDivisionId, language, user);
        return SUCCESS;
    }

    public String setBitset() {
        logger.info("setting bit set : {}", approvedIndex.length);

        Hashtable<Integer, Boolean> approveBitset = new Hashtable<Integer, Boolean>();
        DeathAlteration da = deathAlterationService.getById(deathAlterationId, user);
        for (int i = 0; i < approvedIndex.length; i++) {
            int bit = approvedIndex[i];
            approveBitset.put(bit, true);
        }
        if (da.getStatus().equals(DeathAlteration.State.PARTIALY_APPROVED)) {
            logger.debug("alteration is partially approved : idUKey : {}", da.getIdUKey());
            if (approvedIndex.length < pendingListSize) {
                deathAlterationService.approveDeathAlteration(deathAlterationId, approveBitset, false, user);
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return SUCCESS;
            }
            deathAlterationService.approveDeathAlteration(deathAlterationId, approveBitset, true, user);
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            return SUCCESS;
        }
        if (approvedIndex.length < pendingListSize) {
            deathAlterationService.approveDeathAlteration(deathAlterationId, approveBitset, false, user);
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            return SUCCESS;
        }
        deathAlterationService.approveDeathAlteration(deathAlterationId, approveBitset, true, user);
        populatePrimaryLists(districtUKey, dsDivisionId, language, user);
        return SUCCESS;
    }

    public String editDeathAlteration() {
        logger.debug("attempt to edit a death alteration : idUKey : {} ", deathAlterationId);
        deathAlteration = deathAlterationService.getById(deathAlterationId, user);
        if (deathAlteration != null) {
            if (deathAlteration.getStatus().equals(DeathAlteration.State.DATA_ENTRY)) {
                //populate death person info at alteration
                deathRegister = deathRegistrationService.getById(deathAlteration.getDeathId(), user); //this is no use bt cannot set for null object so popullate it aswell
                deathRegister.setDeathPerson(deathAlteration.getDeathPerson());
                //populate death info
                deathRegister.setDeath(populateDeathInfo(deathAlteration.getDeathInfo(), deathRegister.getDeath()));
                editMode = true;
                toDay = deathAlteration.getDateReceived();
            } else {
                logger.debug("cannot edit death alteration idUKey : {} : not in DATA_ENTRY mode", deathAlterationId);
                populatePrimaryLists(districtUKey, dsDivisionId, language, user);
                return ERROR;
            }
        } else {
            logger.debug("unble to find death alteration : idUKey : {} : for edit", deathAlterationId);
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            return ERROR;
        }
        populateOtherLists();
        return SUCCESS;
    }

    public String deleteDeathAlteration() {
        logger.debug("attempt to delete death alteration : idUKey :{} by User : {}", deathAlterationId, user.getUserName());
        deathAlterationService.deleteDeathAlteration(deathAlterationId, user);
        populatePrimaryLists(districtUKey, dsDivisionId, language, user);
        return SUCCESS;
    }

    public String rejectDeathAlteration() {
        if (pageNumber > 0) {
            logger.debug("attemp to reject death alteration : idUKey : {} by User : {}", deathAlterationId, user.getUserName());
            deathAlterationService.rejectDeathAlteration(deathAlterationId, user, rejectComment);
            populatePrimaryLists(districtUKey, dsDivisionId, language, user);
            return SUCCESS;
        }
        deathAlteration = deathAlterationService.getById(deathAlterationId, user);
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
    *//**
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

    /**
     * type
     * String = 0
     * int =1
     * long=2
     * boolean=3
     * country=4
     * date=5
     * race=6
     */

    private void getDisplayList(int index, Object deathRegistreValue, Object deathAlterationValue, int type) {
        List stringList = new ArrayList();
        stringList.add(0, deathRegistreValue);
        stringList.add(1, deathAlterationValue);
        switch (type) {
            case 0:
                if (deathAlterationValue != null & deathRegistreValue != null) {
                    if (compareStiring((String) deathRegistreValue, (String) deathAlterationValue)) {
                        pendingList.put(index, stringList);
                    }
                } else {
                    if (!(deathAlterationValue == null & deathRegistreValue == null))
                        pendingList.put(index, stringList);
                }
                break;
            case 1:
                if (deathAlterationValue != null & deathRegistreValue != null) {
                    if (compareInteger((Integer) deathRegistreValue, (Integer) deathAlterationValue)) {
                        pendingList.put(index, stringList);
                    }
                } else {
                    if (!(deathAlterationValue == null & deathRegistreValue == null))
                        pendingList.put(index, stringList);
                }
                break;
            case 2:
                if (deathAlterationValue != null & deathRegistreValue != null) {
                    if (compareLong((Long) deathRegistreValue, (Long) deathAlterationValue)) {
                        pendingList.put(index, stringList);
                    }
                } else {
                    if (!(deathAlterationValue == null & deathRegistreValue == null))
                        pendingList.put(index, stringList);
                }
                break;
            case 3:
                if (deathAlterationValue != null & deathRegistreValue != null) {
                    if (compareBoolean((Boolean) deathRegistreValue, (Boolean) deathAlterationValue)) {
                        pendingList.put(index, stringList);
                    }
                } else {
                    if (!(deathAlterationValue == null & deathRegistreValue == null))
                        pendingList.put(index, stringList);
                }
                break;
            case 4:
                List<String> countryList = new ArrayList<String>();
                Country ex = (Country) deathRegistreValue;
                Country cu = (Country) deathAlterationValue;
                if (ex != null & cu != null) {
                    countryList.add(0, ex.getEnCountryName());
                    countryList.add(1, cu.getEnCountryName());
                    if (compareCountry(ex, cu)) {
                        pendingList.put(index, countryList);
                    }
                } else {
                    if (!(ex == null & cu == null)) {
                        if (ex != null)
                            countryList.add(0, ex.getEnCountryName());
                        if (cu != null)
                            countryList.add(1, cu.getEnCountryName());
                        pendingList.put(index, countryList);
                    }
                }
                break;
            case 6:
                List<String> raceList = new ArrayList<String>();
                Race exRace = (Race) deathRegistreValue;
                Race cuRace = (Race) deathAlterationValue;
                if (exRace != null & cuRace != null) {
                    raceList.add(0, exRace.getEnRaceName());
                    raceList.add(1, cuRace.getEnRaceName());
                    if (compareRaces((Race) deathRegistreValue, (Race) deathAlterationValue)) {
                        pendingList.put(index, raceList);
                    }
                } else {
                    if (!(deathAlterationValue == null & deathRegistreValue == null))
                        if (exRace != null)
                            raceList.add(0, exRace.getEnRaceName());
                    if (cuRace != null)
                        raceList.add(1, cuRace.getEnRaceName());
                    pendingList.put(index, raceList);
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
    }

    /**
     * other lists race/country
     */
    private void populateOtherLists() {
        raceList = raceDAO.getRaces(language);
        countryList = countryDAO.getCountries(language);
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

    public District getDeathDistrict() {
        return deathDistrict;
    }

    public void setDeathDistrict(District deathDistrict) {
        this.deathDistrict = deathDistrict;
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

    public long getPin() {
        return pin;
    }

    public void setPin(long pin) {
        this.pin = pin;
    }

    public boolean isEditDeathInfo() {
        return editDeathInfo;
    }

    public void setEditDeathInfo(boolean editDeathInfo) {
        this.editDeathInfo = editDeathInfo;
    }

    public boolean isEditDeathPerson() {
        return editDeathPerson;
    }

    public void setEditDeathPerson(boolean editDeathPerson) {
        this.editDeathPerson = editDeathPerson;
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

    public Map<Integer, List<String>> getPendingList() {
        return pendingList;
    }

    public void setPendingList(Map<Integer, List<String>> pendingList) {
        this.pendingList = pendingList;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
}
