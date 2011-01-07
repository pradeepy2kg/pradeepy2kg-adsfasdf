package lk.rgd.common;

import junit.framework.TestCase;
import lk.rgd.AppConstants;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @author asankha
 */
public class MasterTablesTest extends TestCase {

    private final ApplicationContext ctx = UnitTestManager.ctx;

    public void testDistrictsAndBDDivisionListsReturnedForUsers() throws Exception {
        DistrictDAO districtDAO = (DistrictDAO) ctx.getBean("districtDAOImpl", DistrictDAO.class);
        DSDivisionDAO dsDivisionDAO = (DSDivisionDAO) ctx.getBean("dsDivisionDAOImpl", DSDivisionDAO.class);
        BDDivisionDAO bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
        UserDAO userDAO = (UserDAO) ctx.getBean("userDAOImpl", UserDAO.class);

        // RG must see all districts and all BDDivisions for a selected district
        User rg = userDAO.getUserByPK("rg");
        Map<Integer, String> districts = districtDAO.getDistrictNames(AppConstants.SINHALA, rg);
        Assert.assertTrue(districts.size() == 25);
        Map<Integer, String> dsDivisions = dsDivisionDAO.getDSDivisionNames(1, AppConstants.SINHALA, rg);
        Assert.assertTrue(dsDivisions.size() > 1);
        Map<Integer, String> bdDivisions = bdDivisionDAO.getBDDivisionNames(1, AppConstants.SINHALA, rg);
        Assert.assertTrue(bdDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(2, AppConstants.SINHALA, rg);
        Assert.assertTrue(bdDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(3, AppConstants.SINHALA, rg);
        Assert.assertTrue(bdDivisions.size() > 1);

        // ARG-Western Province sees all 3 districts in province
        User arg = userDAO.getUserByPK("arg-western");
        districts = districtDAO.getDistrictNames(AppConstants.SINHALA, arg);
        Assert.assertTrue(districts.size() == 3);
        dsDivisions = dsDivisionDAO.getDSDivisionNames(1, AppConstants.SINHALA, arg);
        Assert.assertTrue(dsDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(1, AppConstants.SINHALA, arg);
        Assert.assertTrue(bdDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(2, AppConstants.SINHALA, rg);
        Assert.assertTrue(bdDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(3, AppConstants.SINHALA, rg);
        Assert.assertTrue(bdDivisions.size() > 1);

        // DR-colombo must see only colombo, but all BDDivisions within it
        User dr = userDAO.getUserByPK("dr-colombo");
        districts = districtDAO.getDistrictNames(AppConstants.SINHALA, dr);
        Assert.assertTrue(districts.size() == 1);
        Assert.assertTrue("11 : කොළඹ".equals(districts.values().iterator().next()));
        dsDivisions = dsDivisionDAO.getDSDivisionNames(1, AppConstants.SINHALA, dr);
        Assert.assertTrue(dsDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(1, AppConstants.SINHALA, dr);
        Assert.assertTrue(bdDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(2, AppConstants.SINHALA, dr);
        Assert.assertTrue(bdDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(3, AppConstants.SINHALA, dr);
        Assert.assertTrue(bdDivisions.size() > 1);

        // ADR-colombo must see only colombo, and fort BD division
        User adr = userDAO.getUserByPK("adr-colombo-colombo");
        districts = districtDAO.getDistrictNames(AppConstants.SINHALA, adr);
        Assert.assertTrue(districts.size() == 1);
        Assert.assertTrue("11 : කොළඹ".equals(districts.values().iterator().next()));
        dsDivisions = dsDivisionDAO.getDSDivisionNames(1, AppConstants.SINHALA, adr);
        Assert.assertTrue(dsDivisions.size() == 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(1, AppConstants.SINHALA, adr);
        Assert.assertTrue(bdDivisions.size() > 1);
    }

    public void testCountriesAndRaces() throws Exception {
        CountryDAO countryDAO = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
        Map<Integer, String> countries = countryDAO.getCountries(AppConstants.SINHALA);
        Assert.assertTrue(countries.size() > 0);

        RaceDAO raceDAO = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
        Map<Integer, String> races = raceDAO.getRaces(AppConstants.SINHALA);
        Assert.assertTrue(races.size() > 0);
    }
}