package com.epsilon.FunwithStatus.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.epsilon.FunwithStatus.whatsappImageSlideActivity;

public class MyGridAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    Sessionmanager sessionmanager;


    public MyGridAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sessionmanager = new Sessionmanager(context);
    }


    @Override
    public int getCount() {
        return Constants.items.size();
    }


    @Override
    public Object getItem(int position) {
        return Constants.items.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.subimage_item, null);
        }

        TextView text = (TextView) convertView.findViewById(R.id.username);
        ImageView like = (ImageView) convertView.findViewById(R.id.like);
        text.setVisibility(View.GONE);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.tvimage);
        final String image = Constants.items.get(position).getImage();
        String filenameArray[] = image.split("\\.");
        String extension = filenameArray[filenameArray.length - 1];
       like.setVisibility(View.GONE);

       if(extension.equalsIgnoreCase("jpg"))
        {
            Glide.with(context).load(image).thumbnail(Glide.with(context).load(R.drawable.load)).into(imageView);
        }
        else
        {
            Toast.makeText(context, "no image", Toast.LENGTH_SHORT).show();
        }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, whatsappImageSlideActivity.class);
                    it.putExtra("U_NAME", sessionmanager.getValue(Sessionmanager.Name));
                    it.putExtra("picture", Constants.items.get(position).getImage());
                    it.putExtra("NAME", "WHATSAPP");
                    it.putExtra("position", position);
                    Log.e("PATH", ":" + Constants.items.get(position).getImage());
                    context.startActivity(it);
                }
            });

        return convertView;
    }
}

