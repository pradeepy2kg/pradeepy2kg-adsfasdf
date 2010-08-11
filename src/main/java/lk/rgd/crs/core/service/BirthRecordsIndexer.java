package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.domain.AppParameter;
import lk.rgd.common.core.index.SolrIndexManager;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
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
import java.util.*;

/**
 * This is the bridge class that exposes the Solr indexing to the rest of the application
 *
 * @author asankha
 */
public class BirthRecordsIndexer {

    private static final Logger logger = LoggerFactory.getLogger(BirthRecordsIndexer.class);

    private final SolrIndexManager solrIndexManager;
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final AppParametersDAO appParametersDAO;

    private static final String COLON = ":";
    private static final String SPACE = " ";
    private static final String QUOTE = "\"";
    private static final String QUOTE_TILDE_3 = "\"~3";
    private static final String QUOTE_TILDE_12 = "\"~12";
    private static final String FIELD_CHILD_FULL_NAME_ENGLISH = "childFullNameEnglish";
    private static final String FIELD_CHILD_FULL_NAME_OFFICIAL_LANG = "childFullNameOfficialLang";
    private static final String FIELD_DATE_OF_BIRTH = "dateOfBirth";
    private static final String FIELD_CHILD_GENDER = "childGender";
    private static final String FIELD_PLACE_OF_BIRTH = "placeOfBirth";
    private static final String FIELD_BIRTH_AT_HOSPITAL = "birthAtHospital";
    private static final String FIELD_BIRTH_DIVISION = "birthDivision";
    private static final String FIELD_BIRTH_DISTRICT = "birthDistrict";
    private static final String FIELD_BDF_SERIAL_NO = "bdfSerialNo";
    private static final String FIELD_DATE_OF_REGISTRATION = "dateOfRegistration";
    private static final String FIELD_PIN = "pin";
    private static final String FIELD_MOTHER_FULL_NAME = "motherFullName";
    private static final String FIELD_MOTHER_ADDRESS = "motherAddress";
    private static final String FIELD_MOTHER_DOB = "motherDOB";
    private static final String FIELD_MOTHER_NICOR_PIN = "motherNICorPIN";
    private static final String FIELD_MOTHER_PASSPORT_NO = "motherPassportNo";
    private static final String FIELD_MOTHER_PLACE_OF_BIRTH = "motherPlaceOfBirth";
    private static final String FIELD_FATHER_FULL_NAME = "fatherFullName";
    private static final String FIELD_FATHER_DOB = "fatherDOB";
    private static final String FIELD_FATHER_NICOR_PIN = "fatherNICorPIN";
    private static final String FIELD_FATHER_PASSPORT_NO = "fatherPassportNo";
    private static final String FIELD_FATHER_PLACE_OF_BIRTH = "fatherPlaceOfBirth";
    private static final String FIELD_INFORMANT_NAME = "informantName";
    private static final String FIELD_INFORMANT_ADDRESS = "informantAddress";
    private static final String FIELD_INFORMANT_NICOR_PIN = "informantNICorPIN";
    private static final String FIELD_GRAND_FATHER_FULL_NAME = "grandFatherFullName";
    private static final String FIELD_GRAND_FATHER_BIRTH_PLACE = "grandFatherBirthPlace";
    private static final String FIELD_GRAND_FATHER_BIRTH_YEAR = "grandFatherBirthYear";
    private static final String FIELD_GREAT_GRAND_FATHER_FULL_NAME = "greatGrandFatherFullName";
    private static final String FIELD_GREAT_GRAND_FATHER_BIRTH_PLACE = "greatGrandFatherBirthPlace";
    private static final String FIELD_GREAT_GRAND_FATHER_BIRTH_YEAR = "greatGrandFatherBirthYear";
    private static final String FIELD_ID_UKEY = "idUKey";

    public BirthRecordsIndexer(SolrIndexManager solrIndexManager, BirthDeclarationDAO birthDeclarationDAO,
        AppParametersDAO appParametersDAO) {
        this.solrIndexManager = solrIndexManager;
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.appParametersDAO = appParametersDAO;
    }

    public void add(BirthDeclaration bdf) {
        try {
            addRecord(bdf);
            solrIndexManager.getServer().commit();
            logger.info("Successfully indexed birth record : {}", bdf.getIdUKey());
        } catch (Exception e) {
            logger.error("Error indexing birth record : " + bdf.getIdUKey(), e);
        }
    }

