package com.epsilon.FunwithStatus.adapter;


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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.epsilon.FunwithStatus.ImageListActivity;
import com.epsilon.FunwithStatus.R;

import com.epsilon.FunwithStatus.jsonpojo.addlike.AddLike;
import com.epsilon.FunwithStatus.jsonpojo.deleteimage.DeleteImage;
import com.epsilon.FunwithStatus.jsonpojo.image_list.ImageListDatum;
import com.epsilon.FunwithStatus.jsonpojo.imagedislike.ImageDislike;
import com.epsilon.FunwithStatus.jsonpojo.imagelike.ImageLike;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;


public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private LayoutInflater inflater;
    APIInterface apiInterface;
    Sessionmanager sessionmanager;
    ProgressDialog mProgressDialog;
    int i;


    // constructor
    public FullScreenImageAdapter(Activity activity,int i) {
        this._activity = activity;
        sessionmanager = new Sessionmanager(_activity);
        this.i = i;
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @Override
    public int getCount() {
        return Constants.imageListData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final String email = sessionmanager.getValue(Sessionmanager.Email);
        final String loginuser = sessionmanager.getValue(Sessionmanager.Name);
        final String u_name = Constants.imageListData.get(i).userName;
        final int Id = Constants.imageListData.get(i).id;


        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

//        ImageListDatum imglist = Constants.imageListData.get(position);
        ImageView display_image = (ImageView) viewLayout.findViewById(R.id.display_image);
        final ImageView download = (ImageView) viewLayout.findViewById(R.id.download);
        final ImageView like = (ImageView) viewLayout.findViewById(R.id.like);
        final ImageView dislike = (ImageView) viewLayout.findViewById(R.id.dislike);
        final ImageView share = (ImageView) viewLayout.findViewById(R.id.share);
        final ImageView delete = (ImageView) viewLayout.findViewById(R.id.delete);
        final ImageView whatsapp = (ImageView) viewLayout.findViewById(R.id.whatsapp);
        final ImageView facebook = (ImageView) viewLayout.findViewById(R.id.facebook);
        share.setColorFilter(_activity.getResources().getColor(R.color.colorAccent));
        like.setColorFilter(_activity.getResources().getColor(R.color.colorAccent));
        dislike.setColorFilter(_activity.getResources().getColor(R.color.colorAccent));
        delete.setColorFilter(_activity.getResources().getColor(R.color.colorAccent));
        download.setColorFilter(_activity.getResources().getColor(R.color.colorAccent));

        final String imgURL = Constants.imageListData.get(position).file;
        Glide.with(_activity).load(imgURL).thumbnail(Glide.with(_activity).load(R.drawable.load)).into(display_image);

        if (u_name.equalsIgnoreCase(loginuser)) {
            delete.setVisibility(View.VISIBLE);
        }
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.abc_fade_out);

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
                            final Uri uri = Uri.parse(imgURL);
                            DownloadManager.Request r = new DownloadManager.Request(uri);

                            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName");

                            r.allowScanningByMediaScanner();

                            r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                            DownloadManager dm = (DownloadManager) _activity.getSystemService(DOWNLOAD_SERVICE);
                            dm.enqueue(r);
                            addImageToGallery(imgURL, _activity);

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
                final Animation animation_2 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.abc_fade_out);

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
                            Log.e("pic ", imgURL);
                            new ShareImage().execute(imgURL);
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
                final Animation animation_1 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.abc_fade_out);

                like.startAnimation(animation_1);

                animation_1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        like.startAnimation(animation_3);
                        addlike(Id,"like");
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
                final Animation animation_2 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.abc_fade_out);

                dislike.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dislike.startAnimation(animation_3);
                        addlike(Id,"unlike");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });
//
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Animation animation_2 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.move);
//                final Animation animation_3 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.abc_fade_out);
//
//                delete.startAnimation(animation_2);
//
//                animation_2.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        delete.startAnimation(animation_3);
//                        delete(Id);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//            }
//        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animation_2 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.abc_fade_out);

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
                            new sharewhatsappImage().execute(imgURL);
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
                final Animation animation_2 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.abc_fade_out);

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
                            new sharefacebookImage().execute(imgURL);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });

        ((ViewPager)container).addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (_activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted2");
                return true;
            } else {
                ActivityCompat.requestPermissions(_activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }


    // TODO : IMAGE LIKE API >>

    public void addlike(int id, String type) {
        final ProgressDialog dialog = new ProgressDialog(_activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<AddLike> countrycall = apiInterface.addlikepojo(id, type);
        countrycall.enqueue(new Callback<AddLike>() {
            @Override
            public void onResponse(Call<AddLike> call, Response<AddLike> response) {
                dialog.dismiss();
                Toast.makeText(_activity, response.body().msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AddLike> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }

    // TODO IMAGE LIKE API END

    // TODO : IMAGE DELETE API >>

//    public void delete(String id) {
//        final ProgressDialog dialog = new ProgressDialog(_activity);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setMessage("Please Wait...");
//        dialog.show();
//        final Call<DeleteImage> countrycall = apiInterface.deleteimage(id);
//        countrycall.enqueue(new Callback<DeleteImage>() {
//            @Override
//            public void onResponse(Call<DeleteImage> call, Response<DeleteImage> response) {
//                dialog.dismiss();
//                if (response.body().getStatus().equals("Succ")) {
//                    Intent it = new Intent(_activity, ImageListActivity.class);
//                    it.putExtra("NAME", name);
//                    it.putExtra("REALNAME", maincat);
//                    _activity.startActivity(it);
//                    _activity.finish();
//                    Toast.makeText(_activity, "Delete Successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(_activity, "Try Again", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DeleteImage> call, Throwable t) {
//                dialog.dismiss();
//
//            }
//        });
//    }


    // TODO IMAGE DELETE API END


    // TODO IMAGE DISLIKE API >>

    public void dislike(String category, String email, String status_id, String status) {
        final ProgressDialog dialog = new ProgressDialog(_activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<ImageDislike> countrycall = apiInterface.imagedislikepojo(category, email, status_id, status);
        countrycall.enqueue(new Callback<ImageDislike>() {
            @Override
            public void onResponse(Call<ImageDislike> call, Response<ImageDislike> response) {
                dialog.dismiss();
                Toast.makeText(_activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            mProgressDialog = new ProgressDialog(_activity);
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
            SharewhatsappImage(_activity, result);
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
            mProgressDialog = new ProgressDialog(_activity);
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
            shareFacebook(_activity, result);
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
            mProgressDialog = new ProgressDialog(_activity);
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

        String path = MediaStore.Images.Media.insertImage(_activity.getContentResolver(),
                finalBitmap, "Design", null);

        Uri uri = Uri.parse(path);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
        _activity.startActivity(Intent.createChooser(share, "Fun With Status"));
    }
}
