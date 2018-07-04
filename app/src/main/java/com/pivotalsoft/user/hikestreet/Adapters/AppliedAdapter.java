package com.pivotalsoft.user.hikestreet.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.pivotalsoft.user.hikestreet.Items.AppliedItem;
import com.pivotalsoft.user.hikestreet.R;
import com.pivotalsoft.user.hikestreet.UserDetailsActivity;

import java.util.List;

/**
 * Created by Gangadhar on 11/30/2017.
 */

public class AppliedAdapter extends RecyclerView.Adapter<AppliedAdapter.MyViewHolder> {

    private Context mContext;
    private List<AppliedItem> coursesItemList;
    AppliedItem album;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFulltime,txtDays, txtTitle,txtCity,txtAmount,txtStatus;
        public RelativeLayout parentLayout;


        public MyViewHolder(View view) {
            super(view);
            txtFulltime = (TextView) view.findViewById(R.id.txtFullTime);
            txtDays = (TextView) view.findViewById(R.id.txtDays);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtCity = (TextView) view.findViewById(R.id.txtCity);
            txtAmount = (TextView) view.findViewById(R.id.txtAmount);

            txtStatus=(TextView) view.findViewById(R.id.buttonApply);

            parentLayout = (RelativeLayout) view.findViewById(R.id.parentLayout);
        }
    }


    public AppliedAdapter(Context mContext, List<AppliedItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public AppliedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.applied_card, parent, false);

        return new AppliedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AppliedAdapter.MyViewHolder holder, final int position) {

        album = coursesItemList.get(position);
        holder.txtFulltime.setText(album.getCategory()+" | "+album.getType());
        holder.txtDays.setText(album.getPostedon());
        holder.txtTitle.setText(album.getDescription());
        holder.txtCity.setText(album.getLocation());
        holder.txtAmount.setText(album.getCompensation());

        // loading album cover using Glide library
        //  Glide.with(mContext).load(album.getImage()).into(holder.thumbnail);

        if (album.getApplied().equals("1")){

            holder.txtStatus.setText("Applied");

        }else if (album.getHired().equals("1")){

            holder.txtStatus.setText("Hired");

        }else if (album.getRejected().equals("1")){

            holder.txtStatus.setText("Rejected");

        }else if (album.getShortlisted().equals("1")){

            holder.txtStatus.setText("Shortlisted");

        }

        //holder.button.setBackgroundColor(Color.parseColor("#2ECC71"));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppliedItem appliedItem =coursesItemList.get(position);
                Intent details =new Intent(mContext,UserDetailsActivity.class);
                details.putExtra("applicantid",appliedItem.getUserid());
                details.putExtra("fullname",appliedItem.getCategory());
                details.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(details);
            }
        });


    }


    @Override
    public int getItemCount() {
        return coursesItemList.size();
    }



}