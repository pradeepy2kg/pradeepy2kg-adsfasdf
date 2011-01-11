package lk.rgd.common.core.service;

import lk.rgd.common.api.dao.StatisticsDAO;
import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.Statistics;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.StatisticsManager;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.domain.MarriageRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author shan
 */
public class StatisticsManagerImpl implements StatisticsManager {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsManagerImpl.class);
    private final StatisticsDAO statisticsDAO;
    private final UserDAO userDAO;
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final DeathRegisterDAO deathRegisterDAO;
    private final MarriageRegistrationDAO marriageRegistrationDAO;

    public StatisticsManagerImpl(StatisticsDAO statisticsDAO, UserDAO userDAO, BirthDeclarationDAO birthDeclarationDAO,
        DeathRegisterDAO deathRegisterDAO, MarriageRegistrationDAO marriageRegistrationDAO) {
        this.statisticsDAO = statisticsDAO;
        this.userDAO = userDAO;
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.deathRegisterDAO = deathRegisterDAO;
        this.marriageRegistrationDAO = marriageRegistrationDAO;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void updateStatistics(String userId, Statistics statistics) {
        // todo
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional(propagation = Propagation.NEVER)
    public void triggerScheduledStatJobs() {
        logger.info("Start executing Statistics related scheduled tasks..");

        /* start of the day */
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date startTime = cal.getTime();

        /* now */
        Date endTime = new Date();

        /* get a list of all DEOs */
        List<User> deoUserList = userDAO.getUsersByRole(Role.ROLE_DEO);

        /* get one DEO at a time */
        for (User deoUser : deoUserList) {

            /* statistics object for current DEO */
            Statistics statistics = populateStatistics(deoUser, startTime, endTime);

            /* save record */
            statisticsDAO.addStatistics(statistics);
        }

        /* get a list of all ADRs */
        List<User> adrUserList = userDAO.getUsersByRole(Role.ROLE_ADR);

        logger.debug("adr size = {}", adrUserList.size());

        adrUserList.addAll(userDAO.getUsersByRole(Role.ROLE_DR));

        logger.debug("full size = {}", adrUserList.size());

        /* get one ADR at a time */
        for (User adrUser : adrUserList) {

            /* statistics object current ADR */
            Statistics statistics = populateStatistics(adrUser, startTime, endTime);

            /* get assigned dsDivision list for ADR */
            Set<DSDivision> dsDivisionList = adrUser.getAssignedBDDSDivisions();
            Iterator<DSDivision> i = dsDivisionList.iterator();

            /* get one dsDivision at a time */
            while (i.hasNext()) {
                DSDivision dsDivision = i.next();

                /* get one DEO at a time */
                for (User deoForAdr : deoUserList) {
                    if (deoForAdr.getAssignedBDDSDivisions().contains(dsDivision)) {

                        /* get statistics of DEO */
                        Statistics statisticsOfDeo = statisticsDAO.getByUser(deoForAdr.getUserId());

                        if (statisticsOfDeo != null) {
                            /* add DEO Birth statistics to ADR statistics */
                            {
                                statistics.setBirthsApprovedItems(statistics.getBirthsApprovedItems() +
                                    statisticsOfDeo.getBirthsApprovedItems());
                            }
                            statistics.setBirthsRejectedItems(statistics.getBirthsRejectedItems() +
                                statisticsOfDeo.getBirthsRejectedItems());
                            statistics.setBirthsTotalPendingItems(statistics.getBirthsTotalPendingItems() +
                                statisticsOfDeo.getBirthsTotalPendingItems());

                            /* add DEO Death statistics to ADR statistics */
                            statistics.setDeathsApprovedItems(statistics.getDeathsApprovedItems() +
                                statisticsOfDeo.getBirthsApprovedItems());
                            statistics.setDeathsRejectedItems(statistics.getDeathsRejectedItems() +
                                statisticsOfDeo.getDeathsRejectedItems());
                            statistics.setDeathsTotalPendingItems(statistics.getDeathsTotalPendingItems() +
                                statisticsOfDeo.getDeathsTotalPendingItems());

                            /* add DEO Marriage statistics to ADR statistics */
                            statistics.setMrgApprovedItems(statistics.getMrgApprovedItems() +
                                statisticsOfDeo.getMrgApprovedItems());
                            statistics.setMrgRejectedItems(statistics.getMrgRejectedItems() +
                                statisticsOfDeo.getMrgRejectedItems());
                            statistics.setMrgTotalPendingItems(statistics.getMrgTotalPendingItems() +
                                statisticsOfDeo.getMrgTotalPendingItems());
                        }
                    }
                }
            }

            /* save record */
            statisticsDAO.addStatistics(statistics);
        }

        /* get a list of all DRs */
        List<User> drUserList = userDAO.getUsersByRole(Role.ROLE_DR);    // todo

        /* get a DR at a time */
        for (User drUser : drUserList) {

            /* statistics object current DR */
            Statistics statistics = populateStatistics(drUser, startTime, endTime);

            /* get assigned dsDivision list for DR */
            Set<DSDivision> dsDivisionList = drUser.getAssignedBDDSDivisions();
            Iterator<DSDivision> i = dsDivisionList.iterator();

            /* get one dsDivision at a time */
            while (i.hasNext()) {
                DSDivision dsDivision = i.next();

                /* get one ADR at a time */
                for (User adrForDr : adrUserList) {     // todo: may be this is not the best
                    if (adrForDr.getAssignedBDDSDivisions().contains(dsDivision)) {

                        /* get statistics of ADR */
                        Statistics statisticsOfAdr = statisticsDAO.getByUser(adrForDr.getUserId());

                        /* add ADR Birth statistics to DR statistics */
                        statistics.setBirthsApprovedItems(statistics.getBirthsApprovedItems() +
                            statisticsOfAdr.getBirthsApprovedItems());
                        statistics.setBirthsRejectedItems(statistics.getBirthsRejectedItems() +
                            statisticsOfAdr.getBirthsRejectedItems());
                        statistics.setBirthsTotalPendingItems(statistics.getBirthsTotalPendingItems() +
                            statisticsOfAdr.getBirthsTotalPendingItems());

                        /* add ADR Death statistics to DR statistics */
                        statistics.setDeathsApprovedItems(statistics.getDeathsApprovedItems() +
                            statisticsOfAdr.getBirthsApprovedItems());
                        statistics.setDeathsRejectedItems(statistics.getDeathsRejectedItems() +
                            statisticsOfAdr.getDeathsRejectedItems());
                        statistics.setDeathsTotalPendingItems(statistics.getDeathsTotalPendingItems() +
                            statisticsOfAdr.getDeathsTotalPendingItems());

                        /* add ADR Marriage statistics to DR statistics */
                        statistics.setMrgApprovedItems(statistics.getMrgApprovedItems() +
                            statisticsOfAdr.getMrgApprovedItems());
                        statistics.setMrgRejectedItems(statistics.getMrgRejectedItems() +
                            statisticsOfAdr.getMrgRejectedItems());
                        statistics.setMrgTotalPendingItems(statistics.getMrgTotalPendingItems() +
                            statisticsOfAdr.getMrgTotalPendingItems());
                    }
                }

            }

        }
        List<User> argUserList = userDAO.getUsersByRole(Role.ROLE_ARG);   // todo

        List<User> rgUserList = userDAO.getUsersByRole(Role.ROLE_RG);    // todo

    }

    private Statistics populateStatistics(User user, Date startTime, Date endTime) {

        /* statistics object for current User */
        Statistics statistics = new Statistics();
        statistics.setUser(user);

        /* get all the Birth Statistics in last day */
        List<BirthDeclaration> bdfList = birthDeclarationDAO
            .getByCreatedUser(user, startTime, endTime);
        for (BirthDeclaration birthDeclaration : bdfList) {
            if (birthDeclaration.getRegister().getStatus() == BirthDeclaration.State.APPROVED) {

                /* increment approved item count by 1 */
                statistics.setBirthsApprovedItems(statistics.getBirthsApprovedItems() + 1);
            } else if (birthDeclaration.getRegister().getStatus() == BirthDeclaration.State.ARCHIVED_REJECTED) {

                /* increment rejected item count by 1 */
                statistics.setBirthsRejectedItems(statistics.getBirthsRejectedItems() + 1);
            } else if (birthDeclaration.getRegister().getStatus() == BirthDeclaration.State.DATA_ENTRY) {

                /* increment pending item count by 1 */
                statistics.setBirthsTotalPendingItems(statistics.getBirthsTotalPendingItems() + 1);
            }
        }

        /* get all the Death Statistics in last day */
        List<DeathRegister> deathList = deathRegisterDAO
            .getByCreatedUser(user, startTime, endTime);
        for (DeathRegister deathRegister : deathList) {
            if (deathRegister.getStatus() == DeathRegister.State.APPROVED) {

                /* increment approved item count by 1 */
                statistics.setDeathsApprovedItems(statistics.getDeathsApprovedItems() + 1);
            } else if (deathRegister.getStatus() == DeathRegister.State.REJECTED) {

                /* increment rejected item count by 1 */
                statistics.setDeathsRejectedItems(statistics.getDeathsRejectedItems() + 1);
            } else if (deathRegister.getStatus() == DeathRegister.State.DATA_ENTRY) {

                /* increment pending item count by 1 */
                statistics.setDeathsTotalPendingItems(statistics.getDeathsTotalPendingItems() + 1);
            }
        }

        /* get all the Marriage Statistics in last day */
        List<MarriageRegister> mrList = marriageRegistrationDAO
            .getByCreatedUser(user, startTime, endTime);
        for (MarriageRegister marriageRegister : mrList) {
            if (marriageRegister.getState() == MarriageRegister.State.REGISTRATION_APPROVED) {

                /* increment approved item count by 1 */
                statistics.setMrgApprovedItems(statistics.getMrgApprovedItems() + 1);
            } else if (marriageRegister.getState() == MarriageRegister.State.REGISTRATION_REJECTED) {

                /* increment rejected item count by 1 */
                statistics.setMrgRejectedItems(statistics.getMrgRejectedItems() + 1);
            } else if (marriageRegister.getState() == MarriageRegister.State.DATA_ENTRY) {

                /* increment pending item count by 1 */
                statistics.setMrgTotalPendingItems(statistics.getMrgTotalPendingItems() + 1);
            }
        }

        return statistics;
    }

}
