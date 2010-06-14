package lk.rgd.crs.web;

/**
 *@author amith jayasekara
 */
public class Link {
    private String propertyKey;
    private String link;
    private String action;

    public Link(String propertyKey, String link, String action) {
        this.propertyKey = propertyKey;
        this.link = link;
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPropertyKey() {
        return propertyKey;
    }


    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
