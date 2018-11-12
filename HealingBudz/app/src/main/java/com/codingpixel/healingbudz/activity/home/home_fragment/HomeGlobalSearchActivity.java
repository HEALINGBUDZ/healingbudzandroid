package com.codingpixel.healingbudz.activity.home.home_fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.DataModel.HomeSearchListData;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.JournalDetailsActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.HomeSearchListRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.APPSwitch;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.home.HomeActivity.SetIcon;
import static com.codingpixel.healingbudz.activity.home.HomeActivity.setColor;
import static com.codingpixel.healingbudz.activity.home.HomeActivity.showSubFragmentListner_object;
import static com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.DiscussQuestionFragment.isNewScreen;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.global_search;
import static com.codingpixel.healingbudz.network.model.URL.globle_search;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class HomeGlobalSearchActivity extends AppCompatActivity implements View.OnClickListener, APIResponseListner, HomeSearchListRecylerAdapter.ItemClickListener {
    ImageView back, home, gloobal_search_btn_click, Home_search_filter_layout_close_btn;
    EditText Home_search_editText;
    LinearLayout Home_search_main_content_list, Home_search_filter_button;
    RelativeLayout Home_search_Header, Home_search_filter_layout;
    private boolean isAppiCallActive = false;
    int pages = 0;
    RecyclerView Home_search_recyler_view;
    TextView not_found;
    ImageView cross_icon;
    HomeSearchListRecylerAdapter global_Search_recyler_adapter;

    List<HomeSearchListData> home_search_list_data = new ArrayList<>();
    String filter = "q,a,s,bm,u";
    APPSwitch global_search_check_box_qa, global_search_check_box_user, global_search_check_box_groups, global_search_check_box_journals, global_search_check_box_strains, global_search_check_box_budzmap;
    boolean isQa = false, isGroup = false, isJour = false, isStrain = false, isBudz = false;
    boolean isUa = false, isUaOff = false, isQaOff = false, isGroupOff = false, isJourOff = false, isStrainOff = false, isBudzOff = false;
    private SlideUp home_search_filter_slide;
    //    private SwipeRefreshLayout refresher;
    private LinearLayout global_search_refresh;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_global_search);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        intVIew();
        intlistener();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("follow_bud") && bundle.getBoolean("follow_bud")) {
                global_search_refresh.setVisibility(View.VISIBLE);
                JSONObject jsonObject = new JSONObject();
//                        global_search_refresh.setVisibility(View.VISIBLE);

                new VollyAPICall(HomeGlobalSearchActivity.this
                        , false, globle_search + "?query=" + Home_search_editText.getText().toString() + "&zip_code=" + user.getZip_code() + "&filter=" + "u" + "&state=" + "" + "&skip=" + pages
                        , jsonObject, user.getSession_key()
                        , Request.Method.GET
                        , HomeGlobalSearchActivity.this
                        , global_search);
            }
        }
    }

    private void intlistener() {
        gloobal_search_btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Home_search_editText.getText().toString().trim().length() == 0) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                InputMethodManager imm = (InputMethodManager) Home_search_editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInputFromWindow(Home_search_editText.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                                Home_search_editText.requestFocus();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 300);
                } else {
                    Home_search_editText.setText("");
                    home_search_list_data.clear();
                    global_Search_recyler_adapter.notifyDataSetChanged();
                    if (home_search_list_data.size() == 0) {
                        not_found.setVisibility(View.VISIBLE);
                    } else {
                        not_found.setVisibility(View.GONE);
                    }
                }
                //                if (Home_search_editText.getText().toString().trim().length() > 0) {
