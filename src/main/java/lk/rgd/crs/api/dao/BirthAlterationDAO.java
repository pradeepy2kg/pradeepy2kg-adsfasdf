package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;

import java.util.List;
import java.util.Date;

/**
 * @author Indunil Moremada
 */
public interface BirthAlterationDAO {
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
     */
    public void deleteBirthAlteration(long idUKey);

    /**
     * returns a Birth alteration object for the given idUKey
     *
     * @param idUKey Birth Alteration Id for the given
     *               birth alteration
     * @return BirthAlteration or null if none exist
     */
    public BirthAlteration getById(long idUKey);

    /**
     * Returns a limited set of BirthAlterations based on given DSDivison id.
     *
     * @param dsDivision the divisional Secretariat
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows
     * @return the birth alteration results
     */
    public List<BirthAlteration> getBulkOfAlterationByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows);

    /**
     * Returns a limited set of BirthAlterations based on given BDDivison id
     *
     * @param BDDivision the birth/death division
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows
     * @return the birth alteration results
     */
    public List<BirthAlteration> getBulkOfAlterationByBDDivision(BDDivision BDDivision, int pageNo, int noOfRows);


    /**
     * Get the active record by BD Division and Serial number
     *
     * @param bdDivision         the Birth Death declaration division
     * @param alterationSerialNo the Serial No within the division
     * @param isAlt52_1          is alteration is in act 51_1 or not
     * @return the BDF marked as active, or null if none exist
     */
    public BirthAlteration getActiveRecordByBDDivisionAndSerialNo(BDDivision bdDivision, long alterationSerialNo, boolean isAlt52_1);


    /**
     * Returns a limited set of BirthAlterations based on given idUKey
     *
     * @param idUKey   idUKey of the birth alteration
     * @param pageNo   the page number for the results required (start from 1)
     * @param noOfRows number of rows
     * @return the birth alteration results
     */
    public List<BirthAlteration> getBulkOfAlterationByIdUKey(long idUKey, int pageNo, int noOfRows);

    /**
     * Returns a limited set of BirthAlterations based on given recived date
     *
     * @param recivedDateFrom stared date of recived the birth alteration  to search birth alteration
     * @param recivedDateTo   end  date of recived the birth alteration to search birth alteration
     * @param pageNo          the page number for the results required (start from 1)
     * @param noOfRows        number of rows
     * @return the birth alteration results
     */
    public List<BirthAlteration> getBulkOfAlterationByRecivedDate(Date recivedDateFrom, Date recivedDateTo, int pageNo, int noOfRows);

    /**
     * Returns a limited set of BirthAlterations based on given recived date
     *
     * @param bdDivision         birth Division Id  of the birth alteration
     * @param alterationserialNo The serial Number of the birth Alteration
     * @param pageNo             the page number for the results required (start from 1)
     * @param noOfRows           number of rows
     * @return the birth alteration results
     */
    public List<BirthAlteration> getBulkOfAlterationByBDDivisionAndAlterationSerialNo(BDDivision bdDivision, Long alterationserialNo, int pageNo, int noOfRows);

    /**
     * Returns a limited set of BirthAlterations based on given recived date
     *
     * @param bdDivision    birth Division Id  of the birth alteration
     * @param birthSerialNo The serial Number of the birth Declaration
     * @param pageNo        the page number for the results required (start from 1)
     * @param noOfRows      number of rows
     * @return the birth alteration results
     */
    public List<BirthAlteration> getBulkOfAlterationByBDDivisionAndBirthSerialNo(BDDivision bdDivision, Long birthSerialNo, int pageNo, int noOfRows);
}
