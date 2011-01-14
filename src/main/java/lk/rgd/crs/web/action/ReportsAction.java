package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.crs.api.service.ReportsGenerator;

/**
 * Generate and create statistical reports.
 * @author Ashoka Ekanayaka
 *
 */
public class ReportsAction extends ActionSupport {
    private static final Logger logger = LoggerFactory.getLogger(EventsViewerAction.class);

    private final ReportsGenerator reportsService;
    private int year;
    
    public ReportsAction(ReportsGenerator reportsService) {
        this.reportsService = reportsService;
    }

    public String loadPage(){
        return SUCCESS;
    }

    /**
     * create a statistical report
     * @return
     */
    public String create() {
        // todo permission and security validations
        reportsService.generate(); //todo don't generate if we already have one
        reportsService.createReport();
        addActionMessage(getText("report.generation.completed"));

        return ActionSupport.SUCCESS;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
