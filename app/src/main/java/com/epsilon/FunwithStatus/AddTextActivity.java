package com.epsilon.FunwithStatus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.provider.FontRequest;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.vdurmont.emoji.EmojiParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTextActivity extends AppCompatActivity {
    Toolbar toolbar;
    EmojiconEditText edit_text;
    Context context;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    String subcat,u_name;
    Sessionmanager sessionmanager;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.epsilon.FunwithStatus.R.layout.activity_add_text);

        context = this;
        sessionmanager = new Sessionmanager(this);
        idMappings();
        subcat = getIntent().getStringExtra("NAME");
        u_name= sessionmanager.getValue(Sessionmanager.Name);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.vc_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("Add Text");
    }

    private void idMappings() {
        edit_text = (EmojiconEditText) findViewById(R.id.edit_text);


    }

    public void addstatus(String subcat,String u_name, final String status) {
        final Call<AddStatus> countrycall = apiInterface.addstatuspojo(subcat,u_name, status);
        countrycall.enqueue(new Callback<AddStatus>() {
            @Override
            public void onResponse(Call<AddStatus> call, Response<AddStatus> response) {
                Log.e("TEXT",status);
                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AddStatus> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onBackPressed() {
        Intent it = new Intent(AddTextActivity.this,TextListActivity.class);
        it.putExtra("NAME",subcat);
        startActivity(it);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuaddtext, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.vc_addtext) {
            if (!edit_text.getText().toString().equalsIgnoreCase("")) {
                String str= edit_text.getText().toString();
                String result = EmojiParser.parseToAliases(str);
                addstatus(subcat,u_name, result);
                Intent it = new Intent(AddTextActivity.this, TextListActivity.class);
                it.putExtra("NAME",subcat);
                startActivity(it);
                finish();
            } else {
                Toast.makeText(AddTextActivity.this, "Please Enter Text", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

}