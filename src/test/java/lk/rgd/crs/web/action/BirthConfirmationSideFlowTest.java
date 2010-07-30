package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.action.births.BirthRegisterAction;
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

    public void testSkipConfirmationChanges() throws Exception {
        Map session = login("rg", "password");
        //initiating action
        initAndExecute("/births/eprBirthConfirmationInit.do", session);
        //getting the required bdId which is having confirmation changes
        BirthDeclaration bdTemp = action.getService().getByBDDivisionAndSerialNo(action.getBDDivisionDAO().getBDDivisionByPK(1), new Long("07000804"), (User) session.get(WebConstants.SESSION_USER_BEAN));
        Object obj;
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
        request.setParameter("confirmant.confirmantNICorPIN", "685031035V");
        request.setParameter("confirmant.confirmantFullName", "සංගුණි ෙද්ව ෙග්");
        initAndExecute("/births/eprBirthConfirmation.do", session);
        session = action.getSession();
        assertEquals("Action erros Confirmation skiping changes", 0, action.getActionErrors().size());
        assertFalse("faild to set skipConfirmationChages in confirmation changes captured state", action.isSkipConfirmationChages());
    }
}
