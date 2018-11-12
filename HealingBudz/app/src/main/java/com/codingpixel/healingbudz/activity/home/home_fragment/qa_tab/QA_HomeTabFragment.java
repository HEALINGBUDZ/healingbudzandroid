package com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.DataModel.QAHomeHeaderListDataModel;
import com.codingpixel.healingbudz.DataModel.QuestionAnswersDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.ReportItView.Report;
import com.codingpixel.healingbudz.Utilities.Url.StringUtils;
import com.codingpixel.healingbudz.activity.home.HomeActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.my_answers.EditAnswerFragment;
import com.codingpixel.healingbudz.adapter.HomeDrawerRecylerAdapter;
import com.codingpixel.healingbudz.adapter.QAHeaderListRecylerAdapter;
import com.codingpixel.healingbudz.adapter.QAHomeFragmentRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.fragment_back_button_listner.BackButtonClickListner;
import com.codingpixel.healingbudz.interfaces.DetectMenuBarHideListner;
import com.codingpixel.healingbudz.interfaces.QAAddSubFragmentListner;
import com.codingpixel.healingbudz.interfaces.ReportButtonClickListner;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
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
import java.util.Objects;

import static com.codingpixel.healingbudz.activity.home.HomeActivity.drawerLayout;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_flag_q;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_my_question;
import static com.codingpixel.healingbudz.network.model.URL.add_question_flag;
import static com.codingpixel.healingbudz.network.model.URL.get_questions;
import static com.codingpixel.healingbudz.network.model.URL.search_question;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;
import static com.codingpixel.healingbudz.test_data.QAHomeHeaderListData.qa_home_header_data;
import static com.codingpixel.healingbudz.test_data.QAHomeHeaderListData.report_main_question_list;

public class QA_HomeTabFragment extends Fragment implements QAHomeFragmentRecylerAdapter.ItemClickListener, HomeDrawerRecylerAdapter.ItemClickListener, QAAddSubFragmentListner, BackButtonClickListner, ReportButtonClickListner, APIResponseListner, QAHeaderListRecylerAdapter.ItemClickListener, ReportSendButtonLstner {
    public static HomeQAfragmentDataModel dataModel;
    RecyclerView recyclerView, q_a_header_list_recyler_view;
    private static SwipeRefreshLayout refreshLayout;
    RelativeLayout Main_layout;
    RelativeLayout q_a_list, q_a_list_bottom;
    RelativeLayout slide_header_list;
    //    boolean is_list_open = false;
    final boolean[] is_list_open = {false};
    private SlideUp slide;
    public Button QA_AskQuestion;
    ImageView Menu_Btn;
    public static boolean isAnsswred = false;
    ImageView Search_btn;
    public EditText Search_field;
    Report report;
    static Context context;
    static APIResponseListner responseListner;
    TextView No_Question_Found;
    public AskQuestionFragment askQuestionFragment = new AskQuestionFragment();
    public static ReplyQuestionFragment replyQuestionFragment = new ReplyQuestionFragment();
    public DiscussQuestionFragment discussQuestionFragment;
    public AnswerFragment answerFragment = new AnswerFragment();
    public EditAnswerFragment answerEditFragment = new EditAnswerFragment();
    QAHomeFragmentRecylerAdapter recyler_adapter;
    ArrayList<HomeQAfragmentDataModel> main_data = new ArrayList<>();
    public DetectMenuBarHideListner detectMenuBarHideListner;
    public boolean isSearchkyword = false;
    public String search_keyword = "";
    String lower_case_key = "";
    private static String pathUrl = "";
    RelativeLayout Main_Menu;
    private static int pages = 0;
    private static boolean isAppiCallActive = false;
    public boolean isFromAnswer = false;
    //// Header Variables

    //ic_cross_black
//    ic_qa_fragment_search
    public QA_HomeTabFragment() {
        this.detectMenuBarHideListner = (DetectMenuBarHideListner) getActivity();
        discussQuestionFragment = new DiscussQuestionFragment(this);
    }

