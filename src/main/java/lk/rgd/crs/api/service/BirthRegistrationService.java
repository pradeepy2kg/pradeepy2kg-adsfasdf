package lk.rgd.crs.api.service;

import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.BirthDeclaration;

import java.util.List;

public interface BirthRegistrationService {

    public List<UserWarning> registerNormalBirth(BirthDeclaration bdf, boolean ignoreWarnings);
    public List<UserWarning> lateRegistrationOfBirth(BirthDeclaration bdf, boolean ignoreWarnings);

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