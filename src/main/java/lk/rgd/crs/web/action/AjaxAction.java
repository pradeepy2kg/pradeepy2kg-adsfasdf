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

    private final DistrictDAO districtDAO;
    private final CountryDAO countryDAO;
    private final RaceDAO raceDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final PopulationRegistry registryService;

    private Map<Integer, String> districtList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> allDistrictList;
    private Map<Integer, String> allDSDivisionList;

    private Map session;

    private ParentInfo parent;
    private GrandFatherInfo grandFather;
    private MarriageInfo marriage;
    private InformantInfo informant;
    private NotifyingAuthorityInfo notifyingAuthority;
    private ConfirmantInfo confirmant;
    private BirthRegisterInfo register;
    private User user;

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    private int birthDistrictId;
    private int birthDivisionId;
    private int fatherCountry;
    private int motherCountry;
    private int fatherRace;
    private int motherRace;
    private int dsDivisionId;
    private int motherDistrictId;
    private int motherDSDivisionId;

    public AjaxAction(PopulationRegistry service, DistrictDAO districtDAO, CountryDAO countryDAO, RaceDAO raceDAO,
                      BDDivisionDAO bdDivisionDAO, DSDivisionDAO dsDivisionDAO) {
        this.districtDAO = districtDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.registryService = service;
    }

    private void dsDivList() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        populateBasicLists(language);
        populateDynamicLists(language);
        logger.debug("DS division list set from Ajax : {} {}", birthDistrictId, dsDivisionId);
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
    
    public String loadDSDivListBDFConfirmation() {
        dsDivList();
        return "DSDivListBDFConfirmation";
    }

    public String loadBDDivList() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        if (dsDivisionId != 0) { // in case UI does not return a ID, (at page load) use the existing list
            bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        } else {
            populateBasicLists(language);
            populateDynamicLists(language);
        }
        birthDivisionId = bdDivisionList.keySet().iterator().next();
        logger.debug("BD division list set from Ajax : {} {}", dsDivisionId, birthDivisionId);
        return "BDDivList";
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

    private void populateDynamicLists(String language) {
//        if (birthDistrictId == 0) {
//            if (!districtList.isEmpty()) {
//                birthDistrictId = districtList.keySet().iterator().next();
//                logger.debug("first allowed district in the list {} was set", birthDistrictId);
//            }
//        }
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, language, user);

        Object o = session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        if (o != null) {
            try {
                BirthDeclaration bdf = (BirthDeclaration) o;
                BirthRegisterInfo register = bdf.getRegister();

                dsDivisionId = register.getBirthDivision().getDsDivision().getDsDivisionUKey();
                logger.debug(" DS division found from session {}", dsDivisionId);

                bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);

                birthDivisionId = register.getBirthDivision().getBdDivisionUKey();
                logger.debug(" BD division found from session {}", birthDivisionId);
            } catch (Exception e) {
                logger.debug(" Problem with birth division from session. ignoring.. {} ", e);
                dsDivisionId = 0;
            }
        }

        if (dsDivisionId == 0) {
            if (!dsDivisionList.isEmpty()) {
                dsDivisionId = dsDivisionList.keySet().iterator().next();
                logger.debug("first allowed DS Div in the list {} was set", dsDivisionId);
                bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);

                birthDivisionId = bdDivisionList.keySet().iterator().next();
                logger.debug("first allowed BD Div in the list {} was set", birthDivisionId);
            }
        }
    }

    private void populateBasicLists(String language) {
        countryList = countryDAO.getCountries(language);
        districtList = districtDAO.getDistrictNames(language, user);
        raceList = raceDAO.getRaces(language);

        /** getting full district list and DS list for mother info on page 4 */
        allDistrictList = districtDAO.getDistrictNames(language, null);
        if (!allDistrictList.isEmpty()) {
            int selectedDistrictId = allDistrictList.keySet().iterator().next();
            allDSDivisionList = dsDivisionDAO.getDSDivisionNames(selectedDistrictId, language, null);
        }
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

    public Map<Integer, String> getCountryList() {
        return countryList;
    }

    public void setCountryList(Map<Integer, String> countryList) {
        this.countryList = countryList;
    }

    public Map<Integer, String> getRaceList() {
        return raceList;
    }

    public void setRaceList(Map<Integer, String> raceList) {
        this.raceList = raceList;
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
        if (register == null) {
            register = new BirthRegisterInfo();
        }
        register.setBirthDivision(bdDivisionDAO.getBDDivisionByPK(birthDivisionId));
        logger.debug("setting BirthDivision: {}", register.getBirthDivision().getEnDivisionName());
    }

    public int getFatherCountry() {
        return fatherCountry;
    }

    public void setFatherCountry(int fatherCountry) {
        this.fatherCountry = fatherCountry;
        if (parent == null) {
            parent = new ParentInfo();
        }
        parent.setFatherCountry(countryDAO.getCountry(fatherCountry));
        logger.debug("setting Father Country: {}", parent.getFatherCountry().getEnCountryName());
    }

    public int getMotherCountry() {
        return motherCountry;
    }

    public void setMotherCountry(int motherCountry) {
        this.motherCountry = motherCountry;
        if (parent == null) {
            parent = new ParentInfo();
        }
        parent.setMotherCountry(countryDAO.getCountry(motherCountry));
        logger.debug("setting Mother Country: {}", parent.getMotherCountry().getEnCountryName());
    }

    public MarriageInfo getMarriage() {
        return marriage;
    }

    public void setMarriage(MarriageInfo marriage) {
        this.marriage = marriage;
    }

    public InformantInfo getInformant() {
        return informant;
    }

    public void setInformant(InformantInfo informant) {
        this.informant = informant;
    }

    public GrandFatherInfo getGrandFather() {
        return grandFather;
    }

    public void setGrandFather(GrandFatherInfo grandFather) {
        this.grandFather = grandFather;
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

    public ConfirmantInfo getConfirmant() {
        return confirmant;
    }

    public void setConfirmant(ConfirmantInfo confirmant) {
        this.confirmant = confirmant;
    }

    public int getFatherRace() {
        return fatherRace;
    }

    public void setFatherRace(int fatherRace) {
        this.fatherRace = fatherRace;
        if (parent == null) {
            parent = new ParentInfo();
        }
        parent.setFatherRace(raceDAO.getRace(fatherRace));
        logger.debug("setting Father Race: {}", parent.getFatherRace().getEnRaceName());
    }

    public int getMotherRace() {
        return motherRace;
    }

    public void setMotherRace(int motherRace) {
        this.motherRace = motherRace;
        if (parent == null) {
            parent = new ParentInfo();
        }
        parent.setMotherRace(raceDAO.getRace(motherRace));
        logger.debug("setting Mother Race: {}", parent.getMotherRace().getEnRaceName());
    }

    public int getMotherDSDivisionId() {
        return motherDSDivisionId;
    }

    public void setMotherDSDivisionId(int motherDSDivisionId) {
        this.motherDSDivisionId = motherDSDivisionId;
        if (parent == null) {
            parent = new ParentInfo();
        }
        parent.setMotherDSDivision(dsDivisionDAO.getDSDivisionByPK(motherDSDivisionId));
        logger.debug("setting Mother DSDivision: {}", parent.getMotherDSDivision().getEnDivisionName());
    }

    public BirthRegisterInfo getRegister() {
        return register;
    }

    public void setRegister(BirthRegisterInfo register) {
        this.register = register;
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

    public int getMotherDistrictId() {
        return motherDistrictId;
    }

    public void setMotherDistrictId(int motherDistrictId) {
        this.motherDistrictId = motherDistrictId;
    }
}