package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.domain.AppParameter;
import lk.rgd.common.core.index.SolrIndexManager;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.*;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is the bridge class that exposes the Solr indexing to the rest of the application
 *
 * @author Chathuranga Withana
 */
public class DeathRecordsIndexer {

    private static final Logger logger = LoggerFactory.getLogger(DeathRecordsIndexer.class);

    private final SolrIndexManager solrIndexManager;
    private final DeathRegisterDAO deathRegisterDAO;
    private final AppParametersDAO appParametersDAO;

    private static final String COLON = ":";
    private static final String SPACE = " ";
    private static final String QUOTE = "\"";
    private static final String QUOTE_TILDE_3 = "\"~3";
    private static final String QUOTE_TILDE_12 = "\"~12";
    private static final String AND = " AND ";
    private static final String FIELD_DDF_SERIAL_NO = "deathSerialNo";
    private static final String FIELD_DDF_STATE = "status";
    private static final String FIELD_DATE_OF_REGISTRATION = "dateOfRegistration";
    private static final String FIELD_DEATH_DISTRICT = "deathDistrict";
    private static final String FIELD_DEATH_DIVISION = "deathDivision";
    private static final String FIELD_DEATH_CAUSE = "causeOfDeath";
    private static final String FIELD_PLACE_OF_DEATH = "placeOfDeath";
    private static final String FIELD_DATE_OF_DEATH = "dateOfDeath";
    private static final String FIELD_PLACE_OF_BURIAL = "placeOfBurial";
    private static final String FIELD_PERSON_NICOR_PIN = "deathPersonPINorNIC";
    private static final String FIELD_PERSON_PASSPORT_NO = "deathPersonPassportNo";
    private static final String FIELD_PERSON_AGE = "deathPersonAge";
    private static final String FIELD_PERSON_GENDER = "deathPersonGender";
    private static final String FIELD_PERSON_FULL_NAME_ENGLISH = "deathPersonNameInEnglish";
    private static final String FIELD_PERSON_FULL_NAME_OFFICIAL_LANG = "deathPersonNameOfficialLang";
    private static final String FIELD_PERSON_ADDRESS = "deathPersonPermanentAddress";
    private static final String FIELD_FATHER_FULL_NAME = "deathPersonFatherFullName";
    private static final String FIELD_FATHER_NICOR_PIN = "deathPersonFatherPINorNIC";
    private static final String FIELD_MOTHER_FULL_NAME = "deathPersonMotherFullName";
    private static final String FIELD_MOTHER_NICOR_PIN = "deathPersonMotherPINorNIC";
    private static final String FIELD_DECLARANT_FULL_NAME = "declarantFullName";
    private static final String FIELD_DECLARANT_NICOR_PIN = "declarantNICorPIN";
    private static final String FIELD_DECLARANT_ADDRESS = "declarantAddress";
    private static final String FIELD_ID_UKEY = "deathUKey";

    public DeathRecordsIndexer(SolrIndexManager solrIndexManager, DeathRegisterDAO deathRegisterDAO,
        AppParametersDAO appParametersDAO) {
        this.solrIndexManager = solrIndexManager;
        this.deathRegisterDAO = deathRegisterDAO;
        this.appParametersDAO = appParametersDAO;
    }

    public void add(DeathRegister ddf) {
        try {
            addRecord(ddf);
            solrIndexManager.getDeathServer().commit();
            logger.info("Successfully indexed death record : {}", ddf.getIdUKey());
        } catch (Exception e) {
            logger.error("Error indexing death record : " + ddf.getIdUKey() + " cause : " + e.getMessage()); // do not stack trace
        }
    }

