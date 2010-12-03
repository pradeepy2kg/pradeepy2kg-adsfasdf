package lk.rgd.crs.core.service;

import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.core.index.SolrIndexManager;
import lk.rgd.common.util.CivilStatusUtil;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.common.util.LifeStatusUtil;
import lk.rgd.crs.api.service.PRSRecordsIndexer;
import lk.rgd.crs.core.DatabaseInitializer;
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

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private final DataSource dataSource;
    private final PersonDAO personDAO;

    public PRSRecordsIndexerImpl(SolrIndexManager solrIndexManager, DataSource dataSource, PersonDAO personDAO) {
        this.solrIndexManager = solrIndexManager;
        this.dataSource = dataSource;
        this.personDAO = personDAO;
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

        Connection conn = null;

        try {
            logger.info("Begin re-indexing of all PRS records into Solr");

            long start = System.currentTimeMillis();
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            conn.setReadOnly(true);

            Statement s = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (Boolean.getBoolean("ecivildb.mysql")) {
                s.setFetchSize(Integer.MIN_VALUE);
            }
            ResultSet rs = s.executeQuery("SELECT * FROM PRS.PERSON");

            int count = 0;
            while (rs.next()) {
                addRecord(rs);
                count++;
                if (count % 10000 == 0) {
                    logger.info("Indexed : {} records in : {}s", count, (System.currentTimeMillis() - start)/1000);
                }
            }

            logger.debug("Indexed : " + count + " PRS records in " + (System.currentTimeMillis() - start) / 1000 + "s");
            start = System.currentTimeMillis();
            solrIndexManager.getPRSServer().optimize();
            solrIndexManager.getPRSServer().commit();
            logger.debug("Optimized indexed of : " + count + " PRS records in : " + 
                (System.currentTimeMillis() - start) / 1000 + "s");
            return true;

            // TODO we do not print the stack trace for now..
        } catch (SolrServerException e) {
            logger.error("Error from Solr server during PRS record re-indexing", e);
        } catch (IOException e) {
            logger.error("IO Exception encountered during PRS record re-indexing", e);
        } catch (Exception e) {
            logger.error("Unexpected Exception encountered during PRS record re-indexing", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {}
            }
        }
        return false;
    }

    public void addRecord(ResultSet rs) throws SQLException, IOException, SolrServerException {

        SolrInputDocument d = new SolrInputDocument();

        final long personUKey = rs.getLong(FIELD_PERSON_UKEY);
        d.addField(FIELD_PERSON_UKEY, personUKey);
        d.addField(FIELD_FULL_NAME_ENGLISH, rs.getString(FIELD_FULL_NAME_ENGLISH));
        d.addField(FIELD_FULL_NAME_OFFICIAL_LANG, rs.getString(FIELD_FULL_NAME_OFFICIAL_LANG));
        d.addField(FIELD_ALL_NAMES, rs.getString(FIELD_FULL_NAME_ENGLISH));
        d.addField(FIELD_ALL_NAMES, rs.getString(FIELD_FULL_NAME_OFFICIAL_LANG));
        d.addField(FIELD_GENDER, GenderUtil.getGenderCharacter(rs.getInt(FIELD_GENDER)));
        d.addField(FIELD_NIC, rs.getString(FIELD_NIC));
        d.addField(FIELD_PIN, rs.getLong(FIELD_PIN));

        d.addField(FIELD_PLACE_OF_BIRTH, rs.getString(FIELD_PLACE_OF_BIRTH));
        d.addField(FIELD_DATE_OF_BIRTH, rs.getDate(FIELD_DATE_OF_BIRTH));
        d.addField(FIELD_DATE_OF_DEATH, rs.getDate(FIELD_DATE_OF_DEATH));

        //List<PersonCitizenship> pcList = personDAO.getCitizenshipsByPersonUKey(personUKey);
        //if (pcList.isEmpty()) {
            d.addField(FIELD_CITIZENSHIP, SRI_LANKA);
        //} else {
        //    for (PersonCitizenship c : pcList) {
        //        d.addField(FIELD_CITIZENSHIP, c.getCountry().getCountryCode());
        //        d.addField(FIELD_PASSPORT, c.getPassportNo());
        //    }
        //}

        Long lastAddressUKey = rs.getLong("lastAddressUKey");
        if (lastAddressUKey != null && lastAddressUKey > 0) {
            d.addField(FIELD_LAST_ADDRESS, personDAO.getAddressByUKey(lastAddressUKey).toString());

            // process any other addressed
            //for (Address a : personDAO.getAddressesByPersonUKey(personUKey)) {
            //    d.addField(FIELD_ALL_ADDRESSES, a.toString());
            //}
        }

        d.addField(FIELD_EMAIL, rs.getString("personEmail"));
        d.addField(FIELD_PHONE, rs.getString("personPhoneNo"));

        d.addField(FIELD_LIFE_STATUS,   LifeStatusUtil.getStatusAsString(rs.getInt(FIELD_LIFE_STATUS)));
        d.addField(FIELD_CIVIL_STATUS,  CivilStatusUtil.getStatusAsString(rs.getInt(FIELD_CIVIL_STATUS)));
        d.addField(FIELD_RECORD_STATUS, getRecordStatus(rs.getInt("status")));

        solrIndexManager.getPRSServer().add(d);
    }

    private static String getRecordStatus(int s) {
        switch (s) {
            case 0: return "U";
            case 1: return "S";
            case 2: return "V";
            case 3: return "C";
        }
        throw new IllegalArgumentException("Illegal record state : " + s);
    }
}
