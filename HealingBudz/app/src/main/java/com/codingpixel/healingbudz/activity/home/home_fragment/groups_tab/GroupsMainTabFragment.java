package com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.GroupsDataModel;
import com.codingpixel.healingbudz.DataModel.JournalHeaderDataModel;
import com.codingpixel.healingbudz.GestureListner.OnSwipeTouchListner;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.GroupsHeaderListRecylerAdapter;
import com.codingpixel.healingbudz.adapter.GroupsHomeFragmentRecylerAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.HomeActivity.drawerLayout;
import static com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity.groupsDataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.accept_reject_invitaion;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_groups;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;
import static com.codingpixel.healingbudz.test_data.GroupsHeaderListData.groups_headerData;


public class GroupsMainTabFragment extends Fragment implements GroupsHomeFragmentRecylerAdapter.ItemClickListener, APIResponseListner, GroupsHeaderListRecylerAdapter.ItemClickListener {
    private SlideUp slide = null;
    RecyclerView group_RecylerView;
    SwipeRefreshLayout refreshLayout;
    ImageView Menu;
    int pages = 0;
    ProgressBar Refresh_layout_progress;
    String API_urls = "";
    RelativeLayout Main_Menu;
    ImageView Add_New_Group;
    RelativeLayout slide_header_list;
    ImageView list_indicator;
    ArrayList<GroupsDataModel> group_data = new ArrayList<>();
    TextView No_Record_found;
    RecyclerView journal_header_list_recyler_view;
    ImageView list_indicator_close;
    boolean[] is_list_open = {false};
    RelativeLayout Indicator_layout;
    EditText Search_Field;
    ImageView Search_Btn;
    boolean isResume = false;
    boolean isInviteAcceptdialog = false;
    String NotificaitonData = "";
    String keyword = "";
    boolean is_search_keyword = false;
    ArrayList<JournalHeaderDataModel> data = new ArrayList<>();
    GroupsHomeFragmentRecylerAdapter recyler_adapter;

    public GroupsMainTabFragment() {
    }

    public GroupsMainTabFragment(boolean isInviteAcceptdialog, final String json_data) {
        this.isInviteAcceptdialog = isInviteAcceptdialog;
        this.NotificaitonData = json_data;
    }

    public GroupsMainTabFragment(String key_word, boolean isSearchKeyword) {
        this.keyword = key_word;
        this.is_search_keyword = isSearchKeyword;
    }

