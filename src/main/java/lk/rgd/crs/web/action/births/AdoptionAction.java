package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Locale;
import java.util.List;
import java.util.Date;

import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.Permission;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Duminda Dharmakeerthi
 * @author amith jayasekaa
 */
@SuppressWarnings({"ALL"})
public class AdoptionAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionAction.class);
    private final AdoptionOrderService service;
    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final CountryDAO countryDAO;

    private int birthDistrictId;
    private int birthDivisionId;
    private int dsDivisionId;
    private String language;

    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;
    private List<AdoptionOrder> adoptionPendingApprovalList;
    private Map<Integer, String> countryList;

    private AdoptionOrder adoption;
    private User user;
    private Map session;

    private long idUKey;
    private int pageNo;
    private String courtOrderNo;
    private boolean allowEditAdoption;
    private boolean allowApproveAdoption;

    public AdoptionAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO,
                          AdoptionOrderService service, CountryDAO countryDAO) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.countryDAO = countryDAO;
    }

    public String adoptionAction() {
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        //AdoptionOrder adoption;
        //adoption = (AdoptionOrder) session.get(WebConstants.SESSION_ADOPTION_ORDER);
        adoption.setStatus(AdoptionOrder.State.DATA_ENTRY);
        //courtOrderNo=adoption.getCourtOrderNumber();
        service.addAdoptionOrder(adoption, user);
        logger.debug("IdUkey : {} ",idUKey);
        return SUCCESS;
    }

    public String adoptionDeclaration() {
        logger.debug("Adoption declaration ok");
        populate();
        return SUCCESS;
    }

    public String initAdoptionReRegistration() {
        logger.debug("Adoption reregistration for IdUkey : {}",idUKey);
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
        //todo this is a mock method real backend is not implemented yet
        populate();
        initPermissionForApprovalAndPrint();
        adoptionPendingApprovalList = service.findAll(user);
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
        adoptionPendingApprovalList = service.findAll(user);
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
        adoptionPendingApprovalList = service.findAll(user);
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
        adoptionPendingApprovalList = service.findAll(user);
        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    public String adoptionApplicantInfo() {
        //todo check pageNo value
        if (pageNo == 1) {
            AdoptionOrder adpt = (AdoptionOrder) session.get(WebConstants.SESSION_ADOPTION_ORDER);
            //todo remove this to JSP level
            service.updateAdoptionOrder(adpt, user);
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
        session.put(WebConstants.SESSION_ADOPTION_ORDER, adoption);
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


    public List<AdoptionOrder> getAdoptionPendingApprovalList() {
        return adoptionPendingApprovalList;
    }

    public void setAdoptionPendingApprovalList(List<AdoptionOrder> adoptionPendingApprovalList) {
        this.adoptionPendingApprovalList = adoptionPendingApprovalList;
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
}
