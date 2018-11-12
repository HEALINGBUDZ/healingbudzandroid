package com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.folllowkeys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.StrainDataModel;
import com.codingpixel.healingbudz.DataModel.UserStrainDetailsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.KeyWordFollwingDialogRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView;
import com.codingpixel.healingbudz.customeUI.sliderclasses.Tricks.ViewPagerEx;
import com.codingpixel.healingbudz.custome_dialog.save_discussion.SaveDiscussionAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.interfaces.RewardInterface;
import com.codingpixel.healingbudz.interfaces.ShowMoreStrainDetails;
import com.codingpixel.healingbudz.interfaces.ViewAllDetailsAddedByUserButtonListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.setBool;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;

public class FollowKeywordActivity extends AppCompatActivity implements ShowMoreStrainDetails, View.OnClickListener, SaveDiscussionAlertDialog.OnDialogFragmentClickListener, ViewAllDetailsAddedByUserButtonListner, ReportSendButtonLstner, APIResponseListner
        , ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener
        , RewardInterface, KeyWordFollwingDialogRecylerAdapter.ItemClickListener {

    public static int points = 0;
    public static StrainDataModel strainDataModel;
    public static int comment_top_int_y = 4000;
    public static ScrollView reward_main_scroll;
    Button Tab_One, Tab_two, Tab_three;
    ImageView Back, Home, Back_button_eddits_by_user, slider_img;
    RelativeLayout Main_layout, top_view_1, top_view_2;
    LinearLayout Menu_Main_content, tab_text, tab_three_text;
    LinearLayout Menu_all_edits_by_user;
    TextView points_content, points_top;
    RecyclerView recycler_view;
    List<KeywordModel> keywordList = new ArrayList<>();
    KeyWordFollwingDialogRecylerAdapter recyler_adapter;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getNotify()) {
            if (!this.isDestroyed()) {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (SharedPrefrences.getBool("isFromSignup", FollowKeywordActivity.this)) {
            setBool("isFromSignup", false, FollowKeywordActivity.this);
            GoToHome(FollowKeywordActivity.this, true);
            finish();
        } else {
            super.onBackPressed();
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
        setContentView(R.layout.activity_key_word);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Back = (ImageView) findViewById(R.id.back_btn);


        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefrences.getBool("isFromSignup", FollowKeywordActivity.this)) {
                    setBool("isFromSignup", false, FollowKeywordActivity.this);
                    GoToHome(FollowKeywordActivity.this, true);
                    finish();
                } else {
                    finish();
                }

            }
        });
        Home = (ImageView) findViewById(R.id.home_btn);
        recycler_view = findViewById(R.id.recycler_view);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(FollowKeywordActivity.this, true);
                finish();
            }
        });


//        Back.setOnClickListener(this);
//        Home.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(linearLayoutManager);
        recyler_adapter = new KeyWordFollwingDialogRecylerAdapter(this, keywordList);
        recycler_view.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);

        SetData();

    }


    public void SetData() {
        new VollyAPICall(this,
                true
                , URL.get_keywords, new JSONObject(), Splash.user.getSession_key(), Request.Method.GET, new APIResponseListner() {
            @Override
            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                try {
                    if (new JSONObject(response).getString("status").equalsIgnoreCase("success")) {
                        JSONArray jsonObject = new JSONObject(response).getJSONArray("successData");
                        keywordList.clear();
                        for (int i = 0; i < jsonObject.length(); i++) {
                            JSONObject object = jsonObject.getJSONObject(i);
                            keywordList.add(new KeywordModel(object.getString("title"), object.optInt("id"), object.optInt("is_following_count")));
                        }
                        recyler_adapter = new KeyWordFollwingDialogRecylerAdapter(FollowKeywordActivity.this, keywordList);
                        recycler_view.setAdapter(recyler_adapter);
                        recyler_adapter.setClickListener(FollowKeywordActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onRequestError(String response, APIActions.ApiActions apiActions) {

            }
        }, APIActions.ApiActions.key_words);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                break;
        }
    }

    @Override
    public void onOkClicked(SaveDiscussionAlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onCancelClicked(SaveDiscussionAlertDialog dialog) {
        dialog.dismiss();
    }


    @Override
    public void ShowDetails() {

    }

    @Override
    public void viewAllEditsButtonClick(int position, UserStrainDetailsDataModel userStrainDetailsDataModel) {


    }

    @Override
    public void OnSnedClicked(JSONObject data, int position) {

    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {

    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
//        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setTopPointUser(String point) {

    }

    @Override
    public void GotoHbStore() {
        Tab_three.performClick();
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
