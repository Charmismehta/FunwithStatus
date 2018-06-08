package com.epsilon.FunwithStatus.jsonpojo.videolist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Images")
    @Expose
    private List<VideoListDatum> images = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<VideoListDatum> getImages() {
        return images;
    }

    public void setImages(List<VideoListDatum> images) {
        this.images = images;
    }
}
