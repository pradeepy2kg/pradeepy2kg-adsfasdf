package lk.rgd.crs.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import lk.rgd.crs.web.action.deaths.DeathRegisterAction;
import lk.rgd.crs.web.action.births.BirthRegisterAction;
import lk.rgd.common.CustomStrutsTestCase;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: tharanga
 * Date: Aug 9, 2010
 * Time: 9:37:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class DeathRegistrationDeclarionTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(DeathRegistrationDeclarionTest.class);
    private DeathRegisterAction deathAction;
    private ActionProxy proxy;

    public void testBirthConfirmationInitMappingProxy() throws Exception {
        Map session = UserLogin("ashoka", "ashoka");
        ActionMapping mapping = getActionMapping("/deaths/eprInitDeathDeclaration.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/deaths", mapping.getNamespace());
        assertEquals("eprInitDeathDeclaration", mapping.getName());

        request.setParameter("pageNo", "1");
        request.setParameter("death.deathSerialNo", "1");
        request.setParameter("death.dateOfDeath", "2010-07-21T00:00:00+05:30");
        request.setParameter("death.deathDistrictId", "11");
        request.setParameter("death.deathDsDivisionId", "3");
        request.setParameter("death.placeOfDeath", "කොළඹ කොටුව");
        request.setParameter("death.placeOfDeathInEnglish", "colombo port");
        request.setParameter("death.causeOfDeathEstablished", "true");
        request.setParameter("death.infantLessThan30Days", "false");
        request.setParameter("death.causeOfDeath", "cancer");
        request.setParameter("death.placeOfBurial", "public ");
        request.setParameter("deathPerson.deathPersonPINorNIC", "707433191V");
        request.setParameter("deathPerson.deathPersonCountryId", "3");
        request.setParameter("deathPerson.deathPersonPassportNo", "1200");
        request.setParameter("deathPerson.deathPersonAge", "86");
        request.setParameter("deathPerson.deathPersonGender", "female");
        request.setParameter("deathPerson.deathPersonNameOfficialLang", "සෙල්ලදොරේයි කනවති අමිමා");
        request.setParameter("deathPerson.deathPersonNameInEnglish", "selverdorei kanawathi amma");
        request.setParameter("deathPerson.deathPersonPermanentAddress", "Erapalamulla Ruwanwella");
        request.setParameter("deathPerson.deathPersonFatherPINorNIC", "707433191V");
        request.setParameter("deathPerson.deathPersonFatherFullName", "selverdorei kanawathi amma");
        request.setParameter("deathPerson.deathPersonMotherPINorNIC", "707433191V");
        request.setParameter("deathPerson.deathPersonMotherFullNamed", "selverdorei kanawathi amma");


        /*request.setParameter("declarant.declarantFullName","Tharanga Punchihewa")
         request.setParameter("declarant.declarantAddress","Erapalamulla,Ruwanwella");
         request.setParameter("declarant.declarantPhone","0718617804V");
         request.setParameter("declarant.declarantEMail","htpunchihewa@gmail.com");
         request.setParameter("declarant.declarantType","3");
         request.setParameter("firstWitness.witnessNICorPIN","782790393");
         request.setParameter("firstWitness.witnessFullName","Sampath Naline");
         request.setParameter("firstWitness.witnessAddress",""Molawaththa,Morawaththa,Ruwanwella);
         request.setParameter("secondWitness.witnessNICorPIN","892790393V");
         request.setParameter("secondWitness.witnessFullName","Rasika Niranjana Punchihewa");
         request.setParameter("secondWitness.witnessAddress","E51 Erapalamulla Ruwanwella");
         request.setParameter("notifyingAuthority.notifyingAuthorityPIN","853303399v");
         request.setParameter("notifyingAuthority.notifyingAuthorityName","Saman kUmara";
         request.setParameter("notifyingAuthority.notifyingAuthorityAddress","කැලුම් කොඩිප්පිලි"");
         request.setParameter("notifyingAuthority.notifyingAuthoritySignDate","2010-07-28T00:00:00+05:30");
        */


    }


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
        deathAction = (DeathRegisterAction) proxy.getAction();
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


    public DeathRegisterAction getDeathAction() {
        return deathAction;
    }

    public void setDeathAction(DeathRegisterAction deathAction) {
        this.deathAction = deathAction;
    }

    public ActionProxy getProxy() {
        return proxy;
    }

    public void setProxy(ActionProxy proxy) {
        this.proxy = proxy;
    }
}
