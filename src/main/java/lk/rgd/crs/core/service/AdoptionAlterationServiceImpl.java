package lk.rgd.crs.core.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.AdoptionAlterationDAO;
import lk.rgd.crs.api.dao.AdoptionOrderDAO;
import lk.rgd.crs.api.domain.AdoptionAlteration;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.api.service.AdoptionAlterationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Duminda Dharmakeerthi
 */
public class AdoptionAlterationServiceImpl implements AdoptionAlterationService {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionAlterationServiceImpl.class);
    private final AdoptionAlterationDAO adoptionAlterationDAO;
    private final AdoptionOrderDAO adoptionOrderDAO;

    public AdoptionAlterationServiceImpl(AdoptionAlterationDAO adoptionAlterationDAO, AdoptionOrderDAO adoptionOrderDAO) {
        this.adoptionAlterationDAO = adoptionAlterationDAO;
        this.adoptionOrderDAO = adoptionOrderDAO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        logger.debug("Attempt to add an alteration for adoption : {} by {}", adoptionAlteration.getAoUKey(), user.getUserId());
        AdoptionOrder adoptionOrder = adoptionOrderDAO.getById(adoptionAlteration.getAoUKey());
        adoptionAlteration = markChangedFields(adoptionAlteration, adoptionOrder);
        adoptionAlteration.setStatus(AdoptionAlteration.State.DATA_ENTRY);
        adoptionAlterationDAO.addAdoptionAlteration(adoptionAlteration, user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private AdoptionAlteration markChangedFields(AdoptionAlteration adoptionAlteration, AdoptionOrder adoptionOrder){
        if (!adoptionAlteration.getApplicantName().equals(adoptionOrder.getApplicantName())) {
            adoptionAlteration.getChangedFields().set(AdoptionAlteration.APPLICANT_NAME);
        }
        if(!adoptionAlteration.getApplicantAddress().equals(adoptionOrder.getApplicantAddress())){
            adoptionAlteration.getChangedFields().set(AdoptionAlteration.APPLICANT_ADDRESS);
        }
        if(!adoptionAlteration.getApplicantSecondAddress().equals(adoptionOrder.getApplicantSecondAddress())){
            adoptionAlteration.getChangedFields().set(AdoptionAlteration.APPLICANT_SECOND_ADDRESS);
        }
        if(!adoptionAlteration.getApplicantOccupation().equals(adoptionOrder.getApplicantOccupation())){
            adoptionAlteration.getChangedFields().set(AdoptionAlteration.APPLICANT_OCCUPATION);
        }
        if(adoptionOrder.isJointApplicant()){
            if(!adoptionAlteration.getSpouseName().equals(adoptionOrder.getSpouseName())){
                adoptionAlteration.getChangedFields().set(AdoptionAlteration.SPOUSE_NAME);
            }
            if(!adoptionAlteration.getSpouseOccupation().equals(adoptionOrder.getSpouseOccupation())){
                adoptionAlteration.getChangedFields().set(AdoptionAlteration.SPOUSE_OCCUPATION);
            }
        }
        if(adoptionOrder.getChildNewName()!= null && !adoptionOrder.getChildNewName().isEmpty()){
            if(!adoptionAlteration.getChildName().equals(adoptionOrder.getChildNewName())){
                adoptionAlteration.getChangedFields().set(AdoptionAlteration.CHILD_NAME);
            }
        }else{
            if(!adoptionAlteration.getChildName().equals(adoptionOrder.getChildExistingName())){
                adoptionAlteration.getChangedFields().set(AdoptionAlteration.CHILD_NAME);
            }
        }
        if(!adoptionAlteration.getChildBirthDate().equals(adoptionOrder.getChildBirthDate())){
            adoptionAlteration.getChangedFields().set(AdoptionAlteration.CHILD_DOB);
        }
        if(adoptionAlteration.getChildGender() != adoptionOrder.getChildGender()){
            adoptionAlteration.getChangedFields().set(AdoptionAlteration.CHILD_GENDER);
        }
        return adoptionAlteration;
    }
}
