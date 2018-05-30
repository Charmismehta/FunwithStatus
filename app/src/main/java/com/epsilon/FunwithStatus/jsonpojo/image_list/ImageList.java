package com.epsilon.FunwithStatus.jsonpojo.image_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Images")
    @Expose
    private List<ImageListDatum> images = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ImageListDatum> getImages() {
        return images;
    }

    public void setImages(List<ImageListDatum> images) {
        this.images = images;
    }
}
