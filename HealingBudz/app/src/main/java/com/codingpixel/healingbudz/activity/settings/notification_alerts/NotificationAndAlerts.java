package com.codingpixel.healingbudz.activity.settings.notification_alerts;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.NotificationAndAlertSettingDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.adapter.NotificationAndAlertSettingsRecylerAdapter;
import com.codingpixel.healingbudz.adapter.NotificationSettingKeywordRecylerAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_groups;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_notification;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_remove_tag;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;
import static com.codingpixel.healingbudz.test_data.Notification_Alert_Setting_testData.notification_data;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class NotificationAndAlerts extends AppCompatActivity implements NotificationAndAlertSettingsRecylerAdapter.ItemClickListener, APIResponseListner, NotificationSettingKeywordRecylerAdapter.ItemClickListenerKey {
    ImageView Back, Home;
    RecyclerView recyclerView;
    RecyclerView keyword_recyclerView;
    ArrayList<NotificationAndAlertSettingDataModel> notification_data = notification_data();
    NotificationAndAlertSettingsRecylerAdapter recyler_adapter;
    NotificationSettingKeywordRecylerAdapter addNewJournalSelectTagRecylerAdapter;
    List<String> tag_data = new ArrayList<>();
    List<Integer> tag_data_id = new ArrayList<>();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_and_alerts);
        ChangeStatusBarColor(NotificationAndAlerts.this, "#171717");
        HideKeyboard(NotificationAndAlerts.this);

        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateSetting();
                finish();
            }
        });

        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateSetting();
                GoToHome(NotificationAndAlerts.this, true);
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationAndAlerts.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyler_adapter = new NotificationAndAlertSettingsRecylerAdapter(NotificationAndAlerts.this, notification_data);
        recyclerView.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
        recyclerView.setNestedScrollingEnabled(false);


        keyword_recyclerView = (RecyclerView) findViewById(R.id.keywords_recyler_view);


        addNewJournalSelectTagRecylerAdapter = new NotificationSettingKeywordRecylerAdapter(NotificationAndAlerts.this, tag_data);
        addNewJournalSelectTagRecylerAdapter.setmClickListener(this);
        FlexboxLayoutManager layoutManager_1 = new FlexboxLayoutManager(NotificationAndAlerts.this);
        layoutManager_1.setFlexDirection(FlexDirection.ROW);
        layoutManager_1.setJustifyContent(JustifyContent.FLEX_START);
        keyword_recyclerView.setLayoutManager(layoutManager_1);
        keyword_recyclerView.setAdapter(addNewJournalSelectTagRecylerAdapter);
        keyword_recyclerView.setNestedScrollingEnabled(false);
        SettingAPiCall();
    }

    private void SettingAPiCall() {
        new VollyAPICall(NotificationAndAlerts.this, true, URL.get_notification_settings, new JSONObject(), user.getSession_key(), Request.Method.GET, NotificationAndAlerts.this, APIActions.ApiActions.get_notification);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public void UpdateSetting() {
        try {
            JSONObject jsonObject = new JSONObject();
            if (notification_data.get(0).isCheck()) {
                jsonObject.put("new_question", 1);
            } else {
                jsonObject.put("new_question", 0);
            }
            if (notification_data.get(1).isCheck()) {
                jsonObject.put("follow_question_answer", 1);
            } else {
                jsonObject.put("follow_question_answer", 0);
            }


//            if (notification_data.get(3).isCheck()) {
//                jsonObject.put("public_joined", 1);
//            } else {
//                jsonObject.put("public_joined", 0);
//            }
//
//
//            if (notification_data.get(4).isCheck()) {
//                jsonObject.put("private_joined", 1);
//            } else {
//                jsonObject.put("private_joined", 0);
//            }

            if (notification_data.get(2).isCheck()) {
                jsonObject.put("follow_strains", 1);
            } else {
                jsonObject.put("follow_strains", 0);
            }

            if (notification_data.get(4).isCheck()) {
                jsonObject.put("specials", 1);
            } else {
                jsonObject.put("specials", 0);
            }


            if (notification_data.get(5).isCheck()) {
                jsonObject.put("shout_out", 1);
            } else {
                jsonObject.put("shout_out", 0);
            }

            if (notification_data.get(6).isCheck()) {
                jsonObject.put("message", 1);
            } else {
                jsonObject.put("message", 0);
            }


            if (notification_data.get(8).isCheck()) {
                jsonObject.put("follow_profile", 1);
            } else {
                jsonObject.put("follow_profile", 0);
            }


//            if (notification_data.get(12).isCheck()) {
//                jsonObject.put("follow_journal", 1);
//            } else {
//                jsonObject.put("follow_journal", 0);
//            }

            if (notification_data.get(9).isCheck()) {
                jsonObject.put("your_strain", 1);
            } else {
                jsonObject.put("your_strain", 0);
            }


            if (notification_data.get(11).isCheck()) {
                jsonObject.put("like_question", 1);
            } else {
                jsonObject.put("like_question", 0);
            }

            if (notification_data.get(12).isCheck()) {
                jsonObject.put("like_answer", 1);
            } else {
                jsonObject.put("like_answer", 0);
            }

//            if (notification_data.get(17).isCheck()) {
//                jsonObject.put("like_journal", 1);
//            } else {
//                jsonObject.put("like_journal", 0);
//            }

            new VollyAPICall(NotificationAndAlerts.this, false, URL.add_notificarion_setting, jsonObject, user.getSession_key(), Request.Method.POST, NotificationAndAlerts.this, get_groups);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SetSetting(JSONObject jsonObject) {
        try {

            if (jsonObject.getInt("new_question") == 1) {

                notification_data.get(0).setCheck(true);
            } else {
                notification_data.get(0).setCheck(false);
            }
            if (jsonObject.getInt("follow_question_answer") == 1) {


                notification_data.get(1).setCheck(true);
            } else {
                notification_data.get(1).setCheck(false);
            }

//
//            if (jsonObject.getInt("public_joined") == 1) {
//
//                notification_data.get(3).setCheck(true);
//            } else {
//                notification_data.get(3).setCheck(false);
//            }
//
//
//            if (jsonObject.getInt("private_joined") == 1) {
//
//                notification_data.get(4).setCheck(true);
//            } else {
//                notification_data.get(4).setCheck(false);
//            }

            if (jsonObject.getInt("follow_strains") == 1) {

                notification_data.get(2).setCheck(true);
            } else {
                notification_data.get(2).setCheck(false);
            }

            if (jsonObject.getInt("specials") == 1) {

                notification_data.get(4).setCheck(true);
            } else {
                notification_data.get(4).setCheck(false);
            }


            if (jsonObject.getInt("shout_out") == 1) {

                notification_data.get(5).setCheck(true);
            } else {
                notification_data.get(5).setCheck(false);
            }

            if (jsonObject.getInt("message") == 1) {

                notification_data.get(6).setCheck(true);
            } else {
                notification_data.get(6).setCheck(false);
            }

            if (jsonObject.getInt("follow_profile") == 1) {

                notification_data.get(8).setCheck(true);
            } else {
                notification_data.get(8).setCheck(false);
            }


//            if (jsonObject.getInt("follow_journal") == 1) {
//
//                notification_data.get(12).setCheck(true);
//            } else {
//                notification_data.get(12).setCheck(false);
//            }

            if (jsonObject.getInt("your_strain") == 1) {

                notification_data.get(9).setCheck(true);
            } else {
                notification_data.get(9).setCheck(false);
            }
            if (jsonObject.getInt("like_question") == 1) {

                notification_data.get(11).setCheck(true);
            } else {
                notification_data.get(11).setCheck(false);
            }

            if (jsonObject.getInt("like_answer") == 1) {

                notification_data.get(12).setCheck(true);
            } else {
                notification_data.get(12).setCheck(false);
            }
//
//            if (jsonObject.getInt("like_journal") == 1) {
//
//                notification_data.get(17).setCheck(true);
//            } else {
//                notification_data.get(17).setCheck(false);
//            }
            recyler_adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == get_notification) {
            try {
                Log.d("onRequestSuccess: ", response);
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jObject = jsonObject.getJSONObject("successData");
                if (!jObject.isNull("notification_setting")) {
                    SetSetting(jObject.getJSONObject("notification_setting"));
                }
                if (jObject.getJSONArray("tags").length() > 0) {
                    SetKeyWord(jObject.getJSONArray("tags"));
                }
                recyler_adapter.notifyDataSetChanged();
                addNewJournalSelectTagRecylerAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();

            }
        } else if (apiActions == get_groups) {
            Log.d("onRequestSuccess: ", response);
        } else if (apiActions == get_remove_tag) {

            Log.d("onRequestSuccess: ", response);
        }


    }

    private void SetKeyWord(JSONArray tags) throws JSONException {
        tag_data.clear();
        tag_data_id.clear();
        for (int i = 0; i < tags.length(); i++) {
            tag_data.add(tags.getJSONObject(i).getJSONObject("tag").getString("title"));
            tag_data_id.add(tags.getJSONObject(i).getInt("id"));
        }
        addNewJournalSelectTagRecylerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("onRequestError: ", response);
        if (apiActions == get_notification) {
            Log.d("onRequestError: ", response);
        } else if (apiActions == get_groups) {
            Log.d("onRequestError: ", response);
        } else if (apiActions == get_remove_tag) {

            Log.d("onRequestError: ", response);
        }
    }

    @Override
    public void onItemClickKey(View view, int position) {
        tag_data.remove(position);
        addNewJournalSelectTagRecylerAdapter.notifyDataSetChanged();
        new VollyAPICall(NotificationAndAlerts.this, true, URL.remove_tag + "/" + tag_data_id.get(position), new JSONObject(), user.getSession_key(), Request.Method.GET, NotificationAndAlerts.this, APIActions.ApiActions.get_remove_tag);
        tag_data_id.remove(position);
    }
}
