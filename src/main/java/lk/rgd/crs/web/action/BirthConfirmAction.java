package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.RaceDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.RequestAware;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.ChildInfo;
import lk.rgd.crs.api.domain.ParentInfo;
import lk.rgd.AppConstants;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.DSDivisionDAOImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.beans.PropertyDescriptor;
import java.beans.Introspector;
import java.beans.BeanInfo;
import java.io.UnsupportedEncodingException;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * Birth Confirmation related actions.
 *
 * @author chathuranga
 * @author amith
 */
public class BirthConfirmAction extends ActionSupport implements SessionAware, RequestAware {
    private static final Logger logger = LoggerFactory.getLogger(BirthConfirmAction.class);

    private final RaceDAO raceDao;
    private final DistrictDAO districtDAO;
    private final DSDivisionDAOImpl dsDivisionDAOImpl;

    /**
     * approveSelected is from BirhRegisterApproval.jsp
     * if it is populated redirected to BirthApprovalAction
     */
    private String approveSelected;
    private int pageNo;
    private String language;
    private Map<Integer, String> districtList;
    private Map<Integer, String> raceList;
    private Map session;
    private HttpServletRequest request;
    private ChildInfo child;
    private ParentInfo parent;
    private MarriageInfo marriage;
    private ConfirmantInfo confirmar;
    private boolean confirmantSign;

    private BirthDeclaration birthConfirm;

    public BirthConfirmAction(RaceDAO raceDao, DistrictDAO districtDAO, DSDivisionDAOImpl dsDivisionDAOImpl) {
        this.raceDao = raceDao;
        this.districtDAO = districtDAO;
        this.dsDivisionDAOImpl = dsDivisionDAOImpl;
        logger.debug("inside birth register action constructor");
    }

    /**
     * This method is responsible for loading and capture data for all 2 BCF pages as well
     * as their persistance. pageNo hidden variable which is passed to the action (empty=0 for the
     * very first form page) is used to decide which state of the process we are in. at the last step
     * only we do a persistance, until then all data will be in the session. This is a design decision
     * to limit DB writes. Masterdata population will be done before displaying every page.
     * This will have no performace impact as they will be cached in the backend.
     */
    public String birthConfirmation() {
        logger.debug("Step {} of 2.", pageNo);
        if (pageNo > 2) {
            return "error";
        } else if (pageNo == 2) {
            //logger.debug("submitted value {} {} {}", child.getChildFullNameOfficialLang(),
            //        child.getChildFullNameEnglish());
            // all pages captured, proceed to persist after validations
            // todo business validations and persiatance
          // BirthDeclaration confirm = (BirthDeclaration) session.get("birthConfirm");
//
//            logger.debug("Birth Confirmation Persist : {} , {} .", confirm.getBdfSerialNo(), confirm.getBirthDistrictId());
          // logger.debug("Birth Confirmation Persist : {} , {}.", confirm.getFatherFullName(), confirm.getMotherFullName());
            logger.debug("Birth Confirmation Persist : {} , {} .",getParent().getFatherFullName(), getParent().getMotherFullName());
            return "success";
        }

        populate();
        if (pageNo == 0) {
            initForm();
        } else {
            logger.debug("Step {} of 2.", pageNo);
            if (pageNo > 2) {
                return "error";
            } else if (pageNo == 2) {
                // all pages captured, proceed to persist after validations
                // todo business validations and persiatance
               // BirthDeclaration confirm = (BirthDeclaration) session.get("birthConfirm");

                logger.debug("Birth Confirmation Persist : {} , {} .",getParent().getFatherFullName(), getParent().getMotherFullName());
                return "success";
            }

            populate();
            if (pageNo == 0) {
                initForm();
            } else {
                // submissions of pages 1 - 2
                //BirthDeclaration confirm = (BirthDeclaration) session.get("birthConfirm");

                //logger.debug("Birth Confirmation : District {} .", confirm.getBdfSerialNo(), confirm.getBirthDistrictId());
//logger.debug("Birth Confirmation Persist : {} , {}.", confirm.getFatherFullName(), confirm.getMotherFullName());

                try {
                    beanMerge();
                } catch (Exception e) {
                    handleErrors(e);
                    return "error";
                }
            }

          
        }
    return "form" + pageNo;
    }

    /**
     * update the bean in session with the values of local bean
     */
    private void beanMerge() throws Exception {
        BirthDeclaration target = (BirthDeclaration) session.get("birthConfirm");
        BeanInfo beanInfo = Introspector.getBeanInfo(BirthDeclaration.class);

        // Iterate over all the attributes
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            Object originalValue = descriptor.getReadMethod().invoke(target);

            // Only copy values where the session value is null or empty (do not replace already set
            // values in the session)
            if ((originalValue == null) || (originalValue.equals(""))) {
                Object defaultValue = descriptor.getReadMethod().invoke(birthConfirm);
                descriptor.getWriteMethod().invoke(target, defaultValue);
            } else {
                logger.debug("field {} not merged, value was {}", descriptor.getReadMethod(), originalValue);
            }
        }

        session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, target);
    }

    private void handleErrors(Exception e) {
        logger.error(e.getLocalizedMessage());
        //todo pass the error to the error.jsp page
    }

    /**
     *  update the bean in session with the values of local bean
     */
