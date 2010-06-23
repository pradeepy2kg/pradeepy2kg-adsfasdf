package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.web.WebConstants;
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
    private Map<Integer, String> divisionList;
    private Map<Integer, String> districtList;
    private User user;
    private BirthDeclaration bdf;


    private int district;

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
     * Search method for searching based on idUKey or SearialNo
     *
     * @return String
     */
    public String searchBDF() {
        logger.debug("inside searchBDF : idUKey {} recieved , SearialNo {} recieved", idUKey, serialNo);
        if (idUKey != 0) {
            /*bdf = service.getById(idUKey, user);
            if (bdf.getRegister().getStatus() != BirthDeclaration.State.APPROVED ||
                bdf.getRegister().getStatus() != BirthDeclaration.State.CONFIRMATION_PRINTED ||
                bdf.getRegister().getStatus() != BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED) {
                addActionError("bdfSearch.EditNotAllowed");
                return "error";
            }*/
        } else if (serialNo != null && !serialNo.equals("")) {
        } else {
            addActionError(getText("bdfSearch.InvalidEntryError"));
        }
        return "success";
    }

    /**
     * Populate master data to the UIs
     */
    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("inside populate : {} observed.", language);
        setDistrictList(districtDAO.getDistrictNames(language, user));
        setInitialDistrict();
        Map<Integer, String> dsDivisionList = dsDivisionDAO.getDSDivisionNames(getDistrict(), language, user);
        if (!dsDivisionList.isEmpty()) {
            int dsDivisionId = dsDivisionList.keySet().iterator().next();
            setDivisionList(bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user));
        }
        logger.debug("inside populate : districts , countriees and races populated.");
    }

    /**
     * initial district is set to the
     * first district of the allowed
     * district list of a perticular
     * user
     */
    public void setInitialDistrict() {
        if (!getDistrictList().isEmpty()) {
            setDistrict(getDistrictList().keySet().iterator().next());
        }
    }

    private String serialNo;
    private long idUKey;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
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

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public Map<Integer, String> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(Map<Integer, String> divisionList) {
        this.divisionList = divisionList;
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }
}
