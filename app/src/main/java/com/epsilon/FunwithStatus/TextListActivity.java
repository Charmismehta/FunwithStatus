package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.epsilon.FunwithStatus.adapter.TextListAdapter;
import com.epsilon.FunwithStatus.adapter.TrendingAdapter;
import com.epsilon.FunwithStatus.jsonpojo.textstatus.Status;
import com.epsilon.FunwithStatus.jsonpojo.trending.Trending;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextListActivity extends AppCompatActivity {

    Toolbar toolbar;
    Activity context;
    SwipeRefreshLayout swipelayout;
    RecyclerView recyclerView;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_list);
        context = this;
        idMapping();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        name = getIntent().getStringExtra("NAME");
        textstatus(name);


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
                textstatus(name);
                swipelayout.setRefreshing(false);
            }
        });
    }

    private void idMapping() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                    recyclerView.setAdapter(adapter);
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "No Video Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

//    public void trending() {
////        final ProgressDialog dialog = new ProgressDialog(context);
////        dialog.setCanceledOnTouchOutside(false);
////        dialog.setMessage("Please Wait...");
////        dialog.show();
////        final Call<Trending> countrycall = apiInterface.trendingpojo();
////        countrycall.enqueue(new Callback<Trending>() {
////            @Override
////            public void onResponse(Call<Trending> call, Response<Trending> response) {
////                dialog.dismiss();
////
////                if (Constants.trendingData != null) {
////                    Constants.trendingData.clear();
////                }
////                if (!Constants.trendingData.equals("") && Constants.statusData != null) {
////                    Constants.trendingData.addAll(response.body().getData());
////                    TrendingAdapter adapter = new TrendingAdapter(context);
////                    recyclerView.setAdapter(adapter);
////                } else {
////                    Toast.makeText(context, "No Status Found", Toast.LENGTH_SHORT).show();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<Trending> call, Throwable t) {
////                dialog.dismiss();
////            }
////        });
////    }

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
            if (Sessionmanager.getPreferenceBoolean(context, Constants.IS_LOGIN, false))
            {
                Intent it = new Intent(TextListActivity.this, AddTextActivity.class);
                it.putExtra("NAME", name);
                startActivity(it);
                finish();
            }
            else
            {

                Intent mainIntent = new Intent(context, LoginPage.class);
                startActivity(mainIntent);
                LayoutInflater inflater = getLayoutInflater();
                View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.llCustom));
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(toastLayout);
                toast.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}