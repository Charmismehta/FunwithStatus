package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.epsilon.FunwithStatus.adapter.VideoAdapter;
import com.epsilon.FunwithStatus.fragment.VideoFragment;
import com.epsilon.FunwithStatus.jsonpojo.videolist.VideoList;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoListActivity extends BaseActivity {

    Activity context;
    RecyclerView recyclerView;
    FloatingActionButton floatingbtn;
    APIInterface apiInterface ;
    SwipeRefreshLayout swipelayout;
    int category_id;
    RelativeLayout main;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        context = this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Intent mIntent = getIntent();
        category_id = mIntent.getIntExtra("ID", 0);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        swipelayout=(SwipeRefreshLayout)findViewById(R.id.swipelayout);
        main=(RelativeLayout) findViewById(R.id.main);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        floatingbtn = (FloatingActionButton)findViewById(R.id.floatingbtn);

        if (!Helper.isConnectingToInternet(context)) {
            Helper.showToastMessage(context, "Please Connect Internet");
        } else {
            videolist(category_id);
        }


        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                videolist(category_id);
                swipelayout.setRefreshing(false);
            }
        });

        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Sessionmanager.getPreferenceBoolean(context, Constants.IS_LOGIN, false))
                {
                    Intent it = new Intent(getActivity(),AddVideoActivity.class);
                    it.putExtra("ID",category_id);
                    startActivity(it);
                }
                else
                {
                    Intent mainIntent = new Intent(context, LoginPageActivity.class);
                    startActivity(mainIntent);
                    LayoutInflater inflater = getLayoutInflater();
                    View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup)view.findViewById(R.id.llCustom));
                    Toast toast = new Toast(getActivity());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(toastLayout);
                    toast.show();
                }


            }
        });
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

    @Override
    public void onPause() {
        super.onPause();
//        JZVideoPlayer.releaseAllVideos();
    }
    private void videolist(int categories_id) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<VideoList> countrycall = apiInterface.videolist(categories_id);
        countrycall.enqueue(new Callback<VideoList>() {
            @Override
            public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                dialog.dismiss();

                if (Constants.videoListData != null) {
                    Constants.videoListData.clear();
                }
                if (!Constants.videoListData.equals("") && Constants.videoListData != null) {
                    Constants.videoListData.addAll(response.body().data.data);
                    VideoAdapter adapter = new VideoAdapter(context);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(context, "No Video Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoList> call, Throwable t) {
                dialog.dismiss();
                Helper.showToastMessage(context, "Please Connect Internet");
            }
        });
    }

        @Override
        public void onBackPressed() {
            main.setVisibility(View.GONE);
            Fragment fragment = new VideoFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.containers, fragment);
            transaction.commit();
        }
}
