package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Categoriesoption extends AppCompatActivity {
    Activity activity;
    TextView title;
    ImageView ileft,iright;
    EditText hashtag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoriesoption);
        activity = this;
        title = (TextView)findViewById(R.id.title);
        ileft = (ImageView)findViewById(R.id.ileft);
        iright = (ImageView)findViewById(R.id.iright);
        hashtag = (EditText)findViewById(R.id.hashtag);

        title.setText("Categories");
        ileft.setVisibility(View.GONE);
        iright.setImageResource(R.drawable.vc_done);

        iright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hashtag.equals("") && hashtag != null)
                {
                    Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show();
                }
                else 
                {
                    Toast.makeText(activity, "select Category", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
    }

    public void onBackPressed() {
            Intent it = new Intent(activity, Dashboard.class);
            startActivity(it);
            finish();
        }
}
