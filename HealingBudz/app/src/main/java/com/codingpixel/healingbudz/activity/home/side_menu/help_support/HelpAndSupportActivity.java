package com.codingpixel.healingbudz.activity.home.side_menu.help_support;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.SubUserHelpSupport;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.HelpAndSupportRecylerAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;
import static com.codingpixel.healingbudz.test_data.JournalDetailsTestdata.get_help_support_recyler_Data;

public class HelpAndSupportActivity extends AppCompatActivity implements APIResponseListner {
    RecyclerView My_questions_recyler_view;
    ImageView Home, Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_support);
        ChangeStatusBarColor(HelpAndSupportActivity.this, "#171717");
        HideKeyboard(HelpAndSupportActivity.this);
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
                GoToHome(HelpAndSupportActivity.this, true);
                finish();
            }
        });

        My_questions_recyler_view = (RecyclerView) findViewById(R.id.my_questions_recyler_view);


        //Update user information
        FreshchatUser user = Freshchat.getInstance(getApplicationContext()).getUser();
        user.setFirstName(Splash.user.getFirst_name()).setLastName(Splash.user.getLast_name()).setEmail(Splash.user.getEmail()).setPhone("001", "0");
        Freshchat.getInstance(getApplicationContext()).setUser(user);
        new VollyAPICall(HelpAndSupportActivity.this, true, URL.get_sub_users, new JSONObject(), Splash.user.getSession_key(), Request.Method.GET, HelpAndSupportActivity.this, APIActions.ApiActions.get_sub_user);

    }

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
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HideKeyboard(HelpAndSupportActivity.this);
            }
        }, 100);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("onRequestSuccess: ", response);
        if (apiActions == APIActions.ApiActions.get_sub_user) {
            List<SubUserHelpSupport> listSubUser = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(response);
                JSONArray array = object.getJSONObject("successData").getJSONArray("sub_users");
                for (int i = 0; i < array.length(); i++) {
                    listSubUser.add(new SubUserHelpSupport(array.getJSONObject(i).getInt("id"), array.getJSONObject(i).getString("title")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            RecyclerView.ItemAnimator animator = My_questions_recyler_view.getItemAnimator();
            if (animator instanceof DefaultItemAnimator) {
                ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
            }
            HelpAndSupportRecylerAdapter adapter;
            adapter = new HelpAndSupportRecylerAdapter(get_help_support_recyler_Data(), listSubUser);
            adapter.setOnGroupClickListener(new OnGroupClickListener() {
                @Override
                public boolean onGroupClick(int flatPos) {
                    return true;
                }
            });
            My_questions_recyler_view.setLayoutManager(layoutManager);
            My_questions_recyler_view.setAdapter(adapter);

            for (int x = 0; x < adapter.getGroups().size(); x++) {
                if (!adapter.isGroupExpanded(adapter.getGroups().get(x))) {
                    adapter.toggleGroup(adapter.getGroups().get(x));
                }
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator animator = My_questions_recyler_view.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        HelpAndSupportRecylerAdapter adapter;
        adapter = new HelpAndSupportRecylerAdapter(get_help_support_recyler_Data());
        adapter.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(int flatPos) {
                return true;
            }
        });
        My_questions_recyler_view.setLayoutManager(layoutManager);
        My_questions_recyler_view.setAdapter(adapter);

        for (int x = 0; x < adapter.getGroups().size(); x++) {
            if (!adapter.isGroupExpanded(adapter.getGroups().get(x))) {
                adapter.toggleGroup(adapter.getGroups().get(x));
            }
        }
    }
}
