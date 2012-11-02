package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.ErrorCodes;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.ZonalOfficesDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.ZonalOffice;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.service.MasterDataManagementService;
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
public class ZonalOfficeManagementAction extends ActionSupport implements SessionAware {
    private static final Logger logger = LoggerFactory.getLogger(ZonalOfficeManagementAction.class);

    private Map session;
    private User user;
    private String language;

    private final ZonalOfficesDAO zonalOfficesDAO;
    private final MasterDataManagementService service;

    private List<ZonalOffice> zonalOfficeList = new ArrayList<ZonalOffice>();
    private ZonalOffice zonalOffice;
    private int page;
    private int zonalOfficeUKey;

    public ZonalOfficeManagementAction(ZonalOfficesDAO zonalOfficesDAO, MasterDataManagementService service) {
        this.zonalOfficesDAO = zonalOfficesDAO;
        this.service = service;
    }

    public String pageLoad() {
        zonalOffice = null;
        populate();
        return SUCCESS;
    }

    public String manageZonalOffices() {
        logger.debug("Manage zonal offices [Page: {}]", page);
        switch (page) {
            case 1:
                try {
                    service.activateOrInactivateZonalOffice(zonalOfficeUKey, false, user);
                    addActionMessage("Deactivate the zonal offices (" + zonalOfficeUKey + ") successful");
                } catch (CRSRuntimeException e) {
                    switch (e.getErrorCode()) {
                        case ErrorCodes.INVALID_ZONAL_OFFICE:
                            addActionError("Unable to deactivated zonal office");
                            break;
                        case ErrorCodes.PERMISSION_DENIED:
                            addActionError("You don't have permission to deactivate the zonal office");
                            break;
                        default:
                            addActionError("Unable to deactivated zonal office");
                    }
                }
                break;
            case 2:
                try {
                    service.activateOrInactivateZonalOffice(zonalOfficeUKey, true, user);
                    addActionMessage("Activate the zonal office (" + zonalOfficeUKey + ") successful");
                } catch (CRSRuntimeException e) {
                    switch (e.getErrorCode()) {
                        case ErrorCodes.INVALID_ZONAL_OFFICE:
                            addActionError("Unable to deactivated zonal office");
                            break;
                        case ErrorCodes.PERMISSION_DENIED:
                            addActionError("You don't have permission to deactivate the zonal office");
                            break;
                        default:
                            addActionError("Unable to deactivated zonal office");
                    }
                }
                break;
            case 3:
                populate(zonalOfficeUKey);
                break;
            case 4:
                if (zonalOfficeUKey > 0) {
                    try {
                        service.updateZonalOffice(zonalOffice, user);
                        logger.debug("Successfully update the zonal office : {}", zonalOffice.getZonalOfficeUKey());
                        addActionMessage("Zonal Office (" + zonalOffice.getEnZonalOfficeName() + ") updated successfully.");
                    } catch (CRSRuntimeException e) {
                        switch (e.getErrorCode()) {
                            case ErrorCodes.INVALID_ZONAL_OFFICE:
                                addActionError("Unable to update the zonal office");
                                break;
                            case ErrorCodes.PERMISSION_DENIED:
                                addActionError("You don't have permission to update the zonal office");
                                break;
                            default:
                                addActionError("Unable to update zonal office");
                        }
                    }
                } else if (zonalOfficeUKey == 0) {
                    try {
                        service.addZonalOffice(zonalOffice, user);
                        logger.debug("Successfully add the zonal office : {}", zonalOffice.getZonalOfficeUKey());
                        addActionMessage("Zonal Office (" + zonalOffice.getEnZonalOfficeName() + ") added successfully.");
                    } catch (CRSRuntimeException e) {
                        switch (e.getErrorCode()) {
                            case ErrorCodes.INVALID_ZONAL_OFFICE:
                                addActionError("Unable to add a zonal office");
                                break;
                            case ErrorCodes.PERMISSION_DENIED:
                                addActionError("You don't have permission to add a zonal office");
                                break;
                            default:
                                addActionError("Unable to add zonal office");
                        }
                    }
                }
                break;
        }
        populate();
        return SUCCESS;
    }

    private void populate(int zonalOfficeId) {
        zonalOffice = zonalOfficesDAO.getZonalOffice(zonalOfficeId);
    }

    private void populate() {
        zonalOfficeList = zonalOfficesDAO.getAll();
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ZonalOffice> getZonalOfficeList() {
        return zonalOfficeList;
    }

    public void setZonalOfficeList(List<ZonalOffice> zonalOfficeList) {
        this.zonalOfficeList = zonalOfficeList;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getZonalOfficeUKey() {
        return zonalOfficeUKey;
    }

    public void setZonalOfficeUKey(int zonalOfficeUKey) {
        this.zonalOfficeUKey = zonalOfficeUKey;
    }

    public ZonalOffice getZonalOffice() {
        return zonalOffice;
    }

    public void setZonalOffice(ZonalOffice zonalOffice) {
        this.zonalOffice = zonalOffice;
    }

}
