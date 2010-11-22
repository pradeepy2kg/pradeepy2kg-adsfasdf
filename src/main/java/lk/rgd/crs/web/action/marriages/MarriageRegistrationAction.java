package lk.rgd.crs.web.action.marriages;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.MarriageNotice;
import lk.rgd.crs.web.WebConstants;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author amith
 *         action class for marriage registration
 */
public class MarriageRegistrationAction extends ActionSupport implements SessionAware {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MarriageRegistrationAction.class);

    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final MRDivisionDAO mrDivisionDAO;

    private User user;

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> mrDivisionList;

    private int marriageDistrictId;
    private int dsDivisionId;
    private int mrDivisionId;

    private String language;

    public MarriageRegistrationAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, MRDivisionDAO mrDivisionDAO) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.mrDivisionDAO = mrDivisionDAO;
    }

    /**
     * loading marriage notice page
     */
    public String marriageNoticeInit() {
        logger.debug("attempt to load marriage notice page");
/*        MarriageNotice marriageNotice = new MarriageNotice();*/
        populateBasicLists();
        return "pageLoad";
    }

    /**
     * populate basic list such as district list/ds division lis and mr division list
     */
    private void populateBasicLists() {
        //TODO remove
        districtList = districtDAO.getDistrictNames(language, user);
        if (marriageDistrictId == 0) {
            if (!districtList.isEmpty()) {
                marriageDistrictId = districtList.keySet().iterator().next();
                logger.debug("first allowed district in the list {} was set", marriageDistrictId);
            }
        }
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(marriageDistrictId, language, user);

        if (dsDivisionId == 0) {
            if (!dsDivisionList.isEmpty()) {
                dsDivisionId = dsDivisionList.keySet().iterator().next();
                logger.debug("first allowed DS Div in the list {} was set", dsDivisionId);
            }
        }

        mrDivisionList = mrDivisionDAO.getMRDivisionNames(dsDivisionId, language, user);
        if (mrDivisionId == 0) {
            mrDivisionId = mrDivisionList.keySet().iterator().next();
            logger.debug("first allowed BD Div in the list {} was set", mrDivisionId);
        }
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("setting User: {}", user.getUserName());
        Locale userLocale = (Locale) session.get(WebConstants.SESSION_USER_LANG);
        language = userLocale.getLanguage();
        logger.debug("setting language : {}", language);
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
