package com.epsilon.FunwithStatus;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.adapter.GridViewItem;
import com.epsilon.FunwithStatus.adapter.MyGridAdapter;
import com.epsilon.FunwithStatus.utills.BitmapHelper;
import com.epsilon.FunwithStatus.utills.Constants;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class whatsappImage extends AppCompatActivity implements AdapterView.OnItemClickListener{

    GridView gridView;
    private static final int STORAGE_PERMISSION_CODE = 123;
    SwipeRefreshLayout swipelayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_image);
        requestStoragePermission();
       gridView =(GridView)findViewById(R.id.gridView);
        swipelayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);

        final String path = Environment.getExternalStorageDirectory().toString()+"/WhatsApp/Media/.Statuses";
        Log.e("path",":"+path);
        setGridAdapter(path);


        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setGridAdapter(path);
                swipelayout.setRefreshing(false);
            }
        });

    }

    private void setGridAdapter(String path) {

//        String path = Environment.getExternalStorageDirectory().toString()+"WhatsApp/Media/.Statuses";
        Constants.items = createGridItems(path);
        MyGridAdapter adapter = new MyGridAdapter(this);

        // Set the grid adapter
        gridView.setAdapter(adapter);

        // Set the onClickListener
        gridView.setOnItemClickListener(this);
    }

    private List<GridViewItem> createGridItems(String directoryPath) {
        List<GridViewItem> items = new ArrayList<GridViewItem>();

        // List all the items within the folder.
        File[] files = new File(directoryPath).listFiles();
//        File[] files = new File(directoryPath).listFiles(new ImageFileFilter());
        for (File file : files) {

            // Add the directories containing images or sub-directories
            if (file.isDirectory()
                    && file.listFiles(new ImageFileFilter()).length > 0) {

                items.add(new GridViewItem(file.getAbsolutePath(), true, null));
            }
            // Add the images
            else {
                String image = file.getAbsolutePath();
                String filenameArray[] = image.split("\\.");
                String extension = filenameArray[filenameArray.length - 1];

                if(extension.equalsIgnoreCase("jpg"))
                {
                    items.add(new GridViewItem(file.getAbsolutePath(), false, image));

                }
                else
                {

                }
            }
        }

        return items;
    }

    private boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png"))
        // Add other formats as desired
        {
            return true;
        }
        return false;
    }


    @Override
    public void
    onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if ( Constants.items.get(position).isDirectory()) {
            setGridAdapter( Constants.items.get(position).getPath());
        }
        else {
            // Display the image
        }

    }

    private class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            else if (isImageFile(file.getAbsolutePath())) {
                return true;
            }
            return false;
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void onBackPressed() {
        Intent it = new Intent(this, WhatsappActivity.class);
        startActivity(it);
        finish();
    }
}
