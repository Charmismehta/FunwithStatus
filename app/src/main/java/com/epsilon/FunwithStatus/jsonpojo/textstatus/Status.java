package com.epsilon.FunwithStatus.jsonpojo.textstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Status {
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<StatusDatum> data = null;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StatusDatum> getData() {
        return data;
    }

    public void setData(List<StatusDatum> data) {
        this.data = data;
    }

}
