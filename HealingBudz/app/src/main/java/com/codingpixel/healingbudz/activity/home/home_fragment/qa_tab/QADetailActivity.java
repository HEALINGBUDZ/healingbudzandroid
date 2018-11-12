package com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.DataModel.QuestionAnswersDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.ReportItView.Report;
import com.codingpixel.healingbudz.activity.home.side_menu.my_answers.EditAnswerFragment;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.dialog.UserFollowFollwingAlertDialog;
import com.codingpixel.healingbudz.adapter.DiscussQuestionAnswerRecylerViewAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.custome_dialog.save_discussion.SaveDiscussionAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.fragment_back_button_listner.BackButtonClickListner;
import com.codingpixel.healingbudz.interfaces.QAAddSubFragmentListner;
import com.codingpixel.healingbudz.interfaces.ReportButtonClickListner;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.Utilities.DateConverter.convertDate_with_month_name;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_answer_flag;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_question_like_dislike;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_followings;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_my_question;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_question_answers;
import static com.codingpixel.healingbudz.network.model.URL.add_question_like;
import static com.codingpixel.healingbudz.network.model.URL.get_answer;
import static com.codingpixel.healingbudz.network.model.URL.get_answers;
import static com.codingpixel.healingbudz.network.model.URL.get_question;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareHBContent;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;
import static com.codingpixel.healingbudz.test_data.QAHomeHeaderListData.report_question_list;

public class QADetailActivity extends AppCompatActivity implements ReportSendButtonLstner, APIResponseListner, ReportButtonClickListner, SaveDiscussionAlertDialog.OnDialogFragmentClickListener, QAAddSubFragmentListner, BackButtonClickListner {
    RelativeLayout Menu;
    TextView Question, Question_detail, Header_Question_Text;
    Button Budz_answers;
    RecyclerView answer_details_recyler_view;
    DiscussQuestionAnswerRecylerViewAdapter adapter;
    ImageView Menu_btn, Back_btn;
    android.support.design.widget.AppBarLayout Main_layout;
    static LinearLayout Refresh;
    public static boolean isRefreshable = false;
    ImageView UserProfile_img, profile_img_topi;
    TextView UserName_Location, Question_Created_Date;
    boolean isFavorite = false;
    private SwipeRefreshLayout refreshLayout;
    RelativeLayout HeaderQuestion;
    ExpandableRelativeLayout TopContent;
    RecyclerView.LayoutManager layoutManager;
    boolean isLayoutOpen = true;
    RelativeLayout Edit_question_dialog;
    ImageView EditQuestion_btn;
    ImageView Share_btn, Favorite_btn;
    boolean isShared = false;
    Report report;
    int x = 0;
    int xy = 0;
    String answerID = "";
    boolean isSingleAnswers;
    static Context context;
    static APIResponseListner listner;
    boolean isBackAble = false;
    ArrayList<QuestionAnswersDataModel> questionAnswersDataModels = new ArrayList<>();
    public static HomeQAfragmentDataModel dataModel;
    EditQuestionFragment editQuestionFragment = new EditQuestionFragment();
    QAAddSubFragmentListner qaAddSubFragmentListner;
    int question_id;
    boolean isAnswerd;
    ProgressDialog pd;
    boolean is_load_question;
    public static boolean isNewScreen = false;
    private Button Reply_Answer_btn;
    RelativeLayout toppp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qadetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DisableClickListner();
        x = 0;
        InitRefreshLayout();
        Init();
        qaAddSubFragmentListner = this;
        report = new Report(this, this);
        Main_layout = findViewById(R.id.main_layout);
        toppp = findViewById(R.id.toppp);
        Main_layout.addView(report.getView());
        report.InitSlide();
        listner = this;
        context = this;
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.main_layout);

//        findViewById(R.id.main_layoutt).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Edit_question_dialog.setVisibility(View.GONE);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isShared = false;
        if (isNewScreen) {
            isNewScreen = false;
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(this, false, URL.get_followings + "/" + user.getUser_id(), jsonObject, user.getSession_key(), Request.Method.GET, this, get_followings);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void Init() {
        Refresh = findViewById(R.id.refresh);
        Reply_Answer_btn = findViewById(R.id.reply_answer);
        UserProfile_img = findViewById(R.id.profile_image);
        profile_img_topi = findViewById(R.id.profile_img_topi);
        UserProfile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToProfile(QADetailActivity.this, dataModel.getUser_id());
            }
        });
        UserName_Location = findViewById(R.id.user_name_title);
        Question_Created_Date = findViewById(R.id.question_date);
        Question = findViewById(R.id.question);
        Budz_answers = findViewById(R.id.budz_answers);
        Header_Question_Text = findViewById(R.id.header_question_text);

        HeaderQuestion = findViewById(R.id.question_header_content);
