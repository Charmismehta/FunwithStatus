package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.adapter.TextAdapter;
import com.epsilon.FunwithStatus.adapter.TextListAdapter;
import com.epsilon.FunwithStatus.fragment.HomeFragment;
import com.epsilon.FunwithStatus.fragment.ImageFragment;
import com.epsilon.FunwithStatus.jsonpojo.category_text.Category;
import com.epsilon.FunwithStatus.jsonpojo.login.Login;
import com.epsilon.FunwithStatus.jsonpojo.textstatus.Status;
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
    TextView title;
    Activity context;
    ImageView ileft, iright;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_list);
        context = this;
        Intent mIntent = getIntent();
        int i = mIntent.getIntExtra("position", 0);
        final String name = Constants.subCategories.get(i).getName();

        idMapping();

        iright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TextListActivity.this, AddTextActivity.class);
                it.putExtra("SUBCAT",name);
                startActivity(it);
            }
        });

        ileft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                {
                    getSupportFragmentManager().popBackStack();
                } else
                {
                   finish();
                }
            }
        });
        lv_text_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(TextListActivity.this,DisplayText.class);
                it.putExtra("text",Constants.statusData.get(position).getStatus());
                it.putExtra("Id",Constants.statusData.get(position).getId());
                it.putExtra("SUBCAT",name);
                startActivity(it);
            }
        });

        if (!Helper.isConnectingToInternet(context)) {
            Helper.showToastMessage(context, "Please Connect Internet");
        } else {
            textstatus(Constants.subCategories.get(i).getName());
        }

        iright.setImageResource(R.drawable.addmember);
        ileft.setImageResource(R.drawable.back);
        title.setText("");
    }


    private void idMapping() {
        lv_text_list = (ListView) findViewById(R.id.lv_text_list);
        title = (TextView) findViewById(R.id.title);
        iright = (ImageView) findViewById(R.id.iright);
        ileft = (ImageView) findViewById(R.id.ileft);
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
                Constants.statusData.addAll(response.body().getData());
                TextListAdapter adapter = new TextListAdapter(context);
                lv_text_list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
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
}
