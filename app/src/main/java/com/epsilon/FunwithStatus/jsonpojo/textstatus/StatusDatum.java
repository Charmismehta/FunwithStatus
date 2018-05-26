package com.epsilon.FunwithStatus.jsonpojo.textstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusDatum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("like")
    @Expose
    private String like;
    @SerializedName("dislike")
    @Expose
    private String dislike;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }
}
