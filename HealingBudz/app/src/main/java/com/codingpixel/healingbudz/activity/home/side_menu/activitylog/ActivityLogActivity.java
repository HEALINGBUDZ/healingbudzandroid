package com.codingpixel.healingbudz.activity.home.side_menu.activitylog;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.DataModel.JournalHeaderDataModel;
import com.codingpixel.healingbudz.DataModel.ShootOutDataModel;
import com.codingpixel.healingbudz.DataModel.SlideMenuActivityListData;
import com.codingpixel.healingbudz.GestureListner.OnSwipeTouchListner;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.Flags;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.Wall.WallPostDetailActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.JournalDetailsActivity;
import com.codingpixel.healingbudz.activity.shoot_out.dialog.ShootOutAlertDialog;
import com.codingpixel.healingbudz.adapter.ActivityLogHeaderListRecylerAdapter;
import com.codingpixel.healingbudz.adapter.ActivityLogRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
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

import static com.codingpixel.healingbudz.Utilities.DateConverter.convertDate;
import static com.codingpixel.healingbudz.activity.home.HomeActivity.showSubFragmentListner_object;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_shout_outs;
import static com.codingpixel.healingbudz.network.model.URL.filter_activity;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;
import static com.codingpixel.healingbudz.test_data.GroupsHeaderListData.activity_log_groups_headerData;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class ActivityLogActivity extends AppCompatActivity implements APIResponseListner, ActivityLogHeaderListRecylerAdapter.ItemClickListener, ActivityLogRecylerAdapter.ItemClickListener, ShootOutAlertDialog.OnDialogFragmentClickListener {
    RecyclerView Home_search_recyler_view;
    private SlideUp slide = null;
    ImageView Home, Back;
    ArrayList<SlideMenuActivityListData> activity_list_data = new ArrayList<>();
    ArrayList<SlideMenuActivityListData> filter_activity_list_data = new ArrayList<>();
    RelativeLayout Main_Menu;
    RelativeLayout slide_header_list;
    ImageView list_indicator;
    RecyclerView journal_header_list_recyler_view;
    ImageView list_indicator_close;
    boolean[] is_list_open = {false};
    RelativeLayout Indicator_layout;
    TextView No_Activity_Found;
    int clicked_first_time = 0;
    TextView not_fount;
    ActivityLogRecylerAdapter recyler_adapter;
    private int pages = 0;
    String filer = "&filter=";
    SwipeRefreshLayout swipe_rf;
    LinearLayout refresh;
    private boolean isAppiCallActive = false;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        clicked_first_time = 0;
        SharedPrefrences.setInt("ACTIVITY_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", -1, ActivityLogActivity.this);
        ChangeStatusBarColor(ActivityLogActivity.this, "#171717");
        HideKeyboard(ActivityLogActivity.this);
        No_Activity_Found = (TextView) findViewById(R.id.no_activity);
        Home_search_recyler_view = (RecyclerView) findViewById(R.id.activity_log_recyler_view);
        refresh = (LinearLayout) findViewById(R.id.refresh);
        swipe_rf = (SwipeRefreshLayout) findViewById(R.id.swipe_rf);
        swipe_rf.setColorSchemeColors(Color.parseColor("#0083cb"));
        swipe_rf.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        swipe_rf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_rf.setRefreshing(true);
                isAppiCallActive = true;
                JSONObject jsonObject = new JSONObject();
                pages = 0;
                new VollyAPICall(ActivityLogActivity.this, false, filter_activity + "/" + user.getUser_id() + "?skip=0" + filer + "", jsonObject, user.getSession_key(), Request.Method.GET, ActivityLogActivity.this, APIActions.ApiActions.get_activities);

            }
        });
        RecyclerView.LayoutManager layoutManager_home_saerch = new LinearLayoutManager(ActivityLogActivity.this);
        Home_search_recyler_view.setLayoutManager(layoutManager_home_saerch);
        recyler_adapter = new ActivityLogRecylerAdapter(ActivityLogActivity.this, filter_activity_list_data);
        Home_search_recyler_view.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
        InitHeaderSlideView();

        JSONObject jsonObject = new JSONObject();
        isAppiCallActive = true;
        new VollyAPICall(this, true, filter_activity + "/" + user.getUser_id() + "?skip=0" + filer + "", jsonObject, user.getSession_key(), Request.Method.GET, ActivityLogActivity.this, APIActions.ApiActions.get_activities);

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
                GoToHome(ActivityLogActivity.this, true);
                finish();
            }
        });
        Home_search_recyler_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(Home_search_recyler_view.getLayoutManager());
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                Log.d("vissible", lastVisible + "");
                Log.d("vissible_t", (lastVisible % 10) + "");
                if (lastVisible >= (pages + 1) * 9 && !isAppiCallActive) {
                    if ((lastVisible % 9) == 0) {
                        pages = pages + 1;
                        JSONObject object = new JSONObject();
                        refresh.setVisibility(View.VISIBLE);
                        String url = "";
//                        swipe_rf.setRefreshing(true);
//                        Refresh.setVisibility(View.VISIBLE);
                        new VollyAPICall(ActivityLogActivity.this, false, filter_activity + "/" + user.getUser_id() + "?skip=" + pages + filer, object, user.getSession_key(), Request.Method.GET, ActivityLogActivity.this, APIActions.ApiActions.get_activities);
                    }
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void InitHeaderSlideView() {
        Main_Menu = (RelativeLayout) findViewById(R.id.top_menu);
        slide_header_list = (RelativeLayout) findViewById(R.id.slide_header_list);
        RelativeLayout main_content = (RelativeLayout) findViewById(R.id.main_layout);
        RelativeLayout indicator_close_layout = (RelativeLayout) findViewById(R.id.indicator_close_layout);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(ActivityLogActivity.this);
        journal_header_list_recyler_view = (RecyclerView) findViewById(R.id.journal_header_recyler_view);
        journal_header_list_recyler_view.setLayoutManager(layoutManager1);

        list_indicator = (ImageView) findViewById(R.id.indicator);
        Indicator_layout = (RelativeLayout) findViewById(R.id.indicator_layout);
        list_indicator_close = (ImageView) findViewById(R.id.indicator_close);
        list_indicator.setVisibility(View.VISIBLE);
        Indicator_layout.setVisibility(View.VISIBLE);


        list_indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_list_open[0] = true;
                slide_header_list.setVisibility(View.VISIBLE);
                list_indicator.setVisibility(View.GONE);
                Indicator_layout.setVisibility(View.GONE);
                ArrayList<JournalHeaderDataModel> data = activity_log_groups_headerData();
                if (clicked_first_time == 0) {
                    clicked_first_time = 1;
                } else {
                    if (SharedPrefrences.getInt("ACTIVITY_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", ActivityLogActivity.this) != -1) {
                        data.get(SharedPrefrences.getInt("ACTIVITY_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", ActivityLogActivity.this)).setSelected(true);
                    }
                }
                ActivityLogHeaderListRecylerAdapter recyler_adapter = new ActivityLogHeaderListRecylerAdapter(ActivityLogActivity.this, data);
                journal_header_list_recyler_view.setAdapter(recyler_adapter);
                recyler_adapter.setClickListener(ActivityLogActivity.this);
                slide.show();
            }
        });
        indicator_close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_list_open[0] = false;
                slide.hide();
            }
        });
        list_indicator_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_list_open[0] = false;
                slide.hide();
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

        Main_Menu.setOnTouchListener(new OnSwipeTouchListner(ActivityLogActivity.this) {
            public void onSwipeTop() {
                Log.d("bottom to ,", "Top");
            }

            public void onSwipeRight() {
                Log.d("left to ,", "Right");
            }

            public void onSwipeLeft() {
                Log.d("Right to ,", "Left");
            }

            public void onSwipeBottom() {
                Log.d("Top to ,", "Bottom");
                HideKeyboard(ActivityLogActivity.this);
                is_list_open[0] = true;
                slide_header_list.setVisibility(View.VISIBLE);
                list_indicator.setVisibility(View.GONE);
                Indicator_layout.setVisibility(View.GONE);
                ArrayList<JournalHeaderDataModel> data = activity_log_groups_headerData();
                if (clicked_first_time == 0) {

                } else {
                    if (SharedPrefrences.getInt("ACTIVITY_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", ActivityLogActivity.this) != -1) {
                        data.get(SharedPrefrences.getInt("ACTIVITY_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", ActivityLogActivity.this)).setSelected(true);
                    }
                }
                ActivityLogHeaderListRecylerAdapter recyler_adapter = new ActivityLogHeaderListRecylerAdapter(ActivityLogActivity.this, data);
                journal_header_list_recyler_view.setAdapter(recyler_adapter);
                slide.show();

            }

        });
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == get_shout_outs) {
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
                shootOutDataModel.setZip_code(object.optString("zip_code"));
                shootOutDataModel.setPublic_location(object.optString("public_location"));
                shootOutDataModel.setCreated_at(object.getString("created_at"));
                shootOutDataModel.setUpdated_at(object.getString("updated_at"));
                shootOutDataModel.setDistance(String.valueOf(Constants.distance(object.optDouble("lat"), object.optDouble("lng"), Double.parseDouble(SharedPrefrences.getString("lat_cur", this)), Double.parseDouble(SharedPrefrences.getString("lng_cur", this)))));//object.getString("distance")
                shootOutDataModel.setReceived_From(object.getJSONObject("get_sub_user").getString("title"));
                shootOutDataModel.setUser_Image_Path(object.getJSONObject("get_sub_user").getString("logo"));
                shootOutDataModel.setAvatar("");
                if (!object.isNull("budz_special_id"))
                    shootOutDataModel.setBudzSpId(object.getInt("budz_special_id"));
                else {
                    shootOutDataModel.setBudzSpId(-1);
                }
                shootOutDataModel.setSpecial_icon("");
                ShootOutAlertDialog shootOutAlertDialog = ShootOutAlertDialog.newInstance(this, shootOutDataModel);
                shootOutAlertDialog.show(this.getSupportFragmentManager(), "dialog");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            isAppiCallActive = false;
            refresh.setVisibility(View.GONE);
            swipe_rf.setRefreshing(false);
            try {
                JSONObject object = new JSONObject(response);
                JSONArray activity_array = object.getJSONArray("successData");
                if (pages == 0) {
                    filter_activity_list_data.clear();
                    activity_list_data.clear();
                }
                if (activity_array.length() == 0) {
//                filter_activity_list_data.clear();
//                activity_list_data.clear();
                    No_Activity_Found.setVisibility(View.VISIBLE);
                    recyler_adapter.notifyDataSetChanged();
                } else {
                    No_Activity_Found.setVisibility(View.GONE);
                    if (pages == 0) {
                        filter_activity_list_data.clear();
                        activity_list_data.clear();
                    }
                    for (int x = 0; x < activity_array.length(); x++) {
                        JSONObject activity_object = activity_array.getJSONObject(x);
                        if (activity_object.getString("type").length() > 0) {
                            String Activity_description = "";
                            if (activity_object.getString("type").equalsIgnoreCase("Answers")) {
                                Activity_description = "<b><font color=#FFFFFF>Q: </b> </font>" + activity_object.getString("description");
                            } else {
                                Activity_description = activity_object.getString("description");
                            }
                            activity_list_data.add(new SlideMenuActivityListData(activity_object.getString("model"), activity_object.getInt("type_id"), activity_object.getString("text"),
                                    Activity_description, getActivityIcon(activity_object.getString("type")),
                                    getActivityColor(activity_object.getString("type")), convertDate(activity_object.getString("updated_at")), activity_object.getString("type")));

                            filter_activity_list_data.add(new SlideMenuActivityListData(
                                    activity_object.getString("model")
                                    , activity_object.getInt("type_id")
                                    , activity_object.getString("text"),
                                    Activity_description,
                                    getActivityIcon(activity_object.getString("type")),
                                    getActivityColor(activity_object.getString("type")),
                                    convertDate(activity_object.getString("updated_at")),
                                    activity_object.getString("type"))
                            );
                        }
                    }
                    recyler_adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        isAppiCallActive = false;
        refresh.setVisibility(View.GONE);
        swipe_rf.setRefreshing(false);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(ActivityLogActivity.this, object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getActivityIcon(String model) {
        switch (model) {
            case "Questions":
                return R.drawable.ic_question_icon;
            case "Answers":
                return R.drawable.ic_answer_icon;
            case "Budz Map":
            case "Budz Adz":
                return R.drawable.ic_budz_adn;
            case "Likes":
                return R.drawable.ic_thumb_licked;
            case "Favorites":
                return R.drawable.like_heart_blue;
            case "Journal":
                return R.drawable.ic_tab_journal;
            case "Groups":
                return R.drawable.ic_tab_group;
            case "Tags":
                return R.drawable.ic_hashtag;
            case "Strains":
                return R.drawable.ic_tab_strain;
            case "Comment":
                return R.drawable.comment_icon;
            case "Post":
                return R.drawable.social_wall_new;
            default:
                return R.drawable.ic_block_group_icon;
        }
    }

    public String getActivityColor(String model) {
        switch (model) {
            case "Questions":
                return "#0083cb";
            case "Answers":
                return "#0083cb";
            case "Budz Map":
            case "Budz Adz":
                return "#932a88";
            case "Likes":
                return "#0083cb";
            case "Favorites":
                return "#0083cb";
            case "Journal":
                return "#64a81f";
            case "Groups":
                return "#f89224";
            case "Tags":
                return "#67a821";
            case "Strains":
                return "#f3c729";
            case "Comment":
                return "#A3A2A1";
            case "Post":
                return "#8C9097";
            default:
                return "#ff0000";
        }
    }

    @Override
    public void onSlideListItemClick(View view, int position) {
        slide.hide();
        String Type = "";
        switch (position) {
//            case 0:
//                Type = "Questions";
//                break;
            case 0:
                Type = "Answers";
                break;
//            case 3:
//                Type = "SubUser";
//                Type = "Budz Map";
//                break;
            case 2:
                Type = "Likes";
                break;
            case 1:
                Type = "Favorites";
                break;
//            case 5:
//                Type = "Journal";
//                break;
//            case 4:
//                Type = "Groups";
//                break;
//            case 4:
//                Type = "Tags";
//                break;
            case 3:
                Type = "Strains";
                break;
            case 4:
                Type = "Comment";
                break;
            case 5:
                Type = "Post";
                break;
        }
        clicked_first_time = 1;
        SharedPrefrences.setInt("ACTIVITY_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", position, ActivityLogActivity.this);
        /*Questions
Answers
Favorites
Likes
Strains
SubUser*/
        filer = "&filter=" + Type;
        isAppiCallActive = true;
        pages = 0;
        new VollyAPICall(ActivityLogActivity.this, true, filter_activity + "/" + user.getUser_id() + "?skip=" + pages + filer, new JSONObject(), user.getSession_key(), Request.Method.GET, ActivityLogActivity.this, APIActions.ApiActions.get_activities);
//        filter_activity_list_data.clear();
//        for (int x = 0; x < activity_list_data.size(); x++) {
//            if (Type.equalsIgnoreCase(activity_list_data.get(x).getType())) {
//                filter_activity_list_data.add(activity_list_data.get(x));
//            }
//        }
//        if (filter_activity_list_data.size() > 0) {
//            No_Activity_Found.setVisibility(View.GONE);
//        } else {
//            No_Activity_Found.setVisibility(View.VISIBLE);
//        }
//        recyler_adapter.notifyDataSetChanged();


    }

    @Override
    public void onCrossListener(View view, int position) {
        slide.hide();
        filer = "&filter=";
        clicked_first_time = 0;
        SharedPrefrences.setInt("ACTIVITY_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", -1, ActivityLogActivity.this);
        pages = 0;
        isAppiCallActive = true;
        new VollyAPICall(ActivityLogActivity.this, true, filter_activity + "/" + user.getUser_id() + "?skip=" + pages + filer, new JSONObject(), user.getSession_key(), Request.Method.GET, ActivityLogActivity.this, APIActions.ApiActions.get_activities);
    }

    @Override
    public void onItemClick(View view, int position) {
        String type = filter_activity_list_data.get(position).getType();

        switch (type) {
            case "Questions":
                HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                model.setId(filter_activity_list_data.get(position).getType_id());
                showSubFragmentListner_object.ShowQuestions(model, true);
//                finish();
                break;
            case "Answers":
                HomeQAfragmentDataModel model1 = new HomeQAfragmentDataModel();
                model1.setId(filter_activity_list_data.get(position).getType_id());
                showSubFragmentListner_object.ShowAnswers(model1, filter_activity_list_data.get(position).getType_id(), true);
//                finish();
                break;
            case "Budz Map":
            case "Budz Adz":
                Intent budzmap_intetn = new Intent(ActivityLogActivity.this, BudzMapDetailsActivity.class);
                budzmap_intetn.putExtra("budzmap_id", filter_activity_list_data.get(position).getType_id());
                startActivity(budzmap_intetn);
                break;
            case "Likes":
                Model_Intent(position);
                break;
            case "Favorites":
                Model_Intent(position);
                break;
            case "Journal":
                Intent journals_intetn = new Intent(ActivityLogActivity.this, JournalDetailsActivity.class);
                journals_intetn.putExtra("journal_id", filter_activity_list_data.get(position).getType_id());
                startActivity(journals_intetn);
                break;
            case "Groups":
                Intent intent = new Intent(ActivityLogActivity.this, GroupsChatViewActivity.class);
                intent.putExtra("goup_id", filter_activity_list_data.get(position).getType_id());
                startActivity(intent);
                break;
            case "Tags":
                Model_Intent(position);
                break;
            case "Strains":
                Intent strain_intetn = new Intent(ActivityLogActivity.this, StrainDetailsActivity.class);
                strain_intetn.putExtra("strain_id", filter_activity_list_data.get(position).getType_id());
                startActivity(strain_intetn);
                break;
            case "Comment":
            case "Post":
                launchPostDetailActivity(filter_activity_list_data.get(position).getType_id());
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Flags.SESSION_OUT) {
            //do functionality for session out
            return;
        }
    }

    private void launchPostDetailActivity(int postId) {
        Bundle b = new Bundle();
        b.putInt(Constants.POST_ID_EXTRA, postId);
        Utility.launchActivityForResult(this, WallPostDetailActivity.class, b, Flags.ACTIVITIES_COMMUNICATION_FLAG);
        //for fragment
        //Utility.launchActivityForResultFromFragment(YOUR_FRAGMENT.this, getActivity(), b, Flags.ACTIVITIES_COMMUNICATION_FLAG);
    }

    public void Model_Intent(int position) {
        String activty_model = filter_activity_list_data.get(position).getModel();
        if (activty_model.contains("Question")) {
            HomeQAfragmentDataModel model_q = new HomeQAfragmentDataModel();
            model_q.setId(filter_activity_list_data.get(position).getType_id());
            showSubFragmentListner_object.ShowQuestions(model_q, true);
            finish();
        } else if (activty_model.contains("Answers")) {
            HomeQAfragmentDataModel model_a = new HomeQAfragmentDataModel();
            model_a.setId(filter_activity_list_data.get(position).getType_id());
            showSubFragmentListner_object.ShowAnswers(model_a, filter_activity_list_data.get(position).getType_id(), true);
            finish();
        } else if (activty_model.contains("Strains") || activty_model.contains("Strain")) {
            Intent strain_intetn = new Intent(ActivityLogActivity.this, StrainDetailsActivity.class);
            strain_intetn.putExtra("strain_id", filter_activity_list_data.get(position).getType_id());
            startActivity(strain_intetn);
        } else if (activty_model.contains("Groups")) {
            Intent intent = new Intent(ActivityLogActivity.this, GroupsChatViewActivity.class);
            intent.putExtra("goup_id", filter_activity_list_data.get(position).getType_id());
            startActivity(intent);
        } else if (activty_model.contains("Budz Map") || activty_model.contains("Budz Adz") || activty_model.contains("SubUser")) {
            Intent budzmap_intetn = new Intent(ActivityLogActivity.this, BudzMapDetailsActivity.class);
            budzmap_intetn.putExtra("budzmap_id", filter_activity_list_data.get(position).getType_id());
            startActivity(budzmap_intetn);
        } else if (activty_model.contains("Journal")) {
            Intent journals_intetn = new Intent(ActivityLogActivity.this, JournalDetailsActivity.class);
            journals_intetn.putExtra("journal_id", filter_activity_list_data.get(position).getType_id());
            startActivity(journals_intetn);
        } else if (activty_model.contains("ShoutOutLike")) {
            //UserPost
//            GoTo(this, ShootOutActivity.class);
//            Intent shout_specail = new Intent(this, ShootOutActivity.class);
//
//            shout_specail.putExtra("id", filter_activity_list_data.get(position).getType_id());
//            startActivity(shout_specail);
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(this, true, URL.shout_detail + "/" + filter_activity_list_data.get(position).getType_id() + "?lat=" + Double.parseDouble(SharedPrefrences.getString("lat_cur", this)) + "&lng=" + Double.parseDouble(SharedPrefrences.getString("lng_cur", this)), jsonObject, user.getSession_key(), Request.Method.GET, this, get_shout_outs);

//            Intent budzmap_intetn = new Intent(ActivityLogActivity.this, BudzMapDetailsActivity.class);
//            budzmap_intetn.putExtra("budzmap_id", filter_activity_list_data.get(position).getType_id());
//            startActivity(budzmap_intetn);
        } else if (activty_model.contains("UserPost")) {
            launchPostDetailActivity(filter_activity_list_data.get(position).getType_id());

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

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

    @Override
    public void onViewSpecialBtnClink(ShootOutAlertDialog dialog, ShootOutDataModel dataModel) {
        Intent budzmap_intetn = new Intent(getApplicationContext(), BudzMapDetailsActivity.class);
        budzmap_intetn.putExtra("budzmap_id", dataModel.getSub_user_id());
        budzmap_intetn.putExtra("view_specials", true);
        startActivity(budzmap_intetn);
        dialog.dismiss();
    }
}
