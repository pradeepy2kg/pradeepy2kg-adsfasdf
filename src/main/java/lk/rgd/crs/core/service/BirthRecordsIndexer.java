package lk.rgd.crs.core.service;

import lk.rgd.common.core.index.SolrIndexManager;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.*;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author asankha
 */
public class BirthRecordsIndexer {

    private static final Logger logger = LoggerFactory.getLogger(BirthRecordsIndexer.class);

    private final SolrIndexManager solrIndexManager;
    private final BirthDeclarationDAO birthDeclarationDAO;

    public BirthRecordsIndexer(SolrIndexManager solrIndexManager, BirthDeclarationDAO birthDeclarationDAO) {
        this.solrIndexManager = solrIndexManager;
        this.birthDeclarationDAO = birthDeclarationDAO;
    }

    public void indexAll() {
        List<BirthDeclaration> bdfList = birthDeclarationDAO.findAll();

        int count = 0;
        try {
            for (BirthDeclaration bdf : bdfList) {

                SolrInputDocument d = new SolrInputDocument();
                d.addField("idUKey", bdf.getIdUKey());

                // add registration info
                BirthRegisterInfo regInfo = bdf.getRegister();
                d.addField("bdfSerialNo", regInfo.getBdfSerialNo());
                d.addField("dateOfRegistration", regInfo.getDateOfRegistration());
                //d.addField("comments", regInfo.getComments());
                d.addField("originalBCDateOfIssue", regInfo.getOriginalBCDateOfIssue());
                //d.addField("originalBCPlaceOfIssue", regInfo.getOriginalBCPlaceOfIssue());
                d.addField("birthDistrict",
                    regInfo.getBirthDistrict().getEnDistrictName() + " " +
                        regInfo.getBirthDistrict().getSiDistrictName() + " " +
                        regInfo.getBirthDistrict().getTaDistrictName() + " ");
                d.addField("birthDivision",
                    regInfo.getBirthDivision().getEnDivisionName() + " " +
                        regInfo.getBirthDivision().getSiDivisionName() + " " +
                        regInfo.getBirthDivision().getTaDivisionName() + " ");

                // add child details
                ChildInfo child = bdf.getChild();
                d.addField("childFullNameEnglish", child.getChildFullNameEnglish());
                d.addField("childFullNameOfficialLang", child.getChildFullNameOfficialLang());
                d.addField("dateOfBirth", child.getDateOfBirth());
                d.addField("birthAtHospital", child.getBirthAtHospital());
                d.addField("childGender", GenderUtil.getGenderString(child.getChildGender()));
                d.addField("pin", child.getPin());
                d.addField("placeOfBirth", child.getPlaceOfBirth());
                //d.addField("numberOfChildrenBorn", child.getNumberOfChildrenBorn());

                // add mother details
                ParentInfo parent = bdf.getParent();
                if (parent != null) {
                    d.addField("motherFullName", parent.getMotherFullName());
                    d.addField("motherAddress", parent.getMotherAddress());
                    d.addField("motherDOB", parent.getMotherDOB());
                    d.addField("motherNICorPIN", parent.getMotherNICorPIN());
                    d.addField("motherPassportNo", parent.getMotherPassportNo());
                    d.addField("motherPlaceOfBirth", parent.getMotherPlaceOfBirth());

                    // add father details
                    d.addField("fatherFullName", parent.getFatherFullName());
                    d.addField("fatherDOB", parent.getFatherDOB());
                    d.addField("fatherNICorPIN", parent.getFatherNICorPIN());
                    d.addField("fatherPassportNo", parent.getFatherPassportNo());
                    d.addField("fatherPlaceOfBirth", parent.getFatherPlaceOfBirth());
                }

                // add informant info
                InformantInfo informant = bdf.getInformant();
                d.addField("informantName", informant.getInformantName());
                d.addField("informantAddress", informant.getInformantAddress());
                d.addField("informantNICorPIN", informant.getInformantNICorPIN());

                // add grandfather and great grand father info
                GrandFatherInfo grandFatherInfo = bdf.getGrandFather();
                if (grandFatherInfo != null) {
                    d.addField("grandFatherFullName", grandFatherInfo.getGrandFatherFullName());
                    d.addField("grandFatherBirthPlace", grandFatherInfo.getGrandFatherBirthPlace());
                    d.addField("grandFatherBirthYear", grandFatherInfo.getGrandFatherBirthYear());

                    d.addField("greatGrandFatherFullName", grandFatherInfo.getGreatGrandFatherFullName());
                    d.addField("greatGrandFatherBirthPlace", grandFatherInfo.getGreatGrandFatherBirthPlace());
                    d.addField("greatGrandFatherBirthYear", grandFatherInfo.getGreatGrandFatherBirthYear());
                }

                solrIndexManager.getServer().add(d);
                count++;
            }

            solrIndexManager.getServer().commit();
            logger.info("Successfully indexed : " + count + " documents..");

        // TODO we do not print the stack trace for now..
        } catch (SolrServerException e) {
            logger.error("Error from Solr server during re-indexing");
        } catch (IOException e) {
            logger.error("IO Exception encountered during re-indexing");
        } catch (Exception e) {
            logger.error("Unexpected Exception encountered during re-indexing");
        }
    }
}
