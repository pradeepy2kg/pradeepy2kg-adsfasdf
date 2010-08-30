package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.core.service.UserManagerImpl;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.service.MasterDataManagementService;


/**
 * @author amith jayasekara
 * @author Duminda Dharmakeerthi
 */
public class UserManagmentAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(UserManagmentAction.class);
    private final UserManagerImpl service;
    private final MasterDataManagementService dataManagementService;
    private Map session;
    private User user;
    private User currentUser;
    private int pageNo;
    private String divisions = new String();
    private String button;
    private int[] assignedDistricts;
    private int[] assignedDivisions;
    private List<User> usersList;
    private String nameOfUser;
    private String userId;
    private String userName;

    private int UserDistrictId;
    private int dsDivisionId;
    private int divisionId;

    private BDDivision bdDivision;
    private DSDivision dsDivision;
    private District district;

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {

        return roleId;
    }

    private String roleId;
    private final DistrictDAO districtDAO;
    private final RoleDAO roleDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;

    private Map<Integer, String> districtList;
    private Map<Integer, String> divisionList;
    private Map<String, String> roleList;
    private Map<Integer, String> dsDivisionList;

    public UserManagmentAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, RoleDAO roleDAO, UserManagerImpl service,
                               BDDivisionDAO bdDivisionDAO, MasterDataManagementService dataManagementService) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.roleDAO = roleDAO;
        this.service = service;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dataManagementService = dataManagementService;
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
        session.put("viewUsers", usersList);
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

    public String initAddDivisionsAndDsDivision() {
        populate();
        populateDynamicLists("en");
        return "success";
    }

    public String initAddDistrict() {
        populate();
        if (button.equals("ADD")) {
            pageNo = 1;
        }
        if (button.equals("INACTIVE")) {
            return "delete" + SUCCESS;
        }
        return "success";
    }

    public String initAddDsDivisions() {
        populate();
        if (button.equals("ADD")) {
            pageNo = 2;
        }
        if (button.equals("INACTIVE")) {
            dataManagementService.inactivateDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId), currentUser);
            return "delete" + SUCCESS;
        }
        return "success";
    }

    public String initAddDivisions() {
        populate();
        if (button.equals("ADD")) {
            district = districtDAO.getDistrict(UserDistrictId);
            pageNo = 3;
        }
        if (button.equals("INACTIVE")) {
            dataManagementService.inactivateBDDivision(bdDivisionDAO.getBDDivisionByPK(divisionId), currentUser);
            return "delete" + SUCCESS;
        }
        return SUCCESS;
    }


    public String AddDivisionsAndDsDivisions() {
        switch (pageNo) {
            case 1:
                district.setActive(true);
                dataManagementService.addDistrict(district, currentUser);
                logger.debug("New Id of new District {} is   :{}", district.getEnDistrictName(), district.getDistrictId());
                break;
            case 2:
                dsDivision.setDistrict(districtDAO.getDistrict(UserDistrictId));
                dsDivision.setActive(true);
                dataManagementService.addDSDivision(dsDivision, currentUser);
                logger.debug("New Id of new Ds Division {} is   :{}", dsDivision.getEnDivisionName(), dsDivision.getDivisionId());
                break;
            case 3:
                bdDivision.setDsDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                bdDivision.setActive(true);
                dataManagementService.addBDDivision(bdDivision, currentUser);
                logger.debug("New Id of new Division {} is   :{}", bdDivision.getEnDivisionName(), bdDivision.getDivisionId());
                break;
        }
        return SUCCESS;
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

    private void populateDynamicLists(String language) {
        if (getUserDistrictId() == 0) {
            if (!districtList.isEmpty()) {
                setUserDistrictId(districtList.keySet().iterator().next());
                logger.debug("first allowed district in the list {} was set", getUserDistrictId());
            }
        }
        dsDivisionList = dsDivisionDAO.getAllDSDivisionNames(getUserDistrictId(), language, user);

        if (getDsDivisionId() == 0) {
            if (!dsDivisionList.isEmpty()) {
                setDsDivisionId(dsDivisionList.keySet().iterator().next());
                logger.debug("first allowed DS Div in the list {} was set", getDsDivisionId());
            }
        }

        setDivisionList(bdDivisionDAO.getBDDivisionNames(getDsDivisionId(), language, user));
        if (getDivisionId() == 0) {
            setDivisionId(dsDivisionList.keySet().iterator().next());
            logger.debug("first allowed BD Div in the list {} was set", getDivisionId());
        }
    }

    private void generateDSDivisions() {
        for (int i = 0; i < assignedDistricts.length; i++) {
            if (i == 0) {
                setDivisionList(dsDivisionDAO.getAllDSDivisionNames(assignedDistricts[i], ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage(), user));
            } else {
                getDivisionList().putAll(dsDivisionDAO.getAllDSDivisionNames(assignedDistricts[i], ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage(), user));
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
        currentUser = (User) session.get(WebConstants.SESSION_USER_BEAN);
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

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public BDDivisionDAO getBdDivisionDAO() {
        return bdDivisionDAO;
    }

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public void setDsDivisionList(Map<Integer, String> dsDivisionList) {
        this.dsDivisionList = dsDivisionList;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public BDDivision getBdDivision() {
        return bdDivision;
    }

    public void setBdDivision(BDDivision bdDivision) {
        this.bdDivision = bdDivision;
    }

    public DSDivision getDsDivision() {
        return dsDivision;
    }

    public void setDsDivision(DSDivision dsDivision) {
        this.dsDivision = dsDivision;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }


    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

}
