package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.GNDivision;

import java.util.Map;

/**
 * @author asankha
 */
public interface GNDivisionDAO {

    /**
     * Returns the list of GNDivision names for the given language with the ID
     *
     * @param districtId the District ID
     * @param dsDivisionId the DS Division ID
     * @param language the language ID (see AppConstants)
     * @return a Map of known G.N. Division names for the given language along with the ID
     */
    public Map<Integer, String> getGNDivisionNames(int districtId, int dsDivisionId, String language);

    /**
     * Return GNDivision by id
     * @param gnDivisionUKey the unique internal PK of a GN Division
     * @return the GNDivision
     */
    public GNDivision getGNDivisionByPK(int gnDivisionUKey);
}