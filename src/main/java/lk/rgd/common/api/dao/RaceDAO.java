package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.Race;

import java.util.Map;

/**
 * @author asankha
 */
public interface RaceDAO {

    /**
     * Returns the list of Races for the given language
     *
     * @param language the language ID (see AppConstants)
     * @return a Map of known races for the given language along with the ID
     */
    public Map<Integer, String> getRaces(String language);

    /**
     * Returns the Race for given Id
     *
     * @param id the race ID
     * @return a Race
     */
    public Race getRace(int id);
}