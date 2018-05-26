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

public class RegistrationPage extends AppCompatActivity implements View.OnClickListener{
    EditText name, username, register_eemail, register_epassword,register_emobile;
    Button btnRegistration;
    Context context;
    TextView tbirthday;
    LinearLayout lbirthday;
    int mYear, mMonth, mDay;
    ImageView pwdeye;
    private int passwordNotVisible=1;
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
        lbirthday.setOnClickListener(this);
    }

    private void idMappings() {
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username);
        register_eemail = (EditText) findViewById(R.id.register_eemail);
        lbirthday=(LinearLayout)findViewById(R.id.lbirthday);
        register_epassword = (EditText) findViewById(R.id.register_epassword);
        tbirthday = (TextView) findViewById(R.id.tbirthday);
        btnRegistration = (Button) findViewById(R.id.btnRegistration);
        pwdeye = (ImageView) findViewById(R.id.pwdeye);
        register_emobile = (EditText) findViewById(R.id.register_emobile);
    }

    private void Listener() {

        btnRegistration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((name.getText().toString().length() == 0)) {
                    name.setError("Please Enter Name");
                } else if (username.getText().toString().length() == 0) {
                    username.setError("Please Enter Last Name");

                } else if (tbirthday.getText().toString().length() == 0) {
                    tbirthday.setError("Please Enter BirthDay");
                } else if (register_emobile.getText().toString().length() == 0) {
                    register_emobile.setError("Please Enter Mobile Number");

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
                        Registercall(name.getText().toString(), username.getText().toString(),
                                register_eemail.getText().toString(),
                                register_emobile.getText().toString(),
                                tbirthday.getText().toString(),
                                register_epassword.getText().toString());

                    } else {
                        Helper.showToastMessage(context, "No Internet Connection");
                    }
                }
            }

            private void Registercall(String name, String username, String email, String mobile, String birthdate,String password) {
                final ProgressDialog dialog = new ProgressDialog(context);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("Please Wait...");
                dialog.show();
                Call<Registration> registercall = apiInterface.registerPojoCall(name ,username,email,mobile, birthdate,password);

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
    public void onClick(View v) {
        if (v == lbirthday) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            tbirthday.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        }
    }
    @Override
    public void onBackPressed() {
        Intent i=new Intent(RegistrationPage.this,LoginPage.class);
        startActivity(i);
    }
}
