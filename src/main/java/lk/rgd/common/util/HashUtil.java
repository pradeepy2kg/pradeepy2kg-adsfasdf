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

    public static final String hashPerson(Person person) {
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append(URLEncoder.encode(person.getFullNameInOfficialLanguage(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.warn("UTF-8 encoding not supported !", e);
            throw new RGDRuntimeException("UTF-8 encoding not supported", e);
        }
        buffer.append(person.getFullNameInEnglishLanguage());
        buffer.append(person.getPin());
        buffer.append(person.getGender());
        buffer.append(person.getDateOfBirth());
        //todo add lifecycleinfo to person and include record creation time.
        // buffer.append(person.getLifeCycleInfo.)

        return hashString(buffer.toString());
    }

    /**
     * @inheritDoc
     */
    public static final String hashString(String s) {
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
