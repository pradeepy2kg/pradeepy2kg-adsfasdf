package lk.rgd.crs.core.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.AssignmentDAO;
import lk.rgd.crs.api.domain.Assignment;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.Registrar;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @author asankha
 */
public class AssignmentDAOImpl extends BaseDAO implements AssignmentDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Assignment getById(long idUKey) {
        return em.find(Assignment.class, idUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addAssignment(Assignment assignment, User user) {
        final Date now = new Date();
        assignment.getLifeCycleInfo().setActive(true);
        assignment.getLifeCycleInfo().setCreatedTimestamp(now);
        assignment.getLifeCycleInfo().setCreatedUser(user);
        assignment.getLifeCycleInfo().setLastUpdatedTimestamp(now);
        assignment.getLifeCycleInfo().setLastUpdatedUser(user);
        em.persist(assignment);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateAssignment(Assignment assignment, User user) {
        assignment.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        assignment.getLifeCycleInfo().setLastUpdatedUser(user);
        em.merge(assignment);
    }

    /**
     * @inheritDoc
     */
    public List<Assignment> getAssignmentsForRegistrar(Registrar registrar) {
        Query q = em.createNamedQuery("get.by.registrarUKey");
        q.setParameter("registrarUKey", registrar.getRegistrarUKey());
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public List<Assignment> getAllAssignments(User user) {
        Query q = em.createNamedQuery("get.all.assignments");
        return q.getResultList();
    }
}
