package lk.rgd.crs.web.util;

import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mahesha
 *         populate basic and dynamic lists related to divisions such as
 *         districts/ ds divisions/ birth divisions/ mr divisions.
 */
public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    private final DistrictDAO districtDAO;                                       
    private final DSDivisionDAO dsDivisionDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final MRDivisionDAO mrDivisionDAO;
    private final CountryDAO countryDAO;
    private final RaceDAO raceDAO;
    private final LocationDAO locationDAO;

    public CommonUtil(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO,
        MRDivisionDAO mrDivisionDAO, CountryDAO countryDAO, RaceDAO raceDAO, LocationDAO locationDAO) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.mrDivisionDAO = mrDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.locationDAO = locationDAO;
    }

    /**
     * populate district list/ds division list and division list(mr or bd)
     *
     * @param districtList
     * @param dsDivisionList
     * @param divisionList
     * @param districtId
     * @param dsDivisionId
     * @param divisionId
     * @param divisionType
     * @param user
     * @param language
     */
    public void populateDynamicLists(Map<Integer, String> districtList, Map<Integer, String> dsDivisionList,
        Map<Integer, String> divisionList, Integer districtId, Integer dsDivisionId,
        Integer divisionId, String divisionType, User user, String language) {

        districtList.putAll(districtDAO.getDistrictNames(language, user));
        districtId = findDefaultListValue(districtList, districtId);

        dsDivisionList.putAll(dsDivisionDAO.getDSDivisionNames(districtId, language, user));
        dsDivisionId = findDefaultListValue(dsDivisionList, dsDivisionId);

        if (divisionType.equals("Birth")) {
            divisionList.putAll(bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user));
            findDefaultListValue(divisionList, divisionId);
        } else if (divisionType.equals("Marriage")) {
            divisionList.putAll(mrDivisionDAO.getMRDivisionNames(dsDivisionId, language, user));
            findDefaultListValue(divisionList, divisionId);
        }
    }

    public void populateCountryAndRaceLists(Map<Integer, String> countryList, Map<Integer, String> raceList,
        String language) {
        logger.debug("generating country list and race lists");
        countryList.putAll(countryDAO.getCountries(language));
        raceList.putAll(raceDAO.getRaces(language));
    }


    public int findDefaultListValue(Map<Integer, String> divisionList, int divisionId) {
        if (divisionId == 0) {
            if (!divisionList.isEmpty()) {
                divisionId = divisionList.keySet().iterator().next();
                logger.debug("first Division in the list {} was set", divisionId);
            }
        }
        return divisionId;
    }

    public int findDivisionList(Map<Integer, String> divisionList, int divisionId, int parentId, String divisionType, User user, String language) {
        if (divisionType.equals("DSDivision")) {
            divisionList.putAll(dsDivisionDAO.getDSDivisionNames(parentId, language, user));
        } else if (divisionType.equals("BDDivision")) {
            divisionList.putAll(bdDivisionDAO.getBDDivisionNames(parentId, language, user));
        } else if (divisionType.equals("MRDivision")) {
            divisionList.putAll(mrDivisionDAO.getMRDivisionNames(parentId, language, user));
        }
        return findDefaultListValue(divisionList, divisionId);
    }

    /**
     * Returns list of available active user locations(concatenation of location code and location name) of a specific
     * user in specified language
     *
     * @param user     the specific user
     * @param language the specified language
     * @return a list of active location list
     */
    public Map<Integer, String> populateActiveUserLocations(User user, String language) {
        Map<Integer, String> al = new LinkedHashMap<Integer, String>();
        List<Location> list = user.getActiveLocations();

        for (Location l : list) {
            al.put(l.getLocationUKey(), locationDAO.getLocationNameByPK(l.getLocationUKey(), language));
        }
        return al;
    }

}
