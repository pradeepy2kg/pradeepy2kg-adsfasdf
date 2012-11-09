package lk.rgd.crs.core.service;

import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.DeathAlterationInfo;
import lk.rgd.crs.api.domain.DeathPersonInfo;
import lk.rgd.crs.api.service.DeathAlterationService;
import lk.rgd.crs.api.domain.DeathAlteration;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.dao.DeathAlterationDAO;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.core.ValidationUtils;
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
 * @author asankha reviewed and enhanced
 */
public class DeathAlterationServiceImpl implements DeathAlterationService {

    private static final Logger logger = LoggerFactory.getLogger(DeathAlterationServiceImpl.class);
    private final DeathAlterationDAO deathAlterationDAO;
    private final DeathRegisterDAO deathRegisterDAO;

    public DeathAlterationServiceImpl(DeathAlterationDAO deathAlterationDAO, DeathRegisterDAO deathRegisterDAO) {
        this.deathAlterationDAO = deathAlterationDAO;
        this.deathRegisterDAO = deathRegisterDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addDeathAlteration(DeathAlteration da, User user) {
        logger.debug("Adding new death alteration record on request of : {}", da.getDeclarant().getDeclarantFullName());
        DeathAlterationValidator.validateMinimumConditions(da);
        da.setSubmittedLocation(user.getPrimaryLocation());
        // any user (DEO, ADR of any DS office or BD division etc) can add a birth alteration request
        deathAlterationDAO.addDeathAlteration(da, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeathAlteration(DeathAlteration da, User user) {
        logger.debug("Attempt to edit death alteration record : {}", da.getIdUKey());
        validateAccessOfUserToEditOrDelete(da, user);

        DeathAlteration existing = deathAlterationDAO.getById(da.getIdUKey());
        validateAccessOfUserToEditOrDelete(existing, user);
        deathAlterationDAO.updateDeathAlteration(da, user);
        logger.debug("Saved changes made to death alteration record : {}  in data entry state", da.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDeathAlteration(long idUKey, User user) {
        logger.debug("Attempt to delete death alteration record : {}", idUKey);
        DeathAlteration existing = deathAlterationDAO.getById(idUKey);
        validateAccessOfUserToEditOrDelete(existing, user);
        deathAlterationDAO.deleteDeathAlteration(idUKey);
        logger.debug("Deleted death alteration record : {}  in data entry state", idUKey);
    }

    /**
     * @inheritDoc
     */
    public void rejectDeathAlteration(long idUKey, User user, String comment) {
        //todo check permissions
        DeathAlteration existing = getByIDUKey(idUKey, user);
        validateAccessOfUserForApproval(existing, user);
        existing.setStatus(DeathAlteration.State.REJECT);
        existing.setComments(comment);
        existing.getLifeCycleInfo().setActiveRecord(false);
        existing.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
        existing.getLifeCycleInfo().setApprovalOrRejectUser(user);
        deathAlterationDAO.updateDeathAlteration(existing, user);
        logger.debug("Rejected death alteration record : {}", idUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathAlteration getByIDUKey(long idUKey, User user) {
        logger.debug("Loading death alteration record : {}", idUKey);
        DeathAlteration da = deathAlterationDAO.getById(idUKey);
        validateAccessOfUserToGet(da, user);
        return da;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathAlteration> getAlterationByDeathCertificateNumber(long idUKey, User user) {
        return deathAlterationDAO.getByCertificateNumber(idUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathAlteration> getAlterationApprovalListByDeathDivision(int pageNo, int numRows, int divisionId, User user) {
        List<DeathAlteration> result = deathAlterationDAO.getPaginatedAlterationApprovalListByDeathDivision(pageNo, numRows, divisionId);
        for (DeathAlteration da : result) {
            loadValuesToDeathAlterationObject(da);
        }
        return result;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathAlteration> getAlterationApprovalListByDeathDSDivision(int pageNo, int numRows, int dsDivisionId, User user) {
        logger.debug("attempt to get death alteration approval list by DSDivision idUKey : {} and pageNo :{}", dsDivisionId, pageNo);
        List<DeathAlteration> result = deathAlterationDAO.getPaginatedAlterationApprovalListByDeathDSDivision(pageNo, numRows, dsDivisionId);
        for (DeathAlteration da : result) {
            loadValuesToDeathAlterationObject(da);
        }
        return Collections.emptyList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathAlteration> getAlterationByDeathId(long deathId, User user) {
        return deathAlterationDAO.getAlterationByDeathId(deathId);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void approveDeathAlteration(DeathAlteration da, Map<Integer, Boolean> fieldsToBeApproved, User user) {
        //todo check permissions
        logger.debug("Attempt to approve death alteration record : {} and apply changes to DC", da.getIdUKey());
        validateAccessOfUserForApproval(da, user);
        DeathAlteration existing = deathAlterationDAO.getById(da.getIdUKey());
        validateAccessOfUserForApproval(existing, user);

        for (Map.Entry<Integer, Boolean> e : fieldsToBeApproved.entrySet()) {
            if (Boolean.TRUE.equals(e.getValue())) {
                logger.debug("Setting status as approved for the alteration statement : {}", e.getKey());
                existing.getApprovalStatuses().set(e.getKey(), true);
            } else {
                logger.debug("Setting status as rejected for the alteration statement : {}", e.getKey());
                existing.getApprovalStatuses().set(e.getKey(), false);
            }
        }
        logger.debug("Requesting the application of changes to the DC as final for : {}", existing.getIdUKey());
        existing.setStatus(DeathAlteration.State.FULLY_APPROVED);

        // We've saved the alteration record, now lets modify the birth record
        DeathRegister deathRegister = deathRegisterDAO.getById(existing.getDeathRegisterIDUkey());
        switch (existing.getType()) {

            case TYPE_53:
            case TYPE_52_1_H:
            case TYPE_52_1_I: {
                logger.debug("Alteration is an amendment, inclusion of omission or correction. Type : {}",
                    existing.getType().ordinal());
                deathRegister.setStatus(DeathRegister.State.ARCHIVED_ALTERED);
                deathRegister.getLifeCycleInfo().setActiveRecord(false);      // mark old record as a non-active record
                deathRegisterDAO.updateDeathRegistration(deathRegister, user);

                // create the new entry as a clone from the existing
                DeathRegister newDR = null;
                try {
                    newDR = deathRegister.clone();
                } catch (CloneNotSupportedException e) {
                    handleException("Unable to clone DR : " + deathRegister.getIdUKey(), ErrorCodes.ILLEGAL_STATE);
                }
                newDR.setStatus(DeathRegister.State.ARCHIVED_CERT_GENERATED);
                applyChanges(existing, newDR, user);
                deathRegisterDAO.addDeathRegistration(newDR, user);
                break;
            }

            case TYPE_52_1_A:
            case TYPE_52_1_B:
            case TYPE_52_1_D:
            case TYPE_52_1_E: {
                deathRegister.setStatus(DeathRegister.State.ARCHIVED_CANCELLED);
                deathRegisterDAO.updateDeathRegistration(deathRegister, user);
                logger.debug("Alteration of type : {} is a cancellation of the existing record : {}",
                    existing.getType().ordinal(), deathRegister.getIdUKey());
                break;
            }
        }

        existing.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
        existing.getLifeCycleInfo().setApprovalOrRejectUser(user);
        deathAlterationDAO.updateDeathAlteration(existing, user);
        logger.debug("Updated death alteration : {}", existing.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    // TODO amith - review if this list needs to be paginated, if so implement the necessary logic
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathAlteration> getDeathAlterationByUserLocation(int locationUKey, User user) {
        List<DeathAlteration> result = deathAlterationDAO.getDeathAlterationByUserLocation(locationUKey);
        for (DeathAlteration da : result) {
            loadValuesToDeathAlterationObject(da);
        }
        return result;
    }

    /**
     * @inheritDoc
     */

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathAlteration> getAlterationByDeathPersonPin(String pin, User user) {
        List<DeathAlteration> result = deathAlterationDAO.getDeathAlterationByDeathPersonPin(pin);
        for (DeathAlteration da : result) {
            loadValuesToDeathAlterationObject(da);
            //    validateAccessOfUserForApproval(da, user);
        }
        return result;
    }

    private void loadValuesToDeathAlterationObject(DeathAlteration da) {
        DeathRegister dr = deathRegisterDAO.getById(da.getDeathRegisterIDUkey());
        if (dr.getDeathPerson().getDeathPersonNameOfficialLang() != null) {
            da.setDeathPersonName(dr.getDeathPerson().getDeathPersonNameOfficialLang());
        }
    }

    private void applyChanges(DeathAlteration da, DeathRegister dr, User user) {

        switch (da.getType()) {

            // section 53 - corrections to sudden deaths
            case TYPE_53: {
//                Alteration27 alt = ba.getAlt27();
//                process27Changes(ba, bdf, alt, user);
                break;
            }

            // section 52 (1) - corrections to normal deaths
            default: {
                // process death alteration info
                DeathAlterationInfo deathAlterationInfo = da.getDeathInfo();

                if (da.getApprovalStatuses().get(DeathAlteration.SUDDEN_DEATH)) {
                    dr.setDeathType(DeathRegister.Type.SUDDEN);
                }
                if (da.getApprovalStatuses().get(DeathAlteration.DATE_OF_DEATH)) {
                    dr.getDeath().setDateOfDeath(deathAlterationInfo.getDateOfDeath());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.TIME_OF_DEATH)) {
                    dr.getDeath().setTimeOfDeath(deathAlterationInfo.getTimeOfDeath());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.PLACE_OF_DEATH)) {
                    dr.getDeath().setPlaceOfDeath(deathAlterationInfo.getPlaceOfDeath());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.PLACE_OF_DEATH_ENGLISH)) {
                    dr.getDeath().setPlaceOfDeathInEnglish(deathAlterationInfo.getPlaceOfDeathInEnglish());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.CAUSE_OF_DEATH_ESTABLISHED)) {
                    dr.getDeath().setCauseOfDeathEstablished(deathAlterationInfo.isCauseOfDeathEstablished());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.CAUSE_OF_DEATH)) {
                    dr.getDeath().setCauseOfDeath(deathAlterationInfo.getCauseOfDeath());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.ICD_CODE)) {
                    dr.getDeath().setIcdCodeOfCause(deathAlterationInfo.getIcdCodeOfCause());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.BURIAL_PLACE)) {
                    dr.getDeath().setPlaceOfBurial(deathAlterationInfo.getPlaceOfBurial());
                }

                // process dead person info
                DeathPersonInfo deathPersonInfo = da.getDeathPerson();

                if (da.getApprovalStatuses().get(DeathAlteration.PIN)) {
                    dr.getDeathPerson().setDeathPersonPINorNIC(deathPersonInfo.getDeathPersonPINorNIC());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.COUNTRY)) {
                    dr.getDeathPerson().setDeathPersonCountry(deathPersonInfo.getDeathPersonCountry());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.PASSPORT)) {
                    dr.getDeathPerson().setDeathPersonPassportNo(deathPersonInfo.getDeathPersonPassportNo());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.AGE)) {
                    dr.getDeathPerson().setDeathPersonAge(deathPersonInfo.getDeathPersonAge());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.GENDER)) {
                    dr.getDeathPerson().setDeathPersonGender(deathPersonInfo.getDeathPersonGender());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.RACE)) {
                    dr.getDeathPerson().setDeathPersonRace(deathPersonInfo.getDeathPersonRace());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.NAME)) {
                    dr.getDeathPerson().setDeathPersonNameOfficialLang(deathPersonInfo.getDeathPersonNameOfficialLang());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.NAME_ENGLISH)) {
                    dr.getDeathPerson().setDeathPersonNameInEnglish(deathPersonInfo.getDeathPersonNameInEnglish());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.ADDRESS)) {
                    dr.getDeathPerson().setDeathPersonPermanentAddress(deathPersonInfo.getDeathPersonPermanentAddress());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.PIN_FATHER)) {
                    dr.getDeathPerson().setDeathPersonFatherPINorNIC(deathPersonInfo.getDeathPersonFatherPINorNIC());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.NAME_FATHER)) {
                    dr.getDeathPerson().setDeathPersonFatherFullName(deathPersonInfo.getDeathPersonFatherFullName());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.PIN_MOTHER)) {
                    dr.getDeathPerson().setDeathPersonMotherPINorNIC(deathPersonInfo.getDeathPersonMotherPINorNIC());
                }
                if (da.getApprovalStatuses().get(DeathAlteration.NAME_MOTHER)) {
                    dr.getDeathPerson().setDeathPersonMotherFullName(deathPersonInfo.getDeathPersonMotherFullName());
                }

