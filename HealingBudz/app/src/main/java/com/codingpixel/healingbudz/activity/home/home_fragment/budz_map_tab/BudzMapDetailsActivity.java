package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.DataModel.BudzMapSpecialProducts;
import com.codingpixel.healingbudz.DataModel.MessagesDataModel;
import com.codingpixel.healingbudz.DataModel.ReportQuestionListDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.ReportItView.Report;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzMapStripeDialog;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.DetailsBusinessInfoCannabitesTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.DetailsBusinessInfoEventTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.DetailsBusinessInfoMedicalTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.DetailsBusinessInfoTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.DetailsBusinessInfoTabOtherFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.EventPricesANDTicketesTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.ProductServicesTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.SpecialInfoTabFragment;
import com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingBusinessChatViewActivity;
import com.codingpixel.healingbudz.activity.shoot_out.send_shoot_out_dialog.SendShootOutAlertDialog;
import com.codingpixel.healingbudz.activity.shoot_out.send_shoot_out_dialog.SendSpecialAlertDialog;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.SliderViewBudzMapDetail;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderLayout;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView;
import com.codingpixel.healingbudz.customeUI.sliderclasses.Tricks.ViewPagerEx;
import com.codingpixel.healingbudz.custome_dialog.save_discussion.SaveDiscussionAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.interfaces.TabChangeListner;
import com.codingpixel.healingbudz.interfaces.TopDataCallBack;
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
import java.util.HashMap;

import static com.codingpixel.healingbudz.Utilities.Constants.D_FORMAT_ONE;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel_abc;
import static com.codingpixel.healingbudz.activity.shoot_out.send_shoot_out_dialog.SendShootOutAlertDialog.budzMapHomeDataModels;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.chech_state;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_map;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.save_budz_call_click;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.save_budz_menu_click;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.save_favorit_bud;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.update_subscription;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part1;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part2;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part3;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.sharedBaseUrl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareHBContent;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class BudzMapDetailsActivity extends AppCompatActivity implements View.OnClickListener, TabChangeListner, SaveDiscussionAlertDialog.OnDialogFragmentClickListener, APIResponseListner, ReportSendButtonLstner, ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener, TopDataCallBack, BudzMapStripeDialog.OnDialogFragmentClickListener, SendShootOutAlertDialog.OnDialogFragmentClickListener, SendSpecialAlertDialog.OnDialogFragmentClickListener {
    public static ScrollView main_scroll_view;
    Button Tab_One, Tab_two, Tab_three;
    ImageView Back, Home;
    ImageView Slider_img;
    SwipeRefreshLayout swipe_main;
    public static boolean isBackFromCameraActivity = false;
    public ImageView Favorite_it, flag_it, Share_btn, Edit_details, Budz_detail_images;
    boolean isFavorite = false;
    ArrayList<Integer> images = new ArrayList<>();
    String BudzType = "";
    int active_count = 0;
    boolean isBackPressed = false;
    ImageView Rating_img, Rating_img_two;
    int clicked_tab_number = 0;
    ImageView Profile_img;
    TextView Review_text, Review_text_two;
    boolean isSubscribe = false;
    LinearLayout Rating_layout, delivery_layout, other_tabs, tabs;
    DetailsBusinessInfoTabFragment detailsBusinessInfoTabFragment;
    DetailsBusinessInfoTabOtherFragment detailsBusinessInfoTabOtherFragment;
    TextView Main_Heading_title, Review_Text_Complete;
    LinearLayout Organic_Layout, Delivery_layout;
    ImageView right_swipe, left_swipe;
    String budzmap_id = "";
    public static Report budz_map_report;
    boolean isShownSpecials = false;
    boolean isBudzMapDataLoadAble = false;
    RelativeLayout Main_layout;
    boolean isPause = false;
    String notFeatured = "Not Featured!";
    private LinearLayout Comment_layout;
    private SliderLayout mDemoSlider;
    DetailsBusinessInfoMedicalTabFragment detailsBusinessInfoMedicalTabFragment;
    DetailsBusinessInfoCannabitesTabFragment detailsBusinessInfoCannabitesTabFragment;
    DetailsBusinessInfoEventTabFragment detailsBusinessInfoEventTabFragment;
    int tabNum = 1;
    public static int mainIdHere = 0;

    @Override
    protected void onPause() {
        if (mDemoSlider != null) {
            mDemoSlider.stopAutoCycle();
        }
        super.onPause();
    }

    @Override
    protected void onRestart() {
//        if (mDemoSlider != null) {
//            mDemoSlider.startAutoCycle();
//        }
        super.onRestart();

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_budz_map);
        if (budz_map_item_clickerd_dataModel != null)
            mainIdHere = budz_map_item_clickerd_dataModel.getId();
        Main_layout = findViewById(R.id.main_layout);
        swipe_main = findViewById(R.id.swipe_main);
        other_tabs = findViewById(R.id.other_tabs);
        tabs = findViewById(R.id.tabs);
        right_swipe = findViewById(R.id.imgae_right);
        left_swipe = findViewById(R.id.left_swipe);
        budz_map_report = new Report(this, this, "#932a88", "budz");
        Main_layout.addView(budz_map_report.getView());
        budz_map_report.InitSlide();

        swipe_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO
                if (tabNum == 1) {
                    Tab_One.performClick();
                } else if (tabNum == 2) {
                    Tab_two.performClick();
                } else if (tabNum == 3) {
                    Tab_three.performClick();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe_main.setRefreshing(false);
                    }
                }, 200);
            }
        });
        isBackPressed = false;
        detailsBusinessInfoTabFragment = new DetailsBusinessInfoTabFragment(this);
        detailsBusinessInfoTabOtherFragment = new DetailsBusinessInfoTabOtherFragment(this);
        detailsBusinessInfoMedicalTabFragment = new DetailsBusinessInfoMedicalTabFragment(BudzMapDetailsActivity.this);

        detailsBusinessInfoCannabitesTabFragment = new DetailsBusinessInfoCannabitesTabFragment(BudzMapDetailsActivity.this);

        detailsBusinessInfoEventTabFragment = new DetailsBusinessInfoEventTabFragment(BudzMapDetailsActivity.this);
        HideKeyboard(BudzMapDetailsActivity.this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (getIntent().hasExtra("budz_type")) {
                BudzType = getIntent().getExtras().getString("budz_type");
                isBudzMapDataLoadAble = false;
                budzmap_id = "";
            } else {
                budzmap_id = extras.getInt("budzmap_id") + "";
                if (budzmap_id.equalsIgnoreCase("0")) {
                    isBudzMapDataLoadAble = false;
                    budzmap_id = "";
                } else {
                    isBudzMapDataLoadAble = true;
                    if (getIntent().hasExtra("view_specials")) {
                        isShownSpecials = extras.getBoolean("view_specials");
                    }
                }
            }
        } else {
            isBudzMapDataLoadAble = false;
            budzmap_id = "";
        }
        Comment_layout = findViewById(R.id.comment_layout);
        mDemoSlider = findViewById(R.id.slider);
