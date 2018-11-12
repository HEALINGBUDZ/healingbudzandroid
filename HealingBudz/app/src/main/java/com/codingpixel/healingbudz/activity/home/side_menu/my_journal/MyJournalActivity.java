package com.codingpixel.healingbudz.activity.home.side_menu.my_journal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.home.HomeActivity.Profile_Img;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.network.model.URL.get_my_journals_calander;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class MyJournalActivity extends AppCompatActivity implements APIResponseListner {
    RelativeLayout Search_Menu;
    ImageView Search_Button;
    ImageView Back, Home;
    ImageView User_Img;
    ModeCalendarFragment modeCalendarFragment = new ModeCalendarFragment();
    JournalFragment journalFragment = new JournalFragment();
    TagsFragment tagsFragment = new TagsFragment();
    boolean isSearchViewOpen = false;
    TextView Total_journals, Total_tags, Entries_today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_journal);
        ChangeStatusBarColor(MyJournalActivity.this, "#171717");
        HideKeyboard(MyJournalActivity.this);
        InitTabs();

        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        User_Img = (ImageView) findViewById(R.id.my_journal_usr_img);
        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(MyJournalActivity.this, true);
                finish();
            }
        });
        Search_Button = (ImageView) findViewById(R.id.search_button);
        Search_Menu = (RelativeLayout) findViewById(R.id.search_menu);
        Search_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSearchViewOpen) {
                    isSearchViewOpen = true;
                    Search_Menu.setVisibility(View.VISIBLE);
                    Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                    Search_Menu.startAnimation(startAnimation);
                } else {
                    isSearchViewOpen = false;
                    Search_Menu.setVisibility(View.GONE);
                    Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                    Search_Menu.startAnimation(startAnimation);
                }
            }
        });
        User_Img.setImageDrawable(Profile_Img.getDrawable());
        Total_journals = (TextView) findViewById(R.id.total_journals);
        Total_tags = (TextView) findViewById(R.id.total_tags);
        Entries_today = (TextView) findViewById(R.id.entries_today);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.content_fragment, modeCalendarFragment, "1");
        transaction.commitAllowingStateLoss();


        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(MyJournalActivity.this, true, get_my_journals_calander, jsonObject, user.getSession_key(), Request.Method.GET, MyJournalActivity.this, APIActions.ApiActions.get_my_journals_calander);
    }

    public void TabOneClick() {
        if (!modeCalendarFragment.isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content_fragment, modeCalendarFragment, "1");
            transaction.commitAllowingStateLoss();
        }

    }

    public void TabTwoClick() {
        if (!journalFragment.isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content_fragment, journalFragment, "1");
            transaction.commitAllowingStateLoss();
        }
    }

    public void TabThreeClick() {
        if (!tagsFragment.isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content_fragment, tagsFragment, "1");
            transaction.commitAllowingStateLoss();
        }

    }

    public void InitTabs() {
        final RelativeLayout Tab_One, Tab_Two, Tab_Three;
        final ImageView Tab_One_icon, Tab_Two_icon, Tab_Three_icon;
        final TextView Tab_One_text, Tab_Two_text, Tab_Three_text;

        Tab_One = (RelativeLayout) findViewById(R.id.tab_one_bg);
        Tab_Two = (RelativeLayout) findViewById(R.id.tab_two_bg);
        Tab_Three = (RelativeLayout) findViewById(R.id.tab_three_bg);

        Tab_One_icon = (ImageView) findViewById(R.id.tab_one_icon);
        Tab_Two_icon = (ImageView) findViewById(R.id.tab_two_icon);
        Tab_Three_icon = (ImageView) findViewById(R.id.tab_three_icon);

        Tab_One_text = (TextView) findViewById(R.id.tab_one_text);
        Tab_Two_text = (TextView) findViewById(R.id.tab_two_text);
        Tab_Three_text = (TextView) findViewById(R.id.tab_three_text);

        Tab_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tab_One.setBackgroundColor(Color.parseColor("#6fa943"));
                Tab_Two.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Three.setBackgroundColor(Color.parseColor("#00000000"));

                Tab_One_text.setTextColor(Color.parseColor("#FFFFFF"));
                Tab_Two_text.setTextColor(Color.parseColor("#bbbaba"));
                Tab_Three_text.setTextColor(Color.parseColor("#bbbaba"));


                Tab_One_icon.setImageResource(R.drawable.ic_calaneder_white);
                Tab_Two_icon.setImageResource(R.drawable.ic_journal_gray_icon);
                Tab_Three_icon.setImageResource(R.drawable.ic_pin_gray_icon_myjournal);

                TabOneClick();
            }
        });

        Tab_Two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tab_One.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Two.setBackgroundColor(Color.parseColor("#6fa943"));
                Tab_Three.setBackgroundColor(Color.parseColor("#00000000"));

                Tab_One_text.setTextColor(Color.parseColor("#bbbaba"));
                Tab_Two_text.setTextColor(Color.parseColor("#FFFFFF"));
                Tab_Three_text.setTextColor(Color.parseColor("#bbbaba"));

                Tab_One_icon.setImageResource(R.drawable.ic_calaneder_gray);
                Tab_Two_icon.setImageResource(R.drawable.ic_journal_white_icon);
                Tab_Three_icon.setImageResource(R.drawable.ic_pin_gray_icon_myjournal);
                TabTwoClick();


            }
        });

        Tab_Three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tab_One.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Two.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Three.setBackgroundColor(Color.parseColor("#6fa943"));

                Tab_One_text.setTextColor(Color.parseColor("#bbbaba"));
                Tab_Two_text.setTextColor(Color.parseColor("#bbbaba"));
                Tab_Three_text.setTextColor(Color.parseColor("#FFFFFF"));

                Tab_One_icon.setImageResource(R.drawable.ic_calaneder_gray);
                Tab_Two_icon.setImageResource(R.drawable.ic_journal_gray_icon);
                Tab_Three_icon.setImageResource(R.drawable.ic_pin_white_icon_myjournal);
                TabThreeClick();
            }
        });

    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("Resposne", response);
        try {
            JSONObject jsonObject = new JSONObject(response).getJSONObject("successData");
            Total_tags.setText(jsonObject.getInt("total_tags_count") + "");
            Total_journals.setText(jsonObject.getInt("user_journal_count") + "");
            if (jsonObject.getJSONArray("today_entry").length() > 0) {
                Entries_today.setText(jsonObject.getJSONArray("today_entry").getJSONObject(0).getInt("count") + "");
                modeCalendarFragment = new ModeCalendarFragment(jsonObject.getJSONArray("today_entry").getJSONObject(0).getInt("count"));
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.content_fragment, modeCalendarFragment, "1");
                transaction.commitAllowingStateLoss();
            } else {
                Entries_today.setText("0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("Resposne", response);
    }
}
