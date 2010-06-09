package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Locale;

import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.dao.*;

/**
 * @author Duminda
 * 
 */
public class UserPreferencesAction extends ActionSupport implements SessionAware {

    private String prefLanguage;
    private int prefDistrictId;
    private int prefDSDivisionId;

    private Map session;

    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;

    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    
    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);


    public UserPreferencesAction(DistrictDAO districtDAO,DSDivisionDAO dsDivisionDAO) {
       this.districtDAO = districtDAO;
       this.dsDivisionDAO = dsDivisionDAO;
    }

    /**
     * Set the Language that the user preffered to work
     * And set preffered language to the session
     */

    public String userPreferenceInit() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);

        districtList = districtDAO.getDistrictNames(language, user);
        int birthDistrictId = districtList.keySet().iterator().next();
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, language, user);

        return "pageload";
    }

    public String selectUserPreference() {
        Locale currentLocale = (Locale) session.get(WebConstants.SESSION_USER_LANG);
        Locale newLocale = new Locale(prefLanguage, currentLocale.getCountry());

        session.put(WebConstants.SESSION_USER_LANG, newLocale);
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        user.setPrefLanguage(prefLanguage);
        user.setPrefDistrict(districtDAO.getDistrict(prefDistrictId));
        user.setPrefDSDivision(dsDivisionDAO.getDSDivisionByPK(prefDSDivisionId));
        session.put(WebConstants.SESSION_USER_BEAN, user);
        // todo put new user object to session
        // todo persist this user

        logger.debug("inside selectUserPreference() : {} passed.", prefLanguage);
        logger.debug("inside selectUserPreference() : {} passed.", prefDistrictId);
        logger.debug("inside selectUserPreference() : {} passed.", prefDSDivisionId);

        return "success";
    }

    public void setSession(Map map) {
        this.session = map;
    }

    public Map getSession() {
        return session;
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public String getPrefLanguage() {
        return prefLanguage;
    }

    public void setPrefLanguage(String prefLanguage) {
        this.prefLanguage = prefLanguage;
    }

    public int getPrefDistrictId() {
        return prefDistrictId;
    }

    public void setPrefDistrictId(int prefDistrictId) {
        this.prefDistrictId = prefDistrictId;
    }

    public int getPrefDSDivisionId() {
        return prefDSDivisionId;
    }

    public void setPrefDSDivisionId(int prefDSDivisionId) {
        this.prefDSDivisionId = prefDSDivisionId;
    }
}
