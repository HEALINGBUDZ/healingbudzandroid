package com.codingpixel.healingbudz.activity.home.side_menu.mysaves;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.DataModel.MessagesDataModel;
import com.codingpixel.healingbudz.DataModel.MySavedDataModal;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.JournalDetailsActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagesActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingBusinessChatViewActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingChatViewActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.MySavesListRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.APPSwitch;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.HomeActivity.showSubFragmentListner_object;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.filter_my_save;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

public class MySavesActivity extends AppCompatActivity implements APIResponseListner, MySavesListRecylerAdapter.ItemClickListener {
    ImageView Home, Back;
    MySavesListRecylerAdapter recyler_adapter;
    private SlideUp slide = null;
    private SwipeRefreshLayout refreshLayout;
    RecyclerView MySaves_recyler_view;
    TextView not_fount;
    //    String Filter_ids = "3,4,7,8,6,10";
//    String Filter_ids = "4,7,8,10,11";
    String Filter_ids = "4,8,10,11,2,13";
    ArrayList<MySavedDataModal> data_list = new ArrayList<>();
    APPSwitch Q_A_Switch, chat_switch_save, chat_budz_switch_save, Group_Switch, Journal_Switch, Budz_MAp_Switch, Strain_Switch, Strain_Switch_Search, special_switch_save;

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
        setContentView(R.layout.activity_my_saves);
        ChangeStatusBarColor(MySavesActivity.this, "#171717");
        Back = (ImageView) findViewById(R.id.back_btn);
        not_fount = (TextView) findViewById(R.id.not_fount);
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
                GoToHome(MySavesActivity.this, true);
                finish();
            }
        });
        MySaves_recyler_view = (RecyclerView) findViewById(R.id.my_saves_recyler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MySavesActivity.this);
        MySaves_recyler_view.setLayoutManager(linearLayoutManager);
        recyler_adapter = new MySavesListRecylerAdapter(MySavesActivity.this, data_list);
        MySaves_recyler_view.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
        InitHeaderSlideView();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.parseColor("#0083cb"));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(MySavesActivity.this, false, URL.filter_my_save + "?filter=" + Filter_ids, jsonObject, user.getSession_key(), Request.Method.GET, MySavesActivity.this, filter_my_save);
            }
        });

        Q_A_Switch = (APPSwitch) findViewById(R.id.q_a_switch);
        chat_switch_save = (APPSwitch) findViewById(R.id.chat_switch_save);
        chat_budz_switch_save = (APPSwitch) findViewById(R.id.chat_budz_switch_save);
        Group_Switch = (APPSwitch) findViewById(R.id.group_switch);
        Journal_Switch = (APPSwitch) findViewById(R.id.journal_switch);
        Budz_MAp_Switch = (APPSwitch) findViewById(R.id.budz_switch);
        Strain_Switch = (APPSwitch) findViewById(R.id.strain_switch);
        Strain_Switch_Search = (APPSwitch) findViewById(R.id.strain_switch_search);
        special_switch_save = (APPSwitch) findViewById(R.id.special_switch_save);

        if (SharedPrefrences.getBool("MysSaveisEdits", MySavesActivity.this)) {
            Filter_ids = "";

            if (SharedPrefrences.getBool("mysave_qa_switch", MySavesActivity.this)) {
                if (Filter_ids.length() == 0) {
                    Filter_ids = Filter_ids + "4";
                } else {
                    Filter_ids = Filter_ids + ",4";
                }
                Q_A_Switch.setChecked(true);
            } else {
                Q_A_Switch.setChecked(false);
            }

//            if (SharedPrefrences.getBool("mysave_journal_switch", MySavesActivity.this)) {
//                if (Filter_ids.length() == 0) {
//                    Filter_ids = Filter_ids + "3";
//                } else {
//                    Filter_ids = Filter_ids + ",3";
//                }
//                Journal_Switch.setChecked(true);
//            } else {
//                Journal_Switch.setChecked(false);
//            }


//            if (SharedPrefrences.getBool("mysave_strain_switch", MySavesActivity.this)) {
//                if (Filter_ids.length() == 0) {
//                    Filter_ids = Filter_ids + "7";
//                } else {
//                    Filter_ids = Filter_ids + ",7";
//                }
//                Strain_Switch.setChecked(true);
//            } else {
//                Strain_Switch.setChecked(false);
//            }


            if (SharedPrefrences.getBool("mysave_budz_switch", MySavesActivity.this)) {
                if (Filter_ids.length() == 0) {
                    Filter_ids = Filter_ids + "8";
                } else {
                    Filter_ids = Filter_ids + ",8";
                }
                Budz_MAp_Switch.setChecked(true);
            } else {
                Budz_MAp_Switch.setChecked(false);
            }


//            if (SharedPrefrences.getBool("mysave_group_switch", MySavesActivity.this)) {
//                if (Filter_ids.length() == 0) {
//                    Filter_ids = Filter_ids + "6";
//                } else {
//                    Filter_ids = Filter_ids + ",6";
//                }
//                Group_Switch.setChecked(true);
//            } else {
//                Group_Switch.setChecked(false);
//            }
            if (SharedPrefrences.getBool("mysave_strain_switch_search", MySavesActivity.this)) {
                if (Filter_ids.length() == 0) {
                    Filter_ids = Filter_ids + "10";
                } else {
                    Filter_ids = Filter_ids + ",10";
                }
                Strain_Switch_Search.setChecked(true);
            } else {
                Strain_Switch_Search.setChecked(false);
            }
            if (SharedPrefrences.getBool("mysave_special_switch_save", MySavesActivity.this)) {
                if (Filter_ids.length() == 0) {
                    Filter_ids = Filter_ids + "11";
                } else {
                    Filter_ids = Filter_ids + ",11";
                }
                special_switch_save.setChecked(true);
            } else {
                special_switch_save.setChecked(false);
            }
            if (SharedPrefrences.getBool("mysave_chat_switch_save", MySavesActivity.this)) {
                if (Filter_ids.length() == 0) {
                    Filter_ids = Filter_ids + "2";
                } else {
                    Filter_ids = Filter_ids + ",2";
                }
                chat_switch_save.setChecked(true);
            } else {
                chat_switch_save.setChecked(false);
            }
            if (SharedPrefrences.getBool("mysave_chat_budz_switch_save", MySavesActivity.this)) {
                if (Filter_ids.length() == 0) {
                    Filter_ids = Filter_ids + "13";
                } else {
                    Filter_ids = Filter_ids + ",13";
                }
                chat_budz_switch_save.setChecked(true);
            } else {
                chat_budz_switch_save.setChecked(false);
            }

        }
        refreshLayout.setRefreshing(true);
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(MySavesActivity.this, false, URL.filter_my_save + "?filter=" + Filter_ids, jsonObject, user.getSession_key(), Request.Method.GET, MySavesActivity.this, filter_my_save);

    }

    public void InitHeaderSlideView() {
        RelativeLayout slide_header_list;
        final ImageView list_indicator;
        final RecyclerView journal_header_list_recyler_view;
        ImageView list_indicator_close;
        final boolean[] is_list_open = {false};
        final RelativeLayout Indicator_layout, indicator_close_layout;
        slide_header_list = (RelativeLayout) findViewById(R.id.slide_header_list);
        indicator_close_layout = (RelativeLayout) findViewById(R.id.indicator_close_layout);
        list_indicator = (ImageView) findViewById(R.id.indicator);
        Indicator_layout = (RelativeLayout) findViewById(R.id.indicator_layout);
        list_indicator_close = (ImageView) findViewById(R.id.indicator_close);
        list_indicator.setVisibility(View.VISIBLE);
        Indicator_layout.setVisibility(View.VISIBLE);
        list_indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_indicator.setVisibility(View.GONE);
                Indicator_layout.setVisibility(View.GONE);
                is_list_open[0] = true;
                slide.show();
            }
        });
        indicator_close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPrefrences.setBool("MysSaveisEdits", true, MySavesActivity.this);

                is_list_open[0] = false;
                slide.hide();
                String filter_ids = "";
                if (Q_A_Switch.isChecked()) {
                    if (filter_ids.length() == 0) {
                        filter_ids = filter_ids + "4";
                    } else {
                        filter_ids = filter_ids + ",4";
                    }

                    SharedPrefrences.setBool("mysave_qa_switch", true, MySavesActivity.this);
                } else {
                    SharedPrefrences.setBool("mysave_qa_switch", false, MySavesActivity.this);
                }
                if (chat_switch_save.isChecked()) {
                    if (filter_ids.length() == 0) {
                        filter_ids = filter_ids + "2";
                    } else {
                        filter_ids = filter_ids + ",2";
                    }

                    SharedPrefrences.setBool("mysave_chat_switch_save", true, MySavesActivity.this);
                } else {
                    SharedPrefrences.setBool("mysave_chat_switch_save", false, MySavesActivity.this);
                }
                if (chat_budz_switch_save.isChecked()) {
                    if (filter_ids.length() == 0) {
                        filter_ids = filter_ids + "13";
                    } else {
                        filter_ids = filter_ids + ",13";
                    }

                    SharedPrefrences.setBool("mysave_chat_budz_switch_save", true, MySavesActivity.this);
                } else {
                    SharedPrefrences.setBool("mysave_chat_budz_switch_save", false, MySavesActivity.this);
                }