//        mDemoSlider.onInterceptTouchEvent(null);
        main_scroll_view = findViewById(R.id.main_scroll);
        main_scroll_view.setEnabled(false);
        main_scroll_view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        main_scroll_view.setFocusable(true);
        main_scroll_view.setFocusableInTouchMode(true);
        main_scroll_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//        UIModification.makeTransParnet(this);
        Slider_img = findViewById(R.id.slider_img);
        Profile_img = findViewById(R.id.budz_map_profile_img);
        Tab_One = findViewById(R.id.tab_one);
        Tab_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabNum = 1;
                Tab_One.setBackgroundColor(Color.parseColor("#932a88"));
                isSpecialTap = false;
                Tab_two.setBackgroundColor(Color.parseColor("#5c5c5c"));
                Tab_three.setBackgroundColor(Color.parseColor("#5c5c5c"));
                Comment_layout.setClickable(true);
                Comment_layout.setEnabled(true);
                Tab_One.setTextColor(Color.parseColor("#e9d4e7"));
                Tab_three.setTextColor(Color.parseColor("#b0b0b0"));
                Tab_two.setTextColor(Color.parseColor("#b0b0b0"));


                if (!detailsBusinessInfoTabFragment.isVisible()) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

//                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                    transaction.replace(R.id.fragment_layout, getTabOneFragment());
                    transaction.commitAllowingStateLoss();
                } else {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.remove(getTabOneFragment());
//                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                    transaction.replace(R.id.fragment_layout, getTabOneFragment());
                    transaction.commitAllowingStateLoss();
                }
            }
        });

        Tab_two = findViewById(R.id.tab_two);
        Tab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (budz_map_item_clickerd_dataModel.getIs_featured() == 1) {
                    tabNum = 2;
                    isSpecialTap = false;
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                    transaction.replace(R.id.fragment_layout, getTabTwoFragment());
                    transaction.commitAllowingStateLoss();
                    Comment_layout.setClickable(false);
                    Comment_layout.setEnabled(false);
                    Tab_One.setBackgroundColor(Color.parseColor("#5c5c5c"));
                    Tab_two.setBackgroundColor(Color.parseColor("#932a88"));
                    Tab_three.setBackgroundColor(Color.parseColor("#5c5c5c"));

                    Tab_two.setTextColor(Color.parseColor("#e9d4e7"));
                    Tab_three.setTextColor(Color.parseColor("#b0b0b0"));
                    Tab_One.setTextColor(Color.parseColor("#b0b0b0"));
                    if (Tab_two.getText().toString().equalsIgnoreCase("Menu")) {
//                        save_budz_menu_click
                        JSONObject object = new JSONObject();
                        try {
                            object.put("sub_user_id", budz_map_item_clickerd_dataModel.getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new VollyAPICall(BudzMapDetailsActivity.this, false, URL.save_budz_menu_click, object, user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                            @Override
                            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                                if (apiActions == save_budz_menu_click) {
                                    Log.d("onRequestSuccess: ", response);
                                }
                            }

                            @Override
                            public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                if (apiActions == save_budz_menu_click) {
                                    Log.d("onRequestSuccess: ", response);
                                }
                            }
                        }, save_budz_menu_click);

                    }
                } else {
                    makeUpgradtion();
//                    CustomeToast.ShowCustomToast(view.getContext(), notFeatured, Gravity.BOTTOM);
                }
            }
        });
        Tab_three = findViewById(R.id.tab_three);
        Tab_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (budz_map_item_clickerd_dataModel.getIs_featured() == 1) {
                    tabNum = 3;
                    isSpecialTap = true;
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                    transaction.replace(R.id.fragment_layout, getTabThreeFragment());
                    transaction.commitAllowingStateLoss();
                    Comment_layout.setClickable(false);
                    Comment_layout.setEnabled(false);
                    Tab_One.setBackgroundColor(Color.parseColor("#5c5c5c"));
                    Tab_two.setBackgroundColor(Color.parseColor("#5c5c5c"));
                    Tab_three.setBackgroundColor(Color.parseColor("#932a88"));

                    Tab_three.setTextColor(Color.parseColor("#e9d4e7"));
                    Tab_two.setTextColor(Color.parseColor("#b0b0b0"));
                    Tab_One.setTextColor(Color.parseColor("#b0b0b0"));
                } else {
                    makeUpgradtion();
//                    CustomeToast.ShowCustomToast(view.getContext(), notFeatured, Gravity.BOTTOM);
                }
            }
        });


        Comment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                main_scroll_view.smoothScrollTo(0, 4100);
            }
        });
        Back = findViewById(R.id.back_btn);
        Home = findViewById(R.id.home_btn);


        Rating_img = findViewById(R.id.rating_img);
//        Rating_img_two = (ImageView) findViewById(R.id.rating_img_two);
        Review_text = findViewById(R.id.review_text);
