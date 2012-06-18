package lk.rgd.common.api.service;

import lk.rgd.common.api.domain.CommonStatistics;
import lk.rgd.common.api.domain.User;

/**
 * @author shan
 */
//TODO Not completed yet...
public interface StatisticsCollectorService {

    /**
     * @param user user who performs the actions
     * @return CommonStatistics   object
     */
    public CommonStatistics getCommonBirthCertificateCount(String user);

    /**
     * @param user user who performs the actions
     * @return CommonStatistics   object
     */
    public CommonStatistics getCommonDeathCertificateCount(String user);

    /**
     * @param user user who performs the actions
     * @return CommonStatistics   object
     */
    public CommonStatistics getCommonMarriageCertificateCount(String user);

}
