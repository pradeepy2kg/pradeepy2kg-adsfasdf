package lk.rgd.crs.core.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.domain.Witness;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @author amith jayasekara
 */
public class MarriageRegistrationDAOImpl extends BaseDAO implements MarriageRegistrationDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addMarriageNotice(MarriageRegister notice, User user) {
        notice.setState(MarriageRegister.State.DATA_ENTRY);
        notice.getLifeCycleInfo().setCreatedTimestamp(new Date());
        notice.getLifeCycleInfo().setCreatedUser(user);
        notice.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        notice.getLifeCycleInfo().setLastUpdatedUser(user);
        em.persist(notice);
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public MarriageRegister getByIdUKey(long idUKey) {
        return em.find(MarriageRegister.class, idUKey);
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addWitness(Witness witness) {
        em.persist(witness);
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateMarriageRegister(MarriageRegister marriageRegister, User user) {
        marriageRegister.setState(MarriageRegister.State.DATA_ENTRY);
        marriageRegister.getLifeCycleInfo().setCreatedTimestamp(new Date());
        marriageRegister.getLifeCycleInfo().setCreatedUser(user);
        marriageRegister.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        marriageRegister.getLifeCycleInfo().setLastUpdatedUser(user);
        em.merge(marriageRegister);
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public MarriageRegister getByMRDivisionAndSerialNo(MRDivision mrDivision, MarriageRegister.State state,
        long serialNo, boolean active) {
        Query q = em.createNamedQuery("filter.by.mrDivision.serial.and.state");
        q.setParameter("mrDivision", mrDivision);
        q.setParameter("serialNo", serialNo);
        q.setParameter("state", state);
        q.setParameter("active", active);
        try {
            return (MarriageRegister) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedListForStateByDSDivision(DSDivision dsDivision,
        MarriageRegister.State state, int pageNo, int noOfRows, boolean active) {
        Query q = em.createNamedQuery("filter.by.dsDivision.and.state").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("dsDivision", dsDivision);
        q.setParameter("state", state);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedListForStateByMRDivision(MRDivision mrDivision,
        MarriageRegister.State state, int pageNo, int noOfRows, boolean active) {
        Query q = em.createNamedQuery("filter.by.mrDivision.and.state").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("mrDivision", mrDivision);
        q.setParameter("state", state);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getByStateAndPINorNIC(MarriageRegister.State state,
        String id, boolean active) {
        Query q = em.createNamedQuery("filter.by.pinOrNic.and.state");
        q.setParameter("id", id);
        q.setParameter("state", state);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getActiveMarriageNoticeByMaleFemaleIdentification(String maleIdentification,
        String femaleIdentification) {
        Query q = em.createNamedQuery("get.notice.by.male.and.female.identification");
        q.setParameter("male", maleIdentification);
        q.setParameter("female", femaleIdentification);
        return q.getResultList();
    }
}
