package com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.HealingBudGalleryModel;
import com.codingpixel.healingbudz.DataModel.JournalDetailsDataModel;
import com.codingpixel.healingbudz.DataModel.JournalDetailsExpandAbleData;
import com.codingpixel.healingbudz.DataModel.JournalFragmentDataModel;
import com.codingpixel.healingbudz.DataModel.JournalTagsDataModal;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.follow_dialog.FollowerJournalAlertDialog;
import com.codingpixel.healingbudz.adapter.JournalDetailsRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.custome_dialog.save_discussion.SaveDiscussionAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.favourtire_journal;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.follow_journal;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_journal;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_journals_events;
import static com.codingpixel.healingbudz.network.model.URL.sharedBaseUrl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareHBContent;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class JournalDetailsActivity extends AppCompatActivity implements SaveDiscussionAlertDialog.OnDialogFragmentClickListener, FollowerJournalAlertDialog.OnDialogFragmentClickListener, APIResponseListner {
    ImageView Back, Home;
    LinearLayout Add_New_JOURNAL;
    ImageView Favorite;
    SwipeRefreshLayout refreshLayout;
    ImageView Share_it;
    List<JournalDetailsDataModel> get_journal_Details_recyler_Data = new ArrayList<>();
    JournalDetailsRecylerAdapter adapter;
    boolean isFavorite = false;
    boolean isFollow = false;
    public static JournalFragmentDataModel journalFragmentDataModel;
    public static int position = 0;
    LinearLayout publicPrivateFilter;
    boolean isppdialogOpen = false;
    RelativeLayout publicPrivateDialog;
    ImageView Follow_btn;
    LinearLayout Follow_Journal;
    TextView Follow_Journal_text;
    boolean iscollaspexpanddialogOpen = false;
    RelativeLayout expandCollapsDialog;
    boolean isFilterPublic = true;
    ImageView collasp_exp_btn;
    public static boolean isRefreshable = false;
    boolean isEdit = false;
    LinearLayout Public_filter, Private_filter;
    ImageView public_icon, private_icon, public_tick, private_tick, pp_filter_icon, EditJournal;
    TextView public_text, private_text, pp_filter_text;
    RelativeLayout Followers_layout;
    LinearLayout Follower_journal_layout;
    View followers_line;
    LinearLayout expand_list, collaps_list;
    ImageView collaps_icon, expand_icon, collaps_tick, expand_tick;
    TextView collaps_text, expand_text, no_record_found;
    boolean isCollaps = false;
    TextView Journal_Name, Journal_Budz;
    RecyclerView Journal_recyler_view;
    RelativeLayout add_new_event_layout;
    String journal_id = "";
    boolean isJournalDataLoadAble = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_details);
        ChangeStatusBarColor(JournalDetailsActivity.this, "#0a0a0a");
        HideKeyboard(JournalDetailsActivity.this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            journal_id = extras.getInt("journal_id") + "";
            if (journal_id.equalsIgnoreCase("0")) {
                isJournalDataLoadAble = false;
                journal_id = "";
            } else {
                isJournalDataLoadAble = true;
            }
        } else {
            isJournalDataLoadAble = false;
            journal_id = "";
        }
        Follow_btn = (ImageView) findViewById(R.id.follow_journal);
        no_record_found = (TextView) findViewById(R.id.no_record_found);
        add_new_event_layout = (RelativeLayout) findViewById(R.id.filtr);
        Follow_Journal = (LinearLayout) findViewById(R.id.follow_journlaa);
        Follow_Journal_text = (TextView) findViewById(R.id.follow_text);
        Journal_Name = (TextView) findViewById(R.id.journal_name);
        Journal_Budz = (TextView) findViewById(R.id.journal_budz);
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
                GoToHome(JournalDetailsActivity.this, true);
                finish();
            }
        });

        Add_New_JOURNAL = (LinearLayout) findViewById(R.id.add_new_journal);
        Add_New_JOURNAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRefreshable = true;
                GoTo(JournalDetailsActivity.this, AddNewJournalEvent.class);
            }
        });

        Favorite = (ImageView) findViewById(R.id.favorite);
        Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SharedPrefrences.getBool("IS_Journal_My_SAVE_Dialog_Shown", JournalDetailsActivity.this)) {
                    SaveDiscussionAlertDialog saveDiscussionAlertDialog = SaveDiscussionAlertDialog.newInstance("SAVED DISCUSSION", "Journals are saved in the app menu under My Saves", "Got it! Do not show again for Journals I save", JournalDetailsActivity.this);
                    saveDiscussionAlertDialog.show(getSupportFragmentManager(), "dialog");
                }
                JSONObject object = new JSONObject();
                try {
                    object.put("journal_id", journalFragmentDataModel.getId());

                    if (isFavorite) {
                        isFavorite = false;
                        object.put("is_favourtire", 0);
                        Favorite.setImageResource(R.drawable.ic_favorite_border_journal);
                    } else {
                        object.put("is_favourtire", 1);
                        isFavorite = true;
                        Favorite.setImageResource(R.drawable.ic_favorite_journal_heart);
                    }
                    new VollyAPICall(JournalDetailsActivity.this, false, URL.favourtire_journal, object, user.getSession_key(), Request.Method.POST, JournalDetailsActivity.this, favourtire_journal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Follow_Journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("journal_id", journalFragmentDataModel.getId());
                    if (isFollow) {
                        isFollow = false;
                        object.put("is_follow", 0);
                        Follow_Journal_text.setText("Follow");
                        Follow_btn.setImageResource(R.drawable.ic_profile_add);
                        journalFragmentDataModel.setGet_followers_count(journalFragmentDataModel.getGet_followers_count() - 1);
                        Journal_Budz.setText((journalFragmentDataModel.getGet_followers_count()) + " BUDZ");
                        journalFragmentDataModel.setFollow(false);
                    } else {
                        object.put("is_follow", 1);
                        isFollow = true;
                        Follow_Journal_text.setText("Unfollow");
                        Follow_btn.setImageResource(R.drawable.ic_unfollow_journal_btn);
                        journalFragmentDataModel.setGet_followers_count(journalFragmentDataModel.getGet_followers_count() + 1);
                        journalFragmentDataModel.setFollow(true);
                        Journal_Budz.setText((journalFragmentDataModel.getGet_followers_count()) + " BUDZ");
                    }
                    new VollyAPICall(JournalDetailsActivity.this, false, URL.follow_journal, object, user.getSession_key(), Request.Method.POST, JournalDetailsActivity.this, follow_journal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        Share_it = (ImageView) findViewById(R.id.share_it);
        Share_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                try {
                    object.put("msg", "Check out Healing Budz for your smartphone: " + sharedBaseUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareHBContent(JournalDetailsActivity.this, object);
            }
        });

        publicPrivateFilter = (LinearLayout) findViewById(R.id.public_private_filter);
        publicPrivateDialog = (RelativeLayout) findViewById(R.id.public_private_dialog);
        publicPrivateDialog.setClickable(false);
        publicPrivateDialog.setEnabled(false);
        publicPrivateDialog.setOnClickListener(null);
        publicPrivateDialog.setOnTouchListener(null);
        publicPrivateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iscollaspexpanddialogOpen) {
                    iscollaspexpanddialogOpen = false;
                    expandCollapsDialog.setVisibility(View.GONE);
                    Animation startAnimation = AnimationUtils.loadAnimation(JournalDetailsActivity.this, R.anim.fade_out);
                    expandCollapsDialog.startAnimation(startAnimation);
                }
                if (!isppdialogOpen) {
                    isppdialogOpen = true;
                    publicPrivateDialog.setVisibility(View.VISIBLE);
                    Animation startAnimation = AnimationUtils.loadAnimation(JournalDetailsActivity.this, R.anim.fade_in);
                    publicPrivateDialog.startAnimation(startAnimation);
                } else {
                    isppdialogOpen = false;
                    publicPrivateDialog.setVisibility(View.GONE);
                    Animation startAnimation = AnimationUtils.loadAnimation(JournalDetailsActivity.this, R.anim.fade_out);
                    publicPrivateDialog.startAnimation(startAnimation);
                }
            }
        });

        //filter init

        Followers_layout = (RelativeLayout) findViewById(R.id.followers);
        followers_line = findViewById(R.id.followers_line);
        pp_filter_text = (TextView) findViewById(R.id.pp_text_main);
        pp_filter_icon = (ImageView) findViewById(R.id.pp_filter_icon);
        private_icon = (ImageView) findViewById(R.id.private_icon);
        private_text = (TextView) findViewById(R.id.private_text);
        private_tick = (ImageView) findViewById(R.id.private_tick);

        public_icon = (ImageView) findViewById(R.id.public_icon);
        public_text = (TextView) findViewById(R.id.public_text);
        public_tick = (ImageView) findViewById(R.id.public_tick);
        Public_filter = (LinearLayout) findViewById(R.id.filtr_public);
        Public_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                isFilterPublic = true;
                public_tick.setVisibility(View.VISIBLE);
                private_tick.setVisibility(View.GONE);

                public_icon.setImageResource(R.drawable.ic_public_icon_journal_checked);
                public_text.setTextColor(Color.parseColor("#232323"));

                private_text.setTextColor(Color.parseColor("#656666"));
                private_icon.setImageResource(R.drawable.ic_private_icon_journal_unchecked);


                pp_filter_text.setText(public_text.getText().toString());
                pp_filter_icon.setImageResource(R.drawable.ic_public_icon_journal);
                isppdialogOpen = false;
                publicPrivateDialog.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(JournalDetailsActivity.this, R.anim.fade_out);
                publicPrivateDialog.startAnimation(startAnimation);

                Followers_layout.setVisibility(View.VISIBLE);
                followers_line.setVisibility(View.VISIBLE);

                Favorite.setVisibility(View.VISIBLE);
                Share_it.setVisibility(View.VISIBLE);
            }
        });


        Private_filter = (LinearLayout) findViewById(R.id.filtr_private);
        Private_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isFilterPublic = false;

                public_tick.setVisibility(View.GONE);
                private_tick.setVisibility(View.VISIBLE);

                public_icon.setImageResource(R.drawable.ic_public_icon_journal_unchecked);
                public_text.setTextColor(Color.parseColor("#656666"));

                private_text.setTextColor(Color.parseColor("#232323"));
                private_icon.setImageResource(R.drawable.ic_private_icon_journal_checked);


                pp_filter_text.setText(private_text.getText().toString());
                pp_filter_icon.setImageResource(R.drawable.ic_private_icon_journal);
                isppdialogOpen = false;
                publicPrivateDialog.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(JournalDetailsActivity.this, R.anim.fade_out);
                publicPrivateDialog.startAnimation(startAnimation);

                Followers_layout.setVisibility(View.GONE);
                followers_line.setVisibility(View.GONE);

                Favorite.setVisibility(View.GONE);
                Share_it.setVisibility(View.GONE);
            }
        });

        //Expand Collapse Dialog
        collasp_exp_btn = (ImageView) findViewById(R.id.clp_exp);
        expandCollapsDialog = (RelativeLayout) findViewById(R.id.exp_collasp_dialog);
        expandCollapsDialog.setClickable(false);
        expandCollapsDialog.setEnabled(false);
        expandCollapsDialog.setOnClickListener(null);
        expandCollapsDialog.setOnTouchListener(null);

        collasp_exp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isppdialogOpen) {
                    isppdialogOpen = false;
                    publicPrivateDialog.setVisibility(View.GONE);
                    Animation startAnimation = AnimationUtils.loadAnimation(JournalDetailsActivity.this, R.anim.fade_out);
                    publicPrivateDialog.startAnimation(startAnimation);
                }

                if (!iscollaspexpanddialogOpen) {
                    iscollaspexpanddialogOpen = true;
                    expandCollapsDialog.setVisibility(View.VISIBLE);
                    Animation startAnimation = AnimationUtils.loadAnimation(JournalDetailsActivity.this, R.anim.fade_in);
                    expandCollapsDialog.startAnimation(startAnimation);
                } else {
                    iscollaspexpanddialogOpen = false;
                    expandCollapsDialog.setVisibility(View.GONE);
                    Animation startAnimation = AnimationUtils.loadAnimation(JournalDetailsActivity.this, R.anim.fade_out);
                    expandCollapsDialog.startAnimation(startAnimation);
                }
            }
        });

        collaps_icon = (ImageView) findViewById(R.id.collasp_icon);
        collaps_text = (TextView) findViewById(R.id.collasp_text);
        collaps_tick = (ImageView) findViewById(R.id.collasp_tick);


        expand_icon = (ImageView) findViewById(R.id.expand_icon);
        expand_text = (TextView) findViewById(R.id.expand_text);
        expand_tick = (ImageView) findViewById(R.id.expand_tick);

        collaps_list = (LinearLayout) findViewById(R.id.collasp_all);
        collaps_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iscollaspexpanddialogOpen = false;

                expand_tick.setVisibility(View.GONE);
                collaps_tick.setVisibility(View.VISIBLE);

                collaps_icon.setImageResource(R.drawable.ic_collapse_checked);
                collaps_text.setTextColor(Color.parseColor("#232323"));

                expand_text.setTextColor(Color.parseColor("#656666"));
                expand_icon.setImageResource(R.drawable.ic_expand_unchecked);

                expandCollapsDialog.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(JournalDetailsActivity.this, R.anim.fade_out);
                expandCollapsDialog.startAnimation(startAnimation);

                for (int x = 0; x < adapter.getGroups().size(); x++) {
                    if (adapter.isGroupExpanded(adapter.getGroups().get(x))) {
                        adapter.toggleGroup(adapter.getGroups().get(x));
                    }
                }
            }
        });
        expand_list = (LinearLayout) findViewById(R.id.expand_all);
        expand_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iscollaspexpanddialogOpen = false;
                expand_tick.setVisibility(View.VISIBLE);
                collaps_tick.setVisibility(View.GONE);

                collaps_icon.setImageResource(R.drawable.ic_collapse_unchecked);
                collaps_text.setTextColor(Color.parseColor("#656666"));

                expand_text.setTextColor(Color.parseColor("#232323"));
                expand_icon.setImageResource(R.drawable.ic_expand_checked);

                expandCollapsDialog.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(JournalDetailsActivity.this, R.anim.fade_out);
                expandCollapsDialog.startAnimation(startAnimation);
                for (int x = 0; x < adapter.getGroups().size(); x++) {
                    if (!adapter.isGroupExpanded(adapter.getGroups().get(x))) {
                        adapter.toggleGroup(adapter.getGroups().get(x));
                    }
                }
            }
        });

        Follower_journal_layout = (LinearLayout) findViewById(R.id.follower_journal_layout);
