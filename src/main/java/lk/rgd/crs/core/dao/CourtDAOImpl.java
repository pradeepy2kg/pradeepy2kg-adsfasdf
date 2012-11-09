package lk.rgd.crs.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.core.dao.PreloadableDAO;
import lk.rgd.crs.api.dao.CourtDAO;
import lk.rgd.crs.api.domain.Court;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author asankha
 */
public class CourtDAOImpl extends BaseDAO implements CourtDAO, PreloadableDAO {

    private final Map<Integer, Court> courtsByPK = new HashMap<Integer, Court>();
    private final Map<Integer, String> siCourts = new TreeMap<Integer, String>();
    private final Map<Integer, String> enCourts = new TreeMap<Integer, String>();
    private final Map<Integer, String> taCourts = new TreeMap<Integer, String>();

    /**
     * @inheritDoc
     */
    public Map<Integer, String> getCourtNames(String language) {

        if (AppConstants.SINHALA.equals(language)) {
            return siCourts;
        } else if (AppConstants.ENGLISH.equals(language)) {
            return enCourts;
        } else if (AppConstants.TAMIL.equals(language)) {
            return taCourts;
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
            return null;
        }
    }

    public String getNameByPK(int courtUKey, String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return courtsByPK.get(courtUKey).getSiCourtName();
        } else if (AppConstants.ENGLISH.equals(language)) {
            return courtsByPK.get(courtUKey).getEnCourtName();
        } else if (AppConstants.TAMIL.equals(language)) {
            return courtsByPK.get(courtUKey).getTaCourtName();
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        return AppConstants.EMPTY_STRING;
    }


    /**
     * @inheritDoc
     */
    public Court getCourt(int id) {
        Court court = courtsByPK.get(id);
        if (court == null) {
            court = em.find(Court.class, id);
        }
        return court;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Court court, User user) {
        em.merge(court);
        updateCache(court);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void add(Court court, User user) {
        em.persist(court);
        updateCache(court);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Court> findAll() {
        Query q = em.createNamedQuery("findAllCourts");
        return q.getResultList();
    }

    @Override
    public Court getCourtByCode(int courtId) {
        Query q = em.createNamedQuery("get.court.by.code");
        q.setParameter("courtId", courtId);
        try {
            return (Court) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Court> getCourtByAnyName(Court court, User user) {
        Query q = em.createNamedQuery("get.court.by.name");
        q.setParameter("enName", court.getEnCourtName());
        q.setParameter("siName", court.getSiCourtName());
        q.setParameter("taName", court.getTaCourtName());
        return q.getResultList();
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT c FROM Court c WHERE c.active = TRUE");
        List<Court> results = query.getResultList();

        for (Court court : results) {
            updateCache(court);
        }

        logger.debug("Loaded : {} courts from the database", results.size());
    }

    private void updateCache(Court court) {
        final int courtId = court.getCourtId();
        final int courtUKey = court.getCourtUKey();
        final boolean active = court.isActive();

        if (active) {
            courtsByPK.put(courtUKey, court);
            siCourts.put(courtUKey, courtId + SPACER + court.getSiCourtName());
            enCourts.put(courtUKey, courtId + SPACER + court.getEnCourtName());
            taCourts.put(courtUKey, courtId + SPACER + court.getTaCourtName());
        } else {
            courtsByPK.remove(courtUKey);
            siCourts.remove(courtUKey);
            enCourts.remove(courtUKey);
            taCourts.remove(courtUKey);
        }

    }
}
