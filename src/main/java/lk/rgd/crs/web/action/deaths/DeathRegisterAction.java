package lk.rgd.crs.web.action.deaths;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Locale;
import java.util.Date;
import java.util.List;

import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.DeathRegisterService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.Permission;

/**
 * @author Duminda Dharmakeerthi
 * @authar amith jayasekara
 */
public class DeathRegisterAction extends ActionSupport implements SessionAware {
    private static final Logger logger = LoggerFactory.getLogger(DeathRegisterAction.class);
    private static final String DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE = "crs.dr_rows_per_page";
    private Map session;
    private int pageNo;
    private User user;
    private DeathInfo death;
    private DeathPersonInfo deathPerson;
    private DeclarantInfo declarant;
    private WitnessInfo witness;
    private NotifyingAuthorityInfo notifyingAuthority;


    private int deathDistrictId;
    private int deathDivisionId;
    private int dsDivisionId;


    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final CountryDAO countryDAO;
    private final AppParametersDAO appParametersDAO;
    private final DeathRegisterService service;

    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> countryList;
    private List<DeathRegister> deathApprovalAndPrintList;

    private int noOfRows;
    private boolean allowEditAdoption;
    private boolean allowApproveAdoption;
    private DeathRegister.State currentStatus;
    private boolean nextFlag;
    private boolean previousFlag;


