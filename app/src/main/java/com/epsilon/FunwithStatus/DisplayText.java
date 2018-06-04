package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.adapter.TextListAdapter;
import com.epsilon.FunwithStatus.jsonpojo.addlike.AddLike;
import com.epsilon.FunwithStatus.jsonpojo.deleteimage.DeleteImage;
import com.epsilon.FunwithStatus.jsonpojo.deletetext.DeleteText;
import com.epsilon.FunwithStatus.jsonpojo.dislike.DisLike;
import com.epsilon.FunwithStatus.jsonpojo.registration.Registration;
import com.epsilon.FunwithStatus.jsonpojo.textstatus.Status;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.rockerhieu.emojicon.EmojiconTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DisplayText extends AppCompatActivity {

    EmojiconTextView display_text;
    ImageView share, like, dislike, copy,delete;
    Activity activity;
    String text, Id, name, email,u_name,loginuser;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    Sessionmanager sessionmanager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_text);
        activity = this;
        sessionmanager = new Sessionmanager(this);
        idMappings();
        Listners();
        text = getIntent().getStringExtra("text");
        Id = getIntent().getStringExtra("Id");
        name = getIntent().getStringExtra("NAME");
        u_name = getIntent().getStringExtra("U_NAME");
        email = sessionmanager.getValue(Sessionmanager.Email);
        loginuser = sessionmanager.getValue(Sessionmanager.Name);
        display_text.setText(text);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.vc_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle(name);

        if(loginuser.equalsIgnoreCase(u_name))
        {
            delete.setVisibility(View.VISIBLE);
        }
    }

    private void Listners()
    {
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) activity.getSystemService(activity.CLIPBOARD_SERVICE);
                cm.setText(display_text.getText());
                Toast.makeText(activity, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                share.putExtra(Intent.EXTRA_TEXT, display_text.getText().toString());

                activity.startActivity(Intent.createChooser(share, "Share link!"));
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addlike(name, email, Id, text);
//                addlike(String category,String email,String status_id,String status)
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dislike(name, email, Id, text);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(Id);
            }
        });
    }

    private void idMappings() {
        display_text = (EmojiconTextView) findViewById(R.id.display_text);
        copy = (ImageView) findViewById(R.id.copy);
        like = (ImageView) findViewById(R.id.like);
        dislike = (ImageView) findViewById(R.id.dislike);
        share = (ImageView) findViewById(R.id.share);
        delete = (ImageView) findViewById(R.id.delete);


    }

    public void addlike(String category, String email, String status_id, String status) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<AddLike> countrycall = apiInterface.addlikepojo(category, email, status_id, status);
        countrycall.enqueue(new Callback<AddLike>() {
            @Override
            public void onResponse(Call<AddLike> call, Response<AddLike> response) {
                dialog.dismiss();
                Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                textstatus(name);
            }

            @Override
            public void onFailure(Call<AddLike> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void dislike(String category, String email, String status_id, String status) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<DisLike> countrycall = apiInterface.dislikepojo(category, email, status_id, status);
        countrycall.enqueue(new Callback<DisLike>() {
            @Override
            public void onResponse(Call<DisLike> call, Response<DisLike> response) {
                dialog.dismiss();
                Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                textstatus(name);
            }

            @Override
            public void onFailure(Call<DisLike> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // TODO : TEXT DELETE API >>

    public void delete(String id) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<DeleteText> countrycall = apiInterface.deletetext(id);
        countrycall.enqueue(new Callback<DeleteText>() {
            @Override
            public void onResponse(Call<DeleteText> call, Response<DeleteText> response) {
                dialog.dismiss();
                if (response.body().getStatus().equals("Succ")) {
                    Intent it = new Intent(activity,TextListActivity.class);
                    it.putExtra("NAME",name);
                    startActivity(it);
                    finish();
                    Toast.makeText(activity, "Delete Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(activity,"Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteText> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // TODO TEXT DELETE API END


    public void onBackPressed() {
        Intent it = new Intent(DisplayText.this, TextListActivity.class);
        it.putExtra("NAME",name);
        startActivity(it);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            Intent it = new Intent(activity,TextListActivity.class);
            it.putExtra("NAME",name);
            startActivity(it);
            finish();
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public void textstatus(String subcat) {
        final Call<Status> countrycall = apiInterface.textstatuspojo(subcat);
        countrycall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (Constants.statusData != null) {
                    Constants.statusData.clear();
                }
                if(!Constants.statusData.equals("") && Constants.statusData != null) {
                    Constants.statusData.addAll(response.body().getData());
                }
                else
                {
                }
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}