//                if (Journal_Switch.isChecked()) {
//                    if (filter_ids.length() == 0) {
//                        filter_ids = filter_ids + "3";
//                    } else {
//                        filter_ids = filter_ids + ",3";
//                    }
//                    SharedPrefrences.setBool("mysave_journal_switch", true, MySavesActivity.this);
//                } else {
//                    SharedPrefrences.setBool("mysave_journal_switch", false, MySavesActivity.this);
//                }

//                if (Strain_Switch.isChecked()) {
//                    if (filter_ids.length() == 0) {
//                        filter_ids = filter_ids + "7";
//                    } else {
//                        filter_ids = filter_ids + ",7";
//                    }
//                    SharedPrefrences.setBool("mysave_strain_switch", true, MySavesActivity.this);
//                } else {
//                    SharedPrefrences.setBool("mysave_strain_switch", false, MySavesActivity.this);
//                }

                if (Budz_MAp_Switch.isChecked()) {
                    if (filter_ids.length() == 0) {
                        filter_ids = filter_ids + "8";
                    } else {
                        filter_ids = filter_ids + ",8";
                    }
                    SharedPrefrences.setBool("mysave_budz_switch", true, MySavesActivity.this);
                } else {
                    SharedPrefrences.setBool("mysave_budz_switch", false, MySavesActivity.this);
                }


