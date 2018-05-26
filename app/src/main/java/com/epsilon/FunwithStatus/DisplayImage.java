package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class DisplayImage extends AppCompatActivity {

    Activity activity;
    ImageView display_image, download, like, dislike, share;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        activity = this;
        idMappings();
        Listners();

    }

    private void idMappings() {
        display_image = (ImageView) findViewById(R.id.display_image);
        download = (ImageView) findViewById(R.id.download);
        like = (ImageView) findViewById(R.id.like);
        dislike = (ImageView) findViewById(R.id.dislike);
        share = (ImageView) findViewById(R.id.share);
    }

    private void Listners() {

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File myDir = new File(root + "/saved_images_1");
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = "Image-" + n + ".jpg";
                File file = new File(myDir, fname);
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(activity, "Saved successfully, Check gallery", Toast.LENGTH_SHORT).show();

                // Tell the media scanner about the new file so that it is
                // immediately available to the user.
                MediaScannerConnection.scanFile(activity, new String[]{file.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);

                // If you want to share a png image only, you can do:
                // setType("image/png"); OR for jpeg: setType("image/jpeg");
                share.setType("image/*");

                // Make sure you put example png image named myImage.png in your
                // directory
//                String imagePath = Environment.getExternalStorageDirectory()+ ;

                File imageFileToShare = new File(String.valueOf(R.drawable.a));

                Uri uri = Uri.fromFile(imageFileToShare);
                share.putExtra(Intent.EXTRA_STREAM, uri);

                activity.startActivity(Intent.createChooser(share, "Share Image!"));
            }
        });

    }

    public void onBackPressed() {
        Intent it = new Intent(DisplayImage.this, ImageListActivity.class);
        startActivity(it);
        finish();
    }
}
