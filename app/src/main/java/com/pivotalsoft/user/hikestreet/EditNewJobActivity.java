package com.pivotalsoft.user.hikestreet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.pivotalsoft.user.hikestreet.Constants.Constants;
import com.pivotalsoft.user.hikestreet.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditNewJobActivity extends AppCompatActivity implements View.OnClickListener {
    final int EXPERIENCE_ALERTDIALOG = 1;
    CharSequence[] experienceList ;
    private ProgressDialog pDialog;
    private final int REQUEST_CODE_PLACEPICKER = 3;
    String latitude,longitude;

    EditText etTitle,etDescription,etCategory,etLocation,etCompensation;
    String title,location,description,category,compensation,jobType,userid,currentDate,jobId;
    Button btnSave;
    RadioButton rb1,rb2;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_new_job);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Edit Job");

        addsData();

        String category=getIntent().getStringExtra("categroy");
        String title=getIntent().getStringExtra("title");
        String description=getIntent().getStringExtra("description");
        String jobtype=getIntent().getStringExtra("jobtype");
        String city=getIntent().getStringExtra("city");
        String compensation=getIntent().getStringExtra("compensation");
        String timeperiod=getIntent().getStringExtra("timeperiod");
        latitude=getIntent().getStringExtra("latitude");
        longitude=getIntent().getStringExtra("longitude");
        jobId=getIntent().getStringExtra("jobid");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 for private mode
        userid=sp.getString("userid",null);
        Log.e("Userid",""+userid);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

       /* coverIamge=(ImageView)findViewById(R.id.coverImage);
        coverIamge.setOnClickListener(this);*/

        etLocation=(EditText) findViewById(R.id.etLocation);
        etLocation.setText(city);
        etLocation.setOnClickListener(this);

        etCategory=(EditText) findViewById(R.id.etCategory);
        etCategory.setText(category);
        etCategory.setOnClickListener(this);

        etTitle=(EditText) findViewById(R.id.etTitle);
        etTitle.setText(title);

        etDescription=(EditText) findViewById(R.id.etDescription);
        etDescription.setText(description);

        etCompensation=(EditText) findViewById(R.id.etCompensation);
        etCompensation.setText(compensation);

        btnSave=(Button)findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(this);

        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        rb1=(RadioButton)findViewById(R.id.radioPartTime);
        rb2=(RadioButton)findViewById(R.id.radioFullTime);

        if (jobtype.equals("Part Time")){
            rb1.setChecked(true);
            jobType = "Part Time";
        }
        else {

            rb2.setChecked(true);
            jobType = "Full Time";
        }


        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    jobType = "Part Time";

                   // Toast.makeText(EditNewJobActivity.this,jobType,Toast.LENGTH_SHORT).show();
                }
            }
        });

        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    jobType = "Full Time";
                    rb2.setChecked(true);

                    //Toast.makeText(EditNewJobActivity.this,jobType,Toast.LENGTH_SHORT).show();


                }
            }
        });


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            /*case R.id.coverImage:

                showFileChooser1(PICK_IMAGE_REQUEST1);

                break;*/

            case R.id.etLocation:
                startPlacePickerActivity();
                break;

            case R.id.buttonSave:
                saveDate();

                break;

            case R.id.etCategory:
                showDialog(EXPERIENCE_ALERTDIALOG);
                break;
        }
    }


    // google place picker
    private void startPlacePickerActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        // this would only work if you have your Google Places API working

        try {
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void displaySelectedPlaceFromPlacePicker(Intent data) {
        Place placeSelected = PlacePicker.getPlace(data, this);
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

        Geocoder gcd=new Geocoder(EditNewJobActivity.this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses=gcd.getFromLocation(lat,lon,1);
            if(addresses.size()>0)

            {
                String address = addresses.get(0).getAddressLine(0);
                String locality = addresses.get(0).getLocality().toString();
                String subLocality = addresses.get(0).getSubLocality().toString();
                String AddressLine = addresses.get(0).getAddressLine(0).toString();
                //locTextView.setText(cityname);
                etLocation.setText(locality);

                if (locality!=null) {
                    etLocation.setText(locality);
                }
                else {
                    Toast.makeText(EditNewJobActivity.this,"No data Found for this ... Please Choose Another City",Toast.LENGTH_SHORT).show();
                }
                // }
                Log.e("locality",""+locality);
                Log.e("SubLocality",""+subLocality);
                Log.e("AddressLine",""+AddressLine);
            }

        } catch (IOException e) {
            e.printStackTrace();

        }


      /*  TextView enterCurrentLocation = (TextView) findViewById(R.id.textView9);
        enterCurrentLocation.setText(name + ", " + address);*/
    }


    private void addsData() {

        final List<String> listItems = new ArrayList<String>();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.JOB_CATEGORIES_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("New Job Activity", response.toString());

                Log.e("JOB_CATEGORIES_URL",""+Constants.JOB_CATEGORIES_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    //categoryItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("catdata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String catId = jsonObject.getString("categoryid");
                        String categoryname = jsonObject.getString("categoryname");

                        // categoryItemList.add(new CategoryItem(catId,categoryname));


                        listItems.add(categoryname);





                    }

                    experienceList = listItems.toArray(new CharSequence[listItems.size()]);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EditNewJobActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                // hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("New Job Activity", "Error: " + error.getMessage());
                // Toast.makeText(OffersActivity.this, "no data Found. check once data is enable or not..", Toast.LENGTH_LONG).show();
                // hide the progress dialog
                // hidepDialog();
            }
        });



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    /*triggered by showDialog method. onCreateDialog creates a dialog*/
    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {

            case EXPERIENCE_ALERTDIALOG:

                AlertDialog.Builder builder1 = new AlertDialog.Builder(EditNewJobActivity.this)
                        .setTitle("Category")
                        .setSingleChoiceItems(experienceList, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                               // Toast.makeText(getApplicationContext(),"The selected" + experienceList[which], Toast.LENGTH_LONG).show();
                                etCategory.setText(experienceList[which]);
                                //dismissing the dialog when the user makes a selection.
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog1 = builder1.create();
                return alertdialog1;


        }
        return null;

    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
// TODO Auto-generated method stub

        switch (id) {

            case EXPERIENCE_ALERTDIALOG:
                AlertDialog expdialog = (AlertDialog) dialog;
                ListView list_exp = expdialog.getListView();
                for (int i = 0; i < list_exp.getCount(); i++) {
                    list_exp.setItemChecked(i, false);
                }
                break;




        }
    }

    private void saveDate(){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDate = df.format(c.getTime());

        title=etTitle.getText().toString().trim();
        location =etLocation.getText().toString().trim();
        description =etDescription.getText().toString().trim();
        category =etCategory.getText().toString().trim();
        compensation =etCompensation.getText().toString().trim();



        if (!location.isEmpty() && !description.isEmpty()  && !category.isEmpty() && !compensation.isEmpty()) {

            addAdds();

        } else {
            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
        }
    }


    private void addAdds(){

        pDialog.setMessage("Loading ...");
        showDialog();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDAE_JOB_POSTING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hideDialog();

                        Intent pivotal = new Intent(EditNewJobActivity.this, BottamMenuActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);
                        Toast.makeText(EditNewJobActivity.this,"Job Update Successfully",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RESPONSE_ERROR: ",""+error);
                        hideDialog();
                        // Toast.makeText(AddAddsActivity.this,"Email Already Exist",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("title",title);
                params.put("description",description);
                params.put("category",category);
                params.put("location", location);
                params.put("latitude",latitude);
                params.put("longitude",longitude);
                params.put("compensation",compensation);
                params.put("type",jobType);
                params.put("postedon",currentDate);
                params.put("userid",userid);
                params.put("jobid",jobId);

                Log.e("RESPONSE_Parasms: ",""+params);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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

