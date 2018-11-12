package com.codingpixel.healingbudz.activity.home.side_menu.my_journal;

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
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.MyJournalTagFragmentTagRecylerAdapter;
import com.codingpixel.healingbudz.adapter.MyJournalTagJournalRecylerAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_journals;

public class TagsFragment extends Fragment implements APIResponseListner {
    RecyclerView Select_Tag_recylerview;
    RecyclerView Journal_recyler_view;
    LinearLayout Refresh;
    List<TagsData> tag_data = new ArrayList<>();
    MyJournalTagFragmentTagRecylerAdapter addNewJournalSelectTagRecylerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_journal_tags_fragment_layout, container, false);


        addNewJournalSelectTagRecylerAdapter = new MyJournalTagFragmentTagRecylerAdapter(getContext(), tag_data);
        Select_Tag_recylerview = view.findViewById(R.id.tags_recyler_view);
        FlexboxLayoutManager layoutManager_1 = new FlexboxLayoutManager(getContext());
        layoutManager_1.setFlexDirection(FlexDirection.ROW);
        layoutManager_1.setJustifyContent(JustifyContent.FLEX_START);
        Select_Tag_recylerview.setLayoutManager(layoutManager_1);
        Select_Tag_recylerview.setAdapter(addNewJournalSelectTagRecylerAdapter);





        Journal_recyler_view = view.findViewById(R.id.journal_list_recyler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        Journal_recyler_view.setLayoutManager(layoutManager);
        MyJournalTagJournalRecylerAdapter recyler_adapter = new MyJournalTagJournalRecylerAdapter(getContext());
        Journal_recyler_view.setAdapter(recyler_adapter);

        Refresh = view.findViewById(R.id.refresh);
        Refresh.setVisibility(View.VISIBLE);

        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(getContext(), false, URL.get_journal_tags, jsonObject, user.getSession_key(), Request.Method.GET, TagsFragment.this, get_journals);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Refresh.setVisibility(View.GONE);
        Log.d("Response", response);

        try {
            JSONArray jsonObject = new JSONObject(response).getJSONObject("successData").getJSONArray("journals_tags");
            tag_data.clear();
            for(int x =0 ; x < jsonObject.length(); x++){
                JSONObject object = jsonObject.getJSONObject(x);
                TagsData data = new TagsData();
                data.setTitle(object.getString("title"));
                data.setCount(object.getInt("get_tag_count"));
                tag_data.add(data);
            }
            addNewJournalSelectTagRecylerAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Refresh.setVisibility(View.GONE);
        Log.d("Response", response);
    }

    public class TagsData {
        int count;
        String title;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
