package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;
import lk.rgd.crs.api.domain.Race;
import lk.rgd.crs.api.dao.RaceDAO;
import lk.rgd.crs.web.WebConstants;

import lk.rgd.AppConstants;
import java.util.Map;
import java.util.List;

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

        if (language.equals("English")) {
            language = AppConstants.ENGLISH;
        } else if (language.equals("Sinhala")) {
            language = AppConstants.SINHALA;
        } else if (language.equals("Tamil")) {
            language = AppConstants.TAMIL;
        }
        districtList = raceDao.getRaces(language);
        logger.debug("inside populate : districts {}.", districtList);
    }

    public String getBirthConfirmationReport() {
        logger.debug("inside birth confirmation report");
        return "success";
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