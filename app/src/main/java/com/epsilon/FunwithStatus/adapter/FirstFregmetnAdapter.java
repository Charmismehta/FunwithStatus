package com.epsilon.FunwithStatus.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
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
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.ImageSliderActivity;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.TextSliderActivity;
import com.epsilon.FunwithStatus.jsonpojo.addlike.AddLike;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.vdurmont.emoji.EmojiParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.media.CamcorderProfile.get;

public class FirstFregmetnAdapter extends RecyclerView.Adapter<FirstFregmetnAdapter.MyViewHolder> {
    Activity activity;
    private LayoutInflater inflater;
    public Resources res;
    ProgressDialog mProgressDialog;
    APIInterface apiInterface;
    int count=0;

    public FirstFregmetnAdapter(Activity a) {
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.firstfragment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.iv_download.setColorFilter(activity.getResources().getColor(R.color.colorAccent));
        holder.iv_share.setColorFilter(activity.getResources().getColor(R.color.colorAccent));
        holder.iv_like.setColorFilter(activity.getResources().getColor(R.color.colorAccent));

        if (Constants.homedata.get(position).type.equalsIgnoreCase("image")) {
            final String imgURL = Constants.homedata.get(position).image;
            holder.iv_imageview.setVisibility(View.VISIBLE);
            holder.tv_textview.setVisibility(View.GONE);
            holder.vv_videoview.setVisibility(View.GONE);
            Glide
                    .with(activity)
                    .load(imgURL)
                    .thumbnail(1.0f)
                    .into(holder.iv_imageview);
//            Glide.with(activity).load(Constants.homedata.get(position).getImage()).into(holder.iv_imageview);
            holder.tv_username.setText(Constants.homedata.get(position).userName);
            holder.tv_caption.setText("#" + Constants.homedata.get(position).categoryName);
            holder.tv_view.setText(String.valueOf(Constants.homedata.get(position).totalViews));
            holder.tv_likecount.setText(String.valueOf(Constants.homedata.get(position).totalLikes));

            holder.iv_imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(activity, ImageSliderActivity.class);
                    it.putExtra("position",position);
                    it.putExtra("ID",Constants.homedata.get(position).categoryId);
                    it.putExtra("NAME","HOME");
                    it.putExtra("pic", Constants.homedata.get(position).image);
                    activity.startActivity(it);
                }
            });

            holder.iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result = checkPermission();
                    if (result) {
                        new ShareImage().execute(imgURL);
                    }
                }
            });

            holder.iv_whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result = checkPermission();
                    if (result) {
                        new sharewhatsappImage().execute(imgURL);
                    }
                }
            });

            holder.iv_facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result = checkPermission();
                    if (result) {
                        new sharefacebookImage().execute(imgURL);
                    }
                }
            });
            holder.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addlike(Constants.homedata.get(position).id,"like");
                    count = Constants.homedata.get(position).totalLikes + 1;
                    holder.tv_likecount.setText(String.valueOf(count));
                }
            });

            holder.iv_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result = checkPermission();
                    if (result) {
                        final Uri uri = Uri.parse(imgURL);
                        DownloadManager.Request r = new DownloadManager.Request(uri);

                        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName");

                        r.allowScanningByMediaScanner();

                        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        DownloadManager dm = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(r);
                        addImageToGallery(imgURL, activity);

                    }
                }
            });


        } else if (Constants.homedata.get(position).type.equalsIgnoreCase("video")) {
            final String video = Constants.homedata.get(position).file;
            holder.iv_imageview.setVisibility(View.VISIBLE);
            holder.tv_textview.setVisibility(View.GONE);
            holder.vv_videoview.setVisibility(View.GONE);
            Glide.with(activity).load(Constants.homedata.get(position).image).into(holder.iv_imageview);
            holder.tv_username.setText(Constants.homedata.get(position).userName);
            holder.tv_caption.setText("#" + Constants.homedata.get(position).categoryName);
            holder.tv_view.setText(String.valueOf(Constants.homedata.get(position).totalViews));
            holder.tv_likecount.setText(String.valueOf(Constants.homedata.get(position).totalLikes));
            holder.iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result = checkPermission();
                    if (result) {
                        new shareDownload(activity, video, position).execute();
                    }
                }
            });

            holder.iv_whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result = checkPermission();
                    if (result) {
                        new Download(activity, video, position).execute();
                    }
                }
            });

            holder.iv_facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result = checkPermission();
                    if (result) {
                        new facebookDownload(activity, video, position).execute();
                    }
                }
            });

            holder.iv_download.setOnClickListener(new View.OnClickListener() {
                File extStore = Environment.getExternalStorageDirectory();
                final File myFile = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.homedata.get(position).file + ".mp4");

                @Override
                public void onClick(View v) {
                    boolean result = checkPermission();
                    if (result) {
                        if (myFile.exists()) {
                            Toast.makeText(activity, "Already Downloaded", Toast.LENGTH_SHORT).show();
                        } else {
                            new fileDownload(activity, video, position).execute();
//
                        }
                    }
                }
            });

            holder.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addlike(Constants.homedata.get(position).id,"like");
                    count = Constants.homedata.get(position).totalLikes + 1;
                    holder.tv_likecount.setText(String.valueOf(count));
                }
            });


        } else if (Constants.homedata.get(position).type.equalsIgnoreCase("status")) {
            holder.tv_textview.setVisibility(View.VISIBLE);
            holder.iv_imageview.setVisibility(View.GONE);
            holder.vv_videoview.setVisibility(View.GONE);
            String str = Constants.homedata.get(position).text.toString();
            final String result = EmojiParser.parseToUnicode(str);
            holder.tv_textview.setText(result);
            holder.tv_username.setText(Constants.homedata.get(position).userName);
            holder.tv_caption.setText("#" + Constants.homedata.get(position).categoryName);
            holder.tv_view.setText(String.valueOf(Constants.homedata.get(position).totalViews));
            holder.tv_likecount.setText(String.valueOf(Constants.homedata.get(position).totalLikes));

            holder.iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_STREAM, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                    share.putExtra(Intent.EXTRA_TEXT, holder.tv_textview.getText().toString());
                    activity.startActivity(Intent.createChooser(share, "Fun With Status"));
                }
            });

            holder.tv_textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(activity, TextSliderActivity.class);
                    it.putExtra("position", position);
                    it.putExtra("NAME", "HOME");
                    it.putExtra("U_NAME", Constants.homedata.get(position).userName);
                    it.putExtra("ID",Constants.homedata.get(position).categoryId);
                    activity.startActivity(it);
                }
            });

            holder.iv_whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result = checkPermission();
                    if (result) {
                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, holder.tv_textview.getText().toString());
                        try {
                            activity.startActivity(whatsappIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(activity, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            holder.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addlike(Constants.homedata.get(position).id,"like");
                    count = Constants.homedata.get(position).totalLikes + 1;
                    holder.tv_likecount.setText(String.valueOf(count));
                }
            });

            holder.iv_facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result = checkPermission();
                    if (result) {
                        ClipboardManager cm = (ClipboardManager) activity.getSystemService(activity.CLIPBOARD_SERVICE);
                        cm.setText(holder.tv_textview.getText());
                        Toast.makeText(activity, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                        String url = holder.tv_textview.getText().toString();
                        boolean facebookAppFound = false;
                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
                        PackageManager pm = activity.getPackageManager();
                        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
                        for (final ResolveInfo app : activityList) {
                            if ((app.activityInfo.packageName).contains("com.facebook.katana")) {
                                final ActivityInfo activityInfo = app.activityInfo;
                                final ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                                shareIntent.setComponent(name);
                                facebookAppFound = true;
                                Toast.makeText(activity, "Paste your text ", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        if (!facebookAppFound) {
                            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + url;
                            shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                        }
                        activity.startActivity(shareIntent);
                    }
                }
            });

            holder.iv_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) activity.getSystemService(activity.CLIPBOARD_SERVICE);
                    cm.setText(holder.tv_textview.getText());
                    Toast.makeText(activity, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
        }

