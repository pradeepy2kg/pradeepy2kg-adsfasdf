package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
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
    void addBDDivision(BDDivision bdDivision, User user);

    /**
     * Mark a BD Division as inactive
     * @param bdDivision the BD Division to be updated
     * @param user the user invoking the action
     */
    void inactivateBDDivision(BDDivision bdDivision, User user);

    /**
     * Mark a BD Division as active
     * @param bdDivision the BD Division to be updated
     * @param user the user invoking the action
     */
    void activateBDDivision(BDDivision bdDivision, User user);

    /**
     * Add a new MR Division
     * @param mrDivision the MR Division to be added
     * @param user the user invoking the action
     */
    void addMRDivision(MRDivision mrDivision, User user);

    /**
     * Mark a MR Division as inactive
     * @param mrDivision the MR Division to be updated
     * @param user the user invoking the action
     */
    void inactivateMRDivision(MRDivision mrDivision, User user);

    /**
     * Mark a MR Division as active
     * @param mrDivision the MR Division to be updated
     * @param user the user invoking the action
     */
    void activateMRDivision(MRDivision mrDivision, User user);
}