//        Follower_journal_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FollowerJournalAlertDialog followerJournalAlertDialog = FollowerJournalAlertDialog.newInstance(JournalDetailsActivity.this);
//                followerJournalAlertDialog.show(getSupportFragmentManager(), "dialog");
//            }
//        });
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.parseColor("#7bbf44"));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                JSONObject object = new JSONObject();
                new VollyAPICall(JournalDetailsActivity.this, false, URL.get_journal_events + "/" + journalFragmentDataModel.getId() + "?skip=0", object, user.getSession_key(), Request.Method.GET, JournalDetailsActivity.this, get_journals_events);
            }
        });

        EditJournal = (ImageView) findViewById(R.id.edit_journal);
        EditJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                GoTo(JournalDetailsActivity.this, EditJournal.class);
            }
        });

        if (isJournalDataLoadAble) {
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(JournalDetailsActivity.this, true, URL.get_journal + "/" + journal_id, jsonObject, user.getSession_key(), Request.Method.GET, JournalDetailsActivity.this, get_journal);
        } else {
            SetData();
        }
    }

    public void SetData() {
        refreshLayout.setRefreshing(true);
        JSONObject object = new JSONObject();
        new VollyAPICall(JournalDetailsActivity.this, false, URL.get_journal_events + "/" + journalFragmentDataModel.getId() + "?skip=0", object, user.getSession_key(), Request.Method.GET, JournalDetailsActivity.this, get_journals_events);
        Journal_Name.setText(journalFragmentDataModel.getTitle());
        Journal_Budz.setText(journalFragmentDataModel.getGet_followers_count() + " BUDZ");
        if (journalFragmentDataModel.isFavorite()) {
            isFavorite = true;
            Favorite.setImageResource(R.drawable.ic_favorite_journal_heart);
        } else {
            isFavorite = false;
            Favorite.setImageResource(R.drawable.ic_favorite_border_journal);
        }
        if (journalFragmentDataModel.getUser_id() == user.getUser_id()) {
            Follow_Journal.setVisibility(View.GONE);
            Add_New_JOURNAL.setClickable(true);
            Add_New_JOURNAL.setEnabled(true);
            Add_New_JOURNAL.setBackgroundResource(R.drawable.add_journal_entry_btn);
            Follower_journal_layout.setVisibility(View.VISIBLE);
        } else {
            EditJournal.setVisibility(View.GONE);
            Follower_journal_layout.setVisibility(View.GONE);
            Follower_journal_layout.setOnClickListener(null);
            Follow_Journal.setVisibility(View.VISIBLE);
            Add_New_JOURNAL.setClickable(false);
            Add_New_JOURNAL.setEnabled(false);
            Add_New_JOURNAL.setBackgroundResource(R.drawable.q_a_menu_ask_q_button_inprogress);
        }
        isFollow = journalFragmentDataModel.isFollow();
        if (isFollow) {
            Follow_Journal_text.setText("Unfollow");
            Follow_btn.setImageResource(R.drawable.ic_unfollow_journal_btn);
        } else {
            Follow_Journal_text.setText("Follow");
            Follow_btn.setImageResource(R.drawable.ic_profile_add);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRefreshable) {
            isRefreshable = false;
            refreshLayout.setRefreshing(true);
            JSONObject object = new JSONObject();
            new VollyAPICall(JournalDetailsActivity.this, false, URL.get_journal_events + "/" + journalFragmentDataModel.getId() + "?skip=0", object, user.getSession_key(), Request.Method.GET, JournalDetailsActivity.this, get_journals_events);
        }
        if (isEdit) {
            isEdit = false;
            Journal_Name.setText(journalFragmentDataModel.getTitle());
        }
    }

    @Override
    public void onOkClicked(SaveDiscussionAlertDialog dialog) {
        dialog.dismiss();
        SharedPrefrences.setBool("IS_Journal_My_SAVE_Dialog_Shown", true, JournalDetailsActivity.this);
    }

    @Override
    public void onCancelClicked(SaveDiscussionAlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onCrossBtnClink(FollowerJournalAlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        refreshLayout.setRefreshing(false);
        if (apiActions == favourtire_journal) {
            Log.d("response", response);
        } else if (apiActions == follow_journal) {
            Log.d("response", response);
        } else if (apiActions == get_journal) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject journal_object = jsonObject.getJSONObject("successData").getJSONObject("user_journal");
                JournalFragmentDataModel journalFragmentDataModel = new JournalFragmentDataModel();
                journalFragmentDataModel.setId(journal_object.getInt("id"));
                journalFragmentDataModel.setTitle(journal_object.getString("title"));
                journalFragmentDataModel.setIs_public(journal_object.getInt("is_public"));
                journalFragmentDataModel.setCreated_at(journal_object.getString("created_at"));
                journalFragmentDataModel.setUpdated_at(journal_object.getString("updated_at"));
                journalFragmentDataModel.setGet_tags_count(journal_object.getInt("get_tags_count"));
                journalFragmentDataModel.setGet_followers_count(journal_object.getInt("get_followers_count"));
                journalFragmentDataModel.setEvents_count(journal_object.getInt("events_count"));
                journalFragmentDataModel.setUser_first_name(journal_object.getJSONObject("get_user").getString("first_name"));
                journalFragmentDataModel.setUser_id(journal_object.getJSONObject("get_user").getInt("id"));
                journalFragmentDataModel.setUser_image_path(journal_object.getJSONObject("get_user").getString("image_path"));
                journalFragmentDataModel.setAvatar(journal_object.getJSONObject("get_user").getString("avatar"));
                journalFragmentDataModel.setFavorite(false);
                if (journal_object.getInt("get_user_favorites_count") == 0) {
                    journalFragmentDataModel.setFavorite(false);
                } else {
                    journalFragmentDataModel.setFavorite(true);
                }

                if (journal_object.getInt("is_following_count") == 0) {
                    journalFragmentDataModel.setFollow(false);
                } else {
                    journalFragmentDataModel.setFollow(true);
                }

                JSONArray events_array = journal_object.getJSONArray("latest_events");
                ArrayList<JournalFragmentDataModel.JournalEvent> journalEvents = new ArrayList<>();
                for (int y = 0; y < events_array.length(); y++) {
                    JSONObject event_object = events_array.getJSONObject(y);
                    JournalFragmentDataModel.JournalEvent event = new JournalFragmentDataModel.JournalEvent();
                    event.setEvent_Title(event_object.getString("title"));
                    event.setEvent_Date(event_object.getString("created_at"));
                    event.setFeeling(event_object.getString("feeling"));
                    journalEvents.add(event);
                }
                journalFragmentDataModel.setJournalEvents(journalEvents);
                journalFragmentDataModel.setSlideOpen(true);
                this.journalFragmentDataModel = journalFragmentDataModel;
                SetData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                get_journal_Details_recyler_Data.clear();
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.optJSONObject("successData") != null) {
                    JSONArray events_array = jsonObject.getJSONObject("successData").getJSONArray("journal_events");
                    List<JournalDetailsExpandAbleData> su = new ArrayList<>();
                    String Month_name = "";
                    for (int x = 0; x < events_array.length(); x++) {
                        JSONObject events_obect = events_array.getJSONObject(x);
                        if (x == 0) {
                            Month_name = events_obect.getString("month_group");
                        }
                        JournalDetailsExpandAbleData journalDetailsExpandAbleData = new JournalDetailsExpandAbleData();
                        journalDetailsExpandAbleData.setId(events_obect.getInt("id"));
                        journalDetailsExpandAbleData.setUser_id(events_obect.getInt("user_id"));
                        journalDetailsExpandAbleData.setJournal_id(events_obect.getInt("journal_id"));
                        journalDetailsExpandAbleData.setTitle(events_obect.getString("title"));
                        journalDetailsExpandAbleData.setName(events_obect.getString("title"));
                        journalDetailsExpandAbleData.setDate(events_obect.getString("date"));
                        journalDetailsExpandAbleData.setFeeling(events_obect.getString("feeling"));
                        journalDetailsExpandAbleData.setDescription(events_obect.getString("description"));
                        journalDetailsExpandAbleData.setCreated_at(events_obect.getString("created_at"));
                        journalDetailsExpandAbleData.setNew_date(events_obect.getString("new_date"));
                        journalDetailsExpandAbleData.setYear(events_obect.getInt("year"));
                        journalDetailsExpandAbleData.setMonth(events_obect.getInt("month"));
                        if (!events_obect.isNull("get_strain")) {
                            journalDetailsExpandAbleData.setStrain_id(events_obect.optInt("strain_id"));
                            journalDetailsExpandAbleData.setStarin_detail(events_obect.getJSONObject("get_strain").getString("overview"));
                            journalDetailsExpandAbleData.setStrain_title(events_obect.getJSONObject("get_strain").getString("title"));

                        } else {
                            journalDetailsExpandAbleData.setStrain_id(0);
                            journalDetailsExpandAbleData.setStarin_detail("");
                            journalDetailsExpandAbleData.setStrain_title("");

                        }
                        JSONArray tags_array = events_obect.getJSONArray("get_tags");
                        ArrayList<JournalTagsDataModal> tags_list = new ArrayList<>();
                        for (int tag = 0; tag < tags_array.length(); tag++) {
                            JSONObject tags_object = tags_array.getJSONObject(tag);
                            JournalTagsDataModal journalTagsDataModal = new JournalTagsDataModal();
                            journalTagsDataModal.setId(tags_object.getJSONObject("tag_detail").getInt("id"));
                            journalTagsDataModal.setTitle(tags_object.getJSONObject("tag_detail").getString("title"));
                            journalTagsDataModal.setIs_approved(tags_object.getJSONObject("tag_detail").getInt("is_approved"));
                            journalTagsDataModal.setCreated_at(tags_object.getJSONObject("tag_detail").getString("created_at"));
                            journalTagsDataModal.setUpdated_at(tags_object.getJSONObject("tag_detail").getString("updated_at"));
                            tags_list.add(journalTagsDataModal);
                        }
                        journalDetailsExpandAbleData.setTags(tags_list);

                        JSONArray get_image_attachments = events_obect.getJSONArray("get_image_attachments");
                        ArrayList<String> image_attachments = new ArrayList<>();

                        for (int img = 0; img < get_image_attachments.length(); img++) {
                            JSONObject tags_object = get_image_attachments.getJSONObject(img);
                            image_attachments.add(tags_object.getString("attachment_path").replace(" ", ""));
                        }
                        journalDetailsExpandAbleData.setGet_image_attachments(image_attachments);


                        JSONArray get_video_attachments = events_obect.getJSONArray("get_video_attachments");
                        ArrayList<HealingBudGalleryModel> video_attachments = new ArrayList<>();

                        for (int img = 0; img < get_video_attachments.length(); img++) {
                            JSONObject video_object = get_video_attachments.getJSONObject(img);
                            HealingBudGalleryModel video_model = new HealingBudGalleryModel();
                            video_model.setPath(video_object.getString("attachment_path"));
                            video_model.setPoster(video_object.getString("poster"));
                            video_model.setType("video");
                            video_attachments.add(video_model);
                        }
                        journalDetailsExpandAbleData.setGet_video_attachments(video_attachments);
                        journalDetailsExpandAbleData.setGet_likes_count(events_obect.getJSONArray("get_likes").length());
                        journalDetailsExpandAbleData.setGet_dis_likes_count(events_obect.getJSONArray("get_dis_likes").length());

                        if (events_obect.getInt("is_liked_count") == 0) {
                            journalDetailsExpandAbleData.setCurrentUserLike(false);
                        } else {
                            journalDetailsExpandAbleData.setCurrentUserLike(true);
                        }

                        if (events_obect.getInt("is_dis_liked_count") == 0) {
                            journalDetailsExpandAbleData.setCurrentUserDisLike(false);
                        } else {
                            journalDetailsExpandAbleData.setCurrentUserDisLike(true);
                        }

                        if (Month_name.equalsIgnoreCase(events_obect.getString("month_group"))) {
                            su.add(journalDetailsExpandAbleData);
                        } else {
                            get_journal_Details_recyler_Data.add(new JournalDetailsDataModel(Month_name, su));
                            su = new ArrayList<>();
                            su.add(journalDetailsExpandAbleData);
                            Month_name = events_obect.getString("month_group");
                        }
                    }
                    get_journal_Details_recyler_Data.add(new JournalDetailsDataModel(Month_name, su));

                    adapter = null;
                    Journal_recyler_view = (RecyclerView) findViewById(R.id.journal_detail_recyler_view);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    RecyclerView.ItemAnimator animator = Journal_recyler_view.getItemAnimator();
                    if (animator instanceof DefaultItemAnimator) {
                        ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
                    }
                    adapter = new JournalDetailsRecylerAdapter(get_journal_Details_recyler_Data);
                    Journal_recyler_view.setLayoutManager(layoutManager);
                    Journal_recyler_view.setAdapter(adapter);
                }
                if (get_journal_Details_recyler_Data.size() == 0) {
                    no_record_found.setVisibility(View.VISIBLE);
                } else {
                    no_record_found.setVisibility(View.GONE);
                }
//                if (adapter.getGroups().size() > 0) {
//                    new Handler().postDelayed(new Runnable() {
//                        public void run() {
//                            if (!adapter.isGroupExpanded(0)) {
//                                adapter.toggleGroup(0);
//                            }
//                        }
//                    }, 0);
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        refreshLayout.setRefreshing(false);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
