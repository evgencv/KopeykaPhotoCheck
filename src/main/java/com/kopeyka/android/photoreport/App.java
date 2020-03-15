package com.kopeyka.android.photoreport;


import android.content.Context;



public class App extends android.app.Application {

    private static android.app.Application application;

    public static Context getContext() {
        return application.getApplicationContext();
    }

    public void onCreate() {
        super.onCreate();
        application = this;
    }
}