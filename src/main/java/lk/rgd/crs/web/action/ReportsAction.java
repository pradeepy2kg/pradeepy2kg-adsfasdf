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
    
    public ReportsAction(ReportsGenerator reportsService) {
        this.reportsService = reportsService;
    }

    /**
     * create a statistical report
     * @return
     */
    public String create() {
        // todo permission and security validations
        reportsService.generate(); //todo don't generate if we already have one
        reportsService.createReport();
        
        return ActionSupport.SUCCESS;
    }
}
