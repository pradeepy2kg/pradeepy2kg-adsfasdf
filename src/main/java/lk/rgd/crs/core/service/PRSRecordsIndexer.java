package lk.rgd.crs.core.service;

import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.core.index.SolrIndexManager;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.prs.api.dao.PersonDAO;
import lk.rgd.prs.api.domain.Person;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author asankha
 */
public class PRSRecordsIndexer {

    private static final String COLON = ":";
    private static final String SPACE = " ";
    private static final String QUOTE = "\"";
    private static final String QUOTE_TILDE_3 = "\"~3";
    private static final String QUOTE_TILDE_12 = "\"~12";
    private static final String AND = " AND ";
    private static final String FIELD_FULL_NAME_ENGLISH = "fullNameInEnglishLanguage";
    private static final String FIELD_FULL_NAME_OFFICIAL_LANG = "fullNameInOfficialLanguage";
    private static final String FIELD_DATE_OF_BIRTH = "dateOfBirth";
    private static final String FIELD_GENDER = "gender";
    private static final String FIELD_PIN = "pin";
    private static final String FIELD_NIC = "nic";
    private static final String FIELD_PERSON_UKEY = "personUKey";

    private static final Logger logger = LoggerFactory.getLogger(PRSRecordsIndexer.class);

    private final SolrIndexManager solrIndexManager;
    private final PersonDAO personDao;
    private final AppParametersDAO appParametersDAO;

    public PRSRecordsIndexer(SolrIndexManager solrIndexManager, PersonDAO personDao, AppParametersDAO appParametersDAO) {
        this.solrIndexManager = solrIndexManager;
        this.personDao = personDao;
        this.appParametersDAO = appParametersDAO;
    }

    public boolean indexAll() {

        if (solrIndexManager.getPRSServer() == null) {
            logger.error("Cannot connect to Solr server to index all PRS records");
            return false;
        }

        // delete all existing
        try {
            logger.info("Deleting all PRS records off Solr index");
            solrIndexManager.getPRSServer().deleteByQuery("*:*");
        } catch (Exception e) {
            logger.error("Error deleting existing PRS records off Solr index");
        }

        List<Person> personList = personDao.findAll();

        int count = 0;
        try {
            logger.info("Begin re-indexing of all PRS records into Solr");
            for (Person person : personList) {
                addRecord(person);
                count++;
                if (count % 10000 == 0) {
                    logger.info("Indexed : {} records..", count);
                }
            }

            solrIndexManager.getPRSServer().optimize();
            solrIndexManager.getPRSServer().commit();

            logger.debug("Successfully indexed : " + count + " PRS records..");
            return true;

            // TODO we do not print the stack trace for now..
        } catch (SolrServerException e) {
            logger.error("Error from Solr server during PRS record re-indexing", e);
        } catch (IOException e) {
            logger.error("IO Exception encountered during PRS record re-indexing", e);
        } catch (Exception e) {
            logger.error("Unexpected Exception encountered during PRS record re-indexing", e);
        }
        return false;
    }

    public void addRecord(Person p) throws IOException, SolrServerException {

        SolrInputDocument d = new SolrInputDocument();

        d.addField(FIELD_PERSON_UKEY, p.getPersonUKey());
        d.addField(FIELD_FULL_NAME_ENGLISH, p.getFullNameInEnglishLanguage());
        d.addField(FIELD_FULL_NAME_OFFICIAL_LANG, p.getFullNameInOfficialLanguage());
        //d.addField(FIELD_GENDER, GenderUtil.getGenderString(p.getGender()));
        d.addField(FIELD_GENDER, p.getGender());
        d.addField(FIELD_NIC, p.getNic());
        d.addField(FIELD_PIN, p.getPin());
        d.addField(FIELD_DATE_OF_BIRTH, p.getDateOfBirth());
        
        solrIndexManager.getPRSServer().add(d);
    }
}
