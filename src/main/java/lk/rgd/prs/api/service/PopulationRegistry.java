package lk.rgd.prs.api.service;

import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Marriage;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.domain.PersonCitizenship;

import java.util.Date;
import java.util.List;

/**
 * This is the main public interface to the ePopulation Registry (PRS). The interface consists of methods to populate and
 * update the PRS, as well as to search for records using the direct Database interface. However direct database
 * queries should be limited to primary key lookups as much as possible to ensure optimal performance. The generic
 * query interface is through Apache Lucene, for advanced search options.
 *
 * @author asankha
 */
public interface PopulationRegistry {

    /**
     * Add a Person to the PRS
     *
     * @param person the Person to be added
     * @param user   the user performing this action
     * @return the PIN number generated for the Person - for verified records, or -1 for any other
     */
    public long addPerson(Person person, User user);

    /**
     * Add existing person to the PRS
     *
     * @param person           the Person to be added
     * @param citizenshipList  the person citizenship list to be added
     * @param ignoreDuplicates an explicit switch to disable optional validations, to ignore duplicates
     * @param user             the user performing this action
     * @return the PIN number generated for the Person - for verified records, or -1 for any other
     */
    public List<Person> addExistingPerson(Person person, List<PersonCitizenship> citizenshipList,
        boolean ignoreDuplicates, User user);

    /**
     * Load person from PRS for edit with permanent address, current address, citizenship list etc.
     *
     * @param personUKey the unique database PK
     * @param user       the user performing this action
     * @return the matching person with values loaded for edit
     */
    public Person loadPersonToEdit(long personUKey, User user);

    /**
     * Update existing person to the PRS <b>before</b> approval of ADR or higher authority
     *
     * @param person          the Person to be updated
     * @param citizenshipList the person citizenship list to be updated
     * @param user            the user performing this action
     */
    public void editExistingPersonBeforeApproval(Person person, List<PersonCitizenship> citizenshipList, User user);

    /**
     * Update an existing person to the PRS <b>after</b> approval of ADR or higher. Cannot update all the fields of the
     * person only limited number of fields can be updated (only contact details) for e.g. current address, permanent
     * address, telephone number etc.
     *
     * @param person          the Person to be updated
     * @param citizenshipList the person citizenship list to be updated
     * @param user            the user performing the action
     */
    public void editExistingPersonAfterApproval(Person person, List<PersonCitizenship> citizenshipList, User user);

    /**
     * Approve a Person by ADR or higher authority
     * <p/>
     * An unique Person Identification Number is generated for successfully approved persons or returns a list of
     * warnings while without ignoring warnings, if ignore warnings is true eliminate warning list
     *
     * @param personUKey     the unique database PK
     * @param ignoreWarnings an explicit switch that indicates that the record should be approved ignoring warnings
     * @param user           the user performing the action
     * @return a list of warnings, if ignoreWarnings is false
     */
    public List<UserWarning> approvePerson(long personUKey, boolean ignoreWarnings, User user);

    /**
     * Delete an existing person <b>before</b> approval by ADR or higher authority
     *
     * @param personUKey the unique database PK
     * @param comment    the comment specifying the reason for deletion
     * @param user       the user performing the action
     */
    public void deletePersonBeforeApproval(long personUKey, String comment, User user);

    /**
     * Reject an existing Person <b>before</b> approval by ADR or higher authority
     *
     * @param personUKey the unique database PK
     * @param comment    the comment specifying the reason for rejection
     * @param user       the user performing the action
     */
    public void rejectPersonBeforeApproval(long personUKey, String comment, User user);

    /**
     * Mark specified Population Registry's certificate as printed by ADR or higher authority
     *
     * @param personUKey the Person whose certificate to be marked as printed
     * @param user       the user initiating the action
     */
    public void markPRSCertificateAsPrinted(long personUKey, User user);

    /**
     * Update a Person on the PRS
     *
     * @param person the Person to be updated. Note, the PRS does not allow deletion of rows
     * @param user   the user performing this action
     */
    public void updatePerson(Person person, User user);

    /**
     * Return the Person object for the person with the given unique key
     *
     * @param personUKey the unique database PK
     * @param user       the user performing this action
     * @return the matching person
     */
    public Person getByUKey(long personUKey, User user);

    /**
     * Return the Person object loaded with all relationships for the person with the given unique key
     *
     * @param personUKey the unique database PK
     * @param user       the user performing this action
     * @return the matching person
     */
    public Person getLoadedObjectByUKey(long personUKey, User user);

    /**
     * Add a Marriage to the PRS
     *
     * @param m    the Marriage to be added
     * @param user the user performing this action
     */
    public void addMarriage(Marriage m, User user);

    /**
     * Update a Marriage to the PRS
     *
     * @param m    the Marriage to be updated
     * @param user the user performing this action
     */
    public void updateMarriage(Marriage m, User user);

