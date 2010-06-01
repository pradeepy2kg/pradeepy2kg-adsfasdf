package lk.rgd.crs.core.dao;

import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedQuery;
import javax.persistence.Query;
import java.util.List;
import java.util.ArrayList;
import java.sql.Date;

/**
 * @author asankha
 */
public class BirthDeclarationDAOImpl extends BaseDAO implements BirthDeclarationDAO {

    @Transactional(propagation = Propagation.REQUIRED)
    public void addBirthDeclaration(BirthDeclaration bdf) {
        em.persist(bdf);
    }

    public BirthDeclaration getBirthDeclaration(BDDivision birthDivision, String bdfSerialNo) {
        //em.find(BirthDeclaration.class, )
        return null;
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getConfirmationPrintPending(BDDivision birthDivision, boolean printed) {

        Query q = null;
        if (printed) {
            q = em.createNamedQuery("confirmation.print.completed");
        } else {
            q = em.createNamedQuery("confirmation.print.pending");
        }
        q.setParameter("birthDivision", birthDivision);
        return q.getResultList();
    }

    public List<BirthDeclaration> getBirthRegistrationPending(int division, boolean isExpired) {
        //sample record for testing
        BirthDeclaration bd = new BirthDeclaration();
        bd.setChildFullNameEnglish("kamal");
        bd.setBdfSerialNo("A25");
        bd.setStatus(4);

        BirthDeclaration bd1 = new BirthDeclaration();
        bd1.setChildFullNameEnglish("Nimal");
        bd1.setBdfSerialNo("A458");
        bd1.setStatus(5);
        List<BirthDeclaration> a = new ArrayList<BirthDeclaration>();
        a.add(bd);
        a.add(bd1);
        return a;
    }
}
