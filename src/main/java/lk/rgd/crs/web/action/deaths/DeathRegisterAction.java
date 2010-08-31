package lk.rgd.crs.web.action.deaths;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.DeathRegistrationService;
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
    private NotifyingAuthorityInfo notifyingAuthority;
    private CRSLifeCycleInfo lifeCycleInfo;

    private int deathDistrictId;
    private int deathDivisionId;
    private int dsDivisionId;
    private int deathPersonCountry;
    private int deathPersonRace;

    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final CountryDAO countryDAO;
    private final AppParametersDAO appParametersDAO;
    private final DeathRegistrationService service;
    private final RaceDAO raceDAO;

    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;

    private Map<Integer, String> countryList;
    private List<DeathRegister> deathApprovalAndPrintList;
    private Map<Integer, String> raceList;
    private List<String> warnings;

    private int pageNo;
    private int noOfRows;
    private int currentStatus;
    private int rowNumber;

    private long idUKey;

    private boolean allowEditDeath;
    private boolean allowApproveDeath;
    private boolean nextFlag;
    private boolean previousFlag;
    private boolean back;
    private boolean searchByDate;
    private boolean rePrint;
    private boolean lateDeath;

    private String genderEn;
    private String genderSi;
    private String deathPersonDistrict;
    private String deathPersonDsDivision;
    private String deathPersonDeathDivision;
    private String deathPersonDeathDivisionEn;
    private String deathPersonDistrictEn;
    private String deathPersondsDivision;
    private String deathPersondsDivisionEn;
    private String time;

    private Date fromDate;
    private Date endDate;

    private DeathRegister.State state;
    private DeathRegister.Type deathType;
    private boolean ignoreWarning;
    private boolean allowPrintCertificate;


    public DeathRegisterAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO,
                               CountryDAO countryDAO, DeathRegistrationService deathRegistrationService,
                               AppParametersDAO appParametersDAO, RaceDAO raceDAO) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.countryDAO = countryDAO;
        this.service = deathRegistrationService;
        this.appParametersDAO = appParametersDAO;
        this.raceDAO = raceDAO;
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
        ddf.setDeathType(getDeathType());
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
                //TODO checking serial number is taken already for addnew mode
