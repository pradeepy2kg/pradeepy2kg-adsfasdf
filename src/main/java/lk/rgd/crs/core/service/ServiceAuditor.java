package lk.rgd.crs.core.service;

import com.thoughtworks.xstream.XStream;
import lk.rgd.ErrorCodes;
import lk.rgd.common.IdentifiableException;
import lk.rgd.common.RGDException;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.Auditable;
import lk.rgd.common.api.dao.EventDAO;
import lk.rgd.common.api.domain.Event;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.UserLocation;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.api.service.EventViewerService;
import lk.rgd.common.core.service.UserManagerImpl;
import lk.rgd.common.core.service.EventViewerServiceImpl;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.*;
import lk.rgd.prs.api.service.PopulationRegistry;
import lk.rgd.prs.core.service.PopulationRegistryImpl;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Performs auditing of the Service layer method invocations
 *
 * @author asankha
 */
public class ServiceAuditor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ServiceAuditor.class);
    private final Map<Class, Class> serviceClasses;
    private final List<Class> debugClasses;
    private final XStream xstream;
    private final boolean debugInputParameters;
    private final EventDAO eventDao;

    public ServiceAuditor(boolean captureDebugBeforeExecution, EventDAO eventDao) {
        this.debugInputParameters = captureDebugBeforeExecution;
        this.eventDao = eventDao;

        xstream = new XStream();
        xstream.omitField(Location.class, "users");
        xstream.omitField(BirthAlteration.class, "lifeCycleInfo");
        xstream.omitField(BirthDeclaration.class, "lifeCycleInfo");
        xstream.omitField(DeathAlteration.class, "lifeCycleInfo");
        xstream.omitField(DeathRegister.class, "lifeCycleInfo");
        xstream.omitField(AdoptionOrder.class, "lifeCycleInfo");
        xstream.omitField(BirthDeclaration.class, "confirmationPrintUser");
        xstream.omitField(User.class, "assignedBDDistricts");
        xstream.omitField(User.class, "assignedMRDistricts");
        xstream.omitField(User.class, "assignedBDDSDivisions");
        xstream.omitField(User.class, "locations");
        xstream.omitField(UserLocation.class, "user");

        xstream.alias("adoptionOrder", AdoptionOrder.class);
        xstream.alias("assignment", Assignment.class);
        xstream.alias("certificateSearch", CertificateSearch.class);
        xstream.alias("birthDeclatation", BirthDeclaration.class);
        xstream.alias("deathRegister", DeathRegister.class);
        xstream.alias("registrar", Registrar.class);
        xstream.alias("birthAlteration", BirthAlteration.class);
        xstream.alias("deathAlteration", DeathAlteration.class);
        xstream.alias("marriageRegister", MarriageRegister.class);


        // the service classes that need to be audited
        serviceClasses = new HashMap<Class, Class>();
        serviceClasses.put(BirthRegistrationService.class, BirthRegistrationServiceImpl.class);
        serviceClasses.put(DeathRegistrationService.class, DeathRegistrationServiceImpl.class);
        serviceClasses.put(AdoptionOrderService.class, AdoptionOrderServiceImpl.class);
        serviceClasses.put(MasterDataManagementService.class, MasterDataManagementServiceImpl.class);
        serviceClasses.put(PopulationRegistry.class, PopulationRegistryImpl.class);
        serviceClasses.put(BirthAlterationService.class, BirthAlterationServiceImpl.class);
        serviceClasses.put(DeathAlterationService.class, DeathAlterationServiceImpl.class);
        serviceClasses.put(CertificateSearchService.class, CertificateSearchServiceImpl.class);
        serviceClasses.put(UserManager.class, UserManagerImpl.class);
        serviceClasses.put(MarriageRegistrationService.class, MarriageRegistrationServiceImpl.class);

        // the domain objects to be debug audited
        debugClasses = new ArrayList<Class>();
        debugClasses.add(BirthDeclaration.class);
        debugClasses.add(DeathRegister.class);
        debugClasses.add(AdoptionOrder.class);
        debugClasses.add(Assignment.class);
        debugClasses.add(Registrar.class);
        debugClasses.add(CertificateSearch.class);
        debugClasses.add(BirthAlteration.class);
        debugClasses.add(DeathAlteration.class);
        debugClasses.add(MarriageRegister.class);
    }

    /**
     * Intercepts the service level method call and captures debug information and saves error information
     * into the Event table within a new and separate transaction
     *
     * @param methodInvocation not applicable for documentation
     * @return not applicable for documentation
     * @throws Throwable not applicable for documentation
     */
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        // identify the actual method to be invoked via the Spring proxy
        final Method method = methodInvocation.getMethod();
        final Class serviceClass = serviceClasses.get(method.getDeclaringClass());

        // assume we don't need to audit this call
        boolean auditInvocation = false;

        // Do we need to audit this call? If this a transactional service call we should
        if (serviceClass != null) {
            Transactional transactional = serviceClass.getMethod(
                method.getName(), method.getParameterTypes()).getAnnotation(Transactional.class);

            auditInvocation = (transactional != null && (
                transactional.propagation().equals(Propagation.REQUIRED) ||
                    transactional.propagation().equals(Propagation.REQUIRES_NEW)));

            // if not a transactional method, capture those marked auditable
            if (!auditInvocation) {
                auditInvocation = serviceClass.getMethod(
                    method.getName(), method.getParameterTypes()).getAnnotation(Auditable.class) != null;
            }
        }

        // Do we need to debug this call? If this is audited, and debugInputParameters is set, then we should
        boolean debugInvocation = auditInvocation && debugInputParameters;

        if (logger.isDebugEnabled()) {
            logger.debug("Service method : " + method.getDeclaringClass().getName() + "." + method.getName() +
                "() - audit : " + auditInvocation + " debug : " + debugInvocation);
        }

        // the Event for this invocation
        Event event = auditInvocation ? new Event() : null;
        // if we are auditing this call, capture the User, if we are debugging, capture original input parameters
        if (auditInvocation) {
            processMethodParameters(methodInvocation, debugInvocation, event);
        }

        // a reference to a possible exception to track the event ID associated with it
        IdentifiableException identifiableException = null;

        // execute the invoked method
        try {
            return methodInvocation.proceed();

        } catch (Exception e) {

            // if we didn't plan to audit this call, but encountered an error, still we need to log an event
            if (event == null) {
                event = new Event();
                // capture user and any debug information if possible
                processMethodParameters(methodInvocation, true, event);
            }
            event.setEventType(Event.Type.ERROR);

            // capture stack trace
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            event.setStackTrace(sw.toString());

            // if not one of our errors.. create an RGDRuntimeException, by reassigning e
            if (!(e instanceof RGDRuntimeException) && !(e instanceof RGDException)) {
                logger.error("Unexpected error encountered", e);
                e = new RGDRuntimeException("Unexpected error", ErrorCodes.UNKNOWN_ERROR, e);
            }

            // capture error code and re-throw
            identifiableException = (IdentifiableException) e;
            event.setEventCode(identifiableException.getErrorCode());
            throw e;

        } finally {

            // capture event log if an event is available
            if (event != null) {
                eventDao.addEvent(event);

                // if an exception was encountered, cross insert the event id into it
                if (identifiableException != null) {
                    identifiableException.setEventId(event.getIdUKey());
                    logger.debug("Captured event log for exception as ID : {}", event.getIdUKey());
                }
            }
        }
    }

    /**
     * Process method parameters and extract User and set debug information into the passed event object
     *
     * @param methodInvocation the method invocation
     * @param debugInvocation  true if method parameter debugging is required
     * @param event            the event to capture information into
     */
    private void processMethodParameters(MethodInvocation methodInvocation, boolean debugInvocation, Event event) {

        event.setMethodName(methodInvocation.getMethod().getName());
        event.setClassName(methodInvocation.getMethod().getDeclaringClass().getSimpleName());

        // keep a StringBuilder to capture debug info if we encounter
        StringBuilder eventData = new StringBuilder();
        StringBuilder debugData = null;

        // get hold of the user and debug info if captureDebugBeforeExecution is enabled
        for (Object arg : methodInvocation.getArguments()) {

            if (arg != null && arg instanceof User) {
                event.setUser((User) arg);

            } else if (arg != null && debugInvocation && debugClasses.contains(arg.getClass())) {

                // this is a class we are interested to debug
                if (debugData == null) {
                    debugData = new StringBuilder(1024 * 10);
                }
                try {
                    debugData.append(xstream.toXML(arg));
                    debugData.append("\n");
                } catch (Exception ignore) {
                    debugData.append("Could not capture\n");
                }

                // capture event data
                captureEventData(arg, event);

            } else if (arg != null && (arg instanceof Long || arg instanceof Integer)) {
                eventData.append(arg);
                eventData.append(",");
            } else if (arg != null && (arg instanceof String && ((String) arg).length() < 20)) {
                eventData.append(arg);
                eventData.append(",");
            }

            event.setEventData(eventData.toString());
        }

        // save any debug info if captured
        if (debugData != null) {
            event.setDebug(debugData.toString());
        }

        logger.debug("Captured user and or method parameters");
    }

    private final void captureEventData(Object obj, Event event) {
        if (obj instanceof BirthDeclaration) {
            event.setRecordId(((BirthDeclaration) obj).getIdUKey());
        } else if (obj instanceof DeathRegister) {
            event.setRecordId(((DeathRegister) obj).getIdUKey());
        } else if (obj instanceof AdoptionOrder) {
            event.setRecordId(((AdoptionOrder) obj).getIdUKey());
        } else if (obj instanceof Assignment) {
            event.setRecordId(((Assignment) obj).getAssignmentUKey());
        } else if (obj instanceof Registrar) {
            event.setRecordId(((Registrar) obj).getRegistrarUKey());
        } else if (obj instanceof CertificateSearch) {
            event.setRecordId(((CertificateSearch) obj).getIdUKey());
        } else if (obj instanceof BirthAlteration) {
            event.setRecordId(((BirthAlteration) obj).getIdUKey());
        }
    }
}
