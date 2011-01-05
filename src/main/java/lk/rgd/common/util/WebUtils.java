package lk.rgd.common.util;

import lk.rgd.crs.api.domain.MarriageNotice;
import lk.rgd.crs.api.domain.MarriageRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Selection of utility methods to use in web apps, from struts actions and servlets or from other helper classes.
 *
 * @author Ashoka Ekanayaka
 *         Date: Jun 3, 2010
 *         Time: 12:04:08 PM
 */
public class WebUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

    /**
     * Nice method found from the web to merge two form beans. both beans have to be from the same type and
     * copies only where the destination value is empty . Useful to preserve and capture changes to a entity across several pages.
     *
     * @param <M> target
     * @param <M> destination
     * @return <M> target bean
     * @throws Exception
     */
    public static <M> M merge(M target, M destination) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());

        // Iterate over all the attributes
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {

            // Only copy writable attributes
            if (descriptor.getWriteMethod() != null) {
                Object originalValue = descriptor.getReadMethod()
                    .invoke(target);

                // Only copy values values where the destination values is null
                if (originalValue == null) {
                    Object defaultValue = descriptor.getReadMethod().invoke(
                        destination);
                    descriptor.getWriteMethod().invoke(target, defaultValue);
                }

            }
        }

        return target;
    }

    /**
     * Merges 2 java beans of different types. method names and attribute types are assumed exact equals.
     *
     * @param <T> target  the bean which needs to be updated
     * @param <M> source  the bean which has the newer values which needs to be feeded into target
     * @returns target bean
     */
    public static <T, M> T beanMerge(T target, M source) throws Exception {
        BeanInfo sourceInfo = Introspector.getBeanInfo(source.getClass());
        BeanInfo targetInfo = Introspector.getBeanInfo(target.getClass());

        // cache target descriptors to be used in the inner loop later
        PropertyDescriptor[] targetDescriptors = targetInfo.getPropertyDescriptors();

        // Iterate over all the attributes of source bean and copy them into the target
        for (PropertyDescriptor descriptor : sourceInfo.getPropertyDescriptors()) {
            Object newValue = descriptor.getReadMethod().invoke(source);
            logger.debug("processing : {}, value is : {}", descriptor.getReadMethod(), newValue);

            Method sourceMethod = descriptor.getWriteMethod();
            if (sourceMethod != null) {
                String methodName = sourceMethod.getName();
                for (PropertyDescriptor targetDescriptor : targetDescriptors) {
                    Method targetMethod = targetDescriptor.getWriteMethod();
                    if (targetMethod == null) { // if the method not writable just pass on to next
                        continue;
                    }
                    if (methodName.equals(targetMethod.getName())) {
                        targetMethod.invoke(target, newValue);
                        logger.debug("field merged ");
                        break;
                    }
                }
            }
        }

        return target;
    }

    /**
     * Used to set null for blank space and change to upper case
     *
     * @param s string to be checked
     * @return
     */
    public static String filterBlanksAndToUpper(String s) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        return s.length() == 0 ? null : s.toUpperCase();
    }

    public static String filterBlanks(String s) {
        return s == null ? null : (s = s.trim()).length() == 0 ? null : s;
    }

    /**
     * get populated mock notice objects for given marriage register object list(only for displaying purposes)
     *
     * @param marriageRegisterList list of marriage register
     * @return marriage notice list
     */
    public static List<MarriageNotice> populateNoticeList(List<MarriageRegister> marriageRegisterList) {
        //if isBoth submitted  true it means this record is single notice record
        //else
        //  if male serial is available this is a male submitted notice
        //  if female serial is available this is a female submitted notice
        List<MarriageNotice> noticeList = new LinkedList<MarriageNotice>();
        for (MarriageRegister mr : marriageRegisterList) {
            boolean isSingleNotice = mr.isSingleNotice();
            Long maleSerial = mr.getSerialOfMaleNotice();
            Long femaleSerial = mr.getSerialOfFemaleNotice();
            if (isSingleNotice) {
                //one notice male notice
                noticeList.add(new MarriageNotice(mr, MarriageNotice.Type.BOTH_NOTICE));
            } else {
                if (maleSerial != null) {
                    //male notice
                    noticeList.add(new MarriageNotice(mr, MarriageNotice.Type.MALE_NOTICE));
                }
                if (femaleSerial != null) {
                    //female notice
                    noticeList.add(new MarriageNotice(mr, MarriageNotice.Type.FEMALE_NOTICE));
                }
            }
        }

        return noticeList;
    }
}
