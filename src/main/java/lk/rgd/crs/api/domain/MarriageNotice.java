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
    private final NoticeType noticeType;

    public enum NoticeType {
        BOTH_HAVE_ONE_NOTICE, //only one notice
        MALE_NOTICE,
        FEMALE_NOTICE
    }

    public MarriageNotice(long idUKey, MarriageRegister.TypeOfMarriage typeOfMarriage,
        MarriageRegister.PlaceOfMarriage placeOfMarriage, String serialOfNotice, Date dateOfNotice,
        String partyNameInOfficialLang, String partyPIN, NoticeType noticeType) {
        this.idUKey = idUKey;
        this.typeOfMarriage = typeOfMarriage;
        this.placeOfMarriage = placeOfMarriage;
        this.serialOfNotice = serialOfNotice;
        this.dateOfNotice = dateOfNotice;
        this.partyNameInOfficialLang = partyNameInOfficialLang;
        this.partyPIN = partyPIN;
        this.noticeType = noticeType;
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

    public NoticeType getNoticeType() {
        return noticeType;
    }
}
