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
import lk.rgd.crs.api.dao.BDDivisionDAO;

/**
 * Printing actions
 *
 * @author Chathuranga Withana
 */
public class PrintAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);

    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;

    private List<PrintData> printList;
    private Map<Integer, String> districtList;
    private Map<Integer, String> divisionList;
    private Map session;
    private String selectOption;

    public PrintAction(DistrictDAO districtDAO, BDDivisionDAO bdDivisionDAO) {
        this.districtDAO = districtDAO;
        this.bdDivisionDAO = bdDivisionDAO;
    }

    /**
     * Filter print list view
     *
     * @return list of PrintData
     */
    public String filterPrintList() {
        MasterDataLoad masterDataLoad = MasterDataLoad.getInstance();
        session.remove("printStart");

        if (selectOption != null) {
            if (selectOption.equals("Not Printed")) {
                printList = masterDataLoad.getPrintList(WebConstants.VIEW_NOT_PRINTED);
            } else if (selectOption.equals("Printed")) {
//            session.remove("printStart");
                printList = masterDataLoad.getPrintList(WebConstants.VIEW_PRINTED);
            } else {
//            session.remove("printStart");
                printList = masterDataLoad.getPrintList(WebConstants.VIEW_ALL);
            }
        } else {
            printList = masterDataLoad.getPrintList(WebConstants.VIEW_ALL);
        }

        session.put("printList", printList);
        populate();
        return "success";
    }

    public String nextPage() {
        Integer i = (Integer) session.get("printStart");
        Integer count = (Integer) session.get("printCount");
        List<PrintData> printData = (List<PrintData>) session.get("printList");

        logger.debug("Next Page: Count {} , List Size {}", count, printData.size());

        if (i != null && printData.size() != count) {
            session.put("printStart", i + 10);
        }

        populate();
        return "success";
    }

    public String previousPage() {
        Integer i = (Integer) session.get("printStart");
        if (i != null && i != 0) {
            session.put("printStart", i - 10);
        }

        populate();
        return "success";
    }

    /**
     * Populate District list and Division list
     */
    private void populate() {
        String language = (String) session.get(WebConstants.SESSION_USER_LANG);
        districtList = districtDAO.getDistricts(language);
        //todo district id hardcoded for the moment
        divisionList = bdDivisionDAO.getDivisions(language, 11);
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

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public Map<Integer, String> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(Map<Integer, String> divisionList) {
        this.divisionList = divisionList;
    }

    public String getSelectOption() {
        return selectOption;
    }

    public void setSelectOption(String selectOption) {
        this.selectOption = selectOption;
    }
}
