package com.pivotalsoft.user.hikestreet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.pivotalsoft.user.hikestreet.Adapters.DocumentsAdapter;
import com.pivotalsoft.user.hikestreet.Adapters.EducationAdapter;
import com.pivotalsoft.user.hikestreet.Adapters.ExperienceAdapter;
import com.pivotalsoft.user.hikestreet.Adapters.SkillsAdapter;
import com.pivotalsoft.user.hikestreet.Constants.Constants;
import com.pivotalsoft.user.hikestreet.Fragments.ProfileFragment;
import com.pivotalsoft.user.hikestreet.Items.DocumentsItem;
import com.pivotalsoft.user.hikestreet.Items.EducationItem;
import com.pivotalsoft.user.hikestreet.Items.ExperienceItem;
import com.pivotalsoft.user.hikestreet.Items.SkillsItem;
import com.pivotalsoft.user.hikestreet.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Dialog dialog;
    private FirebaseAuth mAuth;
    CircleImageView imageProfile;
    String message,consultantName;
    private ProgressDialog pDialog;

    private static final String TAG = ProfileFragment.class.getSimpleName();
    private RecyclerView recyclerExperince,recyclerEducation,recyclerSkills,recyclerDocuments;
    private ExperienceAdapter experienceAdapter;
    private List<ExperienceItem> experienceItemList= new ArrayList<>();

    private EducationAdapter educationAdapter;
    private List<EducationItem> educationItemList= new ArrayList<>();

    private SkillsAdapter skillsAdapter;
    private List<SkillsItem> skillsItemList= new ArrayList<>();

    private DocumentsAdapter documentsAdapter;
    private List<DocumentsItem> documentsItemList= new ArrayList<>();

    ImageButton imgCandidate,imgCheckCandidate,ibCancelCandidate,imagebuttonAboutme,ibCheckAbout,ibCancelAbout,ibPersonalInfo;
    TextView txtFullName,txtAboutme,txtNewExperience,txtNewEducation,txtNewSkills,txtNewDocuments,txtRole,
            txtLocation,txtEmail,txtMobile,txtExperience,txtQulaification,txtdob,txtLanguages;
    String role,usercode,userid,mobileno,fullname,about,profilepic,location,emailid,experience,qualification,dob,languages,gender,latitude,longitude;
    String PERSONAL_INFO_URL,EXP_URL,EDU_URL,SKILL_URL,DOC_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Profile");
        mAuth = FirebaseAuth.getInstance();

        // Progress dialog
        pDialog = new ProgressDialog(ProfileActivity.this);
        pDialog.setCancelable(false);

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 for private mode
        mobileno = sp.getString("mobileno", null);
        userid = sp.getString("userid", null);
        role = sp.getString("role", null);
        Log.e("ROle",""+role);
        String status = sp.getString("status", null);
        String usercode =sp.getString("usercode",null);


        txtNewExperience=(TextView)findViewById(R.id.txtAddExperience);
        txtNewExperience.setOnClickListener(this);

        txtNewEducation=(TextView)findViewById(R.id.txtEducation);
        txtNewEducation.setOnClickListener(this);

        txtNewSkills=(TextView)findViewById(R.id.txtSkills);
        txtNewSkills.setOnClickListener(this);

        txtNewDocuments=(TextView)findViewById(R.id.txtDocuments);
        txtNewDocuments.setOnClickListener(this);

        // first card

        imgCandidate=(ImageButton)findViewById(R.id.imageButtonCandidate);
        imgCandidate.setOnClickListener(this);

        ibPersonalInfo=(ImageButton)findViewById(R.id.ibPersonalInfo);
        ibPersonalInfo.setOnClickListener(this);

        imagebuttonAboutme=(ImageButton)findViewById(R.id.imagebuttonAboutme);
        imagebuttonAboutme.setOnClickListener(this);

        PERSONAL_INFO_URL= Constants.GET_PERSONAL_DATA_URL+userid;
        Log.e("URL2",""+PERSONAL_INFO_URL);

        EXP_URL=Constants.ALL_EXPERIENCE_URL+userid;
        EDU_URL=Constants.ALL_EDUCATION_URL+userid;
        DOC_URL=Constants.DOCUMENTS_URL+userid;
        SKILL_URL=Constants.SKILLS_URL+userid;


        preparePersonalInfoData();

        imageProfile=(CircleImageView)findViewById(R.id.imageProfile);
        txtLocation=(TextView)findViewById(R.id.txtLocation);
        txtEmail=(TextView)findViewById(R.id.txtEmail);
        txtMobile=(TextView)findViewById(R.id.txtMobile);
        txtExperience=(TextView)findViewById(R.id.txtExperience);
        txtQulaification=(TextView)findViewById(R.id.txtQualification);
        txtdob=(TextView)findViewById(R.id.txtDob);
        txtLanguages=(TextView)findViewById(R.id.txtLangugae);




        txtFullName=(TextView)findViewById(R.id.txtFullName);
        txtRole =(TextView)findViewById(R.id.txtRole);
        txtRole.setText(role);

        //Second card



        txtAboutme=(TextView)findViewById(R.id.txtAboutme);



        // experince recycler
        recyclerExperince = (RecyclerView)findViewById(R.id.recyclerExperience);
        recyclerEducation = (RecyclerView)findViewById(R.id.recyclerEducation);
        recyclerSkills = (RecyclerView)findViewById(R.id.recyclerSkills);
        recyclerDocuments = (RecyclerView)findViewById(R.id.recyclerDocuments);

        GridLayoutManager mLayoutManager1 = new GridLayoutManager(ProfileActivity.this, 1);
        recyclerExperince.setLayoutManager(mLayoutManager1);
        recyclerExperince.setItemAnimator(new DefaultItemAnimator());


        GridLayoutManager mLayoutManager2 = new GridLayoutManager(ProfileActivity.this, 1);
        recyclerEducation.setLayoutManager(mLayoutManager2);
        recyclerEducation.setItemAnimator(new DefaultItemAnimator());


        GridLayoutManager mLayoutManager3 = new GridLayoutManager(ProfileActivity.this, 1);
        recyclerSkills.setLayoutManager(mLayoutManager3);
        recyclerSkills.setItemAnimator(new DefaultItemAnimator());


        GridLayoutManager mLayoutManager4 = new GridLayoutManager(ProfileActivity.this, 1);
        recyclerDocuments.setLayoutManager(mLayoutManager4);
        recyclerDocuments.setItemAnimator(new DefaultItemAnimator());



        prepareExperinceData();
        prepareEducationData();
        prepareSkillsData();
        prepareDocumentsData();


    }

    private void preparePersonalInfoData() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                PERSONAL_INFO_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+PERSONAL_INFO_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    //experienceItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("onedata");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String personalid = jsonObject.getString("personalid");
                        fullname = jsonObject.getString("fullname");
                        about = jsonObject.getString("about");
                        location = jsonObject.getString("location");
                        latitude = jsonObject.getString("latitude");
                        longitude = jsonObject.getString("longitude");
                        emailid = jsonObject.getString("emailid");
                        dob = jsonObject.getString("dob");
                        experience = jsonObject.getString("experience");
                        qualification = jsonObject.getString("qualification");
                        gender = jsonObject.getString("gender");
                        languages = jsonObject.getString("languages");
                        profilepic = Constants.IMAGE_PROFILE_URL+jsonObject.getString("profilepic");
                        String userid = jsonObject.getString("userid");


                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("fullName", fullname);  // Saving string
                        Log.e("FULLNAME",""+fullname);
                        // Save the changes in SharedPreferences
                        editor.apply(); // commit changes

                        txtFullName.setText(fullname);
                        txtAboutme.setText(about);
                        txtLocation.setText(location);
                        txtEmail.setText(emailid);
                        txtMobile.setText(mobileno);
                        txtExperience.setText(experience);
                        txtQulaification.setText(qualification);
                        txtdob.setText(dob);
                        txtLanguages.setText(languages);

                        try {
                            Glide.with(ProfileActivity.this).load(profilepic).into(imageProfile);
                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){


            case R.id.imageButtonCandidate:

                Intent intent1 =new Intent(ProfileActivity.this,AddPersonalInformationActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("fullname",fullname);
                intent1.putExtra("about",about);
                intent1.putExtra("location",location);
                intent1.putExtra("latitude",latitude);
                intent1.putExtra("longitude",longitude);
                intent1.putExtra("emailid",emailid);
                intent1.putExtra("experience",experience);
                intent1.putExtra("qualification",qualification);
                intent1.putExtra("dob",dob);
                intent1.putExtra("languages",languages);
                intent1.putExtra("gender",gender);
                intent1.putExtra("profilepic",profilepic);
                startActivity(intent1);
                break;



            case R.id.imagebuttonAboutme:

                Intent intent2 =new Intent(ProfileActivity.this,AddPersonalInformationActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.putExtra("fullname",fullname);
                intent2.putExtra("about",about);
                intent2.putExtra("location",location);
                intent2.putExtra("latitude",latitude);
                intent2.putExtra("longitude",longitude);
                intent2.putExtra("emailid",emailid);
                intent2.putExtra("experience",experience);
                intent2.putExtra("qualification",qualification);
                intent2.putExtra("dob",dob);
                intent2.putExtra("languages",languages);
                intent2.putExtra("gender",gender);
                intent2.putExtra("profilepic",profilepic);
                startActivity(intent2);

                break;



            case R.id.ibPersonalInfo:

                Intent intent =new Intent(ProfileActivity.this,AddPersonalInformationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("fullname",fullname);
                intent.putExtra("about",about);
                intent.putExtra("location",location);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("emailid",emailid);
                intent.putExtra("experience",experience);
                intent.putExtra("qualification",qualification);
                intent.putExtra("dob",dob);
                intent.putExtra("languages",languages);
                intent.putExtra("gender",gender);
                intent.putExtra("profilepic",profilepic);
                startActivity(intent);

                break;


            case R.id.txtAddExperience:

                Intent intentExperince =new Intent(ProfileActivity.this,AddExperienceActivity.class);
                intentExperince.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentExperince);

                break;


            case R.id.txtEducation:

                Intent intentEducation =new Intent(ProfileActivity.this,AddEducationActivity.class);
                intentEducation.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentEducation);

                break;


            case R.id.txtSkills:

                Intent intentSkills =new Intent(ProfileActivity.this,AddSkillsActivity.class);
                intentSkills.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentSkills);

                break;

            case R.id.txtDocuments:

                Intent intentDocuments =new Intent(ProfileActivity.this,AddDocumentsActivity.class);
                intentDocuments.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentDocuments);

                break;


        }



    }



    //  Experience data
    private void prepareExperinceData() {

        showpDialog();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                EXP_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+EXP_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    experienceItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("userdata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String experienceid = jsonObject.getString("experienceid");
                        String workedas = jsonObject.getString("workedas");
                        String company = jsonObject.getString("company");
                        String startyear = jsonObject.getString("startyear");
                        String endyear = jsonObject.getString("endyear");
                        String stillworking = jsonObject.getString("stillworking");
                        String userid = jsonObject.getString("userid");

                        experienceItemList.add(new ExperienceItem(experienceid,workedas,company,startyear,endyear,stillworking,userid));
                        experienceAdapter = new ExperienceAdapter(ProfileActivity.this, experienceItemList);
                        recyclerExperince.setAdapter(experienceAdapter);
                        experienceAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    // education data
    private void prepareEducationData() {

        showpDialog();



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                EDU_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+EDU_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    educationItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("educationdata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String educationid = jsonObject.getString("educationid");
                        String degree = jsonObject.getString("degree");
                        String schoolcollege = jsonObject.getString("schoolcollege");
                        String startyear = jsonObject.getString("startyear");
                        String endyear = jsonObject.getString("endyear");
                        String stillstudying = jsonObject.getString("stillstudying");
                        String userid = jsonObject.getString("userid");

                        educationItemList.add(new EducationItem(educationid,degree,schoolcollege,startyear,endyear,stillstudying,userid));
                        educationAdapter = new EducationAdapter(ProfileActivity.this, educationItemList);
                        recyclerEducation.setAdapter(educationAdapter);
                        educationAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    // education data
    private void prepareSkillsData() {

        showpDialog();



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                SKILL_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+SKILL_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    skillsItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("skilldata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String skillid = jsonObject.getString("skillid");
                        String institute = jsonObject.getString("institute");
                        String course = jsonObject.getString("course");
                        String startyear = jsonObject.getString("startyear");
                        String endyear = jsonObject.getString("endyear");
                        String userid = jsonObject.getString("userid");

                        skillsItemList.add(new SkillsItem(skillid,institute,course,startyear,endyear,userid));
                        skillsAdapter = new SkillsAdapter(ProfileActivity.this, skillsItemList);
                        recyclerSkills.setAdapter(skillsAdapter);
                        skillsAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    // education data
    private void prepareDocumentsData() {

        showpDialog();



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                DOC_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+DOC_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    documentsItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("documentdata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String documentid = jsonObject.getString("documentid");
                        String documenttype = jsonObject.getString("documenttype");
                        String documentno = jsonObject.getString("documentno");
                        String documenturl = Constants.IMAGE_DOCUMENTS_URL+jsonObject.getString("documenturl");
                        Log.e("docurl",""+documenturl);
                        String userid = jsonObject.getString("userid");

                        documentsItemList.add(new DocumentsItem(documentid,documenttype,documentno,documenturl,userid));
                        documentsAdapter = new DocumentsAdapter(ProfileActivity.this, documentsItemList);
                        recyclerDocuments.setAdapter(documentsAdapter);
                        documentsAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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


    private void signOut() {
        mAuth.signOut();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);//Menu Resource, Menu

        MenuItem item = menu.findItem(R.id.action_usercode);

        if (role.equals("Recruiter")){
            item.setVisible(false);
        }
        else {
            item.setVisible(true);
        }
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
                signOut();

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                Intent i = new Intent(ProfileActivity.this,WelcomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);

                break;

            case R.id.action_usercode:
                updateDilaogue();
                break;

            case R.id.action_share:

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.pivotalsoft.user.hikestreet&hl=en");
                startActivity(Intent.createChooser(share, "Share Hikestreet app"));

                break;

            case android.R.id.home:
                onBackPressed();
                break;


        }
        return true;

    }

    private void updateDilaogue(){

        // custom dialog
        dialog = new Dialog(ProfileActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        dialog.setTitle("Update UserCode");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.update_usercode_layout);

        // set the custom dialog components - text, image and button
        final EditText etusercode = (EditText) dialog.findViewById(R.id.etusercode);

        Log.e("qualification",""+etusercode);



        Button btn =(Button)dialog.findViewById(R.id.button_submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usercode=etusercode.getText().toString().trim();

                if (!usercode.isEmpty() ){

                    prepareCheckusercode(usercode);


                }
                else {

                    Toast.makeText(ProfileActivity.this, "Please enter your details!", Toast.LENGTH_LONG).show();
                }

            }
        });

        dialog.show();
    }


    private void prepareCheckusercode(final String usercode) {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.CHECK_USERCODE_URL+usercode, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.CHECK_USERCODE_URL+usercode);

                try {
                    // Parsing json object response
                    // response will be a json object

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("userdata");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(0);

                        String userid = jsonObject.getString("userid");
                        String mobileno = jsonObject.getString("mobileno");
                        String role = jsonObject.getString("role");
                        String status = jsonObject.getString("status");
                        String lastlogintime = jsonObject.getString("lastlogintime");
                        String usercode = jsonObject.getString("usercode");
                        String firebasekey = jsonObject.getString("firebasekey");
                        consultantName = jsonObject.getString("fullname");

                        JSONObject messageObject =new JSONObject(response.toString());
                        message =messageObject.getString("message");
                        Log.e("msg",""+ message);
                    }

                    if ("success".equals(message)){

                        consultentDiloge();

                    }
                    else {

                        Toast.makeText(ProfileActivity.this, "UserCode is invalid", Toast.LENGTH_LONG).show();

                    }


                }

                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void consultentDiloge(){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ProfileActivity.this);
        } else {
            builder = new AlertDialog.Builder(ProfileActivity.this);
        }
        builder.setTitle("CHECK DETAILS !")
                .setMessage("Do you want to add "+consultantName+" is your consultant ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        updateinfo();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void updateinfo(){
        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_USERCODE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hidepDialog();

                        dialog.dismiss();

                       /* Intent pivotal = new Intent(EditEventActivity.this, EventsActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);
                        Toast.makeText(EditEventActivity.this,"Event Updated Successfully",Toast.LENGTH_LONG).show();*/
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

                params.put("usercode",usercode);
                params.put("userid",userid);

                Log.e("RESPONSE_Parasms: ",""+userid+"\n"+usercode);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(stringRequest);
    }




}
