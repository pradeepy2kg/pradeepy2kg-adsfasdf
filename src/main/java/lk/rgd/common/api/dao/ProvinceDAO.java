package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.Province;
import lk.rgd.common.api.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @author Duminda Dharmakeerthi
 */
public interface ProvinceDAO {

    /**
     * Add a Province
     *
     * @param province  Province to be added.
     * @param admin     User requesting to add the Province.
     */
    public void addProvince(Province province, User admin);

    /**
     * Update a Province
     *
     * @param province  Province to be updated.
     * @param admin     User requesting to update the Province
     */
    public void updateProvince(Province province, User admin);

    /**
     * Returns the Province with the given provinceUKey.
     *
     * @param provinceUKey  ProvinceUKey
     * @return              Province corresponding to given provinceUKey
     */
    public Province getProvinceByUKey(int provinceUKey);

    /**
     * Returns all active Provinces.
     *
     * @return All active Provinces
     */
    public List<Province> getAllActiveProvinces();

    /**
     * Returns a Map of Active Provinces in the given language.
     *
     * @param language  Selected Language
     * @return          Map of Provinces in the selected language
     */
    public Map<Integer, String> getActiveProvinces(String language);
}