//        holder.dot.setOnClickListener(new View.OnClickListener()
//
//        {
//            @Override
//            public void onClick(View v) {
//                final PopupMenu popup = new PopupMenu(activity, v);
//                popup.getMenuInflater().inflate(R.menu.menuitem, popup.getMenu());
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.delete_post:
//                                Toast.makeText(activity, "delete Post", Toast.LENGTH_SHORT).show();
//                                break;
//                            case R.id.edit_post:
//                                Toast.makeText(activity, "edit Post", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                        return true;
//                    }
//                });
//                popup.show(); //showing popup menu
//            }
//        });
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return Constants.homedata.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_download, iv_facebook, iv_whatsapp, iv_share, iv_like, iv_imageview, dot;
        TextView tv_username, tv_view, tv_caption, tv_likecount;
        VideoView vv_videoview;
        EmojiconTextView tv_textview;

        public MyViewHolder(View item) {
            super(item);
            iv_download = (ImageView) item.findViewById(R.id.iv_download);
            iv_facebook = (ImageView) item.findViewById(R.id.iv_facebook);
            iv_whatsapp = (ImageView) item.findViewById(R.id.iv_whatsapp);
            iv_share = (ImageView) item.findViewById(R.id.iv_share);
            iv_like = (ImageView) item.findViewById(R.id.iv_like);
            iv_imageview = (ImageView) item.findViewById(R.id.iv_imageview);
//            dot = (ImageView) item.findViewById(R.id.dot);
            vv_videoview = (VideoView) item.findViewById(R.id.vv_videoview);
            tv_username = (TextView) item.findViewById(R.id.tv_username);
            tv_view = (TextView) item.findViewById(R.id.tv_view);
            tv_caption = (TextView) item.findViewById(R.id.tv_caption);
            tv_textview = (EmojiconTextView) item.findViewById(R.id.tv_textview);
            tv_likecount = (TextView) item.findViewById(R.id.tv_likecount);
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

    public void addlike(int id, String type) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<AddLike> countrycall = apiInterface.addlikepojo(id, type);
        countrycall.enqueue(new Callback<AddLike>() {
            @Override
            public void onResponse(Call<AddLike> call, Response<AddLike> response) {
                dialog.dismiss();
                Toast.makeText(activity, "Like Successfully", Toast.LENGTH_SHORT).show();
//                Toast.makeText(activity, response.body().msg.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AddLike> call, Throwable t) {
                dialog.dismiss();

            }
        });
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
                .setDestinationInExternalPublicDir("/FunwithStatus", Constants.getFileName(Constants.videoListData.get(position).file) + ".mp4");

        mgr.enqueue(request);
        addImageToGallery(url, activity);
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

    public void shareFacebook(int position) {
        {
            try {
                File extStore = Environment.getExternalStorageDirectory();
                File file = new File(extStore.getAbsolutePath(), "/" + "/FunwithStatus" + "/" + Constants.getFileName(Constants.videoListData.get(position).file) + ".mp4");
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
                                                    videoshare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                                                    videoshare.setType("text/plain");
                                                    videoshare.putExtra(Intent.EXTRA_STREAM, picUri);
                                                    videoshare.setType("video/*");
                                                    videoshare.setPackage("com.facebook.katana");
                                                    videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    activity.startActivity(videoshare);

                                                } catch (Exception e) {
                                                    Log.e("Error....", e.toString());
                                                    e.printStackTrace();

                                                    Intent videoshare = new Intent(Intent.ACTION_SEND);
                                                    videoshare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                                                    videoshare.setType("text/plain");
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

    public void sharewhatupp(int position) {

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

    private class sharefacebookImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(activity);
            // Set progressdialog title
            mProgressDialog.setTitle("Share Image");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            shareFacebook(activity, result);
            mProgressDialog.dismiss();
        }
    }

    public static void shareFacebook(Activity activity, Bitmap url) {
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                url, "Design", null);
        boolean facebookAppFound = false;
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));

        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.packageName).contains("com.facebook.katana")) {
                final ActivityInfo activityInfo = app.activityInfo;
                final ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setComponent(name);
                facebookAppFound = true;
                break;
            }
        }
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + url;
            shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        activity.startActivity(shareIntent);
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
                    Constants.getFileName(uRl) + ".mp4"));
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
                ActivityCompat.requestPermissions((Activity) activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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

    private class ShareImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ShareImage(result);
            mProgressDialog.dismiss();
        }

    }

    private void ShareImage(Bitmap finalBitmap) {

        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                finalBitmap, "Design", null);

        Uri uri = Uri.parse(path);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
        activity.startActivity(Intent.createChooser(share, "Fun With Status"));
    }

    private class sharewhatsappImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(activity);
            // Set progressdialog title
            mProgressDialog.setTitle("Share Image");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            SharewhatsappImage(activity, result);
            mProgressDialog.dismiss();
        }
    }

    private void SharewhatsappImage(Activity activity, Bitmap finalBitmap) {
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                finalBitmap, "Design", null);
        Uri uri = Uri.parse(path);
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
        whatsappIntent.setType("text/plain");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, uri);
        whatsappIntent.setType("image/jpeg");
        whatsappIntent.setPackage("com.whatsapp");

        try {
            activity.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }
}

