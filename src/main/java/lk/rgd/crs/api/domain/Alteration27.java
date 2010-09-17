package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.util.BitSet;

/**
 * @author Ashoka Ekanayaka
 * To model the alterations which comes under secion 27 in the Act.
 * Basic Name changes
 */
@Embeddable
public class Alteration27 {
    @Column(nullable = true, length = 600)
    private String childFullNameOfficialLang;

    @Column(nullable = true, length = 600)
    private String childFullNameEnglish;

    @Column(nullable = true)
    private boolean fullNameOfficialLangApproved;

    @Column(nullable = true)
    private boolean fullNameEnglishApproved;

    public String getChildFullNameOfficialLang() {
        return childFullNameOfficialLang;
    }

    public void setChildFullNameOfficialLang(String childFullNameOfficialLang) {
        this.childFullNameOfficialLang = childFullNameOfficialLang;
    }

    public String getChildFullNameEnglish() {
        return childFullNameEnglish;
    }

    public void setChildFullNameEnglish(String childFullNameEnglish) {
        this.childFullNameEnglish = childFullNameEnglish;
    }

    public boolean isFullNameOfficialLangApproved() {
        return fullNameOfficialLangApproved;
    }

    public void setFullNameOfficialLangApproved(boolean fullNameOfficialLangApproved) {
        this.fullNameOfficialLangApproved = fullNameOfficialLangApproved;
    }

    public boolean isFullNameEnglishApproved() {
        return fullNameEnglishApproved;
    }

    public void setFullNameEnglishApproved(boolean fullNameEnglishApproved) {
        this.fullNameEnglishApproved = fullNameEnglishApproved;
    }
}
