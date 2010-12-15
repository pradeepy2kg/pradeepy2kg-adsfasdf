package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageNotice;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author amith jayasekara
 *         validator class for marriage notice and marriage registration processes
 */
public class MarriageRegistrationValidator {
    private static final Logger logger = LoggerFactory.getLogger(MarriageRegistrationValidator.class);
    private static final String SERIAL_NUMBER_PATTERN = "20([1-9][0-9])[0|1]([0-9]{5})";
    private final MarriageRegistrationDAO marriageRegistrationDAO;

    public MarriageRegistrationValidator(MarriageRegistrationDAO marriageRegistrationDAO) {
        this.marriageRegistrationDAO = marriageRegistrationDAO;
    }

    /**
     * validate marriage notice for adding marriage notice
     *
     * @param notice notice to be validate
     * @param type   type of the notice
     */
    public void validateMarriageNotice(MarriageRegister notice, MarriageNotice.Type type) {
        //to be a valid notice notice must have follow properties
        //must have a valid serial number and a receive data and at least on of the party objects must be filled
        //if the notice is FEMALE notice identification number and date of birth* must be filled and vise-versa for
        // notice type MALE and BOTH
        //todo validate more
        switch (type) {
            case BOTH_NOTICE:
                validateBasicNeeds(notice.getSerialOfMaleNotice(), notice.getDateOfMaleNotice(),
                    notice.getFemale().getIdentificationNumberFemale(), notice.getFemale().getDateOfBirthFemale());
            case MALE_NOTICE:
                validateBasicNeeds(notice.getSerialOfMaleNotice(), notice.getDateOfMaleNotice(),
                    notice.getMale().getIdentificationNumberMale(), notice.getMale().getDateOfBirthMale());
                validateSerialNumber(notice.getSerialOfMaleNotice(), notice.getMrDivisionOfMaleNotice());
                break;
            case FEMALE_NOTICE:
                validateBasicNeeds(notice.getSerialOfFemaleNotice(), notice.getDateOfFemaleNotice(),
                    notice.getFemale().getIdentificationNumberFemale(), notice.getFemale().getDateOfBirthFemale());
                validateSerialNumber(notice.getSerialOfFemaleNotice(), notice.getMrDivisionOfFemaleNotice());
        }
        //check other must needed validations
        // todo license req party must be filled default is mail to male
    }

    private void validateSerialNumber(long serialNumber, MRDivision mrDivision) {
        // validate registration serial number
        boolean check = false;
        if (serialNumber >= 2010000001L && serialNumber <= 2099199999L) {
            String s = Long.toString(serialNumber);
            if (!s.matches(SERIAL_NUMBER_PATTERN)) {
                check = true;
            }
        } else {
            check = true;
        }
        if (check) {
            handleException("invalid serial number " + serialNumber + "unable to add marriage notice",
                ErrorCodes.INVALID_SERIAL_NUMBER);
        }/*
        //checking serial number duplication for the division
        MarriageRegister existingRecord = marriageRegistrationDAO.getNoticeByMRDivisionAndSerialNo(mrDivision,
            serialNumber, true).get(0);
        if (existingRecord != null) {
            handleException("serial number duplication  " + serialNumber + " for marriage division " +
                mrDivision.getEnDivisionName() + "unable to add marriage notice",
                ErrorCodes.POSSIBLE_MARRIAGE_NOTICE_SERIAL_NUMBER_DUPLICATION);
        }*/

        //checking serial number duplication for the division
        //todo uncomment above after made changes to that dao method    amith
        List<MarriageRegister> existingRecord = marriageRegistrationDAO.getNoticeByMRDivisionAndSerialNo(mrDivision,
            serialNumber, true);
        if (existingRecord != null && existingRecord.size() > 0) {
            handleException("serial number duplication  " + serialNumber + " for marriage division " +
                mrDivision.getEnDivisionName() + "unable to add marriage notice",
                ErrorCodes.POSSIBLE_MARRIAGE_NOTICE_SERIAL_NUMBER_DUPLICATION);
        }
    }

    /**
     * this warning is issued in special case
     * assume male is submitting first and he nominate female is to be capture the license ,
     * then before submitting female notice male notice is being approved,
     * now female is submitting and she declare male should get the license but the cannot happen because LP(license party)
     * must be approved second in that case warning is given for the user for asking should female keep the license
     * or should rollback the approval of male's approval
     *
     * @param existing notice to be check
     * @param type     type of the second notice
     * @return
     */
    public List<UserWarning> validateAddingSecondNotice(MarriageRegister existing, MarriageNotice.Type type) {
        List<UserWarning> warning = new ArrayList<UserWarning>();
        //if second notice is a MALE notice and if its records current state is FEMALE_NOTICE_APPROVED and
        //second notice is nominating that female should get the notice and vise-versa
        boolean checkFail = ((type == MarriageNotice.Type.MALE_NOTICE) &&
            (existing.getState() == MarriageRegister.State.FEMALE_NOTICE_APPROVED) &&
            ((existing.getLicenseCollectType() == MarriageRegister.LicenseCollectType.HAND_COLLECT_FEMALE)
                || (existing.getLicenseCollectType() == MarriageRegister.LicenseCollectType.MAIL_TO_FEMALE))) ||
            ((type == MarriageNotice.Type.FEMALE_NOTICE) &&
                (existing.getState() == MarriageRegister.State.MALE_NOTICE_APPROVED) &&
                ((existing.getLicenseCollectType() == MarriageRegister.LicenseCollectType.HAND_COLLECT_MALE)
                    || (existing.getLicenseCollectType() == MarriageRegister.LicenseCollectType.MAIL_TO_MALE)));

        if (checkFail) {
            warning.add(new UserWarning("warn.add.or.rollback.other.party.approval", UserWarning.Severity.WARN));
            return warning;
        }
        return Collections.emptyList();
    }

    private void validateBasicNeeds(Long serial, Date recDate, String identificationNumber, Date dob) {
        if (serial == null || recDate == null || identificationNumber == null || dob == null) {
            handleException("marriage notice :serial" + serial + ": is incomplete can not add", ErrorCodes.MR_INCOMPLETE_OBJECT);
        }
    }

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }
}
