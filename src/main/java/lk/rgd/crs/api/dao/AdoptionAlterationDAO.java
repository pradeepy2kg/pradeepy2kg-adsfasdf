package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.AdoptionAlteration;

import java.util.List;

/**
 * @author Duminda Dharmakeerthi
 */
public interface AdoptionAlterationDAO {

    public void addAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user);

    public void updateAdoptionAlteration(AdoptionAlteration adoptionAlteration, User user);

    public void deleteAdoptionAlteration(long idUKey, User user);

    public AdoptionAlteration getAdoptionAlterationByIdUKey(long idUKey);

    public List<AdoptionAlteration> getAllAdoptionAlterationRecords();

    public List<AdoptionAlteration> getAdoptionAlterationsByStatus(AdoptionAlteration.State state);

    public AdoptionAlteration getAdoptionAlterationByAOUKey(long aoUKey);
}
