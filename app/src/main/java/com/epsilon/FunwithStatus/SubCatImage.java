package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.epsilon.FunwithStatus.adapter.SubalbumAdapter;
import com.epsilon.FunwithStatus.adapter.TrendingAdapter;
import com.epsilon.FunwithStatus.adapter.TrendingImgAdapter;
import com.epsilon.FunwithStatus.jsonpojo.tending_img.TrendingImg;
import com.epsilon.FunwithStatus.jsonpojo.trending.Trending;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.SubAlbum;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCatImage extends AppCompatActivity {
    Activity context;
    Toolbar toolbar;
    String name;
    private RecyclerView recyclerView;
    private SubalbumAdapter adapter;
    private List<SubAlbum> albumList;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat_image);
        context = this;

        name = getIntent().getStringExtra("NAME");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.vc_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle(name);


        albumList = new ArrayList<>();
        adapter = new SubalbumAdapter(context, albumList,name);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if(name.equalsIgnoreCase("Featured"))
        {
            TrandingAlbums();
        }

        if(name.equalsIgnoreCase("Laughter"))
        {
            LaughterAlbums();
        }

        if(name.equalsIgnoreCase("Emotions"))
        {
            EmotionsAlbums();
        }

        if(name.equalsIgnoreCase("Life"))
        {
            LifeAlbums();
        }

        if(name.equalsIgnoreCase("Wishes"))
        {
            WishAlbums();
        }

        if(name.equalsIgnoreCase("Sports"))
        {
            SportsAlbums();
        }

        if(name.equalsIgnoreCase("Language"))
        {
            LanguageAlbums();
        }

        if(name.equalsIgnoreCase("Entertainment"))
        {
            EntertainmentAlbums();
        }

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }


    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
        } else
        {
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
    private void TrandingAlbums() {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait...");
            dialog.show();
            final Call<TrendingImg> countrycall = apiInterface.trendingimgpojo();
            countrycall.enqueue(new Callback<TrendingImg>() {
                @Override
                public void onResponse(Call<TrendingImg> call, Response<TrendingImg> response) {
                    dialog.dismiss();

                    if (Constants.trendingimgData != null) {
                        Constants.trendingimgData.clear();
                    }
                    if (!Constants.trendingimgData.equals("") && Constants.trendingimgData != null) {
                        Constants.trendingimgData.addAll(response.body().getData());
                        TrendingImgAdapter adapter = new TrendingImgAdapter(context);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(context, "No Status Found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TrendingImg> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            }
    private void LaughterAlbums() {
        int[] covers = new int[]{
                R.drawable.politics};

        SubAlbum a = new SubAlbum("Politics",  covers[0]);
        albumList.add(a);
        adapter.notifyDataSetChanged();
    }
    private void EmotionsAlbums() {
        int[] covers = new int[]{
                R.drawable.love,
                R.drawable.flirt,
                R.drawable.sad,
                R.drawable.happy,
                R.drawable.missyou};

        SubAlbum a = new SubAlbum("Love", covers[0]);
        albumList.add(a);

        a = new SubAlbum("Flirt",  covers[1]);
        albumList.add(a);

        a = new SubAlbum("Sad", covers[2]);
        albumList.add(a);

        a = new SubAlbum("Happy",  covers[3]);
        albumList.add(a);

        a = new SubAlbum("Miss You",  covers[4]);
        albumList.add(a);

        adapter.notifyDataSetChanged();
    }
    private void LifeAlbums() {
        int[] covers = new int[]{
                R.drawable.family,
                R.drawable.couple,
                R.drawable.students,
                R.drawable.friend};

        SubAlbum a = new SubAlbum("Family", covers[0]);
        albumList.add(a);

        a = new SubAlbum("Couple",  covers[1]);
        albumList.add(a);

        a = new SubAlbum("Student",  covers[2]);
        albumList.add(a);

        a = new SubAlbum("Friend", covers[3]);
        albumList.add(a);
        adapter.notifyDataSetChanged();
    }

    private void WishAlbums() {
        int[] covers = new int[]{
                R.drawable.gm,
                R.drawable.gn,
                R.drawable.congratulation,
                R.drawable.anniversary,
                R.drawable.birthday};

        SubAlbum a = new SubAlbum("Good Morning",  covers[0]);
        albumList.add(a);

        a = new SubAlbum("Good Night", covers[1]);
        albumList.add(a);

        a = new SubAlbum("Congratulations", covers[2]);
        albumList.add(a);

        a = new SubAlbum("Anniversary", covers[3]);
        albumList.add(a);

        a = new SubAlbum("Birthday", covers[4]);
        albumList.add(a);

        adapter.notifyDataSetChanged();
    }

    private void SportsAlbums() {
        int[] covers = new int[]{
                R.drawable.cricket,
                R.drawable.othersports};

        SubAlbum a = new SubAlbum("Cricket",  covers[0]);
        albumList.add(a);

        a = new SubAlbum("Other Sports",  covers[1]);
        albumList.add(a);
        adapter.notifyDataSetChanged();
    }

    private void LanguageAlbums() {
        int[] covers = new int[]{
                R.drawable.gujrati,
                R.drawable.english,
                R.drawable.hindi,
                R.drawable.marathi};

        SubAlbum a = new SubAlbum("Gujarati",  covers[0]);
        albumList.add(a);

        a = new SubAlbum("English",  covers[1]);
        albumList.add(a);

        a = new SubAlbum("Hindi",  covers[2]);
        albumList.add(a);

        a = new SubAlbum("Marathi",  covers[3]);
        albumList.add(a);
        adapter.notifyDataSetChanged();
    }

    private void EntertainmentAlbums() {
        int[] covers = new int[]{
                R.drawable.bollywood,
                R.drawable.hollywood,
                R.drawable.tollywood};

        SubAlbum a = new SubAlbum("BollyWood",  covers[0]);
        albumList.add(a);

        a = new SubAlbum("HollyWood",  covers[1]);
        albumList.add(a);

        a = new SubAlbum("TollyWood",  covers[2]);
        albumList.add(a);
        adapter.notifyDataSetChanged();
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
        return super.onOptionsItemSelected(item);
    }

}
