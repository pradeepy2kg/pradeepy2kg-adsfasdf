package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.context.ApplicationContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Action;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.InformantInfo;
import lk.rgd.crs.api.domain.NotifyingAuthorityInfo;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.web.action.births.BirthRegisterAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.UnitTestManager;

/**
 * @author Chathuranga Withana
 */
public class BirthRegisterActionSideFlowTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterActionSideFlowTest.class);
    private final ApplicationContext ctx = UnitTestManager.ctx;
    private BirthDeclarationDAO birthDeclarationDAO;
    private BDDivisionDAO bdDivisionDAO;

    private ActionProxy proxy;
    private BirthRegisterAction action;
    private LoginAction loginAction;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        birthDeclarationDAO = (BirthDeclarationDAO) ctx.getBean("birthDeclarationDAOImpl", BirthDeclarationDAO.class);
        bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
    }

    private String initAndExecute(String mapping, Map session) throws Exception {
        proxy = getActionProxy(mapping);
        action = (BirthRegisterAction) proxy.getAction();
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        String result = null;
        try {
            result = proxy.execute();
        } catch (Exception e) {
            //logger.error("proxy execution error", e);     TODO
        }
        logger.debug("Result for mapping {} is {}", mapping, result);
        return result;
    }

    @Override
    protected String getContextLocations() {
        return "unitTest_applicationContext.xml";
    }

    public void testBirthRegisterActionMappingProxy() {
        ActionMapping mapping = getActionMapping("/births/eprBirthRegistration.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/births", mapping.getNamespace());
        assertEquals("eprBirthRegistration", mapping.getName());
        ActionProxy proxy = getActionProxy("/births/eprBirthRegistration.do");
        assertNotNull(proxy);
        logger.debug("NameSpace {} and ActionName {}", mapping.getNamespace(), proxy.getMethod());

        BirthRegisterAction action = (BirthRegisterAction) proxy.getAction();
        assertNotNull(action);
    }

    /**
     * Test BirthDeclaration adding in batch mode
     */
    public void testAddNewBDFInBatchMode() throws Exception {
        Map session = login("chathuranga", "chathuranga");

        long serialNum = 19000;
        BDDivision colomboBdDivision = bdDivisionDAO.getBDDivisionByPK(1);

        // get alredy entered BDF entry from DB, BDF with IDUKey 166 and this part used to skip first few steps of Bith
        // Declaration
        BirthDeclaration bdf = getBDFById(166);
        assertNotNull("BirthDeclaration failed to be fetched from the DB", bdf);
        bdf.setIdUKey(0);
        bdf.getRegister().setBdfSerialNo(serialNum);
        bdf.getRegister().setBirthDivision(colomboBdDivision);
        assertEquals("session size incorrect", 3, session.size());
        assertNull("Birth Declaration Bean can not exist in the session", session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
        session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);
        assertEquals("session size incorrect", 4, session.size());
        assertNotNull("Birth Declaration Bean does not exist in the session", session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));

        // adding a Birth Declaration in normal mode
        request.setParameter("pageNo", "4");
        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", "685031035V");
        request.setParameter("notifyingAuthority.notifyingAuthorityName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", "2010-07-02T00:00:00+05:30");

        initAndExecute("/births/eprBirthRegistration.do", session);

        assertNotNull("BDF NotifyingAuthorityInfo object is null", action.getNotifyingAuthority());
        logger.debug("Notifying Authoruty PIN : {}", action.getNotifyingAuthority().getNotifyingAuthorityPIN());
        logger.debug("Notifying Authoruty Name : {}", action.getNotifyingAuthority().getNotifyingAuthorityName());
        logger.debug("Notifying Authoruty Address : {}", action.getNotifyingAuthority().getNotifyingAuthorityAddress());
        logger.debug("Notifying Authoruty Sign Date : {}", action.getNotifyingAuthority().getNotifyingAuthoritySignDate());

        assertNotNull("Birth Declaration registration faild to persist", action.getBdId());
        long persistedBDId = action.getBdId();
        logger.debug("BDF persisted but not in batch mode with IDUKey : {}", persistedBDId);
        assertEquals("Action errors found", 0, action.getActionErrors().size());
        assertEquals("Action messages not match", 1, action.getActionMessages().size());
        assertEquals("Invalid session size", 4, session.size());
        assertNull("Birth Declaration Bean can not exist in the session", session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));

        // adding new entry in batch mode
        logger.debug("Testing Add New BDF in batch mode");
        assertEquals("AddNewMode should be false", false, action.isAddNewMode());
        assertEquals("oldBdId should be 0", 0, action.getOldBdId());
        request.setParameter("oldBdId", Long.toString(persistedBDId));
        request.setParameter("addNewMode", "true");
        initAndExecute("/births/eprBirthRegistrationInit.do", session);

        assertEquals("Invalid session size", 5, session.size());
        BirthDeclaration batchBdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertNotNull("Birth Declaration does not exist in the session", batchBdf);

        assertNotNull(batchBdf.getChild());
        assertNotNull(batchBdf.getRegister());
        assertNotNull(batchBdf.getParent());
        assertNotNull(batchBdf.getGrandFather());
        assertNotNull(batchBdf.getMarriage());
        assertNotNull(batchBdf.getInformant());
        assertNotNull(batchBdf.getNotifyingAuthority());

        // check auto populated fields correctly in batch mode
        assertEquals("BDF serial number not inceremented for the BDF in seesion", serialNum + 1,
            batchBdf.getRegister().getBdfSerialNo());
        assertEquals("BDF date Of registration in session and DB are not equal", bdf.getRegister().getDateOfRegistration(),
            batchBdf.getRegister().getDateOfRegistration());
        assertEquals("BDF live birth type not matched", bdf.getRegister().isLiveBirth(), batchBdf.getRegister().isLiveBirth());
        assertEquals("BDF District id miss match in action and session", action.getBirthDistrictId(),
            batchBdf.getRegister().getBirthDistrict().getDistrictUKey());
        assertEquals("BDF DS Division id miss match in action and session", action.getDsDivisionId(),
            batchBdf.getRegister().getDsDivision().getDsDivisionUKey());
        assertEquals("BDF Birth division id miss match in action and session",
            action.getBirthDivisionId(), batchBdf.getRegister().getBirthDivision().getBdDivisionUKey());
        assertEquals("BDF Birth division id miss match in previously added BDF and BDF in session",
            bdf.getRegister().getBirthDivision().getBdDivisionUKey(), batchBdf.getRegister().getBirthDivision().getBdDivisionUKey());
        assertEquals("BDF NotifyingAutho PIN miss match in previously added BDF and BDF in session",
            bdf.getNotifyingAuthority().getNotifyingAuthorityPIN(), batchBdf.getNotifyingAuthority().getNotifyingAuthorityPIN());
        assertEquals("BDF NotifyingAutho Name miss match in previously added BDF and BDF in session",
            bdf.getNotifyingAuthority().getNotifyingAuthorityName(), batchBdf.getNotifyingAuthority().getNotifyingAuthorityName());
        assertEquals("BDF NotifyingAutho Address miss match in previously added BDF and BDF in session",
            bdf.getNotifyingAuthority().getNotifyingAuthorityAddress(), batchBdf.getNotifyingAuthority().getNotifyingAuthorityAddress());
        assertEquals("BDF NotifyingAutho Sign Date miss match in previously added BDF and BDF in session",
            bdf.getNotifyingAuthority().getNotifyingAuthoritySignDate(), batchBdf.getNotifyingAuthority().getNotifyingAuthoritySignDate());
        logger.debug("Adding BDF auto populating fields populated correctly and Add new in batch mode passed");
        deleteBDF(colomboBdDivision, serialNum);
    }


    public void testBackButtonInBD() throws Exception {
        Map session = login("chathuranga", "chathuranga");

        long serialNum = 19000;
        BDDivision colomboBdDivision = bdDivisionDAO.getBDDivisionByPK(1);

        // get alredy entered BDF entry from DB, BDF with IDUKey 166 and this part used to skip first few steps of Bith
        // Declaration
        BirthDeclaration bdf = getBDFById(166);
        assertNotNull("BirthDeclaration failed to be fetched from the DB", bdf);
        bdf.setIdUKey(0);
        bdf.getRegister().setBdfSerialNo(serialNum);
        bdf.getRegister().setBirthDivision(colomboBdDivision);
        assertEquals("session size incorrect", 3, session.size());
        assertNull("Birth Declaration Bean can not exist in the session", session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
        session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);
        assertEquals("session size incorrect", 4, session.size());
        assertNotNull("Birth Declaration Bean does not exist in the session", session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
    }

    /**
     * Used to test login to the system
     *
     * @param userName the user Id
     * @param password the password
     * @return user session
     * @throws Exception
     */
    private Map login(String userName, String password) throws Exception {
        request.setParameter("userName", userName);
        request.setParameter("password", password);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }

    /**
     * Return BirthDeclaration for a given IdUkey
     *
     * @param serial Birth Declarion Id for the given declaration
     * @return the BDF if found, and the user has access to the record
     */
    private BirthDeclaration getBDFById(long serial) {
        return birthDeclarationDAO.getById(serial);
    }

    /**
     * Delete Birth Declaration by BDDivision and serial number
     *
     * @param bdDivision
     * @param serial
     */
    private void deleteBDF(BDDivision bdDivision, long serial) {
        try {
            birthDeclarationDAO.deleteBirthDeclaration(
                birthDeclarationDAO.getByBDDivisionAndSerialNo(bdDivision, serial).getIdUKey());
        } catch (Exception e) {
        }
    }
}