//                    HideKeyboard(HomeGlobalSearchActivity.this);
//                    global_search_refresh.setVisibility(View.VISIBLE);
//                    JSONObject jsonObject = new JSONObject();
//                    isAppiCallActive = true;
//                    pages = 0;
//                    new VollyAPICall(HomeGlobalSearchActivity.this
//                            , false
//                            , globle_search + "?query=" + Home_search_editText.getText().toString() + "&zip_code=" + user.getZip_code() + "&state=" + "" + "&skip=" + pages + "&filter=" + filter
//                            , jsonObject, user.getSession_key()
//                            , Request.Method.GET
//                            , HomeGlobalSearchActivity.this
//                            , global_search);
//                } else {
//                    CustomeToast.ShowCustomToast(HomeGlobalSearchActivity.this, "Please enter search text!", Gravity.TOP);
//                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void intVIew() {
        global_search_refresh = (LinearLayout) findViewById(R.id.global_search_refresh);
        not_found = findViewById(R.id.not_found);
        cross_icon = findViewById(R.id.cross_icon);
        Home_search_filter_layout = (RelativeLayout) findViewById(R.id.home_search_filter_layout);
        Home_search_filter_button = (LinearLayout) findViewById(R.id.home_search_filter_button_click);
        Home_search_filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home_search_filter_button.setVisibility(View.GONE);
                home_search_filter_slide.show();
            }
        });
        back = findViewById(R.id.back_btn);
        home = findViewById(R.id.home_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToHome(v.getContext(), true);
            }
        });
        gloobal_search_btn_click = findViewById(R.id.gloobal_search_btn_click);
        Home_search_editText = (EditText) findViewById(R.id.home_search_edit_text);
        Home_search_main_content_list = (LinearLayout) findViewById(R.id.home_search_list_view);
        Home_search_Header = (RelativeLayout) findViewById(R.id.home_search_header);
        Home_search_editText.setOnClickListener(this);
        Home_search_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.toString().trim().length() > 0) {
                    gloobal_search_btn_click.setImageResource(R.drawable.ic_cross_black);
                    cross_icon.setVisibility(View.GONE);
                    global_search_refresh.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObject = new JSONObject();
                            isAppiCallActive = true;
                            pages = 0;
                            new VollyAPICall(HomeGlobalSearchActivity.this
                                    , false
                                    , globle_search + "?query=" + s.toString() + "&zip_code=" + user.getZip_code() + "&state=" + "" + "&skip=" + pages + "&filter=" + filter
                                    , jsonObject, user.getSession_key()
                                    , Request.Method.GET
                                    , HomeGlobalSearchActivity.this
                                    , global_search);
                        }
                    }, 200);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObject = new JSONObject();
                            new VollyAPICall(HomeGlobalSearchActivity.this
                                    , false
                                    , globle_search + "?query=" + "" + "&zip_code=" + user.getZip_code() + "&state=" + "" + "&skip=" + pages + "&filter=" + filter
                                    , jsonObject, user.getSession_key()
                                    , Request.Method.GET
                                    , HomeGlobalSearchActivity.this
                                    , global_search);
                        }
                    }, 200);
                }
            }
        });
        cross_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home_search_editText.setText("");
            }
        });
        Home_search_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (Home_search_editText.getText().toString().trim().length() > 0) {
                        HideKeyboard(HomeGlobalSearchActivity.this);
                        global_search_refresh.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = new JSONObject();
                        isAppiCallActive = true;
                        pages = 0;
                        new VollyAPICall(HomeGlobalSearchActivity.this
                                , false
                                , globle_search + "?query=" + Home_search_editText.getText().toString() + "&zip_code=" + user.getZip_code() + "&state=" + "" + "&skip=" + pages + "&filter=" + filter
                                , jsonObject, user.getSession_key()
                                , Request.Method.GET
                                , HomeGlobalSearchActivity.this
                                , global_search);
                    } else {
                        CustomeToast.ShowCustomToast(HomeGlobalSearchActivity.this, "Please enter search text!", Gravity.TOP);
                    }
                    return true;
                }
                return false;
            }
        });
        global_search_check_box_qa = (APPSwitch) findViewById(R.id.global_search_check_box_qa);
        global_search_check_box_user = (APPSwitch) findViewById(R.id.global_search_check_box_user);
        global_search_check_box_groups = (APPSwitch) findViewById(R.id.global_search_check_box_groups);
        global_search_check_box_journals = (APPSwitch) findViewById(R.id.global_search_check_box_journals);
        global_search_check_box_strains = (APPSwitch) findViewById(R.id.global_search_check_box_strains);
        global_search_check_box_budzmap = (APPSwitch) findViewById(R.id.global_search_check_box_budzmap);
        global_search_check_box_qa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isQa = true;
                } else {
                    isQaOff = true;
                }
                callSearchApi();
            }
        });
        global_search_check_box_user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isUa = true;
                } else {
                    isUaOff = true;
                }
                callSearchApi();
            }
        });
        global_search_check_box_budzmap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isBudz = true;
                } else {
                    isBudzOff = true;
                }
                callSearchApi();
            }
        });
        global_search_check_box_strains.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isStrain = true;
                } else {
                    isStrainOff = true;
                }
                callSearchApi();
            }
        });
        Home_search_Header.setVisibility(View.VISIBLE);
        Home_search_main_content_list.setVisibility(View.VISIBLE);
        home_search_filter_slide = new SlideUpBuilder(Home_search_filter_layout)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                            Home_search_filter_button.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .withStartGravity(Gravity.TOP)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build();
        Home_search_filter_layout_close_btn = (ImageView) findViewById(R.id.close_filter);
        Home_search_filter_layout_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = "";
                if (global_search_check_box_qa.isChecked()) {

                    if (filter.length() == 0) {
                        filter = filter + "q,a";
                    } else {
                        filter = filter + "," + "q,a";
                    }

                }
                if (global_search_check_box_user.isChecked()) {

                    if (filter.length() == 0) {
                        filter = filter + "u";
                    } else {
                        filter = filter + "," + "u";
                    }

                }

