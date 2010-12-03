package lk.rgd;

import junit.framework.TestCase;
import lk.rgd.crs.core.DatabaseInitializer;
//import org.apache.lucene.analysis.WhitespaceAnalyzer;
//import org.apache.lucene.document.DateTools;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.util.NumericUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.sql.*;

/**
 * The unit tests are dependent on the availability of a deterministic database. This test suite ensures proper
 * DB initialization, and then invokes other unit test cases
 *
 * @author asankha
 */
public class UnitTestManager extends TestCase {                     

    private static final Logger logger = LoggerFactory.getLogger(UnitTestManager.class);
    public static final ApplicationContext ctx = getApplicationContext();

    private static ApplicationContext getApplicationContext() {

        logger.info("Creating Application Context for testing... Use NW Derby : " +
            Boolean.getBoolean(DatabaseInitializer.USE_NW_DERBY));

        try {
            GenericApplicationContext ctx = new GenericApplicationContext();
            XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
            if (Boolean.getBoolean(DatabaseInitializer.USE_NW_DERBY)) {
                logger.info("**** **** **** Using existing database for unit tests **** **** ****");
                xmlReader.loadBeanDefinitions(new ClassPathResource("unitTestKeepDB_applicationContext.xml"));
            } else {
                xmlReader.loadBeanDefinitions(new ClassPathResource("unitTest_applicationContext.xml"));
            }
            ctx.refresh();
            return ctx;

        } catch (Exception e) {
            logger.error("Exception starting Spring for unit-test backend", e);
            throw new IllegalStateException("Couldn't start Spring...", e);
        }
    }
}
