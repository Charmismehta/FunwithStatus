package com.epsilon.FunwithStatus.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.epsilon.FunwithStatus.AddOptionActivity;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.adapter.FirstFregmetnAdapter;
import com.epsilon.FunwithStatus.jsonpojo.imagedislike.ImageDislike;
import com.epsilon.FunwithStatus.jsonpojo.mainhome.Home;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.facebook.ads.NativeAdsManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrendingFragment extends Fragment {
    Activity activity;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipelayout;
    FloatingActionButton floatingbtn;
    APIInterface apiInterface;
    LinearLayoutManager mLayoutManager;
    int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        activity = getActivity();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipelayout = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout);
        floatingbtn = (FloatingActionButton) view.findViewById(R.id.floatingbtn);
        homepojo();

        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homepojo();
                swipelayout.setRefreshing(false);
            }
        });

        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, AddOptionActivity.class);
                startActivity(it);
                activity.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);


        return view;
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

            if ((visibleItemCount + firstVisibleItem) >= totalItemCount
                    && firstVisibleItem >= 0
                    && totalItemCount >= Constants.home.perPage) {
                page++;

                if (page <= Constants.home.lastPage) {
                    page++;
                    homepojo();
                }
            }
        }
    };

    public void homepojo() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<Home> countrycall = apiInterface.getHomeDataList(page);
        countrycall.enqueue(new Callback<Home>() {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response) {
                dialog.dismiss();
                if (page == 1){
                    if (Constants.homedata != null) {
                        Constants.homedata.clear();
                    }
                }

                if (!Constants.homedata.equals("") && Constants.homedata != null) {
                    for (int i = 0; i < response.body().data.data.size(); i++) {
                        Constants.homedata.add(response.body().data.data.get(i));
                        Constants.home = response.body().data;
                        FirstFregmetnAdapter adapter = new FirstFregmetnAdapter(activity);
                        recyclerView.setAdapter(adapter);
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<Home> call, Throwable t) {
                Toast.makeText(activity, "Please Connect Internet", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
    }

}
