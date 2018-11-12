package com.codingpixel.healingbudz.activity.home.side_menu.myrewardz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.tabs.FreePointsTabFragment;
import com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.tabs.HBStoreTabFragment;
import com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.tabs.PoinsLogTabFragment;
import com.codingpixel.healingbudz.DataModel.StrainDataModel;
import com.codingpixel.healingbudz.DataModel.UserStrainDetailsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView;
import com.codingpixel.healingbudz.customeUI.sliderclasses.Tricks.ViewPagerEx;
import com.codingpixel.healingbudz.custome_dialog.save_discussion.SaveDiscussionAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.interfaces.RewardInterface;
import com.codingpixel.healingbudz.interfaces.ShowMoreStrainDetails;
import com.codingpixel.healingbudz.interfaces.ViewAllDetailsAddedByUserButtonListner;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.setBool;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;

public class MyRewardzActivity extends AppCompatActivity implements ShowMoreStrainDetails, View.OnClickListener, SaveDiscussionAlertDialog.OnDialogFragmentClickListener, ViewAllDetailsAddedByUserButtonListner, ReportSendButtonLstner, APIResponseListner
        , ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener
        , RewardInterface {

    Button Tab_One, Tab_two, Tab_three;
    ImageView Back, Home, Back_button_eddits_by_user, slider_img;
    RelativeLayout Main_layout, top_view_1, top_view_2;

    LinearLayout Menu_Main_content, tab_text, tab_three_text;

    LinearLayout Menu_all_edits_by_user;
    TextView points_content, points_top;
    public static int points = 0;

    public static StrainDataModel strainDataModel;
    public static int comment_top_int_y = 4000;
    public static ScrollView reward_main_scroll;

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

        if (SharedPrefrences.getBool("isFromSignup", MyRewardzActivity.this)) {
            setBool("isFromSignup", false, MyRewardzActivity.this);
            GoToHome(MyRewardzActivity.this, true);
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
        setContentView(R.layout.activity_my_reward);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Back = (ImageView) findViewById(R.id.back_btn);
        slider_img = (ImageView) findViewById(R.id.slider_img);
        slider_img.setImageDrawable(getDrawable(R.drawable.tab_three_bg));
        Home = (ImageView) findViewById(R.id.home_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefrences.getBool("isFromSignup", MyRewardzActivity.this)) {
                    setBool("isFromSignup", false, MyRewardzActivity.this);
                    GoToHome(MyRewardzActivity.this, true);
                    finish();
                } else {
                    finish();
                }

            }
        });
        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(MyRewardzActivity.this, true);
                finish();
            }
        });
        top_view_1 = (RelativeLayout) findViewById(R.id.top_view_1);
        top_view_2 = (RelativeLayout) findViewById(R.id.top_view_2);
        points_content = (TextView) findViewById(R.id.points_content);
        points_top = (TextView) findViewById(R.id.points_top);

