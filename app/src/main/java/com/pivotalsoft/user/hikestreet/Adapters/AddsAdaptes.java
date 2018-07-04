package com.pivotalsoft.user.hikestreet.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pivotalsoft.user.hikestreet.AddsJobDetailsActivity;
import com.pivotalsoft.user.hikestreet.Items.AddCardItem;
import com.pivotalsoft.user.hikestreet.R;

import java.util.ArrayList;

public class AddsAdaptes extends PagerAdapter {

    private ArrayList<AddCardItem> images;
    private LayoutInflater inflater;
    private Context context;

    public AddsAdaptes(Context context, ArrayList<AddCardItem> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.adds_card, view, false);

        final AddCardItem addCardItem =images.get(position);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.thumbnail);

        try {
            Glide.with(context).load(addCardItem.getImage()).into(myImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(context,"imagecilcked"+position,Toast.LENGTH_LONG).show();

                Intent intent =new Intent(context, AddsJobDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("jobid",addCardItem.getJobid());
                context.startActivity(intent);
            }
        });
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}