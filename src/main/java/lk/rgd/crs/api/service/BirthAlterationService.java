package lk.rgd.crs.api.service;

import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;

import java.util.Hashtable;
import java.util.List;

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
     * @param user   the user initiating the action
     */
    public void deleteBirthAlteration(long idUKey, User user);

    /**
     * returns a Birth alteration object for the given idUKey
     *
     * @param idUKey Birth Alteration Id for the given
     *               birth alteration
     * @param user   the user initiating the action
     * @return BirthAlteration or null if none exist
     */
    public BirthAlteration getById(long idUKey, User user);

    /**
     * Approve requested fields of birth alteration statement 27A
     * or alteration statement 52_1 by an ARG or higher authority
     *
     * @param ba                 the birth alteration to be approved
     * @param isAlteration27A    the boolean if true it
     *                           requested to approve filelds in alteration
     *                           statement 27A if it is false requested to
     *                           approve fields in alteration statement 52_1
     * @param fieldsToBeApproved the list of field indexes to be approved
     * @param user               the user initiating the action
     */
    public void approveBirthAlteration(BirthAlteration ba, boolean isAlteration27A, Hashtable<Integer, Boolean> fieldsToBeApproved, User user);

    /**
     * Returns a limited set of BirthAlterations for which one or more fields in the statement 27A or statement
     * 52_1 are awaiting approval by an ARG or higher authority based on given DSDivison id.
     *
     * @param dsDivision the divisional Secretariat
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows
     * @param user       the user initiating the action
     * @return the birth alteration results
     */
    public List<BirthAlteration> getApprovalPendingByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user);

    /**
     * Returns a limited set of BirthAlterations for which one or more fields in the statement 27A or statement
     * 52_1 are awaiting approval by an ARG or higher authority based on given birth/death division.
     *
     * @param bdDivision the birth/death division
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows
     * @param user       the user initiating the action
     * @return the birth alteration results
     */
    public List<BirthAlteration> getApprovalPendingByBDDivision(BDDivision bdDivision, int pageNo, int noOfRows, User user);
}
