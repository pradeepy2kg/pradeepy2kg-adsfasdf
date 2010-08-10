package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.api.domain.BirthDeclaration;

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

    public List<AdoptionOrder> getByCourtOrderNumber(String serialNumber);

    public List<AdoptionOrder> getPaginatedListForState(int pageNo, int noOfRows, AdoptionOrder.State status);

    public List<AdoptionOrder> getPaginatedListForAll(int pageNo, int noOfRows);

    /**
     * Update existing adoption order state as archived and add a new adoption order entry with state adoption
     * certificate printed
     *
     * @param adoption existing adoption order
     * @param bdf      the BDF added for a adopted child
     */
    public void initiateBirthDeclaration(AdoptionOrder adoption, BirthDeclaration bdf);
}
