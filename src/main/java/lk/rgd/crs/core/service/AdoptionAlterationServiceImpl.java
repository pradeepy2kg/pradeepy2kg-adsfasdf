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
        try{
           
        logger.debug("Attempt to add an alteration for adoption : {} by {}", adoptionAlteration.getAoUKey(), user.getUserId());
        checkUserPermission(Permission.EDIT_ADOPTION_ALTERATION, ErrorCodes.PERMISSION_DENIED, "add adoption alteration", user);
        AdoptionOrder adoptionOrder = adoptionOrderDAO.getById(adoptionAlteration.getAoUKey());
        adoptionOrder.setStatus(AdoptionOrder.State.ALTERATION_REQUESTED);
        adoptionOrderDAO.updateAdoptionOrder(adoptionOrder, user);
        logger.debug("Request an alteration for Adoption Order : {}", adoptionOrder.getIdUKey());
        adoptionAlteration = markChangedFields(adoptionAlteration, adoptionOrder);
        adoptionAlteration.setStatus(AdoptionAlteration.State.DATA_ENTRY);
        adoptionAlterationDAO.addAdoptionAlteration(adoptionAlteration, user);
        logger.debug("Added adoption alteration {} for adoption {}", adoptionAlteration.getIdUKey(), adoptionOrder.getIdUKey());
        }catch (Exception e){
            e.printStackTrace();
        }
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
        adoptionAlterationDAO.deleteAdoptionAlteration(adoptionAlteration.getIdUKey(), user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void approveAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        logger.debug("Attempt to approve an Adoption Alteration for adoption : {} by {}", adoptionAlteration.getAoUKey(), user.getUserId());
        checkUserPermission(Permission.APPROVE_ADOPTION_ALTERATION, ErrorCodes.PERMISSION_DENIED, "approve adoption alteration", user);
        checkAdoptionAlterationStatus(adoptionAlteration, AdoptionAlteration.State.DATA_ENTRY, "approve");
        AdoptionAlteration existing = adoptionAlterationDAO.getAdoptionAlterationByIdUKey(adoptionAlteration.getIdUKey());

        /* Check for approved fields. */
        boolean containsApprovedChanges = false;
        for(int i=0; i < adoptionAlteration.getApprovalStatuses().size(); i++){
            if(adoptionAlteration.getApprovalStatuses().get(i)){
                containsApprovedChanges = true;
                logger.debug("Approving {} ", i);
            }
        }
        if(containsApprovedChanges){
            existing.setStatus(AdoptionAlteration.State.FULL_APPROVED);

            AdoptionOrder ao = adoptionOrderDAO.getById(existing.getAoUKey());
            AdoptionOrder newAO = null;
            try{
                newAO = ao.clone();
            }catch (CloneNotSupportedException e){
                e.printStackTrace();
                handleException("Unable to clone Adoption Order : " + ao.getIdUKey(), ErrorCodes.ILLEGAL_STATE);
            }
            applyChanges(adoptionAlteration, newAO);
            newAO.setStatus(AdoptionOrder.State.APPROVED);
            adoptionOrderDAO.addAdoptionOrder(newAO, user);
            logger.debug("Added adoption order : {}", newAO.getIdUKey());
            ao.setStatus(AdoptionOrder.State.ARCHIVED_ALTERED);
            ao.getLifeCycleInfo().setActiveRecord(false);
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
        AdoptionOrder adoptionOrder = adoptionOrderDAO.getById(existing.getAoUKey());
        if(!AdoptionOrder.State.ALTERATION_REQUESTED.equals(adoptionOrder.getStatus())){
            handleException("Adoption Alteration "+ existing.getIdUKey() + " can not be rejected.", ErrorCodes.ILLEGAL_STATE);
        }else{
            existing.setStatus(AdoptionAlteration.State.REJECTED);
            existing.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            existing.getLifeCycleInfo().setApprovalOrRejectUser(user);
            adoptionAlterationDAO.updateAdoptionAlteration(existing, user);
            logger.debug("Rejected adoption alteration : {}", existing.getIdUKey());
            // TODO set the status that was there before requesting the alteration
            adoptionOrder.setStatus(AdoptionOrder.State.NOTICE_LETTER_PRINTED);
            adoptionOrderDAO.updateAdoptionOrder(adoptionOrder, user);
            logger.debug("Updated adoption order : {}", adoptionOrder.getIdUKey());
        }
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

    private void applyChanges(AdoptionAlteration alteration, AdoptionOrder newAO){
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.CHILD_NAME)){
            newAO.setChildNewName(alteration.getChildName());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.CHILD_DOB)){
            newAO.setChildBirthDate(alteration.getChildBirthDate());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.CHILD_GENDER)){
            newAO.setChildGender(alteration.getChildGender());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.APPLICANT_NAME)){
            newAO.setApplicantName(alteration.getApplicantName());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.APPLICANT_ADDRESS)){
            newAO.setApplicantAddress(alteration.getApplicantAddress());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.APPLICANT_SECOND_ADDRESS)){
            newAO.setApplicantSecondAddress(alteration.getApplicantSecondAddress());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.APPLICANT_OCCUPATION)){
            newAO.setApplicantOccupation(alteration.getApplicantOccupation());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.SPOUSE_NAME)){
            newAO.setSpouseName(alteration.getSpouseName());
        }
        if(alteration.getApprovalStatuses().get(AdoptionAlteration.SPOUSE_OCCUPATION)){
            newAO.setSpouseOccupation(alteration.getSpouseOccupation());
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
