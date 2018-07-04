package com.pivotalsoft.user.hikestreet.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.pivotalsoft.user.hikestreet.Adapters.AddsAdaptes;
import com.pivotalsoft.user.hikestreet.AddSkillsActivity;
import com.pivotalsoft.user.hikestreet.BottamMenuActivity;
import com.pivotalsoft.user.hikestreet.Constants.Constants;
import com.pivotalsoft.user.hikestreet.Items.AddCardItem;
import com.pivotalsoft.user.hikestreet.ProfileActivity;
import com.pivotalsoft.user.hikestreet.R;
import com.pivotalsoft.user.hikestreet.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import static android.content.ContentValues.TAG;
import static java.lang.Double.NaN;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {

    ArrayList<LineDataSet> yAxis;
    ArrayList<Entry> yValues;
    ArrayList<String> xAxis1;
    Entry values ;
    LineChart chart;
    LineData data;
    String applicationcount,placementcount;
    private static ViewPager mPager;
    CircleIndicator indicator;
    private static int currentPage = 0;
    AddsAdaptes adapter;
    private ArrayList<AddCardItem> addCardItems = new ArrayList<AddCardItem>();

    TextView txtUserCount,txtApplications,txtJobs,txtPlacements,txtPercentage;
    View rootView;
    public DashBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dash_board, container, false);
        setHasOptionsMenu(true);



        txtUserCount=(TextView)rootView.findViewById(R.id.txtUserCount);
        txtApplications=(TextView)rootView.findViewById(R.id.txtApplicatons);
        txtJobs=(TextView)rootView.findViewById(R.id.txtJobs);
        txtPlacements=(TextView)rootView.findViewById(R.id.txtPlacements);
        txtPercentage=(TextView)rootView.findViewById(R.id.txtPlacementPercentage);

        // Log.d("array",Arrays.toString(fullData));
        chart = (LineChart)rootView .findViewById(R.id.linechart);

        init();
        prepareCharttData();
        prepareApplicationcountData();
        prepareJobscountData();
        preparePlacementcountData();
        prepareUsercountData();

        return rootView;
    }


    private void prepareCharttData() {
        final String url = Constants.GRAPH_URL;
        xAxis1 = new ArrayList<>();
        yAxis = null;
        yValues = new ArrayList<>();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+url);

                try {
                    // Parsing json object response
                    // response will be a json object

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("consdata");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String appliedmonth = jsonObject.getString("appliedmonth");
                        String noofplacements = jsonObject.getString("noofplacements");
                        // float f = Float.parseFloat(noofplacements);

                        xAxis1.add(appliedmonth);

                        values = new BarEntry(Float.valueOf(noofplacements),i);

                        yValues.add(values);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    // Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                LineDataSet barDataSet1 = new LineDataSet(yValues, "Goals LaLiga 16/17");
                barDataSet1.setColor(Color.rgb(0, 82, 159));

                yAxis = new ArrayList<>();
                yAxis.add(barDataSet1);
                String names[]= xAxis1.toArray(new String[xAxis1.size()]);
                data = new LineData(names,yAxis);
                chart.setData(data);
                chart.setDescription("");
                chart.setDrawGridBackground(false);
                chart.setDrawBorders(false);
                // chart.setAutoScaleMinMaxEnabled(false);

                // remove axis
                YAxis leftAxis = chart.getAxisLeft();
                leftAxis.setEnabled(false);
                YAxis rightAxis = chart.getAxisRight();
                rightAxis.setEnabled(false);

                XAxis xAxis = chart.getXAxis();
                xAxis.setEnabled(false);

                // hide legend
                Legend legend = chart.getLegend();
                legend.setEnabled(false);
                chart.animateXY(2000, 2000);
                chart.invalidate();

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


    private void init() {

        mPager = (ViewPager)rootView. findViewById(R.id.pager);

        indicator = (CircleIndicator) rootView.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);


        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == addCardItems.size()-1) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 10000, 10000);

         prepareAddsData();
      /*  AddCardItem addCardItem = new AddCardItem("1","5","https://turtledove.co.in/image/slider/mobileapps.jpg");
        addCardItems.add(addCardItem);

        addCardItem = new AddCardItem("2","6","http://wowslider.com/sliders/demo-87/data1/images/coastallandscape356767_1280.jpg");
        addCardItems.add(addCardItem);

        addCardItem = new AddCardItem("3","7","http://wowslider.com/sliders/demo-96/data1/images/sea_lion_cove.jpg");
        addCardItems.add(addCardItem);

        addCardItem = new AddCardItem("4","8","http://dwijitsolutions.com/wp-content/uploads/2015/10/image-slider-2-home-main.jpg");
        addCardItems.add(addCardItem);
*/



    }

    private void prepareAddsData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.AD_INFO_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.AD_INFO_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    addCardItems.clear();
                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("addata");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String adid = jsonObject.getString("adid");
                        String adpic = Constants.IMAGE_AD_URL+jsonObject.getString("adpic");
                        Log.e("PICURI",""+adpic);
                        String jobid = jsonObject.getString("jobid");

                        addCardItems.add(new AddCardItem(adid,jobid,adpic));

                    }

                    if (getActivity()!=null){

                        adapter=new AddsAdaptes(getContext(),addCardItems);
                        mPager.setAdapter(adapter);
                        //dynamic adapter
                        adapter.registerDataSetObserver(indicator.getDataSetObserver());
                        adapter.notifyDataSetChanged();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    //  count data
    private void prepareUsercountData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.USERCOUNT_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.USERCOUNT_URL);

                try {
                    // Parsing json object response
                    // response will be a json object

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("consdata");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String usercount = jsonObject.getString("usercount");

                        txtUserCount.setText(usercount);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void prepareApplicationcountData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.APPLICATION_COUNT_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.APPLICATION_COUNT_URL);

                try {
                    // Parsing json object response
                    // response will be a json object

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("consdata");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        applicationcount = jsonObject.getString("applicationcount");

                        txtApplications.setText(applicationcount);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void prepareJobscountData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.JOB_COUNT_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.JOB_COUNT_URL);

                try {
                    // Parsing json object response
                    // response will be a json object

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("consdata");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String jobcount = jsonObject.getString("jobcount");

                        txtJobs.setText(jobcount);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void preparePlacementcountData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.PLACEMENT_COUNT_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.PLACEMENT_COUNT_URL);

                try {
                    // Parsing json object response
                    // response will be a json object

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("consdata");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        placementcount = jsonObject.getString("placementcount");

                        txtPlacements.setText(placementcount);

                    }

                  //get entered texts from the edittexts,and convert to integers.
                    Double value1 = Double.parseDouble(placementcount);

                    if (applicationcount !=null) {
                        Double value2 = Double.parseDouble(applicationcount);
                        Log.e("VALUE2", "" + value2);
                        //do the calculation
                        Double calculatedValue = (value1/value2)*100;
                        //set the value to the textview, to display on screen.
                        Log.e("VALUE",""+calculatedValue);
                        if (calculatedValue.equals(NaN)){
                            txtPercentage.setText("0 | 0 %");
                        }
                        else {
                            txtPercentage.setText(placementcount+" | "+calculatedValue.toString().substring(0,2)+"%");
                        }

                    }
                    else {
                        txtPercentage.setText("0 | 0 %");
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent pivotal = new Intent(getContext(), ProfileActivity.class);
                pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(pivotal);
                break;


        }
        return true;

    }



}