    @SuppressLint("ValidFragment")
    public QA_HomeTabFragment(DetectMenuBarHideListner detect_listner) {
        this.detectMenuBarHideListner = detect_listner;
        discussQuestionFragment = new DiscussQuestionFragment(this);
    }

    @SuppressLint("ValidFragment")
    public QA_HomeTabFragment(DetectMenuBarHideListner detect_listner, boolean IsSearch_Keyword, String Keyword) {
        this.detectMenuBarHideListner = detect_listner;
        discussQuestionFragment = new DiscussQuestionFragment(this);
        this.isSearchkyword = IsSearch_Keyword;
        this.search_keyword = Keyword;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (Search_field != null) {
            Search_field.setText("");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.q_a_fragment_layout, container, false);


//        SharedPrefrences.setInt("QA_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", 1, getContext());
        InitRefreshLayout(view);
        context = getContext();
        responseListner = QA_HomeTabFragment.this;
        Search_field = view.findViewById(R.id.search_field);
        Search_field.setText("");
        Search_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    Search_btn.setImageDrawable(Objects.requireNonNull(getContext()).getDrawable(R.drawable.ic_qa_fragment_search));

                } else {
                    Search_btn.setImageDrawable(Objects.requireNonNull(getContext()).getDrawable(R.drawable.ic_cross_black_qa));
                }

            }
        });
        Search_btn = view.findViewById(R.id.search_button);
        q_a_list = view.findViewById(R.id.q_a_list);
        slide_header_list = view.findViewById(R.id.slide_header_list);
        q_a_list_bottom = view.findViewById(R.id.q_a_list_bottom);
        No_Question_Found = view.findViewById(R.id.no_question);
        Menu_Btn = view.findViewById(R.id.q_a_menu_btn);
        QA_AskQuestion = view.findViewById(R.id.q_a_menu_right_button_askquestion);
        QA_AskQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetachSubFragments();
                is_list_open[0] = false;
                slide.hide();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.attatch_fragment_right_to_left);
                if (askQuestionFragment.isAdded()) {
                    transaction.remove(askQuestionFragment);
                }
                transaction.add(R.id.fragment_id_full_screen, askQuestionFragment, "1");
                transaction.commitAllowingStateLoss();
            }
        });

        Menu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        view.setOnClickListener(null);
        view.setOnTouchListener(null);
        view.setClickable(false);

        if (isSearchkyword) {
            isSearchkyword = false;
            HideKeyboard(getActivity());
            refreshLayout.setRefreshing(true);
            JSONObject object = new JSONObject();
            pathUrl = search_question + "?query=" + search_keyword + "&skip=";
            pages = 0;
            isAppiCallActive = true;
            new VollyAPICall(getContext(), false, pathUrl + 0, object, user.getSession_key(), Request.Method.GET, QA_HomeTabFragment.this, get_my_question);
        } else {
            if (main_data != null) {
                if (main_data.size() == 0) {
                    SharedPrefrences.setInt("QA_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", 0, getContext());
                    SharedPrefrences.setBool("QA_HOME_HEADER_LIST_SELECTED_ITEM_INDEX_BOOL", false, getContext());
                    refreshLayout.setRefreshing(true);
                    JSONObject jsonObject = new JSONObject();
                    pathUrl = get_questions + "?skip=" + 0;
                    pages = 0;
                    isAppiCallActive = true;
                    new VollyAPICall(getContext(), false, pathUrl + pages, jsonObject, user.getSession_key(), Request.Method.GET, QA_HomeTabFragment.this, get_my_question);
                } else {
                    recyler_adapter.notifyDataSetChanged();
                }
            }
        }
        InitHeaderSlideView(view);
        Main_layout = view.findViewById(R.id.main_layout);
        report = new Report(getContext(), this);
        Main_layout.addView(report.getView());
        report.InitSlide();
        Search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_field.setText("");
