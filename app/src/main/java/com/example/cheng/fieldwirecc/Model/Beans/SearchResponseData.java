package com.example.cheng.fieldwirecc.Model.Beans;

import java.io.Serializable;

public class SearchResponseData implements Serializable {
    private String link;
    private String id;
    private boolean is_album;

    public SearchResponseData(String id, String link) {
        this.link = link;
        this.id = id;
    }

    public boolean isIs_album() {
        return is_album;
    }

    public void setIs_album(boolean is_album) {
        this.is_album = is_album;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
