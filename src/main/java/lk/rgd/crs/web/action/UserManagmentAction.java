package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.service.UserManager;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.dao.*;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.dao.CourtDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.Court;
import lk.rgd.crs.api.service.MasterDataManagementService;


/**
 * @author amith jayasekara
 * @author Duminda Dharmakeerthi
 */
public class UserManagmentAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(UserManagmentAction.class);
    private final UserManager service;
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
    private List<District> districtNameList;
    private List<DSDivision> dsDivisionNameList;
    private List<BDDivision> bdDivisionNameList;
    private List<MRDivision> mrDivisionNameList;
    private List<Court> courtNameList;
    private List<Location> locationNameList;
    private String nameOfUser;
    private String userId;
    private String userName;

    private int UserDistrictId;
    private int dsDivisionId;
    private int divisionId;
    private int mrdivisionId;
    private int courtId;
    private int locationId;

    private String districtEn;
    private String dsDivisionEn;
    private String msg;

    private BDDivision bdDivision;
    private DSDivision dsDivision;
    private District district;
    private MRDivision mrDivision;
    private Court court;


    private Location location;


    private String roleId;
    private final DistrictDAO districtDAO;
    private final RoleDAO roleDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final MRDivisionDAO mrDivisionDAO;
    private final CourtDAO courtDAO;
    private LocationDAO locationDAO;

    private Map<Integer, String> districtList;
    private Map<Integer, String> divisionList;
    private Map<String, String> roleList;
    private Map<Integer, String> dsDivisionList;

    /*support variables*/
    private Map<Integer, String> currentbdDivisionList; //users current
    private Map<Integer, String> currentDistrictList;

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {

        return roleId;
    }

    public UserManagmentAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, RoleDAO roleDAO, UserManager service, CourtDAO courtDAO,
                               BDDivisionDAO bdDivisionDAO, MasterDataManagementService dataManagementService, MRDivisionDAO mrDivisionDAO, LocationDAO locationDAO) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.roleDAO = roleDAO;
        this.service = service;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dataManagementService = dataManagementService;
        this.mrDivisionDAO = mrDivisionDAO;
        this.courtDAO = courtDAO;
        this.locationDAO = locationDAO;
    }

    public String creatUser() {
        User updated = (User) session.get(WebConstants.SESSION_UPDATED_USER);
        /*logger.debug("creat user called");
        if (divisions.equals(getText("get_ds_divisions.label"))) {
            generateDSDivisions();
            populate();
            return "pageLoad";
        } else {*/
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
        } else if (updated != null) {
            //     user.setUserId(userId);
            //setting new district list and division list and role
            Set assgnDistrict = new HashSet();
            for (int i = 0; i < assignedDistricts.length; i++) {
                assgnDistrict.add(districtDAO.getDistrict(assignedDistricts[i]));
            }
            updated.setAssignedBDDistricts(assgnDistrict);
            updated.setAssignedMRDistricts(assgnDistrict);

            Set assgnDSDivision = new HashSet();
            for (int i = 0; i < assignedDivisions.length; i++) {
                assgnDSDivision.add(dsDivisionDAO.getDSDivisionByPK(assignedDivisions[i]));
            }
            updated.setAssignedBDDSDivisions(assgnDSDivision);
            updated.setRole(roleDAO.getRole(roleId));

            service.updateUser(updated, currentUser);
            session.remove(WebConstants.SESSION_UPDATED_USER);
            session.put("viewUsers", null);
        }
        return "success";
    }


    public String deleteUser() {
        populate();
        user = service.getUserByID(userId);
        service.deleteUser(user, (User) session.get(WebConstants.SESSION_USER_BEAN));
        logger.debug("Deleting  user {} is successoption body.full", user.getUserId());
        usersList = service.getAllUsers();
        session.put("viewUsers", usersList);
        return "success";
    }


    public String initUser() {
        populate();
        populateDynamicLists("en");
        if (userId != null) {
            user = service.getUserByID(getUserId());
            currentDistrictList = convertDistricSetToMap(user.getAssignedBDDistricts());
            currentbdDivisionList = convertDivisionSetToMap(user.getAssignedBDDSDivisions());
            logger.info("current district list size  : {} for user : {}", currentDistrictList.size(), user.getUserName());
            logger.info("current division list size  : {} for user : {}", currentbdDivisionList.size(), user.getUserName());
            session.put(WebConstants.SESSION_UPDATED_USER, user);
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

    public String initDivisionList() {
        populate();
        setDivisionList();
        return SUCCESS;
    }

    public void setDivisionList() {
        switch (pageNo) {
            case 1:
                districtNameList = districtDAO.findAll();
                district = null;
                break;
            case 2:
                districtEn = districtDAO.getNameByPK(UserDistrictId, "en");
                dsDivisionNameList = dsDivisionDAO.findAll();
                dsDivision = null;
                break;
            case 3:
                districtEn = districtDAO.getNameByPK(UserDistrictId, "en");
                dsDivisionEn = dsDivisionDAO.getNameByPK(dsDivisionId, "en");
                bdDivisionNameList = bdDivisionDAO.findAll();
                bdDivision = null;
                break;
            case 4:
                districtEn = districtDAO.getNameByPK(UserDistrictId, "en");
                dsDivisionEn = dsDivisionDAO.getNameByPK(dsDivisionId, "en");
                mrDivisionNameList = mrDivisionDAO.findAll();
                mrDivision = null;
                break;
            case 5:
                courtNameList = courtDAO.findAll();
                court = null;
                break;
            case 6:
                locationNameList = locationDAO.getAllLocations(true);
                logger.debug("Size of the loaded Lacation List is :{}", locationNameList.size());
                location = null;
                break;


        }
    }

    public String initActive() {
        switch (pageNo) {
            case 1:
                dataManagementService.activateDistrict(UserDistrictId, currentUser);
                logger.debug("Id of Active District ({}) is    :{}", districtDAO.getDistrict(UserDistrictId).getEnDistrictName(), UserDistrictId);
                break;
            case 2:
                dataManagementService.activateDSDivision(dsDivisionId, currentUser);
                logger.debug("Id of Active Ds Division ({}) is    :{}", dsDivisionDAO.getDSDivisionByPK(dsDivisionId).getEnDivisionName(), dsDivisionId);
                break;
            case 3:
                dataManagementService.activateBDDivision(divisionId, currentUser);
                logger.debug("Id of Active Division ({}) is    :{}", bdDivisionDAO.getBDDivisionByPK(divisionId).getEnDivisionName(), divisionId);
                break;
            case 4:
                dataManagementService.activateMRDivision(mrdivisionId, currentUser);
                logger.debug("Id of Active MRDivision ({}) is    :{}", mrDivisionDAO.getMRDivisionByPK(mrdivisionId).getEnDivisionName(), mrdivisionId);
                break;
            case 5:
                dataManagementService.inactivateLocation(courtId, currentUser);
                logger.debug("Id of the Active Court ({}) is  :{}", courtDAO.getNameByPK(courtId, "en"), courtId);
                break;
            case 6:
                dataManagementService.activateLocation(locationId, currentUser);
                logger.debug("Id of the Active location( {} ) is  :{}", locationDAO.getLocation(locationId).getEnLocationName(), locationId);
                break;

        }
        setDivisionList();
        return SUCCESS;
    }

    public String initInactive() {
        switch (pageNo) {
            case 1:
                dataManagementService.inactivateDistrict(UserDistrictId, currentUser);
                logger.debug("Id of Inactive District ({}) is    :{}", districtDAO.getDistrict(UserDistrictId).getEnDistrictName(), UserDistrictId);
                break;
            case 2:
                dataManagementService.inactivateDSDivision(dsDivisionId, currentUser);
                logger.debug("Id of Inactive Ds Division ({}) is    :{}", dsDivisionDAO.getDSDivisionByPK(dsDivisionId).getEnDivisionName(), dsDivisionId);
                break;
            case 3:
                dataManagementService.inactivateBDDivision(divisionId, currentUser);
                logger.debug("Id of Inactive Division ({}) is    :{}", bdDivisionDAO.getBDDivisionByPK(divisionId).getEnDivisionName(), divisionId);
                break;
            case 4:
                dataManagementService.inactivateMRDivision(mrdivisionId, currentUser);
                logger.debug("Id of Inactive MRDivision ({}) is    :{}", mrDivisionDAO.getMRDivisionByPK(mrdivisionId).getEnDivisionName(), mrdivisionId);
                break;
            case 5:
                dataManagementService.inactivateLocation(courtId, currentUser);
                logger.debug("Id of the Inactive Court ({}) is   :{}", courtDAO.getNameByPK(courtId, "en"), courtId);
                break;
            case 6:
                dataManagementService.inactivateLocation(locationId, currentUser);
                logger.debug("Id of the Inactive location( {} ) is  :{}", locationDAO.getLocation(locationId).getEnLocationName(), locationId);
                break;

        }
        setDivisionList();
        return SUCCESS;
    }


    public String addDivisionsAndDsDivisions() {
        switch (pageNo) {
            case 1:
                district.setActive(true);
                dataManagementService.addDistrict(district, currentUser);
                logger.debug("New Id of new District {} is   :{}", district.getEnDistrictName(), district.getDistrictId());
                msg = " New District Was Added  :" + district.getEnDistrictName();
                break;
            case 2:
                dsDivision.setDistrict(districtDAO.getDistrict(UserDistrictId));
                dsDivision.setActive(true);
                dataManagementService.addDSDivision(dsDivision, currentUser);
                logger.debug("New Id of new Ds Division {} is   :{}", dsDivision.getEnDivisionName(), dsDivision.getDivisionId());
                msg = "New DSDivision Was Added :" + dsDivision.getEnDivisionName();
                break;
            case 3:
                bdDivision.setDsDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                bdDivision.setActive(true);
                dataManagementService.addBDDivision(bdDivision, currentUser);
                logger.debug("New Id of New Division {} is   :{}", bdDivision.getEnDivisionName(), bdDivision.getDivisionId());
                msg = "New BDDivision Was Added  :" + bdDivision.getEnDivisionName();
                break;
            case 4:
                mrDivision.setDsDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                mrDivision.setActive(true);
                dataManagementService.addMRDivision(mrDivision, currentUser);
                logger.debug("New Id of New MRDivision {} is   :{}", mrDivision.getEnDivisionName(), mrDivision.getDivisionId());
                msg = "New MRDivision Was Added  :" + mrDivision.getEnDivisionName();
                break;
            case 5:
                break;
            case 6:
                logger.debug("English :{}", location.getEnLocationName());
                logger.debug("Sinhala :{}", location.getSiLocationName());
                logger.debug("Tamil :{}", location.getTaLocationName());
                logger.debug("code :{}", location.getLocationCode());
                dataManagementService.addLocation(location, currentUser);
                logger.debug("New Id of New Location {} is  :{}", locationDAO.getLocation(locationId), locationId);
                msg = "New Location Was Added  :" + location.getEnLocationName();


        }
        setDivisionList();
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

    private Map convertDistricSetToMap(Set set) {
        Map retMap = new HashMap();
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            District district = (District) itr.next();
            retMap.put(district.getDistrictUKey(), district.getEnDistrictName());
        }
        return retMap;
    }

    private Map convertDivisionSetToMap(Set set) {
        Map ret = new HashMap();
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            DSDivision dsd = (DSDivision) itr.next();
            ret.put(dsd.getDivisionId(), dsd.getEnDivisionName());
        }
        return ret;
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

    public String getDistrictEn() {
        return districtEn;
    }

    public void setDistrictEn(String districtEn) {
        this.districtEn = districtEn;
    }

    public String getDsDivisionEn() {
        return dsDivisionEn;
    }

    public void setDsDivisionEn(String dsDivisionEn) {
        this.dsDivisionEn = dsDivisionEn;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MRDivisionDAO getMrDivisionDAO() {
        return mrDivisionDAO;
    }

    public MRDivision getMrDivision() {
        return mrDivision;
    }

    public void setMrDivision(MRDivision mrDivision) {
        this.mrDivision = mrDivision;
    }

    public List<District> getDistrictNameList() {
        return districtNameList;
    }

    public void setDistrictNameList(List<District> districtNameList) {
        this.districtNameList = districtNameList;
    }

    public List<DSDivision> getDsDivisionNameList() {
        return dsDivisionNameList;
    }

    public void setDsDivisionNameList(List<DSDivision> dsDivisionNameList) {
        this.dsDivisionNameList = dsDivisionNameList;
    }

    public List<BDDivision> getBdDivisionNameList() {
        return bdDivisionNameList;
    }

    public void setBdDivisionNameList(List<BDDivision> bdDivisionNameList) {
        this.bdDivisionNameList = bdDivisionNameList;
    }

    public List<MRDivision> getMrDivisionNameList() {
        return mrDivisionNameList;
    }

    public void setMrDivisionNameList(List<MRDivision> mrDivisionNameList) {
        this.mrDivisionNameList = mrDivisionNameList;
    }

    public int getMrdivisionId() {
        return mrdivisionId;
    }

    public void setMrdivisionId(int mrdivisionId) {
        this.mrdivisionId = mrdivisionId;
    }

    public Map getSession() {
        return session;
    }

    public Map<Integer, String> getCurrentbdDivisionList() {
        return currentbdDivisionList;
    }

    public void setCurrentbdDivisionList(Map<Integer, String> currentbdDivisionList) {
        this.currentbdDivisionList = currentbdDivisionList;
    }

    public Map<Integer, String> getCurrentDistrictList() {
        return currentDistrictList;
    }

    public void setCurrentDistrictList(Map<Integer, String> currentDistrictList) {
        this.currentDistrictList = currentDistrictList;
    }

    public List<Court> getCourtNameList() {
        return courtNameList;
    }

    public void setCourtNameList(List<Court> courtNameList) {
        this.courtNameList = courtNameList;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public List<Location> getLocationNameList() {
        return locationNameList;
    }

    public void setLocationNameList(List<Location> locationNameList) {
        this.locationNameList = locationNameList;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
