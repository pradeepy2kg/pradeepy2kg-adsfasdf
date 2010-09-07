package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.Assignment;
import lk.rgd.crs.api.domain.Registrar;
import lk.rgd.crs.api.service.RegistrarManagementService;
import lk.rgd.crs.web.WebConstants;

/**
 * action class for managing registras    and their assignments
 *
 * @author amith jayasekara
 */
public class RegistrarsManagmentAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(RegistrarsManagmentAction.class);
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
    private final RegistrarManagementService service;

    private Registrar registrar;
    private Assignment assignment;

    private Assignment.Type type;
    private boolean state;
    private int directAssigment;

    private int assignmentState;
    private int dsDivisionId;
    private int districtId;
    private int assignmentType;
    private int page;
    private int registrarPin;
    private int divisionId;//cannot determine wich type of diviosn to set in struts layer

    private long registrarUkey;

    /*support variable*/
    private boolean onloadPage;

    public RegistrarsManagmentAction(DistrictDAO districtDAO, BDDivisionDAO bdDivisionDAO, DSDivisionDAO dsDivisionDAO, RegistrarManagementService service) {
        this.districtDAO = districtDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.service = service;
    }

    public String registrarsManagmentHome() {
        //todo no get all registrars method yet so setting here
        this.state = true;
        this.type = Assignment.Type.BIRTH;
        this.districtId = 1;
        this.dsDivisionId = 1;
        filter();
        return SUCCESS;
    }

    public String registrarsVeiwInit() {
        logger.info("register veiw init called ");
        //todo remove this
        this.districtId = 1;
        this.dsDivisionId = 1;
        populateLists(districtId, dsDivisionId);
/*        Registrar registrar = service.getRegistrarById(registrarUkey);*/
        assignmentList = service.getAllAssignments(user);
        return SUCCESS;
    }

    public String filter() {
        logger.info("filter called");
        //todo this service method not yet implemented  and find assignment also
        assignmentList = service.getAllAssignments(user);
        populateLists(districtId, dsDivisionId);
        return SUCCESS;
    }

    public String registrarsAdd() {
        logger.info("attemp to add new registrar");
        //todo add action errors here
        if (page > 0) {
            //check is there a registrar for that pin already exsists
            List<Registrar> exsistingregistrarsList = service.getRegistrarByPin(registrar.getPin(), user);
            if (exsistingregistrarsList.size() > 1) { //there is a lready one before
                addActionError(getText("error.registrar.already.exsists"));
            } else {
                service.addRegistrar(registrar, user);
                registrar = null;
                //otherwise it still in requestscope so data in that object populate in new registrar add  
            }
        }
        return SUCCESS;
    }

    public String assignmentAdd() {
        //todo divisions are nt setting properly
        logger.info("attemt to add a new assignment");
        if (directAssigment == 2) {
            //gettting exsiting
            List<Registrar> reg = (List) session.get(WebConstants.SESSION_EXSISTING_REGISTRAR);
            assignment.setRegistrar((Registrar) reg.get(0));
            //setting correct divisiontype
            if (type.equals(Assignment.Type.BIRTH))
                assignment.setBirthDivision(bdDivisionDAO.getBDDivisionByPK(divisionId));
            if (type.equals(Assignment.Type.DEATH))
                assignment.setDeathDivision(bdDivisionDAO.getBDDivisionByPK(divisionId));
            //todo marrige division
            /* if (type.equals(Assignment.Type.MARRIAGE))
            assignment.setMarriageDivision(bdDivisionDAO.getBDDivisionByPK(divisionId));*/
            assignment.setType(type);
            service.addAssignment(assignment, user);
            session.remove(WebConstants.SESSION_EXSISTING_REGISTRAR);
        }
        //todo remove this populate
        populateLists(1, 1);
        return SUCCESS;
    }

    public String assignmentAddPageLoad() {
        //requesting addAssignment page directly
        directAssigment = 1;
        //todo remove this populate
        populateLists(1, 1);
        return SUCCESS;
    }

    public String searchRegistrarByPin() {
        logger.info("searching registrar by pin : {} ", registrarPin);
        List<Registrar> exsistedReg = service.getRegistrarByPin(registrarPin, user);
        if (exsistedReg.size() != 1) {
            addFieldError("noRegistrar", "no registrar found add propertie file");
        } else {
            //adding found registrar to session
            session.put(WebConstants.SESSION_EXSISTING_REGISTRAR, exsistedReg);
        }
        //todo remove this populate
        populateLists(1, 1);
        directAssigment = 1;
        return SUCCESS;
    }

    private void populateLists(int distirictId, int dsDivisionId) {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        districtList = districtDAO.getAllDistrictNames(language, user);
        dsDivisionList = dsDivisionDAO.getAllDSDivisionNames(distirictId, language, user);
    }

    public void setSession(Map map) {
        logger.debug("Set session {}", map);
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("setting User: {}", user.getUserName());
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
            case 0:
                this.type = Assignment.Type.BIRTH;
                break;
            case 1:
                this.type = Assignment.Type.DEATH;
                break;
            case 2:
                this.type = Assignment.Type.MARRIAGE;
                break;
            case 3:
                this.type = Assignment.Type.BIRTH;
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

    public int getDirectAssigment() {
        return directAssigment;
    }

    public void setDirectAssigment(int directAssigment) {
        this.directAssigment = directAssigment;
    }

    public int getRegistrarPin() {
        return registrarPin;
    }

    public void setRegistrarPin(int registrarPin) {
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
}
