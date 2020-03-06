package com.kopeyka.android.photoreport.http;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.kopeyka.android.photoreport.Note;
import com.kopeyka.android.photoreport.Photo;
import com.kopeyka.android.photoreport.PictureUtils;


import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DocRequest {

    @SerializedName("UUID")
    final UUID uuid;

    @SerializedName("Title")
    final String Title;

    @SerializedName("Content")
    final String Content;

    @SerializedName("DocNo")
    final String DocNo;

    @SerializedName("createDate")
    final Date createDate;

    @SerializedName("uploadDate")
    final Date uploadDate;

    @SerializedName("isComplete")
    final String isComplete;

    @SerializedName("ResponseCode")
    public String ResponseCode;

    @SerializedName("userShop")
    public String userShop;

    @SerializedName("ResponseMessage")
    public String ResponseMessage;

    @SerializedName("userSurname")
    public String userSurname;

    @SerializedName("userName")
    public String userName;

    @SerializedName("userTelephone")
    public String userTelephone;

    @SerializedName("PhotoList")
    public List<DocRequest.PhotoJson> PhotoList = new ArrayList();

    public class PhotoJson {
        @SerializedName("PhotoBASE64")
        public String PhotoItem;

        public PhotoJson(String base64){
            PhotoItem = base64;

        }
    }

    
    public DocRequest(Note note, Fragment fragment) {
        SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(fragment.getContext());
        this.isComplete = Boolean.toString(note.isComplete());
        this.createDate = note.getDate();
        this.uploadDate = Calendar.getInstance().getTime();;
        this.DocNo = note.getDocNo();
        this.Title = note.getTitle();
        this.uuid = note.getId();
        this.Content = note.getContent();
        this.userShop = prefs.getString("userShop", "notSelected");
        this.userSurname = prefs.getString("userSurname", "notSelected");
        this.userName = prefs.getString("userName", "notSelected");
        this.userTelephone = prefs.getString("userTelephone", "notSelected");

        List photoArr = note.getPhotoArray();
        for (int i = 0; i < photoArr.size(); i++) {

            Photo photoImg = (Photo) photoArr.get(i);
            BitmapDrawable bitmapDrawable = null;
            if (photoImg != null) {
                String path = fragment.getActivity()
                        .getFileStreamPath(photoImg.getFileName()).getAbsolutePath();
                bitmapDrawable = PictureUtils.getScaledDrawable(fragment.getActivity(),
                        path);


                PhotoJson PhotoItem = new PhotoJson("aaqae1aaswss");
                this.PhotoList.add(PhotoItem);
            }
        }

    }


    public String getResponseCode() {
        return ResponseCode;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }
}
