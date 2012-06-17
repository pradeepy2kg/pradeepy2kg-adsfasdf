package lk.rgd.common;

import lk.rgd.UnitTestManager;
import org.apache.struts2.StrutsSpringTestCase;
import org.springframework.web.context.WebApplicationContext;

/**
 * Extend this class to create Struts test cases, and the Spring, Database and all other initialization will
 * happen only once
 *
 * @author asankha
 */
public abstract class CustomStrutsTestCase extends StrutsSpringTestCase {

    public CustomStrutsTestCase() {
        applicationContext = UnitTestManager.ctx;
    }

    @Override
    protected void setupBeforeInitDispatcher() throws Exception {
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
    }
}
