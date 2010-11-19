package lk.rgd.crs.web.action.marriages;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.WebConstants;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author amith
 *         action class for marriage registration
 */
public class MarriageRegistrationAction extends ActionSupport implements SessionAware {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MarriageRegistrationAction.class);

    private User user;

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> mrDivisionList;

    /**
     * loading marriage notice page
     */
    public String marriageNoticeInit() {
        logger.debug("attempt to load marriage notice page");
        populateBasicLists();
        return "pageLoad";
    }

    /**
     * populate basic list such as district list/ds division lis and mr division list
     */
    private void populateBasicLists() {
        //TODO implements
        districtList = new HashMap();
        dsDivisionList = new HashMap();
        mrDivisionList = new HashMap();
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("setting User: {}", user.getUserName());
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

    public Map<Integer, String> getMrDivisionList() {
        return mrDivisionList;
    }

    public void setMrDivisionList(Map<Integer, String> mrDivisionList) {
        this.mrDivisionList = mrDivisionList;
    }
}
