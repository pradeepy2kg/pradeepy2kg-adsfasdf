package lk.rgd.prs.core.dao;

import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.prs.api.dao.PINNumberDAO;
import lk.rgd.prs.api.domain.PINNumber;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author asankha
 */
public class PINNumberDAOImpl extends BaseDAO implements PINNumberDAO {

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PINNumber getLastPINNumber(long dateOfBirth) {
        return em.find(PINNumber.class, dateOfBirth);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateLastPINNumber(PINNumber lastPIN) {
        em.merge(lastPIN);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addLastPINNumber(PINNumber lastPIN) {
        em.persist(lastPIN);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteLastPINNumber(PINNumber lastPIN) {
        lastPIN = em.merge(lastPIN);
        em.remove(lastPIN);
    }
}
