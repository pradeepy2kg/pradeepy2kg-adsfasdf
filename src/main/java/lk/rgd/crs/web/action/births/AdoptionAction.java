package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.api.domain.AdoptionOrder;

/**
 * Action class to handle Adoption flow in birth module
 */
public class AdoptionAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionAction.class);
    private Map session;
    private final AdoptionOrderService service;
    private AdoptionOrder adoption;

    AdoptionAction(AdoptionOrderService service) {
        this.service = service;
    }

    public String adoptionDeclaration() {
        return SUCCESS;
    }

    public String initAdoptionRegistration() {
        return SUCCESS;
    }

    public String adoptionReRegistration() {
        return SUCCESS;
    }

    public String adoptionApprovalAndPrint() {
        return SUCCESS;
    }

    public void setSession(Map map) {
        logger.debug("Set session {}", map);
        this.session = map;
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
}
