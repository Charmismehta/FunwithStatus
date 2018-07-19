package com.epsilon.FunwithStatus.utills;

public class ServerURl {
//    public static final String BASE_URL="https://epsiloninfotech.co.in/funwithstatus/";
    public static final String BASE_URL="http://develop.ithetasolution.com/api/";

//    public static final String LOGIN = "login.php";
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String LOGOUT = "logout";
    public static final String CATEGORIES = "category";
    public static final String HOME="home";
    public static final String HOME(String page){
        return "home?page="+ page;
    }
    public static final String VIDEOLOIST = "video";
    public static final String IMAGELIST = "image";
    public static final String STATUSLIST = "status";
    public static final String ADDSTATUS = "status/add";
    public static final String ADDLIKE = "likes";
    public static final String ADDIMAGE = "image/add";
    public static final String ADDVIDEO = "video/add";





    public static final String FORGETPWD = "forget_password.php";
    public static final String DISLIKE = "dislike_status.php";
    public static final String IMAGELIKE = "like_image.php";
    public static final String IMAGEDISLIKE = "dislike_image.php";
    public static final String DELETEIMAGE = "delete_image.php";
    public static final String DELETETEXT = "delete_status.php";
    public static final String DELETEVIDEO = "video/delete_video.php";
    public static final String TRANDING = "trending_status.php";
    public static final String TRANDINGIMG = "trending_images.php";
    public static final String VIDEOLIKE = "video/like_video.php";
    public static final String VIDEODISLIKE = "video/dislike_video.php";

    public static  String home(int page){
        return BASE_URL+ "home?page="+page;
    }

}
