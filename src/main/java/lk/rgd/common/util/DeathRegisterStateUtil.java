package lk.rgd.common.util;

import lk.rgd.crs.api.domain.DeathRegister;

/**
 * @author amith jayasekra
 *         simple util class to display values
 */
public class DeathRegisterStateUtil {

    public static String DATA_ENTRY = "DE";
    public static String APPROVED = "APPROVED";
    public static String REJECTED = "REJECTED";
    public static String ARCHIVED_CERT_GENERATED = "GENERATED";

    public static String getDeathRegisterState(DeathRegister.State state) {
        switch (state) {
            case DATA_ENTRY:
                return DATA_ENTRY;
            case APPROVED:
                return APPROVED;
            case REJECTED:
                return REJECTED;
            case ARCHIVED_CERT_GENERATED:
                return ARCHIVED_CERT_GENERATED;
        }
        return "";
    }
}
