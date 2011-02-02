package lk.rgd.common;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.persistence.EntityManagerFactory;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;

import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.jmx.StatisticsService;

public class JMXAgent {

    private EntityManagerFactory entityManagerFactory;
    private MBeanServer mbs;

    public void init() throws Exception {

        SessionFactory sf = ((HibernateEntityManagerFactory) entityManagerFactory).getSessionFactory();

        ObjectName on = new ObjectName(
            "Hibernate:type=statistics,application=flrs-web");

        StatisticsService statsMBean = new StatisticsService();
        statsMBean.setSessionFactory(sf);
        statsMBean.setStatisticsEnabled(true);
        mbs.registerMBean(statsMBean, on);

        CacheManager cacheMgr = CacheManager.getInstance();
        ManagementService.registerMBeans(cacheMgr, mbs, true, true, true, true);
    }

    public MBeanServer getMBeanServer() {
        return mbs;
    }

    public void setEntityManagerFactory(
        EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void setMbs(MBeanServer mbs) {
        this.mbs = mbs;
    }

}