package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;

import java.util.List;
import java.util.Map;
import java.util.Locale;

import lk.rgd.crs.web.util.MasterDataLoad;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.PrintData;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.dao.DistrictDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.common.api.domain.User;

/**
 * Printing actions
 *
 * @author Chathuranga Withana
 */
public class PrintAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);

    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final BirthDeclarationDAO birthDeclarationDAO;

    private List<BirthDeclaration> printList;
    private Map<Integer, String> districtList;
    private Map<Integer, String> divisionList;
    private Map session;
    private String selectOption;

    public PrintAction(DistrictDAO districtDAO, BDDivisionDAO bdDivisionDAO, BirthDeclarationDAO birthDeclarationDAO) {
        this.districtDAO = districtDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.birthDeclarationDAO = birthDeclarationDAO;
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
                printList = birthDeclarationDAO.getConfirmationPrintPending(11, 1, false);
            } else if (selectOption.equals("Printed")) {
                printList = birthDeclarationDAO.getConfirmationPrintPending(11, 1, true);
            }
        }

        session.put("printList", printList);
        populate();
        return "success";
    }

    // todo some problems still there in UI
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

    // todo some problems still there in UI
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
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        districtList = districtDAO.getDistricts(language, user);
        //todo district id hardcoded for the moment
        divisionList = bdDivisionDAO.getDivisions(language, 11, user);
    }

    public List<BirthDeclaration> getPrintList() {
        return printList;
    }

    public void setPrintList(List<BirthDeclaration> printList) {
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
