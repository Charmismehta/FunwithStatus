package com.epsilon.FunwithStatus.jsonpojo.addstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddStatusDatum {
    @SerializedName("category_id")
    @Expose
    public String categoryId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("text")
    @Expose
    public String text;
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
