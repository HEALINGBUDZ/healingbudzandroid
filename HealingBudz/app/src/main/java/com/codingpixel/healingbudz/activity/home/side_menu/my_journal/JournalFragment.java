package com.codingpixel.healingbudz.activity.home.side_menu.my_journal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.AddJournal;
import com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.JournalDetailsActivity;
import com.codingpixel.healingbudz.DataModel.JournalFragmentDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.MyJournalJournalRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.SimpleToolTip.SimpleTooltip;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_journals;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;

public class JournalFragment extends Fragment implements MyJournalJournalRecylerAdapter.ItemClickListener, APIResponseListner {
    LinearLayout Sort_Btn;
    TextView Sort_Text;
    RecyclerView recyclerView;
    LinearLayout Add_New_Journal;
    int sort_click = 1;
    LinearLayout Refresh;
    ArrayList<JournalFragmentDataModel> get_journal_data = new ArrayList<>();
    MyJournalJournalRecylerAdapter recyler_adapter;
    public static boolean refreshData = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_journal_journal_fragment_layout, container, false);
        recyclerView = view.findViewById(R.id.journal_fragment_recyler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyler_adapter = new MyJournalJournalRecylerAdapter(getContext(), get_journal_data);
        recyclerView.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);

        Sort_Btn = view.findViewById(R.id.public_private_filter);
        Sort_Text = view.findViewById(R.id.sort_text_main);
        Sort_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowToolTipDialog(Sort_Text);
            }
        });

        Add_New_Journal = view.findViewById(R.id.add_new_journal);
        Add_New_Journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshData = true;
                GoTo(getContext(), AddJournal.class);
            }
        });
        Refresh = view.findViewById(R.id.refresh);
        Refresh.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        new VollyAPICall(getContext(), false, URL.get_my_journals_sorting + "?sorting=Newest", object, user.getSession_key(), Request.Method.GET, JournalFragment.this, get_journals);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refreshData) {
            refreshData = false;
            JSONObject object = new JSONObject();
            new VollyAPICall(getContext(), false, URL.get_my_journals_sorting + "?sorting=Newest", object, user.getSession_key(), Request.Method.GET, JournalFragment.this, get_journals);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sort_click = 1;
        if (Sort_Btn != null) {
            Sort_Btn.performClick();
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    public void ShowToolTipDialog(final View v) {
        final SimpleTooltip tooltip = new SimpleTooltip.Builder(getContext())
                .anchorView(v)
                .arrowDrawable(null)
                .text("Sample Text")
                .showArrow(false)
                .gravity(Gravity.BOTTOM)
                .dismissOnOutsideTouch(false)
                .dismissOnInsideTouch(false)
                .modal(true)
                .contentView(R.layout.my_journal_sort_tooltip_dialog_layout)
                .focusable(true)
                .build();

        LinearLayout Newset_First = tooltip.findViewById(R.id.newset_list);
        LinearLayout Alpha_batically = tooltip.findViewById(R.id.alpha_batically);
        LinearLayout No_of_entries = tooltip.findViewById(R.id.no_of_entries);

        final ImageView First_Tick = tooltip.findViewById(R.id.first_tick);
        final ImageView Second_Tick = tooltip.findViewById(R.id.second_tick);
        final ImageView Third_Tick = tooltip.findViewById(R.id.third_tick);

        if (sort_click == 1) {
            First_Tick.setVisibility(View.VISIBLE);
            Second_Tick.setVisibility(View.GONE);
            Third_Tick.setVisibility(View.GONE);
        } else if (sort_click == 2) {
            First_Tick.setVisibility(View.GONE);
            Second_Tick.setVisibility(View.VISIBLE);
            Third_Tick.setVisibility(View.GONE);
        } else if (sort_click == 3) {
            First_Tick.setVisibility(View.GONE);
            Second_Tick.setVisibility(View.GONE);
            Third_Tick.setVisibility(View.VISIBLE);
        }
        Newset_First.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_click = 1;
                First_Tick.setVisibility(View.VISIBLE);
                Second_Tick.setVisibility(View.GONE);
                Third_Tick.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        tooltip.dismiss();
                        Refresh.setVisibility(View.VISIBLE);
                        JSONObject object = new JSONObject();
                        new VollyAPICall(getContext(), false, URL.get_my_journals_sorting + "?sorting=Newest", object, user.getSession_key(), Request.Method.GET, JournalFragment.this, get_journals);

                    }
                }, 300);
            }
        });
        Alpha_batically.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_click = 2;
                First_Tick.setVisibility(View.GONE);
                Second_Tick.setVisibility(View.VISIBLE);
                Third_Tick.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        tooltip.dismiss();
                        Refresh.setVisibility(View.VISIBLE);
                        JSONObject object = new JSONObject();
                        new VollyAPICall(getContext(), false, URL.get_my_journals_sorting + "?sorting=Alphabetical", object, user.getSession_key(), Request.Method.GET, JournalFragment.this, get_journals);

                    }
                }, 300);
            }
        });

        No_of_entries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_click = 3;
                First_Tick.setVisibility(View.GONE);
                Second_Tick.setVisibility(View.GONE);
                Third_Tick.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        tooltip.dismiss();
                        Refresh.setVisibility(View.VISIBLE);
                        JSONObject object = new JSONObject();
                        new VollyAPICall(getContext(), false, URL.get_my_journals_sorting + "?sorting=No_Of_Entries", object, user.getSession_key(), Request.Method.GET, JournalFragment.this, get_journals);

                    }
                }, 300);

            }
        });
        tooltip.show();

    }


    @Override
    public void onItemClick(View view, int position) {
        JournalDetailsActivity.journalFragmentDataModel = get_journal_data.get(position);
        GoTo(getContext(), JournalDetailsActivity.class);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Refresh.setVisibility(View.GONE);
        Log.d("response", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray journal_array = jsonObject.getJSONObject("successData").getJSONArray("journals");
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
                journalFragmentDataModel.setFollow(false);
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

            recyler_adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }
}
