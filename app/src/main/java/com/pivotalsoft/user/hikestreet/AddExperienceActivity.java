package com.pivotalsoft.user.hikestreet;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class AddExperienceActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog pDialog;
    private DatePickerDialog endDatePickerDialog,startDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    EditText etstartYear,etEndYear,etDegreeName,etSchool;
    String endYear,startYear,degreeName,collageName,stillworking,userid;
    FloatingActionButton btnSave;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experience);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add Experience");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 for private mode
        userid=sp.getString("userid",null);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        etDegreeName=(EditText) findViewById(R.id.etInstitute);
        etSchool=(EditText) findViewById(R.id.etCourse);


        etstartYear=(EditText) findViewById(R.id.etStartYear);
        etstartYear.setInputType(InputType.TYPE_NULL);
        etstartYear.requestFocus();
        etstartYear.setOnClickListener(this);

        etEndYear=(EditText) findViewById(R.id.etEndYear);
        etEndYear.setInputType(InputType.TYPE_NULL);
        etEndYear.requestFocus();
        etEndYear.setOnClickListener(this);


        checkBox=(CheckBox)findViewById(R.id.checkbox);
        stillworking ="No";
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (checkBox.isChecked()){

                    etEndYear.setVisibility(View.GONE);
                    stillworking ="Yes";
                }
                else {
                    etEndYear.setVisibility(View.VISIBLE);
                    stillworking ="No";
                }
            }
        });



        btnSave =(FloatingActionButton)findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(this);

        setDateTimeField();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.buttonSave:
                saveDate();
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

        degreeName =etDegreeName.getText().toString().trim();
        endYear =etEndYear.getText().toString().trim();
        startYear =etstartYear.getText().toString().trim();
        collageName =etSchool.getText().toString().trim();
       /* stillworking=checkBox.toString();
        Log.e("stillworking",""+stillworking);*/


        if (!degreeName.isEmpty()   && !startYear.isEmpty() && !collageName.isEmpty() ) {

            addAdds();

        } else {
            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
        }
    }


    private void addAdds(){

        pDialog.setMessage("Loading ...");
        showDialog();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_EXPERIENCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hideDialog();

                        Intent pivotal = new Intent(AddExperienceActivity.this, ProfileActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);
                        Toast.makeText(AddExperienceActivity.this,"Experience Added Successfully",Toast.LENGTH_LONG).show();
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
                params.put("workedas",degreeName);
                params.put("company",collageName);
                params.put("endyear", endYear);
                params.put("startyear",startYear);
                params.put("stillworking",stillworking);
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