//                callEmptyViewApi();
                JSONObject jsonObject = new JSONObject();
                if (Search_field.getText().toString().trim().length() > 0) {
                    Search_field.setText("");
                    Search_field.clearFocus();
                }
                refreshLayout.setRefreshing(true);
                if (lower_case_key != null && lower_case_key.length() > 0) {

                    pathUrl = get_questions + "?sort=" + lower_case_key + "&skip=";
                    pages = 0;
                    isAppiCallActive = true;
                    new VollyAPICall(getContext(), false, pathUrl + pages, jsonObject, user.getSession_key(), Request.Method.GET, QA_HomeTabFragment.this, get_my_question);
                } else {
                    pathUrl = get_questions + "?skip=";
                    pages = 0;
                    isAppiCallActive = true;
                    new VollyAPICall(getContext(), false, pathUrl + pages, jsonObject, user.getSession_key(), Request.Method.GET, QA_HomeTabFragment.this, get_my_question);
                }
            }
        });

        Search_field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    HideKeyboard(getActivity());
                    if (Search_field.getText().toString().length() > 1) {
                        HideKeyboard(getActivity());
                        refreshLayout.setRefreshing(true);
                        JSONObject object = new JSONObject();
                        pathUrl = search_question + "?query=" + Search_field.getText().toString().trim() + "&skip=";
                        pages = 0;
                        isAppiCallActive = true;
                        new VollyAPICall(getContext(), false, pathUrl + 0, object, user.getSession_key(), Request.Method.GET, QA_HomeTabFragment.this, get_my_question);
                    } else {
                        callEmptyViewApi();
                    }
                    return true;
                }
                return false;
            }
        });
        if (discussQuestionFragment.isVisible()) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(discussQuestionFragment);
            transaction.commitAllowingStateLoss();
        }

        return view;
    }

    void callEmptyViewApi() {
        if (main_data.size() == 0) {
            JSONObject jsonObject = new JSONObject();
            if (Search_field.getText().toString().trim().length() > 0) {
                Search_field.setText("");
                Search_field.clearFocus();
            }
            refreshLayout.setRefreshing(true);
            if (lower_case_key != null && lower_case_key.length() > 0) {

                pathUrl = get_questions + "?sort=" + lower_case_key + "&skip=";
                pages = 0;
                isAppiCallActive = true;
                new VollyAPICall(getContext(), false, pathUrl + pages, jsonObject, user.getSession_key(), Request.Method.GET, QA_HomeTabFragment.this, get_my_question);
            } else {
                pathUrl = get_questions + "?skip=";
                pages = 0;
                isAppiCallActive = true;
                new VollyAPICall(getContext(), false, pathUrl + pages, jsonObject, user.getSession_key(), Request.Method.GET, QA_HomeTabFragment.this, get_my_question);
            }
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        if (Search_field != null) {
            Search_field.setText("");
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void callAfterAddClosed() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DetachSubFragments();
                discussQuestionFragment.dataModel = dataModelAdd;
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
                transaction.add(R.id.fragment_id_full_screen, discussQuestionFragment, "1");
                transaction.commitAllowingStateLoss();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        GoToProfile(getContext(), main_data.get(position).getUser_id());
    }

    public ArrayList<HomeQAfragmentDataModel> matched_data(String query) {
        ArrayList<HomeQAfragmentDataModel> data = new ArrayList<>();
        data.clear();
        for (int x = 0; x < main_data.size(); x++) {
            if (main_data.get(x).getQuestion().toLowerCase().contains(query.toLowerCase()) || main_data.get(x).getQuestion().toLowerCase().contains(query.toLowerCase())) {
                data.add(main_data.get(x));
            }
        }
        if (data.size() == 0) {
            data.addAll(main_data);
        }
        return data;
    }

    @Override
    public void AddFirstAnswerBudFragment(final HomeQAfragmentDataModel dataModel) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DetachSubFragments();
                answerFragment.dataModel = dataModel;
                answerFragment.isFromMainTabScreen = true;
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.attatch_fragment_right_to_left);
                transaction.add(R.id.fragment_id_full_screen, answerFragment, "1");
                transaction.commitAllowingStateLoss();
            }
        });
    }


    HomeQAfragmentDataModel dataModelAdd;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void AddDiscussAnswerFragment(final HomeQAfragmentDataModel dataModel) {
        if (dataModel.getUser_id() == user.getUser_id() && dataModel.getShow_ads() == 1 && dataModel.getAnswerCount() >= 1) {
            dataModelAdd = dataModel;
            JSONObject object = new JSONObject();
            try {
                object.put("show_ads", 0);
                object.put("question_id", dataModel.getId());
            } catch (JSONException e) {
                e.printStackTrace();

            }
            if (getActivity() != null && getActivity() instanceof HomeActivity) {
//                ((HomeActivity) getActivity()).mInterstitialAd.show();
            }

            new VollyAPICall(getContext(), false, URL.change_show_ads_status
                    , object, user.getSession_key(), Request.Method.POST, QA_HomeTabFragment.this, APIActions.ApiActions.change_show_ads_status);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DetachSubFragments();
                    discussQuestionFragment.dataModel = dataModel;
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
                    transaction.add(R.id.fragment_id_full_screen, discussQuestionFragment, "1");
                    transaction.commitAllowingStateLoss();
                }
            });
