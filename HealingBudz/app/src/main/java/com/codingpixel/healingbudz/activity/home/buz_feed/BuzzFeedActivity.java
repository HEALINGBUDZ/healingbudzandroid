package com.codingpixel.healingbudz.activity.home.buz_feed;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BuzzFeedDataModel;
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.DataModel.MessagesDataModel;
import com.codingpixel.healingbudz.DataModel.ShootOutDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.Flags;
import com.codingpixel.healingbudz.Utilities.GetUserLatLng;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.Wall.WallPostDetailActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.JournalDetailsActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingBusinessChatViewActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingChatViewActivity;
import com.codingpixel.healingbudz.activity.shoot_out.dialog.ShootOutAlertDialog;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.BuzzFeedRecylerAdapter;
import com.codingpixel.healingbudz.adapter.QAHomeFragmentRecylerAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.onesignal.OneSignal;
import com.onesignal.shortcutbadger.ShortcutBadger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.HomeActivity.showSubFragmentListner_object;
import static com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.DiscussQuestionFragment.isNewScreen;
import static com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingChatViewActivity.chat_message_data_modal;
import static com.codingpixel.healingbudz.activity.home.side_menu.profile.UserProfileActivity.profiledataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.clear_all_notifications;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_notifications;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_shout_outs;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;

