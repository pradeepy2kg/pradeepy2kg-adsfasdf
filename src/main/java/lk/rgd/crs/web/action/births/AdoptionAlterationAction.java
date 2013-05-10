package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.ProvinceDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.CourtDAO;
import lk.rgd.crs.api.domain.AdoptionAlteration;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.api.service.AdoptionAlterationService;
import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.web.WebConstants;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Duminda Dharmakeerthi
 */
public class AdoptionAlterationAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionAlterationAction.class);

    private Map session;
    private User user;
    private String language;

    private Map<Integer, String> provinceList;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> allDSDivisionList;
    private Map<Integer, String> courtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> zonalOfficeList;

    private final AdoptionOrderService adoptionOrderService;
    private final AdoptionAlterationService adoptionAlterationService;
    private final ProvinceDAO provinceDAO;
    private final DistrictDAO districtDAO;
    private final CountryDAO countryDAO;
    private final CourtDAO courtDAO;

    private AdoptionOrder adoption;
    private AdoptionAlteration adoptionAlteration;

    private List<AdoptionOrder> adoptionOrderList;
    private List<AdoptionAlteration> adoptionAlterationList;

    private String courtOrderNo;
    private long adoptionEntryNo;
    private long idUKey;
    private String courtName;
    private String birthProvinceName;
    private String birthDistrictName;
    private String applicantCountryName;
    private String spouseCountryName;
    private AdoptionAlteration.Method alterationMethod;
    private AdoptionAlteration.Method[] methodList = AdoptionAlteration.Method.values();
    private boolean editMode;
    private int[] approvedIndex;
    private int courtUKey;

    public AdoptionAlterationAction(AdoptionOrderService adoptionOrderService, AdoptionAlterationService adoptionAlterationService, CourtDAO courtDAO, CountryDAO countryDAO, DistrictDAO districtDAO, ProvinceDAO provinceDAO) {
        this.adoptionOrderService = adoptionOrderService;
        this.adoptionAlterationService = adoptionAlterationService;
        this.courtDAO = courtDAO;
        this.countryDAO = countryDAO;
        this.districtDAO = districtDAO;
        this.provinceDAO = provinceDAO;
    }

    public String pageLoad() {
        return SUCCESS;
    }

    public String addAdoptionAlteration() {
        logger.debug("Attempt to add Adoption Alteration.");
        try {
            if (AdoptionAlteration.Method.BY_COURT_ORDER.equals(adoptionAlteration.getMethod())) {
                adoptionAlteration.setCourt(courtDAO.getCourt(courtUKey));
            }
            adoptionAlterationService.addAdoptionAlteration(adoptionAlteration, user);
            addActionMessage(getText("add.adoption.alteration.success"));
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            addActionError(getText("unknown.error.label"));
            idUKey = adoptionAlteration.getAoUKey();
            populateAdoptionOrder();
            return ERROR;
        }
        return SUCCESS;
    }

    public String updateAdoptionAlteration() {
        logger.debug("Attempt to update Adoption Alteration : {}", idUKey);
        try {
            AdoptionAlteration existing = adoptionAlterationService.getAdoptionAlterationByIdUKey(adoptionAlteration.getIdUKey());
            populateAdoptionAlterationForUpdate(adoptionAlteration, existing);
            if (AdoptionAlteration.Method.BY_COURT_ORDER.equals(adoptionAlteration.getMethod())) {
                adoptionAlteration.setCourt(courtDAO.getCourt(courtUKey));
            }
            adoptionAlterationService.updateAdoptionAlteration(adoptionAlteration, user);
            addActionMessage(getText("update.adoption.alteration.success"));
        } catch (Exception e) {
            e.printStackTrace();
            addActionError(getText("unknown.error.label"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String deleteAdoptionAlteration() {
        logger.debug("Attempt to delete Adoption Alteration : {}", idUKey);
        try {
            AdoptionAlteration existing = adoptionAlterationService.getAdoptionAlterationByIdUKey(idUKey);
            adoptionAlterationService.deleteAdoptionAlteration(existing, user);
            addActionMessage(getText("delete.adoption.alteration.label"));
        } catch (Exception e) {
            e.printStackTrace();
            addActionError(getText("unknown.error.label"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String approveAdoptionAlteration() {
        logger.debug("Attempt to approve Adoption Alteration with {}", approvedIndex);
        adoptionAlteration = adoptionAlterationService.getAdoptionAlterationByIdUKey(idUKey);
        try {
            BitSet approvedBits = new BitSet();
            if (approvedIndex != null) {
                for (int bit : approvedIndex) {
                    logger.debug("Bit {} ", bit);
                    approvedBits.set(bit, true);
                }
            }
            adoptionAlteration.setApprovalStatuses(approvedBits);
            adoptionAlterationService.approveAdoptionAlteration(adoptionAlteration, user);
            addActionMessage(getText("approve.adoption.alteration.success.label"));
        } catch (CRSRuntimeException e) {
            logger.error(e.getMessage());
            addActionError(getText("Error occurred."));
            return ERROR;
        }
        return SUCCESS;
    }

    public String rejectAdoptionAlteration() {
        logger.debug("Attempt to reject Adoption Alteration");
        adoptionAlteration = adoptionAlterationService.getAdoptionAlterationByIdUKey(idUKey);
        try {
            adoptionAlterationService.rejectAdoptionAlteration(adoptionAlteration, user);
            addActionMessage(getText("reject.adoption.alteration.success.label"));
        } catch (CRSRuntimeException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            addActionError(getText("unknown.error.label"));
        }
        return SUCCESS;
    }

    public String loadAdoptionAlterationForApproval() {
        logger.debug("Loading Adoption Alterations for approval");
        adoptionAlterationList = adoptionAlterationService.getAdoptionAlterationsForApproval(user);
        return SUCCESS;
    }

    public String loadAdoptionRecordsForAlteration() {
        logger.debug("Adoption Entry No: {}", adoptionEntryNo);
        adoptionOrderList = new ArrayList<AdoptionOrder>();
        if (adoptionEntryNo > 0) {
            AdoptionOrder adoptionOrder = adoptionOrderService.getAdoptionByEntryNumberForAlteration(adoptionEntryNo);
            if (adoptionOrder != null) {
                adoptionOrderList.add(adoptionOrder);
            }
        }
        if (courtOrderNo != null && !courtOrderNo.isEmpty()) {
            List<AdoptionOrder> adoptionOrders = adoptionOrderService.getAdoptionsByCourtOrderNumberForAlterations(courtOrderNo);
            if (adoptionOrders.size() > 0) {
                adoptionOrderList.addAll(adoptionOrders);
            }
        }
        adoptionEntryNo = 0;
        courtOrderNo = null;
        if (adoptionOrderList.size() > 0) {
            return SUCCESS;
        }
        addActionError(getText("no.adoption.record.found.label"));
        return ERROR;
    }

    public String populateAdoptionOrder() {
        logger.debug("Loading Adoption Order with idUKey : {} for Alteration", idUKey);
        try {
            adoption = adoptionOrderService.getById(idUKey, user);
            adoptionAlteration = new AdoptionAlteration();
            if (adoption != null) {
                adoptionAlteration.setAoUKey(adoption.getIdUKey());
                adoptionAlteration.setMethod(alterationMethod);
                adoptionAlteration.setApplicantName(adoption.getApplicantName());
                adoptionAlteration.setApplicantAddress(adoption.getApplicantAddress());
                if (adoption.getApplicantSecondAddress() != null) {
                    adoptionAlteration.setApplicantSecondAddress(adoption.getApplicantSecondAddress());
                }
                if (adoption.getApplicantOccupation() != null) {
                    adoptionAlteration.setApplicantOccupation(adoption.getApplicantOccupation());
                }
                if (adoption.isJointApplicant()) {
                    adoptionAlteration.setSpouseName(adoption.getSpouseName());
                    if (adoption.getSpouseOccupation() != null) {
                        adoptionAlteration.setSpouseOccupation(adoption.getSpouseOccupation());
                    }
                }
                if (adoption.getChildNewName() != null && !adoption.getChildNewName().isEmpty()) {
                    adoptionAlteration.setChildName(adoption.getChildNewName());
                } else {
                    adoptionAlteration.setChildName(adoption.getChildExistingName());
                }
                adoptionAlteration.setChildGender(adoption.getChildGender());
                adoptionAlteration.setChildBirthDate(adoption.getChildBirthDate());
                adoptionAlteration.setMethod(alterationMethod);

                if (AdoptionAlteration.Method.BY_COURT_ORDER.equals(alterationMethod)) {
                    if (adoption.getCourt().getCourtUKey() > 0) {
                        adoptionAlteration.setCourt(adoption.getCourt());
                        adoptionAlteration.setCourtOrderNumber(adoption.getCourtOrderNumber());
                        adoptionAlteration.setOrderDate(adoption.getOrderIssuedDate());
                    }
                }
                if (adoption.getCourt() != null && adoption.getCourt().getCourtUKey() > 0) {
                    courtName = courtDAO.getNameByPK(adoption.getCourt().getCourtUKey(), language);
                }
                populateBasicLists();
                logger.debug("Success");
                return SUCCESS;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ERROR;
        }
        return ERROR;
    }

    public String populateAdoptionAlteration() {
        logger.debug("Loading Adoption Alteration : {}", idUKey);
        try {
            adoptionAlteration = adoptionAlterationService.getAdoptionAlterationByIdUKey(idUKey);
            if (adoptionAlteration != null) {
                adoption = adoptionOrderService.getById(adoptionAlteration.getAoUKey(), user);
                if (adoption != null && adoption.getCourt() != null) {
                    courtName = courtDAO.getNameByPK(adoption.getCourt().getCourtUKey(), language);
                }
                populateBasicLists();
                return SUCCESS;
            }
            loadAdoptionAlterationForApproval();
            addActionError(getText("unable.to.load.adoption.alteration.record.label"));
            return ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    private void populateAdoptionAlterationForUpdate(AdoptionAlteration alteration, AdoptionAlteration existing) {
        alteration.getLifeCycleInfo().setCreatedTimestamp(existing.getLifeCycleInfo().getCreatedTimestamp());
        alteration.getLifeCycleInfo().setCreatedUser(existing.getLifeCycleInfo().getCreatedUser());
        alteration.setMethod(existing.getMethod());
        alteration.setStatus(existing.getStatus());
    }

    private void populateBasicLists() {
        courtList = courtDAO.getCourtNames(language);
        if (adoptionAlteration != null && adoptionAlteration.getCourt() != null) {
            courtUKey = adoptionAlteration.getCourt().getCourtUKey();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public AdoptionOrder getAdoption() {
        return adoption;
    }

    public void setAdoption(AdoptionOrder adoption) {
        this.adoption = adoption;
    }

    public AdoptionAlteration getAdoptionAlteration() {
        return adoptionAlteration;
    }

    public void setAdoptionAlteration(AdoptionAlteration adoptionAlteration) {
        this.adoptionAlteration = adoptionAlteration;
    }

    public Map<Integer, String> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(Map<Integer, String> provinceList) {
        this.provinceList = provinceList;
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

    public Map<Integer, String> getAllDSDivisionList() {
        return allDSDivisionList;
    }

    public void setAllDSDivisionList(Map<Integer, String> allDSDivisionList) {
        this.allDSDivisionList = allDSDivisionList;
    }

    public Map<Integer, String> getCourtList() {
        return courtList;
    }

    public void setCourtList(Map<Integer, String> courtList) {
        this.courtList = courtList;
    }

    public Map<Integer, String> getCountryList() {
        return countryList;
    }

    public void setCountryList(Map<Integer, String> countryList) {
        this.countryList = countryList;
    }

    public Map<Integer, String> getZonalOfficeList() {
        return zonalOfficeList;
    }

    public void setZonalOfficeList(Map<Integer, String> zonalOfficeList) {
        this.zonalOfficeList = zonalOfficeList;
    }

    public void setSession(Map map) {
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("setting User: {} and language : {}", user.getUserName(), language);
    }

    public Map getSession() {
        return session;
    }

    public String getCourtOrderNo() {
        return courtOrderNo;
    }

    public void setCourtOrderNo(String courtOrderNo) {
        this.courtOrderNo = courtOrderNo;
    }

    public long getAdoptionEntryNo() {
        return adoptionEntryNo;
    }

    public void setAdoptionEntryNo(long adoptionEntryNo) {
        this.adoptionEntryNo = adoptionEntryNo;
    }

    public List<AdoptionOrder> getAdoptionOrderList() {
        return adoptionOrderList;
    }

    public void setAdoptionOrderList(List<AdoptionOrder> adoptionOrderList) {
        this.adoptionOrderList = adoptionOrderList;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getBirthProvinceName() {
        return birthProvinceName;
    }

    public void setBirthProvinceName(String birthProvinceName) {
        this.birthProvinceName = birthProvinceName;
    }

    public String getBirthDistrictName() {
        return birthDistrictName;
    }

    public void setBirthDistrictName(String birthDistrictName) {
        this.birthDistrictName = birthDistrictName;
    }

    public String getApplicantCountryName() {
        return applicantCountryName;
    }

    public void setApplicantCountryName(String applicantCountryName) {
        this.applicantCountryName = applicantCountryName;
    }

    public String getSpouseCountryName() {
        return spouseCountryName;
    }

    public void setSpouseCountryName(String spouseCountryName) {
        this.spouseCountryName = spouseCountryName;
    }

    public AdoptionAlteration.Method getAlterationMethod() {
        return alterationMethod;
    }

    public void setAlterationMethod(AdoptionAlteration.Method alterationMethod) {
        this.alterationMethod = alterationMethod;
    }

    public AdoptionAlteration.Method[] getMethodList() {
        return methodList;
    }

    public void setMethodList(AdoptionAlteration.Method[] methodList) {
        this.methodList = methodList;
    }

    public List<AdoptionAlteration> getAdoptionAlterationList() {
        return adoptionAlterationList;
    }

    public void setAdoptionAlterationList(List<AdoptionAlteration> adoptionAlterationList) {
        this.adoptionAlterationList = adoptionAlterationList;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public int[] getApprovedIndex() {
        return approvedIndex;
    }

    public void setApprovedIndex(int[] approvedIndex) {
        this.approvedIndex = approvedIndex;
    }

    public int getCourtUKey() {
        return courtUKey;
    }

    public void setCourtUKey(int courtUKey) {
        this.courtUKey = courtUKey;
    }
}
