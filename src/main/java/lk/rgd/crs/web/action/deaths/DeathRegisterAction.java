package lk.rgd.crs.web.action.deaths;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Locale;

import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.web.WebConstants;

/**
 * @author Duminda Dharmakeerthi
 */
public class DeathRegisterAction extends ActionSupport implements SessionAware {
    private static final Logger logger = LoggerFactory.getLogger(DeathRegisterAction.class);
    private Map session;
    private int pageNo;
    private User user;


    private int deathDistrictId;
    private int deathDivisionId;
    private int dsDivisionId;


    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;

    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;

    public DeathRegisterAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
    }

    public String welcome() {
        return SUCCESS;
    }

    public String deathDeclaration() {
        populate();
        switch (pageNo) {
            case 0:
                return "form0";
            case 1:
                return "form1";
            case 2:
                return "form2";
            case 3:
                return SUCCESS;
        }
        return ERROR;
    }


    public String deathCertificate() {
        return SUCCESS;
    }

    public String lateDeath() {
        return SUCCESS;
    }


    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        populateBasicLists(language);
        populateDynamicLists(language);
    }

    private void populateBasicLists(String language) {
         User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        districtList = districtDAO.getAllDistrictNames(language, user);
    }

    private void populateDynamicLists(String language) {
         User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        if (getDeathDistrictId() == 0) {
            if (!districtList.isEmpty()) {
                setDeathDistrictId(districtList.keySet().iterator().next());
                logger.debug("first allowed district in the list {} was set", getDeathDistrictId());
            }
        }
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(getDeathDistrictId(), language, user);

        if (getDsDivisionId() == 0) {
            if (!dsDivisionList.isEmpty()) {
                setDsDivisionId(dsDivisionList.keySet().iterator().next());
                logger.debug("first allowed DS Div in the list {} was set", getDsDivisionId());
            }
        }

        bdDivisionList = bdDivisionDAO.getBDDivisionNames(getDsDivisionId(), language, user);
        if (getDeathDivisionId() == 0) {
            setDeathDivisionId(bdDivisionList.keySet().iterator().next());
            logger.debug("first allowed BD Div in the list {} was set", getDeathDivisionId());
        }
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageNo() {
        return pageNo;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map map) {
        this.session = map;
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

    public Map<Integer, String> getBdDivisionList() {
        return bdDivisionList;
    }

    public void setBdDivisionList(Map<Integer, String> bdDivisionList) {
        this.bdDivisionList = bdDivisionList;
    }

    public DistrictDAO getDistrictDAO() {
        return districtDAO;
    }

    public BDDivisionDAO getBdDivisionDAO() {
        return bdDivisionDAO;
    }

    public DSDivisionDAO getDsDivisionDAO() {
        return dsDivisionDAO;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getDeathDistrictId() {
        return deathDistrictId;
    }

    public void setDeathDistrictId(int deathDistrictId) {
        this.deathDistrictId = deathDistrictId;
    }

    public int getDeathDivisionId() {
        return deathDivisionId;
    }

    public void setDeathDivisionId(int deathDivisionId) {
        this.deathDivisionId = deathDivisionId;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }
}
