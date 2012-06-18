package lk.rgd.crs.web.util;

import lk.rgd.AppConstants;

/**
 * @author Mahesha
 */
public enum TypeOfMarriagePlace {

    REGISTRAR_OFFICE(1, "රෙජිස්ට්‍රාර් කන්තෝරුව ", "பதிவாளர் அலுவலகம் ", "Registrars Office"),
    DS_OFFICE(2, "ප්‍රාදේශීය ලේකම් කාර්යාලය", "பிரதேச செயலகம் ", "Divisional Secretariat Office"),
    CHURCH(3, "දේවස්ථානය", "தேவாலயம்", "Church"),
    OTHER(4, "වෙනත්", "வேறு", "Other");

    private int id;
    private String siType;
    private String taType;
    private String enType;
    private String type;

    TypeOfMarriagePlace(int id, String siType, String taType, String enType) {
        this.id = id;
        this.siType = siType;
        this.taType = taType;
        this.enType = enType;
    }

    public int getId() {
        return id;
    }

    public String getEnType() {
        return enType;
    }

    public String getSiType() {
        return siType;
    }

    public String getTaType() {
        return taType;
    }

    public void setTaType(String taType) {
        this.taType = taType;
    }

    public String getType() {
        StringBuilder sb = new StringBuilder(siType).append(AppConstants.SLASH).append(taType).
            append(AppConstants.SLASH).append(enType);
        type = sb.toString();
        return type;
    }

}
