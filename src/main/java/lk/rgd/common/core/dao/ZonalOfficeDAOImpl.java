package lk.rgd.common.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.ZonalOfficesDAO;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.ZonalOffice;
import lk.rgd.crs.api.domain.Court;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;

/**
 * @author Duminda Dharmakeerthi
 */
public class ZonalOfficeDAOImpl extends BaseDAO implements ZonalOfficesDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addZonalOffice(ZonalOffice zonalOffice, User admin) {
        zonalOffice.getLifeCycleInfo().setCreatedUser(admin);
        zonalOffice.getLifeCycleInfo().setCreatedTimestamp(new Date());
        zonalOffice.getLifeCycleInfo().setLastUpdatedUser(admin);
        zonalOffice.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        em.persist(zonalOffice);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateZonalOffice(ZonalOffice zonalOffice, User admin) {
        zonalOffice.getLifeCycleInfo().setLastUpdatedUser(admin);
        zonalOffice.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
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
    public ZonalOffice getZonalOfficeByDistrict(District district) {
        Query q = em.createNamedQuery("getZonalOfficeByDistrict");
        q.setParameter("district", district);
        try{
            return (ZonalOffice)q.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public ZonalOffice getZonalOfficeByCourt(Court court) {
        Query q = em.createNamedQuery("getZonalOfficeByCourt");
        q.setParameter("court", court);
        try{
            return (ZonalOffice)q.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
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
}
