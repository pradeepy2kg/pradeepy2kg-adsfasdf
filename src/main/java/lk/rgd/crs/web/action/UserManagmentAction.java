package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Locale;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.RoleDAO;
import lk.rgd.crs.web.WebConstants;

/**
 * @author amith jayasekara
 * @author Duminda Dharmakeerthi 
 */
public class UserManagmentAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);
    private Map session;
    private static User user;
    private int pageNo = 0;
    private String divisions = new String();
    private int[] assignedDistricts;
    private int[] assignedDivisions;


    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final RoleDAO roleDAO;

    private Map<Integer, String> districtList;
    private Map<Integer, String> divisionList;
    private Map<String, String> roleList;

    public UserManagmentAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, RoleDAO roleDAO){
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.roleDAO = roleDAO;
    }

    public String creatUser() {
        logger.info("creat user called");
        if(divisions.equals(getText("get_ds_divisions.label"))){
            generateDSDivisions();
        }
        else if(pageNo == 1)  {
            // to do...
            return "success";
        }
        populate();
        return "pageLoad";
    }

    private void populate(){
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        if(user == null)
            user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        if(districtList == null)
            districtList = districtDAO.getDistrictNames(language, null);
        if(divisionList == null)
            divisionList = dsDivisionDAO.getDSDivisionNames(user.getPrefDistrict().getDistrictId(), language, null);
        if(roleList == null)
            roleList = roleDAO.getRoleList();
    }

    /**
     * To generate the divisionList according to the selected Districts.
     */

    private void generateDSDivisions(){
        for(int i=0; i< assignedDistricts.length; i++){
            if(i==0){
                setDivisionList(dsDivisionDAO.getDSDivisionNames(assignedDistricts[i], ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage(), null));
            }else{
                divisionList.putAll(dsDivisionDAO.getDSDivisionNames(assignedDistricts[i], ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage(), null));
            }
        }    
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageNo() {

        return pageNo;
    }

    public void setDivisions(String divisions) {
        this.divisions = divisions;
    }

    public String getDivisions() {

        return divisions;
    }
     
    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public Map<Integer, String> getDistrictList() {

        return districtList;
    }

    public void setDivisionList(Map<Integer, String> divisionList) {
        this.divisionList = divisionList;
    }

    public Map<Integer, String> getDivisionList() {
        return divisionList;
    }


    public void setAssignedDistricts(int[] assignedDistricts) {
        this.assignedDistricts = assignedDistricts;
    }

    public int[] getAssignedDistricts() {
        return assignedDistricts;
    }

    public void setAssignedDivisions(int[] assignedDivisions) {
        this.assignedDivisions = assignedDivisions;
    }

    public int[] getAssignedDivisions() {

        return assignedDivisions;
    }

    public void setRoleList(Map<String, String> roleList) {
        this.roleList = roleList;
    }

    public Map<String, String> getRoleList() {

        return roleList;
    }

    public void setSession(Map map) {
        this.session = map;
    }
}
