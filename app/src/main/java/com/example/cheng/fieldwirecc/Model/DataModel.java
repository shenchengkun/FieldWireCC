package com.example.cheng.fieldwirecc.Model;

import android.content.Context;

import com.example.cheng.fieldwirecc.Model.Beans.SearchResponse;
import com.example.cheng.fieldwirecc.Model.Beans.SearchResponseData;
import com.example.cheng.fieldwirecc.Presenter.PresenterCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataModel {

    private PresenterCallback presenterCallback;
    private HttpService httpService;
    //private SearchHistoryService searchHistoryService;
    private SqliteService sqliteService;

    private static final String BASE_URL = "https://api.imgur.com/";
    private String clientID = "Client-ID 6c5b74d4a6a80c9";

    public DataModel(PresenterCallback presenterCallback,Context context) {
        this.presenterCallback = presenterCallback;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        httpService = retrofit.create(HttpService.class);

        //searchHistoryService = new SearchHistoryService();
        sqliteService = new SqliteService(context,"FieldDB",null,1);
    }

    public void getSearch(String keyWord,int page){
        httpService.getSearch(clientID,"top","all",page,
                keyWord).enqueue(
                new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call,
                                           Response<SearchResponse> response) {
                        if(response.isSuccessful()){
                            SearchResponse searchResponse = response.body();
                            presenterCallback.onSuccess(searchResponse.getData());
                        }else{
                            presenterCallback.onFailure();
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        presenterCallback.onFailure();
                    }
                }
        );
    }

    public List<String> readHistory(){
        //return searchHistoryService.readFromFile(context);
        return sqliteService.getSearchHistory();
    }

    public void writeHistory(List<String> list){
        //searchHistoryService.writeToFile(list,context);
        sqliteService.upgradeSearchHistory(list);
    }

    public List<SearchResponseData> readLastList(){
        return sqliteService.getLastList();
    }

    public void writeLastList(List<SearchResponseData> list){
        sqliteService.upgradeLastList(list);
    }
}
