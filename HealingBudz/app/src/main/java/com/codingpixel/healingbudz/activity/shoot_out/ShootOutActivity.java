package com.codingpixel.healingbudz.activity.shoot_out;

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

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.DataModel.BudzMapSpecialProducts;
import com.codingpixel.healingbudz.DataModel.ShootOutDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.GetUserLatLng;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.AddNewBudzMapActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapPaidViewActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzFeedAlertDialog;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzMapStripeDialog;
import com.codingpixel.healingbudz.activity.shoot_out.dialog.ShootOutAlertDialog;
import com.codingpixel.healingbudz.activity.shoot_out.send_shoot_out_dialog.SendShootOutAlertDialog;
import com.codingpixel.healingbudz.adapter.ShoutOutRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.stripe.android.model.Token;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_subscription;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_shout_outs;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;

public class ShootOutActivity extends AppCompatActivity implements View.OnClickListener, ShoutOutRecylerAdapter.ItemClickListener, ShootOutAlertDialog.OnDialogFragmentClickListener, SendShootOutAlertDialog.OnDialogFragmentClickListener, APIResponseListner, BudzFeedAlertDialog.OnDialogFragmentClickListener, BudzMapStripeDialog.OnDialogFragmentClickListener, UserLocationListner {
    RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    ShoutOutRecylerAdapter recyler_adapter;
    ImageView Back, Home;
    ArrayList<BudzMapHomeDataModel> budzMapHomeDataModels = new ArrayList<>();
    LinearLayout Send_shoot_out;
    LinearLayout buy_shoot_out;
    ArrayList<ShootOutDataModel> list = new ArrayList<>();
    int ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot_out);
        InitRefreshLayout();
        Back = (ImageView) findViewById(R.id.back_btn);
        Home = (ImageView) findViewById(R.id.home_btn);
        Back.setOnClickListener(this);
        Home.setOnClickListener(this);
        Send_shoot_out = (LinearLayout) findViewById(R.id.send_shoot_out);
        buy_shoot_out = (LinearLayout) findViewById(R.id.buy_shoot_out);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ID = bundle.getInt("id");
        }
        buy_shoot_out.setOnClickListener(this);
        Send_shoot_out.setOnClickListener(this);
        refreshLayout.setRefreshing(true);
