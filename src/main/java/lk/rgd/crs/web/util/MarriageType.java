package lk.rgd.crs.web.util;

/**
 * @author Mahesha
 */
public enum MarriageType {
    GENERAL(1, "සාමාන්‍ය ", "general marriage in tamil", "General"),
    KANDYAN_BINNA(2, "උඩරට බින්න ", "Kandyan binna in tamil", "Kandyan Binna"),
    KANDYAN_DEEGA(3, "උඩරට බින්න දීග ","kandyan deega in tamil", "Kandyan Deega");

    private int id;
    private String siType;
    private String taType;
    private String enType;

    MarriageType(int id, String siType, String taType, String enType) {
        this.id = id;
        this.siType = siType;
        this.taType = taType;
        this.enType = enType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnType() {
        return enType;
    }

    public void setEnType(String enType) {
        this.enType = enType;
    }

    public String getSiType() {
        return siType;
    }

    public void setSiType(String siType) {
        this.siType = siType;
    }

    public String getTaType() {
        return taType;
    }

    public void setTaType(String taType) {
        this.taType = taType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(siType).append(" / ").append(taType).append(" / ").append(enType);
        return sb.toString();
    }

}
