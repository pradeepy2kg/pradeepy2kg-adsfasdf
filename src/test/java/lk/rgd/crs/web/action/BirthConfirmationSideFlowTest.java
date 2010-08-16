package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.action.births.BirthRegisterAction;
import lk.rgd.crs.web.action.births.BirthRegisterApprovalAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.BirthDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.Map;
import java.util.HashMap;


/**
 * @author Indunil Moremada
 *         unit test for testing the birth confirmation side flows
 */
public class BirthConfirmationSideFlowTest extends CustomStrutsTestCase {

    private static final Logger logger = LoggerFactory.getLogger(BirthConfirmationSideFlowTest.class);
    private ActionProxy proxy;
    private BirthRegisterAction action;
    private LoginAction loginAction;
    private BirthRegisterApprovalAction approvalAction;
    private BirthDeclaration bd;

    private String initAndExecute(String mapping, Map session) throws Exception {
        proxy = getActionProxy(mapping);
        action = (BirthRegisterAction) proxy.getAction();
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        String result = null;
        try {
            result = proxy.execute();
        } catch (NullPointerException e) {
            logger.error("non fatal proxy execution error", e.getMessage(), e);
        }
        logger.debug("result for mapping {} is {}", mapping, result);
        return result;
    }

    private String initAndExecuteApproval(String mapping, Map session) throws Exception {
        proxy = getActionProxy(mapping);
        approvalAction = (BirthRegisterApprovalAction) proxy.getAction();
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        String result = null;
        try {
            result = proxy.execute();
        } catch (NullPointerException e) {
            logger.error("non fatal proxy execution error", e.getMessage(), e);
        }
        logger.debug("result for mapping {} is {}", mapping, result);
        return result;
    }

    @Override
    protected String getContextLocations() {
        return "unitTest_applicationContext.xml";
    }


    private Map login(String userName, String password) throws Exception {
        request.setParameter("userName", userName);
        request.setParameter("password", password);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }

    public void testSkipConfirmationChangesForConfirmationChangesCapturedEntry() throws Exception {
        Object obj;
        Map session = login("rg", "password");
        //initiating action to get the required bdId to start the unit test
        initAndExecute("/births/eprBirthConfirmationInit.do", session);
        //getting the required bdId which is having confirmation changes
        BirthDeclaration bdTemp = action.getService().getActiveRecordByBDDivisionAndSerialNo(action.getBDDivisionDAO().getBDDivisionByPK(1),
            new Long("07000804"), (User) session.get(WebConstants.SESSION_USER_BEAN));
        //searching the required entry
        Long bdId = bdTemp.getIdUKey();
        logger.debug("Got bdId {}", bdId);
        request.setParameter("bdId", bdId.toString());
        initAndExecute("/births/eprBirthConfirmationInit.do", session);
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);

        assertEquals("Action erros Confirmation Search", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
        assertNotNull("failed to populate Confirmation session bean", bd);
        assertNotNull("failed to populate Confirmation Database bean",
            session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_DB_BEAN));

        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", obj);
        assertNotNull("Request District List Presence", action.getDistrictList());
        assertNotNull("Request Race List Presence", action.getRaceList());

        //skipping changes
        request.setParameter("bdId", bdId.toString());
        request.setParameter("pageNo", "2");
        request.setParameter("skipConfirmationChages", "true");
        initAndExecute("/births/eprBirthConfirmationSkipChanges.do", session);
        session = action.getSession();
        assertEquals("Action erros Confirmation skiping changes", 0, action.getActionErrors().size());

        assertNotNull("faild to initialize confirmant bean", action.getConfirmant());

        request.setParameter("pageNo", "3");
        request.setParameter("skipConfirmationChages", "true");
        request.setParameter("confirmant.confirmantSignDate", "2010-07-20T00:00:00+05:30");
        request.setParameter("confirmant.confirmantNICorPIN", "861481131V");
        request.setParameter("confirmant.confirmantFullName", "Ramya de silva");
        initAndExecute("/births/eprBirthConfirmation.do", session);
        session = action.getSession();
        assertEquals("Action erros Confirmation skiping changes", 0, action.getActionErrors().size());
        assertFalse("faild to set skipConfirmationChages in confirmation changes captured state", action.isSkipConfirmationChages());

        //direct approval confirmation changes
        request.setParameter("bdId", Long.toString(action.getBdId()));
        request.setParameter("confirmationApprovalFlag", "true");
        initAndExecuteApproval("/births/eprConfrimationChangesDirectApproval.do", session);
        session = approvalAction.getSession();

