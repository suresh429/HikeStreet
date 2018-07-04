package com.pivotalsoft.user.hikestreet;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
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
import com.pivotalsoft.user.hikestreet.Adapters.AddsJobAdapter;
import com.pivotalsoft.user.hikestreet.Adapters.HireSearchAdapter;
import com.pivotalsoft.user.hikestreet.Adapters.SearchAdapter;
import com.pivotalsoft.user.hikestreet.Constants.Constants;
import com.pivotalsoft.user.hikestreet.Items.HireSearchItem;
import com.pivotalsoft.user.hikestreet.Items.SearchItem;
import com.pivotalsoft.user.hikestreet.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddsJobDetailsActivity extends AppCompatActivity {
    private ProgressDialog pDialog;

    private static final String TAG = AddsJobDetailsActivity.class.getSimpleName();
    private RecyclerView recycleradjob;
    private AddsJobAdapter addsJobAdapter;
    private List<SearchItem> searchItems =new ArrayList<>();
    String jobid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adds_job_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Job Details");

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        jobid=getIntent().getStringExtra("jobid");

        // hire recycler data
        recycleradjob = (RecyclerView)findViewById(R.id.jobsRecycler);
        GridLayoutManager mLayoutManager2= new GridLayoutManager(this, 1);
        recycleradjob.setLayoutManager(mLayoutManager2);
        recycleradjob.setItemAnimator(new DefaultItemAnimator());

        prepareSearchData();
    }


    // hire data.....
    private void prepareSearchData() {
       showpDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.JOB_BY_AD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);

                        hidepDialog();

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray heroArray = obj.getJSONArray("jobdata");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject jsonObject  = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                String jobid = jsonObject.getString("jobid");
                                String title = jsonObject.getString("title");
                                String description = jsonObject.getString("description");
                                String category = jsonObject.getString("category");
                                String location = jsonObject.getString("location");
                                String latitude = jsonObject.getString("latitude");
                                String longitude = jsonObject.getString("longitude");
                                String compensation = jsonObject.getString("compensation");
                                String type = jsonObject.getString("type");
                                String postedon = jsonObject.getString("postedon");
                                String userid = jsonObject.getString("userid");
                                String mobileno = jsonObject.getString("mobileno");
                                String role = jsonObject.getString("role");
                                String status = jsonObject.getString("status");
                                String lastlogintime = jsonObject.getString("lastlogintime");
                                String usercode = jsonObject.getString("usercode");
                                String firebasekey = jsonObject.getString("firebasekey");
                                String recruitername = jsonObject.getString("fullname");


                              searchItems.add(new SearchItem(jobid,title,description,category,location,latitude,longitude,compensation,type,postedon,userid,mobileno,role,status,lastlogintime,usercode,firebasekey,recruitername));
                            }

                            addsJobAdapter = new AddsJobAdapter(AddsJobDetailsActivity.this, searchItems);
                            recycleradjob.setAdapter(addsJobAdapter);
                            addsJobAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RESPONSE_ERROR: ",""+error);
                        hidepDialog();
                        // Toast.makeText(AddAddsActivity.this,"Email Already Exist",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("jobid", jobid);

                Log.e("RESPONSE_Parasms: ",""+params);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddsJobDetailsActivity.this);
        requestQueue.add(stringRequest);

    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }
}
