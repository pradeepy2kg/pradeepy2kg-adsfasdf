package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthAlteration;

import java.util.List;
import java.util.Map;

/**
 * Declares the interface of the birth alteration service
 *
 * @author Indunil Moremada
 * @author Asankha Perera (reviewed and reorganized)
 */
public interface BirthAlterationService {

    /**
     * Adds a birth alteration. A user can add a birth alteration request for a birth registered anywhere in the country
     *
     * @param ba   the birth alteration to be added
     * @param user the user initiating the action
     */
    public void addBirthAlteration(BirthAlteration ba, User user);

    /**
     * Update a given birth alteration in data entry state. An alteration record can be edited by any user of the same
     * submission location for the alteration entry, or any other user that has necessary access to the BD division
     * from the corresponding BDF
     *
     * @param ba   the birth alteration to be update
     * @param user the user initiating the action
     */
    public void updateBirthAlteration(BirthAlteration ba, User user);

    /**
     * Delete a given birth alteration in data entry state. An alteration record can be edited by any user of the same
     * submission location for the alteration entry, or any other user that has necessary access to the BD division
     * from the corresponding BDF remove a requested birth alteration based on given idUKey
     *
     * @param idUKey the unique ID of the BirthAlteration to remove
     * @param user   the user initiating the action
     */
    public void deleteBirthAlteration(long idUKey, User user);

    /**
     * Returns the Birth alteration with the given idUKey
     *
     * @param idUKey Birth Alteration Id for the given
     *               birth alteration
     * @param user   the user initiating the action
     * @return BirthAlteration or null if none exist
     */
    public BirthAlteration getByIDUKey(long idUKey, User user);

    /**
     * Approve requested fields of birth alteration statement 27A
     * or alteration statement 52_1 by an ARG or higher authority
     *
     * @param ba                 the birth alteration to be approved
     * @param fieldsToBeApproved the list of field indexes to be approved
     * @param user               the user initiating the action
     */
    public void approveBirthAlteration(BirthAlteration ba, Map<Integer, Boolean> fieldsToBeApproved, User user);

    /**
     * Returns a limited set of BirthAlterations for which one or more fields in the statement 27,27A or statement
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
     * Returns a limited set of BirthAlterations for which one or more fields in the statement 27,27A or statement
     * 52_1 are awaiting approval by an ARG or higher authority based on given birth/death division.
     *
     * @param bdDivision the birth/death division
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows
     * @param user       the user initiating the action
     * @return the birth alteration results
     */
    public List<BirthAlteration> getApprovalPendingByBDDivision(BDDivision bdDivision, int pageNo, int noOfRows, User user);

    /**
     * Returns a limited set of BirthAlterations for which one or more fields in the statement 27,27A or statement
     * 52_1 are awaiting approval by an ARG or higher authority based on given birth/death division.
     *
     * @param bdDivision the birth/death division
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows
     * @return the birth alteration results
     */
    public List<BirthAlteration> getApprovalPendingByBDDivision(BDDivision bdDivision, int pageNo, int noOfRows);

    /**
     * Returns a limited set of BirthAlterations for which one or more fields in the statement 27,27A or statement
     * 52_1 are awaiting approval by an ARG or higher authority based on given Recived date of birth alteration
     *
     * @param bdDivision        birth Division of the birth
     * @param pageNo            the page number for the results required (start from 1)
     * @param noOfRows          number of rows
     * @param user              the user initiating the action
     * @param birthSerialNumber Serial Number of the Birth Declaration
     * @return the birth alteration results
     */
    public List<BirthAlteration> getApprovalPendingByBDDivisionAndBDFSerialNo
            (BDDivision bdDivision, Long birthSerialNumber, int pageNo, int noOfRows, User user);


    /**
     * Returns a limited set of BirthAlterations for which one or more fields in the statement 27,27A or statement
     * 52_1 are awaiting approval by an ARG or higher authority based on user location
     *
     * @param locationUKey idUKey of the user location
     * @param pageNo       the page number for the results required (start from 1)
     * @param noOfRows     number of rows
     * @param user         the user initiating the action
     * @return the birth alteration results
     */
    public List<BirthAlteration> getApprovalPendingByUserLocationIdUKey
            (int locationUKey, int pageNo, int noOfRows, User user);

    /**
     * Returns a limited set of BirthAlterations for which one or more fields in the statement 27,27A or statement
     * 52_1 are awaiting approval by an ARG or higher authority based on given idUKey
     *
     * @param idUKey   idUKey of the birth Alteration
     * @param pageNo   the page number for the results required (start from 1)
     * @param noOfRows number of rows
     * @param user     the user initiating the action
     * @return the birth alteration results
     */
    public BirthAlteration getApprovalPendingByIdUKey
            (Long idUKey, int pageNo, int noOfRows, User user);
}
