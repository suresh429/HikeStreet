package com.pivotalsoft.user.hikestreet;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.pivotalsoft.user.hikestreet.Adapters.DocumentAdapter1;
import com.pivotalsoft.user.hikestreet.Adapters.EducationAdapter1;
import com.pivotalsoft.user.hikestreet.Adapters.ExperienceAdapter1;
import com.pivotalsoft.user.hikestreet.Adapters.SkillAdapter1;
import com.pivotalsoft.user.hikestreet.Constants.Constants;
import com.pivotalsoft.user.hikestreet.Items.DocumentsItem;
import com.pivotalsoft.user.hikestreet.Items.EducationItem;
import com.pivotalsoft.user.hikestreet.Items.ExperienceItem;
import com.pivotalsoft.user.hikestreet.Items.SkillsItem;
import com.pivotalsoft.user.hikestreet.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsActivity extends AppCompatActivity {

    CircleImageView imageProfile;

    private ProgressDialog pDialog;

    private static final String TAG = UserDetailsActivity.class.getSimpleName();
    private RecyclerView recyclerExperince,recyclerEducation,recyclerSkills,recyclerDocuments;
    private ExperienceAdapter1 experienceAdapter;
    private List<ExperienceItem> experienceItemList= new ArrayList<>();

    private EducationAdapter1 educationAdapter;
    private List<EducationItem> educationItemList= new ArrayList<>();

    private SkillAdapter1 skillsAdapter;
    private List<SkillsItem> skillsItemList= new ArrayList<>();

    private DocumentAdapter1 documentsAdapter;
    private List<DocumentsItem> documentsItemList= new ArrayList<>();

    ImageButton imgCandidate,imagebuttonAboutme,ibPersonalInfo;
    TextView txtFullName,txtAboutme,txtNewExperience,txtNewEducation,txtNewSkills,txtNewDocuments,txtRole,
            txtLocation,txtEmail,txtMobile,txtExperience,txtQulaification,txtdob,txtLanguages;
    String userid,mobileno,fullname,about,profilepic,location,emailid,experience,qualification,dob,languages,gender,latitude,longitude;
    String PERSONAL_INFO_URL,EXP_URL,EDU_URL,SKILL_URL,DOC_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Profile");

        String candidateName =getIntent().getStringExtra("fullname");
        userid =getIntent().getStringExtra("applicantid");


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

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


        //Second card


        txtAboutme=(TextView)findViewById(R.id.txtAboutme);

        // experince recycler
        recyclerExperince = (RecyclerView)findViewById(R.id.recyclerExperience);
        recyclerEducation = (RecyclerView)findViewById(R.id.recyclerEducation);
        recyclerSkills = (RecyclerView)findViewById(R.id.recyclerSkills);
        recyclerDocuments = (RecyclerView)findViewById(R.id.recyclerDocuments);

        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this, 1);
        recyclerExperince.setLayoutManager(mLayoutManager1);
        recyclerExperince.setItemAnimator(new DefaultItemAnimator());


        GridLayoutManager mLayoutManager2 = new GridLayoutManager(this, 1);
        recyclerEducation.setLayoutManager(mLayoutManager2);
        recyclerEducation.setItemAnimator(new DefaultItemAnimator());


        GridLayoutManager mLayoutManager3 = new GridLayoutManager(this, 1);
        recyclerSkills.setLayoutManager(mLayoutManager3);
        recyclerSkills.setItemAnimator(new DefaultItemAnimator());


        GridLayoutManager mLayoutManager4 = new GridLayoutManager(this, 1);
        recyclerDocuments.setLayoutManager(mLayoutManager4);
        recyclerDocuments.setItemAnimator(new DefaultItemAnimator());



        prepareExperinceData();
        prepareEducationData();
        prepareSkillsData();
        prepareDocumentsData();
    }

    private void preparePersonalInfoData() {

        showpDialog();

        PERSONAL_INFO_URL= Constants.GET_PERSONAL_DATA_URL+userid;

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




                        txtFullName.setText(fullname);
                        txtAboutme.setText(about);
                        txtLocation.setText(location);
                        txtEmail.setText("xxxxxxxxx");
                        txtMobile.setText("xxxxxxxxx");
                        txtExperience.setText(experience);
                        txtQulaification.setText(qualification);
                        txtdob.setText(dob);
                        txtLanguages.setText(languages);

                        try {
                            Glide.with(UserDetailsActivity.this).load(profilepic).into(imageProfile);
                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(UserDetailsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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



    //  Experience data
    private void prepareExperinceData() {

        showpDialog();

        EXP_URL=Constants.ALL_EXPERIENCE_URL+userid;

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
                        experienceAdapter = new ExperienceAdapter1(UserDetailsActivity.this, experienceItemList);
                        recyclerExperince.setAdapter(experienceAdapter);
                        experienceAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(UserDetailsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

        EDU_URL=Constants.ALL_EDUCATION_URL+userid;

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
                        educationAdapter = new EducationAdapter1(UserDetailsActivity.this, educationItemList);
                        recyclerEducation.setAdapter(educationAdapter);
                        educationAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(UserDetailsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

        SKILL_URL=Constants.SKILLS_URL+userid;

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
                        skillsAdapter = new SkillAdapter1(UserDetailsActivity.this, skillsItemList);
                        recyclerSkills.setAdapter(skillsAdapter);
                        skillsAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(UserDetailsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

        DOC_URL=Constants.DOCUMENTS_URL+userid;

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
                        documentsAdapter = new DocumentAdapter1(UserDetailsActivity.this, documentsItemList);
                        recyclerDocuments.setAdapter(documentsAdapter);
                        documentsAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(UserDetailsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

