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
        MarriageRegister notice = getMinimalMarriageNotice(2010012345L, colomboMRDivision, false, "1234567890",
            "1234567899", MarriageNotice.Type.MALE_NOTICE);
        //assuming this is male notice
        //and male party is expecting the license
/*
        notice.setLicenseRequestByMale(true);
*/
        marriageRegistrationService.addMarriageNotice(notice, MarriageNotice.Type.MALE_NOTICE, deoColomboColombo);
        //add with same pin numbers
    }

    public void testMarriageNoticeApproval() {

    }

    private MarriageRegister getMinimalMarriageNotice(long serialMale, MRDivision mrDivision, boolean isSingleNotice,
        String malePin, String femalePin, MarriageNotice.Type type) {

        MarriageRegister notice = new MarriageRegister();
        //male party
        MaleParty male = new MaleParty();
        male.setIdentificationNumberMale(malePin);
        male.setDateOfBirthMale(new Date());
        male.setNameInEnglishMale("name in english" + malePin);
        //female party
        FemaleParty female = new FemaleParty();
        female.setIdentificationNumberFemale(femalePin);
        female.setDateOfBirthFemale(new Date());
        female.setNameInEnglishFemale("name in english" + femalePin);

        notice.setSingleNotice(isSingleNotice);
        notice.setTypeOfMarriage(MarriageType.GENERAL);
        notice.setTypeOfMarriagePlace(TypeOfMarriagePlace.REGISTRAR_OFFICE);

        switch (type) {
            case BOTH_NOTICE:
                notice.setFemale(female);
            case MALE_NOTICE:
                notice.setMale(male);
                notice.setDateOfMaleNotice(new Date());
                notice.setMrDivisionOfMaleNotice(mrDivision);
                notice.setSerialOfMaleNotice(serialMale);
                break;
            case FEMALE_NOTICE:
                notice.setFemale(female);
                notice.setDateOfFemaleNotice(new Date());
                notice.setMrDivisionOfFemaleNotice(mrDivision);
                notice.setSerialOfFemaleNotice(serialMale);
        }
        return notice;
    }

}
