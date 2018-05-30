package com.epsilon.FunwithStatus.jsonpojo.image_category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageCategoryDatum {

    @SerializedName("0")
    @Expose
    private List<ImageSubcategory> _0 = null;
    @SerializedName("1")
    @Expose
    private Object _1;
    @SerializedName("2")
    @Expose
    private Object _2;
    @SerializedName("3")
    @Expose
    private Object _3;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("count")
    @Expose
    private String count;

    public List<ImageSubcategory> get0() {
        return _0;
    }

    public void set0(List<ImageSubcategory> _0) {
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

    public Object get3() {
        return _3;
    }

    public void set3(Object _3) {
        this._3 = _3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
