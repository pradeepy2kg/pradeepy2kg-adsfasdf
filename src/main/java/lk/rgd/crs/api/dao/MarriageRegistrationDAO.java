package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageRegister;

import java.util.Date;
import java.util.List;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author amith jayasekara
 *         DAO level interface for marriage registration related functions
 */
public interface MarriageRegistrationDAO {
    /**
     * add marriage notice/register
     *
     * @param marriageRegister marriage notice/register
     * @param user             user who perform action
     */
    public void addMarriageRegister(MarriageRegister marriageRegister, User user);

    /**
     * get marriage register object by it's idUKey
     *
     * @param idUKey primary key of the record
     * @return user who performs the action
     */
    public MarriageRegister getByIdUKey(long idUKey);

    /**
     * updating given marriage register object
     *
     * @param marriageRegister object to be update
     * @param user             use who performs action
     */
    public void updateMarriageRegister(MarriageRegister marriageRegister, User user);

    /**
     * Get active record by MRDivision and Serial Number of male or female party
     *
     * @param mrDivision the Marriage Registration Division
     * @param serialNo   the serial number to check
     * @return the active record with the given serial number and MRDivision, or null if none exist
     */
    public MarriageRegister getActiveRecordByMRDivisionAndSerialNo(MRDivision mrDivision, long serialNo);

    /**
     * Get active or inactive Marriage Notice by MRDivision and Serial Number of male or female party
     *
     * @param mrDivision the Marriage Registration Division
     * @param serialNo   the Serial Number(male or female party) with in the MRDivision
     * @param active     include currently active or inactive items
     * @return the MarriageRegister if exist
     */
    public List<MarriageRegister> getNoticeByMRDivisionAndSerialNo(MRDivision mrDivision, long serialNo, boolean active);

    /**
     * Get active or inactive record by MRDivision and Serial Number of male or female party
     *
     * @param mrDivision the Marriage Registration Division
     * @param state      the state of the record to be returned
     * @param serialNo   the Serial Number(male or female party) with in the MRDivision
     * @param active     include currently active or inactive items
     * @return the MarriageRegister if exist
     */
    public List<MarriageRegister> getByMRDivisionAndSerialNo(MRDivision mrDivision, MarriageRegister.State state,
        long serialNo, boolean active);

    /**
     * Returns a paginated list of active/inactive Marriage Notices based on the District
     *
     * @param district the district
     * @param pageNo   the page number (start from 1)
     * @param noOfRows the number of rows to return per page
     * @param active   include currently active or inactive items
     * @return the matching list of marriage registrations (marriage notices)
     */
    public List<MarriageRegister> getPaginatedListByDistrict(District district, int pageNo, int noOfRows, boolean active);

