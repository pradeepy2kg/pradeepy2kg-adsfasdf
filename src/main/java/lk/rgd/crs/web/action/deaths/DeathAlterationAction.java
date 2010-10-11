package lk.rgd.crs.web.action.deaths;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.lang.reflect.Method;

import lk.rgd.common.api.domain.*;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.service.DeathAlterationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.dao.BDDivisionDAO;


/**
 * @author amith jayasekara
 */
public class DeathAlterationAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(DeathAlterationAction.class);

    private User user;
    private Date toDay;
    private Date startDate;
    private Date endDate;
    private DeathAlterationService deathAlterationService;
    private DeathRegistrationService deathRegistrationService;
    private DistrictDAO districtDAO;
    private DSDivisionDAO dsDivisionDAO;
    private BDDivisionDAO bdDivisionDAO;
    private RaceDAO raceDAO;
    private CountryDAO countryDAO;
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

    private boolean editDeathInfo;
    private boolean editDeathPerson;

    public DeathAlterationAction(DeathAlterationService deathAlterationService, DeathRegistrationService deathRegistrationService
            , DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO, RaceDAO raceDAO, CountryDAO countryDAO) {
        this.deathAlterationService = deathAlterationService;
        this.deathRegistrationService = deathRegistrationService;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.raceDAO = raceDAO;
        this.countryDAO = countryDAO;
    }

    public String deathAlterationSearch() {
        populatePrimaryLists();
        return SUCCESS;
    }

    public String captureDeathAlterations() {
        if (pageNumber > 0) {
            deathAlteration.setAlterationSerialNo(alterationSerialNo);
            deathAlteration.setDeathId(deathId);
            deathAlteration.setDeclarant(deathRegister.getDeclarant());
            deathAlteration.setDeathPerson(deathRegister.getDeathPerson());
            deathAlteration.setStatus(DeathAlteration.State.DATA_ENTRY);

            DeathRegister dr = deathRegistrationService.getById(deathId, user);
            deathAlteration.setDeathDivision(dr.getDeath().getDeathDivision());
            //deathAlteration = deleteDeathAlteration(deathAlteration, dr);
            deathAlterationService.addDeathAlteration(deathAlteration, user);
            addActionMessage(getText("alt.massage.success"));
            populatePrimaryLists();
            //todo check already addedd data before add
            return SUCCESS;
        } else {
            deathAlteration = new DeathAlteration();
            //search by certificate number
            if (certificateNumber != 0)
                deathRegister = deathRegistrationService.getById(certificateNumber, user);
            //search by pin
            if (pin != 0) {
                //todo implemt no service layer mathod to getByPinOrNIC
                //only get firts recode others ignored
                deathRegister = deathRegistrationService.getByPinOrNic(pin, user).get(0);
            }
            //search by  serial and death division
            if (serialNumber != 0 && divisionUKey != 0) {
                BDDivision deathDivision = bdDivisionDAO.getBDDivisionByPK(divisionUKey);
                deathRegister = deathRegistrationService.getByBDDivisionAndDeathSerialNo(deathDivision, serialNumber, user);
            }
            if (deathRegister == null) {
                addActionError(getText("error.cannot.find.death.registration"));
                populatePrimaryLists();
                return ERROR;
            }
            //check is there a ongoing alteration for this cartificate
            List<DeathAlteration> exsistingAlterations = deathAlterationService.getAlterationByDeathCertificateNumber(deathRegister.getIdUKey(), user);
            Iterator<DeathAlteration> itr = exsistingAlterations.iterator();
            while (itr.hasNext()) {
                DeathAlteration da = itr.next();
                if (da.getStatus().equals(DeathAlteration.State.DATA_ENTRY)) {
                    addActionError("error.exsisting.alteratios.data.entry");
                    populatePrimaryLists();
                    return ERROR;
                }
            }
            //check death register is not null and in data approved state
            if (deathRegister != null) {
                if (!deathRegister.getStatus().equals(DeathRegister.State.DEATH_CERTIFICATE_PRINTED)) {
                    addActionError(getText("error.death.certificate.must.print.before"));
                    populatePrimaryLists();
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
                addActionError(getText("error.unknown"));
                populatePrimaryLists();
                return ERROR;
            }
        }
        return "pageload";
    }

    public String deathAlterationApproval() {
        if (pageNumber > 0) {
            pageNo = 1;
            rowNo = 50;
            //search by division
            if (divisionUKey > 0) {
                approvalList = deathAlterationService.getAlterationApprovalListByDeathDivision(pageNo, rowNo, divisionUKey);
            }
            //search by date frame
            if (startDate != null & endDate != null) {
                approvalList = deathAlterationService.getDeathAlterationByTimePeriod(startDate, endDate, user);
            }
            if (approvalList.size() < 1) {
                addActionError(getText("no.pending.alterations"));
                populatePrimaryLists();
                return ERROR;
            }
        } else {
            populatePrimaryLists();
        }
        populatePrimaryLists();
        return SUCCESS;
    }

    public String directApprove() {
        if (deathAlterationId > 0) {
            //get form list
            deathAlteration = deathAlterationService.getById(deathAlterationId, user);

        } else {
            //todo remove
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
            addActionError(getText("cannot.approve.nt.in.correct.state"));
            populatePrimaryLists();
            return ERROR;
        }
        //todo name english
        //todo check sudden death
        //todo check null before getting data
        deathRegister = deathRegistrationService.getById(deathAlteration.getDeathId(), user);

        getDisplayList(DeathAlteration.DATE_OF_DEATH, deathRegister.getDeath().getDateOfDeath(), deathAlteration.getDeathInfo().getDateOfDeath(), 5);
        getDisplayList(DeathAlteration.TIME_OF_DEATH, deathRegister.getDeath().getTimeOfDeath(), deathAlteration.getDeathInfo().getTimeOfDeath(), 0);
        getDisplayList(DeathAlteration.PLACE_OF_DEATH_OFFICIAL, deathRegister.getDeath().getPlaceOfDeath(), deathAlteration.getDeathInfo().getPlaceOfDeath(), 0);
        getDisplayList(DeathAlteration.PLACE_OF_DEATH_OFFICIAL, deathRegister.getDeath().getPlaceOfDeathInEnglish(), deathAlteration.getDeathInfo().getPlaceOfDeathInEnglish(), 0);
        getDisplayList(DeathAlteration.CAUSE_OF_DEATH_ESTABLISHED, deathRegister.getDeath().isCauseOfDeathEstablished(), deathAlteration.getDeathInfo().isCauseOfDeathEstablished(), 3);
        getDisplayList(DeathAlteration.CAUSE_OF_DEATH, deathRegister.getDeath().getCauseOfDeath(), deathAlteration.getDeathInfo().getCauseOfDeath(), 0);
        getDisplayList(DeathAlteration.ICD_CODE, deathRegister.getDeath().getIcdCodeOfCause(), deathAlteration.getDeathInfo().getIcdCodeOfCause(), 0);
        getDisplayList(DeathAlteration.BURIAL_PLACE, deathRegister.getDeath().getPlaceOfBurial(), deathAlteration.getDeathInfo().getPlaceOfBurial(), 0);

        getDisplayList(DeathAlteration.DEATH_PERSON_FATHER_PIN, deathRegister.getDeathPerson().getDeathPersonFatherPINorNIC(), deathAlteration.getDeathPerson().getDeathPersonFatherPINorNIC(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_COUNTRY, deathRegister.getDeathPerson().getDeathPersonCountry(), deathAlteration.getDeathPerson().getDeathPersonCountry(), 4);
        getDisplayList(DeathAlteration.DEATH_PERSON_PASSPORT, deathRegister.getDeathPerson().getDeathPersonPassportNo(), deathAlteration.getDeathPerson().getDeathPersonPassportNo(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_AGE, deathRegister.getDeathPerson().getDeathPersonAge(), deathAlteration.getDeathPerson().getDeathPersonAge(), 1);
        getDisplayList(DeathAlteration.DEATH_PERSON_GENDER, deathRegister.getDeathPerson().getDeathPersonGender(), deathAlteration.getDeathPerson().getDeathPersonGender(), 1);
        getDisplayList(DeathAlteration.DEATH_PERSON_RACE, deathRegister.getDeathPerson().getDeathPersonRace(), deathAlteration.getDeathPerson().getDeathPersonRace(), 6);
        getDisplayList(DeathAlteration.DEATH_PERSON_NAME_OFFICIAL, deathRegister.getDeathPerson().getDeathPersonNameOfficialLang(), deathAlteration.getDeathPerson().getDeathPersonNameOfficialLang(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_NAME_OFFICIAL, deathRegister.getDeathPerson().getDeathPersonNameInEnglish(), deathAlteration.getDeathPerson().getDeathPersonNameInEnglish(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_ADDRESS, deathRegister.getDeathPerson().getDeathPersonPermanentAddress(), deathAlteration.getDeathPerson().getDeathPersonPermanentAddress(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_FATHER_NAME, deathRegister.getDeathPerson().getDeathPersonFatherFullName(), deathAlteration.getDeathPerson().getDeathPersonFatherFullName(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_MOTHER_PIN, deathRegister.getDeathPerson().getDeathPersonMotherPINorNIC(), deathAlteration.getDeathPerson().getDeathPersonMotherPINorNIC(), 0);
        getDisplayList(DeathAlteration.DEATH_PERSON_MOTHER_NAME, deathRegister.getDeathPerson().getDeathPersonMotherFullName(), deathAlteration.getDeathPerson().getDeathPersonMotherFullName(), 0);

        if (deathAlteration.getStatus().equals(DeathAlteration.State.PARTIALY_APPROVED)) {
            //retrimming for display
            BitSet current = deathAlteration.getApprovalStatuses();
            Iterator<Integer> itr = pendingList.keySet().iterator();
            List<Integer> currentList = new ArrayList<Integer>();
            while (itr.hasNext()) {
                boolean available = current.get(itr.next());
                if (available) {
                    currentList.add(itr.next());
                }
            }
            Iterator it = currentList.iterator();
            while (it.hasNext()) {
                pendingList.remove(it.next());
            }
        }

        populatePrimaryLists();
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
        logger.debug("index leangth : {} ,bit set leangth : {}", approvedIndex.length, approveBitset.size());
        //todo wht happen if error  and add action massage for succcesfull add
        if (da.getStatus().equals(DeathAlteration.State.PARTIALY_APPROVED)) {
            if (approvedIndex.length < pendingListSize) {
                deathAlterationService.approveDeathAlteration(deathAlterationId, approveBitset, false, user);
                populatePrimaryLists();
                return SUCCESS;
            }
            deathAlterationService.approveDeathAlteration(deathAlterationId, approveBitset, true, user);
            populatePrimaryLists();
            return SUCCESS;
        }
        if (approvedIndex.length < pendingListSize) {
            deathAlterationService.approveDeathAlteration(deathAlterationId, approveBitset, false, user);
            populatePrimaryLists();
            return SUCCESS;
        }
        deathAlterationService.approveDeathAlteration(deathAlterationId, approveBitset, true, user);
        populatePrimaryLists();
        return SUCCESS;
    }

    public String editDeathAlteration() {
        //todo implement
        return SUCCESS;
    }

    public String deleteDeathAlteration() {
        deathAlterationService.deleteDeathAlteration(deathAlterationId, user);
        populatePrimaryLists();
        return SUCCESS;
    }

    public String rejectDeathAlteration() {
        //todo implement
        return SUCCESS;
    }
/*
    private DeathAlteration trimAlterationObject(DeathAlteration da, DeathRegister dr) {
        //todo remove
        //compare existing values with previous recode values
        if (editDeathInfo) {
            if (compareStiring(da.getDeathInfo().getPlaceOfDeath(), dr.getDeath().getPlaceOfDeath()))
                da.getDeathInfo().setPlaceOfDeath(null);
            if (compareStiring(da.getDeathInfo().getPlaceOfDeathInEnglish(), dr.getDeath().getPlaceOfDeathInEnglish()))
                da.getDeathInfo().setPlaceOfDeathInEnglish(null);
            if (compareStiring(da.getDeathInfo().getTimeOfDeath(), dr.getDeath().getTimeOfDeath()))
                da.getDeathInfo().setTimeOfDeath(null);
            if (compareStiring(da.getDeathInfo().getTimeOfDeath(), dr.getDeath().getTimeOfDeath()))
                da.getDeathInfo().setTimeOfDeath(null);
            if (compareStiring(da.getDeathInfo().getCauseOfDeath(), dr.getDeath().getCauseOfDeath()))
                da.getDeathInfo().setCauseOfDeath(null);
            if (compareStiring(da.getDeathInfo().getIcdCodeOfCause(), dr.getDeath().getIcdCodeOfCause()))
                da.getDeathInfo().setIcdCodeOfCause(null);
            if (compareStiring(da.getDeathInfo().getPlaceOfBurial(), dr.getDeath().getPlaceOfBurial()))
                da.getDeathInfo().setPlaceOfBurial(null);

            if (compareDates(da.getDeathInfo().getDateOfDeath(), dr.getDeath().getDateOfDeath()))
                da.getDeathInfo().setDateOfDeath(null);
            //compare boolean
*//*          //todo
            if(da.isCauseOfDeathEstablished() && dr.getDeath().isCauseOfDeathEstablished())
                da.setCauseOfDeathEstablished(null);*//*

        } else {
            da.setDeathInfo(new DeathAlterationInfo());
        }
        if (editDeathPerson) {
            //todo improve with Method class
            if (compareStiring(dr.getDeathPerson().getDeathPersonNameOfficialLang(), da.getDeathPerson().getDeathPersonNameOfficialLang()))
                da.getDeathPerson().setDeathPersonNameOfficialLang(null);
            if (compareStiring(dr.getDeathPerson().getDeathPersonNameInEnglish(), da.getDeathPerson().getDeathPersonNameInEnglish()))
                da.getDeathPerson().setDeathPersonNameInEnglish(null);
            if (compareStiring(dr.getDeathPerson().getDeathPersonFatherFullName(), da.getDeathPerson().getDeathPersonFatherFullName()))
                da.getDeathPerson().setDeathPersonFatherFullName(null);
            if (compareStiring(dr.getDeathPerson().getDeathPersonMotherFullName(), da.getDeathPerson().getDeathPersonMotherFullName()))
                da.getDeathPerson().setDeathPersonMotherFullName(null);
            if (compareStiring(dr.getDeathPerson().getDeathPersonPermanentAddress(), da.getDeathPerson().getDeathPersonPermanentAddress()))
                da.getDeathPerson().setDeathPersonPermanentAddress(null);
            if (compareStiring(dr.getDeathPerson().getDeathPersonPINorNIC(), da.getDeathPerson().getDeathPersonPINorNIC()))
                da.getDeathPerson().setDeathPersonPINorNIC(null);
            if (compareStiring(dr.getDeathPerson().getDeathPersonFatherPINorNIC(), da.getDeathPerson().getDeathPersonFatherPINorNIC()))
                da.getDeathPerson().setDeathPersonFatherPINorNIC(null);
            if (compareStiring(dr.getDeathPerson().getDeathPersonMotherPINorNIC(), da.getDeathPerson().getDeathPersonMotherPINorNIC()))
                da.getDeathPerson().setDeathPersonMotherPINorNIC(null);

            if (compareRaces(dr.getDeathPerson().getDeathPersonRace(), da.getDeathPerson().getDeathPersonRace()))
                da.getDeathPerson().setDeathPersonRace(null);

            if (compareCountry(dr.getDeathPerson().getDeathPersonCountry(), da.getDeathPerson().getDeathPersonCountry()))
                da.getDeathPerson().setDeathPersonCountry(null);

            if (compareInteger(dr.getDeathPerson().getDeathPersonAge(), da.getDeathPerson().getDeathPersonAge()))
                da.getDeathPerson().setDeathPersonAge(null);
            //todo gender

        } else {
            da.setDeathPerson(new DeathPersonInfo());
        }
        return da;
    }*/


    /*
   * type 0=string
   * 1=int
   * 2=long
   * 3=boolean
   * 4=country
   * 5=dates
   * 6=race
   * */

    private void getDisplayList(int index, Object deathRegistreValue, Object deathAlterationValue, int type) {
        switch (type) {
            case 0:
                if (deathAlterationValue != null & deathRegistreValue != null) {
                    if (compareStiring((String) deathRegistreValue, (String) deathAlterationValue)) {
                        List<String> stringList = new ArrayList<String>();
                        stringList.add(0, (String) deathRegistreValue);
                        stringList.add(1, (String) deathAlterationValue);
                        pendingList.put(index, stringList);
                    }
                }
                break;
            case 1:
                if (deathAlterationValue != null & deathRegistreValue != null) {
                    if (compareInteger((Integer) deathRegistreValue, (Integer) deathAlterationValue)) {
                        List<String> intList = new ArrayList<String>();
                        intList.add(0, Integer.toString((Integer) deathRegistreValue));
                        intList.add(1, Integer.toString((Integer) deathAlterationValue));
                        pendingList.put(index, intList);
                    }
                }
                break;
            case 2:
                if (deathAlterationValue != null & deathRegistreValue != null) {
                    if (compareLong((Long) deathRegistreValue, (Long) deathAlterationValue)) {
                        List<String> intList = new ArrayList<String>();
                        intList.add(0, Integer.toString((Integer) deathRegistreValue));
                        intList.add(1, Integer.toString((Integer) deathAlterationValue));
                        pendingList.put(index, intList);
                    }
                }
                break;
            case 3:
                if (deathAlterationValue != null & deathRegistreValue != null) {
                    if (compareBoolean((Boolean) deathRegistreValue, (Boolean) deathAlterationValue)) {
                        List<String> intList = new ArrayList<String>();
                        intList.add(0, Integer.toString((Integer) deathRegistreValue));
                        intList.add(1, Integer.toString((Integer) deathAlterationValue));
                        pendingList.put(index, intList);
                    }
                }
                break;
            case 4:
                if (deathAlterationValue != null & deathRegistreValue != null) {

                }
                break;
            case 5:
                if (deathAlterationValue != null & deathRegistreValue != null) {

                }
                break;
            case 6:
                if (deathAlterationValue != null & deathRegistreValue != null) {

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

    private boolean compareDates(Date exsisting, Date current) {
        return false;
    }

    private boolean compareRaces(Race ex, Race cu) {
        return false;
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
        return false;
    }

    private void populatePrimaryLists() {
        districtList = districtDAO.getDistrictNames(language, user);
        districtUKey = districtList.keySet().iterator().next();
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(districtUKey, language, user);
        dsDivisionId = dsDivisionList.keySet().iterator().next();
        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
    }

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
}
