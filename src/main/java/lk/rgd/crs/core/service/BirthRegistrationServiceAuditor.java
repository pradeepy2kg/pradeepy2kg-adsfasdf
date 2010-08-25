package lk.rgd.crs.core.service;

import com.thoughtworks.xstream.XStream;
import lk.rgd.common.api.dao.EventDAO;
import lk.rgd.common.api.domain.Event;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.EventDAOImpl;
import lk.rgd.crs.api.domain.*;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Marriage;
import lk.rgd.prs.api.domain.Person;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Performs auditing of the BirthRegistrationService method invocation
 *
 * @author asankha
 */
public class BirthRegistrationServiceAuditor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegistrationServiceAuditor.class);

    private final XStream xstream = new XStream();
    private final boolean captureDebugBeforeExecution;
    private final EventDAO eventDao;

    public BirthRegistrationServiceAuditor(boolean captureDebugBeforeExecution, EventDAO eventDao) {
        this.captureDebugBeforeExecution = captureDebugBeforeExecution;
        this.eventDao = eventDao;
        xstream.alias("birthCertificateSearch", BirthCertificateSearch.class);
        xstream.alias("birthDeclatation", BirthDeclaration.class);
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        final String methodName = methodInvocation.getMethod().getName();
        // get hold of the user and bdf if any. Later include death, marriage, person etc too..
        User user = null;
        Event event = null;
        StringBuilder sb = null;

        final Object[] args = methodInvocation.getArguments();
        for (int i=0; i<args.length; i++) {
            if (args[i] instanceof User) {
                user = (User) args[i];

            } else if (captureDebugBeforeExecution &&
                (args[i] instanceof BirthDeclaration || args[i] instanceof BirthCertificateSearch)) {

                if (sb == null) {
                    sb = new StringBuilder(1024);
                }
                sb.append(xstream.toXML(args[i]));
            }
        }

        try {
            Object result = methodInvocation.proceed();
            if (captureDebugBeforeExecution) {
                event = new Event();
            }
            return result;
            
        } catch (Exception e) {

            event = new Event();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            event.setStackTrace(sw.toString());

            throw e;

        } finally {

            if (event != null) {
                event.setEventData(methodName);

                if (user != null) {
                    event.setUser(user);
                }
                if (sb != null) {
                    event.setDebug(sb.toString());
                }

                eventDao.addEvent(event);
                logger.error("Captured event log for issue with ID : {}", event.getIdUKey());
            }
        }
    }
}
