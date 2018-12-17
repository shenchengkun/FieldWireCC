package com.example.cheng.fieldwirecc.Presenter;

import android.content.Context;

import com.example.cheng.fieldwirecc.Model.Beans.SearchResponseData;
import com.example.cheng.fieldwirecc.Model.DataModel;
import com.example.cheng.fieldwirecc.View.ViewCallback;

import java.util.List;

public class Presenter implements PresenterCallback{
    private ViewCallback viewCallback;
    private DataModel dataModel;

    @Override
    public void onSuccess(List<SearchResponseData> data) {
        if(viewCallback!=null) viewCallback.onSuccess(data);
    }

    @Override
    public void onFailure() {
        if(viewCallback!=null) viewCallback.onFailure();
    }

    public Presenter(ViewCallback viewCallback) {
        this.viewCallback = viewCallback;
        dataModel = new DataModel(this);
    }

    public void getSearch(String keyWord,int page){
        dataModel.getSearch(keyWord,page);
    }

    public List<String> readHistory(Context context){
        return dataModel.readHistory(context);
    }

    public void writeHistory(List<String> list,Context context){
        dataModel.writeHistory(list,context);
    }

    public void detachCallback(){
        viewCallback = null;
    }
}