    public List<DeathRegister> searchDeathRecords(CertificateSearch cs) {

        SolrQuery query = new SolrQuery();
        StringBuilder sb = new StringBuilder(128).append("(");

        SearchInfo search = cs.getSearch();

        // dead person name in official language and english language
        if (isSpecified(search.getSearchFullNameOfficialLang())) {
            sb.append(SPACE);
            sb.append(FIELD_PERSON_FULL_NAME_OFFICIAL_LANG);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(search.getSearchFullNameOfficialLang());
            sb.append(QUOTE_TILDE_3);
        }
        if (isSpecified(search.getSearchFullNameEnglish())) {
            sb.append(SPACE);
            sb.append(FIELD_PERSON_FULL_NAME_ENGLISH);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(search.getSearchFullNameEnglish());
            sb.append(QUOTE_TILDE_3);
        }

        // cause of death
        if (isSpecified(search.getCauseOfEvent())) {
            sb.append(SPACE);
            sb.append(FIELD_DEATH_CAUSE);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(search.getCauseOfEvent());
            sb.append(QUOTE_TILDE_3);
        }

        // names of dead person's mother and father
        if (isSpecified(search.getFatherFullName())) {
            sb.append(SPACE);
            sb.append(FIELD_FATHER_FULL_NAME);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(search.getFatherFullName());
            sb.append(QUOTE_TILDE_3);
        }
        if (isSpecified(search.getMotherFullName())) {
            sb.append(SPACE);
            sb.append(FIELD_MOTHER_FULL_NAME);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(search.getMotherFullName());
            sb.append(QUOTE_TILDE_3);
        }

        // place of death
        if (isSpecified(search.getPlaceOfEvent())) {
            sb.append(SPACE);
            sb.append(FIELD_PLACE_OF_DEATH);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(search.getPlaceOfEvent());
            sb.append(QUOTE_TILDE_12);
        }

        // before we start the "AND" search terms, incase all of the above were not specified, make the query ( *:* ) ..
        if (sb.toString().length() == 1) {
            sb.append("*:*");
        }

        // gender is a required attribute
        sb.append(") AND ");
        sb.append(FIELD_PERSON_GENDER);
        sb.append(COLON);
        sb.append(QUOTE);
        sb.append(GenderUtil.getGenderString(search.getGender()));
        sb.append(QUOTE);

        // state of a Death Register
        sb.append(AND);
        sb.append(FIELD_DDF_STATE);
        sb.append(COLON);
        sb.append(QUOTE);
        sb.append(search.getSearchRecordStatus());
        sb.append(QUOTE);

        // date of death
        if (search.getDateOfEvent() != null) {
            sb.append(AND);
            sb.append(FIELD_DATE_OF_DEATH);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(DateTimeUtils.dayToUTCString(search.getDateOfEvent()));
            sb.append(QUOTE);
        }

        // date of birth registration
        if (search.getCertificateIssueDate() != null) {
            sb.append(" AND ");
            sb.append(FIELD_DATE_OF_REGISTRATION);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(DateTimeUtils.dayToUTCString(search.getCertificateIssueDate()));
            sb.append(QUOTE);
        }

        logger.debug("Solr query : " + sb.toString());

        query.setRows(appParametersDAO.getIntParameter(AppParameter.CRS_CERTIFICATE_SEARCH_LIMIT));
        query.setQuery(sb.toString());

        List<DeathRegister> ddfList = new ArrayList<DeathRegister>();
        try {
            QueryResponse qr = solrIndexManager.getDeathServer().query(query);
            SolrDocumentList docList = qr.getResults();
            Iterator<SolrDocument> it = docList.iterator();

            while (it.hasNext()) {
                SolrDocument doc = it.next();
                DeathRegister ddf = deathRegisterDAO.getById(
                    Long.parseLong(doc.getFieldValue(FIELD_ID_UKEY).toString()));
                if (ddf != null) {
                    ddfList.add(ddf);
                }
            }
        } catch (SolrServerException e) {
            handleException("Error performing search for death records", ErrorCodes.INDEX_SEARCH_FAILURE, e);
        }
        return ddfList;
    }

    private boolean isSpecified(String s) {
        return s != null && s.trim().length() > 0;
    }

    private void handleException(String msg, int code, Exception e) {
        logger.error(msg, e);
        throw new CRSRuntimeException(msg, code, e);
    }

    public boolean indexAll() {

        if (solrIndexManager.getDeathServer() == null) {
            logger.error("Cannot connect to Solr server to index all death records");
            return false;
        }

        // delete all existing
        try {
            logger.info("Deleting all death records off Solr index");
            solrIndexManager.getDeathServer().deleteByQuery("*:*");
        } catch (Exception e) {
            logger.error("Error deleting existing death records off Solr index");
        }

        List<DeathRegister> ddfList = deathRegisterDAO.findAll();

        int count = 0;
        try {
            logger.info("Begin re-indexing of all death records into Solr");
            for (DeathRegister ddf : ddfList) {
                if (ddf.getStatus() == DeathRegister.State.ARCHIVED_CERT_GENERATED) {
                    addRecord(ddf);
                    count++;
                    if (count % 10000 == 0) {
                        logger.info("Indexed : {} records..", count);
                    }
                }
            }

            solrIndexManager.getDeathServer().optimize();
            solrIndexManager.getDeathServer().commit();

            logger.debug("Successfully indexed : " + count + " death records..");
            return true;

        } catch (SolrServerException e) {
            logger.error("Error from Solr server during death record re-indexing", e);
        } catch (IOException e) {
            logger.error("IO Exception encountered during death record re-indexing", e);
        } catch (Exception e) {
            logger.error("Unexpected Exception encountered during death record re-indexing", e);
        }
        return false;
    }

