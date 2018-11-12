package com.codingpixel.healingbudz.activity.home.side_menu.mystrains;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.MySavedDataModal;
import com.codingpixel.healingbudz.DataModel.StrainDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.tabs.EditMoreInfoAboutStrain;
import com.codingpixel.healingbudz.adapter.MySavesListRecylerAdapter;
import com.codingpixel.healingbudz.adapter.MyStrainsRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
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

import static com.codingpixel.healingbudz.Utilities.Constants.D_FORMAT_ONE;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.delete_my_save;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.filter_my_save;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_my_strains;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

public class MyStrainActivity extends AppCompatActivity implements APIResponseListner, MyStrainsRecylerAdapter.ItemClickListener, MySavesListRecylerAdapter.ItemClickListener {
    RecyclerView MyStrain_recyler_view;
    ImageView Home, Back;
    MyStrainsRecylerAdapter recyler_adapter;
    TextView not_fount;
    ArrayList<StrainDataModel> my_strains = new ArrayList<>();
    boolean Refersh = false;
    int pages = 0;
    private SwipeRefreshLayout refreshLayout;
    View user_indicator, saved_indicator;
    Button savedTab_two, userTab_one;
    private String API_urls = "";
    boolean isUserSaved = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
        setContentView(R.layout.activity_my_strain);
        ChangeStatusBarColor(MyStrainActivity.this, "#171717");
        API_urls = URL.get_my_strains;
        user_indicator = findViewById(R.id.user_indicator);
        saved_indicator = findViewById(R.id.saved_indicator);
        savedTab_two = findViewById(R.id.tab_two);
        userTab_one = findViewById(R.id.tab_one);
        saved_indicator.setVisibility(View.VISIBLE);
        userTab_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saved_indicator.setVisibility(View.GONE);
                isUserSaved = false;
                user_indicator.setVisibility(View.VISIBLE);
                refreshLayout.setRefreshing(true);
                pages = 0;
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(MyStrainActivity.this, false, API_urls + "?page_no=" + pages, jsonObject, user.getSession_key(), Request.Method.GET, MyStrainActivity.this, get_my_strains);

            }
        });
        savedTab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saved_indicator.setVisibility(View.VISIBLE);
                user_indicator.setVisibility(View.GONE);
                isUserSaved = true;
                refreshLayout.setRefreshing(true);
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(MyStrainActivity.this, false, URL.filter_my_save + "?filter=7", jsonObject, user.getSession_key(), Request.Method.GET, MyStrainActivity.this, filter_my_save);

            }
        });
        Back = (ImageView) findViewById(R.id.back_btn);
        not_fount = (TextView) findViewById(R.id.not_fount);
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
                GoToHome(MyStrainActivity.this, true);
                finish();
            }
        });

        MyStrain_recyler_view = (RecyclerView) findViewById(R.id.my_strains_recyler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyStrainActivity.this);
        MyStrain_recyler_view.setLayoutManager(linearLayoutManager);
        recyler_adapter = new MyStrainsRecylerAdapter(this, my_strains);
        recyler_adapter.setClickListener(this);
        MyStrain_recyler_view.setAdapter(recyler_adapter);


        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.parseColor("#FFF2BC2E"));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                if (!isUserSaved) {
                    pages = 0;
                    JSONObject jsonObject = new JSONObject();
                    new VollyAPICall(MyStrainActivity.this, false, API_urls + "?page_no=" + pages, jsonObject, user.getSession_key(), Request.Method.GET, MyStrainActivity.this, get_my_strains);
//                new VollyAPICall(MyStrainActivity.this, false, URL.get_my_strains, jsonObject, user.getSession_key(), Request.Method.GET, MyStrainActivity.this, get_my_strains);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    new VollyAPICall(MyStrainActivity.this, false, URL.filter_my_save + "?filter=7", jsonObject, user.getSession_key(), Request.Method.GET, MyStrainActivity.this, filter_my_save);
                }
            }

        });

        isUserSaved = true;
        refreshLayout.setRefreshing(true);
        JSONObject jsonObject = new JSONObject();
