package com.kopeyka.android.photoreport.http;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface API {

    @POST("/bpm-connector/hs/PhotoReport/Shop")
    Call<ShopResponse> getShop();


    @POST("/bpm-connector/hs/PhotoReport/Task")
    Call<TaskResponse> getTask();


    @POST("/bpm-connector/hs/PhotoReport/getDocument")
    Call<DocRequest> postJson(@Body DocRequest body);

    @POST("/bpm-connector/hs/PhotoReport/getDocument")
    Call<DocRequestN> postJsonN(@Body DocRequestN body);
}
