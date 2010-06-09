package lk.rgd.crs.core.dao;

import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.api.domain.User;
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

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getConfirmationPrintPending(BDDivision birthDivision, boolean printed) {

        Query q = em.createNamedQuery("filter.by.division.and.status");
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("status", printed ? 2 : 1);
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
     * Returns the Birth Declaration object for a given bdf serialNo
     *
     * @param bdfSerialNo bdfSerialNo given to the Birth Declarion
     * @Return BirthDeclaration
     */
    public BirthDeclaration getBySerialNo(String bdfSerialNo) {
        Query q = em.createNamedQuery("get.by.serialNo.pending.approval");
        q.setParameter("bdfSerialNo", bdfSerialNo);
        return (BirthDeclaration) q.getSingleResult();
    }
}
