package lk.rgd.common.core.service;

import lk.rgd.common.api.domain.CommonStatistics;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.StatisticsCollectorService;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BirthDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author shan
 */
//TODO Not completed yet...
public class StatisticsCollectorServiceImpl implements StatisticsCollectorService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsCollectorServiceImpl.class);
    private final BirthDeclarationDAO birthDeclarationDAO;

    public StatisticsCollectorServiceImpl(BirthDeclarationDAO birthDeclarationDAO) {
        this.birthDeclarationDAO = birthDeclarationDAO;
    }

    /**
     * @inheritDoc
     */
    public CommonStatistics getCommonBirthCertificateCount(User user) {
        CommonStatistics commonStat = new CommonStatistics();
        commonStat.setTotalSubmissions(birthDeclarationDAO.getBirthCertificateCount(BirthDeclaration.State.ARCHIVED_CERT_PRINTED));
        commonStat.setApprovedItems(birthDeclarationDAO.getBirthCertificateCount(BirthDeclaration.State.APPROVED));
        commonStat.setRejectedItems(birthDeclarationDAO.getBirthCertificateCount(BirthDeclaration.State.ARCHIVED_REJECTED));
        commonStat.setTotalPendingItems(birthDeclarationDAO.getBirthCertificateCount(BirthDeclaration.State.DATA_ENTRY));
        return commonStat;
    }

    /**
     * @inheritDoc
     */
    public CommonStatistics getCommonDeathCertificateCount(User user) {
        CommonStatistics commonStat = new CommonStatistics();
        return commonStat;
    }

}
