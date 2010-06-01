package lk.rgd.crs.api.domain;

import java.util.Date;


/**
 * @author Duminda Dharmakeerthi.
 */

public class BirthRegisterApproval {
    private int serial;
    private String name;
    private boolean changes;
    private Date recievedDate;
    private String actions;

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getActions() {
        return actions;
    }

    public void setRecievedDate(Date recievedDate) {
        this.recievedDate = recievedDate;
    }

    public Date getRecievedDate() {
        return recievedDate;
    }

    public void setChanges(boolean changes) {
        this.changes = changes;
    }

    public boolean isChanges() {
        return changes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public int getSerial() {
        return serial;
    }
}
