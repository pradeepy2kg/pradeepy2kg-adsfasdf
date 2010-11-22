package lk.rgd.crs.core.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author amith jayasekara
 *         implementation of the marriage registration service interface
 */
public class MarriageRegistrationServiceImpl implements MarriageRegistrationService {

    private final MarriageRegistrationDAO marriageRegistrationDAO;

    public MarriageRegistrationServiceImpl(MarriageRegistrationDAO marriageRegistrationDAO) {
        this.marriageRegistrationDAO = marriageRegistrationDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addMarriageNotice(MarriageRegister notice, User user) {
        marriageRegistrationDAO.addMarriageNotice(notice, user);
    }
}
