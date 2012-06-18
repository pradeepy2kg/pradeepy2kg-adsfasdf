package lk.rgd.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date time utils for use with Solr which keeps time in UTC
 *
 * @author asankha
 */
public class DateTimeUtils {
    private static final DateFormat utcDfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final DateFormat ISO8601Format = new SimpleDateFormat("yyyy-MM-dd");

    static {
        utcDfm.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Returns a String representation of the passed Date in ISO 8601
     *
     * @param localDate
     * @return
     */
    public static String dayToUTCString(Date localDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(localDate);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        synchronized (utcDfm) {
            return utcDfm.format(c.getTime());
        }
    }

    /**
     * Returns an ISO8601 comliant (W3C standard as well) date. yyyy-MM-dd
     *
     * @param date A java.util.Date object
     * @return the formatted String
     */
    public static String getISO8601FormattedString(Date date) {
        synchronized (ISO8601Format) {
            return ISO8601Format.format(date);
        }
    }

    /**
     * Returns a java.util.Date from an ISO8601 compliant date string yyyy-MM-dd
     * returns null if the string is not parseable.
     *
     * @param s String formatted in yyyy-MM-dd
     * @return date
     */
    public static Date getDateFromISO8601String(String s) {
        try {
            synchronized (ISO8601Format) {
                return ISO8601Format.parse(s);
            }
        } catch (ParseException e) {
            return null;
        }
    }
}
