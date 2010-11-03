package lk.rgd.prs.api.service;

import lk.rgd.common.api.domain.User;
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
     * @param person the Person to be added
     * @param user the user performing this action
     * @return the PIN number generated for the Person - for verified records, or -1 for any other
     */
    public long addPerson(Person person, User user);

    /**
     * Add existing person to the PRS
     * @param person the Person to be added
     * @param permanentAddress the permanent address to be added
     * @param currentAddress the current address to be added
     * @param user the user performing this action
     * @param citizenshipList the person citizenship list
     * @return the PIN number generated for the Person - for verified records, or -1 for any other
     */
    public List<Person> addExistingPerson(Person person, String permanentAddress, String currentAddress, User user,
        List<PersonCitizenship> citizenshipList);

    /**
     * Update a Person on the PRS
     * @param person the Person to be updated. Note, the PRS does not allow deletion of rows
     * @param user the user performing this action
     */
    public void updatePerson(Person person, User user);

    /**
     * Return the Person object for the person with the given unique key
     *
     * @param personUKey the unique database PK
     * @param user the user performing this action
     * @return the matching person
     */
    public Person getByUKey(long personUKey, User user);

    /**
     * Return the Person object loaded with all relationships for the person with the given unique key
     *
     * @param personUKey the unique database PK
     * @param user the user performing this action
     * @return the matching person
     */
    public Person getLoadedObjectByUKey(long personUKey, User user);

    /**
     * Add a Marriage to the PRS
     * @param m the Marriage to be added
     * @param user the user performing this action
     */
    public void addMarriage(Marriage m, User user);

    /**
     * Update a Marriage to the PRS
     * @param m the Marriage to be updated
     * @param user the user performing this action
     */
    public void updateMarriage(Marriage m, User user);

    /**
     * Add an Address to the PRS
     * @param a the Address to be added
     * @param user the user performing this action
     */
    public void addAddress(Address a, User user);

    /**
     * Update an Address to the PRS
     * @param a the Address to be updated
     * @param user the user performing this action
     */
    public void updateAddress(Address a, User user);

    /**
     * Return the Person object for the person with the given PIN
     *
     * @param pin the unique Personal Identification Number
     * @param user the user performing this action
     * @return the matching person
     */
    public Person findPersonByPIN(long pin, User user);

    /**
     * Return the list of Person objects for the given National ID card number
     *
     * @param nic the national ID card number
     * @param user the user performing this action
     * @return the matching persons
     */
    public List<Person> findPersonsByNIC(String nic, User user);

    /**
     * Return the list of children as a list of Person objects for the given Person
     *
     * @param person the Person
     * @param user the user performing this action
     * @return the matching persons
     */
    public List<Person> findAllChildren(Person person, User user);

    /**
     * Return the list of siblings as a list of Person objects for the given Person
     *
     * @param person the Person
     * @param user the user performing this action
     * @return the matching persons
     */
    public List<Person> findAllSiblings(Person person, User user);

    /**
     * Return the Person object for the given PIN or National ID card number. If the NIC number matches multiple
     * Persons, the first match is returned for simplicity.
     *
     * @param pinOrNic the PIN or national ID card number
     * @param user the user performing this action
     * @return the matching person
     */
    public Person findPersonByPINorNIC(String pinOrNic, User user);

    /**
     * Find possible person matches
     * @param dob date of birth
     * @param gender the gender of the person (@see Gender)
     * @param name name of person
     * @return the list of possible matches if found
     */
    public List<Person> findPersonsByDOBGenderAndName(Date dob, int gender, String name);

}
