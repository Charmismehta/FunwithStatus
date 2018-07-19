package com.epsilon.FunwithStatus.jsonpojo.image_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageListDatum {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("category_id")
    @Expose
    public String categoryId;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("text")
    @Expose
    public Object text;
    @SerializedName("file")
    @Expose
    public String file;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("user_name")
    @Expose
    public String userName;
    @SerializedName("category_name")
    @Expose
    public String categoryName;
    @SerializedName("total_likes")
    @Expose
    public int totalLikes;
    @SerializedName("total_unlikes")
    @Expose
    public int totalUnlikes;
    @SerializedName("total_views")
    @Expose
    public int totalViews;
    @SerializedName("user_data")
    @Expose
    public ImageUser userData;
    @SerializedName("category")
    @Expose
    public ImageCategory category;

}
