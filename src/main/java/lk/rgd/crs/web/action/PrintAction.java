package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.crs.api.service.BirthRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;

import java.util.List;
import java.util.Map;
import java.util.Locale;

import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.dao.BDDivisionDAO;
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
    private final DSDivisionDAO dsDivisionDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final BirthRegistrationService birthRegistrationService;
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
    private int printStart;

    public PrintAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO,
                       BirthRegistrationService birthRegistrationService, AppParametersDAO appParametersDAO) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.birthRegistrationService = birthRegistrationService;
        this.appParametersDAO = appParametersDAO;
    }

    /**
     * Birth Cetificate list confirmed without changes and confirmation changes approved.
     * Filter Cetificate print list, Not Printed and Printed. Default viwing Not Printed Confirmation List.
     *
     * @return
     */

    public String birthCertificatePrintList() {
        populate();
        session.remove(WebConstants.SESSION_PRINT_COUNT);
        divisionId = divisionList.keySet().iterator().next();
        selectOption = "Not Printed";
        int pageNo = 1;

        printList = birthRegistrationService.getBirthCertificatePrintList(
                bdDivisionDAO.getBDDivisionByPK(divisionId), pageNo,
                appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE),
                WebConstants.RADIO_ALREADY_PRINT.equals(selectOption));

        logger.debug("Cetificate Print List : items {}", printList.size());
        return "pageLoad";
    }

    /**
     * Filter print list view by Not Printed and Printed. By default viwing Not Printed Confirmation List.
     * Used in first page load and first division selection.
     * Selected divisionId and selected option(Not Printed or Printed) used in pagination.
     * returned List<BirthDeclaration >
     *
     * @return String success
     */

    public String filterPrintList() {
        populate();
        session.remove(WebConstants.SESSION_PRINT_COUNT);
        if (selectOption == null) {
            divisionId = divisionList.keySet().iterator().next();
            selectOption = "Not Printed";
        }
        int pageNo = 1;

        if (selectOption != null) {
            if (WebConstants.RADIO_ALREADY_PRINT.equals(selectOption)) {
                printList = birthRegistrationService.getConfirmationPrintList(
                        bdDivisionDAO.getBDDivisionByPK(divisionId), pageNo, appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), true);
            } else {
                printList = birthRegistrationService.getConfirmationPrintList(
                        bdDivisionDAO.getBDDivisionByPK(divisionId), pageNo, appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), false);
            }
        } else {
            printList = birthRegistrationService.getConfirmationPrintList(
                    bdDivisionDAO.getBDDivisionByPK(divisionId), pageNo, appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), false);
        }
        logger.debug("Confirm Print List : items=" + printList.size());

        return "success";
    }

    /**
     * Used in Pagination to move forward.
     *
     * @return
     */
    public String nextPage() {
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        int pageNo = ((printStart + noOfRows) / noOfRows) + 1;
        boolean printed = checkPrinted();
        printList = birthRegistrationService.getConfirmationPrintList(bdDivisionDAO.getBDDivisionByPK(divisionId), pageNo, noOfRows, printed);
        printStart += noOfRows;
        populate();
        return "success";
    }

    /**
     * Used in Pagination to move backward.
     *
     * @return
     */
    public String previousPage() {
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        int pageNo = printStart / noOfRows;
        boolean printed = checkPrinted();
        printList = birthRegistrationService.getConfirmationPrintList(bdDivisionDAO.getBDDivisionByPK(divisionId), pageNo, noOfRows, printed);
        printStart -= noOfRows;
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
            Map<Integer, String> dsDivisionList = dsDivisionDAO.getDSDivisionNames(selectedDistrictId, language, user);
            if (!dsDivisionList.isEmpty()) {
                int dsDivisionId = dsDivisionList.keySet().iterator().next();
                divisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
            }
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

    public int getPrintStart() {
        return printStart;
    }

    public void setPrintStart(int printStart) {
        this.printStart = printStart;
    }
}
