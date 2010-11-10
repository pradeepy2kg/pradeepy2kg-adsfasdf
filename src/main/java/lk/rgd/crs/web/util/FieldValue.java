package lk.rgd.crs.web.util;

/**
 * @author amith
 *         object for creating JSP's in alterations
 */
public class FieldValue {

    public FieldValue(String existingValue, String alterationValue, String fieldValue, boolean approved) {
        this.existingValue = existingValue;
        this.alterationValue = alterationValue;
        this.fieldValue = fieldValue;
        this.approved = approved;
    }

    private String existingValue;
    private String alterationValue;
    private String fieldValue;
    private boolean approved;


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

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
