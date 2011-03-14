package lk.rgd.common.util;

/**
 * Utility methods for Name manipulations
 *
 * @author asankha
 */
public class NameFormatUtil {

    /**
     * Return the last <length> characters of the given name
     * @param fullName the full name in English or the official language
     * @param length the maximum number of characters to return
     * @return the trimmed name
     */
    public static String getDisplayName(String fullName, int length) {
        char[] chars = fullName.trim().toCharArray();
        int startPos = -1;
        final int count = chars.length;

        // if the name already fits, return it as it is
        if (count <= length) {
            return fullName.trim();
        }

        for (int i=count-length; i<count; i++) {
            if (chars[i] == ' ') {
                startPos = i+1;
                break;
            }
        }

        if (startPos == -1) {
            // no spaces were found, return all length characters
            return new String(chars, count-length, length).trim();
        } else {
            return new String(chars, startPos, count-startPos).trim();
        }
    }

    /**
     * Return the last 20 characters of the given name
     * @param fullName the full name in English or the official language
     * @return the trimmed name
     */
    public static String getDisplayName(String fullName) {
        return getDisplayName(fullName, 20);
    }
}
