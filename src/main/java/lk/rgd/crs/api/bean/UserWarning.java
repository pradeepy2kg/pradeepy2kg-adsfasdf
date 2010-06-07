package lk.rgd.crs.api.bean;

/**
 * @author asankha
 */
public class UserWarning {

    private String message = null;

    public UserWarning(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
