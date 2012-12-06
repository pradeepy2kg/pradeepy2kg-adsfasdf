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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
            adoptionAlterationService.addAdoptionAlteration(adoptionAlteration, user);
            logger.debug("Adoption Alteration added successfully. {}", adoptionAlteration.getIdUKey());
        } catch (CRSRuntimeException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            addActionError(getText(e.getMessage()+".label"));
            return ERROR;
        }
        addActionMessage(getText("add.adoption.alteration.success"));
        return SUCCESS;
    }

    public String updateAdoptionAlteration() {
        logger.debug("Attempt to update Adoption Alteration");

        return SUCCESS;
    }

    public String approveOrRejectAdoptionAlteration() {
        return SUCCESS;
    }

    public String loadAdoptionRecordsForAlteration() {
        logger.debug("Adoption Entry No: {}", adoptionEntryNo);
        adoptionOrderList = new ArrayList<AdoptionOrder>();
        if (adoptionEntryNo > 0) {
            adoptionOrderList.add(adoptionOrderService.getAdoptionByEntryNumber(adoptionEntryNo));
        } else if (courtOrderNo != null && !courtOrderNo.isEmpty()) {
            adoptionOrderList.addAll(adoptionOrderService.getAdoptionOrdersByCourtOrderNumber(courtOrderNo));
        }
        if (adoptionOrderList.size() > 0) {
            logger.debug("Loading adoption records [{}]for alterations.", adoptionOrderList.size());
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

                if (adoption.getBirthProvinceUKey() > 0) {
                    birthProvinceName = provinceDAO.getNameByPK(adoption.getBirthProvinceUKey(), language);
                }
                if (adoption.getBirthDistrictId() > 0) {
                    birthDistrictName = districtDAO.getNameByPK(adoption.getBirthDistrictId(), language);
                }

                if (adoption.getApplicantCountryId() > 0) {
                    applicantCountryName = countryDAO.getNameByPK(adoption.getApplicantCountryId(), language);
                }
                if (adoption.getSpouseCountryId() > 0) {
                    spouseCountryName = countryDAO.getNameByPK(adoption.getSpouseCountryId(), language);
                }
                if (adoption.getCourt().getCourtId() > 0) {
                    courtName = courtDAO.getNameByPK(adoption.getCourt().getCourtId(), language);
                }
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
}
