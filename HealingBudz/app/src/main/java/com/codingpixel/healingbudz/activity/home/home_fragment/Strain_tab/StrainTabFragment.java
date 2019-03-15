package com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.StrainDataModel;
import com.codingpixel.healingbudz.DataModel.StrainOverViewDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.dialog.StrainSearchFilterSaveAlertDialog;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.StrainTabFragmentRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.PagingCall;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.Utilities.Constants.D_FORMAT_ONE;
import static com.codingpixel.healingbudz.activity.home.HomeActivity.drawerLayout;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_strains;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_strains_matched;
import static com.codingpixel.healingbudz.network.model.URL.search_strain_name;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;
//import static com.codingpixel.healingbudz.test_data.StrainTestData.get_strain_data;


public class StrainTabFragment extends Fragment implements StrainTabFragmentRecylerAdapter.ItemClickListener
        , StrainSearchFilterSaveAlertDialog.OnDialogFragmentClickListener
        , APIResponseListner, PagingCall {
    ImageView Menu;
    SwipeRefreshLayout refreshLayout;
    LinearLayout Search_dialog;
    LinearLayout Search_button;
    ImageView Close_search_dialog;
    RecyclerView Strain_recyler_view;
    Button Help_Me_find_matches;
    Button Search_Matches;
    StrainTabFragmentRecylerAdapter recyler_adapter;
    ImageView Close_search_view;
    LinearLayout Search_filter;
    LinearLayout Indica_type, Stiva_Type, Hybrid_type;
    LinearLayout Match_Relevence_Layout;
    LinearLayout Search_Main_view;
    LinearLayout ListAlphabaticallyMain;
    TextView list_alpha;
    LinearLayout ListAlphabaticallyFilter;
    public static boolean isFilter = false;
    EditText Search_Feild;
    TextView No_Record;
    ImageView clear_search;
    ImageView cross_medical, cross_flavour, cross_mode, cross_disease;
    ImageView Search_Field_Button;
    ArrayList<StrainDataModel> strainDataModels = new ArrayList<>();
    String keyword = "";
    boolean is_search_keyword = false;
    AutoCompleteTextView Search_Medical, Search_Disease, Search_Mode, Search_flavour;
    Button Search_field_btn;
    String medical_use = "", disease_prevention = "", mood_sensation = "", flavor = "";
    StrainOverViewDataModel strainOverViewDataModel = new StrainOverViewDataModel();
    boolean isFilterApi = false;
    String filter = "";
    boolean isNextScreen = true;

    public StrainTabFragment() {
        super();
        isFilterApi = false;
        isNextScreen = true;
    }

    @SuppressLint("ValidFragment")
    public StrainTabFragment(String key_word, boolean isSearchKeyword) {
        this.keyword = key_word;
        this.is_search_keyword = isSearchKeyword;
        isFilterApi = false;
        isNextScreen = true;
    }

    @SuppressLint("ValidFragment")
    public StrainTabFragment(String searchFilterApi) {
        this.filter = searchFilterApi;
        isFilterApi = true;
        isNextScreen = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.strain_tab_fragment, container, false);
        view.setClickable(false);
        isNextScreen = true;
        view.setOnClickListener(null);
        view.setOnTouchListener(null);
        Menu = view.findViewById(R.id.menu_btn);
        clear_search = view.findViewById(R.id.clear_search);
        No_Record = view.findViewById(R.id.no_record_found);
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        ListAlphabaticallyMain = view.findViewById(R.id.list_alpha_imain);
        list_alpha = view.findViewById(R.id.text_alpha);
        list_alpha.setText("List Newest");
        //List Alphabetically
        ListAlphabaticallyFilter = view.findViewById(R.id.list_alpha_filter);
        Match_Relevence_Layout = view.findViewById(R.id.match_relevance);

        Search_button = view.findViewById(R.id.search_button);
        Search_dialog = view.findViewById(R.id.search_dialog);
        Search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_button.setVisibility(View.GONE);
                Search_dialog.setVisibility(View.VISIBLE);
                Search_Main_view.setVisibility(View.VISIBLE);
                Search_filter.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                Search_dialog.startAnimation(startAnimation);
            }
        });

        Close_search_dialog = view.findViewById(R.id.close_Search_dialog);
        Close_search_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_button.setVisibility(View.VISIBLE);
                Search_dialog.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                Search_button.startAnimation(startAnimation);
            }
        });

        Close_search_view = view.findViewById(R.id.clocse_search);
        Close_search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_button.setVisibility(View.VISIBLE);
                Search_dialog.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                Search_button.startAnimation(startAnimation);
            }
        });

        Strain_recyler_view = view.findViewById(R.id.strain_recyler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        Strain_recyler_view.setLayoutManager(layoutManager);
        recyler_adapter = new StrainTabFragmentRecylerAdapter(getContext(), strainDataModels, this);
//        get_strain_data();
        Strain_recyler_view.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.parseColor("#f4c42f"));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Match_Relevence_Layout.setVisibility(View.GONE);
                clear_search.setVisibility(View.GONE);
                isFilter = false;
                ListAlphabaticallyMain.setVisibility(View.VISIBLE);
                list_alpha.setText("List Newest");
                ListAlphabaticallyFilter.setVisibility(View.GONE);
                refreshLayout.setRefreshing(true);
                JSONObject object = new JSONObject();
                page = 0;
                url = URL.get_strains_alphabitically + "?skip=";
                new VollyAPICall(getContext(), false, url + page, object, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains);
                Log.d("wait", "load");
            }
        });
        clear_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Match_Relevence_Layout.setVisibility(View.GONE);
                clear_search.setVisibility(View.GONE);
                isFilter = false;
                isFilterApi = false;
                isNextScreen = false;
                is_search_keyword = false;
                ListAlphabaticallyMain.setVisibility(View.VISIBLE);
                ListAlphabaticallyFilter.setVisibility(View.GONE);
                refreshLayout.setRefreshing(true);
                JSONObject object = new JSONObject();
                page = 0;
                url = URL.get_strains_alphabitically + "?skip=";
                new VollyAPICall(getContext(), false, url + page, object, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains);
            }
        });
        if (is_search_keyword) {
            is_search_keyword = false;
            SearchKeyword(keyword);
        } else {
            if (!isFilterApi) {
                refreshLayout.setRefreshing(true);
                JSONObject object = new JSONObject();
                page = 0;
                url = URL.get_strains_alphabitically + "?skip=";
                new VollyAPICall(getContext(), false, url + page, object, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains);
            }
        }
        ListAlphabaticallyMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list_alpha.getText().toString().equalsIgnoreCase("List Alphabetically")) {
                    list_alpha.setText("List Newest");
                    refreshLayout.setRefreshing(true);
                    JSONObject object = new JSONObject();
                    page = 0;
                    url = URL.get_strains_alphabitically + "?skip=";
                    new VollyAPICall(getContext(), false, url + page, object, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains);
                } else {
                    list_alpha.setText("List Alphabetically");
                    refreshLayout.setRefreshing(true);
                    JSONObject object = new JSONObject();
                    page = 0;
                    url = URL.get_strains + "?skip=";
                    new VollyAPICall(getContext(), false, url + page, object, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains);
                }

            }
        });

        Indica_type = view.findViewById(R.id.indica_type);
        Indica_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLayout.setRefreshing(true);
                JSONObject object = new JSONObject();
                page = 0;
                url = URL.get_strains_by_type + "/2" + "?skip=";
                new VollyAPICall(getContext(), false, url + page, object, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains);
            }
        });

        Stiva_Type = view.findViewById(R.id.sativa_type);
        Stiva_Type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLayout.setRefreshing(true);
                JSONObject object = new JSONObject();
                page = 0;
                url = URL.get_strains_by_type + "/3" + "?skip=";
                new VollyAPICall(getContext(), false, url + page, object, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains);
            }
        });

        Hybrid_type = view.findViewById(R.id.hybrid_type);
        Hybrid_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLayout.setRefreshing(true);
                JSONObject object = new JSONObject();
                page = 0;
                url = URL.get_strains_by_type + "/1" + "?skip=";
                new VollyAPICall(getContext(), false, url + page, object, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains);

            }
        });
        Help_Me_find_matches = view.findViewById(R.id.help_me_find_matches);
        Help_Me_find_matches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_filter.setVisibility(View.VISIBLE);
                Search_Main_view.setVisibility(View.GONE);
            }
        });

        Search_Medical = view.findViewById(R.id.ssearch_medical_use);
        Search_Disease = view.findViewById(R.id.ssearch_disease);
        Search_Mode = view.findViewById(R.id.search_modes);
        Search_flavour = view.findViewById(R.id.search_flavour);
        cross_medical = view.findViewById(R.id.cross_medical);
        cross_medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_Medical.setText("");
                cross_medical.setVisibility(View.GONE);
            }
        });
        cross_flavour = view.findViewById(R.id.cross_flavour);
        cross_flavour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_flavour.setText("");
                cross_flavour.setVisibility(View.GONE);
            }
        });
        cross_mode = view.findViewById(R.id.cross_mode);
        cross_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_Mode.setText("");
                cross_mode.setVisibility(View.GONE);
            }
        });
        cross_disease = view.findViewById(R.id.cross_disease);
        cross_disease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_Disease.setText("");
                cross_disease.setVisibility(View.GONE);
            }
        });

        Search_Disease.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    cross_disease.setVisibility(View.VISIBLE);
                } else {
                    cross_disease.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cross_disease.setVisibility(View.GONE);
        Search_Medical.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    cross_medical.setVisibility(View.VISIBLE);
                } else {
                    cross_medical.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cross_medical.setVisibility(View.GONE);
        Search_Mode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    cross_mode.setVisibility(View.VISIBLE);
                } else {
                    cross_mode.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cross_mode.setVisibility(View.GONE);
        Search_flavour.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    HideKeyboard((AppCompatActivity) v.getContext());
                    return true;
                }
                return false;
            }
        });
        Search_flavour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    cross_flavour.setVisibility(View.VISIBLE);
                } else {
                    cross_flavour.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cross_flavour.setVisibility(View.GONE);

        Search_Matches = view.findViewById(R.id.search_matches);
        Search_Matches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_button.setVisibility(View.VISIBLE);
                Search_dialog.setVisibility(View.GONE);
                clear_search.setVisibility(View.VISIBLE);
                Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                Search_button.startAnimation(startAnimation);
                if (Search_Medical.getText().toString().trim().length() > 0 || Search_Disease.getText().toString().trim().length() > 0 || Search_Mode.getText().toString().trim().length() > 0 || Search_flavour.getText().toString().trim().length() > 0) {
                    No_Record.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(true);
                    JSONObject jsonObject = new JSONObject();
                    medical_use = Search_Medical.getText().toString().trim();
                    disease_prevention = Search_Disease.getText().toString().trim();
                    mood_sensation = Search_Mode.getText().toString().trim();
                    flavor = Search_flavour.getText().toString().trim();
                    ListAlphabaticallyMain.setVisibility(View.INVISIBLE);
                    page = 0;
                    url = URL.search_strain_survey + "?medical_use=" + Search_Medical.getText().toString().trim() + "&disease_prevention=" + Search_Disease.getText().toString().trim() + "&mood_sensation=" + Search_Mode.getText().toString().trim() + "&flavor=" + Search_flavour.getText().toString().trim() + "&skip=";
                    new VollyAPICall(getContext(), false, url + page, jsonObject, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains_matched);
                    Search_Medical.setText("");
                    Search_Disease.setText("");
                    Search_Mode.setText("");
                    Search_flavour.setText("");
                } else {
                    ListAlphabaticallyMain.setVisibility(View.INVISIBLE);
                    strainDataModels.clear();
                    recyler_adapter.notifyDataSetChanged();
                    No_Record.setVisibility(View.VISIBLE);
                    No_Record.setText("No Matches Found, Please change terms.");
                }
            }
        });

        Search_filter = view.findViewById(R.id.search_filter);
        Search_Main_view = view.findViewById(R.id.searc_main_view);
        Search_Main_view.setClickable(false);
        Search_Main_view.setOnClickListener(null);
        Search_Main_view.setOnTouchListener(null);

        Search_filter.setClickable(false);
        Search_filter.setOnClickListener(null);
        Search_filter.setOnTouchListener(null);

        ListAlphabaticallyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFilterApi) {
                    StrainSearchFilterSaveAlertDialog strainSearchFilterSaveAlertDialog = StrainSearchFilterSaveAlertDialog.newInstance(StrainTabFragment.this);
                    strainSearchFilterSaveAlertDialog.show(getActivity().getSupportFragmentManager(), "dialog");
                } else {
                    CustomeToast.ShowCustomToast(view.getContext(), "Already Saved Search!", Gravity.TOP);
                }
            }
        });

        Search_Field_Button = view.findViewById(R.id.search_field_button);
        Search_Field_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_Feild.setText("");
                Search_Field_Button.setImageResource(R.drawable.ic_strain_search_icon);
