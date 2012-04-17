package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.AdoptionOrder;

import java.util.List;

/**
 * @author Ashoka Ekanayaka
 */
public interface AdoptionOrderDAO {

    /**
     * Return adoption order by unique ID
     *
     * @param adoptionId the unique ID assigned to the adoption order
     * @return the adoption order or null
     */
    public AdoptionOrder getById(long adoptionId);

    /**
     * Add an adoption order
     *
     * @param adoption the order to be added
     * @param user     the user marked as the last updater
     */
    public void addAdoptionOrder(AdoptionOrder adoption, User user);

    /**
     * Update and adoption order
     *
     * @param adoption the order being updated
     * @param user     the user making the update
     */
    public void updateAdoptionOrder(AdoptionOrder adoption, User user);

    /**
     * Return all adoption orders - not for normal use - only for indexing
     *
     * @return all adoption order record
     */
    public List<AdoptionOrder> findAll();

    /**
     * Delete an adoption order before approval
     *
     * @param idUKey the unique ID of the order to be deleted
     */
    public void deleteAdoptionOrder(long idUKey);

    /**
     * Add new birth certificate number to the existing adoption order
     *
     * @param adoption     existing adoption order
     * @param serialNumber serial number of the new birth registration for the adopted child
     * @param user         the user making the update
     */
    public void recordNewBirthDeclaration(AdoptionOrder adoption, long serialNumber, User user);

    /**
     * Get a list of adoption orders by court order serial number
     *
     * @param courtUKey    the unique court id
     * @param serialNumber the court order serial number
     * @return matching records
     */
    public AdoptionOrder getByCourtAndCourtOrderNumber(int courtUKey, String serialNumber);

    /**
     * Get a list of adoption orders by court order by status
     *
     * @param pageNo   page number
     * @param noOfRows rows to return
     * @param status   status of the records to return
     * @return matching records
     */
    public List<AdoptionOrder> getPaginatedListForState(int pageNo, int noOfRows, AdoptionOrder.State status);

    /**
     * Get paginated list of adoption orders
     *
     * @param pageNo   page number
     * @param noOfRows rows to return
     * @return all records (paginated)
     */
    public List<AdoptionOrder> getPaginatedListForAll(int pageNo, int noOfRows);

    public Long findCourtUsageInAdoptions(int courtUKey);
}