        logger.debug("current state after direct approval of confrimation chages : {}", (approvalAction.getService().getById(bdId,
            (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());

        //direct birth certificate print after direct approval
        request.setParameter("bdId", Long.toString(approvalAction.getBdId()));
        request.setParameter("directPrintBirthCertificate", "true");
        initAndExecute("/births/eprBirthCertificatDirectPrint.do", session);
        session = action.getSession();
        logger.debug("current state after direct printing the BC : {}", (action.getService().getById(bdId,
            (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());
    }

    public void testCaptureConfirmationChanges() throws Exception {
        Long bdId;
        Map session = login("rg", "password");
        //initiating action to get the required bdId to start the unit test
        initAndExecute("/births/eprBirthConfirmationInit.do", session);
        //getting the required bdId which is having confirmation changes
        BirthDeclaration bdTemp = action.getService().getActiveRecordByBDDivisionAndSerialNo(action.getBDDivisionDAO().getBDDivisionByPK(1),
            new Long("07000805"), (User) session.get(WebConstants.SESSION_USER_BEAN));
        logger.debug("found bdId : {} and current state : {}", bdTemp.getIdUKey(), bdTemp.getRegister().getStatus());
        session = action.getSession();
        bdId = bdTemp.getIdUKey();

        //searches the confirmation which was sent by parent by its idUKey
        request.setParameter("bdId", bdId.toString());
        initAndExecute("/births/eprBirthConfirmationInit.do", session);

        assertEquals("Action erros Confirmation Search", 0, action.getActionErrors().size());
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
        assertEquals("Action erros Confirmation Search", 0, action.getActionErrors().size());
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
        assertNotNull("failed to populate Confirmation session bean", bd);
        assertNotNull("failed to populate Confirmation Database bean",
            session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_DB_BEAN));
        //loading the 2 of 3BCFs
        request.setParameter("pageNo", "1");
        request.setParameter("register.bdfSerialNo", "07000805");
        request.setParameter("register.dateOfRegistration", "2010-07-08T00:00:00+05:30");
        request.setParameter("child.dateOfBirth", "2010-07-01T00:00:00+05:30");
        request.setParameter("birthDistrictId", "1");
        request.setParameter("birthDivisionId", "10");
        request.setParameter("birthDivisionId", "1");
        request.setParameter("child.placeOfBirth", "මාතර");
        request.setParameter("child.placeOfBirthEnglish", "Matara");
        request.setParameter("child.placeOfBirth", "කොළඹ කොටුව");
        request.setParameter("child.placeOfBirthEnglish", "colombo port");
        request.setParameter("fatherRace", "3");
        request.setParameter("motherRace", "1");
        request.setParameter("marriage.parentsMarried", "3");
        request.setParameter("parent.fatherNICorPIN", "853303399v");
        request.setParameter("parent.motherNICorPIN", "666666666v");
        request.setParameter("child.childGender", "1");
        initAndExecute("/births/eprBirthConfirmation.do", session);

        assertEquals("Action erros for loading second page", 0, action.getActionErrors().size());
        session = action.getSession();

        assertNotNull("faild to initialize child bean", action.getChild());
        assertNotNull("faild to initialize parent bean", action.getParent());

        //loading the 3 of 3BCFs
        request.setParameter("pageNo", "2");
        request.setParameter("child.childFullNameOfficialLang", "නිශ්ශංක මුදියන්සේලාගේ ජනිත් විදර්ශන නිශ්ශංක");
        request.setParameter("child.childFullNameEnglish", "Nishshanka Mudiyanselage Janith Wiarshana Nishshanka");
        request.setParameter("parent.fatherFullName", "Nishshanka Mudiyanselage Chandrasena Nishshanka");
        request.setParameter("parent.motherFullName", "Periyapperuma Arachchilage Premawathi");
        initAndExecute("/births/eprBirthConfirmation.do", session);

        assertEquals("Action erros for loading third page", 0, action.getActionErrors().size());
        assertNotNull("faild to initialize confirmant bean", action.getConfirmant());
        session = action.getSession();

        //cheking for back support
        request.setParameter("pageNo", "1");
        initAndExecute("/births/eprBirthConfirmation.do", session);
        assertEquals("Action erros for loading second page with back support", 0, action.getActionErrors().size());

        assertEquals("failed to populate child full name in official language", "නිශ්ශංක මුදියන්සේලාගේ ජනිත් විදර්ශන නිශ්ශංක", action.getChild().getChildFullNameOfficialLang());
        assertEquals("failed to populate childFullNameEnglish", "Nishshanka Mudiyanselage Janith Wiarshana Nishshanka".toUpperCase(), action.getChild().getChildFullNameEnglish());
        assertEquals("failed to populate Father full name", "Nishshanka Mudiyanselage Chandrasena Nishshanka".toUpperCase(), action.getParent().getFatherFullName());
        assertEquals("failed to populate Mother full name", "Periyapperuma Arachchilage Premawathi".toUpperCase(), action.getParent().getMotherFullName());

        session = action.getSession();
        //loading the 3 of 3BCFs
        request.setParameter("pageNo", "2");
        request.setParameter("child.childFullNameOfficialLang", "නිශ්ශංක මුදියන්සේලාගේ ජනිත් විදර්ශන නිශ්ශංක");
        request.setParameter("child.childFullNameEnglish", "Nishshanka Mudiyanselage Janith Wiarshana Nishshanka");
        request.setParameter("parent.fatherFullName", "Nishshanka Mudiyanselage Chandrasena Nishshanka");
        request.setParameter("parent.motherFullName", "Periyapperuma Arachchilage Premawathi");
        initAndExecute("/births/eprBirthConfirmation.do", session);

        assertEquals("Action erros for loading third page", 0, action.getActionErrors().size());
        assertNotNull("faild to initialize confirmant bean after back support", action.getConfirmant());
        session = action.getSession();

        //loading the confirmation form detail page
        request.setParameter("pageNo", "3");
        request.setParameter("confirmantRadio", "GUARDIAN");
        request.setParameter("confirmant.confirmantNICorPIN", "853303399v");
        request.setParameter("confirmant.confirmantFullName", "කැලුම් කොඩිප්පිලි");
        request.setParameter("confirmant.confirmantSignDate", "2010-07-28T00:00:00+05:30");
        request.setParameter("skipConfirmationChanges", "false");
        initAndExecute("/births/eprBirthConfirmation.do", session);
        session = action.getSession();
        bdId = action.getBdId();
        logger.debug("bdId for the new entry {}", bdId);
        logger.debug("current state after capturing confirmation changes : {}", (action.getService().getById(bdId,
            (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());

        //dirct approval of confirmation changes
        request.setParameter("bdId", Long.toString(bdId));
        request.setParameter("confirmationApprovalFlag", "true");
        initAndExecuteApproval("/births/eprConfrimationChangesDirectApproval.do", session);
        session = approvalAction.getSession();

        logger.debug("current state after direct approval of confrimation chages : {}", (approvalAction.getService().getById(bdId,
            (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());

        //direct birth certificate print after direct approval
        request.setParameter("bdId", Long.toString(approvalAction.getBdId()));
        request.setParameter("directPrintBirthCertificate", "true");
        initAndExecute("/births/eprBirthCertificatDirectPrint.do", session);
        session = action.getSession();
        logger.debug("current state after direct printing the BC : {}", (action.getService().getById(bdId,
            (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());
    }

    public void testSkipConfirmationChangesForConfirmationPrintedEntry() throws Exception {
        Object obj;
        Map session = login("rg", "password");
        //initiating action to get the required bdId to start the unit test
        initAndExecute("/births/eprBirthConfirmationInit.do", session);
        //getting the required bdId wich is not having confirmation changes
        BirthDeclaration bdTemp = action.getService().getActiveRecordByBDDivisionAndSerialNo(action.getBDDivisionDAO().getBDDivisionByPK(1),
            new Long("07000810"), (User) session.get(WebConstants.SESSION_USER_BEAN));
        //searching the required entry for which confirmation changes to be skipped
        Long bdId = bdTemp.getIdUKey();
        logger.debug("Got confirmation printed Entry with bdId : {}", bdId);
        request.setParameter("bdId", bdId.toString());
        initAndExecute("/births/eprBirthConfirmationInit.do", session);
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);

        assertEquals("Action erros Confirmation Search", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
        assertNotNull("failed to populate Confirmation session bean", bd);
        assertNotNull("failed to populate Confirmation Database bean",
            session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_DB_BEAN));

        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", obj);
        assertNotNull("Request District List Presence", action.getDistrictList());
        assertNotNull("Request Race List Presence", action.getRaceList());

        //skipping changes
        request.setParameter("bdId", bdId.toString());
        request.setParameter("pageNo", "2");
        request.setParameter("skipConfirmationChages", "true");
        initAndExecute("/births/eprBirthConfirmationSkipChanges.do", session);
        session = action.getSession();
        assertEquals("Action erros Confirmation skiping changes", 0, action.getActionErrors().size());

        assertNotNull("faild to initialize confirmant bean", action.getConfirmant());

        request.setParameter("pageNo", "3");
        request.setParameter("skipConfirmationChages", "true");
        request.setParameter("confirmant.confirmantSignDate", "2010-07-22T00:00:00+05:30");
        request.setParameter("confirmant.confirmantNICorPIN", "861481137V");
        request.setParameter("confirmant.confirmantFullName", "samara jayawardane");
        initAndExecute("/births/eprBirthConfirmation.do", session);
        session = action.getSession();
        assertEquals("Action erros Confirmation skiping changes", 0, action.getActionErrors().size());
        logger.debug("SkipConfirmationChanges : {} ", action.isSkipConfirmationChages());

        logger.debug("current state after skipping confirmation changes : {}", (action.getService().getById(bdId,
            (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());

        //direct birth certificate print after skipping confirmation changes
        request.setParameter("bdId", Long.toString(action.getBdId()));
        request.setParameter("directPrintBirthCertificate", "true");
        initAndExecute("/births/eprBirthCertificatDirectPrint.do", session);
        session = action.getSession();
        logger.debug("current state after direct printing the BC : {}", (action.getService().getById(bdId,
            (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());
    }
}
