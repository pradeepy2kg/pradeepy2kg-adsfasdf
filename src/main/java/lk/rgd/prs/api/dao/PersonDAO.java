package lk.rgd.prs.api.dao;

import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Marriage;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.domain.PersonCitizenship;

import java.util.List;

/**
 * The DAO interface for the PRS / Persons
 *
 * @author asankha
 */
public interface PersonDAO {

    /**
     * Add a Person to the PRS
     *
     * @param person the Person to be added
     * @param user   the User initiating the action
     */
    public void addPerson(Person person, User user);

    /**
     * Update a Person on the PRS
     *
     * @param person the Person to be updated. Note, the PRS does not allow deletion of rows
     * @param user   the User initiating the action
     */
    public void updatePerson(Person person, User user);

    /**
     * Add a Marriage to the PRS
     *
     * @param m the Marriage to be added
     */
    public void addMarriage(Marriage m);

    /**
     * Update a Marriage on the PRS
     *
     * @param m the Marriage to be updated
     */
    public void updateMarriage(Marriage m);

    /**
     * Returns Marriage for the given marriageRegisterUKey
     *
     * @param mrUKey the marriage register unique key
     * @return the matching marriage
     */
    public Marriage findMarriageByMRUKey(long mrUKey);

    /**
     * Return the Address object for the given unique key
     * NOTE: TO BE ONLY USED BY THE PRS INDEXER
     *
     * @param addressUKey the unique database PK
     * @return the matching address
     */
    public Address getAddressByUKey(long addressUKey);

    /**
     * Return the Address objects for the given person UKey
     * NOTE: TO BE ONLY USED BY THE PRS INDEXER
     *
     * @param idUKey the unique key of the person
     * @return the matching addresses
     */
    public List<Address> getAddressesByPersonUKey(long idUKey);

    /**
     * Return the Citizenship objects for the given person UKey
     * NOTE: TO BE ONLY USED BY THE PRS INDEXER
     *
     * @param idUKey the unique key of the person
     * @return the matching citizenships
     */
    public List<PersonCitizenship> getCitizenshipsByPersonUKey(long idUKey);

    /**
     * Add an Address to the PRS
     *
     * @param a the Address to be added
     */
    public void addAddress(Address a);

    /**
     * Update an Address to the PRS
     *
     * @param a the Address to be added
     */
    public void updateAddress(Address a);

    /**
     * Return the Person object for the person with the given unique key
     *
     * @param personUKey the unique database PK
     * @return the matching person
     */
    public Person getByUKey(long personUKey);

    /**
     * Return the Person object for the person with the given PIN
     *
     * @param pin the unique Personal Identification Number
     * @return the matching person
     */
    public Person findPersonByPIN(long pin);

    /**
     * Return the Person object for the person with the given temporary PIN
     *
     * @param temporaryPIN the unique temporary Personal Identification Number
     * @return the matching person
     */
    public Person findPersonByTemporaryPIN(long temporaryPIN);

    /**
     * Return the list of Person objects for the given National ID card number
     *
     * @param nic the national ID card number
     * @return the matching persons
     */
    public List<Person> findPersonsByNIC(String nic);

    /**
     * Return a list of Person objects as children for the given person
     *
     * @param p the Person whose children we want to find
     * @return the matching list of person (children)
     */
    public List<Person> findAllChildren(Person p);

    /**
     * Return a list of Person objects as siblings for the given person
     *
     * @param p the Person whose siblings are to be found
     * @return the matching list of person (siblings)
     */
    public List<Person> findAllSiblings(Person p);

    /**
     * Return all records - for indexing only
     *
     * @return all records
     */
    public List<Person> findAll();

    /**
     * Returns a paginated list of Persons based on the submitted location.
     * <p>Returns a paginated list of Persons whose status is <i>UNVERIFIED, SEMI_VERIFIED, VERIFIED or CERT_PRINTED</i>
     * , based on the submitted location of the PRS entry and ordered by lastUpdatedTimestamp in descending order.</p>
     *
     * @param location the location (submitted location)
     * @param pageNo   the page number for the results required (start from 1)
     * @param noOfRows the number of rows to return per page
     * @return the matching list of persons
     */
    public List<Person> getPaginatedListByLocation(Location location, int pageNo, int noOfRows);

    /**
     * Returns a Person(since PIN is unique) based on the submitted location and Personal Identification Number(PIN).
     *
     * @param location the location (submitted location)
     * @param pin      the Personal Identification Number
     * @return the matching person
     */
    public List<Person> getByLocationAndPIN(Location location, long pin);

    /**
     * Returns a list of Persons based on the submitted location and National Identity Card(NIC) Number.
     * <p>Since NIC number is not unique there can be duplicate entries for the same NIC.
     *
     * @param location the location (submitted location)
     * @param nic      the National Identity Card Number
     * @return the matching list of persons
     */
    public List<Person> getByLocationAndNIC(Location location, String nic);

    /**
     * Returns a Person(since temporary PIN is unique) based on the submitted location and Temporary PIN
     *
     * @param location the location (submitted location)
     * @param tempPin  the Temporary Personal Identification Number
     * @return the matching person
     */
    public List<Person> getByLocationAndTempPIN(Location location, long tempPin);

}
