package lk.rgd.crs.web.util;


import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

import lk.rgd.crs.web.WebConstants;
import org.apache.log4j.Logger;


/**
 * EPopDate is used to convert String into Date format
 *
 * @author indunil moremada.
 */
public class EPopDate {
    private static Logger log = Logger.getLogger(EPopDate.class.getName());
    private static DateFormat df = new SimpleDateFormat(WebConstants.DATEFORMAT);

    /**
     * convert a String into java.util.Date format
     *
     * @param dateString is a String which is converted to required Date Format
     * @return date is a java.util.Date instance*
     */
    public static Date getDate(String dateString) {
        try {
            return df.parse(dateString);
        } catch (Exception e) {
            log.error("Date format Exception " + e);
        }

        return null;
    }

    /**
     * convert a java.util.Date into String
     *
     * @param date is a java.util.Date instance which is Converted to String
     * @return dateInString is a String
     */
    public static String getDateInString(Date date) {
        return df.format(date);
    }
}
