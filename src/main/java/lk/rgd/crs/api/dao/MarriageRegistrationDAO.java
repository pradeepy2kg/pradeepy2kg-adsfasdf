package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.domain.Witness;

import java.util.List;

/**
 * @author amith jayasekara
 *         DAO level interface for marriage registration related functions
 */
public interface MarriageRegistrationDAO {
    /**
     * add marriage notice
     *
     * @param notice marriage notice
     * @param user   user who perform action
     */
    public void addMarriageNotice(MarriageRegister notice, User user);

    /**
     * adding a witness
     *
     * @param witness witness object to be add
     */
    public void addWitness(Witness witness);

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
     * get active marriage notice(marriage register object) where male and female pin or nic numbers are matched
     *
     * @param maleIdentification   male party pin or nic
     * @param femaleIdentification female party pin or nic
     * @return marriage notice(register object)
     */
    public List<MarriageRegister> getActiveMarriageNoticeByMaleFemaleIdentification(String maleIdentification,
        String femaleIdentification);
}
