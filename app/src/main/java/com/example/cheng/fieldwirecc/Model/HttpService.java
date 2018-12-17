package com.example.cheng.fieldwirecc.Model;

import com.example.cheng.fieldwirecc.Model.Beans.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HttpService {
    @GET("3/gallery/search/{sort}/{window}/{page}")
    Call<SearchResponse> getSearch(@Header("Authorization") String cliendID,
                                   @Path("sort") String sort,
                                   @Path("window") String window,
                                   @Path("page") Integer page,
                                   @Query("q_all") String keyWord);
}
