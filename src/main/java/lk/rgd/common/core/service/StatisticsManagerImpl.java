package lk.rgd.common.core.service;

import lk.rgd.common.api.dao.StatisticsDAO;
import lk.rgd.common.api.dao.UserDAO;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        /* get a list of all users */
        List<User> userList = userDAO.getAllUsers();

        /* get one user at a time */
        for (User user : userList) {

            /* get all the Birth Statistics in last day */
            List<BirthDeclaration> bdfList = birthDeclarationDAO
                .getByCreatedUser(user, startTime, endTime);
            //todo: analyze bdfList

            /* get all the Death Statistics in last day */
            List<DeathRegister> deathList = deathRegisterDAO
                .getByCreatedUser(user, startTime, endTime);
            //todo: analyze deathList

            /* get all the Marriage Statistics in last day */
            List<MarriageRegister> mrList = marriageRegistrationDAO
                .getByCreatedUser(user, startTime, endTime);
            //todo: analyze mrList
        }
    }

}
