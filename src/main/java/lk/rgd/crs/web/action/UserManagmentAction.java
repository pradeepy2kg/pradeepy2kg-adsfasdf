package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Locale;
import java.util.Set;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.crs.web.WebConstants;

/**
 * @author amith jayasekara
 * @author Duminda Dharmakeerthi 
 */
public class UserManagmentAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);
    private Map session;
    private User user = new User();
    private int pageNo = 0;
    private Set prefferedDistricts;

    private final DistrictDAO districtDAO;
    
    private Map<Integer, String> districtList;

    public UserManagmentAction(DistrictDAO districtDAO){
        this.districtDAO = districtDAO;
    }

    public String creatUser() {
        logger.info("creat user called");
        if(pageNo == 1)  {
            user.setPasswordHash(getPasswordHash(user.getUserName()));  // to do...
            return "sucess";
        }
        populate();
        return "pageLoad";
    }

    private void populate(){
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        districtList = districtDAO.getDistrictNames(language, null);
    }

    private String getPasswordHash(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return new String(digest);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageNo() {

        return pageNo;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public Map<Integer, String> getDistrictList() {

        return districtList;
    }

    public void setPrefferedDistricts(Set prefferedDistricts) {
        this.prefferedDistricts = prefferedDistricts;
    }

    public Set getPrefferedDistricts() {
        return prefferedDistricts;
    }

    
    public void setSession(Map map) {
        this.session = map;
    }
}
