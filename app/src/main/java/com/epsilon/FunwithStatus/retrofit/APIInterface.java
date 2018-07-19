package com.epsilon.FunwithStatus.retrofit;


import com.epsilon.FunwithStatus.jsonpojo.addimage.AddImage;
import com.epsilon.FunwithStatus.jsonpojo.addlike.AddLike;
import com.epsilon.FunwithStatus.jsonpojo.addstatus.AddStatus;
import com.epsilon.FunwithStatus.jsonpojo.categories.Categories;
import com.epsilon.FunwithStatus.jsonpojo.deleteimage.DeleteImage;
import com.epsilon.FunwithStatus.jsonpojo.deletetext.DeleteText;
import com.epsilon.FunwithStatus.jsonpojo.deletevideo.DeleteVideo;
import com.epsilon.FunwithStatus.jsonpojo.dislike.DisLike;
import com.epsilon.FunwithStatus.jsonpojo.forgetpwd.ForgotPwd;
import com.epsilon.FunwithStatus.jsonpojo.image_category.ImageCategory;
import com.epsilon.FunwithStatus.jsonpojo.image_list.ImageList;
import com.epsilon.FunwithStatus.jsonpojo.imagedislike.ImageDislike;
import com.epsilon.FunwithStatus.jsonpojo.imagelike.ImageLike;
import com.epsilon.FunwithStatus.jsonpojo.logout.Logout;
import com.epsilon.FunwithStatus.jsonpojo.mainhome.Home;
import com.epsilon.FunwithStatus.jsonpojo.tending_img.TrendingImg;
import com.epsilon.FunwithStatus.jsonpojo.textstatus.Status;
import com.epsilon.FunwithStatus.jsonpojo.trending.Trending;
import com.epsilon.FunwithStatus.jsonpojo.videodislike.VideoDisLike;
import com.epsilon.FunwithStatus.jsonpojo.videolike.VideoLike;
import com.epsilon.FunwithStatus.jsonpojo.videolist.VideoList;
import com.epsilon.FunwithStatus.jsonpojo.videolist.VideoListDatum;
import com.epsilon.FunwithStatus.utills.ServerURl;
import com.epsilon.FunwithStatus.jsonpojo.login.Login;
import com.epsilon.FunwithStatus.jsonpojo.registration.Registration;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by DeLL on 12-01-2018.
 */

public interface APIInterface {

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.LOGIN)
    Call<Login> loginpojocall(@Field("email") String email,
                              @Field("password") String password);


    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.REGISTER)
    Call<Registration> registerPojoCall(@Field("login_type") String login_type,
                                        @Field("name") String name,
                                        @Field("email") String email,
                                        @Field("password") String password);

    @GET(ServerURl.LOGOUT)
    Call<Logout> logout();


    @GET(ServerURl.CATEGORIES)
    Call<Categories> categoriespojo();

    @GET(ServerURl.HOME)
    Call<Home> homepojo();

//    public interface FooService {
        @GET("http://develop.ithetasolution.com/api/home")
        Call<Home> getHomeDataList(@Query("page") int page);
//    }

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.STATUSLIST)
    Call<Status> statuspojo(@Field("category_id") String category_id);


    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.ADDSTATUS)
    Call<AddStatus> addstatuspojo(@Field("category_id") int category_id,
                                  @Field("name") String name,
                                  @Field("text") String text,
                                  @Header("Authorization") String token);

    @Multipart
    @POST(ServerURl.ADDIMAGE)
    Call<AddImage> circlepostaddpojocall(@Part("name") RequestBody name,
                                         @Part("category_id") RequestBody category_id,
                                         @Header("Authorization") String token,
                                         @Part MultipartBody.Part file);



    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.ADDLIKE)
    Call<AddLike> addlikepojo(@Field("item_id") int item_id,
                              @Field("type") String type);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.VIDEOLOIST)
    Call<VideoList> videolist(@Field("category_id") int category_id);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.IMAGELIST)
    Call<ImageList> imagelistpojo(@Field("category_id") int category_id);





    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.DISLIKE)
    Call<DisLike> dislikepojo(@Field("category") String category,
                              @Field("email") String email,
                              @Field("status_id") String status_id,
                              @Field("status") String status);


    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.IMAGELIKE)
    Call<ImageLike> addimagelikepojo(@Field("category") String category,
                                @Field("email") String email,
                                @Field("image_id") String image_id,
                                @Field("image") String image);



    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.IMAGEDISLIKE)
    Call<ImageDislike> imagedislikepojo(@Field("category") String category,
                                        @Field("email") String email,
                                        @Field("image_id") String image_id,
                                        @Field("image") String image);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.DELETEIMAGE)
    Call<DeleteImage> deleteimage(@Field("id") String id);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.DELETETEXT)
    Call<DeleteText> deletetext(@Field("id") String id);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.DELETEVIDEO)
    Call<DeleteVideo> deletevideo(@Field("id") String id);

    @GET(ServerURl.TRANDING)
    Call<Trending> trendingpojo();

    @GET(ServerURl.TRANDINGIMG)
    Call<TrendingImg> trendingimgpojo();


    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.VIDEOLIKE)
    Call<VideoLike> videolike(@Field("email") String email,
                                @Field("video_id") String video_id,
                                @Field("video") String video);


    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.VIDEODISLIKE)
    Call<VideoDisLike> videodislike(@Field("email") String email,
                                    @Field("video_id") String video_id,
                                    @Field("video") String video);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.FORGETPWD)
    Call<ForgotPwd> forgotpwdpojo(@Field("email") String email);

}