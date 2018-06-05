package com.epsilon.FunwithStatus.jsonpojo.trending;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrendingDatum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("subcata")
    @Expose
    private String subcata;
    @SerializedName("status")
    @Expose
    private String status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
