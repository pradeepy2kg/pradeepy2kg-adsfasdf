package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.bean.UserWarning;
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
     * @param notice         notice(marriage register object)
     * @param type           type of the second notice
     * @param ignoreWarnings ignore warnings
     * @param undo           undo the approval of the first notice
     * @param user           the user who performing the action
     * @return user warnings <p/>
     *         <p/>
     *         warning is issued in special case
     *         warning is given for the user for asking should female keep the license
     *         or should rollback the approval of male's approval
     *         <p/>      undo scenario
     *         this is the scenario that can be happen in many rare cases and in can only encounter when
     *         there are two notice submitted by two parties
     *         <p/>
     *         assume Male party is submitting the notice first and he declare female party as the
     *         license owner and before female party submit the notice Male notice is being approved by
     *         the ADR this scenario happens now .assume now female is submitting the notice and she
     *         declare male party as the license owner but the approval process says  LP (license party)
     *         can only be approved iff OP get approved  but now Male party is being approved first so
     *         This party cannot hold the license in that case DEO is asked to choose two options
     *         <p/>
     *         1>ask female party to keep to be the license owner as the previous party declare
     *         or
     *         <p/>
     *         2>if female party does not want to be the license party and if she said male must get the
     *         license ,in that case we roll back the approval of the female party and allow female to
     *         declare male as the license owner in that case  male notice has to approve again by the ADR
     *         after female party get approved and vise versa
     *         <p/>
     *         note in funny situations male party (im referring to the above story) may refuse and he may
     *         declare female as the owner again this process can be repeating over and over again and we
     *         cannot avoid that pragmatically so it should resolve manually
     */
    public List<UserWarning> addSecondMarriageNotice(MarriageRegister notice, MarriageNotice.Type type,
        boolean ignoreWarnings, boolean undo, User user);


    /**
     * removing a marriage notice record
     *
     * @param idUKey     idUKey ot the record to be removed
     * @param noticeType type of the notice needs to be remove.
     * @param user       user who performs the action
     *                   <br> notes :
     *                   <u>delete operation works as follows </u>
     *                   there are three cases in removing a marriage notice
     *                   case 1: isBothSubmitted is true that means only one notice is available for delete in that case
     *                   we can simple remove the data base row case 2 : isBothSubmitted is false that means there can
     *                   be more than one marriage notices(at most 2)
     *                   <p/>
     *                   case 2.1:
     *                   having only one marriage notice is available(it could be male party submitted one or female
     *                   party submitted one)in this case also we can just remove data base row
     *                   <p/>
     *                   case 2.2 : there are two notices are remaining in the marriage register row so we cannot simple
     *                   remove the data base row because it is removing the other notice as well.So we have to update
     *                   the data base row for that removing
     *                   <p/>
     *                   <i>as an example :
     *                   if both male and female party submitted notices are available and you just need to remove female
     *                   party submitted notice in that case we have to update the data base row by removing female party
     *                   notice related columns
     *                   <p/>
     *                   note:states regarding to removal
     *                   <br>
     *                   case 1:  record must be in DATA_ENTRY
     *                   <br>
     *                   for removal of male notice record must be in either DATA_ENTRY or FEMALE_NOTICE_APPROVE state
     *                   <br>
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

    /**
     * reject a marriage notice
     *
     * @param idUKey  idUKey if the notice
     * @param type    type of the notice
     * @param comment cause for the rejection
     * @param user    user who perform the action
     *                <p>rejection process </p>
     *                <p/>
     *                <p>a notice <strong>can be rejected</strong> when it is on following states </p>
     *                <ol>
     *                <li>DATA_ENTRY</li>
     *                <li>MALE_NOTICE_APPROVED</li>
     *                <li>FEMALE_NOTICE_APPROVED</li>
     *                </ol>
     *                <p/>
     *                <p>rejection of one notice is effecting on the other notice as well </p>
     *                <p/>
     *                <p>as an example even FEMALE_NOTICE is approved that mean it is in
     *                FEMALE_NOTICE_APPROVED state if MALE_NOTICE is being </p>
     *                <p/>
     *                <p>rejected both notices are treated as REJECTED and data base row is being
     *                archived with a comment </p>
     *                <p/>
     *                <p><em>state changes as follows </em></p>
     *                <p/>
     *                <p>if current state is <strong>DATA_ENTRY</strong> after a rejection(any
     *                MALE/FEMALE/BOTH) change to <strong>REJECT</strong> </p>
     *                <p/>
     *                <p>if current state is <strong>MALE_NOTICE_APPROVED</strong> after rejecting
     *                FEMALE_NOTICE it's become <strong>FEMALE_NOTICE_REJECTED</strong></p>
     *                <p/>
     *                <p>if current state is <strong>FEMALE_NOTICE_APPROVED</strong> after rejecting
     *                MALE_NOTICE it's become <strong>MALE_NOTICE_REJECTED</strong></p>
     *                <p/>
     *                <p></p>
     *                <p/>
     *                <p>all three rejection states are <strong>final status </strong></p>
     */
    public void rejectMarriageNotice(long idUKey, MarriageNotice.Type type, String comment, User user);

}
