package com.epsilon.FunwithStatus.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.adapter.TextAdapter;

import com.epsilon.FunwithStatus.jsonpojo.category_text.Category;
import com.epsilon.FunwithStatus.jsonpojo.category_text.CategoryDatum;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    Context context;
    ListView textlist_view;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    List<CategoryDatum> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle(Html.fromHtml("<font color='#ffffff'> Texts </font>"));
        context = getContext();
        data = new ArrayList<>();
        textlist_view = (ListView)view.findViewById(R.id.textlist_view);

        if (!Helper.isConnectingToInternet(context)) {
            Helper.showToastMessage(context, "Please Connect Internet");
        } else {
            CategoryList();
        }

        AdView mAdView = view.findViewById(R.id.adView);
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
        return view;

    }

    public void CategoryList() {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<Category> countrycall = apiInterface.textpojo();
        countrycall.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                dialog.dismiss();
                data = response.body().getCatagory();

                if (Constants.textcategory != null) {
                    Constants.textcategory.clear();
                }
                Constants.textcategory.addAll(response.body().getCatagory());
                TextAdapter textAdapter = new TextAdapter(context);
                textlist_view.setAdapter(textAdapter);
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}

