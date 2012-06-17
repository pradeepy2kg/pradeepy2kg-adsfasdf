package lk.rgd.crs.web.util;

import java.util.Date;

/**
 * Instance used to display alteration history values
 */
public class HistoryFieldValue {

    private String changeValue;
    private Date approveDate;

    public HistoryFieldValue(String changeValue, Date approveDate) {
        this.setChangeValue(changeValue);
        this.setApproveDate(approveDate);
    }

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }
}