//        Review_text_two = (TextView) findViewById(R.id.review_count_two);
        Review_Text_Complete = findViewById(R.id.review_text_complete);

        Rating_layout = findViewById(R.id.rating_layout);
        Delivery_layout = findViewById(R.id.deliver_layout);

        Main_Heading_title = findViewById(R.id.main_heading_title);
        Main_Heading_title.setAutoLinkMask(0);
        Back.setOnClickListener(this);
        Home.setOnClickListener(this);

        Favorite_it = findViewById(R.id.favorite_it);
        Favorite_it.setOnClickListener(this);
        flag_it = findViewById(R.id.flag_it);
        flag_it.setOnClickListener(this);

        Share_btn = findViewById(R.id.share_btn);
        Share_btn.setOnClickListener(this);

        Edit_details = findViewById(R.id.edit_details);

        if (budz_map_item_clickerd_dataModel != null) {
            if (budz_map_item_clickerd_dataModel.getUser_id() == user.getUser_id()) {
                Edit_details.setVisibility(View.VISIBLE);
                Edit_details.setOnClickListener(this);
                Rating_layout.setVisibility(View.GONE);
            } else {
                Rating_layout.setVisibility(View.VISIBLE);
                Edit_details.setVisibility(View.INVISIBLE);
                Edit_details.setClickable(false);
            }
        } else {
            Rating_layout.setVisibility(View.VISIBLE);
            Edit_details.setVisibility(View.INVISIBLE);
            Edit_details.setOnClickListener(this);
        }
        Rating_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessagesDataModel messagesDataModelBusiness = new MessagesDataModel();
                messagesDataModelBusiness.setId(budz_map_item_clickerd_dataModel.getId());
                messagesDataModelBusiness.setSender_id(user.getUser_id());
                messagesDataModelBusiness.setReceiver_id(budz_map_item_clickerd_dataModel.getUser_id());
                messagesDataModelBusiness.setLast_message_id(0);
                messagesDataModelBusiness.setSender_deleted(0);
                messagesDataModelBusiness.setReceiver_deleted(0);
                messagesDataModelBusiness.setCreated_at(budz_map_item_clickerd_dataModel.getCreated_at());
                messagesDataModelBusiness.setUpdated_at(budz_map_item_clickerd_dataModel.getUpdated_at());
                messagesDataModelBusiness.setMessages_count(0);
                messagesDataModelBusiness.setSender_first_name(user.getFirst_name());
                messagesDataModelBusiness.setSender_image_path(null);
                messagesDataModelBusiness.setSender_avatar(user.getAvatar());
                messagesDataModelBusiness.setReceiver_first_name(budz_map_item_clickerd_dataModel.getTitle());
                messagesDataModelBusiness.setReceiver_image_path(null);
                messagesDataModelBusiness.setReceiver_avatar(budz_map_item_clickerd_dataModel.getLogo());
                MessagingBusinessChatViewActivity.chat_message_data_modal = messagesDataModelBusiness;
                Splash.image_path = URL.images_baseurl + budz_map_item_clickerd_dataModel.getLogo();
                Splash.NameBusiness = budz_map_item_clickerd_dataModel.getTitle();
                Splash.otherName = budz_map_item_clickerd_dataModel.getTitle();
                Intent i = new Intent(BudzMapDetailsActivity.this, MessagingBusinessChatViewActivity.class);
                i.putExtra("budz_id", budz_map_item_clickerd_dataModel.getId());
                i.putExtra("chat_id", -1);
                i.putExtra("other_id", budz_map_item_clickerd_dataModel.getUser_id());
                startActivity(i);
            }
        });
        Budz_detail_images = findViewById(R.id.images_budz_details);
        Budz_detail_images.setOnClickListener(this);


        right_swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getImages() != null && budz_map_item_clickerd_dataModel.getImages().size() > 1) {
                    mDemoSlider.startAutoCycle(10000, 10000, true);
                    mDemoSlider.moveNextPosition(true);
                } else {
                    if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getImages() != null && budz_map_item_clickerd_dataModel.getImages().size() > 1 && budz_map_item_clickerd_dataModel.getBanner().length() > 4) {
                        mDemoSlider.startAutoCycle(10000, 10000, true);
                        mDemoSlider.moveNextPosition(true);
                    }
                }

            }
        });
        left_swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getImages() != null && budz_map_item_clickerd_dataModel.getImages().size() > 1) {
                    mDemoSlider.startAutoCycle(10000, 10000, true);
                    mDemoSlider.movePrevPosition(true);
                } else {
                    if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getImages() != null && budz_map_item_clickerd_dataModel.getImages().size() > 1 && budz_map_item_clickerd_dataModel.getBanner().length() > 4) {
                        mDemoSlider.startAutoCycle(10000, 10000, true);
                        mDemoSlider.movePrevPosition(true);
                    }
                }
            }
        });
        if (BudzType.equalsIgnoreCase("3")) {
            Tab_two.setText("Menu");
        } else if (BudzType.equalsIgnoreCase("2") || BudzType.equalsIgnoreCase("6") || BudzType.equalsIgnoreCase("7")) {
            Tab_two.setText("Products/Services");
        } else if (BudzType.equalsIgnoreCase("5")) {
            Tab_two.setText("Price/Tickets");
            Tab_One.setText("Event Details");
        } else {
            Tab_two.setText("Products");
        }
        if (isBudzMapDataLoadAble) {
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(BudzMapDetailsActivity.this, true, URL.get_budz + "/" + budzmap_id + "?lat=0.00&lng=0.00 ", jsonObject, user.getSession_key(), Request.Method.GET, BudzMapDetailsActivity.this, get_budz_map);
        } else {
            if (budz_map_item_clickerd_dataModel.getBusiness_type_id() == 2 || budz_map_item_clickerd_dataModel.getBusiness_type_id() == 7 || budz_map_item_clickerd_dataModel.getBusiness_type_id() == 6) {
                new VollyAPICall(BudzMapDetailsActivity.this, true, getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + user.getZip_code() + getLocatioZipcode_part3, new JSONObject(), null, Request.Method.POST, BudzMapDetailsActivity.this, APIActions.ApiActions.chech_state);
            }
            BudzType = budz_map_item_clickerd_dataModel.getBusiness_type_id() + "";
            //budz_map_item_clickerd_dataModel.getUser_id() == Splash.user.getUser_id() &&
            if (budz_map_item_clickerd_dataModel.getIs_featured() == 1) {
                other_tabs.setVisibility(View.VISIBLE);
                tabs.setWeightSum(12.0F);
            } else if (budz_map_item_clickerd_dataModel.getUser_id() == Splash.user.getUser_id()) {
                other_tabs.setVisibility(View.VISIBLE);
                tabs.setWeightSum(12.0F);
            } else {
                other_tabs.setVisibility(View.GONE);
                tabs.setWeightSum(12.0F);

            }
            SetTopData();

        }
    }

    void makeUpgradtion() {
        if (budz_map_item_clickerd_dataModel.getUser_id() == user.getUser_id()) {
            Intent intent = new Intent(this, BudzMapPaidViewActivity.class);
            intent.putExtra("isUpdate", true);
            intent.putExtra("budz_id", budz_map_item_clickerd_dataModel.getId());
            startActivityForResult(intent, Constants.UPGRADED);
        }
    }

    boolean isFromTop = false;

    void setPager() {
        HashMap<String, BudzMapHomeDataModel.Images> url_maps = new HashMap<String, BudzMapHomeDataModel.Images>();
        for (int i = 0; i < budz_map_item_clickerd_dataModel.getImages().size(); i++) {
            url_maps.put(budz_map_item_clickerd_dataModel.getImages().get(i).getImage_path(), budz_map_item_clickerd_dataModel.getImages().get(i));
        }

        if (budz_map_item_clickerd_dataModel.getBanner() != null && budz_map_item_clickerd_dataModel.getBanner().length() > 8) {
            SliderViewBudzMapDetail textSliderViewBanner = new SliderViewBudzMapDetail(this);
            // initialize a SliderLayout
            textSliderViewBanner
                    .description(budz_map_item_clickerd_dataModel.getBanner())
                    .image(images_baseurl + budz_map_item_clickerd_dataModel.getBanner())
//                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderViewBanner.bundle(new Bundle());
            textSliderViewBanner.getBundle()
                    .putString("extra", budz_map_item_clickerd_dataModel.getBanner());
            mDemoSlider.addSlider(textSliderViewBanner);
        }
        for (String name : url_maps.keySet()) {
            SliderViewBudzMapDetail textSliderView = new SliderViewBudzMapDetail(this);
            // initialize a SliderLayout
            textSliderView
                    .description(url_maps.get(name).getName())
                    .image(images_baseurl + url_maps.get(name).getImage_path())
//                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", url_maps.get(name).getName());
            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);

        mDemoSlider.setDuration(10000);
        mDemoSlider.addOnPageChangeListener(this);

        if (budz_map_item_clickerd_dataModel.getImages().size() > 0 && (budz_map_item_clickerd_dataModel.getBanner() != null && budz_map_item_clickerd_dataModel.getBanner().length() > 5)) {
//            if()
            mDemoSlider.startAutoCycle();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDemoSlider.setVisibility(View.VISIBLE);
                }
            }, 1000);

            Slider_img.setVisibility(View.GONE);
        } else if (budz_map_item_clickerd_dataModel.getImages().size() > 1) {
            mDemoSlider.startAutoCycle();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDemoSlider.setVisibility(View.VISIBLE);
                }
            }, 1000);
            Slider_img.setVisibility(View.GONE);
        } else {
            mDemoSlider.stopAutoCycle();
            mDemoSlider.setVisibility(View.GONE);
            Slider_img.setVisibility(View.VISIBLE);
            if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getBanner() != null && budz_map_item_clickerd_dataModel.getBanner().length() > 6) {
                Glide.with(this).load(URL.images_baseurl + budz_map_item_clickerd_dataModel.getBanner())
                        .placeholder(R.drawable.budz_bg_df)
                        .error(R.drawable.budz_bg_df)
                        .into(Slider_img);
            } else {
                Slider_img.setVisibility(View.VISIBLE);
                Glide.with(this).load(R.drawable.budz_bg_df)
                        .placeholder(R.drawable.budz_bg_df)
                        .error(R.drawable.budz_bg_df)
                        .into(Slider_img);
            }
        }

