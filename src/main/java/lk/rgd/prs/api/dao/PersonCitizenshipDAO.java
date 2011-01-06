package lk.rgd.prs.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.prs.api.domain.PersonCitizenship;

import java.util.List;

/**
 * @author Chathuranga Withana
 */
public interface PersonCitizenshipDAO {

    /**
     * Get the PersonCitizenship entry by PK
     *
     * @param personUKey the person unique id
     * @param countryId  the country unique id
     * @param passportNo the passport number of the country
     * @return the specific PersonCitizenship or null
     */
    public PersonCitizenship getPersonCitizenship(long personUKey, int countryId, String passportNo);

    /**
     * Add a PersonCitizenship and create relationship to an already existing Person and Country
     *
     * @param citizenship the PersonCitizenship to be added
     * @param user        the user performing the action
     */
    public void addCitizenship(PersonCitizenship citizenship, User user);

    /**
     * Update any of the PersonCitizenship properties except the relationship already made against Person and Country
     *
     * @param citizenship the PersonCitizenship to be updated
     * @param user        the user performing the action
     */
    public void updateCitizenship(PersonCitizenship citizenship, User user);

    /**
     * Remove a PersonCitizenship
     *
     * @param personUKey the person unique id
     * @param countryId  the country unique id
     * @param passportNo the passport number for the country
     */
    public void deleteCitizenship(long personUKey, int countryId, String passportNo);

    /**
     * Return all citizenship list of specific person
     *
     * @param personUKey the person unique id
     * @return filtered citizenship list for specified person
     */
    public List<PersonCitizenship> getCitizenshipsByPersonId(long personUKey);
}
