package lk.rgd.crs.api.service;

import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;

import java.util.List;

/**
 * @author Ashoka Ekanayaka
 */
public interface AdoptionOrderService {
    /**
     * Get adoption order by unique ID
     *
     * @param adoptionId the unique ID of the order
     * @param user       user invoking the action
     * @return the order if found
     */
    public AdoptionOrder getById(long adoptionId, User user);

    /**
     * Get adoption order with main relationships loaded, by unique ID
     *
     * @param adoptionId the unique ID of the order
     * @param user       user invoking the action
     * @return the order if found
     */
    public AdoptionOrder getWithRelationshipsById(long adoptionId, User user);

    /**
     * Get adoption order by the court ID and court order number
     *
     * @param courtUKey        the unique court ID
     * @param courtOrderNumber the number of the court order
     * @param user             user invoking the action
     * @return the unique adoption order
     */
    public AdoptionOrder getByCourtAndCourtOrderNumber(int courtUKey, String courtOrderNumber, User user);

    /**
     * Add a new adoption order
     *
     * @param adoption order to be added
     * @param user     user invoking the action
     */
    public void addAdoptionOrder(AdoptionOrder adoption, User user);

    /**
     * Update an adoption order
     *
     * @param adoption order being updated
     * @param user     user invoking the action
     */
    public void updateAdoptionOrder(AdoptionOrder adoption, User user);

    /**
     * Deleta an adoption order
     *
     * @param adoptionId adoption order unique key in data entry state to be deleted
     * @param user       user invoking the action
     */
    public void deleteAdoptionOrder(long adoptionId, User user);

    /**
     * Approve an adoption order
     *
     * @param adoptionId adoption order unique key to be approved
     * @param user       user invoking the action
     */
    public void approveAdoptionOrder(long adoptionId, User user);

    /**
     * Reject adoption order
     *
     * @param adoptionId adoption order unique key to be approved
     * @param user       user invoking the action
     */
    public void rejectAdoptionOrder(long adoptionId, User user);

    /**
     * @param adoption     Adoption Record representing the court order
     * @param serialNumber Serial number of the generated birth certificate
     * @param user         executing User
     */
    public void recordNewBirthDeclaration(AdoptionOrder adoption, long serialNumber, User user);

    /**
     * Set the information of applicant who is requesting the adoption certificate and set the status to
     * CERTIFICATE_ISSUE_REQUEST_CAPTURED which is 4.
     * pre condition : Status has to be NOTICE_LETTER_PRINTED, 2
     *
     * @param adoption adoption order being updated
     * @param user     user invoking the action
     */
    public void setApplicantInfo(AdoptionOrder adoption, User user);

    /**
     * set the status to NOTICE_LETTER_PRINTED, which is 2. pre condition : Status has to be on 1 (APPROVED)
     *
     * @param adoptionId adoption order unique key to be marked as printed
     * @param user       user invoking the action
     */
    public void setStatusToPrintedNotice(long adoptionId, User user);

    /**
     * set the status to  ADOPTION_CERTIFICATE_PRINTED, which is 5.
     * pre condition : Status need to be  CERTIFICATE_ISSUE_REQUEST_CAPTURED which is 4.
     *
     * @param adoptionId adoption order unique key to be marked as certificate printed
     * @param user       user invoking the action
     */
    public void setStatusToPrintedCertificate(long adoptionId, User user);

    /**
     * Returns all records (page by page) which are in the given state
     *
     * @param pageNo   page number
     * @param noOfRows number of rows
     * @param status   selected status for filtering
     * @param user     user invoking the action
     * @return matching records
     */
    public List<AdoptionOrder> getPaginatedListForState(int pageNo, int noOfRows, AdoptionOrder.State status, User user);

    /**
     * Returns a paginated list of all records
     *
     * @param pageNo   page number
     * @param noOfRows number of rows
     * @param user     user invoking the action
     * @return matching records
     */
    public List<AdoptionOrder> getPaginatedListForAll(int pageNo, int noOfRows, User user);

    /**
     * Check whether a given entry no is already within the system.
     *
     * @param adoptionEntryNo   Entered Entry No
     * @param user              User requesting to check the entry no
     * @return  TRUE (If Entry No exists). FALSE otherwise.
     */
    public boolean isEntryNoExist(long adoptionEntryNo, User user);
}
