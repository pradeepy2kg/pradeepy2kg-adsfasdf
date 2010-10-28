package lk.rgd.prs.api.dao;

import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Marriage;
import lk.rgd.prs.api.domain.Person;

import java.util.List;

/**
 * The DAO interface for the PRS / Persons
 *
 * @author asankha
 */
public interface PersonDAO {

    /**
     * Add a Person to the PRS
     * @param person the Person to be added
     */
    public void addPerson(Person person);

    /**
     * Update a Person on the PRS
     * @param person the Person to be updated. Note, the PRS does not allow deletion of rows
     */
    public void updatePerson(Person person);

    /**
     * Add a Marriage to the PRS
     * @param m the Marriage to be added
     */
    public void addMarriage(Marriage m);

    /**
     * Update a Marriage on the PRS
     * @param m the Marriage to be updated
     */
    public void updateMarriage(Marriage m);

    /**
     * Add an Address to the PRS
     * @param a the Address to be added
     */
    public void addAddress(Address a);

    /**
     * Update an Address to the PRS
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
     * @param p the Person whose childrn we want to find
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

}
