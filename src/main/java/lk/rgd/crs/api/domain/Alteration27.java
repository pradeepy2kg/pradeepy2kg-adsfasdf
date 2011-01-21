package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.util.BitSet;

/**
 * @author Ashoka Ekanayaka
 *         To model the alterations which comes under secion 27 in the Act.
 *         Basic Name changes
 */
@Embeddable
public class Alteration27 {
    public static final int CHILD_FULL_NAME_OFFICIAL_LANG = 0;
    public static final int CHILD_FULL_NAME_ENGLISH = 1;

    @Column(nullable = true, length = 600)
    private String childFullNameOfficialLang;

    @Column(nullable = true, length = 600)
    private String childFullNameEnglish;

    public String getChildFullNameOfficialLang() {
        return childFullNameOfficialLang;
    }

    public void setChildFullNameOfficialLang(String childFullNameOfficialLang) {
        this.childFullNameOfficialLang = WebUtils.filterBlanksAndToUpper(childFullNameOfficialLang);
    }

    public String getChildFullNameEnglish() {
        return childFullNameEnglish;
    }

    public void setChildFullNameEnglish(String childFullNameEnglish) {
        this.childFullNameEnglish = WebUtils.filterBlanksAndToUpper(childFullNameEnglish);
    }
}
