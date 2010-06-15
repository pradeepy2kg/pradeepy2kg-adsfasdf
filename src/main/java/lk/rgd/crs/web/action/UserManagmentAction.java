package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Locale;
import java.sql.SQLException;

import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.core.service.UserManagerImpl;
import lk.rgd.common.RGDException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.web.WebConstants;

/**
 * @author amith jayasekara
 * @author Duminda Dharmakeerthi
 */
public class UserManagmentAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(UserManagmentAction.class);
    private final UserManagerImpl service;
    private Map session;
    private static User user;
    private int pageNo = 0;
    private String divisions = new String();
    private int[] assignedDistricts;
    private int[] assignedDivisions;


    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {

        return roleId;
    }

    private String roleId;


    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final RoleDAO roleDAO;

    private Map<Integer, String> districtList;
    private Map<Integer, String> divisionList;
    private Map<String, String> roleList;

    public UserManagmentAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, RoleDAO roleDAO, UserManagerImpl service) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.roleDAO = roleDAO;
        this.service = service;
    }

    public String creatUser() {
        logger.info("creat user called");
        if (divisions.equals(getText("get_ds_divisions.label"))) {
            generateDSDivisions();
            populate();
            return "pageLoad";
        } else {
            // todo...

            User currentUser = (User) session.get("user_bean");
            user.setRole(roleDAO.getRole(roleId));
            // creating assigned Districts
            Set assDistrict = new HashSet();
            for (int i = 0; i < assignedDistricts.length; i++) {
                assDistrict.add(districtDAO.getDistrict(assignedDistricts[i]));
            }

            user.setAssignedBDDistricts(assDistrict);
            user.setAssignedMRDistricts(assDistrict);

            Set assDSDivision = new HashSet();
            for (int i = 0; i < assignedDivisions.length; i++) {
                assDSDivision.add(dsDivisionDAO.getDSDivisionByPK(assignedDivisions[i]));
            }
            user.setAssignedBDDSDivisions(assDSDivision);

            logger.info("About to create a user..");
            service.createUser(user, currentUser);

            return "success";
        }
    }

    public String initUser() {
        populate();
        return "pageLoad";
    }

    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        if (districtList == null)
            districtList = districtDAO.getDistrictNames(language, null);
        if (roleList == null)
            roleList = roleDAO.getRoleList();
    }

    /**
     * To generate the divisionList according to the selected Districts.
     */

    private void generateDSDivisions() {
        for (int i = 0; i < assignedDistricts.length; i++) {
            if (i == 0) {
                setDivisionList(dsDivisionDAO.getDSDivisionNames(assignedDistricts[i], ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage(), null));
            } else {
                divisionList.putAll(dsDivisionDAO.getDSDivisionNames(assignedDistricts[i], ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage(), null));
            }
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

    public void setDivisions(String divisions) {
        this.divisions = divisions;
    }

    public String getDivisions() {

        return divisions;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public Map<Integer, String> getDistrictList() {

        return districtList;
    }

    public void setDivisionList(Map<Integer, String> divisionList) {
        this.divisionList = divisionList;
    }

    public Map<Integer, String> getDivisionList() {
        return divisionList;
    }


    public void setAssignedDistricts(int[] assignedDistricts) {
        this.assignedDistricts = assignedDistricts;
    }

    public int[] getAssignedDistricts() {
        return assignedDistricts;
    }

    public void setAssignedDivisions(int[] assignedDivisions) {
        this.assignedDivisions = assignedDivisions;
    }

    public int[] getAssignedDivisions() {

        return assignedDivisions;
    }

    public void setRoleList(Map<String, String> roleList) {
        this.roleList = roleList;
    }

    public Map<String, String> getRoleList() {

        return roleList;
    }

    public void setSession(Map map) {
        this.session = map;
    }
}