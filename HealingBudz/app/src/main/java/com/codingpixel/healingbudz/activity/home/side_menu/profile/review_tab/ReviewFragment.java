package com.codingpixel.healingbudz.activity.home.side_menu.profile.review_tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.DataModel.BudzMapReviews;
import com.codingpixel.healingbudz.DataModel.ReviewsDataModel;
import com.codingpixel.healingbudz.DataModel.StrainDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.UserProfileReviewTabRecylerAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.Utilities.Constants.D_FORMAT_ONE;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_map;

public class ReviewFragment extends Fragment implements APIResponseListner {
    private int USER_ID;
    RecyclerView recyclerView;
    LinearLayout Refresh;
    TextView not_found;

    public ReviewFragment(int user_id) {
        this.USER_ID = user_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_profile_review_tab_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyler_View);
        Refresh = (LinearLayout) view.findViewById(R.id.refresh);
        not_found = view.findViewById(R.id.not_found);
        Refresh.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(getContext(), false, URL.user_my_get_reviews + "/" + USER_ID, jsonObject, user.getSession_key(), Request.Method.GET, ReviewFragment.this, get_budz_map);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerView.setNestedScrollingEnabled(false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == get_budz_map) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject array = jsonObject.getJSONObject("successData");
                Log.d("onNew: ", array.toString());
                ArrayList<BudzMapReviews> reviews = new ArrayList<>();
                JSONArray reviews_array = array.getJSONArray("budz_reviews");
                for (int x = 0; x < reviews_array.length(); x++) {
                    JSONObject review_object = reviews_array.getJSONObject(x);
                    BudzMapReviews review = new BudzMapReviews();
                    review.setId(review_object.getInt("sub_user_id"));
                    review.setReviewed_by(review_object.getInt("reviewed_by"));
                    review.setReview(review_object.getString("text"));
//                    review.setRating(review_object.getDouble("rating"));
                    review.setCreated_at(review_object.getString("created_at"));
                    review.setUpdated_at(review_object.getString("updated_at"));
                    review.setUser_id(review_object.getJSONObject("user").getInt("id"));
                    review.setUser_first_name(review_object.getJSONObject("user").getString("first_name"));
                    review.setUser_avatar(review_object.getJSONObject("user").getString("avatar"));
                    review.setUser_image_path(review_object.getJSONObject("user").getString("image_path"));
                    BudzMapHomeDataModel dataModel = new BudzMapHomeDataModel();
                    dataModel.setId(review_object.getJSONObject("bud").getInt("id"));
                    dataModel.setUser_id(review_object.getJSONObject("bud").getInt("user_id"));
                    dataModel.setBusiness_type_id(review_object.getJSONObject("bud").getInt("business_type_id"));
                    dataModel.setTitle(review_object.getJSONObject("bud").getString("title"));
                    if (!review_object.getJSONObject("bud").isNull("logo")) {
                        dataModel.setLogo(review_object.getJSONObject("bud").getString("logo"));
                    }
                    dataModel.setIs_organic(review_object.getJSONObject("bud").getInt("is_organic"));
                    dataModel.setIs_delivery(review_object.getJSONObject("bud").getInt("is_delivery"));
                    dataModel.setDescription(review_object.getJSONObject("bud").getString("description"));
                    dataModel.setLocation(review_object.getJSONObject("bud").getString("location"));
                    dataModel.setLat(review_object.getJSONObject("bud").getDouble("lat"));
                    dataModel.setLng(review_object.getJSONObject("bud").getDouble("lng"));
                    if (review_object.getJSONObject("bud").getString("stripe_id").length() > 1 && review_object.getJSONObject("bud").getString("stripe_id").equalsIgnoreCase("null")) {
                        dataModel.setIs_featured(1);
                    } else {
                        dataModel.setIs_featured(0);
                    }
                    dataModel.setOthers_image(review_object.getJSONObject("bud").optString("others_image"));
                    dataModel.setCreated_at(review_object.getJSONObject("bud").getString("created_at"));
                    dataModel.setUpdated_at(review_object.getJSONObject("bud").getString("updated_at"));
                    dataModel.setStripe_id(review_object.getJSONObject("bud").getString("stripe_id"));
                    dataModel.setCard_brand(review_object.getJSONObject("bud").getString("card_brand"));
                    dataModel.setCard_last_four(review_object.getJSONObject("bud").getString("card_last_four"));
                    dataModel.setTrial_ends_at(review_object.getJSONObject("bud").getString("trial_ends_at"));
                    dataModel.setDistance(review_object.getJSONObject("bud").getInt("distance"));
                    dataModel.setGet_user_save_count(1);
                    review.setBud(dataModel);
                    if (!review_object.isNull("rating")) {

                        review.setRating(Double.valueOf(D_FORMAT_ONE.format(review_object.getDouble("rating"))));
//                        .getInt("rating")
                    } else {
                        review.setRating(0);
                    }
                    if (!review_object.isNull("attachments")) {
                        if (review_object.getJSONArray("attachments").length() > 0) {
                            review.setAttatchment_type(review_object.getJSONArray("attachments").getJSONObject(0).getString("type"));
                            review.setAttatchment_poster(review_object.getJSONArray("attachments").getJSONObject(0).getString("poster"));
                            review.setAttatchment_path(review_object.getJSONArray("attachments").getJSONObject(0).getString("attachment"));
                        }
                    }
                    reviews.add(review);
                }


