package lk.rgd.prs.core;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.prs.PRSRuntimeException;
import lk.rgd.prs.api.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class to contain validation of PRS records
 *
 * @author Chathuranga Withana
 */
public class PRSValidationUtils {

    private static final Logger logger = LoggerFactory.getLogger(PRSValidationUtils.class);

    public static void validatePersonState(Person person, Person.Status state) {
        if (state != person.getStatus()) {
            handleException("Person with personUKey : " + person.getPersonUKey() + " , in invalid state : " +
                person.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Person with personUKey : " + person.getPersonUKey() + " , in valid state : " +
                person.getStatus());
        }
    }

    public static void validateAccessToLocation(Location location, User user) {
        if (location != null && user != null) {
            if (!(User.State.ACTIVE == user.getStatus()
                &&
                (Role.ROLE_RG.equals(user.getRole().getRoleId())
                    ||
                    user.isAllowedAccessToLocation(location.getLocationUKey())
                )
            )) {
                handleException("User : " + user.getUserId() + " is not allowed access to the Location : " +
                    location.getLocationUKey(), ErrorCodes.PERMISSION_DENIED);
            }
        } else {
            handleException("Person or User performing the action not complete", ErrorCodes.INVALID_DATA);
        }
    }

    public static void validateAccessOfUserToEdit(User user) {
        if (user != null) {
            if (!(User.State.ACTIVE == user.getStatus() && !Role.ROLE_ADMIN.equals(user.getRole().getRoleId()) &&
                user.isAuthorized(Permission.PRS_EDIT_PERSON))) {
                handleException("User : " + user.getUserId() + " is not allowed edit entries on the PRS",
                    ErrorCodes.PERMISSION_DENIED);
            }
        } else {
            handleException("Person or User performing the action not complete", ErrorCodes.INVALID_DATA);
        }
    }

    public static void validateAccessOfUserToApprove(User user) {
        if (user != null) {
            if (!(User.State.ACTIVE == user.getStatus() && !Role.ROLE_ADMIN.equals(user.getRole().getRoleId()) &&
                user.isAuthorized(Permission.PRS_APPROVE_PERSON))) {
                handleException("User : " + user.getUserId() + " is not allowed edit entries on the PRS",
                    ErrorCodes.PERMISSION_DENIED);
            }
        } else {
            handleException("Person or User performing the action not complete", ErrorCodes.INVALID_DATA);
        }
    }

    public static final boolean isEmptyString(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static boolean isBlankString(String s) {
        return s != null && s.trim().length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return s != null && s.trim().length() > 0;
    }

    public static void handleException(String message, int code) {
        logger.error(message);
        throw new PRSRuntimeException(message, code);
    }
}
