package lk.rgd.common.util;

import lk.rgd.prs.api.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chathuranga Withana
 */
public class PersonStatusUtil {
    // TODO change this names to appropriate ones
    private static final Logger logger = LoggerFactory.getLogger(PersonStatusUtil.class);
    private static final String PERSON_UNVERIFIED = "අනුමත නොකළ / #Unverified# / UNVERIFIED";
    private static final String PERSON_SEMI_VERIFIED = "අර්ධව අනුමතයි / #Semi Verified# / SEMI VERIFIED";
    private static final String PERSON_DATA_ENTRY = "දත්ත ඇතුලත් කිරීම/ #Data Entry# / DATA ENTRY";
    private static final String PERSON_VERIFIED = "අනුමතයි / #Verified# / VERIFIED";
    private static final String PERSON_CANCELLED = "අවලංගුයි / #Cancelled# / CANCELLED";
    private static final String PERSON_DELETED = "ප්‍රතික්ෂේපිතයි / #Rejected# / REJECTED";

    public static String getPersonStatusString(Person.Status status) {
        switch (status) {
            case UNVERIFIED:
                return PERSON_UNVERIFIED;
            case SEMI_VERIFIED:
                return PERSON_SEMI_VERIFIED;
            case DATA_ENTRY:
                return PERSON_DATA_ENTRY;
            case VERIFIED:
                return PERSON_VERIFIED;
            case CANCELLED:
                return PERSON_CANCELLED;
            case DELETED:
                return PERSON_DELETED;
        }
        logger.error("Invalid person status : {}", status);
        throw new IllegalArgumentException("Invalid person status : " + status);
    }
}
