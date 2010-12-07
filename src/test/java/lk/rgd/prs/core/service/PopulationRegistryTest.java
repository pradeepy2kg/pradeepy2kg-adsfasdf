package lk.rgd.prs.core.service;

import junit.framework.TestCase;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.springframework.context.ApplicationContext;

/**
 * @author Chathuranga Withana
 */
public class PopulationRegistryTest extends TestCase {

    private final ApplicationContext ctx = UnitTestManager.ctx;
    private final PopulationRegistry eCivil;
    private final UserManager userManager;
    private User deoColomboColombo;
    private User deoGampahaNegambo;
    private User adrColomboColombo;
    private User adrGampahaNegambo;
    private User argWestern;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public PopulationRegistryTest() {

        eCivil = (PopulationRegistry) ctx.getBean("ecivilService", PopulationRegistry.class);
        userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);

        try {
            deoColomboColombo = userManager.authenticateUser("deo-colombo-colombo", "password");
            adrColomboColombo = userManager.authenticateUser("adr-colombo-colombo", "password");
            deoGampahaNegambo = userManager.authenticateUser("deo-gampaha-negambo", "password");
            adrGampahaNegambo = userManager.authenticateUser("adr-gampaha-negambo", "password");
            argWestern = userManager.authenticateUser("arg-western", "password");
        } catch (AuthorizationException e) {
            throw new IllegalArgumentException("Cannot authenticate sample users");
        }
    }

    public void testAddExistingPerson() throws Exception {
        // TODO
    }

    private Person getMinimalPerson() {
        // TODO
        Person person = new Person();
        return person;
    }
}
