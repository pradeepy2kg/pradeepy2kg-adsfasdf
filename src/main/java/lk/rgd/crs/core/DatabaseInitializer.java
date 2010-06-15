package lk.rgd.crs.core;

import lk.rgd.Permission;
import lk.rgd.common.api.dao.RoleDAO;
import lk.rgd.common.api.domain.*;
import lk.rgd.common.core.dao.PreloadableDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Marriage;
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
public class
        DatabaseInitializer implements ApplicationContextAware {

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

        entityClasses.add(Address.class);
        entityClasses.add(Person.class);
        entityClasses.add(Marriage.class);
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

        try {
            String fileName = generateSchemaFromHibernate(Dialect.DERBY);
            SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                    new FileSystemResource(fileName), false);
            logger.info("Created tables using generated script : " + fileName);


            // populate with initial data
            //SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
            //    new ClassPathResource("populate_database.sql"), false);
            //logger.info("Populated the tables with initial data from : populate_database.sql");

            // populate with sample data
            SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
                    new ClassPathResource("populate_sample_data.sql"), false);
            logger.info("Populated the tables with sample data from : populate_sample_data.sql");
        } catch (Exception e) {
            logger.info("Skipped creation and population of the database as it exists..", e);
        }

        Map<String, PreloadableDAO> preloadableDaos = ctx.getBeansOfType(PreloadableDAO.class);
        for (PreloadableDAO dao : preloadableDaos.values()) {
            dao.preload();
        }
        logger.debug("Preloaded all PreloadableDAO instances");

        // perform additional initialization with Java code
        additionalInitialization(ctx);
    }

    private void additionalInitialization(ApplicationContext ctx) {
        try {
            // ---------------- populate permissions ---------------------
            RoleDAO roleDao = (RoleDAO) ctx.getBean("roleDAOImpl", RoleDAO.class);

            Role deoRole = roleDao.getRole("DEO");
            BitSet bs = new BitSet();
            // TODO add any DR specific permissions
            deoRole.setPermBitSet(bs);
            roleDao.save(deoRole);

            Role adrRole = roleDao.getRole("ADR");
            bs = new BitSet();
            bs.or(deoRole.getPermBitSet());
            bs.set(Permission.APPROVE_BDF);
            bs.set(Permission.EDIT_BDF);
            bs.set(Permission.BIRTH_CONFIRMATION_PAGE);
            bs.set(Permission.BIRTH_CONFIRMATION_PRINT_PAGE);
            bs.set(Permission.BIRTH_CONFIRMATION_REPORT_PAGE);
            bs.set(Permission.BIRTH_REGISTRATION_APPROVAL_PAGE);
            bs.set(Permission.BIRTH_REGISTRATON_PAGE);
            bs.set(Permission.USER_PREFERANCE_SELECT_PAGE);
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
            bs.or(rgRole.getPermBitSet());
            bs.set(Permission.CREATE_USER_PAGE);
			bs.set(Permission.CREATE_USER);
            bs.set(Permission.VIEW_USERS);
            // TODO add any ADMIN specific permissions
            adminRole.setPermBitSet(bs);
            roleDao.save(adminRole);

            logger.info("Initialized the test database by creating the schema and executing populate_database.sql");
        } catch (Exception ignore) {
        }
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