//        if()
//        mDemoSlider

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (isBackFromCameraActivity) {
//            main_scroll_view.fullScroll(View.FOCUS_DOWN);
//            isBackFromCameraActivity = false;
//        } else {
//            main_scroll_view.fullScroll(View.FOCUS_UP);
//        }
        if (isPause) {
            isPause = false;
            JSONObject jsonObject = new JSONObject();
            if (budz_map_item_clickerd_dataModel != null)
                new VollyAPICall(BudzMapDetailsActivity.this, true, URL.get_budz + "/" + budz_map_item_clickerd_dataModel.getId() + "?lat=0.00&lng=0.00 ", jsonObject, user.getSession_key(), Request.Method.GET, BudzMapDetailsActivity.this, get_budz_map);
            else {
                new VollyAPICall(BudzMapDetailsActivity.this, true, URL.get_budz + "/" + mainIdHere + "?lat=0.00&lng=0.00 ", jsonObject, user.getSession_key(), Request.Method.GET, BudzMapDetailsActivity.this, get_budz_map);
            }
        }

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
    public void onBackPressed() {
        isBackPressed = true;
        budz_map_item_clickerd_dataModel = null;
        super.onBackPressed();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.flag_it:
                if (!isFlagged) {
                    if (budz_map_item_clickerd_dataModel.getUser_id() != user.getUser_id()) {
                        if (!isFlagged) {
                            if (budz_map_report.isSlide()) {
                                budz_map_report.SlideUp();
                            } else {
                                ArrayList<ReportQuestionListDataModel> dataModels = new ArrayList<>();
                                dataModels.add(new ReportQuestionListDataModel("Nudity or sexual content", false));
                                dataModels.add(new ReportQuestionListDataModel("Harassment or hate speech", false));
                                dataModels.add(new ReportQuestionListDataModel("Threatening, violent, or concerning", false));
                                dataModels.add(new ReportQuestionListDataModel("Spam", false));
                                dataModels.add(new ReportQuestionListDataModel("Offensive", false));
                                dataModels.add(new ReportQuestionListDataModel("Unrelated", false));

                                budz_map_report.SlideDown(0, dataModels, BudzMapDetailsActivity.this, "budz");
                            }

                        }
                    } else {
                        CustomeToast.ShowCustomToast(this, "You can't report on your own budz ad!", Gravity.TOP);
                    }
                } else {
                    CustomeToast.ShowCustomToast(this, "Budz ad already reported!", Gravity.TOP);
                }
                break;
            case R.id.back_btn:
                isBackPressed = true;
                budz_map_item_clickerd_dataModel = null;
                finish();
                break;
            case R.id.home_btn:
                budz_map_item_clickerd_dataModel = null;
                GoToHome(BudzMapDetailsActivity.this, true);
                finish();
                break;

            case R.id.favorite_it:

                JSONObject object = new JSONObject();
                try {
                    if (isFavorite) {
                        isFavorite = false;
                        Favorite_it.setImageResource(R.drawable.ic_favorite_border_white);
                        object.put("sub_user_id", budz_map_item_clickerd_dataModel.getId());
                        object.put("is_like", 0);
                        budz_map_item_clickerd_dataModel.setGet_user_save_count(0);
                    } else {
                        if (!SharedPrefrences.getBool("IS_BudzMap_My_SAVE_Dialog_Shown", BudzMapDetailsActivity.this)) {
                            SaveDiscussionAlertDialog saveDiscussionAlertDialog = SaveDiscussionAlertDialog.newInstance("BUDZ ADZ LISTING SAVED", "Business listing are saved in the menu under My Saves.", "Got it! Don't show again for Businesses I Like.", BudzMapDetailsActivity.this);
                            saveDiscussionAlertDialog.show(getSupportFragmentManager(), "dialog");
                        }
                        isFavorite = true;
                        Favorite_it.setImageResource(R.drawable.ic_favorite_white);
                        object.put("sub_user_id", budz_map_item_clickerd_dataModel.getId());
                        object.put("is_like", 1);
                        budz_map_item_clickerd_dataModel.setGet_user_save_count(1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new VollyAPICall(BudzMapDetailsActivity.this, false, URL.save_favorit_bud, object, user.getSession_key(), Request.Method.POST, BudzMapDetailsActivity.this, save_favorit_bud);
                break;
            case R.id.share_btn:
                JSONObject object1 = new JSONObject();
                try {
//                    object1.put("msg", "http://139.162.37.73/healingbudz/budz-map");
                    object1.put("id", budz_map_item_clickerd_dataModel.getId());
                    object1.put("type", "Budz");
                    object1.put("content", budz_map_item_clickerd_dataModel.getTitle());
                    object1.put("url", sharedBaseUrl + "/get-budz?business_id=" + budz_map_item_clickerd_dataModel.getId() + "&business_type_id=" + budz_map_item_clickerd_dataModel.getBusiness_type_id());
                    object1.put("BudzCome", "");

                    object1.put("msg", "Check out Healing Budz for your smartphone: " + sharedBaseUrl + "/get-budz?business_id=" + budz_map_item_clickerd_dataModel.getId() + "&business_type_id=" + budz_map_item_clickerd_dataModel.getBusiness_type_id());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject objectBudz = new JSONObject();
                try {
                    objectBudz.put("sub_user_id", budz_map_item_clickerd_dataModel.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new VollyAPICall(view.getContext(), false, URL.save_budzmap_share, objectBudz, user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                    @Override
                    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                        if (apiActions == save_budz_call_click) {
                            Log.d("onRequestSuccess: ", response);
                        }
                    }

                    @Override
                    public void onRequestError(String response, APIActions.ApiActions apiActions) {
                        if (apiActions == save_budz_call_click) {
                            Log.d("onRequestSuccess: ", response);
                        }
                    }
                }, save_budz_call_click);
                ShareHBContent(BudzMapDetailsActivity.this, object1);
                break;

            case R.id.edit_details:
                if (isSpecialTap) {
                    budzMapHomeDataModels = new ArrayList<>();
                    budzMapHomeDataModels.add(budz_map_item_clickerd_dataModel);
                    if (budzMapHomeDataModels.size() > 0) {

                        SendShootOutAlertDialog sendShootOutAlertDialog = SendShootOutAlertDialog.newInstance(BudzMapDetailsActivity.this, budzMapHomeDataModels);
                        sendShootOutAlertDialog.show(this.getSupportFragmentManager(), "dialog");
                    } else {
                        CustomeToast.ShowCustomToast(this, "You can't create a shout out with pending paid business!", Gravity.TOP);
                    }
                } else {
                    isPause = true;
                    DetailsBusinessInfoCannabitesTabFragment.isFresh = true;
                    DetailsBusinessInfoEventTabFragment.isFresh = true;
                    DetailsBusinessInfoMedicalTabFragment.isFresh = true;
                    DetailsBusinessInfoTabFragment.isFresh = true;
                    budz_map_item_clickerd_dataModel_abc = budz_map_item_clickerd_dataModel;
                    GoTo(BudzMapDetailsActivity.this, AddNewBudzMapActivity.class);
                }
                break;

            case R.id.images_budz_details:
//                if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getImages().size() > 0) {
                GoTo(BudzMapDetailsActivity.this, BudzMapDetailsImagesActivity.class);
//                }
                break;
        }
    }

    boolean isSpecialTap = false;


    public void MakeSlider() {
        if (!this.isBackPressed) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (budz_map_item_clickerd_dataModel != null) {
                        if (budz_map_item_clickerd_dataModel.getImages().size() > 0) {
                            try {
                                if (active_count == budz_map_item_clickerd_dataModel.getImages().size() - 1) {
                                    active_count = 0;
                                } else {
                                    active_count++;
                                }
                                if (!isBackPressed) {
//                                    MakeSlider();
//                                    LoadImage(budz_map_item_clickerd_dataModel.getImages().get(active_count).getImage_path());
                                }
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }, 5000);
        }
    }

    public void LoadImage(String Path) {
        Context context = BudzMapDetailsActivity.this;
        if (context != null) {
            if (!BudzMapDetailsActivity.this.isFinishing()) {
                Glide.with(context)
                        .load(images_baseurl + Path)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.image_plaecholder_bg)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Log.d("ready", model);
                                Slider_img.setImageDrawable(resource);
                                return false;
                            }
                        }).into(1080, 1080);
            }
        }
    }

    public void SetTopData() {
        if (!isFromTop) {
            if (isShownSpecials) {
                isSpecialTap = true;
                if (!getTabThreeFragment().isVisible()) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                    transaction.replace(R.id.fragment_layout, getTabThreeFragment());
                    transaction.commitAllowingStateLoss();

                    Tab_One.setBackgroundColor(Color.parseColor("#5c5c5c"));
                    Tab_two.setBackgroundColor(Color.parseColor("#5c5c5c"));
                    Tab_three.setBackgroundColor(Color.parseColor("#932a88"));

                    Tab_three.setTextColor(Color.parseColor("#e9d4e7"));
                    Tab_two.setTextColor(Color.parseColor("#b0b0b0"));
                    Tab_One.setTextColor(Color.parseColor("#b0b0b0"));
                }
            } else {
                isSpecialTap = false;

                if (!getTabOneFragment().isVisible()) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                    transaction.replace(R.id.fragment_layout, getTabOneFragment());
                    transaction.commitAllowingStateLoss();
                    tabNum = 1;
                    Tab_One.setBackgroundColor(Color.parseColor("#932a88"));
                    isSpecialTap = false;
                    Tab_two.setBackgroundColor(Color.parseColor("#5c5c5c"));
                    Tab_three.setBackgroundColor(Color.parseColor("#5c5c5c"));
                    Comment_layout.setClickable(true);
                    Comment_layout.setEnabled(true);
                    Tab_One.setTextColor(Color.parseColor("#e9d4e7"));
                    Tab_three.setTextColor(Color.parseColor("#b0b0b0"));
                    Tab_two.setTextColor(Color.parseColor("#b0b0b0"));

                }
            }
        }
        isFromTop = false;
        if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getImages() != null) {
            if (budz_map_item_clickerd_dataModel.getImages().size() > 0) {
                Glide.with(BudzMapDetailsActivity.this)
                        .load(images_baseurl + budz_map_item_clickerd_dataModel.getImages().get(0).getImage_path())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.image_plaecholder_bg)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                Slider_img.setBackground(resource);
//                                MakeSlider();
                                return false;
                            }
                        }).into(Slider_img);
                setPager();
            } else if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getBanner() != null && budz_map_item_clickerd_dataModel.getBanner().length() > 6) {
                Glide.with(BudzMapDetailsActivity.this)
                        .load(images_baseurl + budz_map_item_clickerd_dataModel.getBanner())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.image_plaecholder_bg)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                Slider_img.setBackground(resource);
                                return false;
                            }
                        }).into(Slider_img);
                Slider_img.setVisibility(View.VISIBLE);
                mDemoSlider.setVisibility(View.GONE);
                right_swipe.setVisibility(View.INVISIBLE);
                left_swipe.setVisibility(View.INVISIBLE);
                setPager();
            } else {
                if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getBanner() != null && budz_map_item_clickerd_dataModel.getBanner().length() > 6) {
                    Glide.with(this).load(URL.images_baseurl + budz_map_item_clickerd_dataModel.getBanner())
                            .placeholder(R.drawable.budz_bg_df)
                            .error(R.drawable.budz_bg_df)
                            .into(Slider_img);
                } else {
                    Slider_img.setVisibility(View.VISIBLE);
                    Glide.with(this).load(R.drawable.budz_bg_df)
                            .placeholder(R.drawable.budz_bg_df)
                            .error(R.drawable.budz_bg_df)
                            .into(Slider_img);
                }
                right_swipe.setVisibility(View.INVISIBLE);
                left_swipe.setVisibility(View.INVISIBLE);
            }
        } else if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getBanner() != null && budz_map_item_clickerd_dataModel.getBanner().length() > 6) {
            Glide.with(BudzMapDetailsActivity.this)
                    .load(images_baseurl + budz_map_item_clickerd_dataModel.getBanner())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.image_plaecholder_bg)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            Slider_img.setBackground(resource);
                            return false;
                        }
                    }).into(Slider_img);
            Slider_img.setVisibility(View.VISIBLE);
            mDemoSlider.setVisibility(View.GONE);
            setPager();
        } else {
            if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getBanner() != null && budz_map_item_clickerd_dataModel.getBanner().length() > 6) {
                Glide.with(this).load(URL.images_baseurl + budz_map_item_clickerd_dataModel.getBanner())
                        .placeholder(R.drawable.budz_bg_df)
                        .error(R.drawable.budz_bg_df)
                        .into(Slider_img);
            } else {
                Slider_img.setVisibility(View.VISIBLE);
                Glide.with(this).load(R.drawable.budz_bg_df)
                        .placeholder(R.drawable.budz_bg_df)
                        .error(R.drawable.budz_bg_df)
                        .into(Slider_img);
            }
            right_swipe.setVisibility(View.INVISIBLE);
            left_swipe.setVisibility(View.INVISIBLE);
        }
        if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getLogo() != null && budz_map_item_clickerd_dataModel.getLogo().length() > 2) {
            Glide.with(BudzMapDetailsActivity.this)
                    .load(images_baseurl + budz_map_item_clickerd_dataModel.getLogo())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_budz_adn)
                    .error(R.drawable.ic_budz_adn)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("ready", model);
                            Profile_img.setImageDrawable(resource);
                            return false;
                        }
                    }).into(Profile_img);
        } else {
            switch (budz_map_item_clickerd_dataModel.getBusiness_type_id()) {
                case 1:
//                holder.type.setText("Dispensary");
                    Profile_img.setImageResource(R.drawable.ic_dispancry_icon);
                    break;
                case 2:
                case 6:
                case 7:
//                holder.type.setText("Medical");
                    Profile_img.setImageResource(R.drawable.ic_medical_icon);
                    break;
                case 3:
//                holder.type.setText("Cannabites");
                    Profile_img.setImageResource(R.drawable.ic_cannabites_icon);
                    break;
                case 4:
                case 8:
//                holder.type.setText("Entertainment");
                    Profile_img.setImageResource(R.drawable.ic_entertainment_icon);
                    break;
                case 5:
//                holder.type.setText("Events");
                    Profile_img.setImageResource(R.drawable.ic_events_icon);
                    break;
                case 9:
//                holder.type.setText("Other");
                    Profile_img.setImageResource(R.drawable.other_item);
                    break;
            }

        }

        Main_Heading_title.setText(budz_map_item_clickerd_dataModel.getTitle());
