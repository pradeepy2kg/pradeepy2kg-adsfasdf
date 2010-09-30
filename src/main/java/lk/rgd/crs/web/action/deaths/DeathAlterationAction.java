package lk.rgd.crs.web.action.deaths;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.service.DeathAlterationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.domain.DeathAlteration;
import lk.rgd.crs.api.domain.DeathRegister;


/**
 * @author amith jayasekara
 */
public class DeathAlterationAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(DeathAlterationAction.class);

    private User user;
    private DeathAlterationService deathAlterationService;
    private DeathRegistrationService deathRegistrationService;
    private DeathAlteration deathAlteration;
    private DeathRegister deathRegister;

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;

    private int dsDivisionId;
    private int birthDivisionId;
    private int pageNumber; //use to track alteration capture page load and add or edit

    private long certificateNumber;
    private long serialNumber;

    private String language;

    public DeathAlterationAction(DeathAlterationService deathAlterationService, DeathRegistrationService deathRegistrationService) {
        this.deathAlterationService = deathAlterationService;
        this.deathRegistrationService = deathRegistrationService;
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
        deathRegister = deathRegistrationService.getById(certificateNumber, user);
        if (pageNumber > 0) {
            deathAlterationService.addDeathAlteration(deathAlteration, user);
            return SUCCESS;
        }
        return "pageload";
    }


    private void populatePrimaryLists() {
        //todo get original values
        districtList = new HashMap();
        dsDivisionList = new HashMap();
        bdDivisionList = new HashMap();
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

    public DeathAlterationService getDeathAlterationService() {
        return deathAlterationService;
    }

    public void setDeathAlterationService(DeathAlterationService deathAlterationService) {
        this.deathAlterationService = deathAlterationService;
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

    public DeathRegistrationService getDeathRegistrationService() {
        return deathRegistrationService;
    }

    public void setDeathRegistrationService(DeathRegistrationService deathRegistrationService) {
        this.deathRegistrationService = deathRegistrationService;
    }

    public DeathRegister getDeathRegister() {
        return deathRegister;
    }

    public void setDeathRegister(DeathRegister deathRegister) {
        this.deathRegister = deathRegister;
    }
}
