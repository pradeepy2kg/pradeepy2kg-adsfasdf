package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.crs.web.action.births.BirthRegisterAction;
import lk.rgd.crs.web.action.births.BirthRegisterApprovalAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.InformantInfo;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.dao.*;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;

/**
 * @author Janith Widarshana. 
 */
public class BirthConfirmationTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(BirthConfirmationTest.class);
    private BirthRegisterAction registerAction;
    BirthRegisterApprovalAction approvalAction;
    private ActionProxy proxy;

    private Map UserLogin(String username, String passwd) throws Exception {
        request.setParameter("userName", username);
        request.setParameter("password", passwd);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        LoginAction loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }

    private void initAndExucute(String mapping,Map session) {
        proxy = getActionProxy(mapping);
        registerAction = (BirthRegisterAction) proxy.getAction();
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        try {
            proxy.execute();
        } catch (Exception e) {
            logger.error("Handle Error {} : {}", e.getMessage(), e);
        }
    }

    @Override
    public String getContextLocations() {
        return "unitTest_applicationContext.xml";
    }

    public void testBirthConfirmationInitMappingProxy() throws Exception {
        Map session =UserLogin("ashoka", "ashoka");
        ActionMapping mapping = getActionMapping("/births/eprBirthConfirmationInit.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/births", mapping.getNamespace());
        assertEquals("eprBirthConfirmationInit", mapping.getName());

        request.setParameter("bdId", "165");
        initAndExucute("/births/eprBirthConfirmationInit.do",session);
        assertEquals("No Action erros.", 0, registerAction.getActionErrors().size());
        session = registerAction.getSession();

        BirthDeclaration bdf, bcf;
        bdf = (BirthDeclaration) (session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN));
        bcf = (BirthDeclaration) (session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_DB_BEAN));
        //request.setAttribute("",bdf);
        assertNotNull("Session bdf presence", bdf);
        assertNotNull("Session bcf presence", bcf);
        assertEquals("confirmation changes has been captured", 5, bdf.getRegister().getStatus().ordinal());

        assertNotNull("dsDivisionList {} ", registerAction.getDsDivisionList());
        assertNotNull("dsDivisionList {} ", registerAction.getBdDivisionList());

        assertEquals("Found District", 11, bdf.getRegister().getBirthDistrict().getDistrictId());
        assertEquals("Found Division ", 1, bdf.getRegister().getBirthDivision().getDivisionId());
        logger.debug("getBd division {} ", bdf.getRegister().getBirthDivision().getSiDivisionName());

        assertEquals("Fathercountry", 1, bdf.getParent().getFatherCountry().getCountryId());

        assertNotNull("Mother country presence", registerAction.getMotherCountry());
        assertNotNull("Father Race precence", registerAction.getFatherRace());
        assertNotNull("Mother Race precence", registerAction.getMotherRace());
        assertNotNull("Mother Dsdivision Id presence ", registerAction.getMotherDSDivisionId());
        assertNotNull("Mother District Id presence ", registerAction.getMotherDistrictId());

        assertEquals("Serial no ", 1500, bdf.getRegister().getBdfSerialNo());

        assertNotNull("Child date of birth ", bdf.getChild().getDateOfBirth());
        assertEquals("Child place of birth ", "රෝහලේදී", bdf.getChild().getPlaceOfBirth());
        assertEquals("Child place of birth ", "In Hospital", bdf.getChild().getPlaceOfBirthEnglish());
       
        session = registerAction.getSession();
        request.setParameter("pageNo", "1");
        request.setParameter("child.dateOfBirth","2010-07-21T00:00:00+05:30");
        request.setParameter("birthDistrictId", "11");
        request.setParameter("dsDivisionId", "3");
        request.setParameter("birthDivisionId", "1");
        request.setParameter("child.placeOfBirth", "කොළඹ කොටුව");
        request.setParameter("child.placeOfBirthEnglish", "colombo port");
        request.setParameter("fatherRace", "3");
        request.setParameter("marriage.parentsMarried", "3");
        request.setParameter("parent.fatherNICorPIN", "853303399v");
        request.setParameter("parent.motherNICorPIN", "666666666v");
        request.setParameter("child.childGender", "1");
        initAndExucute("/births/eprBirthConfirmation.do",session);
        session = registerAction.getSession();
        bdf = (BirthDeclaration) (session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN));
        assertEquals("No Action erros.", 0, registerAction.getActionErrors().size());

        assertNotNull("child date of birth",registerAction.getChild().getDateOfBirth());
        assertNotNull("child gender ",registerAction.getChild().getChildGender());
        logger.debug("child date of birth : {}",registerAction.getChild().getDateOfBirth()) ;

        assertNotNull("Session bdf presence", bdf);
        assertEquals("confirmation changes captured", 5, bdf.getRegister().getStatus().ordinal());

        request.setParameter("pageNo", "2");
        request.setParameter("child.childFullNameOfficialLang", "නිශ්ශංක මුදියන්සේලාගේ ජනිත් විදර්ශන නිශ්ශංක");
        request.setParameter("child.childFullNameEnglish", "Nishshanka Mudiyanselage Janith Wiarshana Nishshanka");
        request.setParameter("parent.fatherFullName", "Nishshanka Mudiyanselage Chandrasena Nishshanka");
        request.setParameter("parent.motherFullName", "Periyapperuma Arachchilage Premawathi");
        initAndExucute("/births/eprBirthConfirmation.do",session);
        session = registerAction.getSession();
        bdf = (BirthDeclaration) (session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN));

        request.setParameter("pageNo", "3");
        assertEquals("No Action erros.", 0, registerAction.getActionErrors().size());
        request.setParameter("confirmantRadio", "GUARDIAN");
        request.setParameter("confirmant.confirmantNICorPIN", "853303399v");
        request.setParameter("confirmant.confirmantFullName", "කැලුම් කොඩිප්පිලි");
        request.setParameter("confirmant.confirmantSignDate","2010-07-28T00:00:00+05:30");
        request.setParameter("skipConfirmationChanges", "false");
        initAndExucute("/births/eprBirthConfirmation.do",session);
        session = registerAction.getSession();
        assertNotNull("confirment full name ", registerAction.getConfirmant().getConfirmantFullName());

        request.setParameter("confirmationApprovalFlag", "true");
        request.setParameter("bdId", "165");
        request.setParameter("directApprovalFlag", "true");
        approvalinit("/births/eprConfrimationChangesDirectApproval.do",session);
        session = registerAction.getSession();

        assertNotNull("Ds division ", bdf.getRegister().getBirthDivision().getDsDivision().getDivisionId());
    }
    private void approvalinit(String mapping,Map session){
        proxy = getActionProxy(mapping);
        approvalAction = (BirthRegisterApprovalAction) proxy.getAction();
        ActionContext.getContext().setSession(session);
        try {
            proxy.execute();
        } catch (Exception e) {
            logger.error("Handle Error {} : {}", e.getMessage(), e);
        }
    }
}