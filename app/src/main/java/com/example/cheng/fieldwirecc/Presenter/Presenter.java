package com.example.cheng.fieldwirecc.Presenter;

import android.content.Context;

import com.example.cheng.fieldwirecc.Model.Beans.SearchResponseData;
import com.example.cheng.fieldwirecc.Model.DataModel;
import com.example.cheng.fieldwirecc.View.ViewCallback;

import java.util.ArrayList;
import java.util.List;

public class Presenter implements PresenterCallback{
    private ViewCallback viewCallback;
    private DataModel dataModel;

    @Override
    public void onSuccess(List<SearchResponseData> data) {
        if(viewCallback!=null) {
            List<SearchResponseData> notAlbum = new ArrayList<>();
            for (SearchResponseData searchResponseData : data) {
                if (!searchResponseData.isIs_album()) notAlbum.add(searchResponseData);
            }
            viewCallback.onSuccess(notAlbum);
        }
    }

    @Override
    public void onFailure() {
        if(viewCallback!=null) viewCallback.onFailure();
    }

    public Presenter(ViewCallback viewCallback,Context context) {
        this.viewCallback = viewCallback;
        dataModel = new DataModel(this,context);
    }

    public void getSearch(String keyWord,int page){
        dataModel.getSearch(keyWord,page);
    }

    public List<String> readHistory(){
        return dataModel.readHistory();
    }

    public void writeHistory(List<String> list){
        dataModel.writeHistory(list);
    }

    public List<SearchResponseData> readLastList(){
        return dataModel.readLastList();
    }

    public void writeLastList(List<SearchResponseData> list){
        dataModel.writeLastList(list);
    }

    public void detachCallback(){
        viewCallback = null;
    }
}