//        TopContent = findViewById(R.id.top_content);
        Favorite_btn = findViewById(R.id.favorite_btn);
        Question_detail = findViewById(R.id.question_detail_text);
        answer_details_recyler_view = findViewById(R.id.anser_detail_recyler_view);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.scrollToPosition(0);
        answer_details_recyler_view.setLayoutManager(layoutManager);
        adapter = new DiscussQuestionAnswerRecylerViewAdapter(this, getSupportFragmentManager(), questionAnswersDataModels, qaAddSubFragmentListner, this, dataModel);
        answer_details_recyler_view.setAdapter(adapter);
        answer_details_recyler_view.scrollToPosition(0);
        answer_details_recyler_view.setNestedScrollingEnabled(false);
        adapter.notifyDataSetChanged();
        Menu_btn = findViewById(R.id.left_menu_btn_img);
        Menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                if (HomeActivity.drawerLayout != null) {
//                    HomeActivity.drawerLayout.openDrawer(Gravity.START);
//                }
            }
        });

        Back_btn = findViewById(R.id.left_back_btn_img);
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (isBackAble) {
            Back_btn.setVisibility(View.VISIBLE);
            Menu_btn.setVisibility(View.GONE);
        } else {
            Menu_btn.setVisibility(View.VISIBLE);
            Back_btn.setVisibility(View.GONE);
        }

        Share_btn = findViewById(R.id.share_btn);
        Share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!SharedPrefrences.getBool("IS_QA_My_SAVE_Dialog_Shown", getContext())) {
//                    SaveDiscussionAlertDialog saveDiscussionAlertDialog = SaveDiscussionAlertDialog.newInstance("SAVED DISCUSSION", "Q’s & A’s are saved in the app menu under My Saves", "Got it! Do not show again for Q’s & A’s I Save", DiscussQuestionFragment.this);
//                    saveDiscussionAlertDialog.show(getActivity().getSupportFragmentManager(), "dialog");
//                } else {
                JSONObject object = new JSONObject();
                try {
//                        object.put("msg", "Q: " + dataModel.getQuestion() + "\n Discription:\n" + dataModel.getQuestion_description());
                    object.put("id", dataModel.getId());
                    object.put("type", "Question");
                    object.put("msg", "Check out Healing Budz for your smartphone: " + URL.sharedBaseUrl + "/get-question-answers/" + dataModel.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isShared = true;
                ShareHBContent(QADetailActivity.this, object);
//                }

            }
        });
        Reply_Answer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qaAddSubFragmentListner.AddReplyAnswerFragment(dataModel);
            }
        });
//        TopContent.setListener(new ExpandableLayoutListener() {
//            @Override
//            public void onAnimationStart() {
//                Log.d("rea", "ad");
//            }
//
//            @Override
//            public void onAnimationEnd() {
//                Log.d("rea", "ad");
//            }
//
//            @Override
//            public void onPreOpen() {
//                Log.d("rea", "ad");
//            }
//
//            @Override
//            public void onPreClose() {
//                Log.d("rea", "ad");
//            }
//
//            @Override
//            public void onOpened() {
//                Log.d("rea", "ad");
//                isLayoutOpen = true;
//                HeaderQuestion.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onClosed() {
//                isLayoutOpen = false;
//                HeaderQuestion.setVisibility(View.VISIBLE);
//            }
//        });

        answer_details_recyler_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//                Log.d("isexpand", TopContent.isExpanded() + "");
//                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(answer_details_recyler_view.getLayoutManager());
//                int visibleItemCount = layoutManager.getChildCount();
//                int totalItemCount = layoutManager.getItemCount();
//                int lastVisible = layoutManager.findLastVisibleItemPosition();
//                if (lastVisible > 1) {
//                    if (isLayoutOpen) {
//                        TopContent.collapse();
//                    }
//                } else if (lastVisible < 1) {
//                    refreshLayout.setEnabled(true);
//                    if (!isLayoutOpen && xy == 0) {
//                        TopContent.expand();
//                        HeaderQuestion.setVisibility(View.GONE);
//                        xy++;
//                    }
//                }
            }
        });

        Edit_question_dialog = findViewById(R.id.edit_question_dialog);
        EditQuestion_btn = findViewById(R.id.edit_question);
        EditQuestion_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.fade_out);
                transaction.add(R.id.main_fragment, new EditQuestionFragment(dataModel));
                transaction.commitAllowingStateLoss();
