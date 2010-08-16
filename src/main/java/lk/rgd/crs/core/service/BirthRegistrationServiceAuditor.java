package lk.rgd.crs.core.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Arrays;

/**
 * Performs auditing of the BirthRegistrationService method invocation
 *
 * @author asankha
 */
public class BirthRegistrationServiceAuditor implements MethodInterceptor {

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        System.out.println("Method name : "
            + methodInvocation.getMethod().getName());
        System.out.println("Method arguments : "
            + Arrays.toString(methodInvocation.getArguments()));

        try {
            Object result = methodInvocation.proceed();
            return result;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
