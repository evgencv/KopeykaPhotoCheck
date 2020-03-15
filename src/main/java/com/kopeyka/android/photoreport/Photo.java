package com.kopeyka.android.photoreport;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Photo {

    private static final String JSON_FILENAME = "filename";

    private String mFileName;

    public Photo(String fileName) {
        mFileName = fileName;
    }

    public Photo(JSONObject json) throws JSONException {
        mFileName = json.getString(JSON_FILENAME);
    }


    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFileName);
        return json;
    }

    public String getFileName() {
        return mFileName;
    }

    public void deleteFiles() {
        File file = new File(this.mFileName);
        boolean deleted = file.delete();
        if (deleted){
            Toast.makeText(App.getContext(), "файл удален "+this.mFileName, Toast.LENGTH_SHORT).show();
      }


    }
}
