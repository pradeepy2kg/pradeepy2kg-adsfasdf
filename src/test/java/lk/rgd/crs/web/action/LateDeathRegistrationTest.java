package lk.rgd.crs.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.crs.web.action.deaths.DeathRegisterAction;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: widu
 * Date: Aug 9, 2010
 * Time: 10:30:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class LateDeathRegistrationTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(AdoptionActionTest.class);
    private ActionProxy proxy;
    private User user;
    private DeathRegisterAction deathAction;

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
        assertNotNull(deathAction);
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
 /*
    public void testActionMappingProxy() {
        ActionMapping mapping = getActionMapping("/deaths/eprInitLateDeathDeclaration.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/deaths", mapping.getNamespace());
        assertEquals("eprInitLateDeathDeclaration", mapping.getName());
        ActionProxy proxy = getActionProxy("/deaths/eprInitLateDeathDeclaration.do");
        assertNotNull(proxy);
        logger.debug("nameSpace {} and actionName {}", mapping.getNamespace(), proxy.getMethod());

        DeathRegisterAction action = (DeathRegisterAction) proxy.getAction();
        assertNotNull(action);
    }   

     public void testLateDeathDeclaration() throws Exception {
      Map session = UserLogin("ashoka", "ashoka");
      initAndExucute("/deaths/eprInitLateDeathDeclaration.do", session);
      session = deathAction.getSession();

      assertNotNull("Dsdivision list", deathAction.getDsDivisionList());
      assertNotNull("District list", deathAction.getDistrictList());

         death.causeOfDeath	sfgfdgsfdg
death.causeOfDeathEstabli...	false
death.dateOfDeath	08/08/2010
death.dateOfRegistration	08/10/2010
death.deathSerialNo	122
death.icdCodeOfCause	23ee
death.infantLessThan30Day...	false
death.placeOfBurial	sdfgsfdg
death.placeOfBurial	sdgsdfgs
death.placeOfDeath	fgsdfgsdfg
death.placeOfDeathInEngli...	sdfgsdfgsdfg
death.reasonForLateRegist...	dsfgsdfgsdfg
deathDistrictId	1
deathDivisionId	1
deathPerson.deathPersonAg...	44
deathPerson.deathPersonFa...	sdfghdghstgh
deathPerson.deathPersonFa...	34343434
deathPerson.deathPersonGe...	1
deathPerson.deathPersonMo...	whyttttyetry
deathPerson.deathPersonMo...	23435123
deathPerson.deathPersonNa...	sdfgsdfgs
deathPerson.deathPersonNa...	fdfgsdfgsdfg
deathPerson.deathPersonPI...	23444444444
deathPerson.deathPersonPa...	343434
deathPerson.deathPersonPe...	sdfgsdfgsdfg
deathPersonCountry	2
deathPersonRace	1
deathType	MISSING
dsDivisionId	1
pageNo	1

      assertEquals("Action erros for Adoption Declaration ", 0, deathAction.getActionErrors().size());


         initAndExucute("/deaths/eprDeathDeclaration.do", session);
      session = deathAction.getSession();

         declarant.declarantAddres...	dfhgdh
declarant.declarantEMail	dfgshs
declarant.declarantFullNa...	ghdfghdfg
declarant.declarantNICorP...	333333333
declarant.declarantPhone	34324563546
declarant.declarantType	RELATIVE
notifyingAuthority.notify...	sdhsdgfhsg
notifyingAuthority.notify...	sdfgdfgdfg
notifyingAuthority.notify...	2435234523
notifyingAuthority.notify...	08/19/2010
pageNo	2

                 initAndExucute("/popreg/deaths/eprDeathDeclaration.do", session);
      session = deathAction.getSession();
     }
       */

}
