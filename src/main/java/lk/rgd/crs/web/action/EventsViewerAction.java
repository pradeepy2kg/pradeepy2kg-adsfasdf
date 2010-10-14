package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Event;
import lk.rgd.common.api.dao.EventDAO;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.service.EventViewerService;
import lk.rgd.common.util.DateTimeUtils;

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
public class EventsViewerAction extends ActionSupport {
    private static final Logger logger = LoggerFactory.getLogger(EventsViewerAction.class);

    private final EventDAO eventDAO;
    private final AppParametersDAO appParametersDAO;
    private final EventViewerService service;

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

    private Date searchStartDate;
    private Date searchEndDate;
    private String startTime;
    private String endTime;
    private String startDate;
    private String endDate;
    private String userName;
    private boolean nextFlag;
    private boolean previousFlag;
    private boolean debugFlag;
    private boolean goBackFlag;
    private boolean filterFlag;

    private static final String EVENTS_ROWS_PER_PAGE = "common.event_rows_per_page";


    public EventsViewerAction(EventDAO eventDAO, EventViewerService service, AppParametersDAO appParametersDAO) {
        this.eventDAO = eventDAO;
        this.service = service;
        this.appParametersDAO = appParametersDAO;
    }

    public String initEventsManagement() {
        if (!filterFlag) {
            setPageNumber(1);
            numberOfRows = appParametersDAO.getIntParameter(EVENTS_ROWS_PER_PAGE);
            printList = service.getPaginatedListForAll(pageNumber, numberOfRows, user);
            paginationHandler(printList.size());
            setPreviousFlag(false);
        }
        if (goBackFlag) {
            if (filterFlag){
                setPageNumber(pageNumber);
                filter();
            }else {
                setPageNumber(pageNumber);
                numberOfRows = appParametersDAO.getIntParameter(EVENTS_ROWS_PER_PAGE);
                printList = service.getPaginatedListForAll(pageNumber, numberOfRows, user);
            }
            paginationHandler(printList.size());
        }

        return "success";
    }

    public String nextPage() {
        setPageNumber(getPageNumber() + 1);
        if (filterFlag)
            filter();
        else {
            numberOfRows = appParametersDAO.getIntParameter(EVENTS_ROWS_PER_PAGE);
            printList = service.getPaginatedListForAll(pageNumber, numberOfRows, user);
        }
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
        if (filterFlag)
            filter();
        else {
            numberOfRows = appParametersDAO.getIntParameter(EVENTS_ROWS_PER_PAGE);
            printList = service.getPaginatedListForAll(pageNumber, numberOfRows, user);
        }
        if (getRecordCounter() > 0) {
            setRecordCounter(getRecordCounter() - numberOfRows);
        }
        return "success";
    }

    public String debugDisplay() {
        event = service.getEventById(idUKey, user);
        timestamp = event.getTimestamp();
        methodName = event.getMethodName();
        className = event.getClassName();
        eventData = event.getEventData();
        debug = event.getDebug();
        stackTrace = event.getStackTrace();
        setGoBackFlag(true);
        return "success";
    }

    public String filterEvevtsList() {
        setPageNumber(1);
        filter();
        paginationHandler(printList.size());
        setPreviousFlag(false);
        return SUCCESS;
    }

    private List<Event> filter() {
        numberOfRows = appParametersDAO.getIntParameter(EVENTS_ROWS_PER_PAGE);
        searchStartDate = DateTimeUtils.getDateFromISO8601String(startDate);
        searchEndDate = DateTimeUtils.getDateFromISO8601String(endDate);
        searchStartDate.setTime(searchStartDate.getTime() + Integer.parseInt(startTime.substring(0, 2)) * 60 * 60 * 1000L + Integer.parseInt(startTime.substring(3, 5)) * 60 * 1000L);
        searchEndDate.setTime(searchEndDate.getTime() + Integer.parseInt(endTime.substring(0, 2)) * 60 * 60 * 1000L + Integer.parseInt(endTime.substring(3, 5)) * 60 * 1000L);
        printList = service.getPaginatedListByTimestampRange(pageNumber, numberOfRows, searchStartDate, searchEndDate, eventType);
        return printList;
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

    public EventViewerService getService() {
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

    public Date getSearchStartDate() {
        return searchStartDate;
    }

    public void setSearchStartDate(Date searchStartDate) {
        this.searchStartDate = searchStartDate;
        startDate = DateTimeUtils.getISO8601FormattedString(searchStartDate);
    }

    public Date getSearchEndDate() {
        return searchEndDate;
    }

    public void setSearchEndDate(Date searchEndDate) {
        this.searchEndDate = searchEndDate;
        endDate = DateTimeUtils.getISO8601FormattedString(searchEndDate);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isDebugFlag() {
        return debugFlag;
    }

    public void setDebugFlag(boolean debugFlag) {
        this.debugFlag = debugFlag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isGoBackFlag() {
        return goBackFlag;
    }

    public void setGoBackFlag(boolean goBackFlag) {
        this.goBackFlag = goBackFlag;
    }

    public boolean isFilterFlag() {
        return filterFlag;
    }

    public void setFilterFlag(boolean filterFlag) {
        this.filterFlag = filterFlag;
    }
}
