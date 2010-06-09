package lk.rgd.crs.core.service;

import lk.rgd.Permission;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.ErrorCodes;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BirthRegistrationServiceImpl implements BirthRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegistrationServiceImpl.class);
    private static final DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private final BirthDeclarationDAO birthDeclarationDAO;

    public BirthRegistrationServiceImpl(BirthDeclarationDAO dao) {
        birthDeclarationDAO = dao;
    }

    /**
     * Add a Birth declaration to the system. This is a Data Entry operation, and only data entry level validations
     * are performed at this stage. The [ADR] Approval will trigger a manual / human approval after validating any
     * warnings by an ADR or higher level authority
     *
     * @param bdf the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user the user initiating the action
     */
    public void addNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        birthDeclarationDAO.addBirthDeclaration(bdf);
    }

    /**
     * Approve a list of BDF forms. Will only approve those that triggers no warnings. The result will contain
     * information on the warnings returned.
     * @param approvalDataList a list of the unique BDF IDs to be approved in batch
     * @param user the user approving the BDFs
     * @return a list of warnings for those that trigger warnings during approval
     */
    public List<UserWarning> approveBirthDeclarationIdList(long[] approvalDataList, User user) {

        if (user.isAuthorized(Permission.APPROVE_BDF)) {
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

    public void updateNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        // does the user have access to the BDF being updated
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        // a BDF can be edited by a DEO or ADR only before being approved
        if (existing.getRegister().getStatus() == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.updateBirthDeclaration(bdf);
        } else {
            handleException("Cannot modify birth declaration " + existing.getIdUKey() +
                "after its approved", ErrorCodes.ILLEGAL_STATE);
        }
    }

    public void deleteNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        // does the user have access to the BDF being deleted
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        // a BDF can be edited by a DEO or ADR only before being approved
        if (existing.getRegister().getStatus() == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.deleteBirthDeclaration(bdf.getIdUKey());
        } else {
            handleException("Cannot delete birth declaration " + existing.getIdUKey() +
                " after its approved", ErrorCodes.ILLEGAL_STATE);
        }
    }

    public List<UserWarning> approveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        // create a holder to capture any warnings
        List<UserWarning> warnings = new ArrayList<UserWarning>();

        if (!ignoreWarnings) {
            // check if this is a duplicate by checking dateOfBirth and motherNICorPIN
            if (bdf.getParent().getMotherNICorPIN() != null) {
                List<BirthDeclaration> existingRecords = birthDeclarationDAO.getByDOBandMotherNICorPIN(
                    bdf.getChild().getDateOfBirth(), bdf.getParent().getMotherNICorPIN());

                for (BirthDeclaration b : existingRecords) {
                    warnings.add(
                        new UserWarning("Possible duplicate with record ID : " + b.getIdUKey() +
                            " Registered on : " + b.getRegister().getDateOfRegistration() +
                            " Child name : " + b.getChild().getChildFullNameOfficialLangToLength(20)));
                }
            }
        }

        if (ignoreWarnings) {
            StringBuilder sb = new StringBuilder();
            if (existing.getRegister().getComments() != null) {
                sb.append(existing.getRegister().getComments()).append("\n");
            }

            // SimpleDateFormat is not thread-safe
            synchronized (dfm) {
                sb.append(dfm.format(new Date()) + " - Approved birth declaration ignoring warnings. User : " +
                    user.getUserId());
            }
            bdf.getRegister().setComments(sb.toString());
        }
        
        bdf.getRegister().setStatus(BirthDeclaration.State.APPROVED);
        birthDeclarationDAO.updateBirthDeclaration(bdf);
        return warnings;
    }

    private void validateAccessOfUser(User user, BirthDeclaration bdf) {
        BDDivision bdDivision = bdf.getRegister().getBirthDivision();

//        if (user.isAllowedAccessToDistrict(bdDivision.getDistrict().getDistrictId()) &&
//            user.isAllowedAccessToDSDivision(bdDivision.getDsDivision().getDivisionId())) {
//
//        } else {
//            handleException("User : " + user.getUserId() + " is not allowed access to the District : " +
//                bdDivision.getDistrict().getDistrictId() + " and/or DS Division : " +
//                bdDivision.getDsDivision().getDivisionId(), ErrorCodes.PERMISSION_DENIED);
//        }
        // TODO changed by chathuranga
        if (user.isAllowedAccessToDistrict(bdDivision.getDistrict().getDistrictUKey()) &&
            user.isAllowedAccessToDSDivision(bdDivision.getDsDivision().getDsDivisionUKey())) {

        } else {
            handleException("User : " + user.getUserId() + " is not allowed access to the District : " +
                bdDivision.getDistrict().getDistrictUKey() + " and/or DS Division : " +
                bdDivision.getDsDivision().getDsDivisionUKey(), ErrorCodes.PERMISSION_DENIED);
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    /**
     * Returns the Birth Declaration object for a given Id
     *
     * @param bdId Birth Declarion Id for the given declaration
     * @Return BirthDeclaration
     */
    public BirthDeclaration getById(long bdId) {
        return birthDeclarationDAO.getById(bdId);
    }

    /**
     * Returns the Birth Declaration object for a given bdf serialNo
     *
     * @param serialNo bdfSerialNo given to the Birth Declarion
     * @Return BirthDeclaration
     */
    public BirthDeclaration getBySerialNo(String serialNo) {
        return birthDeclarationDAO.getBySerialNo(serialNo);
    }

    public List<BirthDeclaration> getConfirmationApprovalPending(BDDivision birthDivision, int pageNo, int noOfRows) {
        return birthDeclarationDAO.getConfirmationApprovalPending(birthDivision,pageNo,noOfRows);  
    }

    public List<BirthDeclaration> getDeclarationApprovalPending(BDDivision birthDivision,int pageNo,int noOfRows){
        return birthDeclarationDAO.getDeclarationApprovalPending(birthDivision,pageNo,noOfRows);
    }


}
