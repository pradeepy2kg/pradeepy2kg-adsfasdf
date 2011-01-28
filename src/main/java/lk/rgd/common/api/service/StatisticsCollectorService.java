package lk.rgd.common.api.service;

import lk.rgd.common.api.domain.CommonStatistics;
import lk.rgd.common.api.domain.User;

/**
 * @author shan
 */
//TODO Not completed yet...
public interface StatisticsCollectorService {

    /**
     *
     * @param user
     * @return
     */
    public CommonStatistics getCommonBirthCertificateCount(String user);

    /**
     * 
     * @param user
     * @return
     */
    public CommonStatistics getCommonDeathCertificateCount(String user);

    /**
     * 
     * @param user
     * @return
     */
    public CommonStatistics getCommonMarriageCertificateCount(String user);

}
