package com.pivotalsoft.user.hikestreet.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pivotalsoft.user.hikestreet.Constants.Constants;
import com.pivotalsoft.user.hikestreet.Items.SearchItem;
import com.pivotalsoft.user.hikestreet.R;
import com.pivotalsoft.user.hikestreet.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by USER on 12/14/2017.
 */

public class AddsJobAdapter extends RecyclerView.Adapter<AddsJobAdapter.MyViewHolder> {

    private Context mContext;
    private List<SearchItem> coursesItemList;

    String userid,fcmKey,fullName,consultFCmKey;
    String CONSULTANT_URL;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFulltime,txtDays, txtTitle,txtCity,txtAmount,txtDescription;
        public Button button;
        // public RelativeLayout parentLayout;


        public MyViewHolder(View view) {
            super(view);
            txtFulltime = (TextView) view.findViewById(R.id.txtFullTime);
            txtDescription=(TextView) view.findViewById(R.id.txtDescription);
            txtDays = (TextView) view.findViewById(R.id.txtDays);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtCity = (TextView) view.findViewById(R.id.txtCity);
            txtAmount = (TextView) view.findViewById(R.id.txtAmount);
            button=(Button)view.findViewById(R.id.buttonApply);

            // To retrieve value from shared preference in another activity
            SharedPreferences sp = mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 for private mode
            String role = sp.getString("role", null);
            String usercode =sp.getString("usercode",null);
            userid = sp.getString("userid", null);
            fullName = sp.getString("fullName", null);
            // fcmKey = sp.getString("firebaskey", "1");


            if (role.equals("Consultant") || role.equals("Recruiter")){

                button.setVisibility(View.GONE);
            }
            else {

                button.setVisibility(View.VISIBLE);
            }



            // parentLayout = (RelativeLayout) view.findViewById(R.id.parentLayout);
        }
    }


    public AddsJobAdapter(Context mContext, List<SearchItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public AddsJobAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_card, parent, false);

        return new AddsJobAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AddsJobAdapter.MyViewHolder holder, final int position) {

        SearchItem album = coursesItemList.get(position);
        holder.txtFulltime.setText(album.getCategory()+" | "+album.getType());
        holder.txtDays.setText(album.getPostedon());
        holder.txtTitle.setText(album.getTitle());
        holder.txtDescription.setText(album.getDescription());
        holder.txtCity.setText(album.getLocation());
        holder.txtAmount.setText(album.getCompensation());

        // loading album cover using Glide library
        //  Glide.with(mContext).load(album.getImage()).into(holder.thumbnail);



        holder.button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                SearchItem album = coursesItemList.get(position);
                addUser(position);
                holder.button.setText("Applied");
                holder.button.setBackgroundColor(Color.parseColor("#99A3A4"));
                pushnotification(position);

                Log.e("usercode",""+album.getUsercode());
                if (!album.getUsercode().equals("0")){

                    CONSULTANT_URL= Constants.GET_MY_CONSULTANT_URL+album.getUsercode();
                    Log.e("CONSULT_URL",""+CONSULTANT_URL);
                    prepareCounsultentData();
                    consultentpushnotification(position);
                }



            }
        });


    }


    @Override
    public int getItemCount() {
        return coursesItemList.size();
    }


    private void addUser(final int position){

        final SearchItem album = coursesItemList.get(position);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.JOB_APPLIED_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);


                      /*  Intent pivotal = new Intent(AddSkillsActivity.this, EventsActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);
                        Toast.makeText(AddSkillsActivity.this,"Event Added Successfully",Toast.LENGTH_LONG).show();*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RESPONSE_ERROR: ",""+error);
                        //hideDialog();
                        // Toast.makeText(AddAddsActivity.this,"Email Already Exist",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put("jobid",album.getJobid());
                params.put("applicantid",userid);
                params.put("appliedon", formattedDate);
                params.put("shortlisted","0");
                params.put("applied","1");
                params.put("rejected","0");
                params.put("hired","0");

                Log.e("RESPONSE_Parasms: ",""+params);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void pushnotification(final int position){
        SearchItem album = coursesItemList.get(position);
        try{


            RequestQueue queue = Volley.newRequestQueue(mContext);

            String url = "https://fcm.googleapis.com/fcm/send";

            String body ="Hi! "+album.getRecruitername()+" ! "+fullName+" Applied for job "+" '"+album.getTitle()+"' ";
            Log.e("body",""+body);

            JSONObject data = new JSONObject();
            data.put("title", "Notification");
            data.put("body", body);
            JSONObject notification_data = new JSONObject();
            notification_data.put("notification", data);

            notification_data.put("to",album.getFirebasekey());

            JsonObjectRequest request = new JsonObjectRequest(url, notification_data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    String api_key_header_value = "Key="+Constants.AUTH_KEY_FCM;
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", api_key_header_value);
                    return headers;
                }
            };

            queue.add(request);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //   consultent
    private void prepareCounsultentData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                CONSULTANT_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+CONSULTANT_URL);

                try {
                    // Parsing json object response
                    // response will be a json object

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("consdata");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String userid = jsonObject.getString("userid");
                        String mobileno = jsonObject.getString("mobileno");
                        String role = jsonObject.getString("role");
                        String status = jsonObject.getString("status");
                        String lastlogintime = jsonObject.getString("lastlogintime");
                        String usercode = jsonObject.getString("usercode");
                        consultFCmKey = jsonObject.getString("firebasekey");


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //  hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(OffersActivity.this, "no data Found. check once data is enable or not..", Toast.LENGTH_LONG).show();
                // hide the progress dialog
                // hidepDialog();
            }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    private void consultentpushnotification(final int position){
        SearchItem album = coursesItemList.get(position);
        try{


            RequestQueue queue = Volley.newRequestQueue(mContext);

            String url = "https://fcm.googleapis.com/fcm/send";

            String body ="Hi! "+album.getRecruitername()+" ! "+fullName+" Applied for job "+" '"+album.getTitle()+"' ";
            Log.e("body",""+body);

            JSONObject data = new JSONObject();
            data.put("title", "Notification");
            data.put("body", body);
            JSONObject notification_data = new JSONObject();
            notification_data.put("notification", data);

            notification_data.put("to",consultFCmKey);

            Log.e("ConsultKey",""+consultFCmKey);

            JsonObjectRequest request = new JsonObjectRequest(url, notification_data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    String api_key_header_value = "Key="+Constants.AUTH_KEY_FCM;
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", api_key_header_value);
                    return headers;
                }
            };

            queue.add(request);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
