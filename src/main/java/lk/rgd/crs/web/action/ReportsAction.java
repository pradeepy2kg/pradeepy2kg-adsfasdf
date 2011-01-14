package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.crs.api.service.ReportsGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Generate and create statistical reports.
 *
 * @author Ashoka Ekanayaka
 */
public class ReportsAction extends ActionSupport {
    private static final Logger logger = LoggerFactory.getLogger(EventsViewerAction.class);

    private final ReportsGenerator reportsService;
    private int year;
    private List<Integer> yearList;

    public ReportsAction(ReportsGenerator reportsService) {
        this.reportsService = reportsService;
    }

    public String loadPage() {
        populateYearList();
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

        reportsService.generate(year); //todo don't generate if we already have one
        reportsService.createReport();
        addActionMessage(getText("report.generation.completed"));
        logger.debug("Selected year {}", year);

        populateYearList();

        return ActionSupport.SUCCESS;
    }

    private void populateYearList() {
        yearList = new ArrayList<Integer>();
        Calendar cal = Calendar.getInstance();
        int thisYear = cal.get(Calendar.YEAR);
        yearList.add(thisYear);
        for (int i = 0; i < 5; i++) {
            cal.add(Calendar.YEAR, -1);
            yearList.add(cal.get(Calendar.YEAR));
        }
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
}
