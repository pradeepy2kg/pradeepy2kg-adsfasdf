package lk.rgd.crs.web.util;

/**
 * @author amith
 *         object for creating JSP's in alterations
 */
public class FieldValue {

    public FieldValue(String existingValue, String alterationValue, int fieldConstant, String approved) {
        this.existingValue = existingValue;
        this.alterationValue = alterationValue;
        this.fieldConstant = fieldConstant;
        this.approved = approved;
    }

    private String existingValue;
    private String alterationValue;
    private int fieldConstant;
    private String approved;


    public String getExistingValue() {
        return existingValue;
    }

    public void setExistingValue(String existingValue) {
        this.existingValue = existingValue;
    }

    public String getAlterationValue() {
        return alterationValue;
    }

    public void setAlterationValue(String alterationValue) {
        this.alterationValue = alterationValue;
    }

    public int getFieldConstant() {
        return fieldConstant;
    }

    public void setFieldConstant(int fieldConstant) {
        this.fieldConstant = fieldConstant;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }
}
