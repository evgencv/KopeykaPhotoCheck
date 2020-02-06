package com.kopeyka.android.photoreport.http;

import com.google.gson.annotations.SerializedName;
import java.util.List;


public class ShopResponse {
    @SerializedName("shop")
    public List<Shop> shop = null;

    public class Shop {
        @SerializedName("Name")
        public String Name;
        @SerializedName("Code")
        public Integer Code;


     }




}