    public void addRecord(DeathRegister ddf) throws IOException, SolrServerException {
        SolrInputDocument d = new SolrInputDocument();

        d.addField(FIELD_ID_UKEY, ddf.getIdUKey());
        // add death register info
        d.addField(FIELD_DDF_STATE, ddf.getStatus().toString());
        //d.addField("comments", ddf.getComments());

        // add death info
        DeathInfo deathInfo = ddf.getDeath();
        d.addField(FIELD_DDF_SERIAL_NO, deathInfo.getDeathSerialNo());
        d.addField(FIELD_PLACE_OF_DEATH, deathInfo.getPlaceOfDeath());
        d.addField(FIELD_DATE_OF_DEATH, deathInfo.getDateOfDeath());
        d.addField(FIELD_DATE_OF_REGISTRATION, deathInfo.getDateOfRegistration());
        d.addField(FIELD_DEATH_CAUSE, deathInfo.getCauseOfDeath());
        d.addField(FIELD_DEATH_DISTRICT,
            deathInfo.getDeathDistrict().getEnDistrictName() + " / " +
                deathInfo.getDeathDistrict().getSiDistrictName() + " / " +
                deathInfo.getDeathDistrict().getTaDistrictName());
        d.addField(FIELD_DEATH_DIVISION,
            deathInfo.getDeathDivision().getEnDivisionName() + " / " +
                deathInfo.getDeathDivision().getSiDivisionName() + " / " +
                deathInfo.getDeathDivision().getTaDivisionName() + " / ");
        d.addField(FIELD_PLACE_OF_BURIAL, deathInfo.getPlaceOfBurial());

        // add death person info
        DeathPersonInfo deathPerson = ddf.getDeathPerson();
        if (deathPerson != null) {
            // add death person details
            d.addField(FIELD_PERSON_NICOR_PIN, deathPerson.getDeathPersonPINorNIC());
            d.addField(FIELD_PERSON_PASSPORT_NO, deathPerson.getDeathPersonPassportNo());
            d.addField(FIELD_PERSON_AGE, deathPerson.getDeathPersonAge());
            d.addField(FIELD_PERSON_GENDER, GenderUtil.getGenderString(deathPerson.getDeathPersonGender()));
            d.addField(FIELD_PERSON_FULL_NAME_ENGLISH, deathPerson.getDeathPersonNameInEnglish());
            d.addField(FIELD_PERSON_FULL_NAME_OFFICIAL_LANG, deathPerson.getDeathPersonNameOfficialLang());
            d.addField(FIELD_PERSON_ADDRESS, deathPerson.getDeathPersonPermanentAddress());

            // add death person's father info
            d.addField(FIELD_FATHER_FULL_NAME, deathPerson.getDeathPersonFatherFullName());
            d.addField(FIELD_FATHER_NICOR_PIN, deathPerson.getDeathPersonFatherPINorNIC());
            // add death person's mother info
            d.addField(FIELD_MOTHER_FULL_NAME, deathPerson.getDeathPersonMotherFullName());
            d.addField(FIELD_MOTHER_NICOR_PIN, deathPerson.getDeathPersonMotherPINorNIC());
        }

        // add declarant info
        DeclarantInfo declarantInfo = ddf.getDeclarant();
        d.addField(FIELD_DECLARANT_FULL_NAME, declarantInfo.getDeclarantFullName());
        d.addField(FIELD_DECLARANT_NICOR_PIN, declarantInfo.getDeclarantNICorPIN());
        d.addField(FIELD_DECLARANT_ADDRESS, declarantInfo.getDeclarantAddress());

        solrIndexManager.getDeathServer().add(d);
    }
}
