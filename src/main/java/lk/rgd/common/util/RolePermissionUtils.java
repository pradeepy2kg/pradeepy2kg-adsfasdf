package lk.rgd.common.util;

import lk.rgd.Permission;
import lk.rgd.common.api.dao.RoleDAO;
import lk.rgd.common.api.domain.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.BitSet;

/**
 * util class for permission settings and related methods
 *
 * @authar amith jayasekara
 */
public class RolePermissionUtils {
    private static final Logger logger = LoggerFactory.getLogger(RolePermissionUtils.class);

    private static final BitSet deoBitSet;
    private static final BitSet adrBitSet;

    private static final BitSet drBitSet;
    private static final BitSet argBitSet;
    private static final BitSet rgBitSet;
    private static final BitSet adminBitSet;

    static {
        // DEO
        deoBitSet = new BitSet();
        deoBitSet.set(Permission.EDIT_ADOPTION);
        deoBitSet.set(Permission.EDIT_BDF_CONFIRMATION);
        deoBitSet.set(Permission.EDIT_BDF);
        deoBitSet.set(Permission.PRINT_BDF);
        deoBitSet.set(Permission.PRINT_BIRTH_CERTIFICATE);
        deoBitSet.set(Permission.PRINT_DDF);
        deoBitSet.set(Permission.SEARCH_BDF);
        deoBitSet.set(Permission.SEARCH_DDF);
        deoBitSet.set(Permission.EDIT_DEATH);
        deoBitSet.set(Permission.USER_PREFERENCES);
        deoBitSet.set(Permission.EDIT_BIRTH_ALTERATION);
        deoBitSet.set(Permission.SEARCH_PRS);
        deoBitSet.set(Permission.PRS_LOOKUP_PERSON_BY_KEYS);
        deoBitSet.set(Permission.PRS_VIEW_PERSON);
        deoBitSet.set(Permission.PRS_ADD_PERSON);
        deoBitSet.set(Permission.PRS_PRINT_CERT);
        deoBitSet.set(Permission.EDIT_MARRIAGE);
        deoBitSet.set(Permission.ADD_MARRIAGE);
        deoBitSet.set(Permission.DELETE_MARRIAGE);
        deoBitSet.set(Permission.PRINT_MARRIAGE_LICENSE);
        deoBitSet.set(Permission.PRINT_MARRIAGE_EXTRACT);
        deoBitSet.set(Permission.SEARCH_REGISTRAR);
        deoBitSet.set(Permission.VIEW_MARRIAGE_REGISTER);
        deoBitSet.set(Permission.SEARCH_MARRIAGE);
        deoBitSet.set(Permission.VIEW_SCANNED_MARRIAGE_CERT);
        // ADR

        adrBitSet = new BitSet();
        adrBitSet.or(deoBitSet);
        adrBitSet.set(Permission.APPROVE_BDF);
        adrBitSet.set(Permission.APPROVE_BDF_CONFIRMATION);
        adrBitSet.set(Permission.APPROVE_DEATH);
        adrBitSet.set(Permission.PRINT_DEATH_CERTIFICATE);
        adrBitSet.set(Permission.REGISTRAR_MANAGEMENT);
        adrBitSet.set(Permission.PRS_EDIT_PERSON);
        adrBitSet.set(Permission.PRS_APPROVE_PERSON);
        adrBitSet.set(Permission.PRS_DELETE_PERSON);
        adrBitSet.set(Permission.PRS_REJECT_PERSON);
        adrBitSet.set(Permission.PRS_MARK_CERT_PRINTED);
        adrBitSet.set(Permission.PRS_EDIT_PERSON_AFTER_APPROVE);
        adrBitSet.set(Permission.PRINT_MARRIAGE_CERTIFICATE);
        adrBitSet.set(Permission.APPROVE_MARRIAGE);
        adrBitSet.set(Permission.DIVORCE);
        adrBitSet.set(Permission.APPROVE_BIRTH_ALTERATION);
        // DR
        drBitSet = new BitSet();
        drBitSet.or(adrBitSet);

        // ARG
        argBitSet = new BitSet();
        argBitSet.or(adrBitSet);
        argBitSet.set(Permission.APPROVE_ADOPTION);
        argBitSet.set(Permission.APPROVE_BDF_BELATED);

        // RG
        rgBitSet = new BitSet();
        rgBitSet.or(argBitSet);

        // ADMIN
        adminBitSet = new BitSet();
        adminBitSet.set(Permission.USER_MANAGEMENT);
        adminBitSet.set(Permission.SERVICE_MASTER_DATA_MANAGEMENT);
        adminBitSet.set(Permission.USER_PREFERENCES);
        adminBitSet.set(Permission.REGISTRAR_MANAGEMENT);
        adminBitSet.set(Permission.SEARCH_REGISTRAR);
        adminBitSet.set(Permission.EVENTS_MANAGEMENT);
        adminBitSet.set(Permission.INDEX_RECORDS);
        adminBitSet.set(Permission.GENERATE_REPORTS);


    }

    public static void setPermissionBits(ApplicationContext ctx) {
        try {
            // ---------------- populate permissions ---------------------
            RoleDAO roleDao = (RoleDAO) ctx.getBean("roleDAOImpl", RoleDAO.class);

            Role deoRole = roleDao.getRole("DEO");
            deoRole.setPermBitSet(deoBitSet);
            roleDao.save(deoRole);

            Role adrRole = roleDao.getRole("ADR");
            adrRole.setPermBitSet(adrBitSet);
            roleDao.save(adrRole);

            Role drRole = roleDao.getRole("DR");
            drRole.setPermBitSet(drBitSet);
            roleDao.save(drRole);

            Role argRole = roleDao.getRole("ARG");
            argRole.setPermBitSet(argBitSet);
            roleDao.save(argRole);

            Role rgRole = roleDao.getRole("RG");
            rgRole.setPermBitSet(rgBitSet);
            roleDao.save(rgRole);

            Role adminRole = roleDao.getRole("ADMIN");
            adminRole.setPermBitSet(adminBitSet);
            roleDao.save(adminRole);


            logger.info("Initialized the database by performing permission initialization");
            System.out.println("\n**********          **********          **********          **********          **********\n");
        } catch (Exception e) {
            logger.error("Error initializing role permissions on the database");
            throw new IllegalStateException("Error initializing role permissions. See log for details", e);
        }
    }
}
