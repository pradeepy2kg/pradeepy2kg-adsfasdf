package lk.rgd.crs.core.dao;

import lk.rgd.common.api.domain.DSDivision;
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
    public List<Assignment> getAssignmentsByTypeAndDSDivision(int dsDivisionUKey, Assignment.Type type, boolean active) {
        Query q = null;
        switch (type) {
            case BIRTH:
                q = em.createNamedQuery("get.birth.assignments.by.state.type.and.dsdivision");
                break;
            case DEATH:
                q = em.createNamedQuery("get.death.assignments.by.state.type.and.dsdivision");
                break;
            default:
                q = em.createNamedQuery("get.marriage.assignments.by.state.type.and.dsdivision");
                break;
        }
        q.setParameter("dsDivisionUKey", dsDivisionUKey);
        q.setParameter("type", type);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /*  //todo remove no usage found
    *//**
     * @inheritDoc
     *//*
    public List<Assignment> getAssignmentsByStateAndDSDivision(int dsDivisionUKey, boolean active) {
        Query q = em.createNamedQuery("get.assignments.by.state.and.dsdivision");
        q.setParameter("dsDivisionUKey", dsDivisionUKey);
        q.setParameter("active", active);
        return q.getResultList();
    }*/
    public List<Assignment> getAllAssignments(User user) {
        Query q = em.createNamedQuery("get.all.assignments");
        return q.getResultList();
    }

    //todo
    public List<Assignment> getAllAssignmentByDistricAndType(int districtId, Assignment.Type type, boolean active) {
        Query q = null;
        List<Assignment> result = null;
        if (type != null) {
            switch (type) {
                case BIRTH:
                    q = em.createNamedQuery("get.birth.assignments.by.state.type.and.districtID");
                    break;
                case DEATH:
                    q = em.createNamedQuery("get.death.assignments.by.state.type.and.districtID");
                    break;
                default:
                    q = em.createNamedQuery("get.marriage.assignments.by.state.type.and.districtID");
                    break;
            }
            q.setParameter("dsDivisionUKey", districtId);
            q.setParameter("type", type);
            q.setParameter("active", active);
            result = q.getResultList();
        } else {
            //requesting any type
            q = em.createNamedQuery("get.assignments.by.state.all.type.and.districtID");
            result = q.getResultList();
        }
        return result;
    }

}
