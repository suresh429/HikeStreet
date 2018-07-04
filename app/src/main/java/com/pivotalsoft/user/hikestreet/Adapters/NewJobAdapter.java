package com.pivotalsoft.user.hikestreet.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pivotalsoft.user.hikestreet.BottamMenuActivity;
import com.pivotalsoft.user.hikestreet.Constants.Constants;
import com.pivotalsoft.user.hikestreet.EditNewJobActivity;
import com.pivotalsoft.user.hikestreet.Items.NewJobItem;
import com.pivotalsoft.user.hikestreet.JobDetailsActivity;
import com.pivotalsoft.user.hikestreet.R;


import java.util.List;

/**
 * Created by Gangadhar on 11/14/2017.
 */

public class NewJobAdapter extends RecyclerView.Adapter<NewJobAdapter.MyViewHolder> {

    private Context mContext;
    private List<NewJobItem> coursesItemList;
    String DELETE_URL;
    NewJobItem album;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTimeperoid,txtCategory, txtDescription,txtApplications,txtShortlized,txtHired,txtTitle;
        public ImageView overflow,thumnails;
         public LinearLayout parentLayout;


        public MyViewHolder(View view) {
            super(view);
            txtTimeperoid = (TextView) view.findViewById(R.id.txtTimePeriod);
            txtCategory = (TextView) view.findViewById(R.id.txtCategory);
            txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            txtApplications = (TextView) view.findViewById(R.id.txtApplication);
            txtShortlized = (TextView) view.findViewById(R.id.txtShortlisted);
            txtHired = (TextView) view.findViewById(R.id.txtHired);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);

           // thumnails=(ImageView)view.findViewById(R.id.imageProfile);
            overflow=(ImageView)view.findViewById(R.id.overflow);

             parentLayout = (LinearLayout) view.findViewById(R.id.parentLayout);
        }
    }


    public NewJobAdapter(Context mContext, List<NewJobItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hire_new_job_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        album = coursesItemList.get(position);

      /*  try{
            long timeMillis=Long.parseLong(album.getPostedon());
            Log.e("timein millis",""+timeMillis);
        }catch(NumberFormatException ex){ // handle your exception


         }*/


        holder.txtTimeperoid.setText(album.getPostedon());
        holder.txtCategory.setText(album.getCategory()+" | "+album.getType()+" | "+album.getLocation());
        holder.txtDescription.setText(album.getDescription());
        holder.txtTitle.setText(album.getTitle());





        if (album.getAppliedcount().equals("null")){

            holder.txtApplications.setText("0");
            Log.e("A_COUNT",""+album.getAppliedcount());
        }else {
            holder.txtApplications.setText(album.getAppliedcount());
        }

        if (album.getHiredcount() .equals("null")){
            Log.e("H_COUNT",""+album.getHiredcount());
            holder.txtHired.setText("0");

        }else {
            holder.txtHired.setText(album.getHiredcount());

        }

        if (album.getShortlistedcount() .equals("null")){
            holder.txtShortlized.setText("0");
            Log.e("S_COUNT",""+album.getShortlistedcount());
        }else {

            holder.txtShortlized.setText(album.getShortlistedcount());

        }
        // loading album cover using Glide library
        //  Glide.with(mContext).load(album.getImage()).into(holder.thumbnail);



        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewJobItem newJobItem =coursesItemList.get(position);

                Intent details =new Intent(mContext,JobDetailsActivity.class);
                details.putExtra("jobid",newJobItem.getJobid());
                Log.e("JOBID!!",""+newJobItem.getJobid());
                details.putExtra("title",newJobItem.getTitle());
                details.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(details);
            }
        });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPopupMenu(holder.overflow);

                PopupMenu popup = new PopupMenu(mContext, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_job, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_add_edit:
                                NewJobItem newJobItem =coursesItemList.get(position);
                                Intent details = new Intent(mContext, EditNewJobActivity.class);
                                details.putExtra("categroy", newJobItem.getCategory());
                                details.putExtra("description", newJobItem.getDescription());
                                details.putExtra("title", newJobItem.getTitle());
                                details.putExtra("city", newJobItem.getLocation());
                                details.putExtra("jobtype", newJobItem.getType());
                                details.putExtra("compensation", newJobItem.getCompensation());
                                details.putExtra("timeperiod", newJobItem.getPostedon());
                                details.putExtra("latitude", newJobItem.getLatitude());
                                details.putExtra("longitude", newJobItem.getLongitude());
                                details.putExtra("jobid", newJobItem.getJobid());
                                Log.e("JOBID__!", "" + newJobItem.getJobid());
                                details.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(details);
                                return true;

                            case R.id.action_delete:
                                NewJobItem newJobItem1 =coursesItemList.get(position);
                                DELETE_URL = Constants.JOB_POSTING_URL+newJobItem1.getJobid() ;
                                Log.e("deleteurl",""+DELETE_URL);
                                delete();
                                return true;
                            default:
                        }

                        return false;
                    }
                });

                popup.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return coursesItemList.size();
    }


    private void delete(){


        StringRequest dr = new StringRequest(Request.Method.DELETE, DELETE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        // Toast.makeText(EditExperienceActivity.this, response, Toast.LENGTH_LONG).show();
                        Log.e("response",""+response);
                        Intent delete = new Intent(mContext, BottamMenuActivity.class);
                        delete.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext. startActivity(delete);
                        Toast.makeText(mContext,"Job Delete Successfully",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.
                        Log.e("ERRor",""+error);
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(dr);
    }


}

