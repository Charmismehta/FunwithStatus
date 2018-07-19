package com.epsilon.FunwithStatus.jsonpojo.addstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddStatus {
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public AddStatusDatum data;
}
