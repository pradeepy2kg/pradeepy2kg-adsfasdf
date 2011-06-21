package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.domain.BaseLifeCycleInfo;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.WebUtils;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.Assignment;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.Registrar;
import lk.rgd.crs.api.service.RegistrarManagementService;
import lk.rgd.crs.web.WebConstants;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.spec.EllipticCurve;
import java.util.*;

/**
 * action class for managing registrars    and their assignments
 *
 * @author amith jayasekara
 */
public class RegistrarManagementAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(RegistrarManagementAction.class);
    private Map session;
    private User user;

    private Map<Integer, String> districtList;
    private Map<Integer, String> divisionList;
    private Map<Integer, String> dsDivisionList;

    private List<Registrar> registrarList;
    private List<Assignment> assignmentList;

    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final MRDivisionDAO mrDivisionDAO;
    private final RegistrarManagementService service;

    private Registrar registrar;
    private Assignment assignment;
    private Assignment.Type type;
    private int[] mrgType;

    private boolean state;

    private int directAssignment;
    private int assignmentState;
    private int dsDivisionId;
    private int districtId;
    private int assignmentType;
    private int page;
    private long registrarPin;
    private int divisionId;//cannot determine witch type of division to set in struts layer

    private long registrarUkey;
    private long assignmentUKey;

    /*support variable*/
    private boolean onloadPage;
    private boolean editableAssignment;
    private boolean editMode;
    private boolean registrarSession;
    private boolean active;
    private boolean indirect;

    private Date appoinmentDate;
    private Date permanentDate;
    private Date terminationDate;

    private String language;
    private String districtName;
    private String dsDivisionName;
    private String divisionName;
    private String registrarName;//use to search by name or part of the name

    public RegistrarManagementAction(DistrictDAO districtDAO, BDDivisionDAO bdDivisionDAO, DSDivisionDAO dsDivisionDAO, RegistrarManagementService service, MRDivisionDAO mrDivisionDAO) {
        this.districtDAO = districtDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.service = service;
        this.mrDivisionDAO = mrDivisionDAO;
    }

    public String registrarsManagementHome() {
        /* lists are generated according to the user */
        session.remove(WebConstants.SESSION_EXSISTING_REGISTRAR);
        districtId = 1;
        dsDivisionId = 0;

        districtList = districtDAO.getDistrictNames(language, user);
        if (districtList != null) {
            districtId = districtList.keySet().iterator().next();
        }
        dsDivisionList = dsDivisionDAO.getAllDSDivisionNames(districtId, language, user);
        if (dsDivisionList != null) {
            dsDivisionId = districtList.keySet().iterator().next();
        }
        state = true;
        type = Assignment.Type.BIRTH;
        //filter(); /* populating search results */

        return SUCCESS;
    }

    public String registrarsViewInit() {

        logger.debug("registrar view init called - UserId : {}", user.getUserId());

        if (registrarSession) {
            Assignment asg = (Assignment) session.get(WebConstants.SESSION_UPDATED_ASSIGNMENT_REGISTRAR);
            registrarUkey = asg.getRegistrar().getRegistrarUKey();
            session.remove(WebConstants.SESSION_UPDATED_ASSIGNMENT_REGISTRAR);
        }

        if (registrarUkey > 0) {
            registrar = service.getRegistrarById(registrarUkey);
            assignmentList = service.getAssignments(registrarUkey, user);
        }
        Registrar existing = (Registrar) session.get(WebConstants.SESSION_EXSISTING_REGISTRAR);
        logger.debug("existing  : {}", existing);
        if (existing != null && existing.getRegistrarUKey() > 0) {
            registrar = existing;
            assignmentList = service.getAssignments(registrar.getRegistrarUKey(), user);
        }
        /*  assignmentList = service.getAllAssignments(user);*/
        //use for edit this registrar
        session.put(WebConstants.SESSION_EXSISTING_REGISTRAR, registrar);
        return SUCCESS;
    }

    public String filter() {
        logger.debug("filter called");
        //filter by all districts
        if (districtId == 0) {
            assignmentList = service.getAllActiveAssignment(true, user);
        } else {
            //selecting all division
            if (dsDivisionId == 0) {
                if (type != null) {
                    assignmentList = service.getAssignmentsByDistrictId(districtId, type, state, user);

                } else {
                    assignmentList = service.getAssignmentsByDistrictId(districtId, state, user);
                }
            } else {
                if (type != null) {
                    assignmentList = service.getAssignmentsByDSDivision(dsDivisionId, type, state, user);
                } else {
                    assignmentList = service.getAssignmentsByDSDivision(dsDivisionId, state, user);
                }
            }
        }
        districtList = districtDAO.getDistrictNames(language, user);
        dsDivisionList = dsDivisionDAO.getAllDSDivisionNames(districtId, language, user);

        return SUCCESS;
    }

    public String registrarsAdd() {
        logger.debug("attempt to add new registrar");
        //clearing session
        session.remove(WebConstants.SESSION_EXSISTING_REGISTRAR);
        if (page > 0) {
            //check is there a registrar for that pin already exists
            Registrar existingRegistrar = service.getRegistrarByPin(registrar.getPin(), user);
            if (existingRegistrar != null) {
                addActionError(getText("error.registrar.already.exists"));
                return ERROR;
            } else {
                try {
                    service.addRegistrar(registrar, user);
                    session.put(WebConstants.SESSION_EXSISTING_REGISTRAR, registrar);
                } catch (Exception e) {
                    addActionError("error.registrar.add");
                    return ERROR;
                }
            }
        } else {
            return "pageLoad";
        }
        return SUCCESS;
    }

    public String assignmentAdd() {
        if (!editMode) {
            if (directAssignment == 2) {
                //todo addAssignement fails
                //getting exiting
                Registrar reg = (Registrar) session.get(WebConstants.SESSION_EXSISTING_REGISTRAR);
                if (registrarPin > 0) {
                    reg = service.getRegistrarByPin(registrarPin, user);
                }

                assignment.setRegistrar(reg);
                //setting correct division type
                if (type.equals(Assignment.Type.BIRTH)) {
                    assignment.setBirthDivision(bdDivisionDAO.getBDDivisionByPK(divisionId));
                }
                if (type.equals(Assignment.Type.DEATH)) {
                    assignment.setDeathDivision(bdDivisionDAO.getBDDivisionByPK(divisionId));
                }
                if (type.equals(Assignment.Type.GENERAL_MARRIAGE) || (type.equals(Assignment.Type.KANDYAN_MARRIAGE)) ||
                    (type.equals(Assignment.Type.MUSLIM_MARRIAGE))) {
                    assignment.setMarriageDivision(mrDivisionDAO.getMRDivisionByPK(divisionId));
                }
                if (mrgType != null) {
                    if (mrgType.length > 0) {
                        int i = 0;
                        while (i < mrgType.length) {
                            if (mrgType[i] == Assignment.Type.GENERAL_MARRIAGE.ordinal()) {
                                assignment.setType(Assignment.Type.GENERAL_MARRIAGE);
                                service.addAssignment(assignment, user);
                            } else if (mrgType[i] == Assignment.Type.KANDYAN_MARRIAGE.ordinal()) {
                                assignment.setType(Assignment.Type.KANDYAN_MARRIAGE);
                                service.addAssignment(assignment, user);
                            } else if (mrgType[i] == Assignment.Type.MUSLIM_MARRIAGE.ordinal()) {
                                assignment.setType(Assignment.Type.MUSLIM_MARRIAGE);
                                service.addAssignment(assignment, user);
                            }
                            assignment.setAssignmentUKey(0);
                            i++;
                        }
                        assignment = null;
                        addActionMessage("assignment.saved.successfully");
                    }
                } else {
                    assignment.setType(type);
                    service.addAssignment(assignment, user);
                    assignment = null;
                    addActionMessage("assignment.saved.successfully");
                }
            }
        } else {
            logger.debug("attempt to edit a assignment with assignmentUKey : {}", assignmentUKey);
            Assignment beforeEdit = (Assignment) session.get(WebConstants.SESSION_UPDATED_ASSIGNMENT_REGISTRAR);
            beforeEdit.setAppointmentDate(appoinmentDate);
            beforeEdit.setPermanentDate(permanentDate);
            beforeEdit.setTerminationDate(terminationDate);
            BaseLifeCycleInfo life = beforeEdit.getLifeCycleInfo();
            life.setActive(state);
            beforeEdit.setLifeCycleInfo(life);
            service.updateAssignment(beforeEdit, user);
            //todo load base on witch type of going to change
            populateLists(1, 1, 1);
            return "updated";
        }
        populateLists(1, 1, 1);
        return SUCCESS;
    }

    public String editAssignment() {
        //put current assignment in to session for redirection purposes
        assignment = service.getAssignmentById(assignmentUKey, user);
        session.put(WebConstants.SESSION_UPDATED_ASSIGNMENT_REGISTRAR, assignment);
        BDDivision birthDivision = assignment.getBirthDivision();
        BDDivision deathDivision = assignment.getDeathDivision();
        MRDivision mrdivision = assignment.getMarriageDivision();
        if (birthDivision != null) {
            divisionId = birthDivision.getBdDivisionUKey();
            dsDivisionId = birthDivision.getDsDivision().getDsDivisionUKey();
            districtId = birthDivision.getDistrict().getDistrictUKey();
            divisionName = bdDivisionDAO.getNameByPK(divisionId, language);
        }
        if (deathDivision != null) {
            divisionId = deathDivision.getBdDivisionUKey();
            dsDivisionId = deathDivision.getDsDivision().getDsDivisionUKey();
            districtId = deathDivision.getDistrict().getDistrictUKey();
            divisionName = bdDivisionDAO.getNameByPK(divisionId, language);
        }
        if (mrdivision != null) {
            divisionId = mrdivision.getMrDivisionUKey();
            dsDivisionId = mrdivision.getDsDivision().getDsDivisionUKey();
            districtId = mrdivision.getDistrict().getDistrictUKey();
            divisionName = mrDivisionDAO.getNameByPK(divisionId, language);
        }
        districtName = districtDAO.getNameByPK(districtId, language);
        dsDivisionName = dsDivisionDAO.getNameByPK(dsDivisionId, language);
        return SUCCESS;
    }

    public String deleteAssignment() {
        try {
            service.deleteAssignment(assignmentUKey, user);
        } catch (CRSRuntimeException e) {
            if (e.getErrorCode() == ErrorCodes.INVALID_STATE_FOR_REMOVAL) {
                addActionError("Selected assignment can not be deleted since it have mapping registrations");
            }
        }
        return SUCCESS;
    }

    public String updateRegistrar() {
        Registrar existing = (Registrar) session.get(WebConstants.SESSION_EXSISTING_REGISTRAR);

        logger.debug("attempting to update registrar : {}", registrar.getFullNameInEnglishLanguage());
        //setting previous life cycle info
        registrar.setLifeCycleInfo(existing.getLifeCycleInfo());
        //setting current assignment
        registrar.setAssignments(existing.getAssignments());
        //setting uK
        registrar.setRegistrarUKey(existing.getRegistrarUKey());

        service.updateRegistrar(registrar, user);
        session.put(WebConstants.SESSION_EXSISTING_REGISTRAR, registrar);
        return SUCCESS;
    }


    public String assignmentAddPageLoad() {
        session.remove(WebConstants.SESSION_EXSISTING_REGISTRAR);
        //requesting addAssignment page directly
        if (registrarPin > 0) {
            session.put(WebConstants.SESSION_EXSISTING_REGISTRAR, service.getRegistrarByPin(registrarPin, user));
        }
        populateLists(1, 1, assignmentType);
        directAssignment = 1;
        return SUCCESS;
    }

    public String findRegistrar() {
        removeExistingRegistrars();

        if (page > 0) {
            logger.debug("attempt to search a registrar");
            if (registrarPin > 0) {
                //search by registrar pin number
                Registrar existingRegistrar = service.getRegistrarByPin(registrarPin, user);
                registrarList = new ArrayList<Registrar>();
                if (existingRegistrar != null) {
                    registrarList.add(existingRegistrar);
                }
            } else if (registrarName != null) {
                //search by name or part of the name
                registrarList = service.getRegistrarByNameOrPartOfTheName(registrarName, user);
            }
            if (registrarList != null && registrarList.size() == 0) {
                addActionMessage(getText("no.registrars.found"));
            }
            return SUCCESS;
        }
        logger.debug("attempt to load find registrar home page");
        return "pageLoad";
    }

    private void removeExistingRegistrars() {
        Object o = session.get(WebConstants.SESSION_EXSISTING_REGISTRAR);
        if (o != null) {
            session.remove(WebConstants.SESSION_EXSISTING_REGISTRAR);
        }
    }

    //loads basic lists for separate types

    private void populateLists(int districtId, int dsDivisionId, int assignmentType) {
        if (user.getRole().getRoleId().equals(Role.ROLE_ADMIN)) {
            districtList = districtDAO.getAllDistrictNames(language, user);
            dsDivisionList = dsDivisionDAO.getAllDSDivisionNames(districtId, language, user);
        } else {
            districtList = districtDAO.getDistrictNames(language, user);
            dsDivisionList = dsDivisionDAO.getDSDivisionNames(districtId, language, user);

        }
        switch (assignmentType) {
            //requesting death /birth division list
            case 1:
                if (dsDivisionList != null && dsDivisionList.size() > 0) {
                    divisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionList.keySet().iterator().next(), language, user);
                }
                break;
            //requesting marriage division list
            case 2:
                if (dsDivisionList != null && dsDivisionList.size() > 0) {
                    divisionList = mrDivisionDAO.getMRDivisionNames(dsDivisionList.keySet().iterator().next(), language, user);
                }
        }
    }

    public void setSession(Map map) {
        logger.debug("Set session {}", map);
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("setting User: {} and UserLanguage : {}", user.getUserName(), language);
    }

    public Map getSession() {
        return session;
    }

    public Map<Integer, String> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(Map<Integer, String> divisionList) {
        this.divisionList = divisionList;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Assignment.Type getType() {
        return type;
    }

    public void setType(Assignment.Type type) {
        this.type = type;
    }

    public int getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(int assignmentType) {
        this.assignmentType = assignmentType;
        switch (assignmentType) {
            case -1:
                this.type = null;
                break;
            case 0:
                this.type = Assignment.Type.BIRTH;
                break;
            case 1:
                this.type = Assignment.Type.DEATH;
                break;
            case 2:
                this.type = Assignment.Type.GENERAL_MARRIAGE;
                break;
            case 3:
                this.type = Assignment.Type.KANDYAN_MARRIAGE;
                break;
            case 4:
                this.type = Assignment.Type.MUSLIM_MARRIAGE;
                break;
        }
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getAssignmentState() {
        return assignmentState;
    }

    public void setAssignmentState(int assignmentState) {
        this.assignmentState = assignmentState;
        switch (assignmentState) {
            case 0:
                this.state = true;
                break;
            case 1:
                this.state = false;
                break;
            case 2:
                this.state = true;
                break;
        }
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public boolean isOnloadPage() {
        return onloadPage;
    }

    public void setOnloadPage(boolean onloadPage) {
        this.onloadPage = onloadPage;
    }

    public List<Registrar> getRegistrarList() {
        return registrarList;
    }

    public void setRegistrarList(List<Registrar> registrarList) {
        this.registrarList = registrarList;
    }

    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public Registrar getRegistrar() {
        return registrar;
    }

    public void setRegistrar(Registrar registrar) {
        this.registrar = registrar;
    }

    public long getRegistrarUkey() {
        return registrarUkey;
    }

    public void setRegistrarUkey(long registrarUkey) {
        this.registrarUkey = registrarUkey;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getDirectAssignment() {
        return directAssignment;
    }

    public void setDirectAssignment(int directAssignment) {
        this.directAssignment = directAssignment;
    }

    public long getRegistrarPin() {
        return registrarPin;
    }

    public void setRegistrarPin(long registrarPin) {
        this.registrarPin = registrarPin;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public long getAssignmentUKey() {
        return assignmentUKey;
    }

    public void setAssignmentUKey(long assignmentUKey) {
        this.assignmentUKey = assignmentUKey;
    }

    public boolean isEditableAssignment() {
        return editableAssignment;
    }

    public void setEditableAssignment(boolean editableAssignment) {
        this.editableAssignment = editableAssignment;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isRegistrarSession() {
        return registrarSession;
    }

    public void setRegistrarSession(boolean registrarSession) {
        this.registrarSession = registrarSession;
    }

    public Date getAppoinmentDate() {
        return appoinmentDate;
    }

    public void setAppoinmentDate(Date appoinmentDate) {
        this.appoinmentDate = appoinmentDate;
    }

    public Date getPermanentDate() {
        return permanentDate;
    }

    public void setPermanentDate(Date permanentDate) {
        this.permanentDate = permanentDate;
    }

    public Date getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isIndirect() {
        return indirect;
    }

    public void setIndirect(boolean indirect) {
        this.indirect = indirect;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDsDivisionName() {
        return dsDivisionName;
    }

    public void setDsDivisionName(String dsDivisionName) {
        this.dsDivisionName = dsDivisionName;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getRegistrarName() {
        return registrarName;
    }

    public void setRegistrarName(String registrarName) {
        this.registrarName = WebUtils.filterBlanks(registrarName);
    }

    public int[] getMrgType() {
        return mrgType;
    }

    public void setMrgType(int[] mrgType) {
        this.mrgType = mrgType;
        this.type = Assignment.Type.GENERAL_MARRIAGE;
    }
}
