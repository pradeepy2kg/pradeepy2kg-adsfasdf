package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.api.dao.AdoptionOrderDAO;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.api.domain.BDDivision;
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
        //todo access priviledges
        return adoptionOrderDAO.getById(adoptionId);
    }

    public void addAdoptionOrder(AdoptionOrder adoption, User user) {
        //todo security validations, access priviledges and business validations
        adoptionOrderDAO.addAdoptionOrder(adoption);
    }

    public void updateAdoptionOrder(AdoptionOrder adoption, User user) {
        //todo security validations, access priviledges and business validations
        adoptionOrderDAO.updateAdoptionOrder(adoption);
    }

    public List<AdoptionOrder> findAll(User user) {
        //todo access priviledges
        return adoptionOrderDAO.findAll();
    }

    public void deleteAdoptionOrder(long idUKey, User user) {
        //todo security validations, access priviledges and business validations
        adoptionOrderDAO.deleteAdoptionOrder(idUKey);
    }

    public void approveAdoptionOrder(long idUKey, User user) {
        //todo security validations, access priviledges and business validations
        AdoptionOrder adoption = adoptionOrderDAO.getById(idUKey);
        adoption.setStatus(AdoptionOrder.State.APPROVED);
        adoptionOrderDAO.updateAdoptionOrder(adoption);
    }

    public void rejectAdoptionOrder(long idUKey, User user) {
        //todo security validations, access priviledges and business validations
        AdoptionOrder adoption = adoptionOrderDAO.getById(idUKey);
        adoption.setStatus(AdoptionOrder.State.REJECTED);
        adoptionOrderDAO.updateAdoptionOrder(adoption);
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
