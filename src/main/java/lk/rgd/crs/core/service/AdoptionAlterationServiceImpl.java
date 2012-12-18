package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.LocationDAO;
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
        checkUserPermission(Permission.EDIT_ADOPTION_ALTERATION, ErrorCodes.PERMISSION_DENIED, "add adoption alteration", user);
        AdoptionOrder adoptionOrder = adoptionOrderDAO.getById(adoptionAlteration.getAoUKey());
        adoptionAlteration = markChangedFields(adoptionAlteration, adoptionOrder);
        adoptionAlteration.setStatus(AdoptionAlteration.State.DATA_ENTRY);
        adoptionAlterationDAO.addAdoptionAlteration(adoptionAlteration, user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        logger.debug("Attempt to update an alteration for adoption : {} by {}", adoptionAlteration.getAoUKey(), user.getUserId());
        checkUserPermission(Permission.EDIT_ADOPTION_ALTERATION, ErrorCodes.PERMISSION_DENIED, "edit adoption alteration", user);
        checkAdoptionAlterationStatus(adoptionAlteration, AdoptionAlteration.State.DATA_ENTRY, "update");
        AdoptionOrder adoptionOrder = adoptionOrderDAO.getById(adoptionAlteration.getAoUKey());
        adoptionAlteration = markChangedFields(adoptionAlteration, adoptionOrder);
        adoptionAlterationDAO.updateAdoptionAlteration(adoptionAlteration, user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        logger.debug("Attempt to delete an Adoption Alteration for adoption : {} by {}", adoptionAlteration.getAoUKey(), user.getUserId());
        checkUserPermission(Permission.EDIT_ADOPTION_ALTERATION, ErrorCodes.PERMISSION_DENIED, "delete adoption alteration", user);
        checkAdoptionAlterationStatus(adoptionAlteration, AdoptionAlteration.State.DATA_ENTRY, "delete");
        adoptionAlterationDAO.deleteAdoptionAlteration(adoptionAlteration, user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void approveAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        logger.debug("Attempt to approve an Adoption Alteration for adoption : {} by {}", adoptionAlteration.getAoUKey(), user.getUserId());
        checkUserPermission(Permission.APPROVE_ADOPTION_ALTERATION, ErrorCodes.PERMISSION_DENIED, "approve adoption alteration", user);
        checkAdoptionAlterationStatus(adoptionAlteration, AdoptionAlteration.State.DATA_ENTRY, "approve");
        AdoptionAlteration existing = adoptionAlterationDAO.getAdoptionAlterationByIdUKey(adoptionAlteration.getIdUKey());
        logger.debug("Permission OK.\tItem to approve : {}", adoptionAlteration.getApprovalStatuses().size());
        /* Check for approved fields. */
        boolean containsApprovedChanges = false;
        for(int i=0; i < adoptionAlteration.getApprovalStatuses().size(); i++){
            if(adoptionAlteration.getApprovalStatuses().get(i)){
                containsApprovedChanges = true;
                logger.debug("Approving {} ", i);
            }
        }
        logger.debug("Contains Changes : {}", containsApprovedChanges);
        if(containsApprovedChanges){
            logger.debug("Mark adoption alteration {} as FULLY_APPROVED", existing.getIdUKey());
            existing.setStatus(AdoptionAlteration.State.FULL_APPROVED);

            AdoptionOrder ao = adoptionOrderDAO.getById(existing.getAoUKey());
            AdoptionOrder newAO = null;
            try{
                newAO = ao.clone();
            }catch (CloneNotSupportedException e){
                e.printStackTrace();
                handleException("Unable to clone Adoption Order : " + ao.getIdUKey(), ErrorCodes.ILLEGAL_STATE);
            }
            applyChanges(existing, ao);
            newAO.setStatus(AdoptionOrder.State.APPROVED);
            adoptionOrderDAO.addAdoptionOrder(newAO, user);
            logger.debug("Added adoption order : {}", newAO.getIdUKey());
            ao.setStatus(AdoptionOrder.State.ARCHIVED_ALTERED);
            adoptionOrderDAO.updateAdoptionOrder(ao, user);
            logger.debug("Archived adoption order : {}", ao.getIdUKey());
        }
        existing.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
        existing.getLifeCycleInfo().setApprovalOrRejectUser(user);        
        adoptionAlterationDAO.updateAdoptionAlteration(existing, user);
        logger.debug("Approved adoption alteration : {}", existing.getIdUKey());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void rejectAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        logger.debug("Attempt to reject an Adoption Alteration for adoption : {} by {}", adoptionAlteration.getAoUKey(), user.getUserId());
        checkUserPermission(Permission.APPROVE_ADOPTION_ALTERATION, ErrorCodes.PERMISSION_DENIED, "reject adoption alteration", user);
        AdoptionAlteration existing = adoptionAlterationDAO.getAdoptionAlterationByIdUKey(adoptionAlteration.getIdUKey());
        checkAdoptionAlterationStatus(existing, AdoptionAlteration.State.DATA_ENTRY, "reject");

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

    private void applyChanges(AdoptionAlteration alteration, AdoptionOrder ao){
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.CHILD_NAME)){
            ao.setChildNewName(alteration.getChildName());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.CHILD_DOB)){
            ao.setChildBirthDate(alteration.getChildBirthDate());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.CHILD_GENDER)){
            ao.setChildGender(alteration.getChildGender());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.APPLICANT_NAME)){
            ao.setApplicantName(alteration.getApplicantName());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.APPLICANT_ADDRESS)){
            ao.setApplicantAddress(alteration.getApplicantAddress());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.APPLICANT_SECOND_ADDRESS)){
            ao.setApplicantSecondAddress(alteration.getApplicantSecondAddress());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.APPLICANT_OCCUPATION)){
            ao.setApplicantOccupation(alteration.getApplicantOccupation());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.SPOUSE_NAME)){
            ao.setSpouseName(alteration.getSpouseName());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.SPOUSE_OCCUPATION)){
            ao.setSpouseOccupation(alteration.getSpouseOccupation());
        }
    }

    private void checkUserPermission(int permissionBit, int errorCode, String msg, User user) {
        if (!user.isAuthorized(permissionBit)) {
            handleException("User : " + user.getUserId() + " is not permitted to " + msg, errorCode);
        }
    }

    private void checkAdoptionAlterationStatus(AdoptionAlteration alteration, AdoptionAlteration.State state, String message){
        if(!state.equals(alteration.getStatus())){
            handleException("Unable to "+ message+" adoption alteration due to illegal state", ErrorCodes.ILLEGAL_STATE);
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
