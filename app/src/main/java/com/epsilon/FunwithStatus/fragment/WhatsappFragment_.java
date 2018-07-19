package com.epsilon.FunwithStatus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.epsilon.FunwithStatus.LoginPageActivity;
import com.epsilon.FunwithStatus.Object.FacebookDownload;
import com.epsilon.FunwithStatus.Object.WhatsAppDownLoad;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.adapter.GridViewItem;
import com.epsilon.FunwithStatus.adapter.MyVideoGridAdapter;
import com.epsilon.FunwithStatus.adapter.WhatsAppMediaAdapter;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.epsilon.FunwithStatus.utills.Utils;
import com.epsilon.FunwithStatus.whatsappImageActivity;
import com.epsilon.FunwithStatus.whatsappImageSlideActivity;
import com.epsilon.FunwithStatus.whatsappVideoActivity;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WhatsappFragment_ extends Fragment {
//    Activity activity;

    List<GridViewItem> gridViewItems = new ArrayList<GridViewItem>();
    FacebookDownload facebookDownload;
    WhatsAppDownLoad whatsAppDownLoad;
    WhatsAppMediaAdapter whatsAppMediaAdapter;
    Sessionmanager sessionmanager;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_whatsapp_, container, false);
        ButterKnife.bind(this, view);
//        activity = activity;
        sessionmanager = new Sessionmanager(getActivity());

        init();
        return view;
    }

    public void init() {
        Constants.items = createGridItems();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        whatsAppMediaAdapter = new WhatsAppMediaAdapter(getActivity());
        recyclerView.setAdapter(whatsAppMediaAdapter);


        whatsAppMediaAdapter.setEventListener(new WhatsAppMediaAdapter.EventListener() {
            @Override
            public void onItemImageClicked(int position, String itemName) {
                Log.e("PATH", ":" + itemName);
                Intent intent = new Intent(getActivity(), whatsappImageSlideActivity.class);
                intent.putExtra("U_NAME", sessionmanager.getValue(Sessionmanager.Name));
                intent.putExtra("picture", itemName);
                intent.putExtra("NAME", "WHATSAPP");
                intent.putExtra("position", position);
                Log.e("position",":"+position);
                startActivity(intent);
            }

            @Override
            public void onItemVideoClicked(int position) {

            }

            @Override
            public void onDownLoadClicked(int position, final View view) {

                GridViewItem data = whatsAppMediaAdapter.getItem(position);

                final String sourceLocation = data.getImage();
                final String targetLocation = Environment.getExternalStorageDirectory() + "/" + "FunwithStatus" + "/";
                final Animation animation_2 = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_fade_out);

                view.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.startAnimation(animation_3);
                        boolean result = Utils.checkPermission(getActivity());
                        if (result) {
                            Utils.copyFileOrDirectory(sourceLocation, targetLocation);
                            Toast.makeText(getActivity(), "Download Successfully", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onShareClicked(final int position, final View view) {
                final GridViewItem data = whatsAppMediaAdapter.getItem(position);
                final String sourceLocation = data.getImage();
                final String targetLocation = Environment.getExternalStorageDirectory() + "/" + "FunwithStatus" + "/";
                final Animation animation_2 = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.abc_fade_out);

                view.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.startAnimation(animation_3);
                        boolean result = Utils.checkPermission(getActivity());
                        if (result) {
                            String path = data.getImage();//it contain your path of image..im using a temp string..
                            String filename = path.substring(path.lastIndexOf("/") + 1);
                            File myFile = new File(Environment.getExternalStorageDirectory() + "/" + "FunwithStatus" + "/" + filename);
                            Log.e("FILE PATH", filename);

                            if (myFile.exists()) {
                                whatsAppMediaAdapter.sharevideo(data);
                            } else {
                                Utils.copyFileOrDirectory(sourceLocation, targetLocation);
                            }
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onWhatsappClicked(final int position, final View view) {
                final GridViewItem data = whatsAppMediaAdapter.getItem(position);
                final String sourceLocation = data.getImage();
                final String targetLocation = Environment.getExternalStorageDirectory() + "/" + "FunwithStatus" + "/";
                final Animation animation_2 = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.abc_fade_out);

                view.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.startAnimation(animation_3);
                        boolean result = Utils.checkPermission(getActivity());
                        if (result) {
                            whatsAppDownLoad = (WhatsAppDownLoad) new WhatsAppDownLoad(getActivity(), data).execute();
                        }

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onFacebookClicked(final int position, final View view, final String itemName) {
               final GridViewItem data = whatsAppMediaAdapter.getItem(position);
                final Animation animation_2 = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.abc_fade_out);

                view.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.startAnimation(animation_3);
                        boolean result = Utils.checkPermission(getActivity());
                        if (result) {
                            facebookDownload = (FacebookDownload) new FacebookDownload(getActivity(), data).execute();
                        }

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onUploadClicked(View view, String itemName) {

                if (Sessionmanager.getPreferenceBoolean(getActivity(), Constants.IS_LOGIN, false)) {
                    whatsAppMediaAdapter.uploadMultipart(view, itemName);
                } else {
                    Intent mainIntent = new Intent(getActivity(), LoginPageActivity.class);
                    startActivity(mainIntent);
                    LayoutInflater inflater = getLayoutInflater();
                    View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) view.findViewById(R.id.llCustom));
                    Toast toast = new Toast(getActivity().getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(toastLayout);
                    toast.show();
                }

            }
        });

        gridViewItems = createGridItems();

        whatsAppMediaAdapter.addAll(gridViewItems);


    }

    private List<GridViewItem> createGridItems() {
        final String path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses";
        List<GridViewItem> items = new ArrayList<GridViewItem>();

        // List all the items within the folder.
        File[] files = new File(path).listFiles();
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

                if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("mp4")) {
                    items.add(new GridViewItem(file.getAbsolutePath(), false, image));
                } else {

                }
            }
        }

        return items;
    }

    private class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            } else if (isImageFile(file.getAbsolutePath())) {
                return true;
            }
            return false;
        }
    }

    private boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png") || filePath.endsWith(".mp4") || filePath.endsWith(".3gp")) {
            return true;
        }
        return false;
    }


}