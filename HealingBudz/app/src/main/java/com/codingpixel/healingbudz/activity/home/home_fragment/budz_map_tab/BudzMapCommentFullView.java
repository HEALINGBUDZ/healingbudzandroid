package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BudzMapReviews;
import com.codingpixel.healingbudz.DataModel.ReplyBudz;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.ReportItView.Report;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.adapter.BudzMapCommentFullViewAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
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

import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_profile;

public class BudzMapCommentFullView extends AppCompatActivity implements BudzMapCommentFullViewAdapter.ItemClickListener, ReportSendButtonLstner, APIResponseListner {

    RecyclerView recycler_view;
    public static Report budz_map_report_fullscreen;
    BudzMapCommentFullViewAdapter adapter;
    ImageView back_btn;
    public static ArrayList<BudzMapReviews> reviews;
    RelativeLayout Main_layout;

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
        setContentView(R.layout.activity_strain_comment_full_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Main_layout = (RelativeLayout) findViewById(R.id.main_cntnt_strain);
        budz_map_report_fullscreen = new Report(this, this, "#932a88", "budz");
        Main_layout.addView(budz_map_report_fullscreen.getView());
        budz_map_report_fullscreen.InitSlide();

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        if (budz_map_item_clickerd_dataModel.getUser_id() == user.getUser_id()) {
            adapter = new BudzMapCommentFullViewAdapter(this, reviews, true);
        } else {
            adapter = new BudzMapCommentFullViewAdapter(this, reviews, false);
        }
        adapter.setClickListener(this);
        recycler_view.setAdapter(adapter);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        JSONObject object = new JSONObject();
        new VollyAPICall(BudzMapCommentFullView.this, true, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), object, user.getSession_key(), Request.Method.GET, BudzMapCommentFullView.this, get_budz_profile);
    }

    @Override
    public void OnSnedClicked(JSONObject data, int position) {

    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        try {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(response);
            JSONArray reviews_array = jsonObject.getJSONObject("successData").getJSONArray("review");
            reviews.clear();
//            JSONArray reviews_array = jsonObject.getJSONObject("successData").getJSONArray("review");
            for (int x = 0; x < reviews_array.length(); x++) {
                JSONObject review_object = null;

                review_object = reviews_array.getJSONObject(x);
                BudzMapReviews review = new BudzMapReviews();
                review.setId(review_object.getInt("id"));
                if (review_object.isNull("is_flaged")) {
                    review.setIs_user_flaged_count(0);
                } else {
                    review.setIs_user_flaged_count(1);
                }
                review.setReviewed_by(review_object.getInt("reviewed_by"));
                review.setReview(review_object.getString("text"));
                review.setCreated_at(review_object.getString("created_at"));
                review.setUpdated_at(review_object.getString("updated_at"));
                review.setUser_id(review_object.getJSONObject("user").getInt("id"));
                review.setUser_first_name(review_object.getJSONObject("user").getString("first_name"));
                review.setUser_avatar(review_object.getJSONObject("user").getString("avatar"));
                review.setUser_image_path(review_object.getJSONObject("user").getString("image_path"));
                review.setUser_point(review_object.getJSONObject("user").getInt("points"));
                review.setSpecial_icon(review_object.getJSONObject("user").getString("special_icon"));
                if (review_object.getInt("is_reviewed_count") == 0) {
                    review.setLiked(false);
                } else {
                    review.setLiked(true);
                }
                if (!review_object.isNull("rating")) {
                    review.setRating(review_object.getDouble("rating"));
                } else {
                    review.setRating(0);
                }
                review.setTotal_review(review_object.getInt("likes_count"));
                if (!review_object.isNull("attachments")) {
                    if (review_object.getJSONArray("attachments").length() > 0) {
                        review.setAttatchment_type(review_object.getJSONArray("attachments").getJSONObject(0).getString("type"));
                        review.setAttatchment_poster(review_object.getJSONArray("attachments").getJSONObject(0).optString("poster"));
                        review.setAttatchment_path(review_object.getJSONArray("attachments").getJSONObject(0).getString("attachment"));

                    }
                }
                if (!review_object.isNull("reply")) {
                    ReplyBudz replyBudz = new ReplyBudz();
                    replyBudz.setCreatedAt(review_object.getJSONObject("reply").getString("created_at"));
                    replyBudz.setUpdatedAt(review_object.getJSONObject("reply").getString("updated_at"));
                    replyBudz.setReply(review_object.getJSONObject("reply").getString("reply"));
                    replyBudz.setUserId(review_object.getJSONObject("reply").getInt("user_id"));
                    replyBudz.setBusinessReviewId(review_object.getJSONObject("reply").getInt("business_review_id"));
                    replyBudz.setId(review_object.getJSONObject("reply").getInt("id"));
                    review.setReply(replyBudz);
                }
                reviews.add(review);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {

    }
}
