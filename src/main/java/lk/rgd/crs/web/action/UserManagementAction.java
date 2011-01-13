package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.ErrorCodes;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.*;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.util.HashUtil;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.CourtDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.Court;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.service.MasterDataManagementService;
import lk.rgd.crs.api.service.PRSRecordsIndexer;
import lk.rgd.crs.core.service.BirthRecordsIndexer;
import lk.rgd.crs.core.service.DeathRecordsIndexer;
import lk.rgd.crs.web.WebConstants;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * @author amith jayasekara
 * @author Duminda Dharmakeerthi
 */
public class UserManagementAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(UserManagementAction.class);
    private final UserManager service;
    private final MasterDataManagementService dataManagementService;
    private Map session;
    private User user;
    private User currentUser;
    private int pageNo;
    private int pageType;
    private String divisions = new String();
    private String button;
    private int[] assignedDistricts;
    private int[] assignedDivisions;
    private int[] assignedLocations;
    private List<User> usersList;
    private List<District> districtNameList;
    private List<DSDivision> dsDivisionNameList;
    private List<BDDivision> bdDivisionNameList;
    private List<MRDivision> mrDivisionNameList;
    private List<Court> courtNameList;
    private List<Location> locationNameList;
    private List<UserLocation> userLocationNameList;
    private String nameOfUser;
    private String userId;

    private String userName;
    private String language;

    private int userDistrictId;
    private int dsDivisionId;
    private int divisionId;
    private int mrdivisionId;
    private int courtId;
    private int locationId;
    private boolean nextFlag;
    private boolean previousFlag;
    private int noOfRows;
    private int indexRecord;
    private boolean activate;
    private boolean changePassword;
    private String randomPassword;

    private String districtEn;
    private String dsDivisionEn;
    private String msg;
    private String selectedRole;

    private BDDivision bdDivision;
    private DSDivision dsDivision;
    private District district;
    private MRDivision mrDivision;
    private Court court;
    private UserLocation userLocation;
    private boolean newUser;

    private Location location;
    private int primaryLocation;
    private List<Location> primaryLocationSelectionList;

    private String roleId;
    private final DistrictDAO districtDAO;
    private final RoleDAO roleDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final MRDivisionDAO mrDivisionDAO;
    private final CourtDAO courtDAO;
    private final LocationDAO locationDAO;
    private final UserDAO userDAO;
    private final UserLocationDAO userLocationDAO;
    private final AppParametersDAO appParametersDAO;
    private static final String BA_ROWS_PER_PAGE = "crs.br_rows_per_page";

    private Map<Integer, String> districtList;
    private Map<Integer, String> divisionList;
    private Map<String, String> roleList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> locationList;

    /*support variables*/
    private Map<Integer, String> currentbdDivisionList; //users current
    private Map<Integer, String> currentDistrictList;

    private final BirthRecordsIndexer birthRecordsIndexer;
    private final DeathRecordsIndexer deathRecordsIndexer;
    private final PRSRecordsIndexer prsRecordsIndexer;

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public UserManagementAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, RoleDAO roleDAO, UserManager service, CourtDAO courtDAO,
        BDDivisionDAO bdDivisionDAO, MasterDataManagementService dataManagementService, MRDivisionDAO mrDivisionDAO, LocationDAO locationDAO,
        AppParametersDAO appParametersDAO, UserLocationDAO userLocationDAO, UserDAO userDAO,
        BirthRecordsIndexer birthRecordsIndexer, DeathRecordsIndexer deathRecordsIndexer, PRSRecordsIndexer prsRecordsIndexer) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.roleDAO = roleDAO;
        this.service = service;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dataManagementService = dataManagementService;
        this.mrDivisionDAO = mrDivisionDAO;
        this.courtDAO = courtDAO;
        this.locationDAO = locationDAO;
        this.appParametersDAO = appParametersDAO;
        this.userLocationDAO = userLocationDAO;
        this.userDAO = userDAO;
        this.birthRecordsIndexer = birthRecordsIndexer;
        this.deathRecordsIndexer = deathRecordsIndexer;
        this.prsRecordsIndexer = prsRecordsIndexer;
    }

    public String createUser() {
        /* get the current user who are logged in */
        /* user = new User */
        User currentUser = (User) session.get(WebConstants.SESSION_USER_BEAN);
        user.setRole(roleDAO.getRole(roleId));
        user.setStatus(User.State.INACTIVE);

        Set assDistrict = new HashSet();
        Set assDSDivision = new HashSet();

        for (int i = 0; i < assignedDistricts.length; i++) {
            assDistrict.add(districtDAO.getDistrict(assignedDistricts[i]));
        }

        if (roleId.equals(Role.ROLE_DEO) || roleId.equals(Role.ROLE_ADR)) {
            for (int i = 0; i < assignedDistricts.length; i++) {
                assDSDivision.add(dsDivisionDAO.getDSDivisionByPK(assignedDivisions[i]));
            }
        }
        if (roleId.equals(Role.ROLE_DR) || roleId.equals(Role.ROLE_ARG)) {
            Iterator<District> it = assDistrict.iterator();
            while (it.hasNext()) {
                assDSDivision.addAll(dsDivisionDAO.getAllDSDivisionByDistrictKey(it.next().getDistrictUKey()));
            }
        }

        //TODO change password length
        int randomPasswordLength = (int) (Math.random() * 6) + 10;
        if (userId == null) {
            randomPassword = getRandomPassword(randomPasswordLength);
            user.setPasswordHash(hashPassword(randomPassword));
            user.setAssignedBDDistricts(assDistrict);
            user.setAssignedMRDistricts(assDistrict);
            user.setAssignedBDDSDivisions(assDSDivision);
            user.setAssignedMRDSDivisions(assDSDivision);
            /*if (roleId.equals(Role.ROLE_RG) || roleId.equals(Role.ROLE_ADMIN)) {
                user.setPrimaryLocation(locationDAO.getLocation(1));
                userLocation = new UserLocation();
                userLocation.setLocation(user.getPrimaryLocation());
                userLocation.setLocationId(1);
                userLocation.setUser(user);
            }*/
            try {
                service.createUser(user, currentUser);
            } catch (RGDRuntimeException e) {
                if (e.getErrorCode() == ErrorCodes.ENTITY_ALREADY_EXIST) {
                    addActionMessage(getText("user.already.assigned"));
                    pageNo = 2;
                    return SUCCESS;
                }
            }
            userId = user.getUserId();
            addActionMessage(getText("data.Save.Success.label"));
            pageNo = 1;
        } else {
            logger.debug("Edited user name {}", user.getUserName());

            User updatedUser = service.getUserByID(userId);

            updatedUser.setUserName(user.getUserName());
            updatedUser.setPin(user.getPin());
            updatedUser.setSienSignatureText(user.getSienSignatureText());
            updatedUser.setTaenSignatureText(user.getTaenSignatureText());
            updatedUser.setPrefLanguage(user.getPrefLanguage());

            updatedUser.setAssignedBDDistricts(assDistrict);
            updatedUser.setAssignedMRDistricts(assDistrict);
            updatedUser.setAssignedBDDSDivisions(assDSDivision);
            updatedUser.setAssignedMRDSDivisions(assDSDivision);
            updatedUser.setRole(roleDAO.getRole(roleId));
            /*if (roleDAO.getRole(roleId).equals(Role.ROLE_RG) || roleDAO.getRole(roleId).equals(Role.ROLE_ADMIN)) {
                user.setPrimaryLocation(locationDAO.getLocation(1));
                userLocation = new UserLocation();
                userLocation.setLocation(user.getPrimaryLocation());
                userLocation.setLocationId(1);
                userLocation.setUser(user);
            }*/

            if (isAssignedLocations(updatedUser)) {
                updatedUser.setStatus(User.State.ACTIVE);
            } else {
                updatedUser.setStatus(User.State.INACTIVE);
            }
            if (changePassword) {
                logger.debug("Change password {}", userDAO.getUserByPK(userId).getUserName());
                randomPassword = getRandomPassword(randomPasswordLength);
                updatedUser.setPasswordHash(hashPassword(randomPassword));
            }
            service.updateUser(updatedUser, currentUser);
            //todo : to be removed
            //session.remove(WebConstants.SESSION_UPDATED_USER);
            session.put("viewUsers", null);
            addActionMessage(getText("edit.Data.Save.Success.label"));
            pageNo = 3;
        }
        populate();
        return SUCCESS;
    }

    public boolean isAssignedLocations(User u) {
        if (u.getLocations().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public String inactiveUser() {      /* delete icon clicked */
        populate();
        user = service.getUserByID(userId);
        service.deleteUser(user, (User) session.get(WebConstants.SESSION_USER_BEAN));
        logger.debug("Deleting  user {} is successoption body.full", user.getUserId());
        usersList = service.getAllUsers();      /* because of this user loses his search result */
        session.put("viewUsers", usersList);
        return "success";
    }

    public String activeUser() {
        populate();
        user = service.getUserByID(userId);
        user.setLoginAttempts(1);
        user.getLifeCycleInfo().setActive(true);
        service.updateUser(user, (User) session.get(WebConstants.SESSION_USER_BEAN));
        usersList = service.getAllUsers();
        session.put("viewUsers", usersList);
        return "success";
    }

    public String initUser() {
        populate();

        User currentUser = (User) session.get(WebConstants.SESSION_USER_BEAN);     // admin
        logger.debug("user language {}", currentUser.getPrefLanguage());

        populateDynamicLists(currentUser.getPrefLanguage());

        if (userId != null) {
            user = service.getUserByID(userId);
            currentDistrictList = convertDistricSetToMap(user.getAssignedBDDistricts());
            currentbdDivisionList = convertDivisionSetToMap(user.getAssignedBDDSDivisions());
            roleId = user.getRole().getRoleId();
            logger.info("current district list size  : {} for user : {}", currentDistrictList.size(), user.getUserName());
            logger.info("current division list size  : {} for user : {}", currentbdDivisionList.size(), user.getUserName());
        }
        return "pageLoad";
    }

    public String viewUsers() {
        populate();
        session.put("viewUsers", null);
        return "success";
    }

    public String initAssignedUserLocation() {
        userLocationNameList = userLocationDAO.getUserLocationsListByUserId(userId);
        if (userLocationNameList != null) {
            logger.debug("size of the user location list is :{}", userLocationNameList.size());
        }
        Location prmLocation = userDAO.getUserByPK(userId).getPrimaryLocation();
        if (prmLocation != null) {
            primaryLocation = prmLocation.getLocationUKey();
        }
        populateLocationListOnly();
        //populate();
        return SUCCESS;
    }

    public String editPrimaryLocation() {
        User user = userDAO.getUserByPK(userId);
        UserLocation userLocation = userLocationDAO.getUserLocation(user.getUserId(), locationId);
        //service.addUserLocation(userLocation, currentUser, true);
        addPrimaryLocation(userLocation, currentUser);
        Location prmLocation = userDAO.getUserByPK(userId).getPrimaryLocation();
        if (prmLocation != null) {
            primaryLocation = prmLocation.getLocationUKey();
        } else {
            primaryLocation = locationId;
        }
        //primaryLocation = locationId;
        logger.debug("Active location of {} user is :{}", userId, locationDAO.getLocation(locationId).getEnLocationName());
        userLocationNameList = userLocationDAO.getUserLocationsListByUserId(userId);
        //populate();
        populateLocationListOnly();
        return SUCCESS;
    }

    public String assignedUserLocation() {
        //primaryLocation = userDAO.getUserByPK(userId).getPrimaryLocation().getLocationUKey();
        Location prmLocation = userDAO.getUserByPK(userId).getPrimaryLocation();
        if (prmLocation != null) {
            primaryLocation = prmLocation.getLocationUKey();
        }
        if (pageType == 0) {
            UserLocation checkUserLocation = userLocationDAO.getUserLocation(userId, locationId);
            if (checkUserLocation != null) {
                addFieldError("duplicateIdNumberError", "This Location  Already Assigned For User   :" + userId);
                logger.debug("{} location is already assigned for user  :{}", locationDAO.getLocation(locationId).getEnLocationName(), userId);
            } else {
                //userLocation.setLocation();
                Location tempLocation = locationDAO.getLocation(locationId);
                if (tempLocation != null) {
                    userLocation.setLocation(tempLocation);
                } else {
                    addActionError(getText("please.select.valid.location"));
                    populateLocationListOnly();
                    userLocationNameList = userLocationDAO.getUserLocationsListByUserId(userId);
                    return SUCCESS;
                }
                userLocation.setUserId(userId);
                userLocation.setUser(userDAO.getUserByPK(userId));
                service.addUserLocation(userLocation, currentUser);
                if (userDAO.getUserByPK(userId).getPrimaryLocation() == null) {
                    addPrimaryLocation(userLocation, currentUser);
                }

                //userDAO.addPrimaryLocation(User user, Location primaryLocation);
                logger.debug("Add New UserLocation : {} for user :{}", locationDAO.getLocation(locationId).getEnLocationName(), userId);
                userLocation = null;
            }
        }
        if (pageType == 1) {
            try {
                userLocation.setLocation(locationDAO.getLocation(locationId));
                userLocation.setUserId(userId);
                service.updateUserLocation(userLocation, currentUser);
                logger.debug("Updated user location of {} is : {}", userLocation.getUserId(), userLocation.getLocation().getEnLocationName());
                userLocation = null;
                pageType = 0;
            } catch (Exception e) {
                addFieldError("duplicateIdNumberError", "This Location Can Not Be Edit");
            }
        }
        //populate();
        populateLocationListOnly();
        userLocationNameList = userLocationDAO.getUserLocationsListByUserId(userId);
        return SUCCESS;
    }

    private void addPrimaryLocation(UserLocation userLocation, User currentUser) {
        if (userLocation.getLifeCycleInfo().isActive()) {
            Location location = userLocation.getLocation();
            User user = userLocation.getUser();
            user.setPrimaryLocation(location);
            service.updateUser(user, currentUser);
            primaryLocation = location.getLocationUKey();
        } else {
            addActionError(getText("inactive.primary.warning"));
        }
    }

    public String activeUserLocation() {
        Location prmLocation = userDAO.getUserByPK(userId).getPrimaryLocation();
        if (prmLocation != null) {
            primaryLocation = prmLocation.getLocationUKey();
        }
        service.activeUserLocation(userId, locationId, currentUser);
        logger.debug("Active location of {} user is :{}", userId, locationDAO.getLocation(locationId).getEnLocationName());
        userLocationNameList = userLocationDAO.getUserLocationsListByUserId(userId);
        //populate();
        populateLocationListOnly();
        return SUCCESS;
    }

    public String inactiveUserLocation() {
        Location prmLocation = userDAO.getUserByPK(userId).getPrimaryLocation();
        if (prmLocation != null) {
            primaryLocation = prmLocation.getLocationUKey();
        }
        if (primaryLocation != locationId) {
            service.inactiveUserLocation(userId, locationId, currentUser);
        } else {
            addActionError(getText("primary.inactive.warning"));
        }
        logger.debug("Inactive location of {} user is :{}", userId, locationDAO.getLocation(locationId).getEnLocationName());
        userLocationNameList = userLocationDAO.getUserLocationsListByUserId(userId);
        populateLocationListOnly();
        //populate();
        return SUCCESS;
    }


    public String editAssignedUserLocation() {
        userLocationNameList = null;
        userLocation = userLocationDAO.getUserLocation(userId, locationId);
        pageType = 1;
        populate();
        return SUCCESS;
    }

    public String initAddDivisionsAndDsDivision() {
        populate();
        populateDynamicLists(language);
        return "success";
    }

    public String initDivisionList() {
        populate();
        setDivisionList(true);
        return SUCCESS;
    }

    public void setDivisionList(boolean setNull) {
        switch (pageType) {
            case 1:
                districtNameList = districtDAO.findAll();
                if (setNull) {
                    district = null;
                }
                break;
            case 2:
                districtEn = districtDAO.getNameByPK(userDistrictId, "en");
                dsDivisionNameList = dsDivisionDAO.getAllDSDivisionByDistrictKey(userDistrictId);
                if (setNull) {
                    dsDivision = null;
                }
                break;
            case 3:
                districtEn = districtDAO.getNameByPK(userDistrictId, "en");
                dsDivisionEn = dsDivisionDAO.getNameByPK(dsDivisionId, "en");
                bdDivisionNameList = bdDivisionDAO.getAllDSDivisionByDsDivisionKey(dsDivisionId);
                if (setNull) {
                    bdDivision = null;
                }
                break;
            case 4:
                districtEn = districtDAO.getNameByPK(userDistrictId, "en");
                dsDivisionEn = dsDivisionDAO.getNameByPK(dsDivisionId, "en");
                mrDivisionNameList = mrDivisionDAO.findAll();
                if (setNull) {
                    mrDivision = null;
                }
                break;
            case 5:
                courtNameList = courtDAO.findAll();
                logger.debug("Size of the loaded Court list is :{}", courtNameList.size());
                if (setNull) {
                    court = null;
                }
                break;
            case 6:
                districtEn = districtDAO.getNameByPK(userDistrictId, "en");
                dsDivisionEn = dsDivisionDAO.getNameByPK(dsDivisionId, "en");
                locationNameList = locationDAO.getAllLocations();
                logger.debug("Size of the loaded Lacation List is :{}", locationNameList.size());
                if (setNull) {
                    location = null;
                }
                break;


        }
    }

    public String activeOrInactive() {
        switch (pageType) {
            case 1:
                dataManagementService.activateOrInactivateDistrict(userDistrictId, activate, currentUser);
                break;
            case 2:
                dataManagementService.activateOrInactivateDSDivision(dsDivisionId, activate, currentUser);
                break;
            case 3:
                dataManagementService.activateOrInactiveBDDivision(divisionId, activate, currentUser);
                break;
            case 4:
                dataManagementService.activateOrInactivateMRDivision(mrdivisionId, activate, currentUser);
                break;
            case 5:
                dataManagementService.activateOrInactivateCourt(courtId, activate, currentUser);
                break;
            case 6:
                dataManagementService.activateOrInactivateLocation(locationId, activate, currentUser);
                break;

        }
        setDivisionList(true);
        return SUCCESS;
    }

    public String addDivisionsAndDsDivisions() {
        int checkDuplicate = 0;
        switch (pageType) {
            case 1:
                District checkDistrit = districtDAO.getDistrictByCode(district.getDistrictId());
                if (checkDistrit != null) {
                    addFieldError("duplicateIdNumberError", "District Id Number Already Used. Please Insert Another Number");
                    logger.debug("Duplicate District code number is :", checkDistrit.getDistrictId());
                    checkDuplicate++;
                }
                if (checkDuplicate == 0) {
                    district.setActive(true);
                    dataManagementService.addDistrict(district, currentUser);
                    logger.debug("New Id of new District {} is   :{}", district.getEnDistrictName(), district.getDistrictId());
                    msg = " New District Was Added  :" + district.getEnDistrictName();
                }
                break;
            case 2:
                DSDivision checkDSDivision = dsDivisionDAO.getDSDivisionByCode(dsDivision.getDivisionId(),
                    districtDAO.getDistrict(userDistrictId));
                if (checkDSDivision != null) {
                    addFieldError("duplicateIdNumberError", "DS Division Id Number Already Used. Please Insert Another Number");
                    logger.debug("Duplicate District code number is :", checkDSDivision.getDivisionId());
                    checkDuplicate++;
                }
                if (checkDuplicate == 0) {
                    dsDivision.setDistrict(districtDAO.getDistrict(userDistrictId));
                    dsDivision.setActive(true);
                    dataManagementService.addDSDivision(dsDivision, currentUser);
                    logger.debug("New Id of new Ds Division {} is   :{}", dsDivision.getEnDivisionName(), dsDivision.getDivisionId());
                    msg = "New DSDivision Was Added :" + dsDivision.getEnDivisionName();
                }
                break;
            case 3:
                BDDivision checkBDDivision = bdDivisionDAO.getBDDivisionByCode(bdDivision.getDivisionId(), dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                if (checkBDDivision != null) {
                    addFieldError("duplicateIdNumberError", "Division Id Number Already Used. Please Insert Another Number");
                    logger.debug("Duplicate District code number is :", checkBDDivision.getDivisionId());
                    checkDuplicate++;
                }
                if (checkDuplicate == 0) {
                    bdDivision.setDsDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                    bdDivision.setActive(true);
                    dataManagementService.addBDDivision(bdDivision, currentUser);
                    logger.debug("New Id of New Division {} is   :{}", bdDivision.getEnDivisionName(), bdDivision.getDivisionId());
                    msg = "New BDDivision Was Added  :" + bdDivision.getEnDivisionName();
                }
                break;
            case 4:
                MRDivision checkMrDivision = mrDivisionDAO.getMRDivisionByCode(mrDivision.getDivisionId(), dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                if (checkMrDivision != null) {
                    addFieldError("duplicateIdNumberError", "MR Division Id Number Already Used. Please Insert Another Number");
                    logger.debug("Duplicate MR Division code number is :", checkMrDivision.getDivisionId());
                    checkDuplicate++;
                }
                if (checkDuplicate == 0) {
                    mrDivision.setDsDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                    mrDivision.setActive(true);
                    dataManagementService.addMRDivision(mrDivision, currentUser);
                    logger.debug("New Id of New MRDivision {} is   :{}", mrDivision.getEnDivisionName(), mrDivision.getDivisionId());
                    msg = "New MRDivision Was Added  :" + mrDivision.getEnDivisionName();
                }
                break;
            case 5:
                Court checkCourt = courtDAO.getCourtByCode(court.getCourtId());
                if (checkCourt != null) {
                    addFieldError("duplicateIdNumberError", "Court Id Code Already Used. Please Insert Another Number");
                    logger.debug("Duplicate Court code number is :", court.getCourtId());
                    checkDuplicate++;
                }
                if (checkDuplicate == 0) {
                    dataManagementService.addCourt(court, currentUser);
                    logger.debug("New Id of New Court {} is  :{}", locationDAO.getLocation(locationId), locationId);
                    msg = "New Court Was Added  :" + court.getEnCourtName();
                }
                break;
            case 6:
                Location checkLocation = locationDAO.getLocationByCodeAndByDSDivisionID(location.getLocationCode(), dsDivisionId);
                if (checkLocation != null) {
                    addFieldError("duplicateIdNumberError", "Location Code and DSDivision ID Already Used. Please check again");
                    logger.debug("Duplicate Location code number is :", checkLocation.getLocationCode());
                    checkDuplicate++;
                }
                if (checkDuplicate == 0) {
                    location.setDsDivisionId(dsDivisionId);
                    dataManagementService.addLocation(location, currentUser);
                    logger.debug("New Id of New Location {} is  :{}", locationDAO.getLocation(locationId), locationId);
                    msg = "New Location Was Added  :" + location.getEnLocationName();
                }

        }
        if (checkDuplicate == 0) {
            setDivisionList(true);
        }
        if (checkDuplicate == 1) {
            setDivisionList(false);
        }
        return SUCCESS;
    }

    public String selectUsers() {
        //populate();
        selectedRole = roleId;
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
        populate();
        return "success";
    }

    public String indexRecords() {
        logger.debug("indexing records by indexRecord number : {}", indexRecord);

        switch (indexRecord) {
            case 0:
                logger.debug("Indexing Record page loaded");
                break;
            case 1:
                if (birthRecordsIndexer.indexAll()) {
                    logger.debug("Birth Records Re-indexed Successfully");
                    addActionMessage("Birth Record Re-Index Completed");
                } else {
                    addActionMessage("Birth record re-indexing failed. Check log for details");
                }
                break;
            case 2:
                if (deathRecordsIndexer.indexAll()) {
                    logger.debug("Death Records Re-indexed Successfully");
                    addActionMessage("Death Record Re-Index Completed");
                } else {
                    addActionMessage("Death record re-indexing failed. Check log for details");
                }
                break;
            case 3:
                if (prsRecordsIndexer.indexAll()) {
                    addActionMessage("PRS Record Re-Index Completed");
                    logger.debug("PRS Records Re-indexed Successfully");
                } else {
                    addActionMessage("PRS record re-indexing failed. Check log for details");
                }
                break;
            default:
                birthRecordsIndexer.indexAll();
                deathRecordsIndexer.indexAll();
                prsRecordsIndexer.indexAll();
                addActionMessage("All Records Re-Index Completed");
                logger.debug("All REcords Re-indexed Successfully");
        }

        return SUCCESS;
    }

    private void populateLocationListOnly() {
        locationList = new HashMap<Integer, String>();
        Map<Integer, Location> allLocations = locationDAO.getPreLoadedLocations();
        User user = userDAO.getUserByPK(userId);
        Set<DSDivision> st = user.getAssignedBDDSDivisions();

        for (DSDivision ds : st) {
            locationList.putAll(locationDAO.getLocationByDSDivisionID(ds.getDsDivisionUKey(), user.getPrefLanguage()));

        }
        if (userLocationDAO.getActiveUserLocations(userId, true).size() > 0) {
            user.setStatus(User.State.ACTIVE);
            service.updateUser(user);
            logger.debug("User Activated {}", user.getUserName());
        } else {
            user.setStatus(User.State.INACTIVE);
            service.updateUser(user);
            logger.debug("User Deactivated {}", user.getUserName());
        }
        logger.debug("List : {}", locationList.size());

        if (user.getRole().getRoleId().equals(Role.ROLE_RG) || user.getRole().getRoleId().equals(Role.ROLE_ADMIN)) {
            locationList.put(1, locationDAO.getLocationNameByPK(1, user.getPrefLanguage()));
        }

    }

    /**
     * populates Districts, Roles and Locations
     */
    private void populate() {
        logger.debug("USER ID = {}", userId);
        logger.debug("lang : {}", language);
        if (districtList == null) {
            districtList = districtDAO.getAllDistrictNames(language, user);
        }
        if (roleList == null) {
            roleList = roleDAO.getRoleList();
            Iterator<String> it = roleList.keySet().iterator();
            while (it.hasNext()) {
                String r = it.next();
                if (r.equals(Role.ROLE_DEO)) {
                    roleId = r;
                }
            }
        }
        if (locationList == null) {
            locationList = locationDAO.getLocationList(language, currentUser);
            logger.debug("size of the location list is :{}", locationList.size());
        }
    }

    /**
     * this sets default values that should appear on the jsp page
     */
    private void populateDynamicLists(String language) {
        if (userDistrictId == 0) {
            if (!districtList.isEmpty()) {
                userDistrictId = districtList.keySet().iterator().next();
                logger.debug("first allowed district in the list {} was set", userDistrictId);
            }
        }
        dsDivisionList = dsDivisionDAO.getAllDSDivisionNames(userDistrictId, language, user);

        if (dsDivisionId == 0) {
            if (!dsDivisionList.isEmpty()) {
                dsDivisionId = dsDivisionList.keySet().iterator().next();
                logger.debug("first allowed DS Div in the list {} was set", dsDivisionId);
            }
        }
        divisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);

        if (divisionId == 0) {
            divisionId = dsDivisionList.keySet().iterator().next();
            logger.debug("first allowed BD Div in the list {} was set", divisionId);
        }
    }

    private void generateDSDivisions() {
        for (int i = 0; i < assignedDistricts.length; i++) {
            if (i == 0) {
                setDivisionList(dsDivisionDAO.getAllDSDivisionNames(assignedDistricts[i], language, user));
            } else {
                getDivisionList().putAll(dsDivisionDAO.getAllDSDivisionNames(assignedDistricts[i], language, user));
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
            ret.put(dsd.getDsDivisionUKey(), dsd.getEnDivisionName());
        }
        return ret;
    }

    private String getRandomPassword(int length) {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        char[] charArray = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9'};
        for (int i = 0; i < length; i++) {
            buffer.append(charArray[random.nextInt(charArray.length)]);
        }
        return buffer.toString();
    }

    private final String hashPassword(String password) {
        return HashUtil.hashString(password);
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
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
    }

    public int getUserDistrictId() {
        return userDistrictId;
    }

    public void setUserDistrictId(int userDistrictId) {
        this.userDistrictId = userDistrictId;
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

    public boolean isNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(boolean nextFlag) {
        this.nextFlag = nextFlag;
    }

    public boolean isPreviousFlag() {
        return previousFlag;
    }

    public void setPreviousFlag(boolean previousFlag) {
        this.previousFlag = previousFlag;
    }

    public int getNoOfRows() {
        return noOfRows;
    }

    public void setNoOfRows(int noOfRows) {
        this.noOfRows = noOfRows;
    }

    public int getPageType() {
        return pageType;
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    public Map<Integer, String> getLocationList() {
        return locationList;
    }

    public void setLocationList(Map<Integer, String> locationList) {
        this.locationList = locationList;
    }

    public int[] getAssignedLocations() {
        return assignedLocations;
    }

    public void setAssignedLocations(int[] assignedLocations) {
        this.assignedLocations = assignedLocations;
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }

    public List<UserLocation> getUserLocationNameList() {
        return userLocationNameList;
    }

    public void setUserLocationNameList(List<UserLocation> userLocationNameList) {
        this.userLocationNameList = userLocationNameList;
    }

    public int getIndexRecord() {
        return indexRecord;
    }

    public void setIndexRecord(int indexRecord) {
        this.indexRecord = indexRecord;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

    public String getRandomPassword() {
        return randomPassword;
    }

    public void setRandomPassword(String randomPassword) {
        this.randomPassword = randomPassword;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPrimaryLocation() {
        return primaryLocation;
    }

    public void setPrimaryLocation(int primaryLocation) {
        this.primaryLocation = primaryLocation;
    }

    public List<Location> getPrimaryLocationSelectionList() {
        return primaryLocationSelectionList;
    }

    public void setPrimaryLocationSelectionList(List<Location> primaryLocationSelectionList) {
        this.primaryLocationSelectionList = primaryLocationSelectionList;
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }
}
