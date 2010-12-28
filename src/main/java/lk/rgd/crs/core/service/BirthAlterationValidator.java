package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.crs.api.service.BirthAlterationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * basic back end validation class for death alterations
 *
 * @author amith jayasekara
 */

public class BirthAlterationValidator {
    private static final Logger logger = LoggerFactory.getLogger(BirthAlterationValidator.class);
    private final BirthAlterationService birthAlterationService;

    public BirthAlterationValidator(BirthAlterationService birthAlterationService) {
        this.birthAlterationService = birthAlterationService;
    }

    public void checkOnGoingAlterationOnThisSection(long birthCertificateId, BirthAlteration.
        AlterationType alterationType, User user) {
        boolean isOnGoing = false;
        //get active birth alterations for
        List<BirthAlteration> birthAlterationList = birthAlterationService.
            getBirthAlterationByBirthCertificateNumber(birthCertificateId, user);
        outer:
        for (BirthAlteration ba : birthAlterationList) {
            if (ba.getLifeCycleInfo().isActiveRecord()) {
                switch (alterationType) {
                    case TYPE_27:
                        if (ba.getAlt27() != null) {
                            isOnGoing = true;
                            break outer;
                        }
                        break;
                    case TYPE_27A:
                        if (ba.getAlt27A() != null) {
                            isOnGoing = true;
                            break outer;
                        }
                        break;
                    case TYPE_52_1_A:
                    case TYPE_52_1_B:
                    case TYPE_52_1_D:
                    case TYPE_52_1_E:
                    case TYPE_52_1_H:
                    case TYPE_52_1_I: {
                        if (ba.getAlt52_1() != null) {
                            isOnGoing = true;
                            break outer;
                        }
                    }
                }
            }
        }
        if (isOnGoing) {
            handleException("can not load page for alteration there is an ongoing alteration for birth certificate :" +
                birthCertificateId + " requesting act :" + alterationType,
                ErrorCodes.CAN_NOT_ADD_A_NEW_ALTERATION_ON_THIS_SECTION);
        }
    }

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }
}
