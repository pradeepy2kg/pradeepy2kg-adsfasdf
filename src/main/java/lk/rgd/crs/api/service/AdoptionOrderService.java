package lk.rgd.crs.api.service;

import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.common.api.domain.User;

import java.util.List;

/**
 * @author Ashoka Ekanayaka
 */
public interface AdoptionOrderService {
    public AdoptionOrder getById(long adoptionId, User user);

    public AdoptionOrder getByCourtOrderNumber(String courtOrderNumber, User user);

    public void addAdoptionOrder(AdoptionOrder adoption, User user);

    public void updateAdoptionOrder(AdoptionOrder bdf, User user);

    public List<AdoptionOrder> findAll(User user);

    public void deleteAdoptionOrder(long adoptionId, User user);

    public void approveAdoptionOrder(long adoptionId, User user);

    public void rejectAdoptionOrder(long adoptionId, User user);

    /**
     * Set the information of applicant who is requesting the adoption certificate and set the status to
     * CERTIFICATE_ISSUE_REQUEST_CAPTURED which is 4.
     * pre condition : Status has to be NOTICE_LETTER_PRINTED, 2
     * @param adoption
     * @param user
     */
    public void setApplicantInfo(AdoptionOrder adoption, User user);

    /**
     * set the status to NOTICE_LETTER_PRINTED, which is 2. pre condition : Status has to be on 1 (APPROVED)
     * @param adoptionId
     * @param user
     */
    public void setStatusToPrintedNotice(long adoptionId, User user);

    /**
     * set the status to  ADOPTION_CERTIFICATE_PRINTED, which is 5.
     *  pre condition : Status need to be  CERTIFICATE_ISSUE_REQUEST_CAPTURED which is 4.
     * @param adoptionId
     * @param user
     */
    public void setStatusToPrintedCertificate (long adoptionId, User user);

    /**
     *  Returns all records (page by page) which are in the given state
     * @param pageNo
     * @param noOfRows
     * @param status
     * @param user
     * @return
     */
    public List<AdoptionOrder> getPaginatedListForState(int pageNo, int noOfRows, AdoptionOrder.State status, User user);

    /**
     * Returns a paginated list of all records
     * @param pageNo
     * @param noOfRows
     * @param user
     * @return
     */
    public List<AdoptionOrder> getPaginatedListForAll(int pageNo, int noOfRows, User user);
}