    public DeathRegisterAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO,
                               CountryDAO countryDAO, DeathRegisterService deathRegisterService, AppParametersDAO appParametersDAO) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.countryDAO = countryDAO;
        this.service = deathRegisterService;
        this.appParametersDAO = appParametersDAO;
    }

    public String welcome() {
        return SUCCESS;
    }
    public String initDeathHome() {
        return SUCCESS;
    }
 public String initDeathDeclaration() {
        DeathRegister ddf;
        session.remove(WebConstants.SESSION_DEATH_DECLARATION_BEAN);
        ddf = new DeathRegister();
        session.put(WebConstants.SESSION_DEATH_DECLARATION_BEAN, ddf);
        populate();
        return SUCCESS;
    }
    public String deathDeclaration() {
               populate();
        logger.debug("Step {} of 2 ", pageNo);
        DeathRegister ddf;
        ddf = (DeathRegister) session.get(WebConstants.SESSION_DEATH_DECLARATION_BEAN);
        switch (pageNo) {
            case 1:
                logger.debug("Death Declaration Step {} of 2 ", pageNo);
                ddf.setDeath(death);
                ddf.setDeathPerson(deathPerson);
                logger.debug("Death Declaration Step {} of 2  was completed", pageNo);
                session.put(WebConstants.SESSION_DEATH_DECLARATION_BEAN, ddf);
                return "form1";
            case 2:
                logger.debug("Death Declaration Step {} of 2 ", pageNo);
                ddf.setDeclarant(declarant);
                logger.info("declarent was completed");
                ddf.setWitness(witness);
                logger.info("witness was completed");
                ddf.setNotifyingAuthority(notifyingAuthority);
                logger.debug("Death Declaration Step {} of 2  was completed", pageNo);

                User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
                service.addDeathRegistration(ddf, user);
                return SUCCESS;
        }
        return ERROR;
    }


    public String deathCertificate() {
        return SUCCESS;
    }

    public String lateDeath() {
        populate();
        return SUCCESS;
    }

    public String deathApprovalAndPrint() {
        setPageNo(1);
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        populate();
        initPermissionForApprovalAndPrint();
        if (currentStatus != null) {
            deathApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, currentStatus, user);
        } else {
            deathApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        }
        paginationHandler(deathApprovalAndPrintList.size());
        previousFlag = false;

        return SUCCESS;
    }

    public String filterByStatus() {
        setPageNo(1);
        logger.debug("requested to filter by : {}", currentStatus);
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        populate();
        initPermissionForApprovalAndPrint();
        deathApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, currentStatus, user);
        paginationHandler(deathApprovalAndPrintList.size());
        return SUCCESS;
    }

    /**
     * responsible whether to display the next link in
     * the jsp or not and handles the page number
     *
     * @param recordsFound no of AdoptionOrders found
     */
    public void paginationHandler(int recordsFound) {
        if (recordsFound == appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE)) {
            setNextFlag(true);
        } else {
            setNextFlag(false);
        }
    }

    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        populateBasicLists(language);
        populateDynamicLists(language);
    }

    public void initPermissionForApprovalAndPrint() {
        //todo change permission
        allowApproveAdoption = user.isAuthorized(Permission.APPROVE_ADOPTION);
        allowEditAdoption = user.isAuthorized(Permission.EDIT_ADOPTION);

    }
    private void populateBasicLists(String language) {
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        districtList = districtDAO.getAllDistrictNames(language, user);
        setCountryList(countryDAO.getCountries(language));
    }

    private void populateDynamicLists(String language) {
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        if (getDeathDistrictId() == 0) {
            if (!districtList.isEmpty()) {
                setDeathDistrictId(districtList.keySet().iterator().next());
                logger.debug("first allowed district in the list {} was set", getDeathDistrictId());
            }
        }
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(getDeathDistrictId(), language, user);

        if (getDsDivisionId() == 0) {
            if (!dsDivisionList.isEmpty()) {
                setDsDivisionId(dsDivisionList.keySet().iterator().next());
                logger.debug("first allowed DS Div in the list {} was set", getDsDivisionId());
            }
        }

        bdDivisionList = bdDivisionDAO.getBDDivisionNames(getDsDivisionId(), language, user);
        if (getDeathDivisionId() == 0) {
            setDeathDivisionId(bdDivisionList.keySet().iterator().next());
            logger.debug("first allowed BD Div in the list {} was set", getDeathDivisionId());
        }
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageNo() {
        return pageNo;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map map) {
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
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

    public DistrictDAO getDistrictDAO() {
        return districtDAO;
    }

    public BDDivisionDAO getBdDivisionDAO() {
        return bdDivisionDAO;
    }

    public DSDivisionDAO getDsDivisionDAO() {
        return dsDivisionDAO;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getDeathDistrictId() {
        return deathDistrictId;
    }

    public void setDeathDistrictId(int deathDistrictId) {
        this.deathDistrictId = deathDistrictId;
    }

    public int getDeathDivisionId() {
        return deathDivisionId;
    }

    public void setDeathDivisionId(int deathDivisionId) {
        this.deathDivisionId = deathDivisionId;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public Map<Integer, String> getCountryList() {
        return countryList;
    }

    public void setCountryList(Map<Integer, String> countryList) {
        this.countryList = countryList;
    }

    public DeathInfo getDeath() {
        return death;
    }

    public void setDeath(DeathInfo death) {
        this.death = death;
    }

    public DeathPersonInfo getDeathPerson() {
        return deathPerson;
    }

    public void setDeathPerson(DeathPersonInfo deathPerson) {
        this.deathPerson = deathPerson;
    }

    public DeclarantInfo getDeclarant() {
        return declarant;
    }

    public void setDeclarant(DeclarantInfo declarant) {
        this.declarant = declarant;
    }

    public WitnessInfo getWitness() {
        return witness;
    }

    public void setWitness(WitnessInfo witness) {
        this.witness = witness;
    }

    public NotifyingAuthorityInfo getNotifyingAuthority() {
        return notifyingAuthority;
    }

    public void setNotifyingAuthority(NotifyingAuthorityInfo notifyingAuthority) {
        this.notifyingAuthority = notifyingAuthority;
    }

    public int getNoOfRows() {
        return noOfRows;
    }

    public void setNoOfRows(int noOfRows) {
        this.noOfRows = noOfRows;
    }

    public boolean isAllowEditAdoption() {
        return allowEditAdoption;
    }

    public void setAllowEditAdoption(boolean allowEditAdoption) {
        this.allowEditAdoption = allowEditAdoption;
    }

    public boolean isAllowApproveAdoption() {
        return allowApproveAdoption;
    }

    public void setAllowApproveAdoption(boolean allowApproveAdoption) {
        this.allowApproveAdoption = allowApproveAdoption;
    }

    public DeathRegister.State getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(DeathRegister.State currentStatus) {
        this.currentStatus = currentStatus;
    }

    public boolean isNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(boolean nextFlag) {
        this.nextFlag = nextFlag;
    }

    public List<DeathRegister> getDeathApprovalAndPrintList() {
        return deathApprovalAndPrintList;
    }

    public void setDeathApprovalAndPrintList(List<DeathRegister> deathApprovalAndPrintList) {
        this.deathApprovalAndPrintList = deathApprovalAndPrintList;
    }

    public boolean isPreviousFlag() {
        return previousFlag;
    }

    public void setPreviousFlag(boolean previousFlag) {
        this.previousFlag = previousFlag;
    }
}
