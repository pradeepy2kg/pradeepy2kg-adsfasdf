package lk.rgd.crs.core.service;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.LocationDAO;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.AdoptionAlterationDAO;
import lk.rgd.crs.api.dao.AdoptionOrderDAO;
import lk.rgd.crs.api.domain.AdoptionAlteration;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.api.service.AdoptionAlterationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Duminda Dharmakeerthi
 */
public class AdoptionAlterationServiceImpl implements AdoptionAlterationService {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionAlterationServiceImpl.class);
    private final AdoptionAlterationDAO adoptionAlterationDAO;
    private final AdoptionOrderDAO adoptionOrderDAO;
    private final LocationDAO locationDAO;

    public AdoptionAlterationServiceImpl(AdoptionAlterationDAO adoptionAlterationDAO, AdoptionOrderDAO adoptionOrderDAO, LocationDAO locationDAO) {
        this.adoptionAlterationDAO = adoptionAlterationDAO;
        this.adoptionOrderDAO = adoptionOrderDAO;
        this.locationDAO = locationDAO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AdoptionAlteration getAdoptionAlterationByIdUKey(long idUKey) {
        return adoptionAlterationDAO.getAdoptionAlterationByIdUKey(idUKey);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        logger.debug("Attempt to add an alteration for adoption : {} by {}", adoptionAlteration.getAoUKey(), user.getUserId());
        AdoptionOrder adoptionOrder = adoptionOrderDAO.getById(adoptionAlteration.getAoUKey());
        adoptionAlteration = markChangedFields(adoptionAlteration, adoptionOrder);
        adoptionAlteration.setStatus(AdoptionAlteration.State.DATA_ENTRY);
        adoptionAlterationDAO.addAdoptionAlteration(adoptionAlteration, user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        logger.debug("Attempt to update an alteration for adoption : {} by {}", adoptionAlteration.getAoUKey(), user.getUserId());
        AdoptionOrder adoptionOrder = adoptionOrderDAO.getById(adoptionAlteration.getAoUKey());
        adoptionAlteration = markChangedFields(adoptionAlteration, adoptionOrder);
        adoptionAlteration.setStatus(AdoptionAlteration.State.DATA_ENTRY);
        adoptionAlterationDAO.updateAdoptionAlteration(adoptionAlteration, user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        logger.debug("Attempt to delete an Adoption Alteration for adoption : {} by {}", adoptionAlteration.getAoUKey(), user.getUserId());
        validateAccessOfUserToEditOrDelete(user, adoptionAlteration);
        adoptionAlterationDAO.deleteAdoptionAlteration(adoptionAlteration, user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void approveAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        logger.debug("Attempt to approve an Adoption Alteration for adoption : {} by {}", adoptionAlteration.getAoUKey(), user.getUserId());
        validateAccessOfUserToApproval(user, adoptionAlteration);
        adoptionAlteration.setStatus(AdoptionAlteration.State.FULL_APPROVED);
        adoptionAlteration.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
        adoptionAlteration.getLifeCycleInfo().setApprovalOrRejectUser(user);
        adoptionAlterationDAO.updateAdoptionAlteration(adoptionAlteration, user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void rejectAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        logger.debug("Attempt to reject an Adoption Alteration for adoption : {} by {}", adoptionAlteration.getAoUKey(), user.getUserId());
        validateAccessOfUserToApproval(user, adoptionAlteration);
        adoptionAlteration.setStatus(AdoptionAlteration.State.REJECTED);
        adoptionAlteration.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
        adoptionAlteration.getLifeCycleInfo().setApprovalOrRejectUser(user);
        adoptionAlterationDAO.updateAdoptionAlteration(adoptionAlteration, user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdoptionAlteration> getAdoptionAlterationsForApproval(User user) {
        List<AdoptionAlteration> adoptionAlterations = adoptionAlterationDAO.getAdoptionAlterationsByStatus(AdoptionAlteration.State.DATA_ENTRY);
        if (adoptionAlterations.size() > 0) {
            return adoptionAlterations;
        } else {
            return Collections.emptyList();
        }
    }

    private AdoptionAlteration markChangedFields(AdoptionAlteration adoptionAlteration, AdoptionOrder adoptionOrder) {
        if (adoptionAlteration.getApplicantName() != null && !adoptionAlteration.getApplicantName().equals(adoptionOrder.getApplicantName())) {
            adoptionAlteration.getChangedFields().set(AdoptionAlteration.APPLICANT_NAME);
        }
        if (adoptionAlteration.getApplicantAddress() != null && !adoptionAlteration.getApplicantAddress().equals(adoptionOrder.getApplicantAddress())) {
            adoptionAlteration.getChangedFields().set(AdoptionAlteration.APPLICANT_ADDRESS);
        }
        if (adoptionAlteration.getApplicantSecondAddress() != null && !adoptionAlteration.getApplicantSecondAddress().equals(adoptionOrder.getApplicantSecondAddress())) {
            adoptionAlteration.getChangedFields().set(AdoptionAlteration.APPLICANT_SECOND_ADDRESS);
        }
        if (adoptionAlteration.getApplicantOccupation() != null && !adoptionAlteration.getApplicantOccupation().equals(adoptionOrder.getApplicantOccupation())) {
            adoptionAlteration.getChangedFields().set(AdoptionAlteration.APPLICANT_OCCUPATION);
        }
        if (adoptionOrder.isJointApplicant()) {
            if (adoptionAlteration.getSpouseName() != null && !adoptionAlteration.getSpouseName().equals(adoptionOrder.getSpouseName())) {
                adoptionAlteration.getChangedFields().set(AdoptionAlteration.SPOUSE_NAME);
            }
            if (adoptionAlteration.getSpouseOccupation() != null && !adoptionAlteration.getSpouseOccupation().equals(adoptionOrder.getSpouseOccupation())) {
                adoptionAlteration.getChangedFields().set(AdoptionAlteration.SPOUSE_OCCUPATION);
            }
        }
        if (adoptionOrder.getChildNewName() != null && !adoptionOrder.getChildNewName().isEmpty()) {
            if (adoptionAlteration.getChildName() != null && !adoptionAlteration.getChildName().equals(adoptionOrder.getChildNewName())) {
                adoptionAlteration.getChangedFields().set(AdoptionAlteration.CHILD_NAME);
            }
        } else {
            if (adoptionAlteration.getChildName() != null && !adoptionAlteration.getChildName().equals(adoptionOrder.getChildExistingName())) {
                adoptionAlteration.getChangedFields().set(AdoptionAlteration.CHILD_NAME);
            }
        }
        if (adoptionAlteration.getChildBirthDate() != null && !adoptionAlteration.getChildBirthDate().equals(adoptionOrder.getChildBirthDate())) {
            adoptionAlteration.getChangedFields().set(AdoptionAlteration.CHILD_DOB);
        }
        if (adoptionAlteration.getChildGender() != adoptionOrder.getChildGender()) {
            adoptionAlteration.getChangedFields().set(AdoptionAlteration.CHILD_GENDER);
        }
        return adoptionAlteration;
    }

    private void validateAccessOfUserToEditOrDelete(User user, AdoptionAlteration adoptionAlteration) {
        if (AdoptionAlteration.State.DATA_ENTRY.equals(adoptionAlteration.getStatus())) {
            validateAccessOfUserToAdoption(user);
            // TODO handle permissions
        } else {
            handleException("Can not be edited as Adoption Alteration (" + adoptionAlteration.getIdUKey() + "is not in the Data Entry State", ErrorCodes.ILLEGAL_STATE);
        }
    }

    private void validateAccessOfUserToApproval(User user, AdoptionAlteration adoptionAlteration) {
        if (AdoptionAlteration.State.DATA_ENTRY.equals(adoptionAlteration.getStatus())) {
            validateAccessOfUserToAdoption(user);
            // TODO handle permissions
        } else {
            handleException("Can not be Approved as Adoption Alteration (" + adoptionAlteration.getIdUKey() + "is not in the Data Entry State", ErrorCodes.ILLEGAL_STATE);
        }
    }

    private void validateAccessOfUserToAdoption(User user) {
        Location headOffice = locationDAO.getLocation(AppConstants.HEAD_OFFICE_LOCATION_ID);
        if (!user.getActiveLocations().contains(headOffice)) {
            handleException("User is not authorized for adoption alteration", ErrorCodes.USER_IS_NOT_ALLOWED_FOR_LOCATION);
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
