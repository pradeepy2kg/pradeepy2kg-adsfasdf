package lk.rgd.crs.web.util;

/**
 * @author Mahesha
 */
public enum MarriageType {
    GENERAL("සාමාන්‍ය ", "general marriage in tamil", "General"),
    KANDYAN_BINNA("උඩරට බින්න ", "Kandyan binna in tamil", "Kandyan Binna"),
    KANDYAN_DEEGA("උඩරට දීග ", "kandyan deega in tamil", "Kandyan Deega"),
    MUSLIM("මුස්ලිම්", "Muslim in Tamil", "Muslim");

    private String siType;
    private String taType;
    private String enType;
    private String type;

    MarriageType(String siType, String taType, String enType) {
        this.siType = siType;
        this.taType = taType;
        this.enType = enType;
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
