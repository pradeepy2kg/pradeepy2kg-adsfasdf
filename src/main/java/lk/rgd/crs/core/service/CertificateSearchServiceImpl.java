package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.dao.CertificateSearchDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.CertificateSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The central service managing the Certificate Search process
 *
 * @author Chathuranga Withana
 */
public class CertificateSearchServiceImpl implements CertificateSearchService {

    private static final Logger logger = LoggerFactory.getLogger(CertificateSearchServiceImpl.class);
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final CertificateSearchDAO certificateSearchDAO;
    private final BirthRecordsIndexer birthRecordsIndexer;

    public CertificateSearchServiceImpl(
        BirthDeclarationDAO birthDeclarationDAO, CertificateSearchDAO certificateSearchDAO,
        BirthRecordsIndexer birthRecordsIndexer) {
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.certificateSearchDAO = certificateSearchDAO;
        this.birthRecordsIndexer = birthRecordsIndexer;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public boolean isValidCertificateSearchApplicationNo(DSDivision dsDivision, String applicationNo) {

        logger.debug("Get record by DSDivision ID : {} and Application No : {}", dsDivision.getDsDivisionUKey(),
            applicationNo);
        CertificateSearch cs = certificateSearchDAO.getByDSDivisionAndApplicationNo(dsDivision, applicationNo);
        if (cs != null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> performBirthCertificateSearch(CertificateSearch cs, User user) {

        logger.debug("Birth certificate search started");
        // TODO Before search verify certificate type is BIRTH

        List<BirthDeclaration> results = new ArrayList<BirthDeclaration>();
        BirthDeclaration exactRecord = null;
        CertificateSearch existing = null;

        SearchInfo search = cs.getSearch();
        CertificateInfo certificate = cs.getCertificate();

        if (certificate.getApplicationNo() != null && certificate.getDsDivision() != null) {
            existing = certificateSearchDAO.getByDSDivisionAndApplicationNo(certificate.getDsDivision(), certificate.getApplicationNo());
        }

        if (existing == null) {
            if (search.getCertificateNo() != null) {
                logger.debug("Search narrowed against certificate IDUKey : {}", search.getCertificateNo());
                exactRecord = birthDeclarationDAO.getById(search.getCertificateNo());
                if (exactRecord != null) {
                    final BirthDeclaration.State currentState = exactRecord.getRegister().getStatus();
                    if (BirthDeclaration.State.ARCHIVED_CERT_GENERATED == currentState ||
                        BirthDeclaration.State.ARCHIVED_CERT_PRINTED == currentState) {
                        results = new ArrayList<BirthDeclaration>();
                        results.add(exactRecord);
                    }
                }
            }

            // add any matches from Solr search, except for the exact match
            for (BirthDeclaration bdf : birthRecordsIndexer.searchBirthRecords(cs)) {
                if (exactRecord == null || exactRecord.getIdUKey() != bdf.getIdUKey()) {
                    results.add(bdf);
                }
            }

            // set user perform searching and the timestamp
            cs.getCertificate().setSearchUser(user);
            cs.getCertificate().setSearchPerformDate(new Date());
            cs.getSearch().setResultsFound(results.size());

            certificateSearchDAO.addCertificateSearch(cs);
            logger.debug("Birth certificate search completed and recorded as SearchUKey : {} Results found : {}",
                cs.getIdUKey(), results.size());

        } else {
            handleException("The birth certificate search DS Division/Application number is a duplicate : " +
                certificate.getDsDivision().getDsDivisionUKey() + " " + certificate.getApplicationNo(), ErrorCodes.INVALID_DATA);
        }

        return results;
    }

    /**
     * @inheritDoc
     */
    public List<DeathRegister> performDeathCertificateSearch(CertificateSearch cs, User user) {
        return null;
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
