package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.AdoptionAlteration;

/**
 * @author Duminda Dharmakeerthi
 */
public interface AdoptionAlterationService {
    
    public void addAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user);

    public void updateAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user);
}
