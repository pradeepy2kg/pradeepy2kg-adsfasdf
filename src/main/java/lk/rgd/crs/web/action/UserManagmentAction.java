package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.core.service.UserManagerImpl;
import lk.rgd.crs.web.WebConstants;


/**
 * @author amith jayasekara
 * @author Duminda Dharmakeerthi
 */
public class UserManagmentAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(UserManagmentAction.class);
    private final UserManagerImpl service;
    private Map session;
    private User user;
    private int pageNo = 0;
    private String divisions = new String();
    private int[] assignedDistricts;
    private int[] assignedDivisions;
    private int UserDistrictId;
    private List<User> usersList;
    private String nameOfUser;
    private String userId;
    private String userName;
    private String add;
    private String edit;

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
        logger.debug("creat user called");
        if (divisions.equals(getText("get_ds_divisions.label"))) {
            generateDSDivisions();
            populate();
            return "pageLoad";
        } else {
            // todo...
            User currentUser = (User) session.get(WebConstants.SESSION_USER_BEAN);
            user.setRole(roleDAO.getRole(roleId));
            user.setStatus(User.State.ACTIVE);
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
            if (userId == null) {
                service.createUser(user, currentUser);
            } else if (userId.length() > 0) {
                user.setUserId(userId);
                service.updateUser(user, currentUser);
                session.put("viewUsers", null);
            }
            return "success";
        }
    }

    public String deleteUser() {
        populate();
        user = service.getUsersByIDMatch(userId).get(0);
        service.deleteUser(user, (User) session.get(WebConstants.SESSION_USER_BEAN));
        logger.debug("Deleting  user {} is successfull", user.getUserId());
        usersList = service.getAllUsers();
        session.put("viewUsers", null);
        return "success";
    }


    public String initUser() {
        populate();
        if (userId != null) {
            user = service.getUsersByIDMatch(getUserId()).get(0);
        }
        return "pageLoad";
    }

    public String viewUsers() {
        populate();
        session.put("viewUsers", null);
        return "success";
    }

    public String initAddEditDivisions() {
        populate();
        return "success";
    }

    public String addEditDivisions() {
        return "success";
    }

    public String selectUsers() {
        populate();
        int pageNo = 1;
        usersList = service.getUsersByRole("");
        if (getUserDistrictId() == 0 && getRoleId().length() == 1 && getNameOfUser().length() == 0) {
            usersList = service.getAllUsers();
        } else if (getUserDistrictId() == 0 && getRoleId().length() != 1) {

            usersList = service.getUsersByRole(getRoleId());

        } else if (getUserDistrictId() != 0 && getRoleId().length() == 1) {
            usersList = service.getUsersByAssignedMRDistrict(districtDAO.getDistrict(getUserDistrictId()));
        } else if (getUserDistrictId() != 0 && getRoleId().length() != 1) {
            usersList = service.getUsersByRoleAndAssignedMRDistrict(roleDAO.getRole(getRoleId()), districtDAO.getDistrict(getUserDistrictId()));
        }
        if (getNameOfUser().length() != 0) {
            usersList = service.getUsersByNameMatch(getNameOfUser());
        }
        session.put("viewUsers", usersList);
        return "success";
    }

    private void populate() {
        String language = "en";//((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        if (districtList == null)
            districtList = districtDAO.getAllDistrictNames(language, user);
        if (roleList == null)
            roleList = roleDAO.getRoleList();
    }


    private void generateDSDivisions() {
        for (int i = 0; i < assignedDistricts.length; i++) {
            if (i == 0) {
                setDivisionList(dsDivisionDAO.getAllDSDivisionNames(assignedDistricts[i], ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage(), user));
            } else {
                divisionList.putAll(dsDivisionDAO.getAllDSDivisionNames(assignedDistricts[i], ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage(), user));
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

    public int getUserDistrictId() {
        return UserDistrictId;
    }

    public void setUserDistrictId(int userDistrictId) {
        UserDistrictId = userDistrictId;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }
}
