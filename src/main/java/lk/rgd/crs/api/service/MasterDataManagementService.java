package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.BDDivision;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Perform the management of Master Data
 *
 * @author asankha
 */
public interface MasterDataManagementService {

    /**
     * Add a new BD Division
     * @param bdDivision the BD Division to be added
     * @param user the user invoking the action
     */
    void add(BDDivision bdDivision, User user);

    /**
     * Mark a BD Division as inactive
     * @param bdDivision the BD Division to be updated
     * @param user the user invoking the action
     */
    void inactivate(BDDivision bdDivision, User user);

    /**
     * Mark a BD Division as active
     * @param bdDivision the BD Division to be updated
     * @param user the user invoking the action
     */
    void activate(BDDivision bdDivision, User user);
}
