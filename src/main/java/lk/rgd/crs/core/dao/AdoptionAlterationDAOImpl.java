package lk.rgd.crs.core.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.AdoptionAlterationDAO;
import lk.rgd.crs.api.domain.AdoptionAlteration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @author Duminda Dharmakeerthi
 */
public class AdoptionAlterationDAOImpl extends BaseDAO implements AdoptionAlterationDAO {

    @Transactional(propagation = Propagation.MANDATORY)
    public void addAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        adoptionAlteration.setStatus(AdoptionAlteration.State.DATA_ENTRY);
        adoptionAlteration.getLifeCycleInfo().setCreatedTimestamp(new Date());
        adoptionAlteration.getLifeCycleInfo().setCreatedUser(user);
        adoptionAlteration.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        adoptionAlteration.getLifeCycleInfo().setLastUpdatedUser(user);
        adoptionAlteration.getLifeCycleInfo().setActiveRecord(true);
        em.persist(adoptionAlteration);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        adoptionAlteration.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        adoptionAlteration.getLifeCycleInfo().setLastUpdatedUser(user);
        em.merge(adoptionAlteration);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteAdoptionAlteration(long idUKey, User user) {
        em.remove(getAdoptionAlterationByIdUKey(idUKey));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public AdoptionAlteration getAdoptionAlterationByIdUKey(long idUkey) {
        Query q = em.createNamedQuery("getAdoptionAlterationByIdUKey");
        q.setParameter("idUKey", idUkey);
        try {
            return (AdoptionAlteration) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AdoptionAlteration> getAllAdoptionAlterationRecords() {
        Query q = em.createNamedQuery("getAllAdoptionAlterationRecords");
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AdoptionAlteration> getAdoptionAlterationsByStatus(AdoptionAlteration.State state) {
        Query q = em.createNamedQuery("getAdoptionAlterationsByStatus");
        q.setParameter("state", state);
        return q.getResultList();
    }
}
