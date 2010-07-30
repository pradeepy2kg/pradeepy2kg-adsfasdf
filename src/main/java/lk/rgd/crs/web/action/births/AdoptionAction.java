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
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.web.WebConstants;

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

    private int birthDistrictId;
    private int birthDivisionId;
    private int dsDivisionId;
    private String language;

    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;
    private List<AdoptionOrder> adoptionPendingApprovalList;

    private AdoptionOrder adoption;
    private User user;
    private Map session;

    private long idUKey;
    //registering adoption


    //requesting for certificate
    private String courtOrderNo;
    private String certificateApplicantAddress;
    private String certificateApplicantName;
    private String certificateApplicantPassportNo;
    private String certifcateApplicantCountry;
    private String certifcateApplicantPin;
    private String certificateApplicantType;
    private AdoptionOrder adoptionOrder;

    public AdoptionAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO,
                          AdoptionOrderService service) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
    }

    public String adoptionAction() {
        adoption.setStatus(AdoptionOrder.State.DATA_ENTRY);
        adoption.setApplicantMother(true);
        User currentUser = (User) session.get(WebConstants.SESSION_USER_BEAN);
        service.addAdoptionOrder(adoption, currentUser);
        return SUCCESS;
    }

    public String adoptionDeclaration() {
        populate();
        return SUCCESS;
    }

    public String initAdoptionReRegistration() {
        AdoptionOrder adoption;
        if (idUKey != 0) {
            try {
                adoption = service.getById(idUKey, user);
                logger.debug("Id u key : {}", idUKey);
            } catch (Exception e) {
                logger.debug("catch exception : {}", e);
            }
        } else {
            logger.debug("idUkey is zero");
        }
        return SUCCESS;
    }

    public String adoptionReRegistration() {
        return SUCCESS;
    }

    public String adoptionApprovalAndPrint() {
        //todo this is a mock method real backend is not implemented yet
        populate();
        adoptionPendingApprovalList = service.findAll(user);
        return SUCCESS;
    }

    public String adoptionApplicantInfo() {
        return SUCCESS;
    }

    public String adoptionCertificate() {
        try {
            adoption = service.getById(idUKey, user);
            logger.debug("Id u key : {}", idUKey);
        } catch (Exception e) {
            logger.debug("catch exception : {}", e);
        }
        adoption.setCertificateApplicantAddress(certificateApplicantAddress);
        //changing state
        adoption.setStatus(AdoptionOrder.State.CERTIFICATE_ISSUE_REQUEST_CAPTURED);
        logger.info(adoption.getCertificateApplicantType().name());
        service.updateAdoptionOrder(adoption, user);
        session.remove(WebConstants.SESSION_ADOPTION_ORDER);
        return SUCCESS;
    }

    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        populateBasicLists(language);
        populateDynamicLists(language);
    }

    public String populateAdoption() {
        //todo replace this with getAdoptionBySerialnumber
        adoption = service.getById(1, user);
        session.put(WebConstants.SESSION_ADOPTION_ORDER, adoption);
        return SUCCESS;
    }

    private void populateBasicLists(String language) {
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

    public String getCertificateApplicantName() {
        return certificateApplicantName;
    }

    public void setCertificateApplicantName(String certificateApplicantName) {
        this.certificateApplicantName = certificateApplicantName;
    }

    public String getCertificateApplicantAddress() {
        return certificateApplicantAddress;
    }

    public void setCertificateApplicantAddress(String certificateApplicantAddress) {
        this.certificateApplicantAddress = certificateApplicantAddress;
    }

    public String getCertificateApplicantPassportNo() {
        return certificateApplicantPassportNo;
    }

    public void setCertificateApplicantPassportNo(String certificateApplicantPassportNo) {
        this.certificateApplicantPassportNo = certificateApplicantPassportNo;
    }

    public String getCertifcateApplicantCountry() {
        return certifcateApplicantCountry;
    }

    public void setCertifcateApplicantCountry(String certifcateApplicantCountry) {
        this.certifcateApplicantCountry = certifcateApplicantCountry;
    }

    public String getCertifcateApplicantPin() {
        return certifcateApplicantPin;
    }

    public void setCertifcateApplicantPin(String certifcateApplicantPin) {
        this.certifcateApplicantPin = certifcateApplicantPin;
    }

    public String getCertificateApplicantType() {
        return certificateApplicantType;
    }

    public void setCertificateApplicantType(String certificateApplicantType) {
        this.certificateApplicantType = certificateApplicantType;
    }

    public AdoptionOrder getAdoptionOrder() {
        return adoptionOrder;
    }

    public void setAdoptionOrder(AdoptionOrder adoptionOrder) {
        this.adoptionOrder = adoptionOrder;
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
}
