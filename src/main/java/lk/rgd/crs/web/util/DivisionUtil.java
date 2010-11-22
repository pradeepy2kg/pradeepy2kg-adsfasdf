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
 * User: indunil
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

        if (districtList == null) {
            districtList = districtDAO.getDistrictNames(language, user);
        }
        if (districtId == 0) {
            if (!districtList.isEmpty()) {
                districtId = districtList.keySet().iterator().next();
                logger.debug("first district in the list {} was set", districtId);
            }
        }
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(districtId, language, user);
        if (dsDivisionId == 0) {
            if (!dsDivisionList.isEmpty()) {
                dsDivisionId = dsDivisionList.keySet().iterator().next();
                logger.debug("first DS Div in the list {} was set", dsDivisionId);
            }
        }
        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        if (bdDivisionId == 0) {
            if (!bdDivisionList.isEmpty()) {
                bdDivisionId = bdDivisionList.keySet().iterator().next();
                logger.debug("first BD Div in the list {} was set", bdDivisionId);
            }
        }
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

}
