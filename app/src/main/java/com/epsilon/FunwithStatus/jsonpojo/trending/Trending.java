package com.epsilon.FunwithStatus.jsonpojo.trending;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trending {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<TrendingDatum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TrendingDatum> getData() {
        return data;
    }

    public void setData(List<TrendingDatum> data) {
        this.data = data;
    }
}
