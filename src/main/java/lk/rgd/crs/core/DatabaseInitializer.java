package lk.rgd.crs.core;

import lk.rgd.Permission;
import lk.rgd.common.api.dao.RoleDAO;
import lk.rgd.common.api.domain.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

import javax.sql.DataSource;
import java.util.BitSet;

/**
 * Initializes an in-memory database for testing and populates it with sample/initial data
 *
 * @author asankha
 */
public class DatabaseInitializer implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(dataSource),
            new ClassPathResource("test_database.sql"), false);
        logger.info("Initialized the test database by executing test_database.sql");

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
    }
}
