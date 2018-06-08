package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.epsilon.FunwithStatus.utills.Constants;
import com.rockerhieu.emojicon.EmojiconEditText;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AddVideoActivity extends AppCompatActivity {

    Activity activity;
    VideoView iv_video;
    TextView vc_text;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 12;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    static final int VIDEO_CAPTURE = 3;
    String selectpath;
    EmojiconEditText edit_caption;
    Button button_caption_send;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        activity = this;

        iv_video = (VideoView) findViewById(R.id.iv_video);
        vc_text = (TextView) findViewById(R.id.vc_text);
        edit_caption = (EmojiconEditText) findViewById(R.id.edit_caption);
        button_caption_send = (Button) findViewById(R.id.button_caption_send);

        vc_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show();
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

        button_caption_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_caption.equals("") && edit_caption == null)
                {
                    Toast.makeText(activity, "please Describe the video", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(activity, "successfull", Toast.LENGTH_SHORT).show();
//                    uploadMultipart();
                }
            }
        });
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
//    public void uploadMultipart() {
//        //getting name for the image
//        name = edit_caption.getText().toString().trim();
//        //getting the actual path of the image
//        String path = getPath(filePath);
//        //Uploading code
//        try {
//            String uploadId = UUID.randomUUID().toString();
//            //Creating a multi part request
//            new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_URL)
//                    .addFileToUpload(path, "video") //Adding file
//                    .addParameter("subcata", subcat) //Adding text parameter to the request
//                    .addParameter("name", name) //Adding text parameter to the request
//                    .addParameter("user", user) //Adding text parameter to the request
//                    .setNotificationConfig(new UploadNotificationConfig())
//                    .setMaxRetries(2)
//                    .startUpload(); //Starting the upload
//            Toast.makeText(activity, "Add Successfully", Toast.LENGTH_SHORT).show();
//            Intent it = new Intent(activity,ImageListActivity.class);
//            it.putExtra("NAME",subcat);
//            startActivity(it);
//            finish();
//        } catch (Exception exc) {
//            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }



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

            //Go ahead with recording audio now
//            recordAudio();
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),VIDEO_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == activity.RESULT_CANCELED) {
            return;
        } if (requestCode == VIDEO_CAPTURE)
        {
            Uri selectedVideoUri = data.getData();
            selectpath = getPath(selectedVideoUri);

        }
    }
    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        cursor.moveToFirst();
        String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        int fileSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
        long duration = TimeUnit.MILLISECONDS.toSeconds(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
        return filePath;
    }
}
