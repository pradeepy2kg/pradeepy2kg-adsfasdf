package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.DeathAlteration;

/**
 * @author amith jayasekara
 */

public interface DeathAlterationDAO {

    /**
     * adding a new death alteration
     *
     * @param da   the death alteration to be added
     * @param user the user who has permission to add death alteration
     */
    public void addBirthAlteration(DeathAlteration da, User user);

    /**
     * Update a given death alteration
     *
     * @param da   the death alteration to be update
     * @param user the user who has permission to add death alteration
     */
    public void updateBirthAlteration(DeathAlteration da, User user);

    /**
     * remove a requested death alteration based on given idUKey
     *
     * @param idUKey the unique ID of the DeathAlteration to remove
     */
    public void deleteBirthAlteration(long idUKey);

    /**
     * returns a Death alteration object for the given idUKey
     *
     * @param idUKey Death Alteration Id for the given
     *               death alteration
     * @return DeathAlteration or null if none exist
     */
    public DeathAlteration getById(long idUKey);
}
