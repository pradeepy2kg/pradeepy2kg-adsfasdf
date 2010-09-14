package lk.rgd.crs.web;

import java.io.Serializable;

/**
 * @author amith jayasekara
 */
public class Link implements Serializable {
    private String propertyKey;
    private String category;
    private String action;
    private int permissionKey;
    private int orderNum;

    public Link(String propertyKey, String link, String action, int permissionKey, int orderNum) {
        this.propertyKey = propertyKey;
        this.category = link;
        this.action = action;
        this.permissionKey = permissionKey;
        this.orderNum = orderNum;
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

    public int getPermissionKey() {
        return permissionKey;
    }

    public void setPermissionKey(int permissionKey) {
        this.permissionKey = permissionKey;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}
