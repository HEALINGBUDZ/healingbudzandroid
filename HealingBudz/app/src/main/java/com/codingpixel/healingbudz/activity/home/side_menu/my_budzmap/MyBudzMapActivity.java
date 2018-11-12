package com.codingpixel.healingbudz.activity.home.side_menu.my_budzmap;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
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
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.AddNewBudzMapActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.DetailsBusinessInfoCannabitesTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.DetailsBusinessInfoEventTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.DetailsBusinessInfoMedicalTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.DetailsBusinessInfoTabFragment;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.GetUserLatLng;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.adapter.BudzMapHomeRecylAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
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
import static com.codingpixel.healingbudz.network.model.URL.delete_sub_user;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class MyBudzMapActivity extends AppCompatActivity implements APIResponseListner, BudzMapHomeRecylAdapter.ItemClickListener, UserLocationListner {
    RecyclerView MyBudzMap_recyler_view;
    RecyclerView my_budzmap_recyler_view_pending;
    ImageView Home, Back;
    BudzMapHomeRecylAdapter recyler_adapter;
    BudzMapHomeRecylAdapter recyler_adapter_pending;
    LinearLayout pending_layout, my_layout;
    TextView not_fount;
    SwipeRefreshLayout swipe_rf;
    ArrayList<BudzMapHomeDataModel> budz_map_test_data = new ArrayList<>();
    ArrayList<BudzMapHomeDataModel> budz_map_test_data_pendin = new ArrayList<>();
    boolean isAppiCalled = false;
    LinearLayout Refresh;
    Double lat = 0.0, lng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_budz_map);
        ChangeStatusBarColor(MyBudzMapActivity.this, "#171717");

        Back = (ImageView) findViewById(R.id.back_btn);
        not_fount = (TextView) findViewById(R.id.not_fount);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pending_layout = (LinearLayout) findViewById(R.id.pending_layout);
        my_layout = (LinearLayout) findViewById(R.id.my_layout);
        pending_layout.setVisibility(View.GONE);
        my_layout.setVisibility(View.GONE);
        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(MyBudzMapActivity.this, true);
                finish();
            }
        });
        Refresh = (LinearLayout) findViewById(R.id.refresh);
        Refresh.setVisibility(View.VISIBLE);
        swipe_rf = (SwipeRefreshLayout) findViewById(R.id.swipe_rf);
        swipe_rf.setColorSchemeColors(Color.parseColor("#0083cb"));
        swipe_rf.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        swipe_rf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_rf.setRefreshing(true);
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(MyBudzMapActivity.this, false, URL.my_get_budz_map + "?lat=" + lat + "&lng=" + lng, jsonObject, user.getSession_key(), Request.Method.GET, MyBudzMapActivity.this, get_budz_map);

            }
        });
        MyBudzMap_recyler_view = (RecyclerView) findViewById(R.id.my_budzmap_recyler_view);
        my_budzmap_recyler_view_pending = (RecyclerView) findViewById(R.id.my_budzmap_recyler_view_pending);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyBudzMapActivity.this);
        MyBudzMap_recyler_view.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager_pending = new LinearLayoutManager(MyBudzMapActivity.this);
        my_budzmap_recyler_view_pending.setLayoutManager(linearLayoutManager_pending);
        recyler_adapter = new BudzMapHomeRecylAdapter(this, budz_map_test_data, false);
        recyler_adapter_pending = new BudzMapHomeRecylAdapter(this, budz_map_test_data_pendin, false);
        MyBudzMap_recyler_view.setAdapter(recyler_adapter);
        my_budzmap_recyler_view_pending.setAdapter(recyler_adapter_pending);
        recyler_adapter.setClickListener(this);
        recyler_adapter_pending.setClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetUserLatLng().getUserLocation(getContext(), MyBudzMapActivity.this);
