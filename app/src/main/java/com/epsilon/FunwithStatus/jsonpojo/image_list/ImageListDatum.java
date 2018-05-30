package com.epsilon.FunwithStatus.jsonpojo.image_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageListDatum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("subcata")
    @Expose
    private String subcata;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("like")
    @Expose
    private String like;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubcata() {
        return subcata;
    }

    public void setSubcata(String subcata) {
        this.subcata = subcata;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

}
