package lk.rgd.common.util;

import lk.rgd.prs.api.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chathuranga Withana
 */
public class PersonStatusUtil {

    private static final Logger logger = LoggerFactory.getLogger(PersonStatusUtil.class);
    private static final String PERSON_UNVERIFIED = "@Unverified@ / #Unverified# / Unverified";
    private static final String PERSON_SEMI_VERIFIED = "@Semi Verified@ / #Semi Verified# / Semi Verified";
    private static final String PERSON_VERIFIED = "අනුමතයි / #Verified# / Verified";
    private static final String PERSON_CERT_PRINTED = "සහතිකය මුද්‍රිතයි / #Certificate Printed# / Certificate Printed";
    private static final String PERSON_CANCELLED = "අවලංගුයි / #Cancelled# / Cancelled";
    private static final String PERSON_DELETED = "ප්‍රතික්ෂේපිතයි / #Rejected# / Rejected";

    public static String getPersonStatusString(Person.Status status) {
        switch (status) {
            case UNVERIFIED:
                return PERSON_UNVERIFIED;
            case SEMI_VERIFIED:
                return PERSON_SEMI_VERIFIED;
            case VERIFIED:
                return PERSON_VERIFIED;
            case CERT_PRINTED:
                return PERSON_CERT_PRINTED;
            case CANCELLED:
                return PERSON_CANCELLED;
            case DELETED:
                return PERSON_DELETED;
        }
        logger.error("Invalid person status : {}", status);
        throw new IllegalArgumentException("Invalid person status : " + status);
    }
}
