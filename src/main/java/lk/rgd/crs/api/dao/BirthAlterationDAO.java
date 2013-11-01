package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthAlteration;

import java.util.List;

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
     * Returns a limited set of BirthAlterations based on given BDDivison id
     *
     * @param BDDivision the birth/death division
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows
     * @return the birth alteration results
     */
    public List<BirthAlteration> getBulkOfAlterationByBDDivision(BDDivision BDDivision, int pageNo, int noOfRows);


    /**
     * Returns a limited set of BirthAlterations based on given idUKey
     *
     * @param idUKey   idUKey of the birth alteration
     * @param pageNo   the page number for the results required (start from 1)
     * @param noOfRows number of rows
     * @return the birth alteration results
     */
    public BirthAlteration getBulkOfAlterationByIdUKey(long idUKey, int pageNo, int noOfRows);

    /**
     * Returns a limited set of BirthAlterations based on given user location
     *
     * @param birthCertificateNumber of the certificate
     * @param pageNo  the page number for the results required (start from 1)
     * @param noOfRows     number of rows
     * @return the birth alteration results
     */
    public List<BirthAlteration> getBulkOfAlterationByCertificateNumber(long birthCertificateNumber, int pageNo, int noOfRows);

    /**
     * Returns a limited set of BirthAlterations based on given user location
     *
     * @param locationUKey idUKey  of the user location
     * @param pageNo       the page number for the results required (start from 1)
     * @param noOfRows     number of rows
     * @return the birth alteration results
     */
    public List<BirthAlteration> getBulkOfAlterationByUserLocationIdUKey(int locationUKey, int pageNo, int noOfRows);

    /**
     * get birth alterations for given birth record.
     *
     * @param idUKey idUKey of birth record
     * @return list of birth alterations for given birth certificate.
     */
    public List<BirthAlteration> getBirthAlterationByBirthCertificateNumber(long idUKey);

}
