package com.codingpixel.healingbudz.activity.settings.journal_setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_journal_settings;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class DataSettingScreen extends AppCompatActivity implements APIResponseListner {
    ImageView Back, Home;
    Button btn_off, btn_on;
    Button btn_off_notify, btn_on_notify;
    LinearLayout Switch_Layout;
    LinearLayout Switch_Layout_notify;
    boolean isQucikEntry = true;
    boolean isQucikEntry_notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_settings);
        ChangeStatusBarColor(DataSettingScreen.this, "#171717");
        HideKeyboard(DataSettingScreen.this);

        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                finish();
            }
        });

        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                GoToHome(DataSettingScreen.this, true);
                finish();
            }
        });

        btn_off = (Button) findViewById(R.id.private_btn);
        btn_on = (Button) findViewById(R.id.public_btn);
        Switch_Layout = (LinearLayout) findViewById(R.id.switch_layout);
        Switch_Layout.setClickable(true);
        Switch_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isQucikEntry) {
                    SharedPrefrences.setBool("journal_data_setting_wifi_only", false, DataSettingScreen.this);

                    btn_on.setText("");
                    btn_off.setText("OFF");
                    btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                    btn_on.setBackground(null);
                    isQucikEntry = false;
                } else {
                    SharedPrefrences.setBool("journal_data_setting_wifi_only", true, DataSettingScreen.this);

                    btn_on.setText("ON");
                    btn_off.setText("");
                    btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
                    btn_off.setBackground(null);
                    isQucikEntry = true;
                }
            }
        });

        btn_off_notify = (Button) findViewById(R.id.btn_off_notify);
        btn_on_notify = (Button) findViewById(R.id.btn_onn_notify);
        Switch_Layout_notify = (LinearLayout) findViewById(R.id.switch_layout_notify);
        Switch_Layout_notify.setClickable(true);
        Switch_Layout_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isQucikEntry_notify) {
                    SharedPrefrences.setBool("journal_data_setting_sync_notification", false, DataSettingScreen.this);

                    btn_on_notify.setText("");
                    btn_off_notify.setText("OFF");
                    btn_off_notify.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                    btn_on_notify.setBackground(null);
                    isQucikEntry_notify = false;
                } else {
                    SharedPrefrences.setBool("journal_data_setting_sync_notification", true, DataSettingScreen.this);

                    btn_on_notify.setText("ON");
                    btn_off_notify.setText("");
                    btn_on_notify.setBackgroundResource(R.drawable.toggle_btn_public);
                    btn_off_notify.setBackground(null);
                    isQucikEntry_notify = true;
                }
            }
        });


        if (SharedPrefrences.getBool("journal_data_setting_sync_notification", DataSettingScreen.this)) {
            btn_on_notify.setText("ON");
            btn_off_notify.setText("");
            btn_on_notify.setBackgroundResource(R.drawable.toggle_btn_public);
            btn_off_notify.setBackground(null);
            isQucikEntry_notify = true;
        } else {
            btn_on_notify.setText("");
            btn_off_notify.setText("OFF");
            btn_off_notify.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
            btn_on_notify.setBackground(null);
            isQucikEntry_notify = false;
        }


        if (!SharedPrefrences.getBool("journal_data_setting_wifi_only", DataSettingScreen.this)) {
            btn_on.setText("");
            btn_off.setText("OFF");
            btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
            btn_on.setBackground(null);
            isQucikEntry = false;
        } else {
            btn_on.setText("ON");
            btn_off.setText("");
            btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
            btn_off.setBackground(null);
            isQucikEntry = true;
        }
        getJournetSettingApi();
    }

    public void getJournetSettingApi() {
        new VollyAPICall(DataSettingScreen.this, false, com.codingpixel.healingbudz.network.model.URL.get_journal_settings, new JSONObject(), user.getSession_key(), Request.Method.GET, DataSettingScreen.this, get_journal_settings);
    }

    public void updateData() {
        try {
            int check_wifi = 0, check_sync = 0;
            if (isQucikEntry) {
                check_wifi = 1;
            }
            if (isQucikEntry_notify) {
                check_sync = 1;
            }
            new VollyAPICall(DataSettingScreen.this, false, com.codingpixel.healingbudz.network.model.URL.add_journal_data_setting, new JSONObject().put("wifi", check_wifi).put("data_sync",check_sync), user.getSession_key(), Request.Method.POST, DataSettingScreen.this, APIActions.ApiActions.get_journal_settings_update);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == get_journal_settings) {
            try {
                JSONObject jsonObject = new JSONObject(response).getJSONObject("successData");
                if (!jsonObject.isNull("jouranl_setting")) {
                    if (!jsonObject.getJSONObject("jouranl_setting").isNull("data_sync")) {
                        if (jsonObject.getJSONObject("jouranl_setting").getInt("data_sync") == 1) {
                            SharedPrefrences.setBool("journal_data_setting_sync_notification", true, DataSettingScreen.this);
                            btn_on_notify.setText("ON");
                            btn_off_notify.setText("");
                            btn_on_notify.setBackgroundResource(R.drawable.toggle_btn_public);
                            btn_off_notify.setBackground(null);
                            isQucikEntry_notify = true;
                        } else {
                            SharedPrefrences.setBool("journal_data_setting_sync_notification", false, DataSettingScreen.this);
                            btn_on_notify.setText("");
                            btn_off_notify.setText("OFF");
                            btn_off_notify.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                            btn_on_notify.setBackground(null);
                            isQucikEntry_notify = false;
                        }
                    }

                    if (!jsonObject.getJSONObject("jouranl_setting").isNull("wifi")) {
                        if (jsonObject.getJSONObject("jouranl_setting").getInt("wifi") == 1) {
                            SharedPrefrences.setBool("journal_data_setting_wifi_only", true, DataSettingScreen.this);
                            btn_on.setText("");
                            btn_off.setText("OFF");
                            btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                            btn_on.setBackground(null);
                            isQucikEntry = false;
                        } else {
                            SharedPrefrences.setBool("journal_data_setting_wifi_only", false, DataSettingScreen.this);
                            btn_on.setText("ON");
                            btn_off.setText("");
                            btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
                            btn_off.setBackground(null);
                            isQucikEntry = true;
                        }
                    }
                }
                Log.d("Response", jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(DataSettingScreen.this, object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
