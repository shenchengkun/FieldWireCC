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

    private static final String BASE_URL = "https://api.imgur.com/";
    private PresenterCallback presenterCallback;
    private HttpService httpService;
    private SearchHistoryService searchHistoryService;
    private String clientID = "Client-ID 6c5b74d4a6a80c9";

    public DataModel(PresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        httpService = retrofit.create(HttpService.class);

        searchHistoryService = new SearchHistoryService();
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
                            List<SearchResponseData> notAlbum = new ArrayList<>();
                            for (SearchResponseData searchResponseData:searchResponse.getData()){
                                if(!searchResponseData.isIs_album()) notAlbum.add(searchResponseData);
                            }
                            presenterCallback.onSuccess(notAlbum);
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

    public List<String> readHistory(Context context){
        return searchHistoryService.readFromFile(context);
    }

    public void writeHistory(List<String> list,Context context){
        searchHistoryService.writeToFile(list,context);
    }
}