    public List<BirthDeclaration> searchBirthRecords(BirthCertificateSearch bcs) {

        SolrQuery query = new SolrQuery();

        StringBuilder sb = new StringBuilder(128).append("(");

        // childs name in official and english languages
        if (isSpecified(bcs.getChildFullNameOfficialLang())) {
            sb.append(SPACE);
            sb.append(FIELD_CHILD_FULL_NAME_OFFICIAL_LANG);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(bcs.getChildFullNameOfficialLang());
            sb.append(QUOTE_TILDE_3);
        }
        if (isSpecified(bcs.getChildFullNameEnglish())) {
            sb.append(SPACE);
            sb.append(FIELD_CHILD_FULL_NAME_ENGLISH);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(bcs.getChildFullNameEnglish());
            sb.append(QUOTE_TILDE_3);
        }

        // names of mother and father
        if (isSpecified(bcs.getMotherFullName())) {
            sb.append(SPACE);
            sb.append(FIELD_MOTHER_FULL_NAME);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(bcs.getMotherFullName());
            sb.append(QUOTE_TILDE_3);
        }
        if (isSpecified(bcs.getFatherFullName())) {
            sb.append(SPACE);
            sb.append(FIELD_FATHER_FULL_NAME);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(bcs.getFatherFullName());
            sb.append(QUOTE_TILDE_3);
        }

        // place of birth
        if (bcs.getPlaceOfBirth() != null) {
            sb.append(SPACE);
            sb.append(FIELD_PLACE_OF_BIRTH);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(bcs.getPlaceOfBirth());
            sb.append(QUOTE_TILDE_12);
        }

        // before we start the "AND" search terms, incase all of the above were not specified, make the query ( *:* ) ..
        if (sb.toString().length() == 1) {
            sb.append("*:*");
        }

        // gender is a required attribute
        sb.append(") AND ");
        sb.append(FIELD_CHILD_GENDER);
        sb.append(COLON);
        sb.append(QUOTE);
        sb.append(GenderUtil.getGenderString(bcs.getGender()));
        sb.append(QUOTE);

        // date of birth
        if (bcs.getDateOfBirth() != null) {
            sb.append(" AND ");
            sb.append(FIELD_DATE_OF_BIRTH);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(DateTimeUtils.dayToUTCString(bcs.getDateOfBirth()));
            sb.append(QUOTE);
        }

        // date of birth
        if (bcs.getDateOfRegistration() != null) {
            sb.append(" AND ");
            sb.append(FIELD_DATE_OF_REGISTRATION);
            sb.append(COLON);
            sb.append(QUOTE);
            sb.append(DateTimeUtils.dayToUTCString(bcs.getDateOfRegistration()));
            sb.append(QUOTE);
        }

        logger.debug("Solr query : " + sb.toString());

        query.setRows(appParametersDAO.getIntParameter(AppParameter.CRS_BIRTH_CERT_SEARCH_LIMIT));
        query.setQuery(sb.toString());

        List<BirthDeclaration> bdfList = new ArrayList<BirthDeclaration>();
        try {
            QueryResponse qr = solrIndexManager.getServer().query(query);
            SolrDocumentList docList = qr.getResults();
            Iterator<SolrDocument> iter = docList.iterator();
            while (iter.hasNext()) {
                SolrDocument doc = iter.next();
                BirthDeclaration bdf = birthDeclarationDAO.getById(
                    Long.parseLong(doc.getFieldValue(FIELD_ID_UKEY).toString()));
                if (bdf != null) {
                    bdfList.add(bdf);
                }
            }
        } catch (SolrServerException e) {
            handleException("Error performing search for birth records", ErrorCodes.INDEX_SEARCH_FAILURE, e);
        }
        return bdfList;
    }

    private boolean isSpecified(String s) {
        return s != null && s.trim().length() > 0;
    }

    private void handleException(String msg, int code, Exception e) {
        logger.error(msg, e);
        throw new CRSRuntimeException(msg, code, e);
    }

    public void indexAll() {

        // delete all existing
        try {
            solrIndexManager.getServer().deleteByQuery("*:*");
        } catch (Exception e) {
            logger.error("Error deleting existing records off Solr index");
        }

        List<BirthDeclaration> bdfList = birthDeclarationDAO.findAll();

        int count = 0;
        try {
            for (BirthDeclaration bdf : bdfList) {
                addRecord(bdf);
                count++;
            }

            solrIndexManager.getServer().optimize();
            solrIndexManager.getServer().commit();
            
            logger.debug("Successfully indexed : " + count + " documents..");

        // TODO we do not print the stack trace for now..
        } catch (SolrServerException e) {
            logger.error("Error from Solr server during re-indexing");
        } catch (IOException e) {
            logger.error("IO Exception encountered during re-indexing");
        } catch (Exception e) {
            logger.error("Unexpected Exception encountered during re-indexing");
        }
    }

