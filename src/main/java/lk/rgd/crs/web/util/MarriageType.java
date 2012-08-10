package lk.rgd.crs.web.util;

import lk.rgd.AppConstants;

/**
 * @author Mahesha
 */
public enum MarriageType {

    GENERAL("සාමාන්‍ය ", "பொது", "General"),
    KANDYAN_BINNA("උඩරට බින්න ", "கண்டிய  பின்ன ", "Kandyan Binna"),
    KANDYAN_DEEGA("උඩරට දීග ", "கண்டிய தீக ", "Kandyan Deega"),
    MUSLIM("මුස්ලිම්", "முஸ்லிம்", "Muslim");

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
        StringBuilder sb = new StringBuilder(siType).append(AppConstants.SLASH).append(taType).
            append(AppConstants.SLASH).append(enType);
        type = sb.toString();
        return type;
    }

}
