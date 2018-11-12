package com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.JournalFragmentDataModel;
import com.codingpixel.healingbudz.DataModel.JournalHeaderDataModel;
import com.codingpixel.healingbudz.GestureListner.OnSwipeTouchListner;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.JournalHeaderListRecylerAdapter;
import com.codingpixel.healingbudz.adapter.JournalHomeFragmentRecylerAdapter;
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
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_journals;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;
import static com.codingpixel.healingbudz.test_data.JournalHeaderListData.journal_headerData;


public class JournalTabFragment extends Fragment implements JournalHomeFragmentRecylerAdapter.ItemClickListener, APIResponseListner, JournalHeaderListRecylerAdapter.ItemClickListener {
    private SlideUp slide = null;
    RecyclerView Journal_RecylerView;
    SwipeRefreshLayout refreshLayout;
    ImageView Menu;
    int pages = 0;
    boolean isRefreshAble = false;
    ImageView Search_Btn;
    EditText Search_Field;
    boolean isAppiCallActive = false;
    ProgressBar Refresh_layout_progress;
    String Sort_by = "Newest";
    ArrayList<JournalFragmentDataModel> get_journal_data = new ArrayList<>();
    JournalHomeFragmentRecylerAdapter recyler_adapter;
    ImageView Add_New_JOURNAL;
    TextView No_Record_found;
    String keyword = "";
    boolean is_search_keyword = false;

    //Header variables
    RelativeLayout slide_header_list;
    ImageView list_indicator;
    RecyclerView journal_header_list_recyler_view;
    ImageView list_indicator_close;
    boolean[] is_list_open = {false};
    RelativeLayout Indicator_layout;
    RelativeLayout main_content;

    public JournalTabFragment() {
//        super();
    }

