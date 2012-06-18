package lk.rgd.common.util;

/**
 * @author asankha
 */
public final class IdentificationNumberUtil {

    public static boolean isValidNIC(String id) {
        // day of year 001 - 366 or 501 - 866
        if (id != null && id.matches("\\d{9}+[V|X]")) {
            try {
                int day = Integer.parseInt(id.substring(2,5));
                return ((day >= 1 && day <= 366) || (day >= 501 && day <= 866));
            } catch (NumberFormatException ignore) {}
        }
        return false;
    }

    /*public static void main(String[] args) {
        System.out.println("752111430V : " + isValidNIC("752111430V"));
        System.out.println("752111430X : " + isValidNIC("752111430X"));
        System.out.println("752111430 : " + isValidNIC("752111430"));
        System.out.println("752111430C : " + isValidNIC("752111430C"));
        System.out.println("75211143V : " + isValidNIC("75211143V"));
        System.out.println("754111430V : " + isValidNIC("754111430V"));
        System.out.println("759111430V : " + isValidNIC("759111430V"));
    }*/
}
