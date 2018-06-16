package com.epsilon.FunwithStatus;

import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.epsilon.FunwithStatus.fragment.ImageFragment;
import com.epsilon.FunwithStatus.fragment.MainFragment;
import com.epsilon.FunwithStatus.fragment.VideoFragment;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.rockerhieu.emojicon.EmojiconEditText;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AddVideoActivity extends AppCompatActivity {

    Activity activity;
    VideoView iv_video;
    ImageView imageview;
    LinearLayout main;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 12;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    static final int VIDEO_CAPTURE = 3;
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    String selectpath,user;
    EmojiconEditText edit_caption;
    Button button_caption_send,vc_camera,vc_album;
    String name;
    Sessionmanager sessionmanager;
    File fileGallery;
    Fragment fragment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        activity = this;
        sessionmanager = new Sessionmanager(this);
        iv_video = (VideoView) findViewById(R.id.iv_video);
        imageview = (ImageView) findViewById(R.id.imageview);
        vc_album = (Button) findViewById(R.id.vc_album);
        edit_caption = (EmojiconEditText) findViewById(R.id.edit_caption);
        button_caption_send = (Button) findViewById(R.id.button_caption_send);
        main = (LinearLayout) findViewById(R.id.main);
        user = sessionmanager.getValue(Sessionmanager.Name);

        imageview.setVisibility(View.VISIBLE);
        iv_video.setVisibility(View.GONE);
        imageview.setImageResource(R.drawable.videocamera);

        button_caption_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMultipart();
                main.setVisibility(View.GONE);
                Fragment fragment = new VideoFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.containers, fragment);
                transaction.commit();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakeVideoIntent();

            } else {
                //Request Location Permission
                checkCameraPermission();
                requestAudioPermissions();
            }
        } else {
            dispatchTakeVideoIntent();
        }


        vc_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        dispatchTakeVideoIntent();

                    } else {
                        //Request Location Permission
                        checkCameraPermission();
                        requestAudioPermissions();
                    }
                } else {
                    dispatchTakeVideoIntent();
                }
            }
        });

//        vc_camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                            && ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                            && ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                        TakeVideoIntent();
//
//                    } else {
//                        //Request Location Permission
//                        checkCameraPermission();
//                        requestAudioPermissions();
//                    }
//                } else {
//                    TakeVideoIntent();
//                }
//            }
//        });


//        button_caption_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ( selectpath.toString().endsWith(".mp4")) {
//                    uploadMultipart();
//
//                } else {
//                    Toast.makeText(activity, "Please select mp4 video", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//    private void TakeVideoIntent() {
//        Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
//        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
//    }
    }
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.CAMERA)
                    ) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions( activity,
                        new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
    }
    public void uploadMultipart() {
        //getting name for the image
        name = edit_caption.getText().toString().trim();
        //getting the actual path of the image
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_VIDEO)
                    .addFileToUpload(selectpath, "image") //Adding file
                    .addParameter("filename", name) //Adding text parameter to the request
                    .addParameter("user", user) //Adding text parameter to the request
                    .addParameter("name", user) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            Toast.makeText(activity, "Add video Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception exc) {

        }
    }



    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(activity, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording
        else if (ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

        }
    }

    private void dispatchTakeVideoIntent() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),VIDEO_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == VIDEO_CAPTURE)
        {
            iv_video.setVisibility(View.VISIBLE);
            imageview.setVisibility(View.GONE);
            Uri selectedVideoUri = data.getData();
            selectpath = getPath(selectedVideoUri);
            iv_video.setVideoPath(selectpath);
            iv_video.setVideoURI(selectedVideoUri);
            iv_video.setMediaController(new MediaController(this));
            iv_video.start();
        }
//        else if (requestCode == REQUEST_VIDEO_CAPTURE)
//        {
//            iv_video.setVisibility(View.VISIBLE);
//            imageview.setVisibility(View.GONE);
//            Uri selectedVideoUri = data.getData();
//            selectpath = getPath(selectedVideoUri);
//            iv_video.setVideoURI(selectedVideoUri);
//            iv_video.setMediaController(new MediaController(this));
//            iv_video.start();
//            fileGallery = new File(selectpath);
//            }
    }


    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        cursor.moveToFirst();
        String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        int fileSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
        long duration = TimeUnit.MILLISECONDS.toSeconds(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
        return filePath;
    }
}
