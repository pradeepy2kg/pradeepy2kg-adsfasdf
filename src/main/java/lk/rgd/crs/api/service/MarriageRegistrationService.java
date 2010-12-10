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
     * @param type   notice type
     * @param user   user who perform the action
     */
    public void addMarriageNotice(MarriageRegister notice, MarriageNotice.Type type, User user);

    /**
     * add a new marriage Register
     *
     * @param marriageRegister marriage register Entry for muslim marriages
     * @param user             user who perform the action
     */
    public void addMarriageRegister(MarriageRegister marriageRegister, User user);

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
     * @param active     include currently active items
     * @param user       the user initiating the action
     * @return list of marriage records, but should be a single marriage record
     */
    public List<MarriageRegister> getMarriageNoticePendingApprovalByMRDivisionAndSerial(MRDivision mrDivision,
        long serialNo, boolean active, User user);

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
     *                   <br> notes :
     *                   <u>delete operation works as follows </u>
     *                   there are three cases in removing a marriage notice
     *                   case 1: isBothSubmitted is true that means only one notice is available for delete
     *                   in that case we can simple remove the data base row
     *                   case 2 : isBothSubmitted is false that means there can be more than one marriage notices(at most 2)
     *                   case 2.1:
     *                   having only one marriage notice is available(it could be male party submitted one or female party submitted one)
     *                   in this case also we can just remove data base row
     *                   <p/>
     *                   case 2.2 : there are two notices are remaining in the marriage register row so we cannot simple remove the data
     *                   base row because it is removing the other notice as well.
     *                   So we have to update the data base row for that removing
     *                   <i>as an example :
     *                   if both male and female party submitted notices are available and you just need to remove female party
     *                   submitted notice in that case we have to update the data base row by removing female party notice related columns
     *                   note:states regarding to removal
     *                   case 1:  record must be in DATA_ENTRY
     *                   for removal of male notice record must be in either DATA_ENTRY or FEMALE_NOTICE_APPROVE state
     *                   for removal of female notice record must in either DATA_ENTRY or MALE_NOTICE_APPROVE state
     */
    public void deleteMarriageNotice(long idUKey, MarriageNotice.Type noticeType, User user);

    /**
     * get pending (REG_DATA_ENTRY state) marriage register objects by serial number of the marriage register record
     * and the idUKey of the MRDivision
     *
     * @param serialNumber serial number of the marriage register record
     * @param mrDivision   mr division  which marriage captured
     * @param pageNumber   page number(for pagination)
     * @param numOfRows    number of results need to be fetched
     * @param active       what kind of records (active or inactive records)
     * @param user         user who performs the task
     * @return
     */
    public List<MarriageRegister> getMarriageRegisterBySerialAndMRDivision(long serialNumber,
        MRDivision mrDivision, int pageNumber, int numOfRows, boolean active, User user);

    /**
     * get pending marriage registration list by given DSDivision
     *
     * @param dsDivision DSDivision
     * @param pageNumber page number(for pagination)
     * @param numOfRows  number of results need to be fetched
     * @param active     record type (active or inactive)
     * @param user       user who performs the action
     * @return list of marriage register objects in a given DS Division
     */
    public List<MarriageRegister> getMarriageRegistersByDSDivision(DSDivision dsDivision, int pageNumber,
        int numOfRows, boolean active, User user);

    /**
     * get marriage registration list by given MRDivision
     *
     * @param mrDivision MRDivision
     * @param pageNumber page number(for pagination)
     * @param numOfRows  number of results need to be fetched
     * @param active     record type (active or inactive)
     * @param user       user who performs the action
     * @return list of marriage register objects in a given MR Division
     */
    public List<MarriageRegister> getMarriageRegisterByMRDivision(MRDivision mrDivision, int pageNumber,
        int numOfRows, boolean active, User user);

    /**
     * Returns the active/inactive list of Marriage Registers by PIN or NIC of male or female party
     *
     * @param pinOrNic the unique PIN or NIC of male or female party
     * @param active   include currently active items
     * @param user     the user initiating the action
     * @return list of marriage records
     */
    public List<MarriageRegister> getMarriageRegisterByPINorNIC(String pinOrNic, boolean active,
        User user);

    /**
     * approving marriage notice by idUKey and notice type
     *
     * @param idUKey idUKey of the marriage notice
     * @param type   type of the notice
     * @param user   user who performing the action
     *               <p/>
     *               <br> <h3><i><b>approving process work as follow</i></b> </h3>
     *               <p>
     *               if the <b>notice is single</b> that means (both parties only submit one marriage notice) record <b><u>must</b></u>
     *               be in DATA_ENTRY state and after approving notice change it's state in to NOTICE_APPROVED state that mean register
     *               record is completely approved and editing for that notice is not allowed </p>
     *               <p/>
     *               else
     *               there are two approving cases<br>
     *               <i>(do not care about how many notices are available there can be 1 or 2 notices)</i>
     *               </p>
     *               case 1;
     *               no notice has been approved,
     *               as an example there are 2 notices available but both are not approved
     *               assume we are trying to approve MALE_NOTICE
     *               in this case this method change state of the marriage notice(register) into MALE_NOTICE_APPROVED and
     *               same scenario for approving FEMALE_NOTICE(actually vise-versa)
     *               case 2:
     *               one notice has been approved
     *               assume case 1 is completed that mean MALE_NOTICE is approved and register in state MALE_NOTICE_APPROVED
     *               and now we are trying to approve FEMALE_NOTICE
     *               in this scenario we change notice state in to NOTICE_APPROVED state directly <b><u>not in to  FEMALE_NOTICE_APPROVED
     *               now notice is fully approved
     *               <p/>
     *               note:if any case change its state in to notice approved it means this is the only active notice that can be have
     *               for that couple.
     *               the other fact we have to consider when approving marriage notice is weather other party has approved
     *               (not the license requesting party)
     *               it is looks like this
     *               <p/>
     *               if(SINGLE NOTICE)
     *               no need to check
     *               else
     *               if(male party request license)
     *               then
     *               to approve male notice and complete approval female party must be approved
     *               and vise-versa
     */
    public void approveMarriageNotice(long idUKey, MarriageNotice.Type type, User user);
}
