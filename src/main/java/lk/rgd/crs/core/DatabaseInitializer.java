package lk.rgd.crs.core;

import lk.rgd.AppConstants;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.RoleDAO;
import lk.rgd.common.api.domain.Role;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BirthDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.BitSet;

/**
 * Initializes an in-memory database for testing and populates it with sample/initial data
 *
 * @author asankha
 */
public class DatabaseInitializer implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
            new ClassPathResource("test_database.sql"), false);
        logger.info("Initialized the test database by executing test_database.sql");

        // ---------------- populate permissions ---------------------
        RoleDAO roleDao = (RoleDAO) ctx.getBean("roleDAOImpl", RoleDAO.class);

        Role adrRole = roleDao.getRole("ADR");
        BitSet bs = new BitSet();
        bs.set(Permission.APPROVE_BDF);
        adrRole.setPermBitSet(bs);
        roleDao.save(adrRole);

        Role drRole = roleDao.getRole("DR");
        bs = new BitSet();
        bs.or(adrRole.getPermBitSet());
        bs.set(Permission.DISTRICT_WIDE_ACCESS);
        drRole.setPermBitSet(bs);
        roleDao.save(drRole);

        Role rgRole = roleDao.getRole("RG");
        bs = new BitSet();
        bs.or(drRole.getPermBitSet());
        bs.set(Permission.DISTRICT_WIDE_ACCESS);
        rgRole.setPermBitSet(bs);
        roleDao.save(rgRole);

        // ---------------- populate sample BDFs ---------------------
        BirthDeclarationDAO dao = (BirthDeclarationDAO) ctx.getBean("birthDeclarationDAOImpl", BirthDeclarationDAO.class);
        try {

            BirthDeclaration bdf = new BirthDeclaration();
            bdf.setBirthDistrict(11);
            bdf.setBirthDivision(1);
            bdf.setBdfSerialNo("A112");
            bdf.setChildFullNameEnglish("Amith Sampath Jayasekara");
            bdf.setChildFullNameOfficialLang("අමිත් සමිපත් ජයසේකර");
            bdf.setDateOfBirth(dfm.parse("2010-02-26"));
            bdf.setDateOfRegistration(dfm.parse("2010-03-01"));
            bdf.setChildGender(AppConstants.GENDER_FEMALE);
            bdf.setStatus(1);
            dao.addBirthDeclaration(bdf);

            bdf = new BirthDeclaration();
            bdf.setBirthDistrict(11);
            bdf.setBirthDivision(1);
            bdf.setBdfSerialNo("A113");
            bdf.setChildFullNameEnglish("Baby A113 name in English");
            bdf.setChildFullNameOfficialLang("A113 බබාගේ නම සිංහලෙන්");
            bdf.setDateOfBirth(dfm.parse("2010-02-23"));
            bdf.setDateOfRegistration(dfm.parse("2010-03-02"));
            bdf.setChildGender(AppConstants.GENDER_MALE);
            bdf.setStatus(1);
            dao.addBirthDeclaration(bdf);

            bdf = new BirthDeclaration();
            bdf.setBirthDistrict(11);
            bdf.setBirthDivision(1);
            bdf.setBdfSerialNo("A114");
            bdf.setChildFullNameEnglish("Baby A114 name in English");
            bdf.setChildFullNameOfficialLang("A114 බබාගේ නම සිංහලෙන්");
            bdf.setDateOfBirth(dfm.parse("2010-01-12"));
            bdf.setDateOfRegistration(dfm.parse("2010-02-01"));
            bdf.setChildGender(AppConstants.GENDER_MALE);
            bdf.setStatus(5);
            dao.addBirthDeclaration(bdf);

            bdf = new BirthDeclaration();
            bdf.setBirthDistrict(11);
            bdf.setBirthDivision(1);
            bdf.setBdfSerialNo("A115");
            bdf.setChildFullNameEnglish("Chathuranaga Gihan Chandimal Withana");
            bdf.setChildFullNameOfficialLang("චතුරංග ගිහාන් චන්දිමාල් විතාන");
            bdf.setDateOfBirth(dfm.parse("2010-02-21"));
            bdf.setDateOfRegistration(dfm.parse("2010-02-22"));
            bdf.setChildGender(AppConstants.GENDER_MALE);
            bdf.setStatus(2);
            dao.addBirthDeclaration(bdf);
            
        } catch (Exception e) {
            logger.warn("Error populating database with sample BDFs");
        }
    }
}
