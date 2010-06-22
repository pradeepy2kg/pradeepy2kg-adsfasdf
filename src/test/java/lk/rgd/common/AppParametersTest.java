package lk.rgd.common;

import junit.framework.TestCase;
import lk.rgd.UnitTestSuite;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.crs.api.BirthConstants;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;

/**
 * @author asankha
 */
public class AppParametersTest extends TestCase {

    private final ApplicationContext ctx = UnitTestSuite.ctx;

    public void testAppParameters() throws Exception {
        AppParametersDAO bean = (AppParametersDAO) ctx.getBean("appParametersDAOImpl", AppParametersDAO.class);
        Assert.assertEquals(bean.getIntParameter(BirthConstants.CRS_BIRTH_LATE_REG_DAYS), 90);

        bean.setIntParameter("test_int", 3);
        Assert.assertEquals(bean.getIntParameter("test_int"), 3);

        bean.setIntParameter("test_int", 4);
        Assert.assertEquals(bean.getIntParameter("test_int"), 4);

        bean.setStringParameter("test_string", "hello");
        Assert.assertEquals(bean.getStringParameter("test_string"), "hello");
    }
}
