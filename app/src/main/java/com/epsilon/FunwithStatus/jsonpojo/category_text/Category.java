package com.epsilon.FunwithStatus.jsonpojo.category_text;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {
    @SerializedName("Catagory")
    @Expose
    private List<CategoryDatum> catagory = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<CategoryDatum> getCatagory() {
        return catagory;
    }

    public void setCatagory(List<CategoryDatum> catagory) {
        this.catagory = catagory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
