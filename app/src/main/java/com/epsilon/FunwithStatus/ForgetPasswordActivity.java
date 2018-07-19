package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.jsonpojo.forgetpwd.ForgotPwd;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends BaseActivity {

    Activity activity;
    EditText email;
    TextView btnok,btnback;
    APIInterface apiInterface ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        activity = this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        email = (EditText)findViewById(R.id.email);
        btnok = (TextView) findViewById(R.id.btnok);
        btnback = (TextView) findViewById(R.id.btnback);

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Enter Email Id", Toast.LENGTH_SHORT).show();
                } else {
                    forgot(email.getText().toString());
                }
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(),LoginPageActivity.class);
                startActivity(it);
                finish();
            }
        });

    }

    public void forgot(String email) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<ForgotPwd> countrycall = apiInterface.forgotpwdpojo(email);
        countrycall.enqueue(new Callback<ForgotPwd>() {
            @Override
            public void onResponse(Call<ForgotPwd> call, Response<ForgotPwd> response) {
                dialog.dismiss();
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                Intent it = new Intent(getActivity(),LoginPageActivity.class);
                startActivity(it);
                finish();
            }

            @Override
            public void onFailure(Call<ForgotPwd> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

}
