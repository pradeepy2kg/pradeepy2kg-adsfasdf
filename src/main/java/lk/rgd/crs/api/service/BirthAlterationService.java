package lk.rgd.crs.api.service;

import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.common.api.domain.User;

/**
 * @author Indunil Moremada
 */
public interface BirthAlterationService {

    /**
     * Adds a birth alteration
     *
     * @param ba   the birth alteration to be added
     * @param user the user initiating the action
     */
    public void addBirthAlteration(BirthAlteration ba, User user);

    /**
     * Update a given birth alteration
     *
     * @param ba   the birth alteration to be added
     * @param user the user initiating the action
     */
    public void updateBirthAlteration(BirthAlteration ba, User user);

    /**
     * remove a requested birth alteration based on given idUKey
     *
     * @param idUKey the unique ID of the BirthAlteration to remove
     * @param user the user initiating the action
     */
    public void deleteBirthAlteration(long idUKey,User user);

    /**
     * returns a Birth alteration object for the given idUKey
     *
     * @param idUKey Birth Alteration Id for the given
     *               birth alteration
     * @param user the user initiating the action
     * @return BirthAlteration or null if none exist
     */
    public BirthAlteration getById(long idUKey,User user);
}
