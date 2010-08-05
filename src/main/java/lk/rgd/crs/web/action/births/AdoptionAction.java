package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Locale;
import java.util.List;

import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.Permission;

/**
 * @author Duminda Dharmakeerthi
 * @author amith jayasekaa
 * @author Indunil Moremada
 */
@SuppressWarnings({"ALL"})
public class AdoptionAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionAction.class);
    private static final String ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE = "crs.br_approval_rows_per_page";

    private final AdoptionOrderService service;
    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final CountryDAO countryDAO;
    private final AppParametersDAO appParametersDAO;

    private int birthDistrictId;
    private int birthDivisionId;
    private int dsDivisionId;
    private String language;
    private AdoptionOrder.State currentStatus;

    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;
    private List<AdoptionOrder> adoptionApprovalAndPrintList;
    private Map<Integer, String> countryList;
    private AdoptionOrder adoption;
    private User user;
    private Map session;

    private long idUKey;
    private int pageNo;
    private String courtOrderNo;
    private boolean allowEditAdoption;
    private boolean allowApproveAdoption;
    private boolean nextFlag;
    private boolean previousFlag;

    private String dsDivisionName;
    private String birthDivisionName;
    private String applicantCountryName;
    private String wifeCountryName;
    private String birthDistrictName;
    private String certificateApplicantAddress;
    private String certificateApplicantPINorNIC;
    private String certificateApplicantName;
    private AdoptionOrder.ApplicantType certificateApplicantType;

    private boolean alreadyPrinted;
    private int noOfRows;

    public AdoptionAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO,
                          AdoptionOrderService service, CountryDAO countryDAO, AppParametersDAO appParametersDAO) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.countryDAO = countryDAO;
        this.appParametersDAO = appParametersDAO;
    }

    public String initAdoptionRegistration() {
        return SUCCESS;

    }

    public String adoptionAction() {
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        adoption.setStatus(AdoptionOrder.State.DATA_ENTRY);
        if (idUKey > 0) {
            adoption.setIdUKey(idUKey);
            service.updateAdoptionOrder(adoption, user);
        } else {
            service.addAdoptionOrder(adoption, user);
        }
        return SUCCESS;
    }

    public String adoptionDeclaration() {
        logger.debug("Adoption declaration ok");
        populate();
        return SUCCESS;
    }

    public String initAdoptionReRegistration() {
        logger.debug("Adoption reregistration for IdUKey : {}", idUKey);
        AdoptionOrder adoption;
        if (idUKey != 0) {
            try {
                adoption = service.getById(getIdUKey(), user);
                logger.debug("Id u key : {}", getIdUKey());
            } catch (Exception e) {
                logger.debug("catch exception : {}", e);
            }
        } else {
            logger.debug("idUkey is zero");
        }
        return SUCCESS;
    }

    /**
     * responsible for loading the AdoptionOrder based
     * on requested idUKey. Error will be thrown if it
     * is not in the DATA_ENTRY mode
     *
     * @return
     */
    public String adoptionDeclarationEditMode() {
        logger.debug("requested to edit AdoptionOrder with idUKey : {}", idUKey);
        adoption = service.getById(idUKey, user);
        if (adoption.getStatus().ordinal() != AdoptionOrder.State.DATA_ENTRY.ordinal()) {
            //not in data entry mode
            addActionError(getText("adoption.error.editNotAllowed"));
            return ERROR;
        }

        populate();
        return SUCCESS;
    }

    /**
     * responsible for loading the AdoptionOrder based
     * on requested idUKey. which is then redirected to
     * no editable page
     *
     * @return
     */
    public String adoptionDeclarationViewMode() {
        logger.debug("initializing view mode for idUKey : {}", idUKey);
        adoption = service.getById(idUKey, user);
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();

        birthDivisionName = bdDivisionDAO.getNameByPK(adoption.getBirthDivisionId(), language);
        dsDivisionName = dsDivisionDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(adoption.getBirthDivisionId()).getDsDivision().getDsDivisionUKey(), language);
        applicantCountryName = countryDAO.getNameByPK(adoption.getApplicantCountryId(), language);
        wifeCountryName = countryDAO.getNameByPK(adoption.getWifeCountryId(), language);
        birthDistrictName = districtDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(adoption.getBirthDivisionId()).getDistrict().getDistrictUKey(), language);
        return SUCCESS;
    }

    /**
     * marks requested Adoption Regisration as printed
     *
     * @return
     */
    public String adoptionDeclarationMarkAsPrint() {
        logger.debug("requested to mark as printed AdoptionOrder with idUKey : {} ", idUKey);
        service.setStatusToPrintedNotice(idUKey, user);
        adoption = service.getById(idUKey, user);
        return SUCCESS;
    }

    /**
     * marks requested Adoption as it's certificate
     * printed
     *
     * @return
     */
    public String adoptionDeclarationMarkAsCertificatePrint() {
        logger.debug("requested to mark adoption certificate as printed with idUKey : {} alreadyPrinted : {} ", idUKey, alreadyPrinted);
        if (!alreadyPrinted) {
            service.setStatusToPrintedCertificate(idUKey, user);
        }
        adoption = service.getById(idUKey, user);
        return SUCCESS;
    }

    public String adoptionReRegistration() {
        return SUCCESS;
    }

    /**
     * get the adoption which are to be approved and printed
     *
     * @return
     */
    public String adoptionApprovalAndPrint() {
        setPageNo(1);
        noOfRows = appParametersDAO.getIntParameter(ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        populate();
        initPermissionForApprovalAndPrint();
        if (currentStatus != null) {
            adoptionApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, currentStatus, user);
        } else {
            adoptionApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        }
        paginationHandler(adoptionApprovalAndPrintList.size());
        previousFlag = false;
        return SUCCESS;
    }

    /**
     * method is responsible for loading the previous records
     * after viewing and printing a particular adoption
     *
     * @return
     */
    public String adoptionBackToPreviouseState() {
        logger.debug("loading previous page : currentStatus {} , pageNo  {}", currentStatus, pageNo);
        populate();
        initPermissionForApprovalAndPrint();
        noOfRows = appParametersDAO.getIntParameter(ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (currentStatus != null) {
            adoptionApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, currentStatus, user);
        } else {
            adoptionApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        }
        return SUCCESS;
    }

    /**
     * filtering based on the user selected state
     *
     * @return
     */
    public String filterByStatus() {
        setPageNo(1);
        logger.debug("requested to filter by : {}", currentStatus);
        noOfRows = appParametersDAO.getIntParameter(ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        populate();
        initPermissionForApprovalAndPrint();
        adoptionApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, currentStatus, user);
        paginationHandler(adoptionApprovalAndPrintList.size());
        return SUCCESS;
    }

    /**
     * responsible whether to display the next link in
     * the jsp or not and handles the page number
     *
     * @param recordsFound no of AdoptionOrders found
     */
    public void paginationHandler(int recordsFound) {
        if (recordsFound == appParametersDAO.getIntParameter(ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE)) {
            setNextFlag(true);
        } else {
            setNextFlag(false);
        }
    }

    /**
     * handles pagination of AdoptionOrders approval and print data
     *
     * @return String
     */
    public String loadPreviousRecords() {
        logger.debug("requested previous records current pageNo : {} ", pageNo);
        if (previousFlag && getPageNo() == 2) {
            /**
             * request is comming backword(calls previous
             * to load the very first page
             */
            setPreviousFlag(false);
        } else if (getPageNo() == 1) {
            /**
             * if request is from page one
             * in the next page previous link
             * should be displayed
             */
            setPreviousFlag(false);
        } else {
            setPreviousFlag(true);
        }
        setNextFlag(true);
        if (getPageNo() > 1) {
            setPageNo(getPageNo() - 1);
        }
        noOfRows = appParametersDAO.getIntParameter(ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (currentStatus != null) {
            adoptionApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, currentStatus, user);
        } else {
            adoptionApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        }
        populate();
        initPermissionForApprovalAndPrint();
        return SUCCESS;
    }

    public String loadNextRecords() {
        logger.debug("requested next records current pageNo : {} ", pageNo);
        setPageNo(getPageNo() + 1);

        noOfRows = appParametersDAO.getIntParameter(ADOPTION_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (currentStatus != null) {
            adoptionApprovalAndPrintList = service.getPaginatedListForState(pageNo, noOfRows, currentStatus, user);
        } else {
            adoptionApprovalAndPrintList = service.getPaginatedListForAll(pageNo, noOfRows, user);
        }
        paginationHandler(adoptionApprovalAndPrintList.size());
        setPreviousFlag(true);
        populate();
        initPermissionForApprovalAndPrint();
        return SUCCESS;
    }

    /**
     * responsible for approving requested adoption
     * based on the idUKey
     *
     * @return
     */
    public String approveAdoption() {
        logger.debug("requested to approve AdoptionOrder with idUKey : {}", idUKey);
        try {
            service.approveAdoptionOrder(getIdUKey(), user);
        } catch (CRSRuntimeException e) {
            addActionError(getText("adoption.error.no.permission"));
        }
        adoptionApprovalAndPrintList = service.findAll(user);
        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    /**
     * reject the AdoptionOrder based on requested
     * idUKey
     *
     * @return
     */
    public String rejectAdoption() {
        logger.debug("requested to reject AdoptionOrder with idUKey : {}", idUKey);
        try {
            service.rejectAdoptionOrder(idUKey, user);
        } catch (CRSRuntimeException e) {
            addActionError(getText("adoption.error.no.permission"));
        }
        adoptionApprovalAndPrintList = service.findAll(user);
        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    /**
     * delete the AdoptionOrder based on requested
     * idUKey
     *
     * @return
     */

    public String deleteAdoption() {
        logger.debug("requested to delete AdoptionOrder with idUKey : {}", idUKey);
        service.deleteAdoptionOrder(idUKey, user);
        adoptionApprovalAndPrintList = service.findAll(user);
        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    /**
     * capture certificate applicant data and chage adoption order state in to CERTIFICATE_ISSUE_REQUEST_CAPTURED
     *
     * @return SUCCESS if successfully captured data
     */
    public String adoptionApplicantInfo() {
        //todo check pageNo value
        if (pageNo == 1) {
            //set type
            adoption = (AdoptionOrder) session.get(WebConstants.SESSION_ADOPTION_ORDER);
            adoption.setCertificateApplicantAddress(certificateApplicantAddress);
            adoption.setCertificateApplicantName(certificateApplicantName);
            adoption.setCertificateApplicantPINorNIC(certificateApplicantPINorNIC);
            adoption.setCertificateApplicantType(certificateApplicantType);
            service.setApplicantInfo(adoption, user);
            session.remove(WebConstants.SESSION_ADOPTION_ORDER);
        }
        return SUCCESS;
    }

    public String adoptionCertificate() {
        try {
            adoption = service.getById(getIdUKey(), user);
            logger.debug("Id u key : {}", getIdUKey());
        } catch (Exception e) {
            logger.debug("catch exception : {}", e);
        }
        //adoption.setCertificateApplicantAddress(certificateApplicantAddress);
        //changing state
        //adoption.setCurrentStatus(AdoptionOrder.State.CERTIFICATE_ISSUE_REQUEST_CAPTURED);
        //logger.info(adoption.getCertificateApplicantType().name());
        //service.updateAdoptionOrder(adoption, user);
        //session.remove(WebConstants.SESSION_ADOPTION_ORDER);
        return SUCCESS;
    }

    public String test() {
        return SUCCESS;
    }

    public void initPermissionForApprovalAndPrint() {

        allowApproveAdoption = user.isAuthorized(Permission.APPROVE_ADOPTION);
        allowEditAdoption = user.isAuthorized(Permission.EDIT_ADOPTION);

    }

    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        populateBasicLists(language);
        populateDynamicLists(language);
    }

    public String populateAdoption() {
        adoption = service.getByCourtOrderNumber(courtOrderNo, user);
        if (adoption != null) {
            session.put(WebConstants.SESSION_ADOPTION_ORDER, adoption);
        } else {
            addActionError(getText("adoption_order_notfound.message"));
        }
        return SUCCESS;
    }

    private void populateBasicLists(String language) {
        countryList = countryDAO.getCountries(language);
        districtList = districtDAO.getAllDistrictNames(language, user);
    }

    private void populateDynamicLists(String language) {
        if (birthDistrictId == 0) {
            if (!districtList.isEmpty()) {
                birthDistrictId = districtList.keySet().iterator().next();
                logger.debug("first allowed district in the list {} was set", birthDistrictId);
            }
        }
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, language, user);

        if (dsDivisionId == 0) {
            if (!dsDivisionList.isEmpty()) {
                dsDivisionId = dsDivisionList.keySet().iterator().next();
                logger.debug("first allowed DS Div in the list {} was set", dsDivisionId);
            }
        }

        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        if (birthDivisionId == 0) {
            birthDivisionId = bdDivisionList.keySet().iterator().next();
            logger.debug("first allowed BD Div in the list {} was set", birthDivisionId);
        }
    }

    public void setBirthDistrictId(int birthDistrictId) {
        this.birthDistrictId = birthDistrictId;
    }

    public void setBirthDivisionId(int birthDivisionId) {
        this.birthDivisionId = birthDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public void setDsDivisionList(Map<Integer, String> dsDivisionList) {
        this.dsDivisionList = dsDivisionList;
    }

    public void setBdDivisionList(Map<Integer, String> bdDivisionList) {
        this.bdDivisionList = bdDivisionList;
    }

    public int getBirthDistrictId() {
        return birthDistrictId;
    }

    public int getBirthDivisionId() {
        return birthDivisionId;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public Map<Integer, String> getBdDivisionList() {
        return bdDivisionList;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setSession(Map map) {
        logger.debug("Set session {}", map);
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("setting User: {}", user.getUserName());
    }

    public Map getSession() {
        return session;
    }

    public AdoptionOrder getAdoption() {
        return adoption;
    }

    public void setAdoption(AdoptionOrder adoption) {
        this.adoption = adoption;
    }

    public String getCourtOrderNo() {
        return courtOrderNo;
    }

    public void setCourtOrderNo(String courtOrderNo) {
        this.courtOrderNo = courtOrderNo;
    }


    public List<AdoptionOrder> getAdoptionApprovalAndPrintList() {
        return adoptionApprovalAndPrintList;
    }

    public void setAdoptionApprovalAndPrintList(List<AdoptionOrder> adoptionApprovalAndPrintList) {
        this.adoptionApprovalAndPrintList = adoptionApprovalAndPrintList;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<Integer, String> getCountryList() {
        return countryList;
    }

    public void setCountryList(Map<Integer, String> countryList) {
        this.countryList = countryList;
    }

    public int getPageNumber() {
        return pageNo;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNo = pageNumber;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
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

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public String getDsDivisionName() {
        return dsDivisionName;
    }

    public void setDsDivisionName(String dsDivisionName) {
        this.dsDivisionName = dsDivisionName;
    }

    public String getBirthDivisionName() {
        return birthDivisionName;
    }

    public void setBirthDivisionName(String birthDivisionName) {
        this.birthDivisionName = birthDivisionName;
    }

    public String getApplicantCountryName() {
        return applicantCountryName;
    }

    public void setApplicantCountryName(String applicantCountryName) {
        this.applicantCountryName = applicantCountryName;
    }

    public String getWifeCountryName() {
        return wifeCountryName;
    }

    public void setWifeCountryName(String wifeCountryName) {
        this.wifeCountryName = wifeCountryName;
    }

    public String getBirthDistrictName() {
        return birthDistrictName;
    }

    public void setBirthDistrictName(String birthDistrictName) {
        this.birthDistrictName = birthDistrictName;
    }

    public String getCertificateApplicantAddress() {
        return certificateApplicantAddress;
    }

    public void setCertificateApplicantAddress(String certificateApplicantAddress) {
        this.certificateApplicantAddress = certificateApplicantAddress;
    }

    public String getCertificateApplicantPINorNIC() {
        return certificateApplicantPINorNIC;
    }

    public void setCertificateApplicantPINorNIC(String certificateApplicantPINorNIC) {
        this.certificateApplicantPINorNIC = certificateApplicantPINorNIC;
    }

    public String getCertificateApplicantName() {
        return certificateApplicantName;
    }

    public void setCertificateApplicantName(String certificateApplicantName) {
        this.certificateApplicantName = certificateApplicantName;
    }

    public AdoptionOrder.ApplicantType getCertificateApplicantType() {
        return certificateApplicantType;
    }

    public void setCertificateApplicantType(AdoptionOrder.ApplicantType certificateApplicantType) {
        this.certificateApplicantType = certificateApplicantType;
    }

    public boolean isAlreadyPrinted() {
        return alreadyPrinted;
    }

    public void setAlreadyPrinted(boolean alreadyPrinted) {
        this.alreadyPrinted = alreadyPrinted;
    }

    public boolean isNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(boolean nextFlag) {
        this.nextFlag = nextFlag;
    }

    public boolean isPreviousFlag() {
        return previousFlag;
    }

    public void setPreviousFlag(boolean previousFlag) {
        this.previousFlag = previousFlag;
    }

    public AdoptionOrder.State getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(AdoptionOrder.State currentStatus) {
        this.currentStatus = currentStatus;
    }
}
