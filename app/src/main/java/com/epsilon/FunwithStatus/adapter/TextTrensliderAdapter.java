package com.epsilon.FunwithStatus.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.epsilon.FunwithStatus.ImageSlider;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.TextListActivity;
import com.epsilon.FunwithStatus.jsonpojo.addlike.AddLike;
import com.epsilon.FunwithStatus.jsonpojo.deletetext.DeleteText;
import com.epsilon.FunwithStatus.jsonpojo.dislike.DisLike;
import com.epsilon.FunwithStatus.jsonpojo.textstatus.Status;
import com.epsilon.FunwithStatus.jsonpojo.textstatus.StatusDatum;
import com.epsilon.FunwithStatus.jsonpojo.trending.TrendingDatum;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.vdurmont.emoji.EmojiParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextTrensliderAdapter extends PagerAdapter {
    private Activity _activity;
    private LayoutInflater inflater;
    String text, Id, name, email, u_name, loginuser, subcat;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    Sessionmanager sessionmanager;

    // constructor
    public TextTrensliderAdapter(Activity activity, String subcat, String u_name) {
        this._activity = activity;
        this.subcat = subcat;
        this.u_name = u_name;
        sessionmanager = new Sessionmanager(activity);
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Constants.trendingData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View viewLayout = inflater.inflate(R.layout.textsliderlayout, container,
                false);

        email = sessionmanager.getValue(Sessionmanager.Email);
        loginuser = sessionmanager.getValue(Sessionmanager.Name);
        text = Constants.trendingData.get(position).getStatus();
        Id = Constants.trendingData.get(position).getId();
        name = Constants.trendingData.get(position).getUser();

        final EmojiconTextView display_text = (EmojiconTextView) viewLayout.findViewById(R.id.display_text);
        final ImageView like = (ImageView) viewLayout.findViewById(R.id.like);
        final ImageView dislike = (ImageView) viewLayout.findViewById(R.id.dislike);
        final ImageView copy = (ImageView) viewLayout.findViewById(R.id.copy);
        final ImageView share = (ImageView) viewLayout.findViewById(R.id.share);
        final ImageView delete = (ImageView) viewLayout.findViewById(R.id.delete);
        final ImageView whatsapp = (ImageView) viewLayout.findViewById(R.id.whatsapp);
        final ImageView facebook = (ImageView) viewLayout.findViewById(R.id.facebook);

        String textURL = Constants.trendingData.get(position).getStatus();
        String result = EmojiParser.parseToUnicode(textURL);
        display_text.setText(result);

        if (loginuser.equalsIgnoreCase(u_name)) {
            delete.setVisibility(View.VISIBLE);
        }


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
                        addlike(name, email, Id, text);
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
                        dislike(name, email, Id, text);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Animation animation_2 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.bounce);
                final Animation animation_3 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.abc_fade_out);

                copy.startAnimation(animation_2);

                animation_2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        copy.startAnimation(animation_3);
                        ClipboardManager cm = (ClipboardManager) _activity.getSystemService(_activity.CLIPBOARD_SERVICE);
                        cm.setText(display_text.getText());
                        Toast.makeText(_activity, "Copied to clipboard", Toast.LENGTH_SHORT).show();
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
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_STREAM, display_text.getText().toString());
                        share.setType("text/plain");
                        _activity.startActivity(Intent.createChooser(share, "Fun With Status"));
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
                final Animation animation_2 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.move);
                final Animation animation_3 = AnimationUtils.loadAnimation(_activity.getBaseContext(), R.anim.abc_fade_out);

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
                            whatsapp.startAnimation(animation_3);
                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                            whatsappIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus");
                            whatsappIntent.setType("text/plain");
                            whatsappIntent.putExtra(Intent.EXTRA_STREAM, display_text.getText().toString());
                            whatsappIntent.setType("text/plain");
                            whatsappIntent.setPackage("com.whatsapp");
                            try {
                                _activity.startActivity(whatsappIntent);
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(_activity, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                            }
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
                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                            whatsappIntent.setType("text/plain");
                            whatsappIntent.setPackage("com.facebook.katana");
                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, display_text.getText().toString());
                            try {
                                _activity.startActivity(whatsappIntent);
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(_activity, "Facebook have not been installed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

        });


        (container).addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }

    public void addlike(String category, String email, String status_id, String status) {
        final ProgressDialog dialog = new ProgressDialog(_activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<AddLike> countrycall = apiInterface.addlikepojo(category, email, status_id, status);
        countrycall.enqueue(new Callback<AddLike>() {
            @Override
            public void onResponse(Call<AddLike> call, Response<AddLike> response) {
                dialog.dismiss();
                Toast.makeText(_activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                textstatus(name);
            }

            @Override
            public void onFailure(Call<AddLike> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }

    public void dislike(String category, String email, String status_id, String status) {
        final ProgressDialog dialog = new ProgressDialog(_activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<DisLike> countrycall = apiInterface.dislikepojo(category, email, status_id, status);
        countrycall.enqueue(new Callback<DisLike>() {
            @Override
            public void onResponse(Call<DisLike> call, Response<DisLike> response) {
                dialog.dismiss();
                Toast.makeText(_activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                textstatus(name);
            }

            @Override
            public void onFailure(Call<DisLike> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }
    // TODO : TEXT DELETE API >>

    public void delete(String id) {
        final ProgressDialog dialog = new ProgressDialog(_activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<DeleteText> countrycall = apiInterface.deletetext(id);
        countrycall.enqueue(new Callback<DeleteText>() {
            @Override
            public void onResponse(Call<DeleteText> call, Response<DeleteText> response) {
                dialog.dismiss();
                if (response.body().getStatus().equals("Succ")) {
                    Intent it = new Intent(_activity, TextListActivity.class);
                    it.putExtra("NAME", subcat);
                    _activity.startActivity(it);
                    _activity.finish();
                    Toast.makeText(_activity, "Delete Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(_activity, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteText> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }

    // TODO TEXT DELETE API END

    public void textstatus(String subcat) {
        final Call<Status> countrycall = apiInterface.textstatuspojo(subcat);
        countrycall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (Constants.statusData != null) {
                    Constants.statusData.clear();
                }
                if (!Constants.statusData.equals("") && Constants.statusData != null) {
                    Constants.statusData.addAll(response.body().getData());
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(_activity, "No Video Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
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
}
