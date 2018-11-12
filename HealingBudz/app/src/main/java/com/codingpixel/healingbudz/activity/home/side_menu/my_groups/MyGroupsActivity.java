package com.codingpixel.healingbudz.activity.home.side_menu.my_groups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.AddNewGroup;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.CreateGroupSuccessfully;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity;
import com.codingpixel.healingbudz.DataModel.GroupsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.adapter.MyGroupRecylerAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity.groupsDataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_groups;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

public class MyGroupsActivity extends AppCompatActivity implements MyGroupRecylerAdapter.ItemClickListener, APIResponseListner {
    RecyclerView My_groups_recyler_view;
    ImageView Home, Back;
    ImageView Add_New_Group;
    TextView Group_Name;
    public static GroupsDataModel my_groups_activity_DataModel;
    public static boolean isRefreshable = false;
    ArrayList<GroupsDataModel> group_data = new ArrayList<>();
    TextView No_Record_found;
    MyGroupRecylerAdapter recyler_adapter;
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
        setContentView(R.layout.activity_my_groups);
        ChangeStatusBarColor(MyGroupsActivity.this, "#171717");

        No_Record_found = (TextView) findViewById(R.id.no_group_found);
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
                GoToHome(MyGroupsActivity.this, true);
                finish();
            }
        });
        My_groups_recyler_view = (RecyclerView) findViewById(R.id.my_groups_recyler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyGroupsActivity.this);
        My_groups_recyler_view.setLayoutManager(linearLayoutManager);
        recyler_adapter = new MyGroupRecylerAdapter(this, group_data);
        My_groups_recyler_view.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);

        Add_New_Group = (ImageView) findViewById(R.id.add_new_group);
        Add_New_Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoTo(MyGroupsActivity.this, AddNewGroup.class);
            }
        });


        JSONObject object = new JSONObject();
        new VollyAPICall(MyGroupsActivity.this, true, URL.get_my_groups, object, user.getSession_key(), Request.Method.GET, MyGroupsActivity.this, get_groups);

    }


    @Override
    public void onItemClick(View view, int position) {
        groupsDataModel = group_data.get(position);
        GoTo(MyGroupsActivity.this, GroupsChatViewActivity.class);
    }

    @Override
    public void OnMemberClick(View view, int position) {
        CreateGroupSuccessfully.groupsDataModel = group_data.get(position);
        Intent i = new Intent(MyGroupsActivity.this, CreateGroupSuccessfully.class);
        i.putExtra("isOnlyList", true);
        startActivity(i);
    }

    @Override
    public void onEditClickListner(View view, int position) {
        my_groups_activity_DataModel = group_data.get(position);
        Intent i = new Intent(MyGroupsActivity.this, CloseGroupActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRefreshable) {
            isRefreshable = false;
            JSONObject object = new JSONObject();
            new VollyAPICall(MyGroupsActivity.this, true, URL.get_my_groups, object, user.getSession_key(), Request.Method.GET, MyGroupsActivity.this, get_groups);
        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("reasponse", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("successData");
            if (jsonArray.length() > 0) {
                group_data.clear();
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
                    String tags_idss = "";
                    JSONArray tags_array = groups_object.getJSONArray("tags");
                    for (int y = 0; y < tags_array.length(); y++) {
                        JSONObject tags_object = tags_array.getJSONObject(y);
                        if (y == 0) {
                            tags = tags + tags_object.getJSONObject("get_tag").getString("title");
                            tags_idss = tags_idss + tags_object.getJSONObject("get_tag").getString("id");
                        } else {
                            tags = tags + "," + tags_object.getJSONObject("get_tag").getString("title");
                            tags_idss = tags_idss + "," + tags_object.getJSONObject("get_tag").getString("id");
                        }
                    }
                    groupsDataModel.setGroup_tags(tags);
                    groupsDataModel.setGroup_tags_ids(tags_idss);
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
                        groupsDataModel.setGroup_owner(followers_object.getJSONObject("user").getString("first_name"));
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
                if (apiActions == get_groups) {
                    No_Record_found.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("reasponse", response);
    }
}
