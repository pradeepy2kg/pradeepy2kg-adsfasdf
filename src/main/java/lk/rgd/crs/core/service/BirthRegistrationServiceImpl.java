package lk.rgd.crs.core.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BirthRegistrationServiceImpl implements BirthRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegistrationServiceImpl.class);
    private final BirthDeclarationDAO birthDeclarationDAO;

    public BirthRegistrationServiceImpl(BirthDeclarationDAO dao) {
        birthDeclarationDAO = dao;
    }

    public List<UserWarning> addNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        validateAccessOfUser(user, bdf);
        birthDeclarationDAO.addBirthDeclaration(bdf);
        return Collections.emptyList();
    }

    public List<UserWarning> approveAllBirthDeclaration(long[] approvalDataList, boolean ignoreWarnings, User user) {
        //todo has to be implemented
        /*validateAccessOfUser(user, bdf);
        birthDeclarationDAO.addBirthDeclaration(bdf);*/
        return Collections.emptyList();
    }

    public List<UserWarning> updateNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        validateAccessOfUser(user, bdf);
        // a BDF can be edited by a DEO or ADR only before being approved
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        birthDeclarationDAO.updateBirthDeclaration(bdf);
        return Collections.emptyList();
    }

    public List<UserWarning> deleteNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        validateAccessOfUser(user, bdf);
        birthDeclarationDAO.deleteBirthDeclaration(bdf.getIdUKey());
        return Collections.emptyList();
    }

    private void validateAccessOfUser(User user, BirthDeclaration bdf) {
        BDDivision bdDivision = bdf.getChild().getBirthDivision();
        if (user.isAllowedAccessToDistrict(bdDivision.getDistrict().getDistrictId()) &&
            user.isAllowedAccessToDSDivision(bdDivision.getDsDivision().getDivisionId())) {

        } else {
            handleException("User : " + user.getUserId() + " is not allowed access to the District : " +
                bdDivision.getDistrict().getDistrictId() + " and/or DS Division : " +
                bdDivision.getDsDivision().getDivisionId());
        }
    }

    private List<UserWarning> handleException(String message) {
        logger.warn(message);
        List<UserWarning> warnings = new ArrayList<UserWarning>();
        warnings.add(new UserWarning(message));
        return warnings;
    }

    /**
     * Returns the Birth Declaration object for a given Id
     *
     * @param bdId Birth Declarion Id for the given declaration
     * @Return BirthDeclaration
     */
    public BirthDeclaration getById(long bdId) {
        return birthDeclarationDAO.getById(bdId);
    }

    /**
     * Returns the Birth Declaration object for a given bdf serialNo
     *
     * @param serialNo bdfSerialNo given to the Birth Declarion
     * @Return BirthDeclaration
     */
    public BirthDeclaration getBySerialNo(String serialNo) {
        return birthDeclarationDAO.getBySerialNo(serialNo);
    }
}
