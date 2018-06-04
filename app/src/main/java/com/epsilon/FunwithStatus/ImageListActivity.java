package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.adapter.ImageListAdapter;
import com.epsilon.FunwithStatus.jsonpojo.addimage.AdddImage;
import com.epsilon.FunwithStatus.jsonpojo.image_list.ImageList;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.epsilon.FunwithStatus.utills.Sessionmanager.Id;

public class ImageListActivity extends AppCompatActivity {

    Activity context;
    GridView imagelistgrid_view;
    Toolbar toolbar;
    String subcat;
    String post_files;
    File fileGallery;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    public static final int GALLARY_REQUEST = 2;
    public static final int MY_PERMISSIONS_REQUEST_GALLARY = 11;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 12;
    public static final int CAMERA_CROP_RESULT = 10;
    private final int RESULT_CROP = 40;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    SwipeRefreshLayout swipelayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        context = this;
        idMappings();
        setSupportActionBar(toolbar);

        subcat = getIntent().getStringExtra("NAME");

        if (!Helper.isConnectingToInternet(context)) {
            Helper.showToastMessage(context, "Please Connect Internet");
        } else {
            Imagecategory(subcat);
        }

        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Imagecategory(subcat);
                swipelayout.setRefreshing(false);
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.vc_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle(subcat);

        AdView mAdView = findViewById(R.id.adView);
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
    }

    public void onBackPressed() {
        Intent it = new Intent(ImageListActivity.this, SubCatImage.class);
        it.putExtra("NAME",subcat);
        startActivity(it);
        finish();
    }

    private void idMappings() {

        imagelistgrid_view = (GridView) findViewById(R.id.imagelistgrid_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        swipelayout=(SwipeRefreshLayout) findViewById(R.id.swipelayout);
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLARY_REQUEST);
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
                ActivityCompat.requestPermissions(context,
                        new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
    }
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(context,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_GALLARY);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_GALLARY);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLARY_REQUEST) {
            Toast.makeText(context, "gallary request", Toast.LENGTH_LONG).show();
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    post_files = saveImage(bitmap);
                    if (bitmap != null) {
                        popup(bitmap);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private String saveImage(Bitmap thumbnail) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + "IMAGE_DIRECTORY");
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File f = new File(path, "DemoPicture.jpg");
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.e("TAG", "File Saved::--->" + f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public void popup(Bitmap bitmap) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.addpicture);

        final ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_image);
        ImageView button_chat_send = (ImageView) dialog.findViewById(R.id.button_caption_send);
        final EditText edit_caption = (EditText) dialog.findViewById(R.id.edit_caption);

        iv_image.setImageBitmap(bitmap);
        fileGallery = new File(post_files);
        dialog.show();

        button_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_image != null) {
                    Uploadpic(edit_caption.getText().toString(),
                            fileGallery);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    public void Uploadpic( String comments,File post_files) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), comments);
        if (post_files != null) {
            RequestBody  requestFile = RequestBody.create(MediaType.parse("image/*"), post_files);
            MultipartBody.Part image = MultipartBody.Part.createFormData("post_files", post_files.getName(), requestFile);
            Call<AdddImage> circlePostAddPCall = apiInterface.addimage(name,image);
            circlePostAddPCall.enqueue(new Callback<AdddImage>() {
                @Override
                public void onResponse(Call<AdddImage> uploadImageCall, Response<AdddImage> response)
                {
                    if (response.body().getStatus().equalsIgnoreCase("Succ")) {
                        dialog.dismiss();
                        Toast.makeText(context,"success", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(context, "try again", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<AdddImage> call, Throwable t) {
                    dialog.dismiss();
                    }
            });
        }
    }

    public void Imagecategory(String subcat) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<ImageList> countrycall = apiInterface.imagelistpojo(subcat);
        countrycall.enqueue(new Callback<ImageList>() {
            @Override
            public void onResponse(Call<ImageList> call, Response<ImageList> response) {
                dialog.dismiss();
                if (response.body().getStatus().equals("Success")) {
                    if (Constants.imageListData != null) {
                        Constants.imageListData.clear();
                    }
                    Constants.imageListData.addAll(response.body().getImages());
                    ImageListAdapter adapter = new ImageListAdapter(context);
                    imagelistgrid_view.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(context,"Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageList> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuimage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.vc_addtoolbar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    choosePhotoFromGallary();
                } else {
                    //Request Location Permission
                    checkCameraPermission();
                    checkStoragePermission();
                }
            } else {
                choosePhotoFromGallary();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


