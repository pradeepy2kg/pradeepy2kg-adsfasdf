package lk.rgd.prs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.WebUtils;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.prs.PRSRuntimeException;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

/**
 * @author Chathuranga Withana
 */
public class PopulationRegisterAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(PopulationRegisterAction.class);

    // services and DAOs
    private final PopulationRegistry service;
    private final RaceDAO raceDAO;
    private final CountryDAO countryDAO;

    private Map<Integer, String> raceList;
    private Map<Integer, String> countryList;

    private Map session;
    private User user;

    private Person person;

    private int personCountryId;
    private int personRaceId;
    private String personPassportNo;
    private String permanentAddress;
    private String currentAddress;

    public PopulationRegisterAction(PopulationRegistry service, RaceDAO raceDAO, CountryDAO countryDAO) {
        this.service = service;
        this.raceDAO = raceDAO;
        this.countryDAO = countryDAO;
    }

    public String personRegistration() {
        logger.debug("Registration of exiting person to PRS");
        try {
            // add passport number in given format - e.g. LK:M1203456 
            person.addPassportNo(countryDAO.getCountry(personCountryId), personPassportNo);
            person.setStatus(Person.Status.VERIFIED);
            person.setLifeStatus(Person.LifeStatus.ALIVE);

            long pin = service.addPerson(person, user);

            // add permanent address
            final Address permanent = new Address(permanentAddress);
            permanent.setPermanent(true);
            person.specifyAddress(permanent);
            service.addAddress(permanent, user);

            // add current address if available
            if (WebUtils.filterBlanks(currentAddress) != null) {
                final Address current = new Address(currentAddress);
                person.specifyAddress(current);
                service.addAddress(current, user);
            }
            service.updatePerson(person, user);

            addActionMessage(getText("person_reg_success.message") + pin);

        } catch (PRSRuntimeException e) {
            logger.error("PRS Exception occurred while adding person to the PRS", e);
        }
        populate();         // TODO remove this temporary used
        return SUCCESS;
    }

    /**
     * This method is used to load existing person registration form
     */
    public String personRegistrationInit() {
        logger.debug("Registration of existing person to PRS page loaded");
        populate();
        return SUCCESS;
    }

    /**
     * Populate master data (race list, country list etc.) to the UI
     */
    private void populate() {
        logger.debug("Populating initializing data");
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();

        raceList = raceDAO.getRaces(language);
        countryList = countryDAO.getCountries(language);
        logger.debug("Race list and Country list populated with size : {} and {}", raceList.size(), countryList.size());
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("setting User: {}", user.getUserName());
    }

    public Map<Integer, String> getRaceList() {
        return raceList;
    }

    public void setRaceList(Map<Integer, String> raceList) {
        this.raceList = raceList;
    }

    public Map<Integer, String> getCountryList() {
        return countryList;
    }

    public void setCountryList(Map<Integer, String> countryList) {
        this.countryList = countryList;
    }

    public int getPersonCountryId() {
        return personCountryId;
    }

    public void setPersonCountryId(int personCountryId) {
        this.personCountryId = personCountryId;
    }

    public int getPersonRaceId() {
        return personRaceId;
    }

    public void setPersonRaceId(int personRaceId) {
        this.personRaceId = personRaceId;
        person.setRace(raceDAO.getRace(personRaceId));
        logger.debug("setting person Race : {}", person.getRace().getEnRaceName());
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getPersonPassportNo() {
        return personPassportNo;
    }

    public void setPersonPassportNo(String personPassportNo) {
        this.personPassportNo = personPassportNo;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }
}