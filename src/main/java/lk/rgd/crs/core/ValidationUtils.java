package lk.rgd.crs.core;

import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.domain.BDDivision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author asankha
 */
public class ValidationUtils {

    private static final Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

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

    public static void validateAccessToDSDivison(DSDivision dsDivision, User user) {
        if (!(User.State.ACTIVE == user.getStatus() &&
            (Role.ROLE_RG.equals(user.getRole().getRoleId())
                || (user.isAllowedAccessToBDDistrict(dsDivision.getDistrict().getDistrictUKey()))
                || (user.isAllowedAccessToBDDSDivision(dsDivision.getDsDivisionUKey()))
            )
        )) {
            handleException("User : " + user.getUserId() + " is not allowed access to the District : " +
                dsDivision.getDistrictId(), ErrorCodes.PERMISSION_DENIED);
        }
    }

    private static void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

}
