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
import lk.rgd.Permission;

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
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map session;
    private boolean selectOption1;

    private User user;

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    private int birthDistrictId;
    private int birthDivisionId;
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
        //session.remove(WebConstants.SESSION_PRINT_COUNT);
        int pageNo = 1;

        printList = birthRegistrationService.getBirthCertificatePrintList(
                bdDivisionDAO.getBDDivisionByPK(dsDivisionList.keySet().iterator().next()), pageNo,
                appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), selectOption1, user);

        logger.debug("Cetificate Print List : items {} ", printList.size());
        return "pageLoad";
    }

    /**
     * Filter print list view by Not Printed and Printed. By default viwing Not Printed Confirmation List.
     * Used in first page load and first division selection.
     * Selected birthDivisionId and selected option(Not Printed or Printed) used in pagination.
     * returned List<BirthDeclaration >
     *
     * @return String success
     */

    public String filterPrintList() {
        populate();
        //session.remove(WebConstants.SESSION_PRINT_COUNT);

        int pageNo = 1;

        printList = birthRegistrationService.getConfirmationPrintList(
                bdDivisionDAO.getBDDivisionByPK(dsDivisionList.keySet().iterator().next()), pageNo,
                appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), selectOption1, user);

        logger.debug("Confirm Print List : items=" + printList.size());

        return "success";
    }

    /**
     * Used to move forward when list more than 10
     * in cetificate list.
     *
     * @return
     */
    public String cetificateNextPage() {
        populate();
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        int pageNo = ((printStart + noOfRows) / noOfRows) + 1;
        printList = birthRegistrationService.getBirthCertificatePrintList(bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, selectOption1, user);
        printStart += noOfRows;
        populate();
        return "success";
    }

    /**
     * Used to move backward.
     *
     * @return
     */
    public String cetificatePreviousPage() {
        populate();
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        int pageNo = printStart / noOfRows;
        printList = birthRegistrationService.getBirthCertificatePrintList(bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, selectOption1, user);
        printStart -= noOfRows;
        populate();
        return "success";
    }

    /**
     * Used to move forward when list more than 10
     * in confirmation list.
     *
     * @return
     */
    public String confirmNextPage() {
        populate();
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        int pageNo = ((printStart + noOfRows) / noOfRows) + 1;
        printList = birthRegistrationService.getConfirmationPrintList(bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, selectOption1, user);
        printStart += noOfRows;
        populate();
        return "success";
    }

    /**
     * Used to move backward.
     *
     * @return
     */
    public String confirmPreviousPage() {
        populate();
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        int pageNo = printStart / noOfRows;
        printList = birthRegistrationService.getConfirmationPrintList(bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, selectOption1, user);
        printStart -= noOfRows;
        populate();
        return "success";
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
                this.dsDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
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

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public void setDsDivisionList(Map<Integer, String> dsDivisionList) {
        this.dsDivisionList = dsDivisionList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getBirthDistrictId() {
        return birthDistrictId;
    }

    public void setBirthDistrictId(int birthDistrictId) {
        this.birthDistrictId = birthDistrictId;
    }

    public Map<Integer, String> getBdDivisionList() {
        return bdDivisionList;
    }

    public void setBdDivisionList(Map<Integer, String> bdDivisionList) {
        this.bdDivisionList = bdDivisionList;
    }

    public int getBirthDivisionId() {
        return birthDivisionId;
    }

    public void setBirthDivisionId(int birthDivisionId) {
        this.birthDivisionId = birthDivisionId;
    }

    public int getPrintStart() {
        return printStart;
    }

    public void setPrintStart(int printStart) {
        this.printStart = printStart;
    }


    public boolean isSelectOption1() {
        return selectOption1;
    }

    public void setSelectOption1(boolean selectOption1) {
        this.selectOption1 = selectOption1;
    }

}
