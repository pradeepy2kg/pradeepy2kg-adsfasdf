package lk.rgd.common.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.common.api.dao.ProvinceDAO;
import lk.rgd.common.api.domain.Province;
import lk.rgd.common.api.domain.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Duminda Dharmakeerthi
 */
public class ProvinceDAOImpl extends BaseDAO implements ProvinceDAO {

    private final Map<Integer, String> provinces = new TreeMap<Integer, String>();

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addProvince(Province province, User admin) {
        em.persist(province);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateProvince(Province province, User admin) {
        em.merge(province);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Province getProvinceByUKey(int provinceUKey) {
        return em.find(Province.class, provinceUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Province> getAllActiveProvinces() {
        Query q = em.createNamedQuery("getAllActiveProvinces");
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<Integer, String> getActiveProvinces(String language) {
        Query q = em.createNamedQuery("getAllActiveProvinces");
        List<Province> results = q.getResultList();

        if (AppConstants.SINHALA.equals(language)) {
            for (Province p : results) {
                provinces.put(p.getProvinceUKey(), p.getSiProvinceName());
            }
        } else if (AppConstants.TAMIL.equals(language)) {
            for (Province p : results) {
                provinces.put(p.getProvinceUKey(), p.getTaProvinceName());
            }
        } else if (AppConstants.ENGLISH.equals(language)) {
            for (Province p : results) {
                provinces.put(p.getProvinceUKey(), p.getEnProvinceName());
            }
        }
        return provinces;
    }
}
