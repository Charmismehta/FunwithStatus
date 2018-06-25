package com.epsilon.FunwithStatus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.jsonpojo.login.Login;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {

    EditText login_email, login_epassword;
    TextView login_tlogin, login_tsignin, login_tforgot,login_tskip;
    ImageView login_user,login_password,pwdeye;
    Context context;
    private int passwordNotVisible=1;
    Sessionmanager sessionmanager;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    private InterstitialAd interstitialAd;
    private final String TAG = TextListActivity.class.getSimpleName();

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        context = this;
        sessionmanager = new Sessionmanager(this);
        idMapping();
        Listener();

        String udata="Want to Skip this page ?";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        login_tskip.setText(content);

        interstitialAd = new InterstitialAd(this, getString(R.string.placement_id));
        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
    }

    private void Listener() {
        login_tsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(LoginPage.this, RegistrationPage.class);
                startActivity(it);
                finish();
            }
        });

        login_tskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginPage.this, Dashboard.class);
                startActivity(i);
                finish();
            }
        });

        login_tforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context,ForgetPassword.class);
                startActivity(it);
            }
        });


        login_tlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login_email.getText().toString().length() == 0) {
                    login_email.setError("Enter Email");
                }
                else if (login_epassword.getText().toString().length() == 0) {
                    login_epassword.setError("Enter Password");
                }
                else if (login_epassword.getText().toString().length() < 8) {
                    login_epassword.setError("Enter Minimum 8 digit");
                }
                else {
                    if(Helper.isConnectingToInternet(context)){
                        LoginPage(login_email.getText().toString(), login_epassword.getText().toString());
                    }
                    else {
                        Helper.showToastMessage(context,"No Internet Connection");
                    }
                }
            }
        });
        pwdeye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordNotVisible == 1) {
                    login_epassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordNotVisible = 0;
                    pwdeye.setImageResource(R.drawable.registrathioneye);
                } else {

                    login_epassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordNotVisible = 1;
                    pwdeye.setImageResource(R.drawable.eyeclosed);
                }
            }
        });
    }

    private void idMapping() {
        login_email = (EditText) findViewById(R.id.login_email);
        login_epassword = (EditText) findViewById(R.id.login_epassword);
        login_password = (ImageView) findViewById(R.id.login_password);
        login_user = (ImageView) findViewById(R.id.login_user);
        login_tlogin = (TextView) findViewById(R.id.login_tlogin);
        login_tsignin = (TextView) findViewById(R.id.login_tsignin);
        login_tforgot = (TextView) findViewById(R.id.login_tforgot);
        login_tskip = (TextView) findViewById(R.id.login_tskip);
        pwdeye = (ImageView) findViewById(R.id.pwdeye);
    }
    public void LoginPage(String email, String password) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

        Call<Login> logincall = apiInterface.loginpojocall(email, password);
        logincall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response)
            {
                dialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("Success"))
                {
                    sessionmanager.createSession_userLogin((response.body().getData()));
                    Log.e("UserName ",sessionmanager.getValue(sessionmanager.Name));
                    Sessionmanager.setPreferenceBoolean(LoginPage.this, Constants.IS_LOGIN,true);
                    Toast.makeText(LoginPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginPage.this, Dashboard.class);
                    startActivity(i);
                    finish();

                    Log.e("LoginID",response.body().getData().getUser().getId());
                }
                else
                {
                    Toast.makeText(LoginPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "No internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}
