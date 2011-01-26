package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.*;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageNotice;
import lk.rgd.crs.api.domain.MarriageRegister;

import java.util.Date;
import java.util.List;
import java.io.File;

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
     * @param user             Scanned Image of the Marriage Certificate
     */
    public void addMarriageRegister(MarriageRegister marriageRegister, User user, File scannedImage, String fileName);

    /**
     * get marriage register object by its idUKey value
     *
     * @param idUKey primary key of the record
     * @param user   user who performs the action
     * @return marriage register object
     */
    public MarriageRegister getByIdUKey(long idUKey, User user);

    /**
     * Find marriage register by idUKey
     *
     * @param idUKey     primary key of Marriage Register
     * @param user       user who performs the action
     * @param permission permission level of the action
     * @return
     */
    public MarriageRegister getMarriageRegisterByIdUKey(long idUKey, User user, int permission);

    /**
     * get marriage register primary key and state.
     *
     * @param idUKey     primary key of the record
     * @param user       user who performs the action
     * @param permission permission level of the action
     * @return
     */
    public MarriageRegister getMarriageRegisterByIdUKeyAndState(long idUKey, User user, int permission);

    /**
     * Returns a Marriage register filtered by serial number
     *
     * @param serialNumber Serial Number of the marriage register
     * @param user         user who performs the action
     * @param permission   permission level of the action
     * @return
     */
    public List<MarriageRegister> getMarriageRegisterBySerialNumber(long serialNumber, User user, int permission);

    /**
     * Returns a paginated list of Marriage Notices based on the specified District
     *
     * @param district the district
     * @param pageNo   the page number of the results required
     * @param noOfRows the number of rows to return per page
     * @param active   include currently active or inactive items
     * @param user     the user initiating the action
     * @return the list of marriage notices filtered by District
     */
    public List<MarriageRegister> getMarriageNoticeByDistrict(District district, int pageNo, int noOfRows,
        boolean active, User user);

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
     * Find the marriage register by male or female identification number or
     * find a list of marriage register by registrar identification number
     *
     * @param pinOrNic the identification number (male or female or registrar PIN or NIC)
     * @param active   status of the marriage register active or inactive
     * @param user     the user who perform the action
     * @return
     */
    public List<MarriageRegister> getMarriageRegisterByPINNumber(String pinOrNic, boolean active, User user);

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
     * Returns paginated list of active/inactive Marriage Notices for the given <b>MRDivision</b> and <b>submitting date
     * of male or female notice</b>
     *
     * @param mrDivision the Marriage Registration Division
     * @param startDate  the starting date of the search
     * @param endDate    the ending date of the search
     * @param pageNo     the page number of the results required
     * @param noOfRows   the number of rows to return per page
     * @param active     include currently active or inactive items
     * @param user       the user initiating the action
     * @return the matching list of marriage registrations (marriage notices)
     */
    public List<MarriageRegister> getMarriageNoticesByMRDivisionAndRegisterDateRange(MRDivision mrDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, boolean active, User user);

    /**
     * Returns paginated list of active/inactive Marriage Notices for the given <b>DSDivision</b> and <b>submitting date
     * of male or female notice</b>
     *
     * @param dsDivision the divisional secretariat
     * @param startDate  the starting date of the search
     * @param endDate    the ending date of the search
     * @param pageNo     the page number of the results required
     * @param noOfRows   the number of rows to return per page
     * @param active     include currently active or inactive items
     * @param user       the user initiating the action
     * @return the matching list of marriage registrations (marriage notices)
     */
    public List<MarriageRegister> getMarriageNoticesByDSDivisionAndRegisterDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, boolean active, User user);

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
    public void updateMarriageRegister(MarriageRegister marriageRegister, User user, File scannedImage, String fileName);

    /**
     * update Muslim marriage details
     *
     * @param marriageRegister
     * @param user
     */
    //TODO to be removed
    public void updateMuslimMarriageDetails(MarriageRegister marriageRegister, User user);

    /**
     * update marriage registration detail (registrar details, registration place, etc)
     *
     * @param marriageRegister marriage register record to be updated
     * @param user             user who performs the action
     */
    public void updateMarriageRegistrationDetails(MarriageRegister marriageRegister, User user);


    /**
     * adding second notice(actually updating the existing notice record)
     * <p/>
     * <p/>
     * warning is issued in special case
     * warning is given for the user for asking should female keep the license
     * or should rollback the approval of male's approval
     * <p/>      undo scenario
     * this is the scenario that can be happen in many rare cases and in can only encounter when
     * there are two notice submitted by two parties
     * <p/>
     * assume Male party is submitting the notice first and he declare female party as the
     * license owner and before female party submit the notice Male notice is being approved by
     * the ADR this scenario happens now .assume now female is submitting the notice and she
     * declare male party as the license owner but the approval process says  LP (license party)
     * can only be approved iff OP get approved  but now Male party is being approved first so
     * This party cannot hold the license in that case DEO is asked to choose two options
     * <p/>
     * 1>ask female party to keep to be the license owner as the previous party declare
     * or
     * <p/>
     * 2>if female party does not want to be the license party and if she said male must get the
     * license ,in that case we roll back the approval of the female party and allow female to
     * declare male as the license owner in that case  male notice has to approve again by the ADR
     * after female party get approved and vise versa
     * <p/>
     * note in funny situations male party (im referring to the above story) may refuse and he may
     * declare female as the owner again this process can be repeating over and over again and we
     * cannot avoid that pragmatically so it should resolve manually
     *
     * @param notice         notice(marriage register object)
     * @param type           type of the second notice
     * @param ignoreWarnings ignore warnings
     * @param undo           undo the approval of the first notice
     * @param user           the user who performing the action
     * @return user warnings <p/>
     */
    public List<UserWarning> addSecondMarriageNotice(MarriageRegister notice, MarriageNotice.Type type,
        boolean ignoreWarnings, boolean undo, User user);


    /**
     * removing a marriage notice record
     * <p/>
     * <br> notes :
     * <u>delete operation works as follows </u>
     * there are three cases in removing a marriage notice
     * case 1: isBothSubmitted is true that means only one notice is available for delete in that case
     * we can simple remove the data base row case 2 : isBothSubmitted is false that means there can
     * be more than one marriage notices(at most 2)
     * <p/>
     * case 2.1:
     * having only one marriage notice is available(it could be male party submitted one or female
     * party submitted one)in this case also we can just remove data base row
     * <p/>
     * case 2.2 : there are two notices are remaining in the marriage register row so we cannot simple
     * remove the data base row because it is removing the other notice as well.So we have to update
     * the data base row for that removing
     * <p/>
     * <i>as an example :
     * if both male and female party submitted notices are available and you just need to remove female
     * party submitted notice in that case we have to update the data base row by removing female party
     * notice related columns
     * <p/>
     * note:states regarding to removal
     * <br>
     * case 1:  record must be in DATA_ENTRY
     * <br>
     * for removal of male notice record must be in either DATA_ENTRY or FEMALE_NOTICE_APPROVE state
     * <br>
     * for removal of female notice record must in either DATA_ENTRY or MALE_NOTICE_APPROVE state
     *
     * @param idUKey     idUKey ot the record to be removed
     * @param noticeType type of the notice needs to be remove.
     * @param user       user who performs the action
     */
    public void deleteMarriageNotice(long idUKey, MarriageNotice.Type noticeType, User user);

    /**
     * Returns the active Marriage Register record for a given Serial Number of male or female party under a selected
     * MRDivision
     *
     * @param mrDivision the Marriage Registration Division
     * @param serialNo   the serial number to check
     * @param user       the user initiating the action
     * @return the active record with the given serial number with the MRDivision
     */
    public MarriageRegister getActiveRecordByMRDivisionAndSerialNo(MRDivision mrDivision, long serialNo, User user);

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
     * Returns paginated marriage register list filtered by district
     *
     * @param district   the district of Marriage Notice/Register performed
     * @param pageNumber page number for pagination
     * @param numOfRows  number of rows to be retured
     * @param isActive   Active or Inactive status
     * @param user       user who performs the action
     * @return
     */
    public List<MarriageRegister> getMarriageRegistersByDistrict(District district, int pageNumber,
        int numOfRows, boolean isActive, User user);

    /**
     * Returns paginated marriage register list filtered by State
     *
     * @param state    the current state of the marriage register
     * @param pageNo   page number for pagination
     * @param noOfRows number of rows to be retured
     * @param isActive Active or Inactive status
     * @param user     user who performs the action
     * @return
     */
    public List<MarriageRegister> getMarriageRegisterByState(MarriageRegister.State state, int pageNo, int noOfRows,
        boolean isActive, User user);

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
     * Returns Marriage Register list
     *
     * @param divisionType type of division (district, DS division or MR division)
     * @param divisionUKey Primary key of the division
     * @param state        state the marriage register
     * @param isActive     Active or inactive status of marriage register
     * @param startDate    from date of the date range - Marriage Date of the marriage register
     * @param endDate      to date of the date range - Marriage Date of the marriage register
     * @param pageNumber   the page number (starting from 1)
     * @param numOfRows    no of rows to be retured
     * @return
     */
    public List<MarriageRegister> getMarriageRegisterList(String divisionType, int divisionUKey,
        MarriageRegister.State state, boolean isActive, Date startDate, Date endDate,
        int pageNumber, int numOfRows, User user);

    /**
     * Returns the active/inactive list of Marriage Registers by PIN or NIC of male or female party
     *
     * @param pinOrNic the unique PIN or NIC of male or female party
     * @param active   include currently active items
     * @param user     the user initiating the action
     * @return list of marriage records
     */
    public List<MarriageRegister> getMarriageRegisterByPINorNIC(String pinOrNic, boolean active, User user);

    /**
     * approving marriage notice by idUKey and notice type
     * <p/>
     * <br> <h3><i><b>approving process work as follow</i></b> </h3>
     * <p>
     * if the <b>notice is single</b> that means (both parties only submit one marriage notice) record <b><u>must</b></u>
     * be in DATA_ENTRY state and after approving notice change it's state in to NOTICE_APPROVED state that mean register
     * record is completely approved and editing for that notice is not allowed </p>
     * <p/>
     * else
     * there are two approving cases<br>
     * <i>(do not care about how many notices are available there can be 1 or 2 notices)</i>
     * </p>
     * case 1;
     * no notice has been approved,
     * as an example there are 2 notices available but both are not approved
     * assume we are trying to approve MALE_NOTICE
     * in this case this method change state of the marriage notice(register) into MALE_NOTICE_APPROVED and
     * same scenario for approving FEMALE_NOTICE(actually vise-versa)
     * case 2:
     * one notice has been approved
     * assume case 1 is completed that mean MALE_NOTICE is approved and register in state MALE_NOTICE_APPROVED
     * and now we are trying to approve FEMALE_NOTICE
     * in this scenario we change notice state in to NOTICE_APPROVED state directly <b><u>not in to  FEMALE_NOTICE_APPROVED
     * now notice is fully approved
     * <p/>
     * note:if any case change its state in to notice approved it means this is the only active notice that can be have
     * for that couple.
     * the other fact we have to consider when approving marriage notice is weather other party has approved
     * (not the license requesting party)
     * it is looks like this
     * <p/>
     * if(SINGLE NOTICE)
     * no need to check
     * else
     * if(male party request license)
     * then
     * to approve male notice and complete approval female party must be approved
     * and vise-versa
     *
     * @param idUKey         idUKey of the marriage notice
     * @param type           type of the notice
     * @param ignoreWarnings ignore warnings and approve
     * @param user           user who performing the action
     */
    public List<UserWarning> approveMarriageNotice(long idUKey, MarriageNotice.Type type, boolean ignoreWarnings, User user);

    /**
     * reject a marriage notice
     * <p/>
     * <p>rejection process </p>
     * <p/>
     * <p>a notice <strong>can be rejected</strong> when it is on following states </p>
     * <ol>
     * <li>DATA_ENTRY</li>
     * <li>MALE_NOTICE_APPROVED</li>
     * <li>FEMALE_NOTICE_APPROVED</li>
     * </ol>
     * <p/>
     * <p>rejection of one notice is effecting on the other notice as well </p>
     * <p/>
     * <p>as an example even FEMALE_NOTICE is approved that mean it is in
     * FEMALE_NOTICE_APPROVED state if MALE_NOTICE is being </p>
     * <p/>
     * <p>rejected both notices are treated as REJECTED and data base row is being
     * archived with a comment </p>
     * <p/>
     * <p><em>state changes as follows </em></p>
     * <p/>
     * <p>if current state is <strong>DATA_ENTRY</strong> after a rejection(any
     * MALE/FEMALE/BOTH) change to <strong>REJECT</strong> </p>
     * <p/>
     * <p>if current state is <strong>MALE_NOTICE_APPROVED</strong> after rejecting
     * FEMALE_NOTICE it's become <strong>FEMALE_NOTICE_REJECTED</strong></p>
     * <p/>
     * <p>if current state is <strong>FEMALE_NOTICE_APPROVED</strong> after rejecting
     * MALE_NOTICE it's become <strong>MALE_NOTICE_REJECTED</strong></p>
     * <p/>
     * <p></p>
     * <p/>
     * <p>all three rejection states are <strong>final status </strong></p>
     *
     * @param idUKey  idUKey if the notice
     * @param type    type of the notice
     * @param comment cause for the rejection
     * @param user    user who perform the action
     * @throws lk.rgd.crs.CRSRuntimeException throw when illegal usage of function
     */
    public void rejectMarriageNotice(long idUKey, MarriageNotice.Type type, String comment, User user);

    /**
     * get marriage register object for printing license for marriage
     * <br>
     * <p>only allowed state is NOTICE_APPROVED </p>
     *
     * @param idUKey idUKey of the record need to be print license
     * @param user   user who performs the action
     * @return MarriageRegister object
     * @throws lk.rgd.crs.CRSRuntimeException
     */
    public MarriageRegister getMarriageNoticeForPrintLicense(long idUKey, User user);

    /**
     * Approve muslim or noticed marriage registration
     *
     * @param idUKey primary key of the marriage register
     * @param user   user who perform the action
     */
    public void approveMarriageRegister(long idUKey, User user);

    /**
     * Update the status of marriage register
     *
     * @param idUKey     primary key of the marriage register
     * @param user       user who perform the action
     * @param permission permission level of the action
     * @param state      state of the marriage register
     */
    public void updateMarriageRegisterState(long idUKey, User user, int permission, MarriageRegister.State state);

    /**
     * reject muslim or noticed marriage registration
     *
     * @param idUKey  primary key of the marriage register
     * @param comment comment on the marriage register
     * @param user    user who perform the action
     */
    public void rejectMarriageRegister(long idUKey, String comment, User user);

    /**
     * marking marriage notice as license printed
     * Only the NOTICE_APPROVED state records are able to mark as notice printed
     *
     * @param idUKey               idUKey of the notice
     * @param licenseIssueLocation license printed location id
     * @param issuedUserId         license issuing user
     * @param user                 user who perform the action
     */
    public void markLicenseToMarriageAsPrinted(long idUKey, Location licenseIssueLocation, User issuedUserId, User user);

    /**
     * Marking the Extract of Marriage as Printed
     *
     * @param idUKey         idUKey of the marriage register
     * @param issuedLocation location where the Extract of Marriage is issued
     * @param issuedUserId   the user who issued the Extract of Marriage
     * @param user           user who perform the action
     */
    public void markMarriageExtractAsPrinted(long idUKey, Location issuedLocation, User issuedUserId, User user);

    /**
     * Returns all statistics information related to Deaths
     *
     * @param user
     * @return CommonStatistics object which encapsulated all the death statistics information
     */
    public CommonStatistics getCommonMarriageCertificateCount(String user);

    /**
     * Returns all statistics information belongs to given user. Return object cannot be null
     *
     * @param user user DEO / ADR
     * @return CommonStatistics object which encapsulated all the birth statistics information
     */
    public CommonStatistics getMarriageStatisticsForUser(String user);

    /**
     * Return the root path to the scanned copies of marriage certificates
     *
     * @return the root file system path to the scanned copies
     */
    public String getContentRoot();

    /**
     * Return the content type of scanned marriage certificates
     *
     * @return the content type of scanned certificate images
     */
    public String getContentType();

    /**
     * updating marriage notice record details
     * <br>
     * in follow case this method issues nn empty list of UserWarnings
     * <ul>
     * <li>assume there are two notice and male notice is declare female as the license owner and ADR approve male notice</li>
     * <li>but female notice is still allowed to edit and now female declare male as the license owner but by definition
     * that cannot be happen in this case </li>
     * <li>only in this case this method issue non empty user warning list</li>
     * </ul>
     *
     * @param notice notice to be updated
     * @param user   user who performs the action
     * @param type   type of the notice to be edited
     * @return list of warnings while updating marriage notice
     * @throws lk.rgd.crs.CRSRuntimeException
     */
    public List<UserWarning> editMarriageNotice(MarriageRegister notice, MarriageNotice.Type type, boolean ignoreWarnings,
        User user);

    /**
     * Get the scanned image path for the marriage registration record. This method is audited
     *
     * @param idUKey the idUKey of the marriage record
     * @param user   the user for auditing
     * @return the path within the content repository, or null if not stored
     */
    public String getImagePathByIdUKey(long idUKey, User user);
}
