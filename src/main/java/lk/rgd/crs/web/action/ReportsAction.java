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
    private int viewYear;
    private int chartType;
    private int viewChartType;
    private List<Integer> yearList;
    private List<Integer> viewYearList;
    private Map<Integer, String> chartList;
    private Map<Integer, String> viewChartList;
    private Map session;
    private boolean clearCache;

    public ReportsAction(ReportsGenerator reportsService, StatisticsManager statisticsManager) {
        this.reportsService = reportsService;
        this.statisticsManager = statisticsManager;
    }

    public String loadPage() {
        populateLists();
        year = yearList.iterator().next();
        viewYear = viewYearList.iterator().next();
        return SUCCESS;
    }

    /**
     * create a statistical report
     * @return String
     */
    public String create() {
        // todo permission and security validations
        logger.info("Clear Cache {}", clearCache);

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
            } else if (chartType == 8) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.TABLE_2_11);
                reportsService.createReport(user, ReportCodes.TABLE_2_11);
            } else if (chartType == 9) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.TABLE_2_10);
                reportsService.createReport(user, ReportCodes.TABLE_2_10);
            } else if (chartType == 10) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.TABLE_2_12);
                reportsService.createReport(user, ReportCodes.TABLE_2_12);
            } else if (chartType == 11) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.INFANT_DEATH_TABLE_1);
                //reportsService.createReport(user, ReportCodes.INFANT_DEATH_TABLE_1);
            } else if (chartType == 12) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.INFANT_DEATH_TABLE_3_5a);
            } else if (chartType == 13) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.DEATH_TABLE_4_3);
                reportsService.createDeathReport_all(user, ReportCodes.DEATH_TABLE_4_3);
                reportsService.createDeathReport_4_6(user, ReportCodes.DEATH_TABLE_4_3);
                reportsService.createDeathReport_4_4(user, ReportCodes.DEATH_TABLE_4_4);
                reportsService.createDeathReport_4_2(user, ReportCodes.DEATH_TABLE_4_2);
            } /*else if (chartType == 14) {
                logger.debug("Chart Type {}", chartType);
                //generateReport(year, user, ReportCodes.DEATH_TABLE_4_3);
                reportsService.createDeathReport_4_6(user, ReportCodes.DEATH_TABLE_4_3);
            } else if (chartType == 15) {
                logger.debug("Chart Type {}", chartType);
                //generateReport(year, user, ReportCodes.DEATH_TABLE_4_3);
                reportsService.createDeathReport_4_4(user, ReportCodes.DEATH_TABLE_4_4);
            }*/
            else if (chartType == 16) {
                logger.debug("Chart Type {}", chartType);
                generateReport(year, user, ReportCodes.BIRTH_RAW_DATA);
            }

        } catch (RGDRuntimeException error) {
            addActionError(getText("permission.denied"));
        }
        addActionMessage(getText("report.generation.completed"));
        logger.debug("Selected year {}", year);

        populateLists();

        return ActionSupport.SUCCESS;
    }

    public String viewReport() {
        logger.info("VIEW called");
        populateLists();
        return ActionSupport.SUCCESS;
    }

    public String populateStatistics() {
        statisticsManager.deleteOldStatistics();
        statisticsManager.triggerScheduledStatJobs();
        statisticsManager.updateStatisticsList();
        populateLists();
        addActionMessage("Statistics populating completed");
        return SUCCESS;
    }

    private void populateLists() {
        yearList = new ArrayList<Integer>();
        Calendar cal = Calendar.getInstance();
        int thisYear = cal.get(Calendar.YEAR);
        yearList.add(thisYear);
        viewYearList = new ArrayList<Integer>();
        viewYearList.add(thisYear);
        for (int i = 0; i < 5; i++) {
            cal.add(Calendar.YEAR, -1);
            yearList.add(cal.get(Calendar.YEAR));
            viewYearList.add(cal.get(Calendar.YEAR));
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
        chartList.put(8, "TABLE 2.11");
        chartList.put(9, "TABLE 2.10");
        chartList.put(10, "TABLE 2.12");
        chartList.put(11, "INFANT DEATH REPORT 3.2/3.3/3.4");
        chartList.put(12, "INFANT DEATH REPORT 3.5a/3.5b");
        chartList.put(13, "DEATH REPORTS 4.2/4.3/4.4/4.6");
        //chartList.put(14, "DEATH TABLE 4.6");
        //chartList.put(15, "DEATH TABLE 4.4");
        chartList.put(16, "BIRTH RAW DATA");

        viewChartList = chartList;
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

    public boolean isClearCache() {
        return clearCache;
    }

    public void setClearCache(boolean clearCache) {
        this.clearCache = clearCache;
    }

    public Map<Integer, String> getViewChartList() {
        return viewChartList;
    }

    public void setViewChartList(Map<Integer, String> viewChartList) {
        this.viewChartList = viewChartList;
    }

    public int getViewChartType() {
        return viewChartType;
    }

    public void setViewChartType(int viewChartType) {
        this.viewChartType = viewChartType;
    }

    public List<Integer> getViewYearList() {
        return viewYearList;
    }

    public void setViewYearList(List<Integer> viewYearList) {
        this.viewYearList = viewYearList;
    }

    public int getViewYear() {
        return viewYear;
    }

    public void setViewYear(int viewYear) {
        this.viewYear = viewYear;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    private void generateReport(int year, User user, int reportCode) {
        switch (reportCode) {
            case ReportCodes.TABLE_2_2:
                reportsService.generate_2_2(year, user, clearCache);
                break;
            case ReportCodes.TABLE_2_8:
                reportsService.generate_2_8(year, user, clearCache);
                break;
            case ReportCodes.TABLE_2_5:
                reportsService.generate_2_5_new(year, user, clearCache);
                break;
            case ReportCodes.TABLE_2_4:
                reportsService.generate_2_4(year, user, clearCache);
                break;
            case ReportCodes.TABLE_2_7:
                reportsService.generate_2_7(year, user, clearCache);
                break;
            case ReportCodes.TABLE_2_6:
                reportsService.generate_2_6(year, user, clearCache);
                break;
            case ReportCodes.TABLE_2_2A:
                reportsService.generate_2_2a(year, user, clearCache);
                break;
            case ReportCodes.TABLE_2_3:
                reportsService.generate_2_3(year, user, clearCache);
                break;
            case ReportCodes.TABLE_2_11:
                reportsService.generate_2_11(year, user, clearCache);
                break;
            case ReportCodes.TABLE_2_10:
                reportsService.generate_2_10(year, user, clearCache);
                break;
            case ReportCodes.TABLE_2_12:
                reportsService.generate_2_12(year, user, clearCache);
                break;
            case ReportCodes.INFANT_DEATH_TABLE_1:
                reportsService.generateDeathReport(year, user, clearCache);
                break;
            case ReportCodes.INFANT_DEATH_TABLE_3_5a:
                reportsService.generateDeathReport2(year, user, clearCache);
                break;
            case ReportCodes.DEATH_TABLE_4_3:
                reportsService.populateDeathStatistics(year, user, clearCache);
                break;
            case ReportCodes.BIRTH_RAW_DATA:
                reportsService.createBirthRawDataTable(year, user, clearCache);
                break;
        }
    }
}