//                if (global_search_check_box_groups.isChecked()) {
//                    if (filter.length() == 0) {
//                        filter = filter + "g";
//                    } else {
//                        filter = filter + "," + "g";
//                    }
//                }

                if (global_search_check_box_strains.isChecked()) {

                    if (filter.length() == 0) {
                        filter = filter + "s";
                    } else {
                        filter = filter + "," + "s";
                    }
                }

                if (global_search_check_box_budzmap.isChecked()) {

                    if (filter.length() == 0) {
                        filter = filter + "bm";
                    } else {
                        filter = filter + "," + "bm";
                    }
                }
                if (filter.length() == 0) {
                    filter = "q,a,s,bm,u";
                }


//                if (global_search_check_box_journals.isChecked()) {
//                    if (filter.length() == 0) {
//                        filter = filter + "j";
//                    } else {
//                        filter = filter + "," + "j";
//                    }
//                }
                if (isQa || isBudz || isGroup || isJour || isStrain || isQaOff || isBudzOff || isGroupOff || isJourOff || isStrainOff || isUa || isUaOff) {
                    if (filter.length() > 0) {
                        home_search_list_data.clear();
                        global_Search_recyler_adapter.notifyDataSetChanged();
                        HideKeyboard(HomeGlobalSearchActivity.this);
                        global_search_refresh.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = new JSONObject();
                        isQa = false;
                        isUa = false;
                        isUaOff = false;
                        isBudz = false;
                        isGroup = false;
                        isJour = false;
                        isStrain = false;
                        isQaOff = false;
                        isBudzOff = false;
                        isGroupOff = false;
                        isJourOff = false;
                        isStrainOff = false;
                        isAppiCallActive = true;
                        pages = 0;
                        new VollyAPICall(HomeGlobalSearchActivity.this, false, globle_search + "?query=" + Home_search_editText.getText().toString() + "&filter=" + filter + "&zip_code=" + user.getZip_code() + "&state=" + "", jsonObject, user.getSession_key(), Request.Method.GET, HomeGlobalSearchActivity.this, global_search);
                    }
                }
                home_search_filter_slide.hide();
            }
        });
        Home_search_recyler_view = (RecyclerView) findViewById(R.id.home_search_recyler_view);
