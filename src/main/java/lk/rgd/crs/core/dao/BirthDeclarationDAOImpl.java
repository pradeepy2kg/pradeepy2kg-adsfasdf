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
        if (bdf.getChild().getBirthDistrict().getDistrictId() !=
            bdf.getChild().getDsDivision().getDistrict().getDistrictId()) {
            throw new IllegalArgumentException(
                "The District for the Birth Registration Division and the D.S. Division are different");
        }
        em.persist(bdf);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getConfirmationPrintPending(BDDivision birthDivision, boolean printed) {

        Query q = em.createNamedQuery("filter.by.division.and.status");
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("status", printed ? 2 : 1);
        return q.getResultList();
    }

    public List<BirthDeclaration> getConfirmationApprovalPending(BDDivision birthDivision, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("confirmation.pending.approval").setFirstResult((pageNo-1)*noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        return q.getResultList();
    }
}
