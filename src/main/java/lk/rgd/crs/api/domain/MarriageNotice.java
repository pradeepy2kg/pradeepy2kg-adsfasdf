package lk.rgd.crs.api.domain;

import java.util.Date;

/**
 * An instance used to list marriage notices
 *
 * @author Chathuranga Withana
 */
public final class MarriageNotice {

    private final long idUKey;
    private final MarriageRegister.TypeOfMarriage typeOfMarriage;
    private final MarriageRegister.PlaceOfMarriage placeOfMarriage;
    private final String serialOfNotice;
    private final Date dateOfNotice;
    private final String partyNameInOfficialLang;
    private final String partyPIN;
    private final Type type;

    public enum Type {
        BOTH_NOTICE, /** 0 - Male and female party have only a single marriage notice */
        MALE_NOTICE, /** 1 - Marriage notice of male party */
        FEMALE_NOTICE   /** 2 - Marriage notice of female party */
    }

    public MarriageNotice(long idUKey, MarriageRegister.TypeOfMarriage typeOfMarriage,
        MarriageRegister.PlaceOfMarriage placeOfMarriage, String serialOfNotice, Date dateOfNotice,
        String partyNameInOfficialLang, String partyPIN, Type type) {
        this.idUKey = idUKey;
        this.typeOfMarriage = typeOfMarriage;
        this.placeOfMarriage = placeOfMarriage;
        this.serialOfNotice = serialOfNotice;
        this.dateOfNotice = dateOfNotice;
        this.partyNameInOfficialLang = partyNameInOfficialLang;
        this.partyPIN = partyPIN;
        this.type = type;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public MarriageRegister.TypeOfMarriage getTypeOfMarriage() {
        return typeOfMarriage;
    }

    public MarriageRegister.PlaceOfMarriage getPlaceOfMarriage() {
        return placeOfMarriage;
    }

    public String getSerialOfNotice() {
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
}
