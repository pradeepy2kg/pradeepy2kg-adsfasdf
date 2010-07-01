package lk.rgd.crs.core.service;

import lk.rgd.AppConstants;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.ErrorCodes;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.ChildInfo;
import lk.rgd.crs.api.domain.InformantInfo;
import lk.rgd.crs.api.domain.ParentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;

/**
 * A class to contain validations for BDFs
 *
 * @author asankha
 */
public class BirthDeclarationValidator {

    private static final ResourceBundle rb_si =
        ResourceBundle.getBundle("messages/bdf_validation_messages", AppConstants.LK_SI);
    private static final ResourceBundle rb_ta =
        ResourceBundle.getBundle("messages/bdf_validation_messages", AppConstants.LK_TA);
    private static final ResourceBundle rb_en =
        ResourceBundle.getBundle("messages/bdf_validation_messages", AppConstants.LK_EN);

    private static final Logger logger = LoggerFactory.getLogger(BirthDeclarationValidator.class);
    private static final int WEEKS_FOR_FOETUS_TO_SURVIVE = 28;

    /**
     * Validate if the record satisfy the minimum requirements for acceptance and storage. These checks does not
     * even mandate the name of the child being registered - but simply the declaration division, serial, date, sex
     * informant and the notifying authority
     * @param bdf the BDF to validate
     */
    public static void validateMinimalRequirements(BirthDeclaration bdf) {

        if (bdf.getRegister().getBdfSerialNo() == 0 ||
            bdf.getRegister().getDateOfRegistration() == null ||
            bdf.getRegister().getBirthDivision() == null ||
            bdf.getChild().getDateOfBirth() == null ||
            isEmptyString(bdf.getChild().getPlaceOfBirth()) ||
            //bdf.getChild().getChildGender() - cannot validate
            isEmptyString(bdf.getInformant().getInformantName()) ||
            isEmptyString(bdf.getInformant().getInformantAddress()) ||
            bdf.getInformant().getInformantSignDate() == null ||
            //bdf.getInformant().getInformantType() - cannot validate

            isEmptyString(bdf.getNotifyingAuthority().getNotifyingAuthorityAddress()) ||
            bdf.getNotifyingAuthority().getNotifyingAuthoritySignDate() == null ||
            isEmptyString(bdf.getNotifyingAuthority().getNotifyingAuthorityName()) ||
            bdf.getNotifyingAuthority().getNotifyingAuthorityPIN() == 0) {

            if (bdf.getIdUKey() > 0) {
                handleException("Birth declaration record ID : " + bdf.getIdUKey() + " is not complete. " +
                    "Check required field values", ErrorCodes.INVALID_DATA);
            } else if (bdf.getRegister().getBdfSerialNo() > 0) {
                handleException("Birth declaration record with serial number : " + bdf.getRegister().getBdfSerialNo() +
                    " is not complete. Check required field values", ErrorCodes.INVALID_DATA);
            } else {
                handleException("Birth declaration record being processed is incomplete " +
                    "Check required field values", ErrorCodes.INVALID_DATA);
            }
        }
    }

    /**
     * Validate typical requirements for a birth registration
     * @param birthDeclarationDAO the BirthDeclarationDAO
     * @param bdf the BDF to validate
     * @param user the user initiating the validation.
     * The resulting warnings will be in the language preferred by the user
     * @return a list of warnings issued against the BDF
     */
    public static List<UserWarning> validateStandardRequirements(
        BirthDeclarationDAO birthDeclarationDAO, BirthDeclaration bdf, User user) {

        // create a holder to capture any warnings
        List<UserWarning> warnings = new ArrayList<UserWarning>();

        // select locale for user
        ResourceBundle rb = rb_en;
        if (AppConstants.SINHALA.equals(user.getPrefLanguage())) {
            rb = rb_si;
        } else if (AppConstants.TAMIL.equals(user.getPrefLanguage())) {
            rb = rb_ta;
        }

        // check child and parent  information - the names and mothers address to be non-empty
        ChildInfo child = bdf.getChild();
        checkValidString(child.getChildFullNameOfficialLang(), warnings, rb, "child_name_official_invalid");
        checkValidString(child.getChildFullNameEnglish(), warnings, rb, "child_name_en_invalid");
        ParentInfo parent = bdf.getParent();
        checkValidString(parent.getFatherFullName(), warnings, rb, "father_name_invalid");
        checkValidString(parent.getMotherFullName(), warnings, rb, "mother_name_invalid");
        checkValidString(parent.getMotherAddress(), warnings, rb, "mother_address_invalid");

        // if mother or father is known, a guardian cannot be the informant
        if (bdf.getInformant().getInformantType() == InformantInfo.InformantType.GUARDIAN &&
            (!isEmptyString(parent.getMotherFullName()) || !isEmptyString(parent.getFatherFullName()))) {
            warnings.add(new UserWarning(rb.getString("illegal_informant"), UserWarning.Severity.ERROR));
        }

        // check if this is a duplicate by checking dateOfBirth and motherNICorPIN
        if (bdf.getParent() != null && bdf.getParent().getMotherNICorPIN() != null) {

            Calendar start = Calendar.getInstance();
            start.setTime(bdf.getChild().getDateOfBirth());
            start.add(Calendar.DATE, -30 * WEEKS_FOR_FOETUS_TO_SURVIVE);

            Calendar end = Calendar.getInstance();
            end.setTime(bdf.getChild().getDateOfBirth());
            end.add(Calendar.DATE, 30 * WEEKS_FOR_FOETUS_TO_SURVIVE);

            List<BirthDeclaration> existingRecords = birthDeclarationDAO.getByDOBRangeandMotherNICorPIN(
                start.getTime(), end.getTime(), bdf.getParent().getMotherNICorPIN());

            for (BirthDeclaration b : existingRecords) {
                if (b.getIdUKey() != bdf.getIdUKey()) {
                    warnings.add(
                        new UserWarning(MessageFormat.format(rb.getString("possible_duplicate"),
                            new Object[] {b.getIdUKey(), b.getRegister().getDateOfRegistration(),
                                b.getChild().getChildFullNameOfficialLangToLength(20)})));
                }
            }
        }

        return warnings;
    }

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }

    private static void checkValidString(String s, List<UserWarning> warnings, ResourceBundle rb, String key) {
        if (s == null || s.trim().length() == 0) {
            warnings.add(new UserWarning(rb.getString(key), UserWarning.Severity.WARN));
        }
    }

    private static boolean isEmptyString(String s) {
        return s == null || s.trim().length() == 0;
    }
    
}
