package lk.rgd.crs.core.service;

import lk.rgd.AppConstants;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.common.util.PinAndNicUtils;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.api.dao.AdoptionOrderDAO;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

/**
 * Service API implementation to manage Adoption Orders and related activities
 *
 * @author Ashoka Ekanayaka
 *         <p/>
 *         TODO check if access priviledges needs to be performed? e.g. should these methods be only accessible from the
 *         Colombo office?
 */
public class AdoptionOrderServiceImpl implements AdoptionOrderService {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionOrderServiceImpl.class);
    private final AdoptionOrderDAO adoptionOrderDAO;

    private static final ResourceBundle rb_en = ResourceBundle.getBundle("messages/adoption_validation_messages_en", AppConstants.LK_EN);
    private static final ResourceBundle rb_si = ResourceBundle.getBundle("messages/adoption_validation_messages_si", AppConstants.LK_SI);
    private static final ResourceBundle rb_ta = ResourceBundle.getBundle("messages/adoption_validation_messages_ta", AppConstants.LK_TA);

    private final PopulationRegistry ecivil;

    AdoptionOrderServiceImpl(AdoptionOrderDAO adoptionOrderDAO, PopulationRegistry ecivil) {
        this.adoptionOrderDAO = adoptionOrderDAO;
        this.ecivil = ecivil;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AdoptionOrder getById(long adoptionId, User user) {
        return adoptionOrderDAO.getById(adoptionId);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AdoptionOrder getWithRelationshipsById(long adoptionId, User user) {
        AdoptionOrder ao = adoptionOrderDAO.getById(adoptionId);
        //trigger lazy loder handler by calling this lazy loading object
        ao.getLifeCycleInfo().getApprovalOrRejectUser().getUserName();
        return ao;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public AdoptionOrder getByCourtAndCourtOrderNumber(int courtUKey, String courtOrderNumber, User user) {
        return adoptionOrderDAO.getByCourtAndCourtOrderNumber(courtUKey, courtOrderNumber);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addAdoptionOrder(AdoptionOrder adoption, User user) {
        logger.debug("Adding an adoption order for court order : {}", adoption.getCourtOrderNumber());
        businessValidations(adoption);
        adoption.setStatus(AdoptionOrder.State.DATA_ENTRY);
        adoptionOrderDAO.addAdoptionOrder(adoption, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAdoptionOrder(AdoptionOrder adoption, User user) {
        logger.debug("Update adoption order for court order : {}", adoption.getCourtOrderNumber());
        businessValidations(adoption);
        adoptionOrderDAO.updateAdoptionOrder(adoption, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAdoptionOrder(long idUKey, User user) {
        AdoptionOrder adoption = adoptionOrderDAO.getById(idUKey);
        if (adoption.getStatus() == AdoptionOrder.State.DATA_ENTRY) {
            adoptionOrderDAO.deleteAdoptionOrder(idUKey);
        } else {
            handleException("Cannot delete adoption order " + adoption.getIdUKey() +
                    " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> approveAdoptionOrder(long idUKey, boolean ignoreWarnings, User user) {
        logger.debug("Approve adoption order : {}", idUKey);
        AdoptionOrder adoption = adoptionOrderDAO.getById(idUKey);
        // Adoption Record should be in DATA_ENTRY state for approval
        if (AdoptionOrder.State.DATA_ENTRY != adoption.getStatus()) {
            handleException("Cannot approve adoption order " + adoption.getIdUKey() +
                " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        validateAccess(user);
        List<UserWarning> warnings = validateStandardRequirements(adoption, user);
        // Generate formatted comment for adoption record for the cases where there are warnings and the user selected to ignore them all and approve.
        if (!warnings.isEmpty() && ignoreWarnings) {
            StringBuilder formattedComment = new StringBuilder();
            if(adoption.getComments() != null){
                formattedComment.append(adoption.getComments()).append("\n");
            }
            formattedComment.append(DateTimeUtils.getISO8601FormattedString(new Date())).append(" - Approved adoption record ignoring warnings. User : ").
                            append(user.getUserId()).append("\n");

            for(UserWarning w: warnings){
                formattedComment.append(w.getSeverity());
                formattedComment.append("-");
                formattedComment.append(w.getMessage());
            }
            adoption.setComments(formattedComment.toString());
        }

        if (warnings.isEmpty() || ignoreWarnings) {
            adoption.setStatus(AdoptionOrder.State.APPROVED);
            adoption.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            adoption.getLifeCycleInfo().setApprovalOrRejectUser(user);
            adoptionOrderDAO.updateAdoptionOrder(adoption, user);
            logger.debug("Approved adoption {} Ignore warnings", idUKey);
        } else {
            logger.debug("Approval of adoption {} stopped due to warnings.", idUKey);
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void rejectAdoptionOrder(long idUKey, User user, String comments) {
        logger.debug("Rejecting adoption order : {}", idUKey);
        setApprovalStatus(idUKey, user, AdoptionOrder.State.REJECTED, comments);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void recordNewBirthDeclaration(AdoptionOrder adoption, long serialNumber, User user) {
        //todo permission check for this adoption and bdf
        adoption.setNewBirthCertificateNumber(serialNumber);
        adoption.getLifeCycleInfo().setCertificateGeneratedTimestamp(new Date());
        adoption.getLifeCycleInfo().setCertificateGeneratedUser(user);

        adoptionOrderDAO.updateAdoptionOrder(adoption, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void setApplicantInfo(AdoptionOrder adoption, User user) {
        logger.debug("Recording adoption certificate applicant information for : {}", adoption.getIdUKey());

        AdoptionOrder adopt = getById(adoption.getIdUKey(), user);
        if ((adopt.getStatus() != AdoptionOrder.State.NOTICE_LETTER_PRINTED) ||
                (adoption.getStatus() != AdoptionOrder.State.NOTICE_LETTER_PRINTED)) {
            handleException("Cannot change status to certificate issue request captured, " + adoption.getIdUKey() +
                    " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        adoption.setStatus(AdoptionOrder.State.CERTIFICATE_ISSUE_REQUEST_CAPTURED);
        adoptionOrderDAO.updateAdoptionOrder(adoption, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void setStatusToPrintedAdoptionOrderDetails(long adoptionId, User user) {
        AdoptionOrder adoption = getById(adoptionId, user);
        if (adoption.getStatus() != AdoptionOrder.State.APPROVED) {
            handleException("Cannot change status to adoption order details printed, " + adoption.getIdUKey() +
                    " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        adoption.setStatus(AdoptionOrder.State.ORDER_DETAILS_PRINTED);
        adoptionOrderDAO.updateAdoptionOrder(adoption, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void setStatusToPrintedNotice(long adoptionId, User user) {
        AdoptionOrder adoption = getById(adoptionId, user);
        if (adoption.getStatus() != AdoptionOrder.State.ORDER_DETAILS_PRINTED) {
            handleException("Cannot change status to notice letter printed, " + adoption.getIdUKey() +
                    " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        adoption.setStatus(AdoptionOrder.State.NOTICE_LETTER_PRINTED);
        adoptionOrderDAO.updateAdoptionOrder(adoption, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void setStatusToPrintedCertificate(long adoptionId, User user) {
        AdoptionOrder adoption = getById(adoptionId, user);
        if (adoption.getStatus() != AdoptionOrder.State.CERTIFICATE_ISSUE_REQUEST_CAPTURED) {
            handleException("Cannot change status to adoption certificate printed, " + adoption.getIdUKey() +
                    " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }

        adoption.setStatus(AdoptionOrder.State.ADOPTION_CERTIFICATE_PRINTED);
        adoptionOrderDAO.updateAdoptionOrder(adoption, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<AdoptionOrder> getPaginatedListForState(int pageNo, int noOfRows,
                                                        AdoptionOrder.State status, User user) {
        try {
            return adoptionOrderDAO.getPaginatedListForState(pageNo, noOfRows, status);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<AdoptionOrder> getPaginatedListForAll(int pageNo, int noOfRows, User user) {
        try {
            logger.debug("Adoptions getPaginatedListForAll: {}");
            return adoptionOrderDAO.getPaginatedListForAll(pageNo, noOfRows);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean isEntryNoExist(long adoptionEntryNo, User user) {
        logger.debug("Look for Adoption Entry No: {} by {}", adoptionEntryNo, user.getUserId());
        return adoptionOrderDAO.isEntryNoExist(adoptionEntryNo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Long getLastEntryNo(User user) {
        logger.debug("{} is requesting the latest adoptionEntryNo", user.getUserId());
        return adoptionOrderDAO.getLastEntryNo();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdoptionOrder> searchAdoptionOrder(Long adoptionEntryNo, String courtOrderNumber, int courtUKey, String childName, Date childBirthDate) {
        logger.debug("Search Adoption");
        List<AdoptionOrder> searchResults = new ArrayList<AdoptionOrder>();
        searchResults = adoptionOrderDAO.searchAdoptionRecords(adoptionEntryNo, courtOrderNumber, courtUKey, childName,childBirthDate);

        if (searchResults != null && searchResults.size() > 0) {
            return searchResults;
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AdoptionOrder getAdoptionByCourtOrderNumberAndEntryNumber(String courtOrderNumber, long adoptionEntryNo) {
        logger.debug("Looking for Adoption Order with the Entry No: {} and Court Order No: {}", adoptionEntryNo, courtOrderNumber);
        return adoptionOrderDAO.getAdoptionByCourtOrderNumberAndEntryNumber(courtOrderNumber, adoptionEntryNo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AdoptionOrder getAdoptionByEntryNumber(long adoptionEntryNo) {
        return adoptionOrderDAO.getAdoptionByEntryNumber(adoptionEntryNo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdoptionOrder> getHistoryRecords(long adoptionEntryNo) {
        return adoptionOrderDAO.getHistoryRecords(adoptionEntryNo);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdoptionOrder> getAdoptionOrdersByCourtOrderNumber(String courtOrderNo) {
        List<AdoptionOrder> adoptionOrders = adoptionOrderDAO.getAdoptionsByCourtOrderNumber(courtOrderNo);
        if (adoptionOrders.size() > 0) {
            return adoptionOrders;
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AdoptionOrder getAdoptionByEntryNumberForAlteration(long adoptionEntryNo) {
        return adoptionOrderDAO.getAdoptionByEntryNumberForAlteration(adoptionEntryNo, AdoptionOrder.State.APPROVED, AdoptionOrder.State.RE_REGISTRATION_REQUESTED);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdoptionOrder> getAdoptionsByCourtOrderNumberForAlterations(String courtOrderNumber) {
        //List<AdoptionOrder> adoptionOrders = adoptionOrderDAO.getAdoptionsByCourtOrderNumberForAlteration(courtOrderNumber, AdoptionOrder.State.APPROVED, AdoptionOrder.State.RE_REGISTRATION_REQUESTED);
        List<AdoptionOrder> adoptionOrders = adoptionOrderDAO.getAdoptionsByCourtOrderNumberAndState(courtOrderNumber, AdoptionOrder.State.ADOPTION_CERTIFICATE_PRINTED);

        if (adoptionOrders.size() > 0) {
            return adoptionOrders;
        } else {
            return Collections.emptyList();
        }
    }

    private void setApprovalStatus(long idUKey, User user, AdoptionOrder.State state, String comments) {
        AdoptionOrder adoption = adoptionOrderDAO.getById(idUKey);
        if (AdoptionOrder.State.DATA_ENTRY != adoption.getStatus()) {
            handleException("Cannot approve/reject adoption order " + adoption.getIdUKey() +
                    " Illegal state : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        validateAccess(user);

        adoption.setStatus(state);
        adoption.setComments(comments);
        adoption.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
        adoption.getLifeCycleInfo().setApprovalOrRejectUser(user);
        adoptionOrderDAO.updateAdoptionOrder(adoption, user);
        logger.debug("Updated adoption order : {}", idUKey);
    }

    private void businessValidations(AdoptionOrder adoption) {
        /*todo remove no need this validation for adding DAO set state DE
     if (adoption.getStatus() != AdoptionOrder.State.DATA_ENTRY) {
            handleException("can not update adoption order " + adoption.getIdUKey() +
                    " Illegal State : " + adoption.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }*/

        if ((adoption.getChildNewName() == null) && (adoption.getChildExistingName() == null)) {
            handleException("can not update adoption order " + adoption.getIdUKey() +
                    " A Name not given : " + adoption.getStatus(), ErrorCodes.INVALID_DATA);
        }
    }

    private void validateAccess(User user) {
        String role = user.getRole().getRoleId();
        if (!(User.State.ACTIVE == user.getStatus()) ||
                !(Role.ROLE_ARG.equals(role) || Role.ROLE_RG.equals(role))) {
            handleException("User : " + user.getUserId() + " of role : " + role +
                    " is not allowed access to approve/reject an adoption : ", ErrorCodes.PERMISSION_DENIED);
        }

        if (!user.isAuthorized(Permission.APPROVE_ADOPTION)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject Adoptions",
                    ErrorCodes.PERMISSION_DENIED);
        }

    }

    private List<UserWarning> validateStandardRequirements(AdoptionOrder adoptionOrder, User user) {
        List<UserWarning> warningList = new ArrayList<UserWarning>();
        ResourceBundle rb = rb_en;
        if (AppConstants.SINHALA.equals(user.getPrefLanguage())) {
            rb = rb_si;
        } else if (AppConstants.TAMIL.equals(user.getPrefLanguage())) {
            rb = rb_ta;
        }

        // Check whether the court order number already used for another entry.
        if (adoptionOrderDAO.getAdoptionsByCourtOrderNumber(adoptionOrder.getCourtOrderNumber()).size() > 1) {
            warningList.add(new UserWarning(MessageFormat.format(rb.getString("used_court_order_number"), adoptionOrder.getCourtOrderNumber())));
        }

        // Check whether there are other records with the same child name.
        if(adoptionOrder.getChildExistingName() != null && !adoptionOrder.getChildExistingName().isEmpty()){
            if(adoptionOrderDAO.getAdoptionsWithSameChildName(adoptionOrder.getChildExistingName(), adoptionOrder.getAdoptionEntryNo()).size() > 0){
                warningList.add(new UserWarning(MessageFormat.format(rb.getString("duplicate_with_child_existing_name"), adoptionOrder.getChildExistingName())));
            }
        }

        // Check for the parents names.
        if(adoptionOrder.getApplicantName() != null && !adoptionOrder.getApplicantName().isEmpty()){
            if(adoptionOrder.isJointApplicant() && adoptionOrder.getSpouseName() != null && !adoptionOrder.getSpouseName().isEmpty()){
                if(adoptionOrderDAO.getAdoptionsWithSameParentNames(adoptionOrder.getApplicantName(), adoptionOrder.getSpouseName(), adoptionOrder.getAdoptionEntryNo()).size() > 0){
                    warningList.add(new UserWarning(MessageFormat.format(rb.getString("duplicate_parent_names"), adoptionOrder.getApplicantName(), adoptionOrder.getSpouseName())));
                }
            }else{
                if(adoptionOrderDAO.getAdoptionsWithSameApplicantName(adoptionOrder.getApplicantName(), adoptionOrder.getAdoptionEntryNo()).size() > 0){
                    warningList.add(new UserWarning(MessageFormat.format(rb.getString("duplicate_applicant_name"), adoptionOrder.getApplicantName())));
                }
            }
        }

        // Check the applicant PIN or NIC
        String pinOrNIC = adoptionOrder.getApplicantPINorNIC();
        if (!PinAndNicUtils.isValidPINorNIC(pinOrNIC, ecivil, user)) {
            warningList.add(new UserWarning(MessageFormat.format(rb.getString("invalid_applicant_pin_or_nic"), pinOrNIC)));
        }

        // Check spouse PIN or NIC
        if (adoptionOrder.isJointApplicant()) {
            pinOrNIC = adoptionOrder.getSpousePINorNIC();
            if (!PinAndNicUtils.isValidPINorNIC(pinOrNIC, ecivil, user)) {
                warningList.add(new UserWarning(MessageFormat.format(rb.getString("invalid_spouse_pin_or_nic"), pinOrNIC)));
            }
        }

        // TODO check other relevant adoption requirements and generate warning messages

        return warningList;
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    public List<AdoptionOrder> generateAdoptionReports(int courtId, Date dataEntryDateFrom, Date dataEntryDateTo) {
        logger.debug("Court Id Service: {} \t Court ID : {}", courtId);
        List<AdoptionOrder> searchResults = new ArrayList<AdoptionOrder>();
        if (courtId > 0 && dataEntryDateFrom != null && dataEntryDateTo != null) {
            searchResults = adoptionOrderDAO.getAdoptionsByCourtIdAndDataEntryPeriod(courtId, dataEntryDateFrom, dataEntryDateTo);
        }
        else if(courtId==0 && dataEntryDateFrom != null && dataEntryDateTo != null){
            searchResults = adoptionOrderDAO.getAdoptionsByDataEntryPeriod(dataEntryDateFrom, dataEntryDateTo);
        }
        else if(courtId>0){
            searchResults = adoptionOrderDAO.getAdoptionsByCourt(courtId);
        }
        return searchResults;
    }
}
