package com.codingpixel.healingbudz.activity.settings.bussiness_listing_setting;

import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.AddNewBudzMapActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.GetUserLatLng;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.adapter.BudzMapHomeSettingRecylAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.Utilities.Constants.D_FORMAT_ONE;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_subscription;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_map;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class Bussiness_Listing_Setting_Activity extends AppCompatActivity implements APIResponseListner, BudzMapHomeSettingRecylAdapter.ItemClickListener, UserLocationListner {
    ImageView Back, Home;
    LinearLayout Item_One, Item_Two;
    RecyclerView MyBudzMap_recyler_view;
    LinearLayout Refresh, bussiness_head;
    TextView not_fount;
    BudzMapHomeSettingRecylAdapter recyler_adapter;
    ArrayList<BudzMapHomeDataModel> budz_map_test_data = new ArrayList<>();

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
        setContentView(R.layout.activity_bussiness__listing__setting_);
        ChangeStatusBarColor(Bussiness_Listing_Setting_Activity.this, "#171717");
        HideKeyboard(Bussiness_Listing_Setting_Activity.this);
        Refresh = (LinearLayout) findViewById(R.id.refresh);
        bussiness_head = (LinearLayout) findViewById(R.id.bussiness_head);
        bussiness_head.setVisibility(View.GONE);
        not_fount = (TextView) findViewById(R.id.not_fount);
        Refresh.setVisibility(View.VISIBLE);
        Back = (ImageView) findViewById(R.id.back_btn);
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
                GoToHome(Bussiness_Listing_Setting_Activity.this, true);
                finish();
            }
        });

        Item_One = (LinearLayout) findViewById(R.id.item_one);
        Item_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoTo(Bussiness_Listing_Setting_Activity.this, AddNewBudzMapActivity.class);
            }
        });
        Item_Two = (LinearLayout) findViewById(R.id.item_two);
//        Item_Two.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GoTo(Bussiness_Listing_Setting_Activity.this, AddNewBudzMapActivity.class);
//            }
//        });
        MyBudzMap_recyler_view = (RecyclerView) findViewById(R.id.setting_recyler_View);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Bussiness_Listing_Setting_Activity.this);
        MyBudzMap_recyler_view.setLayoutManager(linearLayoutManager);

        recyler_adapter = new BudzMapHomeSettingRecylAdapter(Bussiness_Listing_Setting_Activity.this, budz_map_test_data);
        MyBudzMap_recyler_view.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetUserLatLng().getUserLocation(getContext(), Bussiness_Listing_Setting_Activity.this);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == get_budz_map) {
            Refresh.setVisibility(View.GONE);
            try {
                budz_map_test_data.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray array = jsonObject.getJSONArray("successData");
                if (array.length() > 0) {
                    bussiness_head.setVisibility(View.VISIBLE);
                } else {
                    bussiness_head.setVisibility(View.GONE);

                }
                for (int x = 0; x < array.length(); x++) {
//                    isAppiCalled = true;
                    JSONObject object = array.getJSONObject(x);
                    BudzMapHomeDataModel dataModel = new BudzMapHomeDataModel();
                    if (object.isNull("business_type_id") || object.isNull("lat") || object.isNull("lng")) {
                    } else {
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
//                    dataModel.setLat(object.getDouble("lat"));
//                    dataModel.setLng(object.getDouble("lng"));
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
                        dataModel.setGet_user_save_count(object.getInt("get_user_save_count"));
                        if (!object.isNull("rating_sum")) {
                            dataModel.setRating_sum(Double.valueOf(D_FORMAT_ONE.format(object.getJSONObject("rating_sum").getDouble("total"))));
                            //                        dataModel.setRating_sum(object.optJSONObject("rating_sum").getDouble("total"));
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
                        if (object.getString("stripe_id").length() > 1 && !object.getString("stripe_id").equalsIgnoreCase("null")) {
                            dataModel.setIs_featured(1);
                            if(object.getJSONObject("subscriptions").getString("name").equalsIgnoreCase("healingbudz") ||
                                    object.getJSONObject("subscriptions").getString("name").equalsIgnoreCase("monthly_plan")){
                                dataModel.setSubScriptionName("Monthly");
                            }else if (object.getJSONObject("subscriptions").getString("name").equalsIgnoreCase("three_months")){
                                dataModel.setSubScriptionName("3 Months");
                            }else {
                                dataModel.setSubScriptionName("Annually");
                            }
                            if(!object.getJSONObject("subscriptions").isNull("ends_at")){
                                dataModel.setCanceled(true);
                                dataModel.setEndTime(DateConverter.convertDateTrial(object.getJSONObject("subscriptions").getString("ends_at")));
                            }else{
                                dataModel.setCanceled(false);
                            }

                            budz_map_test_data.add(dataModel);
                        } else {
                            dataModel.setIs_featured(0);
                        }
                    }

                }

                if (budz_map_test_data.size() > 0) {
//                    Refresh.setVisibility(View.GONE);
                    recyler_adapter.notifyDataSetChanged();
                    not_fount.setVisibility(View.GONE);
                    bussiness_head.setVisibility(View.VISIBLE);
//                    SetMarkers();
                } else {
                    bussiness_head.setVisibility(View.GONE);
                    not_fount.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (apiActions == add_subscription) {
            Log.d("log", response);
            try {
                JSONObject jsonObject = new JSONObject(response).getJSONObject("successData");
                Intent intent = new Intent(Bussiness_Listing_Setting_Activity.this, AddNewBudzMapActivity.class);
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
        Intent i = new Intent(Bussiness_Listing_Setting_Activity.this, BudzMapDetailsActivity.class);
        i.putExtra("budzmap_id", budz_map_test_data.get(position).getBusiness_type_id() + "");
        startActivity(i);
    }

    @Override
    public void refreschCall() {
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(Bussiness_Listing_Setting_Activity.this, false, URL.my_get_budz_map + "?lat=" + lat + "&lng=" + lng, jsonObject, user.getSession_key(), Request.Method.GET, Bussiness_Listing_Setting_Activity.this, get_budz_map);

    }
    Double lat;
    Double lng;
    @Override
    public void onLocationSuccess(Location location) {

        lat = location.getLatitude();

        lng = location.getLongitude();
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(Bussiness_Listing_Setting_Activity.this, false, URL.my_get_budz_map + "?lat=" + lat + "&lng=" + lng, jsonObject, user.getSession_key(), Request.Method.GET, Bussiness_Listing_Setting_Activity.this, get_budz_map);
    }

    @Override
    public void onLocationFailed(String Error) {
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(Bussiness_Listing_Setting_Activity.this, false
                , URL.my_get_budz_map + "?lat=" + SharedPrefrences.getString("lat_cur", this) + "&lng=" + SharedPrefrences.getString("lng_cur", this)
                , jsonObject, user.getSession_key(), Request.Method.GET, Bussiness_Listing_Setting_Activity.this, get_budz_map);
    }
}
