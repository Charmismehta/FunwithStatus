package com.epsilon.FunwithStatus.jsonpojo.category_text;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryDatum {
    @SerializedName("0")
    @Expose
    private List<SubCategory> _0 = null;
    @SerializedName("1")
    @Expose
    private Object _1;
    @SerializedName("2")
    @Expose
    private Object _2;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("count")
    @Expose
    private String count;

    public List<SubCategory> get0() {
        return _0;
    }

    public void set0(List<SubCategory> _0) {
        this._0 = _0;
    }

    public Object get1() {
        return _1;
    }

    public void set1(Object _1) {
        this._1 = _1;
    }

    public Object get2() {
        return _2;
    }

    public void set2(Object _2) {
        this._2 = _2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

}
