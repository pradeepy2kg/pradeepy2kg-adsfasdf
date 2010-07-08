package lk.rgd.crs.core.dao;

import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
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
        em.persist(bdf);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBirthDeclaration(BirthDeclaration bdf) {
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
}
