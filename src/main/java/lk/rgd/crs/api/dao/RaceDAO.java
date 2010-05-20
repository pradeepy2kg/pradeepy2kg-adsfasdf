package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.Race;

import java.util.List;

/**
 * @author asankha
 */
public interface RaceDAO {

    /**
     * Returns the list of Races for the given language
     * @param language the language ID (see AppConstants)
     * @return the list of known races for the given language
     */
    public List<Race> getRaces(String language);
}