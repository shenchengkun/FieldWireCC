package com.example.cheng.fieldwirecc.Model.Beans.NYTtopBeans;

import java.util.List;

public class NYTResponse {
    List<NYTResponseResult> results;

    public List<NYTResponseResult> getResults() {
        return results;
    }

    public void setResults(List<NYTResponseResult> results) {
        this.results = results;
    }
}
