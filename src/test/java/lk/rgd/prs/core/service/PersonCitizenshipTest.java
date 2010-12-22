package lk.rgd.prs.core.service;

import junit.framework.TestCase;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.domain.PersonCitizenship;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Chathuranga Withana
 */
public class PersonCitizenshipTest extends TestCase {
    private final ApplicationContext ctx = UnitTestManager.ctx;
    private final PopulationRegistry eCivil;
    private final CountryDAO countryDAO;
    private final RaceDAO raceDAO;
    private final UserManager userManager;
    private User deoColomboColombo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public PersonCitizenshipTest() {
        eCivil = (PopulationRegistry) ctx.getBean("ecivilService", PopulationRegistry.class);
        countryDAO = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
        raceDAO = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
        userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);

        try {
            deoColomboColombo = userManager.authenticateUser("deo-colombo-colombo", "password");
        } catch (AuthorizationException e) {
            throw new IllegalArgumentException("Cannot authenticate sample users");
        }
    }

    public void testAddCitizenship() throws Exception {
        Calendar dob = Calendar.getInstance();
        dob.add(Calendar.YEAR, -30);

        Person p1 = getMinimalPerson(dob.getTime());
        try {
            eCivil.addExistingPerson(p1, addSampleCitizenship(), false, deoColomboColombo);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Person failed to add PRS");
        }
    }

    private Person getMinimalPerson(Date dob) {
        Date today = new Date();
        Person person = new Person();
        person.setDateOfRegistration(today);
        person.setGender(1);
        person.setDateOfBirth(dob);
        person.setFullNameInOfficialLanguage("පුද්ගලයාගේ නම");
        person.setFullNameInEnglishLanguage("Person Name");
        person.setPlaceOfBirth("Place of birth");
        person.setRace(raceDAO.getRace(1));
        person.setPermanentAddress("Permanent Address");
        person.setCurrentAddress("Current Address");
        person.setCivilStatus(Person.CivilStatus.NEVER_MARRIED);

        return person;
    }

    private List<PersonCitizenship> addSampleCitizenship() {
        List<PersonCitizenship> pc = new ArrayList<PersonCitizenship>();
        PersonCitizenship c1 = new PersonCitizenship();
        c1.setCountry(countryDAO.getCountry(1));
        c1.setPassportNo("LK:1231111");
        pc.add(c1);

        PersonCitizenship c2 = new PersonCitizenship();
        c2.setCountry(countryDAO.getCountry(2));
        c2.setPassportNo("JP:1232222");
        pc.add(c2);

        PersonCitizenship c3 = new PersonCitizenship();
        c3.setCountry(countryDAO.getCountry(3));
        c3.setPassportNo("IN:1233333");
        pc.add(c3);

        return pc;
    }
}