//            QADetailActivity.dataModel = dataModel;
//            GoTo(getContext(), QADetailActivity.class);
            //TODO ADD DISPLAY
        } else if (dataModel.getAnswerCount() > 0 && (dataModel.getUser_id() != user.getUser_id() || dataModel.getUser_id() == user.getUser_id())) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DetachSubFragments();
                    discussQuestionFragment.dataModel = dataModel;
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
                    transaction.add(R.id.fragment_id_full_screen, discussQuestionFragment, "1");
                    transaction.commitAllowingStateLoss();
                }
            });
//            QADetailActivity.dataModel = dataModel;
//            GoTo(getContext(), QADetailActivity.class);
        } else if (dataModel.getAnswerCount() == 0 && dataModel.getUser_id() == user.getUser_id()) {
            QAUserNotifyScreenActivity.dataModel = dataModel;
            GoTo(getActivity(), QAUserNotifyScreenActivity.class);
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DetachSubFragments();
                    discussQuestionFragment.dataModel = dataModel;
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
                    transaction.add(R.id.fragment_id_full_screen, discussQuestionFragment, "1");
                    transaction.commitAllowingStateLoss();
                }
            });
//            QADetailActivity.dataModel = dataModel;
//            GoTo(getContext(), QADetailActivity.class);
        }
    }


    @Override
    public void AddReplyAnswerFragment(final HomeQAfragmentDataModel dataModel) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DiscussQuestionFragment.isRefreshable = true;
                answerFragment.dataModel = dataModel;
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.attatch_fragment_right_to_left);
                transaction.add(R.id.fragment_id_full_screen, answerFragment, "1");
                transaction.commitAllowingStateLoss();
            }
        });
    }

    @Override
    public void EditReplyAnswerFragment(final HomeQAfragmentDataModel dataModel, final QuestionAnswersDataModel answersDataModel) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DiscussQuestionFragment.isRefreshable = true;
                answerEditFragment = new EditAnswerFragment();
                answerEditFragment.dataModel = answersDataModel;
                answerEditFragment.setOnClickListener(QA_HomeTabFragment.this);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.attatch_fragment_right_to_left);
                transaction.add(R.id.fragment_id_full_screen, answerEditFragment, "1");
                transaction.commitAllowingStateLoss();
            }
        });
    }

    public void SearchFromKeyword(String Keyword) {
        main_data.clear();
        recyler_adapter.notifyDataSetChanged();
        HideKeyboard(getActivity());
        refreshLayout.setRefreshing(true);
        JSONObject object = new JSONObject();
        pathUrl = search_question + "?query=" + Keyword + "&skip=";
        pages = 0;
        isAppiCallActive = true;
        new VollyAPICall(getContext(), false, pathUrl + 0, object, user.getSession_key(), Request.Method.GET, QA_HomeTabFragment.this, get_my_question);

    }

    public void DetachSubFragments() {
        if (answerFragment.isVisible()) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
            transaction.remove(answerFragment);
            transaction.commitAllowingStateLoss();
        }

        if (discussQuestionFragment.isVisible()) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_out, R.anim.fade_out);
            transaction.remove(discussQuestionFragment);
            transaction.commitAllowingStateLoss();
        }

        if (replyQuestionFragment.isVisible()) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_out, R.anim.fade_out);
            transaction.remove(replyQuestionFragment);
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Search_field != null) {
            Search_field.setText("");
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static void ReloadData() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(true);
            JSONObject jsonObject = new JSONObject();
            pathUrl = get_questions + "?skip=";
            pages = 0;
            isAppiCallActive = true;
            new VollyAPICall(context, false, pathUrl + pages, jsonObject, user.getSession_key(), Request.Method.GET, responseListner, get_my_question);
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void InitRefreshLayout(View view) {
        recyclerView = view.findViewById(R.id.qa_recyler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyler_adapter = new QAHomeFragmentRecylerAdapter(getActivity(), detectMenuBarHideListner, QA_HomeTabFragment.this, QA_HomeTabFragment.this, main_data);
        recyler_adapter.setClickListener(this);
        recyclerView.setAdapter(recyler_adapter);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.parseColor("#0083cb"));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                JSONObject jsonObject = new JSONObject();
                if (Search_field.getText().toString().trim().length() > 0) {
                    Search_field.setText("");
                    Search_field.clearFocus();
                }
                if (lower_case_key != null && lower_case_key.length() > 0) {
                    pathUrl = get_questions + "?sort=" + lower_case_key + "&skip=";
                    pages = 0;
                    isAppiCallActive = true;
                    new VollyAPICall(getContext(), false, pathUrl + pages, jsonObject, user.getSession_key(), Request.Method.GET, QA_HomeTabFragment.this, get_my_question);
                } else {
                    pathUrl = get_questions + "?skip=";
                    pages = 0;
                    isAppiCallActive = true;
                    new VollyAPICall(getContext(), false, pathUrl + pages, jsonObject, user.getSession_key(), Request.Method.GET, QA_HomeTabFragment.this, get_my_question);
                }

            }
        });
