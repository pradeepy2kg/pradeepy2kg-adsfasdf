package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;
import lk.rgd.crs.api.domain.Person;
import lk.rgd.crs.api.dao.RaceDAO;
import lk.rgd.crs.api.dao.DistrictDAO;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.AppConstants;

import java.util.Map;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
/**
 * Birth Confirmation related actions.
 *
 * @author Chathuranga Withana
 * @author Amith Jayasekara
 */
public class BirthConfirmAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);

    private final RaceDAO raceDao;
    private final DistrictDAO districtDAO;
    
    private int pageNo;
    private String language;
    private Map<Integer,String> districtList;
    private Map<Integer,String> raceList;
    private Map session;

    public BirthConfirmAction(RaceDAO raceDao, DistrictDAO districtDAO) {
        this.raceDao = raceDao;
        this.districtDAO = districtDAO;
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

        //todo temporary fix : should be changed
        if (language.equals("English")) {
            language = AppConstants.ENGLISH;
        } else if (language.equals("Sinhala")) {
            language = AppConstants.SINHALA;
        } else if (language.equals("Tamil")) {
            language = AppConstants.TAMIL;
        }

        districtList=districtDAO.getDistricts(language);
        raceList=raceDao.getRaces(language);

        //todo temporary solution until use a method to show Map in UI
        session.put("districtList", districtList);
        session.put("raceList",raceList);

        logger.debug("inside populte : districts , countries and races populated.");
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
            lk.rgd.crs.web.jasper.JasperExportManager.exportReportToHtmlFileIncludeExternalScript(print, "/home/amith23/Desktop/amith23/Templates/5.html", "hikzzzzz");
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
}