public class BuzzFeedActivity extends AppCompatActivity implements View.OnClickListener, APIResponseListner, QAHomeFragmentRecylerAdapter.ItemClickListener, UserLocationListner, ShootOutAlertDialog.OnDialogFragmentClickListener {
    RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    BuzzFeedRecylerAdapter recyler_adapter;
    ArrayList<BuzzFeedDataModel> list = new ArrayList<>();
    ImageView Back, Home;
    TextView New_Notifications, not_found;
    Button Clear_All;
    LinearLayout main_count;
    View dis_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzz_feed);
        main_count = findViewById(R.id.main_count);
        not_found = findViewById(R.id.not_found);
        dis_view = findViewById(R.id.dis_view);
        InitRefreshLayout();

        Back = (ImageView) findViewById(R.id.back_btn);
        Home = (ImageView) findViewById(R.id.home_btn);


        Back.setOnClickListener(this);
        Home.setOnClickListener(this);
        refreshLayout.setRefreshing(true);
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(BuzzFeedActivity.this, false, URL.get_notifications, jsonObject, user.getSession_key(), Request.Method.GET, BuzzFeedActivity.this, get_notifications);
        dis_view.setVisibility(View.VISIBLE);
        main_count.setVisibility(View.GONE);
        OneSignal.clearOneSignalNotifications();
        ShortcutBadger.removeCount(this);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
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
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void InitRefreshLayout() {
        New_Notifications = (TextView) findViewById(R.id.new_notifications);
        recyclerView = (RecyclerView) findViewById(R.id.buzz_feed_recyler_item);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list.clear();
        recyler_adapter = new BuzzFeedRecylerAdapter(this, list);
        recyler_adapter.setClickListener(this);
        recyclerView.setAdapter(recyler_adapter);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.parseColor("#7cc144"));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                dis_view.setVisibility(View.VISIBLE);
                main_count.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(BuzzFeedActivity.this, false, URL.get_notifications, jsonObject, user.getSession_key(), Request.Method.GET, BuzzFeedActivity.this, get_notifications);


            }
        });

        Clear_All = (Button) findViewById(R.id.buzz_feed_clear_btn);
        Clear_All.setVisibility(View.GONE);
        Clear_All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                New_Notifications.setText(+list.size() + " New notifications since you last logged in");
                recyler_adapter.notifyDataSetChanged();
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(BuzzFeedActivity.this, false, URL.clear_all_notifications, jsonObject, user.getSession_key(), Request.Method.GET, BuzzFeedActivity.this, clear_all_notifications);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.home_btn:
                GoToHome(this, true);
                finish();
                break;
        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {

        if (apiActions == clear_all_notifications) {

            Clear_All.setVisibility(View.GONE);
            refreshLayout.setRefreshing(true);
            dis_view.setVisibility(View.VISIBLE);
            main_count.setVisibility(View.GONE);
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(BuzzFeedActivity.this, false, URL.get_notifications, jsonObject, user.getSession_key(), Request.Method.GET, BuzzFeedActivity.this, get_notifications);
        } else if (apiActions == get_shout_outs) {
            Log.d("response", response);
            try {
                JSONObject object = new JSONObject(response).getJSONObject("successData").getJSONObject("shout_outs");
                ShootOutDataModel shootOutDataModel = new ShootOutDataModel();
                shootOutDataModel.setLikes_count(object.getInt("likes_count"));
                shootOutDataModel.setUserlike_count(object.getInt("userlike_count"));
                shootOutDataModel.setText("");//jsonObject.getJSONObject(x).getString("text")
                shootOutDataModel.setId(object.getInt("id"));
                shootOutDataModel.setUser_id(object.getInt("user_id"));
                shootOutDataModel.setSub_user_id(object.getInt("sub_user_id"));
                shootOutDataModel.setTitle(object.getString("title"));
                shootOutDataModel.setMessage(object.getString("message"));
                shootOutDataModel.setValidity_date(object.getString("validity_date"));
                shootOutDataModel.setImage(object.optString("image"));
                shootOutDataModel.setLat(object.optDouble("lat"));
                shootOutDataModel.setLng(object.optDouble("lng"));
                if (!object.isNull("budz_special_id"))
                    shootOutDataModel.setBudzSpId(object.getInt("budz_special_id"));
                else {
                    shootOutDataModel.setBudzSpId(-1);
                }
                shootOutDataModel.setZip_code(object.optString("zip_code"));
                shootOutDataModel.setPublic_location(object.optString("public_location"));
                shootOutDataModel.setCreated_at(object.getString("created_at"));
                shootOutDataModel.setUpdated_at(object.getString("updated_at"));
                shootOutDataModel.setDistance(String.valueOf(Constants.distance(object.optDouble("lat"), object.optDouble("lng"), lat, lng)));//object.getString("distance")
                shootOutDataModel.setReceived_From(object.getJSONObject("get_sub_user").getString("title"));
                shootOutDataModel.setUser_Image_Path(object.getJSONObject("get_sub_user").getString("logo"));
                shootOutDataModel.setAvatar("");
                shootOutDataModel.setSpecial_icon("");
                ShootOutAlertDialog shootOutAlertDialog = ShootOutAlertDialog.newInstance(this, shootOutDataModel);
                shootOutAlertDialog.show(this.getSupportFragmentManager(), "dialog");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            refreshLayout.setRefreshing(false);
            Log.d("response", response);
            list.clear();
            try {
                JSONArray jsonObject = new JSONObject(response).getJSONObject("successData").getJSONArray("notifications");
                for (int x = 0; x < jsonObject.length(); x++) {
                    JSONObject object = jsonObject.getJSONObject(x);
                    BuzzFeedDataModel dataModel = new BuzzFeedDataModel();
                    if (!object.getString("type").equalsIgnoreCase("admin")) {

                        dataModel.setId(object.getInt("id"));
                        dataModel.setUser_id(object.optInt("user_id"));
                        dataModel.setType(object.optString("type"));
                        dataModel.setOn_user(String.valueOf(object.optInt("on_user")));
                        if (!object.isNull("type_sub_id")) {
                            dataModel.setTypeSubId(String.valueOf(object.optInt("type_sub_id")));
                        } else {
                            dataModel.setTypeSubId("-1");
                        }
                        dataModel.setModel(object.getString("model"));
                        dataModel.setDescription(object.getString("description"));
                        dataModel.setText(object.getString("text"));
                        dataModel.setNotification_text(object.getString("notification_text"));
                        dataModel.setCreated_at(object.getString("created_at"));
                        dataModel.setUpdated_at(object.getString("updated_at"));
                        dataModel.setType_id(object.getInt("type_id"));
                        dataModel.setIs_read(object.getInt("is_read"));
                    } else {
                        dataModel.setId(-1);
                        dataModel.setUser_id(-1);
                        dataModel.setType(object.getString("type"));
                        dataModel.setOn_user("-1");
                        if (!object.isNull("type_sub_id")) {
                            dataModel.setTypeSubId("-1");
                        } else {
                            dataModel.setTypeSubId("-1");
                        }
                        dataModel.setModel("-1");
                        dataModel.setDescription(object.getString("description"));
                        dataModel.setText(object.getString("text"));
                        dataModel.setNotification_text(object.getString("notification_text"));
                        dataModel.setCreated_at(object.getString("created_at"));
                        dataModel.setUpdated_at(object.getString("updated_at"));
                        dataModel.setType_id(-1);
                        dataModel.setIs_read(-1);
                    }
                    list.add(dataModel);
                }
                recyler_adapter.notifyDataSetChanged();
                if (list.size() > 0) {
                    Clear_All.setVisibility(View.VISIBLE);
                    not_found.setVisibility(View.GONE);
                } else {
                    Clear_All.setVisibility(View.GONE);
                    not_found.setVisibility(View.VISIBLE);
                }
                New_Notifications.setText(user.getNotificationCOunt() + " New notifications since you last logged in");
                if (user.getNotificationCOunt() == 0) {
                    dis_view.setVisibility(View.VISIBLE);
                    main_count.setVisibility(View.GONE);
                } else {
                    user.setNotificationCOunt(0);
                    main_count.setVisibility(View.VISIBLE);
                    dis_view.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetUserLatLng().getUserLocation(this, BuzzFeedActivity.this);
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        refreshLayout.setRefreshing(false);
        Log.d("response", response);
    }

    @Override
    public void onItemClick(View view, int position) {
        String type = list.get(position).getType();

        switch (type) {
            case "ShoutOut":
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(this, true, URL.shout_detail + "/" + list.get(position).getType_id() + "?lat=" + lat + "&lng=" + lng, jsonObject, user.getSession_key(), Request.Method.GET, this, get_shout_outs);

//                Intent shout_specail = new Intent(this, ShootOutActivity.class);
//                shout_specail.putExtra("id", list.get(position).getType_id());
//                startActivity(shout_specail);
                break;
            case "Questions":
                HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                model.setId(list.get(position).getType_id());
                showSubFragmentListner_object.ShowQuestions(model, true);
//                finish();
                break;
            case "Answers":
                HomeQAfragmentDataModel model1 = new HomeQAfragmentDataModel();
                model1.setId(list.get(position).getType_id());
                showSubFragmentListner_object.ShowAnswers(model1, list.get(position).getType_id(), true);
//                finish();
                break;
            case "Budz Map":
            case "Budz Adz":
                Intent budzmap_intetn = new Intent(BuzzFeedActivity.this, BudzMapDetailsActivity.class);
                budzmap_intetn.putExtra("budzmap_id", list.get(position).getType_id());
                startActivity(budzmap_intetn);
                break;
            case "Likes":
                Model_Intent(position);
                break;
            case "Favorites":
                Model_Intent(position);
                break;
            case "Journal":
                Intent journals_intetn = new Intent(BuzzFeedActivity.this, JournalDetailsActivity.class);
                journals_intetn.putExtra("journal_id", list.get(position).getType_id());
                startActivity(journals_intetn);
                break;
            case "Groups":
                Intent intent = new Intent(BuzzFeedActivity.this, GroupsChatViewActivity.class);
                intent.putExtra("goup_id", list.get(position).getType_id());
                startActivity(intent);
                break;
            case "Tags":
                Model_Intent(position);
                break;
            case "Strains":
                Intent strain_intetn = new Intent(BuzzFeedActivity.this, StrainDetailsActivity.class);
                strain_intetn.putExtra("strain_id", list.get(position).getType_id());
                startActivity(strain_intetn);
                break;
            case "Chat":
                MessagesDataModel messagesDataModel = new MessagesDataModel();
                messagesDataModel.setId(list.get(position).getType_id());
                messagesDataModel.setSender_id(user.getUser_id());
                messagesDataModel.setReceiver_id(list.get(position).getUser_id());
                messagesDataModel.setLast_message_id(list.get(position).getType_id());
                messagesDataModel.setSender_deleted(0);
                messagesDataModel.setReceiver_deleted(0);
                messagesDataModel.setCreated_at(profiledataModel.getCreated_at());
                messagesDataModel.setUpdated_at(profiledataModel.getCreated_at());
                messagesDataModel.setMessages_count(0);
                messagesDataModel.setSender_first_name("");
                messagesDataModel.setSender_image_path(null);
                messagesDataModel.setSender_avatar("");
                messagesDataModel.setReceiver_first_name("");
                messagesDataModel.setReceiver_image_path(null);
                messagesDataModel.setReceiver_avatar("");
                chat_message_data_modal = messagesDataModel;
                GoTo(BuzzFeedActivity.this, MessagingChatViewActivity.class);
//                finish();
                break;
            case "BudzChat":
                MessagesDataModel messagesDataModelBusiness = new MessagesDataModel();
                messagesDataModelBusiness.setId(list.get(position).getType_id());
                messagesDataModelBusiness.setSender_id(Integer.parseInt(list.get(position).getOn_user()));
                messagesDataModelBusiness.setReceiver_id(list.get(position).getUser_id());
                messagesDataModelBusiness.setLast_message_id(list.get(position).getType_id());
                messagesDataModelBusiness.setSender_deleted(0);
                messagesDataModelBusiness.setReceiver_deleted(0);
                messagesDataModelBusiness.setCreated_at(list.get(position).getCreated_at());
                messagesDataModelBusiness.setUpdated_at(list.get(position).getUpdated_at());
                messagesDataModelBusiness.setMessages_count(0);
                messagesDataModelBusiness.setSender_first_name("");
                messagesDataModelBusiness.setSender_image_path(null);
                messagesDataModelBusiness.setSender_avatar("");
                messagesDataModelBusiness.setReceiver_first_name("");
                messagesDataModelBusiness.setReceiver_image_path(null);
                messagesDataModelBusiness.setReceiver_avatar("");
                Splash.image_path = URL.images_baseurl + "";
                Splash.NameBusiness = list.get(position).getNotification_text().replace(" sent you a private message.", "").replace(" send you a budz message.", "");
                Splash.otherName = list.get(position).getNotification_text().replace(" sent you a private message.", "").replace(" send you a budz message.", "");
                MessagingBusinessChatViewActivity.chat_message_data_modal = messagesDataModelBusiness;
                Intent i = new Intent(this, MessagingBusinessChatViewActivity.class);
                i.putExtra("budz_id", Integer.parseInt(list.get(position).getTypeSubId()));
                i.putExtra("chat_id", list.get(position).getType_id());
                startActivity(i);
//                finish();
                break;
            case "Users":
                isNewScreen = true;
                GoToProfile(this, list.get(position).getType_id());
                break;
            case "Comment":
            case "Post":
                launchPostDetailActivity(list.get(position).getType_id());
                break;
            default:
                break;
        }
    }

    public void Model_Intent(int position) {
        String activty_model = list.get(position).getModel();
        if (activty_model.contains("Question")) {
            HomeQAfragmentDataModel model_q = new HomeQAfragmentDataModel();
            model_q.setId(list.get(position).getType_id());
            showSubFragmentListner_object.ShowQuestions(model_q, true);
//            finish();
        } else if (activty_model.contains("Answers")) {
            HomeQAfragmentDataModel model_a = new HomeQAfragmentDataModel();
            model_a.setId(list.get(position).getType_id());
            showSubFragmentListner_object.ShowAnswers(model_a, list.get(position).getType_id(), true);
//            finish();
        } else if (activty_model.contains("Strains") || activty_model.contains("Strain")) {
            Intent strain_intetn = new Intent(BuzzFeedActivity.this, StrainDetailsActivity.class);
            strain_intetn.putExtra("strain_id", list.get(position).getType_id());
            startActivity(strain_intetn);
        } else if (activty_model.contains("Groups")) {
            Intent intent = new Intent(BuzzFeedActivity.this, GroupsChatViewActivity.class);
            intent.putExtra("goup_id", list.get(position).getType_id());
            startActivity(intent);
        } else if (activty_model.contains("Budz Map") || activty_model.contains("Budz Adz") || activty_model.contains("SubUser")) {
            Intent budzmap_intetn = new Intent(BuzzFeedActivity.this, BudzMapDetailsActivity.class);
            budzmap_intetn.putExtra("budzmap_id", list.get(position).getType_id());
            startActivity(budzmap_intetn);
        } else if (activty_model.contains("Journal")) {
            Intent journals_intetn = new Intent(BuzzFeedActivity.this, JournalDetailsActivity.class);
            journals_intetn.putExtra("journal_id", list.get(position).getType_id());
            startActivity(journals_intetn);
        } else if (activty_model.contains("ShoutOutLike")) {
//            GoTo(this, ShootOutActivity.class);
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(this, true, URL.shout_detail + "/" + list.get(position).getType_id() + "?lat=" + lat + "&lng=" + lng, jsonObject, user.getSession_key(), Request.Method.GET, this, get_shout_outs);

//            Intent budzmap_intetn = new Intent(this, BudzMapDetailsActivity.class);
//            budzmap_intetn.putExtra("budzmap_id", Integer.parseInt(list.get(position).getTypeSubId()));
//            budzmap_intetn.putExtra("view_specials", true);
//            startActivity(budzmap_intetn);
        } else if (activty_model.contains("UserPost")) {
            launchPostDetailActivity(list.get(position).getType_id());

        }

    }

    private void launchPostDetailActivity(int postId) {
        Bundle b = new Bundle();
        b.putInt(Constants.POST_ID_EXTRA, postId);
        Utility.launchActivityForResult(this, WallPostDetailActivity.class, b, Flags.ACTIVITIES_COMMUNICATION_FLAG);
        //for fragment
        //Utility.launchActivityForResultFromFragment(YOUR_FRAGMENT.this, getActivity(), b, Flags.ACTIVITIES_COMMUNICATION_FLAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Flags.SESSION_OUT) {
            //do functionality for session out
            return;
        }
    }

    Double lat = 0.0, lng = 0.0;

    @Override
    public void onLocationSuccess(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    @Override
    public void onLocationFailed(String Error) {
        lat = Double.parseDouble(SharedPrefrences.getString("lat_cur", this, "0"));
        lng = Double.parseDouble(SharedPrefrences.getString("lng_cur", this, "0"));
    }

    @Override
    public void onViewSpecialBtnClink(ShootOutAlertDialog dialog, ShootOutDataModel dataModel) {
        Intent budzmap_intetn = new Intent(getApplicationContext(), BudzMapDetailsActivity.class);
        budzmap_intetn.putExtra("budzmap_id", dataModel.getSub_user_id());
        budzmap_intetn.putExtra("view_specials", true);
        startActivity(budzmap_intetn);
        dialog.dismiss();
    }
}