//                if (Group_Switch.isChecked()) {
//                    if (filter_ids.length() == 0) {
//                        filter_ids = filter_ids + "6";
//                    } else {
//                        filter_ids = filter_ids + ",6";
//                    }
//                    SharedPrefrences.setBool("mysave_group_switch", true, MySavesActivity.this);
//                } else {
//                    SharedPrefrences.setBool("mysave_group_switch", false, MySavesActivity.this);
//                }
                if (Strain_Switch_Search.isChecked()) {
                    if (filter_ids.length() == 0) {
                        filter_ids = filter_ids + "10";
                    } else {
                        filter_ids = filter_ids + ",10";
                    }
                    SharedPrefrences.setBool("mysave_strain_switch_search", true, MySavesActivity.this);
                } else {
                    SharedPrefrences.setBool("mysave_strain_switch_search", false, MySavesActivity.this);
                }
                if (special_switch_save.isChecked()) {
                    if (filter_ids.length() == 0) {
                        filter_ids = filter_ids + "11";
                    } else {
                        filter_ids = filter_ids + ",11";
                    }
                    SharedPrefrences.setBool("mysave_special_switch_save", true, MySavesActivity.this);
                } else {
                    SharedPrefrences.setBool("mysave_special_switch_save", false, MySavesActivity.this);
                }

                Filter_ids = filter_ids;
                refreshLayout.setRefreshing(true);
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(MySavesActivity.this, false, URL.filter_my_save + "?filter=" + Filter_ids, jsonObject, user.getSession_key(), Request.Method.GET, MySavesActivity.this, filter_my_save);

            }
        });
        list_indicator_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPrefrences.setBool("MysSaveisEdits", true, MySavesActivity.this);

                is_list_open[0] = false;
                slide.hide();
                String filter_ids = "";
                if (Q_A_Switch.isChecked()) {
                    if (filter_ids.length() == 0) {
                        filter_ids = filter_ids + "4";
                    } else {
                        filter_ids = filter_ids + ",4";
                    }

                    SharedPrefrences.setBool("mysave_qa_switch", true, MySavesActivity.this);
                } else {
                    SharedPrefrences.setBool("mysave_qa_switch", false, MySavesActivity.this);
                }
                if (chat_switch_save.isChecked()) {
                    if (filter_ids.length() == 0) {
                        filter_ids = filter_ids + "2";
                    } else {
                        filter_ids = filter_ids + ",2";
                    }

                    SharedPrefrences.setBool("mysave_chat_switch_save", true, MySavesActivity.this);
                } else {
                    SharedPrefrences.setBool("mysave_chat_switch_save", false, MySavesActivity.this);
                }
                if (chat_budz_switch_save.isChecked()) {
                    if (filter_ids.length() == 0) {
                        filter_ids = filter_ids + "13";
                    } else {
                        filter_ids = filter_ids + ",13";
                    }

                    SharedPrefrences.setBool("mysave_chat_budz_switch_save", true, MySavesActivity.this);
                } else {
                    SharedPrefrences.setBool("mysave_chat_budz_switch_save", false, MySavesActivity.this);
                }
