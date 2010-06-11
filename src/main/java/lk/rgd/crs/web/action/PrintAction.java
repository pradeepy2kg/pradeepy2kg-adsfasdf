package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.AppParametersDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;

import java.util.List;
import java.util.Map;
import java.util.Locale;

import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.PrintData;
import lk.rgd.crs.api.domain.BirthDeclaration;
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

    private static final String BC_PRINT_ROWS_PER_PAGE = "crs.br_approval_rows_per_page";
    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final AppParametersDAO appParametersDAO;

    private List<BirthDeclaration> printList;
    private Map<Integer, String> districtList;
    private Map<Integer, String> divisionList;
    private Map session;
    private String selectOption;

    private User user;

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    private int districtId;
    private int divisionId;

    public PrintAction(DistrictDAO districtDAO, BDDivisionDAO bdDivisionDAO, BirthDeclarationDAO birthDeclarationDAO, AppParametersDAO appParametersDAO) {
        this.districtDAO = districtDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.appParametersDAO = appParametersDAO;
    }

    /**
     * Filter print list view by Not Printed and Printed. By default viwing Not Printed Confirmation List.
     * Used in first page load and first division selection.
     * Selected divisionId and selected option(Not Printed or Printed) assigned to the session to use in pagination.
     * returned List<BirthDeclaration > assigned to the session.
     *
     * @return String success
     */
    public String filterPrintList() {
        populate();
        session.remove(WebConstants.SESSION_PRINT_START);
        session.remove(WebConstants.SESSION_PRINT_COUNT);
        session.put(WebConstants.SESSION_BCPRINT_SELECTED_DIVISION, divisionId);
        session.put(WebConstants.RADIO_ALREADY_PRINT, selectOption);

        int pageNo = 1;

        if (selectOption != null) {
            if (WebConstants.RADIO_ALREADY_PRINT.equals(selectOption)) {
                printList = birthDeclarationDAO.getConfirmationPrintPending(
                        bdDivisionDAO.getBDDivisionByPK(divisionId), pageNo, appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), true);
            } else {
                printList = birthDeclarationDAO.getConfirmationPrintPending(
                        bdDivisionDAO.getBDDivisionByPK(divisionId), pageNo, appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), false);
            }
        } else {
            printList = birthDeclarationDAO.getConfirmationPrintPending(
                    bdDivisionDAO.getBDDivisionByPK(divisionId), pageNo, appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), false);
        }

        logger.debug("Confirm Print List : items=" + printList.size());
        session.put(WebConstants.SESSION_PRINT_LIST, printList);
        return "success";
    }

    /**
     * Used in Pagination to move forward.
     *
     * @return
     */
    public String nextPage() {
        Integer printStart = (Integer) session.get(WebConstants.SESSION_PRINT_START);
        Integer divisionId = (Integer) session.get(WebConstants.SESSION_BCPRINT_SELECTED_DIVISION);
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        int pageNo = ((printStart + noOfRows) / noOfRows) + 1;
        boolean printed = checkPrinted();

        printList = birthDeclarationDAO.getConfirmationPrintPending(bdDivisionDAO.getBDDivisionByPK(divisionId), pageNo, noOfRows, printed);
        session.put(WebConstants.SESSION_PRINT_START, printStart + noOfRows);
        session.put(WebConstants.SESSION_PRINT_LIST, printList);

        populate();
        return "success";
    }

    /**
     * Used in Pagination to move backward.
     *
     * @return
     */
    public String previousPage() {
        Integer printStart = (Integer) session.get(WebConstants.SESSION_PRINT_START);
        Integer divisionId = (Integer) session.get(WebConstants.SESSION_BCPRINT_SELECTED_DIVISION);
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        int pageNo = printStart / noOfRows;
        boolean printed = checkPrinted();

        printList = birthDeclarationDAO.getConfirmationPrintPending(bdDivisionDAO.getBDDivisionByPK(divisionId), pageNo, noOfRows, printed);
        session.put(WebConstants.SESSION_PRINT_START, printStart - noOfRows);
        session.put(WebConstants.SESSION_PRINT_LIST, printList);

        populate();
        return "success";
    }

    /**
     * Checks selected radio button option.
     * If Printed selected return true else false.
     *
     * @return true if printed
     */
    private boolean checkPrinted() {
        return WebConstants.RADIO_ALREADY_PRINT.equals((String) session.get(WebConstants.RADIO_ALREADY_PRINT));
    }

    /**
     * Populate District list and Division list
     */
    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        districtList = districtDAO.getDistrictNames(language, user);
        // TODO division list loaded by using district list's first district
        if (!districtList.isEmpty()) {
            int selectedDistrictId = districtList.keySet().iterator().next();
            divisionList = bdDivisionDAO.getBDDivisionNames(selectedDistrictId, language, user);
        }
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}