                ArrayList<BudzMapReviews> reviews_strain = new ArrayList<>();
                JSONArray reviews_array_strain = array.getJSONArray("strains_reviews");
                for (int xy = 0; xy < reviews_array_strain.length(); xy++) {
                    JSONObject review_object = reviews_array_strain.getJSONObject(xy);
                    BudzMapReviews review = new BudzMapReviews();
                    review.setId(review_object.getInt("id"));
                    review.setStrain_id(review_object.getInt("strain_id"));
                    review.setReviewed_by(review_object.getInt("reviewed_by"));
                    review.setReview(review_object.getString("review"));
                    review.setCreated_at(review_object.getString("created_at"));
                    review.setUpdated_at(review_object.getString("updated_at"));


                    if (!review_object.isNull("rating")) {
                        review.setRating(review_object.getJSONObject("rating").getInt("rating"));
                    } else {
                        review.setRating(0);
                    }
                    if (!review_object.isNull("attachment")) {
                        review.setAttatchment_type(review_object.getJSONObject("attachment").getString("type"));
                        review.setAttatchment_poster(review_object.getJSONObject("attachment").optString("poster"));
                        review.setAttatchment_path(review_object.getJSONObject("attachment").getString("attachment"));
                    }

                    ////Strain

                    JSONObject strain_object = review_object.getJSONObject("get_strain");
                    StrainDataModel strainDataModel = new StrainDataModel();
                    strainDataModel.setMy_strain_id(review_object.getInt("strain_id"));
                    strainDataModel.setMathces(0);
                    strainDataModel.setId(strain_object.getInt("id"));
                    strainDataModel.setType_id(strain_object.getInt("type_id"));
                    strainDataModel.setType_title(strain_object.getJSONObject("get_type").getString("title"));
                    strainDataModel.setTitle(strain_object.getString("title"));
                    strainDataModel.setOverview(strain_object.getString("overview"));
                    strainDataModel.setApproved(strain_object.getInt("approved"));
                    strainDataModel.setCreated_at(strain_object.getString("created_at"));
                    strainDataModel.setUpdated_at(strain_object.getString("updated_at"));
                    if (strain_object.optJSONObject("rating_sum") != null) {

                        strainDataModel.setRating_sum(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").getDouble("total"))));
                        strainDataModel.setRating(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").getDouble("total"))));
                    }

                    /////
                    review.setGet_strain(strainDataModel);
                    reviews_strain.add(review);

                }
                List<ReviewsDataModel> dataModels = new ArrayList<>();
                if (reviews.size() > 0)
                    dataModels.add(new ReviewsDataModel("My Budz Adz Reviews", reviews));
                if (reviews_strain.size() > 0)
                    dataModels.add(new ReviewsDataModel("My Strain Reviews", reviews_strain));
                UserProfileReviewTabRecylerAdapter adapter = new UserProfileReviewTabRecylerAdapter(dataModels);
                adapter.setOnGroupClickListener(new OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(int flatPos) {
                        return true;
                    }
                });
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                for (int x = 0; x < adapter.getGroups().size(); x++) {
                    if (!adapter.isGroupExpanded(adapter.getGroups().get(x))) {
                        adapter.toggleGroup(adapter.getGroups().get(x));
                    }
                }
                recyclerView.setNestedScrollingEnabled(false);
                if (dataModels.size() == 0) {
                    not_found.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    not_found.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                Refresh.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
                Refresh.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Refresh.setVisibility(View.GONE);
//        Refresh.setVisibility(View.GONE);
    }
}
