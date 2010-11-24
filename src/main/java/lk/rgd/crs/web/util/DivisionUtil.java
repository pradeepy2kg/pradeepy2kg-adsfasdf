package lk.rgd.crs.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BDDivisionDAO;

import java.util.Map;

/**
 * @author Mahesha
 */
public class DivisionUtil {

    private static final Logger logger = LoggerFactory.getLogger(DivisionUtil.class);

    private static DistrictDAO districtDAO;
    private static DSDivisionDAO dsDivisionDAO;
    private static BDDivisionDAO bdDivisionDAO;

    public void populateDynamicLists(Map<Integer, String> districtList, Map<Integer, String> dsDivisionList, Map<Integer, String> bdDivisionList, Integer districtId, Integer dsDivisionId, Integer bdDivisionId, User user, String language) {

        districtList = districtDAO.getDistrictNames(language, user);
        districtId = findDefaultListValue(districtList, districtId);

        dsDivisionList = dsDivisionDAO.getDSDivisionNames(districtId, language, user);
        dsDivisionId = findDefaultListValue(dsDivisionList, dsDivisionId);

        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        bdDivisionId = findDefaultListValue(bdDivisionList, bdDivisionId);

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
        } else if(divisionType.equals("BDDivision")) {
            divisionList.putAll(bdDivisionDAO.getBDDivisionNames(parentId, language, user));
        }
        return findDefaultListValue(divisionList,divisionId);
    }

    public DistrictDAO getDistrictDAO() {
        return districtDAO;
    }

    public void setDistrictDAO(DistrictDAO districtDAO) {
        this.districtDAO = districtDAO;
    }

    public DSDivisionDAO getDsDivisionDAO() {
        return dsDivisionDAO;
    }

    public void setDsDivisionDAO(DSDivisionDAO dsDivisionDAO) {
        this.dsDivisionDAO = dsDivisionDAO;
    }

    public BDDivisionDAO getBdDivisionDAO() {
        return bdDivisionDAO;
    }

    public void setBdDivisionDAO(BDDivisionDAO bdDivisionDAO) {
        this.bdDivisionDAO = bdDivisionDAO;
    }

}
