package com.epsilon.FunwithStatus.jsonpojo.tending_img;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrendingImgDatum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("subcata")
    @Expose
    private String subcata;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("liked")
    @Expose
    private String liked;
    @SerializedName("user")
    @Expose
    private String user;

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

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
