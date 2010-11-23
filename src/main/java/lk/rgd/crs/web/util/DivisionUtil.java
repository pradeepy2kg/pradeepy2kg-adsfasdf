package lk.rgd.crs.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BDDivisionDAO;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Mahesha
 * Date: Nov 22, 2010
 * Time: 11:33:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class DivisionUtil {

    private static final Logger logger = LoggerFactory.getLogger(GenderUtil.class);
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final BDDivisionDAO bdDivisionDAO;

    public DivisionUtil(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
    }

    public void populateDynamicLists(Map<Integer, String> districtList, Map<Integer, String> dsDivisionList, Map<Integer, String> bdDivisionList, Integer districtId, Integer dsDivisionId, Integer bdDivisionId, User user, String language) {

        districtList = districtDAO.getDistrictNames(language, user);
        districtId = findDefaultListValue(districtList, districtId);

        dsDivisionList = dsDivisionDAO.getDSDivisionNames(districtId, language, user);
        dsDivisionId = findDefaultListValue(dsDivisionList, dsDivisionId);

        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        bdDivisionId = findDefaultListValue(bdDivisionList, bdDivisionId);

    }

    public int findDefaultListValue(Map<Integer, String> divisionList, int divisionId){
        if (divisionId == 0) {
            if (!divisionList.isEmpty()) {
                divisionId = divisionList.keySet().iterator().next();
                logger.debug("first Division in the list {} was set", divisionId);
            }
        }
        return divisionId;
    }

    public int findDSDivisionList(Map<Integer, String> divisionList, int divisionId, int districtId, User user, String language){
        divisionList.putAll(dsDivisionDAO.getDSDivisionNames(districtId, language, user));
        return findDefaultListValue(divisionList,divisionId);
    }

    public int findBDDivisionList(Map<Integer, String> divisionList, int divisionId, int dsDivisionId, User user, String language){
        divisionList.putAll(bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user));
        return findDefaultListValue(divisionList,divisionId);
    }

    public int findDivisionList(Map<Integer, String> divisionList, int divisionId, int parentId, String divisionType, User user, String language){
        if (divisionType.equals("DSDivision")) {
            divisionList.putAll(dsDivisionDAO.getDSDivisionNames(parentId, language, user));
        } else if(divisionType.equals("BDDivision")) {
            divisionList.putAll(bdDivisionDAO.getBDDivisionNames(parentId, language, user));
        }
        return findDefaultListValue(divisionList,divisionId);
    }

}
