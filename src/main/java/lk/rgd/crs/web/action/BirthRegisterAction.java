package lk.rgd.crs.web.action;


import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.crs.api.domain.District;
import lk.rgd.crs.api.domain.BirthRegister;
import lk.rgd.crs.api.domain.Person;
import lk.rgd.crs.api.service.BirthRegisterService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.LoginBD;
import lk.rgd.crs.web.util.MasterDataLoad;
import org.apache.struts2.interceptor.SessionAware;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;


/**
 * EntryAction is a struts action class
 *
 * @author indunil Moremada
 * @author Chathranga
 * @author Amith
 * @author Duminda
 */

public class BirthRegisterAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);
    private final BirthRegisterService service;

    private String childDOB;
    private String year;
    private String month;
    private String day;
    private String fatherDOB;
    private String motherAdmissionNoAndDate;
    private String motherDOB;
    private String dateOfMarriage;
    private BirthRegister birthRegister;
    /*pageNo is used to decide the next pageNo of the Birht Registration Form*/
    private int pageNo;
    private LoginBD loginBD = new LoginBD();

    private String language;
    private List<Person> myList;
    private Map session;

    private String scopeKey;
    private ArrayList<District> districtList;
    private ArrayList countryList;

    public String welcome() {
        return "success";
    }

    public BirthRegisterAction(BirthRegisterService service) {
        this.service = service;
        logger.debug("inside birth register action constructor");
    }

   /**
      *  This method is responsible for loading and capture data for all 4 BDF pages as well
      *  as their persistance. pageNo hidden variable which is passed to the action (empty=0 for the
      *  very first form page) is used to decide which state of the process we are in. at the last step
      *  only we do a persistance, until then all data will be in the session. This is a design decision
      *  to limit DB writes. Masterdata population will be done before displaying every page.
      *  This will have no performace impact as they will be cached in the backend.
      */
   public String birthRegistration() {
       logger.debug("Step {} of 4 ", pageNo);
       if (pageNo > 4) {
           return "error";
       } else if (pageNo == 4) {
           // all pages captured, proceed to persist after validations
           // todo business validations and persiatance
           return "success";
       }

       populate();
       if (pageNo == 0) {
           initForm();
       } else {
           // submissions of pages 1 - 4
           try {
               beanMerge();
           } catch (Exception e) {
               handleErrors(e);
               return "error";
           }
       }

       return "form" + pageNo;
   }

   private void handleErrors(Exception e) {
       logger.error(e.getLocalizedMessage());
       //todo pass the error to the error.jsp page
   }

   /**
    *  update the bean in session with the values of local bean
    */
   private void beanMerge() throws Exception {
       BirthRegister target = (BirthRegister) session.get("birthRegister");
       BeanInfo beanInfo = Introspector.getBeanInfo(BirthRegister.class);

       // Iterate over all the attributes
       for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
           Object originalValue = descriptor.getReadMethod().invoke(target);

           // Only copy values values where the destination values is null
           if (originalValue == null) {
               Object defaultValue = descriptor.getReadMethod().invoke(birthRegister);
               descriptor.getWriteMethod().invoke(target, defaultValue);
           }
       }

       session.put("birthRegister", target);
   }

   private void initForm() {
       birthRegister = new BirthRegister();
       //todo set fields to proper initial values based on user and date
   }

    /**
     * creat Birth confermation report in PDF type
     * exception throws if pre compiled .jasper file is not available
     * <p/>
     * eng>>>1
     * sinhala>>2
     * tamil>>>3
     */
    public String getBirtConfermationReport() {
        /** List to use as our JasperReports dataSource.
         * Store people in our dataSource list (normally would come from database).
         */
        return "success";
    }

    /**
     * birthRegisterFinalizer is called by the fourth jsp page of the Birth
     * Registration Form it finalize the birthRegister Entity bean and send
     * it to the Business delegate
     *
     * @return String
     */
    public String birthRegisterFinalizer() {
        birthRegister = (BirthRegister) session.get("birthRegister");
        //birthRegister.setChildDOB(new EPopDate().getDate(childDOB));
        return "success";
    }

    /**
     * Populate data to the UIs
     */
    private void populate() {
        MasterDataLoad masterDataLoad = MasterDataLoad.getInstance();
        logger.debug("inside populate : masterload obtained.");

        language = (String) (session.get(WebConstants.SESSION_USER_LANG));
        logger.debug("inside populate : {} observed.", language);

        setDistrictList((ArrayList<District>) masterDataLoad.loadDistricts(language));
        logger.debug("inside populte : districts set, setting countries.");
        setCountryList((ArrayList) masterDataLoad.loadCountries(language));
        logger.debug("inside populte : countries {}", countryList);
    }

    public String getFatherDOB() {
        return fatherDOB;
    }

    public void setFatherDOB(String fatherDOB) {
        this.fatherDOB = fatherDOB;
    }

    public String getMotherAdmissionNoAndDate() {
        return motherAdmissionNoAndDate;
    }

    public void setMotherAdmissionNoAndDate(String motherAdmissionNoAndDate) {
        this.motherAdmissionNoAndDate = motherAdmissionNoAndDate;
    }

    public String getMotherDOB() {
        return motherDOB;
    }

    public void setMotherDOB(String motherDOB) {
        this.motherDOB = motherDOB;
    }

    public String getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(String dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public BirthRegister getBirthRegister() {
        return birthRegister;
    }

    public void setBirthRegister(BirthRegister birthRegister) {
        this.birthRegister = birthRegister;
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

    public List<Person> getMyList() {
        return myList;
    }

    public void setMyList(List<Person> myList) {
        this.myList = myList;
    }

    public void setSession(Map map) {
        this.session = map;
    }

    public Map getSession() {
        return session;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getChildDOB() {
        return childDOB;
    }

    public void setChildDOB(String childDOB) {
        this.childDOB = childDOB;
    }

    public void setModel(BirthRegister o) {
        birthRegister = o;
    }

    public BirthRegister getModel() {
        return birthRegister;
    }

    public void setScopeKey(String s) {
        this.scopeKey = s;
    }

    public String getScopeKey() {
        return scopeKey;
    }

    public ArrayList<District> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(ArrayList<District> districtList) {
        this.districtList = districtList;
    }

    public ArrayList getCountryList() {
        return countryList;
    }

    public void setCountryList(ArrayList countryList) {
        this.countryList = countryList;
    }
}