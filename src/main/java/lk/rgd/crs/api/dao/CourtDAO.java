package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.Court;

import java.util.List;
import java.util.Map;

/**
 * @author asankha
 */
public interface CourtDAO {

    /**
     * Returns the list of Courts for the given language
     *
     * @param language the language ID (see AppConstants)
     * @return a Map of known courts for the given language along with the ID
     */
    public Map<Integer, String> getCourtNames(String language);

    /**
     * Return the name of the court in the selected language
     *
     * @param courtUKey the court unique key
     * @param language  the selected language
     * @return the name of the court in the selected language
     */
    public String getNameByPK(int courtUKey, String language);

    /**
     * Return Court by id
     *
     * @param id the Court ID
     * @return the Court
     */
    public Court getCourt(int id);

    /**
     * Update a court
     *
     * @param court the court updated
     * @param user  user performing the action
     */
    public void update(Court court, User user);

    /**
     * Add a court
     *
     * @param court the court added
     * @param user  user performing the action
     */
    public void add(Court court, User user);

    /**
     * Return all Courts in the system
     *
     * @return all Courts
     */
    public List<Court> findAll();

    /**
     * @param courtId
     * @return the Court
     */
    public Court getCourtByCode(int courtId);
}
