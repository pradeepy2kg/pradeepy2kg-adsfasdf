package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;

import java.util.Map;
import java.util.Locale;

/**
 * @author Indunil Moremada
 *         Struts Action Class for Search puposes
 */
public class SearchAction extends ActionSupport implements SessionAware {
    private static final Logger logger = LoggerFactory.getLogger(SearchAction.class);

    private final BirthRegistrationService service;
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private Map session;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private User user;
    private BirthDeclaration bdf;


    private int birthDistrictId;
    private int dsDivisionId;
    private int birthDivisionId;

    private long serialNo;
    private long idUKey;
    private String childName;
    private String status;

    public SearchAction(BirthRegistrationService service, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
    }

    public String welcome() {
        populate();
        return "success";
    }

    /**
     * Search method for searching based on SearialNo,
     * district and bdDivision
     *
     * @return String
     */
    public String searchBDFBySerialNumber() {
        logger.debug("inside searchBDFBySerialNumber() : search parameters serialNo {}, birthDistrictId {} " + "and birthDivisionId " +
            birthDivisionId, serialNo, birthDistrictId + " recieved");
        try {
            bdf = service.getByBDDivisionAndSerialNo(bdDivisionDAO.getBDDivisionByPK(birthDivisionId), serialNo, user);
            setStatus("searchBDF.status." + bdf.getRegister().getStatus().ordinal());
        } catch (CRSRuntimeException e) {
            logger.error("inside searchBDFBySerialNumber() SearchBDFBySerialNumber : {} ", e);
            addActionError(getText("SearchBDF.error." + e.getErrorCode()));
        } catch (Exception e) {
            logger.error("inside searchBDFByIdUKey() SearchBDFByIdUKey : {} ", e);
            addActionError(getText("SearchBDF.error.NoResult"));
        }
        populate();
        return "success";
    }

    /**
     * Populate master data to the UI
     */
    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("inside populate() : language {} observed ", language);
        setDistrictList(districtDAO.getDistrictNames(language, user));
        setInitialDistrict();
    }

    /**
     * search method to search particular BirthDeclaration
     * based on IdUKey
     *
     * @return
     */
    public String searchBDFByIdUKey() {
        logger.debug("inside searchBDFByIdUKey() : search parameter idUKey {} recieved", idUKey);
        try {
            bdf = service.getById(idUKey, user);
            setStatus("searchBDF.status." + bdf.getRegister().getStatus().ordinal());
        } catch (CRSRuntimeException e) {
            logger.error("inside searchBDFByIdUKey() SearchBDFByIdUKey : {} ", e);
            addActionError(getText("SearchBDF.error." + e.getErrorCode()));
        } catch (Exception e) {
            logger.error("inside searchBDFByIdUKey() SearchBDFByIdUKey : {} ", e);
            addActionError(getText("SearchBDF.error.NoResult"));
        }
        populate();
        return "success";
    }

    /**
     * initial district is set to the
     * first district of the allowed
     * district list of a perticular
     * user
     */
    public void setInitialDistrict() {
        if (!getDistrictList().isEmpty()) {
            setBirthDistrictId(getDistrictList().keySet().iterator().next());
        }
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
}
