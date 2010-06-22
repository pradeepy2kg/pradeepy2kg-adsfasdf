package lk.rgd.prs.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.prs.api.domain.Person;

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
     */
    public void addPerson(Person person, User user);

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
    public List<Person> findPersonsByNIC(long nic, User user);

}