//                if (!(questionAnswersDataModels.size() > 0)) {
//                    Edit_question_dialog.setVisibility(View.VISIBLE);
//                    Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.attatch_fragment_right_to_left);
//                    Edit_question_dialog.startAnimation(startAnimation);
//                }
            }
        });

        if (isAnswerd) {
            pd.Show();
            String url = get_question + "/" + question_id;
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(this, false, url, jsonObject, user.getSession_key(), Request.Method.GET, this, APIActions.ApiActions.get_my_question);
        } else if (is_load_question) {
            pd.Show();
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(this, false, get_question + "/" + question_id, jsonObject, user.getSession_key(), Request.Method.GET, this, APIActions.ApiActions.get_my_question);
        } else {
            setData();
        }


    }

    public void setData() {
        if (!(dataModel.getAnswerCount() > 0)) {
            if (dataModel.getUser_id() == user.getUser_id()) {
                EditQuestion_btn.setVisibility(View.VISIBLE);
            } else {
                EditQuestion_btn.setVisibility(View.GONE);
            }
        } else {
            EditQuestion_btn.setVisibility(View.GONE);
        }
        if (dataModel.getAnswerCount() == 0) {
            if (dataModel.getUser_id() == user.getUser_id()) {
                Reply_Answer_btn.setVisibility(View.GONE);
            }
        }
        Edit_question_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Edit_question_dialog.setVisibility(View.GONE);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.fade_out);
                transaction.add(R.id.main_fragment, new EditQuestionFragment(dataModel));
                transaction.commitAllowingStateLoss();
            }
        });
        if (dataModel.getGet_user_likes_count() == 1) {
            Favorite_btn.setImageResource(R.drawable.ic_fevorite);
        } else {
            Favorite_btn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        Favorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject object = new JSONObject();
                if (dataModel.getGet_user_likes_count() == 1) {
                    dataModel.setGet_user_likes_count(0);
                    Favorite_btn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    try {
                        object.put("question_id", dataModel.getId());
                        object.put("is_like", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (!SharedPrefrences.getBool("IS_QA_My_SAVE_Dialog_Shown", QADetailActivity.this)) {
                        SaveDiscussionAlertDialog saveDiscussionAlertDialog = SaveDiscussionAlertDialog.newInstance("SAVED DISCUSSION", "Q’s & A’s are saved in the app menu under My Saves", "Got it! Do not show again for Q’s & A’s I save", QADetailActivity.this);
                        saveDiscussionAlertDialog.show(QADetailActivity.this.getSupportFragmentManager(), "dialog");
                    }
                    dataModel.setGet_user_likes_count(1);
                    Favorite_btn.setImageResource(R.drawable.ic_fevorite);
                    try {
                        object.put("question_id", dataModel.getId());
                        object.put("is_like", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                new VollyAPICall(QADetailActivity.this, false, add_question_like, object, user.getSession_key(), Request.Method.POST, QADetailActivity.this, add_question_like_dislike);

            }
        });

        String path = "";
        if (dataModel.getUser_photo().length() > 8) {
            String pathImage = "";
            if (dataModel.getUser_photo().contains("facebook.com")
                    || dataModel.getUser_photo().contains("google.com")
                    || dataModel.getUser_photo().contains("googleusercontent.com")

                    || dataModel.getUser_photo().contains("http")
                    || dataModel.getUser_photo().contains("https")) {
                path = dataModel.getUser_photo();
            } else {
                path = images_baseurl + dataModel.getUser_photo();
            }
//            path = images_baseurl + dataModel.getUser_photo();
        } else {
            path = images_baseurl + dataModel.getUser_photo();
        }
        if (dataModel.getSpecial_icon().length() > 5) {
            profile_img_topi.setVisibility(View.VISIBLE);
            Glide.with(QADetailActivity.this)
                    .load(images_baseurl + dataModel.getSpecial_icon())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.topi_ic).error(R.drawable.noimage)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.d("ready", e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("ready", model);
//                            profile_img_topi.setImageDrawable(resource);
                            return false;
                        }
                    })
                    .into(profile_img_topi);
        } else {
            profile_img_topi.setVisibility(View.GONE);
        }
        Glide.with(QADetailActivity.this)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_user_profile_green).error(R.drawable.noimage)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d("ready", model);
