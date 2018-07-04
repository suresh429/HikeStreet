package com.pivotalsoft.user.hikestreet.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
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
import com.pivotalsoft.user.hikestreet.Items.HireSearchItem;
import com.pivotalsoft.user.hikestreet.R;
import com.pivotalsoft.user.hikestreet.UserDetailsActivity;
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
 * Created by Gangadhar on 11/13/2017.
 */

public class HireSearchAdapter extends RecyclerView.Adapter<HireSearchAdapter.MyViewHolder> {

    private Context mContext;
    private List<HireSearchItem> coursesItemList;
    HireSearchItem hireSearchItem;
    String userid,fcmKey,consultFCmKey;
    String CONSULTANT_URL;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFullName,txtDistance, txtYears,txtOthers,txtStatus;
        public ImageView overflow,thumnails;
        public RelativeLayout parentLayout;
        Button shotlist,reject,hire;


        public MyViewHolder(View view) {
            super(view);
            txtFullName = (TextView) view.findViewById(R.id.txtFullName);
            txtDistance = (TextView) view.findViewById(R.id.txtDistance);
            txtYears = (TextView) view.findViewById(R.id.txtFresher);
            txtOthers = (TextView) view.findViewById(R.id.txtOthers);
            txtStatus=(TextView)view.findViewById(R.id.txtStatus);
            shotlist=(Button)view.findViewById(R.id.btnShotlist);
            reject=(Button)view.findViewById(R.id.btnReject);
            hire=(Button)view.findViewById(R.id.btnHire);
            thumnails=(ImageView)view.findViewById(R.id.imageProfile);

            parentLayout = (RelativeLayout) view.findViewById(R.id.parentLayout);

            // To retrieve value from shared preference in another activity
            SharedPreferences sp = mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 for private mode
            String role = sp.getString("role", null);
            String usercode =sp.getString("usercode",null);
            userid = sp.getString("userid", null);
           // fcmKey = sp.getString("firebaskey", "1");


        }
    }


    public HireSearchAdapter(Context mContext, List<HireSearchItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public HireSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hire_search_card, parent, false);

        return new HireSearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HireSearchAdapter.MyViewHolder holder, final int position) {

        HireSearchItem  album = coursesItemList.get(position);
        holder.txtFullName.setText(album.getFullname());
        holder.txtDistance.setText(album.getLocation());
        holder.txtYears.setText(album.getExperience());
        holder.txtOthers.setText(album.getQualification());

        String test = album.getFullname();
        String firstLetter=test.substring(0,1);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color1 = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(60)  // width in px
                .height(60) // height in px
                .endConfig()
                .buildRound(firstLetter, color1);

        holder.thumnails.setImageDrawable(drawable);
        // loading album cover using Glide library
        //  Glide.with(mContext).load(album.getImage()).into(holder.thumbnail);

        if (!album.getUsercode().equals("0")){

             CONSULTANT_URL= Constants.GET_MY_CONSULTANT_URL+album.getUsercode();
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HireSearchItem hireSearchItem =coursesItemList.get(position);
                Intent details =new Intent(mContext,UserDetailsActivity.class);
                details.putExtra("applicantid",hireSearchItem.getUserid());
                details.putExtra("fullname",hireSearchItem.getFullname());
                details.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(details);
            }
        });

        if (album.getApplied().equals("1")){

            holder.txtStatus.setText("Applied");

        }else if (album.getHired().equals("1")){

            holder.txtStatus.setText("Hired");

        }else if (album.getRejected().equals("1")){

            holder.txtStatus.setText("Rejected");

        }else if (album.getShortlisted().equals("1")){

            holder.txtStatus.setText("Shortlisted");

        }

        holder.shotlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 hireSearchItem =coursesItemList.get(position);

                addShortList(position);
                shortlistpushnotification(position);

                if (!hireSearchItem.getUsercode().equals("0")){

                    CONSULTANT_URL=Constants.GET_MY_CONSULTANT_URL+hireSearchItem.getUsercode();
                    Log.e("CONSULT",""+CONSULTANT_URL);

                    prepareCounsultentData(position);
                    consultshortlistpushnotification(position);
                }
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 hireSearchItem =coursesItemList.get(position);

                 addReject(position);
                rejectpushnotification(position);

                if (!hireSearchItem.getUsercode().equals("0")){

                    CONSULTANT_URL=Constants.GET_MY_CONSULTANT_URL+hireSearchItem.getUsercode();
                    Log.e("CONSULT",""+CONSULTANT_URL);

                    prepareCounsultentData(position);
                    consultrejectpushnotification(position);
                }
            }
        });

        holder.hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hireSearchItem =coursesItemList.get(position);
               Log.e("KEYYYYYY",""+hireSearchItem.getFirebasekey()+"pos"+position);

               addHire(position);
                hirepushnotification(position);

                if (!hireSearchItem.getUsercode().equals("0")){

                    CONSULTANT_URL=Constants.GET_MY_CONSULTANT_URL+hireSearchItem.getUsercode();
                    Log.e("CONSULT",""+CONSULTANT_URL);

                    prepareCounsultentData(position);
                    consulthirepushnotification(position);
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return coursesItemList.size();
    }


    private void addShortList(final int position){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());

        final HireSearchItem  album = coursesItemList.get(position);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_APPLICATION_URL,
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
                //params.put("jobid",album.getJobid());
                params.put("applicationid",album.getApplicationid());
                params.put("shortlisted","1");
                params.put("applied","0");
                params.put("rejected","0");
                params.put("hired","0");



                // Log.e("RESPONSE_Parasms: ",""+eventName+"\n"+venue+"\n"+tentdate+"\n"+startDaet);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void shortlistpushnotification(final int position){
        HireSearchItem  album = coursesItemList.get(position);
        try{


            RequestQueue queue = Volley.newRequestQueue(mContext);

            String url = "https://fcm.googleapis.com/fcm/send";
            String body ="Hi! "+album.getFullname()+" ! "+" Your Status for this job "+" '"+album.getTitle()+"' "+" is Shortlisted";
            Log.e("body",""+body);

            JSONObject data = new JSONObject();
            data.put("title", "Shortlisted !");
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

    private void addReject(final int position){


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());

        final HireSearchItem  album = coursesItemList.get(position);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_APPLICATION_URL,
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
                //params.put("jobid",album.getJobid());

                params.put("applicationid",album.getApplicationid());
                params.put("shortlisted","0");
                params.put("applied","0");
                params.put("rejected","1");
                params.put("hired","0");



                // Log.e("RESPONSE_Parasms: ",""+eventName+"\n"+venue+"\n"+tentdate+"\n"+startDaet);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void rejectpushnotification(final int position){
        HireSearchItem  album = coursesItemList.get(position);
        try{


            RequestQueue queue = Volley.newRequestQueue(mContext);

            String url = "https://fcm.googleapis.com/fcm/send";
            String body ="Hi! "+album.getFullname()+" ! "+" Your Status for this job "+" '"+album.getTitle()+"' "+" is Rejected";
            Log.e("body",""+body);

            JSONObject data = new JSONObject();
            data.put("title", "Rejected !");
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

    private void addHire(final int position){


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());

        final HireSearchItem  album = coursesItemList.get(position);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_APPLICATION_URL,
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
                //params.put("jobid",album.getJobid());
                params.put("applicationid",album.getApplicationid());
                params.put("shortlisted","1");
                params.put("applied","0");
                params.put("rejected","0");
                params.put("hired","1");



                // Log.e("RESPONSE_Parasms: ",""+eventName+"\n"+venue+"\n"+tentdate+"\n"+startDaet);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void hirepushnotification(final int position){
        HireSearchItem  album = coursesItemList.get(position);
        try{


            RequestQueue queue = Volley.newRequestQueue(mContext);

            String url = "https://fcm.googleapis.com/fcm/send";
            String body ="Hi! "+album.getFullname()+" ! "+" Your Status for this job "+" '"+album.getTitle()+"' "+" is Hired";
            Log.e("body",""+body);

            JSONObject data = new JSONObject();
            data.put("title", "Hired !");
            data.put("body", body);
            JSONObject notification_data = new JSONObject();
            notification_data.put("notification", data);
            notification_data.put("to",album.getFirebasekey());

            Log.e("HireFIREKEY",""+album.getFirebasekey());

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
    private void prepareCounsultentData(final int position) {

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

    private void consultshortlistpushnotification(final int position){
        HireSearchItem  album = coursesItemList.get(position);
        try{


            RequestQueue queue = Volley.newRequestQueue(mContext);

            String url = "https://fcm.googleapis.com/fcm/send";
            String body ="Hi! "+album.getFullname()+" ! "+" Your Status for this job "+" '"+album.getTitle()+"' "+" is Shortlisted";
            Log.e("body",""+body);

            JSONObject data = new JSONObject();
            data.put("title", "Shortlisted !");
            data.put("body", body);
            JSONObject notification_data = new JSONObject();
            notification_data.put("notification", data);
            notification_data.put("to",consultFCmKey);

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

    private void consultrejectpushnotification(final int position){
        HireSearchItem  album = coursesItemList.get(position);
        try{


            RequestQueue queue = Volley.newRequestQueue(mContext);

            String url = "https://fcm.googleapis.com/fcm/send";
            String body ="Hi! "+album.getFullname()+" ! "+" Your Status for this job "+" '"+album.getTitle()+"' "+" is Rejected";
            Log.e("body",""+body);

            JSONObject data = new JSONObject();
            data.put("title", "Rejected !");
            data.put("body", body);
            JSONObject notification_data = new JSONObject();
            notification_data.put("notification", data);
            notification_data.put("to",consultFCmKey);

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

    private void consulthirepushnotification(final int position){
        HireSearchItem  album = coursesItemList.get(position);
        try{


            RequestQueue queue = Volley.newRequestQueue(mContext);

            String url = "https://fcm.googleapis.com/fcm/send";
            String body ="Hi! "+album.getFullname()+" ! "+" Your Status for this job "+" '"+album.getTitle()+"' "+" is Hired";
            Log.e("body",""+body);

            JSONObject data = new JSONObject();
            data.put("title", "Hired !");
            data.put("body", body);
            JSONObject notification_data = new JSONObject();
            notification_data.put("notification", data);
            notification_data.put("to",consultFCmKey);

            Log.e("HireFIREKEY",""+consultFCmKey);

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
