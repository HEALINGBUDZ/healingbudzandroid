package com.codingpixel.healingbudz.activity.home.side_menu.profile.budz_map_tab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.AddNewBudzMapActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment;
import com.codingpixel.healingbudz.adapter.BudzMapHomeRecylAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.Constants.D_FORMAT_ONE;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_subscription;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_map;

@SuppressLint("ValidFragment")
public class BudzMapFragment extends Fragment implements APIResponseListner, BudzMapHomeRecylAdapter.ItemClickListener {
    RecyclerView recyclerView;
    public static boolean isFromProfile = true;
    int USER_ID;
    RecyclerView MyBudzMap_recyler_view;
    ImageView Home, Back;
    BudzMapHomeRecylAdapter recyler_adapter;
    ArrayList<BudzMapHomeDataModel> budz_map_test_data = new ArrayList<>();
    boolean isAppiCalled = false;
    LinearLayout Refresh, main_cc;
    TextView not_found;

    public BudzMapFragment(int user_id) {
        this.USER_ID = user_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_profile_budz_map_tab_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyler_View);
        JSONObject jsonObject = new JSONObject();
        Refresh = (LinearLayout) view.findViewById(R.id.refresh);
        main_cc = (LinearLayout) view.findViewById(R.id.main_cc);
        not_found = view.findViewById(R.id.not_found);
        Refresh.setVisibility(View.VISIBLE);
        new VollyAPICall(view.getContext(), false, URL.user_my_get_budz_map + "/" + USER_ID + "?lat=" + SharedPrefrences.getString("lat_cur", view.getContext(),"0") + "&lng=" + SharedPrefrences.getString("lng_cur", view.getContext(),"0")
                , jsonObject, user.getSession_key(), Request.Method.GET, BudzMapFragment.this, get_budz_map);
//        ArrayList<BudzMapDataModel> dataModels = budz_map_test_data();
//        ArrayList<BudzMapDataModel> data = new ArrayList<>();
//        for (int x = 3; x < dataModels.size(); x++) {
//            data.add(dataModels.get(x));
//        }
        RecyclerView.LayoutManager layoutManager_home_saerch = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager_home_saerch);
        recyler_adapter = new BudzMapHomeRecylAdapter(getContext(), budz_map_test_data);
        recyclerView.setAdapter(recyler_adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyler_adapter.setClickListener(this);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Refresh.setVisibility(View.GONE);
        if (apiActions == get_budz_map) {
            try {
                budz_map_test_data.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray array = jsonObject.getJSONArray("successData");
                for (int x = 0; x < array.length(); x++) {
                    isAppiCalled = true;
                    JSONObject object = array.getJSONObject(x);
                    if (object.isNull("business_type_id") || object.isNull("lat") || object.isNull("lng")) {
                        BudzMapHomeDataModel dataModel = new BudzMapHomeDataModel();
                        dataModel.setId(object.getInt("id"));
                        dataModel.setUser_id(object.getInt("user_id"));
                        if (object.getString("stripe_id").length() > 1 && !object.getString("stripe_id").equalsIgnoreCase("null")) {
                            dataModel.setIs_featured(1);
                        } else {
                            dataModel.setIs_featured(0);
                        }
                        dataModel.setPending(true);
//                        budz_map_test_data_pendin.add(dataModel);
                    } else {
                        BudzMapHomeDataModel dataModel = new BudzMapHomeDataModel();
                        dataModel.setId(object.getInt("id"));
                        dataModel.setUser_id(object.getInt("user_id"));
                        dataModel.setBusiness_type_id(object.getInt("business_type_id"));
                        dataModel.setTitle(object.getString("title"));
                        dataModel.setLogo(object.getString("logo"));
                        dataModel.setOthers_image(object.optString("others_image"));
                        dataModel.setBanner(object.has("banner") ? object.getString("banner") : "");
                        dataModel.setBanner_full(object.has("banner_full") ? object.getString("banner_full") : "");
                        dataModel.setIs_organic(object.getInt("is_organic"));
                        dataModel.setIs_delivery(object.getInt("is_delivery"));
                        dataModel.setDescription(object.getString("description"));
                        dataModel.setLocation(object.getString("location"));
                        if (!object.isNull("lat") && !object.isNull("lng")) {
                            dataModel.setLat(object.getDouble("lat"));
                            dataModel.setLng(object.getDouble("lng"));
                        } else {
                            dataModel.setLat(0.0);
                            dataModel.setLng(0.0);
                        }
//                        dataModel.setLat(object.getDouble("lat"));
//                        dataModel.setLng(object.getDouble("lng"));
                        if (object.getString("stripe_id").length() > 1 && !object.getString("stripe_id").equalsIgnoreCase("null")) {
                            dataModel.setIs_featured(1);
                        } else {
                            dataModel.setIs_featured(0);
                        }
//                    dataModel.setIs_featured(object.getInt("is_featured"));
                        dataModel.setPhone(object.getString("phone"));
                        dataModel.setWeb(object.getString("web"));
                        dataModel.setFacebook(object.getString("facebook"));
                        dataModel.setTwitter(object.getString("twitter"));
                        dataModel.setInstagram(object.getString("instagram"));
                        dataModel.setCreated_at(object.getString("created_at"));
                        dataModel.setUpdated_at(object.getString("updated_at"));
                        dataModel.setStripe_id(object.getString("stripe_id"));
                        dataModel.setCard_brand(object.getString("card_brand"));
                        dataModel.setCard_last_four(object.getString("card_last_four"));
                        dataModel.setTrial_ends_at(object.getString("trial_ends_at"));
                        dataModel.setDistance(object.optDouble("distance"));
                        dataModel.setGet_user_save_count(1);
                        if (!object.isNull("rating_sum")) {
                            dataModel.setRating_sum(Double.valueOf(D_FORMAT_ONE.format(object.optJSONObject("rating_sum").getDouble("total"))));
                        }
                        ArrayList<BudzMapHomeDataModel.Reviews> reviews = new ArrayList<>();
                        JSONArray reviews_Array = object.getJSONArray("review");
                        for (int y = 0; y < reviews_Array.length(); y++) {
                            BudzMapHomeDataModel.Reviews reviews_model = new BudzMapHomeDataModel.Reviews();
                            JSONObject review_object = reviews_Array.getJSONObject(y);
                            reviews_model.setId(review_object.getInt("id"));
                            reviews_model.setSub_user_id(review_object.getInt("sub_user_id"));
                            reviews_model.setReviewed_by(review_object.getInt("reviewed_by"));
                            reviews_model.setText(review_object.getString("text"));
                            reviews_model.setCreated_at(review_object.getString("created_at"));
                            reviews.add(reviews_model);
                        }
                        dataModel.setReviews(reviews);
                        if (!object.isNull("timeing")) {
                            JSONObject timing_obj = object.getJSONObject("timeing");
                            BudzMapHomeDataModel.Timing timing = new BudzMapHomeDataModel.Timing();
                            timing.setId(timing_obj.getInt("id"));
                            timing.setSub_user_id(timing_obj.getInt("sub_user_id"));
                            timing.setMonday(timing_obj.optString("monday"));
                            timing.setTuesday(timing_obj.optString("tuesday"));
                            timing.setWednesday(timing_obj.optString("wednesday"));
                            timing.setThursday(timing_obj.optString("thursday"));
                            timing.setFriday(timing_obj.optString("friday"));
                            timing.setSaturday(timing_obj.optString("saturday"));
                            timing.setSunday(timing_obj.optString("sunday"));
                            timing.setMon_end(timing_obj.optString("mon_end"));
                            timing.setTue_end(timing_obj.optString("tue_end"));
                            timing.setWed_end(timing_obj.optString("wed_end"));
                            timing.setThu_end(timing_obj.optString("thu_end"));
                            timing.setFri_end(timing_obj.optString("fri_end"));
                            timing.setSat_end(timing_obj.optString("sat_end"));
                            timing.setSun_end(timing_obj.optString("sun_end"));
                            timing.setCreated_at(timing_obj.optString("created_at"));
                            dataModel.setTimings(timing);
                        }
                        JSONArray images_array = object.getJSONArray("get_images");
                        ArrayList<BudzMapHomeDataModel.Images> images = new ArrayList<>();
                        for (int y = 0; y < images_array.length(); y++) {
                            JSONObject image_object = images_array.getJSONObject(y);
                            BudzMapHomeDataModel.Images img = new BudzMapHomeDataModel.Images();
                            img.setId(image_object.getInt("id"));
                            img.setUser_id(image_object.getInt("user_id"));
                            img.setImage_path(image_object.getString("image"));
                            img.setIs_approved(0);
                            img.setIs_main(0);
                            img.setCreated_at(image_object.getString("created_at"));
                            img.setUpdated_at(image_object.getString("updated_at"));
                            images.add(img);
                        }
                        dataModel.setImages(images);
                        budz_map_test_data.add(dataModel);
                    }
                }
                Refresh.setVisibility(View.GONE);
                if (budz_map_test_data.size() > 0) {

                    recyler_adapter.notifyDataSetChanged();
                    not_found.setVisibility(View.GONE);
                    main_cc.setVisibility(View.VISIBLE);
//                    SetMarkers();
                } else {
                    main_cc.setVisibility(View.GONE);
                    not_found.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (apiActions == add_subscription) {
            Log.d("log", response);
            try {
                JSONObject jsonObject = new JSONObject(response).getJSONObject("successData");
                Intent intent = new Intent(getContext(), AddNewBudzMapActivity.class);
                intent.putExtra("isSubcribed", true);
                intent.putExtra("sub_user_id", jsonObject.getString("id"));
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Refresh.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View view, int position) {
        BudzMapHomeFragment.budz_map_item_clickerd_dataModel = budz_map_test_data.get(position);
        BudzMapHomeFragment.budz_map_item_clickerd_dataModel_abc = budz_map_test_data.get(position);
        Intent i = new Intent(getContext(), BudzMapDetailsActivity.class);
        i.putExtra("budzmap_id", budz_map_test_data.get(position).getBusiness_type_id() + "");
        startActivity(i);
    }

    @Override
    public void onEditClick(View view, int position) {

    }

    @Override
    public void onDeleteClick(View view, int position) {

    }

    @Override
    public void onEditPendingClick(View view, int position) {

    }

    @Override
    public void onDeletePendingClick(View view, int position) {

    }
}
