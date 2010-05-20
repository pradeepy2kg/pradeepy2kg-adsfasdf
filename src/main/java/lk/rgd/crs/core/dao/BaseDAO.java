package lk.rgd.crs.core.dao;

import lk.rgd.crs.CRSRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author asankha
 */
@Repository
public class BaseDAO {

    /** Make subclasses pick up the correct logger */
    protected final Logger logger;
    /** The EntityManager instance injected by Spring */
    protected EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    protected BaseDAO() {
        logger = LoggerFactory.getLogger(this.getClass());  // makes subclasses pick up the correct logger
    }

    protected void handleException(String msg, int code) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, code);
    }

    protected void handleException(String msg, int code, Exception e) {
        logger.error(msg, e);
        throw new CRSRuntimeException(msg, code, e);
    }
}
