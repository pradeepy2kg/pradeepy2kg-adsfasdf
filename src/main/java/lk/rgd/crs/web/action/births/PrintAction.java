package lk.rgd.crs.web.action.births;

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
 * @author Indunil Moremada
 */
public class PrintAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(PrintAction.class);

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

    private boolean printed;
    private boolean confirmListFlag;
    String language;

    private User user;

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    private int birthDistrictId;
    private int dsDivisionId;
    private int birthDivisionId;
    private int printStart;
    private int pageNo;

    public PrintAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO,
                       BirthRegistrationService birthRegistrationService, AppParametersDAO appParametersDAO) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.birthRegistrationService = birthRegistrationService;
        this.appParametersDAO = appParametersDAO;
    }

    /**
     * This method responsible for initially loading birth certificates or birth confirmations
     * which are not already printed. If confirmListFlag is set to true loads the birth
     * confirmations else laod the birth certificates. Returns a list of BirthDeclarations.
     *
     * @return
     */
    public String intiPrint() {
        populate();

        dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, language, user);
        if (!dsDivisionList.isEmpty()) {
            birthDivisionId = dsDivisionList.keySet().iterator().next();
        }

        bdDivisionList = bdDivisionDAO.getBDDivisionNames(birthDivisionId, language, user);
        if (!bdDivisionList.isEmpty()) {
            birthDivisionId = bdDivisionList.keySet().iterator().next();
        }
        logger.debug("birthDivisionId {} selected isPrinted {}", birthDivisionId, printed);
        setPageNo(1);
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        if (confirmListFlag) {
            printList = birthRegistrationService.getConfirmationPrintList(
                bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo,
                noOfRows, printed, user);
            logger.debug("Initializing confirmation Print list with {} items ", printList.size());
        } else {
            printList = birthRegistrationService.getBirthCertificatePrintList(
                bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo,
                appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), printed, user);
            logger.debug("Initializing certificate Print list with {} items ", printList.size());
        }
        return "pageLoad";
    }

    /**
     * This method responsible for filtering the displaying data based on
     * birth division and also it filters list as printed or Not-printed.
     * finally returns a list of BirthDeclarations according to the request.
     *
     * @return String success
     */

    public String filterPrintList() {
        populate();
        setPageNo(1);
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        logger.debug("Filtering list with birthDivision {} and printedFlag {}", birthDivisionId, printed);
        if (confirmListFlag) {
            printList = birthRegistrationService.getConfirmationPrintList(
                bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo,
                noOfRows, printed, user);
            logger.debug("Confirmation Print list {}  items  found ", printList.size());
        } else {
            printList = birthRegistrationService.getBirthCertificatePrintList(
                bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo,
                appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), printed, user);
            logger.debug("Certificate Print list {}  items  found ", printList.size());
        }
        return "success";
    }

    /**
     * Used to move forward when list grows more than 10 records.
     *
     * @return
     */
    public String nextPage() {
        populate();
        if (logger.isDebugEnabled()) {
            logger.debug("inside nextPage() : birthDivision {} selected isPrinted {}" + " current pageNo " + pageNo);
        }
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        pageNo = ((printStart + noOfRows) / noOfRows) + 1;

        if (confirmListFlag) {
            printList = birthRegistrationService.getConfirmationPrintList
                (bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, printed, user);
            logger.debug("next {}  items  loaded to birthConfirmation print list ", printList.size());
        } else {
            printList = birthRegistrationService.getBirthCertificatePrintList
                (bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, printed, user);
            logger.debug("next {}  items  loaded to birthCertificate print list ", printList.size());
        }
        printStart += noOfRows;
        populate();
        return "success";
    }

    /**
     * Used to move backward.
     *
     * @return
     */
    public String previousPage() {
        populate();
        if (logger.isDebugEnabled()) {
            logger.debug("inside previousPage() : birthDivision {} selected isPrinted {}" + " current pageNo " + pageNo);
        }
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        pageNo = printStart / noOfRows;

        if (confirmListFlag) {
            printList = birthRegistrationService.getConfirmationPrintList
                (bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, printed, user);
            logger.debug("previous {}  items  loaded to birthConfirmation print list ", printList.size());
        } else {
            printList = birthRegistrationService.getBirthCertificatePrintList
                (bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, printed, user);
            logger.debug("previous {}  items  loaded to birthCertificate print list ", printList.size());
        }
        printStart -= noOfRows;
        populate();
        return "success";
    }

    /**
     * Populate District list and Division list based on user
     */
    private void populate() {
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        setDistrictList(districtDAO.getDistrictNames(language, user));
        if (!getDistrictList().isEmpty()) {
            setBirthDistrictId(getDistrictList().keySet().iterator().next());
        }
        logger.debug("inside populate() : birthDistrictId {} selected", birthDistrictId);
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


    public boolean isPrinted() {
        return printed;
    }

    public void setPrinted(boolean printed) {
        this.printed = printed;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isConfirmListFlag() {
        return confirmListFlag;
    }

    public void setConfirmListFlag(boolean confirmListFlag) {
        this.confirmListFlag = confirmListFlag;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }
}
