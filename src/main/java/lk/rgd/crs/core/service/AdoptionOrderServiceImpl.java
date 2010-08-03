package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.api.dao.AdoptionOrderDAO;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.ErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

/**
 * Main service to manage Adoption Orders and related activities
 *
 * @author Ashoka Ekanayaka
 */
public class AdoptionOrderServiceImpl implements AdoptionOrderService {
    private static final Logger logger = LoggerFactory.getLogger(AdoptionOrderServiceImpl.class);
    private final AdoptionOrderDAO adoptionOrderDAO;

    AdoptionOrderServiceImpl(AdoptionOrderDAO adoptionOrderDAO) {
        this.adoptionOrderDAO = adoptionOrderDAO;
    }

    public AdoptionOrder getById(long adoptionId, User user) {
        return adoptionOrderDAO.getById(adoptionId);
    }

    public AdoptionOrder getByCourtOrderNumber(String courtOrderNumber, User user) {
        try {
            return adoptionOrderDAO.getByCourtOrderNumber(courtOrderNumber).get(0);
        } catch (Exception e) {
            logger.debug("No results found for {} : {}", courtOrderNumber,e);
            return null;
        }
    }

    public void addAdoptionOrder(AdoptionOrder adoption, User user) {
        businessValidations(adoption);

        AdoptionOrder adopt = getByCourtOrderNumber(adoption.getCourtOrderNumber(), user);
        if (adopt != null) {
            handleException("can not add adoption order " + adoption.getIdUKey() +
                    " Court Order number already exists : " + adoption.getStatus(), ErrorCodes.ENTITY_ALREADY_EXIST);
        }

        adoptionOrderDAO.addAdoptionOrder(adoption);
    }

    public void updateAdoptionOrder(AdoptionOrder adoption, User user) {
        businessValidations(adoption);

        AdoptionOrder adopt = adoptionOrderDAO.getById(adoption.getIdUKey());
        if (AdoptionOrder.State.DATA_ENTRY != adopt.getStatus()) {
            handleException("Cannot update adoption order " + adoption.getIdUKey() +
                    " Illegal state at target : " + adopt.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }

        adoptionOrderDAO.updateAdoptionOrder(adoption);
    }

    public List<AdoptionOrder> findAll(User user) {
        //todo access priviledges ?
        try {
            return adoptionOrderDAO.findAll();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public void deleteAdoptionOrder(long idUKey, User user) {
        //todo access priviledges ?
        AdoptionOrder adoption = adoptionOrderDAO.getById(idUKey);
        if (adoption.getStatus() == AdoptionOrder.State.DATA_ENTRY) {
            adoptionOrderDAO.deleteAdoptionOrder(idUKey);
        } else {
            handleException("Cannot delete adoption order " + adoption.getIdUKey() +
                    " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
    }

    public void approveAdoptionOrder(long idUKey, User user) {
        setApprovalStatus(idUKey, user, AdoptionOrder.State.APPROVED);
    }

    public void rejectAdoptionOrder(long idUKey, User user) {
        setApprovalStatus(idUKey, user, AdoptionOrder.State.REJECTED);
    }

    /**
     * @inheritDoc
     */
    public void setApplicantInfo(AdoptionOrder adoption, User user) {
        AdoptionOrder adopt = getById(adoption.getIdUKey(), user);
        if ((adopt.getStatus() != AdoptionOrder.State.NOTICE_LETTER_PRINTED) ||
                (adoption.getStatus() != AdoptionOrder.State.NOTICE_LETTER_PRINTED)) {
            handleException("Cannot change status to 4, " + adoption.getIdUKey() +
                    " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        adoption.setStatus(AdoptionOrder.State.CERTIFICATE_ISSUE_REQUEST_CAPTURED);
        adoptionOrderDAO.updateAdoptionOrder(adoption);
    }

    /**
     * @inheritDoc
     */
    public void setStatusToPrintedNotice(long adoptionId, User user) {
        AdoptionOrder adoption = getById(adoptionId, user);
        if (adoption.getStatus() != AdoptionOrder.State.APPROVED) {
            handleException("Cannot change status to 2, " + adoption.getIdUKey() +
                    " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }

        adoption.setStatus(AdoptionOrder.State.NOTICE_LETTER_PRINTED);
        adoptionOrderDAO.updateAdoptionOrder(adoption);
    }

    /**
     * @inheritDoc
     */
    public void setStatusToPrintedCertificate(long adoptionId, User user) {
        AdoptionOrder adoption = getById(adoptionId, user);
        if (adoption.getStatus() != AdoptionOrder.State.CERTIFICATE_ISSUE_REQUEST_CAPTURED) {
            handleException("Cannot change status to 5, " + adoption.getIdUKey() +
                    " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }

        adoption.setStatus(AdoptionOrder.State.ADOPTION_CERTIFICATE_PRINTED);
        adoptionOrderDAO.updateAdoptionOrder(adoption);
    }

    public List<AdoptionOrder> getPaginatedListForState(int pageNo, int noOfRows,
                                                        AdoptionOrder.State status, User user) {
        try {
            return adoptionOrderDAO.getPaginatedListForState(pageNo, noOfRows, status);
        } catch (Exception e) {
            return new ArrayList();  //returns an empty List if no records are found
        }
    }

    public List<AdoptionOrder> getPaginatedListForAll(int pageNo, int noOfRows, User user) {
        try {
            return adoptionOrderDAO.getPaginatedListForAll(pageNo, noOfRows);
        } catch (Exception e) {
            return new ArrayList();  //returns an empty List if no records are found
        }
    }

    private void setApprovalStatus(long idUKey, User user, AdoptionOrder.State state) {
        AdoptionOrder adoption = adoptionOrderDAO.getById(idUKey);
        if (adoption.getStatus() == AdoptionOrder.State.DATA_ENTRY) {
            validateAccess(user);
            adoption.setStatus(state);
        } else {
            handleException("Cannot approve/reject adoption order " + adoption.getIdUKey() +
                    " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        adoptionOrderDAO.updateAdoptionOrder(adoption);
    }

    private void businessValidations(AdoptionOrder adoption) {
        if (adoption.getStatus() != AdoptionOrder.State.DATA_ENTRY) {
            handleException("can not update adoption order " + adoption.getIdUKey() +
                    " Illegal State : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }

        if ((adoption.getChildNewName() == null) && (adoption.getChildExistingName() == null)) {
            handleException("can not update adoption order " + adoption.getIdUKey() +
                    " A Name not given : " + adoption.getStatus(), ErrorCodes.INVALID_DATA);
        }
    }

    private void validateAccess(User user) {
        String role = user.getRole().getName();
        if (!(User.State.ACTIVE == user.getStatus()) ||
                !(Role.ROLE_ARG.equals(role) || Role.ROLE_RG.equals(role))) {
            handleException("User : " + user.getUserId() + " of role : " + role +
                    " is not allowed access to approve/reject an adoption : ", ErrorCodes.PERMISSION_DENIED);
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
