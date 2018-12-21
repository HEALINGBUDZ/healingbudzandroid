package com.codingpixel.healingbudz.activity.home.side_menu.my_answers;

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
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.DataModel.QuestionAnswersDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.AnswerFragment;
import com.codingpixel.healingbudz.adapter.MyAnswersRecylerAdapter;
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
import static com.codingpixel.healingbudz.network.model.URL.delete_answer;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

public class MyAnswersActivity extends AppCompatActivity implements APIResponseListner, MyAnswersRecylerAdapter.ItemClickListener, BackButtonClickListner {
    RecyclerView My_questions_recyler_view;
    ImageView Home, Back;
    LinearLayout Refresh, refresh_btm;
    TextView No_Record_found;
    AnswerFragment answerFragment = new AnswerFragment();
    boolean IsRefresh = false;
    SwipeRefreshLayout swipe_rf;
    int pages = 0;

    MyAnswersRecylerAdapter recyler_adapter;
    EditAnswerFragment edit_answerfragment;
    ArrayList<QuestionAnswersDataModel> main_data = new ArrayList<>();
    private boolean isAppiCallActive = false;

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
        setContentView(R.layout.activity_my_answers);
        ChangeStatusBarColor(MyAnswersActivity.this, "#171717");
        Back = (ImageView) findViewById(R.id.back_btn);

