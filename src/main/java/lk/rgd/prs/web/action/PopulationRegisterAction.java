package lk.rgd.prs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.AppConstants;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.common.util.NameFormatUtil;
import lk.rgd.common.util.WebUtils;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.domain.PersonCitizenship;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Chathuranga Withana
 */
public class PopulationRegisterAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(PopulationRegisterAction.class);
    // This delimiter used to delimit countryId and passportNo from citizenship string
    private static final String delimiter = ":,";

    // services and DAOs
    private final PopulationRegistry service;
    private final RaceDAO raceDAO;
    private final CountryDAO countryDAO;

    private Map<Integer, String> raceList;
    private Map<Integer, String> countryList;
    private List<Person> personList;
    private List<PersonCitizenship> citizenshipList;

    private Map session;
    private User user;
    private Person person;

    private long personUKey;
    private int personCountryId;
    private int personRaceId;
    private String personPassportNo;
    private String permanentAddress;
    private String currentAddress;
    private String citizenship;
    private String language;
    private boolean ignoreDuplicate;

    public PopulationRegisterAction(PopulationRegistry service, RaceDAO raceDAO, CountryDAO countryDAO) {
        this.service = service;
        this.raceDAO = raceDAO;
        this.countryDAO = countryDAO;
    }

    /**
     * This method used to register persons to the PRS
     */
    public String personRegistration() {
        logger.debug("Registration of exiting person to PRS");
        // TODO implement validation 
        validateExistingPersonRegistration();

        citizenshipList = new ArrayList<PersonCitizenship>();
        getCitizenshipList(citizenshipList);
        if (personCountryId != 0 && personPassportNo != null) {
            citizenshipList.add(createPersonCitizenship(countryDAO.getCountry(personCountryId), personPassportNo));
        }

        if (personUKey == 0) {
            personList = service.addExistingPerson(person, permanentAddress, currentAddress, citizenshipList, ignoreDuplicate, user);
        } else {
            logger.debug("Editing existing person in PRS with personUKey : {}", personUKey);
            service.editExistingPerson(person, permanentAddress, currentAddress, citizenshipList, user);
            personList = Collections.emptyList();
        }

        if (personList.isEmpty()) {
            final long pin = person.getPin();
            // personUKey used to redirect to PRS certificate page
            personUKey = person.getPersonUKey();
            addActionMessage(getText("person_reg_success.message") + pin);

            if (logger.isDebugEnabled()) {
                logger.debug("Person with name : " + NameFormatUtil.getDisplayName(person.getFullNameInEnglishLanguage(), 30)
                    + " and dateOfBirth : " + DateTimeUtils.getISO8601FormattedString(person.getDateOfBirth())
                    + " added to the PRS with PersonUKey : " + person.getPersonUKey() + " and generated PIN : " + pin);
            }
            return SUCCESS;
        } else {
            populate();
            return "form";
        }
    }

    /**
     * Returns citizenship list after processing citizenship String passed by the Existing Person Registration Form
     */
    private void getCitizenshipList(List<PersonCitizenship> list) {
        logger.debug("Received citizenship string : {}", citizenship);
        if (citizenship != null && citizenship.trim().length() > 0) {

            int countryId;
            String passportNo;
            StringTokenizer tokenizer = new StringTokenizer(citizenship, delimiter);
            while (tokenizer.hasMoreTokens()) {
                countryId = Integer.parseInt(tokenizer.nextToken());
                passportNo = tokenizer.nextToken();
                list.add(createPersonCitizenship(countryDAO.getCountry(countryId), passportNo));
            }
        }
    }

    /**
     * Return PersonCitizenship object when country and passport number passed
     */
    private PersonCitizenship createPersonCitizenship(Country country, String passport) {
        PersonCitizenship pc = new PersonCitizenship();
        pc.setCountry(country);
        pc.setPassportNo(passport);
        return pc;
    }

    /**
     * Validations for existing person registration form
     */
    private void validateExistingPersonRegistration() {
        // TODO validate inputs
        /*if (person.getDateOfRegistration() == null || person.getDateOfBirth() == null || person.getPlaceOfBirth() == null
            || person.getFullNameInOfficialLanguage() == null || person.getFullNameInEnglishLanguage() == null) {

        }*/
    }

    /**
     * This method edits existing persons in the PRS
     */
    public String personEditInit() {
        logger.debug("Edit Person Details with PersonUKey : {}", personUKey);
        populate();
        person = service.getByUKey(personUKey, user);
        return SUCCESS;
    }

    /**
     * This method is used to load existing person registration form
     */
    public String personRegistrationInit() {
        logger.debug("Registration of existing person to PRS page loaded");
        populate();
        // followings used to load basic values in page load. e.g: gender:male, preferredLang:sinhala etc. 
        person = new Person();
        person.setPreferredLanguage(AppConstants.SINHALA);

        return SUCCESS;
    }

    /**
     * Populate master data (race list, country list etc.) to the UI
     */
    private void populate() {
        logger.debug("Populating initializing data");
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
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("setting User: {} and Language : {}", user.getUserName(), language);
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

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public List<PersonCitizenship> getCitizenshipList() {
        return citizenshipList;
    }

    public void setCitizenshipList(List<PersonCitizenship> citizenshipList) {
        this.citizenshipList = citizenshipList;
    }

    public long getPersonUKey() {
        return personUKey;
    }

    public void setPersonUKey(long personUKey) {
        this.personUKey = personUKey;
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
        this.personPassportNo = WebUtils.filterBlanksAndToUpper(personPassportNo);
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = WebUtils.filterBlanksAndToUpper(permanentAddress);
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = WebUtils.filterBlanksAndToUpper(currentAddress);
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = WebUtils.filterBlanksAndToUpper(citizenship);
        logger.debug("setting citizenship list : {}", citizenship);
    }

    public boolean isIgnoreDuplicate() {
        return ignoreDuplicate;
    }

    public void setIgnoreDuplicate(boolean ignoreDuplicate) {
        this.ignoreDuplicate = ignoreDuplicate;
    }
}