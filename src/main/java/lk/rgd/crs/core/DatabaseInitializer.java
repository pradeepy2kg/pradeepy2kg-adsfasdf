package lk.rgd.crs.core;

import lk.rgd.AppConstants;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.RoleDAO;
import lk.rgd.common.api.domain.*;
import lk.rgd.common.core.dao.PreloadableDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.*;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

import javax.sql.DataSource;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

/**
 * Initializes an in-memory database for testing and populates it with sample/initial data
 *
 * @author asankha
 */
public class DatabaseInitializer implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");

    private DataSource dataSource;

    private static final List<Class> entityClasses = new ArrayList<Class>();

    static {
        entityClasses.add(AppParameter.class);
        entityClasses.add(Country.class);
        entityClasses.add(District.class);
        entityClasses.add(BDDivision.class);
        entityClasses.add(DSDivision.class);
        entityClasses.add(GNDivision.class);
        entityClasses.add(Race.class);
        entityClasses.add(Role.class);
        entityClasses.add(User.class);
        entityClasses.add(BirthDeclaration.class);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {

        // create schemas
        try {
            SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                new ClassPathResource("create_schemas.sql"), false);
            logger.info("Created the schemas : COMMON, CRS, PRS");
        } catch (Exception ignore) {
        }

        String fileName = generateSchemaFromHibernate(Dialect.DERBY);
        SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
            new FileSystemResource(fileName), false);
        logger.info("Created tables using generated script : " + fileName);

        // populate with initial data
        SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
            new ClassPathResource("populate_database.sql"), false);
        logger.info("Populated the tables with initial data from : populate_database.sql");

        // populate with sample data
        SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
            new ClassPathResource("populate_sample_data.sql"), false);
        logger.info("Populated the tables with sample data from : populate_sample_data.sql");

        Map<String, PreloadableDAO> preloadableDaos = ctx.getBeansOfType(PreloadableDAO.class);
        for (PreloadableDAO dao : preloadableDaos.values()) {
            dao.preload();
        }
        logger.debug("Preloaded all PreloadableDAO instances");

        // perform additional initialization with Java code
        additionalInitialization(ctx);
    }

    private void additionalInitialization(ApplicationContext ctx) {
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
        BirthDeclarationDAO birthDeclarationdao = (BirthDeclarationDAO) ctx.getBean("birthDeclarationDAOImpl", BirthDeclarationDAO.class);
        BDDivisionDAO bdDivisionDao = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);

        try {
            BirthDeclaration bdf = new BirthDeclaration();
            bdf.getChild().setBirthDivision(bdDivisionDao.getBDDivision(11, 1));
            bdf.getChild().setBdfSerialNo("A112");
            bdf.getChild().setChildFullNameEnglish("Amith Sampath Jayasekara");
            bdf.getChild().setChildFullNameOfficialLang("අමිත් සමිපත් ජයසේකර");
            bdf.getChild().setDateOfBirth(dfm.parse("2010-02-26"));
            bdf.getChild().setDateOfRegistration(dfm.parse("2010-03-01"));
            bdf.getChild().setChildGender(AppConstants.GENDER_FEMALE);
            bdf.getChild().setStatus(1);
            bdf.getInformant().setInformantName("The name of the informant");
            bdf.getNotifyingAuthority().setNotifyingAuthorityPIN("1222233453");
            bdf.getNotifyingAuthority().setNotifyingAuthorityName("The name of the NA");
            birthDeclarationdao.addBirthDeclaration(bdf);

            bdf = new BirthDeclaration();
            bdf.getChild().setBirthDivision(bdDivisionDao.find(11, 1));
            bdf.getChild().setBdfSerialNo("A113");
            bdf.getChild().setChildFullNameEnglish("Baby A113 name in English");
            bdf.getChild().setChildFullNameOfficialLang("A113 බබාගේ නම සිංහලෙන්");
            bdf.getChild().setDateOfBirth(dfm.parse("2010-02-23"));
            bdf.getChild().setDateOfRegistration(dfm.parse("2010-03-02"));
            bdf.getChild().setChildGender(AppConstants.GENDER_MALE);
            bdf.getChild().setStatus(1);
            bdf.getInformant().setInformantName("The name of the informant");
            bdf.getNotifyingAuthority().setNotifyingAuthorityPIN("1222233453");
            bdf.getNotifyingAuthority().setNotifyingAuthorityName("The name of the NA");
            birthDeclarationdao.addBirthDeclaration(bdf);

            bdf = new BirthDeclaration();
            bdf.getChild().setBirthDivision(bdDivisionDao.find(11, 1));
            bdf.getChild().setBdfSerialNo("A114");
            bdf.getChild().setChildFullNameEnglish("Baby A114 name in English");
            bdf.getChild().setChildFullNameOfficialLang("A114 බබාගේ නම සිංහලෙන්");
            bdf.getChild().setDateOfBirth(dfm.parse("2010-01-12"));
            bdf.getChild().setDateOfRegistration(dfm.parse("2010-02-01"));
            bdf.getChild().setChildGender(AppConstants.GENDER_MALE);
            bdf.getChild().setStatus(5);
            bdf.getInformant().setInformantName("The name of the informant");
            bdf.getNotifyingAuthority().setNotifyingAuthorityPIN("1222233453");
            bdf.getNotifyingAuthority().setNotifyingAuthorityName("The name of the NA");
            bdf.getConfirmant().setConfirmationReceiveDate(dfm.parse("2010-02-17"));
            birthDeclarationdao.addBirthDeclaration(bdf);

            bdf = new BirthDeclaration();
            bdf.getChild().setBirthDivision(bdDivisionDao.find(11, 1));
            bdf.getChild().setBdfSerialNo("A115");
            bdf.getChild().setChildFullNameEnglish("Chathuranaga Gihan Chandimal Withana");
            bdf.getChild().setChildFullNameOfficialLang("චතුරංග ගිහාන් චන්දිමාල් විතාන");
            bdf.getChild().setDateOfBirth(dfm.parse("2010-02-21"));
            bdf.getChild().setDateOfRegistration(dfm.parse("2010-02-22"));
            bdf.getChild().setChildGender(AppConstants.GENDER_MALE);
            bdf.getChild().setStatus(2);
            bdf.getInformant().setInformantName("The name of the informant");
            bdf.getNotifyingAuthority().setNotifyingAuthorityPIN("1222233453");
            bdf.getNotifyingAuthority().setNotifyingAuthorityName("The name of the NA");
            birthDeclarationdao.addBirthDeclaration(bdf);

            bdf = new BirthDeclaration();
            bdf.getChild().setBirthDivision(bdDivisionDao.find(11, 1));
            bdf.getChild().setBdfSerialNo("A116");
            bdf.getChild().setChildFullNameEnglish("Baby A116 name in English");
            bdf.getChild().setChildFullNameOfficialLang("A116 බබාගේ නම සිංහලෙන්");
            bdf.getChild().setDateOfBirth(dfm.parse("2010-02-12"));
            bdf.getChild().setDateOfRegistration(dfm.parse("2010-02-13"));
            bdf.getChild().setChildGender(AppConstants.GENDER_MALE);
            bdf.getChild().setStatus(5);
            bdf.getInformant().setInformantName("The name of the informant");
            bdf.getNotifyingAuthority().setNotifyingAuthorityPIN("1222233453");
            bdf.getNotifyingAuthority().setNotifyingAuthorityName("The name of the NA");
            bdf.getConfirmant().setConfirmationReceiveDate(dfm.parse("2010-02-16"));
            birthDeclarationdao.addBirthDeclaration(bdf);

            bdf = new BirthDeclaration();
            bdf.getChild().setBirthDivision(bdDivisionDao.find(11, 1));
            bdf.getChild().setBdfSerialNo("A117");
            bdf.getChild().setChildFullNameEnglish("Baby A117 name in English");
            bdf.getChild().setChildFullNameOfficialLang("A117 බබාගේ නම සිංහලෙන්");
            bdf.getChild().setDateOfBirth(dfm.parse("2010-02-11"));
            bdf.getChild().setDateOfRegistration(dfm.parse("2010-02-12"));
            bdf.getChild().setChildGender(AppConstants.GENDER_MALE);
            bdf.getChild().setStatus(5);
            bdf.getInformant().setInformantName("The name of the informant");
            bdf.getNotifyingAuthority().setNotifyingAuthorityPIN("1222233445");
            bdf.getNotifyingAuthority().setNotifyingAuthorityName("The name of the NA");
            bdf.getConfirmant().setConfirmationReceiveDate(dfm.parse("2010-02-15"));
            birthDeclarationdao.addBirthDeclaration(bdf);

            bdf = new BirthDeclaration();
            bdf.getChild().setBirthDivision(bdDivisionDao.find(11, 1));
            bdf.getChild().setBdfSerialNo("A118");
            bdf.getChild().setChildFullNameEnglish("Baby A118 name in English");
            bdf.getChild().setChildFullNameOfficialLang("A118 බබාගේ නම සිංහලෙන්");
            bdf.getChild().setDateOfBirth(dfm.parse("2010-05-11"));
            bdf.getChild().setDateOfRegistration(dfm.parse("2010-05-12"));
            bdf.getChild().setChildGender(AppConstants.GENDER_MALE);
            bdf.getChild().setStatus(5);
            bdf.getInformant().setInformantName("The name of the informant");
            bdf.getNotifyingAuthority().setNotifyingAuthorityPIN("1222234545");
            bdf.getNotifyingAuthority().setNotifyingAuthorityName("The name of the NA");
            bdf.getConfirmant().setConfirmationReceiveDate(dfm.parse("2010-05-15"));
            birthDeclarationdao.addBirthDeclaration(bdf);


        } catch (Exception e) {
            logger.error("Error populating database with sample BDFs", e);
        }
        logger.info("Initialized the test database by creating the schema and executing populate_database.sql");
    }

    // for testing
    public static void main(String[] args) {
        new DatabaseInitializer().generateSchemaFromHibernate(Dialect.DERBY);
    }

    private String generateSchemaFromHibernate(Dialect dialect) {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        cfg.setProperty("hibernate.hbm2ddl.auto", "create");

        for (Class<Object> clazz : entityClasses) {
            cfg.addAnnotatedClass(clazz);
        }
        cfg.setProperty("hibernate.dialect", dialect.getDialectClass());

        SchemaExport export = new SchemaExport(cfg);
        export.setDelimiter(";");
        export.setFormat(true);
        String fileName = System.getProperty("java.io.tmpdir") + File.separator + dialect.name().toLowerCase() + ".sql";
        export.setOutputFile(fileName);
        export.execute(false, false, false, true /* Set to false to create drop table DDL*/);
        return fileName;
    }

    private static enum Dialect {
        DERBY("org.hibernate.dialect.DerbyDialect"),
        MYSQL("org.hibernate.dialect.MySQLDialect");

        private String dialectClass;

        private Dialect(String dialectClass) {
            this.dialectClass = dialectClass;
        }

        public String getDialectClass() {
            return dialectClass;
        }
    }
}