        No_Record_found = (TextView) findViewById(R.id.no_record_found);
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
                GoToHome(MyAnswersActivity.this, true);
                finish();
            }
        });
        Refresh = (LinearLayout) findViewById(R.id.refresh);
        refresh_btm = (LinearLayout) findViewById(R.id.refresh_btm);


        My_questions_recyler_view = (RecyclerView) findViewById(R.id.my_questions_recyler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyAnswersActivity.this);
        My_questions_recyler_view.setLayoutManager(linearLayoutManager);
        recyler_adapter = new MyAnswersRecylerAdapter(this, main_data);
        My_questions_recyler_view.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
        final JSONObject object = new JSONObject();
        Refresh.setVisibility(View.VISIBLE);
        pages = 0;
        new VollyAPICall(MyAnswersActivity.this, false, URL.get_my_answers + "?page_no=" + pages, object, user.getSession_key(), Request.Method.GET, MyAnswersActivity.this, get_groups);
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
                new VollyAPICall(MyAnswersActivity.this, false, URL.get_my_answers + "?page_no=" + pages, object, user.getSession_key(), Request.Method.GET, MyAnswersActivity.this, get_groups);

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
                        refresh_btm.setVisibility(View.VISIBLE);
                        new VollyAPICall(MyAnswersActivity.this, false, URL.get_my_answers + "?page_no=" + pages, object, user.getSession_key(), Request.Method.GET, MyAnswersActivity.this, get_groups);
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (IsRefresh) {
////            IsRefresh = false;
////            Refresh.setVisibility(View.VISIBLE);
//            pages = 0;
//            new VollyAPICall(MyAnswersActivity.this, true, URL.get_my_answers + "?page_no=" + pages, new JSONObject(), user.getSession_key(), Request.Method.GET, MyAnswersActivity.this, get_groups);
//        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("question", response);
        if (apiActions == APIActions.ApiActions.delete_answer) {
            CustomeToast.ShowCustomToast(getContext(), "Deleted Successfully!", Gravity.TOP);
        } else {
            isAppiCallActive = false;
            swipe_rf.setRefreshing(false);
            Refresh.setVisibility(View.GONE);
            refresh_btm.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(response);
                JSONArray question_array = object.getJSONArray("successData");
                if (pages == 0) {
                    main_data.clear();
                }
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
                    dataModel.setFlag_by_user_count(json_object.getInt("flag_count"));
                    dataModel.setQuestion(json_object.getJSONObject("get_question").getString("question"));
                    dataModel.setQuestion_discription(json_object.getJSONObject("get_question").getString("description"));

                    //static adata

                    JSONObject user_object = json_object.getJSONObject("get_question").getJSONObject("get_user");
                    dataModel.setUser_first_name(user_object.getString("first_name"));
                    String user_photo = user_object.optString("image_path");
                    if (!user_photo.equalsIgnoreCase("null") && user_photo.length() > 6) {
                        dataModel.setUser_image_path(user_photo);
                        dataModel.setUser_avatar(user_object.optString("avatar"));
                    } else {
                        dataModel.setUser_image_path(user_object.optString("avatar"));
                        dataModel.setUser_avatar(user_object.optString("avatar"));
                    }
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

                    }
                    dataModel.setAttachments(attachments);
                    dataModel.setUserLocation(user_object.optString("location"));
                    dataModel.setQuestion_get_answers_count(json_object.getInt("get_answers_count"));
                    dataModel.setQuestion_get_flag_count(json_object.getInt("get_user_flag_count"));
                    dataModel.setQuestion_get_likes_count(json_object.getInt("get_user_likes_count"));
                    main_data.add(dataModel);
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
        Refresh.setVisibility(View.GONE);
        No_Record_found.setVisibility(View.VISIBLE);
        swipe_rf.setRefreshing(false);
    }

    @Override
    public void onItemClick(View view, int position) {
//        finish();
        showSubFragmentListner_object.ShowAnswers(genrateDataModal(main_data.get(position)), main_data.get(position).getQuestion_id(), true);
    }

    public HomeQAfragmentDataModel genrateDataModal(QuestionAnswersDataModel dataModel) {
        HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
        model.setQuestion(dataModel.getQuestion());
        model.setId(dataModel.getQuestion_id());
        model.setQuestion_description(dataModel.getQuestion_discription());
        model.setUser_photo(dataModel.getUser_image_path());
        model.setuser_name(dataModel.getUser_first_name());
        model.setCreated_at(dataModel.getCreated_at());
        model.setUpdated_at(dataModel.getUpdated_at());
        model.setAnswerCount(dataModel.getQuestion_get_answers_count());
        model.setGet_user_likes_count(dataModel.getQuestion_get_likes_count());
        model.setGet_user_flag_count(dataModel.getQuestion_get_flag_count());
        model.setUser_location(dataModel.getUserLocation());
        model.setUser_id(dataModel.getUser_id());
        model.setuser_name_dscription("asks...");
        return model;
    }

    @Override
    public void onItemDeleteClick(View view, final int position) {
        new SweetAlertDialog(MyAnswersActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to delete this answer?")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        JSONObject jsonObject = new JSONObject();
                        new VollyAPICall(MyAnswersActivity.this, false, delete_answer + "/" + main_data.get(position).getId(), jsonObject, user.getSession_key(), Request.Method.GET, MyAnswersActivity.this, APIActions.ApiActions.delete_answer);
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
//        new VollyAPICall(MyAnswersActivity.this, false, delete_answer + "/" + main_data.get(position).getId(), jsonObject, user.getSession_key(), Request.Method.GET, MyAnswersActivity.this, APIActions.ApiActions.delete_answer);
//        main_data.remove(position);
//        recyler_adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemEditClick(View view, int position) {
        if (Refresh.getVisibility() == View.GONE) {
            IsRefresh = true;

            edit_answerfragment = new EditAnswerFragment();
            edit_answerfragment.dataModel = main_data.get(position);
            edit_answerfragment.setOnClickListener(MyAnswersActivity.this);
            FragmentManager manager = this.getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.attatch_fragment_right_to_left);
            transaction.add(R.id.main_fragment, edit_answerfragment, "1");
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackButtonClick() {
        if (IsRefresh) {
            IsRefresh = false;
            Refresh.setVisibility(View.VISIBLE);
            pages = 0;
            new VollyAPICall(MyAnswersActivity.this, true, URL.get_my_answers + "?page_no=" + pages, new JSONObject(), user.getSession_key(), Request.Method.GET, MyAnswersActivity.this, get_groups);
        }
    }
}
