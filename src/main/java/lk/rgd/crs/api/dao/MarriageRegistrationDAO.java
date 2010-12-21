package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.District;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageRegister;

import java.util.Date;
import java.util.List;

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
     * @param active     include currently active or inactive items    @return the matching list of marriage registrations
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
}
