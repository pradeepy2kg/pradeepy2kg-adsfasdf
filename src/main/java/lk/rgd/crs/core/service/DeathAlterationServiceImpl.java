package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.DeathAlterationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.domain.DeathAlteration;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.dao.DeathAlterationDAO;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.core.ValidationUtils;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.*;

/**
 * @author amith jayasekara
 */
public class DeathAlterationServiceImpl implements DeathAlterationService {

    private static final Logger logger = LoggerFactory.getLogger(DeathAlterationServiceImpl.class);
    private final DeathAlterationDAO deathAlterationDAO;
    private final DeathRegistrationService deathRegistrationService;
    private final DeathAlterationValidator deathAlterationValidator;

    public DeathAlterationServiceImpl(DeathAlterationDAO deathAlterationDAO, DeathRegistrationService deathRegistrationService, DeathAlterationValidator deathAlterationValidator) {
        this.deathAlterationDAO = deathAlterationDAO;
        this.deathRegistrationService = deathRegistrationService;
        this.deathAlterationValidator = deathAlterationValidator;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addDeathAlteration(DeathAlteration da, User user) {
        logger.debug("adding a new death alteration");
        da.setSubmittedLocation(user.getPrimaryLocation());
        if (da != null)
            deathAlterationValidator.validateMinimumConditions(da);
        deathAlterationDAO.addDeathAlteration(da, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeathAlteration(DeathAlteration da, User user) {
        logger.debug("updatign death alteration : idUKey : {}", da.getIdUKey());
        deathAlterationDAO.updateDeathAlteration(da, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDeathAlteration(long idUKey, User user) {
        logger.debug("about to remove alteration recode idUkey : {}", idUKey);
        validateAccessOfUserToEditOrDelete(getById(idUKey, user), user);
        deathAlterationDAO.deleteDeathAlteration(idUKey);
    }

    /**
     * @inheritDoc
     */
    public void rejectDeathAlteration(long idUKey, User user, String comment) {
        DeathAlteration exsisting = getById(idUKey, user);
        exsisting.setStatus(DeathAlteration.State.REJECT);
        exsisting.setComments(comment);
        validateAccessOfUserToEditOrDelete(exsisting, user);
        deathAlterationDAO.rejectDeathAlteration(exsisting, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public DeathAlteration getById(long idUKey, User user) {
        DeathAlteration da = deathAlterationDAO.getById(idUKey);
        return da;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<DeathAlteration> getAlterationByDeathCertificateNumber(long idUKey, User user) {
        return deathAlterationDAO.getByCertificateNumber(idUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<DeathAlteration> getAlterationApprovalListByDeathDivision(int pageNo, int numRows, int divisionId, User user) {
        return deathAlterationDAO.getPaginatedAlterationApprovalListByDeathDivision(pageNo, numRows, divisionId);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<DeathAlteration> getAlterationByDeathId(long deathId, User user) {
        return deathAlterationDAO.getAlterationByDeathId(deathId);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void approveDeathAlteration(long deathAlterationUkey, Hashtable<Integer, Boolean> fieldsToBeApproved, boolean appStatus, User user) {
        //no need to validate because only approval is allowed to ARG so he has permission to all divisions
        //getting alteration to approve
        DeathAlteration da = getById(deathAlterationUkey, user);
        //setting bit set
        BitSet approvalBitSet = new BitSet(WebConstants.DEATH_ALTERATION_APPROVE);
        Enumeration<Integer> fieldList = fieldsToBeApproved.keys();
        while (fieldList.hasMoreElements()) {
            Integer aKey = fieldList.nextElement();
            if (fieldsToBeApproved.get(aKey) == true) {
                approvalBitSet.set(aKey, true);
            }
        }
        //merge  with exsisting
        BitSet ex = da.getApprovalStatuses();
        if (ex != null) {
            ex.or(approvalBitSet);
            da.setApprovalStatuses(ex);
        } else {
            da.setApprovalStatuses(approvalBitSet);
        }
        //true means fully
        if (appStatus) {
            da.setStatus(DeathAlteration.State.FULLY_APPROVED);
        } else {
            da.setStatus(DeathAlteration.State.PARTIALY_APPROVED);
        }
        deathAlterationDAO.updateDeathAlteration(da, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public void loadValuesToDeathAlterationObject(DeathAlteration da) {
        DeathRegister dr = deathRegistrationService.getById(da.getDeathId());
        da.setDeathPersonName(dr.getDeathPerson().getDeathPersonNameOfficialLang());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<DeathAlteration> getDeathAlterationByUserLocation(int locationUKey, User user) {
        List<DeathAlteration> result = deathAlterationDAO.getDeathAlterationByUserLocation(locationUKey);
        Iterator itr = result.iterator();
        while (itr.hasNext()) {
            DeathAlteration da = (DeathAlteration) itr.next();
            loadValuesToDeathAlterationObject(da);
        }
        return result;
    }

    /**
     * @inheritDoc
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public List<DeathAlteration> getAlterationByDeathPersonPin(String pin, User user) {
        List<DeathAlteration> result = deathAlterationDAO.getDeathAlterationByDeathPersonPin(pin);
        Iterator itr = result.iterator();
        while (itr.hasNext()) {
            DeathAlteration da = (DeathAlteration) itr.next();
            loadValuesToDeathAlterationObject(da);
            validateAccessOfUserForApproval(da, user);
        }
        return result;
    }

    /**
     * Checks if the user can edit or delete a death alteration entry before approval by an ARG
     * <p/>
     * A DEO or ADR at the same "SubissionLocation" can edit or delete an entry at data entry stage. Any other who has
     * access to the BD division of the corresponding BDF has access to edit or delete
     *
     * @param da   the death alteration entry
     * @param user the user attempting to update or delete
     */
    private void validateAccessOfUserToEditOrDelete(DeathAlteration da, User user) {
        if (Role.ROLE_DEO.equals(user.getRole().getRoleId()) || Role.ROLE_ADR.equals(user.getRole().getRoleId())) {
            if (da.getSubmittedLocation().equals(user.getPrimaryLocation())) {
                return;
            }
        } else if (!Role.ROLE_ADMIN.equals(user.getRole().getRoleId())) {
            return;
        }

        ValidationUtils.validateAccessToBDDivision(user, deathAlterationDAO.getById(da.getDeathId()).getDeathRecodDivision());
    }

    /**
     * Checks if the user can approve the death alteration entry. Only an ARG level user, in charge of the BDF under
     * consideration can approve an alteration
     *
     * @param da   the death alteration entry
     * @param user the user attempting to approve
     */
    private void validateAccessOfUserForApproval(DeathAlteration da, User user) {
        if (Role.ROLE_RG.equals(user.getRole().getRoleId())) {
            // RG can approve any record
        } else if (Role.ROLE_ARG.equals(user.getRole().getRoleId())) {
            ValidationUtils.validateAccessToBDDivision(user, deathAlterationDAO.getById(da.getDeathId()).getDeathRecodDivision());
            if (!user.isAuthorized(Permission.APPROVE_BIRTH_ALTERATION)) {
                handleException("User : " + user.getUserId() + " is not allowed to approve/reject death alteration",
                        ErrorCodes.PERMISSION_DENIED);
            }
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
