package com.kopeyka.android.photoreport;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Note {

    private UUID mId;
    private String mTitle;
    private String mDocNo;
    private String mContent;
    private Date mDate;
    private Photo mPhoto;
    private ArrayList<Photo>  mPhotoArray = new ArrayList<Photo>();
    private boolean mComplete;

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_CONTENT = "content";
    private static final String JSON_COMPLETE = "complete";
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "photo";
    private static final String JSON_PHOTO_ARR = "photo_array";
    private static final String JSON_DOC_NO = "document_number";

    public Note() {
        mId = UUID.randomUUID();
        mDate = new Date();

    }

    public Note(JSONObject json) throws JSONException {
        mId  = UUID.fromString(json.getString(JSON_ID));


        if (json.has(JSON_DOC_NO)) {
            mDocNo = json.getString(JSON_DOC_NO);
        }

        if (json.has(JSON_TITLE)) {
            mTitle = json.getString(JSON_TITLE);
        }
        if (json.has(JSON_CONTENT)) {
            mContent = json.getString(JSON_CONTENT);
        }

        if (json.has(JSON_PHOTO)) {
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }

        if (json.has(JSON_PHOTO_ARR)){
            JSONArray jsonArray = json.getJSONArray(JSON_PHOTO_ARR);
            for (int i = 0; i < jsonArray.length(); i++) {
                mPhotoArray.add(new Photo(jsonArray.getJSONObject(i)));
            }
        }



        mComplete = json.getBoolean(JSON_COMPLETE);
        mDate = new Date(json.getLong(JSON_DATE));
        Log.d(" NOTE Note", json.toString());
    }

    @Override
    public String toString() {
        return getTitle();
    }

    public JSONObject toJSON() throws JSONException {
        if (mDocNo == null){
            Random r = new Random();
            Integer mDocNoInt = r.nextInt(1000);
            mDocNo = mDocNoInt.toString();
        }
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_DOC_NO,mDocNo);
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_CONTENT, mContent);
        json.put(JSON_COMPLETE, mComplete);
        json.put(JSON_DATE, mDate.getTime());
        if (mPhoto != null) {
            json.put(JSON_PHOTO, mPhoto.toJSON());
        }

        if (mPhotoArray != null ){
            JSONArray jsonArr = new JSONArray();
            for (int i = 0; i < mPhotoArray.size();i++) {
                jsonArr.put(mPhotoArray.get(i).toJSON());
            }
            json.put(JSON_PHOTO_ARR, jsonArr);
        }

        Log.d(" NOTE toJSON", json.toString());
        return json;
    }


    public String getDocNo() {
        if (mDocNo == null){
            Random r = new Random();
            Integer mDocNoInt = r.nextInt(1000);
            mDocNo = mDocNoInt.toString();

        }

        return mDocNo;
    }

    public void setDocNo(String DocNo) {
        mDocNo = DocNo;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isComplete() {
        return mComplete;
    }

    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public ArrayList<Photo> getPhotoArray() {
        return mPhotoArray;
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
        mPhotoArray.add(photo);

    }
}