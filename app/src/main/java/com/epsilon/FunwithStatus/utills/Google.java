package com.epsilon.FunwithStatus.utills;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Kartum Infotech (Bhavesh Hirpara) on 19-04-2018.
 */
public class Google {


    public static final int GOOGLE_SIGNIN_REQUEST_CODE = 7777;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private EventListner eventListner;
    private Activity activity;

    private Google() {
    }

    public GoogleSignInClient getGoogleSignInClient() {

        return mGoogleSignInClient;
    }

    public static Google getInstance() {
        return  new Google();
    }

    public GoogleSignInAccount getExistingGoogleUser(Activity activity) {
        account = GoogleSignIn.getLastSignedInAccount(activity);
        return account;
    }

    public void configureSignIn(Activity activity) {
        this.activity = activity;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }


    public void getSignInAccount(Intent intent) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
        try {
            account = task.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            e.printStackTrace();
            account = null;
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
        if (eventListner != null) {
            eventListner.onGoogleSignInResult(account);
        }
    }

    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (eventListner != null) {
                            eventListner.onGoogleSignoutResult(true);
                        }
                        // ...
                    }
                });
    }

    public void setEventListner(EventListner eventListner) {
        this.eventListner = eventListner;
    }

    public interface EventListner {
        public void onGoogleSignInResult(GoogleSignInAccount account);
        public void onGoogleSignoutResult(boolean isSignOut);
    }


}
