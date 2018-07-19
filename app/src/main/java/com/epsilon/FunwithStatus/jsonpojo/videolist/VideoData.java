package com.epsilon.FunwithStatus.jsonpojo.videolist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VideoData {
    @SerializedName("current_page")
    @Expose
    public int currentPage;
    @SerializedName("data")
    @Expose
    public List<VideoListDatum> data = new ArrayList<VideoListDatum>();
    @SerializedName("first_page_url")
    @Expose
    public String firstPageUrl;
    @SerializedName("from")
    @Expose
    public int from;
    @SerializedName("last_page")
    @Expose
    public int lastPage;
    @SerializedName("last_page_url")
    @Expose
    public String lastPageUrl;
    @SerializedName("next_page_url")
    @Expose
    public Object nextPageUrl;
    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("per_page")
    @Expose
    public int perPage;
    @SerializedName("prev_page_url")
    @Expose
    public Object prevPageUrl;
    @SerializedName("to")
    @Expose
    public int to;
    @SerializedName("total")
    @Expose
    public int total;
}
