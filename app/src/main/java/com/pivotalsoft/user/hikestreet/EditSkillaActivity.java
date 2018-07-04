package com.pivotalsoft.user.hikestreet;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pivotalsoft.user.hikestreet.Constants.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditSkillaActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog pDialog;
    private DatePickerDialog endDatePickerDialog,startDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    EditText etstartYear,etEndYear,etInstitute,etCourse;
    String endYear,startYear,institute,course,skillid,userid;
    FloatingActionButton btnSave,btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_skilla);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Edit Skills and Certifications");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 for private mode
        userid=sp.getString("userid",null);
        Log.e("Userid",""+userid);

        institute=getIntent().getStringExtra("institute");
        course=getIntent().getStringExtra("course");
        startYear=getIntent().getStringExtra("startyear");
        endYear=getIntent().getStringExtra("endyear");
        skillid=getIntent().getStringExtra("skillid");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        etInstitute=(EditText) findViewById(R.id.etInstitute);
        etInstitute.setText(institute);

        etCourse=(EditText) findViewById(R.id.etCourse);
        etCourse.setText(course);


        etstartYear=(EditText) findViewById(R.id.etStartYear);
        etstartYear.setText(startYear);
        etstartYear.setInputType(InputType.TYPE_NULL);
        etstartYear.requestFocus();
        etstartYear.setOnClickListener(this);

        etEndYear=(EditText) findViewById(R.id.etEndYear);
        etEndYear.setText(endYear);
        etEndYear.setInputType(InputType.TYPE_NULL);
        etEndYear.requestFocus();
        etEndYear.setOnClickListener(this);



        btnSave =(FloatingActionButton)findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(this);

        btnDelete=(FloatingActionButton)findViewById(R.id.buttonDelete);
        btnDelete.setOnClickListener(this);

        setDateTimeField();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.buttonSave:
                saveDate();
                break;

            case R.id.buttonDelete:

                delete();
                break;

            case R.id.etStartYear:
                startDatePickerDialog.show();


                break;

            case R.id.etEndYear:

                endDatePickerDialog.show();
                break;

        }
    }

    private void setDateTimeField(){

        Calendar newCalendar = Calendar.getInstance();
        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etEndYear.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etstartYear.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void saveDate(){

        institute =etInstitute.getText().toString().trim();
        endYear =etEndYear.getText().toString().trim();
        startYear =etstartYear.getText().toString().trim();
        course =etCourse.getText().toString().trim();



        if (!institute.isEmpty() && !endYear.isEmpty()  && !startYear.isEmpty() && !course.isEmpty() ) {

            addAdds();

        } else {
            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
        }
    }


    private void addAdds(){

        pDialog.setMessage("Loading ...");
        showDialog();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_SKILLS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hideDialog();

                        Intent pivotal = new Intent(EditSkillaActivity.this, ProfileActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);
                        Toast.makeText(EditSkillaActivity.this,"Skill Updated Successfully",Toast.LENGTH_LONG).show();
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
                params.put("skillid",skillid);
                params.put("institute",institute);
                params.put("course",course);
                params.put("endyear", endYear);
                params.put("startyear",startYear);
                params.put("userid",userid);

                // Log.e("RESPONSE_Parasms: ",""+eventName+"\n"+venue+"\n"+tentdate+"\n"+startDaet);
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

    private void delete(){
        String DELETE_URL=Constants.SKILLS_URL+skillid;
        Log.e("deleteurl",""+DELETE_URL);

        StringRequest dr = new StringRequest(Request.Method.DELETE,DELETE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("response",""+response);

                        Intent delete = new Intent(EditSkillaActivity.this, ProfileActivity.class);
                        delete.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(delete);
                        Toast.makeText(EditSkillaActivity.this,"Skill Delete Successfully",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.
                        Log.e("error",""+error);
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(dr);
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

