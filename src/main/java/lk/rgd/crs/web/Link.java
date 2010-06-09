package lk.rgd.crs.web;

/**
 *@author amith jayasekara
 */
public class Link {
    private String prppertyKey;
    private String link;

    public Link(String prppertyKey, String link) {
        this.prppertyKey = prppertyKey;
        this.link = link;
    }

    public String getPrppertyKey() {
        return prppertyKey;
    }


    public void setPrppertyKey(String prppertyKey) {
        this.prppertyKey = prppertyKey;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
