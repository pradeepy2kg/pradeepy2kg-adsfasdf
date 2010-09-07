package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.BirthAlterationService;
import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.crs.api.dao.BirthAlterationDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The central service managing the CRS Birth Alteration process
 *
 * @author Indunil Moremada
 */
public class BirthAlterationServiceImpl implements BirthAlterationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthAlterationServiceImpl.class);
    private final BirthAlterationDAO birthAlterationDAO;

    public BirthAlterationServiceImpl(BirthAlterationDAO birthAlterationDAO) {
        this.birthAlterationDAO = birthAlterationDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBirthAlteration(BirthAlteration ba, User user) {
    //todo validations 
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBirthAlteration(BirthAlteration ba, User user) {
        //todo validations
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteBirthAlteration(long idUKey) {
        //todo validations
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthAlteration getById(long idUKey) {
        //todo validations
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
