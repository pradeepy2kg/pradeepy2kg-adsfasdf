package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.StatisticsManager;
import lk.rgd.crs.web.ReportCodes;
import lk.rgd.crs.web.WebConstants;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.crs.api.service.ReportsGenerator;

import java.util.*;

/**
 * Generate and create statistical reports.
 *
 * @author Ashoka Ekanayaka
 */
public class ReportsAction extends ActionSupport implements SessionAware {
    private static final Logger logger = LoggerFactory.getLogger(EventsViewerAction.class);

    private final ReportsGenerator reportsService;
    private final StatisticsManager statisticsManager;
    private int year;
    private int chartType;
    private List<Integer> yearList;
    private Map<Integer, String> chartList;
    private Map session;

    public ReportsAction(ReportsGenerator reportsService, StatisticsManager statisticsManager) {
        this.reportsService = reportsService;
        this.statisticsManager = statisticsManager;
    }

    public String loadPage() {
        populateLists();
        year = yearList.iterator().next();
        return SUCCESS;
    }

    /**
     * create a statistical report
     *
     * @return
     */
    public String create() {
        // todo permission and security validations

        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        try {
            if (chartType == 0) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.TABLE_2_2);                //todo don't generate if we already have one
                reportsService.createReport(user, ReportCodes.TABLE_2_2);
            } else if (chartType == 1) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.TABLE_2_4);
                reportsService.createReport(user, ReportCodes.TABLE_2_4);
            } else if (chartType == 3) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.TABLE_2_8);
                reportsService.createReport(user, ReportCodes.TABLE_2_8);
            } else if (chartType == 2) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.TABLE_2_5);
                reportsService.createReport(user, ReportCodes.TABLE_2_5);
            } else if (chartType == 4) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.TABLE_2_7);
                reportsService.createReport(user, ReportCodes.TABLE_2_7);
            } else if (chartType == 5) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.TABLE_2_6);
                reportsService.createReport(user, ReportCodes.TABLE_2_6);
            } else if (chartType == 6) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.TABLE_2_2A);
                reportsService.createReport(user, ReportCodes.TABLE_2_2A);
            } else if (chartType == 7) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.TABLE_2_3);
                reportsService.createReport(user, ReportCodes.TABLE_2_3);
            }

        } catch (RGDRuntimeException error) {
            addActionError(getText("permission.denied"));
        }
        addActionMessage(getText("report.generation.completed"));
        logger.debug("Selected year {}", year);

        populateLists();

        return ActionSupport.SUCCESS;
    }

    public String populateStatistics() {
        statisticsManager.deleteOldStatistics();
        statisticsManager.triggerScheduledStatJobs();
        statisticsManager.updateStatisticsList();
        populateLists();
        return SUCCESS;
    }

    private void populateLists() {
        yearList = new ArrayList<Integer>();
        Calendar cal = Calendar.getInstance();
        int thisYear = cal.get(Calendar.YEAR);
        yearList.add(thisYear);
        for (int i = 0; i < 5; i++) {
            cal.add(Calendar.YEAR, -1);
            yearList.add(cal.get(Calendar.YEAR));
        }

        chartList = new HashMap<Integer, String>();
        chartList.put(0, "TABLE 2.2");
        chartList.put(1, "TABLE 2.4");
        chartList.put(2, "TABLE 2.5");
        chartList.put(3, "TABLE 2.8");
        chartList.put(4, "TABLE 2.7");
        chartList.put(5, "TABLE 2.6");
        chartList.put(6, "TABLE 2.2A");
        chartList.put(7, "TABLE 2.3");
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List getYearList() {
        return yearList;
    }

    public void setYearList(List yearList) {
        this.yearList = yearList;
    }

    public Map<Integer, String> getChartList() {
        return chartList;
    }

    public void setChartList(Map<Integer, String> chartList) {
        this.chartList = chartList;
    }

    public int getChartType() {
        return chartType;
    }

    public void setChartType(int chartType) {
        this.chartType = chartType;
    }

    public Map getSession() {
        return session;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    private void generateReport(int year, User user, int reportCode) {
        switch (reportCode) {
            case ReportCodes.TABLE_2_2:
                reportsService.generate_2_2(year, user);
                break;
            case ReportCodes.TABLE_2_8:
                reportsService.generate_2_8(year, user);
                break;
            case ReportCodes.TABLE_2_5:
                reportsService.generate_2_5(year, user);
                break;
            case ReportCodes.TABLE_2_4:
                reportsService.generate_2_4(year, user);
                break;
            case ReportCodes.TABLE_2_7:
                reportsService.generate_2_5(year, user);
                break;
            case ReportCodes.TABLE_2_6:
                reportsService.generate_2_5(year, user);
                break;
            case ReportCodes.TABLE_2_2A:
                reportsService.generate_2_5(year, user);
                break;
            case ReportCodes.TABLE_2_3:
                reportsService.generate_2_3(year, user);
                break;
        }
    }
}
