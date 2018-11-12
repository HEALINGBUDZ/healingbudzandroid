package com.codingpixel.healingbudz.activity.Registration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.AddNewBudzMapActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzFeedAlertDialog;
import com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.MyRewardzActivity;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;
import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;

public class FinalStepProfileComplete extends AppCompatActivity implements BudzFeedAlertDialog.OnDialogFragmentClickListener {
    Button Skip;
    Button Add_budz, View_My_Profile;
    TextView reward_activity;
    public static boolean isFromSignup = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_step_profile_complete);
        FullScreen(FinalStepProfileComplete.this);


        Skip = (Button) findViewById(R.id.skip);
        reward_activity = findViewById(R.id.reward_activity);
        reward_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefrences.setBool("isFromSignup", true, FinalStepProfileComplete.this);
                GoTo(FinalStepProfileComplete.this, MyRewardzActivity.class);
                finish();
            }
        });
        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(FinalStepProfileComplete.this, false);
                finish();
            }
        });

        Add_budz = (Button) findViewById(R.id.add_new_budz);
        Add_budz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromSignup = true;
//                BudzFeedAlertDialog budzFeedAlertDialog = BudzFeedAlertDialog.newInstance( FinalStepProfileComplete.this , false);
//                budzFeedAlertDialog.show(getSupportFragmentManager(), "dialog");
                SharedPrefrences.setBool("isFromProfile", true, FinalStepProfileComplete.this);
                GoTo(FinalStepProfileComplete.this, AddNewBudzMapActivity.class);
                finish();
            }
        });

        View_My_Profile = (Button) findViewById(R.id.view_my_profile);
        View_My_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefrences.setBool("isFromSignup", true, FinalStepProfileComplete.this);
                GoToProfile(FinalStepProfileComplete.this, user.getUser_id());
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void onCountinueFreeListingBtnClink(BudzFeedAlertDialog dialog) {
        SharedPrefrences.setBool("isFromProfile", true, FinalStepProfileComplete.this);
        GoTo(FinalStepProfileComplete.this, AddNewBudzMapActivity.class);
        finish();
    }

    @Override
    public void onSubcribeNowBtnClick(BudzFeedAlertDialog dialog) {
        CustomeToast.ShowCustomToast(FinalStepProfileComplete.this, "Purchase First...", Gravity.TOP);
    }
}
