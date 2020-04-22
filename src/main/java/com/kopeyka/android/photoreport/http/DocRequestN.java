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

public class DocRequestN {

    @SerializedName("UUID")
    final UUID uuid;


    @SerializedName("PhotoN")
    final Integer PhotoN;

    @SerializedName("PhotoCount")
    final Integer PhotoCount;



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
    public List<DocRequestN.PhotoJson> PhotoList = new ArrayList();

    public class PhotoJson {
        @SerializedName("PhotoBASE64")
        public String PhotoItem;

        public PhotoJson(String base64){
            PhotoItem = base64;

        }
    }




    public DocRequestN(Note note, Fragment fragment,Integer PhotoN,Integer PhotoCount ) {
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
        this.PhotoN = PhotoN;
        this.PhotoCount = PhotoCount;

        List photoArr = note.getPhotoArray();


        Photo photoImg = (Photo) photoArr.get(PhotoN);
        if (photoImg != null) {
            String path = fragment.getActivity().getFileStreamPath(photoImg.getFileName()).getAbsolutePath();
            String base64Img = PictureUtils.getBase64FromPath(path);
            PhotoJson PhotoItem = new PhotoJson(base64Img);
            //PhotoJson PhotoItem = new PhotoJson("8769696");
            this.PhotoList.add(PhotoItem);

        }

    }

    public Integer getPhotoN() {
        return PhotoN+1;
    }

    public Integer getPhotoCount() {
        return PhotoCount;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }
}
