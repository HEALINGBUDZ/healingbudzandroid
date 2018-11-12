package com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity.groupsDataModel;
import static com.codingpixel.healingbudz.activity.home.side_menu.my_groups.MyGroupsActivity.isRefreshable;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_group_settings;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.leave_group;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.remove_group;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class GroupSettingActivity extends AppCompatActivity implements APIResponseListner {
    Button btn_private, btn_public;
    ImageView Back, Home;
    TextView Group_Name;
    Button Leave_Group;
    RadioGroup radio_group;
    int mute_forever = 0;
    int mute_hours = 0;
    public static boolean isGroupClose = false;
    boolean isON = false, isOff = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_setting);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        Group_Name = (TextView) findViewById(R.id.group_name_Setting_page);
        Leave_Group = (Button) findViewById(R.id.leave_group);
        Group_Name.setText(groupsDataModel.getName());
        ChangeStatusBarColor(GroupSettingActivity.this, "#171717");
        HideKeyboard(GroupSettingActivity.this);
        btn_private = (Button) findViewById(R.id.private_btn);
        btn_public = (Button) findViewById(R.id.public_btn);
        btn_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOff) {
                    isON = false;
                    isOff = true;
                    btn_private.setBackgroundResource(R.drawable.toggle_btn_private);
                    btn_public.setBackground(null);
                }
            }
        });
        btn_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isON) {
                    isOff = false;
                    isON = true;
                    btn_public.setBackgroundResource(R.drawable.toggle_btn_public);
                    btn_private.setBackground(null);
                }
            }
        });
        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPrefrences.setBool("isMuteOn_" + groupsDataModel.getId(), isON, GroupSettingActivity.this);
                SharedPrefrences.setInt("MuteHours_" + groupsDataModel.getId(), mute_hours, GroupSettingActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("group_id", groupsDataModel.getId());
                    if (isON) {
                        jsonObject.put("is_mute", 1);
                    } else {
                        jsonObject.put("is_mute", 0);
                    }
                    jsonObject.put("mute_time", mute_hours);
                    jsonObject.put("mute_forever", mute_forever);
                    new VollyAPICall(GroupSettingActivity.this, false, URL.add_group_settings, jsonObject, user.getSession_key(), Request.Method.POST, GroupSettingActivity.this, add_group_settings);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(GroupSettingActivity.this, true);
                finish();
            }
        });
        if (groupsDataModel.isCurrentUserAdmin()) {
            Leave_Group.setText("CLOSE GROUP");
        } else {
            Leave_Group.setText("LEAVE GROUP");
        }
        Leave_Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupsDataModel.isCurrentUserAdmin()) {
                    JSONObject jsonObject = new JSONObject();
                    new VollyAPICall(GroupSettingActivity.this, true, URL.remove_group + "/" + groupsDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, GroupSettingActivity.this, remove_group);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("group_id", groupsDataModel.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new VollyAPICall(GroupSettingActivity.this, true, URL.leave_group, jsonObject, user.getSession_key(), Request.Method.POST, GroupSettingActivity.this, leave_group);
                }
            }
        });


        if (SharedPrefrences.getBool("isMuteOn_" + groupsDataModel.getId(), GroupSettingActivity.this)) {
            isOff = false;
            isON = true;
            btn_public.setBackgroundResource(R.drawable.toggle_btn_public);
            btn_private.setBackground(null);
        } else {
            isON = false;
            isOff = true;
            btn_private.setBackgroundResource(R.drawable.toggle_btn_private);
            btn_public.setBackground(null);
        }
        int checck = SharedPrefrences.getInt("MuteHours_" + groupsDataModel.getId(), GroupSettingActivity.this);
        mute_hours = checck;
        if (checck == 1) {
            checck = R.id.one;
        } else if (checck == 2) {
            checck = R.id.two;
        } else if (checck == 3) {
            checck = R.id.three;
        } else if (checck == 8) {
            checck = R.id.four;
        } else if (checck == 0) {
            checck = R.id.five;
        }
        radio_group.check(checck);

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.one:
                        mute_forever = 0;
                        mute_hours = 1;
                        Log.d("click", "one");
                        break;
                    case R.id.two:
                        mute_forever = 0;
                        mute_hours = 2;
                        Log.d("click", "two");
                        break;
                    case R.id.three:
                        mute_forever = 0;
                        mute_hours = 3;
                        Log.d("click", "tree");
                        break;
                    case R.id.four:
                        mute_forever = 0;
                        mute_hours = 8;
                        Log.d("click", "four");
                        break;
                    case R.id.five:
                        mute_forever = 1;
                        mute_hours = 0;
                        Log.d("click", "five");
                        break;
                }
            }
        });

    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        isGroupClose = true;
        if (apiActions == remove_group) {
            isRefreshable = true ;
            finish();
        } else if (apiActions == leave_group) {
            isRefreshable = true ;
            finish();
        } else if (apiActions == add_group_settings) {
            Log.d("response", response);
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
