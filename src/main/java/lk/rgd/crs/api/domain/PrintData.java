package lk.rgd.crs.api.domain;

/**
 * This bean used for birth confirmation printing action
 *
 * @author Chathuranga Withana
 */
public class PrintData {
    private String serial;
    private String name;
    private int status;

    public PrintData(String serial, String name, int status) {
        this.serial = serial;
        this.name = name;
        this.status = status;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
