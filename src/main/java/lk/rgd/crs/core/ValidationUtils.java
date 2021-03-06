package lk.rgd.crs.core;

import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.*;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author asankha
 */
public class ValidationUtils {

    private static final Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

    public static void validateAccessToMRDistrict(User user, District district) {
        if (!(User.State.ACTIVE == user.getStatus() &&
            (Role.ROLE_RG.equals(user.getRole().getRoleId())
                || (user.isAllowedAccessToMRDistrict(district.getDistrictUKey()))
            )
        )) {
            handleException("User : " + user.getUserId() + " is not allowed access to the District : " +
                district.getDistrictUKey(), ErrorCodes.PERMISSION_DENIED);
        }
    }

    public static void validateAccessToBDDistrict(User user, District district) {
        if (!(User.State.ACTIVE == user.getStatus() &&
            (Role.ROLE_RG.equals(user.getRole().getRoleId())
                || (user.isAllowedAccessToBDDistrict(district.getDistrictUKey()))
            )
        )) {
            handleException("User : " + user.getUserId() + " is not allowed access to the District : " +
                district.getDistrictUKey(), ErrorCodes.PERMISSION_DENIED);
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

    public static void validateAccessToBDDivision(User user, BDDivision bdDivision) {
        if (!(User.State.ACTIVE == user.getStatus()
            &&
            (Role.ROLE_RG.equals(user.getRole().getRoleId())
                ||
                (user.isAllowedAccessToBDDistrict(bdDivision.getDistrict().getDistrictUKey())
                    &&
                    user.isAllowedAccessToBDDSDivision(bdDivision.getDsDivision().getDsDivisionUKey())
                )
            )
        )) {
            handleException("User : " + user.getUserId() + " is not allowed access to the District : " +
                bdDivision.getDistrict().getDistrictUKey() + " and/or DS Division : " +
                bdDivision.getDsDivision().getDsDivisionUKey(), ErrorCodes.PERMISSION_DENIED);
        }
    }

    public static void validateAccessToDSDivision(DSDivision dsDivision, User user) {
        if (!(User.State.ACTIVE == user.getStatus() &&
            (Role.ROLE_RG.equals(user.getRole().getRoleId())
                || (user.isAllowedAccessToBDDistrict(dsDivision.getDistrict().getDistrictUKey()))
                || (user.isAllowedAccessToBDDSDivision(dsDivision.getDsDivisionUKey()))
            )
        )) {
            handleException("User : " + user.getUserId() + " is not allowed access to the DSDivision : " +
                dsDivision.getDistrictId(), ErrorCodes.PERMISSION_DENIED);
        }
    }

    public static void validateAccessToMRDivision(MRDivision mrDivision, User user) {
        if (!(User.State.ACTIVE == user.getStatus()
            &&
            (Role.ROLE_RG.equals(user.getRole().getRoleId()))
            ||
            (user.isAllowedAccessToMRDistrict(mrDivision.getDistrict().getDistrictUKey())
                &&
                user.isAllowedAccessToMRDSDivision(mrDivision.getDsDivision().getDsDivisionUKey()))
        )) {
            handleException("User : " + user.getUserId() + " is not allowed to access to the District : " +
                mrDivision.getDistrict().getDistrictUKey() + " and/or DS Division : " +
                mrDivision.getDsDivision().getDsDivisionUKey(), ErrorCodes.PERMISSION_DENIED);
        }
    }

    /**
     * only the following users are able to access the marriage notice /license
     *
     * @param register license or notice
     * @param user     user who performs the action
     */
    public static void validateAccessToMarriageNotice(MarriageRegister register, User user) {
        if (!(User.State.ACTIVE == user.getStatus() && (Role.ROLE_RG.equals(user.getRole().getRoleId())) ||
            (register.getMrDivisionOfMaleNotice() != null &&
                user.isAllowedAccessToMRDSDivision(register.getMrDivisionOfMaleNotice().getMrDivisionUKey())) ||
            (register.getMrDivisionOfFemaleNotice() != null &&
                user.isAllowedAccessToMRDSDivision(register.getMrDivisionOfFemaleNotice().getMrDivisionUKey())))) {
            handleException("User : " + user.getUserId() + " is not allowed to access marriage notice/license idUKey :" +
                register.getIdUKey(), ErrorCodes.PERMISSION_DENIED);
        }
    }

    /**
     * Validate user access to the DS Division
     *
     * @param dsDivisionUKey primary key of DS division
     * @param user           user who performs the action
     */
    public static void validateUserAccessToDSDivision(int dsDivisionUKey, User user) {
        if (!(User.State.ACTIVE == user.getStatus() && (Role.ROLE_RG.equals(user.getRole().getRoleId())) ||
            user.isAllowedAccessToMRDSDivision(dsDivisionUKey))) {
            handleException("User : " + user.getUserId() + " is not allowed to access to the DS Division", ErrorCodes.PERMISSION_DENIED);
        }
    }

    /**
     * Validate user permission to perform an action
     *
     * @param permission permission level of the action
     * @param user       user who performs the action
     */
    public static void validateUserPermission(int permission, User user) {
        if (!user.isAuthorized(permission)) {
            handleException("User : " + user.getUserId() + " is not authorized to perform this action", ErrorCodes.PERMISSION_DENIED);
        }
    }

    private static void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