    private void addRecord(BirthDeclaration bdf) throws SolrServerException, IOException {
        SolrInputDocument d = new SolrInputDocument();
        d.addField(FIELD_ID_UKEY, bdf.getIdUKey());

        // add registration info
        BirthRegisterInfo regInfo = bdf.getRegister();
        d.addField(FIELD_BDF_SERIAL_NO, regInfo.getBdfSerialNo());
        d.addField(FIELD_DATE_OF_REGISTRATION, regInfo.getDateOfRegistration());
        //d.addField("comments", regInfo.getComments());
        //d.addField("originalBCDateOfIssue", regInfo.getOriginalBCDateOfIssue());
        //d.addField("originalBCPlaceOfIssue", regInfo.getOriginalBCPlaceOfIssue());
        d.addField(FIELD_BIRTH_DISTRICT,
            regInfo.getBirthDistrict().getEnDistrictName() + " / " +
                regInfo.getBirthDistrict().getSiDistrictName() + " / " +
                regInfo.getBirthDistrict().getTaDistrictName() + " / ");
        d.addField(FIELD_BIRTH_DIVISION,
            regInfo.getBirthDivision().getEnDivisionName() + " / " +
                regInfo.getBirthDivision().getSiDivisionName() + " / " +
                regInfo.getBirthDivision().getTaDivisionName() + " / ");

        // add child details
        ChildInfo child = bdf.getChild();
        d.addField(FIELD_CHILD_FULL_NAME_ENGLISH, child.getChildFullNameEnglish());
        d.addField(FIELD_CHILD_FULL_NAME_OFFICIAL_LANG, child.getChildFullNameOfficialLang());
        d.addField(FIELD_DATE_OF_BIRTH, child.getDateOfBirth());
        d.addField(FIELD_BIRTH_AT_HOSPITAL, child.getBirthAtHospital());
        d.addField(FIELD_CHILD_GENDER, GenderUtil.getGenderString(child.getChildGender()));
        d.addField(FIELD_PIN, child.getPin());
        d.addField(FIELD_PLACE_OF_BIRTH, child.getPlaceOfBirth());
        //d.addField("numberOfChildrenBorn", child.getNumberOfChildrenBorn());

        // add mother details
        ParentInfo parent = bdf.getParent();
        if (parent != null) {
            d.addField(FIELD_MOTHER_FULL_NAME, parent.getMotherFullName());
            d.addField(FIELD_MOTHER_ADDRESS, parent.getMotherAddress());
            d.addField(FIELD_MOTHER_DOB, parent.getMotherDOB());
            d.addField(FIELD_MOTHER_NICOR_PIN, parent.getMotherNICorPIN());
            d.addField(FIELD_MOTHER_PASSPORT_NO, parent.getMotherPassportNo());
            d.addField(FIELD_MOTHER_PLACE_OF_BIRTH, parent.getMotherPlaceOfBirth());

            // add father details
            d.addField(FIELD_FATHER_FULL_NAME, parent.getFatherFullName());
            d.addField(FIELD_FATHER_DOB, parent.getFatherDOB());
            d.addField(FIELD_FATHER_NICOR_PIN, parent.getFatherNICorPIN());
            d.addField(FIELD_FATHER_PASSPORT_NO, parent.getFatherPassportNo());
            d.addField(FIELD_FATHER_PLACE_OF_BIRTH, parent.getFatherPlaceOfBirth());
        }

        // add informant info
        InformantInfo informant = bdf.getInformant();
        d.addField(FIELD_INFORMANT_NAME, informant.getInformantName());
        d.addField(FIELD_INFORMANT_ADDRESS, informant.getInformantAddress());
        d.addField(FIELD_INFORMANT_NICOR_PIN, informant.getInformantNICorPIN());

        // add grandfather and great grand father info
        GrandFatherInfo grandFatherInfo = bdf.getGrandFather();
        if (grandFatherInfo != null) {
            d.addField(FIELD_GRAND_FATHER_FULL_NAME, grandFatherInfo.getGrandFatherFullName());
            d.addField(FIELD_GRAND_FATHER_BIRTH_PLACE, grandFatherInfo.getGrandFatherBirthPlace());
            d.addField(FIELD_GRAND_FATHER_BIRTH_YEAR, grandFatherInfo.getGrandFatherBirthYear());

            d.addField(FIELD_GREAT_GRAND_FATHER_FULL_NAME, grandFatherInfo.getGreatGrandFatherFullName());
            d.addField(FIELD_GREAT_GRAND_FATHER_BIRTH_PLACE, grandFatherInfo.getGreatGrandFatherBirthPlace());
            d.addField(FIELD_GREAT_GRAND_FATHER_BIRTH_YEAR, grandFatherInfo.getGreatGrandFatherBirthYear());
        }

        solrIndexManager.getServer().add(d);
    }
}
