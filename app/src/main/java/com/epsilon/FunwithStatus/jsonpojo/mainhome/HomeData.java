package com.epsilon.FunwithStatus.jsonpojo.mainhome;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HomeData {
    @SerializedName("current_page")
    @Expose
    public int currentPage;
    @SerializedName("data")
    @Expose
    public List<HomeDatum> data = new ArrayList<HomeDatum>();
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
    public String nextPageUrl;
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
