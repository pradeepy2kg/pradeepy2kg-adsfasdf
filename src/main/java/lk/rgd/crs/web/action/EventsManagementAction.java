package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Event;
import lk.rgd.common.api.dao.EventDAO;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.service.EventManagementService;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User: widu
 * Date: Sep 30, 2010
 * Time: 9:14:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventsManagementAction extends ActionSupport {
    private static final Logger logger = LoggerFactory.getLogger(EventsManagementAction.class);

    private final EventDAO eventDAO;
    private final AppParametersDAO appParametersDAO;
    private final EventManagementService service;

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
    private String stackTrace;
    private List<Event> printList;
    private int pageNumber;
    private int numberOfRows;
    private int recordCounter;

    private boolean nextFlag;
    private boolean previousFlag;

    private static final String EVENTS_ROWS_PER_PAGE = "common.event_rows_per_page";


    public EventsManagementAction(EventDAO eventDAO, EventManagementService service, AppParametersDAO appParametersDAO) {
        this.eventDAO = eventDAO;
        this.service = service;
        this.appParametersDAO = appParametersDAO;
    }

    public String initEventsManagement() {
        setPageNumber(1);
        numberOfRows = appParametersDAO.getIntParameter(EVENTS_ROWS_PER_PAGE);
        logger.debug("No of rows: {} ", numberOfRows);
        printList = service.getPaginatedListForAll(pageNumber, numberOfRows, user);
        paginationHandler(printList.size());
        setPreviousFlag(false);
        return "success";
    }

    public String nextPage() {
        setPageNumber(getPageNumber() + 1);
        numberOfRows = appParametersDAO.getIntParameter(EVENTS_ROWS_PER_PAGE);
        printList = service.getPaginatedListForAll(pageNumber, numberOfRows, user);
        paginationHandler(printList.size());
        setPreviousFlag(true);
        setRecordCounter(getRecordCounter() + numberOfRows);
        return "success";
    }

    public String previousPage() {

        if (previousFlag && getPageNumber() == 2) {
            setPreviousFlag(false);
        } else if (getPageNumber() == 1) {
            setPreviousFlag(false);
        } else {
            setPreviousFlag(true);
        }
        setNextFlag(true);
        if (getPageNumber() > 1) {
            setPageNumber(getPageNumber() - 1);
        }
        numberOfRows = appParametersDAO.getIntParameter(EVENTS_ROWS_PER_PAGE);
        printList = service.getPaginatedListForAll(pageNumber, numberOfRows, user);
        paginationHandler(printList.size());
        if (getRecordCounter() > 0) {
            setRecordCounter(getRecordCounter() - numberOfRows);
        }
        return "success";
    }

    public String debugDisplay() {
        event = service.getEventById(idUKey, user);
        debug = event.getDebug();
        timestamp=event.getTimestamp();
        logger.debug("time stamp : {}",timestamp);
        return "success";
    }

    public String stackTraceDisplay(){
        event = service.getEventById(idUKey, user);
        stackTrace = event.getStackTrace();
        return "success";
    }

    /**
     * responsible whether to display the next link in the jsp or not and handles the page number
     *
     * @param recordsFound, no of events records found
     */
    public void paginationHandler(int recordsFound) {
        if (recordsFound == appParametersDAO.getIntParameter(EVENTS_ROWS_PER_PAGE)) {
            setNextFlag(true);
        } else {
            setNextFlag(false);
        }
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

    public EventManagementService getService() {
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

    public List<Event> getPrintList() {
        return printList;
    }

    public void setPrintList(List<Event> printList) {
        this.printList = printList;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public boolean isNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(boolean nextFlag) {
        this.nextFlag = nextFlag;
    }

    public boolean isPreviousFlag() {
        return previousFlag;
    }

    public void setPreviousFlag(boolean previousFlag) {
        this.previousFlag = previousFlag;
    }

    public int getRecordCounter() {
        return recordCounter;
    }

    public void setRecordCounter(int recordCounter) {
        this.recordCounter = recordCounter;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
