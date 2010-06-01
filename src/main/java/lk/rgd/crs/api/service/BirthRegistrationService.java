package lk.rgd.crs.api.service;

import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.BirthDeclaration;

import java.util.List;

public interface BirthRegistrationService {

    public List<UserWarning> registerNormalBirth(BirthDeclaration bdf, boolean ignoreWarnings);
    public List<UserWarning> lateRegistrationOfBirth(BirthDeclaration bdf, boolean ignoreWarnings);
}