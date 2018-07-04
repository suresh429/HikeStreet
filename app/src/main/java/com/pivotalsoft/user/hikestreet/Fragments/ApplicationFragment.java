package com.pivotalsoft.user.hikestreet.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.pivotalsoft.user.hikestreet.Adapters.AppliedAdapter;
import com.pivotalsoft.user.hikestreet.Adapters.CandidateSearchAdapter;
import com.pivotalsoft.user.hikestreet.AddPersonalInformationActivity;
import com.pivotalsoft.user.hikestreet.Constants.Constants;
import com.pivotalsoft.user.hikestreet.Items.AppliedItem;
import com.pivotalsoft.user.hikestreet.Items.CandidateSearchItem;
import com.pivotalsoft.user.hikestreet.R;
import com.pivotalsoft.user.hikestreet.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplicationFragment extends DialogFragment implements View.OnClickListener {
    private ProgressDialog pDialog;

    private static final String TAG = ApplicationFragment.class.getSimpleName();

    final CharSequence[] experienceList = {"Fresher","0-1 years","1-3 years","3-5 years","5-10 years"," years10+"};
    final CharSequence[] qualificationList = {"Graduation and above","10+2 Pass","10th Pass","Below 10th","Diploma","Other than Mentioned"};

    private final int REQUEST_CODE_PLACEPICKER = 3;
    String latitude,longitude;
    EditText etLocation,etExperience,etQualification;
    String location,experience,qualification;

    private RecyclerView recyclerAppliedData,recyclerCandidateList;

    private CandidateSearchAdapter hireSearchAdapter;
    private List<CandidateSearchItem> hireSearchItems =new ArrayList<>();


    private AppliedAdapter appliedAdapter;
    private List<AppliedItem> appliedItemList =new ArrayList<>();
    LinearLayout recruitrLayout;
    String APPLIED_URL;
    String userid;
    View rootView;
    public ApplicationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_application, container, false);

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 for private mode
        String role = sp.getString("role", null);
        Log.e("Userid",""+role);
        userid = sp.getString("userid", null);
        String usercode =sp.getString("usercode",null);

        if (role.equals("Job Seeker")){

            APPLIED_URL = Constants.MY_APPLICATION_URL+userid;
            Log.e("URL1",""+APPLIED_URL);
        }
        else {

            APPLIED_URL =Constants.MY_ALL_APPLICATION_URL+usercode;
            Log.e("URL2",""+APPLIED_URL);
        }




        etLocation=(EditText)rootView. findViewById(R.id.etLocation);
        etLocation.setOnClickListener(this);


        recyclerAppliedData = (RecyclerView) rootView.findViewById(R.id.recyclerHireData);
        GridLayoutManager mLayoutManager1= new GridLayoutManager(getContext(), 1);
        recyclerAppliedData.setLayoutManager(mLayoutManager1);
        recyclerAppliedData.setItemAnimator(new DefaultItemAnimator());

        // hire recycler data
        recyclerCandidateList =(RecyclerView)rootView.findViewById(R.id.recyclerCandidateList);
        GridLayoutManager mLayoutManager2= new GridLayoutManager(getContext(), 1);
        recyclerCandidateList.setLayoutManager(mLayoutManager2);
        recyclerCandidateList.setItemAnimator(new DefaultItemAnimator());


        recruitrLayout=(LinearLayout)rootView.findViewById(R.id.recruiterLayout);


        if (role.equals("Job Seeker")){

            pDialog = new ProgressDialog(getContext());
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.show();

            //recyclerHireData.setVisibility(View.GONE);
            recyclerAppliedData.setVisibility(View.VISIBLE);
            recruitrLayout.setVisibility(View.GONE);
            prepareAppliedData();
        }
        else if (role.equals("Recruiter")){

//            recyclerHireData.setVisibility(View.VISIBLE);
            recyclerAppliedData.setVisibility(View.GONE);
            recruitrLayout.setVisibility(View.VISIBLE);

        }else {

            pDialog = new ProgressDialog(getContext());
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.show();

            //recyclerHireData.setVisibility(View.GONE);
            recyclerAppliedData.setVisibility(View.VISIBLE);
            recruitrLayout.setVisibility(View.GONE);
            prepareAppliedData();

        }


        Spinner spin = (Spinner)rootView. findViewById(R.id.spinner1);
        Spinner spin2 = (Spinner)rootView. findViewById(R.id.spinner2);


        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,qualificationList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter bb = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,experienceList);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin2.setAdapter(bb);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(getContext(),qualificationList[i] ,Toast.LENGTH_LONG).show();
                qualification= String.valueOf(qualificationList[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(),experienceList[i] ,Toast.LENGTH_LONG).show();
                experience= String.valueOf(experienceList[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        return rootView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){



            case R.id.etLocation:
                startPlacePickerActivity();
                break;




        }

    }

    // google place picker
    private void startPlacePickerActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        // this would only work if you have your Google Places API working

        try {
            Intent intent = intentBuilder.build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void displaySelectedPlaceFromPlacePicker(Intent data) {
        Place placeSelected = PlacePicker.getPlace(data, getContext());
        Log.e("placeSelected",""+placeSelected);

        String name = placeSelected.getName().toString();

        // places latitude
        LatLng qLoc = placeSelected.getLatLng();
        Double lat =qLoc.latitude;
        Log.e("lat", "Place: " + lat);
        latitude = String.valueOf(lat);

        // places longitude
        Double lon =qLoc.longitude;
        Log.e("lon", "Place: " + lon);
        longitude =String.valueOf(lon);


        Geocoder gcd=new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;

        try {
            addresses=gcd.getFromLocation(lat,lon,1);
            if(addresses.size()>0)

            {
                String address = addresses.get(0).getAddressLine(0);
                location = addresses.get(0).getLocality().toString();
                String subLocality = addresses.get(0).getSubLocality().toString();
                String AddressLine = addresses.get(0).getAddressLine(0).toString();
                //locTextView.setText(cityname);
                if (location!=null) {
                    etLocation.setText(location);
                }
                else {
                    Toast.makeText(getContext(),"No data Found for this ... Please Choose Another City",Toast.LENGTH_SHORT).show();
                }
                // }
                Log.e("locality",""+location);
                Log.e("SubLocality",""+subLocality);
                Log.e("AddressLine",""+AddressLine);

                addCandidateSearch();
            }

        } catch (IOException e) {
            e.printStackTrace();

        }


      /*  TextView enterCurrentLocation = (TextView) findViewById(R.id.textView9);
        enterCurrentLocation.setText(name + ", " + address);*/
    }


    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CODE_PLACEPICKER){
                displaySelectedPlaceFromPlacePicker(data);
            }
        }

    }

    /*  @Override
      protected  void onActivityResult(int requestCode, int resultCode, Intent data) {

          super.onActivityResult(requestCode, resultCode, data);

          if (resultCode == Activity.RESULT_OK) {

              if (requestCode == REQUEST_CODE_PLACEPICKER){
                  displaySelectedPlaceFromPlacePicker(data);
              }
          }

      }*/
    /*triggered by showDialog method. onCreateDialog creates a dialog*/





    private void addCandidateSearch(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CANDIDATE_SEARCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            hireSearchItems.clear();
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray heroArray = obj.getJSONArray("searchdata");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                CandidateSearchItem hero = new CandidateSearchItem(
                                        heroObject.getString("personalid"),
                                        heroObject.getString("fullname"),
                                        heroObject.getString("about"),
                                        heroObject.getString("location"),
                                        heroObject.getString("latitude"),
                                        heroObject.getString("longitude"),
                                        heroObject.getString("emailid"),
                                        heroObject.getString("dob"),
                                        heroObject.getString("experience"),
                                        heroObject.getString("qualification"),
                                        heroObject.getString("gender"),
                                        heroObject.getString("languages"),
                                        heroObject.getString("profilepic"),
                                        heroObject.getString("userid"),
                                        heroObject.getString("mobileno"),
                                        heroObject.getString("role"),
                                        heroObject.getString("status"),
                                        heroObject.getString("lastlogintime"),
                                        heroObject.getString("usercode"));


                                //adding the hero to herolist
                                hireSearchItems.add(hero);
                            }

                            hireSearchAdapter = new CandidateSearchAdapter(getContext(), hireSearchItems);
                            recyclerCandidateList.setAdapter(hireSearchAdapter);
                            hireSearchAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("qualification",qualification);
                params.put("experience",experience);
                params.put("location", location);


                // Log.e("RESPONSE_Parasms: ",""+eventName+"\n"+venue+"\n"+tentdate+"\n"+startDaet);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void prepareAppliedData() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                APPLIED_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+APPLIED_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    appliedItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("appdata");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String jobid = jsonObject.getString("jobid");
                        String description = jsonObject.getString("description");
                        String category = jsonObject.getString("category");
                        String location = jsonObject.getString("location");
                        String latitude = jsonObject.getString("latitude");
                        String longitude = jsonObject.getString("longitude");
                        String compensation = jsonObject.getString("compensation");
                        String type = jsonObject.getString("type");
                        String postedon = jsonObject.getString("postedon");
                        String userid = jsonObject.getString("userid");
                        String appliedon = jsonObject.getString("appliedon");
                        // String profilepic = Constants.IMAGE_PROFILEPIC_URL+jsonObject.getString("profilepic");
                        String applied = jsonObject.getString("applied");
                        String rejected = jsonObject.getString("rejected");
                        String shortlisted = jsonObject.getString("shortlisted");
                        String hired = jsonObject.getString("hired");

                        appliedItemList.add(new AppliedItem(jobid,description,category,location,latitude,longitude,compensation,type,postedon,userid,appliedon,applied,rejected,shortlisted,hired));
                        appliedAdapter = new AppliedAdapter(getContext(), appliedItemList);
                        recyclerAppliedData.setAdapter(appliedAdapter);
                        appliedAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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



}
