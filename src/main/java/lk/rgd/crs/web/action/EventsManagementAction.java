package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Event;
import lk.rgd.common.api.dao.EventDAO;
import lk.rgd.common.api.service.EventsMamagementService;

import java.util.Date;


/**
 * User: widu
 * Date: Sep 30, 2010
 * Time: 9:14:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventsManagementAction extends ActionSupport  {

    private final EventDAO eventDAO;
    private final EventsMamagementService service;

    private Event event;
    private long idUKey;
    private User user;
    private Event.Type eventType;
    private Date timestamp;
    private int eventCode;
    private long recordId;
    private String methodName;
    private String className;
    private String eventData;
    private String debug;


    public EventsManagementAction(EventDAO eventDAO,EventsMamagementService service) {
        this.eventDAO = eventDAO;
        this.service = service;
    }

    public String initEventsManagement(){
        event =service.getEventsByIdUkey(2,user);
        timestamp= event.getTimestamp();
        eventType=event.getEventType();
        eventCode=event.getEventCode();
        recordId=event.getRecordId();
        methodName=event.getMethodName();
        className=event.getClassName();
        eventData=event.getEventData();
        debug=event.getDebug();
        user=event.getUser();
        return "success";
    }


    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event.Type getEventType() {
        return eventType;
    }

    public void setEventType(Event.Type eventType) {
        this.eventType = eventType;
    }

    public EventDAO getEventDAO() {
        return eventDAO;
    }

    public EventsMamagementService getService() {
        return service;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }


    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
