package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.epsilon.FunwithStatus.ImageListActivity;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.jsonpojo.textstatus.Status;
import com.epsilon.FunwithStatus.jsonpojo.trending.Trending;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.MyViewHolder> {
    Activity activity;
    private LayoutInflater inflater;
    public Resources res;
    Sessionmanager sessionmanager;
    String name;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    RecyclerView rv_text;
    RecyclerView.LayoutManager mLayoutManager;


    public RecycleviewAdapter(Activity a,String name) {
        this.activity = a;
        this.name = name;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        sessionmanager = new Sessionmanager(activity);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_item, parent, false);
        rv_text = itemView.findViewById(R.id.rv_text);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        rv_text.setHasFixedSize(true);
        // The number of Columns
        mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        rv_text.setLayoutManager(mLayoutManager);

        if (name.equalsIgnoreCase("Trending")) {
            trending();
        } else {
            textstatus(name);
        }

        holder.catname.setText(name);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return Constants.videoitems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView catname;

        public MyViewHolder(View item) {
            super(item);
            catname = (TextView)item.findViewById(R.id.catname);
        }
    }
    public void trending() {
        final ProgressDialog dialog = new ProgressDialog(activity);
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
                    TrendingAdapter adapter = new TrendingAdapter(activity);
                    rv_text.setAdapter(adapter);
                } else {
                    Toast.makeText(activity, "No Status Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Trending> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public void textstatus(String subcat) {
        final ProgressDialog dialog = new ProgressDialog(activity);
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
                    TextListAdapter adapter = new TextListAdapter(activity);
                    rv_text.setAdapter(adapter);
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(activity, "No Video Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}