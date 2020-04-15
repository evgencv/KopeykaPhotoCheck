package com.kopeyka.android.photoreport;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

public class SplashActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, NoteListActivity.class);
        startActivity(intent);
        finish();
    }
}
