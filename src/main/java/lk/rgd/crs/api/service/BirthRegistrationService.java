package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.BirthDeclaration;

import java.util.List;

public interface BirthRegistrationService {

    /**
     * Add a new BDF by a DEO or ADR
     * @param bdf the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user the user initiating the action
     * @return a list of warnings
     */
    public List<UserWarning> addNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Update an existing BDF by a DEO or ADR before approval
     * @param bdf the BDF to be updated
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user the user initiating the action
     * @return a list of warnings
     */
    public List<UserWarning> updateNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Remove an existing BDF by a DEO or ADR before approval
     * @param bdf the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user the user initiating the action
     * @return a list of warnings
     */
    public List<UserWarning> deleteNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Returns the Birth Declaration object for a given Id
     * @param  bdId Birth Declarion Id for the given declaration
     * @Return BirthDeclaration
     */
    public BirthDeclaration getById(long bdId);

    /**
     * Returns the Birth Declaration object for a given bdf serialNo
     * @param  serialNo bdfSerialNo given to the Birth Declarion
     * @Return BirthDeclaration
     */
    public BirthDeclaration getBySerialNo(String serialNo);
}