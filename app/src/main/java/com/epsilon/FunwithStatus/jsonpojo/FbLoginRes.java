package com.epsilon.FunwithStatus.jsonpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FbLoginRes {

    @SerializedName("picture")
    @Expose
    public Picture picture;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("name")
    @Expose
    public String name;

    public class Data {

        @SerializedName("url")
        @Expose
        public String url;
        @SerializedName("is_silhouette")
        @Expose
        public boolean isSilhouette;
        @SerializedName("width")
        @Expose
        public int width;
        @SerializedName("height")
        @Expose
        public int height;

    }
    public class Picture {

        @SerializedName("data")
        @Expose
        public Data data;

    }
}
