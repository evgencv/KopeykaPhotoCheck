package com.kopeyka.android.photoreport;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class NoteListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }


//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuItem mi = menu.add(0, 1, 0, "Preferences");
//        mi.setIntent(new Intent(this, PrefActivity.class));
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        //Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, PrefActivity.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }


}
