package lk.rgd.crs.web.action.marriages;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.domain.Witness;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.DivisionUtil;
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

    private User user;
    private MarriageRegister marriage;

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> mrDivisionList;

    private int marriageDistrictId;
    private int dsDivisionId;
    private int mrDivisionId;
    private int marriageDivisionId;
    private int marriageDivisionIdFemale;

    private long idUKey;

    private boolean male;

    private String language;
    private String serialNumber;

    private Date noticeReceivedDate;

    public MarriageRegistrationAction(MarriageRegistrationService marriageRegistrationService, MRDivisionDAO mrDivisionDAO) {
        this.marriageRegistrationService = marriageRegistrationService;
        this.mrDivisionDAO = mrDivisionDAO;
        districtList = new HashMap<Integer, String>();
        dsDivisionList = new HashMap<Integer, String>();
        mrDivisionList = new HashMap<Integer, String>();
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
        DivisionUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
            marriageDistrictId, dsDivisionId, mrDivisionId, "Marriage", user, language);
        return "pageLoad";
    }

    public String addMarriageNotice() {
        logger.debug("attempt to add marriage notice serial number : {} ");
        //todo      remove true
        /*check serial number is already exists*/
        populateNoticeForPersists();
        marriageRegistrationService.addMarriageNotice(marriage, true, user);
        logger.debug("successfully added marriage notice serial number: {}");
        return SUCCESS;
    }

    public String editMarriageNoticeInit() {
        logger.debug("attempt to edit marriage notice :idUKey {}", idUKey);
        marriage = marriageRegistrationService.getByIdUKey(idUKey, user);
        if (marriage == null) {
            logger.debug("cannot find marriage register record to edit : idUKey {}", idUKey);
            addActionError(getText("error.cannot.find.record.for.edit"));
            DivisionUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
                marriageDistrictId, dsDivisionId, mrDivisionId, "Marriage", user, language);
            return ERROR;
        }
        DivisionUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
            marriageDistrictId, dsDivisionId, mrDivisionId, "Marriage", user, language);
        return "pageLoad";
    }

    /**
     * loading search page for marriage notice search
     */
    public String marriageNoticeSearchInit() {
        logger.debug("loading search page for marriage notice");
        DivisionUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
            marriageDistrictId, dsDivisionId, mrDivisionId, "Marriage", user, language);
        return "pageLoad";
    }

    public String marriageRegistrationInit() {
        logger.debug("loading marriage registration page");
        DivisionUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList,
            marriageDistrictId, dsDivisionId, mrDivisionId, "Marriage", user, language);
        return "pageLoad";
    }

    public String registerMarriage() {
        return "success";
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
}