//        JSONObject jsonObject = new JSONObject();
//        new VollyAPICall(ShootOutActivity.this, false, URL.get_shout_outs, jsonObject, user.getSession_key(), Request.Method.GET, ShootOutActivity.this, get_shout_outs);
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

    Double lat = 0.0, lng = 0.0;

    public void InitRefreshLayout() {
        recyclerView = (RecyclerView) findViewById(R.id.buzz_feed_recyler_item);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyler_adapter = new ShoutOutRecylerAdapter(this, list);
        recyclerView.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
        refreshLayout.setEnabled(true);
        refreshLayout.setColorSchemeColors(Color.parseColor("#93268f"));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(ShootOutActivity.this, false, URL.user_shout_outs + "?lat=" + lat + "&lng=" + lng, jsonObject, user.getSession_key(), Request.Method.GET, ShootOutActivity.this, get_shout_outs);
                Log.d("wait", "load");
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.home_btn:
                GoToHome(this, true);
                finish();
                break;
            case R.id.send_shoot_out:
                if (budzMapHomeDataModels.size() > 0) {
                    SendShootOutAlertDialog sendShootOutAlertDialog = SendShootOutAlertDialog.newInstance(ShootOutActivity.this, budzMapHomeDataModels);
                    sendShootOutAlertDialog.show(this.getSupportFragmentManager(), "dialog");
                } else {
                    CustomeToast.ShowCustomToast(this, "You can't create a shout out with pending paid business!", Gravity.TOP);
                }
                break;
            case R.id.buy_shoot_out:
                Intent intent = new Intent(view.getContext(), BudzMapPaidViewActivity.class);
                startActivity(intent);
//                BudzFeedAlertDialog budzFeedAlertDialog = BudzFeedAlertDialog.newInstance(ShootOutActivity.this, false);
//                budzFeedAlertDialog.show(this.getSupportFragmentManager(), "dialog");
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetUserLatLng().getUserLocation(getContext(), this);

    }

    @Override
    public void onItemClick(View view, int position) {
        ShootOutAlertDialog shootOutAlertDialog = ShootOutAlertDialog.newInstance(ShootOutActivity.this, list.get(position));
        shootOutAlertDialog.show(this.getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onViewSpecialBtnClink(ShootOutAlertDialog dialog, ShootOutDataModel dataModel) {
        Intent budzmap_intetn = new Intent(getApplicationContext(), BudzMapDetailsActivity.class);
        budzmap_intetn.putExtra("budzmap_id", dataModel.getSub_user_id());
        budzmap_intetn.putExtra("view_specials", true);
        startActivity(budzmap_intetn);
        dialog.dismiss();
    }

    @Override
    public void onSendShootOutButtonClick(SendShootOutAlertDialog dialog) {
        refreshLayout.setRefreshing(true);
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(ShootOutActivity.this, false, URL.user_shout_outs + "?lat=" + lat + "&lng=" + lng, jsonObject, user.getSession_key(), Request.Method.GET, ShootOutActivity.this, get_shout_outs);
        Log.d("wait", "load");
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {

        if (apiActions == add_subscription) {
            Log.d("log", response);
            try {
                JSONObject jsonObject = new JSONObject(response).getJSONObject("successData");
                Intent intent = new Intent(this, AddNewBudzMapActivity.class);
                intent.putExtra("isSubcribed", true);
                intent.putExtra("sub_user_id", jsonObject.getString("id"));
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            budzMapHomeDataModels.clear();
            refreshLayout.setRefreshing(false);
            list.clear();
            Log.d("response", response);
            try {
                JSONArray jsonObject = new JSONObject(response).getJSONObject("successData").getJSONArray("shout_outs");
                for (int x = 0; x < jsonObject.length(); x++) {
                    JSONObject object = jsonObject.getJSONObject(x);
                    ShootOutDataModel shootOutDataModel = new ShootOutDataModel();
                    shootOutDataModel.setLikes_count(jsonObject.getJSONObject(x).getInt("likes_count"));
                    shootOutDataModel.setUserlike_count(jsonObject.getJSONObject(x).getInt("userlike_count"));
                    shootOutDataModel.setText("");//jsonObject.getJSONObject(x).getString("text")
                    shootOutDataModel.setId(object.getInt("id"));
                    shootOutDataModel.setUser_id(object.getInt("user_id"));
                    shootOutDataModel.setSub_user_id(object.getInt("sub_user_id"));
                    shootOutDataModel.setTitle(object.getString("title"));
                    shootOutDataModel.setMessage(object.getString("message"));
                    shootOutDataModel.setValidity_date(object.getString("validity_date"));
                    shootOutDataModel.setImage(object.optString("image"));
                    shootOutDataModel.setLat(object.optDouble("lat"));
                    shootOutDataModel.setLng(object.optDouble("lng"));
                    shootOutDataModel.setZip_code(object.optString("zip_code"));
                    if (!object.isNull("budz_special_id"))
                        shootOutDataModel.setBudzSpId(object.getInt("budz_special_id"));
                    else {
                        shootOutDataModel.setBudzSpId(-1);
                    }
                    shootOutDataModel.setPublic_location(object.optString("public_location"));
                    shootOutDataModel.setCreated_at(object.getString("created_at"));
                    shootOutDataModel.setUpdated_at(object.getString("updated_at"));
                    shootOutDataModel.setDistance(String.valueOf(Constants.distance(object.optDouble("lat"), object.optDouble("lng"), lat, lng)));//object.getString("distance")
                    shootOutDataModel.setReceived_From(object.getJSONObject("get_sub_user").getString("title"));
                    shootOutDataModel.setUser_Image_Path(object.getJSONObject("get_sub_user").getString("logo"));
                    shootOutDataModel.setAvatar("");
                    shootOutDataModel.setSpecial_icon("");
                    list.add(shootOutDataModel);
                }

                JSONArray budz_map_array = new JSONObject(response).getJSONObject("successData").getJSONArray("subusers");
                for (int x = 0; x < budz_map_array.length(); x++) {
                    JSONObject object = budz_map_array.getJSONObject(x);
                    if (!object.isNull("business_type_id")) {
                        BudzMapHomeDataModel dataModel = new BudzMapHomeDataModel();
                        dataModel.setId(object.getInt("id"));
                        dataModel.setUser_id(object.optInt("user_id"));
                        dataModel.setBusiness_type_id(object.optInt("business_type_id"));
                        dataModel.setTitle(object.optString("title"));
                        dataModel.setLogo(object.optString("logo"));
                        dataModel.setBanner(object.optString("banner"));
                        dataModel.setIs_organic(object.optInt("is_organic"));
                        dataModel.setIs_delivery(object.optInt("is_delivery"));
                        dataModel.setDescription(object.optString("description"));
                        dataModel.setLocation(object.optString("location"));
                        dataModel.setOthers_image(object.optString("others_image"));
                        dataModel.setLat(object.optDouble("lat"));
                        dataModel.setLng(object.optDouble("lng"));
                        dataModel.setIs_featured(1);
                        dataModel.setPhone(object.optString("phone"));
                        dataModel.setWeb(object.optString("web"));
                        dataModel.setFacebook(object.optString("facebook"));
                        dataModel.setTwitter(object.optString("twitter"));
                        dataModel.setInstagram(object.optString("instagram"));
                        dataModel.setCreated_at(object.optString("created_at"));
                        dataModel.setUpdated_at(object.optString("updated_at"));
                        dataModel.setStripe_id(object.optString("stripe_id"));
                        dataModel.setCard_brand(object.optString("card_brand"));
                        dataModel.setCard_last_four(object.optString("card_last_four"));
                        dataModel.setTrial_ends_at(object.optString("trial_ends_at"));
                        JSONArray specials = object.getJSONArray("special");
                        List<BudzMapSpecialProducts> sp_array = new ArrayList<>();
                        for (int yx = 0; yx < specials.length(); yx++) {
                            JSONObject sp = specials.getJSONObject(yx);
                            BudzMapSpecialProducts specialProducts = new BudzMapSpecialProducts();
                            specialProducts.setId(sp.getInt("id"));
                            specialProducts.setUser_id(sp.getInt("user_id"));
//                    specialProducts.setSub_user_id(sp.getInt("sub_user_id"));
                            specialProducts.setTitle(sp.getString("title"));
                            specialProducts.setMessage(sp.getString("description"));
                            specialProducts.setValidity_date(sp.getString("date"));
//                    specialProducts.setImage(sp.getString("image"));
//                    specialProducts.setLat(sp.getDouble("lat"));
//                    specialProducts.setLng(sp.getDouble("lng"));
//                    specialProducts.setZip_code(sp.getString("zip_code"));
//                    specialProducts.setPublic_location(sp.getString("public_location"));
                            specialProducts.setCreated_at(sp.getString("created_at"));
                            specialProducts.setUpdated_at(sp.getString("updated_at"));
                            if (!sp.isNull("user_like_count")) {
                                if (sp.getJSONArray("user_like_count").length() > 0) {
                                    specialProducts.setSaved(true);
                                } else {
                                    specialProducts.setSaved(false);
                                }
                            } else {
                                specialProducts.setSaved(false);
                            }
                            sp_array.add(specialProducts);
                        }
                        dataModel.setSpecialProducts(sp_array);
                        budzMapHomeDataModels.add(dataModel);
                    }
                }
                recyler_adapter.notifyDataSetChanged();
                if (budzMapHomeDataModels.size() > 0) {
                    Send_shoot_out.setVisibility(View.VISIBLE);
                    Send_shoot_out.setVisibility(View.VISIBLE);
                    buy_shoot_out.setVisibility(View.GONE);
                } else {
                    Send_shoot_out.setVisibility(View.GONE);
                    buy_shoot_out.setVisibility(View.VISIBLE);
                }
                if (user.isPaidBudz()) {
                    Send_shoot_out.setVisibility(View.VISIBLE);
                    buy_shoot_out.setVisibility(View.GONE);
                    Send_shoot_out.setVisibility(View.VISIBLE);
                } else {
                    Send_shoot_out.setVisibility(View.GONE);
                    buy_shoot_out.setVisibility(View.GONE);
                }

                if (ID != 0) {
                    buy_shoot_out.setVisibility(View.GONE);
                    Send_shoot_out.setVisibility(View.GONE);
                    int position = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() == ID) {
                            position = i;
                            break;
                        }
                    }
                    if (position != -1) {
                        ShootOutAlertDialog shootOutAlertDialog = ShootOutAlertDialog.newInstance(ShootOutActivity.this, list.get(position));
                        shootOutAlertDialog.show(this.getSupportFragmentManager(), "dialog");
                        ShootOutDataModel teds = list.get(position);
                        list.clear();
                        list.add(teds);
                    }

                } else {
//                    list.clear();
                }

                recyler_adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        refreshLayout.setRefreshing(false);
        Log.d("response", response);
    }

    @Override
    public void onCountinueFreeListingBtnClink(BudzFeedAlertDialog dialog) {
        Log.d("free", "btn click");
        Intent intent = new Intent(this, AddNewBudzMapActivity.class);
        intent.putExtra("isSubcribed", false);
        intent.putExtra("sub_user_id", "");
        startActivity(intent);
    }

    @Override
    public void onSubcribeNowBtnClick(BudzFeedAlertDialog dialog) {
        Log.d("free", "btn click");
//        BudzMapStripeDialog budzFeedAlertDialog = BudzMapStripeDialog.newInstance(ShootOutActivity.this, false);
//        budzFeedAlertDialog.show(this.getSupportFragmentManager(), "dialog");
    }

    @Override
    public void TokenGenrate(Token token) {
        Log.d("token", token.toString());
        JSONObject object = new JSONObject();
        try {
            object.put("stripe_token", token.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new VollyAPICall(this, true, URL.add_subscription, object, user.getSession_key(), Request.Method.POST, ShootOutActivity.this, add_subscription);
    }

    @Override
    public void onLocationSuccess(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        refreshLayout.setRefreshing(true);
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(ShootOutActivity.this, false, URL.user_shout_outs + "?lat=" + lat + "&lng=" + lng, jsonObject, user.getSession_key(), Request.Method.GET, ShootOutActivity.this, get_shout_outs);

    }

    @Override
    public void onLocationFailed(String Error) {
        lat = Double.parseDouble(SharedPrefrences.getString("lat_cur", this));
        lng = Double.parseDouble(SharedPrefrences.getString("lng_cur", this));
        refreshLayout.setRefreshing(true);
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(ShootOutActivity.this, false, URL.user_shout_outs + "?lat=" + lat + "&lng=" + lng, jsonObject, user.getSession_key(), Request.Method.GET, ShootOutActivity.this, get_shout_outs);

    }
}
