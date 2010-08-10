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
import lk.rgd.common.util.GenderUtil;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.DeathRegisterService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.AppConstants;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.Permission;

/**
 * @author Duminda Dharmakeerthi
 * @authar amith jayasekara
 */
public class DeathRegisterAction extends ActionSupport implements SessionAware {
    private static final Logger logger = LoggerFactory.getLogger(DeathRegisterAction.class);
    private static final String DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE = "crs.dr_rows_per_page";
    private Map session;
    private User user;
    private DeathRegister deathRegister;
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

    private long idUKey;
    private int pageNo;
    private int noOfRows;
    private boolean allowEditDeath;
    private boolean allowApproveDeath;
    private DeathRegister.State currentStatus;
    private boolean nextFlag;
    private boolean previousFlag;
    private boolean back;

    private String genderEn;
    private String genderSi;
    private String deathPersonDistrict;
    private String deathPersonDsDivision;
    private String deathPersonDeathDivision;
    private String deathPersonDeathDivisionEn;
    private String deathPersonDistrictEn;
    private String deathPersondsDivision;
    private String deathPersondsDivisionEn;

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
        logger.debug("Step {} of 2", pageNo);
        populate();
        DeathRegister ddf;
        if (back) {
            populate((DeathRegister) session.get(WebConstants.SESSION_DEATH_DECLARATION_BEAN));
            return "form" + pageNo;
        }
        ddf = (DeathRegister) session.get(WebConstants.SESSION_DEATH_DECLARATION_BEAN);
        switch (pageNo) {
            case 1:
                logger.debug("Death Declaration Step {} of 2 ", pageNo);
                ddf.setDeath(death);
                ddf.setDeathPerson(deathPerson);                
                session.put(WebConstants.SESSION_DEATH_DECLARATION_BEAN, ddf);
                break;
            case 2:
                ddf.setDeclarant(declarant);
                ddf.setWitness(witness);
                ddf.setNotifyingAuthority(notifyingAuthority);
                
                service.addDeathRegistration(ddf, user);
                session.remove(WebConstants.SESSION_DEATH_DECLARATION_BEAN);
        }
        return "form" + pageNo;
    }


    public String deathCertificate() {
        idUKey = 4;
        deathRegister = service.getById(idUKey, user);
        deathPerson=deathRegister.getDeathPerson();
        death=deathRegister.getDeath();
        declarant=deathRegister.getDeclarant();
        notifyingAuthority=deathRegister.getNotifyingAuthority();
        declarant=deathRegister.getDeclarant();

        genderEn=GenderUtil.getGender(deathPerson.getDeathPersonGender(), AppConstants.ENGLISH);
        genderSi=GenderUtil.getGender(deathPerson.getDeathPersonGender(), AppConstants.SINHALA);
/*      
  deathPersonDeathDivision=bdDivisionDAO.getNameByPK(deathRegister.getDeath().getDeathDivisionId(),AppConstants.SINHALA);
        deathPersonDeathDivisionEn=bdDivisionDAO.getNameByPK(deathRegister.getDeath().getDeathDivisionId(),AppConstants.ENGLISH);
        deathPersondsDivision=dsDivisionDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(deathRegister.getDeath().getDeathDivisionId()).getDsDivision().getDsDivisionUKey(),AppConstants.SINHALA);
        deathPersondsDivisionEn=dsDivisionDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(deathRegister.getDeath().getDeathDivisionId()).getDsDivision().getDsDivisionUKey(),AppConstants.ENGLISH);
        deathPersonDistrict=districtDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(deathRegister.getDeath().getDeathDivisionId()).getDistrict().getDistrictUKey(),AppConstants.SINHALA);
        deathPersonDistrictEn=districtDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(deathRegister.getDeath().getDeathDivisionId()).getDistrict().getDistrictUKey(),AppConstants.ENGLISH);
*/        
return SUCCESS;
    }

    public String initLateDeath() {
        populate();
        return SUCCESS;
    }

    public String lateDeath() {
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        deathRegister.setStatus(DeathRegister.State.DATA_ENTRY);
        service.addDeathRegistration(deathRegister, user);
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
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        deathApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, currentStatus, user);
        paginationHandler(deathApprovalAndPrintList.size());
        return SUCCESS;
    }

    public String test() {
        return SUCCESS;
    }

    public String deathDeclarationEditMode() {
        logger.debug("death edit mode requested for idUkey : {} ", idUKey);
        deathRegister = service.getById(idUKey, user);
        if (deathRegister.getStatus() != DeathRegister.State.DATA_ENTRY) {
            addActionError("death.error.editNotAllowed");
        }
        return SUCCESS;
    }

    public String approveDeath() {
        logger.debug("requested to approve Death Decalaration with idUKey : {}", idUKey);
        try {
            service.approveDeathRegistration(getIdUKey(), user);
        } catch (CRSRuntimeException e) {
            addActionError(getText("death.error.no.permission"));
        }
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        deathApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    public String rejectDeath() {
        logger.debug("requested to reject Death Decalaration with idUKey : {}", idUKey);
        try {
            service.rejectDeathRegistration(idUKey, user);
        } catch (CRSRuntimeException e) {
            addActionError(getText("death.error.no.permission.reject"));
        }
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        deathApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        initPermissionForApprovalAndPrint();
        populate();

        return SUCCESS;
    }

    public String deleteDeath() {
        logger.debug("requested to delete Death Decalaration with idUKey : {}", idUKey);
        try {
            service.deleteDeathRegistration(idUKey, user);
        }
        catch (CRSRuntimeException e) {
            addActionError("death.error.no.permission.delete");
        }
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        deathApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    public String deathDeclarationViewMode() {
        //todo implement
        logger.debug("initializing view mode for idUKey : {}", idUKey);
        deathRegister = service.getById(idUKey, user);
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        return SUCCESS;
    }

    public String markDeathDeclarationAsPrinted() {
        logger.debug("requested to mark Death Declaration as printed for idUKey : {} ", idUKey);
        try {
            service.markDeathCertificateAsPrinted(idUKey, user);
        }
        catch (CRSRuntimeException e) {
            addActionError("death.error.no.permission.print");
        }
        populate();
        initPermissionForApprovalAndPrint();
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (currentStatus != null) {
            deathApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, currentStatus, user);
        } else {
            deathApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        }
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
 private void populate(DeathRegister ddf) {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        populateBasicLists(language);

        beanPopulate(ddf);

        boolean idsPopulated = false;
        if (death != null) {
            // TODO remove these comments after BDDivision added
//            if (death.getBirthDivision() != null) {  //if data present, populate with existing values
//                deathDistrictId = death.getBirthDistrict().getDistrictUKey();
//                deathDivisionId = death.getBirthDivision().getBdDivisionUKey();
//                dsDivisionId = death.getDsDivision().getDsDivisionUKey();
//                idsPopulated = true;
//            }
            logger.debug("Districts, DS and BD divisions set from RegisterInfo : {} {}", deathDistrictId, dsDivisionId);
        }

        if (!idsPopulated) {         // populate distric and ds div Ids with user preferences or set to 0 temporarily
            if (user.getPrefBDDistrict() != null) {
                deathDistrictId = user.getPrefBDDistrict().getDistrictUKey();
                logger.debug("Prefered district {} set in user {}", deathDistrictId, user.getUserId());
            } else {
                deathDistrictId = 0;
                logger.debug("First district in the list {} was set in user {}", deathDistrictId, user.getUserId());
            }

            if (user.getPrefBDDSDivision() != null) {
                dsDivisionId = user.getPrefBDDSDivision().getDsDivisionUKey();
            } else {
                dsDivisionId = 0;
            }
            logger.debug("Districts, DS and BD divisions set from defaults : {} {}", deathDistrictId, dsDivisionId);
        }
    }
    private void beanPopulate(DeathRegister ddf) {
        //TODO is all needed
        death = ddf.getDeath();
        deathPerson = ddf.getDeathPerson();
        notifyingAuthority = ddf.getNotifyingAuthority();
        declarant = ddf.getDeclarant();
        witness = ddf.getWitness();
    }
    public void initPermissionForApprovalAndPrint() {
        allowApproveDeath = user.isAuthorized(Permission.APPROVE_DEATH);
        allowEditDeath = user.isAuthorized(Permission.EDIT_DEATH);

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
        logger.debug("setting User: {}", user.getUserName());
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

    public boolean isAllowApproveAdoption() {
        return allowApproveDeath;
    }

    public void setAllowApproveAdoption(boolean allowApproveAdoption) {
        this.allowApproveDeath = allowApproveAdoption;
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

    public boolean isAllowApproveDeath() {
        return allowApproveDeath;
    }

    public void setAllowApproveDeath(boolean allowApproveDeath) {
        this.allowApproveDeath = allowApproveDeath;
    }

    public boolean isAllowEditDeath() {
        return allowEditDeath;
    }

    public void setAllowEditDeath(boolean allowEditDeath) {
        this.allowEditDeath = allowEditDeath;
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
    public boolean isBack() {
        return back;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public String getGenderEn() {
        return genderEn;
    }

    public void setGenderEn(String genderEn) {
        this.genderEn = genderEn;
    }

    public String getGenderSi() {
        return genderSi;
    }

    public void setGenderSi(String genderSi) {
        this.genderSi = genderSi;
    }

    public String getDeathPersonDistrict() {
        return deathPersonDistrict;
    }

    public void setDeathPersonDistrict(String deathPersonDistrict) {
        this.deathPersonDistrict = deathPersonDistrict;
    }

    public String getDeathPersonDsDivision() {
        return deathPersonDsDivision;
    }

    public void setDeathPersonDsDivision(String deathPersonDsDivision) {
        this.deathPersonDsDivision = deathPersonDsDivision;
    }

    public String getDeathPersonDeathDivision() {
        return deathPersonDeathDivision;
    }

    public void setDeathPersonDeathDivision(String deathPersonDeathDivision) {
        this.deathPersonDeathDivision = deathPersonDeathDivision;
    }


    public String getDeathPersonDeathDivisionEn() {
        return deathPersonDeathDivisionEn;
    }

    public void setDeathPersonDeathDivisionEn(String deathPersonDeathDivisionEn) {
        this.deathPersonDeathDivisionEn = deathPersonDeathDivisionEn;
    }

    public String getDeathPersonDistrictEn() {
        return deathPersonDistrictEn;
    }

    public void setDeathPersonDistrictEn(String deathPersonDistrictEn) {
        this.deathPersonDistrictEn = deathPersonDistrictEn;
    }

    public String getDeathPersondsDivision() {
        return deathPersondsDivision;
    }

    public void setDeathPersondsDivision(String deathPersondsDivision) {
        this.deathPersondsDivision = deathPersondsDivision;
    }

    public String getDeathPersondsDivisionEn() {
        return deathPersondsDivisionEn;
    }

    public void setDeathPersondsDivisionEn(String deathPersondsDivisionEn) {
        this.deathPersondsDivisionEn = deathPersondsDivisionEn;
    }

    public DeathRegister getDeathRegister() {
        return deathRegister;
    }

    public void setDeathRegister(DeathRegister deathRegister) {
        this.deathRegister = deathRegister;
    }
}
