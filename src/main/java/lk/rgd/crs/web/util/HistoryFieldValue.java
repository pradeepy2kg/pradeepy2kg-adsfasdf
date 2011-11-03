package lk.rgd.crs.web.util;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: widu
 */
public class HistoryFieldValue {
    private String changeValue;
    private Date approveDate;
    //private int alterations;

    public HistoryFieldValue(String changeValue,Date approveDate) {
        this.setChangeValue(changeValue);
        this.setApproveDate(approveDate);
       // this.setAlterations(alterations);
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

   /* public int getAlterations() {
        return alterations;
    }

    public void setAlterations(int alterations) {
        this.alterations = alterations;
    }*/
}