    public void ShowInviteNotification(final String data) {
        try {
            LayoutInflater factory = LayoutInflater.from(getContext());
            final View main_dialog = factory.inflate(R.layout.group_invitaion_dialog, null);
            final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.CENTER;
            dialog.setView(main_dialog);
            final Button reject_button = main_dialog.findViewById(R.id.reject_button);
            final Button accept_button = main_dialog.findViewById(R.id.accept_button);
            final TextView text_notification = main_dialog.findViewById(R.id.text_notification);
            text_notification.setText("\"" + new JSONObject(data).getString("admin_name") + "\" send an invitaion to join a new created group \"" + new JSONObject(data).getString("group_name") + "\" , Do you want to accept this invitaion?");
            reject_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("group_id", new JSONObject(data).getString("group_id"));
                        jsonObject.put("is_accepted", 0);
                        new VollyAPICall(getContext(), false, URL.accept_reject_invitaion, jsonObject, user.getSession_key(), Request.Method.POST, GroupsMainTabFragment.this, accept_reject_invitaion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });
            accept_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        int group_id = Integer.parseInt(new JSONObject(data).getString("group_id"));
                        for (int x = 0; x < group_data.size(); x++) {
                            if (group_data.get(x).getId() == group_id) {
                                group_data.get(x).setIs_following_count(1);
                                group_data.get(x).setGet_members_count(group_data.get(x).getGet_members_count() + 1);
                                recyler_adapter.notifyItemChanged(x);
                                break;
                            }
                        }
                        jsonObject.put("group_id", new JSONObject(data).getString("group_id"));
                        jsonObject.put("is_accepted", 1);
                        new VollyAPICall(getContext(), false, URL.accept_reject_invitaion, jsonObject, user.getSession_key(), Request.Method.POST, GroupsMainTabFragment.this, accept_reject_invitaion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.groups_tab_fragment, container, false);
        SharedPrefrences.setInt("GROUPS_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", 0, getContext());
        view.setClickable(false);
        view.setOnClickListener(null);
        view.setOnTouchListener(null);
        InitHeaderSlideView(view);
        Main_Menu = view.findViewById(R.id.top_menu);
        No_Record_found = view.findViewById(R.id.no_group_found);
        Refresh_layout_progress = view.findViewById(R.id.refresh_layout_progress);
        No_Record_found.setVisibility(View.GONE);
        Menu = view.findViewById(R.id.menu_btn);
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
        group_RecylerView = view.findViewById(R.id.journal_recyler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        group_RecylerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = group_RecylerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyler_adapter = new GroupsHomeFragmentRecylerAdapter(getContext(), group_data);
        group_RecylerView.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(GroupsMainTabFragment.this);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.parseColor("#f5921e"));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pages = 0;
                refreshLayout.setRefreshing(true);
                JSONObject object = new JSONObject();
                Search_Field.clearFocus();
                Search_Field.setText("");

//                API_urls = URL.get_all_public_groups;
                if (API_urls == null && API_urls.length() == 0) {
                    API_urls = URL.get_all_public_groups_created;
                } else if (Search_Field.getText().toString().length() == 0 && API_urls != null && API_urls.contains("search_group")) {
                    API_urls = URL.get_all_public_groups_created;
                }

                new VollyAPICall(getContext(), false, API_urls + "?skip=" + 0, object, user.getSession_key(), Request.Method.GET, GroupsMainTabFragment.this, get_groups);
                Log.d("wait", "load");
            }
        });
        group_RecylerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(group_RecylerView.getLayoutManager());
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                Log.d("vissible", lastVisible + "");
                Log.d("vissible_t", (lastVisible % 10) + "");
                if (lastVisible >= (pages + 1) * 9) {
                    if ((lastVisible % 9) == 0) {
                        pages = pages + 1;
                        JSONObject object = new JSONObject();
                        String url = "";
                        if (API_urls.contains("search_group")) {
                            url = API_urls + "?skip=" + pages + "&query=" + Search_Field.getText().toString();
                        } else {
                            url = API_urls + "?skip=" + pages;
                        }
                        Refresh_layout_progress.setVisibility(View.VISIBLE);
//                        new VollyAPICall(getContext(), false, url, object, user.getSession_key(), Request.Method.GET, GroupsMainTabFragment.this, get_groups_with_pagination);
                        new VollyAPICall(getContext(), false, url, object, user.getSession_key(), Request.Method.GET, GroupsMainTabFragment.this, get_groups);
                    }
                }
            }
        });

        Add_New_Group = view.findViewById(R.id.add_new_group);
        Add_New_Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoTo(getContext(), AddNewGroup.class);
            }
        });


        Search_Btn = view.findViewById(R.id.search_button);
        Search_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Search_Field.getText().length() > 0) {
                    HideKeyboard(getActivity());
                    refreshLayout.setRefreshing(true);
                    pages = 0;
                    JSONObject object = new JSONObject();
                    API_urls = URL.search_group;
                    new VollyAPICall(getContext(), false, API_urls + "?skip=" + 0 + "&query=" + Search_Field.getText().toString(), object, user.getSession_key(), Request.Method.GET, GroupsMainTabFragment.this, get_groups);
                }
            }
        });
        Search_Field = view.findViewById(R.id.search_field);

        Search_Field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (Search_Field.getText().toString().length() > 1) {
                        HideKeyboard(getActivity());
                        refreshLayout.setRefreshing(true);
                        pages = 0;
                        JSONObject object = new JSONObject();
                        API_urls = URL.search_group;
                        new VollyAPICall(getContext(), false, API_urls + "?skip=" + 0 + "&query=" + Search_Field.getText().toString(), object, user.getSession_key(), Request.Method.GET, GroupsMainTabFragment.this, get_groups);
                    }
                    return true;
                }
                return false;
            }
        });
        Search_Field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    pages = 0;
                    refreshLayout.setRefreshing(true);
                    JSONObject object = new JSONObject();
                    Search_Field.clearFocus();

