package com.epsilon.FunwithStatus.utills;


import android.annotation.SuppressLint;
import android.util.Log;

import com.epsilon.FunwithStatus.adapter.GridViewItem;
import com.epsilon.FunwithStatus.adapter.GridViewVideoItem;
import com.epsilon.FunwithStatus.jsonpojo.image_category.ImageCategoryDatum;
import com.epsilon.FunwithStatus.jsonpojo.image_category.ImageSubcategory;
import com.epsilon.FunwithStatus.jsonpojo.image_list.ImageListDatum;
import com.epsilon.FunwithStatus.jsonpojo.tending_img.TrendingImgDatum;
import com.epsilon.FunwithStatus.jsonpojo.textstatus.StatusDatum;
import com.epsilon.FunwithStatus.jsonpojo.trending.TrendingDatum;
import com.epsilon.FunwithStatus.jsonpojo.videolist.VideoListDatum;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static String IS_LOGIN="isLogin";
    public static final String NAV_ITEM_INTENT="nav_item_intent";
    public static List<StatusDatum> statusData = new ArrayList<>();
    public static List<ImageCategoryDatum> imageCategoryData = new ArrayList<>();
    public static List<ImageListDatum> imageListData = new ArrayList<>();
    public static List<ImageSubcategory> imageSubcategories = new ArrayList<>();
    public static List<TrendingDatum> trendingData = new ArrayList<>();
    public static List<TrendingImgDatum> trendingimgData = new ArrayList<>();
    public static List<VideoListDatum> videoListData = new ArrayList<>();
    public static List<GridViewItem> items = new ArrayList<>();
    public static List<GridViewVideoItem> videoitems = new ArrayList<>();


    public static final String UPLOAD_URL = "https://epsiloninfotech.co.in/funwithstatus/image/upload.php";
    public static final String UPLOAD_VIDEO = "https://epsiloninfotech.co.in/funwithstatus/video/upload.php";


    @SuppressLint("LongLogTag")
    public static String getFileName(String urlStr) {

        String fileNameWithoutExtension="Video_"+System.currentTimeMillis();
        if (urlStr != null && urlStr.length() != 0) {
            String fileName = urlStr.substring(urlStr.lastIndexOf('/') + 1, urlStr.length());
            fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));

            Log.i("File Name", fileName);
            Log.i("File Name Without Extension", fileNameWithoutExtension);

        }

        return fileNameWithoutExtension;
    }
}
