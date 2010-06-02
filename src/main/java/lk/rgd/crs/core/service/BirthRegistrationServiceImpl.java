package lk.rgd.crs.core.service;

import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.service.BirthRegistrationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BirthRegistrationServiceImpl implements BirthRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegistrationServiceImpl.class);

    public List<UserWarning> registerNormalBirth(BirthDeclaration bdf, boolean ignoreWarnings) {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<UserWarning> lateRegistrationOfBirth(BirthDeclaration bdf, boolean ignoreWarnings) {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
