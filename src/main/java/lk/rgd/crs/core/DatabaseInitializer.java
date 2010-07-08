package lk.rgd.crs.core;

import lk.rgd.Permission;
import lk.rgd.common.api.dao.RoleDAO;
import lk.rgd.common.api.domain.*;
import lk.rgd.common.core.dao.PreloadableDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Marriage;
import lk.rgd.prs.api.domain.PINNumber;
import lk.rgd.prs.api.domain.Person;
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
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
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

    public static final String USE_NW_DERBY = "nwderby";
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");

    private DataSource dataSource;
    private boolean createCleanDB = true;

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

        entityClasses.add(Address.class);
        entityClasses.add(Person.class);
        entityClasses.add(Marriage.class);
        entityClasses.add(PINNumber.class);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setCreateCleanDB(boolean createCleanDB) {
        this.createCleanDB = createCleanDB;
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {

        boolean mysql = false;

        // detect the target DB
        EntityManagerFactoryInfo emf = (EntityManagerFactoryInfo) ctx.getBean("entityManagerFactory");
        if ("org.hibernate.dialect.MySQLDialect".equals(emf.getPersistenceUnitInfo().getProperties().
            getProperty("hibernate.dialect"))) {
            mysql = true;
        }

        // decide if DB is to be recreated
        boolean recreateDb = false;
        if (Boolean.getBoolean(USE_NW_DERBY)) {
            try {
                SimpleJdbcTestUtils.countRowsInTable(new SimpleJdbcTemplate(dataSource), "PRS.PERSON");
            } catch (Exception ignore) {
                recreateDb = true;
            }
        } else {
            recreateDb = !mysql || createCleanDB;    // recreate for derby or for MySQL when explicitly requested
        }

        if (recreateDb) {
            recreateCleanDB(mysql);
            // perform additional initialization with Java code
            additionalInitialization(ctx);
        }

        Map<String, PreloadableDAO> preloadableDaos = ctx.getBeansOfType(PreloadableDAO.class);
        for (PreloadableDAO dao : preloadableDaos.values()) {
            dao.preload();
        }
        logger.info("Pre-loaded master tables ...");
    }

    private void recreateCleanDB(boolean mysql) {

        System.out.println("\n**********          **********          **********          **********          **********\n");
        logger.info("Re-creating a clean Database ...");

        // drop mysql databases if they exist
        if (mysql) {
            try {
                SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                    new ClassPathResource("database/drop_mysql_databases.sql"), false);
                logger.info("Drop existing MySQL databases : COMMON, CRS, PRS");
            } catch (Exception ignore) {
                logger.warn("Exception while dropping existing MySQL databases", ignore);
            }
        }

        // create schemas (derby) or databases (mysql) - common, crs and prs
        if (mysql) {
            try {
                SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                    new ClassPathResource("database/create_mysql_databases.sql"), false);
                logger.info("Created MySQL databases : COMMON, CRS, PRS");
            } catch (Exception e) {
                logger.error("Error creating MySQL databases - COMMON, CRS and PRS", e);
                throw new IllegalStateException("Could not create MySQL databases. See log for details", e);
            }
        } else {
            try {
                SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                    new ClassPathResource("database/create_schemas.sql"), false);
                logger.info("Created the schemas : COMMON, CRS, PRS");
            } catch (Exception ignore) {
            }
        }

        // generate schema creation and drop script
        String[] fileName = null;
        if (mysql) {
            fileName = generateSchemaFromHibernate(Dialect.MYSQL);
        } else {
            fileName = generateSchemaFromHibernate(Dialect.DERBY);
        }

        // drop tables if any exist
        if (!mysql) {
            try {
                SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                    new FileSystemResource(fileName[1]), false);
                logger.info("Drop existing tables using generated script : " + fileName[1]);
            } catch (Exception e) {
                logger.debug("Exception while dropping existing tables using script : " + fileName[1]);
            }
        }

        // create tables
        try {
            SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                new FileSystemResource(fileName[0]), false);
            logger.info("Created tables using generated script : " + fileName[0]);

        } catch (Exception e) {
            logger.error("Error creating tables", e);
            throw new IllegalStateException("Could not create tables. See log for details", e);
        }

        try {
            // populate with initial data
            //SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
            //    new ClassPathResource("database/populate_database.sql"), false);
            //logger.info("Populated the tables with initial data from : populate_database.sql");

            // populate with sample data
            SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                new ClassPathResource("database/populate_sample_data.sql"), false);
            logger.info("Populated the tables with sample data from : populate_sample_data.sql");

            SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                new ClassPathResource("database/populate_sample_crs.sql"), false);
            logger.info("Populated the tables with sample data from : populate_sample_crs.sql");

            SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                new ClassPathResource("database/populate_sample_prs.sql"), false);
            logger.info("Populated the tables with sample data from : populate_sample_prs.sql");

        } catch (Exception e) {
            logger.error("Error populating the database with initial data from : populate_sample_data/crs/prs.sql", e);
            throw new IllegalStateException("Could not initialize the database. See log for details", e);
        }
    }

    private void additionalInitialization(ApplicationContext ctx) {
        try {
            // ---------------- populate permissions ---------------------
            RoleDAO roleDao = (RoleDAO) ctx.getBean("roleDAOImpl", RoleDAO.class);

            Role deoRole = roleDao.getRole("DEO");
            BitSet bs = new BitSet();
            // TODO add any DEO specific permissions
            bs.set(Permission.PRS_LOOKUP_PERSON_BY_KEYS);
            bs.set(Permission.PRS_ADD_PERSON);
            deoRole.setPermBitSet(bs);
            roleDao.save(deoRole);

            Role adrRole = roleDao.getRole("ADR");
            bs = new BitSet();
            bs.or(deoRole.getPermBitSet());
            bs.set(Permission.APPROVE_BDF);
            bs.set(Permission.APPROVE_BDF_CONFIRMATION);
            bs.set(Permission.EDIT_BDF);
            bs.set(Permission.PAGE_BIRTH_CONFIRMATION);
            bs.set(Permission.PAGE_BIRTH_CONFIRMATION_PRINT);
            bs.set(Permission.PAGE_BIRTH_CONFIRMATION_REPORT);
            bs.set(Permission.PAGE_BIRTH_REGISTRATION_APPROVAL);
            bs.set(Permission.PAGE_BIRTH_REGISTRATON);
            bs.set(Permission.PAGE_USER_PREFERANCE_SELECT);
            bs.set(Permission.PAGE_BIRTH_CONFIRMATION_SEARCH);
            bs.set(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL);
            bs.set(Permission.PAGE_BIRTH_CERTIFICATE_PRINT);
            bs.set(Permission.PAGE_STILL_BIRTH_REGISTRATION);
            adrRole.setPermBitSet(bs);
            roleDao.save(adrRole);

            Role drRole = roleDao.getRole("DR");
            bs = new BitSet();
            bs.or(adrRole.getPermBitSet());
            // TODO add any DR specific permissions
            drRole.setPermBitSet(bs);
            roleDao.save(drRole);

            Role argRole = roleDao.getRole("ARG");
            bs = new BitSet();
            bs.or(drRole.getPermBitSet());
            // TODO add any ARG specific permissions
            argRole.setPermBitSet(bs);
            roleDao.save(argRole);

            Role rgRole = roleDao.getRole("RG");
            bs = new BitSet();
            bs.or(argRole.getPermBitSet());
            // TODO add any RG specific permissions
            rgRole.setPermBitSet(bs);
            roleDao.save(rgRole);

            Role adminRole = roleDao.getRole("ADMIN");
            bs = new BitSet();
            //        bs.or(rgRole.getPermBitSet());
            bs.set(Permission.PAGE_CREATE_USER);
            bs.set(Permission.USER_MANAGEMENT);
            bs.set(Permission.PAGE_VIEW_USERS);
            bs.set(Permission.PAGE_USER_PREFERANCE_SELECT);
            // TODO add any ADMIN specific permissions
            adminRole.setPermBitSet(bs);
            roleDao.save(adminRole);

            logger.info("Initialized the database by performing permission initialization");
            System.out.println("\n**********          **********          **********          **********          **********\n");
        } catch (Exception e) {
            logger.error("Error initializing role permissions on the database");
            throw new IllegalStateException("Error initializing role permissions. See log for details", e);
        }
    }

    // for testing
    public static void main(String[] args) {
        new DatabaseInitializer().generateSchemaFromHibernate(Dialect.DERBY);
        new DatabaseInitializer().generateSchemaFromHibernate(Dialect.MYSQL);
    }

    private String[] generateSchemaFromHibernate(Dialect dialect) {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        cfg.setProperty("hibernate.hbm2ddl.auto", "create");

        for (Class<Object> clazz : entityClasses) {
            cfg.addAnnotatedClass(clazz);
        }
        if (Dialect.MYSQL.equals(dialect)) {
            cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        } else {
            cfg.setProperty("hibernate.dialect", dialect.getDialectClass());
        }

        SchemaExport export = new SchemaExport(cfg);
        export.setDelimiter(";");
        export.setFormat(true);

        String[] fileName = new String[2];
        fileName[0] = System.getProperty("java.io.tmpdir") + File.separator + "create_" + dialect.name().toLowerCase() + ".sql";
        export.setOutputFile(fileName[0]);
        export.execute(false, false, false, true /* Set to false to create drop table DDL*/);

        fileName[1] = System.getProperty("java.io.tmpdir") + File.separator + "drop_" + dialect.name().toLowerCase() + ".sql";
        export.setOutputFile(fileName[1]);
        export.execute(false, false, true, false);
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
