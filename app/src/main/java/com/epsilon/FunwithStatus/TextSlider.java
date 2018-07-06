package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.epsilon.FunwithStatus.adapter.RecycleviewAdapter;
import com.epsilon.FunwithStatus.adapter.TextListAdapter;
import com.epsilon.FunwithStatus.adapter.TextSlideAdapter;
import com.epsilon.FunwithStatus.adapter.TextTrensliderAdapter;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.vdurmont.emoji.EmojiParser;

public class TextSlider extends AppCompatActivity {
    Activity activity;
    ViewPager pager;
    ImageView share, like, dislike, copy, delete, whatsapp,facebook;
    String text, Id, name, email, u_name, loginuser;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    Sessionmanager sessionmanager;
    RelativeLayout rlayout;
    RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_slider);

        activity = this;
        sessionmanager = new Sessionmanager(this);
        idMappings();
        Listners();
        Intent mIntent = getIntent();
        final int position = mIntent.getIntExtra("position", 0);
        text = getIntent().getStringExtra("text");
        Id = getIntent().getStringExtra("Id");
        name = getIntent().getStringExtra("NAME");
        u_name = getIntent().getStringExtra("U_NAME");
        email = sessionmanager.getValue(Sessionmanager.Email);
        loginuser = sessionmanager.getValue(Sessionmanager.Name);


        if(name.equalsIgnoreCase("Trending") )
        {
            TextTrensliderAdapter adapter = new TextTrensliderAdapter(activity,name,u_name,position);
            pager.setAdapter(adapter);
            pager.post(new Runnable() {
                @Override
                public void run() {
                    pager.setCurrentItem(position,true);
                }
            });
        }
        else
            {
            TextSlideAdapter adapter = new TextSlideAdapter(activity, name, u_name,position);
            pager.setAdapter(adapter);
            pager.post(new Runnable() {
                @Override
                public void run() {
                    pager.setCurrentItem(position, true);
                }
            });
        }
    }

    private void Listners() {
    }


    private void idMappings() {
        pager = (ViewPager)findViewById(R.id.pager);
    }

    public void onBackPressed() {
        if (name.equalsIgnoreCase("Trending"))
        {
            Intent it = new Intent(activity, TextListActivity.class);
            it.putExtra("NAME", "Trending");
            startActivity(it);
            finish();// close this activity and return to preview activity (if there is any)
        }
        else
        {
            Intent it = new Intent(activity, TextListActivity.class);
            it.putExtra("NAME", name);
            startActivity(it);
            finish();
        }
    }

    }
