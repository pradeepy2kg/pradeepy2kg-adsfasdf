package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;
import lk.rgd.crs.api.domain.Race;
import lk.rgd.crs.api.domain.Person;
import lk.rgd.crs.api.dao.RaceDAO;
import lk.rgd.crs.web.WebConstants;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
/**
 * Birth Confirmation related actions.
 *
 * @author chathuranga
 * @author amith
 */
public class BirthConfirmAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);

    private final RaceDAO raceDao;

    /**
     * pageNo is used to navigate through Birht Confirmation Form
     */
    private int pageNo;
    private String language;
    private List<Race> districtList;
    private Map session;

    public BirthConfirmAction(RaceDAO raceDao) {
        this.raceDao = raceDao;
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
        logger.debug("Step {} of 2.", getPageNo());

        switch (getPageNo()) {
            case 0:
            case 1:
                populate();
                logger.debug("birth confirmation page {}", getPageNo());
                return "form" + getPageNo();
            case 2:
                logger.debug("persistence code here.");
                //todo persist after validations
                return "success";
            default:
                return "error";
        }
    }

    /**
     * Populate data to Birth Confirmation Forms
     */
    private void populate() {
        language = (String) session.get(WebConstants.SESSION_USER_LANG);
        logger.debug("inside populate : {} observed.", getLanguage());

        districtList=raceDao.getRaces(language);
        logger.debug("inside populte : districts {}.", districtList);
    }

    public String getBirthConfirmationReport() {
  try {
            // compiling jasper report
            JasperReport report = JasperCompileManager.compileReport("/home/amith23/Desktop/amith23/Templates/BirthConfermation_report_test.jrxml");
            //setting data
            JRDataSource dataSource = createReportDataSource();
            // filling report with data sorce
            JasperPrint print = JasperFillManager.fillReport(report, new HashMap(), dataSource);
            // exporting report to HTML format
            lk.rgd.crs.web.jasper.JasperExportManager.exportReportToHtmlFileIncludeExternalScript(print, "/home/amith23/Desktop/amith23/Templates/BirthConfermation_report_test.html", "hikzzzzz");
        }
        catch (Exception e) {
            logger.info("jasper error", e);
        }

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

    public List<Race> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<Race> districtList) {
        this.districtList = districtList;
    }
}