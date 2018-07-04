package com.pivotalsoft.user.hikestreet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.pivotalsoft.user.hikestreet.Fragments.ApplicationFragment;
import com.pivotalsoft.user.hikestreet.Fragments.DashBoardFragment;
import com.pivotalsoft.user.hikestreet.Fragments.ProfileFragment;
import com.pivotalsoft.user.hikestreet.Fragments.SearchFragment;

import java.lang.reflect.Field;

public class BottamMenuActivity extends AppCompatActivity {

    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottam_menu);

        String fcmtkn = FirebaseInstanceId.getInstance().getToken();
        Log.e("App", "Token ["+fcmtkn+"]");

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 for private mode
         role = sp.getString("role", null);

        BottomNavigationView navigation1 = (BottomNavigationView) findViewById(R.id.navigation1);
        navigation1.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //BottomNavigationViewHelper.removeShiftMode(navigation1);

        BottomNavigationView navigation2 = (BottomNavigationView) findViewById(R.id.navigation);
        navigation2.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
       // BottomNavigationViewHelper.removeShiftMode(navigation2);

        if (role.equals("Recruiter")){

            navigation1.setVisibility(View.VISIBLE);
            navigation2.setVisibility(View.GONE);


        }else {

            navigation2.setVisibility(View.VISIBLE);
            navigation1.setVisibility(View.GONE);

        }



        // changeFragment(new HomeFragment());

        // default fragment after launch
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().show();
        DashBoardFragment homeFragment = new DashBoardFragment();
        FragmentManager manager1 =getSupportFragmentManager();
        manager1.beginTransaction().replace(R.id.content_layout,homeFragment,homeFragment.getTag()).commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    //changeFragment(new HomeFragment());
                    getSupportActionBar().setTitle("Home");
                    getSupportActionBar().show();
                    DashBoardFragment homeFragment = new DashBoardFragment();
                    FragmentManager manager1 = getSupportFragmentManager();
                    manager1.beginTransaction().replace(R.id.content_layout, homeFragment, homeFragment.getTag()).commit();

                    return true;
                case R.id.navigation_search:
                    // changeFragment(new SearchFragment());
                    if (role.equals("Recruiter")) {
                        getSupportActionBar().setTitle("My Jobs");
                        getSupportActionBar().show();
                    }else {
                        getSupportActionBar().setTitle("Search");
                        getSupportActionBar().show();
                    }

                    SearchFragment searchFragment = new SearchFragment();
                    FragmentManager manager3 = getSupportFragmentManager();
                    manager3.beginTransaction().replace(R.id.content_layout, searchFragment, searchFragment.getTag()).commit();

                    return true;
                case R.id.navigation_chats:
                    // changeFragment(new ProfileFragment());

                    if (role.equals("Recruiter")) {
                        getSupportActionBar().setTitle("Candidates");
                        getSupportActionBar().show();
                    }else {
                        getSupportActionBar().setTitle("Applications");
                        getSupportActionBar().show();
                    }

                    ApplicationFragment applicationFragment = new ApplicationFragment();
                    FragmentManager manager5 = getSupportFragmentManager();
                    manager5.beginTransaction().replace(R.id.content_layout, applicationFragment, applicationFragment.getTag()).commit();
                    return true;

                /*case R.id.navigation_profile:
                    // changeFragment(new ProfileFragment());
                    getSupportActionBar().setTitle("Profile");
                    getSupportActionBar().show();
                    ProfileFragment profileFragment = new ProfileFragment();
                    FragmentManager manager6 = getSupportFragmentManager();
                    manager6.beginTransaction().replace(R.id.content_layout, profileFragment, profileFragment.getTag()).commit();
                    return true;*/
            }
            return false;
        }
    };

    static class BottomNavigationViewHelper {

        @SuppressLint("RestrictedApi")
        static void removeShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                if(menuView.getChildCount()<6)
                {
                    for (int i = 0; i < menuView.getChildCount(); i++) {
                        BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                        //noinspection RestrictedApi
                        item.setShiftingMode(false);
                        // set once again checked value, so view will be updated
                        //noinspection RestrictedApi
                        item.setChecked(item.getItemData().isChecked());
                    }
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }

}

    @Override
    public void onBackPressed() {
        //super.onBackPressed();


        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(BottamMenuActivity.this);
        } else {
            builder = new AlertDialog.Builder(BottamMenuActivity.this);
        }
        builder.setTitle("Confirm Exit ")
                .setMessage("Do you want to exit app?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        // moveTaskToBack(true);
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
