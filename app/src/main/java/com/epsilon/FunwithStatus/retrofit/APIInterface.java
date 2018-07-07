package com.epsilon.FunwithStatus.retrofit;


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
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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


    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.TEXTLIST)
    Call<Status> textstatuspojo(@Field("subcat") String subcat);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.ADDSTATUS)
    Call<AddStatus> addstatuspojo(@Field("subcat") String subcat,
                                  @Field("user") String user,
                                  @Field("status") String status);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.ADDLIKE)
    Call<AddLike> addlikepojo(@Field("category") String category,
                                @Field("email") String email,
                                @Field("status_id") String status_id,
                                @Field("status") String status);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.DISLIKE)
    Call<DisLike> dislikepojo(@Field("category") String category,
                              @Field("email") String email,
                              @Field("status_id") String status_id,
                              @Field("status") String status);

    @GET(ServerURl.IMAGECATEGORY)
    Call<ImageCategory> imagepojo();

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(ServerURl.IMAGELIST)
    Call<ImageList> imagelistpojo(@Field("subcata") String subcata);

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

    @GET(ServerURl.VIDEOLIST)
    Call<VideoList> videolistpojo();

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