package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.dao.CertificateSearchDAO;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
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
    private final DeathRegisterDAO deathRegisterDAO;
    private final CertificateSearchDAO certificateSearchDAO;
    private final BirthRecordsIndexer birthRecordsIndexer;
    private final DeathRecordsIndexer deathRecordsIndexer;
    private final DSDivisionDAO dsDivisionDAO;
    private final DistrictDAO districtDAO;

    public CertificateSearchServiceImpl(
            BirthDeclarationDAO birthDeclarationDAO, DeathRegisterDAO deathRegisterDAO,
            CertificateSearchDAO certificateSearchDAO, BirthRecordsIndexer birthRecordsIndexer,
            DeathRecordsIndexer deathRecordsIndexer, DSDivisionDAO dsDivisionDAO, DistrictDAO districtDAO) {
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.deathRegisterDAO = deathRegisterDAO;
        this.certificateSearchDAO = certificateSearchDAO;
        this.birthRecordsIndexer = birthRecordsIndexer;
        this.deathRecordsIndexer = deathRecordsIndexer;
        this.dsDivisionDAO = dsDivisionDAO;
        this.districtDAO = districtDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public boolean isValidCertificateSearchApplicationNo(DSDivision dsDivision, String applicationNo) {

        logger.debug("Get record by DSDivision ID : {} and Application No : {}", dsDivision.getDsDivisionUKey(),
                applicationNo);
        CertificateSearch cs = certificateSearchDAO.getByDSDivisionAndApplicationNo(dsDivision, applicationNo);
        return (cs == null);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> performBirthCertificateSearch(CertificateSearch cs, User user, int dsDivisionUKey, int birthDistrictId) {

        //TODO validations for CertificateSearch
        logger.debug("Birth certificate search started");
        validateCertificateType(cs, CertificateSearch.CertificateType.BIRTH);

        List<BirthDeclaration> results = new ArrayList<BirthDeclaration>();
        BirthDeclaration exactRecord = null;
        CertificateSearch existing = null;

        cs.getSearch().setSearchRecordStatus(BirthDeclaration.State.ARCHIVED_CERT_PRINTED.toString());
        SearchInfo search = cs.getSearch();
        CertificateInfo certificate = cs.getCertificate();

        if (certificate.getApplicationNo() != null && certificate.getDsDivision() != null) {
            existing = certificateSearchDAO.getByDSDivisionAndApplicationNo(certificate.getDsDivision(),
                    certificate.getApplicationNo());
        }

        if (existing == null) {

            // add exact match using Birth Declaration idUKey
            if (search.getCertificateNo() != null) {
                logger.debug("Search narrowed against Birth certificate IDUKey : {}", search.getCertificateNo());
                exactRecord = birthDeclarationDAO.getById(search.getCertificateNo());
                addMatchingBirth(results, exactRecord, search);
            }

            // add exact match using Generated Identification Number
            if (exactRecord == null && search.getSearchPIN() != null) {
                logger.debug("Search narrowed against Child/Person PIN");
                exactRecord = birthDeclarationDAO.getByPINorNIC(search.getSearchPIN());
                addMatchingBirth(results, exactRecord, search);
            }

            // add exact match using Birth Declaration serial no and BDDivision
            if (exactRecord == null && search.getSearchSerialNo() != null && search.getBdDivision() != null) {
                logger.debug("Search narrowed against Birth declaration Serial No : {} and BDDivision : {}",
                        search.getSearchSerialNo(), search.getBdDivision().getBdDivisionUKey());
                exactRecord = birthDeclarationDAO.getActiveRecordByBDDivisionAndSerialNo(
                        search.getBdDivision(), search.getSearchSerialNo());
                addMatchingBirth(results, exactRecord, search);
            }

            //add exact match using DSDivision
            if (search.getCertificateNo() == null && search.getSearchPIN() == null) {
                if (results.size() == 0 && exactRecord == null) {
                    if (dsDivisionUKey != 0) {
                        logger.debug("Search narrowed against DSDivision");
                        for (BirthDeclaration bdf : birthDeclarationDAO.getByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionUKey))) {
                            results.add(bdf);
                        }
                        logger.debug("BirthDeclaration List:{}", results.size());
                    } else if (birthDistrictId != 0 && dsDivisionUKey == 0) {
                        logger.debug("Search narrowed against DSDivision - for all DSDivisions");
                        for (BirthDeclaration bdf : birthDeclarationDAO.getByDistrict(districtDAO.getDistrict(birthDistrictId))) {
                            results.add(bdf);
                        }
                    } else if (birthDistrictId == 0) {
                        logger.debug("Search narrowed against DSDivision - for all Districts");
                        for (BirthDeclaration bdf : birthDeclarationDAO.findAll()) {
                            results.add(bdf);
                        }
                    }
                }

                // add any matches from Solr search, except for the exact match
                if (results.size() == 0) {
                    for (BirthDeclaration bdf : birthRecordsIndexer.searchBirthRecords(cs)) {
                        if (exactRecord == null || exactRecord.getIdUKey() != bdf.getIdUKey()) {
                            results.add(bdf);
                        }
                    }
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
    public List<DeathRegister> performDeathCertificateSearch(CertificateSearch cs, User user, int dsDivisionUKey, int birthDistrictId) {

        //TODO validations for CertificateSearch
        logger.debug("Death certificate search started");
        validateCertificateType(cs, CertificateSearch.CertificateType.DEATH);

        List<DeathRegister> results = new ArrayList<DeathRegister>();
        List<DeathRegister> possibleRecords;
        DeathRegister exactRecord = null;
        CertificateSearch existing = null;

        cs.getSearch().setSearchRecordStatus(DeathRegister.State.ARCHIVED_CERT_GENERATED.toString());
        SearchInfo search = cs.getSearch();
        CertificateInfo certificate = cs.getCertificate();

        if (certificate.getApplicationNo() != null && certificate.getDsDivision() != null) {
            existing = certificateSearchDAO.getByDSDivisionAndApplicationNo(certificate.getDsDivision(),
                    certificate.getApplicationNo());
        }

        if (existing == null) {

            // add exact match using Death Register idUKey
            if (search.getCertificateNo() != null) {
                logger.debug("Search narrowed against Death certificate IDUKey : {}", search.getCertificateNo());
                exactRecord = deathRegisterDAO.getById(search.getCertificateNo());
                addMatchingDeath(results, exactRecord, search);
            }

            // add exact match using Identification Number
            if (exactRecord == null && search.getSearchPIN() != null) {
                logger.debug("Search narrowed against Person PIN");
                possibleRecords =
                        deathRegisterDAO.getDeathRegisterByDeathPersonPINorNIC(search.getSearchPIN().toString());
                for (DeathRegister record : possibleRecords) {
                    addMatchingDeath(results, record, search);
                }
            }

            // add exact match using Death Register serial no and BDDivision
            if (exactRecord == null && search.getSearchSerialNo() != null && search.getBdDivision() != null) {
                logger.debug("Search narrowed against Death declaration Serial No : {} and BDDivision : {}",
                        search.getSearchSerialNo(), search.getBdDivision().getBdDivisionUKey());
                exactRecord = deathRegisterDAO.getActiveRecordByBDDivisionAndDeathSerialNo(
                        search.getBdDivision(), search.getSearchSerialNo());
                addMatchingDeath(results, exactRecord, search);
            }

            // add any matches from Solr search, except for the exact match
            for (DeathRegister ddf : deathRecordsIndexer.searchDeathRecords(cs)) {
                if (exactRecord == null || exactRecord.getIdUKey() != ddf.getIdUKey()) {
                    results.add(ddf);
                }
            }

            // set user perform searching and the timestamp
            cs.getCertificate().setSearchUser(user);
            cs.getCertificate().setSearchPerformDate(new Date());
            cs.getSearch().setResultsFound(results.size());

            certificateSearchDAO.addCertificateSearch(cs);
            logger.debug("Death certificate search completed and recorded as SearchUKey : {} Results found : {}",
                    cs.getIdUKey(), results.size());

        } else {
            handleException("The death certificate search DS Division/Application number is a duplicate : " +
                    certificate.getDsDivision().getDsDivisionUKey() + " " + certificate.getApplicationNo(), ErrorCodes.INVALID_DATA);
        }

        return results;
    }

    /**
     * Add matching birth record to the results list
     *
     * @param results     the results list which holds matching birth records
     * @param exactRecord the exact record which exist in the database
     * @param search      the search info bean which consist searching info
     */
    private void addMatchingBirth(List<BirthDeclaration> results, BirthDeclaration exactRecord, SearchInfo search) {
        if (exactRecord != null) {
            final BirthDeclaration.State currentState = exactRecord.getRegister().getStatus();
            if (BirthDeclaration.State.ARCHIVED_CERT_PRINTED == currentState) {
                results.add(exactRecord);
            } else {
                handleException("The birth declaration state is invalid for IDUKey : " +
                        search.getCertificateNo() + " " + currentState, ErrorCodes.INVALID_DATA);
            }
        }
    }

    /**
     * Add matching death record to the results list
     *
     * @param results     the results list which holds matching death records
     * @param exactRecord the exact record which exist in the database
     * @param search      the search info bean which consist searching info
     */
    private void addMatchingDeath(List<DeathRegister> results, DeathRegister exactRecord, SearchInfo search) {
        if (exactRecord != null) {
            final DeathRegister.State currentState = exactRecord.getStatus();
            if (DeathRegister.State.ARCHIVED_CERT_GENERATED == currentState) {
                results.add(exactRecord);
            } else {
                handleException("The death declaration state is invalid for IDUKey : " +
                        search.getCertificateNo() + " " + currentState, ErrorCodes.INVALID_DATA);
            }
        }
    }

    private void validateCertificateType(CertificateSearch cs, CertificateSearch.CertificateType certificateType) {
        if (certificateType != cs.getCertificate().getCertificateType()) {
            handleException("Certificate type : " + cs.getCertificate().getCertificateType() + ", but required : " +
                    certificateType + " for Certificate Search AppNo/DSDivision : " + cs.getCertificate().getApplicationNo() +
                    " , " + cs.getCertificate().getDsDivision().getDsDivisionUKey(), ErrorCodes.ILLEGAL_STATE);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Certificate type checking for Certificate AppNo/DSDivision : " +
                    cs.getCertificate().getApplicationNo() + " , " + cs.getCertificate().getDsDivision().getDsDivisionUKey()
                    + " passed for Certificate type : " + cs.getCertificate().getCertificateType());
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}