package com.epsilon.FunwithStatus.jsonpojo.tending_img;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrendingImg {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<TrendingImgDatum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TrendingImgDatum> getData() {
        return data;
    }

    public void setData(List<TrendingImgDatum> data) {
        this.data = data;
    }

}
