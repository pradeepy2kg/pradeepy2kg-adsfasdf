package lk.rgd.crs.web.action;


import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.domain.AppParameter;
import org.apache.struts2.interceptor.SessionAware;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Locale;
import java.util.List;

import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.RGDRuntimeException;

import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.bean.UserWarning;

import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.DateState;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.Permission;
import lk.rgd.prs.api.service.PopulationRegistry;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.core.service.PopulationRegistryImpl;
import lk.rgd.prs.core.dao.PersonDAOImpl;

/**
 * EntryAction is a struts action class  responsible for  data capture for a birth declaration and the persistance of the same.
 * Data capture forms (4) will be kept in session until persistance at the end of 4th page.
 */
public class AjaxAction extends ActionSupport implements SessionAware {
    private static final Logger logger = LoggerFactory.getLogger(AjaxAction.class);

    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final PopulationRegistry registryService;

    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> allDistrictList;
    private Map<Integer, String> allDSDivisionList;

    private Map session;

    private ParentInfo parent;
    private InformantInfo informant;
    private NotifyingAuthorityInfo notifyingAuthority;
    private User user;

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    private int birthDistrictId;
    private int birthDivisionId;
    private int dsDivisionId;
    private int motherDistrictId;
    private int motherDSDivisionId;

    public AjaxAction(PopulationRegistry service, DistrictDAO districtDAO, BDDivisionDAO bdDivisionDAO,
                      DSDivisionDAO dsDivisionDAO) {
        this.bdDivisionDAO = bdDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.registryService = service;
    }

    private void dsDivList() {
        populateDSDivList();
        logger.debug("DS division list set from Ajax : {} {}", birthDistrictId, dsDivisionId);
    }

    private void populateDSDivList() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        this.getDSDivisionNames(language, birthDistrictId);

        Object o = session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        if (o != null) {
            try {
                BirthDeclaration bdf = (BirthDeclaration) o;
                BirthRegisterInfo register = bdf.getRegister();

                dsDivisionId = register.getDsDivision().getDsDivisionUKey();
                logger.debug(" DS division found from session {}", dsDivisionId);
                return;
            } catch (Exception e) {
                logger.debug(" Problem with DS division in session. ignoring.. {} ", e.getMessage());
            }
        }