//                if (Journal_Switch.isChecked()) {
//                    if (filter_ids.length() == 0) {
//                        filter_ids = filter_ids + "3";
//                    } else {
//                        filter_ids = filter_ids + ",3";
//                    }
//                    SharedPrefrences.setBool("mysave_journal_switch", true, MySavesActivity.this);
//                } else {
//                    SharedPrefrences.setBool("mysave_journal_switch", false, MySavesActivity.this);
//                }

//                if (Strain_Switch.isChecked()) {
//                    if (filter_ids.length() == 0) {
//                        filter_ids = filter_ids + "7";
//                    } else {
//                        filter_ids = filter_ids + ",7";
//                    }
//                    SharedPrefrences.setBool("mysave_strain_switch", true, MySavesActivity.this);
//                } else {
//                    SharedPrefrences.setBool("mysave_strain_switch", false, MySavesActivity.this);
//                }

                if (Budz_MAp_Switch.isChecked()) {
                    if (filter_ids.length() == 0) {
                        filter_ids = filter_ids + "8";
                    } else {
                        filter_ids = filter_ids + ",8";
                    }
                    SharedPrefrences.setBool("mysave_budz_switch", true, MySavesActivity.this);
                } else {
                    SharedPrefrences.setBool("mysave_budz_switch", false, MySavesActivity.this);
                }


