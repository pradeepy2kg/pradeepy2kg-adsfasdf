package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.DeathAlterationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.domain.DeathAlteration;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.dao.DeathAlterationDAO;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.ErrorCodes;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.*;

/**
 * @author amith jayasekara
 */
public class DeathAlterationServiceImpl implements DeathAlterationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthAlterationServiceImpl.class);
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
        DeathRegister dr = deathRegistrationService.getById(da.getDeathId(), user);
        deathAlterationValidator.validateMinimulCondiations(da, dr);
/*        validateAccessToBDDivision(user, da.getDeathDivision());*/
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
        deathAlterationDAO.deleteDeathAlteration(idUKey);
    }

    /**
     * @inheritDoc
     */
    public void rejectDeathAlteration(long idUKey, User user, String comment) {
        DeathAlteration exsisting = getById(idUKey, user);
        exsisting.setStatus(DeathAlteration.State.REJECT);
        exsisting.setComments(comment);
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
    public List<DeathAlteration> getAlterationApprovalListByDeathDivision(int pageNo, int numRows, int divisionId) {
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

        //setting state
        //true means fully
        if (appStatus) {
            da.setStatus(DeathAlteration.State.FULLY_APPROVED);
        } else {
            da.setStatus(DeathAlteration.State.PARTIALY_APPROVED);
        }
        //merging object
        deathAlterationDAO.updateDeathAlteration(da, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<DeathAlteration> getDeathAlterationByTimePeriod(Date startDate, Date endDate, User user) {
        //add user validation if others also get permission (other than ARG and above)
        return deathAlterationDAO.getDeathAlterationByTimePeriod(startDate, endDate);
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
