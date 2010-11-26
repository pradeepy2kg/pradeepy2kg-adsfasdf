package lk.rgd.crs.core.service;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.domain.Witness;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import lk.rgd.crs.core.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * implementation of the marriage registration service interface
 * todo check user permissions for performing tasks
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
    public void addMarriageNotice(MarriageRegister notice, boolean isMale, User user) {
        logger.debug("adding new marriage notice :male pin number  {}", notice.getMale().getIdentificationNumberMale());
        //TODO check users permission to add marriage
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

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageNoticePendingApprovalByDSDivision(DSDivision dsDivision, int pageNo,
        int noOfRows, User user) {
        logger.debug("Get MarriageNotices pending approval by DSDivision : {}", dsDivision.getDsDivisionUKey());
        ValidationUtils.validateAccessToDSDivison(dsDivision, user);
        return marriageRegistrationDAO.getPaginatedListForStateByDSDivision(dsDivision,
            MarriageRegister.State.DATA_ENTRY, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public MarriageRegister getActiveMarriageNoticeByMaleAndFemaleIdentification(String maleIdentification,
        String femaleIdentification, User user) {
        logger.debug("getting active marriage notice for male identification : {} :and female identification : {}",
            maleIdentification, femaleIdentification);
        //getting latest record
        List<MarriageRegister> records = marriageRegistrationDAO.getActiveMarriageNoticeByMaleFemaleIdentification
            (maleIdentification, femaleIdentification);
        if (records.size() > 0) {
            return records.get(0);
        }
        return null;
        //throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void updateMarriageRegister(MarriageRegister marriageRegister, User user) {
        //todo check user permissions
        //     marriageRegistrationDAO.updateMarriageRegister(marriageRegister,user);
        throw new UnsupportedOperationException("not yet implemented");
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
