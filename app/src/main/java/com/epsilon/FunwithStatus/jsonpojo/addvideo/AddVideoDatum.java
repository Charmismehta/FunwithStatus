package com.epsilon.FunwithStatus.jsonpojo.addvideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddVideoDatum {
    @SerializedName("category_id")
    @Expose
    public String categoryId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("file")
    @Expose
    public String file;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("id")
    @Expose
    public int id;
}
