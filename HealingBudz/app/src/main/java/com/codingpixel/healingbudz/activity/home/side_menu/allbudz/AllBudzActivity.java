package com.codingpixel.healingbudz.activity.home.side_menu.allbudz;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import com.codingpixel.healingbudz.DataModel.User;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.AnswerFragment;
import com.codingpixel.healingbudz.activity.home.side_menu.my_answers.EditAnswerFragment;
import com.codingpixel.healingbudz.adapter.UserRecylerAdapter;
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

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.follow_unfollow_user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_groups;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

public class AllBudzActivity extends AppCompatActivity implements APIResponseListner, UserRecylerAdapter.ItemClickListener, BackButtonClickListner {
    RecyclerView My_questions_recyler_view;
    ImageView Home, Back;
    LinearLayout Refresh, refresh_btm;
    TextView No_Record_found;
    AnswerFragment answerFragment = new AnswerFragment();
    boolean IsRefresh = false;
    SwipeRefreshLayout swipe_rf;
    int pages = 0;

    UserRecylerAdapter recyler_adapter;
    EditAnswerFragment edit_answerfragment;
    ArrayList<User> main_data = new ArrayList<>();
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
        setContentView(R.layout.activity_all_budz);
        ChangeStatusBarColor(AllBudzActivity.this, "#171717");
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
                GoToHome(AllBudzActivity.this, true);
                finish();
            }
        });
        Refresh = (LinearLayout) findViewById(R.id.refresh);
        refresh_btm = (LinearLayout) findViewById(R.id.refresh_btm);
        My_questions_recyler_view = (RecyclerView) findViewById(R.id.my_questions_recyler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllBudzActivity.this);
        My_questions_recyler_view.setLayoutManager(linearLayoutManager);
        recyler_adapter = new UserRecylerAdapter(this, main_data);
        My_questions_recyler_view.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
        final JSONObject object = new JSONObject();
        Refresh.setVisibility(View.VISIBLE);
        pages = 0;
        new VollyAPICall(AllBudzActivity.this, false, URL.get_all_users + pages, object, user.getSession_key(), Request.Method.GET, AllBudzActivity.this, get_groups);
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
                new VollyAPICall(AllBudzActivity.this, false, URL.get_all_users + pages, object, user.getSession_key(), Request.Method.GET, AllBudzActivity.this, get_groups);

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
                        new VollyAPICall(AllBudzActivity.this, false, URL.get_all_users + pages, object, user.getSession_key(), Request.Method.GET, AllBudzActivity.this, get_groups);
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

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
                    User dataModel = new User();
                    dataModel.setId(json_object.getInt("id"));
                    dataModel.setUser_id(json_object.getInt("id"));
                    dataModel.setAvatar(json_object.optString("avatar"));
                    dataModel.setCover(json_object.optString("cover"));
                    dataModel.setImage_path(json_object.optString("image_path"));
                    dataModel.setSpecial_icon(json_object.optString("special_icon"));
                    dataModel.setFirst_name(json_object.optString("first_name"));
                    dataModel.setLast_name(json_object.optString("last_name"));
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
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        GoToProfile(view.getContext(), main_data.get(position).getUser_id());

    }

    int position = 0;

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

    }

    @Override
    public void onItemEditClick(final View view, final int position) {
        //FOLLOW API CALL
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("followed_id", main_data.get(position).getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new VollyAPICall(view.getContext(), true, URL.follow_user, jsonObject, user.getSession_key(), Request.Method.POST, new APIResponseListner() {
            @Override
            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                new SweetAlertDialog(view.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("User Followed Successfully")
                        .setContentText("")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                main_data.remove(position);
                                recyler_adapter.notifyItemRemoved(position);
                                recyler_adapter.notifyDataSetChanged();
                            }
                        }).show();
            }

            @Override
            public void onRequestError(String response, APIActions.ApiActions apiActions) {
                new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Network Error")
                        .setContentText("")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        }).show();
            }
        }, follow_unfollow_user);
    }

    @Override
    public void onBackButtonClick() {

    }
}
