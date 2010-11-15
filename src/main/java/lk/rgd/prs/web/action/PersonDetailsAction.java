package lk.rgd.prs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.prs.api.service.PopulationRegistry;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.common.util.CivilStatusUtil;
import lk.rgd.common.util.LifeStatusUtil;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.AppConstants;

import java.util.*;

public class PersonDetailsAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(PersonDetailsAction.class);

    // services and DAOs
    private final PopulationRegistry service;
    private final RaceDAO raceDAO;
    private final CountryDAO countryDAO;

    private Map session;
    private User user;

    private String gender;
    private String genderEn;
    private String lifeStatus;
    private String race;
    private String raceEn;
    private String civilStatus;
    private Address permanentAddress;
    private Set<Address> address;

    private Person person;
    private long personId;
    private List<Person> children;
    private List<Person> siblings;

    public PersonDetailsAction(PopulationRegistry service, RaceDAO raceDAO, CountryDAO countryDAO) {
        this.service = service;
        this.raceDAO = raceDAO;
        this.countryDAO = countryDAO;
    }

    public String personDetails() {
        logger.debug("getting extended details of an existing person in PRS");
        person = service.getLoadedObjectByUKey(personId, user);
        gender = GenderUtil.getGender(person.getGender(), user.getPrefLanguage());
        if (person.getRace() != null) {
            race = raceDAO.getNameByPK(person.getRace().getRaceId(), user.getPrefLanguage());
        }
        if (person.getCivilStatus() != null) {
            civilStatus = CivilStatusUtil.getCivilStatus(person.getCivilStatus(), user.getPrefLanguage());
        }
        if (person.getLifeStatus() != null) {
            lifeStatus = LifeStatusUtil.getLifeStatus(person.getLifeStatus(), user.getPrefLanguage());
        }
        children = service.findAllChildren(person, user);
        logger.debug("number of children for {} is {}", person.getFullNameInOfficialLanguage(), children.size());
        siblings = service.findAllSiblings(person, user);
        logger.debug("number of siblings for {} is {}", person.getFullNameInOfficialLanguage(), siblings.size());
        return SUCCESS;
    }

    /**
     * This method is used to load PRS certificate
     */
    public String initPRSCertificate() {
        person = service.getLoadedObjectByUKey(personId, user);
        gender = GenderUtil.getGender(person.getGender(), person.getPreferredLanguage());
        genderEn = GenderUtil.getGender(person.getGender(), AppConstants.ENGLISH);
        if (person.getRace() != null) {
            race = raceDAO.getNameByPK(person.getRace().getRaceId(), person.getPreferredLanguage());
            raceEn = raceDAO.getNameByPK(person.getRace().getRaceId(), AppConstants.ENGLISH);
        }
        if (person.getAddresses() != null) {
            for (Address address : person.getAddresses()) {
                if (address.isPermanent()) {
                    permanentAddress = address;
                    break;
                }
            }
        }
        return SUCCESS;
    }

    /**
     * This method is used to load existing person registration form
     */
    public String personRegistrationInit() {
        logger.debug("getting extended details of an existing person in PRS");
        return SUCCESS;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("setting User: {}", user.getUserName());
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public List<Person> getChildren() {
        return children;
    }

    public void setChildren(List<Person> children) {
        this.children = children;
    }

    public List getSiblings() {
        return siblings;
    }

    public void setSiblings(List siblings) {
        this.siblings = siblings;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(String lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getGenderEn() {
        return genderEn;
    }

    public void setGenderEn(String genderEn) {
        this.genderEn = genderEn;
    }

    public String getRaceEn() {
        return raceEn;
    }

    public void setRaceEn(String raceEn) {
        this.raceEn = raceEn;
    }

    public Address getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(Address permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public Set<Address> getAddress() {
        return address;
    }

    public void setAddress(Set<Address> address) {
        this.address = address;
    }

    public CountryDAO getCountryDAO() {
        return countryDAO;
    }

    // TODO remove shan
    public String pageTest(){
        return SUCCESS;
    }
}