//   private void beanMerge() throws Exception {
//       BirthDeclaration target = (BirthDeclaration) session.get("birthRegister");
//       BeanInfo beanInfo = Introspector.getBeanInfo(BirthDeclaration.class);
//
//       // Iterate over all the attributes
//       for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
//           Object originalValue = descriptor.getReadMethod().invoke(target);
//
//           // Only copy values values where the destination values is null
//           if (originalValue == null) {
//               Object defaultValue = descriptor.getReadMethod().invoke(birthRegister);
//               descriptor.getWriteMethod().invoke(target, defaultValue);
//           } else {
//               logger.debug("field {} not merged, value was {}", descriptor.getReadMethod(), originalValue );
//           }
//       }
//
//       session.put("birthRegister", target);
//   }

    /**
     * initialises the birthRegister bean with proper initial values (depending on user, date etc) and
     * store it in session
     */
    private void initForm() {
        setBirthConfirm(new BirthDeclaration());
        //session.put("birthConfirm", getBirthConfirm());
        //todo set fields to proper initial values based on user and date
    }

    /**
     * Populate data to Birth Confirmation Forms
     */
    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("inside populate : {} observed.", language);


        districtList = districtDAO.getDistrictNames(language, user);
        raceList = raceDao.getRaces(language);

        //todo temporary solution until use a method to show Map in UI
        session.put("districtList", districtList);
        session.put("raceList", raceList);

        logger.debug("inside populte : districts , countries and races populated.");
    }

    public String getBirthConfirmationReport() {
        try {
            logger.info(AppConstants.HOME_DIRECTORY);
            // compiling jasper report
            JasperReport report = JasperCompileManager.compileReport(AppConstants.HOME_DIRECTORY + "/templates/birth_confirmation_report_part1_final_test.jrxml");
            //setting data
            JRDataSource dataSource = createReportDataSource();
            // filling report with data sorce
            JasperPrint print = JasperFillManager.fillReport(report, new HashMap(), dataSource);
            // exporting report to HTML format
            lk.rgd.crs.web.jasper.JasperExportManager.exportReportToHtmlFileIncludeExternalScript(print, "/home/amith23/Desktop/amith23/Templates/5.html", "hikzzzzz");
        }
        catch (Exception e) {
            logger.info("jasper error", e);
        }
        session.put("page_title", "birth confirm report");
        return "success";
    }

    /*creating a pojo data source*/
    private JRDataSource createReportDataSource() {
        JRBeanArrayDataSource dataSource;
        Person[] reportRows = initializeBeanArray();
        dataSource = new JRBeanArrayDataSource(reportRows);
        return dataSource;
    }

    /*
    * init data
    * */
    private Person[] initializeBeanArray() {
        //test data
        String uniName = "\u0DBD\u0DD2\u0DBA\u0DCF\u0DB4\u0DAF\u0DD2\u0D82\u0DA0\u0DD2 \u0DAF\u0DD2\u0DB1\u0DBA";
        String uniLastName = "\u0D9A\u0DAD\u0DD4\u0DC0\u0DBB\u0DBA\u0DCF\u0D9C\u0DDA \u0DB1\u0DB8";
        Person[] reportRows = new Person[1];
        // accessing session data
        Object dataObj = session.get("birthRegister");
        BirthDeclaration birt = (BirthDeclaration) dataObj;
        logger.info("session object", dataObj);
        Person p = new Person();
        p.setId(new Long("10000"));
        p.setLastName(convertToString(uniLastName));
        p.setName(convertToString(uniName));

        reportRows[0] = p;
        return reportRows;
    }

    /*convert a unicode sequance to a String*/
    private String convertToString(String unicodeSequance) {
        String converted = null;
        try {
            byte[] utf8 = unicodeSequance.getBytes("UTF-8");
            converted = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("unicode sequance cannot convert to UTF-8", e);
        }
        return converted;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public Map<Integer, String> getRaceList() {
        return raceList;
    }

    public void setRaceList(Map<Integer, String> raceList) {
        this.raceList = raceList;
    }

    public BirthDeclaration getBirthConfirm() {
        return birthConfirm;
    }

    public void setBirthConfirm(BirthDeclaration birthConfirm) {
        this.birthConfirm = birthConfirm;
    }

    public void setRequest(Map map) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public ChildInfo getChild() {
        return child;
    }

    public void setChild(ChildInfo child) {
        this.child = child;
    }
    
    public ParentInfo getParent() {
        return parent;
    }

    public void setParent(ParentInfo parent) {
        this.parent = parent;
    }

    public MarriageInfo getMarriage() {
        return marriage;
    }

    public void setMarriage(MarriageInfo marriage) {
        this.marriage = marriage;
    }
     public ConfirmantInfo getConfirmar() {
        return confirmar;
    }

    public void setConfirmar(ConfirmantInfo confirmar) {
        this.confirmar = confirmar;
    }

    public boolean isConfirmantSign() {
        return confirmantSign;
    }

    public void setConfirmantSign(boolean confirmantSign) {
        this.confirmantSign = confirmantSign;
    }
}