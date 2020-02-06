package com.kopeyka.android.photoreport.http;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.FormUrlEncoded;


public interface API {

    @POST("/bpm-connector/hs/PhotoReport/Shop")
    Call<ShopResponse> getShop();


    @POST("/bpm-connector/hs/PhotoReport/Task")
    Call<TaskResponse> getTask();


}
