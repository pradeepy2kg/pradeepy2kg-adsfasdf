package lk.rgd.crs.web;

import java.io.Serializable;

/**
 *@author amith jayasekara
 */
public class Link implements Serializable {
    private String propertyKey;
    private String category;
    private String action;

    public Link(String propertyKey, String link, String action) {
        this.propertyKey = propertyKey;
        this.category = link;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
