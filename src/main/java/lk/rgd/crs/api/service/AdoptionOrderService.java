package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.AdoptionOrder;

import java.util.Date;
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
     * @param ignoreWarnings Whether user choose to ignore the warnings
     * @return List of Warning messages. (if any)
     */
    public List<UserWarning> approveAdoptionOrder(long adoptionId, boolean ignoreWarnings, User user);

    /**
     * Reject adoption order
     *
     * @param adoptionId adoption order unique key to be approved
     * @param user       user invoking the action
     * @param comments       user entered comments for reject
     */
    public void rejectAdoptionOrder(long adoptionId, User user, String comments);

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

    public void setStatusToPrintedAdoptionOrderDetails(long adoptionId, User user);
    
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

    /**
     * Returns the latest adoptionEntryNo
     *
     * @param user User requesting the adoptionEntryNo.
     * @return Latest adoptionEntryNo
     */
    public Long getLastEntryNo(User user);

    /**
     * Returns a List of adoption records matches the given parameters
     *
     * @param adoptionEntryNo   Selected Adoption Entry Number
     * @param courtOrderNumber  Selected Court Order Number
     * @param courtUKey         CourtUKey of the selected court
     * @param childName        nem childName
     * @param childBirthDate        nem childName
     * @return List of adoption records match the given parameters.
     */
    public List<AdoptionOrder> searchAdoptionOrder(Long adoptionEntryNo, String courtOrderNumber, int courtUKey, String childName, Date childBirthDate);

    /**
     * Return the adoption record which is entered as the given entry number and court order number.
     *
     * @param courtOrderNumber
     * @param adoptionEntryNo
     * @return
     */
    public AdoptionOrder getAdoptionByCourtOrderNumberAndEntryNumber(String courtOrderNumber, long adoptionEntryNo);

    /**
     * Returns an adoption record which is entered as the given entry number.
     *
     * @param adoptionEntryNo Selected adoption entry number.
     * @return Adoption records which has the given entry number
     */
    public AdoptionOrder getAdoptionByEntryNumber(long adoptionEntryNo);

    /**
     *
     * @param adoptionEntryNo
     * @return
     */
    public List<AdoptionOrder> getHistoryRecords(long adoptionEntryNo);

    /**
     * Returns a list of adoption records with the given court order number.
     *
     * @param courtOrderNo  Court Order Number
     * @return              List of Adoption records.
     */
    public List<AdoptionOrder> getAdoptionOrdersByCourtOrderNumber(String courtOrderNo);

    /**
     * Returns an adoption record which is entered as the given entry number and in ADOPTION_CERTIFICATE_PRINTED state
     *
     * @param adoptionEntryNo
     * @return
     */
    public AdoptionOrder getAdoptionByEntryNumberForAlteration(long adoptionEntryNo);

    /**
     * Returns a list of adoption records which is under the given court order number and in the ADOPTION_CERTIFICATE_PRINTED state.
     * @param courtOrderNumber
     * @return
     */
    public List<AdoptionOrder> getAdoptionsByCourtOrderNumberForAlterations(String courtOrderNumber);

    /**
     * Returns a list of adoption records according to the court(i.e if a court has not been selected then show records of all courts) and entered date period.
     *
     * @param courtId Court Id
     * @param dataEntryDateFrom  Start date of the data entry period.
     * @param dataEntryDateTo    End date of the data entry period.
     *
     * @return   List of Adoption records.
     */
    public List<AdoptionOrder> generateAdoptionReports(int courtId,Date dataEntryDateFrom,Date dataEntryDateTo);


}
