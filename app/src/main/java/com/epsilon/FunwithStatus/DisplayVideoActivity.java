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
import android.content.res.Configuration;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.epsilon.FunwithStatus.adapter.VideoAdapter;
import com.epsilon.FunwithStatus.fragment.VideoFragment;
import com.epsilon.FunwithStatus.jsonpojo.addlike.AddLike;
import com.epsilon.FunwithStatus.jsonpojo.deletevideo.DeleteVideo;
import com.epsilon.FunwithStatus.jsonpojo.imagelike.ImageLike;
import com.epsilon.FunwithStatus.jsonpojo.videodislike.VideoDisLike;
import com.epsilon.FunwithStatus.jsonpojo.videolike.VideoLike;
import com.epsilon.FunwithStatus.jsonpojo.videolist.VideoList;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayVideoActivity extends BaseActivity {

    Activity activity;
    LinearLayout layout_content;
    ImageView download, like, dislike, share, delete, whatsapp,facebook;
    BetterVideoPlayer bvp;
    String name, email, video_id,video;
    InputStream is = null;
    Sessionmanager sessionmanager;
    Toolbar toolbar;
    Uri uri;
    int position,category_id;
    RelativeLayout main;
    TextView user_name,likecount;
    APIInterface apiInterface;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE  = 123;
    RecyclerView rv_video;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_video);

        activity = this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionmanager = new Sessionmanager(this);
        email = sessionmanager.getValue(Sessionmanager.Email);
        Intent mIntent = getIntent();
        position = mIntent.getIntExtra("position", 0);
        category_id = Integer.valueOf(Constants.videoListData.get(position).categoryId);
        video = Constants.videoListData.get(position).file;
        video_id = String.valueOf(Constants.videoListData.get(position).id);
        String uname = Constants.videoListData.get(position).userName;
        String u_name = sessionmanager.getValue(Sessionmanager.Name);
        uri = Uri.parse(video);
        idMappings();
        Listener();
        share.setColorFilter(getResources().getColor(R.color.colorAccent));
        like.setColorFilter(getResources().getColor(R.color.colorAccent));
        dislike.setColorFilter(getResources().getColor(R.color.colorAccent));
        delete.setColorFilter(getResources().getColor(R.color.colorAccent));
        download.setColorFilter(getResources().getColor(R.color.colorAccent));

        likecount.setText(String.valueOf(Constants.videoListData.get(position).totalLikes));

        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            if (savedInstanceState == null) {
                bvp.setAutoPlay(true);
                bvp.setSource(uri);
//                bvp.setCaptions(R.raw.sub, CaptionsView.CMime.SUBRIP);
            }

            bvp.setHideControlsOnPlay(true);
