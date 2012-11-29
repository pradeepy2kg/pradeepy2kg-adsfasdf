package lk.rgd.crs.core.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.AdoptionAlterationDAO;
import lk.rgd.crs.api.domain.AdoptionAlteration;
import lk.rgd.crs.api.service.AdoptionAlterationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Duminda Dharmakeerthi
 */
public class AdoptionAlterationServiceImpl implements AdoptionAlterationService{

    private static final Logger logger = LoggerFactory.getLogger(AdoptionAlterationServiceImpl.class);
    private final AdoptionAlterationDAO adoptionAlterationDAO;

    public AdoptionAlterationServiceImpl(AdoptionAlterationDAO adoptionAlterationDAO) {
        this.adoptionAlterationDAO = adoptionAlterationDAO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
