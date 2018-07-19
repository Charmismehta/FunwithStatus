package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.epsilon.FunwithStatus.adapter.ImageListAdapter;
import com.epsilon.FunwithStatus.jsonpojo.image_list.ImageList;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageListActivity extends AppCompatActivity {

    Activity context;
    TextView title;
    ImageView ileft, iright;
    private RecyclerView recyclerView;
    String name;
    int Id;
    APIInterface apiInterface;
    SwipeRefreshLayout swipelayout;
    Sessionmanager sessionmanager;
    EditText edit_caption;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        context = this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionmanager = new Sessionmanager(this);
        idMappings();
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        Intent mIntent = getIntent();
        Id = mIntent.getIntExtra("ID", 0);
        name = getIntent().getStringExtra("NAME");

        if (!Helper.isConnectingToInternet(context)) {
            Helper.showToastMessage(context, "Please Connect Internet");
        } else {
            Imagecategory(Id);
        }

        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Imagecategory(Id);
                swipelayout.setRefreshing(false);
            }
        });

        ileft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        title.setText(name);
        adView = new AdView(this, getString(R.string.Bannerplacement_id), AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adContainer.addView(adView);
        adView.loadAd();

        iright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Sessionmanager.getPreferenceBoolean(context, Constants.IS_LOGIN, false))
//                {
                    Intent it = new Intent(context,DemoActivity.class);
                    it.putExtra("ID",Id);
                    it.putExtra("NAME",name);
                    startActivity(it);
                    finish();
//                }
//                else
//                {
//                    Intent mainIntent = new Intent(context, LoginPage.class);
//                    startActivity(mainIntent);
//                    LayoutInflater inflater = getLayoutInflater();
//                    View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.llCustom));
//                    Toast toast = new Toast(getApplicationContext());
//                    toast.setDuration(Toast.LENGTH_LONG);
//                    toast.setView(toastLayout);
//                    toast.show();
//                }
            }
        });
    }
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            this.finish();
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private void idMappings() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipelayout=(SwipeRefreshLayout) findViewById(R.id.swipelayout);
        title=(TextView) findViewById(R.id.title);
        ileft=(ImageView) findViewById(R.id.ileft);
        iright=(ImageView) findViewById(R.id.iright);
    }

    public void Imagecategory(int subcat) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<ImageList> countrycall = apiInterface.imagelistpojo(subcat);
        countrycall.enqueue(new Callback<ImageList>() {
            @Override
            public void onResponse(Call<ImageList> call, Response<ImageList> response) {
                dialog.dismiss();

                Log.e("imageList--> ", response.toString());

                if (response.body().status == 1) {
                    if (Constants.imageListData != null) {
                        Constants.imageListData.clear();
                    }
                    List<Object> recipeList = new ArrayList<>();
                    recipeList.add(response.body());
                    Constants.imageListData.addAll(response.body().data.data);
                    ImageListAdapter adapter = new ImageListAdapter(context);
                    recyclerView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(context,response.body().msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageList> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

}



