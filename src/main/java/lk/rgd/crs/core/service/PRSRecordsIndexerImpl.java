package lk.rgd.crs.core.service;

import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.core.index.SolrIndexManager;
import lk.rgd.common.util.CivilStatusUtil;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.common.util.LifeStatusUtil;
import lk.rgd.crs.api.service.PRSRecordsIndexer;
import lk.rgd.prs.api.dao.PersonDAO;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.domain.PersonCitizenship;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * @author asankha
 */
public class PRSRecordsIndexerImpl implements PRSRecordsIndexer {

    private static final String COLON = ":";
    private static final String SPACE = " ";
    private static final String QUOTE = "\"";
    private static final String QUOTE_TILDE_3 = "\"~3";
    private static final String QUOTE_TILDE_12 = "\"~12";
    private static final String AND = " AND ";
    private static final String SRI_LANKA = "LK";
    private static final String FIELD_FULL_NAME_ENGLISH = "fullNameInEnglishLanguage";
    private static final String FIELD_FULL_NAME_OFFICIAL_LANG = "fullNameInOfficialLanguage";
    private static final String FIELD_ALL_NAMES = "allNames";
    private static final String FIELD_PLACE_OF_BIRTH = "placeOfBirth";
    private static final String FIELD_DATE_OF_BIRTH = "dateOfBirth";
    private static final String FIELD_DATE_OF_DEATH = "dateOfDeath";
    private static final String FIELD_GENDER = "gender";
    private static final String FIELD_CITIZENSHIP = "citizenship";
    private static final String FIELD_LAST_ADDRESS = "lastAddress";
    private static final String FIELD_ALL_ADDRESSES = "allAddresses";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_PHONE = "phone";
    private static final String FIELD_PIN = "pin";
    private static final String FIELD_NIC = "nic";
    private static final String FIELD_LIFE_STATUS = "lifeStatus";
    private static final String FIELD_CIVIL_STATUS = "civilStatus";
    private static final String FIELD_RECORD_STATUS = "recordStatus";
    private static final String FIELD_PASSPORT = "passport";
    private static final String FIELD_PERSON_UKEY = "personUKey";

    private static final Logger logger = LoggerFactory.getLogger(PRSRecordsIndexerImpl.class);

    private final SolrIndexManager solrIndexManager;
    private final PersonDAO personDao;
    private final AppParametersDAO appParametersDAO;

    public PRSRecordsIndexerImpl(SolrIndexManager solrIndexManager, PersonDAO personDao, AppParametersDAO appParametersDAO) {
        this.solrIndexManager = solrIndexManager;
        this.personDao = personDao;
        this.appParametersDAO = appParametersDAO;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
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
        d.addField(FIELD_ALL_NAMES, p.getFullNameInOfficialLanguage());
        d.addField(FIELD_GENDER, GenderUtil.getGenderCharacter(p.getGender()));
        d.addField(FIELD_NIC, p.getNic());
        d.addField(FIELD_PIN, p.getPin());

        d.addField(FIELD_PLACE_OF_BIRTH, p.getPlaceOfBirth());
        d.addField(FIELD_DATE_OF_BIRTH, p.getDateOfBirth());
        d.addField(FIELD_DATE_OF_DEATH, p.getDateOfDeath());

        if (p.getCountries().isEmpty()) {
            d.addField(FIELD_CITIZENSHIP, SRI_LANKA);
        } else {
            for (PersonCitizenship c : p.getCountries()) {
                d.addField(FIELD_CITIZENSHIP, c.getCountry().getCountryCode());
                d.addField(FIELD_PASSPORT, c.getPassportNo());
            }
        }

        if (p.getLastAddress() != null) {
            d.addField(FIELD_LAST_ADDRESS, p.getLastAddress().toString());
        }
        
        for (Address a : p.getAddresses()) {
            d.addField(FIELD_ALL_ADDRESSES, a.toString());
        }

        d.addField(FIELD_EMAIL, p.getPersonEmail());
        d.addField(FIELD_PHONE, p.getPersonPhoneNo());

        d.addField(FIELD_LIFE_STATUS,   LifeStatusUtil.getStatusAsString(p.getLifeStatus()));
        d.addField(FIELD_CIVIL_STATUS,  CivilStatusUtil.getStatusAsString(p.getCivilStatus()));
        d.addField(FIELD_RECORD_STATUS, getRecordStatus(p.getStatus()));

        solrIndexManager.getPRSServer().add(d);
    }

    private static String getRecordStatus(Person.Status s) {
        switch (s) {
            case UNVERIFIED: return "U";
            case SEMI_VERIFIED: return "S";
            case VERIFIED: return "V";
            case CANCELLED: return "C";
        }
        throw new IllegalArgumentException("Illegal record state : " + s);
    }
}
