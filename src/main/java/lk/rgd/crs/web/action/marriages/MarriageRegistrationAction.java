package lk.rgd.crs.web.action.marriages;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.CommonUtil;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;

import java.util.*;

/**
 * @author amith
 *         action class for marriage registration
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

    private int marriageDistrictId;
    private int dsDivisionId;
    private int mrDivisionId;
    private int marriageDivisionId;
    private int marriageDivisionIdFemale;
    private int raceIdMale;
    private int raceIdFemale;
    private int countryIdMale;
    private int countryIdFemale;

    private long idUKey;

    private boolean male;
    private boolean secondNotice;
    private boolean editMode;

    private String language;
    private String serialNumber;

    private Date noticeReceivedDate;

    public MarriageRegistrationAction(MarriageRegistrationService marriageRegistrationService,
        MRDivisionDAO mrDivisionDAO, RaceDAO raceDAO, CountryDAO countryDAO, CommonUtil commonUtil) {
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
            marriageDistrictId, dsDivisionId, mrDivisionId, "Marriage", user, language);
        commonUtil.populateCountryAndRaceLists(countryList, raceList, language);
        logger.debug("successfully loaded the page");
        return "pageLoad";
    }

    /**
     * adding a new marriage notice
     * notes: if there is and already added notice (both parties submit notice separate)this will force user
     * (who try to add second notice with same data) to edit existing record
     */
    public String addMarriageNotice() {
        logger.debug("attempt to add marriage notice serial number : {} ");
        //check existing marriage notice
        MarriageRegister existingMarriage = checkExistingMarriageNotice(marriage.getMale().getIdentificationNumberMale(),
            marriage.getFemale().getIdentificationNumberFemale());
        if (existingMarriage != null) {
            //there is already submitted notice so current one is second notice so, this action is forwarding to
            //edit action of the 1 st notice   
            logger.debug("existing marriage notice found for :  male pin :{} female pin : {}",
                marriage.getMale().getIdentificationNumberMale(), marriage.getFemale().getIdentificationNumberFemale());

            idUKey = existingMarriage.getIdUKey();
            secondNotice = true;
            addActionMessage(getText("massage.existing.notice.found"));
            return SUCCESS;
        }
        //check serial number is already exists
        populateNoticeForPersists();
        //add mr division and race male party and female party
        populatePartyObjectsForPersisting();

        boolean both = marriage.isBothPartySubmitted();
        marriageRegistrationService.addMarriageNotice(marriage, (both || male), user);
        logger.debug("successfully added marriage notice serial number: {}");
        return SUCCESS;
    }

    public String editMarriageNoticeInit() {
        logger.debug("attempt to edit marriage notice :idUKey {}", idUKey);
        marriage = marriageRegistrationService.getByIdUKey(idUKey, user);
        if (marriage == null) {
            logger.debug("cannot find marriage register record to edit : idUKey {}", idUKey);
            addActionError(getText("error.cannot.find.record.for.edit"));
            commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
                marriageDistrictId, dsDivisionId, mrDivisionId, "Marriage", user, language);
            return ERROR;
        }
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
            marriageDistrictId, dsDivisionId, mrDivisionId, "Marriage", user, language);
        editMode = true;
        return "pageLoad";
    }

    /**
     * editing(updating) a marriage notice (register)
     */
    public String editMarriageNotice() {
        logger.debug("attempt to edit marriage notice : idUKey {}", marriage.getIdUKey());
        MarriageRegister notice = marriageRegistrationService.getByIdUKey(marriage.getIdUKey(), user);
        if (notice != null) {
            marriageRegistrationService.updateMarriageRegister(marriage, user);
            addActionMessage(getText("marriage.notice.updated.success"));
        } else {
            logger.debug("marriage notice : idUKey {} : update fails");
            addActionError(getText("marriage.notice.update.fails"));
            return ERROR;
        }
        logger.debug("marriage notice : idUKey {} : edited success fully", marriage.getIdUKey());
        return SUCCESS;
    }

    /**
     * special case submit notices to two locations and second notice about to be add(actually updating same record)
     */
    public String addSecondNotice() {
        logger.debug("attempt to add second notice : idUKey of the record : {}", idUKey);
        MarriageRegister notice = marriageRegistrationService.getByIdUKey(idUKey, user);
        if (notice != null) {
            marriageRegistrationService.updateMarriageRegister(marriage, user);
        } else {
            logger.debug("cannot add second notice to idUKey : {}", idUKey);
            addActionError(getText("cannot.add.second,notice.first.does.not.exist"));
            return ERROR;
        }
        logger.debug("added second notice to idUKey : {}", idUKey);
        return SUCCESS;
    }

    public String marriageRegistrationInit() {
        logger.debug("loading marriage registration page");
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
            marriageDistrictId, dsDivisionId, mrDivisionId, "Marriage", user, language);
        raceList = raceDAO.getRaces(language);
        return "pageLoad";
    }

    public String registerMarriage() {
        return "success";
    }

    /**
     * check existing record
     *
     * @return marriage notice (marriage register object if found) else return null
     */
    private MarriageRegister checkExistingMarriageNotice(String malePinOrNic, String femalePinOrNic) {
        MarriageRegister notice = marriageRegistrationService.getActiveMarriageNoticeByMaleAndFemaleIdentification(
            malePinOrNic, femalePinOrNic, user);
        return notice;
    }

    /**
     * populate additional fields for persisting notice
     */
    private void populateNoticeForPersists() {
        //populate mr division and submitted date
        boolean isBothParty = marriage.isBothPartySubmitted();
        if ((!isBothParty && male) || isBothParty) {
            //male submitted   or both
            marriage.setMrDivisionOfMaleNotice(mrDivisionDAO.getMRDivisionByPK(marriageDivisionId));
            marriage.setDateOfMaleNotice(noticeReceivedDate);
            marriage.setSerialOfMaleNotice(serialNumber);
            marriage.setFemaleNoticeWitness_1(null);
            marriage.setFemaleNoticeWitness_2(null);
        } else {
            //female
            marriage.setMrDivisionOfFemaleNotice(mrDivisionDAO.getMRDivisionByPK(marriageDivisionIdFemale));
            marriage.setDateOfFemaleNotice(noticeReceivedDate);
            marriage.setSerialOfFemaleNotice(serialNumber);
            marriage.setMaleNoticeWitness_1(null);
            marriage.setMaleNoticeWitness_2(null);
        }
    }

    /**
     * populate male/female mr division,race,country(if foreign)
     */
    private void populatePartyObjectsForPersisting() {
        if (marriageDivisionId != 0 && marriageDivisionIdFemale != 0) {
            MRDivision mrDivision = mrDivisionDAO.getMRDivisionByPK(marriageDivisionId);
            marriage.getMale().setMrDivisionMale(mrDivision);
            mrDivision = mrDivisionDAO.getMRDivisionByPK(marriageDivisionIdFemale);
            marriage.getFemale().setMrDivisionFemale(mrDivision);
        }
        if (raceIdMale != 0 && raceIdFemale != 0) {
            Race race = raceDAO.getRace(raceIdMale);
            marriage.getMale().setMaleRace(race);
            race = raceDAO.getRace(raceIdFemale);
            marriage.getFemale().setFemaleRace(race);
        }
        if (countryIdMale != 0 && countryIdFemale != 0) {
            //todo add country
            Country country = countryDAO.getCountry(countryIdMale);
            country = countryDAO.getCountry(countryIdFemale);
        }
    }

    /**
     * populating marriage notice(register object) for edit mode
     */
    private void populateMarriageObjectForEditMode() {

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

    public int getMarriageDivisionIdFemale() {
        return marriageDivisionIdFemale;
    }

    public void setMarriageDivisionIdFemale(int marriageDivisionIdFemale) {
        this.marriageDivisionIdFemale = marriageDivisionIdFemale;
    }

    public int getMarriageDivisionId() {
        return marriageDivisionId;
    }

    public void setMarriageDivisionId(int marriageDivisionId) {
        this.marriageDivisionId = marriageDivisionId;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
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
}

