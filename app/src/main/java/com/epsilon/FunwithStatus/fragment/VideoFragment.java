package com.epsilon.FunwithStatus.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.epsilon.FunwithStatus.Dashboard;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.adapter.VideoAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.concurrent.TimeUnit;


public class VideoFragment extends Fragment {

    Activity context;
    GridView videogrid_view;
    ImageView plus;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 12;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    static final int VIDEO_CAPTURE = 3;
    String selectpath;
    File fileGallery;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        context = getActivity();

        videogrid_view = (GridView)view.findViewById(R.id.videogrid_view);
        plus = (ImageView) view.findViewById(R.id.plus);

//        if (!Helper.isConnectingToInternet(context)) {
//            Helper.showToastMessage(context, "Please Connect Internet");
//        } else {
        VideoAdapter adapter = new VideoAdapter(context);
        videogrid_view.setAdapter(adapter);
//        }

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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

        AdView mAdView =view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });

return view;
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    android.Manifest.permission.CAMERA)
                    ) {
                ActivityCompat.requestPermissions(context,
                        new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions( context,
                        new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
    }



    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(getActivity(), "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording
        else if (ContextCompat.checkSelfPermission(getActivity(),
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

        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        } if (requestCode == VIDEO_CAPTURE)
        {
            Uri selectedVideoUri = data.getData();
            selectpath = getPath(selectedVideoUri);
            popup_video(selectedVideoUri,"video",selectpath);
        }
    }
    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        cursor.moveToFirst();
        String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        int fileSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
        long duration = TimeUnit.MILLISECONDS.toSeconds(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
        return filePath;
    }
    public void popup_video(Uri uri,String media_type,String selectpath) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.addvideo);

        final VideoView icircle_post_video = (VideoView) dialog.findViewById(R.id.icircle_post_video);
        ImageView button_chat_send = (ImageView) dialog.findViewById(R.id.button_caption_send);
        final EditText edit_caption = (EditText) dialog.findViewById(R.id.edit_caption);

        icircle_post_video.getHolder().setFixedSize(500,500);
        icircle_post_video.setVideoPath(selectpath);
        icircle_post_video.start();
        fileGallery = new File(selectpath);
        dialog.show();

        button_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_caption.getWindowToken(), 0);
                Toast.makeText(context, "Add Video Succcessfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent it = new Intent(getContext(), Dashboard.class);
                    startActivity(it);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }
}
