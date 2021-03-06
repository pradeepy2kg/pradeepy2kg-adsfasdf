package lk.rgd.common.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.ZonalOfficesDAO;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.ZonalOffice;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

/**
 * @author Duminda Dharmakeerthi
 */
public class ZonalOfficeDAOImpl extends BaseDAO implements ZonalOfficesDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addZonalOffice(ZonalOffice zonalOffice, User admin) {
        em.persist(zonalOffice);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateZonalOffice(ZonalOffice zonalOffice, User admin) {
        em.merge(zonalOffice);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public ZonalOffice getZonalOffice(int zonalOfficeUKey) {
        return em.find(ZonalOffice.class, zonalOfficeUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public String getZonalOfficeNameByPK(int zonalOfficeUKey, String language) {
        ZonalOffice zonalOffice = getZonalOffice(zonalOfficeUKey);
        if (zonalOffice != null) {
            if (AppConstants.SINHALA.equals(language)) {
                return zonalOffice.getSiZonalOfficeName();
            } else if (AppConstants.TAMIL.equals(language)) {
                return zonalOffice.getTaZonalOfficeName();
            } else if (AppConstants.ENGLISH.equals(language)) {
                return zonalOffice.getEnZonalOfficeName();
            } else {
                handleException("Unsupported Language " + language, ErrorCodes.INVALID_LANGUAGE);
            }
        } else {
            handleException("Zonal Office doesn't Exist. " + zonalOfficeUKey, ErrorCodes.INVALID_ZONAL_OFFICE);
        }
        return AppConstants.EMPTY_STRING;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public String getZonalOfficeMailAddressByPK(int zonalOfficeUKey, String language) {
        ZonalOffice zonalOffice = getZonalOffice(zonalOfficeUKey);
        if (zonalOffice != null) {
            if (AppConstants.SINHALA.equals(language)) {
                return zonalOffice.getSiZonalOfficeMailAddress();
            } else if (AppConstants.TAMIL.equals(language)) {
                return zonalOffice.getTaZonalOfficeMailAddress();
            } else if (AppConstants.ENGLISH.equals(language)) {
                return zonalOffice.getEnZonalOfficeMailAddress();
            } else {
                handleException("Unsupported Language " + language, ErrorCodes.INVALID_LANGUAGE);
            }
        } else {
            handleException("Zonal Office doesn't Exist. " + zonalOfficeUKey, ErrorCodes.INVALID_ZONAL_OFFICE);
        }
        return AppConstants.EMPTY_STRING;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ZonalOffice> getAllActiveZonalOffices() {
        Query q = em.createNamedQuery("getAllActiveZonalOffices");
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<Integer, String> getActiveZonalOffices(String language) {
        Map<Integer, String> zonalOffices = new TreeMap<Integer, String>();
        List<ZonalOffice> zonalOfficeList = getAllActiveZonalOffices();
        if (zonalOfficeList != null && zonalOfficeList.size() > 0) {
            for (ZonalOffice z : zonalOfficeList) {
                if (AppConstants.SINHALA.equals(language)) {
                    zonalOffices.put(z.getZonalOfficeUKey(), z.getSiZonalOfficeName());
                } else if (AppConstants.TAMIL.equals(language)) {
                    zonalOffices.put(z.getZonalOfficeUKey(), z.getTaZonalOfficeName());
                } else if (AppConstants.ENGLISH.equals(language)) {
                    zonalOffices.put(z.getZonalOfficeUKey(), z.getEnZonalOfficeName());
                }
            }
            return zonalOffices;
        } else {
            return Collections.EMPTY_MAP;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ZonalOffice> getAll() {
        Query q = em.createNamedQuery("getAll");
        return q.getResultList();
    }
}
