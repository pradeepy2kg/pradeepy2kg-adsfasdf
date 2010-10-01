package lk.rgd.crs.web.action.deaths;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.Date;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
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
    private DeathAlterationService deathAlterationService;
    private DeathRegistrationService deathRegistrationService;
    private DistrictDAO districtDAO;
    private DSDivisionDAO dsDivisionDAO;
    private BDDivisionDAO bdDivisionDAO;
    private DeathAlteration deathAlteration;
    private DeathRegister deathRegister;
    private District deathDistrict;

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> countryList;

    private int dsDivisionId;
    private int birthDivisionId;
    private int pageNumber; //use to track alteration capture page load and add or edit

    private long certificateNumber;
    private long serialNumber;
    private long alterationSerialNo;
    private long deathId;

    private String language;
    private String district;
    private String dsDivision;
    private String deathDivision;

    public DeathAlterationAction(DeathAlterationService deathAlterationService, DeathRegistrationService deathRegistrationService
            , DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO) {
        this.deathAlterationService = deathAlterationService;
        this.deathRegistrationService = deathRegistrationService;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
    }

    /**
     * loading death alteration searching page
     *
     * @return success if successfully load the page
     */
    public String deathAlterationSearch() {
        populatePrimaryLists();
        return SUCCESS;
    }

    /**
     * capturing death alterations
     *
     * @return success of alteration scuccess
     */
    public String captureDeathAlterations() {
        if (pageNumber > 0) {
            //     deathAlteration = new DeathAlteration();
            //setting up alteration 52 act
            deathAlteration.setAlterationSerialNo(alterationSerialNo);
            deathAlteration.setDeathId(deathId);
            //setting alterations done to declarant ,death and death person
            deathAlteration.setDeclarant(deathRegister.getDeclarant());
            deathAlteration.setDeathPerson(deathRegister.getDeathPerson());

            deathAlterationService.addDeathAlteration(deathAlteration, user);
            return SUCCESS;
        } else {

            deathRegister = deathRegistrationService.getById(certificateNumber, user);
            deathAlteration = new DeathAlteration();
            if (deathRegister != null) {
                populatePrimaryLists();
                //setting up death district    ds and death division
                district = districtDAO.getNameByPK(deathRegister.getDeath().getDeathDistrict().getDistrictUKey(), language);
                DSDivision division = deathRegister.getDeath().getDeathDivision().getDsDivision();
                dsDivision = dsDivisionDAO.getNameByPK(division.getDsDivisionUKey(), language);
                deathDivision = bdDivisionDAO.getNameByPK(deathRegister.getDeath().getDeathDivision().getBdDivisionUKey(), language);
            } else {
                addActionError("error.cannot.find.death.registration");
            }
        }
        return "pageload";
    }


    private void populatePrimaryLists() {
        //todo get original values
        districtList = new HashMap();
        dsDivisionList = new HashMap();
        bdDivisionList = new HashMap();
        raceList = new HashMap();
        countryList = new HashMap();
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
}
