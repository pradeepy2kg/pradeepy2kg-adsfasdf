package lk.rgd.crs.api.service;

import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.common.api.domain.User;

import java.util.List;

/**
 * @author Ashoka Ekanayaka
 */
public interface AdoptionOrderService {
    public AdoptionOrder getById(long adoptionId, User user);

    public void addAdoptionOrder(AdoptionOrder adoption, User user);

    public void updateAdoptionOrder(AdoptionOrder bdf, User user);

    public List<AdoptionOrder> findAll(User user);

    public void deleteAdoptionOrder(long idUKey, User user);

    public void approveAdoptionOrder(long idUKey, User user);

    public void rejectAdoptionOrder(long idUKey, User user);
}