        if (!dsDivisionList.isEmpty()) {
            dsDivisionId = dsDivisionList.keySet().iterator().next();
            logger.debug("first allowed DS Div in the list {} was set", dsDivisionId);
            bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        }
    }

    private void getDSDivisionNames(String language, int BDId) {
        this.dsDivisionList = dsDivisionDAO.getDSDivisionNames(BDId, language, user);
        logger.debug("DS Divisions are selected for the birth district: {} ", BDId);
        if (!dsDivisionList.isEmpty()) {
            dsDivisionId = dsDivisionList.keySet().iterator().next();
            logger.debug("And set DS division: {} as the first element of the list.", dsDivisionId);
        }
    }

    private void populateBDDivList() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);

        Object o = session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        if (o != null) {
            try {
                BirthDeclaration bdf = (BirthDeclaration) o;
                BirthRegisterInfo register = bdf.getRegister();

                birthDivisionId = register.getBirthDivision().getBdDivisionUKey();
                logger.debug(" BD division found from session {}", birthDivisionId);
                return;
            } catch (Exception e) {
                logger.debug(" Problem with BD division in session. ignoring.. {} ", e.getMessage());
            }
        }

        if (!bdDivisionList.isEmpty()) {
            birthDivisionId = bdDivisionList.keySet().iterator().next();
            logger.debug("first allowed BD Div in the list {} was set", birthDivisionId);
        }
    }

    public String loadBDDivList() {
        populateBDDivList();
        logger.debug("BD division list set from Ajax : {} {}", dsDivisionId, birthDivisionId);
        return "BDDivList";
    }

    public String loadDSDivList() {
        dsDivList();
        return "DSDivList";
    }

    public String loadDSDivListSearch() {
        dsDivList();
        return "DSDivListSearch";
    }

    public String loadDSDivListBDFApproval() {
        dsDivList();
        return "DSDivListBDFApproval";
    }

    public String loadDSDivListBCPrint() {
        dsDivList();
        return "BirthCertificatePrint";
    }

    public String loadDSDivListBDFConfirmationPrint() {
        dsDivList();
        return "DSDivListBDFConfirmationPrint";
    }

    public String loadDSDivListBDFConfirmation() {
        dsDivList();
        return "DSDivListBDFConfirmation";
    }

    public String loadDSDivListUserPreferences() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        this.getDSDivisionNames(language, birthDistrictId);
        logger.debug("DS division list set from Ajax : {} {}", birthDistrictId, dsDivisionId);
        return "DSDivListUserPreference";
    }

    public String loadMotherDSDivList(){
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        this.getDSDivisionNames(language, motherDistrictId);
        if (!dsDivisionList.isEmpty())
            motherDSDivisionId = dsDivisionList.keySet().iterator().next();
        logger.debug("Mother DS division list set from Ajax : {} {}", birthDistrictId, dsDivisionId);
        return "MotherDSDivList";
    }

    public String loadFatherInfo() {
        String pin = parent.getFatherNICorPIN();
        if (!"".equals(pin)) {
            logger.debug("Father NIC/PIN received : {}", pin);
            try {
                Person father = registryService.findPersonByPINorNIC(pin, user);
                parent.setFatherFullName(father.getFullNameInOfficialLanguage());
                parent.setFatherDOB(father.getDateOfBirth());
                logger.debug("Father info set from Ajax : {} {}", pin, parent.getFatherFullName());
            } catch (Exception e) {
                logger.debug("No match from Ajax for Father PIN/NIC : {}", pin);
            }
        }
        return "FatherInfo";
    }

    public String loadMotherInfo() {
        String pin = parent.getMotherNICorPIN();
        if (!"".equals(pin)) {
            logger.debug("Mother NIC/PIN received : {}", pin);
            try {
                Person mother = registryService.findPersonByPINorNIC(pin, user);
                parent.setMotherFullName(mother.getFullNameInOfficialLanguage());
                parent.setMotherDOB(mother.getDateOfBirth());
                logger.debug("Mother info set from Ajax : {} {}", pin, parent.getMotherFullName());
            } catch (Exception e) {
                logger.debug("No match from Ajax for Mother PIN/NIC : {}", pin);
            }
        }
        return "MotherInfo";
    }

    public String loadNotifyerInfo() {
        String pin = notifyingAuthority.getNotifyingAuthorityPIN();
        if (!"".equals(pin)) {
            logger.debug("Notifyer NIC/PIN received : {}", pin);
            try {
                Person notifyer = registryService.findPersonByPINorNIC(pin, user);
                notifyingAuthority.setNotifyingAuthorityName(notifyer.getFullNameInOfficialLanguage());
                logger.debug("Notifyer info set from Ajax : {} {}", pin, notifyer.getFullNameInOfficialLanguage());
            } catch (Exception e) {
                logger.debug("No match from Ajax for Notifyer PIN/NIC : {}", pin);
            }
        }
        return "NotifyerInfo";
    }

    public void setSession(Map map) {
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("setting User: {}", user.getUserName());
    }

    public Map getSession() {
        return session;
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public Map<Integer, String> getBdDivisionList() {
        return bdDivisionList;
    }

    public ParentInfo getParent() {
        return parent;
    }

    public void setParent(ParentInfo parent) {
        this.parent = parent;
    }

    public NotifyingAuthorityInfo getNotifyingAuthority() {
        return notifyingAuthority;
    }

    public void setNotifyingAuthority(NotifyingAuthorityInfo notifyingAuthority) {
        this.notifyingAuthority = notifyingAuthority;
    }

    public int getBirthDistrictId() {
        return birthDistrictId;
    }

    public void setBirthDistrictId(int birthDistrictId) {
        this.birthDistrictId = birthDistrictId;
    }

    public int getBirthDivisionId() {
        return birthDivisionId;
    }

    public void setBirthDivisionId(int birthDivisionId) {
        this.birthDivisionId = birthDivisionId;
        logger.debug("setting BD Division: {}", birthDivisionId);
    }

    public InformantInfo getInformant() {
        return informant;
    }

    public void setInformant(InformantInfo informant) {
        this.informant = informant;
    }

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
        logger.debug("setting DS Division: {}", dsDivisionId);
    }

    public Map<Integer, String> getAllDistrictList() {
        return allDistrictList;
    }

    public void setAllDistrictList(Map<Integer, String> allDistrictList) {
        this.allDistrictList = allDistrictList;
    }

    public Map<Integer, String> getAllDSDivisionList() {
        return allDSDivisionList;
    }

    public void setAllDSDivisionList(Map<Integer, String> allDSDivisionList) {
        this.allDSDivisionList = allDSDivisionList;
    }

    public void setMotherDistrictId(int motherDistrictId) {
        this.motherDistrictId = motherDistrictId;
    }

    public int getMotherDistrictId() {
        return motherDistrictId;
    }

    public void setMotherDSDivisionId(int motherDSDivisionId) {
        this.motherDSDivisionId = motherDSDivisionId;
    }

    public int getMotherDSDivisionId() {
        return motherDSDivisionId;
    }       
}