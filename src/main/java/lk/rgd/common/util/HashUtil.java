package lk.rgd.common.util;

import lk.rgd.prs.api.domain.Person;
import lk.rgd.common.RGDRuntimeException;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility functions to create Hash values for person objects and passwords
 * @author : Ashoka Ekanayaka
 */
public class HashUtil {
    private static final Logger logger = LoggerFactory.getLogger(HashUtil.class);

    public static String hashPerson(Person person) {
        StringBuilder buffer = new StringBuilder();
        if (person.getFullNameInEnglishLanguage() != null) {
            buffer.append(person.getFullNameInEnglishLanguage());
        }
        buffer.append(person.getPin());
        buffer.append(person.getGender());
        buffer.append(person.getDateOfBirth());
        return hashString(buffer.toString());
    }

    /**
     * @inheritDoc
     */
    public static String hashString(String s) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            logger.warn("Cannot instantiate a SHA-1 message digest", e);
            throw new RGDRuntimeException("Cannot instantiate a SHA-1 message digest", e);
        }
        sha.reset();
        sha.update(s.getBytes());
        return new String(Base64.encode(sha.digest()));
    }

}
