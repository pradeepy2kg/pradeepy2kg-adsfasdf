package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.BirthAlterationService;
import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.dao.BirthAlterationDAO;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.ErrorCodes;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The central service managing the CRS Birth Alteration process
 *
 * @author Indunil Moremada
 */
public class BirthAlterationServiceImpl implements BirthAlterationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthAlterationServiceImpl.class);
    private final BirthAlterationDAO birthAlterationDAO;

    public BirthAlterationServiceImpl(BirthAlterationDAO birthAlterationDAO) {
        this.birthAlterationDAO = birthAlterationDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBirthAlteration(BirthAlteration ba, User user) {
        birthAlterationDAO.addBirthAlteration(ba,user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBirthAlteration(BirthAlteration ba, User user) {
        //todo validations
        birthAlterationDAO.updateBirthAlteration(ba,user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteBirthAlteration(long idUKey) {
        //todo validations
        birthAlterationDAO.deleteBirthAlteration(idUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthAlteration getById(long idUKey) {
        //todo validations
        return birthAlterationDAO.getById(idUKey);  
    }

    private void validateAccessOfUser(User user, BirthAlteration ba) {
        if (ba != null) {
            BDDivision bdDivision = ba.getAlt52_1().getBirthDivision();
            validateAccessToBDDivision(user, bdDivision);
        }
    }

    private void validateAccessToBDDivision(User user, BDDivision bdDivision) {
        if (!(User.State.ACTIVE == user.getStatus()
            &&
            (Role.ROLE_ARG.equals(user.getRole().getRoleId())
                ||
                (user.isAllowedAccessToBDDistrict(bdDivision.getDistrict().getDistrictUKey())
                    &&
                    user.isAllowedAccessToBDDSDivision(bdDivision.getDsDivision().getDsDivisionUKey())
                )
            )
        )) {

            handleException("User : " + user.getUserId() + " is not allowed access to the District : " +
                bdDivision.getDistrict().getDistrictId() + " and/or DS Division : " +
                bdDivision.getDsDivision().getDivisionId(), ErrorCodes.PERMISSION_DENIED);
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