//            bvp.getToolbar().inflateMenu(R.menu.menu_dizi);
//            bvp.getToolbar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    Intent it = new Intent(activity, FullscreenActivity.class);
//                    it.putExtra("uri" , uri.toString());
//                    Log.e("URI",uri.toString());
//                    startActivity(it);
//                    return false;
//                }
//            });

            bvp.enableSwipeGestures(getWindow());

            bvp.setCallback(new BetterVideoCallback() {
                @Override
                public void onStarted(BetterVideoPlayer player) {
                    //Log.i(TAG, "Started");
                }

                @Override
                public void onPaused(BetterVideoPlayer player) {
                    //Log.i(TAG, "Paused");
                }

                @Override
                public void onPreparing(BetterVideoPlayer player) {
                    //Log.i(TAG, "Preparing");
                }

                @Override
                public void onPrepared(BetterVideoPlayer player) {
                    //Log.i(TAG, "Prepared");
                }

                @Override
                public void onBuffering(int percent) {
                    //Log.i(TAG, "Buffering " + percent);
                }

                @Override
                public void onError(BetterVideoPlayer player, Exception e) {
                    //Log.i(TAG, "Error " +e.getMessage());
                }

                @Override
                public void onCompletion(BetterVideoPlayer player) {
                    //Log.i(TAG, "Completed");
                }

                @Override
                public void onToggleControls(BetterVideoPlayer player, boolean isShowing) {

                }
            });
        } else {
            Intent it = new Intent(activity, FullscreenActivity.class);
            it.putExtra("uri" , uri.toString());
            startActivity(it);
        }

        user_name.setText(uname);
        if (uname.equalsIgnoreCase(u_name))
        {
            delete.setVisibility(View.VISIBLE);
        }
        else
        {
            delete.setVisibility(View.GONE);
        }
        rv_video.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_video.setLayoutManager(mLayoutManager);

        videolist(category_id);
    }

    private void videolist(int category_id) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<VideoList> countrycall = apiInterface.videolist(category_id);
        countrycall.enqueue(new Callback<VideoList>() {
            @Override
            public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                dialog.dismiss();

                if (Constants.videoListData != null) {
                    Constants.videoListData.clear();
                }
                if (!Constants.videoListData.equals("") && Constants.videoListData != null) {
                    Constants.videoListData.addAll(response.body().data.data);
                    VideoAdapter adapter = new VideoAdapter(getActivity());
                    rv_video.setAdapter(adapter);
                    if (adapter != null)
                        adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getActivity(), "No Video Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoList> call, Throwable t) {
                dialog.dismiss();
                Helper.showToastMessage(getActivity(), "Please Connect Internet");
            }
        });
    }


    private void Listener() {

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.move);
                final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

                delete.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        delete.startAnimation(animation_3);
                        delete(video_id);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce);
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

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File extStore = Environment.getExternalStorageDirectory();
                final File myFile = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.videoListData.get(position).name + ".mp4");
                final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce);
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
                            if (myFile.exists()) {
                                Toast.makeText(getActivity(), "Already Downloaded", Toast.LENGTH_SHORT).show();
                            } else {
                                new fileDownload(getActivity(),video,position).execute();
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

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

                whatsapp.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        whatsapp.startAnimation(animation_3);
                        Log.e("URI", ":" + uri);
                        boolean result = checkPermission();
                        if (result) {
                            new Download(getActivity(), video, position).execute();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

                facebook.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        facebook.startAnimation(animation_3);
                        boolean result = checkPermission();
                        if (result) {
                            new facebookDownload(getActivity(), video, position).execute();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

                share.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        share.startAnimation(animation_3);
                        Log.e("URI", ":" + uri);
                        boolean result = checkPermission();
                        if (result) {
                            new shareDownload(getActivity(), video, position).execute();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

                like.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        like.startAnimation(animation_3);
                            final ProgressDialog dialog = new ProgressDialog(activity);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setMessage("Please Wait...");
                            dialog.show();
                            final Call<AddLike> countrycall = apiInterface.addlikepojo(Constants.videoListData.get(position).id, "like");
                            countrycall.enqueue(new Callback<AddLike>() {
                                @Override
                                public void onResponse(Call<AddLike> call, Response<AddLike> response) {
                                    dialog.dismiss();
                                    Toast.makeText(activity, response.body().msg, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<AddLike> call, Throwable t) {
                                    dialog.dismiss();

                                }
                            });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        dislike.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(activity);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("Please Wait...");
                dialog.show();
                final Call<AddLike> countrycall = apiInterface.addlikepojo(Constants.videoListData.get(position).id, "unlike");
                countrycall.enqueue(new Callback<AddLike>() {
                    @Override
                    public void onResponse(Call<AddLike> call, Response<AddLike> response) {
                        dialog.dismiss();
                        Toast.makeText(activity, response.body().msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<AddLike> call, Throwable t) {
                        dialog.dismiss();

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
            File myFile = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.getFileName(Constants.videoListData.get(position).file) + ".mp4");

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
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<DeleteVideo> countrycall = apiInterface.deletevideo(id);
        countrycall.enqueue(new Callback<DeleteVideo>() {
            @Override
            public void onResponse(Call<DeleteVideo> call, Response<DeleteVideo> response) {
                dialog.dismiss();
                if (response.body().getStatus().equals("Succ")) {
                    bvp.release();
                    main.setVisibility(View.GONE);
                    Fragment fragment = new VideoFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.containers, fragment);
                    transaction.commit();
                    Toast.makeText(getActivity(), "Delete Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteVideo> call, Throwable t) {
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
            File myFile = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.getFileName(Constants.videoListData.get(position).file) + ".mp4");

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
                File file = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.getFileName(Constants.videoListData.get(position).file) + ".mp4");
                if (file.isFile()) {
                    MediaScannerConnection.scanFile(getActivity(),
                            new String[]{file.toString()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(final String path,
                                                            final Uri picUri) {
                                    try {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Intent videoshare = new Intent(Intent.ACTION_SEND);
                                                    videoshare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                                                    videoshare.setType("text/plain");
                                                    videoshare.putExtra(Intent.EXTRA_STREAM, picUri);
                                                    videoshare.setType("video/*");
                                                    videoshare.setPackage("com.facebook.katana");
                                                    videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    getActivity().startActivity(videoshare);

                                                } catch (Exception e) {
                                                    Log.e("Error....", e.toString());
                                                    e.printStackTrace();

                                                    Intent videoshare = new Intent(Intent.ACTION_SEND);
                                                    videoshare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                                                    videoshare.setType("text/plain");
                                                    videoshare.putExtra(Intent.EXTRA_STREAM, picUri);
                                                    videoshare.setType("video/*");
                                                    videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    getActivity().startActivity(Intent.createChooser(videoshare, "Share video by..."));
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
    public class shareDownload extends AsyncTask<Void, Void, String> {
        ProgressDialog mProgressDialog;

        Context context;
        String video;
        int position;

        public shareDownload(Context context, String video, int position) {
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
            File myFile = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.getFileName(Constants.videoListData.get(position).file) + ".mp4");

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
                sharevideo(position);
            }
        }
    }

    public void sharevideo(int position) {
        File extStore = Environment.getExternalStorageDirectory();
        File file = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.getFileName(Constants.videoListData.get(position).file) + ".mp4");
        if (file.exists()) {
            Log.e("downloadFile", "file:" + file.getAbsolutePath());
            Uri uri = Uri.parse(file.getPath());
            try {
                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                videoshare.setType("text/plain");
                videoshare.putExtra(Intent.EXTRA_STREAM, uri);
                videoshare.setType("video/*");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getActivity().startActivity(videoshare);
            } catch (Exception e) {
                Log.e("Error....", e.toString());
                e.printStackTrace();

                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                videoshare.setType("text/plain");
                videoshare.putExtra(Intent.EXTRA_STREAM, uri);
                videoshare.setType("video/*");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getActivity().startActivity(Intent.createChooser(videoshare, "Share video by..."));

            }

        }
    }


    public void sharewhatupp(int position) {

        File extStore = Environment.getExternalStorageDirectory();
        File file = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.getFileName(Constants.videoListData.get(position).file)+ ".mp4");
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
                getActivity().startActivity(videoshare);
            } catch (Exception e) {
                Log.e("Error....", e.toString());
                e.printStackTrace();
                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                videoshare.setType("text/plain");
                videoshare.putExtra(Intent.EXTRA_STREAM, uri);
                videoshare.setType("video/*");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getActivity().startActivity(Intent.createChooser(videoshare, "Share video by..."));
            }
        }
    }

    public class fileDownload extends AsyncTask<Void, Void, String> {
        ProgressDialog mProgressDialog;

        Context context;
        String video;
        int position;

        public fileDownload(Context context, String video, int position) {
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
            File myFile = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.getFileName(Constants.videoListData.get(position).file) + ".mp4");

            if (!myFile.exists()) {
                downloadonly(video, position);
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
                Toast.makeText(context, "Download Successfully", Toast.LENGTH_SHORT).show();


            }
        }
    }

    public void downloadonly(String url, int position) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Download");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Download")
                .setDestinationInExternalPublicDir("/FunwithStatus", Constants.getFileName(Constants.videoListData.get(position).file) + ".mp4");

        mgr.enqueue(request);
        addImageToGallery(url, getActivity());
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
        addImageToGallery(rootDir, getActivity());
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted2");
                return true;
            } else {
                ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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

    private void idMappings() {
        download = (ImageView) findViewById(R.id.download);
        bvp = (BetterVideoPlayer) findViewById(R.id.bvp);
        like = (ImageView) findViewById(R.id.like);
        dislike = (ImageView) findViewById(R.id.dislike);
        share = (ImageView) findViewById(R.id.share);
        delete = (ImageView) findViewById(R.id.delete);
        whatsapp = (ImageView) findViewById(R.id.whatsapp);
        facebook = (ImageView) findViewById(R.id.facebook);
        user_name = (TextView) findViewById(R.id.user_name);
        likecount = (TextView) findViewById(R.id.likecount);
        main = (RelativeLayout) findViewById(R.id.main);
        rv_video = (RecyclerView)findViewById(R.id.rv_video);

    }

    @Override
    public void onBackPressed() {
        bvp.release();
        Intent it = new Intent(activity,VideoListActivity.class);
        it.putExtra("ID",category_id);
        startActivity(it);
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        bvp.pause();
    }

}

