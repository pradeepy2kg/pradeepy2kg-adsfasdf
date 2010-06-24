package lk.rgd;

import junit.framework.TestCase;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.NumericUtils;
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
        try {
            GenericApplicationContext ctx = new GenericApplicationContext();
            XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
            xmlReader.loadBeanDefinitions(new ClassPathResource("unitTest_applicationContext.xml"));
            ctx.refresh();

            // create Lucene indexes
            createTestLuceneIndex();
            return ctx;

        } catch (Exception e) {
            logger.error("Exception starting Spring for unit-test backend", e);
            throw new IllegalStateException("Couldn't start Spring...", e);
        }
    }

    private static void createTestLuceneIndex() {

        logger.info("Creating Lucene indexes ...");
        
        // delete any existing index
        File f = new File("/tmp/index/person");
        if (f.exists()) {
            f.delete();
        }
        f.mkdirs();

        try {
            Connection c = DriverManager.getConnection("jdbc:derby:memory:unit-testing-jpa", "epop", "epop");
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select * from PRS.PERSON");

            IndexWriter writer = new IndexWriter(
                FSDirectory.open(new File("/tmp/index/person")),
                new WhitespaceAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);

            long start = System.currentTimeMillis();
            int count = 0;

            while (rs.next()) {

                Document d = new Document();
                d.add(new Field("personUKey", rs.getString("personUKey"), Field.Store.YES, Field.Index.NO));
                final Date dateOfBirth = rs.getDate("dateOfBirth");
                if (dateOfBirth != null) {
                    d.add(new Field("dateOfBirth", DateTools.dateToString(dateOfBirth, DateTools.Resolution.DAY), Field.Store.YES, Field.Index.ANALYZED));
                }
                d.add(new Field("fullNameInEnglishLanguage", rs.getString("fullNameInEnglishLanguage"), Field.Store.YES, Field.Index.ANALYZED));
                d.add(new Field("fullNameInOfficialLanguage", rs.getString("fullNameInOfficialLanguage"), Field.Store.YES, Field.Index.ANALYZED));
                d.add(new Field("gender", NumericUtils.intToPrefixCoded(rs.getInt("gender")), Field.Store.YES, Field.Index.ANALYZED));
                d.add(new Field("lastNameInEnglish", rs.getString("lastNameInEnglish"), Field.Store.YES, Field.Index.ANALYZED));
                d.add(new Field("lastNameInOfficialLanguage", rs.getString("lastNameInOfficialLanguage"), Field.Store.YES, Field.Index.ANALYZED));
                final String nic = rs.getString("nic");
                if (nic != null) {
                    d.add(new Field("nic", nic, Field.Store.YES, Field.Index.ANALYZED));
                }
                final String pin = rs.getString("pin");
                if (pin != null) {
                    d.add(new Field("pin", pin, Field.Store.YES, Field.Index.ANALYZED));
                }
                final String placeOfBirth = rs.getString("placeOfBirth");
                if (placeOfBirth != null) {
                    d.add(new Field("placeOfBirth", placeOfBirth, Field.Store.YES, Field.Index.ANALYZED));
                }
                writer.addDocument(d);
                count++;
            }

            System.out.println("Indexed : " + count + " documents in : " + (System.currentTimeMillis() - start) + " ms");

            writer.optimize();
            writer.close();

            rs.close();
            c.close();
            
        } catch (Exception e) {
            logger.error("Error indexing the Person records of the PRS", e);
            throw new RuntimeException("Error indexing the Person records of the PRS", e);
        }
    }
}