//        JSONObject jsonObject = new JSONObject();
//        new VollyAPICall(MyBudzMapActivity.this, false, URL.my_get_budz_map, jsonObject, user.getSession_key(), Request.Method.GET, MyBudzMapActivity.this, get_budz_map);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == get_budz_map) {
            Refresh.setVisibility(View.GONE);
            try {
                swipe_rf.setRefreshing(false);
                budz_map_test_data.clear();
                budz_map_test_data_pendin.clear();
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
                        budz_map_test_data_pendin.add(dataModel);
                    } else {
                        BudzMapHomeDataModel dataModel = new BudzMapHomeDataModel();
                        dataModel.setId(object.getInt("id"));
                        dataModel.setUser_id(object.getInt("user_id"));
                        dataModel.setBusiness_type_id(object.getInt("business_type_id"));
                        dataModel.setPending(false);
                        dataModel.setTitle(object.getString("title"));
                        dataModel.setLogo(object.getString("logo"));
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
                        dataModel.setEmail(object.getString("email"));
                        dataModel.setTwitter(object.getString("twitter"));
                        dataModel.setOthers_image(object.optString("others_image"));
                        dataModel.setInstagram(object.getString("instagram"));
                        dataModel.setCreated_at(object.getString("created_at"));
                        dataModel.setUpdated_at(object.getString("updated_at"));
                        dataModel.setStripe_id(object.getString("stripe_id"));
                        dataModel.setCard_brand(object.getString("card_brand"));
                        dataModel.setCard_last_four(object.getString("card_last_four"));
                        dataModel.setTrial_ends_at(object.getString("trial_ends_at"));
                        dataModel.setDistance(object.getInt("distance"));
                        dataModel.setGet_user_save_count(object.getInt("get_user_save_count"));
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
                if (budz_map_test_data_pendin.size() > 0) {
                    pending_layout.setVisibility(View.VISIBLE);
                } else {
                    pending_layout.setVisibility(View.GONE);
                }
                recyler_adapter.notifyDataSetChanged();
                recyler_adapter_pending.notifyDataSetChanged();
                my_layout.setVisibility(View.VISIBLE);
                if (budz_map_test_data.size() > 0) {
                    not_fount.setVisibility(View.GONE);
                    recyler_adapter.notifyDataSetChanged();
                    recyler_adapter_pending.notifyDataSetChanged();
//                    SetMarkers();
                } else {
                    if (budz_map_test_data_pendin.size() > 0) {
                        not_fount.setVisibility(View.GONE);
                    } else {
                        not_fount.setVisibility(View.VISIBLE);
                    }
                    my_layout.setVisibility(View.GONE);
//                    not_fount.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                not_fount.setVisibility(View.VISIBLE);
            }

        } else if (apiActions == add_subscription) {
            Log.d("log", response);
            try {
                JSONObject jsonObject = new JSONObject(response).getJSONObject("successData");
                Intent intent = new Intent(MyBudzMapActivity.this, AddNewBudzMapActivity.class);
                intent.putExtra("isSubcribed", true);
                intent.putExtra("sub_user_id", jsonObject.getString("id"));
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == APIActions.ApiActions.delete_my_budz) {
            CustomeToast.ShowCustomToast(getContext(), "Deleted Successfully!", Gravity.TOP);
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Refresh.setVisibility(View.GONE);
        swipe_rf.setRefreshing(false);
    }

    @Override
    public void onItemClick(View view, int position) {
        BudzMapHomeFragment.budz_map_item_clickerd_dataModel = budz_map_test_data.get(position);
        BudzMapHomeFragment.budz_map_item_clickerd_dataModel_abc = budz_map_test_data.get(position);
        Intent i = new Intent(MyBudzMapActivity.this, BudzMapDetailsActivity.class);
        i.putExtra("budzmap_id", budz_map_test_data.get(position).getId());
        startActivity(i);
    }

    @Override
    public void onEditClick(View view, int position) {
        BudzMapHomeFragment.budz_map_item_clickerd_dataModel = budz_map_test_data.get(position);
        BudzMapHomeFragment.budz_map_item_clickerd_dataModel_abc = budz_map_test_data.get(position);
        DetailsBusinessInfoCannabitesTabFragment.isFresh = true;
        DetailsBusinessInfoEventTabFragment.isFresh = true;
        DetailsBusinessInfoMedicalTabFragment.isFresh = true;
        DetailsBusinessInfoTabFragment.isFresh = true;
        GoTo(MyBudzMapActivity.this, AddNewBudzMapActivity.class);
    }

    @Override
    public void onDeleteClick(View view, final int position) {
        new SweetAlertDialog(MyBudzMapActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to delete this bud?")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("sub_user_id", budz_map_test_data.get(position).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new VollyAPICall(MyBudzMapActivity.this, true, delete_sub_user, jsonObject, user.getSession_key(), Request.Method.POST, MyBudzMapActivity.this, APIActions.ApiActions.delete_my_budz);
                        budz_map_test_data.remove(position);
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
    }

    @Override
    public void onEditPendingClick(View view, int position) {
        BudzMapHomeFragment.budz_map_item_clickerd_dataModel = budz_map_test_data_pendin.get(position);
        BudzMapHomeFragment.budz_map_item_clickerd_dataModel_abc = budz_map_test_data_pendin.get(position);
        DetailsBusinessInfoCannabitesTabFragment.isFresh = true;
        DetailsBusinessInfoEventTabFragment.isFresh = true;
        DetailsBusinessInfoMedicalTabFragment.isFresh = true;
        DetailsBusinessInfoTabFragment.isFresh = true;
        GoTo(MyBudzMapActivity.this, AddNewBudzMapActivity.class);
    }

    @Override
    public void onDeletePendingClick(View view, final int position) {
        new SweetAlertDialog(MyBudzMapActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to delete this bud?")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("sub_user_id", budz_map_test_data_pendin.get(position).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new VollyAPICall(MyBudzMapActivity.this, true, delete_sub_user, jsonObject, user.getSession_key(), Request.Method.POST, MyBudzMapActivity.this, APIActions.ApiActions.delete_my_budz);
                        budz_map_test_data_pendin.remove(position);
                        recyler_adapter_pending.notifyDataSetChanged();
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
    public void onLocationSuccess(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(MyBudzMapActivity.this, false, URL.my_get_budz_map + "?lat=" + lat + "&lng=" + lng, jsonObject, user.getSession_key(), Request.Method.GET, MyBudzMapActivity.this, get_budz_map);
    }

    @Override
    public void onLocationFailed(String Error) {
        JSONObject jsonObject = new JSONObject();
        lat = Double.parseDouble(SharedPrefrences.getString("lat_cur", this,""));
        lng = Double.parseDouble(SharedPrefrences.getString("lng_cur", this,""));
        new VollyAPICall(MyBudzMapActivity.this, false
                , URL.my_get_budz_map + "?lat=" + SharedPrefrences.getString("lat_cur", this,"") + "&lng=" + SharedPrefrences.getString("lng_cur", this)
                , jsonObject, user.getSession_key(), Request.Method.GET, MyBudzMapActivity.this, get_budz_map);
//        "?lat=" + SharedPrefrences.getString("lat_cur", view.getContext()) + "&lng=" + SharedPrefrences.getString("lng_cur", view.getContext())
    }
}
