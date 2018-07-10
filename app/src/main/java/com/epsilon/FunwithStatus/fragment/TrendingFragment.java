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

import com.epsilon.FunwithStatus.AddOption;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.adapter.FirstFregmetnAdapter;
import com.epsilon.FunwithStatus.jsonpojo.mainhome.Home;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrendingFragment extends Fragment {
    Activity activity;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipelayout;
    FloatingActionButton floatingbtn;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        activity = getActivity();
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        swipelayout = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout);
        floatingbtn = (FloatingActionButton) view.findViewById(R.id.floatingbtn);

        if (!Helper.isConnectingToInternet(activity)) {
            Helper.showToastMessage(activity, "Please Connect Internet");
        } else {
            homepojo();
        }


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
                Intent it = new Intent(activity, AddOption.class);
                startActivity(it);
                activity.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    public void homepojo() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

        Call<Home> logincall = apiInterface.homepojo();
        logincall.enqueue(new Callback<Home>() {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response) {
                dialog.dismiss();
                if (response.body().getStatus() == 1) {
                    if (Constants.homedata != null) {
                        Constants.homedata.clear();
                    }
                    if (!Constants.homedata.equals("") && Constants.homedata != null) {
                        for (int i = 0; i < response.body().getData().getData().size(); i++) {
                            Constants.homedata.add(response.body().getData().getData().get(i));
                            Constants.home = response.body().getData();
                            FirstFregmetnAdapter adapter = new FirstFregmetnAdapter(activity);
                            recyclerView.setAdapter(adapter);
                            if (adapter != null)
                                adapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        } else{
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                }
            }
            @Override
            public void onFailure(Call<Home> call, Throwable t) {
                dialog.dismiss();
                Helper.showToastMessage(activity,"No Internet Connection");
            }
        });
    }

}