    public JournalTabFragment(String key_word, boolean isSearchKeyword) {
        this.keyword = key_word;
        this.is_search_keyword = isSearchKeyword;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.journal_tab_fragment, container, false);
        SharedPrefrences.setInt("JOURNAL_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", 0, getContext());
        view.setClickable(false);
        view.setOnClickListener(null);
        view.setOnTouchListener(null);
        InitHeaderSlideView(view);
        Search_Field = view.findViewById(R.id.search_field);
        Search_Btn = view.findViewById(R.id.search_button);
        Refresh_layout_progress = view.findViewById(R.id.refresh_layout_progress);
        No_Record_found = view.findViewById(R.id.no_journal_found);
        No_Record_found.setVisibility(View.GONE);
        Menu = view.findViewById(R.id.menu_btn);
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
        Journal_RecylerView = view.findViewById(R.id.journal_recyler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        Journal_RecylerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = Journal_RecylerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyler_adapter = new JournalHomeFragmentRecylerAdapter(getContext(), get_journal_data);
        Journal_RecylerView.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(JournalTabFragment.this);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.parseColor("#7bbf44"));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isAppiCallActive = true;
                pages = 0;
//                if (Sort_by.length() == 0) {
//                    Sort_by = "Newest";
//                }
                Search_Field.clearFocus();
                Search_Field.setText("");
                refreshLayout.setRefreshing(true);
                JSONObject object = new JSONObject();
                new VollyAPICall(getContext(), false, URL.get_all_journals + "?skip=" + pages + "&sort_by=" + Sort_by, object, user.getSession_key(), Request.Method.GET, JournalTabFragment.this, get_journals);
            }
        });
        Add_New_JOURNAL = view.findViewById(R.id.add_new_journal);
        Add_New_JOURNAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRefreshAble = true;
                GoTo(getContext(), AddJournal.class);
            }
        });

        if (is_search_keyword) {
            SearchKeyword(keyword);
        } else {
            pages = 0;
            Sort_by = "Newest";
            isAppiCallActive = true;
            refreshLayout.setRefreshing(true);
            JSONObject object = new JSONObject();
            new VollyAPICall(getContext(), false, URL.get_all_journals + "?skip=" + pages + "&sort_by=" + Sort_by, object, user.getSession_key(), Request.Method.GET, JournalTabFragment.this, get_journals);

        }
        Journal_RecylerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(Journal_RecylerView.getLayoutManager());
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                Log.d("vissible", lastVisible + "");
                Log.d("vissible_t", (lastVisible % 10) + "");
                if (lastVisible >= (pages + 1) * 9 && !isAppiCallActive) {
                    if ((lastVisible % 9) == 0) {
                        pages = pages + 1;
                        JSONObject object = new JSONObject();
                        String url = "";
                        Refresh_layout_progress.setVisibility(View.VISIBLE);
                        new VollyAPICall(getContext(), false, URL.get_all_journals + "?skip=" + pages + "&sort_by=" + Sort_by, object, user.getSession_key(), Request.Method.GET, JournalTabFragment.this, get_journals);
                    }
                }
            }
        });

        Search_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Search_Field.getText().length() > 0) {
                    HideKeyboard(getActivity());
                    refreshLayout.setRefreshing(true);
                    pages = 0;
                    JSONObject object = new JSONObject();
                    new VollyAPICall(getContext(), false, URL.search_journal + "?skip=" + 0 + "&title=" + Search_Field.getText().toString(), object, user.getSession_key(), Request.Method.GET, JournalTabFragment.this, get_journals);
                }
            }
        });

        Search_Field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (Search_Field.getText().toString().length() > 1) {
                        HideKeyboard(getActivity());
                        refreshLayout.setRefreshing(true);
                        pages = 0;
                        JSONObject object = new JSONObject();
                        new VollyAPICall(getContext(), false, URL.search_journal + "?skip=" + 0 + "&title=" + Search_Field.getText().toString(), object, user.getSession_key(), Request.Method.GET, JournalTabFragment.this, get_journals);
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
                if (s.toString().isEmpty()) {
                    if (refreshLayout != null) {
                        isAppiCallActive = true;
                        pages = 0;
                        refreshLayout.setRefreshing(true);
                        JSONObject object = new JSONObject();
                        new VollyAPICall(getContext(), false, URL.get_all_journals + "?skip=" + pages + "&sort_by=" + Sort_by, object, user.getSession_key(), Request.Method.GET, JournalTabFragment.this, get_journals);
                    }
                }

            }
        });
        main_content.setOnTouchListener(new OnSwipeTouchListner(getActivity()) {
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

                list_indicator.setVisibility(View.GONE);
                Indicator_layout.setVisibility(View.GONE);
                is_list_open[0] = true;
                slide.show();
                ArrayList<JournalHeaderDataModel> data = journal_headerData();
                data.get(SharedPrefrences.getInt("JOURNAL_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", getContext())).setSelected(true);
                JournalHeaderListRecylerAdapter recyler_adapter = new JournalHeaderListRecylerAdapter(getContext(), data);
                recyler_adapter.setClickListener(JournalTabFragment.this);
                journal_header_list_recyler_view.setAdapter(recyler_adapter);
            }

        });
        return view;
    }

    public void SearchKeyword(String keywrd) {
        get_journal_data.clear();
        recyler_adapter.notifyDataSetChanged();
        HideKeyboard(getActivity());
        refreshLayout.setRefreshing(true);
        pages = 0;
        JSONObject object = new JSONObject();
        new VollyAPICall(getContext(), false, URL.search_journal + "?skip=" + 0 + "&title=" + keywrd, object, user.getSession_key(), Request.Method.GET, JournalTabFragment.this, get_journals);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefreshAble) {
            isRefreshAble = false;
            refreshLayout.setRefreshing(true);
            JSONObject object = new JSONObject();
            new VollyAPICall(getContext(), false, URL.get_all_journals + "?skip=" + pages + "&sort_by=" + Sort_by, object, user.getSession_key(), Request.Method.GET, JournalTabFragment.this, get_journals);
        }
    }

    public void InitHeaderSlideView(View view) {

        slide_header_list = view.findViewById(R.id.slide_header_list);
        main_content = view.findViewById(R.id.top_menu);
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
                list_indicator.setVisibility(View.GONE);
                Indicator_layout.setVisibility(View.GONE);
                is_list_open[0] = true;
                slide.show();
                ArrayList<JournalHeaderDataModel> data = journal_headerData();
                data.get(SharedPrefrences.getInt("JOURNAL_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", getContext())).setSelected(true);
                JournalHeaderListRecylerAdapter recyler_adapter = new JournalHeaderListRecylerAdapter(getContext(), data);
                recyler_adapter.setClickListener(JournalTabFragment.this);
                journal_header_list_recyler_view.setAdapter(recyler_adapter);
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
        if (position >= 0 && position < get_journal_data.size()) {
            isRefreshAble = true;
            JournalDetailsActivity.journalFragmentDataModel = get_journal_data.get(position);
            JournalDetailsActivity.position = position;
            GoTo(getContext(), JournalDetailsActivity.class);
        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        isAppiCallActive = false;
        refreshLayout.setRefreshing(false);
        Refresh_layout_progress.setVisibility(View.GONE);
        Log.d("response", response);
//        if(!Search_Field.getText().toString().isEmpty()){
//            Search_Field.setText("");
//            Search_Field.clearFocus();
//        }
        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray journal_array = jsonObject.getJSONObject("successData").getJSONArray("user_journal");
            if (journal_array.length() > 0) {
                No_Record_found.setVisibility(View.GONE);
                if (pages == 0) {
                    get_journal_data.clear();
                    recyler_adapter.notifyDataSetChanged();
                }
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
                    if (x == 0 && journalEvents.size() > 0 && pages == 0) {
                        journalFragmentDataModel.setSlideOpen(false);
                    } else {
                        journalFragmentDataModel.setSlideOpen(false);
                    }
                    get_journal_data.add(journalFragmentDataModel);
                }
            } else {
                if (pages == 0) {
                    get_journal_data.clear();
                    No_Record_found.setVisibility(View.VISIBLE);
                }
            }
            if(JournalDetailsActivity.position>0){

            }
            recyler_adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        refreshLayout.setRefreshing(false);
        Refresh_layout_progress.setVisibility(View.GONE);
        Log.d("response", response);
    }

    @Override
    public void onSlideItemClick(View view, int position) {
        if (position == 1) {
            Sort_by = "Name";
        } else if (position == 0) {
            Sort_by = "Newest";
        } else if (position == 2) {
            Sort_by = "Favorites";
        }
        refreshLayout.setRefreshing(true);
        pages = 0;
        is_list_open[0] = false;
        slide.hide();
        JSONObject object = new JSONObject();
        new VollyAPICall(getContext(), false, URL.get_all_journals + "?skip=" + pages + "&sort_by=" + Sort_by, object, user.getSession_key(), Request.Method.GET, JournalTabFragment.this, get_journals);
    }
}
