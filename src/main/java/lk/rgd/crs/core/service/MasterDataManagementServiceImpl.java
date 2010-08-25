package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.service.MasterDataManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Performs management of master data
 *
 * @author asankha
 */
public class MasterDataManagementServiceImpl implements MasterDataManagementService {

    private static final Logger logger = LoggerFactory.getLogger(MasterDataManagementServiceImpl.class);

    private final BDDivisionDAO bdDivisionDAO;
    private final MRDivisionDAO mrDivisionDAO;

    public MasterDataManagementServiceImpl(BDDivisionDAO bdDivisionDAO, MRDivisionDAO mrDivisionDAO) {
        this.bdDivisionDAO = bdDivisionDAO;
        this.mrDivisionDAO = mrDivisionDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBDDivision(BDDivision bdDivision, User user) {

        if (isEmptyString(bdDivision.getEnDivisionName()) ||
            isEmptyString(bdDivision.getEnDivisionName()) ||
            isEmptyString(bdDivision.getEnDivisionName())) {
            throw new CRSRuntimeException(
                "One or more names of the BD Division is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }

        if (user.isAuthorized(Permission.ADD_EDIT_DIVISIONS)) {
            try {
                bdDivisionDAO.add(bdDivision, user);
            } catch (Exception e) {
                logger.error("Attempt to add BD Division : " + bdDivision.getEnDivisionName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                " was not allowed to add a new BD Division : " + bdDivision.getEnDivisionName());
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void inactivateBDDivision(BDDivision bdDivision, User user) {
        updateBDActivation(bdDivision.getBdDivisionUKey(), false, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void activateBDDivision(BDDivision bdDivision, User user) {
        updateBDActivation(bdDivision.getBdDivisionUKey(), true, user);
    }

    private void updateBDActivation(int bdDivisionUKey, boolean activate, User user) {

        if (user.isAuthorized(Permission.ADD_EDIT_DIVISIONS)) {
            try {
                BDDivision existing = bdDivisionDAO.getBDDivisionByPK(bdDivisionUKey);
                if (existing != null) {
                    existing.setActive(activate);
                    bdDivisionDAO.update(existing, user);
                    logger.info("BD Division : {} inactivated by : {}", existing.getEnDivisionName(), user.getUserId());
                }
            } catch (Exception e) {
                logger.error("Attempt to inactivate BD Division with key : " + bdDivisionUKey + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                " was not allowed to activate/inactivate BD Division with key : " + bdDivisionUKey);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addMRDivision(MRDivision mrDivision, User user) {

        if (isEmptyString(mrDivision.getEnDivisionName()) ||
            isEmptyString(mrDivision.getEnDivisionName()) ||
            isEmptyString(mrDivision.getEnDivisionName())) {
            throw new CRSRuntimeException(
                "One or more names of the MR Division is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }

        if (user.isAuthorized(Permission.ADD_EDIT_DIVISIONS)) {
            try {
                mrDivisionDAO.add(mrDivision, user);
            } catch (Exception e) {
                logger.error("Attempt to add MR Division : " + mrDivision.getEnDivisionName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                " was not allowed to add a new MR Division : " + mrDivision.getEnDivisionName());
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void inactivateMRDivision(MRDivision mrDivision, User user) {
        updateMRActivation(mrDivision.getMrDivisionUKey(), false, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void activateMRDivision(MRDivision mrDivision, User user) {
        updateMRActivation(mrDivision.getMrDivisionUKey(), true, user);
    }

    private void updateMRActivation(int mrDivisionUKey, boolean activate, User user) {

        if (user.isAuthorized(Permission.ADD_EDIT_DIVISIONS)) {
            try {
                MRDivision existing = mrDivisionDAO.getMRDivisionByPK(mrDivisionUKey);
                if (existing != null) {
                    existing.setActive(activate);
                    mrDivisionDAO.update(existing, user);
                    logger.info("MR Division : {} inactivated by : {}", existing.getEnDivisionName(), user.getUserId());
                }
            } catch (Exception e) {
                logger.error("Attempt to inactivate MR Division with key : " + mrDivisionUKey + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                " was not allowed to activate/inactivate MR Division with key : " + mrDivisionUKey);
        }
    }


    private static final boolean isEmptyString(String s) {
        return s == null || s.trim().length() == 0;
    }
}
