package com.example.cheng.fieldwirecc.Model.Beans;

import java.util.List;

public class SearchResponse {
    private List<SearchResponseData> data;
    private boolean success;
    private int status;

    public List<SearchResponseData> getData() {
        return this.data;
    }

    public void setData(List<SearchResponseData> data) {
        this.data = data;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
