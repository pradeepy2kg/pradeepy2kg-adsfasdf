package lk.rgd.prs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.prs.api.service.PopulationRegistry;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.WebConstants;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class PersonDetailsAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(PersonDetailsAction.class);

    // services and DAOs
    private final PopulationRegistry service;
    private final RaceDAO raceDAO;
    private final CountryDAO countryDAO;

    private Map session;
    private User user;

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
        setPerson(service.getByUKey(personId, user));
        setChildren(service.findAllChildren(getPerson(), user));
        logger.debug("number of children for {} is {}", person.getFullNameInOfficialLanguage(), children.size());

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
}
