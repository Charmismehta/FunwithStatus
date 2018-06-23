package com.epsilon.FunwithStatus.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.epsilon.FunwithStatus.ImageSlider;
import com.epsilon.FunwithStatus.LoginPage;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.SingleUploadBroadcastReceiver;
import com.epsilon.FunwithStatus.fragment.VideoFragment;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.UUID;

import cn.jzvd.JZVideoPlayerStandard;

public class MyVideoGridAdapter extends BaseAdapter implements SingleUploadBroadcastReceiver.Delegate{

    Activity context;
    LayoutInflater inflater;
    Sessionmanager sessionmanager;



    public MyVideoGridAdapter(Activity context) {

        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sessionmanager = new Sessionmanager(context);

    }


    @Override
    public int getCount() {
        return Constants.videoitems.size();
    }


    @Override
    public Object getItem(int position) {
        return Constants.videoitems.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String email = sessionmanager.getValue(Sessionmanager.Email);
        final String video = Constants.videoitems.get(position).getImage();
        final Uri uri = Uri.parse(video);
        final String u_name = sessionmanager.getValue(Sessionmanager.Name);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.whatsappvideo_item, null);
        }

        JZVideoPlayerStandard JZVideoPlayerStandard = (JZVideoPlayerStandard) convertView.findViewById(R.id.Thumbnail);
        TextView user_name = (TextView) convertView.findViewById(R.id.user_name);
        TextView Upload = (TextView) convertView.findViewById(R.id.upload);
        LinearLayout mainlayout = (LinearLayout) convertView.findViewById(R.id.mainlayout);
        final ImageView download = (ImageView) convertView.findViewById(R.id.download);
        final ImageView share = (ImageView) convertView.findViewById(R.id.share);
        final ImageView whatsapp = (ImageView) convertView.findViewById(R.id.whatsapp);
        final ImageView facebook = (ImageView) convertView.findViewById(R.id.facebook);

        user_name.setText(u_name);
        String filenameArray[] = video.split("\\.");
        String extension = filenameArray[filenameArray.length - 1];

        if (extension.equalsIgnoreCase("mp4")) {
            RequestOptions options = new RequestOptions().frame(10000);
//        holder.JZVideoPlayerStandard.releaseAllVideos();
            JZVideoPlayerStandard.setUp(video,
                    JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                    "");
            Glide.with(context).asBitmap()
                    .load(video)
                    .apply(options)
                    .into(JZVideoPlayerStandard.thumbImageView);
        } else {
            Toast.makeText(context, "no image", Toast.LENGTH_SHORT).show();
        }

        share.setColorFilter(context.getResources().getColor(R.color.colorAccent));
        download.setColorFilter(context.getResources().getColor(R.color.colorAccent));

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Sessionmanager.getPreferenceBoolean(context, Constants.IS_LOGIN, false)) {
                    uploadMultipart(v,video);
                } else {
                    Intent mainIntent = new Intent(context, LoginPage.class);
                    context.startActivity(mainIntent);
                    LayoutInflater inflater = context.getLayoutInflater();
                    View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup)v.findViewById(R.id.llCustom));
                    Toast toast = new Toast(context.getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(toastLayout);
                    toast.show();
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sourceLocation = Constants.videoitems.get(position).getImage();
                final String targetLocation =Environment.getExternalStorageDirectory() + "/" + "FunwithStatus"+"/" ;
                final Animation animation_2 = AnimationUtils.loadAnimation(context.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(context.getBaseContext(), R.anim.abc_fade_out);

                share.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        share.startAnimation(animation_3);
                        boolean result = checkPermission();
                        if (result) {
                            new Download(context, video, position).execute();
                            String path = Constants.videoitems.get(position).getImage();//it contain your path of image..im using a temp string..
                            String filename = path.substring(path.lastIndexOf("/") + 1);
                            File myFile = new File(Environment.getExternalStorageDirectory() + "/" + "FunwithStatus" + "/" + filename);
                            Log.e("FILE PATH", filename);

                            if (myFile.exists()) {
                                sharevideo(position);
                            } else {
                                copyFileOrDirectory(sourceLocation, targetLocation);
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
                final String sourceLocation = Constants.videoitems.get(position).getImage();
                final String targetLocation =Environment.getExternalStorageDirectory() + "/" + "FunwithStatus"+"/" ;
                final Animation animation_2 = AnimationUtils.loadAnimation(context.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(context.getBaseContext(), R.anim.abc_fade_out);

                whatsapp.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        whatsapp.startAnimation(animation_3);
                        boolean result = checkPermission();
                        if (result) {
                            new Download(context, video, position).execute();
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
                final Animation animation_2 = AnimationUtils.loadAnimation(context.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(context.getBaseContext(), R.anim.abc_fade_out);

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
                            new facebookDownload(context, video, position).execute();
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
                final String sourceLocation = Constants.videoitems.get(position).getImage();
                final String targetLocation =Environment.getExternalStorageDirectory() + "/" + "FunwithStatus"+"/" ;
                final Animation animation_2 = AnimationUtils.loadAnimation(context.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(context, R.anim.abc_fade_out);

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
                            copyFileOrDirectory(sourceLocation,targetLocation);
                        }
                    }


                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        return convertView;
    }

    @Override
    public void onProgress(int progress) {
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {
    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        Toast.makeText(context, "Upload successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelled() {

    }

    public void uploadMultipart(View view,String selectpath) {
        Helper.hideSoftKeyboard(context);
        String user = sessionmanager.getValue(Sessionmanager.Name);
        //getting name for the image
        //getting the actual path of the image
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);
            //Creating a multi part request
            new MultipartUploadRequest(context, uploadId, Constants.UPLOAD_VIDEO)
                    .addFileToUpload(selectpath, "image") //Adding file
                    .addParameter("filename", "Whatsapp") //Adding text parameter to the request
                    .addParameter("user", user) //Adding text parameter to the request
                    .addParameter("name", user) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            showToast(view);
        } catch (Exception exc) {

        }
    }

    public void showToast(View view) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        // Set the toast and duration
        int toastDurationInMilliSeconds = 20000;
        dialog.show();
//        custom_layout.setVisibility(View.VISIBLE);

//        mToastToShow = Toast.makeText(this, "Please Wait for a Moment.", Toast.LENGTH_LONG);

        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                dialog.show();
//                custom_layout.setVisibility(View.VISIBLE);
//                mToastToShow.show();
            }

            public void onFinish() {
                dialog.dismiss();
//                custom_layout.setVisibility(View.GONE);

//                mToastToShow.cancel();
            }
        };

        // Show the toast and starts the countdown
        dialog.show();
        toastCountDown.start();
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
            final String sourceLocation = Constants.videoitems.get(position).getImage();
            final String targetLocation =Environment.getExternalStorageDirectory() + "/" + "FunwithStatus"+"/" ;
            String path=Constants.videoitems.get(position).getImage();//it contain your path of image..im using a temp string..
            String filename=path.substring(path.lastIndexOf("/")+1);
            File myFile = new File(  Environment.getExternalStorageDirectory() + "/" + "FunwithStatus"+"/" +filename);
            if (!myFile.exists()) {
                copyFileOrDirectory(sourceLocation,targetLocation);

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

    public void shareFacebook(int position) {
        {
            try {
                String path=Constants.videoitems.get(position).getImage();//it contain your path of image..im using a temp string..
                String filename=path.substring(path.lastIndexOf("/")+1);
                File myFile = new File(  Environment.getExternalStorageDirectory() + "/" + "FunwithStatus"+"/" +filename);
                if (myFile.isFile()) {
                    MediaScannerConnection.scanFile(context,
                            new String[]{myFile.toString()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(final String path,
                                                            final Uri picUri) {
                                    try {
                                        context.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Intent videoshare = new Intent(Intent.ACTION_SEND);
                                                    videoshare.putExtra(Intent.EXTRA_STREAM, picUri);
                                                    videoshare.setType("video/*");
                                                    videoshare.setPackage("com.facebook.katana");
                                                    videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    context.startActivity(videoshare);

                                                } catch (Exception e) {
                                                    Log.e("Error....", e.toString());
                                                    e.printStackTrace();

                                                    Intent videoshare = new Intent(Intent.ACTION_SEND);
                                                    videoshare.putExtra(Intent.EXTRA_STREAM, picUri);
                                                    videoshare.setType("video/*");
                                                    videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    context.startActivity(Intent.createChooser(videoshare, "Share video by..."));
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
        String path=Constants.videoitems.get(position).getImage();//it contain your path of image..im using a temp string..
        String filename=path.substring(path.lastIndexOf("/")+1);
        File myFile = new File(  Environment.getExternalStorageDirectory() + "/" + "FunwithStatus"+"/" +filename);
        if (myFile.exists()) {
            Log.e("downloadFile", "file:" + myFile.getAbsolutePath());
            Uri uri = Uri.parse(myFile.getPath());
            try {

                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.putExtra(Intent.EXTRA_STREAM, uri);
                videoshare.setType("video/*");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(videoshare);
            } catch (Exception e) {
                Log.e("Error....", e.toString());
                e.printStackTrace();

                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                videoshare.setType("text/plain");
                videoshare.putExtra(Intent.EXTRA_STREAM, uri);
                videoshare.setType("video/*");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(videoshare, "Share video by..."));

            }

        }
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
            final String sourceLocation = Constants.videoitems.get(position).getImage();
            final String targetLocation =Environment.getExternalStorageDirectory() + "/" + "FunwithStatus"+"/" ;
            String path=Constants.videoitems.get(position).getImage();//it contain your path of image..im using a temp string..
            String filename=path.substring(path.lastIndexOf("/")+1);
            File myFile = new File(  Environment.getExternalStorageDirectory() + "/" + "FunwithStatus"+"/" +filename);

            if (!myFile.exists()) {
                copyFileOrDirectory(sourceLocation,targetLocation);
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

    public void sharewhatupp(int position) {

        String path=Constants.videoitems.get(position).getImage();//it contain your path of image..im using a temp string..
        String filename=path.substring(path.lastIndexOf("/")+1);
        File myFile = new File(  Environment.getExternalStorageDirectory() + "/" + "FunwithStatus"+"/" +filename);
        if (myFile.exists()) {
            Log.e("downloadFile", "file:" + myFile.getAbsolutePath());
            Uri uri = Uri.parse(myFile.getPath());
            try {

                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                videoshare.setType("text/plain");
                videoshare.putExtra(Intent.EXTRA_STREAM, uri);
                videoshare.setType("video/*");
                videoshare.setPackage("com.whatsapp");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(videoshare);
            } catch (Exception e) {
                Log.e("Error....", e.toString());
                e.printStackTrace();
                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                videoshare.setType("text/plain");
                videoshare.putExtra(Intent.EXTRA_STREAM, uri);
                videoshare.setType("video/*");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(videoshare, "Share video by..."));
            }
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted2");
                return true;
            } else {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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
        values.put(MediaStore.Images.Media.MIME_TYPE, "video/*");
        values.put(MediaStore.MediaColumns.DATA, myDir);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

}
