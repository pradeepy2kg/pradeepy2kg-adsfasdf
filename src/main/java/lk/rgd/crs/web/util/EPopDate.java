package lk.rgd.crs.web.util;


import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

import org.apache.log4j.Logger;


/**
 * EPopDate is used to convert String into Date format
 *
 * @author indunil moremada.
 */
public class EPopDate {

    private Date date;
    private String dateInString;
    private Logger log = Logger.getLogger(this.getClass().getName());
    private DateFormat df;

    /**
     * convert a String into java.util.Date format
     *
     * @param dateString is a String which is converted to required Date Format
     * @return date is a java.util.Date instance*
     */
    public Date getDate(String dateString) {
        df = new SimpleDateFormat(Constant.DATEFORMAT);
        try {
            date = df.parse(dateString);
        } catch (Exception e) {
            log.error("Date format Exception" + e);
        }
        return date;
    }

    /**
     * convert a java.util.Date into String
     *
     * @param date is a java.util.Date instance which is Converted to String
     * @return dateInString is a String
     */
    public String getDateInString(Date date) {
        df = new SimpleDateFormat(Constant.DATEFORMAT);
        dateInString = df.format(date);
        return dateInString;
    }
}
