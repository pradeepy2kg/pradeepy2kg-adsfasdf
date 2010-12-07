package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.domain.MarriageNotice;
import lk.rgd.crs.api.domain.MarriageRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author amith jayasekara
 *         validator class for marriage notice and marriage registration processes
 */
public class MarriageRegistrationValidator {
    private static final Logger logger = LoggerFactory.getLogger(MarriageRegistrationValidator.class);

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
        if (type == MarriageNotice.Type.BOTH_NOTICE || type == MarriageNotice.Type.MALE_NOTICE) {
            if (type == MarriageNotice.Type.BOTH_NOTICE && (notice.getFemale().getIdentificationNumberFemale() == null
                || notice.getFemale().getDateOfBirthFemale() == null)) {
                //this means this record is a both notice and its female data is incomplete
                handleException("marriage notice :serial" + notice.getSerialOfMaleNotice() +
                    ": is incomplete can not add", ErrorCodes.MR_INCOMPLETE_OBJECT);
            }
            //if female data is not incomplete now we can check male data are completed for both MALE and "BOTH" notice
            //types
            validateBasicNeeds(notice.getSerialOfMaleNotice(), notice.getDateOfMaleNotice(),
                notice.getMale().getIdentificationNumberMale(), notice.getMale().getDateOfBirthMale());
        } else {
            validateBasicNeeds(notice.getSerialOfFemaleNotice(), notice.getDateOfFemaleNotice(),
                notice.getFemale().getIdentificationNumberFemale(), notice.getFemale().getDateOfBirthFemale());
        }
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
