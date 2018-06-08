package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.epsilon.FunwithStatus.jsonpojo.addlike.AddLike;
import com.epsilon.FunwithStatus.jsonpojo.deleteimage.DeleteImage;
import com.epsilon.FunwithStatus.jsonpojo.deletetext.DeleteText;
import com.epsilon.FunwithStatus.jsonpojo.imagedislike.ImageDislike;
import com.epsilon.FunwithStatus.jsonpojo.imagelike.ImageLike;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.System.in;

public class DisplayImage extends AppCompatActivity {

    Context activity;
    LinearLayout layout_content;
    RelativeLayout mainlayout;
    ImageView display_image, download, like, dislike, share, delete,whatsapp;
    String pic, name, root, Id, email, u_name, loginuser;
    InputStream is = null;
    Sessionmanager sessionmanager;
    ProgressDialog mProgressDialog;
    File myDir;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    Toolbar toolbar;
    InputStream stream = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        activity = this;
        idMappings();
        Listners();
        sessionmanager = new Sessionmanager(this);
        Id = getIntent().getStringExtra("Id");
        name = getIntent().getStringExtra("NAME");
        u_name = getIntent().getStringExtra("U_NAME");
        Log.e("##########NAME", name);
        email = sessionmanager.getValue(Sessionmanager.Email);
        loginuser = sessionmanager.getValue(Sessionmanager.Name);
        pic = getIntent().getStringExtra("pic");

        share.setColorFilter(getResources().getColor(R.color.colorAccent));
        like.setColorFilter(getResources().getColor(R.color.colorAccent));
        dislike.setColorFilter(getResources().getColor(R.color.colorAccent));
        delete.setColorFilter(getResources().getColor(R.color.colorAccent));
        download.setColorFilter(getResources().getColor(R.color.colorAccent));

        Glide.with(activity).load(pic)
                .thumbnail(Glide.with(activity).load(R.drawable.loading))
                .fitCenter()
                .crossFade()
                .into(display_image);
//        Glide.with(activity).load(pic).into(display_image);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.vc_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (u_name.equalsIgnoreCase(loginuser)) {
            delete.setVisibility(View.VISIBLE);
        }


    }

    private void idMappings() {
        display_image = (ImageView) findViewById(R.id.display_image);
        download = (ImageView) findViewById(R.id.download);
        like = (ImageView) findViewById(R.id.like);
        dislike = (ImageView) findViewById(R.id.dislike);
        share = (ImageView) findViewById(R.id.share);
        delete = (ImageView) findViewById(R.id.delete);
        whatsapp = (ImageView) findViewById(R.id.whatsapp);
        layout_content = (LinearLayout) findViewById(R.id.layout_content);
        mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);
    }

    private void Listners() {

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
                        finish();
                        new DownloadImage().execute(pic);
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
                        new ShareImage().execute(pic);
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
                final Animation animation_1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
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
                final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.antirotate);
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
                final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_down);
                final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

                whatsapp.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        whatsapp.startAnimation(animation_3);
                        new sharewhatsappImage().execute(pic);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            });

        display_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_content.getVisibility() == View.VISIBLE) {
                    layout_content.setVisibility(View.GONE);
                    mainlayout.setBackgroundColor(Color.BLACK);
                } else if (layout_content.getVisibility() == View.GONE) {
                    layout_content.setVisibility(View.VISIBLE);
                    mainlayout.setBackgroundColor(Color.WHITE);
                } else {
                    layout_content.setVisibility(View.GONE);
                    mainlayout.setBackgroundColor(Color.BLACK);
                }
            }
        });
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
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // TODO : IMAGE DISLIKE API END >>>>

    public void onBackPressed() {
        if (name.equalsIgnoreCase("featured"))
        {
            Intent it = new Intent(activity, SubCatImage.class);
            it.putExtra("NAME", name);
            startActivity(it);
            finish();// close this activity and return to preview activity (if there is any)
        }
        else
            {
            Intent it = new Intent(activity, ImageListActivity.class);
            it.putExtra("NAME", name);
            startActivity(it);
            finish();
        }
    }

    // TODO : SHARE ON WHATSAPP

    private class sharewhatsappImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(DisplayImage.this);
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
            SharewhatsappImage(result);
            mProgressDialog.dismiss();
            Toast.makeText(activity, "Image Share Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void SharewhatsappImage(Bitmap finalBitmap) {

        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                finalBitmap, "Design", null);

        Uri uri = Uri.parse(path);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Share From epsilon infotech");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.setType("image/jpeg");
        intent.setPackage("com.whatsapp");
        startActivity(intent);
    }


    // END SHARE



    // TODO SAVE IMAGE CODE :
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(DisplayImage.this);
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

        root = Environment.getExternalStorageDirectory().toString();
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
    }
    // TODO END SAVE IMAGE CODE ///////

    // TODO SHARE IMAGE :

    private class ShareImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(DisplayImage.this);
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
            addImageToGallery(pic, activity);
        }
    }

    private void ShareImage(Bitmap finalBitmap) {

        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                finalBitmap, "Design", null);

        Uri uri = Uri.parse(path);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, "Share by Fun With Status");
        activity.startActivity(Intent.createChooser(share, "Fun With Status"));
    }

    // TODO : END SHARE IMAGE CODE ////////////////


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            if (name.equalsIgnoreCase("featured")) {
                Intent it = new Intent(activity, SubCatImage.class);
                it.putExtra("NAME", name);
                startActivity(it);
                finish();// close this activity and return to preview activity (if there is any)
            } else {
                Intent it = new Intent(activity, ImageListActivity.class);
                it.putExtra("NAME", name);
                startActivity(it);
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
