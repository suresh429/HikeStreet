package com.pivotalsoft.user.hikestreet.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.pivotalsoft.user.hikestreet.Items.CandidateSearchItem;
import com.pivotalsoft.user.hikestreet.R;
import com.pivotalsoft.user.hikestreet.UserDetailsActivity;


import java.util.List;

/**
 * Created by Gangadhar on 12/1/2017.
 */

public class CandidateSearchAdapter extends RecyclerView.Adapter<CandidateSearchAdapter.MyViewHolder> {

    private Context mContext;
    private List<CandidateSearchItem> coursesItemList;
    CandidateSearchItem album;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFullName,txtDistance, txtYears,txtOthers;
        public ImageView overflow,thumnails;
        public RelativeLayout parentLayout;
       // Button shotlist,reject;


        public MyViewHolder(View view) {
            super(view);
            txtFullName = (TextView) view.findViewById(R.id.txtFullName);
            txtDistance = (TextView) view.findViewById(R.id.txtDistance);
            txtYears = (TextView) view.findViewById(R.id.txtFresher);
            txtOthers = (TextView) view.findViewById(R.id.txtOthers);
           /* shotlist=(Button)view.findViewById(R.id.btnShotlist);
            reject=(Button)view.findViewById(R.id.btnReject);*/
            thumnails=(ImageView)view.findViewById(R.id.imageProfile);

            parentLayout = (RelativeLayout) view.findViewById(R.id.parentLayout);
        }
    }


    public CandidateSearchAdapter(Context mContext, List<CandidateSearchItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public CandidateSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.candidate_search_card, parent, false);

        return new CandidateSearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CandidateSearchAdapter.MyViewHolder holder, final int position) {

        album = coursesItemList.get(position);
        holder.txtFullName.setText(album.getFullname());
        holder.txtDistance.setText(album.getLocation());
        holder.txtYears.setText(album.getExperience());
        holder.txtOthers.setText(album.getQualification());

        String test = album.getFullname();
        String firstLetter=test.substring(0,1);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color1 = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(60)  // width in px
                .height(60) // height in px
                .endConfig()
                .buildRound(firstLetter, color1);

        holder.thumnails.setImageDrawable(drawable);
        // loading album cover using Glide library
        //  Glide.with(mContext).load(album.getImage()).into(holder.thumbnail);



        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CandidateSearchItem candidateSearchItem =coursesItemList.get(position);
                Intent details =new Intent(mContext,UserDetailsActivity.class);
                details.putExtra("applicantid",candidateSearchItem.getUserid());
                details.putExtra("fullname",candidateSearchItem.getFullname());
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