package com.kopeyka.android.photoreport;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.kopeyka.android.photoreport.http.API;
import com.kopeyka.android.photoreport.http.APIClient;
import com.kopeyka.android.photoreport.http.ShopResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PrefActivity extends PreferenceActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        API apiboss;
        apiboss = APIClient.getShop().create(API.class);
        Call<ShopResponse> call = apiboss.getShop();
        call.enqueue(new Callback<ShopResponse>() {
            @Override
            public void onResponse(Call<ShopResponse> call, Response<ShopResponse> response) {
                ShopResponse userList = response.body();
                List<ShopResponse.Shop> datumList = userList.shop;

                for (ShopResponse.Shop shop : datumList) {

                    //Toast.makeText(NoteListFragment.super.getContext(), "Name : " + shop.Name + " Code: " + shop.Code, Toast.LENGTH_SHORT).show();
                    //Log.d("SHOP","Name : " + shop.Name + " Code: " + shop.Code);

                }
                Toast.makeText(getApplicationContext(), "Обновлен список магазинов", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ShopResponse> call, Throwable t) {
                call.cancel();
            }


        });







        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment

    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref);
        }

    }
}

