package com.epsilon.FunwithStatus;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.jsonpojo.imagelike.ImageLike;
import com.epsilon.FunwithStatus.jsonpojo.videodislike.VideoDisLike;
import com.epsilon.FunwithStatus.jsonpojo.videolike.VideoLike;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayVideo extends AppCompatActivity {

    Context activity;
    LinearLayout layout_content;
    RelativeLayout mainlayout;
    ImageView download, like, dislike, share, delete, whatsapp;
    VideoView display_video;
    String name, email, video_id;
    InputStream is = null;
    Sessionmanager sessionmanager;
    Toolbar toolbar;
    String video;
    Uri uri;
    MediaController media_Controller;
    ProgressBar progressBar = null;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE  = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_video);

        activity = this;
        idMappings();
        Listener();
        sessionmanager = new Sessionmanager(this);
        video = getIntent().getStringExtra("VIDEO");
        video_id = getIntent().getStringExtra("VIDEO_ID");
        email = sessionmanager.getValue(Sessionmanager.Email);
        uri = Uri.parse(video);
        media_Controller = new MediaController(activity);
        share.setColorFilter(getResources().getColor(R.color.colorAccent));
        like.setColorFilter(getResources().getColor(R.color.colorAccent));
        dislike.setColorFilter(getResources().getColor(R.color.colorAccent));
        delete.setColorFilter(getResources().getColor(R.color.colorAccent));
        download.setColorFilter(getResources().getColor(R.color.colorAccent));

        new BackgroundAsyncTask()
                .execute(video);

        media_Controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // next button clicked
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        media_Controller.show(10000);
    }


    private void Listener() {

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein);
                final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

                download.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        download.startAnimation(animation_3);
                        boolean result = checkPermission();
                        if (result) {
//                            saveimage(uri);
                            Bitmap finalBitmap = null;
                            try {
                                finalBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            File file=null;
                            FileOutputStream outputStream;
                            try {
                                Random generator = new Random();
                                int n = 10000;
                                n = generator.nextInt(n);
                                file = new File(getCacheDir(), "MyCache"+n);

                                FileOutputStream out = new FileOutputStream(file);
                                finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                out.flush();
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveimage(uri);
                String path = Environment.getExternalStorageDirectory().toString();
                // String path = "Environment.getExternalStorageDirectory().toString()+"/myvideo";

                ArrayList<String> alPath = new ArrayList<String>();
                ArrayList<String> alName = new ArrayList<String>();

                File directory = new File(path);
                File[] file = directory.listFiles();
                for (int i = 0; i < file.length; i++) {

                    alName.add(file[i].getName());
                    alPath.add(file[i].getAbsolutePath());

                    Log.e("ALPATH",":"+alPath);
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri intentUri = Uri.parse(video);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(intentUri, "video/mp4");
                startActivity(intent);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addlike(email, video_id, video);
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddislike(email, video_id, video);
            }
        });

    }

    public void addlike(String email, String video_id, String video) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<VideoLike> countrycall = apiInterface.videolike(email, video_id, video);
        countrycall.enqueue(new Callback<VideoLike>() {
            @Override
            public void onResponse(Call<VideoLike> call, Response<VideoLike> response) {
                dialog.dismiss();
                Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<VideoLike> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public void adddislike(String email, String video_id, String video) {
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

            }
        });
    }


    public class BackgroundAsyncTask extends AsyncTask<String, Uri, Void> {
        Integer track = 0;
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("Loading, Please Wait...");
            dialog.setCancelable(true);
            dialog.show();
        }

        protected void onProgressUpdate(final Uri... uri) {

            try {

                display_video.setMediaController(media_Controller);
                media_Controller.setPrevNextListeners(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // next button clicked

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                    }
                });
                media_Controller.show(10000);

                display_video.setVideoURI(uri[0]);
                display_video.requestFocus();
                display_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    public void onPrepared(MediaPlayer arg0) {
                        display_video.start();
                        dialog.dismiss();
                    }
                });


            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Uri uri = Uri.parse(params[0]);

                publishProgress(uri);
            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }

    }

    private void idMappings() {
        display_video = (VideoView) findViewById(R.id.display_video);
        download = (ImageView) findViewById(R.id.download);
        like = (ImageView) findViewById(R.id.like);
        dislike = (ImageView) findViewById(R.id.dislike);
        share = (ImageView) findViewById(R.id.share);
        delete = (ImageView) findViewById(R.id.delete);
        whatsapp = (ImageView) findViewById(R.id.whatsapp);
        layout_content = (LinearLayout) findViewById(R.id.layout_content);
        mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted2");
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //resume tasks needing this permission
                    saveimage(uri);
                } else {

                    break;
                }
        }
    }

    private void saveimage(Uri uri) {
        DownloadManager.Request r = new DownloadManager.Request(uri);
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName");
        r.allowScanningByMediaScanner();
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(r);
        addImageToGallery(video, activity);
    }

    public static void addImageToGallery(String myDir, Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "video/*");
        values.put(MediaStore.MediaColumns.DATA, myDir);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Toast.makeText(context, "Save Video Successfully", Toast.LENGTH_SHORT).show();
    }
}