//                if (Group_Switch.isChecked()) {
//                    if (filter_ids.length() == 0) {
//                        filter_ids = filter_ids + "6";
//                    } else {
//                        filter_ids = filter_ids + ",6";
//                    }
//                    SharedPrefrences.setBool("mysave_group_switch", true, MySavesActivity.this);
//                } else {
//                    SharedPrefrences.setBool("mysave_group_switch", false, MySavesActivity.this);
//                }
                if (Strain_Switch_Search.isChecked()) {
                    if (filter_ids.length() == 0) {
                        filter_ids = filter_ids + "10";
                    } else {
                        filter_ids = filter_ids + ",10";
                    }
                    SharedPrefrences.setBool("mysave_strain_switch_search", true, MySavesActivity.this);
                } else {
                    SharedPrefrences.setBool("mysave_strain_switch_search", false, MySavesActivity.this);
                }
                if (special_switch_save.isChecked()) {
                    if (filter_ids.length() == 0) {
                        filter_ids = filter_ids + "11";
                    } else {
                        filter_ids = filter_ids + ",11";
                    }
                    SharedPrefrences.setBool("mysave_special_switch_save", true, MySavesActivity.this);
                } else {
                    SharedPrefrences.setBool("mysave_special_switch_save", false, MySavesActivity.this);
                }

                Filter_ids = filter_ids;
                refreshLayout.setRefreshing(true);
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(MySavesActivity.this, false, URL.filter_my_save + "?filter=" + Filter_ids, jsonObject, user.getSession_key(), Request.Method.GET, MySavesActivity.this, filter_my_save);

            }
        });

        slide = new SlideUpBuilder(slide_header_list)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {

                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                            list_indicator.setVisibility(View.VISIBLE);
                            Indicator_layout.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .withStartGravity(Gravity.TOP)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        data_list.clear();
        refreshLayout.setRefreshing(false);
        try {
            JSONArray array = new JSONObject(response).getJSONArray("successData");
            for (int x = 0; x < array.length(); x++) {
                JSONObject object = array.getJSONObject(x);
                MySavedDataModal dataModal = new MySavedDataModal();
                dataModal.setId(object.getInt("id"));
                dataModal.setUser_id(object.getInt("user_id"));
                dataModal.setModel(object.getString("model"));
                dataModal.setDescription(object.getString("description"));
                dataModal.setType_id(object.getInt("type_id"));
                dataModal.setType_sub_id(object.getInt("type_sub_id"));
                dataModal.setCreated_at(object.getString("created_at"));
                dataModal.setUpdated_at(object.getString("updated_at"));
                dataModal.setTitle(object.getString("title"));
                if(object.getInt("type_id") == 2 || object.getInt("type_id") == 13){
                    dataModal.setNameUser(object.getJSONObject("user").getString("first_name"));
                    dataModal.setAvatarUser(object.getJSONObject("user").getString("avatar"));
                    dataModal.setImageUser(object.getJSONObject("user").getString("image_path"));
                }
                data_list.add(dataModal);
            }
            if (data_list.size() > 0) {
                not_fount.setVisibility(View.GONE);
            } else {
                not_fount.setVisibility(View.VISIBLE);

            }
            recyler_adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(View view, final int position) {
        int type = data_list.get(position).getType_id();
        switch (type) {
            case 3:
                Intent journals_intetn = new Intent(MySavesActivity.this, JournalDetailsActivity.class);
                journals_intetn.putExtra("journal_id", data_list.get(position).getType_sub_id());
                startActivity(journals_intetn);
                break;
            case 2:
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(MySavesActivity.this, true, URL.get_chat_detail_by_id + data_list.get(position).getType_sub_id(), jsonObject, user.getSession_key(), Request.Method.GET, new APIResponseListner() {
                    @Override
                    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                        Log.d("onRequestSuccess: ", response);
                        try {
                            JSONObject msg_object = new JSONObject(response).getJSONArray("successData").getJSONObject(0);
                            MessagesDataModel messagesDataModel = new MessagesDataModel();
                            messagesDataModel.setId(msg_object.getInt("id"));
                            messagesDataModel.setSender_id(msg_object.getInt("sender_id"));
                            messagesDataModel.setReceiver_id(msg_object.getInt("receiver_id"));
//                            messagesDataModel.setLast_message_id(msg_object.getInt("last_message_id"));
//                            messagesDataModel.setSender_deleted(msg_object.getInt("sender_deleted"));
//                            messagesDataModel.setReceiver_deleted(msg_object.getInt("receiver_deleted"));
                            messagesDataModel.setCreated_at(msg_object.getString("created_at"));
                            messagesDataModel.setUpdated_at(msg_object.getString("updated_at"));
//                            messagesDataModel.setMessages_count(msg_object.getInt("messages_count"));
                            messagesDataModel.setSender_first_name(msg_object.getJSONObject("sender").getString("first_name"));
                            messagesDataModel.setSender_image_path(msg_object.getJSONObject("sender").getString("image_path"));
                            messagesDataModel.setSender_avatar(msg_object.getJSONObject("sender").getString("avatar"));
                            messagesDataModel.setSender_point(msg_object.getJSONObject("sender").getInt("points"));
                            messagesDataModel.setReceiver_first_name(msg_object.getJSONObject("receiver").getString("first_name"));
                            messagesDataModel.setReceiver_image_path(msg_object.getJSONObject("receiver").getString("image_path"));
                            messagesDataModel.setReceiver_avatar(msg_object.getJSONObject("receiver").getString("avatar"));
                            messagesDataModel.setSpecial_icon_rec(msg_object.getJSONObject("receiver").getString("special_icon"));
                            messagesDataModel.setRecev_point(msg_object.getJSONObject("receiver").getInt("points"));
                            messagesDataModel.setSpecial_icon_sen(msg_object.getJSONObject("sender").getString("special_icon"));
                            messagesDataModel.setMain(true);
                            MessagingChatViewActivity.chat_message_data_modal = messagesDataModel;
                            if (MessagingChatViewActivity.chat_message_data_modal.getReceiver_id() == Splash.user.getUser_id()) {
                                Splash.otherName = MessagingChatViewActivity.chat_message_data_modal.getSender_first_name();
                            } else {
                                Splash.otherName = MessagingChatViewActivity.chat_message_data_modal.getReceiver_first_name();
                            }
                            GoTo(MySavesActivity.this, MessagingChatViewActivity.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onRequestError(String response, APIActions.ApiActions apiActions) {

                    }
                }, filter_my_save);
                break;
            case 13:
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("chat_id", data_list.get(position).getType_sub_id());
                    new VollyAPICall(MySavesActivity.this, true, URL.get_budz_detail_chat, jsonObject, user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                        @Override
                        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                            Log.d("onRequestSuccess: ", response);
                            try {
                                JSONObject msg_object = new JSONObject(response).getJSONArray("successData").getJSONObject(0);
                                JSONObject bud = new JSONObject(response).getJSONObject("successMessage");//.getJSONObject(0);
                                MessagesDataModel messagesDataModel = new MessagesDataModel();
                                messagesDataModel.setId(msg_object.getInt("id"));
                                messagesDataModel.setSender_id(msg_object.getInt("sender_id"));
                                messagesDataModel.setReceiver_id(msg_object.getInt("receiver_id"));
//                            messagesDataModel.setLast_message_id(msg_object.getInt("last_message_id"));
//                            messagesDataModel.setSender_deleted(msg_object.getInt("sender_deleted"));
//                            messagesDataModel.setReceiver_deleted(msg_object.getInt("receiver_deleted"));
                                messagesDataModel.setCreated_at(msg_object.getString("created_at"));
                                messagesDataModel.setUpdated_at(msg_object.getString("updated_at"));
//                            messagesDataModel.setMessages_count(msg_object.getInt("messages_count"));
                                messagesDataModel.setSender_first_name(msg_object.getJSONObject("sender").getString("first_name"));
                                messagesDataModel.setSender_image_path(msg_object.getJSONObject("sender").getString("image_path"));
                                messagesDataModel.setSender_avatar(msg_object.getJSONObject("sender").getString("avatar"));
                                messagesDataModel.setSender_point(msg_object.getJSONObject("sender").getInt("points"));
                                messagesDataModel.setReceiver_first_name(msg_object.getJSONObject("receiver").getString("first_name"));
                                messagesDataModel.setReceiver_image_path(msg_object.getJSONObject("receiver").getString("image_path"));
                                messagesDataModel.setReceiver_avatar(msg_object.getJSONObject("receiver").getString("avatar"));
                                messagesDataModel.setSpecial_icon_rec(msg_object.getJSONObject("receiver").getString("special_icon"));
                                messagesDataModel.setRecev_point(msg_object.getJSONObject("receiver").getInt("points"));
                                messagesDataModel.setSpecial_icon_sen(msg_object.getJSONObject("sender").getString("special_icon"));
                                Splash.image_path = URL.images_baseurl + bud.getString("logo");
                                Splash.NameBusiness = bud.getString("title");
                                MessagingBusinessChatViewActivity.chat_message_data_modal = messagesDataModel;
                                if (MessagingBusinessChatViewActivity.chat_message_data_modal.getReceiver_id() == Splash.user.getUser_id()) {
                                    Splash.otherName = MessagingBusinessChatViewActivity.chat_message_data_modal.getSender_first_name();
                                } else {
                                    Splash.otherName = MessagingBusinessChatViewActivity.chat_message_data_modal.getReceiver_first_name();
                                }
                                Splash.otherName = bud.getString("title");
                                Intent i = new Intent(MySavesActivity.this, MessagingBusinessChatViewActivity.class);
                                i.putExtra("budz_id", msg_object.getInt("budz_id"));
                                i.putExtra("chat_id", data_list.get(position).getType_sub_id());
                                startActivity(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onRequestError(String response, APIActions.ApiActions apiActions) {

                        }
                    }, filter_my_save);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case 4:
                HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                model.setId(data_list.get(position).getType_sub_id());
                showSubFragmentListner_object.ShowQuestions(model, true);
                finish();
                break;
            case 7:
                Intent strain_intetn = new Intent(MySavesActivity.this, StrainDetailsActivity.class);
                strain_intetn.putExtra("strain_id", data_list.get(position).getType_sub_id());
                startActivity(strain_intetn);
                break;
            case 8:
                Intent budzmap_intetn = new Intent(MySavesActivity.this, BudzMapDetailsActivity.class);
                budzmap_intetn.putExtra("budzmap_id", data_list.get(position).getType_sub_id());
                startActivity(budzmap_intetn);
                break;
            case 11:
                Intent budzmap_intetn_sp = new Intent(MySavesActivity.this, BudzMapDetailsActivity.class);
                int id_business = Integer.parseInt(data_list.get(position).getDescription().split("&")[0].replace("business_id=", ""));
                budzmap_intetn_sp.putExtra("budzmap_id", id_business);
                budzmap_intetn_sp.putExtra("view_specials", true);
                startActivity(budzmap_intetn_sp);
                break;
            case 6:
                Intent intent = new Intent(MySavesActivity.this, GroupsChatViewActivity.class);
                intent.putExtra("goup_id", data_list.get(position).getType_sub_id());
                startActivity(intent);
                break;
            case 10:
                JSONObject object = null;
                try {
                    object = new JSONObject(data_list.get(position).getDescription());
                    HomeQAfragmentDataModel model1 = new HomeQAfragmentDataModel();
                    showSubFragmentListner_object.ShowStrainSearch(model1, object.getString("search_data"));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}
