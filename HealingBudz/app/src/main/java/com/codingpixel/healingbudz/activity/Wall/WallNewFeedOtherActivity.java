package com.codingpixel.healingbudz.activity.Wall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.interfaces.UserFollowCallBack;
//import com.google.android.gms.ads.MobileAds;

/*
 * Created by M_Muzammil Sharif on 09-Mar-18.
 */

public class WallNewFeedOtherActivity extends AppCompatActivity implements UserFollowCallBack {
    FrameLayout frame_feed;
    public static int USER_FEED_ID = 0;
    WallFeedsOtherFragment wallFeedsMainFragment;
    ImageView activity_wall_post_detail_back_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_new_feed_other);
        frame_feed = findViewById(R.id.frame_feed);
//        MobileAds.initialize(this, Constants.add_init);
        activity_wall_post_detail_back_btn = findViewById(R.id.activity_wall_post_detail_back_btn);
        activity_wall_post_detail_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallNewFeedOtherActivity.this.onBackPressed();
            }
        });
        wallFeedsMainFragment = new WallFeedsOtherFragment(USER_FEED_ID, true, null, WallNewFeedOtherActivity.this);
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