//        Back.setOnClickListener(this);
//        Home.setOnClickListener(this);

        Tab_One = (Button) findViewById(R.id.tab_one);
        Tab_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu_all_edits_by_user.setVisibility(View.GONE);
                Menu_Main_content.setVisibility(View.VISIBLE);
                top_view_2.setVisibility(View.GONE);
                top_view_1.setVisibility(View.VISIBLE);
                tab_text.setVisibility(View.GONE);
                tab_three_text.setVisibility(View.GONE);
                slider_img.setImageDrawable(getDrawable(R.drawable.tab_three_bg));
                if (!getTabOneFragment().isVisible()) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.fade_in);
                    transaction.replace(R.id.fragment_layout, getTabOneFragment());
                    transaction.commitAllowingStateLoss();
                }
                Tab_One.setBackgroundColor(Color.parseColor("#82BB2B"));
                Tab_two.setBackgroundColor(Color.parseColor("#5C5D5D"));
                Tab_three.setBackgroundColor(Color.parseColor("#5C5D5D"));

                Tab_One.setTextColor(Color.parseColor("#FFFFFF"));
                Tab_three.setTextColor(Color.parseColor("#C4C3C3"));
                Tab_two.setTextColor(Color.parseColor("#C4C3C3"));
            }
        });
        TextView text_redeem = findViewById(R.id.text_redeem);
        text_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tab_three.performClick();
            }
        });
        Tab_two = (Button) findViewById(R.id.tab_two);
        Tab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu_all_edits_by_user.setVisibility(View.GONE);
                top_view_2.setVisibility(View.GONE);
                top_view_1.setVisibility(View.VISIBLE);
                Menu_Main_content.setVisibility(View.VISIBLE);
                tab_text.setVisibility(View.GONE);
                top_view_2.setVisibility(View.GONE);
                slider_img.setImageDrawable(getDrawable(R.drawable.tab_three_bg));
                tab_three_text.setVisibility(View.GONE);
                if (!getTabOneFragment().isVisible()) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.fade_in);
                    transaction.replace(R.id.fragment_layout, getTabTwoFragment());
                    transaction.commitAllowingStateLoss();
                }

                Tab_One.setBackgroundColor(Color.parseColor("#5C5D5D"));
                Tab_two.setBackgroundColor(Color.parseColor("#82BB2B"));
                Tab_three.setBackgroundColor(Color.parseColor("#5C5D5D"));

                Tab_two.setTextColor(Color.parseColor("#FFFFFF"));
                Tab_three.setTextColor(Color.parseColor("#C4C3C3"));
                Tab_One.setTextColor(Color.parseColor("#C4C3C3"));
            }
        });
        Tab_three = (Button) findViewById(R.id.tab_three);
        Tab_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu_all_edits_by_user.setVisibility(View.GONE);
                Menu_Main_content.setVisibility(View.VISIBLE);
                top_view_2.setVisibility(View.VISIBLE);
                top_view_1.setVisibility(View.GONE);
                slider_img.setImageDrawable(getDrawable(R.drawable.reward_bg));
                Tab_One.setBackgroundColor(Color.parseColor("#5C5D5D"));
                Tab_two.setBackgroundColor(Color.parseColor("#5C5D5D"));
                Tab_three.setBackgroundColor(Color.parseColor("#82BB2B"));

                Tab_three.setTextColor(Color.parseColor("#FFFFFF"));
                Tab_two.setTextColor(Color.parseColor("#C4C3C3"));
                Tab_One.setTextColor(Color.parseColor("#C4C3C3"));
                tab_text.setVisibility(View.GONE);
                tab_three_text.setVisibility(View.VISIBLE);


                if (!getTabOneFragment().isVisible()) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragment_layout, getTabThreeFragment());
                    transaction.commitAllowingStateLoss();
                }
            }
        });
        reward_main_scroll = (ScrollView) findViewById(R.id.main_scroll);
        reward_main_scroll.fullScroll(View.FOCUS_UP);


        Menu_Main_content = (LinearLayout) findViewById(R.id.menu_top_content);
        tab_text = (LinearLayout) findViewById(R.id.tab_text);
        tab_three_text = (LinearLayout) findViewById(R.id.tab_three_text);
        Menu_all_edits_by_user = (LinearLayout) findViewById(R.id.all_edit_added_top_menu);

        Back_button_eddits_by_user = (ImageView) findViewById(R.id.back_btn_eddit);
        Back_button_eddits_by_user.setOnClickListener(this);

        Main_layout = (RelativeLayout) findViewById(R.id.main_cntnt_strain);
        SetData();

    }


    public void SetData() {
        if (!getTabOneFragment().isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.fade_in);
            transaction.add(R.id.fragment_layout, getTabOneFragment());
            transaction.commitAllowingStateLoss();
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                if (SharedPrefrences.getBool("isFromSignup", this)) {
                    setBool("isFromSignup", false, this);
                    GoToHome(this, true);
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.home_btn:

                break;
            case R.id.share_btn:

                break;
            case R.id.favorite_it:

                break;
            case R.id.back_btn_eddit:

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

    public Fragment getTabOneFragment() {
        return new FreePointsTabFragment();
    }

    public Fragment getTabTwoFragment() {
        return new PoinsLogTabFragment();

    }

    public Fragment getTabThreeFragment() {
        return new HBStoreTabFragment();
    }

    @Override
    public void ShowDetails() {
        Tab_One.setBackgroundColor(Color.parseColor("#5c5c5c"));
        Tab_two.setBackgroundColor(Color.parseColor("#f4c42f"));
        Tab_three.setBackgroundColor(Color.parseColor("#5c5c5c"));

        Tab_two.setTextColor(Color.parseColor("#FFFFFF"));
        Tab_three.setTextColor(Color.parseColor("#c4c4c4"));
        Tab_One.setTextColor(Color.parseColor("#c4c4c4"));

        if (!getTabOneFragment().isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_layout, getTabTwoFragment());
            transaction.commitAllowingStateLoss();
        }
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
        points_content.setText(point);
        points_top.setText(point);
        points = Integer.parseInt(point);
    }

    @Override
    public void GotoHbStore() {
        Tab_three.performClick();
    }
}
