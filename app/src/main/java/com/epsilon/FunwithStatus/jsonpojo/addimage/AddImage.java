package com.epsilon.FunwithStatus.jsonpojo.addimage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddImage {
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public AddImageDatum data;

}