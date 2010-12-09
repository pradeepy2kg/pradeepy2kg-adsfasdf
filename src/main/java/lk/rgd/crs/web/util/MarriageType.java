package lk.rgd.crs.web.util;

/**
 * @author Mahesha
 */
public enum MarriageType {
    GENERAL(1, "සාමාන්‍ය ", "general marriage in tamil", "General"),
    KANDYAN_BINNA(2, "උඩරට බින්න ", "Kandyan binna in tamil", "Kandyan Binna"),
    KANDYAN_DEEGA(3, "උඩරට බින්න දීග ","kandyan deega in tamil", "Kandyan Deega"),
    MUSLIM(4, "Muslim in Sinhala","Muslim in Tamil", "Muslim");

    private int id;
    private String siType;
    private String taType;
    private String enType;
    private String type;

    MarriageType(int id, String siType, String taType, String enType) {
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
