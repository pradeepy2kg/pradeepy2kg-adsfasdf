package lk.rgd.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

/**
 * The default Date to String and String to Date converter
 * @author asankha
 */
public class DateTypeConverter extends StrutsTypeConverter {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {

        if (values.length == 0 || values[0] == null || values[0].trim().equals("")) {
            return null;
        }
        try {
            synchronized (dateFormat) {
                return dateFormat.parse(values[0]);
            }
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public String convertToString(Map context, Object o) {

        if (o instanceof Date) {
            return dateFormat.format((Date) o);
        }
        return "";
    }
}