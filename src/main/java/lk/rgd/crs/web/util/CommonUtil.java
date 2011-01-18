package lk.rgd.crs.web.util;

import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.LocaleUtil;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.AppConstants;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Mahesha
 *         populate basic and dynamic lists related to divisions
 *         (districts/ ds divisions/ birth divisions/ mr divisions) and languages etc
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
     * populate district list, ds division list of first district in the district list.
     * and division list(mr division or bd division based on the parameter divisionType and the first ds division in the ds division list)
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

        if (divisionType.equals(AppConstants.BIRTH)) {
            divisionList.putAll(bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user));
            findDefaultListValue(divisionList, divisionId);
        } else if (divisionType.equals(AppConstants.MARRIAGE)) {
            divisionList.putAll(mrDivisionDAO.getMRDivisionNames(dsDivisionId, language, user));
            findDefaultListValue(divisionList, divisionId);
        } else {
            logger.debug("Invalid Division Type");
        }
    }

    /**
     * populate All districts , All ds divisions of first district in the district list
     * and bd division list of first ds Division in list
     *
     * @param districtList
     * @param dsDivisionList
     * @param divisionList
     * @param districtId
     * @param dsDivisionId
     * @param divisionId
     * @param user
     * @param language
     */
    public void populateAllDivisions(Map<Integer, String> districtList, Map<Integer, String> dsDivisionList,
        Map<Integer, String> divisionList, Integer districtId, Integer dsDivisionId,
        Integer divisionId, User user, String language) {

        districtList.putAll(districtDAO.getAllDistrictNames(language, user));
        districtId = findDefaultListValue(districtList, districtId);

        dsDivisionList.putAll(dsDivisionDAO.getAllDSDivisionNames(districtId, language, user));
        dsDivisionId = findDefaultListValue(dsDivisionList, dsDivisionId);

        divisionList.putAll(bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user));
        findDefaultListValue(divisionList, divisionId);
    }

    public void populateDynamicListsWithAllOption(Map<Integer, String> districtList, Map<Integer, String> dsDivisionList,
        Map<Integer, String> divisionList, User user, String language) {

        districtList.put(0, LocaleUtil.getLocalizedString(language, AppConstants.ALL));
        districtList.putAll(districtDAO.getDistrictNames(language, user));
        dsDivisionList.put(0, LocaleUtil.getLocalizedString(language, AppConstants.ALL));
        divisionList.put(0, LocaleUtil.getLocalizedString(language, AppConstants.ALL));

    }

    //TODO: to be removed. Write two lines of code in ur action method to populate Country and Race. This is not a good coding practice.
    public void populateCountryAndRaceLists(Map<Integer, String> countryList, Map<Integer, String> raceList,
        String language) {
        logger.debug("generating country list and race lists");
        countryList.putAll(countryDAO.getCountries(language));
        raceList.putAll(raceDAO.getRaces(language));
    }

    /**
     * @param divisionList
     * @param divisionId
     * @return Returns the key of the first item of the division list as default id
     */
    public int findDefaultListValue(Map<Integer, String> divisionList, int divisionId) {
        if (divisionId == 0) {
            if (!divisionList.isEmpty()) {
                divisionId = divisionList.keySet().iterator().next();
                logger.debug("first Division in the list {} was set", divisionId);
            }
        }
        return divisionId;
    }

    /**
     * Find the division list (DS, Birth, Marriage) based on the parameter divisionType
     *
     * @param divisionList
     * @param divisionId
     * @param parentId
     * @param divisionType
     * @param user
     * @param language
     * @return default id for the division list
     */
    public int findDivisionList(Map<Integer, String> divisionList, int divisionId, int parentId, String divisionType, User user, String language) {
        if (AppConstants.DS_DIVISION.equals(divisionType)) {
            divisionList.putAll(dsDivisionDAO.getDSDivisionNames(parentId, language, user));
        } else if (AppConstants.BIRTH.equals(divisionType)) {
            divisionList.putAll(bdDivisionDAO.getBDDivisionNames(parentId, language, user));
        } else if (AppConstants.MARRIAGE.equals(divisionType)) {
            divisionList.putAll(mrDivisionDAO.getMRDivisionNames(parentId, language, user));
        } else {
            logger.debug("Invalid Division Type");
        }
        return findDefaultListValue(divisionList, divisionId);
    }

    /**
     * @return the list of languages which can be preferred Languages
     */
    public Map<String, String> findLanguageList() {
        Map<String, String> languages = new HashMap<String, String>();
        languages.put(AppConstants.SINHALA, LocaleUtil.getLocalizedString(AppConstants.SINHALA, AppConstants.SINHALA));
        languages.put(AppConstants.TAMIL, LocaleUtil.getLocalizedString(AppConstants.TAMIL, AppConstants.TAMIL));
        return languages;
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
