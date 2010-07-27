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
 * Created by IntelliJ IDEA.
 * User: widu
 * Date: Jul 20, 2010
 * Time: 11:52:16 AM
 * To change this template use File | Settings | File Templates.
 * @author Janith Widarshana. 
 */
public class BirthConfirmationTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(BirthConfirmationTest.class);
    private BirthRegisterAction registerAction;
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
        Map session =UserLogin("rg", "password");
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
        assertEquals("Father NIC ", "855012132V", bdf.getParent().getFatherNICorPIN());
        assertEquals("Mother NIC ", "855012132V", bdf.getParent().getMotherNICorPIN());
        assertEquals("Father Race ", 1, bdf.getParent().getFatherRace().getRaceId());
        assertEquals("Mother Race ", 1, bdf.getParent().getMotherRace().getRaceId());
        assertEquals("Parent Married ", 1, bdf.getMarriage().getParentsMarried().intValue());
        assertEquals("Child full Name in English", "ANGAMMANA RANPANHINDA SAMARADIVAKARA WICKRAMASINGHE ILLANKONE SENANAYAKE RAJAPAKSE", bdf.getChild().getChildFullNameEnglish());
        assertEquals("Child full Name in OfficialLang", "අංගම්මන රන්පන්හිඳ සමරදිවාකර වික්‍රමසිංහ ඉලන්කෝන් සේනානායක රාජපක්ෂ", bdf.getChild().getChildFullNameOfficialLang());
        assertEquals("Father Full Name", "කුසුමාවතී රාම්‍යා ජයසිංහ", bdf.getParent().getFatherFullName());
        assertEquals("Mother Full Name", "කුසුමාවතී රාම්‍යා ජයසිංහ", bdf.getParent().getMotherFullName());

        request.setParameter("pageNo", "1");
        
        request.setParameter("child.dateOfBirth","2010-07-21T00:00:00+05:30");
        request.setParameter("birthDivisionId", "3");
        request.setParameter("child.placeOfBirth", "කොළඹ කොටුව");
        request.setParameter("child.placeOfBirthEnglish", "colombo port");
        request.setParameter("fatherRace", "3");
        request.setParameter("marriage.parentsMarried", "3");
        request.setParameter("parent.fatherNICorPIN", "853303399v");
        request.setParameter("parent.motherNICorPIN", "666666666v");
        request.setParameter("child.childGender", "1");
        initAndExucute("/births/eprBirthConfirmation.do",session);
        assertEquals("No Action erros.", 0, registerAction.getActionErrors().size());
        session = registerAction.getSession();

        assertNotNull("Session bdf presence", bdf);
        assertEquals("confirmation changes captured", 5, bdf.getRegister().getStatus().ordinal());

        request.setParameter("pageNo", "2");
        request.setParameter("child.childFullNameOfficialLang", "නිශ්ශංක මුදියන්සේලාගේ ජනිත් විදර්ශන නිශ්ශංක");
        request.setParameter("child.childFullNameEnglish", "Nishshanka Mudiyanselage Janith Wiarshana Nishshanka");
        request.setParameter("parent.fatherFullName", "Nishshanka Mudiyanselage Chandrasena Nishshanka");
        request.setParameter("parent.motherFullName", "Periyapperuma Arachchilage Premawathi");
        initAndExucute("/births/eprBirthConfirmation.do",session);

        request.setParameter("pageNo", "3");
        assertEquals("No Action erros.", 0, registerAction.getActionErrors().size());
        request.setParameter("confirmantRadio", "GUARDIAN");
        request.setParameter("confirmant.confirmantNICorPIN", "853303399v");
        request.setParameter("confirmant.confirmantFullName", "කැලුම් කොඩිප්පිලි");
        request.setParameter("confirmant.confirmantSignDate","2010-07-21T00:00:00+05:30");
        initAndExucute("/births/eprBirthConfirmation.do",session);

        session = registerAction.getSession();
        request.setParameter("confirmationApprovalFlag","true");
        request.setParameter("bdId","165");
        approvalinit("/births/eprConfrimationChangesDirectApproval.do",session);
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        try {
            proxy.execute();
        } catch (Exception e) {
            logger.error("Handle Error {} : {}", e.getMessage(), e);
        }

        logger.debug("Status of the tested data : {}",registerAction.getRegister().getStatus().ordinal());

        session = registerAction.getSession();
        request.setParameter("directPrintBirthCertificate","true");
        request.setParameter("bdId","165");
        initAndExucute("/births/eprBirthCertificatDirectPrint.do",session);
        assertEquals("No Action erros.", 0, registerAction.getActionErrors().size());
//        logger.debug("Status of the tested data : {}",registerAction.getRegister().getStatus().ordinal());
    }
    private void approvalinit(String mapping,Map session){
        proxy = getActionProxy(mapping);
        BirthRegisterApprovalAction approvalAction = (BirthRegisterApprovalAction) proxy.getAction();
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        try {
            proxy.execute();
        } catch (Exception e) {
            logger.error("Handle Error {} : {}", e.getMessage(), e);
        }
        assertEquals("No Action erros.", 0, approvalAction.getActionErrors().size());
    }
}
