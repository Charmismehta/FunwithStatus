package com.epsilon.FunwithStatus.jsonpojo.image_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageList {
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public ImageData data;
}
