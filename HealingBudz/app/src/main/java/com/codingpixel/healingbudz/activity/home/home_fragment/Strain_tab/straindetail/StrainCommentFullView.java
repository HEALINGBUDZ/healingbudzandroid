package com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail;

import android.annotation.TargetApi;
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
import com.codingpixel.healingbudz.DataModel.StrainOverViewDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.ReportItView.Report;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.adapter.StrainCommentFullViewAdapter;
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

import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity.strainDataModel;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.tabs.StrainOverViewTabFragment.commentArray;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_strain_detail;

public class StrainCommentFullView extends AppCompatActivity implements StrainCommentFullViewAdapter.ItemClickListener, ReportSendButtonLstner, APIResponseListner {
    public static Report strain_report_full_screen;
    RecyclerView recycler_view;
    StrainCommentFullViewAdapter adapter;
    ImageView back_btn;
    RelativeLayout Main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strain_comment_full_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StrainCommentFullViewAdapter(this, commentArray);
        adapter.setClickListener(this);
        recycler_view.setAdapter(adapter);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Main_layout = (RelativeLayout) findViewById(R.id.main_cntnt_strain);
        strain_report_full_screen = new Report(this, this, "#f4c42f", "strain");
        Main_layout.addView(strain_report_full_screen.getView());
        strain_report_full_screen.InitSlide();

    }

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
    public void onItemClick(View view, int position) {
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(StrainCommentFullView.this, true, URL.get_strain_detail + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainCommentFullView.this, get_strain_detail);
    }

    @Override
    public void OnSnedClicked(JSONObject data, int position) {
        //use less
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            final ArrayList<StrainOverViewDataModel.Reviews> reviews = new ArrayList<>();
            JSONArray reviews_array = jsonObject.getJSONObject("successData").getJSONObject("strain").getJSONArray("get_review");
            for (int x = 0; x < reviews_array.length(); x++) {
                JSONObject review_object = reviews_array.getJSONObject(x);
                StrainOverViewDataModel.Reviews review = new StrainOverViewDataModel.Reviews();
                review.setId(review_object.getInt("id"));
                review.setIs_user_flaged_count(review_object.getInt("is_user_flaged_count"));
                review.setStrain_id(review_object.getInt("strain_id"));
                review.setReviewed_by(review_object.getInt("reviewed_by"));
                review.setReview(review_object.getString("review"));
                review.setCreated_at(review_object.getString("created_at"));
                review.setUpdated_at(review_object.getString("updated_at"));
                review.setUpdated_at(review_object.getString("updated_at"));
                review.setUser_id(review_object.getJSONObject("get_user").getInt("id"));
                review.setUser_first_name(review_object.getJSONObject("get_user").getString("first_name"));
                review.setUser_avatar(review_object.getJSONObject("get_user").getString("avatar"));
                review.setUser_image_path(review_object.getJSONObject("get_user").getString("image_path"));
                review.setUser_point(review_object.getJSONObject("get_user").getInt("points"));
                review.setTotal_likes(review_object.optInt("likes_count"));
                review.setSpecial_icon(review_object.getJSONObject("get_user").getString("special_icon"));
                if (review_object.getInt("is_reviewed_count") == 0) {
                    review.setLiked(false);
                } else {
                    review.setLiked(true);
                }
                if (!review_object.isNull("rating")) {
                    review.setRating(review_object.getJSONObject("rating").getDouble("rating"));
                } else {
                    review.setRating(0);
                }
                if (!review_object.isNull("attachment")) {
                    review.setAttatchment_type(review_object.getJSONObject("attachment").getString("type"));
                    review.setAttatchment_poster(review_object.getJSONObject("attachment").optString("poster"));
                    review.setAttatchment_path(review_object.getJSONObject("attachment").getString("attachment"));
                }
                reviews.add(review);
            }
            commentArray.clear();
            commentArray.addAll(reviews);

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {

    }
}
