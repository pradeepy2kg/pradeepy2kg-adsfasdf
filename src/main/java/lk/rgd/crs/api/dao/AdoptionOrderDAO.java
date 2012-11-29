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

    /**
     * Check whether a given entry no is already within the system.
     *
     * @param adoptionEntryNo Entered Entry No
     * @return  TRUE (If Entry No exists). FALSE otherwise.
     */
    public boolean isEntryNoExist(long adoptionEntryNo);

    /**
     * Returns the latest adoptionEntryNo
     * @return Latest adoptionEntryNo
     */
    public Long getLastEntryNo();

    /**
     * Returns list of adoptions which are under the given court order number.
     *
     * @param courtOrderNumber Selected court order number.
     * @return List of adoptions which are under the given court order number
     */
    public List<AdoptionOrder> getAdoptionsByCourtOrderNumber(String courtOrderNumber);

    /**
     * Returns an adoption record which is entered as the given entry number.
     *
     * @param adoptionEntryNo Selected adoption entry number.
     * @return Adoption records which has the given entry number
     */
    public AdoptionOrder getAdoptionByEntryNumber(long adoptionEntryNo);

    /**
     * Returns a list of adoption records where the court order is given by the selected court.
     *
     * @param courtUKey  CourtUKey of the selected court
     * @return List of adoptions from the selected court
     */
    public List<AdoptionOrder> getAdoptionsByCourt(int courtUKey);

    /**
     * Return the adoption record which is entered as the given entry number and court order number.
     *
     * @param courtOrderNumber
     * @param adoptionEntryNo
     * @return
     */
    public AdoptionOrder getAdoptionByCourtOrderNumberAndEntryNumber(String courtOrderNumber, long adoptionEntryNo);
}
