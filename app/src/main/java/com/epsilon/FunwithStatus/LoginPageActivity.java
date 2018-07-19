package com.epsilon.FunwithStatus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.jsonpojo.FbLoginRes;
import com.epsilon.FunwithStatus.jsonpojo.login.Login;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Google;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.epsilon.FunwithStatus.utills.Utils;
import com.epsilon.FunwithStatus.utills.UtilsConstant;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;


import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPageActivity extends BaseActivity {

    EditText login_email, login_epassword;
    TextView login_tlogin, login_tsignin, login_tforgot, login_tskip;
    ImageView login_user, login_password, pwdeye;
    Context context;
    private int passwordNotVisible = 1;
    Sessionmanager sessionmanager;
    APIInterface apiInterface ;
    private InterstitialAd interstitialAd;
    private final String TAG = TextListActivity.class.getSimpleName();
    private final String PREFERENCE_NAME = "ad_counter_preference";
    private final String COUNTER_INTERSTITIAL_ADS = "ad_counter";
    private int mAdCounter = 0;
    private AdRequest mInterstitialAdRequest;
    private static final String EMAIL = "email";

    // Facebook Login
    CallbackManager callbackManager;

    @BindView(R.id.btnFbLogin)
    LoginButton btnFbLogin;
    @BindView(R.id.btnGoogleLogin)
    TextView btnGoogleLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        ButterKnife.bind(this);

        context = this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionmanager = new Sessionmanager(this);

        getHashKey(getApplicationContext());
        GoogleSignIn();


        idMapping();
        Listener();

        loadInterstitialAd();


        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        mAdCounter = preferences.getInt(COUNTER_INTERSTITIAL_ADS, 0);

        if (mAdCounter == 3) {
            // Load interstitial ad now
            interstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });
            mAdCounter = 0; //Clear counter variable
        } else {
            mAdCounter++; // Increment counter variable
        }

        // Save counter value back to SharedPreferences
        editor.putInt(COUNTER_INTERSTITIAL_ADS, mAdCounter);
        editor.commit();


        String udata = "Want to Skip this page ?";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        login_tskip.setText(content);

        init();
    }

    public void init() {

        btnFbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFacebook();
            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (btnGoogleLogin.getText().toString().equals("Google Logout")) {
//                    google.signOut();
//                } else if (google.getGoogleSignInClient() != null) {
                Intent signInIntent = google.getGoogleSignInClient().getSignInIntent();
                startActivityForResult(signInIntent, Google.GOOGLE_SIGNIN_REQUEST_CODE);
//                }
            }
        });
    }

    private void initFacebook() {
        callbackManager = CallbackManager.Factory.create();
        btnFbLogin.setReadPermissions(Arrays.asList(EMAIL, "public_profile"));
        FacebookSdk.sdkInitialize(getApplicationContext());

        LoginManager.getInstance().logOut();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginManager.getInstance().logOut();

                getFacebookProfile(loginResult.getAccessToken());
                Log.e("FB", "onProcess");
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
                Log.e("FB", "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("FB", "onError : " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Facebook login has error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getFacebookProfile(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.e("getFacebookProfile", "" + object.toString());
                try {

                    FbLoginRes data = new Gson().fromJson(object.toString(), new TypeToken<FbLoginRes>() {
                    }.getType());


                    fbLogin(data.firstName, data.lastName, data.email,data.id);
//                    fbRes = new Gson().fromJson(object.toString(), new TypeToken<FbRes>() {
//                    }.getType());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, first_name, last_name, email,birthday,gender,location,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    Google google;

    private void GoogleSignIn() {
        google = Google.getInstance();
        google.configureSignIn(LoginPageActivity.this);


        google.setEventListner(new Google.EventListner() {
            @Override
            public void onGoogleSignInResult(GoogleSignInAccount account) {
                if (account != null) {
                    Log.d("", "Email : " + account.getEmail());


                } else {
                    Log.d("", "Google Sign In Failed");
                }
            }

            @Override
            public void onGoogleSignoutResult(boolean isSignOut) {
                Log.d("", "Email : " + isSignOut);
                btnGoogleLogin.setText(isSignOut ? "Google" : "Google Logout");

            }
        });
    }

    public  void fbLogin(String f_Name, String l_Name, String email, String social_id){

    }

    private void loadInterstitialAd() {

        mInterstitialAdRequest = new AdRequest.Builder()
                .build();

        //interstitial
        interstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        interstitialAd.setAdUnitId("Your Ad unit Id");

        // Load ads into Interstitial Ads
        interstitialAd.loadAd(mInterstitialAdRequest);
    }

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    private void Listener() {
        login_tsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(LoginPageActivity.this, RegistrationPageActivity.class);
                startActivity(it);
                finish();
            }
        });
        login_tskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginPageActivity.this, Dashboard.class);
                startActivity(i);
                finish();
            }
        });
        login_tforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, ForgetPasswordActivity.class);
                startActivity(it);
            }
        });
        login_tlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login_email.getText().toString().length() == 0) {
                    login_email.setError("Enter Email");
                } else if (login_epassword.getText().toString().length() == 0) {
                    login_epassword.setError("Enter Password");
                } else if (login_epassword.getText().toString().length() < 6) {
                    login_epassword.setError("Enter Minimum 6 digit");
                } else {
                    if (Helper.isConnectingToInternet(context)) {
                        LoginPage(login_email.getText().toString(), login_epassword.getText().toString());
                    } else {
                        Helper.showToastMessage(context, "No Internet Connection");
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
            public void onResponse(Call<Login> call, Response<Login> response) {
                dialog.dismiss();
                if (response.body().getStatus() == 1) {
                    sessionmanager.createSession_userLogin((response.body().getData()));
                    Sessionmanager.setPreferenceBoolean(LoginPageActivity.this, Constants.IS_LOGIN, true);
                    Toast.makeText(LoginPageActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("TOKEN", response.body().getData().getToken());

                    Utils.setPref(getActivity(), UtilsConstant.TOKEN, response.body().getData().getToken());
                    Log.e("Token-->>", Utils.getPref(context, UtilsConstant.TOKEN, ""));
                    Intent i = new Intent(LoginPageActivity.this, Dashboard.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(LoginPageActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                dialog.dismiss();
                Helper.showToastMessage(context, "No Internet Connection");
            }
        });
    }

    public static void getHashKey(Context c) {

        try {
            PackageInfo info = c.getPackageManager().getPackageInfo(
                    c.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:--> ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (Exception e) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Google.GOOGLE_SIGNIN_REQUEST_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            google.getSignInAccount(data);
//            GoogleSignIn();
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}
