package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.AdoptionOrder;

import java.util.List;

/**
 * @author Ashoka Ekanayaka
 */
public interface AdoptionOrderDAO {
    public AdoptionOrder getById(long adoptionId);

    public void addAdoptionOrder(AdoptionOrder adoption);

    public void updateAdoptionOrder(AdoptionOrder bdf);

    public List<AdoptionOrder> findAll();

    public void deleteAdoptionOrder(long idUKey);
}
