package lk.rgd.crs.api.domain;

import lk.rgd.crs.web.util.MarriageType;

import java.util.Date;

/**
 * An mock instance used to list marriage notices separated from the MarriageRegister
 *
 * @author Chathuranga Withana
 */
public final class MarriageNotice {

    private final long idUKey;
    private final MarriageType typeOfMarriage;
    private final Long serialOfNotice;
    private final Date dateOfNotice;
    private final String partyNameInOfficialLang;
    private final String partyPIN;
    private final Type type;
    private boolean hasSecond = true;
    private final MarriageRegister.State state;

    public enum Type {
        BOTH_NOTICE, /**
         * 0 - Male and female party have only a single marriage notice
         */
        MALE_NOTICE, /**
         * 1 - Marriage notice of male party
         */
        FEMALE_NOTICE   /** 2 - Marriage notice of female party */
    }

    public MarriageNotice(MarriageRegister mr, Type type) {
        this.idUKey = mr.getIdUKey();
        this.typeOfMarriage = mr.getTypeOfMarriage();
        this.type = type;
        this.state = mr.getState();

        if (Type.BOTH_NOTICE == type || Type.MALE_NOTICE == type) {
            this.serialOfNotice = mr.getSerialOfMaleNotice();
            this.dateOfNotice = mr.getDateOfMaleNotice();
            this.partyNameInOfficialLang = mr.getMale().getNameInOfficialLanguageMale();
            this.partyPIN = mr.getMale().getIdentificationNumberMale();
        } else {
            this.serialOfNotice = mr.getSerialOfFemaleNotice();
            this.dateOfNotice = mr.getDateOfFemaleNotice();
            this.partyNameInOfficialLang = mr.getFemale().getNameInOfficialLanguageFemale();
            this.partyPIN = mr.getFemale().getIdentificationNumberFemale();
        }
        if (mr.isSingleNotice() || (mr.getSerialOfMaleNotice() != null & mr.getSerialOfFemaleNotice() != null)) {
            this.hasSecond = false;
        }
    }

    public long getIdUKey() {
        return idUKey;
    }

    public MarriageType getTypeOfMarriage() {
        return typeOfMarriage;
    }

    public Long getSerialOfNotice() {
        return serialOfNotice;
    }

    public Date getDateOfNotice() {
        return dateOfNotice == null ? dateOfNotice : new Date(dateOfNotice.getTime());
    }

    public String getPartyNameInOfficialLang() {
        return partyNameInOfficialLang;
    }

    public String getPartyPIN() {
        return partyPIN;
    }

    public Type getType() {
        return type;
    }

    public boolean isHasSecond() {
        return hasSecond;
    }

    public MarriageRegister.State getState() {
        return state;
    }
}
