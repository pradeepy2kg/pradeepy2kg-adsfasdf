package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.core.index.SolrIndexManager;
import lk.rgd.common.util.CivilStatusUtil;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.common.util.LifeStatusUtil;
import lk.rgd.crs.api.service.PRSRecordsIndexer;
import lk.rgd.crs.core.DatabaseInitializer;
import lk.rgd.prs.PRSRuntimeException;
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
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

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
    private CountDownLatch doneSignal = new CountDownLatch(0);

    private long start;
    private volatile long completed;
    private static final int MILLION = 1000000;
    /** Ensure that the maximum DB connections defined in the connection pool exceeds this - else may deadlock */
    private static final int THREADS = 30;

    public PRSRecordsIndexerImpl(SolrIndexManager solrIndexManager, DataSource dataSource, PersonDAO personDAO) {
        this.solrIndexManager = solrIndexManager;
        this.dataSource = dataSource;
        this.personDAO = personDAO;
    }

    public boolean deleteIndex() {
        if (solrIndexManager.getPRSServer() == null) {
            logger.error("Cannot connect to Solr server to index all PRS records");
            return false;
        }

        // delete all existing
        try {
            logger.info("Deleting all PRS records off Solr index");
            solrIndexManager.getPRSServer().deleteByQuery("*:*");
            logger.info("Deleted all PRS records off Solr index");
            return true;
        } catch (Exception e) {
            logger.error("Error deleting existing PRS records off Solr index");
            return false;
        }
    }

    public boolean optimizeIndex() {
        try {
            long start = System.currentTimeMillis();
            solrIndexManager.getPRSServer().optimize();
            solrIndexManager.getPRSServer().commit();
            logger.debug("Optimized PRS records in : {}s", (System.currentTimeMillis() - start) / 1000);
            return true;
        } catch (Exception e) {
            logger.error("Error optimizing the PRS index", e);
        }
        return false;
    }

    public boolean indexAll() {

        if (doneSignal.getCount() > 0) {
            logger.warn("An indexing process is already executing : {} worker threads", doneSignal.getCount());
            return false;

        } else {
            logger.info("Starting new indexing process for all PRS records...");

            deleteIndex();
            start = System.currentTimeMillis();
            completed = 0;

            try {
                Connection conn = dataSource.getConnection();

                Statement s = conn.createStatement();
                ResultSet rs = s.executeQuery("SELECT min(personUKey) FROM PRS.PERSON");
                rs.next();
                long minUKey = rs.getLong(1);
                rs = s.executeQuery("SELECT max(personUKey) FROM PRS.PERSON");
                rs.next();
                long maxUKey = rs.getLong(1);
                rs = s.executeQuery("SELECT count(*) FROM PRS.PERSON");
                rs.next();
                long count = rs.getLong(1);
                conn.close();

                long block = ((maxUKey - minUKey) / THREADS) + 1;
                if (block > MILLION) {
                    block = MILLION;
                }

                int threads = THREADS + 1;
                doneSignal = new CountDownLatch(threads);
                logger.info("Using : " + threads + " threads with a block size of : " + block +
                    " to process : " + count + " rows from : " + minUKey + " to: " + maxUKey);

                long startIDUkey = minUKey;
                long endIDUkey = minUKey;

                Worker w;
                for (int i = 0; i < threads; i++) {
                    endIDUkey += block;
                    w = new Worker(i, startIDUkey, endIDUkey, doneSignal);
                    w.start();
                    startIDUkey = endIDUkey + 1;
                }

                try {
                    doneSignal.await();
                    logger.info("Indexed PRS records in : {}s", (System.currentTimeMillis() - start) / 1000);
                } catch (InterruptedException e) {
                    logger.info("Interrupted while waiting for PRS indexer worker threads to complete");
                }

                logger.info("PRS Indexing process started at : " + new Date() + " successfully completed indexing : " +
                    count + " records. Indexing took: " + (System.currentTimeMillis() - start) / 1000 + " S");

                optimizeIndex();

            } catch (SQLException e) {
                logger.error("Could not count the number of rows in the PERSON table. PRS indexing failed", e);
                return false;
            }

            return true;
        }
    }

    /**
     * Index rows with a personIDUkey from startIDUKey (inclusive) to endIDUKey (inclusive)
     */
    private boolean indexRange(int worker, long startIDUkey, long endIDUkey) {

        if (solrIndexManager.getPRSServer() == null) {
            logger.error("Worker : {} cannot connect to Solr server to index PRS records", worker);
            return false;
        }

        logger.info("Worker : " + worker +
            " re-indexing 1000 records from idUKey : {} to : {}", startIDUkey, endIDUkey);

        long count = 0;
        Connection conn = null;

        try {
            conn = dataSource.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                "SELECT * from PRS.PERSON WHERE personUKey >= ? AND personUKey <= ?",
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setLong(1, startIDUkey);
            ps.setLong(2, endIDUkey);

            if (Boolean.getBoolean("ecivildb.mysql")) {
                ps.setFetchSize(Integer.MIN_VALUE);
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                addRecord(rs);
                count++;
                if (count % 1000 == 0) {
                    completed += 1000;
                    logger.info("Indexed : {} records in : {}s", completed, (System.currentTimeMillis() - start) / 1000);
                }
            }
            logger.debug("Worker : {} completed assigned batch", worker);
            return true;

        } catch (SolrServerException e) {
            logger.error("Error from Solr server during PRS record re-indexing", e);
        } catch (SQLException e) {
            logger.error("Database Exception encountered during PRS record re-indexing", e);
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

        logger.error("Failed to re-index PRS range from : {} to : {}", startIDUkey, endIDUkey);
        return false;
    }

    public void updateIndex(Person person) {

        SolrInputDocument d = new SolrInputDocument();

        final long personUKey = person.getPersonUKey();
        d.addField(FIELD_PERSON_UKEY, personUKey);
        d.addField(FIELD_FULL_NAME_ENGLISH, person.getFullNameInEnglishLanguage());
        d.addField(FIELD_FULL_NAME_OFFICIAL_LANG, person.getFullNameInOfficialLanguage());
        d.addField(FIELD_ALL_NAMES, person.getFullNameInEnglishLanguage());
        d.addField(FIELD_ALL_NAMES, person.getFullNameInOfficialLanguage());
        d.addField(FIELD_GENDER, GenderUtil.getGenderCharacter(person.getGender()));
        d.addField(FIELD_NIC, person.getNic());
        d.addField(FIELD_PIN, person.getPin());

        d.addField(FIELD_PLACE_OF_BIRTH, person.getPlaceOfBirth());
        d.addField(FIELD_DATE_OF_BIRTH, person.getDateOfBirth());
        d.addField(FIELD_DATE_OF_DEATH, person.getDateOfDeath());

        List<PersonCitizenship> pcList = personDAO.getCitizenshipsByPersonUKey(personUKey);
        if (pcList.isEmpty()) {
            d.addField(FIELD_CITIZENSHIP, SRI_LANKA);
        } else {
            for (PersonCitizenship c : pcList) {
                d.addField(FIELD_CITIZENSHIP, c.getCountry().getCountryCode());
                d.addField(FIELD_PASSPORT, c.getPassportNo());
            }
        }

        Address lastAddress = person.getLastAddress();
        if (lastAddress != null) {
            d.addField(FIELD_LAST_ADDRESS, lastAddress.toString());

            // process any other addressed
            for (Address a : personDAO.getAddressesByPersonUKey(personUKey)) {
                d.addField(FIELD_ALL_ADDRESSES, a.toString());
            }
        }

        d.addField(FIELD_EMAIL, person.getPersonEmail());
        d.addField(FIELD_PHONE, person.getPersonPhoneNo());

        d.addField(FIELD_LIFE_STATUS, LifeStatusUtil.getStatusAsString(person.getLifeStatus()));
        d.addField(FIELD_CIVIL_STATUS, CivilStatusUtil.getStatusAsString(person.getCivilStatus()));
        d.addField(FIELD_RECORD_STATUS, getRecordStatus(person.getStatus()));

        /*try {
            solrIndexManager.getPRSServer().add(d);
        } catch (SolrServerException e) {
            if (Boolean.getBoolean("ecivildb.mysql")) {
                logger.error("Solr Error updating Solr index for Person with UKey : " + person.getPersonUKey(), e);
                // throw an error causing a rollback, only when using MySQL (i.e. production) and not on unit tests
                throw new PRSRuntimeException("", ErrorCodes.PRS_INDEX_UPDATE_FAILED, e);
            } else {
                logger.error("Solr Error updating Solr index for Person with UKey : {}", person.getPersonUKey());
            }
        } catch (IOException e) {
            if (Boolean.getBoolean("ecivildb.mysql")) {
                logger.error("IO Error updating Solr index for Person with UKey : " + person.getPersonUKey(), e);
                // throw an error causing a rollback, only when using MySQL (i.e. production) and not on unit tests
                throw new PRSRuntimeException("", ErrorCodes.PRS_INDEX_UPDATE_FAILED, e);
            } else {
                logger.error("IO Error updating Solr index for Person with UKey : {}", person.getPersonUKey());
            }
            e.printStackTrace();
        } catch (Exception e) {
            if (Boolean.getBoolean("ecivildb.mysql")) {
                logger.error("Unknown Error updating Solr index for Person with UKey : " + person.getPersonUKey(), e);
                // throw an error causing a rollback, only when using MySQL (i.e. production) and not on unit tests
                throw new PRSRuntimeException("", ErrorCodes.PRS_INDEX_UPDATE_FAILED, e);
            } else {
                logger.error("Error updating Solr index for Person with UKey : {}", person.getPersonUKey());
            }
        }

        logger.debug("Updated Solr index for Person with UKey : {}", person.getPersonUKey());*/
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

        List<PersonCitizenship> pcList = personDAO.getCitizenshipsByPersonUKey(personUKey);
        if (pcList.isEmpty()) {
            d.addField(FIELD_CITIZENSHIP, SRI_LANKA);
        } else {
            for (PersonCitizenship c : pcList) {
                d.addField(FIELD_CITIZENSHIP, c.getCountry().getCountryCode());
                d.addField(FIELD_PASSPORT, c.getPassportNo());
            }
        }

        Long lastAddressUKey = rs.getLong("lastAddressUKey");
        if (lastAddressUKey != null && lastAddressUKey > 0) {
            d.addField(FIELD_LAST_ADDRESS, personDAO.getAddressByUKey(lastAddressUKey).toString());

            // process any other addressed
            for (Address a : personDAO.getAddressesByPersonUKey(personUKey)) {
                d.addField(FIELD_ALL_ADDRESSES, a.toString());
            }
        }

        d.addField(FIELD_EMAIL, rs.getString("personEmail"));
        d.addField(FIELD_PHONE, rs.getString("personPhoneNo"));

        d.addField(FIELD_LIFE_STATUS, LifeStatusUtil.getStatusAsString(rs.getInt(FIELD_LIFE_STATUS)));
        d.addField(FIELD_CIVIL_STATUS, CivilStatusUtil.getStatusAsString(rs.getInt(FIELD_CIVIL_STATUS)));
        d.addField(FIELD_RECORD_STATUS, getRecordStatus(rs.getInt("status")));

        solrIndexManager.getPRSServer().add(d);
    }

    private static String getRecordStatus(int s) {
        switch (s) {
            case 0:
                return "U";
            case 1:
                return "S";
            case 2:
                return "V";
            case 3:
                return "P";
            case 4:
                return "C";
            case 5:
                return "D";
            case 6:
                return "A";
        }
        throw new IllegalArgumentException("Illegal record state : " + s);
    }

    private static String getRecordStatus(Person.Status s) {
        switch (s) {
            case UNVERIFIED:
                return "U";
            case SEMI_VERIFIED:
                return "S";
            case DATA_ENTRY:
                return "N";
            case VERIFIED:
                return "V";
            case CANCELLED:
                return "C";
            case DELETED:
                return "D";
            case ARCHIVED_ALTERED:
                return "A";
        }
        throw new IllegalArgumentException("Illegal record state : " + s);
    }

    private class Worker extends Thread {

        private final int id;
        private final long startIDUKey;
        private final long endIDUKey;
        private final CountDownLatch doneSignal;

        private Worker(int id, long startIDUKey, long endIDUKey, CountDownLatch doneSignal) {
            this.id = id;
            this.startIDUKey = startIDUKey;
            this.endIDUKey = endIDUKey;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run() {
            logger.info("Worker : " + id + " will re-index records from idUKey: " + startIDUKey + " to: " + endIDUKey);

            try {
                long s = startIDUKey;
                long e = startIDUKey - 1;
                while (s <= endIDUKey) {
                    e += 1000;
                    if (e > endIDUKey) {
                        e = endIDUKey;
                    }

                    indexRange(id, s, e);
                    s = e + 1;
                }

            } finally {
                doneSignal.countDown();
                logger.info("Worker : {} exiting. Remaining threads : {}", id, doneSignal.getCount());
            }
        }
    }
}
