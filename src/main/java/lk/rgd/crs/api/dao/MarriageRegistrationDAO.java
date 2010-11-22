package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.MarriageRegister;

/**
 * @author amith jayasekara
 *         DAO level interface for marriage registration related functions
 */
public interface MarriageRegistrationDAO {
    /**
     * add marriage notice
     *
     * @param notice marriage notice
     * @param user   user who perform action
     */
    public void addMarriageNotice(MarriageRegister notice, User user);
}
