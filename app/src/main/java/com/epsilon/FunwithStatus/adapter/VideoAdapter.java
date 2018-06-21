package com.epsilon.FunwithStatus.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.epsilon.FunwithStatus.DisplayImage;
import com.epsilon.FunwithStatus.DisplayVideo;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.TextListActivity;
import com.epsilon.FunwithStatus.jsonpojo.deletetext.DeleteText;
import com.epsilon.FunwithStatus.jsonpojo.deletevideo.DeleteVideo;
import com.epsilon.FunwithStatus.jsonpojo.videodislike.VideoDisLike;
import com.epsilon.FunwithStatus.jsonpojo.videolike.VideoLike;
import com.epsilon.FunwithStatus.jsonpojo.videolist.VideoList;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;
import static cn.jzvd.JZVideoPlayer.TAG;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {


    Activity activity;
    private LayoutInflater inflater;
    public Resources res;
    //String video, email, video_id, name,u_name;
    //   Uri uri;
    int count = 0;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    Sessionmanager sessionmanager;


    public VideoAdapter(Activity a) {
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        sessionmanager = new Sessionmanager(activity);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final String email = sessionmanager.getValue(Sessionmanager.Email);
        final String video_id = Constants.videoListData.get(position).getId();
        final String video = Constants.videoListData.get(position).getImage();
        final Uri uri = Uri.parse(video);
        final String name = Constants.videoListData.get(position).getFilename();
        final String u_name = sessionmanager.getValue(Sessionmanager.Name);
        String uname = Constants.videoListData.get(position).getUser();

        RequestOptions options = new RequestOptions().frame(10000);
//        holder.JZVideoPlayerStandard.releaseAllVideos();
        holder.JZVideoPlayerStandard.setUp(video,
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                name);
        Glide.with(activity).asBitmap()
                .load(video)
                .apply(options)
                .into(holder.JZVideoPlayerStandard.thumbImageView);

        holder.user_name.setText(Constants.videoListData.get(position).getUser());
        holder.like_count.setText(Constants.videoListData.get(position).getLiked());
        holder.share.setColorFilter(activity.getResources().getColor(R.color.colorAccent));
        holder.like.setColorFilter(activity.getResources().getColor(R.color.colorAccent));
        holder.dislike.setColorFilter(activity.getResources().getColor(R.color.colorAccent));
        holder.delete.setColorFilter(activity.getResources().getColor(R.color.colorAccent));
        holder.download.setColorFilter(activity.getResources().getColor(R.color.colorAccent));

        if (uname.equalsIgnoreCase(u_name))
        {
            holder.delete.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.delete.setVisibility(View.GONE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(activity.getBaseContext(), R.anim.move);
                final Animation animation_3 = AnimationUtils.loadAnimation(activity.getBaseContext(), R.anim.abc_fade_out);

                holder.delete.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        holder.delete.startAnimation(animation_3);
                        delete(Constants.videoListData.get(position).getId());
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


        holder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(activity, DisplayVideo.class);
                it.putExtra("VIDEO", Constants.videoListData.get(position).getImage());
                it.putExtra("VIDEO_ID", Constants.videoListData.get(position).getId());
                activity.startActivity(it);
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File extStore = Environment.getExternalStorageDirectory();
                final File myFile = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.videoListData.get(position).getFilename() + ".mp4");
                final Animation animation_2 = AnimationUtils.loadAnimation(activity.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(activity.getBaseContext(), R.anim.abc_fade_out);

                holder.download.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        holder.download.startAnimation(animation_3);
                        boolean result = checkPermission();
                        if (result) {
                            if (myFile.exists()) {
                                Toast.makeText(activity, "Already Downloaded", Toast.LENGTH_SHORT).show();
                            } else {
                                downloadonly(video, position);
//
                            }

                        }

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(activity.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(activity.getBaseContext(), R.anim.abc_fade_out);

                holder.whatsapp.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        holder.whatsapp.startAnimation(animation_3);
                        Log.e("URI", ":" + uri);

                        new Download(activity, video, position).execute();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        holder.facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(activity.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(activity.getBaseContext(), R.anim.abc_fade_out);

                holder.facebook.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        holder.facebook.startAnimation(animation_3);
                        new facebookDownload(activity, video, position).execute();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(activity.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(activity.getBaseContext(), R.anim.abc_fade_out);

                holder.share.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        holder.share.startAnimation(animation_3);
                        Log.e("URI", ":" + uri);

                        File extStore = Environment.getExternalStorageDirectory();
                        File myFile = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.getFileName(video)  + ".mp4");

                        if (myFile.exists()) {
                            sharevideo(position);
                        } else {
                            downloadFile(video, position);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(activity);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("Please Wait...");
                dialog.show();
                final Call<VideoLike> countrycall = apiInterface.videolike(email, video_id, video);
                countrycall.enqueue(new Callback<VideoLike>() {
                    @Override
                    public void onResponse(Call<VideoLike> call, Response<VideoLike> response) {
                        dialog.dismiss();
                        if (!Constants.videoListData.get(position).getLiked().equalsIgnoreCase("") && Constants.videoListData.get(position).getLiked() != null) {
                            count = Integer.valueOf(1 + Constants.videoListData.get(position).getLiked());
                        } else {
                            count = count + 1;
                        }
                        holder.like_count.setText(Constants.videoListData.get(position).getLiked());
                        Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<VideoLike> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        holder.dislike.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(activity);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("Please Wait...");
                dialog.show();
                final Call<VideoDisLike> countrycall = apiInterface.videodislike(email, video_id, video);
                countrycall.enqueue(new Callback<VideoDisLike>() {
                    @Override
                    public void onResponse(Call<VideoDisLike> call, Response<VideoDisLike> response) {
                        dialog.dismiss();
                        Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<VideoDisLike> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public class facebookDownload extends AsyncTask<Void, Void, String> {
        ProgressDialog mProgressDialog;

        Context context;
        String video;
        int position;

        public facebookDownload(Context context, String video, int position) {
            this.context = context;
            this.video = video;
            this.position = position;

        }

        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(context, "",
                    "Please wait, Download …");
        }

        @Override
        protected String doInBackground(Void... voids) {
            File extStore = Environment.getExternalStorageDirectory();
            File myFile = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.videoListData.get(position).getFilename() + ".mp4");

            if (!myFile.exists()) {
                downloadFile(video, position);
            } else {
                Log.e("downloadFile", "myFile:" + myFile.getAbsolutePath());
                //sharewhatupp(position);

            }
            return "done";
        }


        protected void onPostExecute(String result) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            if (result.equals("done")) {
                shareFacebook(position);


            }
        }
    }

    public void delete(String id) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<DeleteVideo> countrycall = apiInterface.deletevideo(id);
        countrycall.enqueue(new Callback<DeleteVideo>() {
            @Override
            public void onResponse(Call<DeleteVideo> call, Response<DeleteVideo> response) {
                dialog.dismiss();
                if (response.body().getStatus().equals("Succ")) {
                    videolist();
                    Toast.makeText(activity, "Delete Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteVideo> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }

    private void videolist() {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<VideoList> countrycall = apiInterface.videolistpojo();
        countrycall.enqueue(new Callback<VideoList>() {
            @Override
            public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                dialog.dismiss();
                if (Constants.videoListData != null) {
                    Constants.videoListData.clear();
                }
                if (!Constants.videoListData.equals("") && Constants.videoListData != null) {
                    Constants.videoListData.addAll(response.body().getImages());
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(activity, "No Video Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoList> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }


    public class Download extends AsyncTask<Void, Void, String> {
        ProgressDialog mProgressDialog;

        Context context;
        String video;
        int position;

        public Download(Context context, String video, int position) {
            this.context = context;
            this.video = video;
            this.position = position;

        }

        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(context, "",
                    "Please wait, Download …");
        }

        @Override
        protected String doInBackground(Void... voids) {
            File extStore = Environment.getExternalStorageDirectory();
            File myFile = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.getFileName(Constants.videoListData.get(position).getImage()) + ".mp4");

            if (!myFile.exists()) {
                downloadFile(video, position);
            } else {
                Log.e("downloadFile", "myFile:" + myFile.getAbsolutePath());
                //sharewhatupp(position);

            }
            return "done";
        }


        protected void onPostExecute(String result) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            if (result.equals("done")) {
                sharewhatupp(position);


            }
        }
    }


    public void shareFacebook(int position) {
        {
            try {
                File extStore = Environment.getExternalStorageDirectory();
                File file = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.videoListData.get(position).getFilename() + ".mp4");
                if (file.isFile()) {
                    MediaScannerConnection.scanFile(activity,
                            new String[]{file.toString()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(final String path,
                                                            final Uri picUri) {
                                    try {
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Intent videoshare = new Intent(Intent.ACTION_SEND);
                                                    videoshare.putExtra(Intent.EXTRA_STREAM, picUri);
                                                    videoshare.setType("video/*");
                                                    videoshare.setPackage("com.facebook.katana");
                                                    videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    activity.startActivity(videoshare);

                                                } catch (Exception e) {
                                                    Log.e("Error....", e.toString());
                                                    e.printStackTrace();

                                                    Intent videoshare = new Intent(Intent.ACTION_SEND);
                                                    videoshare.putExtra(Intent.EXTRA_STREAM, picUri);
                                                    videoshare.setType("video/*");
                                                    videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    activity.startActivity(Intent.createChooser(videoshare, "Share video by..."));
                                                }
                                            }
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void sharevideo(int position) {
        File extStore = Environment.getExternalStorageDirectory();
        File file = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.videoListData.get(position).getFilename() + ".mp4");
        if (file.exists()) {
            Log.e("downloadFile", "file:" + file.getAbsolutePath());
            Uri uri = Uri.parse(file.getPath());
            try {

                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.putExtra(Intent.EXTRA_STREAM, uri);
                videoshare.setType("video/*");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivity(videoshare);
            } catch (Exception e) {
                Log.e("Error....", e.toString());
                e.printStackTrace();

                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.putExtra(Intent.EXTRA_STREAM, uri);
                videoshare.setType("video/*");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivity(Intent.createChooser(videoshare, "Share video by..."));

            }

        }
    }


    public void sharewhatupp(int position) {

        File extStore = Environment.getExternalStorageDirectory();
        File file = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.getFileName(Constants.videoListData.get(position).getImage())+ ".mp4");
        if (file.exists()) {
            Log.e("downloadFile", "file:" + file.getAbsolutePath());
            Uri uri = Uri.parse(file.getPath());
            try {

                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                videoshare.setType("text/plain");
                videoshare.putExtra(Intent.EXTRA_STREAM, uri);
                videoshare.setType("video/*");
                videoshare.setPackage("com.whatsapp");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivity(videoshare);
            } catch (Exception e) {
                Log.e("Error....", e.toString());
                e.printStackTrace();
                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                videoshare.setType("text/plain");
                videoshare.putExtra(Intent.EXTRA_STREAM, uri);
                videoshare.setType("video/*");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivity(Intent.createChooser(videoshare, "Share video by..."));
            }
        }
    }

    public void downloadonly(String url, int position) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Download");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Download")
                .setDestinationInExternalPublicDir("/FunwithStatus", Constants.getFileName(Constants.videoListData.get(position).getImage()) + ".mp4");

        mgr.enqueue(request);
        addImageToGallery(url, activity);
    }

    public void downloadFile(String uRl, int position) {

        String rootDir = Environment.getExternalStorageDirectory()
                + File.separator + "FunwithStatus";
        try {

            File rootFile = new File(rootDir);
            rootFile.mkdir();
            URL url = new URL(uRl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    Constants.getFileName(uRl)+ ".mp4"));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
            Log.e("Saved....", rootFile.getPath());
        } catch (IOException e) {
            Log.e("Error....", e.toString());
        }
        addImageToGallery(rootDir, activity);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted2");
                return true;
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }

    public static void addImageToGallery(String myDir, Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
        values.put(MediaStore.MediaColumns.DATA, myDir);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return Constants.videoListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mainlayout;
        public TextView user_name, like_count;
        JZVideoPlayerStandard JZVideoPlayerStandard;
        public ImageView download, like, dislike, share, delete, whatsapp, facebook;


        public MyViewHolder(View item) {
            super(item);
            JZVideoPlayerStandard = (JZVideoPlayerStandard) item.findViewById(R.id.Thumbnail);
            user_name = (TextView) item.findViewById(R.id.user_name);
            like_count = (TextView) item.findViewById(R.id.like_count);
            mainlayout = (LinearLayout) item.findViewById(R.id.mainlayout);
            download = (ImageView) item.findViewById(R.id.download);
            like = (ImageView) item.findViewById(R.id.like);
            dislike = (ImageView) item.findViewById(R.id.dislike);
            share = (ImageView) item.findViewById(R.id.share);
            delete = (ImageView) item.findViewById(R.id.delete);
            whatsapp = (ImageView) item.findViewById(R.id.whatsapp);
            facebook = (ImageView) item.findViewById(R.id.facebook);

        }
    }
}


