package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.api.dao.AdoptionOrderDAO;
import lk.rgd.crs.api.domain.AdoptionOrder;
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

    public AdoptionOrder getById(long adoptionId) {
        return adoptionOrderDAO.getById(adoptionId);
    }

    public void addAdoptionOrder(AdoptionOrder adoption) {
        adoptionOrderDAO.addAdoptionOrder(adoption);
    }

    public void updateAdoptionOrder(AdoptionOrder adoption) {
        adoptionOrderDAO.updateAdoptionOrder(adoption);
    }

    public List<AdoptionOrder> findAll() {
        return adoptionOrderDAO.findAll();
    }

    public void deleteAdoptionOrder(long idUKey) {
        adoptionOrderDAO.deleteAdoptionOrder(idUKey);
    }
}
