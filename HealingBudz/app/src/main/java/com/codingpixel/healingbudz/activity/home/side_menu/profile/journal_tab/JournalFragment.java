package com.codingpixel.healingbudz.activity.home.side_menu.profile.journal_tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.JournalDetailsDataModel;
import com.codingpixel.healingbudz.DataModel.JournalDetailsExpandAbleData;
import com.codingpixel.healingbudz.DataModel.JournalFragmentDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.UserProfileQATabRecylerAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_journals;

public class JournalFragment extends Fragment implements APIResponseListner {
    RecyclerView recyclerView;
    LinearLayout Refresh;
    TextView no_record_found;
    ArrayList<JournalFragmentDataModel> get_journal_data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_profile_journal_tab_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyler_View);
        no_record_found = view.findViewById(R.id.no_record_found);
        Refresh = view.findViewById(R.id.refresh);
        Refresh.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        JSONObject object = new JSONObject();
        new VollyAPICall(getContext(), false, URL.get_user_all_journals + "/" + user.getUser_id() + "?skip=" + 0 + "&sort_by=no_of_entries", object, user.getSession_key(), Request.Method.GET, JournalFragment.this, get_journals);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Refresh.setVisibility(View.GONE);
        Log.d("response", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray journal_array = jsonObject.getJSONObject("successData").getJSONArray("user_journal");
            get_journal_data.clear();
            for (int x = 0; x < journal_array.length(); x++) {
                JSONObject journal_object = journal_array.getJSONObject(x);
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

//                if (journal_object.getInt("is_following_count") == 0) {
                    journalFragmentDataModel.setFollow(false);
//                } else {
//                    journalFragmentDataModel.setFollow(true);
//                }

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
                journalFragmentDataModel.setSlideOpen(false);
                get_journal_data.add(journalFragmentDataModel);
            }


            List<JournalDetailsDataModel> dataModels = new ArrayList<>();
            List<JournalDetailsExpandAbleData> my_journals = new ArrayList<>();
            List<JournalDetailsExpandAbleData> follow_journals = new ArrayList<>();
            for (int x = 0; x < get_journal_data.size(); x++) {
                if (get_journal_data.get(x).isFollow()) {
                    JournalDetailsExpandAbleData data = new JournalDetailsExpandAbleData();
                    data.setName(get_journal_data.get(x).getTitle());
                    data.setId(get_journal_data.get(x).getId());
                    data.setFavorite(false);
                    follow_journals.add(data);
                } else {
                    JournalDetailsExpandAbleData data = new JournalDetailsExpandAbleData();
                    data.setName(get_journal_data.get(x).getTitle());
                    data.setId(get_journal_data.get(x).getId());
                    data.setFavorite(true);
                    my_journals.add(data);
                   }
            }
            dataModels.add(new JournalDetailsDataModel("My Journals", my_journals));
            dataModels.add(new JournalDetailsDataModel("Journals I Follow", follow_journals));
            UserProfileQATabRecylerAdapter adapter = new UserProfileQATabRecylerAdapter(dataModels);
            recyclerView.setAdapter(adapter);
//            for (int x = 0; x < adapter.getGroups().size(); x++) {
//                if (!adapter.isGroupExpanded(adapter.getGroups().get(x))) {
//                    adapter.toggleGroup(adapter.getGroups().get(x));
//                }
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }
}
