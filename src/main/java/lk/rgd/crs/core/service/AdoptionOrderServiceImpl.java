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

/**
 * Main service to manage Adoption Orders and related activities
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
        return adoptionOrderDAO.getByCourtOrderNumber(courtOrderNumber).get(0);
    }

    public void addAdoptionOrder(AdoptionOrder adoption, User user) {
        //todo validations and access rights
        adoptionOrderDAO.addAdoptionOrder(adoption);
    }

    public void updateAdoptionOrder(AdoptionOrder adoption, User user) {
        //todo security validations, access priviledges and business validations
        adoptionOrderDAO.updateAdoptionOrder(adoption);
    }

    public List<AdoptionOrder> findAll(User user) {
        //todo access priviledges ?
        return adoptionOrderDAO.findAll();
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

    private void validateAccess(User user) {
        String role = user.getRole().getName();
        if ( !(User.State.ACTIVE == user.getStatus()) ||
                !(Role.ROLE_ARG.equals(role) || Role.ROLE_RG.equals(role)) ) {
            handleException("User : " + user.getUserId() + " of role : " + role +
                " is not allowed access to approve/reject an adoption : ",ErrorCodes.PERMISSION_DENIED);
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
