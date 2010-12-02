package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageNotice;
import lk.rgd.crs.api.domain.MarriageRegister;

import java.util.List;

/**
 * @authar amith jayasekara
 * service interface for marriage registration related functions
 */
public interface MarriageRegistrationService {

    //TODO return warnings

    /**
     * add a new marriage notice
     *
     * @param notice marriage notice
     * @param isMale true if notice type is male or both
     * @param user   user who perform the action
     */
    public void addMarriageNotice(MarriageRegister notice, boolean isMale, User user);

    /**
     * get marriage register object by its idUKey value
     *
     * @param idUKey primary key of the record
     * @param user   user who performs the action
     * @return marriage register object
     */
    public MarriageRegister getByIdUKey(long idUKey, User user);

    /**
     * Returns a paginated list of Marriage Registers for which marriage notices are awaiting approval by ADR or higher
     * authority based on the specified DSDivision
     *
     * @param dsDivision the divisional secretariat
     * @param pageNo     the page number of the results required
     * @param noOfRows   the number of rows to return per page
     * @param active     include currently active items
     * @param user       the user initiating the action
     * @return the list of marriage notices pending approval
     */
    public List<MarriageRegister> getMarriageNoticePendingApprovalByDSDivision(DSDivision dsDivision, int pageNo,
        int noOfRows, boolean active, User user);

    /**
     * Returns a paginated list of Marriage Registers for which marriage notices are awaiting approval by ADR or higher
     * authority based on the specified MRDivision
     *
     * @param mrDivision the Marriage Registration Division
     * @param pageNo     the page number of the results required
     * @param noOfRows   the number of rows to return per page
     * @param active     include currently active items
     * @param user       the user initiating the action
     * @return the list of marriage notices pending approval
     */
    public List<MarriageRegister> getMarriageNoticePendingApprovalByMRDivision(MRDivision mrDivision, int pageNo,
        int noOfRows, boolean active, User user);

    /**
     * Returns the active/inactive list of approval pending marriage notices awaiting approval by ADR or higher
     * authority
     *
     * @param pinOrNic the unique PIN or NIC of male or female party
     * @param active   include currently active items
     * @param user     the user initiating the action
     * @return list of marriage records
     */
    public List<MarriageRegister> getMarriageNoticePendingApprovalByPINorNIC(String pinOrNic, boolean active, User user);

    /**
     * Returns the active/inactive MarriageRegister record for given serial number of any party and under selected
     * MRDivision
     *
     * @param mrDivision the Marriage Registration Division
     * @param serialNo   the serial number of the marriage notice (male or female party)
     * @param user       the user initiating the action
     * @return list of marriage records, but should be a single marriage record
     */
    public List<MarriageRegister> getMarriageNoticePendingApprovalByMRDivisionAndSerial(MRDivision mrDivision,
        long serialNo, User user);

    /**
     * get active marriage notice(marriage register objects) by male party and female party identification numbers
     * note: record must be active and in data entry mode
     *
     * @param maleIdentification   male pin or nic  number
     * @param femaleIdentification female pin or nic number
     * @param user                 the user initiating the action
     * @return marriage notice (marriage register object)
     */
    public MarriageRegister getActiveMarriageNoticeByMaleAndFemaleIdentification(String maleIdentification,
        String femaleIdentification, User user);

    /**
     * update marriage notice/register
     *
     * @param marriageRegister marriage register object to be updated
     * @param user             user who performs the action
     */
    public void updateMarriageRegister(MarriageRegister marriageRegister, User user);

    /**
     * adding second notice(actually updating the existing notice record)
     *
     * @param notice notice(marriage register object)
     * @param isMale is male/both or a female notice
     * @param user   the user who performing the action
     */
    public void addSecondMarriageNotice(MarriageRegister notice, boolean isMale, User user);


    /**
     * removing a marriage notice record
     *
     * @param idUKey     idUKey ot the record to be removed
     * @param noticeType type of the notice needs to be remove.
     * @param user       user who performs the action
     */
    public void deleteMarriageNotice(long idUKey, MarriageNotice.Type noticeType, User user);
}
