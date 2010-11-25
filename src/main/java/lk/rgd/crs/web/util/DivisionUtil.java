package lk.rgd.crs.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;

import java.util.Map;

/**
 * @author Mahesha
 * populate basic and dynamic lists related to divisions such as 
 * districts/ ds divisions/ birth divisions/ mr divisions.
 */
public class DivisionUtil {

    private static final Logger logger = LoggerFactory.getLogger(DivisionUtil.class);

    private static DistrictDAO districtDAO;
    private static DSDivisionDAO dsDivisionDAO;
    private static BDDivisionDAO bdDivisionDAO;
    private static MRDivisionDAO mrDivisionDAO;
    private static DivisionUtil divisionUtil = new DivisionUtil();

    private static DivisionUtil createInstance(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO) {
        DivisionUtil.districtDAO = districtDAO;
        DivisionUtil.dsDivisionDAO = dsDivisionDAO;
        return divisionUtil;
    }

    public static DivisionUtil createBirthInstance(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO) {
        DivisionUtil.bdDivisionDAO = bdDivisionDAO;
        return DivisionUtil.createInstance(districtDAO, dsDivisionDAO);
    }

    public static DivisionUtil createMarriageInstance(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, MRDivisionDAO mrDivisionDAO) {
        DivisionUtil.mrDivisionDAO = mrDivisionDAO;
        return DivisionUtil.createInstance(districtDAO, dsDivisionDAO);
    }

    public static void populateDynamicLists(Map<Integer, String> districtList, Map<Integer, String> dsDivisionList, Map<Integer, String> divisionList, Integer districtId, Integer dsDivisionId, Integer divisionId, String divisionType, User user, String language) {

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

    public static int findDefaultListValue(Map<Integer, String> divisionList, int divisionId){
        if (divisionId == 0) {
            if (!divisionList.isEmpty()) {
                divisionId = divisionList.keySet().iterator().next();
                logger.debug("first Division in the list {} was set", divisionId);
            }
        }
        return divisionId;
    }

    public static int findDivisionList(Map<Integer, String> divisionList, int divisionId, int parentId, String divisionType, User user, String language){
        if (divisionType.equals("DSDivision")) {
            divisionList.putAll(dsDivisionDAO.getDSDivisionNames(parentId, language, user));
        } else if (divisionType.equals("BDDivision")) {
            divisionList.putAll(bdDivisionDAO.getBDDivisionNames(parentId, language, user));
        } else if (divisionType.equals("MRDivision")){
           divisionList.putAll(mrDivisionDAO.getMRDivisionNames(parentId, language, user));
        }
        return findDefaultListValue(divisionList,divisionId);
    }


}