//        new VollyAPICall(MyStrainActivity.this, false, API_urls + "?page_no=" + pages, jsonObject, user.getSession_key(), Request.Method.GET, MyStrainActivity.this, get_my_strains);
//        new VollyAPICall(MyStrainActivity.this, false, URL.get_my_strains, jsonObject, user.getSession_key(), Request.Method.GET, MyStrainActivity.this, get_my_strains);
//        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(MyStrainActivity.this, false, URL.filter_my_save + "?filter=7", jsonObject, user.getSession_key(), Request.Method.GET, MyStrainActivity.this, filter_my_save);
        MyStrain_recyler_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (!isUserSaved) {
                    LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(MyStrain_recyler_view.getLayoutManager());
                    int lastVisible = layoutManager.findLastVisibleItemPosition();
                    Log.d("vissible", lastVisible + "");
                    Log.d("vissible_t", (lastVisible % 10) + "");
                    if (lastVisible >= (pages + 1) * 9) {
                        if ((lastVisible % 9) == 0) {
                            pages = pages + 1;
                            JSONObject object = new JSONObject();
                            String url = "";
                            if (API_urls.contains("search_group")) {
                                url = API_urls + "?page_no=" + pages;
                            } else {
                                url = API_urls + "?page_no=" + pages;
                            }
                            refreshLayout.setRefreshing(true);
                            new VollyAPICall(MyStrainActivity.this, false, url, object, user.getSession_key(), Request.Method.GET, MyStrainActivity.this, get_my_strains);
//                        new VollyAPICall(MyStrainActivity.this, false, URL.get_my_strains, object, user.getSession_key(), Request.Method.GET, MyStrainActivity.this, get_my_strains);
                        }
                    }
                }
            }
        });

    }

    ArrayList<MySavedDataModal> data_list = new ArrayList<>();
    MySavesListRecylerAdapter recyler_adapter_saved;

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("Response", response);
        if (apiActions == delete_my_save) {
            CustomeToast.ShowCustomToast(getContext(), "Deleted Successfully!", Gravity.TOP);
        } else if (apiActions == filter_my_save) {
            refreshLayout.setRefreshing(false);
            data_list.clear();
            refreshLayout.setRefreshing(false);
            try {
                JSONArray array = new JSONObject(response).getJSONArray("successData");
                for (int x = 0; x < array.length(); x++) {
                    JSONObject object = array.getJSONObject(x);
                    MySavedDataModal dataModal = new MySavedDataModal();
                    dataModal.setId(object.getInt("id"));
                    dataModal.setUser_id(object.getInt("user_id"));
                    dataModal.setModel(object.getString("model"));
                    dataModal.setDescription(object.getString("description"));
                    dataModal.setType_id(object.getInt("type_id"));
                    dataModal.setType_sub_id(object.getInt("type_sub_id"));
                    dataModal.setCreated_at(object.getString("created_at"));
                    dataModal.setUpdated_at(object.getString("updated_at"));
                    dataModal.setTitle(object.getString("title"));
                    data_list.add(dataModal);
                }
                recyler_adapter_saved = new MySavesListRecylerAdapter(this, data_list);
                MyStrain_recyler_view.setAdapter(recyler_adapter_saved);
                recyler_adapter_saved.setClickListener(this);
                if (data_list.size() > 0) {
                    not_fount.setVisibility(View.GONE);
                } else {
                    not_fount.setVisibility(View.VISIBLE);
                    not_fount.setText("No Saved Strain Found!");
                }
                recyler_adapter_saved.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            refreshLayout.setRefreshing(false);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                if (pages == 0) {
                    my_strains.clear();
                    MyStrain_recyler_view.setAdapter(recyler_adapter);
                }
                JSONArray jsonArray = jsonObject.getJSONArray("successData");
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject strain_object = jsonArray.getJSONObject(x).getJSONObject("get_strain");
                    StrainDataModel strainDataModel = new StrainDataModel();
                    strainDataModel.setMy_strain_id(jsonArray.getJSONObject(x).getInt("id"));
                    strainDataModel.setMathces(0);
                    strainDataModel.setId(strain_object.getInt("id"));
                    strainDataModel.setType_id(strain_object.getInt("type_id"));
                    strainDataModel.setTitle(strain_object.getString("title"));
                    strainDataModel.setOverview(strain_object.getString("overview"));
                    strainDataModel.setApproved(strain_object.getInt("approved"));
                    strainDataModel.setCreated_at(jsonArray.getJSONObject(x).getString("created_at"));
                    strainDataModel.setUpdated_at(jsonArray.getJSONObject(x).getString("updated_at"));
                    strainDataModel.setGet_review_count(strain_object.getInt("get_review_count"));
                    strainDataModel.setGet_likes_count(strain_object.getInt("get_likes_count"));
                    strainDataModel.setGet_dislikes_count(strain_object.getInt("get_dislikes_count"));
                    strainDataModel.setType_title(strain_object.getJSONObject("get_type").getString("title"));
                    strainDataModel.setCurrent_user_like(strain_object.getInt("get_user_like_count"));
                    strainDataModel.setCurrent_user_dis_like(strain_object.getInt("get_user_dislike_count"));
                    strainDataModel.setCurrent_user_flag(strain_object.getInt("get_user_flag_count"));
                    strainDataModel.setFavorite(strain_object.getInt("is_saved_count"));
                    JSONArray images_array = strain_object.getJSONArray("get_images");
                    ArrayList<StrainDataModel.Images> images = new ArrayList<>();
                    for (int y = 0; y < images_array.length(); y++) {
                        JSONObject image_object = images_array.getJSONObject(y);
                        StrainDataModel.Images img = new StrainDataModel.Images();
                        img.setId(image_object.getInt("id"));
                        img.setStrain_id(image_object.getInt("strain_id"));
                        if (!image_object.isNull("user_id")) {
                            img.setUser_id(image_object.getInt("user_id"));
                            img.setName(image_object.getJSONObject("get_user").getString("first_name"));
                            img.setUser_rating(image_object.getJSONObject("get_user").getInt("points"));
                        } else {
                            img.setName("Healing Budz");
                            img.setUser_rating(0);
                            img.setUser_id(-1);
                        }
                        img.setImage_path(image_object.getString("image_path"));
                        img.setIs_approved(image_object.getInt("is_approved"));
                        img.setIs_main(image_object.getInt("is_main"));
                        img.setCreated_at(image_object.getString("created_at"));
                        img.setUpdated_at(image_object.getString("updated_at"));
//                        img.setName(image_object.getJSONObject("get_user").getString("first_name"));
                        images.add(img);
                    }
                    strainDataModel.setImages(images);
                    if (strain_object.optJSONObject("rating_sum") != null) {
//                        strainDataModel.setRating_sum(Double.parseDouble(strain_object.getJSONObject("rating_sum").getDouble("total") + ""));
//                        strainDataModel.setRating(Double.parseDouble(strain_object.getJSONObject("rating_sum").getDouble("total") + ""));
                        strainDataModel.setRating_sum(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").getDouble("total"))));
                        strainDataModel.setRating(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").getDouble("total"))));

                    }
                    strainDataModel.setAlphabetic_keyword(String.valueOf(strainDataModel.getType_title().charAt(0)));
                    strainDataModel.setReviews(strain_object.getInt("get_review_count") + " Reviews");
                    my_strains.add(strainDataModel);
//                    recyler_adapter.notifyItemInserted(my_strains.size() - 1);
                }
                if (my_strains.size() > 0) {
                    not_fount.setVisibility(View.GONE);
                } else {
                    not_fount.setVisibility(View.VISIBLE);
                    not_fount.setText("No User Strain Found!");
                }

                recyler_adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("Response", response);
        refreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Refersh) {
////            pages = 0;
//            Refersh = false;
//            refreshLayout.setRefreshing(true);
//            JSONObject jsonObject = new JSONObject();
//            new VollyAPICall(MyStrainActivity.this, false, API_urls + "?page_no=" + pages, jsonObject, user.getSession_key(), Request.Method.GET, MyStrainActivity.this, get_my_strains);
////            new VollyAPICall(MyStrainActivity.this, false, URL.get_my_strains, jsonObject, user.getSession_key(), Request.Method.GET, MyStrainActivity.this, get_my_strains);
            saved_indicator.setVisibility(View.VISIBLE);
            user_indicator.setVisibility(View.GONE);
            isUserSaved = true;
            refreshLayout.setRefreshing(true);
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(MyStrainActivity.this, false, URL.filter_my_save + "?filter=7", jsonObject, user.getSession_key(), Request.Method.GET, MyStrainActivity.this, filter_my_save);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Refersh = true;
        if (isUserSaved) {
            Intent strain_intetn = new Intent(this, StrainDetailsActivity.class);
            strain_intetn.putExtra("strain_id", data_list.get(position).getType_sub_id());
            startActivity(strain_intetn);
        } else {
            StrainDetailsActivity.strainDataModel = my_strains.get(position);
            GoTo(MyStrainActivity.this, StrainDetailsActivity.class);
        }

    }

    @Override
    public void onEditClick(View view, int position) {
        Refersh = true;
        StrainDetailsActivity.strainDataModel = my_strains.get(position);
        GoTo(MyStrainActivity.this, EditMoreInfoAboutStrain.class);
    }

    @Override
    public void onDeleteItemCLick(View view, final int position) {
        new SweetAlertDialog(MyStrainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to delete this strain?")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("user_strain_id", my_strains.get(position).getMy_strain_id());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new VollyAPICall(MyStrainActivity.this, false, URL.delete_user_strain, jsonObject, user.getSession_key(), Request.Method.POST, MyStrainActivity.this, delete_my_save);
                        my_strains.remove(position);
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
//        try {
//            jsonObject.put("user_strain_id", my_strains.get(position).getMy_strain_id());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        new VollyAPICall(MyStrainActivity.this, false, URL.delete_user_strain, jsonObject, user.getSession_key(), Request.Method.POST, MyStrainActivity.this, delete_my_save);
//        my_strains.remove(position);
//        recyler_adapter.notifyItemRemoved(position);

    }
}
