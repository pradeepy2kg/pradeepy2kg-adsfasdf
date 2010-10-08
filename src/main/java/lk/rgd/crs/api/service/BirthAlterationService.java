package lk.rgd.crs.api.service;

import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;

import java.util.Hashtable;
import java.util.List;
import java.util.Date;

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
     * @param ba   the birth alteration to be update
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
     * @param fieldsToBeApproved the list of field indexes to be approved
     * @param user               the user initiating the action
     * @param appStatus          the alteration fully approved or not
     */
    public void approveBirthAlteration(BirthAlteration ba, Hashtable<Integer, Boolean> fieldsToBeApproved, boolean appStatus, User user);

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
     * Returns the active Birth Declaration record for a given bdf serialNo under a selected BD Division
     *
     * @param bdDivision the Birth Death declaration division
     * @param serialNo   serial number to check
     * @param isAlt52_1  is alteration is in act 51_1 or not
     * @param user       the user making the request
     * @return true if the serial number is unique and not used at present
     */
    public BirthAlteration getActiveRecordByBDDivisionAndSerialNo(BDDivision bdDivision, long serialNo, User user, boolean isAlt52_1);


    /**
     * Returns a limited set of BirthAlterations for which one or more fields in the statement 27,27A or statement
     * 52_1 are awaiting approval by an ARG or higher authority based on given idUKey of birth altaration
     *
     * @param idUKey   idUKey of the Birth Alteration
     * @param pageNo   the page number for the results required (start from 1)
     * @param noOfRows number of rows
     * @param user     the user initiating the action
     * @return the birth alteration results
     */
    public List<BirthAlteration> getApprovalPendingByIdUKey(long idUKey, int pageNo, int noOfRows, User user);

    /**
     * Returns a limited set of BirthAlterations for which one or more fields in the statement 27,27A or statement
     * 52_1 are awaiting approval by an ARG or higher authority based on given Recived date of birth alteration
     *
     * @param recivedDateFrom start of  recived Date of the birth alteration
     * @param pageNo          the page number for the results required (start from 1)
     * @param noOfRows        number of rows
     * @param user            the user initiating the action
     * @return the birth alteration results
     */
    public List<BirthAlteration> getApprovalPendingByRecivedDate(Date recivedDateFrom, Date recivedDateTo, int pageNo, int noOfRows, User user);

    /**
     * Returns a limited set of BirthAlterations for which one or more fields in the statement 27,27A or statement
     * 52_1 are awaiting approval by an ARG or higher authority based on given Recived date of birth alteration
     *
     * @param bdDivision             birth Division of the birth alteration
     * @param pageNo                 the page number for the results required (start from 1)
     * @param noOfRows               number of rows
     * @param user                   the user initiating the action
     * @param alterationSerialNumber Serial Number of the Birth Alteration
     * @return the birth alteration results
     */
    public List<BirthAlteration> getApprovalPendingByBDDivisionAndAlterationSerialNo
            (BDDivision bdDivision, Long alterationSerialNumber, int pageNo, int noOfRows, User user);


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
    public List<BirthAlteration> getApprovalPendingByBDDivisionAndBirthSerialNo
            (BDDivision bdDivision, Long birthSerialNumber, int pageNo, int noOfRows, User user);
}
