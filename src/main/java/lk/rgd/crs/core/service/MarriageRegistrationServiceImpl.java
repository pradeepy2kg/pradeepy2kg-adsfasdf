package lk.rgd.crs.core.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author amith jayasekara
 *         implementation of the marriage registration service interface
 */
public class MarriageRegistrationServiceImpl implements MarriageRegistrationService {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addMarriageNotice(MarriageRegister notice, User user) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
