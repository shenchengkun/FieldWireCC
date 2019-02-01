package com.example.cheng.fieldwirecc.Model.Beans.NYTtopBeans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NYTResponseResult {
    @SerializedName("abstract")
    String abstracts;

    String short_url;
    List<NYTMultiMedia> multimedia;

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getShort_url() {
        return short_url;
    }

    public void setShort_url(String short_url) {
        this.short_url = short_url;
    }

    public List<NYTMultiMedia> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<NYTMultiMedia> multimedia) {
        this.multimedia = multimedia;
    }
}
