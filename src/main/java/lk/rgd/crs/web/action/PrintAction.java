package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;

import java.util.List;
import java.util.Map;

import lk.rgd.crs.web.util.MasterDataLoad;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.PrintData;
import lk.rgd.crs.api.dao.DistrictDAO;

/**
 * Printing actions
 *
 * @author Chathuranga Withana
 */
public class PrintAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);

    private final DistrictDAO districtDAO;

    private List<PrintData> printList;
    private Map districtList;
    private Map session;

    public PrintAction(DistrictDAO districtDAO) {
        this.districtDAO = districtDAO;
    }

    /**
     * List to be printed returned
     *
     * @return
     */
    public String viewPrintList() {
        printList = populateList();
        session.put("printList", printList);
        return "success";
    }

    /**
     * Populate List to be printed
     *
     * @return
     */
    private List populateList() {
        // todo loading hard coded valued have to be loaded from DB
        MasterDataLoad masterDataLoad = MasterDataLoad.getInstance();
        String language = (String) session.get(WebConstants.SESSION_USER_LANG);
        districtList = districtDAO.getDistricts(language);
        return masterDataLoad.getPrintList();
    }

    public List<PrintData> getPrintList() {
        return printList;
    }

    public void setPrintList(List<PrintData> printList) {
        this.printList = printList;
    }

    public void setSession(Map session) {
        this.session = session;
    }

    public Map getSession() {
        return session;
    }

    public Map getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map districtList) {
        this.districtList = districtList;
    }
}
