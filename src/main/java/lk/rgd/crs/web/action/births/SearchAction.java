package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.CertificateSearch;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.service.CertificateSearchService;
import lk.rgd.crs.web.WebConstants;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Indunil Moremada
 * @author Chathuranga Withana
 *         Struts Action Class for Search puposes
 */
public class SearchAction extends ActionSupport implements SessionAware {
    private static final Logger logger = LoggerFactory.getLogger(SearchAction.class);

    private final BirthRegistrationService service;
    private final CertificateSearchService certificateSearchService;
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final BDDivisionDAO bdDivisionDAO;

    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private List searchResultList;
    private Map session;
    private User user;
    private BirthDeclaration bdf;
    private CertificateSearch certSearch;
    private CertificateSearch.CertificateType certificateType;

    private int birthDistrictId;
    private int dsDivisionId;
    private int birthDivisionId;

    private Long serialNo;
    private Long idUKey;
    private String childName;
    private String status;
    private int pageNo;

    public SearchAction(BirthRegistrationService service, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO,
        BDDivisionDAO bdDivisionDAO, CertificateSearchService certificateSearchService) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.certificateSearchService = certificateSearchService;
    }

    public String welcome() {
        if (pageNo == 4) {
            session.remove(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
            session.remove(WebConstants.SESSION_OLD_BD_FOR_ADOPTION);
        }
        populate();
        return SUCCESS;
    }

    public String advancedSearch() {
        return SUCCESS;
    }

    /**
     * This method responsible for searching  Birth declaration based on serialNo, district and bdDivision. If serialNo
     * is set to  0 search is done based on the birthDivision
     *
     * @return String
     */
    public String searchBDFBySerialNumber() {
        if (logger.isDebugEnabled()) {
            logger.debug("attempt to search birth record by serial number :" + serialNo + "and birth division :" +
                birthDivisionId + " and ds division id :" + dsDivisionId);
        }
        try {
            if (serialNo != null) {
                if (birthDivisionId != 0) {
                    //search by birth division and the serial number
                    bdf = service.getActiveRecordByBDDivisionAndSerialNo(
                        bdDivisionDAO.getBDDivisionByPK(birthDivisionId), serialNo, user);
                } else {
                    //search by ds division
                    searchResultList = service.getActiveRecordByDSDivisionAndSerialNumber(serialNo, dsDivisionId, user);
                }
                if (bdf == null & (searchResultList != null && searchResultList.size() == 0)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("no result found for birth registration serial number :" + serialNo +
                            " and birth division id :" + birthDistrictId + " and ds division id : " + dsDivisionId);
                    }
                    addActionMessage(getText("SearchBDF.error.NoResult.for.serial", new String[]{Long.toString(serialNo)}));
                }
            }

        } catch (CRSRuntimeException e) {
            logger.error("inside searchBDFBySerialNumber() ", e);
            addActionError(getText("SearchBDF.error." + e.getErrorCode()));
        }
        populate();
        return SUCCESS;
    }

    /**
     * Populate master data to the UI
     */
    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("inside populate() : language {} observed ", language);
        setDistrictList(districtDAO.getDistrictNames(language, user));
        //initial birth district id
        if (districtList != null) {
            birthDistrictId = districtList.keySet().iterator().next();
        }
        setBirthDistrictId(birthDistrictId);
        //dsDivisions
        this.dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, language, user);
        //setting bdDivisions
        if (!dsDivisionList.isEmpty()) {
            dsDivisionId = dsDivisionList.keySet().iterator().next();
            bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        }
    }

    /**
     * This method is responsible for searching particular BirthDeclaration based on IdUKey
     *
     * @return
     */
    public String searchBDFByIdUKey() {
        logger.debug("inside searchBDFByIdUKey() : search parameter idUKey {} recieved", idUKey);
        try {
            bdf = service.getById(idUKey, user);
            if (bdf != null) {
                setStatus(bdf.getRegister().getStatus().toString());
            } else {
                logger.debug("inside searchBDFByIdUKey() : No result found for Birth declaration UKey : {}", idUKey);
                addActionMessage(getText("SearchBDF.error.NoResult"));
            }
        } catch (CRSRuntimeException e) {
            logger.error("inside searchBDFByIdUKey() SearchBDFByIdUKey : ", e);
            addActionError(getText("SearchBDF.error." + e.getErrorCode()));
        }
        populate();
        return SUCCESS;
    }

    /**
     * Used to search birth certificates or search of registers flow.
     *
     * @return
     */
    public String certificateSearch() {
        logger.debug("{} certificate search: Page {}", certificateType, pageNo);
        if (pageNo == 1) {
            try {
                // setting Certificate type, DSdivision and BDDivision to Certificate Search 
                certSearch.getCertificate().setDsDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                certSearch.getCertificate().setCertificateType(certificateType);
                if (birthDivisionId != 0 && certSearch.getSearch().getSearchSerialNo() != null) {
                    certSearch.getSearch().setBdDivision(bdDivisionDAO.getBDDivisionByPK(birthDivisionId));
                }

                // validate duplicate application number entering
                boolean validNo = certificateSearchService.isValidCertificateSearchApplicationNo(
                    certSearch.getCertificate().getDsDivision(), certSearch.getCertificate().getApplicationNo());

                if (!validNo) {
                    addFieldError("duplicateApplicationNoError", getText("duplicateApplicationNo.label"));
                    pageNo = 0;
                } else {
                    if (certificateType == CertificateSearch.CertificateType.BIRTH) {
                        searchResultList = certificateSearchService.performBirthCertificateSearch(certSearch, user);
                    } else if (certificateType == CertificateSearch.CertificateType.DEATH) {
                        searchResultList = certificateSearchService.performDeathCertificateSearch(certSearch, user);
                    }
                    logger.debug("Certificate search result size : {}", searchResultList.size());
                    if (searchResultList.size() == 0) {
                        addActionMessage(getText("noItemMsg.label"));
                    }
                }
            } catch (CRSRuntimeException e) {
                logger.error("inside birthCertificateSearch()", e);
                addActionError(getText("CertSearch.error." + e.getErrorCode()) + "\n" + certSearch.getCertificate().getApplicationNo() +
                    " , " + certSearch.getCertificate().getDsDivision().getDsDivisionUKey());
            } catch (Exception e) {
                logger.error("inside birthCertificateSearch()", e);
                return ERROR;
            }
        }
        populate();
        return "page" + pageNo;
    }

    public String markSearchCertificate() {
        // TODO if needed info related to certificate searching
        return SUCCESS;
    }

    public long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(long serialNo) {
        this.serialNo = serialNo;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public void setSession(Map session) {
        this.session = session;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
    }

    public BirthDeclaration getBdf() {
        return bdf;
    }

    public void setBdf(BirthDeclaration bdf) {
        this.bdf = bdf;
    }

    public int getBirthDistrictId() {
        return birthDistrictId;
    }

    public void setBirthDistrictId(int birthDistrictId) {
        this.birthDistrictId = birthDistrictId;
    }

    public Map<Integer, String> getBdDivisionList() {
        return bdDivisionList;
    }

    public void setBdDivisionList(Map<Integer, String> bdDivisionList) {
        this.bdDivisionList = bdDivisionList;
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public int getBirthDivisionId() {
        return birthDivisionId;
    }

    public void setBirthDivisionId(int birthDivisionId) {
        this.birthDivisionId = birthDivisionId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public void setDsDivisionList(Map<Integer, String> dsDivisionList) {
        this.dsDivisionList = dsDivisionList;
    }

    public List<BirthDeclaration> getSearchResultList() {
        return searchResultList;
    }

    public void setSearchResultList(List<BirthDeclaration> searchResultList) {
        this.searchResultList = searchResultList;
    }

    public CertificateSearch getCertSearch() {
        return certSearch;
    }

    public void setCertSearch(CertificateSearch certSearch) {
        this.certSearch = certSearch;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public CertificateSearch.CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateSearch.CertificateType certificateType) {
        this.certificateType = certificateType;
    }
}
