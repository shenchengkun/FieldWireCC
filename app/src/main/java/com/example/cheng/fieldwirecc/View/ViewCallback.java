package com.example.cheng.fieldwirecc.View;

import com.example.cheng.fieldwirecc.Model.Beans.NYTtopBeans.NYTResponseResult;
import com.example.cheng.fieldwirecc.Model.Beans.SearchResponseData;

import java.util.List;

public interface ViewCallback {
    void onSuccess(List<SearchResponseData> data);
    void onFailure();

    void displayNYT(List<NYTResponseResult> list);
}
