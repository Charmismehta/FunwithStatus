package com.epsilon.FunwithStatus;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationPage extends AppCompatActivity {
    EditText name,register_eemail, register_epassword;
    Context context;
    LinearLayout login_tsignin;
    ImageView pwdeye;
    private int passwordNotVisible=1;
    TextView btnRegistration;
Sessionmanager sessionmanager;

    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        context=this;
        sessionmanager = new Sessionmanager(this);
        idMappings();
        Listener();

        login_tsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(RegistrationPage.this,LoginPage.class);
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
                } else if (register_epassword.getText().toString().length() < 8) {
                    register_epassword.setError("Please Enter Minimum 8 Digit");
                } else {
                    if (Helper.isConnectingToInternet(context)) {
                        Registercall(name.getText().toString(),
                                register_eemail.getText().toString(),
                                register_epassword.getText().toString());

                    } else {
                        Helper.showToastMessage(context, "No Internet Connection");
                    }
                }


            }

            private void Registercall(String name, String email, String password) {
                final ProgressDialog dialog = new ProgressDialog(context);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("Please Wait...");
                dialog.show();
                Call<Registration> registercall = apiInterface.registerPojoCall(name ,email,password);

                registercall.enqueue(new Callback<Registration>() {
                    @Override
                    public void onResponse(Call<Registration> call, Response<Registration> response) {
                        dialog.dismiss();
                        if (response.body().getStatus().equals("Success")) {
                            sessionmanager.createSession_userRegister((response.body().getData()));
                            Sessionmanager.setPreferenceBoolean(RegistrationPage.this, Constants.IS_LOGIN,true);
                            Toast.makeText(RegistrationPage.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(RegistrationPage.this, Dashboard.class);
                            it.putExtra("login_name",response.body().getData().getUser().getName());
                            startActivity(it);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(RegistrationPage.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
        Intent i=new Intent(RegistrationPage.this,LoginPage.class);
        startActivity(i);
    }
}
