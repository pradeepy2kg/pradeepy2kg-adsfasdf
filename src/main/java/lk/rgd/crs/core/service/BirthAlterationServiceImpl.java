package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BirthAlterationDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.crs.api.service.BirthAlterationService;
import lk.rgd.crs.core.ValidationUtils;
import lk.rgd.crs.web.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Handles the birth alteration process
 *
 * @author Indunil Moremada
 * @author Asankha Perera (reviewed and reorganized)
 */
public class BirthAlterationServiceImpl implements BirthAlterationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthAlterationServiceImpl.class);
    private final BirthAlterationDAO birthAlterationDAO;
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final BirthAlterationValidator birthAlterationValidator;

    public BirthAlterationServiceImpl(BirthAlterationDAO birthAlterationDAO, BirthDeclarationDAO birthDeclarationDAO, BirthAlterationValidator birthAlterationValidator) {
        this.birthAlterationDAO = birthAlterationDAO;
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.birthAlterationValidator = birthAlterationValidator;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBirthAlteration(BirthAlteration ba, User user) {
        logger.debug("Adding new birth alteration record on request of : {}", ba.getDeclarant().getDeclarantFullName());
        ba.setSubissionLocation(user.getPrimaryLocation());
        // any user (DEO, ADR of any DS office or BD division etc) can add a birth alteration request
        birthAlterationDAO.addBirthAlteration(ba, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBirthAlteration(BirthAlteration ba, User user) {
        logger.debug("Attempt to edit birth alteration record : {}", ba.getIdUKey());
        validateAccessOfUserToEditOrDelete(ba, user);

        BirthAlteration existing = birthAlterationDAO.getById(ba.getIdUKey());
        validateAccessOfUserToEditOrDelete(existing, user);
        birthAlterationDAO.updateBirthAlteration(ba, user);
        logger.debug("Saved changes made to birth alteration record : {}  in data entry state", ba.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteBirthAlteration(long idUKey, User user) {
        logger.debug("Attempt to delete birth alteration record : {}", idUKey);
        BirthAlteration existing = birthAlterationDAO.getById(idUKey);
        validateAccessOfUserToEditOrDelete(existing, user);
        birthAlterationDAO.deleteBirthAlteration(idUKey);
        logger.debug("Deleted birth alteration record : {}  in data entry state", idUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthAlteration getByIDUKey(long idUKey, User user) {
        logger.debug("Loading birth alteration record : {}", idUKey);
        BirthAlteration ba = birthAlterationDAO.getById(idUKey);
        validateAccessOfUserToEditOrDelete(ba, user);
        return ba;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void approveBirthAlteration(BirthAlteration ba, Map<Integer, Boolean> fieldsToBeApproved,
        boolean applyChangesToBC, User user) {

        if (applyChangesToBC) {
            logger.debug("Attempt to approve birth alteration record : {} and apply changes to BC", ba.getIdUKey());
        } else {
            logger.debug("Attempt to save intermediate approvals for alteration record : {}", ba.getIdUKey());
        }

        validateAccessOfUserForApproval(ba, user);
        BirthAlteration existing = birthAlterationDAO.getById(ba.getIdUKey());
        validateAccessOfUserForApproval(existing, user);

        for (Map.Entry<Integer, Boolean> e : fieldsToBeApproved.entrySet()) {
            if (Boolean.TRUE.equals(e.getValue())) {
                logger.debug("Setting status as approved for the alteration statement : {}", e.getKey());
                existing.getApprovalStatuses().set(e.getKey(), WebConstants.BIRTH_ALTERATION_APPROVE_TRUE);
            } else {
                logger.debug("Setting status as rejected for the alteration statement : {}", e.getKey());
                existing.getApprovalStatuses().set(e.getKey(), false);
            }
        }

        if (applyChangesToBC) {
            logger.debug("Requesting the application of changes to the BC as final for : {}", existing.getIdUKey());
            existing.setStatus(BirthAlteration.State.FULLY_APPROVED);
        }

        existing.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
        existing.getLifeCycleInfo().setApprovalOrRejectUser(user);
        birthAlterationDAO.updateBirthAlteration(existing, user);

        logger.debug("Updated birth alteration : {}", existing.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getApprovalPendingByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alteration pending approval by DSDivision ID : " + dsDivision.getDsDivisionUKey()
                + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        ValidationUtils.validateAccessToDSDivison(dsDivision, user);
        return birthAlterationDAO.getBulkOfAlterationByDSDivision(dsDivision, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getApprovalPendingByBDDivision
        (BDDivision bdDivision, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alteration pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey()
                + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
        return birthAlterationDAO.getBulkOfAlterationByBDDivision(bdDivision, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getApprovalPendingByBDDivisionAndBDFSerialNo(
        BDDivision bdDivision, Long birthSerialNumber, int pageNo, int noOfRows, User user) {

        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alterations pending approval - by Birth Division : " + bdDivision.getEnDivisionName() +
                " and BDF serial : " + birthSerialNumber + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        return birthAlterationDAO.getBulkOfAlterationByBDDivisionAndBirthSerialNo(bdDivision, birthSerialNumber, pageNo, noOfRows);
    }

    /**
     * Checks if the user can edit or delete a birth alteration entry before approval by an ARG
     * <p/>
     * A DEO or ADR at the same "SubissionLocation" can edit or delete an entry at data entry stage. Any other who has
     * access to the BD division of the corresponding BDF has access to edit or delete
     *
     * @param ba   the birth alteration entry
     * @param user the user attempting to update or delete
     */
    private void validateAccessOfUserToEditOrDelete(BirthAlteration ba, User user) {
        if (Role.ROLE_DEO.equals(user.getRole()) || Role.ROLE_ADR.equals(user.getRole())) {
            if (ba.getSubissionLocation().equals(user.getPrimaryLocation())) {
                return;
            }
        } else if (!Role.ROLE_ADMIN.equals(user.getRole())) {
            return;
        }

        ValidationUtils.validateAccessToBDDivision(user,
            birthDeclarationDAO.getById(ba.getBdfIDUKey()).getRegister().getBirthDivision());
    }

    /**
     * Checks if the user can approve the birth alteration entry. Only an ARG level user, in charge of the BDF under
     * consideration can approve an alteration
     *
     * @param ba   the birth alteration entry
     * @param user the user attempting to approve
     */
    private void validateAccessOfUserForApproval(BirthAlteration ba, User user) {
        if (Role.ROLE_RG.equals(user.getRole())) {
            // RG can approve any record
            return;
        } else if (Role.ROLE_ARG.equals(user.getRole())) {
            ValidationUtils.validateAccessToBDDivision(user,
                birthDeclarationDAO.getById(ba.getBdfIDUKey()).getRegister().getBirthDivision());

            if (!user.isAuthorized(Permission.APPROVE_BIRTH_ALTERATION)) {
                handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth alteration",
                    ErrorCodes.PERMISSION_DENIED);
            }
            return;
        } else {
            handleException("User : " + user.getUserId() + " is not an ARG for alteration approval",
                ErrorCodes.PERMISSION_DENIED);
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
