package lk.rgd.common.core.service;

import lk.rgd.common.api.dao.StatisticsDAO;
import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.Statistics;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.StatisticsManager;
import lk.rgd.common.util.DateTimeUtils;
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
 * @author Chathuranga Withana
 */
public class StatisticsManagerImpl implements StatisticsManager {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsManagerImpl.class);
    private final StatisticsDAO statisticsDAO;
    private final UserDAO userDAO;
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final DeathRegisterDAO deathRegisterDAO;
    private final MarriageRegistrationDAO marriageRegistrationDAO;

    private List<User> deoUserList;
    private List<User> adrUserList;
    private List<User> drUserList;
    private List<User> argUserList;
    private List<User> rgUserList;
    private static List<Statistics> statisticsList;

    public StatisticsManagerImpl(StatisticsDAO statisticsDAO, UserDAO userDAO, BirthDeclarationDAO birthDeclarationDAO,
        DeathRegisterDAO deathRegisterDAO, MarriageRegistrationDAO marriageRegistrationDAO) {
        this.statisticsDAO = statisticsDAO;
        this.userDAO = userDAO;
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.deathRegisterDAO = deathRegisterDAO;
        this.marriageRegistrationDAO = marriageRegistrationDAO;
        statisticsList = new ArrayList<Statistics>();
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateStatistics(String userId, Statistics statistics) {
        statisticsDAO.updateStatistics(statistics);
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateStatisticsList() {
        for (Statistics stat : StatisticsManagerImpl.statisticsList) {
            statisticsDAO.addStatistics(stat);
        }
    }

    public void triggerScheduledStatJobs() {
        runScheduledStatJobs();
        logger.debug("Start Statistics Recording...");
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER)
    public void runScheduledStatJobs() {
        logger.info("Start executing Statistics related scheduled tasks..");
        statisticsList = new ArrayList<Statistics>();
        populateAllUserLists();

        calculateDEOStatistics();
        calculateADRStatistics();
        calculateDRStatistics();
        calculateARGStatistics();
        calculateRGStatistics();

    }

    private void populateAllUserLists() {
        deoUserList = userDAO.getUsersByRole(Role.ROLE_DEO);
        adrUserList = userDAO.getUsersByRole(Role.ROLE_ADR);
        drUserList = userDAO.getUsersByRole(Role.ROLE_DR);
        argUserList = userDAO.getUsersByRole(Role.ROLE_ARG);
        rgUserList = userDAO.getUsersByRole(Role.ROLE_RG);
    }

    private void calculateDEOStatistics() {
        Statistics statistics;
        if (deoUserList != null) {
            /* get one DEO at a time */
            for (User deoUser : deoUserList) {

                /* statistics object for current DEO */
                statistics = populateStatistics(deoUser, null, null, null);

                /* this can't be null. but ... */
                if (statistics == null) {
                    statistics = new Statistics();
                }

                /* save record */
//                statisticsDAO.addStatistics(statistics);
                statistics.setUser(deoUser.getUserId());
                statisticsList.add(statistics);
            }
        }
    }

    private void calculateADRStatistics() {
        Statistics statistics;

        if (adrUserList != null) {
            /* get one ADR at a time */
            for (User adrUser : adrUserList) {

                /* statistics object current ADR */
                statistics = populateStatistics(adrUser, null, null, null);

                /* this can't be null. but ... */
                if (statistics == null) {
                    statistics = new Statistics();
                }

                /* get assigned dsDivision list for ADR */
                Set<DSDivision> dsDivisionList = adrUser.getAssignedBDDSDivisions();

                /* get one dsDivision at a time */
                for (DSDivision dsDivision : dsDivisionList) {
                    if (deoUserList != null)
                        /* get one DEO at a time */ {
                        for (User deoForAdr : deoUserList) {
                            if (deoForAdr.getAssignedBDDSDivisions().contains(dsDivision)) {
                                /* get statistics of DEO */
                                statistics = populateStatistics(deoForAdr, statistics, null, null);
                            }
                        }
                    }
                }

                /* save record */
//                statisticsDAO.addStatistics(statistics);
                statistics.setUser(adrUser.getUserId());
                statisticsList.add(statistics);
            }
        }
    }

    private void calculateDRStatistics() {
        Statistics statistics;

        if (drUserList != null)
            /* get a DR at a time */ {
            for (User drUser : drUserList) {

                /* statistics object current DR */
                statistics = populateStatistics(drUser/*, startTime, endTime*/, null, null, null);

                /* this can't be null. but ... */
                if (statistics == null) {
                    statistics = new Statistics();
                }

                /* get assigned dsDivision list for DR */
                Set<DSDivision> dsDivisionList = drUser.getAssignedBDDSDivisions();

                /* get one dsDivision at a time */
                for (DSDivision dsDivision : dsDivisionList) {

                    if (deoUserList != null)
                        /* get one DEO at a time */ {
                        for (User deoForDr : deoUserList) {

                            if (deoForDr.getAssignedBDDSDivisions().contains(dsDivision)) {

                                /* get statistics of DEO */
                                statistics = populateStatistics(deoForDr, statistics, null, null);
                            }
                        }
                    }
                }
//                statisticsDAO.addStatistics(statistics);
                statistics.setUser(drUser.getUserId());
                statisticsList.add(statistics);
            }
        }
    }

    private void calculateARGStatistics() {
        Statistics statistics;

        if (argUserList != null)
            /* get a ARG at a time */ {
            for (User argUser : argUserList) {

                /* statistics object current ARG */
                statistics = populateStatistics(argUser, null, null, null);

                /* this can't be null. but ... */
                if (statistics == null) {
                    statistics = new Statistics();
                }

                /* get assigned dsDivision list for ARG */
                Set<DSDivision> dsDivisionList = argUser.getAssignedBDDSDivisions();

                /* get one dsDivision at a time */
                for (DSDivision dsDivision : dsDivisionList) {

                    if (deoUserList != null)
                        /* get one DEO at a time */ {
                        for (User deoForDr : deoUserList) {
                            if (deoForDr.getAssignedBDDSDivisions().contains(dsDivision)) {
                                /* get statistics of DEO */
                                Statistics statisticsOfAdr = statisticsDAO.getByUser(deoForDr.getUserId());
                                if (statisticsOfAdr == null) {
                                    statisticsOfAdr = new Statistics();
                                }

                                /* add DEO Birth statistics to ARG statistics */
                                statistics.setBirthsApprovedItems(statistics.getBirthsApprovedItems() +
                                    statisticsOfAdr.getBirthsApprovedItems());
                                statistics.setBirthsRejectedItems(statistics.getBirthsRejectedItems() +
                                    statisticsOfAdr.getBirthsRejectedItems());
                                statistics.setBirthsTotalPendingItems(statistics.getBirthsTotalPendingItems() +
                                    statisticsOfAdr.getBirthsTotalPendingItems());

                                /* add DEO Death statistics to ARG statistics */
                                statistics.setDeathsApprovedItems(statistics.getDeathsApprovedItems() +
                                    statisticsOfAdr.getBirthsApprovedItems());
                                statistics.setDeathsRejectedItems(statistics.getDeathsRejectedItems() +
                                    statisticsOfAdr.getDeathsRejectedItems());
                                statistics.setDeathsTotalPendingItems(statistics.getDeathsTotalPendingItems() +
                                    statisticsOfAdr.getDeathsTotalPendingItems());

                                /* add DEO Marriage statistics to ARG statistics */
                                statistics.setMrgApprovedItems(statistics.getMrgApprovedItems() +
                                    statisticsOfAdr.getMrgApprovedItems());
                                statistics.setMrgRejectedItems(statistics.getMrgRejectedItems() +
                                    statisticsOfAdr.getMrgRejectedItems());
                                statistics.setMrgTotalPendingItems(statistics.getMrgTotalPendingItems() +
                                    statisticsOfAdr.getMrgTotalPendingItems());
                            }
                            if (adrUserList != null) {
                                for (User adrUserForRg : adrUserList) {
                                    if (adrUserForRg.getAssignedBDDSDivisions().contains(dsDivision)) {
                                        Statistics statisticsOfAdr = populateStatistics(adrUserForRg/*, startTime, endTime*/, null, null, null);

                                        /* add DEO Birth statistics to ARG statistics */
                                        statistics.setBirthsApprovedItems(statistics.getBirthsApprovedItems() +
                                            statisticsOfAdr.getBirthsApprovedItems());
                                        statistics.setBirthsRejectedItems(statistics.getBirthsRejectedItems() +
                                            statisticsOfAdr.getBirthsRejectedItems());
                                        statistics.setBirthsTotalPendingItems(statistics.getBirthsTotalPendingItems() +
                                            statisticsOfAdr.getBirthsTotalPendingItems());

                                        /* add DEO Death statistics to ARG statistics */
                                        statistics.setDeathsApprovedItems(statistics.getDeathsApprovedItems() +
                                            statisticsOfAdr.getBirthsApprovedItems());
                                        statistics.setDeathsRejectedItems(statistics.getDeathsRejectedItems() +
                                            statisticsOfAdr.getDeathsRejectedItems());
                                        statistics.setDeathsTotalPendingItems(statistics.getDeathsTotalPendingItems() +
                                            statisticsOfAdr.getDeathsTotalPendingItems());

                                        /* add DEO Marriage statistics to ARG statistics */
                                        statistics.setMrgApprovedItems(statistics.getMrgApprovedItems() +
                                            statisticsOfAdr.getMrgApprovedItems());
                                        statistics.setMrgRejectedItems(statistics.getMrgRejectedItems() +
                                            statisticsOfAdr.getMrgRejectedItems());
                                        statistics.setMrgTotalPendingItems(statistics.getMrgTotalPendingItems() +
                                            statisticsOfAdr.getMrgTotalPendingItems());
                                    }
                                }
                            }
                            if (drUserList != null) {
                                for (User drUserForRg : drUserList) {
                                    if (drUserForRg.getAssignedBDDSDivisions().contains(dsDivision)) {
                                        Statistics statisticsOfDr = populateStatistics(drUserForRg/*, startTime, endTime*/, null, null, null);

                                        /* add DEO Birth statistics to ARG statistics */
                                        statistics.setBirthsApprovedItems(statistics.getBirthsApprovedItems() +
                                            statisticsOfDr.getBirthsApprovedItems());
                                        statistics.setBirthsRejectedItems(statistics.getBirthsRejectedItems() +
                                            statisticsOfDr.getBirthsRejectedItems());
                                        statistics.setBirthsTotalPendingItems(statistics.getBirthsTotalPendingItems() +
                                            statisticsOfDr.getBirthsTotalPendingItems());

                                        /* add DEO Death statistics to ARG statistics */
                                        statistics.setDeathsApprovedItems(statistics.getDeathsApprovedItems() +
                                            statisticsOfDr.getBirthsApprovedItems());
                                        statistics.setDeathsRejectedItems(statistics.getDeathsRejectedItems() +
                                            statisticsOfDr.getDeathsRejectedItems());
                                        statistics.setDeathsTotalPendingItems(statistics.getDeathsTotalPendingItems() +
                                            statisticsOfDr.getDeathsTotalPendingItems());

                                        /* add DEO Marriage statistics to ARG statistics */
                                        statistics.setMrgApprovedItems(statistics.getMrgApprovedItems() +
                                            statisticsOfDr.getMrgApprovedItems());
                                        statistics.setMrgRejectedItems(statistics.getMrgRejectedItems() +
                                            statisticsOfDr.getMrgRejectedItems());
                                        statistics.setMrgTotalPendingItems(statistics.getMrgTotalPendingItems() +
                                            statisticsOfDr.getMrgTotalPendingItems());
                                    }
                                }
                            }
                        }
                    }
                }

//                statisticsDAO.addStatistics(statistics);
                statistics.setUser(argUser.getUserId());
                statisticsList.add(statistics);
            }
        }
    }

    private void calculateRGStatistics() {
        Statistics statistics;

        if (rgUserList != null)
            /* get a RG at a time */ {
            for (User rgUser : rgUserList) {

                /* statistics object current RG */
                statistics = populateStatistics(rgUser/*, startTime, endTime*/, null, null, null);

                /* this can't be null. but ... */
                if (statistics == null) {
                    statistics = new Statistics();
                }

                Set<DSDivision> dsDivisionList = rgUser.getAssignedBDDSDivisions();

                /* get one dsDivision at a time */
                for (DSDivision dsDivision : dsDivisionList) {

                    if (deoUserList != null) {
                        /* get one DEO at a time */
                        for (User deoForDr : deoUserList) {
                            if (deoForDr.getAssignedBDDSDivisions().contains(dsDivision)) {
                                /* get statistics of DEO */
                                Statistics statisticsOfDeo = statisticsDAO.getByUser(deoForDr.getUserId());
                                if (statisticsOfDeo == null) {
                                    statisticsOfDeo = new Statistics();
                                }

                                /* add DEO Birth statistics to ARG statistics */
                                statistics.setBirthsApprovedItems(statistics.getBirthsApprovedItems() +
                                    statisticsOfDeo.getBirthsApprovedItems());
                                statistics.setBirthsRejectedItems(statistics.getBirthsRejectedItems() +
                                    statisticsOfDeo.getBirthsRejectedItems());
                                statistics.setBirthsTotalPendingItems(statistics.getBirthsTotalPendingItems() +
                                    statisticsOfDeo.getBirthsTotalPendingItems());

                                /* add DEO Death statistics to ARG statistics */
                                statistics.setDeathsApprovedItems(statistics.getDeathsApprovedItems() +
                                    statisticsOfDeo.getBirthsApprovedItems());
                                statistics.setDeathsRejectedItems(statistics.getDeathsRejectedItems() +
                                    statisticsOfDeo.getDeathsRejectedItems());
                                statistics.setDeathsTotalPendingItems(statistics.getDeathsTotalPendingItems() +
                                    statisticsOfDeo.getDeathsTotalPendingItems());

                                /* add DEO Marriage statistics to ARG statistics */
                                statistics.setMrgApprovedItems(statistics.getMrgApprovedItems() +
                                    statisticsOfDeo.getMrgApprovedItems());
                                statistics.setMrgRejectedItems(statistics.getMrgRejectedItems() +
                                    statisticsOfDeo.getMrgRejectedItems());
                                statistics.setMrgTotalPendingItems(statistics.getMrgTotalPendingItems() +
                                    statisticsOfDeo.getMrgTotalPendingItems());
                            }
                        }
                    }
                    if (adrUserList != null) {
                        for (User adrUserForRg : adrUserList) {
                            if (adrUserForRg.getAssignedBDDSDivisions().contains(dsDivision)) {
                                Statistics statisticsOfAdr = populateStatistics(adrUserForRg/*, startTime, endTime*/, null, null, null);

                                /* add DEO Birth statistics to ARG statistics */
                                statistics.setBirthsApprovedItems(statistics.getBirthsApprovedItems() +
                                    statisticsOfAdr.getBirthsApprovedItems());
                                statistics.setBirthsRejectedItems(statistics.getBirthsRejectedItems() +
                                    statisticsOfAdr.getBirthsRejectedItems());
                                statistics.setBirthsTotalPendingItems(statistics.getBirthsTotalPendingItems() +
                                    statisticsOfAdr.getBirthsTotalPendingItems());

                                /* add DEO Death statistics to ARG statistics */
                                statistics.setDeathsApprovedItems(statistics.getDeathsApprovedItems() +
                                    statisticsOfAdr.getBirthsApprovedItems());
                                statistics.setDeathsRejectedItems(statistics.getDeathsRejectedItems() +
                                    statisticsOfAdr.getDeathsRejectedItems());
                                statistics.setDeathsTotalPendingItems(statistics.getDeathsTotalPendingItems() +
                                    statisticsOfAdr.getDeathsTotalPendingItems());

                                /* add DEO Marriage statistics to ARG statistics */
                                statistics.setMrgApprovedItems(statistics.getMrgApprovedItems() +
                                    statisticsOfAdr.getMrgApprovedItems());
                                statistics.setMrgRejectedItems(statistics.getMrgRejectedItems() +
                                    statisticsOfAdr.getMrgRejectedItems());
                                statistics.setMrgTotalPendingItems(statistics.getMrgTotalPendingItems() +
                                    statisticsOfAdr.getMrgTotalPendingItems());
                            }
                        }
                    }
                    if (drUserList != null) {
                        for (User drUserForRg : drUserList) {
                            if (drUserForRg.getAssignedBDDSDivisions().contains(dsDivision)) {
                                Statistics statisticsOfDr = populateStatistics(drUserForRg/*, startTime, endTime*/, null, null, null);

                                /* add DEO Birth statistics to ARG statistics */
                                statistics.setBirthsApprovedItems(statistics.getBirthsApprovedItems() +
                                    statisticsOfDr.getBirthsApprovedItems());
                                statistics.setBirthsRejectedItems(statistics.getBirthsRejectedItems() +
                                    statisticsOfDr.getBirthsRejectedItems());
                                statistics.setBirthsTotalPendingItems(statistics.getBirthsTotalPendingItems() +
                                    statisticsOfDr.getBirthsTotalPendingItems());

                                /* add DEO Death statistics to ARG statistics */
                                statistics.setDeathsApprovedItems(statistics.getDeathsApprovedItems() +
                                    statisticsOfDr.getBirthsApprovedItems());
                                statistics.setDeathsRejectedItems(statistics.getDeathsRejectedItems() +
                                    statisticsOfDr.getDeathsRejectedItems());
                                statistics.setDeathsTotalPendingItems(statistics.getDeathsTotalPendingItems() +
                                    statisticsOfDr.getDeathsTotalPendingItems());

                                /* add DEO Marriage statistics to ARG statistics */
                                statistics.setMrgApprovedItems(statistics.getMrgApprovedItems() +
                                    statisticsOfDr.getMrgApprovedItems());
                                statistics.setMrgRejectedItems(statistics.getMrgRejectedItems() +
                                    statisticsOfDr.getMrgRejectedItems());
                                statistics.setMrgTotalPendingItems(statistics.getMrgTotalPendingItems() +
                                    statisticsOfDr.getMrgTotalPendingItems());
                            }
                        }
                    }
                    if (argUserList != null) {
                        for (User argUserForRg : argUserList) {
                            if (argUserForRg.getAssignedBDDSDivisions().contains(dsDivision)) {
                                Statistics statisticsOfArg = populateStatistics(argUserForRg, null, null, null);

                                /* add DEO Birth statistics to ARG statistics */
                                statistics.setBirthsApprovedItems(statistics.getBirthsApprovedItems() +
                                    statisticsOfArg.getBirthsApprovedItems());
                                statistics.setBirthsRejectedItems(statistics.getBirthsRejectedItems() +
                                    statisticsOfArg.getBirthsRejectedItems());
                                statistics.setBirthsTotalPendingItems(statistics.getBirthsTotalPendingItems() +
                                    statisticsOfArg.getBirthsTotalPendingItems());

                                /* add DEO Death statistics to ARG statistics */
                                statistics.setDeathsApprovedItems(statistics.getDeathsApprovedItems() +
                                    statisticsOfArg.getBirthsApprovedItems());
                                statistics.setDeathsRejectedItems(statistics.getDeathsRejectedItems() +
                                    statisticsOfArg.getDeathsRejectedItems());
                                statistics.setDeathsTotalPendingItems(statistics.getDeathsTotalPendingItems() +
                                    statisticsOfArg.getDeathsTotalPendingItems());

                                /* add DEO Marriage statistics to ARG statistics */
                                statistics.setMrgApprovedItems(statistics.getMrgApprovedItems() +
                                    statisticsOfArg.getMrgApprovedItems());
                                statistics.setMrgRejectedItems(statistics.getMrgRejectedItems() +
                                    statisticsOfArg.getMrgRejectedItems());
                                statistics.setMrgTotalPendingItems(statistics.getMrgTotalPendingItems() +
                                    statisticsOfArg.getMrgTotalPendingItems());
                            }
                        }
                    }
                }
//                statisticsDAO.addStatistics(statistics);
                statistics.setUser(rgUser.getUserId());
                statisticsList.add(statistics);
            }
        }
    }

    /**
     * This is for triggerScheduledStatJobs()
     *
     * @param user
     * @param startDate
     * @param endDate   @return
     */
    private Statistics populateStatistics(User user, Statistics stat, Date startDate, Date endDate) {

        // when date range not specified show statistics of the current year until today
        if (startDate == null || endDate == null) {
            // set first day of current year
            Calendar cal1 = Calendar.getInstance();
            int currentYear = cal1.get(Calendar.YEAR);
            cal1.clear();
            cal1.set(currentYear, Calendar.JANUARY, 1, 0, 0, 0);
            startDate = cal1.getTime();

            // set end date to today midnight
            Calendar cal2 = Calendar.getInstance();
            cal2.set(Calendar.HOUR_OF_DAY, 23);
            cal2.set(Calendar.MINUTE, 59);
            cal2.set(Calendar.SECOND, 59);
            endDate = cal2.getTime();
        } else {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(startDate);
            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            startDate = cal1.getTime();


            // set end date to today midnight
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(endDate);
            cal2.set(Calendar.HOUR_OF_DAY, 23);
            cal2.set(Calendar.MINUTE, 59);
            cal2.set(Calendar.SECOND, 59);
            endDate = cal2.getTime();
        }
        Calendar cal3 = Calendar.getInstance();
        cal3.set(Calendar.HOUR_OF_DAY, 0);
        cal3.set(Calendar.MINUTE, 0);
        cal3.set(Calendar.SECOND, 0);
        cal3.set(Calendar.DAY_OF_MONTH, 1);
        // TODO http://obscuredclarity.blogspot.com/2010/08/get-previous-business-day-date-object.html
        Date thisMonthStart = cal3.getTime();

        if (logger.isDebugEnabled()) {
            logger.debug("Load statistics for userId : {} from {} to {} period",
                new Object[]{user.getUserId(), DateTimeUtils.getISO8601FormattedString(startDate),
                    DateTimeUtils.getISO8601FormattedString(endDate)});
        }

        /* statistics object for current User */
        Statistics statistics = (stat == null) ? new Statistics() : stat;
        //statistics.setUser(user);

        /* get all the Birth Statistics in last day */
        List<BirthDeclaration> bdfList = birthDeclarationDAO.getByCreatedUser(user, startDate, endDate);
        statistics.setBirthsTotalSubmissions(bdfList.size());
        for (BirthDeclaration birthDeclaration : bdfList) {

            BirthDeclaration.State status = birthDeclaration.getRegister().getStatus();
            switch (status) {
                case APPROVED:
                    statistics.setBirthsApprovedItems(statistics.getBirthsApprovedItems() + 1);
                    break;
                case ARCHIVED_REJECTED:
                    statistics.setBirthsRejectedItems(statistics.getBirthsRejectedItems() + 1);
                    break;
                case DATA_ENTRY:
                    if (birthDeclaration.getLifeCycleInfo().getCreatedTimestamp().before(thisMonthStart)) {
                        statistics.setBirthsArrearsPendingItems(statistics.getBirthsArrearsPendingItems() + 1);
                    } else {
                        statistics.setBirthsThisMonthPendingItems(statistics.getBirthsThisMonthPendingItems() + 1);
                    }
            }

            BirthDeclaration.BirthType birthType = birthDeclaration.getRegister().getBirthType();
            switch (birthType) {
                case BELATED:
                    statistics.setBirthsLateSubmissions(statistics.getBirthsThisMonthPendingItems() + 1);
                    break;
                case ADOPTION:
                case LIVE:
                    Date dob = birthDeclaration.getChild().getDateOfBirth();
                    Calendar lateDate = Calendar.getInstance();
                    lateDate.setTime(dob);
                    lateDate.add(Calendar.MONTH, -3);
                    if (birthDeclaration.getChild().getDateOfBirth().after(lateDate.getTime())) {
                        statistics.setBirthsNormalSubmissions(statistics.getBirthsNormalSubmissions() + 1);
                    } else {
                        statistics.setBirthsLateSubmissions(statistics.getBirthsLateSubmissions() + 1);
                    }
                    break;
                case STILL:
                    statistics.setBirthsStillSubmissions(statistics.getBirthsStillSubmissions() + 1);
                    break;

            }
        }

        /* get all the Death Statistics in last day */
        List<DeathRegister> deathList = deathRegisterDAO.getByCreatedUser(user, startDate, endDate);
        statistics.setDeathsTotalSubmissions(deathList.size());
        for (DeathRegister deathRegister : deathList) {

            DeathRegister.State status = deathRegister.getStatus();
            switch (status) {
                case APPROVED:
                    statistics.setDeathsApprovedItems(statistics.getDeathsApprovedItems() + 1);
                    break;
                case REJECTED:
                    statistics.setDeathsRejectedItems(statistics.getDeathsRejectedItems() + 1);
                    break;
                case DATA_ENTRY:
                    if (deathRegister.getLifeCycleInfo().getCreatedTimestamp().before(thisMonthStart)) {
                        statistics.setDeathsArrearsPendingItems(statistics.getDeathsArrearsPendingItems() + 1);
                    } else {
                        statistics.setDeathsThisMonthPendingItems(statistics.getDeathsThisMonthPendingItems() + 1);
                    }
            }

            DeathRegister.Type deathType = deathRegister.getDeathType();
            switch (deathType) {
                case NORMAL:
                    statistics.setDeathsNormalSubmissions(statistics.getDeathsNormalSubmissions() + 1);
                    break;
                case LATE:
                    statistics.setDeathsLateSubmissions(statistics.getDeathsLateSubmissions() + 1);
                    break;
                case SUDDEN:
                case MISSING:
            }
        }

        /* get all the Marriage Statistics in last day */
        List<MarriageRegister> mrList = marriageRegistrationDAO.getByCreatedUser(user, startDate, endDate);
        statistics.setMrgTotalSubmissions(mrList.size());
        for (MarriageRegister marriageRegister : mrList) {
            MarriageRegister.State status = marriageRegister.getState();
            switch (status) {
                case REGISTRATION_APPROVED:
                    statistics.setMrgApprovedItems(statistics.getMrgApprovedItems() + 1);
                    break;
                case REGISTRATION_REJECTED:
                    statistics.setMrgRejectedItems(statistics.getMrgRejectedItems() + 1);
                    break;
                case DATA_ENTRY:
                    if (marriageRegister.getLifeCycleInfo().getCreatedTimestamp().before(thisMonthStart)) {
                        statistics.setMrgArrearsPendingItems(statistics.getMrgArrearsPendingItems() + 1);
                    } else {
                        statistics.setMrgThisMonthPendingItems(statistics.getMrgThisMonthPendingItems() + 1);
                    }
            }
        }
        statistics.setUser(user.getUserId());

        return statistics;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Statistics getStatisticsForUser(User user, Date startDate, Date endDate) {

        Statistics statistics = statisticsDAO.getByUser(user.getUserId());
        final String userRole = user.getRole().getRoleId();

        if (statistics != null) {
            deleteEntries(statistics);
        }

        if (Role.ROLE_DEO.equals(userRole)) {
            statistics = populateStatistics(user, statistics, startDate, endDate);
        }
        if (Role.ROLE_ADR.equals(userRole) || Role.ROLE_DR.equals(userRole)) {
            statistics = populateStatistics(user, statistics, startDate, endDate);
            Set<DSDivision> dsDivisionList = user.getAssignedBDDSDivisions();
            List<User> deoList = userDAO.getUsersByRole(Role.ROLE_DEO);
            for (DSDivision dsDivision : dsDivisionList) {
                for (User deo : deoList) {
                    if (deo.getAssignedBDDSDivisions().contains(dsDivision)) {
                        statistics = populateStatistics(deo, statistics, startDate, endDate);
                    }
                }
            }
        }
        if (Role.ROLE_ARG.equals(userRole)) {
            statistics = populateStatistics(user, statistics, startDate, endDate);
            List<User> allUserList = userDAO.getAllUsers();
            for (User oneUser : allUserList) {
                final String oneUserRole = oneUser.getRole().getRoleId();
                if (Role.ROLE_DEO.equals(oneUserRole) || Role.ROLE_ADR.equals(oneUserRole) || Role.ROLE_DR.equals(oneUserRole)) {
                    statistics = populateStatistics(oneUser, statistics, startDate, endDate);
                }
            }
        }
        if (Role.ROLE_RG.equals(userRole)) {
            statistics = populateStatistics(user, statistics, startDate, endDate);
            List<User> allUserList = userDAO.getAllUsers();
            for (User oneUser : allUserList) {
                if (!oneUser.getUserId().equals(user.getUserId()) && !oneUser.getRole().getRoleId().equals(Role.ROLE_RG)) {
                    logger.debug("RG gets the user : {}", oneUser.getUserId());
                    statistics = populateStatistics(oneUser, statistics, startDate, endDate);
                }
            }
        }

        return statistics;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addStatistics(User user, Statistics statistics) {
        statisticsDAO.addStatistics(statistics);
    }

    @Override
    public boolean existsStatisticsForUser(User user) {
        return statisticsDAO.getByUser(user.getUserId()) != null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteOldStatistics() {
        statisticsDAO.deleteAll();
    }

    private void deleteEntries(final Statistics st) {
        st.setBirthsApprovedItems(0);
        st.setBirthsArrearsPendingItems(0);
        st.setBirthsLateSubmissions(0);
        st.setBirthsNormalSubmissions(0);
        st.setBirthsRejectedItems(0);
        st.setBirthsThisMonthPendingItems(0);
        st.setBirthsTotalPendingItems(0);
        st.setBirthsTotalSubmissions(0);

        st.setDeathsApprovedItems(0);
        st.setDeathsArrearsPendingItems(0);
        st.setDeathsLateSubmissions(0);
        st.setDeathsNormalSubmissions(0);
        st.setDeathsRejectedItems(0);
        st.setDeathsThisMonthPendingItems(0);
        st.setDeathsTotalPendingItems(0);
        st.setDeathsTotalSubmissions(0);

        st.setMrgApprovedItems(0);
        st.setMrgArrearsPendingItems(0);
        st.setMrgLateSubmissions(0);
        st.setMrgNormalSubmissions(0);
        st.setMrgRejectedItems(0);
        st.setMrgThisMonthPendingItems(0);
        st.setMrgTotalPendingItems(0);
        st.setMrgTotalSubmissions(0);
    }

}
