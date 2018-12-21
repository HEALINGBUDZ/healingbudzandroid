package com.codingpixel.healingbudz.activity.Wall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.ReportItView.WallTopDropDowns;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.interfaces.UserFollowCallBack;
import com.codingpixel.healingbudz.static_function.IntentFunction;
//import com.google.android.gms.ads.MobileAds;

/*
 * Created by M_Muzammil Sharif on 09-Mar-18.
 */

public class WallNewFeedOtherActivity extends AppCompatActivity implements UserFollowCallBack {
    FrameLayout frame_feed;
    public static int USER_FEED_ID = 0;
    public static boolean isFromUserFile = false;
    WallFeedsOtherFragment wallFeedsMainFragment;
    ImageView activity_wall_post_detail_back_btn, home_btn, activity_wall_post_detail_add_btn;
    WallTopDropDowns topDropDowns;
    TextView txt_head;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_new_feed_other);
        frame_feed = findViewById(R.id.frame_feed);
        activity_wall_post_detail_add_btn = findViewById(R.id.activity_wall_post_detail_add_btn);
        home_btn = findViewById(R.id.home_btn);
        txt_head = findViewById(R.id.txt_head);
        if (USER_FEED_ID == Splash.user.getUser_id() && !isFromUserFile) {
            txt_head.setText("My Buzz");
            home_btn.setVisibility(View.VISIBLE);
            activity_wall_post_detail_add_btn.setVisibility(View.VISIBLE);
        } else {
            txt_head.setText("The Buzz");
            home_btn.setVisibility(View.GONE);
            activity_wall_post_detail_add_btn.setVisibility(View.GONE);
        }
        activity_wall_post_detail_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wallFeedsMainFragment != null) {
                    wallFeedsMainFragment.callFragForNewPost();
                }
//                callFragForNewPost
//                Utility.launchActivity(WallNewFeedOtherActivity.this, WallNewPostActivity.class, false, null);
            }
        });
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFunction.GoToHome(WallNewFeedOtherActivity.this, true);
            }
        });
//        MobileAds.initialize(this, Constants.add_init);
        activity_wall_post_detail_back_btn = findViewById(R.id.activity_wall_post_detail_back_btn);
        activity_wall_post_detail_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallNewFeedOtherActivity.this.onBackPressed();
            }
        });
        topDropDowns = new WallTopDropDowns(this);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_wall_post_detail_main_view);
        layout.addView(topDropDowns.getView());

        wallFeedsMainFragment = new WallFeedsOtherFragment(USER_FEED_ID, true, topDropDowns, WallNewFeedOtherActivity.this);
        Bundle bundle = new Bundle();
        bundle.putInt("USER_ID", USER_FEED_ID);
        wallFeedsMainFragment.setArguments(bundle);
        if (wallFeedsMainFragment != null && !wallFeedsMainFragment.isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame_feed, wallFeedsMainFragment, "1");
            transaction.commit();
        }
    }


    @Override
    public void onUserFollow(int userID) {

    }

    @Override
    public void onUserUnfollow(int userID) {

    }
}
