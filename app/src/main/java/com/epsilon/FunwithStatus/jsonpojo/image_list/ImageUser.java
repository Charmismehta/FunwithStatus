package com.epsilon.FunwithStatus.jsonpojo.image_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageUser {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("profile_pic")
    @Expose
    public Object profilePic;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("facebook_id")
    @Expose
    public Object facebookId;
    @SerializedName("device_token")
    @Expose
    public Object deviceToken;
    @SerializedName("device_id")
    @Expose
    public Object deviceId;
    @SerializedName("device_type")
    @Expose
    public Object deviceType;
}
