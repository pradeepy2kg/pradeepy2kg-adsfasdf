package lk.rgd.prs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.AppConstants;
import lk.rgd.Permission;
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
public class PersonRegisterAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(PersonRegisterAction.class);
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

    private boolean direct;
    private boolean ignoreDuplicate;
    private boolean allowEdit;
    private boolean allowApprove;

    private int personCountryId;
    private int personRaceId;
    private long personUKey;
    private String personPassportNo;
    private String citizenship;
    private String language;

    public PersonRegisterAction(PopulationRegistry service, RaceDAO raceDAO, CountryDAO countryDAO) {
        this.service = service;
        this.raceDAO = raceDAO;
        this.countryDAO = countryDAO;
    }

    /**
     * This method used to register persons to the PRS and to edit existing PRS records before approval by ADR or higher
     */
    public String personRegistration() {
        logger.debug("Registration of exiting person to PRS");
        validateExistingPersonRegistration();

        citizenshipList = new ArrayList<PersonCitizenship>();
        getCitizenshipList(citizenshipList);
        if (personCountryId != 0 && personPassportNo != null) {
            citizenshipList.add(createPersonCitizenship(countryDAO.getCountry(personCountryId), personPassportNo));
        }

        if (personUKey == 0) {
            personList = service.addExistingPerson(person, citizenshipList, ignoreDuplicate, user);
            if (personList.isEmpty()) {
                addActionMessage(getText("person_reg_success.message", new String[]{person.getPin().toString()}));
            }
        } else {
            logger.debug("Editing existing person in PRS with personUKey : {}", personUKey);
            service.editExistingPersonBeforeApproval(person, citizenshipList, user);
            personList = Collections.emptyList();
            addActionMessage(getText("person_edit_success.message"));
        }

        if (personList.isEmpty()) {
            // personUKey used to redirect to PRS certificate page
            personUKey = person.getPersonUKey();
            if (logger.isDebugEnabled()) {
                logger.debug("Person with name : " + NameFormatUtil.getDisplayName(person.getFullNameInEnglishLanguage(), 30)
                    + " and dateOfBirth : " + DateTimeUtils.getISO8601FormattedString(person.getDateOfBirth())
                    + " added to the PRS with PersonUKey : " + person.getPersonUKey());
            }
            initPermissions();
            return SUCCESS;
        } else {
            populate();
            initPermissions();
            return "form";
        }
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
     * This method is used to load existing person registration form in edit mode for specified personUKey
     */
    public String personEditInit() {
        logger.debug("Edit Person Details with PersonUKey : {}", personUKey);
        populate();
        person = service.loadPersonToEdit(personUKey, user);
        // load race of the person
        if (person.getRace() != null) {
            personRaceId = person.getRace().getRaceId();
        }
        // load person citizenship list
        final Set<PersonCitizenship> citizenSet = person.getCountries();
        if (citizenSet != null && !citizenSet.isEmpty()) {
            citizenshipList = new ArrayList<PersonCitizenship>();
            for (PersonCitizenship pc : citizenSet) {
                citizenshipList.add(pc);
            }
        }
        return SUCCESS;
    }

    /**
     * This method is used to return back from PRS certificate to person register details page
     */
    public String backFromPRSCertificate() {
        logger.debug("Return back from PRS certificate to person details page in direct mode : {}", direct);
        initPermissions();
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

    /**
     * This method used to show/hide specific links in JSPs according to user permissions
     */
    private void initPermissions() {
        allowEdit = user.isAuthorized(Permission.PRS_EDIT_PERSON);
        allowApprove = user.isAuthorized(Permission.PRS_APPROVE_PERSON);

        if (logger.isDebugEnabled()) {
            logger.debug("User : " + user.getUserId() + " is allowed to edit PRS entries : " + allowEdit +
                " ,allowed to approve PRS entries : " + allowApprove);
        }
    }

    /**
     * Validations for existing person registration form
     */
    private void validateExistingPersonRegistration() {
        if (person.getDateOfRegistration() == null || person.getDateOfBirth() == null || person.getRace() == null ||
            person.getPlaceOfBirth() == null || person.getFullNameInOfficialLanguage() == null ||
            person.getFullNameInEnglishLanguage() == null || person.getPermanentAddress() == null) {
            addFieldError("requiredFieldsEmpty", getText("er.label.requiredFields"));
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

    public boolean isAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    public boolean isAllowApprove() {
        return allowApprove;
    }

    public void setAllowApprove(boolean allowApprove) {
        this.allowApprove = allowApprove;
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

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = WebUtils.filterBlanksAndToUpper(citizenship);
        logger.debug("setting citizenship list : {}", citizenship);
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public boolean isIgnoreDuplicate() {
        return ignoreDuplicate;
    }

    public void setIgnoreDuplicate(boolean ignoreDuplicate) {
        this.ignoreDuplicate = ignoreDuplicate;
    }
}