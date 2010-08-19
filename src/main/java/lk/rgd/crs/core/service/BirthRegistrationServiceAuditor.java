package lk.rgd.crs.core.service;

import lk.rgd.common.api.domain.User;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Performs auditing of the BirthRegistrationService method invocation
 *
 * @author asankha
 */
public class BirthRegistrationServiceAuditor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegistrationServiceAuditor.class);

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        logger.debug("Method name : " + methodInvocation.getMethod().getName());
        /*final Object[] args = methodInvocation.getArguments();

        // always ensure that the FIRST User argument is the user executing the method
        for (int i=0; i<args.length; i++) {
            if (args[i] instanceof User) {
                MethodInvocationInformation.user.set((User) args[i]);
                break;
            }
        }*/

        try {
            Object result = methodInvocation.proceed();
            return result;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
