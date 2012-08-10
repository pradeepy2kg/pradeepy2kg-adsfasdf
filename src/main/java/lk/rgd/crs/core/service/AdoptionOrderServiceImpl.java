package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.api.dao.AdoptionOrderDAO;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * Service API implementation to manage Adoption Orders and related activities
 *
 * @author Ashoka Ekanayaka
 *         <p/>
 *         TODO check if access priviledges needs to be performed? e.g. should these methods be only accessible from the
 *         Colombo office?
 */
public class AdoptionOrderServiceImpl implements AdoptionOrderService {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionOrderServiceImpl.class);
    private final AdoptionOrderDAO adoptionOrderDAO;

    AdoptionOrderServiceImpl(AdoptionOrderDAO adoptionOrderDAO) {
        this.adoptionOrderDAO = adoptionOrderDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AdoptionOrder getById(long adoptionId, User user) {
        return adoptionOrderDAO.getById(adoptionId);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AdoptionOrder getWithRelationshipsById(long adoptionId, User user) {
        AdoptionOrder ao = adoptionOrderDAO.getById(adoptionId);
        //trigger lazy loder handler by calling this lazy loading object
        ao.getLifeCycleInfo().getApprovalOrRejectUser().getUserName();
        return ao;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public AdoptionOrder getByCourtAndCourtOrderNumber(int courtUKey, String courtOrderNumber, User user) {
        return adoptionOrderDAO.getByCourtAndCourtOrderNumber(courtUKey, courtOrderNumber);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addAdoptionOrder(AdoptionOrder adoption, User user) {

        logger.debug("Adding an adoption order for court order : {}", adoption.getCourtOrderNumber());
        businessValidations(adoption);

        AdoptionOrder adopt = getByCourtAndCourtOrderNumber(0 /* TODO FIX ME */, adoption.getCourtOrderNumber(), user);
        if (adopt != null) {
            handleException("can not add adoption order " + adoption.getIdUKey() +
                " Court Order number already exists : " + adoption.getStatus(), ErrorCodes.ENTITY_ALREADY_EXIST);
        }

        adoptionOrderDAO.addAdoptionOrder(adoption, user);
        logger.debug("Added adoption order for court order : {}", adoption.getCourtOrderNumber());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAdoptionOrder(AdoptionOrder adoption, User user) {

        logger.debug("Update adoption order for court order : {}", adoption.getCourtOrderNumber());
        businessValidations(adoption);

        /*todo no need 
    AdoptionOrder adopt = adoptionOrderDAO.getById(adoption.getIdUKey());
        if (AdoptionOrder.State.DATA_ENTRY != adopt.getStatus()) {
            handleException("Cannot update adoption order " + adoption.getIdUKey() +
                " Illegal state at target : " + adopt.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }*/
        adoptionOrderDAO.updateAdoptionOrder(adoption, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAdoptionOrder(long idUKey, User user) {
        AdoptionOrder adoption = adoptionOrderDAO.getById(idUKey);
        if (adoption.getStatus() == AdoptionOrder.State.DATA_ENTRY) {
            adoptionOrderDAO.deleteAdoptionOrder(idUKey);
        } else {
            handleException("Cannot delete adoption order " + adoption.getIdUKey() +
                " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void approveAdoptionOrder(long idUKey, User user) {
        logger.debug("Approve adoption order : {}", idUKey);
        setApprovalStatus(idUKey, user, AdoptionOrder.State.APPROVED);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void rejectAdoptionOrder(long idUKey, User user) {
        logger.debug("Rejecting adoption order : {}", idUKey);
        setApprovalStatus(idUKey, user, AdoptionOrder.State.REJECTED);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void recordNewBirthDeclaration(AdoptionOrder adoption, long serialNumber, User user) {
        //todo permission check for this adoption and bdf
        adoption.setNewBirthCertificateNumber(serialNumber);
        adoption.getLifeCycleInfo().setCertificateGeneratedTimestamp(new Date());
        adoption.getLifeCycleInfo().setCertificateGeneratedUser(user);

        adoptionOrderDAO.updateAdoptionOrder(adoption, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void setApplicantInfo(AdoptionOrder adoption, User user) {

        logger.debug("Recording adoption certificate applicant information for : {}", adoption.getIdUKey());

        AdoptionOrder adopt = getById(adoption.getIdUKey(), user);
        if ((adopt.getStatus() != AdoptionOrder.State.NOTICE_LETTER_PRINTED) ||
            (adoption.getStatus() != AdoptionOrder.State.NOTICE_LETTER_PRINTED)) {
            handleException("Cannot change status to certificate issue request captured, " + adoption.getIdUKey() +
                " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        adoption.setStatus(AdoptionOrder.State.CERTIFICATE_ISSUE_REQUEST_CAPTURED);
        adoptionOrderDAO.updateAdoptionOrder(adoption, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void setStatusToPrintedNotice(long adoptionId, User user) {
        AdoptionOrder adoption = getById(adoptionId, user);
        if (adoption.getStatus() != AdoptionOrder.State.APPROVED) {
            handleException("Cannot change status to notice letter printed, " + adoption.getIdUKey() +
                " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        adoption.setStatus(AdoptionOrder.State.NOTICE_LETTER_PRINTED);
        adoptionOrderDAO.updateAdoptionOrder(adoption, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void setStatusToPrintedCertificate(long adoptionId, User user) {
        AdoptionOrder adoption = getById(adoptionId, user);
        if (adoption.getStatus() != AdoptionOrder.State.CERTIFICATE_ISSUE_REQUEST_CAPTURED) {
            handleException("Cannot change status to adoption certificate printed, " + adoption.getIdUKey() +
                " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }

        adoption.setStatus(AdoptionOrder.State.ADOPTION_CERTIFICATE_PRINTED);
        adoptionOrderDAO.updateAdoptionOrder(adoption, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<AdoptionOrder> getPaginatedListForState(int pageNo, int noOfRows,
        AdoptionOrder.State status, User user) {
        try {
            return adoptionOrderDAO.getPaginatedListForState(pageNo, noOfRows, status);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<AdoptionOrder> getPaginatedListForAll(int pageNo, int noOfRows, User user) {
        try {
            return adoptionOrderDAO.getPaginatedListForAll(pageNo, noOfRows);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean isEntryNoExist(long adoptionEntryNo, User user) {
        logger.debug("Look for Adoption Entry No: {} by {}", adoptionEntryNo, user.getUserId());
        return adoptionOrderDAO.isEntryNoExist(adoptionEntryNo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Long getLastEntryNo(User user) {
        logger.debug("{} is requesting the latest adoptionEntryNo", user.getUserId());
        return adoptionOrderDAO.getLastEntryNo();
    }

    private void setApprovalStatus(long idUKey, User user, AdoptionOrder.State state) {
        AdoptionOrder adoption = adoptionOrderDAO.getById(idUKey);
        if (adoption.getStatus() == AdoptionOrder.State.DATA_ENTRY) {
            validateAccess(user);
            adoption.setStatus(state);
            adoption.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            adoption.getLifeCycleInfo().setApprovalOrRejectUser(user);
        } else {
            handleException("Cannot approve/reject adoption order " + adoption.getIdUKey() +
                " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        adoptionOrderDAO.updateAdoptionOrder(adoption, user);
        logger.debug("Updated adoption order : {}", idUKey);
    }

    private void businessValidations(AdoptionOrder adoption) {
        /*todo remove no need this validation for adding DAO set state DE
     if (adoption.getStatus() != AdoptionOrder.State.DATA_ENTRY) {
            handleException("can not update adoption order " + adoption.getIdUKey() +
                    " Illegal State : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }*/

        if ((adoption.getChildNewName() == null) && (adoption.getChildExistingName() == null)) {
            handleException("can not update adoption order " + adoption.getIdUKey() +
                " A Name not given : " + adoption.getStatus(), ErrorCodes.INVALID_DATA);
        }
    }

    private void validateAccess(User user) {
        String role = user.getRole().getRoleId();
        if (!(User.State.ACTIVE == user.getStatus()) ||
            !(Role.ROLE_ARG.equals(role) || Role.ROLE_RG.equals(role))) {
            handleException("User : " + user.getUserId() + " of role : " + role +
                " is not allowed access to approve/reject an adoption : ", ErrorCodes.PERMISSION_DENIED);
        }

        if (!user.isAuthorized(Permission.APPROVE_ADOPTION)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject Adoptions",
                ErrorCodes.PERMISSION_DENIED);
        }

    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
