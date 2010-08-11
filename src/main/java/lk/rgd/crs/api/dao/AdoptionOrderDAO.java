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
     * Add new birth certificate number to the existing adoption order
     *
     * @param adoption     existing adoption order
     * @param serialNumber serial number of the new birth registration for the adopted child
     */
    public void initiateBirthDeclaration(AdoptionOrder adoption, long serialNumber);
}
