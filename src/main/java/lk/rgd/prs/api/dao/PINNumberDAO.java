package lk.rgd.prs.api.dao;

import lk.rgd.prs.api.domain.PINNumber;

/**
 * @author asankha
 */
public interface PINNumberDAO {

    public PINNumber getLastPINNumber(int dateOfBirth);

    public void updateLastPINNumber(PINNumber lastPIN);

    public void addLastPINNumber(PINNumber lastPIN);

    public void deleteLastPINNumber(PINNumber lastPIN);
}
