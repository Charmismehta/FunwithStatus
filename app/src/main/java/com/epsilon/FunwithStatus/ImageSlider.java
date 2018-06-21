package com.epsilon.FunwithStatus;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.epsilon.FunwithStatus.adapter.FullScreenImageAdapter;
import com.epsilon.FunwithStatus.adapter.FullScreenTrenImageAdapter;
import com.epsilon.FunwithStatus.adapter.ImageListAdapter;
import com.epsilon.FunwithStatus.jsonpojo.deleteimage.DeleteImage;
import com.epsilon.FunwithStatus.jsonpojo.image_list.ImageList;
import com.epsilon.FunwithStatus.jsonpojo.image_list.ImageListDatum;
import com.epsilon.FunwithStatus.jsonpojo.imagedislike.ImageDislike;
import com.epsilon.FunwithStatus.jsonpojo.imagelike.ImageLike;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageSlider extends AppCompatActivity {

    ViewPager pager;
    Activity activity;
    String name, maincat, pic;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    LinearLayout layout_content, ll;
    RelativeLayout mainlayout;
    ImageView display_image, download, like, dislike, share, delete, whatsapp, facebook;
    InputStream is = null;
    Sessionmanager sessionmanager;
    ProgressDialog mProgressDialog;
    File myDir;
    Toolbar toolbar;
    InputStream stream = null;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);

        activity = this;
        sessionmanager = new Sessionmanager(activity);
        pager = (ViewPager) findViewById(R.id.pager);
        Intent mIntent = getIntent();
        final int position = mIntent.getIntExtra("position", 0);
        final String Id = getIntent().getStringExtra("Id");
        name = getIntent().getStringExtra("NAME");
        String u_name = getIntent().getStringExtra("U_NAME");
        String maincat = getIntent().getStringExtra("REALNAME");
        Log.e("##########NAME", name);
        final String email = sessionmanager.getValue(Sessionmanager.Email);
        final String loginuser = sessionmanager.getValue(Sessionmanager.Name);
        pic = getIntent().getStringExtra("pic");
        final Uri uri = Uri.parse(pic);

        display_image = (ImageView) findViewById(R.id.display_image);
        download = (ImageView) findViewById(R.id.download);
        like = (ImageView) findViewById(R.id.like);
        dislike = (ImageView) findViewById(R.id.dislike);
        share = (ImageView) findViewById(R.id.share);
        delete = (ImageView) findViewById(R.id.delete);
        whatsapp = (ImageView) findViewById(R.id.whatsapp);
        facebook = (ImageView) findViewById(R.id.facebook);
        layout_content = (LinearLayout) findViewById(R.id.layout_content);
        ll = (LinearLayout) findViewById(R.id.ll);
        mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);
        share.setColorFilter(getResources().getColor(R.color.colorAccent));
        like.setColorFilter(getResources().getColor(R.color.colorAccent));
        dislike.setColorFilter(getResources().getColor(R.color.colorAccent));
        delete.setColorFilter(getResources().getColor(R.color.colorAccent));
        download.setColorFilter(getResources().getColor(R.color.colorAccent));

        if (u_name.equalsIgnoreCase(loginuser)) {
            delete.setVisibility(View.VISIBLE);
        }

        if (name.equalsIgnoreCase("Featured")) {
            FullScreenTrenImageAdapter adapter = new FullScreenTrenImageAdapter(activity);
            pager.setAdapter(adapter);
            pager.post(new Runnable() {
                @Override
                public void run() {
                    pager.setCurrentItem(position, true);
                }
            });
        } else {
            FullScreenImageAdapter adapter = new FullScreenImageAdapter(activity);
            pager.setAdapter(adapter);
            pager.post(new Runnable() {
                @Override
                public void run() {
                    pager.setCurrentItem(position, true);
                }
            });
        }
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
                            saveimage(uri);
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
                        boolean result = checkPermission();
                        if (result) {
                            new ShareImage().execute(pic);
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
                final Animation animation_1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

                like.startAnimation(animation_1);

                animation_1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        like.startAnimation(animation_3);
                        addlike(name, email, Id, pic);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

                dislike.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dislike.startAnimation(animation_3);
                        dislike(name, email, Id, pic);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

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
                        delete(Id);
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
                        boolean result = checkPermission();
                        if (result) {
                            new sharewhatsappImage().execute(pic);
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
            public void onClick(View v) {
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
                            new sharefacebookImage().execute(pic);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });


        mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_content.getVisibility() == View.VISIBLE) {
                    layout_content.setVisibility(View.VISIBLE);
                    mainlayout.setBackgroundColor(Color.WHITE);
                } else if (layout_content.getVisibility() == View.GONE) {
                    layout_content.setVisibility(View.VISIBLE);
                    mainlayout.setBackgroundColor(Color.WHITE);
                } else {
                    layout_content.setVisibility(View.VISIBLE);
                    mainlayout.setBackgroundColor(Color.WHITE);
                }
            }
        });
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
        addImageToGallery(pic, activity);

    }

    // TODO : IMAGE LIKE API >>

    public void addlike(String category, String email, String image_id, String image) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<ImageLike> countrycall = apiInterface.addimagelikepojo(category, email, image_id, image);
        countrycall.enqueue(new Callback<ImageLike>() {
            @Override
            public void onResponse(Call<ImageLike> call, Response<ImageLike> response) {
                dialog.dismiss();
                Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ImageLike> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }

    // TODO IMAGE LIKE API END

    // TODO : IMAGE DELETE API >>

    public void delete(String id) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<DeleteImage> countrycall = apiInterface.deleteimage(id);
        countrycall.enqueue(new Callback<DeleteImage>() {
            @Override
            public void onResponse(Call<DeleteImage> call, Response<DeleteImage> response) {
                dialog.dismiss();
                if (response.body().getStatus().equals("Succ")) {
                    Intent it = new Intent(activity, ImageListActivity.class);
                    it.putExtra("NAME", name);
                    it.putExtra("REALNAME", maincat);
                    startActivity(it);
                    finish();
                    Toast.makeText(activity, "Delete Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteImage> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }


    // TODO IMAGE DELETE API END


    // TODO IMAGE DISLIKE API >>

    public void dislike(String category, String email, String status_id, String status) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<ImageDislike> countrycall = apiInterface.imagedislikepojo(category, email, status_id, status);
        countrycall.enqueue(new Callback<ImageDislike>() {
            @Override
            public void onResponse(Call<ImageDislike> call, Response<ImageDislike> response) {
                dialog.dismiss();
                Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ImageDislike> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }

    // TODO : IMAGE DISLIKE API END >>>>

//    public void onBackPressed() {
//        if (name.equalsIgnoreCase("featured")) {
//            Intent it = new Intent(activity, SubCatImage.class);
//            it.putExtra("NAME", name);
//            it.putExtra("REALNAME", maincat);
//            startActivity(it);
//            finish();// close this activity and return to preview activity (if there is any)
//        } else {
//            Intent it = new Intent(activity, ImageListActivity.class);
//            it.putExtra("NAME", name);
//            it.putExtra("REALNAME", maincat);
//            startActivity(it);
//            finish();
//        }
//    }

    // TODO : SHARE ON WHATSAPP

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


// TODO : SHARE ON FACEBOOK
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

// END SHARE


// TODO SAVE IMAGE CODE :
private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Create a progressdialog
        mProgressDialog = new ProgressDialog(activity);
        // Set progressdialog title
        mProgressDialog.setTitle("Download Image");
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
        SaveImage(result);
        mProgressDialog.dismiss();
        Toast.makeText(activity, "Image Save Successfully", Toast.LENGTH_SHORT).show();
        addImageToGallery(pic, activity);
    }
}

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addImageToGallery(String myDir, Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
        values.put(MediaStore.MediaColumns.DATA, myDir);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Toast.makeText(context, "Save Image Successfully", Toast.LENGTH_SHORT).show();
    }
// TODO END SAVE IMAGE CODE ///////

// TODO SHARE IMAGE :

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

    // TODO : END SHARE IMAGE CODE ////////////////


}
