package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.AppConstants;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.bean.BirthDivisionSerialNumberStatistics;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.dao.GNDivisionDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.GNDivision;
import lk.rgd.crs.web.WebConstants;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Sisiruwan Senadeera
 */
public class BirthRegistrationReportsAction extends ActionSupport implements SessionAware{

    private static final Logger logger = LoggerFactory.getLogger(AdoptionAction.class);

    private User user;
    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> allDSDivisionList;
    private List<BirthDivisionSerialNumberStatistics> searchResults;

    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final BirthDeclarationDAO birthDeclarationDAO;
    private String language;
    private String dsDivisionName;

    private int birthDistrictId;
    private int dsDivisionId;

    private Date dataEntryPeriodFrom;
    private Date dataEntryPeriodTo;



    public BirthRegistrationReportsAction(DistrictDAO districtDAO,DSDivisionDAO dsDivisionDAO,BDDivisionDAO bdDivisionDAO,BirthDeclarationDAO birthDeclarationDAO){
         this.districtDAO=districtDAO;
         this.dsDivisionDAO=dsDivisionDAO;
         this.bdDivisionDAO=bdDivisionDAO;
         this.birthDeclarationDAO=birthDeclarationDAO;
    }

    public void setSession(Map map) {
        logger.debug("Set session {}", map);
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("setting User: {}", user.getUserName());
    }

     private void populateBasicLists(String language) {
        districtList = districtDAO.getDistrictNames(language, user);
        int districtId=user.getPrefBDDistrict().getDistrictUKey();
        populateAllDSDivisionList(districtId,language);
     }

     private void populateAllDSDivisionList(int districtID, String language) {
        allDSDivisionList = dsDivisionDAO.getAllDSDivisionNames(districtID, language, user);
     }

    public String loadSerialNumberSearch(){
        populateBasicLists(language);
        return SUCCESS;
    }

    public String generateSerialNumberReport(){
        if (dataEntryPeriodFrom != null && dataEntryPeriodTo != null && dataEntryPeriodTo.before(dataEntryPeriodFrom)) {
            logger.debug("Inside the date validation condition. ");
            addActionError(getText("date.comparison.validation.label"));
        }

        try{
            dsDivisionName= dsDivisionDAO.getNameByPK(dsDivisionId,language);
            logger.debug("DS Division Name $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$. {}",dsDivisionName);

            List<BDDivision> bdDivList= bdDivisionDAO.getAllBDDivisionByDsDivisionKey(getDsDivisionId());
            searchResults=new ArrayList<BirthDivisionSerialNumberStatistics>();

                for(BDDivision bd : bdDivList ){
                     List<BirthDeclaration> bdl=birthDeclarationDAO.getByBDDivisionStatusAndEnteredDateRange(bd,BirthDeclaration.State.DATA_ENTRY,dataEntryPeriodFrom,setMaxTimePartForDates(dataEntryPeriodTo) );

                     Long firstSerialNo= null;
                     Long lastSerialNo= null;
                     String serialNoRange=null;

                     if(bdl.size()>0){
                          firstSerialNo= bdl.get(0).getRegister().getBdfSerialNo();
                          lastSerialNo=bdl.get(bdl.size()-1).getRegister().getBdfSerialNo();
                          serialNoRange=firstSerialNo + " - " + lastSerialNo;
                     }
                     else{
                          serialNoRange="";
                     }

                     BirthDivisionSerialNumberStatistics bdsns = new BirthDivisionSerialNumberStatistics();

                     if(bd!=null){
                          bdsns.setBdDivisionUkey(bd.getBdDivisionUKey());

                          if(AppConstants.SINHALA.equals(language)){
                              bdsns.setBdDivisionName(bd.getSiDivisionName());
                          }
                          else if(AppConstants.ENGLISH.equals(language)){
                               bdsns.setBdDivisionName(bd.getEnDivisionName());
                          }
                          else if(AppConstants.ENGLISH.equals(language)){
                               bdsns.setBdDivisionName(bd.getTaDivisionName());
                          }

                          bdsns.setSerialNumberRange(serialNoRange);
                     }

                    searchResults.add(bdsns);
                }

            populateBasicLists(language);
        }
        catch(Exception ex){

        }

        return SUCCESS;

    }

    private Date setMaxTimePartForDates(Date date) {
        Date d = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            d = cal.getTime();
        } catch (Exception ex) {

        }
        return d;
    }

    public Map getSession() {
        return session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public Map<Integer, String> getAllDSDivisionList() {
        return allDSDivisionList;
    }

    public void setAllDSDivisionList(Map<Integer, String> allDSDivisionList) {
        this.allDSDivisionList = allDSDivisionList;
    }

    public int getBirthDistrictId() {
        return birthDistrictId;
    }

    public void setBirthDistrictId(int birthDistrictId) {
        this.birthDistrictId = birthDistrictId;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public Date getDataEntryPeriodFrom() {
        return dataEntryPeriodFrom;
    }

    public void setDataEntryPeriodFrom(Date dataEntryPeriodFrom) {
        this.dataEntryPeriodFrom = dataEntryPeriodFrom;
    }

    public Date getDataEntryPeriodTo() {
        return dataEntryPeriodTo;
    }

    public void setDataEntryPeriodTo(Date dataEntryPeriodTo) {
        this.dataEntryPeriodTo = dataEntryPeriodTo;
    }

    public List<BirthDivisionSerialNumberStatistics> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<BirthDivisionSerialNumberStatistics> searchResults) {
        this.searchResults = searchResults;
    }

    public String getDsDivisionName() {
        return dsDivisionName;
    }

    public void setDsDivisionName(String dsDivisionName) {
        this.dsDivisionName = dsDivisionName;
    }
}
