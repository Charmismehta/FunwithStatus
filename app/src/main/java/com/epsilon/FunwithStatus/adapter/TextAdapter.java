package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.TextListActivity;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.jsonpojo.category_text.Category;
import com.epsilon.FunwithStatus.jsonpojo.category_text.SubCategory;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextAdapter extends BaseAdapter {
    Context activity;
    private LayoutInflater inflater;
    public Resources res;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    Sessionmanager sessionmanager;
    int count;
    List<SubCategory> subCategories = new ArrayList<>();
    String name;

    public TextAdapter(Context a) {
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        sessionmanager = new Sessionmanager(activity);
    }
    @Override
    public int getCount() {
        return  Constants.textcategory.size();
    }

    @Override
    public Object getItem(int i) {
        return  Constants.textcategory.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final MyViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.text_item, viewGroup, false);
            viewHolder = new MyViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }
        viewHolder.tvcategory_name.setText( Constants.textcategory.get(i).getName());
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "fonts/Jokerman-Regular.ttf");
        viewHolder.tvcategory_name.setTypeface(face);

        Call<Category> followerlistCall = apiInterface.textpojo();

        followerlistCall.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.body().getStatus().equals("Success")) {
                    if(Constants.subCategories != null)
                    {
                        Constants.subCategories.clear();
                    }
                    Constants.subCategories.addAll(response.body().getCatagory().get(i).get0());
                    subCategories = response.body().getCatagory().get(i).get0();
                    TextPicAdapter adapter = new TextPicAdapter(activity,subCategories);
                    count = Constants.textcategory.get(i).get0().size();
                    if(count >= 4)
                    {
                        viewHolder.textgrid_view2.setVisibility(View.VISIBLE);
                        viewHolder.textgrid_view2.setAdapter(adapter);

                    }
                    else
                    {
                        viewHolder.textgrid_view1.setVisibility(View.VISIBLE);
                        viewHolder.textgrid_view1.setAdapter(adapter);
                    }

                } else {
                    Toast.makeText(activity, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.textgrid_view1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long l) {
                final String subcat = Constants.textcategory.get(i).getName();
                Intent it = new Intent(view.getContext(), TextListActivity.class);
                it.putExtra("SUBCAT_NAME",subcat);
                it.putExtra("position",position);
                view.getContext().startActivity(it);
            }
        });
        viewHolder.textgrid_view2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long l) {
                final String subcat = Constants.textcategory.get(i).getName();
                Intent it = new Intent(view.getContext(), TextListActivity.class);
                it.putExtra("SUBCAT_NAME",subcat);
                it.putExtra("position",position);
                view.getContext().startActivity(it);
            }
        });

        return view;
    }

    class MyViewHolder {
        public TextView tvcategory_name;
        public GridView textgrid_view1,textgrid_view2;

        public MyViewHolder(final View item) {
            tvcategory_name = (TextView) item.findViewById(R.id.tvcategory_name);
            textgrid_view1 = (GridView)item.findViewById(R.id.textgrid_view1);
            textgrid_view2 = (GridView)item.findViewById(R.id.textgrid_view2);

        }
    }
}
