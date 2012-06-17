package lk.rgd.crs.api.bean;

/**
 * @author asankha
 */
public class UserWarning {

    public enum Severity {
        INFO, WARN, ERROR
    }

    private String message = null;
    private Severity severity = Severity.WARN;

    public UserWarning(String message) {
        this(message, Severity.WARN);
    }

    public UserWarning(String message, Severity severity) {
        this.message = message;
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
}
