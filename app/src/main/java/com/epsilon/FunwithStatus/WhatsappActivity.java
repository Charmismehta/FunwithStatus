package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WhatsappActivity extends AppCompatActivity {
    Activity activity;
    Button gallary,video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp);

        activity=this;
        gallary = (Button)findViewById(R.id.gallary);
        video = (Button)findViewById(R.id.video);


        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity,whatsappImage.class);
                startActivity(it);
                finish();
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity,whatsappVideo.class);
                startActivity(it);
                finish();
            }
        });
    }
    public void onBackPressed() {
        Intent it = new Intent(activity, Dashboard.class);
        startActivity(it);
        finish();
    }
}
