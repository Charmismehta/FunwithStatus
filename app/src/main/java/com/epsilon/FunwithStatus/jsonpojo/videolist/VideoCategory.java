package com.epsilon.FunwithStatus.jsonpojo.videolist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoCategory {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("category_name")
    @Expose
    public String categoryName;
    @SerializedName("category_image")
    @Expose
    public String categoryImage;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("image")
    @Expose
    public String image;
}
