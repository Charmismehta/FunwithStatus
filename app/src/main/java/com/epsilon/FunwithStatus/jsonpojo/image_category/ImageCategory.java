package com.epsilon.FunwithStatus.jsonpojo.image_category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageCategory {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Catagory")
    @Expose
    private List<ImageCategoryDatum> catagory = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ImageCategoryDatum> getCatagory() {
        return catagory;
    }

    public void setCatagory(List<ImageCategoryDatum> catagory) {
        this.catagory = catagory;
    }
}
