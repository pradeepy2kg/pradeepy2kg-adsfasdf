package lk.rgd.crs.web.action;


import lk.rgd.crs.api.domain.BirthRegister;
import lk.rgd.crs.api.domain.Person;
import lk.rgd.crs.api.service.BirthRegisterService;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.log4j.Logger;
import lk.rgd.crs.web.util.Constant;
import lk.rgd.crs.web.util.EPopDate;
import lk.rgd.crs.web.util.LoginBD;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;


/**
 * EntryAction is a struts action class
 *
 * @author indunil Moremada
 * @author Chathranga
 * @author Amith
 * @author Duminda
 */

public class BirthRegisterAction extends ActionSupport implements SessionAware {

    private String userName;
    private String password;

    private String childDOB;
    private String year;
    private String month;
    private String day;
    private String fatherDOB;
    private String motherAdmissionNoAndDate;
    private String motherDOB;
    private String dateOfMarriage;
    private BirthRegister birthRegister;
    private Logger log = Logger.getLogger(this.getClass().getName());
    /*pageNo is used to decide the next pageNo of the Birht Registration Form*/
    private int pageNo;
    private LoginBD loginBD = new LoginBD();

    private String language;
    private List<Person> myList;
    private Map session;

    public String welcome() {
        return "success";
    }

    /*
   *  User Login of the EPR System.
   * */
    public String login() {
        if (loginBD.login(userName, password)) {
            this.setLanguage(loginBD.getLanguage(userName));
            return "success";
        }
        return "error";
    }


    /**
     * Set the Language that the user preffered to work.
     */
    public String selectLanguage() {
        this.setLanguage(language);
        return "success";
    }

    public String pageLoad() {
        //birthRegister = new BirthRegister();
        //session = ActionContext.getContext().getSession();
        //session.put("birthRegister", birthRegister);
        return "pageLoad";
    }

    /**
     * method is used for birth registration purposes birthRegister Entity
     * Bean is populated based on the variable values and return type is
     * decided based on the pageNo variable which is  passed from the
     * jsp page
     *
     * @return String which decides next page to be loaded
     */
    public String birthRegistrationPreProcessor() {
        switch (pageNo) {
            case 1:
                //birthRegister.setChildDOB(new EPopDate().getDate(year + "/" + month + "/" + day));
                return "form2";
            case 2:
                /*temp is a tempory  instance which holds the previously entered birth registration information*/
//                    BirthRegister temp=(BirthRegister) session.get("birthRegister");
//                    temp.setFathersNIC(birthRegister.getFathersNIC());
//                    temp.setFatherForeignerPassportNo(birthRegister.getFatherForeignerPassportNo());
//                    temp.setFatherForeignerCountry(birthRegister.getFatherForeignerCountry());
//                    temp.setFatherFullName(birthRegister.getFatherFullName());
//                    temp.setFatherDOB(new EPopDate().getDate(fatherDOB));
//                    temp.setFatherBirthPlace(birthRegister.getFatherBirthPlace());
//                    temp.setFatherRace(birthRegister.getFatherRace());
//                    temp.setMotherNIC(birthRegister.getMotherNIC());
//                    temp.setMotherPassportNo(birthRegister.getMotherPassportNo());
//                    temp.setMotherCountry(birthRegister.getMotherCountry());
//                    temp.setMotherAdmissionNoAndDate(birthRegister.getMotherAdmissionNoAndDate());
//                    temp.setMotherFullName(birthRegister.getMotherFullName());
//                    temp.setMotherDOB(new EPopDate().getDate(motherDOB));
//                    temp.setMotherBirthPlace(birthRegister.getMotherBirthPlace());
//                    temp.setMotherRace(birthRegister.getMotherRace());
//                    temp.setMotherAgeAtBirth(birthRegister.getMotherAgeAtBirth());
//                    temp.setMotherRace(birthRegister.getMotherRace());
//                    temp.setMotherAddress(birthRegister.getMotherAddress());
//                    temp.setMotherPhoneNo(birthRegister.getMotherPhoneNo());
//                    temp.setMotherEmail(birthRegister.getMotherEmail());
//                    session.put("birthRegister", temp);
                return "form3";
            case 3:

//                birthRegister = (BirthRegister) session.get("birthRegister");
//                birthRegister.setDateOfMarriage(new EPopDate().getDate(dateOfMarriage));
//                session.put("birthRegister", birthRegister);
                return "form4";
        }
        return "error";
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public String getPassword() {

        return password;
    }

    public String getUserName() {

        return userName;
    }
}

