package lk.rgd.crs.core.service;

import junit.framework.TestCase;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import lk.rgd.crs.web.util.MarriageType;
import lk.rgd.crs.web.util.TypeOfMarriagePlace;
import org.springframework.context.ApplicationContext;

import java.util.Date;

/**
 * @author amith jayasekara
 */
public class MarriageRegistrationServiceTest extends TestCase {

    protected final ApplicationContext ctx = UnitTestManager.ctx;
    protected final MarriageRegistrationService marriageRegistrationService;
    protected final CountryDAO countryDAO;
    protected final RaceDAO raceDAO;
    protected final UserManager userManager;
    protected final MRDivision colomboMRDivision;
    protected final MRDivisionDAO mrDivisionDAO;
    protected MarriageRegistrationDAO marriageRegistrationDAO;
    protected User deoColomboColombo;
    protected User deoGampahaNegambo;
    protected User adrColomboColombo;
    protected User adrGampahaNegambo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        marriageRegistrationDAO = (MarriageRegistrationDAO)
            ctx.getBean("marriageRegistrationDAOImpl", MarriageRegistrationDAO.class);
    }

    public MarriageRegistrationServiceTest() {
        countryDAO = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
        raceDAO = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
        userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
        mrDivisionDAO = (MRDivisionDAO) ctx.getBean("mrDivisionDAOImpl", MRDivisionDAO.class);
        marriageRegistrationService = (MarriageRegistrationService) ctx.getBean("marriageRegistrationService",
            MarriageRegistrationService.class);
        try {
            deoColomboColombo = userManager.authenticateUser("deo-colombo-colombo", "password");
            adrColomboColombo = userManager.authenticateUser("adr-colombo-colombo", "password");
            deoGampahaNegambo = userManager.authenticateUser("deo-gampaha-negambo", "password");
            adrGampahaNegambo = userManager.authenticateUser("adr-gampaha-negambo", "password");
        } catch (AuthorizationException e) {
            throw new IllegalArgumentException("Cannot authenticate sample users");
        }
        colomboMRDivision = mrDivisionDAO.getMRDivisionByPK(1);
    }

    public void testAddMinimalMarriageNotice() {
        //adding a marriage notice with minimal requirements
        MarriageRegister notice = getMinimalMarriageNotice(2010012345L, "1234567890", "1234567899");
        //this is submitted by male so  true
        marriageRegistrationService.addMarriageNotice(notice, MarriageNotice.Type.MALE_NOTICE, deoColomboColombo);

        //adding second notice
        //add with same pin numbers 
    }

    private MarriageRegister getMinimalMarriageNotice(long serialMale, String malePin, String femalePin) {
        MarriageRegister notice = new MarriageRegister();
        //male party
        MaleParty male = new MaleParty();
        male.setIdentificationNumberMale(malePin);
        male.setNameInEnglishMale("name in english" + malePin);
        notice.setMale(male);

        //female party
        FemaleParty female = new FemaleParty();
        female.setIdentificationNumberFemale(femalePin);
        female.setNameInEnglishFemale("name in english" + femalePin);
        notice.setFemale(female);

        //setting witness for male (assume notice is submitted by male party)
//        Witness m_1 = new Witness();
//        m_1.setIdentificationNumber((Integer.parseInt(malePin)) + 10 + "");
//        m_1.setFullName("full name witness 1 " + malePin);
//        notice.setMaleNoticeWitness_1(m_1);
//
//        Witness m_2 = new Witness();
//        m_2.setIdentificationNumber((Integer.parseInt(malePin)) + 20 + "");
//        m_2.setFullName("full name witness 2 " + malePin);
//        notice.setMaleNoticeWitness_2(m_2);

        //setting mandatory fields
        //this is submitted by male party
        notice.setSingleNotice(true);
        notice.setDateOfMaleNotice(new Date());
        notice.setMrDivisionOfMaleNotice(colomboMRDivision);
        notice.setSerialOfMaleNotice(serialMale);
        notice.setTypeOfMarriage(MarriageType.GENERAL);
        notice.setTypeOfMarriagePlace(TypeOfMarriagePlace.REGISTRAR_OFFICE);

        return notice;
    }

}
