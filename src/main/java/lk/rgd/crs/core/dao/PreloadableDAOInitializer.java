package lk.rgd.crs.core.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * Pre-loads all PreloadableDAOs on Spring context initialization
 *
 * @author asankha
 */
public class PreloadableDAOInitializer implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(PreloadableDAOInitializer.class);

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {

        Map<String, PreloadableDAO> preloadableDaos = ctx.getBeansOfType(PreloadableDAO.class);
        for (PreloadableDAO dao : preloadableDaos.values()) {
            dao.preload();
        }
        logger.debug("Preloaded all PreloadableDAO instances");
    }
}
