package com.kopeyka.android.photoreport.http;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TaskResponse {
    @SerializedName("document")
    public List<Document> document = null;

    public class Document {
        @SerializedName("Date")
        public String Date;

        @SerializedName("Name")
        public String Name;

        @SerializedName("Description")
        public String Description;

        @SerializedName("guid")
        public String guid;
    }

}