    /**
     * Returns paginated list of active/inactive Marriage Notices based on the DSDivision
     *
     * @param dsDivision the divisional secretariat
     * @param pageNo     the page number (start from 1)
     * @param noOfRows   the number of rows to return per page
     * @param active     include currently active or inactive items
     * @return the matching list of marriage registrations (marriage notices)
     */
    public List<MarriageRegister> getPaginatedNoticeListByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows,
        boolean active);

    /**
     * Returns paginated list of active/inactive Marriage Registrations for the given status based on the DSDivision
     *
     * @param dsDivision the divisional secretariat
     * @param state      the state of the record to be returned
     * @param pageNo     the page number (start from 1)
     * @param noOfRows   the number of rows to return per page
     * @param active     include currently active or inactive items
     * @return the matching list of marriage registrations
     */
    public List<MarriageRegister> getPaginatedListForStateByDSDivision(DSDivision dsDivision,
        MarriageRegister.State state, int pageNo, int noOfRows, boolean active);

    /**
     * Returns paginated marriage register list filtered by district and State
     *
     * @param district the District
     * @param state    the state of the marriage register
     * @param pageNo   the page number (starting from 1)
     * @param noOfRows no of rows to be to be retured
     * @param isActive Active or inactive
     * @return
     */
    public List<MarriageRegister> getPaginatedListByDistrict(District district,
        MarriageRegister.State state, int pageNo, int noOfRows, boolean isActive);

    /**
     * Returns paginated marriage register list filtered by district and Date Range
     *
     * @param district  the District
     * @param state     the state of the marriage register
     * @param pageNo    the page number (starting from 1)
     * @param noOfRows  no of rows to be to be retured
     * @param isActive  Active or inactive status
     * @param startDate from date of the date range the marrige notice or registration performed
     * @param endDate   to date of the date range the marrige notice or registration performed
     * @return
     */
    public List<MarriageRegister> getPaginatedListByDistrictAndDate(District district,
        MarriageRegister.State state, int pageNo, int noOfRows, boolean isActive,
        Date startDate, Date endDate);

    /**
     * Returns paginated marriage register list filtered by State
     *
     * @param state    the state of the marriage register
     * @param pageNo   the page number (starting from 1)
     * @param noOfRows no of rows to be to be retured
     * @param active   Active or inactive status
     * @return
     */
    public List<MarriageRegister> getPaginatedMarriageRegisterListByState(MarriageRegister.State state, int pageNo,
        int noOfRows, boolean active);

    /**
     * Returns paginated marriage register list
     *
     * @param stateList list of marriage register states
     * @param pageNo    the page number (starting from 1)
     * @param noOfRows  no of rows to be retured
     * @param isActive  Active or inactive status of the marriage register
     * @return
     */
    public List<MarriageRegister> getPaginatedMarriageRegisterList(EnumSet stateList, int pageNo, int noOfRows, boolean isActive);

    /**
     * Returns Marriage Register list
     *
     * @param divisionType   type of division (district, DS division or MR division)
     * @param divisionUKey   Primary key of the division
     * @param dsDivisionList Permitted list of Divisions when divisionUKey is empty
     * @param stateList      List of States the marriage register can have
     * @param isActive       Active or inactive status of marriage register
     * @param startDate      from date of the date range - Marriage Date of the marriage register
     * @param endDate        to date of the date range - Marriage Date of the marriage register
     * @param pageNo         the page number (starting from 1)
     * @param noOfRows       no of rows to be retured
     * @return
     */
    public List<MarriageRegister> getPaginatedMarriageRegisterList(String divisionType, int divisionUKey, Set<DSDivision> dsDivisionList,
        EnumSet stateList, boolean isActive, Date startDate, Date endDate, int pageNo, int noOfRows);

    /**
     * Returns paginated marriage register list filtered by a list of Districts
     *
     * @param districtList list of districts
     * @param stateList    list of marriage register states
     * @param pageNo       the page number (starting from 1)
     * @param noOfRows     no of rows to be retured
     * @param isActive     Active or inactive status of the marriage register
     * @return
     */
    public List<MarriageRegister> getPaginatedMarriageRegisterListByDistricts(Set<District> districtList, EnumSet stateList, int pageNo,
        int noOfRows, boolean isActive);

    /**
     * Returns paginated list of active/inactive Marriage Notices based on the MRDivision
     *
     * @param mrDivision the Marriage Registration Division
     * @param pageNo     the page number (start from 1)
     * @param noOfRows   the number of rows to return per page
     * @param active     include currently active or inactive items
     * @return the matching list of marriage registrations (marriage notices)
     */
    public List<MarriageRegister> getPaginatedNoticeListByMRDivision(MRDivision mrDivision, int pageNo, int noOfRows,
        boolean active);

    /**
     * Returns paginated list of active/inactive Marriage Registrations for given status based on the MRDivision
     *
     * @param mrDivision the Marriage Registration Division
     * @param state      the state of the record to be returned
     * @param pageNo     the page number (start from 1)
     * @param noOfRows   the number of rows to return per page
     * @param active     include currently active or inactive items
     * @return the matching list of marriage registrations
     */
    public List<MarriageRegister> getPaginatedListForStateByMRDivision(MRDivision mrDivision,
        MarriageRegister.State state, int pageNo, int noOfRows, boolean active);

    /**
     * Returns a limited list of Marriage Notices based on identification number (it can be male or female NIC or unique
     * PIN)
     *
     * @param id     the identification number (male or female PIN or NIC)
     * @param active include currently active or inactive items
     * @return the matching list of marriage registrations (marriage notices)
     */
    public List<MarriageRegister> getNoticeByPINorNIC(String id, boolean active);

    /**
     * Find the marriage register by male or female identification number or
     * find a list of marriage register by registrar identification number
     *
     * @param id     the identification number (male or female or registrar PIN or NIC)
     * @param active status of the marriage register active or inactive
     * @return
     */
    public List<MarriageRegister> getMarriageRegisterByIdNumber(String id, boolean active);

    /**
     * Returns a limited list of MarriageRegistrations for given status and identification number (it can be male or
     * female NIC or unique PIN)
     *
     * @param state  the state of the record to be returned
     * @param id     the identification number (male or female PIN or NIC)
     * @param active include currently active or inactive items
     * @return the matching list of marriage registrations
     */
    public List<MarriageRegister> getByStateAndPINorNIC(MarriageRegister.State state, String id, boolean active);

    /**
     * Returns paginated list of active/inactive Marriage Notices based on the <b>MRDivision</b> and <b>submitting date
     * of male or female notice</b> ordered by MarriageRegister unique key in descending order
     *
     * @param mrDivision the Marriage Registration Division
     * @param startDate  the starting date of the search
     * @param endDate    the ending date of the search
     * @param pageNo     the page number (start from 1)
     * @param noOfRows   the number of rows to return per page
     * @param active     include currently active or inactive items
     * @return the matching list of marriage registrations (marriage notices)
     */
    public List<MarriageRegister> getPaginatedNoticesByMRDivisionAndRegisterDateRange(MRDivision mrDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, boolean active);

    /**
     * Returns paginated list of active/inactive Marriage Notices based on the <b>DSDivision</b> and <b>submitting date
     * of male or female notice</b> ordered by MarriageRegister unique key in descending order
     *
     * @param dsDivision the divisional secretariat
     * @param startDate  the starting date of the search
     * @param endDate    the ending date of the search
     * @param pageNo     the page number (start from 1)
     * @param noOfRows   the number of rows to return per page
     * @param active     include currently active or inactive items
     * @return the matching list of marriage registrations (marriage notices)
     */
    public List<MarriageRegister> getPaginatedNoticesByDSDivisionAndRegisterDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, boolean active);

    /**
     * get active marriage notice(marriage register object) where male and female pin or nic numbers are matched
     *
     * @param maleIdentification   male party pin or nic
     * @param femaleIdentification female party pin or nic
     * @return marriage notice(register object)
     */
    public List<MarriageRegister> getActiveMarriageNoticeByMaleFemaleIdentification(String maleIdentification,
        String femaleIdentification);

    /**
     * removing marriage register object from the data base
     *
     * @param idUKey idUKey of the record
     */
    public void deleteMarriageRegister(long idUKey);

    /**
     * Returns count of all records which are in given state
     *
     * @param status    marriage Register form's state
     * @param startDate start Date
     * @param endDate   end Date
     * @return the count of records
     */
    public int getMarriageCertificateCount(MarriageRegister.State status, Date startDate, Date endDate);

    /**
     * Returns all Marriage Registration records which are created by given User
     *
     * @param user  created user
     * @param start
     * @param end   @return list of Marriage Registrations
     */
    public List<MarriageRegister> getByCreatedUser(User user, Date start, Date end);
}
