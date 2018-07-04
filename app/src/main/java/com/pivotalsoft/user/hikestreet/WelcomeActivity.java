package com.pivotalsoft.user.hikestreet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WelcomeActivity extends Activity implements View.OnClickListener {

    TextView txtJob,txtHire,txtCounsultant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        txtHire=(TextView)findViewById(R.id.txtHire);
        txtHire.setOnClickListener(this);

        txtJob=(TextView)findViewById(R.id.txtJob);
        txtJob.setOnClickListener(this);

        txtCounsultant=(TextView)findViewById(R.id.txtCounsultant);
        txtCounsultant.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.txtHire:

               /* txtHire.setTextColor(Color.parseColor("#FFFFFF"));
                txtHire.setBackgroundResource(R.drawable.welcome_layout_bg);*/

                hireDiloge();
                break;

            case R.id.txtJob:

               /* txtJob.setTextColor(Color.parseColor("#FFFFFF"));
                txtJob.setBackgroundResource(R.drawable.welcome_layout_bg);*/

                jobDiloge();
                break;

            case R.id.txtCounsultant:

               /* txtJob.setTextColor(Color.parseColor("#FFFFFF"));
                txtJob.setBackgroundResource(R.drawable.welcome_layout_bg);*/

                counsultantDiloge();
                break;
        }

    }

    private void hireDiloge(){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(WelcomeActivity.this);
        } else {
            builder = new AlertDialog.Builder(WelcomeActivity.this);
        }
        builder.setTitle("Role : RECRUITER")
                .setMessage("Are you sure you want to continue?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Intent hire =new Intent(WelcomeActivity.this,LoginActivity.class);
                        hire.putExtra("ROLE","Recruiter");
                        hire.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(hire);
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

    private void jobDiloge(){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(WelcomeActivity.this);
        } else {
            builder = new AlertDialog.Builder(WelcomeActivity.this);
        }
        builder.setTitle("Role : CANDIDATE")
                .setMessage("Are you sure you want to continue?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Intent job =new Intent(WelcomeActivity.this,LoginActivity.class);
                        job.putExtra("ROLE","Job Seeker");
                        job.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(job);
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


    private void counsultantDiloge(){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(WelcomeActivity.this);
        } else {
            builder = new AlertDialog.Builder(WelcomeActivity.this);
        }
        builder.setTitle("Role : CONSULTANT")
                .setMessage("Are you sure you want to continue?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Intent hire =new Intent(WelcomeActivity.this,LoginActivity.class);
                        hire.putExtra("ROLE","Consultant");
                        hire.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(hire);
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


    @Override
    public void onBackPressed() {
        //super.onBackPressed();


        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(WelcomeActivity.this);
        } else {
            builder = new AlertDialog.Builder(WelcomeActivity.this);
        }
        builder.setTitle("Confirm Exit ")
                .setMessage("Do you want to exit app?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        //moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
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
}