                break;
            }
        }
    }

    /**
     * Checks if the user can edit or delete a death alteration entry before approval by an ARG
     * <p/>
     * A DEO or ADR at the same "SubmissionLocation" can edit or delete an entry at data entry stage. Any other who has
     * access to the BD division of the corresponding BDF has access to edit or delete
     *
     * @param da   the death alteration entry
     * @param user the user attempting to update or delete
     */
    private void validateAccessOfUserToEditOrDelete(DeathAlteration da, User user) {
        if (!DeathAlteration.State.DATA_ENTRY.equals(da.getStatus())) {
            handleException("Death alteration ID : " + da.getIdUKey() + " cannot be edited as its not in the " +
                "Data entry state", ErrorCodes.ILLEGAL_STATE);
        }

        if (Role.ROLE_DEO.equals(user.getRole().getRoleId()) || Role.ROLE_ADR.equals(user.getRole().getRoleId())) {
            if (da.getSubmittedLocation().equals(user.getPrimaryLocation())) {
                return;
            }
        } else if (!Role.ROLE_ADMIN.equals(user.getRole().getRoleId())) {
            return;
        }

        ValidationUtils.validateAccessToBDDivision(user, da.getDeathRecordDivision());
    }

    /**
     * validate user's permission to get death alteration
     *
     * @param da   death alteration
     * @param user user
     */
    private void validateAccessOfUserToGet(DeathAlteration da, User user) {

        if (Role.ROLE_DEO.equals(user.getRole().getRoleId()) || Role.ROLE_ADR.equals(user.getRole().getRoleId())) {
            if (da.getSubmittedLocation().equals(user.getPrimaryLocation())) {
                return;
            }
        } else if (!Role.ROLE_ADMIN.equals(user.getRole().getRoleId())) {
            return;
        }

        ValidationUtils.validateAccessToBDDivision(user, da.getDeathRecordDivision());
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
            ValidationUtils.validateAccessToBDDivision(user, da.getDeathRecordDivision());
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
