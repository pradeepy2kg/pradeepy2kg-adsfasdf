package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BirthAlterationDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthAlterationService;
import lk.rgd.crs.core.ValidationUtils;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.prs.api.dao.PersonDAO;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Handles the birth alteration process
 *
 * @author Indunil Moremada
 * @author Asankha Perera (reviewed, reorganized and completed)
 */
public class BirthAlterationServiceImpl implements BirthAlterationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthAlterationServiceImpl.class);
    private final BirthAlterationDAO birthAlterationDAO;
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final PersonDAO personDAO;
    private final PopulationRegistry ecivil;

    public BirthAlterationServiceImpl(BirthAlterationDAO birthAlterationDAO, BirthDeclarationDAO birthDeclarationDAO,
                                      PopulationRegistry ecivil, PersonDAO personDAO) {
        this.birthAlterationDAO = birthAlterationDAO;
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.ecivil = ecivil;
        this.personDAO = personDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBirthAlteration(BirthAlteration ba, User user) {
        logger.debug("Adding new birth alteration record on request of : {}", ba.getDeclarant().getDeclarantFullName());
        ba.setSubmittedLocation(user.getPrimaryLocation());
        // any user (DEO, ADR of any DS office or BD division etc) can add a birth alteration request
        birthAlterationDAO.addBirthAlteration(ba, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBirthAlteration(BirthAlteration ba, User user) {
        logger.debug("Attempt to edit birth alteration record : {}", ba.getIdUKey());
        validateAccessOfUserToEditOrDelete(ba, user);

        BirthAlteration existing = birthAlterationDAO.getById(ba.getIdUKey());
        validateAccessOfUserToEditOrDelete(existing, user);
        birthAlterationDAO.updateBirthAlteration(ba, user);
        logger.debug("Saved changes made to birth alteration record : {}  in data entry state", ba.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteBirthAlteration(long idUKey, User user) {
        logger.debug("Attempt to delete birth alteration record : {}", idUKey);
        BirthAlteration existing = birthAlterationDAO.getById(idUKey);
        validateAccessOfUserToEditOrDelete(existing, user);
        birthAlterationDAO.deleteBirthAlteration(idUKey);
        logger.debug("Deleted birth alteration record : {}  in data entry state", idUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthAlteration getByIDUKey(long idUKey, User user) {
        logger.debug("Loading birth alteration record : {}", idUKey);
        BirthAlteration ba = birthAlterationDAO.getById(idUKey);
        validateAccessOfUserToEditOrDelete(ba, user);
        return ba;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void approveBirthAlteration(BirthAlteration ba, Map<Integer, Boolean> fieldsToBeApproved,
                                       boolean applyChangesToBC, User user) {

        if (applyChangesToBC) {
            logger.debug("Attempt to approve birth alteration record : {} and apply changes to BC", ba.getIdUKey());
        } else {
            logger.debug("Attempt to save intermediate approvals for alteration record : {}", ba.getIdUKey());
        }

        validateAccessOfUserForApproval(ba, user);
        BirthAlteration existing = birthAlterationDAO.getById(ba.getIdUKey());
        validateAccessOfUserForApproval(existing, user);

        boolean containsApprovedChanges = false;
        for (Map.Entry<Integer, Boolean> e : fieldsToBeApproved.entrySet()) {
            if (Boolean.TRUE.equals(e.getValue())) {
                logger.debug("Setting status as approved for the alteration statement : {}", e.getKey());
                existing.getApprovalStatuses().set(e.getKey(), WebConstants.BIRTH_ALTERATION_APPROVE_TRUE);
                containsApprovedChanges = true;
            } else {
                logger.debug("Setting status as rejected for the alteration statement : {}", e.getKey());
                existing.getApprovalStatuses().set(e.getKey(), false);
            }
        }

        if (containsApprovedChanges && applyChangesToBC) {
            logger.debug("Requesting the application of changes to the BC as final for : {}", existing.getIdUKey());
            existing.setStatus(BirthAlteration.State.FULLY_APPROVED);

            // We've saved the alteration record, now lets modify the birth record
            BirthDeclaration bdf = birthDeclarationDAO.getById(ba.getBdfIDUKey());
            switch (ba.getType()) {

                case TYPE_27:
                case TYPE_27A:
                case TYPE_52_1_H:
                case TYPE_52_1_I: {
                    logger.debug("Alteration is an amendment, inclusion of omission or correction. Type : {}",
                            ba.getType().ordinal());
                    bdf.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_ALTERED);
                    bdf.getLifeCycleInfo().setActiveRecord(false);      // mark old record as a non-active record
                    birthDeclarationDAO.updateBirthDeclaration(bdf, user);

                    // create the new entry as a clone from the existing
                    bdf.setIdUKey(0);
                    bdf.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_GENERATED);
                    applyChanges(ba, bdf, user);
                    birthDeclarationDAO.addBirthDeclaration(bdf, user);
                    break;
                }
                case TYPE_52_1_A:
                case TYPE_52_1_B:
                case TYPE_52_1_D:
                case TYPE_52_1_E: {
                    bdf.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CANCELLED);
                    birthDeclarationDAO.updateBirthDeclaration(bdf, user);
                    logger.debug("Alteration of type : {} is a cancellation of the existing record : {}",
                            ba.getType().ordinal(), bdf.getIdUKey());

                    // cancel any person on the PRS related to this same PIN
                    Person person = personDAO.findPersonByPIN(bdf.getChild().getPin());
                    person.setStatus(Person.Status.CANCELLED);
                    ecivil.updatePerson(person, user);
                    break;
                }
            }
        }

        existing.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
        existing.getLifeCycleInfo().setApprovalOrRejectUser(user);
        birthAlterationDAO.updateBirthAlteration(existing, user);

        logger.debug("Updated birth alteration : {}", existing.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getApprovalPendingByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alteration pending approval by DSDivision ID : " + dsDivision.getDsDivisionUKey()
                    + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        ValidationUtils.validateAccessToDSDivison(dsDivision, user);
        return birthAlterationDAO.getBulkOfAlterationByDSDivision(dsDivision, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getApprovalPendingByBDDivision
            (BDDivision bdDivision, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alteration pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey()
                    + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
        return birthAlterationDAO.getBulkOfAlterationByBDDivision(bdDivision, pageNo, noOfRows);
    }
        @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getApprovalPendingByBDDivision
            (BDDivision bdDivision, int pageNo, int noOfRows) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alteration pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey()
                    + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        return birthAlterationDAO.getBulkOfAlterationByBDDivision(bdDivision, pageNo, noOfRows);
    }
    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getApprovalPendingByBDDivisionAndBDFSerialNo(
            BDDivision bdDivision, Long birthSerialNumber, int pageNo, int noOfRows, User user) {

        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alterations pending approval - by Birth Division : " + bdDivision.getEnDivisionName() +
                    " and BDF serial : " + birthSerialNumber + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        return birthAlterationDAO.getBulkOfAlterationByBDDivisionAndBirthSerialNo(bdDivision, birthSerialNumber, pageNo, noOfRows);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getApprovalPendingByUserLocationIdUKey(int locationUKey, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alterations pending approval - by User locaton id : " + locationUKey +
                    " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        return birthAlterationDAO.getBulkOfAlterationByUserLocationIdUKey(locationUKey, pageNo, noOfRows);
    }

    /**
     * Checks if the user can edit or delete a birth alteration entry before approval by an ARG
     * <p/>
     * A DEO or ADR at the same "SubissionLocation" can edit or delete an entry at data entry stage. Any other who has
     * access to the BD division of the corresponding BDF has access to edit or delete
     *
     * @param ba   the birth alteration entry
     * @param user the user attempting to update or delete
     */
    private void validateAccessOfUserToEditOrDelete(BirthAlteration ba, User user) {
        if (Role.ROLE_DEO.equals(user.getRole().getRoleId()) || Role.ROLE_ADR.equals(user.getRole().getRoleId())) {
            if (ba.getSubmittedLocation().equals(user.getPrimaryLocation())) {
                return;
            }
        } else if (!Role.ROLE_ADMIN.equals(user.getRole().getRoleId())) {
            return;
        }

        ValidationUtils.validateAccessToBDDivision(user,
                birthDeclarationDAO.getById(ba.getBdfIDUKey()).getRegister().getBirthDivision());
    }

    /**
     * Checks if the user can approve the birth alteration entry. Only an ARG level user, in charge of the BDF under
     * consideration can approve an alteration
     *
     * @param ba   the birth alteration entry
     * @param user the user attempting to approve
     */
    private void validateAccessOfUserForApproval(BirthAlteration ba, User user) {
        if (Role.ROLE_RG.equals(user.getRole().getRoleId())) {
            // RG can approve any record
        } else if (Role.ROLE_ARG.equals(user.getRole().getRoleId())) {
            ValidationUtils.validateAccessToBDDivision(user,
                    birthDeclarationDAO.getById(ba.getBdfIDUKey()).getRegister().getBirthDivision());

            if (!user.isAuthorized(Permission.APPROVE_BIRTH_ALTERATION)) {
                handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth alteration",
                        ErrorCodes.PERMISSION_DENIED);
            }
        } else {
            handleException("User : " + user.getUserId() + " is not an ARG for alteration approval",
                    ErrorCodes.PERMISSION_DENIED);
        }
    }

    private void applyChanges(BirthAlteration ba, BirthDeclaration bdf, User user) {

        switch (ba.getType()) {

            // section 27 - name changes
            case TYPE_27: {
                Alteration27 alt = ba.getAlt27();
                process27Changes(ba, bdf, alt, user);
                break;
            }

            // section 27 A - updates to marriage, mothers name after marriage or
            // {father & great/grand father details if those were not specified in the current BDF}
            case TYPE_27A: {
                Alteration27A alt = ba.getAlt27A();
                process27AChanges(ba, bdf, alt);
                break;
            }

            // section 52 (1) - corrections
            default: {
                Alteration52_1 alt = ba.getAlt52_1();
                process52_1Changes(ba, bdf, alt);
                break;
            }
        }
    }

    private void process27Changes(BirthAlteration ba, BirthDeclaration bdf, Alteration27 alt, User user) {
        // Update name of person on the PRS
        Person person = personDAO.findPersonByPIN(bdf.getChild().getPin());

        if (ba.getApprovalStatuses().get(Alteration27.CHILD_FULL_NAME_OFFICIAL_LANG)) {
            bdf.getChild().setChildFullNameOfficialLang(alt.getChildFullNameOfficialLang());
            person.setFullNameInOfficialLanguage(alt.getChildFullNameOfficialLang());
        }
        if (ba.getApprovalStatuses().get(Alteration27.CHILD_FULL_NAME_ENGLISH)) {
            bdf.getChild().setChildFullNameEnglish(alt.getChildFullNameEnglish());
            person.setFullNameInEnglishLanguage(alt.getChildFullNameEnglish());
        }
        ecivil.updatePerson(person, user);
    }

    private void process27AChanges(BirthAlteration ba, BirthDeclaration bdf, Alteration27A alt) {

        // father details
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_BIRTH_PLACE)) {
            bdf.getParent().setFatherPlaceOfBirth(alt.getFather().getFatherPlaceOfBirth());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_BIRTH_DATE)) {
            bdf.getParent().setFatherDOB(alt.getFather().getFatherDOB());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_COUNTRY)) {
            bdf.getParent().setFatherCountry(alt.getFather().getFatherCountry());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_PASSPORT)) {
            bdf.getParent().setFatherPassportNo(alt.getFather().getFatherPassportNo());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_NIC_OR_PIN)) {
            bdf.getParent().setFatherNICorPIN(alt.getFather().getFatherNICorPIN());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_FULLNAME)) {
            bdf.getParent().setFatherFullName(alt.getFather().getFatherFullName());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_RACE)) {
            if (bdf.getParent().getFatherRace() != null) {
                handleException("The fathers race cannot be changed under section 27A, when already specified." +
                        " Use Section 52 (1) instead", ErrorCodes.ILLEGAL_STATE);
            } else {
                bdf.getParent().setFatherRace(alt.getFather().getFatherRace());
            }
        }

        // marriage details
        if (ba.getApprovalStatuses().get(Alteration27A.WERE_PARENTS_MARRIED)) {
            bdf.getMarriage().setParentsMarried(alt.getMarriage().getParentsMarried());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.PLACE_OF_MARRIAGE)) {
            bdf.getMarriage().setPlaceOfMarriage(alt.getMarriage().getPlaceOfMarriage());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.DATE_OF_MARRIAGE)) {
            bdf.getMarriage().setDateOfMarriage(alt.getMarriage().getDateOfMarriage());
        }

        // mothers name after marriage
        if (ba.getApprovalStatuses().get(Alteration27A.MOTHER_NAME_AFTER_MARRIAGE)) {
            bdf.getParent().setMotherFullName(alt.getMothersNameAfterMarriage());
        }

        // grand father and great grand father details
        if (ba.getApprovalStatuses().get(Alteration27A.GRAND_FATHER_BIRTH_PLACE)) {
            bdf.getGrandFather().setGrandFatherBirthPlace(alt.getGrandFather().getGrandFatherBirthPlace());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GRAND_FATHER_BIRTH_YEAR)) {
            bdf.getGrandFather().setGrandFatherBirthYear(alt.getGrandFather().getGrandFatherBirthYear());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GRAND_FATHER_FULLNAME)) {
            bdf.getGrandFather().setGrandFatherFullName(alt.getGrandFather().getGrandFatherFullName());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GRAND_FATHER_NIC_OR_PIN)) {
            bdf.getGrandFather().setGrandFatherNICorPIN(alt.getGrandFather().getGrandFatherNICorPIN());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GREAT_GRAND_FATHER_BIRTH_PLACE)) {
            bdf.getGrandFather().setGreatGrandFatherBirthPlace(alt.getGrandFather().getGreatGrandFatherBirthPlace());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GREAT_GRAND_FATHER_BIRTH_YEAR)) {
            bdf.getGrandFather().setGreatGrandFatherBirthYear(alt.getGrandFather().getGreatGrandFatherBirthYear());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GREAT_GRAND_FATHER_FULLNAME)) {
            bdf.getGrandFather().setGreatGrandFatherFullName(alt.getGrandFather().getGreatGrandFatherFullName());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GREAT_GRAND_FATHER_NIC_OR_PIN)) {
            bdf.getGrandFather().setGreatGrandFatherNICorPIN(alt.getGrandFather().getGreatGrandFatherNICorPIN());
        }
    }

    private void process52_1Changes(BirthAlteration ba, BirthDeclaration bdf, Alteration52_1 alt) {

        // childs details
        if (ba.getApprovalStatuses().get(Alteration52_1.DATE_OF_BIRTH)) {
            bdf.getChild().setDateOfBirth(alt.getDateOfBirth());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.PLACE_OF_BIRTH)) {
            bdf.getChild().setPlaceOfBirth(alt.getPlaceOfBirth());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.PLACE_OF_BIRTH_ENGLISH)) {
            bdf.getChild().setPlaceOfBirthEnglish(alt.getPlaceOfBirthEnglish());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.GENDER)) {
            bdf.getChild().setChildGender(alt.getChildGender());
        }

        // mothers details
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_NIC_OR_PIN)) {
            bdf.getParent().setMotherNICorPIN(alt.getMother().getMotherNICorPIN());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_COUNTRY)) {
            bdf.getParent().setMotherCountry(alt.getMother().getMotherCountry());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_PASSPORT)) {
            bdf.getParent().setMotherPassportNo(alt.getMother().getMotherPassportNo());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_FULLNAME)) {
            bdf.getParent().setMotherFullName(alt.getMother().getMotherFullName());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_BIRTH_DATE)) {
            bdf.getParent().setMotherDOB(alt.getMother().getMotherDOB());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_AGE_AT_BIRTH)) {
            bdf.getParent().setMotherAgeAtBirth(alt.getMother().getMotherAgeAtBirth());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_RACE)) {
            bdf.getParent().setMotherRace(alt.getMother().getMotherRace());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_BIRTH_PLACE)) {
            bdf.getParent().setMotherPlaceOfBirth(alt.getMother().getMotherPlaceOfBirth());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_ADDRESS)) {
            bdf.getParent().setMotherAddress(alt.getMother().getMotherAddress());
        }

        // informant details
        if (ba.getApprovalStatuses().get(Alteration52_1.INFORMANT_TYPE)) {
            bdf.getInformant().setInformantType(alt.getInformant().getInformantType());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.INFORMANT_NIC_OR_PIN)) {
            bdf.getInformant().setInformantNICorPIN(alt.getInformant().getInformantNICorPIN());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.INFORMANT_NAME)) {
            bdf.getInformant().setInformantName(alt.getInformant().getInformantName());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.INFORMANT_ADDRESS)) {
            bdf.getInformant().setInformantAddress(alt.getInformant().getInformantAddress());
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
