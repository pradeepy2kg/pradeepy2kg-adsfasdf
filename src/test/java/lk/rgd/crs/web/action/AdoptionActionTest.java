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

    private ActionProxy proxy;
    User user;
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

    public void testAddAdoptionDeclaration() throws Exception {
      Map session = UserLogin("ashoka", "ashoka");
      ActionMapping mapping = getActionMapping("/adoption/eprAdoptionRegistrationAction.do");
      assertNotNull("Mapping not null {}", mapping);
      assertEquals("/adoption", mapping.getNamespace());
      assertEquals("eprAdoptionRegistrationAction", mapping.getName());
      ActionProxy proxy = getActionProxy("/adoption/eprAdoptionRegistrationAction.do");
      assertNotNull(proxy);

      initAndExucute("/adoption/eprAdoptionRegistrationAction.do", session);
      session = adoptionAction.getSession();
      User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
      //AdoptionOrder adoption;
      //adoption = (AdoptionOrder) session.get(WebConstants.SESSION_ADOPTION_ORDER);

      assertNotNull("Request District List Presence", adoptionAction.getDistrictList());
      assertNotNull("Request Race List Presence", adoptionAction.getIdUKey());
//      logger.debug("Id u key {} :", adoption.getIdUKey());

      request.setParameter("adoption.orderReceivedDate", "2010-08-01T00:00:00+05:30");
      request.setParameter("adoption.court", "District Court Colombo");
      request.setParameter("adoption.orderIssuedDate", "2010-08-03T00:00:00+05:30");
      request.setParameter("adoption.courtOrderNumber", "123");
      request.setParameter("adoption.judgeName", "Kaluarachchi A.");
      request.setParameter("adoption.applicantMother", "false");
      request.setParameter("adoption.applicantPINorNIC", "654425588V");
      request.setParameter("adoption.applicantCountryId", "1");
      request.setParameter("adoption.applicantPassport", "456456456");
      request.setParameter("adoption.applicantName", "Rathnasiri Banda");
      request.setParameter("adoption.applicantAddress", "Thalawathugoda Rd,Kotte.");
      request.setParameter("adoption.wifePINorNIC", "689789789V");
      request.setParameter("adoption.wifeCountryId", "1");
      request.setParameter("adoption.wifePassport", "456789789");
      request.setParameter("adoption.wifeName", "Kumari Ranathunga");
      request.setParameter("adoption.childBirthDate", "2009-06-01T00:00:00+05:30");
      request.setParameter("adoption.childGender", "1");
      request.setParameter("adoption.childAgeYears", "1");
      request.setParameter("adoption.childAgeMonths", "14");
      request.setParameter("adoption.childExistingName", "Priyankara Perera");
      request.setParameter("adoption.childNewName", "Rasika Bandara");
      //request.setParameter("adoption.birthCertificateSerial","20100801");
      request.setParameter("birthDistrictId", "11");
      request.setParameter("dsDivisionId", "3");
      request.setParameter("birthDivisionId", "1");
      request.setParameter("adoption.birthCertificateSerial", "111002");

      initAndExucute("/adoption/eprAdoptionAction.do", session);
      session = adoptionAction.getSession();
      
      assertNotNull("Birth division ID ", adoptionAction.getBirthDivisionId());
//      assertNotNull("Court OrderNo ", adoptionAction.getCourtOrderNo());

      assertEquals("Action erros for Adoption Declaration ", 0, adoptionAction.getActionErrors().size());
      logger.debug("successfully persisted with the idUkey :{}", adoptionAction.getIdUKey());
  }
    /*
    public void testAdoptionApprovalAndPrint() throws Exception {
        Map session = UserLogin("ashoka", "ashoka");
        ActionMapping mapping = getActionMapping("/adoption/eprAdoptionApprovalAndPrint.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/adoption", mapping.getNamespace());
        assertEquals("eprAdoptionApprovalAndPrint", mapping.getName());

        initAndExucute("/adoption/eprAdoptionApprovalAndPrint.do", session);
        session = adoptionAction.getSession();
        assertNotNull("District list ", adoptionAction.getDistrictList());
        assertNotNull("District list ", adoptionAction.getDsDivisionList());

        request.setParameter("idUkey", "4");
        initAndExucute("/adoption/eprAdoptionEditMode.do", session);
        session = adoptionAction.getSession();

        request.setParameter("adoption.orderReceivedDate", "2010-08-01T00:00:00+05:30");
        request.setParameter("adoption.court", "District Court Colombo");
        request.setParameter("adoption.orderIssuedDate", "2010-08-03T00:00:00+05:30");
        request.setParameter("adoption.courtOrderNumber", "1111122");
        request.setParameter("adoption.judgeName", "Kaluarachchi A.");
        request.setParameter("adoption.applicantMother", "false");
        request.setParameter("adoption.applicantPINorNIC", "654425588V");
        request.setParameter("adoption.applicantCountryId", "1");
        request.setParameter("adoption.applicantPassport", "456456456");
        request.setParameter("adoption.applicantName", "Rathnasiri Banda");
        request.setParameter("adoption.applicantAddress", "Thalawathugoda Rd,Kotte.");
        request.setParameter("adoption.wifePINorNIC", "689789789V");
        request.setParameter("adoption.wifeCountryId", "1");
        request.setParameter("adoption.wifePassport", "456789789");
        request.setParameter("adoption.wifeName", "Kumari Ranathunga");
        request.setParameter("adoption.childBirthDate", "2009-06-01T00:00:00+05:30");
        request.setParameter("adoption.childGender", "1");
        request.setParameter("adoption.childAgeYears", "1");
        request.setParameter("adoption.childAgeMonths", "14");
        request.setParameter("adoption.childExistingName", "Priyankara Perera");
        request.setParameter("adoption.childNewName", "Rasika Bandara");
        //request.setParameter("adoption.birthCertificateSerial","20100801");
        request.setParameter("adoption.birthDistrictId", "11");
        request.setParameter("adoption.dsDivisionId", "3");
        request.setParameter("adoption.birthDivisionId", "1");
        request.setParameter("adoption.birthCertificateSerial", "111002");
        initAndExucute("/adoption/eprAdoptionAction.do",session);
        session = adoptionAction.getSession();
        
    }

    public void testAdoptionDeclarationEditMode() throws Exception{
        Map session =UserLogin("ashoka", "ashoka");
        initAndExucute("/adoption/eprAdoptionEditMode.do", session);
        session = adoptionAction.getSession();

    }

    public void testAdoptionCertificate() throws Exception{
        Map session =UserLogin("ashoka", "ashoka");
        initAndExucute("/adoption/eprAdoptionCertificate.do", session);
        session = adoptionAction.getSession();

    }
    */
    /*
    public void testInitAdoptionReRegistration() throws Exception {
        Map session = UserLogin("ashoka", "ashoka");
        ActionMapping mapping = getActionMapping("/adoption/eprAdoptionReRegistrationAction.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/adoption", mapping.getNamespace());
        assertEquals("eprAdoptionReRegistrationAction", mapping.getName());

        request.setParameter("idUkey", "1");
        initAndExucute("/adoption/eprAdoptionReRegistrationAction.do", session);
        session = adoptionAction.getSession();
        logger.debug("Id ukey : {} ", adoptionAction.getIdUKey());
        logger.debug("Reregid {} :", adoptionAction.getCourtOrderNo());

    }  */

}
