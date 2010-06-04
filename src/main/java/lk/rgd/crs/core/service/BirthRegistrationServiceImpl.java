package lk.rgd.crs.core.service;

import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BirthRegistrationServiceImpl implements BirthRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegistrationServiceImpl.class);
    private final BirthDeclarationDAO birthDeclarationDAO;

    public BirthRegistrationServiceImpl(BirthDeclarationDAO dao) {
        birthDeclarationDAO = dao;    
    }

    public List<UserWarning> registerNormalBirth(BirthDeclaration bdf, boolean ignoreWarnings) {
        birthDeclarationDAO.addBirthDeclaration(bdf);
        return null; //todo handle warnings
    }

    public List<UserWarning> lateRegistrationOfBirth(BirthDeclaration bdf, boolean ignoreWarnings) {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Returns the Birth Declaration object for a given Id
     * @param  bdId Birth Declarion Id for the given declaration
     * @Return BirthDeclaration
     */
    public BirthDeclaration getById(long bdId) {
        return birthDeclarationDAO.getById(bdId);
    }

    /**
     * Returns the Birth Declaration object for a given bdf serialNo
     * @param  serialNo bdfSerialNo given to the Birth Declarion
     * @Return BirthDeclaration
     */
    public BirthDeclaration getBySerialNo(String serialNo) {
        return birthDeclarationDAO.getBySerialNo(serialNo);
    }
}
