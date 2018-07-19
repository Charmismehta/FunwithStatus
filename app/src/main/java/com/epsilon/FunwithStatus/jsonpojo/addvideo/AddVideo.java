package com.epsilon.FunwithStatus.jsonpojo.addvideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddVideo {
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public AddVideoDatum data;

}
