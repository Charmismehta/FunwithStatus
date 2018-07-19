package com.epsilon.FunwithStatus.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.epsilon.FunwithStatus.FullscreenActivity;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.SingleUploadBroadcastReceiver;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WhatsAppMediaAdapter extends RecyclerView.Adapter<WhatsAppMediaAdapter.MyViewHolder> implements SingleUploadBroadcastReceiver.Delegate {
    private List<GridViewItem> data = new ArrayList<>();
    String mData = "";
    Activity context;
    Sessionmanager sessionmanager;

    private EventListener mEventListener;

    public interface EventListener {
        void onItemImageClicked(int position, String itemName);
        void onItemVideoClicked(int position);
        void onDownLoadClicked(int position, View view);
        void onShareClicked(int position, View view);
        void onWhatsappClicked(int position, View view);
        void onFacebookClicked(int position, View view, String itemName);
        void onUploadClicked(View view, String itemName);
    }

    public WhatsAppMediaAdapter(Activity context) {
        this.context = context;
        sessionmanager = new Sessionmanager(context);
    }

    public void addAll(List<GridViewItem> mData) {
        clear();
        data.addAll(mData);
        Log.e("whatsappStatus-->>", data.toString());
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    //
    public void remove(int position, int id) {
        mData = mData + "," + id;
        data.remove(position);
        notifyDataSetChanged();
    }

    public GridViewItem getItem(int pos1) {
        return data.get(pos1);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_whatsapp_media, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position1) {

        final GridViewItem item = data.get(position1);

            final String image = item.getImage();
            String filenameArray[] = image.split("\\.");
            String fileType = filenameArray[filenameArray.length - 1];

        String user = sessionmanager.getValue(Sessionmanager.Name);

        holder.imgLike.setColorFilter(context.getResources().getColor(R.color.colorAccent));
        holder.imgShare.setColorFilter(context.getResources().getColor(R.color.colorAccent));
        holder.imgDownload.setColorFilter(context.getResources().getColor(R.color.colorAccent));

            if (fileType.equalsIgnoreCase("jpg")) {
                Log.e("whatsappStatus-->>", "IMAGE - " + position1);
                holder.imgWhatsAppImage.setVisibility(View.VISIBLE);
                holder.bvp.setVisibility(View.GONE);
                holder.btn.setVisibility(View.GONE);
                Glide.with(context).load(image).thumbnail(Glide.with(context).load(R.drawable.load)).into(holder.imgWhatsAppImage);
            } else if (fileType.equalsIgnoreCase("mp4")) {
                Log.e("whatsappStatus-->>", "Video - " + position1);
                holder.imgWhatsAppImage.setVisibility(View.GONE);
                holder.btn.setVisibility(View.VISIBLE);
                holder.bvp.setVisibility(View.VISIBLE);
                RequestOptions options = new RequestOptions().frame(10000);
                Glide.with(context).asBitmap()
                        .load(image)
                        .apply(options)
                        .into(holder.bvp);
            } else {
                Toast.makeText(context, "no image", Toast.LENGTH_SHORT).show();
            }

            holder.tvUserName.setText(user);

        holder.imgWhatsAppImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventListener != null) {
                    mEventListener.onItemImageClicked(position1, item.getImage());
                }
            }
        });

        holder.bvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventListener != null) {
                    mEventListener.onItemVideoClicked(position1);
                }
            }
        });

        holder.imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventListener != null) {
                    mEventListener.onDownLoadClicked(position1, v);
                }
            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventListener != null) {
                    mEventListener.onShareClicked(position1, v);
                }
            }
        });

        holder.imgWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventListener != null) {
                    mEventListener.onWhatsappClicked(position1, v);
                }
            }
        });

        holder.imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventListener != null) {
                    mEventListener.onFacebookClicked(position1, v, item.getImage());
                }
            }
        });

        holder.tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventListener != null) {
                    mEventListener.onUploadClicked(v, item.getImage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_imageview)
        public ImageView imgWhatsAppImage;
        @BindView(R.id.iv_like)
        public ImageView imgLike;
        @BindView(R.id.tv_likecount)
        public TextView tvLikeCount;
        @BindView(R.id.tvUserName)
        public TextView tvUserName;
        @BindView(R.id.text)
        public TextView tvUpload;
        @BindView(R.id.iv_download)
        public ImageView imgDownload;
        @BindView(R.id.iv_share)
        public ImageView imgShare;
        @BindView(R.id.iv_whatsapp)
        public ImageView imgWhatsapp;
        @BindView(R.id.iv_facebook)
        public ImageView imgFacebook;
        @BindView(R.id.vv_videoview)
        public ImageView bvp;
        @BindView(R.id.rr)
        public RelativeLayout rr;
        @BindView(R.id.btn)
        public Button btn;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    public void sharevideo(GridViewItem data) {
        String path = data.getImage();//it contain your path of image..im using a temp string..
        String filename = path.substring(path.lastIndexOf("/") + 1);
        File myFile = new File(Environment.getExternalStorageDirectory() + "/" + "FunwithStatus" + "/" + filename);
        if (myFile.exists()) {
            Log.e("downloadFile", "file:" + myFile.getAbsolutePath());
            Uri uri = Uri.parse(myFile.getPath());
            try {
                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                videoshare.setType("text/plain");
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

    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    public void uploadMultipart(View view, String selectpath) {
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
                    .addParameter("filename", "Whatsapp Status") //Adding text parameter to the request
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

    }

    @Override
    public void onCancelled() {

    }
}
