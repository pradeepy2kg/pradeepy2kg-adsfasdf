package lk.rgd.crs.core.dao;

import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BirthDeclaration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedQuery;
import javax.persistence.Query;
import java.util.List;

/**
 * @author asankha
 */
public class BirthDeclarationDAOImpl extends BaseDAO implements BirthDeclarationDAO {

    @Transactional(propagation = Propagation.REQUIRED)
    public void addBirthDeclaration(BirthDeclaration bdf) {
        em.persist(bdf);
    }

    public BirthDeclaration getBirthDeclaration(int birthDistrict, int birthDivision, String bdfSerialNo) {
        //em.find(BirthDeclaration.class, )
        return null;
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getConfirmationPrintPending(int birthDistrict, int birthDivision, boolean includeAlreadyPrinted) {

        Query q = null;
        if (includeAlreadyPrinted) {
            q = em.createNamedQuery("confirmation.printed");
        } else {
            q = em.createNamedQuery("confirmation.print.pending");
        }
        q.setParameter("birthDistrict", birthDistrict);
        q.setParameter("birthDivision", birthDivision);
        return q.getResultList();
    }
}
