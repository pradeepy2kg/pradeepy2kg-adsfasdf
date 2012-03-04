package lk.rgd.crs.api.service;

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
     * <br>
     * notes:
     * <br>
     * in following scenario we have to sync all the active(DE state) alterations which use FK of current approving
     * alteration as it's FK
     * <br>
     * example
     * <p>assume we have BC and PK(certificate number) is 1</p>
     * <ul>
     * <li>add a alteration on 27 section and idUKey is 1</li>
     * <li>add a alteration on 27A or 52_1_H or 52_1_I section and idUKey is 2</li>
     * <li>now we are approving and print alteration 27 </li>
     * <li>because of the approval of 27 now FK 1 become in active and new clone BC 2 generated with new altered values</li>
     * <li>but alt 27 A still reference to the FK 1 but FK 1 doesn't have altered values so 27 A should refer to FK 2 </li>
     * </ul>
     * <br>
     * so we have to sync other active DE state alterations when approve
     * <br>
     * <U><B>if act is 52_1_A or 52_1_B or 52_1_D or 52_1_E we don't have to care on this situation </B></U>
     *
     * @param ba                 the birth alteration to be approved
     * @param fieldsToBeApproved the list of field indexes to be approved
     * @param user               the user initiating the action
     */
    public void approveBirthAlteration(BirthAlteration ba, Map<Integer, Boolean> fieldsToBeApproved, User user);

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
    public List<BirthAlteration> getApprovalPendingByBDDivision(BDDivision bdDivision, int pageNo, int noOfRows,
        User user);

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
     * 52_1 are awaiting approval by an ARG or higher authority based on user location
     *
     * @param locationUKey idUKey of the user location
     * @param pageNo       the page number for the results required (start from 1)
     * @param noOfRows     number of rows
     * @param user         the user initiating the action
     * @return the birth alteration results
     */
    public List<BirthAlteration> getApprovalPendingByUserLocationIdUKey(int locationUKey, int pageNo, int noOfRows,
        User user);

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
    public BirthAlteration getApprovalPendingByIdUKey(Long idUKey, int pageNo, int noOfRows, User user);

    /**
     * get birth alterations done to given birth certificate
     *
     * @param idUKey idUKey of birth certificate
     * @param user   the user initiating the action
     * @return List of birth alterations done to given birth record.
     */
    public List<BirthAlteration> getBirthAlterationByBirthCertificateNumber(long idUKey, User user);

    /**
     * rejecting birth alteration
     * <br>
     * only DATA_ENTRY mode alteration records are being rejected and that record is being archived  with a comment for
     * rejection and  the state of the record would be REJECTED
     *
     * @param idUKey  idUKey of the birth alteration record
     * @param comment reason for reject
     * @param user    user who performs the action
     */
    public void rejectBirthAlteration(long idUKey, String comment, User user);

    /**
     * marking birth alteration notice as printed
     *
     * @param idUKey idUKey of the birth alteration notice
     * @param user   user who performs the action
     */
    public void markBirthAlterationNoticeAsPrinted(long idUKey, User user);
}
