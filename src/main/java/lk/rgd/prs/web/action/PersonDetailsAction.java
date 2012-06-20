package lk.rgd.prs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.AppConstants;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.dao.UserLocationDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.CivilStatusUtil;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.common.util.LifeStatusUtil;
import lk.rgd.common.util.NameFormatUtil;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.CommonUtil;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PersonDetailsAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(PersonDetailsAction.class);
    private static final String PAGE_LOAD = "pageLoad";

    // services and DAOs
    private final PopulationRegistry service;
    private final RaceDAO raceDAO;
    private final CountryDAO countryDAO;
    private final UserLocationDAO userLocationDAO;
    private final CommonUtil commonUtil;

    private Map session;
    private User user;

    private boolean direct;
    private int locationId;
    private int pageNo;
    private int printStart;

    private String gender;
    private String genderEn;
    private String lifeStatus;
    private String race;
    private String raceEn;
    private String civilStatus;
    private Address permanentAddress;
    private Address currentAddress;
    private Set<Address> address;
    private String approvedUserSign;
    private String issueLocation;
    private Map<Integer, String> locationList;
    private Map<String, String> userList;


    private Person person;
    private long personUKey;
    private List<Person> children;
    private List<Person> siblings;

    public PersonDetailsAction(PopulationRegistry service, RaceDAO raceDAO, CountryDAO countryDAO, CommonUtil commonUtil, UserLocationDAO userLocationDAO) {
        this.service = service;
        this.raceDAO = raceDAO;
        this.countryDAO = countryDAO;
        this.commonUtil = commonUtil;
        this.userLocationDAO = userLocationDAO;
    }

    public String personDetails() {
        logger.debug("getting extended details of an existing person in PRS");
        person = service.getLoadedObjectByUKey(personUKey, user);
        if (person != null) {
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

            populateAddressDetails(person);

            children = service.findAllChildren(person, user);
            logger.debug("number of children for {} is {}", person.getFullNameInOfficialLanguage(), children.size());
            siblings = service.findAllSiblings(person, user);
            logger.debug("number of siblings for {} is {}", person.getFullNameInOfficialLanguage(), siblings.size());
            return SUCCESS;
        } else {
            logger.debug("For Person with personUKey : {} does not have a valid PRS entry", personUKey);
            addActionError(getText("message.person_null"));
            return PAGE_LOAD;
        }
    }

    private void populateAddressDetails(Person p) {
        Set<Address> addresses = p.getAddresses();
        Map<Long, Address> permanentMap = new LinkedHashMap<Long, Address>();
        Map<Long, Address> currentMap = new LinkedHashMap<Long, Address>();

        for (Address add : addresses) {
            if (add.isPermanent()) {
                permanentMap.put(add.getAddressUKey(), add);
            } else {
                currentMap.put(add.getAddressUKey(), add);
            }
        }

        // load permanent address to the transient field
        if (addresses != null && permanentMap.size() > 0) {
            Set<Long> keys = permanentMap.keySet();
            TreeSet<Long> addressTreeSet = new TreeSet<Long>(keys);

            Iterator<Long> it = addressTreeSet.descendingIterator();
            while (it.hasNext()) {
                Long addKey = it.next();
                permanentAddress = permanentMap.get(addKey);
                break;
            }
        }

        // load current address to the transient field
        if (addresses != null && currentMap.size() > 0) {
            Set<Long> keys = currentMap.keySet();
            TreeSet<Long> addressTreeSet = new TreeSet<Long>(keys);

            Iterator<Long> it = addressTreeSet.descendingIterator();
            while (it.hasNext()) {
                Long addKey = it.next();
                currentAddress = currentMap.get(addKey);
                break;
            }
        }
    }

    /**
     * This method is used to load PRS certificate
     */
    public String initPRSCertificate() {
        person = service.getLoadedObjectByUKey(personUKey, user);
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
        locationList = commonUtil.populateActiveUserLocations(user, user.getPrefLanguage());
        if (!locationList.isEmpty()) {
            userList = new HashMap<String, String>();
            int selectedLocationId = locationList.keySet().iterator().next();
            for (User u : userLocationDAO.getBirthCertSignUsersByLocationId(selectedLocationId, true)) {
                userList.put(u.getUserId(), NameFormatUtil.getDisplayName(u.getUserName(), 50));
            }
            // TODO Use PRS related permissions (Having Birth permission at present)
            User approvalUser = userLocationDAO.getBirthCertSignUsersByLocationId(selectedLocationId, true).iterator().next();
            if (approvalUser != null) {
                logger.debug("Approval user {}", approvalUser.getUserId());
                String prefLang = person.getPreferredLanguage();
                approvedUserSign = approvalUser.getUserSignature(prefLang);
                issueLocation = userLocationDAO.getUserLocation(approvalUser.getUserId(), selectedLocationId).getLocation().getLocationSignature(prefLang);
                logger.debug("Approved by {} in location {}", approvedUserSign, issueLocation);
            }
        }
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

    public long getPersonUKey() {
        return personUKey;
    }

    public void setPersonUKey(long personUKey) {
        this.personUKey = personUKey;
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

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPrintStart() {
        return printStart;
    }

    public void setPrintStart(int printStart) {
        this.printStart = printStart;
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

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getApprovedUserSign() {
        return approvedUserSign;
    }

    public void setApprovedUserSign(String approvedUserSign) {
        this.approvedUserSign = approvedUserSign;
    }

    public String getIssueLocation() {
        return issueLocation;
    }

    public void setIssueLocation(String issueLocation) {
        this.issueLocation = issueLocation;
    }

    public Map<Integer, String> getLocationList() {
        return locationList;
    }

    public void setLocationList(Map<Integer, String> locationList) {
        this.locationList = locationList;
    }

    public Map<String, String> getUserList() {
        return userList;
    }

    public void setUserList(Map<String, String> userList) {
        this.userList = userList;
    }
}