//                        UserProfile_img.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(UserProfile_img);
        String[] str = dataModel.getUser_location().split(",");
        UserName_Location.setText(dataModel.getuser_name());
//        UserName_Location.setTextColor(Color.parseColor(Utility.getBudColor(dataModel.getUser_points())));
        Question_Created_Date.setText(convertDate_with_month_name(dataModel.getCreated_at()));
        Header_Question_Text.setText(dataModel.getQuestion());
        MakeKeywordClickableText(QADetailActivity.this, dataModel.getQuestion_description(), Question_detail);
        MakeKeywordClickableText(QADetailActivity.this, dataModel.getQuestion(), Question);
        Budz_answers.setText(dataModel.getAnswerCount() + " BUDZ ANSWERED");
//        refreshLayout.setRefreshing(true);
        questionAnswersDataModels.clear();
        adapter.notifyDataSetChanged();
//        if (refreshLayout.isRefreshing()) {
//            Refresh.setVisibility(View.GONE);
//        } else {
//            Refresh.setVisibility(View.VISIBLE);
//        }

        JSONObject jsonObject = new JSONObject();
        String url = "";
        if (isSingleAnswers) {
            if (answerID.length() == 0) {
                url = get_answers + "/" + dataModel.getId();
            } else {
                url = get_answer + "/" + answerID;
            }
        } else {
            url = get_answers + "/" + dataModel.getId();
        }
        new VollyAPICall(QADetailActivity.this, false, url, jsonObject, user.getSession_key(), Request.Method.GET, QADetailActivity.this, get_question_answers);
    }

    public void InitRefreshLayout() {
//        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
//        refreshLayout.setColorSchemeColors(Color.parseColor("#0083cb"));
//        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshLayout.setRefreshing(true);
////                pd.Show();
//                JSONObject jsonObject = new JSONObject();
//                new VollyAPICall(QADetailActivity.this, false, get_question + "/" + dataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, listner, APIActions.ApiActions.get_my_question);
////                if (!isLayoutOpen) {
////                    TopContent.expand();
////                    HeaderQuestion.setVisibility(View.GONE);
////                }
//                Log.d("wait", "load");
//            }
//        });
    }

    @Override
    public void OnSnedClicked(JSONObject data, int position) {
        JSONObject object = new JSONObject();
        try {
            object.put("answer_id", questionAnswersDataModels.get(position).getId());
            object.put("reason", data.getString("reason"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        questionAnswersDataModels.get(position).setFlag_by_user_count(1);
        adapter.notifyItemChanged(position);
        new VollyAPICall(this, false, URL.add_answer_flag, object, user.getSession_key(), Request.Method.POST, QADetailActivity.this, add_answer_flag);

    }

    @SuppressLint("ClickableViewAccessibility")
    public void DisableClickListner() {
        Menu = findViewById(R.id.menu);
        Menu.setOnClickListener(null);
        Menu.setClickable(false);
        Menu.setOnTouchListener(null);

    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
//        refreshLayout.setRefreshing(false);
/* JSONObject object = new JSONObject();
            try {
                object.put("show_ads", 0);
                object.put("question_id", dataModel.getId());
            } catch (JSONException e) {
                e.printStackTrace();

            }
            if (getActivity() != null && getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).mInterstitialAd.show();
            }

            new VollyAPICall(getContext(), false, URL.change_show_ads_status
                    , object, user.getSession_key(), Request.Method.POST, QA_HomeTabFragment.this, APIActions.ApiActions.change_show_ads_status);
*/
        if (apiActions == get_my_question) {
            pd.Dismis();
            try {
                JSONObject object = new JSONObject(response);
                JSONArray question_array = object.getJSONArray("successData");
                for (int x = 0; x < question_array.length(); x++) {
                    JSONObject quesiton_object = question_array.getJSONObject(x);
                    HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                    JSONObject user_object = quesiton_object.getJSONObject("get_user");
                    model.setUser_name(user_object.getString("first_name"));
                    model.setUser_name_dscription("asks...");
                    String user_photo = user_object.optString("image_path");
                    if (!user_photo.equalsIgnoreCase("null") && user_photo.length() > 6) {
                        model.setUser_photo(user_photo);
                        model.setAvatar(user_object.optString("avatar"));
                    } else {
                        model.setUser_photo(user_object.optString("avatar"));
                        model.setAvatar(user_object.optString("avatar"));
                    }
                    model.setUser_points(user_object.getInt("points"));
                    model.setUser_location(user_object.optString("location"));
                    model.setQuestion(quesiton_object.getString("question"));
                    model.setSpecial_icon(user_object.optString("special_icon"));
                    model.setShow_ads(quesiton_object.getInt("show_ads"));
                    model.setId(quesiton_object.getInt("id"));
                    model.setUser_id(quesiton_object.getInt("user_id"));
                    model.setCreated_at(quesiton_object.getString("created_at"));
                    model.setUpdated_at(quesiton_object.getString("updated_at"));
                    model.setQuestion_description(quesiton_object.getString("description"));
                    model.setAnswerCount(quesiton_object.getInt("get_answers_count"));
                    model.setGet_user_flag_count(quesiton_object.getInt("get_user_flag_count"));
                    model.setGet_user_likes_count(quesiton_object.getInt("get_user_likes_count"));
                    dataModel = model;
                    if (model.getShow_ads() == 1 && model.getUser_id() == user.getUser_id()) {
                        JSONObject objecta = new JSONObject();
                        try {
                            objecta.put("show_ads", 0);
                            objecta.put("question_id", dataModel.getId());
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
//                        if (getActivity() != null && getActivity() instanceof HomeActivity) {
////                            ((HomeActivity) getActivity()).mInterstitialAd.show();
//                        }

                        new VollyAPICall(this, false, URL.change_show_ads_status
                                , objecta, user.getSession_key(), Request.Method.POST, QADetailActivity.this, APIActions.ApiActions.change_show_ads_status);

                    }
                    setData();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == add_question_like_dislike || apiActions == add_answer_flag) {
            Log.d("response", response);
        } else if (apiActions == get_question_answers) {
//            refreshLayout.setRefreshing(false);
            try {
                JSONObject object = new JSONObject(response);
                JSONArray question_array = object.getJSONArray("successData");
                questionAnswersDataModels.clear();
                adapter.notifyDataSetChanged();
                for (int x = 0; x < question_array.length(); x++) {
                    JSONObject json_object = question_array.getJSONObject(x);
                    QuestionAnswersDataModel dataModel = new QuestionAnswersDataModel();
                    dataModel.setId(json_object.getInt("id"));
                    dataModel.setUser_id(json_object.getInt("user_id"));
                    dataModel.setQuestion_id(json_object.getInt("question_id"));
                    dataModel.setAnswer(json_object.getString("answer"));
                    dataModel.setCreated_at(json_object.getString("created_at"));
                    dataModel.setUpdated_at(json_object.getString("updated_at"));
                    dataModel.setAnswer_like_count(json_object.getInt("answer_like_count"));
                    dataModel.setAnswer_user_like_count(json_object.getInt("answer_user_like_count"));
                    dataModel.setFlag_by_user_count(json_object.getInt("flag_by_user_count"));
                    JSONObject user_object = json_object.getJSONObject("get_user");
                    dataModel.setUser_first_name(user_object.getString("first_name"));
                    dataModel.setUser_image_path(user_object.getString("image_path"));
                    dataModel.setSpecial_icon(user_object.optString("special_icon"));

                    dataModel.setUser_points(user_object.getInt("points"));
                    dataModel.setUser_avatar(user_object.getString("avatar"));
//                    JSONArray attachment_Array = json_object.getJSONArray("get_attachments"); CHANGE MADE BY USMAN AS SUPERVISED
                    JSONArray attachment_Array = json_object.getJSONArray("attachments");
                    ArrayList<QuestionAnswersDataModel.Attachment> attachments = new ArrayList<>();
                    if (attachment_Array.length() > 0) {
                        for (int y = 0; y < attachment_Array.length(); y++) {
                            JSONObject jsn_obj = attachment_Array.getJSONObject(y);
                            QuestionAnswersDataModel.Attachment attachment = new QuestionAnswersDataModel.Attachment();
                            attachment.setId(jsn_obj.getInt("id"));
                            attachment.setAnswer_id(jsn_obj.getInt("answer_id"));
                            attachment.setMedia_type(jsn_obj.getString("media_type"));
                            attachment.setUpload_path(jsn_obj.getString("upload_path"));
                            attachment.setPoster(jsn_obj.optString("poster"));
                            attachments.add(attachment);
                        }
                        dataModel.setAttachments(attachments);
                    }

                    dataModel.setReportButtonOpen(false);
                    if (json_object.getInt("is_following_count") == 0) {
                        dataModel.setFollow(false);
                    } else {
                        dataModel.setFollow(true);
                    }

                    questionAnswersDataModels.add(dataModel);
                    adapter.notifyItemInserted(x);
                }
                Refresh.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                Budz_answers.setText(questionAnswersDataModels.size() + " BUDZ ANSWERED");
                JSONObject jsonObject = new JSONObject();
//                new VollyAPICall(getContext(), false, URL.get_followings + "/" + user.getUser_id(), jsonObject, user.getSession_key(), Request.Method.GET, DiscussQuestionFragment.this, get_followings);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == get_followings) {
            JSONObject jsonObject = null;
            try {

                jsonObject = new JSONObject(response);

                JSONArray jsonArray = jsonObject.getJSONArray("successData");
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject object = jsonArray.getJSONObject(x);
                    UserFollowFollwingAlertDialog.UserFollowDataModel dataModel = new UserFollowFollwingAlertDialog.UserFollowDataModel();
                    dataModel.setId(object.getInt("id"));
                    dataModel.setIs_follow(true);
                    dataModel.setFollowing_Api(true);
                    dataModel.setName(object.getString("first_name"));
                    for (int i = 0; i < questionAnswersDataModels.size(); i++) {
                        if (questionAnswersDataModels.get(i).getUser_id() == dataModel.getId()) {
                            questionAnswersDataModels.get(i).setFollow(true);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                }, 500);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
//        refreshLayout.setRefreshing(false);
        if (apiActions == add_question_like_dislike || apiActions == add_answer_flag) {
            Log.d("response", response);
        } else {
//            refreshLayout.setRefreshing(false);
            try {
                JSONObject object = new JSONObject(response);
                CustomeToast.ShowCustomToast(this, object.getString("errorMessage"), Gravity.TOP);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReportClick(int position) {
        if (report.isSlide()) {
            report.SlideUp();
        } else {
            report.SlideDown(position, report_question_list(), this);
        }
    }

    @Override
    public void onOkClicked(SaveDiscussionAlertDialog dialog) {
        SharedPrefrences.setBool("IS_QA_My_SAVE_Dialog_Shown", true, this);
        JSONObject object = new JSONObject();
        try {
            object.put("msg", "Hey Bud! Check out the Healing Budz app for your smart phone.” Download it today from http://139.162.37.73/healingbudz/");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        ShareHBContent(getActivity(), object);
        dialog.dismiss();
    }

    @Override
    public void onCancelClicked(SaveDiscussionAlertDialog dialog) {
        JSONObject object = new JSONObject();
        try {
            object.put("msg", "Hey Bud! Check out the Healing Budz app for your smart phone.” Download it today from http://139.162.37.73/healingbudz/");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        ShareHBContent(getActivity(), object);
        dialog.dismiss();
    }

    @Override
    public void AddFirstAnswerBudFragment(final HomeQAfragmentDataModel dataModel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DiscussQuestionFragment.isRefreshable = true;
                answerFragment.dataModel = dataModel;
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.attatch_fragment_right_to_left);
                transaction.add(R.id.fragment_id_full_screen, answerFragment, "1");
                transaction.commitAllowingStateLoss();
            }
        });
    }

    public AnswerFragment answerFragment = new AnswerFragment();
    public EditAnswerFragment answerEditFragment = new EditAnswerFragment();

    @Override
    public void AddDiscussAnswerFragment(HomeQAfragmentDataModel dataModel) {

    }

    @Override
    public void AddReplyAnswerFragment(HomeQAfragmentDataModel dataModel) {

    }

    @Override
    public void EditReplyAnswerFragment(HomeQAfragmentDataModel dataModel, final QuestionAnswersDataModel answersDataModel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DiscussQuestionFragment.isRefreshable = true;
                answerEditFragment.dataModel = answersDataModel;
                answerEditFragment.setOnClickListener(QADetailActivity.this);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.attatch_fragment_right_to_left);
                transaction.add(R.id.fragment_id_full_screen, answerEditFragment, "1");
                transaction.commitAllowingStateLoss();
            }
        });
    }

    @Override
    public void onBackButtonClick() {
        HideKeyboard(this);
        if (QADetailActivity.isRefreshable)
            QADetailActivity.RefreshData();
    }

    public static void RefreshData() {
        if (Refresh != null && context != null) {
            Refresh.setVisibility(View.VISIBLE);
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(context, false, get_answers + "/" + dataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, listner, get_question_answers);
        }
    }

}
