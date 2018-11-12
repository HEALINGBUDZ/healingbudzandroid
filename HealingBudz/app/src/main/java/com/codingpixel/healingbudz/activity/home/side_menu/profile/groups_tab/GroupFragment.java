package com.codingpixel.healingbudz.activity.home.side_menu.profile.groups_tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity;
import com.codingpixel.healingbudz.DataModel.GroupsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.GroupsHomeFragmentRecylerAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_user_groups;

public class GroupFragment extends Fragment implements APIResponseListner, GroupsHomeFragmentRecylerAdapter.ItemClickListener {
    RecyclerView recyclerView ;
    LinearLayout Main_content , Refresh ;
    ArrayList<GroupsDataModel> groups_fragement_Data = new ArrayList<>();
    GroupsHomeFragmentRecylerAdapter recyler_adapter;
    int USER_ID;
    public GroupFragment(int user_id){
        this.USER_ID = user_id;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_profile_group_tab_fragment, container, false);
        Main_content = view.findViewById(R.id.main_contetn);
        Refresh = view.findViewById(R.id.refresh);
        recyclerView = view.findViewById(R.id.recyler_View);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyler_adapter = new GroupsHomeFragmentRecylerAdapter(getContext(), groups_fragement_Data);
        recyclerView.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
        recyclerView.setNestedScrollingEnabled(false);
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(getContext(), false, URL.get_user_groups+"/"+USER_ID, jsonObject, user.getSession_key(), Request.Method.GET, GroupFragment.this, get_user_groups);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response" , response) ;
        Refresh.setVisibility(View.GONE);
        Main_content.setVisibility(View.VISIBLE);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("successData");
            groups_fragement_Data.clear();
            for(int x = 0 ; x < jsonArray.length() ; x ++){
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
                groupsDataModel.setGet_members_count(groups_object.getInt("get_members_count"));
                groupsDataModel.setIs_following_count(1);
                groups_fragement_Data.add(groupsDataModel);
            }
            recyler_adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response" , response) ;
        Refresh.setVisibility(View.GONE);
        Main_content.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext() , GroupsChatViewActivity.class);
        intent.putExtra("goup_id" ,groups_fragement_Data.get(position).getId());
        startActivity(intent);
    }
}
