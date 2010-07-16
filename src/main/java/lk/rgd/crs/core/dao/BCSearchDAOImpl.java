package lk.rgd.crs.core.dao;

import lk.rgd.crs.api.dao.BCSearchDAO;
import lk.rgd.crs.api.domain.BirthCertificateSearch;
import lk.rgd.common.core.dao.BaseDAO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

/**
 * @author Chathuranga Withana
 */
public class BCSearchDAOImpl extends BaseDAO implements BCSearchDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBirthCertificateSearch(BirthCertificateSearch bcs) {
        setBlankStringsAsNull(bcs);
        changeToUpper(bcs);
        em.persist(bcs);
    }

    /**
     * Ensure persisted fields in upper case
     *
     * @param bcs
     */
    private void changeToUpper(BirthCertificateSearch bcs) {
        if (bcs.getApplicantFullName() != null) {
            bcs.setApplicantFullName(bcs.getApplicantFullName().toUpperCase());
        }
        if (bcs.getApplicantAddress() != null) {
            bcs.setApplicantAddress(bcs.getApplicantAddress().toUpperCase());
        }
        if (bcs.getFullName() != null) {
            bcs.setFullName(bcs.getFullName().toUpperCase());
        }
        if (bcs.getFatherFullName() != null) {
            bcs.setFatherFullName(bcs.getFatherFullName().toUpperCase());
        }
        if (bcs.getMotherFullName() != null) {
            bcs.setMotherFullName(bcs.getMotherFullName().toUpperCase());
        }
        if (bcs.getPlaceOfBirth() != null) {
            bcs.setPlaceOfBirth(bcs.getPlaceOfBirth().toUpperCase());
        }
    }

    /**
     * Sets fields that are "" or one or more spaces to null
     *
     * @param bcs
     */
    private void setBlankStringsAsNull(BirthCertificateSearch bcs) {
        if (isBlankString(bcs.getFullName())) {
            bcs.setFullName(null);
        }
        if (isBlankString(bcs.getFatherFullName())) {
            bcs.setFatherFullName(null);
        }
        if (isBlankString(bcs.getMotherFullName())) {
            bcs.setMotherFullName(null);
        }
        if (isBlankString(bcs.getPlaceOfBirth())) {
            bcs.setPlaceOfBirth(null);
        }
    }

    private boolean isBlankString(String s) {
        return s != null && s.trim().length() == 0;
    }
}
