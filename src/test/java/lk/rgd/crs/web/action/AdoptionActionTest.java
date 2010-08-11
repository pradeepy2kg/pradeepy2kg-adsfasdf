package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.action.births.AdoptionAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.api.domain.AdoptionOrder;

/**
 * Created by IntelliJ IDEA.
 * User: widu
 * Date: Aug 2, 2010
 * Time: 3:13:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdoptionActionTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(AdoptionActionTest.class);
    private AdoptionAction adoptionAction;
    private AdoptionOrder adoption;

    private ActionProxy proxy;
    private User user;
    private AdoptionOrderService service;

    private Map UserLogin(String username, String passwd) throws Exception {
        request.setParameter("userName", username);
        request.setParameter("password", passwd);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        LoginAction loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }

    private void initAndExucute(String mapping, Map session) {
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

    @Override
    public String getContextLocations() {
        return "unitTest_applicationContext.xml";
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
      initAndExucute("/adoption/eprAdoptionRegistrationAction.do", session);
      session = adoptionAction.getSession();

      assertNotNull("Dsdivision list", adoptionAction.getDsDivisionList());
      assertNotNull("District list", adoptionAction.getDistrictList());

      assertEquals("Action erros for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());
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
      request.setParameter("adoption.court", "badulla");
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
      request.setParameter("dojo.adoption.childBirthDate", "2009-08-03");
      request.setParameter("dojo.adoption.orderIssuedDate", "2010-08-17");
      request.setParameter("dojo.adoption.orderReceivedDate", "2010-08-10");
      request.setParameter("dsDivisionId", "1");
      request.setParameter("pageNo", "1");
      request.setParameter("idUKey", "0");
      initAndExucute("/adoption/eprAdoptionAction.do", session);
      session = adoptionAction.getSession();

      assertNotNull("Court ", adoptionAction.getAdoption().getCourt());
      assertNotNull("Court order Number ", adoptionAction.getAdoption().getCourtOrderNumber());
      assertNotNull("Child Birthday ", adoptionAction.getAdoption().getChildBirthDate());
      assertNotNull("Child New name ", adoptionAction.getAdoption().getChildNewName());

      logger.debug("successfully persisted with the idUkey :{}", adoptionAction.getIdUKey());
  }

  public void testAdoptionEdit() throws Exception {
//      Map session = UserLogin("ashoka", "ashoka");
//      request.setParameter("idUKey", "1");
//      initAndExucute("/adoption/eprAdoptionEditMode.do", session);
//      session = adoptionAction.getSession();
//      assertEquals("Action erros for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());
//
//      assertNotNull("Child Name", adoptionAction.getAdoption().getChildExistingName());
//
//      request.setParameter("adoption.applicantAddress", "Thalawathugoda Rd,Kotte.");
//      request.setParameter("adoption.applicantCountryId", "3");
//      request.setParameter("adoption.applicantMother", "false");
//      request.setParameter("adoption.applicantName", "Rathnasiri Banda");
//      request.setParameter("adoption.applicantPINorNIC", "654425588V");
//      request.setParameter("adoption.applicantPassport", "456456456");
//      request.setParameter("adoption.birthCertificateSerial", "000002");
//      request.setParameter("adoption.childAgeMonths", "2");
//      request.setParameter("adoption.childAgeYears", "1");
//      request.setParameter("adoption.childBirthDate", "2009-08-03T00:00:00+05:30");
//      request.setParameter("adoption.childExistingName", "Priyankara Perera");
//      request.setParameter("adoption.childGender", "1");
//      request.setParameter("adoption.childNewName", "Rasika Bandara");
//      request.setParameter("adoption.court", "badulla");
//      request.setParameter("adoption.courtOrderNumber", "20100803");
//      request.setParameter("adoption.judgeName", "Ruwan Perera");
//      request.setParameter("adoption.languageToTransliterate", "si");
//      request.setParameter("adoption.orderIssuedDate", "2010-08-17T00:00:00+05:30");
//      request.setParameter("adoption.orderReceivedDate", "2010-08-10T00:00:00+05:30");
//      request.setParameter("adoption.wifeCountryId", "2");
//      request.setParameter("adoption.wifeName", "Rathna kumari");
//      request.setParameter("adoption.wifePINorNIC", "689789789V");
//      request.setParameter("adoption.wifePassport", "456535355");
//      request.setParameter("birthDistrictId", "1");
//      request.setParameter("birthDivisionId", "1");
//      request.setParameter("dojo.adoption.childBirthDate", "2009-08-03");
//      request.setParameter("dojo.adoption.orderIssuedDate", "2010-08-17");
//      request.setParameter("dojo.adoption.orderReceivedDate", "2010-08-10");
//      request.setParameter("dsDivisionId", "1");
//      initAndExucute("/adoption/eprAdoptionAction.do", session);
//      session = adoptionAction.getSession();
//
//      assertNotNull("Court ", adoptionAction.getAdoption().getCourt());
//      assertNotNull("Court order Number ", adoptionAction.getAdoption().getCourtOrderNumber());
//      assertNotNull("Child Birthday ", adoptionAction.getAdoption().getChildBirthDate());
//      assertNotNull("Child New name ", adoptionAction.getAdoption().getChildNewName());
//      logger.debug("successfully persisted with the idUkey :{}", adoptionAction.getIdUKey());

  }


  public void testAdoptionCertificate() throws Exception {
//      Map session = UserLogin("ashoka", "ashoka");
//      request.setParameter("idUKey", "8");
//      request.setParameter("pageNo", "1");
//      request.setParameter("nextFlag", "false");
//      request.setParameter("previousFlag", "false");
//      initAndExucute("/adoption/eprPrintAdoptionCertificate.do", session);
//      session = adoptionAction.getSession();
//      assertEquals("Action erros for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());
//
//      assertNotNull("child new name", adoptionAction.getAdoption().getChildNewName());
//      assertNotNull("child birth day", adoptionAction.getAdoption().getChildBirthDate());
//      assertNotNull("Court", adoptionAction.getAdoption().getCourt());
//      assertNotNull("Court", adoptionAction.getAdoption().getCourt());
//      assertNotNull("father NIC ", adoptionAction.getAdoption().getApplicantPINorNIC());
  }

  public void testAdoptionApprovalAndPrint() throws Exception {
//      Map session = UserLogin("ashoka", "ashoka");
//      initAndExucute("/adoption/eprAdoptionApprovalAndPrint.do", session);
//      session = adoptionAction.getSession();
//
//      assertNotNull("Dsdivision list", adoptionAction.getDsDivisionList());
//      assertNotNull("District list", adoptionAction.getDistrictList());
//
//      request.setParameter("idUKey", "9");
//      request.setParameter("pageNo", "1");
//      request.setParameter("nextFlag", "false");
//      request.setParameter("previousFlag", "false");
//      initAndExucute("/adoption/eprPrintAdoptionNotice.do", session);
//      session = adoptionAction.getSession();
//      assertEquals("Action erros for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());
//
//      assertNotNull("Court ", adoptionAction.getAdoption().getCourt());
//      assertNotNull("Court order Number ", adoptionAction.getAdoption().getCourtOrderNumber());
//      assertNotNull("Child Birthday ", adoptionAction.getAdoption().getChildBirthDate());
//      assertNotNull("Child New name ", adoptionAction.getAdoption().getChildNewName());
  }

  public void testAdoptionApproval() throws Exception{
//      Map session = UserLogin("ashoka", "ashoka");
//      request.setParameter("idUKey", "5");
//      initAndExucute("/adoption/eprApproveAdoption.do", session);
//      session = adoptionAction.getSession();
//      assertEquals("Action erros for Adoption Declaration ", 1, adoptionAction.getActionErrors().size());
//      logger.debug("Sucsessfuly approved : {} ",adoptionAction.getIdUKey());
  }
    public void testAdoptionApplicantInfo() throws Exception {
//        Map session = UserLogin("ashoka", "ashoka");
//        initAndExucute("/adoption/eprAdoptionApplicantInfo.do", session);
//        session = adoptionAction.getSession();
//        assertEquals("Action erros for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());
//
//        request.setParameter("courtOrderNo", "147255");
//        request.setParameter("search", "සොයන්න");
//        initAndExucute("/adoption/eprAdoptionFind.do", session);
//        session = adoptionAction.getSession();
//        assertEquals("Action erros for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());
//        assertNotNull("court ", adoptionAction.getAdoption().getCourt());
//        assertNotNull("court ordernumber ", adoptionAction.getAdoption().getCourtOrderNumber());
//        assertNotNull("child existing name ", adoptionAction.getAdoption().getChildExistingName());
//        assertNotNull("child new name ", adoptionAction.getAdoption().getChildNewName());
//
//        request.setParameter("certificateApplicantAddress", "Kinigama,Bandarawela.");
//        request.setParameter("certificateApplicantName", "Dharmasili Allekona");
//        request.setParameter("certificateApplicantPINorNIC", "758825881V");
//        request.setParameter("certificateApplicantType", "OTHER");
//        request.setParameter("pageNo", "1");
//
//        initAndExucute("/adoption/eprAdoptionApplicantInfo.do", session);
//        session = adoptionAction.getSession();
//        assertEquals("Action erros for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());
//
//        assertNotNull("Applicant Address",adoptionAction.getAdoption().getApplicantAddress());
//        assertNotNull("Applicant Name",adoptionAction.getAdoption().getApplicantName());
    }
}
