package lk.rgd.crs.core;

import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
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
     * Validate access to the Extract of Marriage
     *
     * @param register Marriage Register
     * @param user     user who performs the action
     */
    public static void validateAccessToMarriageRegister(MarriageRegister register, User user) {
        if (!(User.State.ACTIVE == user.getStatus() && (Role.ROLE_RG.equals(user.getRole().getRoleId())) ||
            (register.getMrDivision() != null &&
                user.isAllowedAccessToMRDSDivision(register.getMrDivision().getMrDivisionUKey())))) {
            handleException("User : " + user.getUserId() + " is not allowed to access marriage register - idUKey :" +
                register.getIdUKey(), ErrorCodes.PERMISSION_DENIED);
        }
    }

    private static void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
