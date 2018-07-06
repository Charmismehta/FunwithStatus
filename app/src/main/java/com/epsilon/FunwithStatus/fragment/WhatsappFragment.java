package com.epsilon.FunwithStatus.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.epsilon.FunwithStatus.Dashboard;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.whatsappImage;
import com.epsilon.FunwithStatus.whatsappVideo;


public class WhatsappFragment extends Fragment {
    Activity activity;
    Button gallary, video;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_whatsapp, container, false);
        activity = getActivity();
        gallary = (Button)view.findViewById(R.id.gallary);
        video = (Button)view.findViewById(R.id.video);


        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, whatsappImage.class);
                startActivity(it);
                getActivity().finish();
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, whatsappVideo.class);
                startActivity(it);
                getActivity().finish();
            }
        });

        return view;
    }

}