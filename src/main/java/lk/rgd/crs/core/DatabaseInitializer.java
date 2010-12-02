package lk.rgd.crs.core;

import lk.rgd.common.api.domain.*;
import lk.rgd.common.core.dao.PreloadableDAO;
import lk.rgd.common.util.RolePermissionUtils;
import lk.rgd.crs.api.domain.*;
import lk.rgd.prs.api.domain.*;
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
import java.util.ArrayList;
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

    private DataSource dataSource;
    private boolean createCleanDB = true;

    private static final List<Class> entityClasses = new ArrayList<Class>();

    static {
        entityClasses.add(AppParameter.class);
        entityClasses.add(Country.class);
        entityClasses.add(District.class);
        entityClasses.add(BDDivision.class);
        entityClasses.add(DSDivision.class);
        entityClasses.add(MRDivision.class);
        entityClasses.add(Event.class);
        entityClasses.add(Race.class);
        entityClasses.add(Role.class);
        entityClasses.add(User.class);
        entityClasses.add(BirthDeclaration.class);
        entityClasses.add(CertificateSearch.class);
        entityClasses.add(AdoptionOrder.class);
        entityClasses.add(Registrar.class);
        entityClasses.add(Assignment.class);
        entityClasses.add(Court.class);
        entityClasses.add(Location.class);
        entityClasses.add(UserLocation.class);

        entityClasses.add(Address.class);
        entityClasses.add(Person.class);
        entityClasses.add(Marriage.class);
        entityClasses.add(PINNumber.class);
        entityClasses.add(PersonCitizenship.class);

        entityClasses.add(DeathRegister.class);

        entityClasses.add(BirthAlteration.class);
        entityClasses.add(DeathAlteration.class);
        entityClasses.add(MarriageRegister.class);
        entityClasses.add(Witness.class);
        entityClasses.add(MarriageObjection.class);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setCreateCleanDB(boolean createCleanDB) {
        this.createCleanDB = createCleanDB;
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        logger.info("Starting the database initialization..");

        boolean mysql = false;

        // detect the target DB 
        EntityManagerFactoryInfo emf = (EntityManagerFactoryInfo) ctx.getBean("entityManagerFactory");
        if ("org.hibernate.dialect.MySQLDialect".equals(emf.getPersistenceUnitInfo().getProperties().
            getProperty("hibernate.dialect"))) {
            mysql = true;
            logger.debug("Detected MySQL as the target database");
        }

        // decide if DB is to be recreated
        boolean recreateDb = false;
        if (Boolean.getBoolean(USE_NW_DERBY)) {
            logger.debug("Networked Derby selected as the target database..");
            try {
                SimpleJdbcTestUtils.countRowsInTable(new SimpleJdbcTemplate(dataSource), "PRS.PERSON");
            } catch (Exception ignore) {
                recreateDb = true;
            }
        } else {
            recreateDb = !mysql || createCleanDB;    // recreate for derby or for MySQL when explicitly requested
        }

        if (recreateDb || Boolean.getBoolean("recreatedb")) {
            logger.info("Recreating a new database..");
            recreateCleanDB(mysql);
            // perform additional initialization with Java code
            RolePermissionUtils.setPermissionBits(ctx);
            //   additionalInitialization(ctx);
        }

        Map<String, PreloadableDAO> preloadableDaos = ctx.getBeansOfType(PreloadableDAO.class);
        for (PreloadableDAO dao : preloadableDaos.values()) {
            dao.preload();
        }
        logger.info("Pre-loaded master tables ... Application initialized!");

//        BirthRecordsIndexer birthIndexer = (BirthRecordsIndexer) ctx.getBean("birthRecordsIndexer");
//        birthIndexer.indexAll();
//        DeathRecordsIndexer deathIndexer = (DeathRecordsIndexer) ctx.getBean("deathRecordsIndexer");
//        deathIndexer.indexAll();
        //logger.info("Re-indexed DB contents...");
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
            // populate with sample data
            SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                new ClassPathResource("database/populate_sample_data.sql"), false);
            logger.info("Populated the tables with sample data from : populate_sample_data.sql");

            // populate with migrated data
            if (mysql || Boolean.getBoolean("fulldb")) {
                SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                    new ClassPathResource("database/populate_migrated_data.sql"), false);
                logger.info("Populated the tables with sample data from : populate_migrated_data.sql");
            }

            SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                new ClassPathResource("database/populate_sample_prs.sql"), false);
            logger.info("Populated the tables with sample data from : populate_sample_prs.sql");

        } catch (Exception e) {
            logger.error("Error populating the database with initial data from : populate_sample_data/crs/prs.sql", e);
            throw new IllegalStateException("Could not initialize the database. See log for details", e);
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