//                DeathRegister dd = service.
//                if (dd != null) {
//                    addFieldError("duplicateSerialNumberError", getText("p1.duplicateSerialNumber.label"));
//                    pageNo = 0;
//                }
                deathType = ddf.getDeathType();
                ddf.setDeath(death);
                ddf.setDeathPerson(deathPerson);
                ddf.setDeathType(deathType);
                beanPopulate(ddf);
                break;
            case 2:
                deathType = ddf.getDeathType();
                ddf.setDeclarant(declarant);
                ddf.setNotifyingAuthority(notifyingAuthority);
                idUKey = ddf.getIdUKey();
                if (idUKey == 0) {
                    if (DeathRegister.Type.NORMAL == deathType || DeathRegister.Type.SUDDEN == deathType) {
                        service.addNormalDeathRegistration(ddf, user);
                    } else if (DeathRegister.Type.LATE == deathType || DeathRegister.Type.MISSING == deathType) {
                        service.addLateDeathRegistration(ddf, user);
                    }
                    idUKey = ddf.getIdUKey();
                    addActionMessage(getText("saveSuccess.label"));
                } else {
                    if (DeathRegister.Type.NORMAL == deathType || DeathRegister.Type.SUDDEN == deathType
                        || DeathRegister.Type.LATE == deathType || DeathRegister.Type.MISSING == deathType) {
                        service.updateDeathRegistration(ddf, user);
                        addActionMessage(getText("editDataSaveSuccess.label"));
                    }
                }
                session.remove(WebConstants.SESSION_DEATH_DECLARATION_BEAN);
        }
        return "form" + pageNo;
    }


    public String deathCertificate() {
        deathRegister = service.getById(idUKey, user);
        if ((deathRegister.getStatus() != DeathRegister.State.DEATH_CERTIFICATE_PRINTED) &&
            (deathRegister.getStatus() != DeathRegister.State.APPROVED)) {
            addActionError(getText("death.error.no.permission.print"));
            logger.debug("Current state of adoption certificate : {}", deathRegister.getStatus());
            return ERROR;
        } else {
            deathPerson = deathRegister.getDeathPerson();
            death = deathRegister.getDeath();
            declarant = deathRegister.getDeclarant();
            notifyingAuthority = deathRegister.getNotifyingAuthority();
            declarant = deathRegister.getDeclarant();

            genderEn = GenderUtil.getGender(deathPerson.getDeathPersonGender(), AppConstants.ENGLISH);
            genderSi = GenderUtil.getGender(deathPerson.getDeathPersonGender(), AppConstants.SINHALA);

            deathPersonDeathDivision = bdDivisionDAO.getNameByPK(deathRegister.getDeath().getDeathDivision().getDivisionId(), death.getPreferredLanguage());
            deathPersonDeathDivisionEn = bdDivisionDAO.getNameByPK(deathRegister.getDeath().getDeathDivision().getDivisionId(), AppConstants.ENGLISH);
            deathPersondsDivision = dsDivisionDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(deathRegister.getDeath().getDeathDivision().getDivisionId()).getDsDivision().getDsDivisionUKey(), death.getPreferredLanguage());
            deathPersondsDivisionEn = dsDivisionDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(deathRegister.getDeath().getDeathDivision().getDivisionId()).getDsDivision().getDsDivisionUKey(), AppConstants.ENGLISH);
            deathPersonDistrict = districtDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(deathRegister.getDeath().getDeathDivision().getDivisionId()).getDistrict().getDistrictUKey(), death.getPreferredLanguage());
            deathPersonDistrictEn = districtDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(deathRegister.getDeath().getDeathDivision().getDivisionId()).getDistrict().getDistrictUKey(), AppConstants.ENGLISH);
            initPermissionForApprovalAndPrint();
            return SUCCESS;
        }
    }

    public String deathApprovalAndPrint() {
        if (pageNo == 2) {
            session.remove(WebConstants.SESSION_DEATH_DECLARATION_BEAN);
        }
        setPageNo(1);
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        populate();
        initPermissionForApprovalAndPrint();
        if (state != null) {
            deathApprovalAndPrintList = service.getPaginatedListForStateByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                pageNo, noOfRows, state, user);
        } else {
            deathApprovalAndPrintList = service.getPaginatedListForAllByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                pageNo, noOfRows, user);
        }
        paginationHandler(deathApprovalAndPrintList.size());
        previousFlag = false;

        return SUCCESS;
    }

    public String filterByStatus() {
        logger.debug("requested to filter by : {}", currentStatus);
        setPageNo(1);
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        populate();
        initPermissionForApprovalAndPrint();

        searchByDate = ((fromDate != null) && (endDate != null));

        if (searchByDate) {
            //search by date in given divission deathDivisions and all the status
            deathApprovalAndPrintList = service.getByBDDivisionAndRegistrationDateRange(
                bdDivisionDAO.getBDDivisionByPK(deathDivisionId), fromDate, endDate, pageNo, noOfRows, user);
        } else {
            if (currentStatus == 0) {
                if (deathDivisionId != 0) {
                    //search by state with all state with in a deathDivision
                    deathApprovalAndPrintList = service.getPaginatedListForAll(bdDivisionDAO.getBDDivisionByPK(deathDivisionId),
                        pageNo, noOfRows, user);
                } else {
                    deathApprovalAndPrintList = service.getPaginatedListForAllByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                        pageNo, noOfRows, user);
                }
            } else {
                if (deathDivisionId != 0) {
                    //search by state with a state with in a deathDivision
                    deathApprovalAndPrintList = service.getPaginatedListForState(bdDivisionDAO.getBDDivisionByPK(deathDivisionId),
                        pageNo, noOfRows, state, user);
                } else {
                    deathApprovalAndPrintList = service.getPaginatedListForStateByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                        pageNo, noOfRows, state, user);
                }
            }
        }
        paginationHandler(deathApprovalAndPrintList.size());
        return SUCCESS;
    }

    public String test() {
        return SUCCESS;
    }

    public String deathDeclarationEditMode() {
        logger.debug("death edit mode requested for idUkey : {} ", idUKey);
        DeathRegister deathRegister = service.getById(idUKey, user);
        beanPopulate(deathRegister);

        if (deathRegister.getStatus() != DeathRegister.State.DATA_ENTRY) {
            addActionError("death.error.editNotAllowed");
            return ERROR;
        }
        session.put(WebConstants.SESSION_DEATH_DECLARATION_BEAN, deathRegister);
        populate();
        return SUCCESS;
    }

    public String approveDeath() {
        logger.debug("requested to approve Death Decalaration with idUKey : {}", idUKey);
        logger.debug("Current status : {}", currentStatus);
        service.approveDeathRegistration(idUKey, user);

        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (deathDivisionId != 0) {
            deathApprovalAndPrintList = service.getPaginatedListForAll(bdDivisionDAO.getBDDivisionByPK(deathDivisionId), pageNo, noOfRows, user);
        } else {
            deathApprovalAndPrintList = service.getPaginatedListForAllByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        }
        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    public String directApproveDeath() {
        logger.debug("requested to direct approve Death Decalaration with idUKey : {}", idUKey);
        service.approveDeathRegistration(idUKey, user);
        initPermissionForApprovalAndPrint();
        populate();
        pageNo = 3;
        return SUCCESS;
    }

    public String directApproveIgnoringWornings() {
        if (ignoreWarning) {
            service.approveDeathRegistration(idUKey, user);
            initPermissionForApprovalAndPrint();
            populate();
        }
        pageNo = 4;
        return SUCCESS;
    }

    public String rejectDeath() {
        logger.debug("requested to reject Death Decalaration with idUKey : {}", idUKey);
        service.rejectDeathRegistration(idUKey, user);
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (deathDivisionId != 0) {
            deathApprovalAndPrintList = service.getPaginatedListForAll(bdDivisionDAO.getBDDivisionByPK(deathDivisionId), pageNo, noOfRows, user);
        } else {
            deathApprovalAndPrintList = service.getPaginatedListForAllByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        }
        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    public String deleteDeath() {
        logger.debug("requested to delete Death Decalaration with idUKey : {}", idUKey);
        service.deleteDeathRegistration(idUKey, user);
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (deathDivisionId != 0) {
            deathApprovalAndPrintList = service.getPaginatedListForAll(bdDivisionDAO.getBDDivisionByPK(deathDivisionId), pageNo, noOfRows, user);
        } else {
            deathApprovalAndPrintList = service.getPaginatedListForAllByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        }
        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    public String deathDeclarationViewMode() {

        logger.debug("Non Editable Mode Step {} of 2", pageNo);
        DeathRegister ddf;
        if (back) {
            populate((DeathRegister) session.get(WebConstants.SESSION_DEATH_DECLARATION_BEAN));
            return "form" + pageNo;
        } else {
            if (pageNo < 0 || pageNo > 2) {
                addActionError(getText("p1.invalid.Entry"));
                return ERROR;
            }
            if (pageNo == 0) {
                logger.debug("initializing non editable mode for IDUKey {}", idUKey);
                try {
                    ddf = service.getById(idUKey, user);
                    deathType = ddf.getDeathType();
                    session.put(WebConstants.SESSION_DEATH_DECLARATION_BEAN, ddf);

                } catch (Exception e) {
                    handleErrors(e);
                    addActionError(getText("p1.invalid.Entry"));
                    return ERROR;
                }
            }
            if (pageNo == 2) {
                session.remove(WebConstants.SESSION_DEATH_DECLARATION_BEAN);
            }

            return "form" + pageNo;
        }
    }

    public String markDeathDeclarationAsPrinted() {

        logger.debug("requested to mark Death Declaration as printed for idUKey : {} ", idUKey);
        deathRegister = service.getById(idUKey, user);
        if (deathRegister != null && deathRegister.getStatus() == DeathRegister.State.APPROVED) {
            try {
                service.markDeathCertificateAsPrinted(idUKey, user);
            }
            catch (CRSRuntimeException e) {
                addActionError("death.error.no.permission.print");
            }
        }
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (deathDivisionId != 0) {
            deathApprovalAndPrintList = service.getPaginatedListForAll(bdDivisionDAO.getBDDivisionByPK(deathDivisionId), pageNo, noOfRows, user);
        } else {
            deathApprovalAndPrintList = service.getPaginatedListForAllByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        }
        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    /**
     * method to ignore death certificate mariking as printed and
     * load the previous state of the list page
     *
     * @return
     */
    public String backToPreviousState() {
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (deathDivisionId != 0) {
            deathApprovalAndPrintList = service.getPaginatedListForAll(bdDivisionDAO.getBDDivisionByPK(deathDivisionId), pageNo, noOfRows, user);
        } else {
            deathApprovalAndPrintList = service.getPaginatedListForAllByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        }
        initPermissionForApprovalAndPrint();
        populate();
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
        deathType = ddf.getDeathType();

        beanPopulate(ddf);

        boolean idsPopulated = false;
        if (death != null) {
            if (death.getDeathDivision() != null) {  //if data present, populate with existing values
                deathDistrictId = death.getDeathDivision().getDistrict().getDistrictUKey();
                deathDivisionId = death.getDeathDivision().getBdDivisionUKey();
                dsDivisionId = death.getDeathDivision().getDsDivision().getDsDivisionUKey();
                idsPopulated = true;
            }
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
        deathPerson = ddf.getDeathPerson();
        death = ddf.getDeath();
        declarant = ddf.getDeclarant();
        notifyingAuthority = ddf.getNotifyingAuthority();
        declarant = ddf.getDeclarant();
        setLifeCycleInfo(ddf.getLifeCycleInfo());
    }

    private void handleErrors(Exception e) {
        logger.error("Handle Error ", e);
        //todo pass the error to the error.jsp page
    }

    public void initPermissionForApprovalAndPrint() {
        allowApproveDeath = user.isAuthorized(Permission.APPROVE_DEATH);
        allowEditDeath = user.isAuthorized(Permission.EDIT_DEATH);
        allowPrintCertificate = user.isAuthorized(Permission.PRINT_DEATH_CERTIFICATE);
    }

    private void populateBasicLists(String language) {
        districtList = districtDAO.getDistrictNames(language, user);
        setCountryList(countryDAO.getCountries(language));
        setRaceList(raceDAO.getRaces(language));
    }

    private void populateDynamicLists(String language) {
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
        /*if (getDeathDivisionId() == 0) {
            setDeathDivisionId(bdDivisionList.keySet().iterator().next());
            logger.debug("first allowed BD Div in the list {} was set", getDeathDivisionId());
        }*/
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
        if (death == null) {
            death = new DeathInfo();
        }
        death.setDeathDivision(bdDivisionDAO.getBDDivisionByPK(deathDivisionId));
        logger.debug("setting DeathDivision: {}", death.getDeathDivision().getEnDivisionName());

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

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
        switch (currentStatus) {
            case 1:
                this.state = DeathRegister.State.DATA_ENTRY;
                break;
            case 2:
                this.state = DeathRegister.State.APPROVED;
                break;
            case 3:
                this.state = DeathRegister.State.REJECTED;
                break;
            case 4:
                this.state = DeathRegister.State.DEATH_CERTIFICATE_PRINTED;
                //speacial case 0 all status
        }
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

    public DeathRegister.State getState() {
        return state;
    }

    public void setState(DeathRegister.State state) {
        this.state = state;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isSearchByDate() {
        return searchByDate;
    }

    public void setSearchByDate(boolean searchByDate) {
        this.searchByDate = searchByDate;
    }

    public DeathRegister.Type getDeathType() {
        return deathType;
    }

    public void setDeathType(DeathRegister.Type deathType) {
        this.deathType = deathType;
    }

    public int getDeathPersonCountry() {
        return deathPersonCountry;
    }

    public void setDeathPersonCountry(int deathPersonCountry) {
        this.deathPersonCountry = deathPersonCountry;
        if (deathPerson == null) {
            deathPerson = new DeathPersonInfo();
        }
        deathPerson.setDeathPersonCountry(countryDAO.getCountry(deathPersonCountry));
    }

    public int getDeathPersonRace() {
        return deathPersonRace;
    }

    public void setDeathPersonRace(int deathPersonRace) {
        this.deathPersonRace = deathPersonRace;
        if (deathPerson == null) {
            deathPerson = new DeathPersonInfo();
        }
        deathPerson.setDeathPersonRace(raceDAO.getRace(deathPersonRace));
    }

    public RaceDAO getRaceDAO() {
        return raceDAO;
    }

    public Map<Integer, String> getRaceList() {
        return raceList;
    }

    public void setRaceList(Map<Integer, String> raceList) {
        this.raceList = raceList;
    }

    public boolean isRePrint() {
        return rePrint;
    }

    public void setRePrint(boolean rePrint) {
        this.rePrint = rePrint;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isLateDeath() {
        return lateDeath;
    }

    public void setLateDeath(boolean lateDeath) {
        this.lateDeath = lateDeath;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public CRSLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(CRSLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public boolean isApprove() {
        return ignoreWarning;
    }

    public void setApprove(boolean approve) {
        this.ignoreWarning = approve;
    }

    public boolean isAllowPrintCertificate() {
        return allowPrintCertificate;
    }

    public void setAllowPrintCertificate(boolean allowPrintCertificate) {
        this.allowPrintCertificate = allowPrintCertificate;
    }
}
