package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.jsonpojo.addlike.AddLike;
import com.epsilon.FunwithStatus.jsonpojo.deletetext.DeleteText;
import com.epsilon.FunwithStatus.jsonpojo.dislike.DisLike;
import com.epsilon.FunwithStatus.jsonpojo.textstatus.Status;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.vdurmont.emoji.EmojiParser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DisplayTextActivity extends BaseActivity {

    EmojiconTextView display_text;
    ImageView share, like, dislike, copy, delete, whatsapp,facebook;
    TextView catname;
    Activity activity;
    String text, Id, name, email, u_name, loginuser;
    APIInterface apiInterface;
    Sessionmanager sessionmanager;
    RelativeLayout rlayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_text);
        activity = this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionmanager = new Sessionmanager(this);
        idMappings();
        Listners();
         text = getIntent().getStringExtra("text");
         Id = getIntent().getStringExtra("Id");
         name = getIntent().getStringExtra("NAME");
         u_name = getIntent().getStringExtra("U_NAME");
         email = sessionmanager.getValue(Sessionmanager.Email);
         loginuser = sessionmanager.getValue(Sessionmanager.Name);
        String result = EmojiParser.parseToUnicode(text);
        display_text.setText(result);

        if (loginuser.equalsIgnoreCase(u_name)) {
            delete.setVisibility(View.VISIBLE);
        }
    }

    private void Listners() {
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

                copy.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        copy.startAnimation(animation_3);
                        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                        cm.setText(display_text.getText());
                        Toast.makeText(getActivity(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_STREAM, "https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                        share.putExtra(Intent.EXTRA_TEXT, display_text.getText().toString());
                        getActivity().startActivity(Intent.createChooser(share, "Fun With Status"));
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
//                        addlike(name, email, Id, text);
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
//                        dislike(name, email, Id, text);
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
                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, display_text.getText().toString());
                        try {
                            getActivity().startActivity(whatsappIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getActivity(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
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

                        boolean facebookAppFound = false;
                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(text));

                        PackageManager pm = getActivity().getPackageManager();
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
                            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + text;
                            shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                        }
                        getActivity().startActivity(shareIntent);
                }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

        });
    }

    public static void shareFacebook(Activity activity, String url) {
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
                break;
            }
        }
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + url;
            shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        activity.startActivity(shareIntent);
    }


    private void idMappings() {
        display_text = (EmojiconTextView) findViewById(R.id.display_text);
        copy = (ImageView) findViewById(R.id.copy);
        like = (ImageView) findViewById(R.id.like);
        dislike = (ImageView) findViewById(R.id.dislike);
        share = (ImageView) findViewById(R.id.share);
        delete = (ImageView) findViewById(R.id.delete);
        whatsapp = (ImageView) findViewById(R.id.whatsapp);
        facebook = (ImageView) findViewById(R.id.facebook);
        rlayout = (RelativeLayout) findViewById(R.id.rlayout);
        catname = (TextView) findViewById(R.id.catname);


    }

//    public void addlike(String category, String email, String status_id, String status) {
//        final ProgressDialog dialog = new ProgressDialog(getActivity());
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setMessage("Please Wait...");
//        dialog.show();
//        final Call<AddLike> countrycall = apiInterface.addlikepojo(category, email, status_id, status);
//        countrycall.enqueue(new Callback<AddLike>() {
//            @Override
//            public void onResponse(Call<AddLike> call, Response<AddLike> response) {
//                dialog.dismiss();
//                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                textstatus(name);
//            }
//
//            @Override
//            public void onFailure(Call<AddLike> call, Throwable t) {
//                dialog.dismiss();
//
//            }
//        });
//    }
//
//    public void dislike(String category, String email, String status_id, String status) {
//        final ProgressDialog dialog = new ProgressDialog(getActivity());
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setMessage("Please Wait...");
//        dialog.show();
//        final Call<DisLike> countrycall = apiInterface.dislikepojo(category, email, status_id, status);
//        countrycall.enqueue(new Callback<DisLike>() {
//            @Override
//            public void onResponse(Call<DisLike> call, Response<DisLike> response) {
//                dialog.dismiss();
//                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                textstatus(name);
//            }
//
//            @Override
//            public void onFailure(Call<DisLike> call, Throwable t) {
//                dialog.dismiss();
//
//            }
//        });
//    }

    // TODO : TEXT DELETE API >>

    public void delete(String id) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<DeleteText> countrycall = apiInterface.deletetext(id);
        countrycall.enqueue(new Callback<DeleteText>() {
            @Override
            public void onResponse(Call<DeleteText> call, Response<DeleteText> response) {
                dialog.dismiss();
                if (response.body().getStatus().equals("Succ")) {
                    Intent it = new Intent(getActivity(), TextListActivity.class);
                    it.putExtra("NAME", name);
                    startActivity(it);
                    finish();
                    Toast.makeText(getActivity(), "Delete Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteText> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }

    // TODO TEXT DELETE API END


//    public void onBackPressed() {
//        if (name.equalsIgnoreCase("Trending"))
//        {
//            Intent it = new Intent(activity, TextListActivity.class);
//            it.putExtra("NAME", "Trending");
//            startActivity(it);
//            finish();// close this activity and return to preview activity (if there is any)
//        }
//        else
//        {
//            Intent it = new Intent(activity, TextListActivity.class);
//            it.putExtra("NAME", name);
//            startActivity(it);
//            finish();
//        }
//    }



}