//                API_urls = URL.get_all_public_groups;
                    if (API_urls == null && API_urls.length() == 0) {
                        API_urls = URL.get_all_public_groups_created;
                    } else if (Search_Field.getText().toString().length() == 0 && API_urls != null && API_urls.contains("search_group")) {
                        API_urls = URL.get_all_public_groups_created;
                    }

                    new VollyAPICall(getContext(), false, API_urls + "?skip=" + 0, object, user.getSession_key(), Request.Method.GET, GroupsMainTabFragment.this, get_groups);
                    Log.d("wait", "load");
                }

            }
        });
        Main_Menu.setOnTouchListener(new OnSwipeTouchListner(getActivity()) {
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

                is_list_open[0] = true;
                slide_header_list.setVisibility(View.VISIBLE);
                list_indicator.setVisibility(View.GONE);
                Indicator_layout.setVisibility(View.GONE);
                ArrayList<JournalHeaderDataModel> data = groups_headerData();
                data.get(SharedPrefrences.getInt("GROUPS_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", getContext())).setSelected(true);
                GroupsHeaderListRecylerAdapter recyler_adapter = new GroupsHeaderListRecylerAdapter(getContext(), data);
                //recyler_adapter.setClickListener(QA_HomeTabFragment.this);
                journal_header_list_recyler_view.setAdapter(recyler_adapter);
                slide.show();
            }

        });

        if (is_search_keyword) {
            data.clear();
            recyler_adapter.notifyDataSetChanged();
            HideKeyboard(getActivity());
            refreshLayout.setRefreshing(true);
            pages = 0;
            JSONObject object = new JSONObject();
            API_urls = URL.search_group;
            new VollyAPICall(getContext(), false, API_urls + "?skip=" + 0 + "&query=" + keyword, object, user.getSession_key(), Request.Method.GET, GroupsMainTabFragment.this, get_groups);
        } else {
            refreshLayout.setRefreshing(true);
            JSONObject object = new JSONObject();
            pages = 0;
            API_urls = URL.get_all_public_groups_created;
            new VollyAPICall(getContext(), false, API_urls + "?skip=" + 0, object, user.getSession_key(), Request.Method.GET, GroupsMainTabFragment.this, get_groups);

        }
        if (isInviteAcceptdialog) {
            ShowInviteNotification(NotificaitonData);
        }
        return view;
    }

    public void SearchKeyword(String keyword) {
        data.clear();
        recyler_adapter.notifyDataSetChanged();
        HideKeyboard(getActivity());
        refreshLayout.setRefreshing(true);
        pages = 0;
        JSONObject object = new JSONObject();
        API_urls = URL.search_group;
        new VollyAPICall(getContext(), false, API_urls + "?skip=" + 0 + "&query=" + keyword, object, user.getSession_key(), Request.Method.GET, GroupsMainTabFragment.this, get_groups);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResume) {
            isResume = false;
            refreshLayout.setRefreshing(true);
            JSONObject object = new JSONObject();
            pages = 0;
            if (API_urls == null && API_urls.length() == 0) {
                API_urls = URL.get_all_public_groups_created;
            }

            new VollyAPICall(getContext(), false, API_urls + "?skip=" + 0, object, user.getSession_key(), Request.Method.GET, GroupsMainTabFragment.this, get_groups);
        }
    }

    public void InitHeaderSlideView(View view) {

        slide_header_list = view.findViewById(R.id.slide_header_list);

        RelativeLayout main_content = view.findViewById(R.id.main_layout);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        journal_header_list_recyler_view = view.findViewById(R.id.journal_header_recyler_view);
        journal_header_list_recyler_view.setLayoutManager(layoutManager1);
        list_indicator = view.findViewById(R.id.indicator);
        Indicator_layout = view.findViewById(R.id.indicator_layout);
        list_indicator_close = view.findViewById(R.id.indicator_close);
        list_indicator.setVisibility(View.VISIBLE);
        Indicator_layout.setVisibility(View.VISIBLE);

        list_indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_list_open[0] = true;
                slide_header_list.setVisibility(View.VISIBLE);
                list_indicator.setVisibility(View.GONE);
                Indicator_layout.setVisibility(View.GONE);
                data = groups_headerData();
                data.get(SharedPrefrences.getInt("GROUPS_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", getContext())).setSelected(true);
                GroupsHeaderListRecylerAdapter recyler_adapter = new GroupsHeaderListRecylerAdapter(getContext(), data);
                recyler_adapter.setClickListener(GroupsMainTabFragment.this);
                journal_header_list_recyler_view.setAdapter(recyler_adapter);
                slide.show();
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
    }

    @Override
    public void onItemClick(View view, int position) {
        isResume = true;
        groupsDataModel = group_data.get(position);
        GoTo(getContext(), GroupsChatViewActivity.class);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == accept_reject_invitaion) {
            Log.d("response", "Accepted!");
        } else if(apiActions == get_groups){
//            if(!Search_Field.getText().toString().isEmpty()){
//                Search_Field.setText("");
//                Search_Field.clearFocus();
//            }
            refreshLayout.setRefreshing(false);
            Refresh_layout_progress.setVisibility(View.GONE);
//            if (apiActions == get_groups) {
//                No_Record_found.setVisibility(View.GONE);
////                group_data.clear();
//            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("successData");
                if (jsonArray.length() > 0) {
                    if (pages == 0) {
                        group_data.clear();
                        recyler_adapter.notifyDataSetChanged();
                    }
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject groups_object = jsonArray.getJSONObject(x);
                        GroupsDataModel groupsDataModel = new GroupsDataModel();
                        groupsDataModel.setId(groups_object.getInt("id"));
                        groupsDataModel.setUser_id(groups_object.getInt("user_id"));
                        groupsDataModel.setImage(groups_object.getString("image"));
                        groupsDataModel.setName(groups_object.getString("title"));
                        groupsDataModel.setDescription(groups_object.getString("description"));
                        groupsDataModel.setIs_private(groups_object.getInt("is_private"));
                        groupsDataModel.setCreated_at(groups_object.getString("created_at"));
                        groupsDataModel.setUpdated_at(groups_object.getString("updated_at"));
                        groupsDataModel.setIs_admin_count(groups_object.getInt("is_admin_count"));
                        if (groups_object.getInt("is_admin_count") == 1) {
                            groupsDataModel.setCurrentUserAdmin(true);
                        } else {
                            groupsDataModel.setCurrentUserAdmin(false);
                        }
                        groupsDataModel.setGet_members_count(groups_object.getInt("get_members_count"));
                        String tags = "";
                        JSONArray tags_array = groups_object.getJSONArray("tags");
                        for (int y = 0; y < tags_array.length(); y++) {
                            JSONObject tags_object = tags_array.getJSONObject(y);
                            if (y == 0) {
                                tags = tags + tags_object.getJSONObject("get_tag").getString("title");
                            } else {
                                tags = tags + "," + tags_object.getJSONObject("get_tag").getString("title");
                            }
                        }
                        groupsDataModel.setGroup_tags(tags);
                        ArrayList<GroupsDataModel.GroupMembers> groupMembers = new ArrayList<>();
                        JSONArray group_members_array = groups_object.getJSONArray("group_followers");
                        for (int z = 0; z < group_members_array.length(); z++) {
                            JSONObject followers_object = group_members_array.getJSONObject(z);
                            GroupsDataModel.GroupMembers members = new GroupsDataModel.GroupMembers();
                            if (followers_object.getInt("is_admin") == 1) {
                                members.setAdmin(true);
                                groupsDataModel.setGroup_owner(followers_object.getJSONObject("user").getString("first_name"));
                            } else {
                                members.setAdmin(false);
                            }
                            members.setGroup_id(followers_object.getInt("group_id"));
                            members.setUser_id(followers_object.getInt("user_id"));
                            members.setName(followers_object.getJSONObject("user").getString("first_name"));
                            members.setImage_path(followers_object.getJSONObject("user").getString("image_path"));
                            groupMembers.add(members);
                        }
                        groupsDataModel.setIs_following_count(groups_object.getInt("is_following_count"));
                        groupsDataModel.setGroupMembers(groupMembers);
                        group_data.add(groupsDataModel);
                        if (group_data.size() > 1) {
                            recyler_adapter.notifyItemInserted(group_data.size() - 1);
                        } else {
                            recyler_adapter.notifyDataSetChanged();
                        }


                    }
                } else {
                    if (pages == 0) {
                        group_data.clear();
                        No_Record_found.setVisibility(View.VISIBLE);
                    }
                }
                recyler_adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        refreshLayout.setRefreshing(false);
        Refresh_layout_progress.setVisibility(View.GONE);
    }

    @Override
    public void onHeaderListItemClick(View view, int position) {
        is_list_open[0] = false;
        slide.hide();
        pages = 0;
        JSONObject jsonObject = new JSONObject();
        if (position == 1) {
            API_urls = URL.get_all_public_groups_alpha;
        } else if (position == 0) {
            API_urls = URL.get_all_public_groups_created;
        } else if (position == 2) {
            API_urls = URL.get_all_gublic_groups_joined;
        }
        refreshLayout.setRefreshing(true);
        new VollyAPICall(getContext(), false, API_urls + "?skip=" + 0, jsonObject, user.getSession_key(), Request.Method.GET, GroupsMainTabFragment.this, get_groups);

    }
}
