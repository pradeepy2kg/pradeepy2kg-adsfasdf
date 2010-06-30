package lk.rgd.prs.api.dao;

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
     * Return the list of Person objects for the given National ID card number
     *
     * @param nic the national ID card number
     * @return the matching persons
     */
    public List<Person> findPersonsByNIC(String nic);
}
