package com.kopeyka.android.photoreport;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;




public class AboutActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_program);

        TextView tv_ver = (TextView) findViewById(R.id.version);
        tv_ver.setText("ver/ 16.04.2020");

    }


}
