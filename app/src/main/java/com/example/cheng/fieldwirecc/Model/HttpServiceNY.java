package com.example.cheng.fieldwirecc.Model;

import com.example.cheng.fieldwirecc.Model.Beans.NYTtopBeans.NYTResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpServiceNY {
    @GET("svc/topstories/v2/world.json")
    Call<NYTResponse> getNYTop(@Query("api-key") String key);
}
