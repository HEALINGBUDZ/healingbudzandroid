package com.codingpixel.healingbudz.activity.settings.journal_setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_journal_settings;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class Journal_Setting_Activity extends AppCompatActivity implements APIResponseListner {
    ImageView Back, Home;
    LinearLayout Item_One;
    LinearLayout Item_Two;
    TextView quick_entry_text;
    Button btn_off, btn_on;
    LinearLayout Switch_Layout;
    boolean isQucikEntry = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_setting_);
        ChangeStatusBarColor(Journal_Setting_Activity.this, "#171717");
        HideKeyboard(Journal_Setting_Activity.this);

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
                GoToHome(Journal_Setting_Activity.this, true);
                finish();
            }
        });


        quick_entry_text = (TextView) findViewById(R.id.item_one_discription);
        String str = "Clicking on any date in <b><font color=#6fa740> Mode Calendar </b> </font> will open Quick Entry popup. You will be able to add entry Title. Entry Short Description, Choose Mood Icon and Journal (if you have multiple Journals)";
        quick_entry_text.setText(Html.fromHtml(str));
        Item_One = (LinearLayout) findViewById(R.id.item_one);
        Item_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                GoTo(Journal_Setting_Activity.this, ReminderSettings.class);
            }
        });

        Item_Two = (LinearLayout) findViewById(R.id.item_two);
        Item_Two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                GoTo(Journal_Setting_Activity.this, DataSettingScreen.class);
            }
        });

        btn_off = (Button) findViewById(R.id.private_btn);
        btn_on = (Button) findViewById(R.id.public_btn);
        Switch_Layout = (LinearLayout) findViewById(R.id.switch_layout);
        Switch_Layout.setClickable(true);
        if (SharedPrefrences.getBool("journal_setting_quick_entry", Journal_Setting_Activity.this)) {
            btn_on.setText("ON");
            btn_off.setText("");
            btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
            btn_off.setBackground(null);
            isQucikEntry = true;
        } else {
            btn_on.setText("");
            btn_off.setText("OFF");
            btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
            btn_on.setBackground(null);
            isQucikEntry = false;
        }
        Switch_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isQucikEntry) {
                    SharedPrefrences.setBool("journal_setting_quick_entry", false, Journal_Setting_Activity.this);
                    btn_on.setText("");
                    btn_off.setText("OFF");
                    btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                    btn_on.setBackground(null);
                    isQucikEntry = false;
                } else {
                    SharedPrefrences.setBool("journal_setting_quick_entry", true, Journal_Setting_Activity.this);
                    btn_on.setText("ON");
                    btn_off.setText("");
                    btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
                    btn_off.setBackground(null);
                    isQucikEntry = true;
                }
            }
        });
        getJournetSettingApi();

    }

    public void getJournetSettingApi() {
        new VollyAPICall(Journal_Setting_Activity.this, false, com.codingpixel.healingbudz.network.model.URL.get_journal_settings, new JSONObject(), user.getSession_key(), Request.Method.GET, Journal_Setting_Activity.this, get_journal_settings);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == get_journal_settings) {
            try {
                JSONObject jsonObject = new JSONObject(response).getJSONObject("successData");
                if (!jsonObject.isNull("jouranl_setting")) {
                    if (jsonObject.getJSONObject("jouranl_setting").getInt("entry_mode") == 1) {
                        SharedPrefrences.setBool("journal_setting_quick_entry", true, Journal_Setting_Activity.this);
                        btn_on.setText("ON");
                        btn_off.setText("");
                        btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
                        btn_off.setBackground(null);
                        isQucikEntry = true;
                    } else {
                        SharedPrefrences.setBool("journal_setting_quick_entry", false, Journal_Setting_Activity.this);
                        btn_on.setText("");
                        btn_off.setText("OFF");
                        btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                        btn_on.setBackground(null);
                        isQucikEntry = false;
                    }
                }
                Log.d("Response", jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateData() {
        try {
            int check = 0;
            if (isQucikEntry) {
                check = 1;
            }
            new VollyAPICall(Journal_Setting_Activity.this, false, com.codingpixel.healingbudz.network.model.URL.add_journal_setting, new JSONObject().put("entry_mode", check), user.getSession_key(), Request.Method.POST, Journal_Setting_Activity.this, APIActions.ApiActions.get_journal_settings_update);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
