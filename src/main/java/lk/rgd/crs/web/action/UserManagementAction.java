package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.*;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.util.CommonUtil;
import lk.rgd.common.util.HashUtil;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.CourtDAO;
import lk.rgd.crs.api.dao.GNDivisionDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.Court;
import lk.rgd.crs.api.domain.GNDivision;
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
    private List<GNDivision> gnDivisionNameList;
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
    private int gnDivisionId;
    private int selectDistrictId;
    private int selectDSDivisionId;
    private int[] gnDivisions;
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
    private GNDivision gnDivision;
    private boolean newUser;
    private boolean editMode;

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
    private final GNDivisionDAO gnDivisionDAO;
    private final UserDAO userDAO;
    private final UserLocationDAO userLocationDAO;
    private final AppParametersDAO appParametersDAO;
    private static final String BA_ROWS_PER_PAGE = "crs.br_rows_per_page";
    private static final String VIEW_USERS = "viewUsers";
    private static final String COLON = " : ";

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
        BirthRecordsIndexer birthRecordsIndexer, DeathRecordsIndexer deathRecordsIndexer, PRSRecordsIndexer prsRecordsIndexer, GNDivisionDAO gnDivisionDAO) {
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
        this.gnDivisionDAO = gnDivisionDAO;
    }

    public String createUser() {
        User currentUser = (User) session.get(WebConstants.SESSION_USER_BEAN);
        boolean isNewUser = false;
        int randomPasswordLength = (int) (Math.random() * 6) + 10;
        randomPassword = getRandomPassword(randomPasswordLength);

        try {
            isNewUser = service.createUser(user, currentUser, userId, roleId, assignedDistricts, assignedDivisions, changePassword, randomPassword);
        } catch (RGDRuntimeException e) {
            if (e.getErrorCode() == ErrorCodes.ENTITY_ALREADY_EXIST) {
                addActionMessage(getText("user.already.assigned"));
                pageNo = 2;
                return SUCCESS;
            }
        }
        if (isNewUser) {
            userId = user.getUserId();
            addActionMessage(getText("data.Save.Success.label"));
            pageNo = 1;
        } else {
            session.put(VIEW_USERS, null);
            addActionMessage(getText("edit.Data.Save.Success.label"));
            pageNo = 3;
        }

        populate();
        return SUCCESS;
    }

    public boolean isAssignedLocations(User u) {
        return !u.getLocations().isEmpty();
    }

    /**
     * delete icon clicked
     */
    public String inactiveUser() {
        populate();
        user = service.getUserByID(userId);
        user.getLifeCycleInfo().setActive(false);
        service.deleteUser(user, (User) session.get(WebConstants.SESSION_USER_BEAN));
        logger.debug("User : {} deleted successfully by {}", user.getUserId(), currentUser.getUserId());
        filterUsers();
        addActionMessage("User : " + userId + " Deleted Successfully");

        session.put(VIEW_USERS, usersList);
        return SUCCESS;
    }

    public String doInactiveUser() {
        populate();
        user = service.getUserByID(userId);
        user.getLifeCycleInfo().setActive(false);
        service.updateUser(user, (User) session.get(WebConstants.SESSION_USER_BEAN));
        logger.debug("User : {} inactivated successfully by {}", userId, currentUser.getUserId());
        filterUsers();
        addActionMessage("User : " + userId + " Inactivated Successfully");

        session.put(VIEW_USERS, usersList);
        return SUCCESS;
    }

    public String activeUser() {
        populate();
        user = service.getUserByID(userId);

        userLocationNameList = userLocationDAO.getUserLocationsListByUserId(userId);
        if (userLocationNameList != null && !userLocationNameList.isEmpty()) {
            user.setLoginAttempts(1);
            user.getLifeCycleInfo().setActive(true);
            service.updateUser(user, (User) session.get(WebConstants.SESSION_USER_BEAN));
            logger.debug("User : {} activated successfully by {}", userId, currentUser.getUserId());
            addActionMessage("User : " + userId + " Activated Successfully");
        } else {
            addActionError("Please Assign Locations to User : " + userId + ", Before Trying to Activate");
        }
        filterUsers();
        session.put(VIEW_USERS, usersList);
        return SUCCESS;
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
            logger.debug("current district list size  : {} for user : {}", currentDistrictList.size(), user.getUserName());
            logger.debug("current division list size  : {} for user : {}", currentbdDivisionList.size(), user.getUserName());
        }
        return "pageLoad";
    }

    public String viewUsers() {
        populate();
        roleId = "ALL";
        session.put(VIEW_USERS, null);
        return SUCCESS;
    }

    public String initAssignedUserLocation() {
        userLocationNameList = userLocationDAO.getUserLocationsListByUserId(userId);
        if (userLocationNameList != null) {
            logger.debug("size of the user location list is :{}", userLocationNameList.size());
        }
        User user = userDAO.getUserByPK(userId);
        Location prmLocation = user.getPrimaryLocation();
        if (prmLocation != null) {
            primaryLocation = prmLocation.getLocationUKey();
        }
        populateLocationListOnly(user);
        //populate();                                                                                                     // todo shan
        return SUCCESS;
    }

    public String editPrimaryLocation() {
        User user = userDAO.getUserByPK(userId);
        Location toPrimary = locationDAO.getLocation(locationId);
        UserLocation uLocation = userLocationDAO.getUserLocation(userId, locationId);
        if (!uLocation.getLifeCycleInfo().isActive()) {
            addActionError(getText("inactive.primary.warning"));
            primaryLocation = service.getUserByID(userId).getPrimaryLocation().getLocationUKey();
            userLocationNameList = userLocationDAO.getUserLocationsListByUserId(userId);
            populateLocationListOnly(user);
            return SUCCESS;
        }
        user.setPrimaryLocation(toPrimary);
        service.updateUser(user);
        //service.addUserLocation(userLocation, currentUser, true);
        //addPrimaryLocation(userLocation, currentUser);

        Location prmLocation = service.getUserByID(userId).getPrimaryLocation();
        if (prmLocation != null) {
            primaryLocation = prmLocation.getLocationUKey();
        } else {
            primaryLocation = locationId;
        }
        //primaryLocation = locationId;
        logger.debug("Active location of {} user is :{}", userId, locationDAO.getLocation(locationId).getEnLocationName());
        userLocationNameList = userLocationDAO.getUserLocationsListByUserId(userId);
        //populate();
        populateLocationListOnly(user);
        return SUCCESS;
    }

    public String assignedUserLocation() {
        User user = userDAO.getUserByPK(userId);
        Location prmLocation = user.getPrimaryLocation();
        if (prmLocation != null) {
            primaryLocation = prmLocation.getLocationUKey();
        }
        if (pageType == 0) {
            UserLocation checkUserLocation = userLocationDAO.getUserLocation(userId, locationId);
            if (checkUserLocation != null) {
                addFieldError("duplicateIdNumberError", getText("duplicate.location") + userId);
                logger.debug("{} location is already assigned for user  :{}", locationDAO.getLocation(locationId).getEnLocationName(), userId);
            } else {
                Location tempLocation = locationDAO.getLocation(locationId);
                if (tempLocation != null) {
                    userLocation.setLocation(tempLocation);
                } else {
                    addActionError(getText("please.select.valid.location"));
                    populateLocationListOnly(user);
                    userLocationNameList = userLocationDAO.getUserLocationsListByUserId(userId);
                    return SUCCESS;
                }
                userLocation.setUserId(userId);
                userLocation.setUser(user);
                service.addUserLocation(userLocation, currentUser);
                if (user.getPrimaryLocation() == null) {
                    user.setPrimaryLocation(tempLocation);
                    service.updateUser(user);
                    primaryLocation = service.getUserByID(userId).getPrimaryLocation().getLocationUKey();
                    logger.debug("Primary location {}", service.getUserByID(userId).getPrimaryLocation().getEnLocationName());
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
        populateLocationListOnly(user);
        userLocationNameList = userLocationDAO.getUserLocationsListByUserId(userId);
        return SUCCESS;
    }

    public String activeUserLocation() {
        User user = userDAO.getUserByPK(userId);
        Location prmLocation = user.getPrimaryLocation();
        if (prmLocation != null) {
            primaryLocation = prmLocation.getLocationUKey();
        }
        service.activeUserLocation(userId, locationId, currentUser);
        logger.debug("Active location of {} user is :{}", userId, locationDAO.getLocation(locationId).getEnLocationName());
        userLocationNameList = userLocationDAO.getUserLocationsListByUserId(userId);
        //populate();
        populateLocationListOnly(user);
        return SUCCESS;
    }

    public String inactiveUserLocation() {
        User user = userDAO.getUserByPK(userId);
        Location prmLocation = user.getPrimaryLocation();
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
        populateLocationListOnly(user);
        //populate();
        return SUCCESS;
    }


    public String editAssignedUserLocation() {
        userLocationNameList = null;
        userLocation = userLocationDAO.getUserLocation(userId, locationId);
        pageType = 1;
        roleId = userLocation.getUser().getRole().getName();
        populate();
        return SUCCESS;
    }

    public String initAddDivisionsAndDsDivision() {
        populate();
        populateDynamicLists(language);
        return SUCCESS;
    }

    public String initEditDivisionDetails() {
        switch (pageType) {
            // district edit option not provided
            case 2:
                districtEn = districtDAO.getNameByPK(userDistrictId, AppConstants.ENGLISH);
                dsDivision = dsDivisionDAO.getDSDivisionByPK(dsDivisionId);
                break;
            case 3:
                districtEn = districtDAO.getNameByPK(userDistrictId, AppConstants.ENGLISH);
                dsDivisionEn = dsDivisionDAO.getNameByPK(dsDivisionId, AppConstants.ENGLISH);
                bdDivision = bdDivisionDAO.getBDDivisionByPK(divisionId);
                bdDivisionNameList = null;
                break;
            case 4:
                districtEn = districtDAO.getNameByPK(userDistrictId, AppConstants.ENGLISH);
                dsDivisionEn = dsDivisionDAO.getNameByPK(dsDivisionId, AppConstants.ENGLISH);
                mrDivision = mrDivisionDAO.getMRDivisionByPK(mrdivisionId);
                mrDivisionNameList = null;
                break;
            case 5:
                court = courtDAO.getCourt(courtId);
                courtNameList = null;
                break;
            case 6:
                districtEn = districtDAO.getNameByPK(userDistrictId, AppConstants.ENGLISH);
                final DSDivision dsDivision = dsDivisionDAO.getDSDivisionByPK(dsDivisionId);
                dsDivisionEn = dsDivision.getEnDivisionName();
                location = locationDAO.getLocation(locationId);
                locationNameList = null;
                break;
            case 7:
                districtEn = districtDAO.getNameByPK(userDistrictId, AppConstants.ENGLISH);
                dsDivisionEn = dsDivisionDAO.getNameByPK(dsDivisionId, AppConstants.ENGLISH);
                gnDivision = gnDivisionDAO.getGNDivisionByPK(gnDivisionId);
                gnDivisionNameList = null;
                break;
        }
        return SUCCESS;
    }

    public String editDivisionDetails() {
        int checkDuplicate = 0;
        switch (pageType) {
            // districts can not be edited
            case 2:
                List<DSDivision> checkDSDivisions = dsDivisionDAO.getDSDivisionByAnyName(dsDivision);
                if (checkDSDivisions.size() > 0) {
                    if (checkNameDuplicatesForEdits(dsDivision.getDsDivisionUKey(), pageType, checkDSDivisions)) {
                        addFieldError("duplicateIdNumberError", "DS Division names are already used. Please check again");
                        logger.debug("DSDivision names are duplicated");
                        checkDuplicate++;
                        editMode = true;
                    }
                }
                if (checkDuplicate == 0) {
                    try {
                        dataManagementService.updateDSDivision(dsDivision, currentUser);
                        printMessage("update.success");
                    } catch (CRSRuntimeException e) {
                        if (e.getErrorCode() == ErrorCodes.ILLEGAL_STATE) {
                            addFieldError("duplicateIdNumberError", "Can not update Divisional Secretariat Division, since it have mapping records");
                        }
                    }
                }
                break;
            case 3:
                List<BDDivision> checkBDDivisions =
                    bdDivisionDAO.getBDDivisionByAnyNameAndDSDivisionKey(bdDivision, dsDivisionId);
                if (checkBDDivisions.size() > 0) {
                    if (checkNameDuplicatesForEdits(divisionId, pageType, checkBDDivisions)) {
                        addFieldError("duplicateIdNumberError", "BD Division names are already used. Please check again");
                        logger.debug("BDDivision names are duplicated");
                        checkDuplicate++;
                        editMode = true;
                    }
                }
                if (checkDuplicate == 0) {
                    bdDivision.setDsDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                    try {
                        dataManagementService.updateBDDivision(divisionId, bdDivision, currentUser);
                        printMessage("update.success");
                    } catch (CRSRuntimeException e) {
                        if (e.getErrorCode() == ErrorCodes.ILLEGAL_STATE) {
                            addFieldError("duplicateIdNumberError", "Can not update Birth Death Registration Division, since it have mapping records");
                        }
                    }
                }
                break;
            case 4:
                List<MRDivision> checkMrDivisions =
                    mrDivisionDAO.getMRDivisionByAnyNameAndDSDivision(mrDivision, dsDivisionId);

                if (checkMrDivisions.size() > 0) {
                    if (checkNameDuplicatesForEdits(mrdivisionId, pageType, checkMrDivisions)) {
                        addFieldError("duplicateIdNumberError", "MR Division names are already used. Please check again");
                        logger.debug("MRDivision names are duplicated");
                        checkDuplicate++;
                        editMode = true;
                    }
                }
                if (checkDuplicate == 0) {
                    try {
                        mrDivision.setDsDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                        dataManagementService.updateMRDivision(mrdivisionId, mrDivision, currentUser);
                        printMessage("update.success");
                    } catch (CRSRuntimeException e) {
                        if (e.getErrorCode() == ErrorCodes.ILLEGAL_STATE) {
                            addFieldError("duplicateIdNumberError", "Can not update Marriage Registration Division, since it have mapping records");
                        }
                    }
                }
                break;
            case 5:
                List<Court> checkCourts = courtDAO.getCourtByAnyName(court, currentUser);
                if (checkCourts.size() > 0) {
                    if (checkNameDuplicatesForEdits(courtId, pageType, checkCourts)) {
                        addFieldError("duplicateIdNumberError", "Court names are already used. Please check again");
                        logger.debug("Court names are duplicated");
                        checkDuplicate++;
                        editMode = true;
                    }
                }
                if (checkDuplicate == 0) {
                    try {
                        dataManagementService.updateCourt(courtId, court, currentUser);
                        printMessage("update.success");
                    } catch (CRSRuntimeException e) {
                        if (e.getErrorCode() == ErrorCodes.ILLEGAL_STATE) {
                            addFieldError("duplicateIdNumberError", "Can not update Court, since it have mapping records");
                        }
                    }
                }
                break;
            case 6:
                List<Location> checkLocations = locationDAO.getLocationByAnyName(location);

                if (checkLocations.size() > 0) {
                    if (checkNameDuplicatesForEdits(locationId, pageType, checkLocations)) {
                        addFieldError("duplicateIdNumberError", "Office names are already used. Please check again");
                        logger.debug("Location name is duplicated");
                        checkDuplicate++;
                        editMode = true;
                    }
                }
                if (checkDuplicate == 0) {
                    try {
                        dataManagementService.updateLocation(locationId, location, currentUser);
                        printMessage("update.success");
                    } catch (CRSRuntimeException e) {
                        if (e.getErrorCode() == ErrorCodes.ILLEGAL_STATE) {
                            addFieldError("duplicateIdNumberError", "Can not update Location, since it have mapping records");
                        }
                    }
                }
                break;
            case 7:
                List<GNDivision> checkGNDivisions =
                    gnDivisionDAO.getGNDivisionByAnyNameAndDSDivision(gnDivision, dsDivisionId, currentUser);

                if (checkGNDivisions.size() > 0) {
                    if (checkNameDuplicatesForEdits(gnDivisionId, pageType, checkGNDivisions)) {
                        addFieldError("duplicateIdNumberError", "GNDivision names are already exist. Please check again");
                        logger.debug("GNDivision names are duplicated");
                        checkDuplicate++;
                        editMode = true;
                    }
                }
                if (checkDuplicate == 0) {
                    try {
                        gnDivision.setDsDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                        dataManagementService.updateGNDivision(gnDivisionId, gnDivision, currentUser);
                        printMessage("update.success");
                    } catch (CRSRuntimeException e) {
                        if (e.getErrorCode() == ErrorCodes.ILLEGAL_STATE) {
                            addFieldError("duplicateIdNumberError", "Can not update Grama Niladhari Division, since it have mapping records");
                        }
                    }
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

    private boolean checkNameDuplicatesForEdits(int divisionUKey, int type, List divisions) {
        List duplicates = new ArrayList(1);
        Outer:
        for (Object division : divisions) {
            switch (type) {
                case 2:
                    if (((DSDivision) division).getDsDivisionUKey() != divisionUKey) {
                        duplicates.add(division);
                        break Outer;
                    }
                    break;
                case 3:
                    if (((BDDivision) division).getBdDivisionUKey() != divisionUKey) {
                        duplicates.add(division);
                        break Outer;
                    }
                    break;
                case 4:
                    if (((MRDivision) division).getMrDivisionUKey() != divisionUKey) {
                        duplicates.add(division);
                        break Outer;
                    }
                    break;
                case 5:
                    if (((Court) division).getCourtUKey() != divisionUKey) {
                        duplicates.add(division);
                        break Outer;
                    }
                    break;
                case 6:
                    if (((Location) division).getLocationUKey() != divisionUKey) {
                        duplicates.add(division);
                        break Outer;
                    }
                    break;
                case 7:
                    if (((GNDivision) division).getGnDivisionUKey() != divisionUKey) {
                        duplicates.add(division);
                        break Outer;
                    }
                    break;
            }
        }
        return duplicates.size() > 0;
    }

    public String initDivisionList() {
        populate();
        setDivisionList(true);
        return SUCCESS;
    }

    public String initRearrangeDivision() {
        populateGNReArrangeDivisions();
        return SUCCESS;
    }

    public String reArrangeDivisions() {
        logger.debug("Re-arranging GNDivisions, selected gnDivisions : {}", gnDivisions.length);
        try {
            dataManagementService.reArrangeGNDivisions(dsDivisionId, selectDSDivisionId, gnDivisions, currentUser);
            DSDivision moved = dsDivisionDAO.getDSDivisionByPK(selectDSDivisionId);
            addActionMessage("Selected Grama Niladhari Division/s moved to Divisional Secretariat Division : " +
                moved.getEnDivisionName());
        } catch (CRSRuntimeException e) {
            addActionError("Selected one or more Grama Niladhari Division(s) does not match with the minimum requirements");
        }
        gnDivisions = null;
        selectDistrictId = 0;
        selectDSDivisionId = 0;
        populateGNReArrangeDivisions();

        return SUCCESS;
    }

    private void populateGNReArrangeDivisions() {
        districtList = districtDAO.getAllDistrictNames(language, user);
        userDistrictId = userDistrictId == 0 ? districtList.keySet().iterator().next() : userDistrictId;
        dsDivisionList = dsDivisionDAO.getAllDSDivisionNames(userDistrictId, language, user);
        dsDivisionId = dsDivisionId == 0 ? dsDivisionList.keySet().iterator().next() : dsDivisionId;
        logger.debug("Loading GNDivision re-arrange with districtUKey : {} and dsDivisionUKey : {}", userDistrictId, dsDivisionId);
        gnDivisionNameList = gnDivisionDAO.getAllGNDivisionByDsDivisionKey(dsDivisionId);
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
                districtEn = districtDAO.getNameByPK(userDistrictId, AppConstants.ENGLISH);
                dsDivisionNameList = dsDivisionDAO.getAllDSDivisionByDistrictKey(userDistrictId);
                if (setNull) {
                    dsDivision = null;
                }
                break;
            case 3:
                districtEn = districtDAO.getNameByPK(userDistrictId, AppConstants.ENGLISH);
                dsDivisionEn = dsDivisionDAO.getNameByPK(dsDivisionId, AppConstants.ENGLISH);
                bdDivisionNameList = bdDivisionDAO.getAllBDDivisionByDsDivisionKey(dsDivisionId);
                if (setNull) {
                    bdDivision = null;
                }
                break;
            case 4:
                districtEn = districtDAO.getNameByPK(userDistrictId, AppConstants.ENGLISH);
                dsDivisionEn = dsDivisionDAO.getNameByPK(dsDivisionId, AppConstants.ENGLISH);
                mrDivisionNameList = mrDivisionDAO.getAllMRDivisionsByDSDivisionKey(dsDivisionId);
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
                districtEn = districtDAO.getNameByPK(userDistrictId, AppConstants.ENGLISH);
                final DSDivision dsDivision = dsDivisionDAO.getDSDivisionByPK(dsDivisionId);
                dsDivisionEn = dsDivision.getEnDivisionName();
                locationNameList = locationDAO.getAllLocationsByDSDivisionKey(dsDivisionId);

                logger.debug("Size of the loaded Location List is :{}", locationNameList.size());
                if (setNull) {
                    location = new Location();
                    String locationCode = Integer.toString(dsDivision.getDistrict().getDistrictId()) +
                        AppConstants.DASH + Integer.toString(dsDivision.getDivisionId());
                    location.setLocationCode(locationCode);
                    location.setEnLocationName(dsDivision.getEnDivisionName());
                    location.setSiLocationName(dsDivision.getSiDivisionName());
                    location.setTaLocationName(dsDivision.getTaDivisionName());

                    generateLocationSignature(dsDivision);
                    generateLocationMailingAddress(dsDivision);
                }
                break;
            case 7:
                districtEn = districtDAO.getNameByPK(userDistrictId, AppConstants.ENGLISH);
                dsDivisionEn = dsDivisionDAO.getNameByPK(dsDivisionId, AppConstants.ENGLISH);
                gnDivisionNameList = gnDivisionDAO.getAllGNDivisionByDsDivisionKey(dsDivisionId);
                if (setNull) {
                    gnDivision = null;
                }
                break;

        }
    }

    private void generateLocationSignature(DSDivision dsDivision) {
        final StringBuilder sb = new StringBuilder();
        sb.append(CommonUtil.getOfficeSignature(AppConstants.SINHALA)).append(AppConstants.SPACE).
            append(dsDivision.getSiDivisionName()).append(AppConstants.NEW_LINE).
            append(CommonUtil.getOfficeSignature(AppConstants.ENGLISH)).append(AppConstants.SPACE).
            append(dsDivision.getEnDivisionName()).append(AppConstants.FULL_STOP);
        location.setSienLocationSignature(sb.toString());
        sb.delete(0, sb.length());

        sb.append(CommonUtil.getOfficeSignature(AppConstants.TAMIL)).append(AppConstants.SPACE).
            append(dsDivision.getTaDivisionName()).append(AppConstants.NEW_LINE).
            append(CommonUtil.getOfficeSignature(AppConstants.ENGLISH)).append(AppConstants.SPACE).
            append(dsDivision.getEnDivisionName()).append(AppConstants.FULL_STOP);
        location.setTaenLocationSignature(sb.toString());
    }

    private void generateLocationMailingAddress(DSDivision dsDivision) {
        final StringBuilder sb = new StringBuilder();
        sb.append(CommonUtil.getMailingAddress(AppConstants.ENGLISH)).append(dsDivision.getEnDivisionName()).
            append(AppConstants.FULL_STOP);
        location.setEnLocationMailingAddress(sb.toString());
        sb.delete(0, sb.length());

        sb.append(CommonUtil.getMailingAddress(AppConstants.SINHALA)).append(dsDivision.getSiDivisionName()).
            append(AppConstants.FULL_STOP);
        location.setSiLocationMailingAddress(sb.toString());
        sb.delete(0, sb.length());

        sb.append(CommonUtil.getMailingAddress(AppConstants.TAMIL)).append(dsDivision.getTaDivisionName()).
            append(AppConstants.FULL_STOP);
        location.setTaLocationMailingAddress(sb.toString());
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
            case 7:
                dataManagementService.activateOrInactiveGNDivision(gnDivisionId, activate, currentUser);
                break;

        }
        setDivisionList(true);
        return SUCCESS;
    }

    public String addDivisionsAndDsDivisions() {
        int checkDuplicate = 0;
        switch (pageType) {
            case 1:
                District checkDistrict = districtDAO.getDistrictByCode(district.getDistrictId());
                if (checkDistrict != null) {
                    addFieldError("duplicateIdNumberError", "District Id Number Already Used. Please Insert Another Number");
                    logger.debug("Duplicate District code number is :", checkDistrict.getDistrictId());
                    checkDuplicate++;
                }
                if (checkDuplicate == 0) {
                    district.setActive(true);
                    dataManagementService.addDistrict(district, currentUser);
                    logger.debug("New Id of new District {} is   :{}", district.getEnDistrictName(), district.getDistrictId());
                    printMessage("new.district.add", district.getSiDistrictName(), district.getEnDistrictName(),
                        district.getTaDistrictName());
                }
                break;
            case 2:
                List<DSDivision> checkDSDivisions = dsDivisionDAO.getDSDivisionByAnyName(dsDivision);
                if (checkDSDivisions.size() > 0) {
                    addFieldError("duplicateIdNumberError", "DS Division names are already used. Please check again");
                    logger.debug("DSDivision names are duplicated");
                    checkDuplicate++;
                }
                if (checkDuplicate == 0) {
                    dsDivision.setDistrict(districtDAO.getDistrict(userDistrictId));
                    dataManagementService.addDSDivision(dsDivision, currentUser);
                    logger.debug("New Id of new Ds Division {} is   :{}", dsDivision.getEnDivisionName(), dsDivision.getDivisionId());
                    printMessage("new.dsDivision.add", dsDivision.getSiDivisionName(), dsDivision.getEnDivisionName(),
                        dsDivision.getTaDivisionName());
                }
                break;
            case 3:
                List<BDDivision> checkBDDivisions =
                    bdDivisionDAO.getBDDivisionByAnyNameAndDSDivisionKey(bdDivision, dsDivisionId);
                if (checkBDDivisions.size() > 0) {
                    addFieldError("duplicateIdNumberError", "BD Division names are already used. Please check again");
                    logger.debug("BDDivision names are duplicated");
                    checkDuplicate++;
                }
                if (checkDuplicate == 0) {
                    bdDivision.setDsDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                    dataManagementService.addBDDivision(bdDivision, currentUser);
                    logger.debug("New Id of New Division {} is   :{}", bdDivision.getEnDivisionName(), bdDivision.getDivisionId());
                    printMessage("new.bdDivision.add", bdDivision.getSiDivisionName(), bdDivision.getEnDivisionName(),
                        bdDivision.getTaDivisionName());
                }
                break;
            case 4:
                List<MRDivision> checkMrDivisions =
                    mrDivisionDAO.getMRDivisionByAnyNameAndDSDivision(mrDivision, dsDivisionId);

                if (checkMrDivisions.size() > 0) {
                    addFieldError("duplicateIdNumberError", "MR Division names are already used. Please check again");
                    logger.debug("MRDivision names are duplicated");
                    checkDuplicate++;
                }
                if (checkDuplicate == 0) {
                    mrDivision.setDsDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                    dataManagementService.addMRDivision(mrDivision, currentUser);
                    logger.debug("New Id of New MRDivision {} is   :{}", mrDivision.getEnDivisionName(), mrDivision.getDivisionId());
                    printMessage("new.mrDivision.add", mrDivision.getSiDivisionName(), mrDivision.getEnDivisionName(),
                        mrDivision.getTaDivisionName());
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
                    printMessage("new.court.add", court.getSiCourtName(), court.getEnCourtName(), court.getTaCourtName());
                }
                break;
            case 6:
                List<Location> checkLocations = locationDAO.getLocationByAnyName(location);
                if (checkLocations.size() > 0) {
                    addFieldError("duplicateIdNumberError", "Office names are already used. Please check again");
                    logger.debug("Location name is duplicated");
                    checkDuplicate++;
                }
                if (checkDuplicate == 0) {
                    location.setDsDivisionId(dsDivisionId);
                    dataManagementService.addLocation(location, currentUser);
                    logger.debug("New Id of New Location {} is  :{}", locationDAO.getLocation(locationId), locationId);
                    if (language.equals("si")) {
                        msg = location.getSiLocationName() + " " + getText("new.location.add");
                    } else if (language.equals("en")) {
                        msg = getText("new.location.add") + " : " + location.getEnLocationName();
                    } else if (language.equals("ta")) {
                        msg = getText("new.location.add") + " : " + location.getTaLocationName();
                    }
                }
                break;
            case 7:
                List<GNDivision> checkGNDivisions =
                    gnDivisionDAO.getGNDivisionByAnyNameAndDSDivision(gnDivision, dsDivisionId, currentUser);

                if (checkGNDivisions.size() > 0) {
                    addFieldError("duplicateIdNumberError", "GNDivision names are already exist. Please check again");
                    logger.debug("GNDivision names are duplicated");
                    checkDuplicate++;
                }
                if (checkDuplicate == 0) {
                    gnDivision.setDsDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                    try {
                        dataManagementService.addGNDivision(gnDivision, currentUser);
                    } catch (CRSRuntimeException e) {
                        logger.error("GN division adding error :{}", gnDivisionId);
                        addActionError(getText("new.gnDivision.add"));
                    }
                    logger.debug("New Id of New Division {} is   :{}", gnDivision.getEnGNDivisionName(), gnDivision.getGnDivisionId());
                    printMessage("new.gnDivision.add", gnDivision.getSiGNDivisionName(),
                        gnDivision.getEnGNDivisionName(), gnDivision.getTaGNDivisionName());
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

    private void printMessage(String propertyKey, String siName, String enName, String taName) {
        if (AppConstants.SINHALA.equals(language)) {
            msg = getText(propertyKey) + COLON + siName;
        } else if (AppConstants.ENGLISH.equals(language)) {
            msg = getText(propertyKey) + COLON + enName;
        } else if (AppConstants.TAMIL.equals(language)) {
            msg = getText(propertyKey) + COLON + taName;
        }
    }

    private void printMessage(String propertyKey) {
        msg = getText(propertyKey);
    }

    public String selectUsers() {
        filterUsers();
        session.put(VIEW_USERS, usersList);
        populate();
        return SUCCESS;
    }

    private void filterUsers() {
        selectedRole = roleId;
        usersList = Collections.emptyList();

        if (userDistrictId == 0 /*ALL*/ && "ALL".equals(selectedRole)/*ALL*/ && nameOfUser.length() == 0 /*No Name*/) {
            usersList = service.getAllUsers();
            selectedRole = "ALL";
        } else {
            if (userDistrictId == 0 && nameOfUser.length() == 0) {
                usersList = service.getUsersByRole(selectedRole);
            } else if (userDistrictId == 0 && selectedRole.equals("ALL")) {
                usersList = service.getUserByIdOrName(nameOfUser);
                selectedRole = nameOfUser;
            } else if (nameOfUser.length() == 0 && selectedRole.equals("ALL")) {
                District district = districtDAO.getDistrict(userDistrictId);
                usersList = service.getUsersByAssignedBDDistrict(district);
                selectedRole = district.getEnDistrictName();
            } else if (!selectedRole.equals("ALL") && userDistrictId != 0 && nameOfUser.length() == 0) {
                District district = districtDAO.getDistrict(userDistrictId);
                usersList = service.getUsersByRoleAndAssignedBDDistrict(roleDAO.getRole(selectedRole), district);
                selectedRole = selectedRole + " AND " + district.getEnDistrictName();
            } else if (!selectedRole.equals("ALL") && nameOfUser.length() != 0) {
                List<User> tempRole = service.getUsersByRole(selectedRole);
                List<User> tempName = service.getUsersByIDMatch(nameOfUser);
                usersList = new ArrayList<User>();
                for (User userN : tempName) {
                    for (User userR : tempRole) {
                        if (userN.getUserId().equals(userR.getUserId())) {
                            usersList.add(userR);
                        }
                    }
                }
                selectedRole = selectedRole + " AND " + nameOfUser;
            }
        }
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
                logger.debug("All Records Re-indexed Successfully");
        }

        return SUCCESS;
    }

    private void populateLocationListOnly(User user) {
        locationList = new HashMap<Integer, String>();
        Set<DSDivision> st = user.getAssignedBDDSDivisions();

        for (DSDivision ds : st) {
            locationList.putAll(locationDAO.getLocationByDSDivisionID(ds.getDsDivisionUKey(), user.getPrefLanguage()));
        }
        if (userLocationDAO.getActiveUserLocations(userId, true).size() > 0) {
            user.setStatus(User.State.ACTIVE);
            user.getLifeCycleInfo().setActive(true);
            service.updateUser(user);
            logger.debug("User Activated {}", user.getUserName());
        } else {
            user.setStatus(User.State.INACTIVE);
            user.getLifeCycleInfo().setActive(false);
            service.updateUser(user);
            logger.debug("User Deactivated {}", user.getUserName());
        }
        logger.debug("List : {}", locationList.size());

        if (user.getRole().getRoleId().equals(Role.ROLE_RG) || user.getRole().getRoleId().equals(Role.ROLE_ADMIN)) {
            locationList.put(1, locationDAO.getLocationNameByPK(1, user.getPrefLanguage()));
        }
        roleId = user.getRole().getName();
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
                if (r.equals(roleId)) {
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

    public GNDivision getGnDivision() {
        return gnDivision;
    }

    public void setGnDivision(GNDivision gnDivision) {
        this.gnDivision = gnDivision;
    }

    public int getGnDivisionId() {
        return gnDivisionId;
    }

    public void setGnDivisionId(int gnDivisionId) {
        this.gnDivisionId = gnDivisionId;
    }

    public List<GNDivision> getGnDivisionNameList() {
        return gnDivisionNameList;
    }

    public void setGnDivisionNameList(List<GNDivision> gnDivisionNameList) {
        this.gnDivisionNameList = gnDivisionNameList;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public int getSelectDistrictId() {
        return selectDistrictId;
    }

    public void setSelectDistrictId(int selectDistrictId) {
        this.selectDistrictId = selectDistrictId;
    }

    public int getSelectDSDivisionId() {
        return selectDSDivisionId;
    }

    public void setSelectDSDivisionId(int selectDSDivisionId) {
        this.selectDSDivisionId = selectDSDivisionId;
    }

    public int[] getGnDivisions() {
        return gnDivisions;
    }

    public void setGnDivisions(int[] gnDivisions) {
        this.gnDivisions = gnDivisions;
    }
}