    /**
     * Return the Marriage object for the given marriageRegisterUKey of the CRS
     *
     * @param mrUKey the unique Marriage Register identification number
     * @param user   the user performing the action
     * @return the matching marriage
     */
    public Marriage findMarriageByMRUKey(long mrUKey, User user);

    /**
     * Add an Address to the PRS
     *
     * @param a    the Address to be added
     * @param user the user performing this action
     */
    public void addAddress(Address a, User user);

    /**
     * Update an Address to the PRS
     *
     * @param a    the Address to be updated
     * @param user the user performing this action
     */
    public void updateAddress(Address a, User user);

    /**
     * Add PersonCitizenship to the PRS
     *
     * @param citizenship the PersonCitizenship to be added
     * @param user        the use performing this action
     */
    public void addCitizenship(PersonCitizenship citizenship, User user);

    /**
     * Update PersonCitizenship to the PRS
     *
     * @param citizenship the PersonCitizenship to be added
     * @param user        the use performing this action
     */
    public void updateCitizenship(PersonCitizenship citizenship, User user);

    /**
     * Return the Person object for the person with the given PIN
     *
     * @param pin  the unique Personal Identification Number
     * @param user the user performing this action
     * @return the matching person
     */
    public Person findPersonByPIN(long pin, User user);

    /**
     * Return the list of Person objects for the given National ID card number
     *
     * @param nic  the national ID card number
     * @param user the user performing this action
     * @return the matching persons
     */
    public List<Person> findPersonsByNIC(String nic, User user);

    /**
     * Return the list of children as a list of Person objects for the given Person
     *
     * @param person the Person
     * @param user   the user performing this action
     * @return the matching persons
     */
    public List<Person> findAllChildren(Person person, User user);

    /**
     * Return the list of siblings as a list of Person objects for the given Person
     *
     * @param person the Person
     * @param user   the user performing this action
     * @return the matching persons
     */
    public List<Person> findAllSiblings(Person person, User user);

    /**
     * Return the Person object for the given PIN or National ID card number. If the NIC number matches multiple
     * Persons, the first match is returned for simplicity.
     *
     * @param pinOrNic the PIN or national ID card number
     * @param user     the user performing this action
     * @return the matching person
     */
    public Person findPersonByPINorNIC(String pinOrNic, User user);

    /**
     * Return the unique Person object for the given PIN or National ID card number. If the NIC number matches
     * multiple Persons, null is returned
     *
     * @param pinOrNic the PIN or national ID card number
     * @param user     the user performing this action
     * @return the unique matching person
     */
    public Person findUniquePersonByPINorNIC(String pinOrNic, User user);

    /**
     * Find possible person matches
     *
     * @param dob    date of birth
     * @param gender the gender of the person (@see Gender)
     * @param name   name of person
     * @return the list of possible matches if found
     */
    public List<Person> findPersonsByDOBGenderAndName(Date dob, int gender, String name);

    /**
     * Returns a list of Persons awaiting approval, certificate print, edit etc. by ADR or higher authority based on the
     * specified Location (given location should be a assigned active location of the specified User). Results are
     * ordered on the descending order of lastUpdatedTimestamp
     *
     * @param location the specified location
     * @param pageNo   the page number of the results required
     * @param noOfRows the number of rows to return per page
     * @param user     the user initiating the action
     * @return the matching list of persons
     */
    public List<Person> getPersonsByLocation(Location location, int pageNo, int noOfRows, User user);

    /**
     * Returns a list of Persons based on the specified Location and PIN. Since PIN is unique there cannot be more than
     * one results.
     *
     * @param location the specified location
     * @param pin      the Personal Identification Number(PIN)
     * @param user     the user initiating the action
     * @return the matching list of persons(max size is one, since PIN is unique)
     */
    public List<Person> getPersonByLocationAndPIN(Location location, long pin, User user);

    /**
     * Returns a list of Persons based on the specified Location and NIC ordered by the descending order of
     * lastUpdatedTimestamp. There can be more than single result since NIC is not unique.
     *
     * @param location the specified location
     * @param nic      the specified National Identity Card Number
     * @param user     the user initiating the action
     * @return the matching list of persons
     */
    public List<Person> getPersonsByLocationAndNIC(Location location, String nic, User user);

    /**
     * Returns a list of Persons based on the specified Location and temporary PIN. Since temporary PIN is unique there
     * cannot be more than one results.
     *
     * @param location the specified location
     * @param tempPin  the Temporary Personal Identification Number(PIN)
     * @param user     the user initiating the action
     * @return the matching list of persons(max size is one, since temporary PIN is unique)
     */
    public List<Person> getPersonByLocationAndTemporaryPIN(Location location, long tempPin, User user);

    /**
     * find list of  grand mother of given person
     *
     * @param person person
     * @param user   user who perform the action
     * @return list of persons
     */
    public List<Person> findGrandMother(Person person, User user);

    /**
     * find list of  grand father of given person
     *
     * @param person person
     * @param user   user who perform the action
     * @return list of persons
     */
    public List<Person> findGrandFather(Person person, User user);
}