//        refresher = (SwipeRefreshLayout) findViewById(R.id.refresher);
//        refresher.setRefreshing(false);
        RecyclerView.LayoutManager layoutManager_home_saerch = new LinearLayoutManager(HomeGlobalSearchActivity.this);
        Home_search_recyler_view.setLayoutManager(layoutManager_home_saerch);
        global_Search_recyler_adapter = new HomeSearchListRecylerAdapter(HomeGlobalSearchActivity.this, home_search_list_data);
        global_Search_recyler_adapter.setClickListener(this);
        Home_search_recyler_view.setAdapter(global_Search_recyler_adapter);
        Home_search_recyler_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(Home_search_recyler_view.getLayoutManager());
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                Log.d("vissible", lastVisible + "");
                Log.d("vissible_t", (lastVisible % 10) + "");
                if (lastVisible >= (pages + 1) * 9 && !isAppiCallActive) {
                    if ((lastVisible % 9) == 0 || lastVisible > 10) {
                        pages = pages + 1;
                        JSONObject jsonObject = new JSONObject();
                        String url = "";
//                        refresher.setRefreshing(true);
                        global_search_refresh.setVisibility(View.VISIBLE);
//                        global_search_refresh.setVisibility(View.VISIBLE);
                        new VollyAPICall(HomeGlobalSearchActivity.this
                                , false, globle_search + "?query=" + Home_search_editText.getText().toString() + "&zip_code=" + user.getZip_code() + "&filter=" + filter + "&state=" + "" + "&skip=" + pages
                                , jsonObject, user.getSession_key()
                                , Request.Method.GET
                                , HomeGlobalSearchActivity.this
                                , global_search);
                    }
                }
            }
        });
    }

    public void callSearchApi() {
        filter = "";
        if (global_search_check_box_qa.isChecked()) {

            if (filter.length() == 0) {
                filter = filter + "q,a";
            } else {
                filter = filter + "," + "q,a";
            }

        }
        if (global_search_check_box_user.isChecked()) {

            if (filter.length() == 0) {
                filter = filter + "u";
            } else {
                filter = filter + "," + "u";
            }

        }

//                if (global_search_check_box_groups.isChecked()) {
//                    if (filter.length() == 0) {
//                        filter = filter + "g";
//                    } else {
//                        filter = filter + "," + "g";
//                    }
//                }

        if (global_search_check_box_strains.isChecked()) {

            if (filter.length() == 0) {
                filter = filter + "s";
            } else {
                filter = filter + "," + "s";
            }
        }

        if (global_search_check_box_budzmap.isChecked()) {

            if (filter.length() == 0) {
                filter = filter + "bm";
            } else {
                filter = filter + "," + "bm";
            }
        }
        if (filter.length() == 0) {
            filter = "q,a,s,bm,u";
        }


//                if (global_search_check_box_journals.isChecked()) {
//                    if (filter.length() == 0) {
//                        filter = filter + "j";
//                    } else {
//                        filter = filter + "," + "j";
//                    }
//                }
        if (isQa || isBudz || isGroup || isJour || isStrain || isQaOff || isBudzOff || isGroupOff || isJourOff || isStrainOff || isUa || isUaOff) {
            if (filter.length() > 0) {
                home_search_list_data.clear();
                global_Search_recyler_adapter.notifyDataSetChanged();
                HideKeyboard(HomeGlobalSearchActivity.this);
                global_search_refresh.setVisibility(View.VISIBLE);
                JSONObject jsonObject = new JSONObject();
                isQa = false;
                isUa = false;
                isUaOff = false;
                isBudz = false;
                isGroup = false;
                isJour = false;
                isStrain = false;
                isQaOff = false;
                isBudzOff = false;
                isGroupOff = false;
                isJourOff = false;
                isStrainOff = false;
                isAppiCallActive = true;
                pages = 0;
                new VollyAPICall(HomeGlobalSearchActivity.this, false, globle_search + "?query=" + Home_search_editText.getText().toString() + "&filter=" + filter + "&zip_code=" + user.getZip_code() + "&state=" + "", jsonObject, user.getSession_key(), Request.Method.GET, HomeGlobalSearchActivity.this, global_search);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_search_edit_text:
                Home_search_Header.setVisibility(View.VISIBLE);
                Home_search_main_content_list.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == global_search) {
            Log.d("response", response);
            isAppiCallActive = false;
//            refresher.setRefreshing(false);
            global_search_refresh.setVisibility(View.GONE);
            if (pages == 0) {
                home_search_list_data.clear();
            }

            try {
                JSONArray jsonObject = new JSONObject(response).getJSONObject("successData").getJSONArray("records");
                if (new JSONObject(response).getString("status").equalsIgnoreCase("error")) {
                    CustomeToast.ShowCustomToast(getApplicationContext(), new JSONObject(response).getString("errorMessage"), Gravity.CENTER);
                }
                JSONArray jsonObjectSub = new JSONObject(response).getJSONObject("successData").getJSONArray("sub_users");
                for (int x = 0; x < jsonObjectSub.length(); x++) {
                    JSONObject object = jsonObjectSub.getJSONObject(x);
                    HomeSearchListData data = new HomeSearchListData();
//                    if (object.getString("s_type").equalsIgnoreCase("a")) {
//                        String id = object.getString("v_pk");
//                        String[] sp = id.split("_");
//                        data.setId(Integer.parseInt(sp[0]));
//                    } else {
                    data.setId(object.getInt("id"));
//                    }
                    data.setTitle(object.getString("title"));
                    data.setDiscription(object.getString("description"));
                    data.setIcon(SetIcon("bm"));
                    data.setColor(setColor("bm"));
                    data.setType("bm");
                    data.setPremium(true);
                    home_search_list_data.add(data);
                    global_Search_recyler_adapter.notifyItemInserted(home_search_list_data.size() - 1);

                }
                for (int x = 0; x < jsonObject.length(); x++) {
                    JSONObject object = jsonObject.getJSONObject(x);
                    HomeSearchListData data = new HomeSearchListData();
//                    if (object.getString("s_type").equalsIgnoreCase("a")) {
//                        String id = object.getString("v_pk");
//                        String[] sp = id.split("_");
//                        data.setId(Integer.parseInt(sp[0]));
//                    } else {
                    data.setId(object.getInt("id"));
//                    }
                    if (!object.isNull("is_premium")) {
                        if (object.getString("is_premium").length() > 4) {
                            data.setPremium(true);
                        }
                    } else
                        data.setPremium(false);
                    data.setTitle(object.getString("title"));
                    data.setDiscription(object.getString("description"));
                    data.setIcon(SetIcon(object.getString("s_type")));
                    data.setColor(setColor(object.getString("s_type")));
                    data.setType(object.getString("s_type"));
                    if (object.getString("s_type").equalsIgnoreCase("u")) {
                        String icon_path;
                        if (!object.getJSONObject("user").isNull("image_path") && object.getJSONObject("user").getString("image_path").length() > 5) {
                            if (object.getJSONObject("user").getString("image_path").contains("facebook.com")
                                    || object.getJSONObject("user").getString("image_path").contains("google.com")
                                    || object.getJSONObject("user").getString("image_path").contains("https")
                                    || object.getJSONObject("user").getString("image_path").contains("http")
                                    || object.getJSONObject("user").getString("image_path").contains("googleusercontent.com")) {
                                icon_path = object.getJSONObject("user").getString("image_path");
                            } else {
                                icon_path = images_baseurl + object.getJSONObject("user").getString("image_path");

                            }
                        } else {
                            icon_path = images_baseurl + object.getJSONObject("user").getString("avatar");

                        }
//                        data.setColor(Utility.getBudColor(object.getJSONObject("user").getInt("points")));
                        data.setUser_point(object.getJSONObject("user").getInt("points"));
                        data.setImagePath(icon_path);
                    } else {
                        data.setUser_point(0);
                    }
                    if (data.getType().equalsIgnoreCase("u") && data.getId() == Splash.user.getUser_id()) {
//                        home_search_list_data.add(data);
                    } else {
                        home_search_list_data.add(data);
                    }
                    global_Search_recyler_adapter.notifyItemInserted(home_search_list_data.size() - 1);


                }
                global_Search_recyler_adapter.notifyDataSetChanged();
                if (home_search_list_data.size() == 0) {
                    not_found.setVisibility(View.VISIBLE);
                } else {
                    not_found.setVisibility(View.GONE);
                }
            } catch (IndexOutOfBoundsException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
//        refresher.setRefreshing(false);
        global_search_refresh.setVisibility(View.GONE);

        if (apiActions == global_search) {

        }
    }

    @Override
    public void onGlobalSearchItemClick(View view, int position) {
        switch (home_search_list_data.get(position).getType()) {
            case "a":
//                HideViewSearch();
                HomeQAfragmentDataModel model1 = new HomeQAfragmentDataModel();
                model1.setId(home_search_list_data.get(position).getId());
                showSubFragmentListner_object.ShowAnswers(model1, home_search_list_data.get(position).getId(), true);
                finish();
                break;
            case "q":
//                HideViewSearch();
                HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                model.setId(home_search_list_data.get(position).getId());
                showSubFragmentListner_object.ShowQuestions(model, true);
                finish();
                break;
            case "g":
//                HideViewSearch();
//                ChangeStatusBarColor(this, "#00000000");
                Intent intent = new Intent(this, GroupsChatViewActivity.class);
                intent.putExtra("goup_id", home_search_list_data.get(position).getId());
                startActivity(intent);
                break;
            case "j":
//                HideViewSearch();
//                ChangeStatusBarColor(this, "#00000000");
                Intent journals_intetn = new Intent(this, JournalDetailsActivity.class);
                journals_intetn.putExtra("journal_id", home_search_list_data.get(position).getId());
                startActivity(journals_intetn);
                break;
            case "s":
//                HideViewSearch();
//                ChangeStatusBarColor(this, "#00000000");
                Intent strain_intetn = new Intent(this, StrainDetailsActivity.class);
                strain_intetn.putExtra("strain_id", home_search_list_data.get(position).getId());
                startActivity(strain_intetn);
                break;
            case "bm":
//                HideViewSearch();
//                ChangeStatusBarColor(this, "#00000000");
                BudzMapHomeFragment.isSearchedText = true;
                BudzMapHomeFragment.isSearchedTextValue = Home_search_editText.getText().toString().trim();
                Intent budzmap_intetn = new Intent(this, BudzMapDetailsActivity.class);
                budzmap_intetn.putExtra("budzmap_id", home_search_list_data.get(position).getId());
                startActivity(budzmap_intetn);
                break;
            case "u":
//                HideViewSearch();
//                ChangeStatusBarColor(this, "#00000000");
                isNewScreen = true;
                GoToProfile(this, home_search_list_data.get(position).getId());
                break;
        }
    }
}
