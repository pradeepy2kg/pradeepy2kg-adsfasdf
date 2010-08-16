package lk.rgd.crs.web.util;

import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.web.WebConstants;

import java.beans.PropertyDescriptor;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        if (s == null) return null;
        s = s.trim();
        return s.length() == 0 ? null : s.toUpperCase();
    }
}
