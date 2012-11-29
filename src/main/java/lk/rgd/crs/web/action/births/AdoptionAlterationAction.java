package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.domain.User;
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

    private AdoptionOrder adoptionOrder;
    private AdoptionAlteration adoptionAlteration;

    private List<AdoptionOrder> adoptionOrderList;
    private String courtOrderNo;
    private long adoptionEntryNo;
    private long idUKey;

    public AdoptionAlterationAction(AdoptionOrderService adoptionOrderService, AdoptionAlterationService adoptionAlterationService) {
        this.adoptionOrderService = adoptionOrderService;
        this.adoptionAlterationService = adoptionAlterationService;
    }

    public String pageLoad(){
        return SUCCESS;
    }

    public String addAdoptionAlteration(){
        return SUCCESS;
    }

    public String updateAdoptionAlteration(){
        return SUCCESS;
    }

    public String approveOrRejectAdoptionAlteration(){
        return SUCCESS;
    }

    public String loadAdoptionRecordsForAlteration(){
        logger.debug("Adoption Entry No: {}", adoptionEntryNo);
        adoptionOrderList = new ArrayList<AdoptionOrder>();
        if(adoptionEntryNo > 0){
            adoptionOrderList.add(adoptionOrderService.getAdoptionByEntryNumber(adoptionEntryNo));
        }else if(courtOrderNo != null && !courtOrderNo.isEmpty()){
            adoptionOrderList.addAll(adoptionOrderService.getAdoptionOrdersByCourtOrderNumber(courtOrderNo));
        }
        if(adoptionOrderList.size() > 0){
            logger.debug("Loading adoption records [{}]for alterations.", adoptionOrderList.size());
            return SUCCESS;
        }
        addActionError(getText("no.adoption.record.found.label"));
        return ERROR;
    }

    public String populateAdoptionOrder(){
        logger.debug("Loading Adoption Order with idUKey : {} for Alteration", idUKey);
        adoptionOrder = adoptionOrderService.getById(idUKey, user);
        if(adoptionOrder != null){
            return SUCCESS;
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

    public AdoptionOrder getAdoptionOrder() {
        return adoptionOrder;
    }

    public void setAdoptionOrder(AdoptionOrder adoptionOrder) {
        this.adoptionOrder = adoptionOrder;
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
}
