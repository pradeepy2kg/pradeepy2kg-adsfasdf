package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.sql.SQLException;

import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.core.service.UserManagerImpl;
import lk.rgd.crs.api.dao.BDDivisionDAO;


/**
 * @author amith jayasekara
 */
public class UserManagmentAction extends ActionSupport implements SessionAware {

    private final UserManagerImpl service;
    private final DistrictDAO districtDAO;
    private final CountryDAO countryDAO;
    private final RaceDAO raceDAO;
    private final DSDivisionDAO dsDivisionDAO;

    private static final Logger logger = LoggerFactory.getLogger(UserManagmentAction.class);
    private Map session;


    public UserManagmentAction(UserManagerImpl service, DistrictDAO districtDAO, CountryDAO countryDAO, RaceDAO raceDAO, BDDivisionDAO bdDivisionDAO, DSDivisionDAO dsDivisionDAO) {
        this.service = service;
        this.districtDAO = districtDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.dsDivisionDAO = dsDivisionDAO;
    }

    public String creatUser() {
        logger.info("creat user called");
        //testing

        User usr = new User();
        usr.setUserName("testAmith");
        usr.setPin("testPin");
        usr.setUserId("testID");
        usr.setPrefLanguage("SI");
        Role role = new Role();
        role.setRoleId("ADR");
        usr.setRole(role);
        User currentUser = (User) session.get("user_bean");

        logger.info("About to create a user..");
        service.creatUser(usr, currentUser);

        return "pageLoad";
    }


    public void setSession(Map map) {
        this.session = map;
    }
}
