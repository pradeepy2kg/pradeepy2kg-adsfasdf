package lk.rgd.crs.core.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.domain.Witness;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author amith jayasekara
 *         implementation of the marriage registration service interface
 */
public class MarriageRegistrationServiceImpl implements MarriageRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(MarriageRegistrationServiceImpl.class);

    private final MarriageRegistrationDAO marriageRegistrationDAO;

    public MarriageRegistrationServiceImpl(MarriageRegistrationDAO marriageRegistrationDAO) {
        this.marriageRegistrationDAO = marriageRegistrationDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addMarriageNotice(MarriageRegister notice, User user, boolean isMale) {
        logger.debug("adding new marriage notice :serial number  {}", notice.getSerialNumber());
        //persisting witness
        addWitnesses(notice, isMale);
        marriageRegistrationDAO.addMarriageNotice(notice, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public MarriageRegister getByIdUKey(long idUKey, User user) {
        logger.debug("attempt to get marriage register by idUKey : {} ", idUKey);
        return marriageRegistrationDAO.getByIdUKey(idUKey);
    }

    private void addWitnesses(MarriageRegister marriageRegister, boolean isMale) {
        if (isMale) {
            //persisting male witnesses
            marriageRegistrationDAO.addWitness(marriageRegister.getMaleNoticeWitness_1());
            marriageRegistrationDAO.addWitness(marriageRegister.getMaleNoticeWitness_2());
        } else {
            //persisting female notice witnesses
            marriageRegistrationDAO.addWitness(marriageRegister.getFemaleNoticeWitness_1());
            marriageRegistrationDAO.addWitness(marriageRegister.getFemaleNoticeWitness_2());
        }

    }
}
