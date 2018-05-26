package com.epsilon.FunwithStatus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.adapter.TextListAdapter;
import com.epsilon.FunwithStatus.jsonpojo.addstatus.AddStatus;
import com.epsilon.FunwithStatus.jsonpojo.textstatus.Status;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTextActivity extends AppCompatActivity {
    ImageView ileft, iright;
    TextView title;
    EditText edit_text;
    Context context;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.epsilon.FunwithStatus.R.layout.activity_add_text);

        context = this;
        idMappings();
        final String subcat = getIntent().getStringExtra("SUBCAT");

        ileft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(AddTextActivity.this, TextListActivity.class);
                startActivity(it);
            }
        });
        iright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edit_text.getText().toString().equalsIgnoreCase("")) {
                    addstatus(subcat, edit_text.getText().toString());
                    Toast.makeText(AddTextActivity.this, "Add Text Successfully", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(AddTextActivity.this, TextListActivity.class);
                    startActivity(it);
                } else {
                    Toast.makeText(AddTextActivity.this, "Please Enter Text", Toast.LENGTH_SHORT).show();
                }


            }
        });

        title.setText("Add Text");
        ileft.setImageResource(R.drawable.back);
        iright.setImageResource(R.drawable.done);

    }

    private void idMappings() {
        ileft = (ImageView) findViewById(R.id.ileft);
        iright = (ImageView) findViewById(R.id.iright);
        title = (TextView) findViewById(R.id.title);
        edit_text = (EditText) findViewById(R.id.edit_text);

    }

    public void addstatus(String subcat, String status) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<AddStatus> countrycall = apiInterface.addstatuspojo(subcat, status);
        countrycall.enqueue(new Callback<AddStatus>() {
            @Override
            public void onResponse(Call<AddStatus> call, Response<AddStatus> response) {
                dialog.dismiss();
                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AddStatus> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onBackPressed() {
        Intent it = new Intent(AddTextActivity.this,TextListActivity.class);
        startActivity(it);
        finish();
    }
}