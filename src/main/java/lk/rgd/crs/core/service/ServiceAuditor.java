package lk.rgd.crs.core.service;

import com.thoughtworks.xstream.XStream;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.EventDAO;
import lk.rgd.common.api.domain.Event;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Performs auditing of the Service layer method invocations
 *
 * @author asankha
 */
public class ServiceAuditor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ServiceAuditor.class);

    private final XStream xstream = new XStream();
    private final boolean captureDebugBeforeExecution;
    private final EventDAO eventDao;

    public ServiceAuditor(boolean captureDebugBeforeExecution, EventDAO eventDao) {
        this.captureDebugBeforeExecution = captureDebugBeforeExecution;
        this.eventDao = eventDao;

        xstream.alias("adoptionOrder", AdoptionOrder.class);
        xstream.alias("assignment", Assignment.class);
        xstream.alias("birthCertificateSearch", BirthCertificateSearch.class);
        xstream.alias("birthDeclatation", BirthDeclaration.class);
        xstream.alias("deathRegister", DeathRegister.class);
        xstream.alias("registrar", Registrar.class);
    }

    /**
     * Intercepts the service level method call and captures debug information and saves error information
     * into the Event table within a new and separate transaction
     * @param methodInvocation not applicable for documentation
     * @return not applicable for documentation
     * @throws Throwable not applicable for documentation
     */
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        final String methodName = methodInvocation.getMethod().getName();
        final String className  = methodInvocation.getClass().getName();

        // get hold of the user and debug info if captureDebugBeforeExecution is enabled
        User user = null;
        Event event = null;
        StringBuilder sb = null;

        final Object[] args = methodInvocation.getArguments();
        for (int i=0; i<args.length; i++) {
            
            if (args[i] instanceof User) {
                user = (User) args[i];
                if (!captureDebugBeforeExecution) {
                    break;  // optimize if possible when not debugging
                }

            } else if (captureDebugBeforeExecution) {

                if (sb == null) {
                    sb = new StringBuilder(1024 * 10);
                }
                sb.append(xstream.toXML(args[i]));
                event = new Event();
            }
        }

        try {
            return methodInvocation.proceed();
            
        } catch (Exception e) {

            event = new Event();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            event.setStackTrace(sw.toString());

            if (e instanceof RGDRuntimeException) {
                event.setEventCode(((RGDRuntimeException) e).getErrorCode());   
            }

            event.setEventType(Event.Type.ERROR);

            throw e;

        } finally {

            if (event != null) {
                event.setClassName(className);
                event.setMethodName(methodName);

                if (user != null) {
                    event.setUser(user);
                }
                if (sb != null) {
                    event.setDebug(sb.toString());
                }

                eventDao.addEvent(event);
                logger.debug("Captured event log for issue with ID : {}", event.getIdUKey());
            }
        }
    }
}
