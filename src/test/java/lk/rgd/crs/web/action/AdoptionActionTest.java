package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import lk.rgd.UnitTestManager;
import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.api.dao.ZonalOfficesDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.ZonalOffice;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.crs.api.dao.AdoptionOrderDAO;
import lk.rgd.crs.api.dao.CourtDAO;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.api.domain.Court;
import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.web.action.births.AdoptionAction;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.*;


public class AdoptionActionTest extends CustomStrutsTestCase {

    private AdoptionAction adoptionAction;
    private static final Logger logger = LoggerFactory.getLogger(AdoptionActionTest.class);
    private ActionProxy proxy;

    protected static Court AvissawellaCourt;

    protected final static ApplicationContext ctx = UnitTestManager.ctx;
    protected final static AdoptionOrderService adoptionOrderService = (AdoptionOrderService) ctx.getBean("adoptionOrderService", AdoptionOrderService.class);
    protected final static AdoptionOrderDAO adoptionOrderDAO = (AdoptionOrderDAO) ctx.getBean("adoptionOrderDAOImpl", AdoptionOrderDAO.class);
    protected final static UserManager userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
    protected final static CourtDAO courtDAO = (CourtDAO) ctx.getBean("courtDAOImpl", CourtDAO.class);
    protected final static ZonalOfficesDAO zonalOfficeDAO = (ZonalOfficesDAO) ctx.getBean("zonalOfficeDAOImpl", ZonalOfficesDAO.class);


    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(AdoptionActionTest.class)) {
            protected void setUp() throws Exception {
                logger.info("setup called");
                AvissawellaCourt = courtDAO.getCourt(44);
                List adop = persistAdoption();
                User sampleUser = loginSampleUser();
                for (int i = 0; i < adop.size(); i++) {
                    adoptionOrderService.addAdoptionOrder((AdoptionOrder) adop.get(i), sampleUser);
                }

                super.setUp();
            }

            protected void tearDown() throws Exception {
                logger.info("tear down called ");
                super.tearDown();
            }
        };
        return setup;
    }

    public void testActionMappingProxy() {
        ActionMapping mapping = getActionMapping("/adoption/eprAdoptionRegistrationAction.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/adoption", mapping.getNamespace());
        assertEquals("eprAdoptionRegistrationAction", mapping.getName());
        ActionProxy proxy = getActionProxy("/adoption/eprAdoptionRegistrationAction.do");
        assertNotNull(proxy);
        logger.debug("nameSpace {} and actionName {}", mapping.getNamespace(), proxy.getMethod());

        AdoptionAction action = (AdoptionAction) proxy.getAction();
        assertNotNull(action);
    }

    public void testAddAdoptionDeclaration() throws Exception {

        Map session = UserLogin("ashoka", "ashoka");

        initAndExecute("/adoption/eprAdoptionRegistrationAction.do", session);
        session = adoptionAction.getSession();

/*        assertNotNull("Dsdivision list", adoptionAction.getDsDivisionList());
        assertNotNull("District list", adoptionAction.getDistrictList());*/

        assertEquals("Action errors for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());
        request.setParameter("adoption.applicantAddress", "Thalawathugoda Rd,Kotte.");
        request.setParameter("adoption.applicantCountryId", "3");
        request.setParameter("adoption.applicantMother", "false");
        request.setParameter("adoption.applicantName", "Rathnasiri Banda");
        request.setParameter("adoption.applicantPINorNIC", "654425588V");
        request.setParameter("adoption.applicantPassport", "456456456");
        request.setParameter("adoption.birthCertificateSerial", "000002");
        request.setParameter("adoption.childAgeMonths", "2");
        request.setParameter("adoption.childAgeYears", "1");
        request.setParameter("adoption.childBirthDate", "2009-08-03T00:00:00+05:30");
        request.setParameter("adoption.childExistingName", "Priyankara Perera");
        request.setParameter("adoption.childGender", "1");
        request.setParameter("adoption.childNewName", "Rasika Bandara");
        request.setParameter("courtId", "44");
        request.setParameter("adoption.courtOrderNumber", "20100803");
        request.setParameter("adoption.judgeName", "Ruwan Perera");
        request.setParameter("adoption.languageToTransliterate", "si");
        request.setParameter("adoption.orderIssuedDate", "2010-08-17T00:00:00+05:30");
        request.setParameter("adoption.orderReceivedDate", "2010-08-10T00:00:00+05:30");
        request.setParameter("adoption.wifeCountryId", "2");
        request.setParameter("adoption.wifeName", "Rathna kumari");
        request.setParameter("adoption.wifePINorNIC", "689789789V");
        request.setParameter("adoption.wifePassport", "456535355");
        request.setParameter("birthDistrictId", "1");
        request.setParameter("birthDivisionId", "1");
        request.setParameter("adoption.adoptionEntryNo", "1");
        request.setParameter("dojo.adoption.childBirthDate", "2009-08-03");
        request.setParameter("dojo.adoption.orderIssuedDate", "2010-08-17");
        request.setParameter("dojo.adoption.orderReceivedDate", "2010-08-10");
        request.setParameter("dsDivisionId", "1");
        request.setParameter("pageNo", "1");
        request.setParameter("idUKey", "0");
        initAndExecute("/adoption/eprAdoptionAction.do", session);
        session = adoptionAction.getSession();

        //assertNotNull("Court ", adoptionAction.getAdoption().getCourt());
        assertNotNull("Court order Number ", adoptionAction.getAdoption().getCourtOrderNumber());
        assertNotNull("Child Birthday ", adoptionAction.getAdoption().getChildBirthDate());
        assertNotNull("Child New name ", adoptionAction.getAdoption().getChildNewName());

        logger.debug("successfully persisted with the idUkey :{}", adoptionAction.getIdUKey());
    }

    public void testAdoptionEdit() throws Exception {
        Map session = UserLogin("ashoka", "ashoka");
        request.setParameter("idUKey", "1");
        initAndExecute("/adoption/eprAdoptionEditMode.do", session);
        session = adoptionAction.getSession();
        assertEquals("Action errors for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());
        assertNotNull("adoption object", adoptionAction.getAdoption());


        request.setParameter("adoption.applicantAddress", "Thalawathugoda Rd,Kotte.");
        request.setParameter("adoption.applicantCountryId", "3");
        request.setParameter("adoption.applicantMother", "false");
        request.setParameter("adoption.applicantName", "Rathnasiri Banda");
        request.setParameter("adoption.applicantPINorNIC", "654425588V");
        request.setParameter("adoption.applicantPassport", "456456456");
        request.setParameter("adoption.birthCertificateSerial", "000002");
        request.setParameter("adoption.childAgeMonths", "2");
        request.setParameter("adoption.childAgeYears", "1");
        request.setParameter("adoption.childBirthDate", "2009-08-03T00:00:00+05:30");
        request.setParameter("adoption.childExistingName", "Priyankara Perera");
        request.setParameter("adoption.childGender", "1");
        request.setParameter("adoption.childNewName", "Rasika Bandara");
        request.setParameter("courtId", "44");
        request.setParameter("adoption.courtOrderNumber", "20100803");
        request.setParameter("adoption.judgeName", "Ruwan Perera");
        request.setParameter("adoption.languageToTransliterate", "si");
        request.setParameter("adoption.orderIssuedDate", "2010-08-17T00:00:00+05:30");
        request.setParameter("adoption.orderReceivedDate", "2010-08-10T00:00:00+05:30");
        request.setParameter("adoption.wifeCountryId", "2");
        request.setParameter("adoption.wifeName", "Rathna kumari");
        request.setParameter("adoption.wifePINorNIC", "689789789V");
        request.setParameter("adoption.wifePassport", "456535355");
        request.setParameter("birthDistrictId", "1");
        request.setParameter("birthDivisionId", "1");
        request.setParameter("adoption.adoptionEntryNo", "1");
        request.setParameter("dojo.adoption.childBirthDate", "2009-08-03");
        request.setParameter("dojo.adoption.orderIssuedDate", "2010-08-17");
        request.setParameter("dojo.adoption.orderReceivedDate", "2010-08-10");
        request.setParameter("dsDivisionId", "1");
        initAndExecute("/adoption/eprAdoptionAction.do", session);
        session = adoptionAction.getSession();

        assertNotNull("Court order Number ", adoptionAction.getAdoption().getCourtOrderNumber());
        assertNotNull("Child Birthday ", adoptionAction.getAdoption().getChildBirthDate());
        assertNotNull("Child New name ", adoptionAction.getAdoption().getChildNewName());
        logger.debug("successfully persisted with the idUkey :{}", adoptionAction.getIdUKey());

    }

    public void testAdoptionCertificate() throws Exception {
        //set idUKey 5 to CERTIFICATE_REQUEST_CAPTURED state
        User user = loginSampleUser();
        long idUKey = 5;
        //set state to approve
        AdoptionOrder adoption = adoptionOrderService.getById(idUKey, user);
        adoptionOrderService.approveAdoptionOrder(idUKey, user);
        //set to notice letter printed
        adoptionOrderService.setStatusToPrintedNotice(idUKey, user);
        //set applicant info
        adoption = adoptionOrderService.getById(idUKey, user);
        adoption.setApplicantName("applicant name");
        adoption.setApplicantAddress("applicant address");
        adoption.setApplicantCountryId(3);
        adoption.setApplicantPINorNIC("198623610029");
        adoption.setApplicantPassport("applicant passport");
        adoption.setApplicantMother(false);
        //set state to APPLICANT_INFO_CAPTURED
        adoptionOrderService.setApplicantInfo(adoption, user);

        Map session = UserLogin("ashoka", "ashoka");
        request.setParameter("idUKey", "5");
        request.setParameter("pageNo", "1");
        request.setParameter("nextFlag", "false");
        request.setParameter("previousFlag", "false");
        initAndExecute("/adoption/eprPrintAdoptionCertificate.do", session);
        session = adoptionAction.getSession();
        assertEquals("Action errors for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());

        assertNotNull("child new name", adoptionAction.getAdoption().getChildNewName());
        assertNotNull("child birth day", adoptionAction.getAdoption().getChildAgeYears());
        //assertNotNull("Court", adoptionAction.getAdoption().getCourt());
        //assertNotNull("Court", adoptionAction.getAdoption().getCourt());
        assertNotNull("father NIC ", adoptionAction.getAdoption().getApplicantPINorNIC());
    }

    public void testAdoptionApproval() throws Exception {
        //to approve adoption must be in DATA_ENTRY state
        User user = loginSampleUser();
        long idUKey = 4;   //this is in date entry mode

        Map session = UserLogin("ashoka", "ashoka");
        request.setParameter("idUKey", "4");
        initAndExecute("/adoption/eprApproveAdoption.do", session);
        session = adoptionAction.getSession();
        assertEquals("Action errors for Adoption Declaration ", 1, adoptionAction.getActionErrors().size());
        logger.debug("Successfully approved : {} ", adoptionAction.getIdUKey());
    }

    public void testAdoptionApplicantInfo() throws Exception {
        //to capture applicant info adoption must be in NOTICE_LETTER_PRINTED state
        //idUKey 3 currently in DATA_ENTRY mode
        //and court order number is   gampaha1232
        User user = loginSampleUser();
        long idUKey = 3;
        //set state to approve
        adoptionOrderService.approveAdoptionOrder(idUKey, user);
        //set to notice letter printed
        adoptionOrderService.setStatusToPrintedNotice(idUKey, user);

        Map session = UserLogin("rg", "password");
        initAndExecute("/adoption/eprAdoptionApplicantInfo.do", session);
        session = adoptionAction.getSession();
        assertEquals("Action errors for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());

        request.setParameter("idUKey", "3");
        request.setParameter("search", "සොයන්න");
        initAndExecute("/adoption/eprAdoptionFind.do", session);
        session = adoptionAction.getSession();
        assertEquals("Action errors for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());
        //assertNotNull("court ", adoptionAction.getAdoption().getCourt());
        assertNotNull("court order number ", adoptionAction.getAdoption().getCourtOrderNumber());
        assertNotNull("child given name ", adoptionAction.getAdoption().getChildNewName());
        assertNotNull("child new name ", adoptionAction.getAdoption().getChildNewName());

        request.setParameter("certificateApplicantAddress", "Kinigama,Bandarawela.");
        request.setParameter("certificateApplicantName", "Dharmasili Allekona");
        request.setParameter("certificateApplicantPINorNIC", "758825881V");
        request.setParameter("certificateApplicantType", "OTHER");
        request.setParameter("pageNo", "1");

        initAndExecute("/adoption/eprAdoptionApplicantInfo.do", session);
        session = adoptionAction.getSession();
        assertEquals("Action errors for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());
        //todo check follow
/*        assertNotNull("certificate Applicant Address", adoptionAction.getAdoption().getCertificateApplicantAddress());
        assertNotNull("certificate Applicant Name", adoptionAction.getAdoption().getCertificateApplicantName());*/
    }

    public void testAdoptionApprovalAndPrint() throws Exception {
        //set idUKey 2 to CERTIFICATE_PRINTED state
        User user = loginSampleUser();
        long idUKey = 2;
        //set state to approve
        AdoptionOrder adoption = adoptionOrderService.getById(idUKey, user);
        adoptionOrderService.approveAdoptionOrder(idUKey, user);
        //set to notice letter printed
        adoptionOrderService.setStatusToPrintedNotice(idUKey, user);
        //set applicant info
        adoption = adoptionOrderService.getById(idUKey, user);
        adoption.setApplicantName("applicant name");
        adoption.setApplicantAddress("applicant address");
        adoption.setApplicantCountryId(3);
        adoption.setApplicantPINorNIC("197621562569");
        adoption.setApplicantPassport("applocant passport");
        adoption.setApplicantMother(false);
        //set state to APPLICANT_INFO_CAPTURED
        adoptionOrderService.setApplicantInfo(adoption, user);
        adoptionOrderService.setStatusToPrintedCertificate(idUKey, user);

        Map session = UserLogin("rg", "password");
        initAndExecute("/adoption/eprAdoptionApprovalAndPrint.do", session);
        session = adoptionAction.getSession();

        assertNotNull("DSDivision list", adoptionAction.getDsDivisionList());
        assertNotNull("District list", adoptionAction.getDistrictList());

        request.setParameter("idUKey", "2");
        request.setParameter("pageNo", "1");
        request.setParameter("nextFlag", "false");
        request.setParameter("previousFlag", "false");
        initAndExecute("/adoption/eprPrintAdoptionNotice.do", session);
        session = adoptionAction.getSession();
        assertEquals("Action errors for Adoption Declaration ", 1, adoptionAction.getActionErrors().size());

        // assertNotNull("Court ", adoptionAction.getAdoption().getCourt());
        assertNotNull("Court order Number ", adoptionAction.getAdoption().getCourtOrderNumber());
        assertNotNull("Child Age Year ", adoptionAction.getAdoption().getChildAgeYears());
        assertNotNull("Child New name ", adoptionAction.getAdoption().getChildNewName());
    }


    private void initAndExecute(String mapping, Map session) {
        proxy = getActionProxy(mapping);
        adoptionAction = (AdoptionAction) proxy.getAction();
        assertNotNull(adoptionAction);
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        try {
            proxy.execute();
        } catch (Exception e) {
            logger.error("Handle Error {} : {}", e.getMessage(), e);
        }
    }

    private Map UserLogin(String username, String passwd) throws Exception {
        request.setParameter("javaScript", "true");
        request.setParameter("userName", username);
        request.setParameter("password", passwd);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        LoginAction loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }

    private static User loginSampleUser() {
        User rg = null;
        try {
            rg = userManager.authenticateUser("rg", "password");
        } catch (AuthorizationException e) {
            logger.debug("exception when authorizing a user :'rg' ");
        }
        return rg;
    }

    private static List persistAdoption() {
        List x = new LinkedList();
        for (int i = 0; i < 5; i++) {
            AdoptionOrder adoption = new AdoptionOrder();

            adoption.setOrderIssuedDate(new Date());
            adoption.setOrderReceivedDate(new Date());
            adoption.setCourtOrderNumber("gampaha123" + i);
            adoption.setCourt(AvissawellaCourt);
            adoption.setJudgeName("judge name");
            adoption.setApplicantName("applicant name" + i);
            adoption.setApplicantAddress("applicant address" + i);
            adoption.setApplicantPINorNIC("19862361002" + i);
            //set applicant is mother
            adoption.setApplicantMother(true);
            adoption.setWifeName("wife name" + i);
            adoption.setWifePINorNIC("19782361002" + i);
            adoption.setWifeCountryId(3);
            adoption.setWifePassport("wife passport" + i);
            //court given name so existing name can be null
            adoption.setChildNewName("child new name" + i);
            adoption.setChildAgeYears(10);
            adoption.setChildGender(0);
            adoption.setLanguageToTransliterate("si");
            adoption.setNoticingZonalOffice(zonalOfficeDAO.getZonalOffice(1));
            adoption.setStatus(AdoptionOrder.State.DATA_ENTRY);
            x.add(adoption);
        }

        return x;
    }

    private void userLogOut() throws Exception {
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        LoginAction loginAction = (LoginAction) proxy.getAction();
        proxy.execute();
    }
}
