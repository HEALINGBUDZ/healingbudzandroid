package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzFeedAlertDialog;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzMapStripeDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_subscription;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.update_subscription;
import static com.codingpixel.healingbudz.network.model.URL.update_pop_up;
import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;

public class BudzMapPaidViewActivity extends AppCompatActivity implements BudzMapStripeDialog.OnDialogFragmentClickListener, APIResponseListner, BudzFeedAlertDialog.OnDialogFragmentClickListener {

    TextView text_heading, text_detail, text_learn_more, text_middle_doller, text_left_doller, text_right_doller;
    RelativeLayout tab_three_main, tab_tow_main, tab_one_main, middle_bg;
    Button sbcsribe_now, no_thanks;
    ImageView cross_btn;
    LinearLayout no_display, check_box_tap;
    CheckBox checked_no_show;
    boolean isUpdate = false;
    boolean isMakeNewUpdate = false;
    int budz_id = -1;
    int plan_type = 2;

    /*
    * plan_type = 1 (monthly plan)
      plan_type = 2 (3 months plan)
      plan_type = 3 (yearlyly plan)
*/
    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onRestart() {

        super.onRestart();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_budz_map_paid);
        FullScreen(this);
        initView();
        iniListener();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("isUpdate") && bundle.getBoolean("isUpdate")) {
                isUpdate = true;
                budz_id = bundle.getInt("budz_id");
                check_box_tap.setVisibility(View.GONE);
            }
            if (bundle.containsKey("isFromMain") && bundle.getBoolean("isFromMain")) {
                check_box_tap.setVisibility(View.VISIBLE);
            }
            if (bundle.containsKey("isMakeNewUpdate") && bundle.getBoolean("isMakeNewUpdate")) {
                isMakeNewUpdate = true;
                isUpdate = false;
                check_box_tap.setVisibility(View.GONE);
            }
        }
//        Html.fromHtml("5x<sup>2</sup>")

    }

    private void iniListener() {
        cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                no_thanks.performClick();
            }
        });
        //paid_middle_text_unselect //932a88
        //paid_middle_text // 45c5e8
        text_learn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (plan_type == 1) {
                    bundle.putString("main_val", "$29");
                    bundle.putString("small_val", "95");
                    bundle.putString("under_small_val", "per mo");
                } else if (plan_type == 2) {
                    bundle.putString("main_val", "$19");
                    bundle.putString("small_val", "95");
                    bundle.putString("under_small_val", "per mo");

                } else {
                    bundle.putString("main_val", "$15");
                    bundle.putString("small_val", "95");
                    bundle.putString("under_small_val", "per mo");

                }
                BudzFeedAlertDialog budzFeedAlertDialog = BudzFeedAlertDialog.newInstance(BudzMapPaidViewActivity.this, true, bundle);
                budzFeedAlertDialog.show(BudzMapPaidViewActivity.this.getSupportFragmentManager(), "dialog");
            }
        });
        tab_three_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan_type = 3;
                text_heading.setText("Annually Paid Subscription");
                middle_bg.setBackgroundResource(R.drawable.paid_middle_text_unselect);
                tab_three_main.setBackgroundColor(Color.parseColor("#932a88"));
                tab_one_main.setBackgroundColor(Color.parseColor("#45c5e8"));
            }
        });
        tab_one_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan_type = 1;
                middle_bg.setBackgroundResource(R.drawable.paid_middle_text_unselect);
                text_heading.setText("Monthly Paid Subscription");
                tab_three_main.setBackgroundColor(Color.parseColor("#45c5e8"));
                tab_one_main.setBackgroundColor(Color.parseColor("#932a88"));
            }
        });
        tab_tow_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan_type = 2;
                text_heading.setText("3 Month Paid Subscription");
                middle_bg.setBackgroundResource(R.drawable.paid_middle_text);
                tab_three_main.setBackgroundColor(Color.parseColor("#45c5e8"));
                tab_one_main.setBackgroundColor(Color.parseColor("#45c5e8"));
            }
        });
        no_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_box_tap.getVisibility() == View.VISIBLE) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        if (checked_no_show.isChecked()) {
                            jsonObject.put("show_budz_popup", 0);
                            user.setShow_budz_popup(false);
                        } else {
                            jsonObject.put("show_budz_popup", 1);
                            user.setShow_budz_popup(true);
                        }


                        new VollyAPICall(BudzMapPaidViewActivity.this, false, update_pop_up, jsonObject, user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                            @Override
                            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {

                            }

                            @Override
                            public void onRequestError(String response, APIActions.ApiActions apiActions) {

                            }
                        }, APIActions.ApiActions.update_pop_up);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    finish();
                }
