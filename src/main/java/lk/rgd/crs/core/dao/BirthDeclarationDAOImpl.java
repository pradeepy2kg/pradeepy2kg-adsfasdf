package lk.rgd.crs.core.dao;

import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * @author asankha
 */
public class BirthDeclarationDAOImpl extends BaseDAO implements BirthDeclarationDAO {

    @Transactional(propagation = Propagation.REQUIRED)
    public void addBirthDeclaration(BirthDeclaration bdf) {
        setBlankStringsAsNull(bdf);
        bdf.setLastUpdatedTime(new Date());
        bdf.setActiveRecord(true);
        em.persist(bdf);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBirthDeclaration(BirthDeclaration bdf) { 
        setBlankStringsAsNull(bdf);
        bdf.setLastUpdatedTime(new Date());
        em.merge(bdf);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteBirthDeclaration(long idUKey) {
        em.remove(getById(idUKey));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<BirthDeclaration> findAll() {
        Query q = em.createNamedQuery("findAll");
        return q.getResultList();
    }

    // TODO move to service class
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getConfirmationPrintPending(BDDivision birthDivision, int pageNo, int noOfRows, boolean printed) {
        Query q = em.createNamedQuery("filter.by.division.and.status").setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("status", printed ? BirthDeclaration.State.APPROVED : BirthDeclaration.State.DATA_ENTRY);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getPaginatedListForState(BDDivision birthDivision, int pageNo, int noOfRows, BirthDeclaration.State status) {
        Query q = em.createNamedQuery("filter.by.division.and.status").setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("status", status);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public BirthDeclaration getById(long bdfidUKey) {
        logger.debug("Get BDF by ID : {}", bdfidUKey);
        return em.find(BirthDeclaration.class, bdfidUKey);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getByDOBRangeandMotherNICorPIN(Date start, Date end, String motherNICorPIN) {
        Query q = em.createNamedQuery("get.by.dateOfBirth_range.and.motherNICorPIN");
        q.setParameter("start", start, TemporalType.DATE);
        q.setParameter("end", end, TemporalType.DATE);
        q.setParameter("motherNICorPIN", motherNICorPIN);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public BirthDeclaration getByBDDivisionAndSerialNo(BDDivision bdDivision, long bdfSerialNo) {
        Query q = em.createNamedQuery("get.by.bddivision.and.serialNo");
        q.setParameter("birthDivision", bdDivision);
        q.setParameter("bdfSerialNo", bdfSerialNo);
        return (BirthDeclaration) q.getSingleResult();
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getByBDDivisionStatusAndRegisterDateRange(BDDivision birthDivision,
            BirthDeclaration.State status, Date startDate, Date endDate, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("get.by.division.status.register.date").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("status", status);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getByBDDivisionStatusAndConfirmationReceiveDateRange(BDDivision birthDivision,
            BirthDeclaration.State status, Date startDate, Date endDate, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("get.by.division.status.confirmation.receive.date").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("status", status);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getByBirthDivision(BDDivision birthDivision) {
        Query q = em.createNamedQuery("get.by.bddivision");
        q.setParameter("birthDivision", birthDivision);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getUnconfirmedByRegistrationDate(Date date) {
        Query q = em.createNamedQuery("filter.by.unconfirmed.by.register.date");
        q.setParameter("date", date);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getHistoricalRecordsForBDDivisionAndSerialNo(BDDivision birthDivision, long serialNo) {
        Query q = em.createNamedQuery("get.historical.records.by.bddivision.and.serialNo");
        q.setParameter("birthDivision",birthDivision);
        q.setParameter("bdfSerialNo",serialNo);
        return q.getResultList();
    }

    /**
     * Sets fields that are "" or one or more spaces to null
     *
     * @param bdf the BDF to update
     */
    private void setBlankStringsAsNull(BirthDeclaration bdf) {
        BirthRegisterInfo register = bdf.getRegister();
        if (isBlankString(register.getComments())) {
            register.setComments(null);
        }

        ChildInfo child = bdf.getChild();
        if (isBlankString(child.getChildFullNameEnglish())) {
            child.setChildFullNameEnglish(null);
        }
        if (isBlankString(child.getChildFullNameOfficialLang())) {
            child.setChildFullNameOfficialLang(null);
        }
        if (isBlankString(child.getPlaceOfBirth())) {
            child.setPlaceOfBirth(null);
        }
        if (isBlankString(child.getPlaceOfBirthEnglish())) {
            child.setPlaceOfBirthEnglish(null);
        }

        ParentInfo parent = bdf.getParent();
        if (isBlankString(parent.getFatherNICorPIN())) {
            parent.setFatherNICorPIN(null);
        }
        if (isBlankString(parent.getFatherPassportNo())) {
            parent.setFatherPassportNo(null);
        }
        if (isBlankString(parent.getFatherPlaceOfBirth())) {
            parent.setFatherPlaceOfBirth(null);
        }
        if (isBlankString(parent.getFatherFullName())) {
            parent.setFatherFullName(null);
        }

        if (isBlankString(parent.getMotherNICorPIN())) {
            parent.setMotherNICorPIN(null);
        }
        if (isBlankString(parent.getMotherPassportNo())) {
            parent.setMotherPassportNo(null);
        }
        if (isBlankString(parent.getMotherFullName())) {
            parent.setMotherFullName(null);
        }
        if (isBlankString(parent.getMotherPlaceOfBirth())) {
            parent.setMotherPlaceOfBirth(null);
        }
        if (isBlankString(parent.getMotherAdmissionNo())) {
            parent.setMotherAdmissionNo(null);
        }
        if (isBlankString(parent.getMotherAddress())) {
            parent.setMotherAddress(null);
        }
        if (isBlankString(parent.getMotherPhoneNo())) {
            parent.setMotherPhoneNo(null);
        }
        if (isBlankString(parent.getMotherEmail())) {
            parent.setMotherEmail(null);
        }

        MarriageInfo marriage = bdf.getMarriage();
        if (isBlankString(marriage.getPlaceOfMarriage())) {
            marriage.setPlaceOfMarriage(null);
        }

        GrandFatherInfo grandFather = bdf.getGrandFather();
        if (isBlankString(grandFather.getGrandFatherFullName())) {
            grandFather.setGrandFatherFullName(null);
        }
        if (isBlankString(grandFather.getGrandFatherNICorPIN())) {
            grandFather.setGrandFatherNICorPIN(null);
        }
        if (isBlankString(grandFather.getGrandFatherBirthPlace())) {
            grandFather.setGrandFatherBirthPlace(null);
        }
        if (isBlankString(grandFather.getGreatGrandFatherFullName())) {
            grandFather.setGreatGrandFatherFullName(null);
        }
        if (isBlankString(grandFather.getGreatGrandFatherNICorPIN())) {
            grandFather.setGreatGrandFatherNICorPIN(null);
        }
        if (isBlankString(grandFather.getGreatGrandFatherBirthPlace())) {
            grandFather.setGreatGrandFatherBirthPlace(null);
        }

        InformantInfo informant = bdf.getInformant();
        if (isBlankString(informant.getInformantNICorPIN())) {
            informant.setInformantNICorPIN(null);
        }
        if (isBlankString(informant.getInformantPhoneNo())) {
            informant.setInformantPhoneNo(null);
        }
        if (isBlankString(informant.getInformantEmail())) {
            informant.setInformantEmail(null);
        }

        NotifyingAuthorityInfo notifyingAuthority = bdf.getNotifyingAuthority();
        if (isBlankString(notifyingAuthority.getNotifyingAuthorityAddress())) {
            notifyingAuthority.setNotifyingAuthorityAddress(null);
        }
    }

    private static boolean isBlankString(String s) {
        return s != null && s.trim().length() == 0;
    }
}
