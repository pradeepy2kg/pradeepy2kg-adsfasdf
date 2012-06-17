package lk.rgd.prs.core.service;

import junit.framework.TestCase;
import lk.rgd.ErrorCodes;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.prs.PRSRuntimeException;
import lk.rgd.prs.api.dao.PINNumberDAO;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.domain.PersonCitizenship;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * @author Chathuranga Withana
 */
public class PopulationRegistryTest extends TestCase {

    private final ApplicationContext ctx = UnitTestManager.ctx;
    private final PopulationRegistry eCivil;
    private final UserManager userManager;
    private final RaceDAO raceDAO;
    private final CountryDAO countryDAO;
    private User deoColomboColombo;     // Generic Location 1 primaryLocationUKey : 2
    private User deoGampahaNegambo;     // Generic Location 2 primaryLocationUKey : 3
    private User adrColomboColombo;     // Generic Location 1 primaryLocationUKey : 2
    private User adrGampahaNegambo;     // Generic Location 2 primaryLocationUKey : 3
    private User argWestern;            // Head Office Colombo primaryLocationUKey : 1
    private User argNorthWestern;       // Kurunegala DS Office primaryLocationUKey : 9

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        PINNumberDAO pinNumberDAO = (PINNumberDAO) ctx.getBean("pinNumberDAOImpl", PINNumberDAO.class);
        try {
            pinNumberDAO.deleteLastPINNumber(pinNumberDAO.getLastPINNumber(75211));
        } catch (Exception ignore) {
        }
    }

    public PopulationRegistryTest() {

        eCivil = (PopulationRegistry) ctx.getBean("ecivilService", PopulationRegistry.class);
        userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
        raceDAO = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
        countryDAO = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);

        try {
            deoColomboColombo = userManager.authenticateUser("deo-colombo-colombo", "password");
            adrColomboColombo = userManager.authenticateUser("adr-colombo-colombo", "password");
            deoGampahaNegambo = userManager.authenticateUser("deo-gampaha-negambo", "password");
            adrGampahaNegambo = userManager.authenticateUser("adr-gampaha-negambo", "password");
            argWestern = userManager.authenticateUser("arg-western", "password");
            argNorthWestern = userManager.authenticateUser("arg-north-western", "password");
        } catch (AuthorizationException e) {
            throw new IllegalArgumentException("Cannot authenticate sample users");
        }
    }

    public void testAddExistingPerson() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        List<PersonCitizenship> citizenshipList = getSampleCitizenshipList();

        // test saving of minimal Person for colombo by DEO with primaryLocationUKey 2 (Generic Location 1)
        cal.set(1975, Calendar.JULY, 29, 20, 15, 00);

        // test colombo DEO adding for Generic Location 1
        Person p1 = getMinimalPerson(1000, cal.getTime(), "756985623V", null);
        List<Person> duplicates = eCivil.addExistingPerson(p1, citizenshipList, false, deoColomboColombo);

        if (duplicates.size() > 0) {
            fail("Cannot exist duplicate entries in the PRS");
        }
        assertEquals("Wrong PIN generated", 197521120040L, p1.getPin().longValue());

        // test negambo DEO adding duplicate record with same NIC
        Person p2 = getMinimalPerson(1001, cal.getTime(), "756985623V", null);
        duplicates = eCivil.addExistingPerson(p2, citizenshipList, false, deoGampahaNegambo);

        // should return exactly one duplicate record with NIC 756985623V
        if (duplicates.size() == 0 || duplicates.size() > 1) {
            fail("Should exist a single duplicate entry in the PRS");
        }
        // negambo DEO adding duplicated record by ignoring duplicates
        duplicates = eCivil.addExistingPerson(p2, citizenshipList, true, deoGampahaNegambo);

        if (duplicates.size() > 0) {
            fail("Cannot exist duplicate entries in the PRS");
        }
        assertEquals("Wrong PIN generated", 197521120058L, p2.getPin().longValue());

        // colombo DEO cannot approve
        try {
            eCivil.approvePerson(p1.getPersonUKey(), false, deoColomboColombo);
            fail("Colombo DEO cannot approve PRS entry");
        } catch (PRSRuntimeException expected) {
        }
        // negambo DEO cannot approve either
        try {
            eCivil.approvePerson(p1.getPersonUKey(), false, deoGampahaNegambo);
            fail("Negambo DEO cannot approve PRS entry of PrimanryLocationUKey 1");
        } catch (PRSRuntimeException expected) {
        }
        // TODO

    }

    public void testDeletePersonBeforeApproval() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        List<PersonCitizenship> citizenshipList = getSampleCitizenshipList();

        // test saving of minimal Person for colombo by DEO with primaryLocationUKey 2 (Generic Location 1)
        cal.set(1975, Calendar.JULY, 29, 20, 15, 00);

        // test colombo DEO adding for Generic Location 1 ignoring duplicates
        Person p1 = getMinimalPerson(1000, cal.getTime(), "756985623V", null);
        eCivil.addExistingPerson(p1, citizenshipList, true, deoColomboColombo);
        long personUKey = p1.getPersonUKey();
        String comment = "Comment added for Deletion";

        // colombo DEO cannot delete
        try {
            eCivil.deletePersonBeforeApproval(personUKey, null, deoColomboColombo);
            fail("Colombo DEO cannot delete PRS entry");
        } catch (PRSRuntimeException expected) {
            assertEquals("Error code invalid", ErrorCodes.PRS_DELETE_RECORD_DENIED, expected.getErrorCode());
        }

        // negambo DEO cannot delete either
        try {
            eCivil.deletePersonBeforeApproval(personUKey, comment, deoGampahaNegambo);
            fail("Negambo DEO cannot delete PRS entry");
        } catch (PRSRuntimeException expected) {
            assertEquals("Error code invalid", ErrorCodes.PRS_DELETE_RECORD_DENIED, expected.getErrorCode());
        }

        // colombo ADR cannot delete without a comment
        try {
            eCivil.deletePersonBeforeApproval(personUKey, null, adrColomboColombo);
            fail("Colombo ADR cannot delete PRS entry without comment");
        } catch (PRSRuntimeException expected) {
            assertEquals("Error code invalid", ErrorCodes.COMMENT_REQUIRED_PRS_DELETE, expected.getErrorCode());
        }

        // North western ARG cannot delete due to :Colombo DEO location not accessible
        try {
            eCivil.deletePersonBeforeApproval(personUKey, comment, argNorthWestern);
            fail("North west ARG cannot delete PRS entry");
        } catch (PRSRuntimeException expected) {
            assertEquals("Error code invalid", ErrorCodes.PERMISSION_DENIED, expected.getErrorCode());
        }

        // TODO approve person and try to delete by adrColomboColombo and argWestern

        // colombo ADR can delete added person
        try {
            eCivil.deletePersonBeforeApproval(personUKey, comment, adrColomboColombo);
        } catch (Exception unexpected) {
            fail("Unexpected exception occurred");
        }

        // negambo DEO adding PRS entry
        Person p2 = getMinimalPerson(1001, cal.getTime(), "756985624V", null);
        eCivil.addExistingPerson(p2, citizenshipList, true, deoGampahaNegambo);
        personUKey = p2.getPersonUKey();

        // western ARG can delete negambo DEO added PRS entry
        try {
            eCivil.deletePersonBeforeApproval(personUKey, comment, argWestern);
        } catch (Exception unexpected) {
            fail("Unexpected exception occurred");
        }
    }

    public void testRejectPersonBeforeApproval() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        List<PersonCitizenship> citizenshipList = getSampleCitizenshipList();

        // test saving of minimal Person for colombo by DEO with primaryLocationUKey 2 (Generic Location 1)
        cal.set(1975, Calendar.JULY, 29, 20, 15, 00);

        // test colombo DEO adding for Generic Location 1 ignoring duplicates
        Person p1 = getMinimalPerson(1000, cal.getTime(), "756985623V", null);
        eCivil.addExistingPerson(p1, citizenshipList, true, deoColomboColombo);
        long personUKey = p1.getPersonUKey();
        String comment = "Comment added for Rejection";

        // colombo DEO cannot reject
        try {
            eCivil.rejectPersonBeforeApproval(personUKey, comment, deoColomboColombo);
            fail("Colombo DEO cannot reject PRS entry");
        } catch (PRSRuntimeException expected) {
            assertEquals("Error code invalid", ErrorCodes.PRS_REJECT_RECORD_DENIED, expected.getErrorCode());
        }

        // negambo DEO cannot reject either
        try {
            eCivil.rejectPersonBeforeApproval(personUKey, comment, deoGampahaNegambo);
            fail("Negambo DEO cannot reject PRS entry");
        } catch (PRSRuntimeException expected) {
            assertEquals("Error code invalid", ErrorCodes.PRS_REJECT_RECORD_DENIED, expected.getErrorCode());
        }

        // colombo ADR cannot reject without a comment
        try {
            eCivil.rejectPersonBeforeApproval(personUKey, null, adrColomboColombo);
            fail("Colombo ADR cannot reject PRS entry without comment");
        } catch (PRSRuntimeException expected) {
            assertEquals("Error code invalid", ErrorCodes.COMMENT_REQUIRED_PRS_REJECT, expected.getErrorCode());
        }

        // North western ARG cannot reject due to :Colombo DEO location not accessible
        try {
            eCivil.rejectPersonBeforeApproval(personUKey, comment, argNorthWestern);
            fail("North west ARG cannot reject PRS entry");
        } catch (PRSRuntimeException expected) {
            assertEquals("Error code invalid", ErrorCodes.PERMISSION_DENIED, expected.getErrorCode());
        }

        // TODO approve person and try to reject by adrColomboColombo and argWestern

        // colombo ADR can reject PRS entry
        try {
            eCivil.rejectPersonBeforeApproval(personUKey, comment, adrColomboColombo);
        } catch (Exception unexpected) {
            fail("Unexpected exception occurred");
        }

        // negambo DEO adding PRS entry
        Person p2 = getMinimalPerson(1001, cal.getTime(), "756985624V", null);
        eCivil.addExistingPerson(p2, citizenshipList, true, deoGampahaNegambo);
        personUKey = p2.getPersonUKey();

        // western ARG can reject negambo DEO added PRS entry
        try {
            eCivil.rejectPersonBeforeApproval(personUKey, comment, argWestern);
        } catch (Exception unexpected) {
            fail("Unexpected exception occurred");
        }
    }

    private Person getMinimalPerson(long id, Date dob, String nic, Long tempPIN) {
        Date today = new Date();
        Person person = new Person();
        person.setDateOfRegistration(today);
        person.setGender(0);
        person.setDateOfBirth(dob);
        person.setNic(nic);
        person.setTemporaryPin(tempPIN);
        person.setFullNameInOfficialLanguage("පුද්ගලයාගේ නම සිංහලෙන්" + id);
        person.setFullNameInEnglishLanguage("Person Name English" + id);
        person.setPlaceOfBirth("Place of birth" + id);
        person.setRace(raceDAO.getRace(1));
        person.setPermanentAddress("Permanent Address of " + id);
        person.setCurrentAddress("Current Address of " + id);
        person.setCivilStatus(Person.CivilStatus.NEVER_MARRIED);
        return person;
    }

    private List<PersonCitizenship> getSampleCitizenshipList() {
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