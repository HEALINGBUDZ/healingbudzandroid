package com.codingpixel.healingbudz.activity.home.side_menu.my_questions;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.QuestionAnswersDataModel;
import com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.EditQuestionFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.QAUserNotifyScreenActivity;
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.adapter.MyQuestionsRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.fragment_back_button_listner.BackButtonClickListner;
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

import static com.codingpixel.healingbudz.activity.home.HomeActivity.showSubFragmentListner_object;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_groups;
import static com.codingpixel.healingbudz.network.model.URL.delete_question;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

public class MyQuestionActivity extends AppCompatActivity implements APIResponseListner, MyQuestionsRecylerAdapter.ItemClickListener, BackButtonClickListner {
    RecyclerView My_questions_recyler_view;
    ImageView Home, Back;
    LinearLayout Refresh;
    TextView No_Record_found;
    boolean isRefreshOnResume = false;
    MyQuestionsRecylerAdapter recyler_adapter;
    private boolean isAppiCallActive = false;
    ArrayList<HomeQAfragmentDataModel> main_data = new ArrayList<>();
    int pages = 0;
    SwipeRefreshLayout swipe_rf;

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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_question);

        ChangeStatusBarColor(MyQuestionActivity.this, "#171717");
        Back = (ImageView) findViewById(R.id.back_btn);
        No_Record_found = (TextView) findViewById(R.id.no_record_found);
        Refresh = (LinearLayout) findViewById(R.id.refresh);
        Refresh.setVisibility(View.VISIBLE);
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
                GoToHome(MyQuestionActivity.this, true);
                finish();
            }
        });
        My_questions_recyler_view = (RecyclerView) findViewById(R.id.my_questions_recyler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyQuestionActivity.this);
        My_questions_recyler_view.setLayoutManager(linearLayoutManager);
        recyler_adapter = new MyQuestionsRecylerAdapter(this, main_data);
        My_questions_recyler_view.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);

        final JSONObject object = new JSONObject();
        pages = 0;
        new VollyAPICall(MyQuestionActivity.this, false, URL.get_my_question + "?page_no=" + pages, object, user.getSession_key(), Request.Method.GET, MyQuestionActivity.this, get_groups);
        swipe_rf = (SwipeRefreshLayout) findViewById(R.id.swipe_rf);
        swipe_rf.setColorSchemeColors(Color.parseColor("#0083cb"));
        swipe_rf.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        swipe_rf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_rf.setRefreshing(true);
                isAppiCallActive = true;
                JSONObject jsonObject = new JSONObject();
                pages = 0;
                new VollyAPICall(MyQuestionActivity.this, false, URL.get_my_question + "?page_no=" + pages, object, user.getSession_key(), Request.Method.GET, MyQuestionActivity.this, get_groups);

            }
        });
        My_questions_recyler_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(My_questions_recyler_view.getLayoutManager());
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                Log.d("vissible", lastVisible + "");
                Log.d("vissible_t", (lastVisible % 10) + "");
                if (lastVisible >= (pages + 1) * 9 && !isAppiCallActive) {
                    if ((lastVisible % 9) == 0) {
                        pages = pages + 1;
                        JSONObject object = new JSONObject();
                        String url = "";
//                        Refresh.setVisibility(View.VISIBLE);
                        Refresh.setVisibility(View.VISIBLE);
                        new VollyAPICall(MyQuestionActivity.this, false, URL.get_my_question + "?page_no=" + pages, object, user.getSession_key(), Request.Method.GET, MyQuestionActivity.this, get_groups);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRefreshOnResume) {
            isRefreshOnResume = false;
            JSONObject object = new JSONObject();
            pages = 0;
            new VollyAPICall(MyQuestionActivity.this, false, URL.get_my_question + "?page_no=" + pages, object, user.getSession_key(), Request.Method.GET, MyQuestionActivity.this, get_groups);
        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("question", response);
        if (apiActions == APIActions.ApiActions.delete_question) {
            Log.d("question", response);
            CustomeToast.ShowCustomToast(getContext(), "Deleted Successfully!", Gravity.TOP);
        } else {
            Refresh.setVisibility(View.GONE);
            swipe_rf.setRefreshing(false);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("successData");
                if (pages == 0) {
                    main_data.clear();
                }
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject quesiton_object = jsonArray.getJSONObject(x);
                    HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                    JSONObject user_object = quesiton_object.getJSONObject("get_user");
                    model.setUser_name(user_object.getString("first_name"));
                    model.setUser_name_dscription("asks...");
                    String user_photo = user_object.optString("image_path");
                    if (!user_photo.equalsIgnoreCase("null") && user_photo.length() > 6) {
                        model.setUser_photo(user_photo);
                    } else {
                        model.setUser_photo(user_object.optString("avatar"));
                    }
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
                    model.setSpecial_icon(user_object.optString("special_icon"));
                    model.setUser_location(user_object.optString("location"));
                    model.setQuestion(quesiton_object.getString("question"));
                    model.setId(quesiton_object.getInt("id"));
                    model.setShow_ads(quesiton_object.getInt("show_ads"));
                    model.setUser_id(quesiton_object.getInt("user_id"));
                    model.setCreated_at(quesiton_object.getString("created_at"));
                    model.setUpdated_at(quesiton_object.getString("updated_at"));
                    model.setQuestion_description(quesiton_object.getString("description"));
                    model.setAnswerCount(quesiton_object.getInt("get_answers_count"));
                    model.setGet_user_flag_count(quesiton_object.getInt("get_flag_count"));
                    model.setGet_user_likes_count(quesiton_object.getInt("get_likes_count"));
                    main_data.add(model);
                }
                if (main_data.size() == 0) {
                    No_Record_found.setVisibility(View.VISIBLE);
                } else {
                    No_Record_found.setVisibility(View.GONE);
                }
                recyler_adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("question", response);
        Refresh.setVisibility(View.GONE);
        swipe_rf.setRefreshing(false);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (main_data.get(position).getAnswerCount() == 0 && main_data.get(position).getUser_id() == user.getUser_id()) {
            isRefreshOnResume = true;
            QAUserNotifyScreenActivity.dataModel = main_data.get(position);
            GoTo(view.getContext(), QAUserNotifyScreenActivity.class);
        } else {
            finish();
            showSubFragmentListner_object.ShowQuestions(main_data.get(position), true);
        }
    }

    @Override
    public void onEditClick(View view, int position) {
        isRefreshOnResume = true;
        EditQuestionFragment editQuestionFragment = new EditQuestionFragment(main_data.get(position));
        editQuestionFragment.setOnClickListener(MyQuestionActivity.this);
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.fade_out);
        transaction.add(R.id.main_fragment, editQuestionFragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onDeleteClick(View view, final int position) {
        new SweetAlertDialog(MyQuestionActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to delete this question?")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        JSONObject jsonObject = new JSONObject();
                        new VollyAPICall(MyQuestionActivity.this, false, delete_question + "/" + main_data.get(position).getId(), jsonObject, user.getSession_key(), Request.Method.GET, MyQuestionActivity.this, APIActions.ApiActions.delete_question);
                        main_data.remove(position);
                        recyler_adapter.notifyDataSetChanged();
                    }
                })
                .setCancelText("Close!")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();

//        JSONObject jsonObject = new JSONObject();
//        new VollyAPICall(MyQuestionActivity.this, false, delete_question + "/" + main_data.get(position).getId(), jsonObject, user.getSession_key(), Request.Method.GET, MyQuestionActivity.this, APIActions.ApiActions.delete_question);
//        main_data.remove(position);
//        recyler_adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackButtonClick() {
        if (isRefreshOnResume) {
            isRefreshOnResume = false;
            JSONObject object = new JSONObject();
            new VollyAPICall(MyQuestionActivity.this, false, URL.get_my_question, object, user.getSession_key(), Request.Method.GET, MyQuestionActivity.this, get_groups);
        }
    }
}
