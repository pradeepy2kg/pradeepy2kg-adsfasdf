package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.common.api.domain.User;

import java.util.Map;

/**
 * @author Indunil Moremada
 *         Struts Action Class for Search puposes
 */
public class SearchAction extends ActionSupport implements SessionAware {
    private static final Logger logger = LoggerFactory.getLogger(SearchAction.class);

    private final BirthRegistrationService service;
    private Map session;

    private User user;
    private BirthDeclaration bdf;

    public SearchAction(BirthRegistrationService service) {
        this.service = service;
    }

    public String welcome() {
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
}