//                finish();
            }
        });
        sbcsribe_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pay = "";
                if (plan_type == 1) {
                    pay = "$29.95";
//                    bundle.putString("main_val", "$29");
//                    bundle.putString("small_val", "95");
//                    bundle.putString("under_small_val", "per mo");
                } else if (plan_type == 2) {
//                    bundle.putString("main_val", "$19");
//                    bundle.putString("small_val", "95");
//                    bundle.putString("under_small_val", "per mo");
                    pay = "$19.95";
                } else {
//                    bundle.putString("main_val", "$15");
//                    bundle.putString("small_val", "95");
//                    bundle.putString("under_small_val", "per mo");
                    pay = "$15.95";
                }
                BudzMapStripeDialog budzFeedAlertDialog = BudzMapStripeDialog.newInstance(BudzMapPaidViewActivity.this, false, pay);
                budzFeedAlertDialog.show(BudzMapPaidViewActivity.this.getSupportFragmentManager(), "dialog");
            }
        });
        check_box_tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked_no_show.isChecked()) {
                    checked_no_show.setChecked(false);
                } else {
                    checked_no_show.setChecked(true);
                }
            }
        });
    }

    private void initView() {
        text_heading = findViewById(R.id.text_heading);
        cross_btn = findViewById(R.id.cross_btn);
        middle_bg = findViewById(R.id.middle_bg);
        text_detail = findViewById(R.id.text_detail);
        text_learn_more = findViewById(R.id.text_learn_more);
        text_middle_doller = findViewById(R.id.text_middle_doller);
//        text_middle_doller.setText(Html.fromHtml("<sup>$</sup>19"));
        text_left_doller = findViewById(R.id.text_left_doller);
//        text_left_doller.setText(Html.fromHtml("<sup>$</sup>29"));
        text_right_doller = findViewById(R.id.text_right_doller);
//        text_right_doller.setText(Html.fromHtml("<sup>$</sup>15"));
        tab_three_main = findViewById(R.id.tab_three_main);
        tab_tow_main = findViewById(R.id.tab_tow_main);
        tab_one_main = findViewById(R.id.tab_one_main);
        no_thanks = findViewById(R.id.no_thanks);
        sbcsribe_now = findViewById(R.id.sbcsribe_now);
        no_display = findViewById(R.id.no_display);
        check_box_tap = findViewById(R.id.check_box_tap);
        checked_no_show = findViewById(R.id.checked_no_show);
    }


    @Override
    public void TokenGenrate(Token token) {
        Log.d("token", token.toString());
        JSONObject object = new JSONObject();
        try {
            object.put("stripe_token", token.getId());
            object.put("plan_type", plan_type);
            if (isUpdate) {
                object.put("budz_id", budz_id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (isUpdate) {
            new VollyAPICall(BudzMapPaidViewActivity.this, true, URL.update_subscription, object, user.getSession_key(), Request.Method.POST, BudzMapPaidViewActivity.this, update_subscription);
        } else {

            new VollyAPICall(BudzMapPaidViewActivity.this, true, URL.add_subscription, object, user.getSession_key(), Request.Method.POST, BudzMapPaidViewActivity.this, add_subscription);
        }


    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("log", response);
        if (apiActions == add_subscription) {

            try {
                JSONObject jsonObject = new JSONObject(response).getJSONObject("successData");
                if (isMakeNewUpdate) {
                    AddNewBudzMapActivity.add_new_bud_jason_Data.put("sub_user_id", jsonObject.getString("id"));
                    AddNewBudzMapActivity.isNewMaker = true;
                    finish();
                } else {
                    Intent intent = new Intent(BudzMapPaidViewActivity.this, AddNewBudzMapActivity.class);
                    intent.putExtra("isSubcribed", true);
                    intent.putExtra("sub_user_id", jsonObject.getString("id"));
                    AddNewBudzMapActivity.add_new_bud_jason_Data.put("sub_user_id", jsonObject.getString("id"));
                    AddNewBudzMapActivity.isNewMaker = true;
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (apiActions == update_subscription) {
            setResult(Constants.UPGRADED);
            finish();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("log", response);
    }

    @Override
    public void onCountinueFreeListingBtnClink(BudzFeedAlertDialog dialog) {

    }

    @Override
    public void onSubcribeNowBtnClick(BudzFeedAlertDialog dialog) {

    }
}
