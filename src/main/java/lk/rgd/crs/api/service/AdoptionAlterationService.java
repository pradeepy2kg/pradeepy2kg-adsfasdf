package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.AdoptionAlteration;

import java.util.List;

/**
 * @author Duminda Dharmakeerthi
 */
public interface AdoptionAlterationService {

    public  AdoptionAlteration getAdoptionAlterationByIdUKey(long idUKey);

    public void addAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user);

    public void updateAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user);

    public void deleteAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user);

    public void approveAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user);

    public void rejectAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user);
    
    public List<AdoptionAlteration> getAdoptionAlterationsForApproval(User user);
}
