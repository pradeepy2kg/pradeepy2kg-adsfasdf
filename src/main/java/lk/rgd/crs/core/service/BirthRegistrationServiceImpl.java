package lk.rgd.crs.core.service;

import lk.rgd.Permission;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.ErrorCodes;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The central service managing the CRS Birth Registration process
 */
public class BirthRegistrationServiceImpl implements BirthRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegistrationServiceImpl.class);
    private static final DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final CountryDAO countryDAO;
    private final RaceDAO raceDAO;

    public BirthRegistrationServiceImpl(
        BirthDeclarationDAO birthDeclarationDAO, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO,
        BDDivisionDAO bdDivisionDAO, CountryDAO countryDAO, RaceDAO raceDAO) {
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> addNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user,
        String caseFileNumber, String additionalDocumentsComment) {
        // does the user have access to the BDF being added (i.e. check district and DS division)
        // TODO if a mother is specified, is she alive? etc
        // TODO add case file number and additional document list as comments
        if (!ignoreWarnings) {
            // TODO more validations .. like bdf.getParent().getMotherFullName() != null etc
        }
        validateAccessOfUser(user, bdf);
        bdf.getRegister().setStatus(BirthDeclaration.State.DATA_ENTRY);
        birthDeclarationDAO.addBirthDeclaration(bdf);
        return null;
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> approveBirthDeclarationIdList(long[] approvalDataList, User user) {

        if (!user.isAuthorized(Permission.APPROVE_BDF)) {
            handleException("The user : " + user.getUserId() +
                " is not authorized to approve birth declarations", ErrorCodes.PERMISSION_DENIED);
        }

        List<UserWarning> warnings = new ArrayList<UserWarning>();
        for (long id : approvalDataList) {
            BirthDeclaration bdf = birthDeclarationDAO.getById(id);
            List<UserWarning> w = approveBirthDeclaration(bdf, false, user);
            if (!w.isEmpty()) {
                warnings.add(new UserWarning("Birth Declaration ID : " + id +
                    " must be approved after validating warnings"));
            }
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    public void updateNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        // does the user have access to the BDF being updated
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        // TODO check validations as per addNormalBirthDeclaration

        // a BDF can be edited by a DEO or ADR only before being approved
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (currentState == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.updateBirthDeclaration(bdf);
        } else {
            handleException("Cannot modify birth declaration : " + existing.getIdUKey() +
                " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    public void deleteNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        // does the user have access to the BDF being deleted
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        // a BDF can be edited by a DEO or ADR only before being approved
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (currentState == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.deleteBirthDeclaration(bdf.getIdUKey());
        } else {
            handleException("Cannot delete birth declaration " + existing.getIdUKey() +
                " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> approveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        // check approve permission
        if (!user.isAuthorized(Permission.APPROVE_BDF)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth declarations",
                ErrorCodes.PERMISSION_DENIED);
        }

        // is the BDF currently existing in a state for approval
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (BirthDeclaration.State.DATA_ENTRY != currentState) {
            handleException("Cannot approve confirmation : " + bdf.getIdUKey() + " Illegal state : " + currentState,
                ErrorCodes.INVALID_STATE_FOR_BDF_APPROVAL);
        }

        // validate if the minimum required fields are adequately filled
        BirthDeclarationValidator.validateMinimalRequirements(bdf);

        // validate standard validations anyway, since even if validations are rejected a note of it will be made
        // against the approval for audit requirements
        List<UserWarning> warnings = BirthDeclarationValidator.validateStandardRequirements(birthDeclarationDAO, bdf, user);

        if (!warnings.isEmpty() && ignoreWarnings) {
            StringBuilder sb = new StringBuilder();
            if (existing.getRegister().getComments() != null) {
                sb.append(existing.getRegister().getComments()).append("\n");
            }

            // SimpleDateFormat is not thread-safe
            synchronized (dfm) {
                sb.append(dfm.format(new Date())).append(" - Approved birth declaration ignoring warnings. User : ").
                    append(user.getUserId());
            }
            bdf.getRegister().setComments(sb.toString());
        }

        if (warnings.isEmpty() || ignoreWarnings) {
            bdf.getRegister().setStatus(BirthDeclaration.State.APPROVED);
            birthDeclarationDAO.updateBirthDeclaration(bdf);
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    public void markBirthDeclarationAsConfirmedWithoutChanges(BirthDeclaration bdf, User user) {

        // does the user have access to the BDF being confirmed (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        // to ensure correctness, modify the existing copy and not update to whats passed to us
        // i.e. only update the confirmant information
        existing.setConfirmant(bdf.getConfirmant());

        final BirthDeclaration.State currentState = bdf.getRegister().getStatus();
        if (BirthDeclaration.State.CONFIRMATION_PRINTED == currentState) {
            bdf.getRegister().setStatus(BirthDeclaration.State.CONFIRMED_WITHOUT_CHANGES);
            birthDeclarationDAO.updateBirthDeclaration(bdf);
        } else {
            handleException("Cannot approve confirmation : " + bdf.getIdUKey() + " Illegal state : " + currentState,
                ErrorCodes.INVALID_STATE_FOR_BDF_CONFIRMATION);
        }
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> approveChangedConfirmationIDList(long[] approvalDataList, User user) {

        if (!user.isAuthorized(Permission.APPROVE_BDF_CONFIRMATION)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth confirmation",
                ErrorCodes.PERMISSION_DENIED);
        }

        List<UserWarning> warnings = new ArrayList<UserWarning>();
        for (long id : approvalDataList) {
            BirthDeclaration bdf = birthDeclarationDAO.getById(id);
            List<UserWarning> w = approveConfirmationChanges(bdf, false, user);
            if (!w.isEmpty()) {
                warnings.add(new UserWarning("Birth Declaration Confirmation with ID : " + id +
                    " must be approved after validating warnings"));
            }
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> approveConfirmationChanges(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        // check approve permission
        if (!user.isAuthorized(Permission.APPROVE_BDF_CONFIRMATION)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth confirmation",
                ErrorCodes.PERMISSION_DENIED);
        }

        final BirthDeclaration.State currentState = bdf.getRegister().getStatus();
        if (BirthDeclaration.State.CONFIRMATION_PRINTED == currentState ||
            BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED == currentState) {

            List<UserWarning> warnings = prepareForConfirmation(bdf, ignoreWarnings, user);
            if (warnings.isEmpty() || ignoreWarnings) {
                bdf.getRegister().setStatus(BirthDeclaration.State.CONFIRMATION_CHANGES_APPROVED);
                birthDeclarationDAO.updateBirthDeclaration(bdf);
            }
            return warnings;
        } else {
            handleException("Cannot approve confirmation : " + bdf.getIdUKey() + " Illegal state : " + currentState,
                ErrorCodes.INVALID_STATE_FOR_BDF_CONFIRMATION);
        }
        return null;
    }

    private List<UserWarning> prepareForConfirmation(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        // does the user have access to the BDF being confirmed (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        // create a holder to capture any warnings
        List<UserWarning> warnings = new ArrayList<UserWarning>();

        if (!ignoreWarnings) {
            // check if this is a duplicate by checking dateOfBirth and motherNICorPIN
            if (bdf.getParent() != null && bdf.getParent().getMotherNICorPIN() != null) {
                List<BirthDeclaration> existingRecords = birthDeclarationDAO.getByDOBandMotherNICorPIN(
                    bdf.getChild().getDateOfBirth(), bdf.getParent().getMotherNICorPIN());

                for (BirthDeclaration b : existingRecords) {
                    if (b.getIdUKey() != bdf.getIdUKey()) {
                        warnings.add(
                            new UserWarning("Possible duplicate with record ID : " + b.getIdUKey() +
                                " Registered on : " + b.getRegister().getDateOfRegistration() +
                                " Child name : " + b.getChild().getChildFullNameOfficialLangToLength(20)));
                    }
                }
            }
        }

        if (!warnings.isEmpty() && ignoreWarnings) {
            StringBuilder sb = new StringBuilder();
            if (existing.getRegister().getComments() != null) {
                sb.append(existing.getRegister().getComments()).append("\n");
            }

            // SimpleDateFormat is not thread-safe
            synchronized (dfm) {
                sb.append(dfm.format(new Date())).append(" - Approved birth confirmation ignoring warnings. User : ").
                    append(user.getUserId());
            }
            bdf.getRegister().setComments(sb.toString());
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    public void rejectBirthDeclaration(BirthDeclaration bdf, String comments, User user) {
        if (comments == null || comments.trim().length() < 1) {
            handleException("A comment is required to reject a birth declaration",
                ErrorCodes.COMMENT_REQUIRED_BDF_REJECT);
        }

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        // check state of record
        BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (BirthDeclaration.State.CONFIRMATION_PRINTED == currentState ||
            BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED == currentState ||
            BirthDeclaration.State.DATA_ENTRY == currentState) {

            // check approve/reject permission
            if (!user.isAuthorized(Permission.APPROVE_BDF)) {
                handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth declarations",
                    ErrorCodes.PERMISSION_DENIED);
            }
            bdf.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_REJECTED);
            birthDeclarationDAO.updateBirthDeclaration(bdf);

        } else {
            handleException("Cannot reject birth declaration / confirmation : " + bdf.getIdUKey() +
                " Illegal state : " + currentState, ErrorCodes.INVALID_STATE_FOR_BDF_REJECTION);
        }
    }

    private void validateAccessOfUser(User user, BirthDeclaration bdf) {
        BDDivision bdDivision = bdf.getRegister().getBirthDivision();
        if (!user.isAllowedAccessToBDDistrict(bdDivision.getDistrict().getDistrictUKey()) &&
            !user.isAllowedAccessToBDDSDivision(bdDivision.getDsDivision().getDsDivisionUKey())) {

            handleException("User : " + user.getUserId() + " is not allowed access to the District : " +
                bdDivision.getDistrict().getDistrictId() + " and/or DS Division : " +
                bdDivision.getDsDivision().getDivisionId(), ErrorCodes.PERMISSION_DENIED);
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    /**
     * @inheritDoc
     */
    public BirthDeclaration getById(long bdId, User user) {
        BirthDeclaration bdf = birthDeclarationDAO.getById(bdId);
        // does the user have access to the BDF (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        return bdf;
    }

    /**
     * @inheritDoc
     */
    public BirthDeclaration getByBDDivisionAndSerialNo(BDDivision bdDivision, long serialNo, User user) {
        BirthDeclaration bdf = birthDeclarationDAO.getByBDDivisionAndSerialNo(bdDivision, serialNo);
        // does the user have access to the BDF (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        return bdf;
    }

    /**
     * @inheritDoc
     */
    public BirthDeclaration getByBDDivisionAndSerialNo(BDDivision bdDivision, long serialNo) {
        return birthDeclarationDAO.getByBDDivisionAndSerialNo(bdDivision, serialNo);
    }

    public List<BirthDeclaration> getConfirmationApprovalPending(BDDivision birthDivision, int pageNo, int noOfRows) {
        return birthDeclarationDAO.getConfirmationApprovalPending(birthDivision, pageNo, noOfRows);
    }

    public List<BirthDeclaration> getConfirmPrintList(BDDivision birthDivision, int pageNo, int noOfRows, boolean printed) {
        return birthDeclarationDAO.getConfirmPrintList(birthDivision,pageNo,noOfRows,printed);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getDeclarationApprovalPending(BDDivision birthDivision, int pageNo, int noOfRows) {
        return birthDeclarationDAO.getDeclarationApprovalPending(birthDivision, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getDeclarationPendingByBDDivisionAndRegisterDateRange(BDDivision birthDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows) {
        return birthDeclarationDAO.getByBDDivisionStatusAndRegisterDateRange(
            birthDivision, BirthDeclaration.State.DATA_ENTRY, startDate, endDate, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getByBDDivisionStatusAndConfirmationReceiveDateRange(BDDivision birthDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows) {
        return birthDeclarationDAO.getByBDDivisionStatusAndConfirmationReceiveDateRange(
            birthDivision, BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED, startDate, endDate, pageNo, noOfRows);
    }

    /**
     * Populates transient string values for Country, Race, District, Division etc
     *
     * @param bdf the BirthDeclaration to populate transient values
     * @return populated BDF
     */
    public BirthDeclaration loadValuesForPrint(BirthDeclaration bdf) {
        String prefLanguage = bdf.getRegister().getPreferredLanguage();

        ChildInfo child = bdf.getChild();
        child.setChildGenderPrint(GenderUtil.getGender(bdf.getChild().getChildGender(), prefLanguage));

        BirthRegisterInfo brInfo = bdf.getRegister();
        if (brInfo.getOriginalBCPlaceOfIssue() != null) {
            brInfo.setOriginalBCPlaceOfIssuePrint(dsDivisionDAO.getNameByPK(brInfo.getOriginalBCPlaceOfIssue(), prefLanguage));
        }
        if (brInfo.getBirthDivision() != null) {
            brInfo.setDistrictPrint(districtDAO.getNameByPK(brInfo.getBirthDistrict().getDistrictUKey(), prefLanguage));
            brInfo.setDsDivisionPrint(dsDivisionDAO.getNameByPK(brInfo.getDsDivision().getDsDivisionUKey(), prefLanguage));
            brInfo.setBdDivisionPrint(bdDivisionDAO.getNameByPK(brInfo.getBirthDivision().getBdDivisionUKey(), prefLanguage));
        }

        ParentInfo parent = bdf.getParent();
        if (parent != null) {
            if (parent.getFatherCountry() != null) {
                parent.setFatherCountryPrint(
                    countryDAO.getNameByPK(parent.getFatherCountry().getCountryId(), prefLanguage));
            }
            if (parent.getMotherCountry() != null) {
                parent.setMotherCountryPrint(
                    countryDAO.getNameByPK(parent.getMotherCountry().getCountryId(), prefLanguage));
            }
            if (parent.getFatherRace() != null) {
                parent.setFatherRacePrint(
                    raceDAO.getNameByPK(parent.getFatherRace().getRaceId(), prefLanguage));
            }
            if (parent.getMotherRace() != null) {
                parent.setMotherRacePrint(
                    raceDAO.getNameByPK(parent.getMotherRace().getRaceId(), prefLanguage));
            }

            if (parent.getMotherDSDivision() != null) {
                parent.setMotherDistrictPrint(
                    districtDAO.getNameByPK(parent.getMotherDSDivision().getDistrict().getDistrictUKey(), prefLanguage));
                parent.setMotherDsDivisionPrint(
                    dsDivisionDAO.getNameByPK(parent.getMotherDSDivision().getDsDivisionUKey(), prefLanguage));
            }
        }

        return bdf;
    }
}