//                if (Search_Feild.getText().toString().length() > 1) {
//                    Search_button.setVisibility(View.VISIBLE);
//                    Search_dialog.setVisibility(View.GONE);
//                    Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
//                    Search_button.startAnimation(startAnimation);
//                    HideKeyboard(getActivity());
//                    refreshLayout.setRefreshing(true);
//                    JSONObject object = new JSONObject();
//                    new VollyAPICall(getContext(), false, search_strain_name + "?name=" + Search_Feild.getText().toString().trim(), object, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, APIActions.ApiActions.get_strains);
//                }
            }
        });


        Search_field_btn = view.findViewById(R.id.search_field_butttton);
        Search_field_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Search_Feild.getText().toString().trim().length() > 0) {
                    Search_button.setVisibility(View.VISIBLE);
                    Search_dialog.setVisibility(View.GONE);
                    clear_search.setVisibility(View.GONE);
                    ListAlphabaticallyFilter.setVisibility(View.GONE);
                    Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                    Search_button.startAnimation(startAnimation);
                    HideKeyboard(getActivity());
                    refreshLayout.setRefreshing(true);
                    JSONObject object = new JSONObject();
                    ListAlphabaticallyMain.setVisibility(View.INVISIBLE);
                    page = 0;
                    url = search_strain_name + "?name=" + Search_Feild.getText().toString().trim() + "&skip=";
                    new VollyAPICall(getContext(), false, url + page, object, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, APIActions.ApiActions.get_strains);
                    Search_Feild.setText("");
                } else {
                    CustomeToast.ShowCustomToast(view.getContext(), "Please enter your data!", Gravity.TOP);
                }
            }
        });
        Search_Feild = view.findViewById(R.id.search_field);
        Search_Feild.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (Search_Feild.getText().toString().trim().length() > 0) {
                        Search_button.setVisibility(View.VISIBLE);
                        Search_dialog.setVisibility(View.GONE);
                        clear_search.setVisibility(View.GONE);
                        Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                        Search_button.startAnimation(startAnimation);
                        HideKeyboard(getActivity());
                        refreshLayout.setRefreshing(true);
                        JSONObject object = new JSONObject();
                        ListAlphabaticallyFilter.setVisibility(View.GONE);
                        ListAlphabaticallyMain.setVisibility(View.INVISIBLE);
                        page = 0;
                        url = search_strain_name + "?name=" + Search_Feild.getText().toString().trim() + "&skip=";
                        new VollyAPICall(getContext(), false, url + page, object, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains);
                        Search_Feild.setText("");
                    } else {
                        CustomeToast.ShowCustomToast(view.getContext(), "Please enter your data!", Gravity.TOP);
                    }
                    return true;
                }
                return false;
            }
        });
        Search_Feild.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    Search_Field_Button.setImageResource(R.drawable.ic_cross_black);
                } else {
                    Search_Field_Button.setImageResource(R.drawable.ic_strain_search_icon);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    public void SearchKeyword(String keyword) {
        strainDataModels.clear();
        is_search_keyword = true;
//        clear_search.setVisibility(View.VISIBLE);
        recyler_adapter.notifyDataSetChanged();
        HideKeyboard(getActivity());
//        clear_search.setVisibility(View.VISIBLE);
        refreshLayout.setRefreshing(true);
        JSONObject object = new JSONObject();
        ListAlphabaticallyFilter.setVisibility(View.GONE);
        clear_search.setVisibility(View.GONE);
        ListAlphabaticallyMain.setVisibility(View.INVISIBLE);
        page = 0;
        url = search_strain_name + "?name=" + keyword.trim() + "&skip=";
        new VollyAPICall(getContext(), false, url + page, object, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains);

    }

    public void SearchStrain(String keyword) {
        isFilterApi = true;
        filter = keyword;
        Search_button.setVisibility(View.VISIBLE);
        Search_dialog.setVisibility(View.GONE);
        clear_search.setVisibility(View.VISIBLE);
        Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        Search_button.startAnimation(startAnimation);
        No_Record.setVisibility(View.GONE);
        refreshLayout.setRefreshing(true);
        JSONObject jsonObject = new JSONObject();
        ListAlphabaticallyMain.setVisibility(View.INVISIBLE);
        page = 0;
        url = URL.search_strain_survey + filter + "?skip=";
        new VollyAPICall(getContext(), false, url + page, jsonObject, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains_matched);
        Search_Medical.setText("");
        Search_Disease.setText("");
        Search_Mode.setText("");
        Search_flavour.setText("");

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNextScreen) {
            if (is_search_keyword) {
//            SearchKeyword(keyword);
            } else {
                if (isFilterApi) {
                    isNextScreen = false;
                    Search_button.setVisibility(View.VISIBLE);
                    Search_dialog.setVisibility(View.GONE);
                    clear_search.setVisibility(View.VISIBLE);
                    Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                    Search_button.startAnimation(startAnimation);
                    No_Record.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(true);
                    ListAlphabaticallyMain.setVisibility(View.INVISIBLE);
                    JSONObject jsonObject = new JSONObject();
                    page = 0;
                    url = URL.search_strain_survey + filter + "?skip=";
                    new VollyAPICall(getContext(), false, url + page, jsonObject, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains_matched);
                    Search_Medical.setText("");
                    Search_Disease.setText("");
                    Search_Mode.setText("");
                    Search_flavour.setText("");
                } else {
                    Match_Relevence_Layout.setVisibility(View.GONE);
                    clear_search.setVisibility(View.GONE);
                    isFilter = false;
                    ListAlphabaticallyMain.setVisibility(View.VISIBLE);
                    ListAlphabaticallyFilter.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(true);
                    JSONObject object = new JSONObject();
                    page = 0;
                    url = URL.get_strains_alphabitically + "?skip=";
                    new VollyAPICall(getContext(), false, url + page, object, user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains);
                    Log.d("wait", "load");
                }
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        try {
            isNextScreen = false;
            StrainDetailsActivity.strainDataModel = strainDataModels.get(position);
            GoTo(getContext(), StrainDetailsActivity.class);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onCrossBtnClink(StrainSearchFilterSaveAlertDialog dialog) {
        dialog.dismiss();
//        Search_button.setVisibility(View.VISIBLE);
//        Search_dialog.setVisibility(View.GONE);
//        Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
//        Search_button.startAnimation(startAnimation);
//        Match_Relevence_Layout.setVisibility(View.GONE);
//        isFilter = false;
//        recyler_adapter.notifyDataSetChanged();
//        ListAlphabaticallyMain.setVisibility(View.VISIBLE);
//        ListAlphabaticallyFilter.setVisibility(View.GONE);
    }

    @Override
    public void onSaveBtnClink(StrainSearchFilterSaveAlertDialog dialog, String saveName) {
        dialog.dismiss();
//        new VollyAPICall(getContext(), false, URL.save_strain_search + "?medical_use=" + medical_use + "&disease_prevention=" + disease_prevention + "&mood_sensation=" + mood_sensation + "&flavor=" + flavor + "&skip=0", new JSONObject(), user.getSession_key(), Request.Method.GET, StrainTabFragment.this, get_strains_search);

        new VollyAPICall(getContext()
                , false
                , URL.save_strain_search + "?medical_use=" + medical_use + "&disease_prevention=" + disease_prevention + "&mood_sensation=" + mood_sensation + "&flavor=" + flavor + "&search_title=" + saveName
                , new JSONObject()
                , user.getSession_key()
                , Request.Method.GET
                , StrainTabFragment.this
                , APIActions.ApiActions.save_strain_search);
        medical_use = "";
        disease_prevention = "";
        mood_sensation = "";
        flavor = "";
        Search_button.setVisibility(View.VISIBLE);
        Search_dialog.setVisibility(View.GONE);
        Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        Search_button.startAnimation(startAnimation);
        Match_Relevence_Layout.setVisibility(View.GONE);
        isFilter = false;
        recyler_adapter.notifyDataSetChanged();
        ListAlphabaticallyMain.setVisibility(View.VISIBLE);
        ListAlphabaticallyFilter.setVisibility(View.GONE);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        is_search_keyword = false;
        if (getActivity() != null) {
            HideKeyboard(getActivity());
        }
        refreshLayout.setRefreshing(false);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray;
            if (apiActions == get_strains_matched) {
                Match_Relevence_Layout.setVisibility(View.VISIBLE);
                isFilter = true;
                ListAlphabaticallyMain.setVisibility(View.GONE);
                ListAlphabaticallyFilter.setVisibility(View.VISIBLE);
                jsonArray = jsonObject.getJSONObject("successData").getJSONArray("strains");
                strainDataModels.clear();
                for (int yy = 0; yy < jsonArray.length(); yy++) {
                    JSONObject strain_object = jsonArray.getJSONObject(yy).getJSONObject("get_strain");
                    StrainDataModel strainDataModel = new StrainDataModel();
                    strainDataModel.setMathces(jsonArray.getJSONObject(yy).getInt("matched"));
                    strainDataModel.setId(strain_object.getInt("id"));
                    strainDataModel.setType_id(strain_object.getInt("type_id"));
                    strainDataModel.setTitle(strain_object.getString("title"));
                    strainDataModel.setOverview(strain_object.getString("overview"));
                    strainDataModel.setApproved(strain_object.getInt("approved"));
                    strainDataModel.setCreated_at(strain_object.getString("created_at"));
                    strainDataModel.setUpdated_at(strain_object.getString("updated_at"));
                    strainDataModel.setGet_review_count(strain_object.getInt("get_review_count"));
                    strainDataModel.setGet_likes_count(strain_object.getInt("get_likes_count"));
                    strainDataModel.setGet_dislikes_count(strain_object.getInt("get_dislikes_count"));
                    strainDataModel.setType_title(strain_object.getJSONObject("get_type").getString("title"));
                    strainDataModel.setCurrent_user_like(strain_object.getInt("get_user_like_count"));
                    strainDataModel.setCurrent_user_dis_like(strain_object.getInt("get_user_dislike_count"));
                    strainDataModel.setCurrent_user_flag(strain_object.getInt("get_user_flag_count"));
                    strainDataModel.setFavorite(strain_object.getInt("is_saved_count"));
                    JSONArray images_array = strain_object.getJSONArray("get_images");
                    ArrayList<StrainDataModel.Images> images = new ArrayList<>();
                    for (int y = 0; y < images_array.length(); y++) {
                        JSONObject image_object = images_array.getJSONObject(y);
                        StrainDataModel.Images img = new StrainDataModel.Images();
                        img.setId(image_object.getInt("id"));
                        img.setStrain_id(image_object.getInt("strain_id"));
//                        img.setUser_id(image_object.getInt("user_id"));
                        img.setImage_path(image_object.getString("image_path"));
                        img.setIs_approved(image_object.getInt("is_approved"));
                        img.setIs_main(image_object.getInt("is_main"));
                        img.setCreated_at(image_object.getString("created_at"));
                        img.setUpdated_at(image_object.getString("updated_at"));
//                        img.setName(image_object.getJSONObject("get_user").getString("first_name"));
//                        img.setUser_rating(image_object.getJSONObject("get_user").getInt("points"));
                        if (!image_object.isNull("user_id")) {
                            img.setUser_id(image_object.getInt("user_id"));
                            img.setName(image_object.getJSONObject("get_user").getString("first_name"));
                            img.setUser_rating(image_object.getJSONObject("get_user").getInt("points"));
                        } else {
                            img.setName("Healing Budz");
                            img.setUser_rating(0);
                            img.setUser_id(-1);
                        }
                        img.setLike_count(image_object.getJSONArray("like_count").length());
                        img.setDis_like_count(image_object.getJSONArray("dis_like_count").length());
                        if (image_object.isNull("liked")) {
                            img.setIs_current_user_liked(false);
                        } else {
                            img.setIs_current_user_liked(true);
                        }

                        if (image_object.isNull("disliked")) {
                            img.setIs_current_user_dislike(false);
                        } else {
                            img.setIs_current_user_dislike(true);
                        }

                        if (image_object.isNull("flagged")) {
                            img.setIs_current_user_flaged(false);
                        } else {
                            img.setIs_current_user_flaged(true);
                        }
                        images.add(img);
                    }
                    strainDataModel.setImages(images);
                    if (strain_object.optJSONObject("rating_sum") != null) {
                        strainDataModel.setRating_sum(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").getDouble("total"))));
                        strainDataModel.setRating(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").getDouble("total"))));

                    }
                    strainDataModel.setAlphabetic_keyword(String.valueOf(strainDataModel.getType_title().charAt(0)));
                    strainDataModel.setReviews(strain_object.getInt("get_review_count") + " Reviews");
                    strainDataModels.add(strainDataModel);
                }
            } else if (apiActions == APIActions.ApiActions.save_strain_search) {
                //TODO
                CustomeToast.ShowCustomToast(getContext(), "Search Saved!", Gravity.TOP);
            } else if (apiActions == APIActions.ApiActions.paging_call) {
                isPageCall = true;
                jsonArray = jsonObject.getJSONObject("successData").getJSONArray("strains");
//                strainDataModels.clear();
                if (isFilter) {
                    for (int yy = 0; yy < jsonArray.length(); yy++) {
                        JSONObject strain_object = jsonArray.getJSONObject(yy).getJSONObject("get_strain");
                        StrainDataModel strainDataModel = new StrainDataModel();
                        strainDataModel.setMathces(jsonArray.getJSONObject(yy).getInt("matched"));
                        strainDataModel.setId(strain_object.getInt("id"));
                        strainDataModel.setType_id(strain_object.getInt("type_id"));
                        strainDataModel.setTitle(strain_object.getString("title"));
                        strainDataModel.setOverview(strain_object.getString("overview"));
                        strainDataModel.setApproved(strain_object.getInt("approved"));
                        strainDataModel.setCreated_at(strain_object.getString("created_at"));
                        strainDataModel.setUpdated_at(strain_object.getString("updated_at"));
                        strainDataModel.setGet_review_count(strain_object.getInt("get_review_count"));
                        strainDataModel.setGet_likes_count(strain_object.getInt("get_likes_count"));
                        strainDataModel.setGet_dislikes_count(strain_object.getInt("get_dislikes_count"));
                        strainDataModel.setType_title(strain_object.getJSONObject("get_type").getString("title"));
                        strainDataModel.setCurrent_user_like(strain_object.getInt("get_user_like_count"));
                        strainDataModel.setCurrent_user_dis_like(strain_object.getInt("get_user_dislike_count"));
                        strainDataModel.setCurrent_user_flag(strain_object.getInt("get_user_flag_count"));
                        strainDataModel.setFavorite(strain_object.getInt("is_saved_count"));
                        JSONArray images_array = strain_object.getJSONArray("get_images");
                        ArrayList<StrainDataModel.Images> images = new ArrayList<>();
                        for (int y = 0; y < images_array.length(); y++) {
                            JSONObject image_object = images_array.getJSONObject(y);
                            StrainDataModel.Images img = new StrainDataModel.Images();
                            img.setId(image_object.getInt("id"));
                            img.setStrain_id(image_object.getInt("strain_id"));
//                        img.setUser_id(image_object.getInt("user_id"));
                            img.setImage_path(image_object.getString("image_path"));
                            img.setIs_approved(image_object.getInt("is_approved"));
                            img.setIs_main(image_object.getInt("is_main"));
                            img.setCreated_at(image_object.getString("created_at"));
                            img.setUpdated_at(image_object.getString("updated_at"));
//                        img.setName(image_object.getJSONObject("get_user").getString("first_name"));
//                        img.setUser_rating(image_object.getJSONObject("get_user").getInt("points"));
                            if (!image_object.isNull("user_id")) {
                                img.setUser_id(image_object.getInt("user_id"));
                                img.setName(image_object.getJSONObject("get_user").getString("first_name"));
                                img.setUser_rating(image_object.getJSONObject("get_user").getInt("points"));
                            } else {
                                img.setName("Healing Budz");
                                img.setUser_rating(0);
                                img.setUser_id(-1);
                            }
                            img.setLike_count(image_object.getJSONArray("like_count").length());
                            img.setDis_like_count(image_object.getJSONArray("dis_like_count").length());
                            if (image_object.isNull("liked")) {
                                img.setIs_current_user_liked(false);
                            } else {
                                img.setIs_current_user_liked(true);
                            }

                            if (image_object.isNull("disliked")) {
                                img.setIs_current_user_dislike(false);
                            } else {
                                img.setIs_current_user_dislike(true);
                            }

                            if (image_object.isNull("flagged")) {
                                img.setIs_current_user_flaged(false);
                            } else {
                                img.setIs_current_user_flaged(true);
                            }
                            images.add(img);
                        }
                        strainDataModel.setImages(images);
                        if (strain_object.optJSONObject("rating_sum") != null) {
                            strainDataModel.setRating_sum(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").getDouble("total"))));
                            strainDataModel.setRating(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").getDouble("total"))));

                        }
                        strainDataModel.setAlphabetic_keyword(String.valueOf(strainDataModel.getType_title().charAt(0)));
                        strainDataModel.setReviews(strain_object.getInt("get_review_count") + " Reviews");
                        strainDataModels.add(strainDataModel);
                    }
                } else {
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject strain_object = jsonArray.getJSONObject(x);
                        StrainDataModel strainDataModel = new StrainDataModel();
                        strainDataModel.setMathces(0);
                        strainDataModel.setId(strain_object.getInt("id"));
                        strainDataModel.setType_id(strain_object.getInt("type_id"));
                        strainDataModel.setTitle(strain_object.getString("title"));
                        strainDataModel.setOverview(strain_object.getString("overview"));
                        strainDataModel.setApproved(strain_object.getInt("approved"));
                        strainDataModel.setCreated_at(strain_object.getString("created_at"));
                        strainDataModel.setUpdated_at(strain_object.getString("updated_at"));
                        strainDataModel.setGet_review_count(strain_object.getInt("get_review_count"));
                        strainDataModel.setGet_likes_count(strain_object.getInt("get_likes_count"));
                        strainDataModel.setGet_dislikes_count(strain_object.getInt("get_dislikes_count"));
                        strainDataModel.setType_title(strain_object.getJSONObject("get_type").getString("title"));
                        strainDataModel.setCurrent_user_like(strain_object.getInt("get_user_like_count"));
                        strainDataModel.setCurrent_user_dis_like(strain_object.getInt("get_user_dislike_count"));
                        strainDataModel.setCurrent_user_flag(strain_object.getInt("get_user_flag_count"));
                        strainDataModel.setFavorite(strain_object.getInt("is_saved_count"));
                        JSONArray images_array = strain_object.getJSONArray("get_images");
                        ArrayList<StrainDataModel.Images> images = new ArrayList<>();
                        for (int y = 0; y < images_array.length(); y++) {
                            JSONObject image_object = images_array.getJSONObject(y);
                            StrainDataModel.Images img = new StrainDataModel.Images();
                            img.setId(image_object.getInt("id"));
                            img.setStrain_id(image_object.getInt("strain_id"));
                            if (!image_object.isNull("user_id")) {
                                img.setUser_id(image_object.getInt("user_id"));
                                img.setName(image_object.getJSONObject("get_user").getString("first_name"));
                                img.setUser_rating(image_object.getJSONObject("get_user").getInt("points"));
                            } else {
                                img.setName("Healing Budz");
                                img.setUser_rating(0);
                                img.setUser_id(-1);
                            }
                            img.setImage_path(image_object.getString("image_path"));
                            img.setIs_approved(image_object.getInt("is_approved"));
                            img.setIs_main(image_object.getInt("is_main"));
                            img.setCreated_at(image_object.getString("created_at"));
                            img.setUpdated_at(image_object.getString("updated_at"));

                            img.setLike_count(image_object.getJSONArray("like_count").length());
                            img.setDis_like_count(image_object.getJSONArray("dis_like_count").length());
                            if (image_object.isNull("liked")) {
                                img.setIs_current_user_liked(false);
                            } else {
                                img.setIs_current_user_liked(true);
                            }

                            if (image_object.isNull("disliked")) {
                                img.setIs_current_user_dislike(false);
                            } else {
                                img.setIs_current_user_dislike(true);
                            }

                            if (image_object.isNull("flagged")) {
                                img.setIs_current_user_flaged(false);
                            } else {
                                img.setIs_current_user_flaged(true);
                            }
                            images.add(img);
                        }
                        strainDataModel.setImages(images);
                        if (strain_object.optJSONObject("rating_sum") != null) {

                            strainDataModel.setRating_sum(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").optDouble("total"))));
                            strainDataModel.setRating(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").optDouble("total"))));
                        }
                        strainDataModel.setAlphabetic_keyword(String.valueOf(strainDataModel.getType_title().charAt(0)));
                        strainDataModel.setReviews(strain_object.getInt("get_review_count") + " Reviews");
                        strainDataModels.add(strainDataModel);
                    }
                }

            } else {
                Match_Relevence_Layout.setVisibility(View.GONE);
                clear_search.setVisibility(View.GONE);
                isFilter = false;
//                ListAlphabaticallyMain.setVisibility(View.VISIBLE);
                ListAlphabaticallyFilter.setVisibility(View.GONE);
                jsonArray = jsonObject.getJSONObject("successData").getJSONArray("strains");
                strainDataModels.clear();
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject strain_object = jsonArray.getJSONObject(x);
                    StrainDataModel strainDataModel = new StrainDataModel();
                    strainDataModel.setMathces(0);
                    strainDataModel.setId(strain_object.getInt("id"));
                    strainDataModel.setType_id(strain_object.getInt("type_id"));
                    strainDataModel.setTitle(strain_object.getString("title"));
                    strainDataModel.setOverview(strain_object.getString("overview"));
                    strainDataModel.setApproved(strain_object.getInt("approved"));
                    strainDataModel.setCreated_at(strain_object.getString("created_at"));
                    strainDataModel.setUpdated_at(strain_object.getString("updated_at"));
                    strainDataModel.setGet_review_count(strain_object.getInt("get_review_count"));
                    strainDataModel.setGet_likes_count(strain_object.getInt("get_likes_count"));
                    strainDataModel.setGet_dislikes_count(strain_object.getInt("get_dislikes_count"));
                    strainDataModel.setType_title(strain_object.getJSONObject("get_type").getString("title"));
                    strainDataModel.setCurrent_user_like(strain_object.getInt("get_user_like_count"));
                    strainDataModel.setCurrent_user_dis_like(strain_object.getInt("get_user_dislike_count"));
                    strainDataModel.setCurrent_user_flag(strain_object.getInt("get_user_flag_count"));
                    strainDataModel.setFavorite(strain_object.getInt("is_saved_count"));
                    JSONArray images_array = strain_object.getJSONArray("get_images");
                    ArrayList<StrainDataModel.Images> images = new ArrayList<>();
                    for (int y = 0; y < images_array.length(); y++) {
                        JSONObject image_object = images_array.getJSONObject(y);
                        StrainDataModel.Images img = new StrainDataModel.Images();
                        img.setId(image_object.getInt("id"));
                        img.setStrain_id(image_object.getInt("strain_id"));
                        if (!image_object.isNull("user_id")) {
                            img.setUser_id(image_object.getInt("user_id"));
                            img.setName(image_object.getJSONObject("get_user").getString("first_name"));
                            img.setUser_rating(image_object.getJSONObject("get_user").getInt("points"));
                        } else {
                            img.setName("Healing Budz");
                            img.setUser_rating(0);
                            img.setUser_id(-1);
                        }
                        img.setImage_path(image_object.getString("image_path"));
                        img.setIs_approved(image_object.getInt("is_approved"));
                        img.setIs_main(image_object.getInt("is_main"));
                        img.setCreated_at(image_object.getString("created_at"));
                        img.setUpdated_at(image_object.getString("updated_at"));

                        img.setLike_count(image_object.getJSONArray("like_count").length());
                        img.setDis_like_count(image_object.getJSONArray("dis_like_count").length());
                        if (image_object.isNull("liked")) {
                            img.setIs_current_user_liked(false);
                        } else {
                            img.setIs_current_user_liked(true);
                        }

                        if (image_object.isNull("disliked")) {
                            img.setIs_current_user_dislike(false);
                        } else {
                            img.setIs_current_user_dislike(true);
                        }

                        if (image_object.isNull("flagged")) {
                            img.setIs_current_user_flaged(false);
                        } else {
                            img.setIs_current_user_flaged(true);
                        }
                        images.add(img);
                    }
                    strainDataModel.setImages(images);
                    if (strain_object.optJSONObject("rating_sum") != null) {

                        strainDataModel.setRating_sum(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").optDouble("total"))));
                        strainDataModel.setRating(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").optDouble("total"))));
                    }
                    strainDataModel.setAlphabetic_keyword(String.valueOf(strainDataModel.getType_title().charAt(0)));
                    strainDataModel.setReviews(strain_object.getInt("get_review_count") + " Reviews");
                    strainDataModels.add(strainDataModel);
                }
                //@survay Answersss
//                Search_Medical, Search_Disease, Search_Mode, Search_flavour;
                if (getContext() != null && apiActions == get_strains) {
                    ArrayList<StrainOverViewDataModel.MedicalConditionAnswers> medicalConditionAnswers = new ArrayList<>();
                    if (jsonObject.getJSONObject("successData").has("madical_condition_answers")) {
                        JSONArray medical_condition_answers_array = jsonObject.getJSONObject("successData").getJSONArray("madical_condition_answers");
                        for (int x = 0; x < medical_condition_answers_array.length(); x++) {
                            JSONObject m_jsonobject = medical_condition_answers_array.getJSONObject(x);
                            StrainOverViewDataModel.MedicalConditionAnswers medicalConditio_answers_modal = new StrainOverViewDataModel.MedicalConditionAnswers();
                            medicalConditio_answers_modal.setId(m_jsonobject.getInt("id"));
                            medicalConditio_answers_modal.setM_condition(m_jsonobject.getString("m_condition"));
                            medicalConditio_answers_modal.setIs_approved(m_jsonobject.optInt("is_approved"));
                            medicalConditionAnswers.add(medicalConditio_answers_modal);
                        }
                        strainOverViewDataModel.setMedicalConditionAnswers(medicalConditionAnswers);
                        Search_Medical.setThreshold(1);
                        final List<String> StrainKeywordsMed = new ArrayList<String>();
                        for (int x = 0; x < strainOverViewDataModel.getMedicalConditionAnswers().size(); x++) {
                            StrainKeywordsMed.add(strainOverViewDataModel.getMedicalConditionAnswers().get(x).getM_condition());
                        }
                        final ArrayAdapter<String> adapterMed = new ArrayAdapter<String>(getContext(), R.layout.simple_text_view, StrainKeywordsMed);
                        Search_Medical.setAdapter(adapterMed);
                    }

                    if (jsonObject.getJSONObject("successData").has("sensation_answers")) {
                        ArrayList<StrainOverViewDataModel.ModesAndSensationsAnswers> modesAndSensationsAnswers = new ArrayList<>();
                        JSONArray modes_sensation_answers_array = jsonObject.getJSONObject("successData").getJSONArray("sensation_answers");
                        for (int x = 0; x < modes_sensation_answers_array.length(); x++) {
                            JSONObject m_jsonobject = modes_sensation_answers_array.getJSONObject(x);
                            StrainOverViewDataModel.ModesAndSensationsAnswers modesAndSensationsAnswers1 = new StrainOverViewDataModel.ModesAndSensationsAnswers();
                            modesAndSensationsAnswers1.setId(m_jsonobject.getInt("id"));
                            modesAndSensationsAnswers1.setSensation(m_jsonobject.getString("sensation"));
                            modesAndSensationsAnswers1.setIs_approved(m_jsonobject.optInt("is_approved"));
                            modesAndSensationsAnswers.add(modesAndSensationsAnswers1);
                        }
                        strainOverViewDataModel.setModesAndSensationsAnswers(modesAndSensationsAnswers);
                        Search_Mode.setThreshold(1);
                        final List<String> StrainKeywordsMood = new ArrayList<String>();
                        for (int x = 0; x < strainOverViewDataModel.getModesAndSensationsAnswers().size(); x++) {
                            StrainKeywordsMood.add(strainOverViewDataModel.getModesAndSensationsAnswers().get(x).getSensation());
                        }
                        final ArrayAdapter<String> adapterMood = new ArrayAdapter<String>(getContext(), R.layout.simple_text_view, StrainKeywordsMood);
                        Search_Mode.setAdapter(adapterMood);
                    }
                    if (jsonObject.getJSONObject("successData").has("negative_effect_answers")) {
                        ArrayList<StrainOverViewDataModel.NegativeEffectAnswers> negativeEffectAnswers = new ArrayList<>();
                        JSONArray negative_effect_answers_array = jsonObject.getJSONObject("successData").getJSONArray("negative_effect_answers");
                        for (int x = 0; x < negative_effect_answers_array.length(); x++) {
                            JSONObject m_jsonobject = negative_effect_answers_array.getJSONObject(x);
                            StrainOverViewDataModel.NegativeEffectAnswers negativeEffectAnswers1 = new StrainOverViewDataModel.NegativeEffectAnswers();
                            negativeEffectAnswers1.setId(m_jsonobject.getInt("id"));
                            negativeEffectAnswers1.setEffect(m_jsonobject.getString("effect"));
                            negativeEffectAnswers1.setIs_approved(m_jsonobject.optInt("is_approved"));
                            negativeEffectAnswers.add(negativeEffectAnswers1);
                        }
                        strainOverViewDataModel.setNegativeEffectAnswers(negativeEffectAnswers);
                    }
//                Search_Medical.setThreshold(1);
//                final List<String> StrainKeywordsNev = new ArrayList<String>();
//                for (int x = 0; x < strainOverViewDataModel.getNegativeEffectAnswers().size(); x++) {
//                    StrainKeywordsNev.add(strainOverViewDataModel.getNegativeEffectAnswers().get(x).getEffect());
//                }
//                final ArrayAdapter<String> adapterNev = new ArrayAdapter<String>(getContext(), R.layout.simple_text_view, StrainKeywordsNev);
//                Search_Medical.setAdapter(adapterNev);

                    if (jsonObject.getJSONObject("successData").has("prevention_answers")) {
                        ArrayList<StrainOverViewDataModel.DiseasePreventionAnswers> diseasePreventionAnswers = new ArrayList<>();
                        JSONArray diseasePrevention_answers_array = jsonObject.getJSONObject("successData").getJSONArray("prevention_answers");
                        for (int x = 0; x < diseasePrevention_answers_array.length(); x++) {
                            JSONObject m_jsonobject = diseasePrevention_answers_array.getJSONObject(x);
                            StrainOverViewDataModel.DiseasePreventionAnswers diseasePreventionAnswers1 = new StrainOverViewDataModel.DiseasePreventionAnswers();
                            diseasePreventionAnswers1.setId(m_jsonobject.getInt("id"));
                            diseasePreventionAnswers1.setPrevention(m_jsonobject.getString("prevention"));
                            diseasePreventionAnswers1.setIs_approved(m_jsonobject.optInt("is_approved"));
                            diseasePreventionAnswers.add(diseasePreventionAnswers1);
                        }
                        strainOverViewDataModel.setDiseasePreventionAnswers(diseasePreventionAnswers);
                        Search_Disease.setThreshold(1);
                        final List<String> StrainKeywordsDes = new ArrayList<String>();
                        for (int x = 0; x < strainOverViewDataModel.getDiseasePreventionAnswers().size(); x++) {
                            StrainKeywordsDes.add(strainOverViewDataModel.getDiseasePreventionAnswers().get(x).getPrevention());
                        }
                        final ArrayAdapter<String> adapterDes = new ArrayAdapter<String>(getContext(), R.layout.simple_text_view, StrainKeywordsDes);
                        Search_Disease.setAdapter(adapterDes);

                    }
                    if (jsonObject.getJSONObject("successData").has("survey_flavor_answers")) {

                        ArrayList<StrainOverViewDataModel.SurvayFlavourAnswers> survayFlavourAnswers = new ArrayList<>();
                        JSONArray survayFlavour_answers_array = jsonObject.getJSONObject("successData").getJSONArray("survey_flavor_answers");
                        for (int x = 0; x < survayFlavour_answers_array.length(); x++) {
                            JSONObject m_jsonobject = survayFlavour_answers_array.getJSONObject(x);
                            StrainOverViewDataModel.SurvayFlavourAnswers survayFlavourAnswers1 = new StrainOverViewDataModel.SurvayFlavourAnswers();
                            survayFlavourAnswers1.setId(m_jsonobject.getInt("id"));
                            survayFlavourAnswers1.setFlavor(m_jsonobject.getString("flavor"));
                            survayFlavourAnswers1.setIs_approved(m_jsonobject.optInt("is_approved"));
                            survayFlavourAnswers.add(survayFlavourAnswers1);
                        }
                        strainOverViewDataModel.setSurvayFlavourAnswers(survayFlavourAnswers);
                        Search_flavour.setThreshold(1);
                        final List<String> StrainKeywordsFla = new ArrayList<String>();
                        for (int x = 0; x < strainOverViewDataModel.getSurvayFlavourAnswers().size(); x++) {
                            StrainKeywordsFla.add(strainOverViewDataModel.getSurvayFlavourAnswers().get(x).getFlavor());
                        }
                        final ArrayAdapter<String> adapterFla = new ArrayAdapter<String>(getContext(), R.layout.simple_text_view, StrainKeywordsFla);
                        Search_flavour.setAdapter(adapterFla);
                    }
                }
            }

            if (strainDataModels.size() == 0) {
                No_Record.setText("No Record Found!");
                No_Record.setVisibility(View.VISIBLE);
            } else {
                No_Record.setVisibility(View.GONE);
            }
            recyler_adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        if (apiActions == APIActions.ApiActions.paging_call) {
            if (page > 0) {
                page -= 1;
            }
            isPageCall = true;
        }
        Log.d("response", response);
        refreshLayout.setRefreshing(false);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String url = "";
    int page = 0;
    boolean isPageCall = true;

    @Override
    public void callNextPage(int size) {
        if (size > 0 && (size % 10 == 0 && isPageCall)) {
            page += 1;
            isPageCall = false;
            refreshLayout.setRefreshing(true);
            new VollyAPICall(getContext()
                    , false
                    , url + page
                    , new JSONObject()
                    , Splash.user.getSession_key()
                    , Request.Method.GET
                    , this
                    , APIActions.ApiActions.paging_call);
        }
    }
}
