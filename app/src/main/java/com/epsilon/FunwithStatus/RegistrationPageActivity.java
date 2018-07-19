package com.epsilon.FunwithStatus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.jsonpojo.registration.Registration;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationPageActivity extends BaseActivity {
    EditText name,register_eemail, register_epassword;
    Context context;
    LinearLayout login_tsignin;
    ImageView pwdeye;
    private int passwordNotVisible=1;
    TextView btnRegistration;
Sessionmanager sessionmanager;

    APIInterface apiInterface ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        context=this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionmanager = new Sessionmanager(this);
        idMappings();
        Listener();


        login_tsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(RegistrationPageActivity.this,LoginPageActivity.class);
                startActivity(it);
                finish();
            }
        });
    }

    private void idMappings() {
        name = (EditText) findViewById(R.id.name);
        register_eemail = (EditText) findViewById(R.id.register_eemail);
        register_epassword = (EditText) findViewById(R.id.register_epassword);
        login_tsignin = (LinearLayout) findViewById(R.id.login_tsignin);
        pwdeye = (ImageView) findViewById(R.id.pwdeye);
        btnRegistration = (TextView) findViewById(R.id.btnRegistration);
    }

    private void Listener() {

        btnRegistration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((name.getText().toString().length() == 0)) {
                    name.setError("Please Enter Name");
                } else if (register_eemail.getText().toString().length() == 0) {
                    register_eemail.setError("Please Enter Email Address");

                } else if (!(isValidEmail(register_eemail.getText().toString()))) {
                    register_eemail.setError("Please Enter Valid Email ID");
                } else if (register_epassword.getText().toString().length() == 0) {
                    register_epassword.setError("Please Enter Password");
                } else if (register_epassword.getText().toString().length() < 6) {
                    register_epassword.setError("Please Enter Minimum 6 Digit");
                } else {
                    if (Helper.isConnectingToInternet(getActivity())) {
                        Registercall("email",name.getText().toString(),
                                register_eemail.getText().toString(),
                                register_epassword.getText().toString());

                    } else {
                        Helper.showToastMessage(getActivity(), "No Internet Connection");
                    }
                }


            }

            private void Registercall(String login_type,String name, String email, String password) {
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("Please Wait...");
                dialog.show();
                Call<Registration> registercall = apiInterface.registerPojoCall(login_type,name,email,password);

                registercall.enqueue(new Callback<Registration>() {
                    @Override
                    public void onResponse(Call<Registration> call, Response<Registration> response) {
                        dialog.dismiss();
                        if (response.body().getStatus() == 1){
                            sessionmanager.createSession_userRegister((response.body().getData()));
                            Sessionmanager.setPreferenceBoolean(RegistrationPageActivity.this, Constants.IS_LOGIN,true);
                            Toast.makeText(RegistrationPageActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(RegistrationPageActivity.this, Dashboard.class);
                            it.putExtra("login_name",response.body().getData().getUser().getName());
                            startActivity(it);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(RegistrationPageActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                    @Override
                    public void onFailure(Call<Registration> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
            }
        });
        pwdeye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordNotVisible == 1) {
                    register_epassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordNotVisible = 0;
                    pwdeye.setImageResource(R.drawable.registrathioneye);
                } else {

                    register_epassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordNotVisible = 1;
                    pwdeye.setImageResource(R.drawable.eyeclosed);
                }
            }
        });
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(RegistrationPageActivity.this,LoginPageActivity.class);
        startActivity(i);
    }
}
