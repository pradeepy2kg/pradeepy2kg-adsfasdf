package lk.rgd.crs.core.dao;

import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
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

    // TODO move to service class
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getConfirmationPrintPending(BDDivision birthDivision, int pageNo, int noOfRows, boolean printed) {
        Query q = em.createNamedQuery("filter.by.division.and.status").setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("status", printed ? BirthDeclaration.State.APPROVED : BirthDeclaration.State.DATA_ENTRY);
        return q.getResultList();
    }

    public List<BirthDeclaration> getConfirmationApprovalPending(BDDivision birthDivision, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("confirmation.pending.approval").setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        return q.getResultList();
    }

    public List<BirthDeclaration> getDeclarationApprovalPending(BDDivision birthDivision, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("declaration.pending.approval").setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        return q.getResultList();
    }

    /**
     * Returns the Birth Declaration object for a given Id
     *
     * @param bdfidUKey Birth Declarion Id for the given declaration
     * @Return BirthDeclaration
     */
    public BirthDeclaration getById(long bdfidUKey) {
        logger.debug("Get BDF by ID : {}", bdfidUKey);
        Query q = em.createNamedQuery("get.by.id");
        q.setParameter("bdfidUKey", bdfidUKey);
        return (BirthDeclaration) q.getSingleResult();
    }

    public List<BirthDeclaration> getByDOBandMotherNICorPIN(Date dateOfBirth, String motherNICorPIN) {
        Query q = em.createNamedQuery("get.by.dateOfBirth.and.motherNICorPIN");
        q.setParameter("dateOfBirth", dateOfBirth);
        q.setParameter("motherNICorPIN", motherNICorPIN);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public BirthDeclaration getByBDDivisionAndSerialNo(BDDivision bdDivision, long bdfSerialNo) {
        Query q = em.createNamedQuery("get.by.serialNo.pending.approval");
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
}
