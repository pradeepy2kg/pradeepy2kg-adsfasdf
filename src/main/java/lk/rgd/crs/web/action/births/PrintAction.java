package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.web.WebConstants;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Printing actions
 *
 * @author Chathuranga Withana
 * @author Indunil Moremada
 * @authar amith jayasekara
 */
public class PrintAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(PrintAction.class);

    private static final String BC_PRINT_ROWS_PER_PAGE = "crs.br_approval_rows_per_page";
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final LocationDAO locationDAO;
    private final UserDAO userDAO;
    private final AppParametersDAO appParametersDAO;
    private final BirthRegistrationService service;

    private List<BirthDeclaration> printList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map session;

    private boolean printed;
    private boolean confirmListFlag;
    private boolean allowPrintCertificate;
    private boolean directPrint;
    String language;

    private User user;

    /* helper fields to capture input from pages, they will then be processed before populating the bean */
    private int birthDistrictId;
    private int dsDivisionId;
    private int birthDivisionId;
    private int printStart;
    private int pageNo;
    private long[] index;
    private long bdId;
    private int locationId;
    private String issueUserId;
    private BirthDeclaration.BirthType birthType;

    public PrintAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO,
        BirthRegistrationService service, AppParametersDAO appParametersDAO, LocationDAO locationDAO, UserDAO userDAO) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.appParametersDAO = appParametersDAO;
        this.locationDAO = locationDAO;
        this.userDAO = userDAO;
        this.service = service;
    }

    /**
     * Responsible for loading the birth confirmation print list
     *
     * @return
     */
    public String loadConfirmationPrintList() {
        setConfirmListFlag(true);
        intiPrint();
        return "pageLoad";
    }

    /**
     * Responsible for loading the birth certificate print list
     *
     * @return
     */
    public String loadBirthCertificatePrintList() {
        intiPrint();
        return "pageLoad";
    }

    /**
     * This method responsible for initially loading birth certificates or birth confirmations
     * which are not already printed. If confirmListFlag is set to true loads the birth
     * confirmations else load the birth certificates. Returns a list of BirthDeclarations.
     */
    private void intiPrint() {
        populateInitialDistrict();
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, language, user);
        if (!dsDivisionList.isEmpty()) {
            dsDivisionId = dsDivisionList.keySet().iterator().next();
        }
        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        logger.debug("dsDivision {} selected isPrinted {}", dsDivisionId, printed);
        setPageNo(1);
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        if (confirmListFlag) {
            printList = service.getConfirmationPrintListByDSDivision(
                dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo,
                noOfRows, printed, user);
            logger.debug("Initializing confirmation Print list with {} items ", printList.size());
        } else {
            printList = service.getBirthCertificatePrintListByDSDivision(
                dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo,
                appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), printed, user);
            initPermissionForBirthCertificatePrint();
            logger.debug("Initializing certificate Print list with {} items ", printList.size());
        }
        if (printList.size() == 0) {
            addActionMessage(getText("noItemMsg.label"));
        }
    }

    /**
     * This method responsible for filtering the displaying data based on birth division and also it filters list as
     * printed or Not-printed. Finally returns a list of BirthDeclarations according to the request.
     *
     * @return String success
     */
    public String filterPrintList() {
        setPageNo(1);
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        logger.debug("Filtering list with birthDivision {} and printedFlag {}", birthDivisionId, printed);
        if (confirmListFlag) {
            if (birthDivisionId != 0) {
                printList = service.getConfirmationPrintList(
                    bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo,
                    noOfRows, printed, user);
            } else {
                printList = service.getConfirmationPrintListByDSDivision(
                    dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo,
                    noOfRows, printed, user);
            }
            logger.debug("Confirmation Print list {}  items  found ", printList.size());
        } else {
            if (birthDivisionId != 0) {
                printList = service.getBirthCertificatePrintList(
                    bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo,
                    appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), printed, user);
            } else {
                printList = service.getBirthCertificatePrintListByDSDivision(
                    dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo,
                    appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), printed, user);
            }
            initPermissionForBirthCertificatePrint();
            logger.debug("Certificate Print list {}  items  found ", printList.size());
        }
        if (printList.size() == 0) {
            addActionMessage(getText("noItemMsg.label"));
        }
        // TODO remove this
        populate();
        return SUCCESS;
    }

    /**
     * Method responsible for marking requested birth confirmations and birth certificate as printed.
     */
    public String markAsPrinted() {
        BirthDeclaration bdf = service.getById(bdId, user);
        logger.debug("requested to mark as print confirmListFlag : {} and bdId : {}", confirmListFlag, bdId);
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        final BirthDeclaration.State currentState = bdf.getRegister().getStatus();
        if (confirmListFlag) {
            if (currentState != BirthDeclaration.State.APPROVED) {
                addActionError(getText("birth.confirmation.invalid.state.to.marak.as.print"));
            } else {
                service.markLiveBirthConfirmationAsPrinted(bdf, user);
            }
            if (!directPrint) {
                if (birthDivisionId != 0) {
                    printList = service.getConfirmationPrintList(
                        bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, printed, user);
                } else {
                    printList = service.getConfirmationPrintListByDSDivision(
                        dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, printed, user);
                }
            }
        } else {
            birthType = bdf.getRegister().getBirthType();
            if (currentState != BirthDeclaration.State.ARCHIVED_CERT_GENERATED &&
                currentState != BirthDeclaration.State.ARCHIVED_ALTERED &&
                currentState != BirthDeclaration.State.ARCHIVED_CERT_PRINTED) {
                addActionError(getText("birth.certificate.invalid.state.to.mark.as.print"));

            } else {
                if (currentState == BirthDeclaration.State.ARCHIVED_CERT_GENERATED) {

                    if (locationId != 0 && issueUserId != null) {
                        logger.debug("Certificate issued locationId : {} and userId : {}", locationId, issueUserId);
                        bdf.getRegister().setOriginalBCPlaceOfIssue(locationDAO.getLocation(locationId));
                        bdf.getRegister().setOriginalBCIssueUser(userDAO.getUserByPK(issueUserId));

                    } else {
                        logger.warn("For the first time Birth certificate print issued location and user not valid");
                        return ERROR;
                    }

                    if (birthType == BirthDeclaration.BirthType.LIVE) {
                        service.markLiveBirthCertificateAsPrinted(bdf, user);
                    } else if (birthType == BirthDeclaration.BirthType.STILL) {
                        service.markStillBirthCertificateAsPrinted(bdf, user);
                    } else if (birthType == BirthDeclaration.BirthType.ADOPTION) {
                        service.markAdoptionBirthCertificateAsPrinted(bdf, user);
                    } else if (birthType == BirthDeclaration.BirthType.BELATED) {
                        service.markBelatedBirthCertificateAsPrinted(bdf, user);
                    }
                }
            }
            if (!directPrint) {
                //todo fix issue with pageNumber   this is a temporary solution  remove****** this argent
                if (pageNo == 0) {
                    pageNo = 1;
                }
                if (birthDivisionId != 0) {
                    printList = service.getBirthCertificatePrintList(
                        bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo,
                        appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), printed, user);
                } else {
                    printList = service.getBirthCertificatePrintListByDSDivision(
                        dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo,
                        appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), printed, user);
                }
                logger.debug("Certificate Print list {}  items  found ", printList.size());
            }
            initPermissionForBirthCertificatePrint();
        }
        if (!directPrint) {
            if (printList.size() == 0) {
                addActionMessage(getText("noItemMsg.label"));
            }
            populate();
        }
        return SUCCESS;
    }

    /**
     * Used to move forward when list grows more than 10 records.
     */
    public String nextPage() {
        if (logger.isDebugEnabled()) {
            logger.debug("inside nextPage() : birthDivision {} selected isPrinted {}" + " current pageNo " + pageNo);
        }
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        pageNo = ((printStart + noOfRows) / noOfRows) + 1;

        if (confirmListFlag) {
            if (birthDivisionId != 0) {
                printList = service.getConfirmationPrintList
                    (bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, printed, user);
            } else {
                printList = service.getConfirmationPrintListByDSDivision
                    (dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, printed, user);
            }
            logger.debug("next {}  items  loaded to birthConfirmation print list ", printList.size());
        } else {
            if (birthDivisionId != 0) {
                printList = service.getBirthCertificatePrintList
                    (bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, printed, user);
            } else {
                printList = service.getBirthCertificatePrintListByDSDivision
                    (dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, printed, user);
            }
            initPermissionForBirthCertificatePrint();
            logger.debug("next {}  items  loaded to birthCertificate print list ", printList.size());
        }
        printStart += noOfRows;
        populate();
        return SUCCESS;
    }

    /**
     * Used to move backward.
     */
    public String previousPage() {
        if (logger.isDebugEnabled()) {
            logger.debug("inside previousPage() : birthDivision {} selected isPrinted {}" + " current pageNo " + pageNo);
        }
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        pageNo = printStart / noOfRows;

        if (confirmListFlag) {
            if (birthDivisionId != 0) {
                printList = service.getConfirmationPrintList
                    (bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, printed, user);
            } else {
                printList = service.getConfirmationPrintListByDSDivision
                    (dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, printed, user);
            }
            logger.debug("previous {}  items  loaded to birthConfirmation print list ", printList.size());
        } else {
            if (birthDivisionId != 0) {
                printList = service.getBirthCertificatePrintList
                    (bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, printed, user);
            } else {
                printList = service.getBirthCertificatePrintListByDSDivision
                    (dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, printed, user);
            }
            initPermissionForBirthCertificatePrint();
            logger.debug("previous {}  items  loaded to birthCertificate print list ", printList.size());
        }
        printStart -= noOfRows;
        populate();
        return SUCCESS;
    }

    /**
     * This method is responsible for loading the previous state of birth certificate and birth confirmation print list
     * pages if it cancels the printing process
     */
    public String backToPreviousState() {
        int noOfRows = appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE);
        if (confirmListFlag) {
            if (birthDivisionId != 0) {
                printList = service.getConfirmationPrintList
                    (bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, printed, user);
            } else {
                printList = service.getConfirmationPrintListByDSDivision
                    (dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, printed, user);
            }
            logger.debug("previous {}  items  loaded to birthConfirmation print list ", printList.size());
        } else {
            if (birthDivisionId != 0) {
                printList = service.getBirthCertificatePrintList(
                    bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo,
                    appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), printed, user);
            } else {
                printList = service.getBirthCertificatePrintListByDSDivision(
                    dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo,
                    appParametersDAO.getIntParameter(BC_PRINT_ROWS_PER_PAGE), printed, user);
            }
            initPermissionForBirthCertificatePrint();
            logger.debug("Certificate Print list {}  items  found ", printList.size());
        }
        populate();
        return SUCCESS;
    }

    /**
     * Simple method to load the birth confirmation form detail after printing the birth certificate
     */
    public String directPrintBirthCertificate() {
        return SUCCESS;
    }

    /**
     * Populate District list and Division list based on user
     */
    private void populateInitialDistrict() {
        populate();
        if (!getDistrictList().isEmpty()) {
            setBirthDistrictId(getDistrictList().keySet().iterator().next());
        }
        logger.debug("inside populate() : birthDistrictId {} selected", birthDistrictId);
    }

    private void populate() {
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        setDistrictList(districtDAO.getDistrictNames(language, user));
        if (!getDistrictList().isEmpty()) {
            if (birthDistrictId > 0) {
                setBirthDistrictId(birthDistrictId);
            } else {
                setBirthDistrictId(getDistrictList().keySet().iterator().next());
            }
        }
        //dsDivisions

        this.dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, language, user);
        //setting bdDivisions
        if (!dsDivisionList.isEmpty()) {
            if (dsDivisionId > 0) {
                setDsDivisionId(dsDivisionId);
            } else {
                dsDivisionId = dsDivisionList.keySet().iterator().next();
            }
            bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        }
        /*if (!bdDivisionList.isEmpty())
            if (birthDivisionId > 0)
                setBirthDivisionId(birthDivisionId);
            else
                birthDivisionId = bdDivisionList.keySet().iterator().next();*/

    }

    private void initPermissionForBirthCertificatePrint() {
        allowPrintCertificate = user.isAuthorized(Permission.PRINT_BIRTH_CERTIFICATE);
        logger.debug("check user permissions - {}  ", allowPrintCertificate);
    }

    public List<BirthDeclaration> getPrintList() {
        return printList;
    }

    public void setPrintList(List<BirthDeclaration> printList) {
        this.printList = printList;
    }

    public void setSession(Map session) {
        this.session = session;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
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

    public long[] getIndex() {
        return index;
    }

    public void setIndex(long[] index) {
        this.index = index;
    }

    public BirthRegistrationService getService() {
        return this.service;
    }

    public Map getSession() {
        return this.session;
    }

    public long getBdId() {
        return bdId;
    }

    public void setBdId(long bdId) {
        this.bdId = bdId;
    }

    public boolean isAllowPrintCertificate() {
        return allowPrintCertificate;
    }

    public void setAllowPrintCertificate(boolean allowPrintCertificate) {
        this.allowPrintCertificate = allowPrintCertificate;
    }

    public boolean isDirectPrint() {
        return directPrint;
    }

    public void setDirectPrint(boolean directPrint) {
        this.directPrint = directPrint;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getIssueUserId() {
        return issueUserId;
    }

    public void setIssueUserId(String issueUserId) {
        this.issueUserId = issueUserId;
    }
}
