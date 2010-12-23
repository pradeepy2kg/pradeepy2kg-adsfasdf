package lk.rgd.crs.web.util;

/**
 * @author Mahesha
 */
public enum TypeOfMarriagePlace {

    REGISTRAR_OFFICE(1, "රෙජිස්ට්‍රාර් කන්තෝරුව ", "Registrars Office in tamil", "Registrars Office"),
    DS_OFFICE(2, "ප්‍රා. ලේ. කන්තෝරුව", "DS Office in tamil", "DS Office"),
    CHURCH(3, "දේවස්ථානය", "Church in tamil", "Church"),
    OTHER(4, "වෙනත්","Other in tamil", "Other");

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
        StringBuilder sb = new StringBuilder(siType).append(" / ").append(taType).append(" / ").append(enType);
        type = sb.toString();
        return type;
    }

}
