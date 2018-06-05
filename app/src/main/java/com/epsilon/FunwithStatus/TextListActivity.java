package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.adapter.TextListAdapter;
import com.epsilon.FunwithStatus.adapter.TrendingAdapter;
import com.epsilon.FunwithStatus.fragment.HomeFragment;
import com.epsilon.FunwithStatus.fragment.ImageFragment;
import com.epsilon.FunwithStatus.jsonpojo.login.Login;
import com.epsilon.FunwithStatus.jsonpojo.textstatus.Status;
import com.epsilon.FunwithStatus.jsonpojo.trending.Trending;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextListActivity extends AppCompatActivity {

    ListView lv_text_list;
    Toolbar toolbar;
    Activity context;
    SwipeRefreshLayout swipelayout;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_list);
        context = this;
        name = getIntent().getStringExtra("NAME");

        if (name.equalsIgnoreCase("Trending")) {
            trending();
        } else {
            textstatus(name);
        }

        idMapping();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.vc_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle(name);

        if (!Helper.isConnectingToInternet(context)) {
            Helper.showToastMessage(context, "Please Connect Internet");
        } else {

        }

        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (name.equalsIgnoreCase("Trending")) {
                    trending();
                } else {
                    textstatus(name);
                }
                swipelayout.setRefreshing(false);
            }
        });

        lv_text_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(name.equalsIgnoreCase("Trending")) {
                    Intent it = new Intent(TextListActivity.this, DisplayText.class);
                    it.putExtra("text", Constants.trendingData.get(position).getStatus());
                    it.putExtra("Id", Constants.trendingData.get(position).getId());
                    it.putExtra("NAME", name);
                    it.putExtra("U_NAME", Constants.trendingData.get(position).getUser());
                    startActivity(it);
                    finish();
                }
                else{
                    Intent it = new Intent(TextListActivity.this, DisplayText.class);
                    it.putExtra("text", Constants.statusData.get(position).getStatus());
                    it.putExtra("Id", Constants.statusData.get(position).getId());
                    it.putExtra("NAME", name);
                    it.putExtra("U_NAME", Constants.statusData.get(position).getUser());
                    startActivity(it);
                    finish();
                }
            }
        });

    }


    private void idMapping() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lv_text_list = (ListView) findViewById(R.id.lv_text_list);
        swipelayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
    }


    public void textstatus(String subcat) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<Status> countrycall = apiInterface.textstatuspojo(subcat);
        countrycall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                dialog.dismiss();

                if (Constants.statusData != null) {
                    Constants.statusData.clear();
                }
                if (!Constants.statusData.equals("") && Constants.statusData != null) {
                    Constants.statusData.addAll(response.body().getData());
                    TextListAdapter adapter = new TextListAdapter(context);
                    lv_text_list.setAdapter(adapter);
                } else {
                    Toast.makeText(context, "No Status Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void trending() {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<Trending> countrycall = apiInterface.trendingpojo();
        countrycall.enqueue(new Callback<Trending>() {
            @Override
            public void onResponse(Call<Trending> call, Response<Trending> response) {
                dialog.dismiss();

                if (Constants.trendingData != null) {
                    Constants.trendingData.clear();
                }
                if (!Constants.trendingData.equals("") && Constants.statusData != null) {
                    Constants.trendingData.addAll(response.body().getData());
                    TrendingAdapter adapter = new TrendingAdapter(context);
                    lv_text_list.setAdapter(adapter);
                } else {
                    Toast.makeText(context, "No Status Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Trending> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuimage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                this.finish();
            } else {
                getFragmentManager().popBackStack();
            }
//            finish(); // close this activity and return to preview activity (if there is any)
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.vc_addtoolbar) {
            Intent it = new Intent(TextListActivity.this, AddTextActivity.class);
            it.putExtra("NAME", name);
            startActivity(it);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}