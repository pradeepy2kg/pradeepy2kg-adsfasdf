package lk.rgd.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    static {
        utcDfm.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Returns a String representation of the passed Date in ISO 8601
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
}
