package com.pivotalsoft.user.hikestreet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.pivotalsoft.user.hikestreet.Constants.Constants;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPersonalInformationActivity extends AppCompatActivity implements View.OnClickListener {
    //Image request code
    private int PICK_IMAGE_REQUEST1 = 234;



    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath1;
    final int EXPERIENCE_ALERTDIALOG = 1;
    final int QUALIFICATION_ALERTDIALOG = 2;
    final int GENDER_ALERTDIALOG = 3;
    final boolean checked_state[] = {false, false, false};
    final CharSequence[] experienceList = {"Fresher","0-1 years","1-3 years","3-5 years","5-10 years"," years10+"};
    final CharSequence[] qualificationList = {"Graduation and above","10+2 Pass","10th Pass","Below 10th","Diploma","Other than Mentioned"};
    final CharSequence[] genderList = {"Male","Female","Others"};

    private ProgressDialog pDialog;
    private DatePickerDialog DateofBirthPickerDialog;
    private SimpleDateFormat dateFormatter;

    private final int REQUEST_CODE_PLACEPICKER = 3;
    String latitude,longitude;
    CircleImageView circleImageView;
    EditText etLocation,etEmail,etDob,etExperience,etQualification,etGender,etLang,etPersonNAme,etAboutme;
    String location,email,dob,experience,qualification,gender,lang,personName,aboutme,mobileNo,role,userid,profilePic;
    FloatingActionButton btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add Personal Information");

        requestStoragePermission();

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getSharedPreferences("MyPref",  Context.MODE_PRIVATE); // 0 for private mode
        mobileNo = sp.getString("mobileno", null);
        role = sp.getString("role", null );
        userid=sp.getString("userid",null);
        Log.e("USERID",""+userid);
        Log.e("mobileNo",""+mobileNo);


        personName=getIntent().getStringExtra("fullname");
        aboutme=getIntent().getStringExtra("about");
        location=getIntent().getStringExtra("location");
        latitude=getIntent().getStringExtra("latitude");
        longitude=getIntent().getStringExtra("longitude");
        email=getIntent().getStringExtra("emailid");
        dob=getIntent().getStringExtra("dob");
        experience=getIntent().getStringExtra("experience");
        qualification=getIntent().getStringExtra("qualification");
        lang=getIntent().getStringExtra("languages");
        gender=getIntent().getStringExtra("gender");
        profilePic=getIntent().getStringExtra("profilepic");
        Log.e("profilepic",""+profilePic);



        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        etLocation=(EditText) findViewById(R.id.etLocation);
        etLocation.setText(location);
        etLocation.setOnClickListener(this);

        etEmail=(EditText) findViewById(R.id.etEmail);
        etEmail.setText(email);


        etDob=(EditText) findViewById(R.id.etDob);
        etDob.setText(dob);
        etDob.setInputType(InputType.TYPE_NULL);
        etDob.requestFocus();
        etDob.setOnClickListener(this);

        etExperience=(EditText) findViewById(R.id.etExperience);
        etExperience.setText(experience);
        etExperience.setOnClickListener(this);

        etQualification=(EditText) findViewById(R.id.etQualification);
        etQualification.setText(qualification);
        etQualification.setOnClickListener(this);

        etGender=(EditText) findViewById(R.id.etGender);
        etGender.setText(gender);
        etGender.setOnClickListener(this);

        etLang=(EditText) findViewById(R.id.etKnownLang);
        etLang.setText(lang);

        etPersonNAme=(EditText) findViewById(R.id.etFullName);
        etPersonNAme.setText(personName);

        etAboutme=(EditText) findViewById(R.id.editAboutMe);
        etAboutme.setText(aboutme);

        circleImageView=(CircleImageView)findViewById(R.id.imageProfile);
        circleImageView.setOnClickListener(this);



        try {

            if (profilePic.equals("null")){

                Glide.with(this).load(R.drawable.avatar_author).into(circleImageView);
            }else {
                Glide.with(this).load(profilePic).into(circleImageView);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        btnSave =(FloatingActionButton)findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(this);

        setDateTimeField();

    }

    //datepicker
    private void setDateTimeField(){

        Calendar newCalendar = Calendar.getInstance();
        DateofBirthPickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDob.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.imageProfile:

                showFileChooser();

                break;

            case R.id.etLocation:
                startPlacePickerActivity();
                break;

            case R.id.buttonSave:
                saveDate();
                break;

            case R.id.etDob:
                DateofBirthPickerDialog.show();
                break;


            case R.id.etExperience:
                showDialog(EXPERIENCE_ALERTDIALOG);
                break;


            case R.id.etQualification:
                showDialog(QUALIFICATION_ALERTDIALOG);
                break;

            case R.id.etGender:
                showDialog(GENDER_ALERTDIALOG);
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

        Geocoder gcd=new Geocoder(AddPersonalInformationActivity.this, Locale.getDefault());
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

                if (locality!=null) {
                    etLocation.setText(locality);
                }
                else {
                    Toast.makeText(AddPersonalInformationActivity.this,"No data Found for this ... Please Choose Another City",Toast.LENGTH_SHORT).show();
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


    /*triggered by showDialog method. onCreateDialog creates a dialog*/
    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {

            case EXPERIENCE_ALERTDIALOG:

                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddPersonalInformationActivity.this)
                        .setTitle("Total Experience")
                        .setSingleChoiceItems(experienceList, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                               // Toast.makeText(getApplicationContext(),"The selected" + experienceList[which], Toast.LENGTH_LONG).show();

                                etExperience.setText(experienceList[which]);

                                //dismissing the dialog when the user makes a selection.
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog1 = builder1.create();
                return alertdialog1;

            case QUALIFICATION_ALERTDIALOG:

                AlertDialog.Builder builder2 = new AlertDialog.Builder(AddPersonalInformationActivity.this)
                        .setTitle("Highest Qualification")
                        .setSingleChoiceItems(qualificationList, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                               // Toast.makeText(getApplicationContext(),"The selected" + qualificationList[which], Toast.LENGTH_LONG).show();

                                etQualification.setText(qualificationList[which]);

                                //dismissing the dialog when the user makes a selection.
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog2 = builder2.create();
                return alertdialog2;

            case GENDER_ALERTDIALOG:

                AlertDialog.Builder builder3 = new AlertDialog.Builder(AddPersonalInformationActivity.this)
                        .setTitle("Gender")
                        .setSingleChoiceItems(genderList, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                               // Toast.makeText(getApplicationContext(),"The selected" + genderList[which], Toast.LENGTH_LONG).show();

                                etGender.setText(genderList[which]);

                                //dismissing the dialog when the user makes a selection.
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog3 = builder3.create();
                return alertdialog3;
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


            case QUALIFICATION_ALERTDIALOG:
                AlertDialog qualdialog = (AlertDialog) dialog;
                ListView list_qual = qualdialog.getListView();
                for (int i = 0; i < list_qual.getCount(); i++) {
                    list_qual.setItemChecked(i, false);
                }
                break;

            case GENDER_ALERTDIALOG:
                AlertDialog prepare_radio_dialog = (AlertDialog) dialog;
                ListView list_radio = prepare_radio_dialog.getListView();
                for (int i = 0; i < list_radio.getCount(); i++) {
                    list_radio.setItemChecked(i, false);
                }
                break;

        }
    }

    private void saveDate(){

        personName=etPersonNAme.getText().toString().trim();
        aboutme=etAboutme.getText().toString().trim();
        location =etLocation.getText().toString().trim();
        email =etEmail.getText().toString().trim();
        dob =etDob.getText().toString().trim();
        experience =etExperience.getText().toString().trim();
        qualification =etQualification.getText().toString().trim();
        gender =etGender.getText().toString().trim();
        lang =etLang.getText().toString().trim();



        if (!location.isEmpty() && !email.isEmpty()  && !dob.isEmpty() && !experience.isEmpty() && !qualification.isEmpty()  && !gender.isEmpty() && !lang.isEmpty()
                && !personName.isEmpty()  && !aboutme.isEmpty()) {

            uploadProfileImageMultipart();

        } else {
            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
        }
    }



      /*
   * This is the method responsible for image upload
   * We need the full image path and the name for the image in this method
   * */

    public void uploadProfileImageMultipart() {

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Uploading.....");
        showDialog();

        if (filePath1 != null) {

            //getting the actual path of the image
            String path2 = getPath(filePath1);
            Log.e("Path",""+path2+"\n"+path2);

            //Uploading code
            try {


                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(AddPersonalInformationActivity.this, uploadId, Constants.UPDATE_USER_URL)
                        .addFileToUpload(path2, "imagePath") //Adding file
                        .addParameter("fullname",personName)//Adding text parameter to the request
                        .addParameter("about",aboutme)
                        .addParameter("location",location)
                        .addParameter("latitude",latitude)
                        .addParameter("longitude",longitude)
                        .addParameter("emailid",email)
                        .addParameter("mobileno",mobileNo)
                        .addParameter("dob",dob)
                        .addParameter("experience",experience)
                        .addParameter("qualification",qualification)
                        .addParameter("gender",gender)
                        .addParameter("languages",lang)
                        .addParameter("role",role)
                        .addParameter("userid",userid)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent =new Intent(AddPersonalInformationActivity.this,ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        hideDialog();
                    }
                }, 5000);



            } catch (Exception exc) {
                Toast.makeText(AddPersonalInformationActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(AddPersonalInformationActivity.this, "Please Select image", Toast.LENGTH_SHORT).show();

        }



    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);
    }


    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath1 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                int size = imageInByte.length/1024;
                Log.e("LENTGH",""+size);

                if (size<200){

                  circleImageView.setImageBitmap(bitmap);
                }
                else {

                    Toast.makeText(AddPersonalInformationActivity.this, "please select Image size must be 200 kb  or below.", Toast.LENGTH_LONG).show();
                }



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CODE_PLACEPICKER){
                displaySelectedPlaceFromPlacePicker(data);
            }
        }
    }



    //method to get the file path from uri
    public String getPath(Uri uri) {

        Cursor cursor = AddPersonalInformationActivity.this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = AddPersonalInformationActivity.this.getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;

       /* String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);*/
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(AddPersonalInformationActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(AddPersonalInformationActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission


        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(AddPersonalInformationActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(AddPersonalInformationActivity.this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(AddPersonalInformationActivity.this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
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
