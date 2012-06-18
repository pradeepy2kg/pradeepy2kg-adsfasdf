package lk.rgd.common.core.service;

import lk.rgd.common.api.domain.CommonStatistics;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.StatisticsCollectorService;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.DeathRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * @author shan
 */
//TODO Not completed yet...
public class StatisticsCollectorServiceImpl implements StatisticsCollectorService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsCollectorServiceImpl.class);
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final DeathRegisterDAO deathRegisterDAO;

    public StatisticsCollectorServiceImpl(BirthDeclarationDAO birthDeclarationDAO, DeathRegisterDAO deathRegisterDAO) {
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.deathRegisterDAO = deathRegisterDAO;
    }

    /**
     * @inheritDoc
     */
    public CommonStatistics getCommonBirthCertificateCount(String user) {
        CommonStatistics commonStat = new CommonStatistics();

        int data_entry = birthDeclarationDAO.getBirthCertificateCount(BirthDeclaration.State.DATA_ENTRY, new Date(), new Date());
        int approved = birthDeclarationDAO.getBirthCertificateCount(BirthDeclaration.State.APPROVED, new Date(), new Date());
        int rejected = birthDeclarationDAO.getBirthCertificateCount(BirthDeclaration.State.ARCHIVED_REJECTED, new Date(), new Date());

        commonStat.setTotalSubmissions(data_entry + approved + rejected);
        commonStat.setApprovedItems(approved);
        commonStat.setRejectedItems(rejected);
        commonStat.setTotalPendingItems(data_entry);

        //todo call above methods using appropriate Date range

        commonStat.setArrearsPendingItems(0);
        commonStat.setLateSubmissions(0);
        commonStat.setNormalSubmissions(8);
        commonStat.setThisMonthPendingItems(3);
        
        return commonStat;
    }

    /**
     * @inheritDoc
     */
    public CommonStatistics getCommonDeathCertificateCount(String user) {
        CommonStatistics commonStat = new CommonStatistics();

        int data_entry = deathRegisterDAO.getDeathCertificateCount(DeathRegister.State.DATA_ENTRY, new Date(), new Date());
        int approved = deathRegisterDAO.getDeathCertificateCount(DeathRegister.State.APPROVED, new Date(), new Date());
        int rejected = deathRegisterDAO.getDeathCertificateCount(DeathRegister.State.REJECTED, new Date(), new Date());

        commonStat.setTotalSubmissions(data_entry + approved + rejected);
        commonStat.setApprovedItems(approved);
        commonStat.setRejectedItems(rejected);
        commonStat.setTotalPendingItems(data_entry);
        
        //todo call above methods using appropriate Date range

        commonStat.setArrearsPendingItems(0);
        commonStat.setLateSubmissions(0);
        commonStat.setNormalSubmissions(8);
        commonStat.setThisMonthPendingItems(3);

        return commonStat;
    }

    /**
     * @inheritDoc
     */
    public CommonStatistics getCommonMarriageCertificateCount(String user) {
        CommonStatistics commonStat = new CommonStatistics();

        return commonStat;
    }
}
