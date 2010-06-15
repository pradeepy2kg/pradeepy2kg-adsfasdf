package lk.rgd.prs.core.service;

import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This is a mock implementation for the PRS
 *
 * @author asankha
 */
public class PopulationRegistryMockImpl implements PopulationRegistry {

    private static final Logger logger = LoggerFactory.getLogger(PopulationRegistryMockImpl.class);

    private static final DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");

    public Person findPersonByPIN(int pin) {
        switch (pin) {
            case 0750010001: return piyasena;
        }
        return null;
    }

    public Person findPersonByNIC(String nic) {
        if ("750010001".equals(nic)) {
            return piyasena;
        }
        return null;
    }

    /** ----------------- hard coded mock values -----------------*/
    private static final Person piyasena = new Person();

    static {
        try {
            piyasena.setDateOfBirth(dfm.parse("1975-01-01"));
            piyasena.setCivilStatus(Person.CivilStatus.NEVER_MARRIED);

        } catch (Exception e) {
            logger.error("Error initializing mock sample data..", e);
        }
    }
}