//        MakeKeywordClickableText(Main_Heading_title.getContext(), budz_map_item_clickerd_dataModel.getTitle(), Main_Heading_title);
        Rating_img.setImageResource(GetRAtingImg(budz_map_item_clickerd_dataModel.getRating_sum()));
//        Rating_img_two.setImageResource(GetRAtingImg(budz_map_item_clickerd_dataModel.getRating_sum()));

        Review_text.setText(budz_map_item_clickerd_dataModel.getReviews().size() + " Reviews");
//        Review_text_two.setText(budz_map_item_clickerd_dataModel.getReviews().size() + " Reviews");


        if (budz_map_item_clickerd_dataModel.getIs_organic() == 0 && budz_map_item_clickerd_dataModel.getIs_delivery() == 0) {
            Delivery_layout.setVisibility(View.INVISIBLE);
        }

        delivery_layout = findViewById(R.id.delivery_layout);
        Organic_Layout = findViewById(R.id.organic_layout);
        if (budz_map_item_clickerd_dataModel.getIs_organic() == 1) {
            Organic_Layout.setVisibility(View.VISIBLE);
        } else {
            Organic_Layout.setVisibility(View.GONE);
        }

        if (budz_map_item_clickerd_dataModel.getIs_delivery() == 1) {
            delivery_layout.setVisibility(View.VISIBLE);
        } else {
            delivery_layout.setVisibility(View.GONE);
        }

        if (budz_map_item_clickerd_dataModel.getGet_user_save_count() == 1) {
            isFavorite = true;
            Favorite_it.setImageResource(R.drawable.ic_favorite_white);
        } else {
            isFavorite = false;
            Favorite_it.setImageResource(R.drawable.ic_favorite_border_white);

        }
        if (BudzType.equalsIgnoreCase("3")) {
            Tab_two.setText("Menu");
        } else if (BudzType.equalsIgnoreCase("2") || BudzType.equalsIgnoreCase("6") || BudzType.equalsIgnoreCase("7")) {
            Tab_two.setText("Products/Services");
        } else if (BudzType.equalsIgnoreCase("5")) {
            Tab_two.setText("Price/Tickets");
            Tab_One.setText("Event Details");
        } else if (BudzType.equalsIgnoreCase("9")) {
            Tab_two.setText("");
        } else {
            Tab_two.setText("Products/Services");
        }
        if (budz_map_item_clickerd_dataModel.getIs_featured() == 1) {
            other_tabs.setVisibility(View.VISIBLE);
            tabs.setWeightSum(12.0F);
        } else if (budz_map_item_clickerd_dataModel.getUser_id() == Splash.user.getUser_id() && !BudzType.equalsIgnoreCase("9")) {
            other_tabs.setVisibility(View.VISIBLE);
            tabs.setWeightSum(12.0F);
        } else {
            other_tabs.setVisibility(View.GONE);
            tabs.setWeightSum(12.0F);
        }
        if (budz_map_item_clickerd_dataModel != null) {
            if (budz_map_item_clickerd_dataModel.getUser_id() == user.getUser_id()) {
                Edit_details.setVisibility(View.VISIBLE);
                Edit_details.setOnClickListener(this);
                Rating_layout.setVisibility(View.GONE);
            } else {
                Rating_layout.setVisibility(View.VISIBLE);
                Edit_details.setVisibility(View.INVISIBLE);
                Edit_details.setClickable(false);
            }
        } else {
            Rating_layout.setVisibility(View.VISIBLE);
            Edit_details.setVisibility(View.INVISIBLE);
            Edit_details.setOnClickListener(this);
        }
        if (BudzType.equalsIgnoreCase("9")) {
            Budz_detail_images.setVisibility(View.INVISIBLE);
        } else {
            Budz_detail_images.setVisibility(View.VISIBLE);
        }
    }

    public static int GetRAtingImg(double rating) {
        if (rating > 0) {
            if (rating >= 1 && rating < 2) {
                if (rating >= 1.5) {
                    return R.drawable.ic_half_1;
                } else {
                    return R.drawable.ic_1_star;
                }

            } else if (rating >= 2 && rating < 3) {
                if (rating >= 2.5) {
                    return R.drawable.ic_half_2;
                } else {
                    return R.drawable.ic_2_star;
                }
//                return R.drawable.rating_two;
            } else if (rating >= 3 && rating < 4) {
                if (rating >= 3.5) {
                    return R.drawable.ic_half_3;
                } else {
                    return R.drawable.ic_3_star;
                }
//                return R.drawable.rating_three;
            } else if (rating >= 4 && rating < 5) {
                if (rating >= 4.5) {
                    return R.drawable.ic_half_4;
                } else {
                    return R.drawable.ic_4_star;
                }
            } else if (rating == 5) {
                return R.drawable.ic_5_star;
            } else {
                return R.drawable.ic_half_star;
            }
        } else {
            return R.drawable.ic_empty_all;
        }
    }

    @Override
    public void ShowProductTab() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
        transaction.replace(R.id.fragment_layout, getTabTwoFragment());
        transaction.commitAllowingStateLoss();

        Tab_One.setBackgroundColor(Color.parseColor("#5c5c5c"));
        Tab_two.setBackgroundColor(Color.parseColor("#932a88"));
        Tab_three.setBackgroundColor(Color.parseColor("#5c5c5c"));

        Tab_two.setTextColor(Color.parseColor("#e9d4e7"));
        Tab_three.setTextColor(Color.parseColor("#b0b0b0"));
        Tab_One.setTextColor(Color.parseColor("#b0b0b0"));
    }

    @Override
    public void onOkClicked(SaveDiscussionAlertDialog dialog) {
        dialog.dismiss();
        SharedPrefrences.setBool("IS_BudzMap_My_SAVE_Dialog_Shown", true, this);
        JSONObject object = new JSONObject();
        try {
            if (isFavorite) {
                isFavorite = false;
                Favorite_it.setImageResource(R.drawable.ic_favorite_border_white);
                object.put("sub_user_id", budz_map_item_clickerd_dataModel.getId());
                object.put("is_like", 0);
                budz_map_item_clickerd_dataModel.setGet_user_save_count(0);
            } else {
                isFavorite = true;
                Favorite_it.setImageResource(R.drawable.ic_favorite_white);
                object.put("sub_user_id", budz_map_item_clickerd_dataModel.getId());
                object.put("is_like", 1);
                budz_map_item_clickerd_dataModel.setGet_user_save_count(1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new VollyAPICall(BudzMapDetailsActivity.this, false, URL.save_favorit_bud, object, user.getSession_key(), Request.Method.POST, BudzMapDetailsActivity.this, save_favorit_bud);
    }

    @Override
    public void onCancelClicked(SaveDiscussionAlertDialog dialog) {
        dialog.dismiss();
    }


    public Fragment getTabOneFragment() {

        if (BudzType.equalsIgnoreCase("1")) {
            return detailsBusinessInfoTabFragment = new DetailsBusinessInfoTabFragment(this);
        } else if (BudzType.equalsIgnoreCase("2") || BudzType.equalsIgnoreCase("6") || BudzType.equalsIgnoreCase("7")) {
            return detailsBusinessInfoMedicalTabFragment = new DetailsBusinessInfoMedicalTabFragment(this);
        } else if (BudzType.equalsIgnoreCase("3")) {
            return detailsBusinessInfoCannabitesTabFragment = new DetailsBusinessInfoCannabitesTabFragment(this);
        } else if (BudzType.equalsIgnoreCase("5")) {
            return detailsBusinessInfoEventTabFragment = new DetailsBusinessInfoEventTabFragment(this);
        } else if (BudzType.equalsIgnoreCase("9")) {
            return detailsBusinessInfoTabOtherFragment = new DetailsBusinessInfoTabOtherFragment(this);
        } else {
            return detailsBusinessInfoTabFragment = new DetailsBusinessInfoTabFragment(this);
        }
    }


    public Fragment getTabTwoFragment() {
        if (BudzType.equalsIgnoreCase("5")) {
            return new EventPricesANDTicketesTabFragment("-1");
        } else {
            if (BudzType.equalsIgnoreCase("3")) {
                return new ProductServicesTabFragment("3");
            } else {
                return new ProductServicesTabFragment(BudzType);
            }

        }
    }

    public Fragment getTabThreeFragment() {
        return new SpecialInfoTabFragment();
    }


    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == save_favorit_bud) {
            Log.d("response", response);
        } else if (apiActions == get_budz_map) {
            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONObject object = jsonObject.getJSONObject("successData");
                BudzMapHomeDataModel dataModel = new BudzMapHomeDataModel();

                dataModel.setId(object.getInt("id"));
                dataModel.setUser_id(object.getInt("user_id"));
                dataModel.setBusiness_type_id(object.getInt("business_type_id"));
                dataModel.setTitle(object.getString("title"));
                dataModel.setLogo(object.getString("logo"));
                dataModel.setBanner(object.optString("banner"));
                dataModel.setBanner_full(object.optString("banner_full"));
                dataModel.setIs_organic(object.getInt("is_organic"));
                dataModel.setIs_delivery(object.getInt("is_delivery"));
                dataModel.setDescription(object.getString("description"));
                dataModel.setLocation(object.getString("location"));
                dataModel.setOthers_image(object.optString("others_image"));
                dataModel.setLat(object.getDouble("lat"));
                dataModel.setLng(object.getDouble("lng"));
                if (object.getString("stripe_id").length() > 1 && !object.getString("stripe_id").equalsIgnoreCase("null")) {
                    dataModel.setIs_featured(1);
                } else {
                    dataModel.setIs_featured(0);
                }
                dataModel.setPhone(object.getString("phone"));
                dataModel.setOthers_image(object.optString("others_image"));
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
                budz_map_item_clickerd_dataModel = dataModel;
                budz_map_item_clickerd_dataModel_abc = dataModel;
                mainIdHere = budz_map_item_clickerd_dataModel.getId();
                BudzType = budz_map_item_clickerd_dataModel.getBusiness_type_id() + "";
                if (BudzType.equalsIgnoreCase("9")) {
                    Budz_detail_images.setVisibility(View.INVISIBLE);
                } else {
                    Budz_detail_images.setVisibility(View.VISIBLE);
                }
                //budz_map_item_clickerd_dataModel.getUser_id() == Splash.user.getUser_id() &&
                if (budz_map_item_clickerd_dataModel.getIs_featured() == 1) {
                    other_tabs.setVisibility(View.VISIBLE);
                    tabs.setWeightSum(12.0F);
                } else if (budz_map_item_clickerd_dataModel.getUser_id() == Splash.user.getUser_id() && !BudzType.equalsIgnoreCase("9")) {
                    other_tabs.setVisibility(View.VISIBLE);
                    tabs.setWeightSum(12.0F);
                } else {
                    other_tabs.setVisibility(View.GONE);
                    tabs.setWeightSum(12.0F);
                }
                SetTopData();
//                if ( Arrays.asList(Constants.statZipCode).contains(Integer.parseInt(user.getZip_code()))){
//                    new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
//                            .setTitleText("Error!")
//                            .setContentText("You can't view this type of business in your area.")
//                            .setConfirmText("Okay!")
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    sweetAlertDialog.dismissWithAnimation();
//                                    finish();
//                                }
//                            }).show();
//                }
                if (budz_map_item_clickerd_dataModel.getBusiness_type_id() == 2 || budz_map_item_clickerd_dataModel.getBusiness_type_id() == 7 || budz_map_item_clickerd_dataModel.getBusiness_type_id() == 6) {
                    new VollyAPICall(BudzMapDetailsActivity.this, true, getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + user.getZip_code() + getLocatioZipcode_part3, new JSONObject(), null, Request.Method.POST, BudzMapDetailsActivity.this, APIActions.ApiActions.chech_state);
                }
                if (isPaidDone) {
                    isPaidDone = false;
                    new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("Congratulations!")
                            .setContentText("Your business is now paid.")
                            .setConfirmText("Okay!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();

                                }
                            }).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == update_subscription) {
            isPaidDone = true;
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(BudzMapDetailsActivity.this, true, URL.get_budz + "/" + budz_map_item_clickerd_dataModel.getId() + "?lat=0.00&lng=0.00 ", jsonObject, user.getSession_key(), Request.Method.GET, BudzMapDetailsActivity.this, get_budz_map);

        } else if (apiActions == chech_state) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                int stst = -1;
                if (jsonArray.length() > 0) {
                    JSONObject object = jsonArray.getJSONObject(0);
                    JSONArray array = jsonArray.getJSONObject(0).getJSONArray("address_components");
                    for (int i = 0; i < array.length(); i++) {
                        String name = array.getJSONObject(i).getString("long_name");
                        for (int j = 0; j < Constants.stateList.length; j++) {
                            if (Constants.stateList[j].equalsIgnoreCase(name)) {

                                stst = 1;
                            }
                        }
                    }
                    if (stst == -1) {
                        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("Error!")
                                .setContentText("You can't view this type of business in your area.")
                                .setConfirmText("Okay!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        finish();
                                    }
                                }).show();
                    }

                } else {
                    new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("Error!")
                            .setContentText("You can't view this type of business in your area.")
                            .setConfirmText("Okay!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    finish();
                                }
                            }).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Error!")
                        .setContentText("You can't view this type of business in your area.")
                        .setConfirmText("Okay!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                finish();
                            }
                        }).show();
            }


        }
    }

    boolean isPaidDone = false;

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnSnedClicked(JSONObject data, int position) {
        //use less
        flag_it.setImageResource(R.drawable.ic_flag_budz);

        try {
            data.put("budz_id", budz_map_item_clickerd_dataModel.getId());

            new VollyAPICall(BudzMapDetailsActivity.this, false, URL.add_budz_flag, data, user.getSession_key(), Request.Method.POST, BudzMapDetailsActivity.this, APIActions.ApiActions.add_budz_flag);
            budz_map_item_clickerd_dataModel.setFlagged(true);
            setFlaggedCall(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }


    @Override
    public void SetTopDataCall() {
        isFromTop = true;
        JSONObject jsonObject = new JSONObject();
        if (budz_map_item_clickerd_dataModel != null) {
            new VollyAPICall(BudzMapDetailsActivity.this, true, URL.get_budz + "/" + budz_map_item_clickerd_dataModel.getId() + "?lat=0.00&lng=0.00 ", jsonObject, user.getSession_key(), Request.Method.GET, BudzMapDetailsActivity.this, get_budz_map);
        } else {
            //mainIdHere
            new VollyAPICall(BudzMapDetailsActivity.this, true, URL.get_budz + "/" + mainIdHere + "?lat=0.00&lng=0.00 ", jsonObject, user.getSession_key(), Request.Method.GET, BudzMapDetailsActivity.this, get_budz_map);
        }

    }

    boolean isFlagged = false;

    @Override
    public void setFlaggedCall(boolean isFlag) {
        isFlagged = isFlag;
        if (budz_map_item_clickerd_dataModel != null)
            budz_map_item_clickerd_dataModel.setFlagged(isFlagged);
        if (isFlag) {
            flag_it.setImageResource(R.drawable.ic_flag_budz);
        } else {
            flag_it.setImageResource(R.drawable.ic_flag_white);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.UPGRADED) {
            isPaidDone = true;
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(BudzMapDetailsActivity.this, true, URL.get_budz + "/" + budz_map_item_clickerd_dataModel.getId() + "?lat=0.00&lng=0.00 ", jsonObject, user.getSession_key(), Request.Method.GET, BudzMapDetailsActivity.this, get_budz_map);

        }
    }

    @Override
    public void TokenGenrate(Token token) {
        Log.d("token", token.toString());
        JSONObject object = new JSONObject();
        try {
            object.put("stripe_token", token.getId());
            object.put("budz_id", budz_map_item_clickerd_dataModel.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new VollyAPICall(this, true, URL.update_subscription, object, user.getSession_key(), Request.Method.POST, this, update_subscription);
    }

    @Override
    public void onSendShootOutButtonClick(SendShootOutAlertDialog dialog) {
        dialog.dismiss();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
        transaction.replace(R.id.fragment_layout, getTabThreeFragment());
        transaction.commitAllowingStateLoss();
        Comment_layout.setClickable(false);
        Comment_layout.setEnabled(false);
        Tab_One.setBackgroundColor(Color.parseColor("#5c5c5c"));
        Tab_two.setBackgroundColor(Color.parseColor("#5c5c5c"));
        Tab_three.setBackgroundColor(Color.parseColor("#932a88"));

        Tab_three.setTextColor(Color.parseColor("#e9d4e7"));
        Tab_two.setTextColor(Color.parseColor("#b0b0b0"));
        Tab_One.setTextColor(Color.parseColor("#b0b0b0"));
    }

    @Override
    public void onSendShootOutButtonClick(SendSpecialAlertDialog dialog) {
        dialog.dismiss();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
        transaction.replace(R.id.fragment_layout, getTabThreeFragment());
        transaction.commitAllowingStateLoss();
        Comment_layout.setClickable(false);
        Comment_layout.setEnabled(false);
        Tab_One.setBackgroundColor(Color.parseColor("#5c5c5c"));
        Tab_two.setBackgroundColor(Color.parseColor("#5c5c5c"));
        Tab_three.setBackgroundColor(Color.parseColor("#932a88"));

        Tab_three.setTextColor(Color.parseColor("#e9d4e7"));
        Tab_two.setTextColor(Color.parseColor("#b0b0b0"));
        Tab_One.setTextColor(Color.parseColor("#b0b0b0"));
    }

    @Override
    public void onSendShootOutButtonClick(SendSpecialAlertDialog dialog, BudzMapSpecialProducts item, int pos) {

    }

    public void onCreateSpecial() {
        budzMapHomeDataModels = new ArrayList<>();
        budzMapHomeDataModels.add(budz_map_item_clickerd_dataModel);
        if (budzMapHomeDataModels.size() > 0) {

            SendSpecialAlertDialog sendShootOutAlertDialog = SendSpecialAlertDialog.newInstance(BudzMapDetailsActivity.this);
            sendShootOutAlertDialog.show(this.getSupportFragmentManager(), "dialog");
        } else {
            CustomeToast.ShowCustomToast(this, "You can't create a shout out with pending paid business!", Gravity.TOP);
        }
    }
}
