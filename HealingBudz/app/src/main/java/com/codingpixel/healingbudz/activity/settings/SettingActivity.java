package com.codingpixel.healingbudz.activity.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.codingpixel.healingbudz.activity.settings.bussiness_listing_setting.Bussiness_Listing_Setting_Activity;
import com.codingpixel.healingbudz.activity.settings.notification_alerts.NotificationAndAlerts;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.adapter.SettingsRecylerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class SettingActivity extends AppCompatActivity implements SettingsRecylerAdapter.ItemClickListener {
    RecyclerView Setting_recyler_view;
    ImageView Back, Home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ChangeStatusBarColor(SettingActivity.this, "#171717");
        HideKeyboard(SettingActivity.this);

        Setting_recyler_view = (RecyclerView) findViewById(R.id.setting_recyler_View);

        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(SettingActivity.this, true);
                finish();
            }
        });
        ArrayList<String> data = new ArrayList<>();
        data.add("Profile Settings");
        data.add("Business Listing Settings");
//        data.add("Journal Settings");
        data.add("Notification & Alerts");
        data.add("");
        Setting_recyler_view = (RecyclerView) findViewById(R.id.setting_recyler_View);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SettingActivity.this);
        Setting_recyler_view.setLayoutManager(linearLayoutManager);
        SettingsRecylerAdapter recyler_adapter = new SettingsRecylerAdapter(SettingActivity.this, data);
        Setting_recyler_view.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getNotify()) {
            if (!this.isDestroyed()) {
                finish();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case 0:
                GoToProfile(SettingActivity.this, user.getUser_id());
                break;
            case 1:
                GoTo(SettingActivity.this, Bussiness_Listing_Setting_Activity.class);
                break;
//            case 2:
//                GoTo(SettingActivity.this, Journal_Setting_Activity.class);
//                break;
            case 2:
                GoTo(SettingActivity.this, NotificationAndAlerts.class);
                break;
        }
    }
}
