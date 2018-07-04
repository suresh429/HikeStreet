package com.pivotalsoft.user.hikestreet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pivotalsoft.user.hikestreet.Adapters.HireSearchAdapter;
import com.pivotalsoft.user.hikestreet.Constants.Constants;
import com.pivotalsoft.user.hikestreet.Items.HireSearchItem;
import com.pivotalsoft.user.hikestreet.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class JobDetailsActivity extends AppCompatActivity {
    private ProgressDialog pDialog;

    private static final String TAG = JobDetailsActivity.class.getSimpleName();
    private RecyclerView recyclerHireData;
    private HireSearchAdapter hireSearchAdapter;
    private List<HireSearchItem> hireSearchItems =new ArrayList<>();

    String userid,jobid,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Job Details");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 for private mode
        String role = sp.getString("role", "1");
        Log.e("role",""+role);
        userid = sp.getString("userid", null);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        jobid=getIntent().getStringExtra("jobid");
        Log.e("jobid",""+jobid);
        title=getIntent().getStringExtra("title");

        // hire recycler data
        recyclerHireData = (RecyclerView)findViewById(R.id.recyclerAppliedData);
        GridLayoutManager mLayoutManager2= new GridLayoutManager(this, 1);
        recyclerHireData.setLayoutManager(mLayoutManager2);
        recyclerHireData.setItemAnimator(new DefaultItemAnimator());

        prepareHireData();
    }


    // hire data.....
    private void prepareHireData() {

        showpDialog();

        final String CANDIDATES_URL = Constants.MY_CANDIDATES_URL+jobid;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                CANDIDATES_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+CANDIDATES_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    hireSearchItems.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("appdata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String fullname = jsonObject.getString("fullname");
                        String qualification = jsonObject.getString("qualification");
                        String location = jsonObject.getString("location");
                        String latitude = jsonObject.getString("latitude");
                        String longitude = jsonObject.getString("longitude");
                        String experience = jsonObject.getString("experience");
                        String applicationid = jsonObject.getString("applicationid");
                        String applicantid = jsonObject.getString("applicantid");
                        String gender = jsonObject.getString("gender");
                        String appliedon = jsonObject.getString("appliedon");
                        String applied = jsonObject.getString("applied");
                        String rejected = jsonObject.getString("rejected");
                        String shortlisted = jsonObject.getString("shortlisted");
                        String hired = jsonObject.getString("hired");
                        String role = jsonObject.getString("role");
                        String usercode = jsonObject.getString("usercode");
                        String firebasekey = jsonObject.getString("firebasekey");



                        hireSearchItems.add(new HireSearchItem(fullname,qualification,location,latitude,longitude,applicantid,experience,applicationid,gender,appliedon,applied,rejected,shortlisted,hired,role,usercode,firebasekey,title));
                        hireSearchAdapter = new HireSearchAdapter(JobDetailsActivity.this, hireSearchItems);
                        recyclerHireData.setAdapter(hireSearchAdapter);
                        hireSearchAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(JobDetailsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(OffersActivity.this, "no data Found. check once data is enable or not..", Toast.LENGTH_LONG).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

      /*  //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(OffersActivity.this);

        //Adding request to the queue
        requestQueue.add(jsonObjReq);*/

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

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