//        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
//                int lastVisible = layoutManager.findFirstCompletelyVisibleItemPosition();
//                if (lastVisible < 1) {
//                    refreshLayout.setEnabled(true);
//                }
//            }
//        });
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                Log.d("vissible", lastVisible + "");
                Log.d("vissible_t", (lastVisible % 10) + "");
                if (lastVisible >= (pages + 1) * 9 && !isAppiCallActive) {
                    if ((lastVisible % 9) == 0) {
                        pages = pages + 1;
                        JSONObject object = new JSONObject();
                        String url = "";
                        refreshLayout.setEnabled(true);
                        new VollyAPICall(view.getContext(), false, pathUrl + pages, object, user.getSession_key(), Request.Method.GET, QA_HomeTabFragment.this, get_my_question);
                    }
                }
            }
        });
    }

    @Override
    public void onBackButtonClick() {
        HideKeyboard(getActivity());
        if (DiscussQuestionFragment.isRefreshable)
            DiscussQuestionFragment.RefreshData();
    }

    @Override
    public void onReportClick(int position) {
        if (report.isSlide()) {
            report.SlideUp();
        } else {
            report.SlideDown(position, report_main_question_list(), QA_HomeTabFragment.this);
        }
    }

    /* is_list_open[0] = false;
                    slide.hide();*/
    public void InitHeaderSlideView(View view) {
        RelativeLayout slide_header_list;
        final ImageView list_indicator;
        ImageView list_indicator_close;
        final RelativeLayout indicator_close_layout;

        final RelativeLayout Indicator_layout;
        slide_header_list = view.findViewById(R.id.slide_header_list);
        slide_header_list.setClickable(false);
        slide_header_list.setOnClickListener(null);
        slide_header_list.setOnTouchListener(null);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        q_a_header_list_recyler_view = view.findViewById(R.id.q_a_header_recyler_view);
        q_a_header_list_recyler_view.setLayoutManager(layoutManager1);
        list_indicator = view.findViewById(R.id.indicator);
        Indicator_layout = view.findViewById(R.id.indicator_layout);
        list_indicator_close = view.findViewById(R.id.indicator_close);
        indicator_close_layout = view.findViewById(R.id.indicator_close_layout);
        list_indicator.setVisibility(View.VISIBLE);
        Indicator_layout.setVisibility(View.VISIBLE);
        indicator_close_layout.setVisibility(View.VISIBLE);
        list_indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_indicator.setVisibility(View.GONE);
                Indicator_layout.setVisibility(View.GONE);
                indicator_close_layout.setVisibility(View.VISIBLE);
                is_list_open[0] = true;
                slide.show();
                ArrayList<QAHomeHeaderListDataModel> data = qa_home_header_data();
                data.get(SharedPrefrences.getInt("QA_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", getContext())).setSelected(SharedPrefrences.getBool("QA_HOME_HEADER_LIST_SELECTED_ITEM_INDEX_BOOL", getContext()));
                QAHeaderListRecylerAdapter recyler_adapter = new QAHeaderListRecylerAdapter(getContext(), data);
                recyler_adapter.setClickListener(QA_HomeTabFragment.this);
                q_a_header_list_recyler_view.setAdapter(recyler_adapter);
            }
        });
        indicator_close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_list_open[0] = false;
                slide.hide();
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
                            indicator_close_layout.setVisibility(View.VISIBLE);

                        }
                    }
                })
                .withStartGravity(Gravity.TOP)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == add_flag_q) {
            CustomeToast.ShowCustomToast(getContext(), "Reported successfully!", Gravity.TOP);
        } else if (APIActions.ApiActions.change_show_ads_status == apiActions) {

        } else {
            refreshLayout.setRefreshing(false);
            isAppiCallActive = false;
            try {
                JSONObject object = new JSONObject(response);
                JSONArray question_array = object.getJSONArray("successData");
                if (pages == 0) {
                    main_data.clear();
                    if (question_array.length() == 0) {
                        main_data.clear();
                        recyler_adapter.notifyDataSetChanged();
                        No_Question_Found.setVisibility(View.VISIBLE);
                    }
                }
                if (isFromAnswer) {
                    isFromAnswer = false;
                    No_Question_Found.setText("No Answers Found!");
                } else {
                    No_Question_Found.setText("No Questions Found!");
                }

                if (question_array.length() == 0) {
//                main_data.clear();
                    recyler_adapter.notifyDataSetChanged();
//                No_Question_Found.setVisibility(View.VISIBLE);
                } else {
                    No_Question_Found.setVisibility(View.GONE);
//                main_data.clear();
                    recyler_adapter.notifyDataSetChanged();
                    for (int x = 0; x < question_array.length(); x++) {
                        JSONObject quesiton_object = question_array.getJSONObject(x);
                        HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                        JSONObject user_object = quesiton_object.getJSONObject("get_user");
                        model.setUser_points(user_object.getInt("points"));
                        model.setUser_name(user_object.getString("first_name"));
                        model.setUser_name_dscription("asks...");
                        String user_photo = user_object.optString("image_path");
                        if (!user_photo.equalsIgnoreCase("null") && user_photo.length() > 6) {
                            model.setUser_photo(user_photo);
                        } else {
                            model.setUser_photo(user_object.optString("avatar"));
                        }
                        model.setUser_location(user_object.optString("location"));
                        model.setSpecial_icon(user_object.optString("special_icon"));
                        model.setQuestion(quesiton_object.getString("question"));
                        model.setId(quesiton_object.getInt("id"));
                        model.setShow_ads(quesiton_object.getInt("show_ads"));
                        model.setUser_id(quesiton_object.getInt("user_id"));
                        model.setCreated_at(quesiton_object.getString("created_at"));
                        model.setUpdated_at(quesiton_object.getString("updated_at"));
                        model.setIsAnswered(quesiton_object.getInt("is_answered_count"));
                        model.setQuestion_description(quesiton_object.getString("description"));
                        model.setAnswerCount(quesiton_object.getInt("get_answers_count"));
                        model.setGet_user_flag_count(quesiton_object.getInt("get_user_flag_count"));
                        model.setGet_user_likes_count(quesiton_object.getInt("get_user_likes_count"));
                        model.setUser_notify(quesiton_object.getInt("user_notify"));
                        JSONArray attachment_Array = quesiton_object.getJSONArray("attachments");
                        ArrayList<QuestionAnswersDataModel.Attachment> attachments = new ArrayList<>();
                        if (attachment_Array.length() > 0) {
                            for (int y = 0; y < attachment_Array.length(); y++) {
                                JSONObject jsn_obj = attachment_Array.getJSONObject(y);
                                QuestionAnswersDataModel.Attachment attachment = new QuestionAnswersDataModel.Attachment();
                                attachment.setId(jsn_obj.getInt("id"));
                                attachment.setAnswer_id(jsn_obj.getInt("question_id"));
                                attachment.setMedia_type(jsn_obj.getString("media_type"));
                                attachment.setUpload_path(jsn_obj.getString("upload_path"));
                                attachment.setPoster(jsn_obj.optString("poster"));
                                attachments.add(attachment);
                            }

                        }
                        model.setAttachments(attachments);
                        main_data.add(model);
                        recyler_adapter.notifyItemChanged(x);
                    }
                    recyler_adapter.notifyDataSetChanged();
                    if (isAnsswred) {
                        isAnsswred = false;
                        AddDiscussAnswerFragment(dataModel);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        refreshLayout.setRefreshing(false);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHeaderListItemClick(View view, int position) {
        refreshLayout.setRefreshing(true);
        ArrayList<QAHomeHeaderListDataModel> data = qa_home_header_data();
        lower_case_key = data.get(position).getTitle().toLowerCase().replace(" ", "_");
        SharedPrefrences.setBool("QA_HOME_HEADER_LIST_SELECTED_ITEM_INDEX_BOOL", true, getContext());
        slide.hide();
        if (lower_case_key.equalsIgnoreCase("TRENDING")) {
            lower_case_key = StringUtils.capitalizeFirstLetter("featured");
        }
        JSONObject jsonObject = new JSONObject();
        pathUrl = get_questions + "?sort=" + lower_case_key + "&skip=";
        pages = 0;
        isAppiCallActive = true;
        new VollyAPICall(getContext(), false, pathUrl + pages, jsonObject, user.getSession_key(), Request.Method.GET, QA_HomeTabFragment.this, get_my_question);
    }

    //UNANSWERED
    @Override
    public void onCrossListener(View view, int position) {
        refreshLayout.setRefreshing(true);
        ArrayList<QAHomeHeaderListDataModel> data = qa_home_header_data();
        SharedPrefrences.setBool("QA_HOME_HEADER_LIST_SELECTED_ITEM_INDEX_BOOL", false, getContext());
        lower_case_key = "";
        slide.hide();
        JSONObject jsonObject = new JSONObject();
        pathUrl = get_questions + "?sort=" + lower_case_key + "&skip=";
        pages = 0;
        isAppiCallActive = true;
        new VollyAPICall(getContext(), false, pathUrl + pages, jsonObject, user.getSession_key(), Request.Method.GET, QA_HomeTabFragment.this, get_my_question);
    }

    @Override
    public void OnSnedClicked(JSONObject data, int position) {
        JSONObject object = new JSONObject();
        main_data.get(position).setGet_user_flag_count(1);
        try {
            object.put("question_id", main_data.get(position).getId());
            object.put("is_flag", 1);
            object.put("reason", data.getString("reason"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyler_adapter.notifyItemChanged(position);
        new VollyAPICall(context, false, add_question_flag, object, user.getSession_key(), Request.Method.POST, QA_HomeTabFragment.this, add_flag_q);

    }

}
