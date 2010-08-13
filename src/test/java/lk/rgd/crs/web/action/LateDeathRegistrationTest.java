package lk.rgd.crs.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.crs.web.action.deaths.DeathRegisterAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.DeathRegister;

import java.util.Map;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

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
        assertEquals("Action erros for Adoption Declaration ", 0, deathAction.getActionErrors().size());

        DeathRegister ddf;
        session = deathAction.getSession();
        assertNotNull("Dsdivision list", deathAction.getDsDivisionList());
        assertNotNull("District list", deathAction.getDistrictList());
        ddf = (DeathRegister) session.get(WebConstants.SESSION_DEATH_DECLARATION_BEAN);
        
        request.setParameter("death.causeOfDeath", "Bus accident");
        request.setParameter("death.causeOfDeathEstablished", "false");
        request.setParameter("death.dateOfDeath", "2010-08-01");
        request.setParameter("death.dateOfRegistration", "2010-08-17");
        request.setParameter("death.deathSerialNo", "123");
        request.setParameter("death.icdCodeOfCause", "33EE");
        request.setParameter("death.infantLessThan30Days", "false");
        request.setParameter("death.placeOfBurial", "මහරගම");
        request.setParameter("death.placeOfDeath", "මහරගම මහරෝහල");
        request.setParameter("death.placeOfDeathInEnglish.", "Maharagama Hospital");
        request.setParameter("death.reasonForLateRegistration", "No reason");
        request.setParameter("death.timeOfDeath", "12:30");
        request.setParameter("deathDistrictId", "1");
        request.setParameter("deathDivisionId", "1");
        request.setParameter("deathPerson.deathPersonAge", "34");
        request.setParameter("deathPerson.deathPersonFatherFullName", "Samarakone P.");
        request.setParameter("deathPerson.deathPersonFatherPINorNIC", "34343434");
        request.setParameter("deathPerson.deathPersonGender", "0");
        request.setParameter("deathPerson.deathPersonMotherFullName", "Silawathi S.");
        request.setParameter("deathPerson.deathPersonMotherPINorNIC", "34354455");
        request.setParameter("deathPerson.deathPersonNameOfficialLang", "සෝමතිලක ජයසූරිය");
        request.setParameter("deathPerson.deathPersonNameInEnglish", "Somathilaka Jayasooriya");
        request.setParameter("deathPerson.deathPersonPINorNIC", "333333");
        request.setParameter("deathPerson.deathPersonPassportNo", "343434");
        request.setParameter("deathPerson.deathPersonPermanentAddress", "Wijayarama Rd,Egodawaththa.");
        request.setParameter("deathPersonCountry", "1");
        request.setParameter("deathPersonRace", "0");
        request.setParameter("deathType", "MISSING");
        request.setParameter("dsDivisionId", "1");
        request.setParameter("pageNo", "1");

        initAndExucute("/deaths/eprDeathDeclaration.do", session);
        session = deathAction.getSession();
        assertEquals("Action erros for Adoption Declaration ", 0, deathAction.getActionErrors().size());

        assertEquals("Caurse of Death","BUS ACCIDENT",ddf.getDeath().getCauseOfDeath());
        assertEquals("Date of Death","2010-08-01",df.format(ddf.getDeath().getDateOfDeath()));
        assertEquals("date of Registration","2010-08-17",df.format(ddf.getDeath().getDateOfRegistration()));
        assertEquals("Cause Of Death Established",false,ddf.getDeath().isCauseOfDeathEstablished());
        assertEquals("Infant Less Than 30 Days",false,ddf.getDeath().isInfantLessThan30Days());
        assertEquals("Icd Code Of Cause","33EE",ddf.getDeath().getIcdCodeOfCause());
        assertEquals("Place of Death","මහරගම මහරෝහල",ddf.getDeath().getPlaceOfDeath());
        assertEquals("Place of Burial","මහරගම",ddf.getDeath().getPlaceOfBurial());
        assertEquals("Time of daath","12:30",ddf.getDeath().getTimeOfDeath());
        assertEquals("deathDistrictId",11,ddf.getDeath().getDeathDivision().getDistrict().getDistrictId());
        assertEquals("deathDivisionId",1,ddf.getDeath().getDeathDivision().getDivisionId());
        assertEquals("deathPersonFatherFullName","SAMARAKONE P.",ddf.getDeathPerson().getDeathPersonFatherFullName());
        assertEquals("deathPersonMotherFullName","SILAWATHI S.",ddf.getDeathPerson().getDeathPersonMotherFullName());
        assertEquals("deathPersonNameOfficialLang","සෝමතිලක ජයසූරිය",ddf.getDeathPerson().getDeathPersonNameOfficialLang());
        assertEquals("deathPersonNameInEnglish","SOMATHILAKA JAYASOORIYA",ddf.getDeathPerson().getDeathPersonNameInEnglish());
        assertEquals("deathPersonPermanentAddress","WIJAYARAMA RD,EGODAWATHTHA.",ddf.getDeathPerson().getDeathPersonPermanentAddress());

        request.setParameter("declarant.declarantAddress", "Egodawaththa,Maharagama.");
        request.setParameter("declarant.declarantEMail", "wwwww@gmail.com");
        request.setParameter("declarant.declarantFullName", "Rangith Kumara");
        request.setParameter("declarant.declarantNICorPIN", "333333333");
        request.setParameter("declarant.declarantPhone", "3434343434");
        request.setParameter("declarant.declarantType", "RELATIVE");
        request.setParameter("notifyingAuthority.notifyingAuthorityName", "Rajapaksha M.");
        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", "GANGODAVILA,EGODAWATHTHA");
        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", "852012132V");
        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", "2010-08-17");
        request.setParameter("pageNo", "2");

        initAndExucute("/deaths/eprDeathDeclaration.do", session);
        session = deathAction.getSession();
        assertEquals("Action erros for Adoption Declaration ", 0, deathAction.getActionErrors().size());

        assertEquals("Declarent Address","EGODAWATHTHA,MAHARAGAMA.",ddf.getDeclarant().getDeclarantAddress());
        assertEquals("Declarent E-Mail Address","WWWWW@GMAIL.COM",ddf.getDeclarant().getDeclarantEMail());
        assertEquals("Declarent Name","RANGITH KUMARA",ddf.getDeclarant().getDeclarantFullName());
        assertEquals("NotifyingAuthority Name","RAJAPAKSHA M.",ddf.getNotifyingAuthority().getNotifyingAuthorityName());
        assertEquals("NotifyingAuthority Address","GANGODAVILA,EGODAWATHTHA",ddf.getNotifyingAuthority().getNotifyingAuthorityAddress());
        logger.debug("New late death declaration successfuly persist S idUKey : {}",ddf.getIdUKey());
    }


}
