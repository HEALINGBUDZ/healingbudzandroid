package com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.codingpixel.healingbudz.DataModel.ReportQuestionListDataModel;
import com.codingpixel.healingbudz.DataModel.StrainDataModel;
import com.codingpixel.healingbudz.DataModel.UserStrainDetailsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.ReportItView.Report;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.tabs.StrainDetailsTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.tabs.StrainLocateThisBudTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.tabs.StrainOverViewTabFragment;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.SliderViewStrainDetail;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderLayout;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView;
import com.codingpixel.healingbudz.customeUI.sliderclasses.Tricks.ViewPagerEx;
import com.codingpixel.healingbudz.custome_dialog.save_discussion.SaveDiscussionAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.interfaces.ShowMoreStrainDetails;
import com.codingpixel.healingbudz.interfaces.TopDataCallBack;
import com.codingpixel.healingbudz.interfaces.ViewAllDetailsAddedByUserButtonListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_file_with_progress.UploadFileWithProgress;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.codingpixel.healingbudz.static_function.IntentFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.view.View.VISIBLE;
import static com.codingpixel.healingbudz.Utilities.Constants.D_FORMAT_ONE;
import static com.codingpixel.healingbudz.Utilities.StrainRatingImage.Strain_Rating;
import static com.codingpixel.healingbudz.Utilities.UserNameTextColorWRTRating.getUserRatingColor;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_strains;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.save_favorit_strain;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareHBContent;

//import com.daimajia.slider.library.Animations.DescriptionAnimation;
//import com.daimajia.slider.library.SliderLayout;
//import com.daimajia.slider.library.SliderTypes.BaseSliderView;
//import com.daimajia.slider.library.Tricks.ViewPagerEx;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class
StrainDetailsActivity extends AppCompatActivity implements ShowMoreStrainDetails, View.OnClickListener, SaveDiscussionAlertDialog.OnDialogFragmentClickListener, ViewAllDetailsAddedByUserButtonListner, ReportSendButtonLstner, APIResponseListner, ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener, TopDataCallBack {
    ImageView Slider_img;
    TextView Photo_Powered_By, Photo_Date;
    ImageView Images_activity;
    Button Tab_One, Tab_two, Tab_three;
    ImageView Back, Home, Share_btn, Favorite_it, Back_button_eddits_by_user;
    boolean isFavorite = false;
    LinearLayout Comment_layout;
    LinearLayout pic_detail;
    public static boolean isDataUpdateFromImageActivity = false;
    RelativeLayout Main_layout;
    public static Report strain_report;
    int active_count = 0;
    ImageView Upload_image_button;
    boolean isBackPressed = false;
    public static RelativeLayout Main_content_stain_Deail_Activity;
    LinearLayout Menu_Main_content, Like_btn, Dislike_btn, Flag_Strain;
    ImageView Flag_Img;
    RelativeLayout Menu_Main_contentHIde;

    TextView Edit_By_User_Name, Edit_by_Date, Edit_By_like_Count;
    ImageView Edit_By_like_Thumb;
    LinearLayout pressed_all_edit;

    LinearLayout Menu_all_edits_by_user, review_take_to;
    TextView StainTitle, StrainType, Rating, Total_reviews, Like_Count, Dislike_count;
    ImageView Image_Prevois, Image_Next, Rating_image, Like_Icon, Dislike_icon,type_image;
    public static StrainDataModel strainDataModel;
    public static int comment_top_int_y = 4000;
    public static ScrollView strainDetailsActivity_scrollView;
    String strain_id = "";
    boolean isStrainDataLoadAble = false;
    private SliderLayout mDemoSlider;
    private boolean isKeyValuApi = false;
    private String keyWordApi = "";
    SwipeRefreshLayout swipe_strain;
    LinearLayout tabs;
    View line_botn;
    int tabNUm = 1;

    @Override
    protected void onPause() {
        if (mDemoSlider != null)
            mDemoSlider.stopAutoCycle();
        super.onPause();

    }

    @Override
    protected void onRestart() {
//        if (mDemoSlider != null) {
//            if (strainDataModel != null && strainDataModel.getImages() != null && strainDataModel.getImages().size() > 0) {
//                mDemoSlider.startAutoCycle();
//            } else {
//                mDemoSlider.stopAutoCycle();
//            }
//        }
        super.onRestart();

    }

    boolean isRefrsh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strain_details);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getBoolean("isKeyValuApi", false)) {
                keyWordApi = extras.getString("keyWordApi", "");
                isKeyValuApi = true;
                isStrainDataLoadAble = false;
                strain_id = "";
            } else {
                isKeyValuApi = false;
                strain_id = extras.getInt("strain_id") + "";
                if (strain_id.equalsIgnoreCase("0")) {
                    isStrainDataLoadAble = false;
                    strain_id = "";
                } else {
                    isStrainDataLoadAble = true;
                }
            }

        } else {
            isStrainDataLoadAble = false;
            isKeyValuApi = false;
            strain_id = "";
        }


        mDemoSlider = findViewById(R.id.slider);
        swipe_strain = findViewById(R.id.swipe_strain);
        mDemoSlider.setVisibility(View.GONE);
        tabs = findViewById(R.id.tabs);
        pic_detail = findViewById(R.id.pic_detail);
//        mDemoSlider.onInterceptTouchEvent(null);
//        mDemoSlider
//        mDemoSlider.


        Photo_Powered_By = findViewById(R.id.photo_powered_by);
        Photo_Date = findViewById(R.id.photo_date);
        Slider_img = findViewById(R.id.slider_img);
        Main_content_stain_Deail_Activity = findViewById(R.id.main_conten);
        Images_activity = findViewById(R.id.images_activity);
        Share_btn = findViewById(R.id.share_btn);
        Share_btn.setOnClickListener(this);
        Favorite_it = findViewById(R.id.favorite_it);
        Favorite_it.setOnClickListener(this);

        Back = findViewById(R.id.back_btn);
        Home = findViewById(R.id.home_btn);
        Comment_layout = findViewById(R.id.review_take_to);
        Back.setOnClickListener(this);
        Home.setOnClickListener(this);
        Image_Prevois = findViewById(R.id.image_left);
        Image_Next = findViewById(R.id.imgae_right);
        Tab_One = findViewById(R.id.tab_one);
        Tab_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu_all_edits_by_user.setVisibility(View.GONE);
                line_botn.setVisibility(VISIBLE);
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(line_botn.getWidth(), line_botn.getHeight());
//                params.set
//                line_botn.setLayoutParams();
                tabs.setVisibility(VISIBLE);
                tabNUm = 1;
                Menu_Main_contentHIde.setVisibility(VISIBLE);
                Main_content_stain_Deail_Activity.setVisibility(VISIBLE);
                Menu_Main_content.setVisibility(VISIBLE);
                Comment_layout.setClickable(true);
                Comment_layout.setEnabled(true);
                if (!getTabOneFragment().isVisible()) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.fade_in);
                    transaction.replace(R.id.fragment_layout, getTabOneFragment());
                    transaction.commitAllowingStateLoss();
                }
                Tab_One.setBackgroundColor(Color.parseColor("#f4c42f"));
                Tab_two.setBackgroundColor(Color.parseColor("#5c5c5c"));
                Tab_three.setBackgroundColor(Color.parseColor("#5c5c5c"));

                Tab_One.setTextColor(Color.parseColor("#FFFFFF"));
                Tab_three.setTextColor(Color.parseColor("#c4c4c4"));
                Tab_two.setTextColor(Color.parseColor("#c4c4c4"));
            }
        });

        Tab_two = findViewById(R.id.tab_two);
        Tab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu_all_edits_by_user.setVisibility(View.GONE);
                line_botn.setVisibility(VISIBLE);
                tabs.setVisibility(VISIBLE);
                tabNUm = 2;
                Menu_Main_contentHIde.setVisibility(VISIBLE);
                Main_content_stain_Deail_Activity.setVisibility(VISIBLE);
                Menu_Main_content.setVisibility(VISIBLE);
                Comment_layout.setClickable(false);
                Comment_layout.setEnabled(false);
                if (!getTabOneFragment().isVisible()) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.fade_in);
                    transaction.replace(R.id.fragment_layout, getTabTwoFragment());
                    transaction.commitAllowingStateLoss();
                }

                Tab_One.setBackgroundColor(Color.parseColor("#5c5c5c"));
                Tab_two.setBackgroundColor(Color.parseColor("#f4c42f"));
                Tab_three.setBackgroundColor(Color.parseColor("#5c5c5c"));

                Tab_two.setTextColor(Color.parseColor("#FFFFFF"));
                Tab_three.setTextColor(Color.parseColor("#c4c4c4"));
                Tab_One.setTextColor(Color.parseColor("#c4c4c4"));
            }
        });
        Tab_three = findViewById(R.id.tab_three);
        Tab_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu_all_edits_by_user.setVisibility(View.GONE);
                line_botn.setVisibility(VISIBLE);
                tabs.setVisibility(VISIBLE);
                tabNUm = 3;
                Menu_Main_contentHIde.setVisibility(VISIBLE);
                Main_content_stain_Deail_Activity.setVisibility(VISIBLE);
                Menu_Main_content.setVisibility(VISIBLE);
                Tab_One.setBackgroundColor(Color.parseColor("#5c5c5c"));
                Tab_two.setBackgroundColor(Color.parseColor("#5c5c5c"));
                Tab_three.setBackgroundColor(Color.parseColor("#f4c42f"));
                Comment_layout.setClickable(false);
                Comment_layout.setEnabled(false);
                Tab_three.setTextColor(Color.parseColor("#FFFFFF"));
                Tab_two.setTextColor(Color.parseColor("#c4c4c4"));
                Tab_One.setTextColor(Color.parseColor("#c4c4c4"));

                if (!getTabOneFragment().isVisible()) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragment_layout, getTabThreeFragment());
                    transaction.commitAllowingStateLoss();
                }
            }
        });
        strainDetailsActivity_scrollView = findViewById(R.id.main_scroll);
        strainDetailsActivity_scrollView.fullScroll(View.FOCUS_UP);


        Comment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strainDetailsActivity_scrollView.smoothScrollTo(0, 4100);
            }
        });

        Images_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strainDataModel.getImages().size() > 0) {
                    Menu_all_edits_by_user.setVisibility(View.GONE);
                    line_botn.setVisibility(VISIBLE);
                    tabs.setVisibility(VISIBLE);
                    Menu_Main_contentHIde.setVisibility(VISIBLE);
                    Main_content_stain_Deail_Activity.setVisibility(VISIBLE);
                    Menu_Main_content.setVisibility(VISIBLE);
                    GoTo(StrainDetailsActivity.this, StrainImagesActivity.class);
                } else {
                    CustomeToast.ShowCustomToast(getApplicationContext(), "No Image Added In Gallery!", Gravity.TOP);
                }
            }
        });

        Menu_Main_content = findViewById(R.id.menu_top_content);
        Menu_Main_contentHIde = findViewById(R.id.top_content);
        Menu_all_edits_by_user = findViewById(R.id.all_edit_added_top_menu);
        line_botn = findViewById(R.id.line_botn);

        Back_button_eddits_by_user = findViewById(R.id.back_btn_eddit);
        Back_button_eddits_by_user.setOnClickListener(this);

        Image_Prevois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (active_count > 1) {
//                    active_count = active_count - 1;
//                    LoadImage(strainDataModel.getImages().get(active_count).getImage_path());
//                }
                mDemoSlider.startAutoCycle(10000, 10000, true);
                mDemoSlider.movePrevPosition(true);
            }
        });

        Image_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (active_count < strainDataModel.getImages().size() - 1) {
//                    active_count = active_count + 1;
//                    LoadImage(strainDataModel.getImages().get(active_count).getImage_path());
//                }
                mDemoSlider.startAutoCycle(10000, 10000, true);
                mDemoSlider.moveNextPosition(true);

            }
        });


        Main_layout = findViewById(R.id.main_cntnt_strain);
        strain_report = new Report(this, this, "#f4c42f", "strain");
        Main_layout.addView(strain_report.getView());
        strain_report.InitSlide();

        Like_btn = findViewById(R.id.like_strain);

        Dislike_btn = findViewById(R.id.dislike_strain);

        Flag_Img = findViewById(R.id.flag_img);
        Flag_Strain = findViewById(R.id.flag_strain);


        Upload_image_button = findViewById(R.id.upload_image_button);
        Upload_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StrainDetailsActivity.this, HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", false);
                startActivityForResult(intent, 1200);
            }
        });

        Edit_By_User_Name = findViewById(R.id.all_edits_name);
        Edit_by_Date = findViewById(R.id.all_edits_date);
        Edit_By_like_Thumb = findViewById(R.id.all_edits_like_thumb);
        pressed_all_edit = findViewById(R.id.pressed_all_edit);
        Edit_By_like_Count = findViewById(R.id.all_edits_like_count);

        if (isStrainDataLoadAble) {
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(StrainDetailsActivity.this, true, URL.get_strain + "/" + strain_id, jsonObject, user.getSession_key(), Request.Method.GET, StrainDetailsActivity.this, get_strains);
        } else {
            if (isKeyValuApi) {
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(StrainDetailsActivity.this, true, URL.strain_details_by_name + keyWordApi, jsonObject, user.getSession_key(), Request.Method.GET, StrainDetailsActivity.this, get_strains);
            } else {
                SetData();
            }
        }
        swipe_strain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefrsh = true;
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(StrainDetailsActivity.this, true, URL.get_strain + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainDetailsActivity.this, get_strains);
            }
        });
    }

    void setPager() {
        HashMap<String, StrainDataModel.Images> url_maps = new HashMap<String, StrainDataModel.Images>();
        for (int i = 0; i < strainDataModel.getImages().size(); i++) {
            url_maps.put(strainDataModel.getImages().get(i).getImage_path(), strainDataModel.getImages().get(i));
        }


        for (String name : url_maps.keySet()) {
            SliderViewStrainDetail textSliderView = new SliderViewStrainDetail(this);
            // initialize a SliderLayout
            Bundle bundle = new Bundle();
            bundle.putString("date", DateConverter.getCustomDateString(url_maps.get(name).getUpdated_at()));
            bundle.putInt("color", getUserRatingColor(url_maps.get(name).getUser_rating()));
            bundle.putInt("id", url_maps.get(name).getUser_id());
            textSliderView
                    .description(url_maps.get(name).getName())
                    .image(images_baseurl + url_maps.get(name).getImage_path())
                    .bundle(bundle)
//                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            //add your extra information

            textSliderView.bundle(bundle);
            textSliderView.getBundle()
                    .putString("extra", url_maps.get(name).getName());

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
//        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(10000);
        mDemoSlider.addOnPageChangeListener(this);
        if (strainDataModel.getImages().size() > 0) {
            mDemoSlider.startAutoCycle();
        } else {
            mDemoSlider.stopAutoCycle();
        }

        if (strainDataModel.getImages().size() == 0 || strainDataModel.getImages().size() == 1) {
            mDemoSlider.stopAutoCycle();

            Image_Next.setVisibility(View.INVISIBLE);
            Image_Prevois.setVisibility(View.INVISIBLE);
            Slider_img.setVisibility(VISIBLE);
            pic_detail.setVisibility(VISIBLE);
            mDemoSlider.setVisibility(View.GONE);
            Photo_Powered_By.setText(strainDataModel.getImages().get(0).getName() + "");
            Photo_Powered_By.setTextColor(getUserRatingColor(strainDataModel.getImages().get(0).getUser_rating()));
            Photo_Date.setText(DateConverter.getCustomDateString(strainDataModel.getImages().get(0).getUpdated_at()));
            Glide.with(StrainDetailsActivity.this)
                    .load(images_baseurl + strainDataModel.getImages().get(0).getImage_path())
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
//                            Slider_img.setImageDrawable(resource);
                            return false;
                        }
                    }).into(Slider_img);
        } else {
            Slider_img.setVisibility(View.GONE);
            pic_detail.setVisibility(View.INVISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDemoSlider.setVisibility(VISIBLE);
                }
            }, 1000);

            Image_Next.setVisibility(VISIBLE);
            mDemoSlider.startAutoCycle();
            Image_Prevois.setVisibility(VISIBLE);
        }
        Dislike_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrainDetailsActivity.isDataUpdateFromImageActivity = true;
                if (mDemoSlider.getVisibility() == VISIBLE) {
                    positionsImage = mDemoSlider.getCurrentPosition();
                    if (strainDataModel.getImages().size() == positionsImage)
                        positionsImage = positionsImage - 1;
                } else {
                    positionsImage = 0;
                }
                try {
                    JSONObject jsonObject = new JSONObject();
                    if (strainDataModel.getImages().get(positionsImage).isIs_current_user_dislike()) {
//                        strainDataModel.setCurrent_user_dis_like(0);
//                        Dislike_icon.setImageResource(R.drawable.ic_dislike_icon);
//                        jsonObject.put("is_dislike", 0);
//                        Dislike_count.setTextColor(Color.parseColor("#FFFFFF"));
//                        strainDataModel.setGet_dislikes_count(strainDataModel.getGet_dislikes_count() - 1);
                        strainDataModel.getImages().get(positionsImage).setIs_current_user_dislike(false);
                        Dislike_icon.setImageResource(R.drawable.ic_dislike_icon);
                        jsonObject.put("is_disliked", 0);
                        Dislike_count.setTextColor(Color.parseColor("#FFFFFF"));
                        Dislike_icon.setColorFilter(0);
                        strainDataModel.getImages().get(positionsImage).setDis_like_count(strainDataModel.getImages().get(positionsImage).getDis_like_count() - 1);
                    } else {
                        Dislike_icon.setImageResource(R.drawable.ic_dislike_icon_disliked);
                        Dislike_icon.setColorFilter(Color.parseColor("#FEC14A"), PorterDuff.Mode.SRC_IN);
                        strainDataModel.getImages().get(positionsImage).setIs_current_user_dislike(true);
                        jsonObject.put("is_disliked", 1);
                        Dislike_count.setTextColor(Color.parseColor("#FEC14A"));
                        strainDataModel.getImages().get(positionsImage).setDis_like_count(strainDataModel.getImages().get(positionsImage).getDis_like_count() + 1);
//                        strainDataModel.getImages().get(positionsImage).setIs_current_user_dislike(true);
//                        Dislike_icon.setImageResource(R.drawable.ic_dislike_icon_disliked);
//                        jsonObject.put("is_disliked", 0);
//                        Dislike_count.setTextColor(Color.parseColor("#FFFFFF"));
//                        Dislike_icon.setColorFilter(0);
//                        strainDataModel.getImages().get(positionsImage).setDis_like_count(strainDataModel.getImages().get(positionsImage).getDis_like_count() + 1);
//                        Dislike_icon.setImageResource(R.drawable.ic_dislike_icon_disliked);
//                        strainDataModel.setCurrent_user_dis_like(1);
//                        jsonObject.put("is_dislike", 1);
//                        Dislike_count.setTextColor(Color.parseColor("#6CB835"));
//                        strainDataModel.setGet_dislikes_count(strainDataModel.getGet_dislikes_count() + 1);

                    }
                    if (strainDataModel.getImages().get(positionsImage).isIs_current_user_liked()) {
                        strainDataModel.getImages().get(positionsImage).setIs_current_user_liked(false);
                        Like_Icon.setImageResource(R.drawable.ic_like_icon);
                        Like_Icon.setColorFilter(0);
                        strainDataModel.getImages().get(positionsImage).setLike_count(strainDataModel.getImages().get(positionsImage).getLike_count() - 1);
                        Like_Count.setTextColor(Color.parseColor("#FFFFFF"));
                        Like_Count.setText(strainDataModel.getImages().get(positionsImage).getLike_count() + "");
                    }

                    Dislike_count.setText(strainDataModel.getImages().get(positionsImage).getDis_like_count() + "");
                    jsonObject.put("strain_image_id", strainDataModel.getImages().get(positionsImage).getId());

                    new VollyAPICall(StrainDetailsActivity.this, false, URL.save_user_image_strain_dislike, jsonObject, user.getSession_key(), Request.Method.POST, StrainDetailsActivity.this, APIActions.ApiActions.testAPI);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrainDetailsActivity.isDataUpdateFromImageActivity = true;
                if (mDemoSlider.getVisibility() == VISIBLE) {
                    positionsImage = mDemoSlider.getCurrentPosition();
                    if (strainDataModel.getImages().size() == positionsImage)
                        positionsImage = positionsImage - 1;
                } else {
                    positionsImage = 0;
                }
                try {
                    JSONObject jsonObject = new JSONObject();
                    if (strainDataModel.getImages().get(positionsImage).isIs_current_user_liked()) {
                        strainDataModel.getImages().get(positionsImage).setIs_current_user_liked(false);
                        Like_Icon.setImageResource(R.drawable.ic_like_icon);
                        Like_Icon.setColorFilter(0);
                        jsonObject.put("is_like", 0);
                        Like_Count.setTextColor(Color.parseColor("#FFFFFF"));
                        strainDataModel.getImages().get(positionsImage).setLike_count(strainDataModel.getImages().get(positionsImage).getLike_count() - 1);

                    } else {
                        Like_Icon.setImageResource(R.drawable.ic_like_icon_liked);
                        Like_Icon.setColorFilter(Color.parseColor("#FEC14A"), PorterDuff.Mode.SRC_IN);
                        strainDataModel.getImages().get(positionsImage).setIs_current_user_liked(true);
                        jsonObject.put("is_like", 1);
                        Like_Count.setTextColor(Color.parseColor("#FEC14A"));
                        strainDataModel.getImages().get(positionsImage).setLike_count(strainDataModel.getImages().get(positionsImage).getLike_count() + 1);
                    }

                    if (strainDataModel.getImages().get(positionsImage).isIs_current_user_dislike()) {
                        strainDataModel.getImages().get(positionsImage).setIs_current_user_dislike(false);
                        Dislike_icon.setImageResource(R.drawable.ic_dislike_icon);
                        Dislike_icon.setColorFilter(0);
                        strainDataModel.getImages().get(positionsImage).setDis_like_count(strainDataModel.getImages().get(positionsImage).getDis_like_count() - 1);
                        Dislike_count.setTextColor(Color.parseColor("#FFFFFF"));
                        Dislike_count.setText(strainDataModel.getImages().get(positionsImage).getDis_like_count() + "");
                    }
                    Like_Count.setText(strainDataModel.getImages().get(positionsImage).getLike_count() + "");
                    jsonObject.put("strain_image_id", strainDataModel.getImages().get(positionsImage).getId());
                    new VollyAPICall(StrainDetailsActivity.this, true, URL.save_user_image_strain_like, jsonObject, user.getSession_key(), Request.Method.POST, StrainDetailsActivity.this, APIActions.ApiActions.testAPI);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    if (strainDataModel.getCurrent_user_like() == 1) {
//                        strainDataModel.setCurrent_user_like(0);
//                        Like_Icon.setImageResource(R.drawable.ic_like_icon);
//                        jsonObject.put("is_like", 0);
//                        Like_Count.setTextColor(Color.parseColor("#FFFFFF"));
//                        strainDataModel.setGet_likes_count(strainDataModel.getGet_likes_count() - 1);
//
//                    } else {
//                        Like_Icon.setImageResource(R.drawable.ic_like_icon_liked);
//                        strainDataModel.setCurrent_user_like(1);
//                        jsonObject.put("is_like", 1);
//                        Like_Count.setTextColor(Color.parseColor("#6CB835"));
//                        strainDataModel.setGet_likes_count(strainDataModel.getGet_likes_count() + 1);
//                    }
//
//                    if (strainDataModel.getCurrent_user_dis_like() == 1) {
//                        strainDataModel.setCurrent_user_dis_like(0);
//                        Dislike_icon.setImageResource(R.drawable.ic_dislike_icon);
//                        strainDataModel.setGet_dislikes_count(strainDataModel.getGet_dislikes_count() - 1);
//                        Dislike_count.setTextColor(Color.parseColor("#FFFFFF"));
//                        Dislike_count.setText(strainDataModel.getGet_dislikes_count() + "");
//                    }
//                    Like_Count.setText(strainDataModel.getGet_likes_count() + "");
//                    jsonObject.put("is_dislike", 0);
//                    jsonObject.put("strain_id", strainDataModel.getId());
//
//                    new VollyAPICall(StrainDetailsActivity.this, false, URL.strain_like_dislike, jsonObject, user.getSession_key(), Request.Method.POST, StrainDetailsActivity.this, APIActions.ApiActions.testAPI);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
        Flag_Strain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                if (mDemoSlider.getVisibility() == VISIBLE) {
                    positionsImage = mDemoSlider.getCurrentPosition();
                    if (strainDataModel.getImages().size() == positionsImage)
                        positionsImage = positionsImage - 1;
                } else {
                    positionsImage = 0;
                }
                try {
                    jsonObject.put("strain_id", strainDataModel.getId());
                    if (strainDataModel.getImages().get(positionsImage).isIs_current_user_flaged()) {
//                        strainDataModel.setCurrent_user_flag(0);
//                        jsonObject.put("is_flaged", 0);
//                        jsonObject.put("reason", "");
//                        Flag_Img.setImageResource(R.drawable.ic_flag_white);
//                        new VollyAPICall(StrainDetailsActivity.this, false, URL.strain_flaged, jsonObject, user.getSession_key(), Request.Method.POST, StrainDetailsActivity.this, APIActions.ApiActions.testAPI);
                        CustomeToast.ShowCustomToast(StrainDetailsActivity.this, "you already reported this photo!", Gravity.TOP);
                    } else {
                        ArrayList<ReportQuestionListDataModel> dataModels = new ArrayList<>();
                        dataModels.add(new ReportQuestionListDataModel("Nudity or sexual content", false));
                        dataModels.add(new ReportQuestionListDataModel("Harassment or hate speech", false));
                        dataModels.add(new ReportQuestionListDataModel("Threatening, violent, or concerning", false));
                        dataModels.add(new ReportQuestionListDataModel("Spam", false));
                        dataModels.add(new ReportQuestionListDataModel("Offensive", false));
                        dataModels.add(new ReportQuestionListDataModel("Unrelated", false));
                        strain_report.SlideDown(positionsImage, dataModels, StrainDetailsActivity.this, "strain");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        mDemoSlider

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void LoadImage(String Path) {
        Context context = StrainDetailsActivity.this;
        if (context != null && !this.isDestroyed()) {
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

    public void SetData() {
        swipe_strain.setRefreshing(false);
        StainTitle = findViewById(R.id.main_heading_title);
        Like_Count = findViewById(R.id.like_count);
        Rating_image = findViewById(R.id.rating_image);
        Like_Icon = findViewById(R.id.like_icon);
        Dislike_icon = findViewById(R.id.dislike_icon);
        Dislike_count = findViewById(R.id.dislike_count);
        StrainType = findViewById(R.id.strain_type);
        type_image = findViewById(R.id.type_image);
        if (isRefrsh) {
            switch (tabNUm) {
                case 1:
                    Tab_One.performClick();
                    break;
                case 2:
                    Tab_two.performClick();
                    break;
                case 3:
                    Tab_three.performClick();
                    break;
            }
        } else {
            if (strainOverViewTabFragment != null && !strainOverViewTabFragment.isVisible()) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.fade_in);
                if (strainOverViewTabFragment.isAdded()) {
                    transaction.remove(strainOverViewTabFragment);
                }
                transaction.add(R.id.fragment_layout, getTabOneFragment());
                transaction.commitAllowingStateLoss();
            } else if (strainOverViewTabFragment == null) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.fade_in);
                transaction.add(R.id.fragment_layout, getTabOneFragment());
                transaction.commitAllowingStateLoss();
            }
        }

        if (strainDataModel != null && strainDataModel.getImages() != null) {
            if (strainDataModel.getImages().size() > 0) {
                Dislike_btn.setVisibility(VISIBLE);
                Like_btn.setVisibility(VISIBLE);
                Flag_Strain.setVisibility(VISIBLE);
                setValutop(0);
                Photo_Powered_By.setText(strainDataModel.getImages().get(0).getName() + "");
                Photo_Powered_By.setTextColor(getUserRatingColor(strainDataModel.getImages().get(0).getUser_rating()));
                Photo_Date.setText(DateConverter.getCustomDateString(strainDataModel.getImages().get(0).getUpdated_at()));
                Glide.with(StrainDetailsActivity.this)
                        .load(images_baseurl + strainDataModel.getImages().get(0).getImage_path())
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
//                                Slider_img.setImageDrawable(resource);
                                return false;
                            }
                        }).into(Slider_img);
                setPager();
            } else {
                Dislike_btn.setVisibility(View.INVISIBLE);
                Flag_Strain.setVisibility(View.INVISIBLE);
                Like_btn.setVisibility(View.INVISIBLE);
                Image_Next.setVisibility(View.INVISIBLE);
                Image_Prevois.setVisibility(View.INVISIBLE);
                Slider_img.setVisibility(VISIBLE);

            }
        } else {
            Slider_img.setVisibility(VISIBLE);
            Image_Next.setVisibility(View.INVISIBLE);
            Image_Prevois.setVisibility(View.INVISIBLE);
        }
        isBackPressed = false;

        StainTitle.setText(strainDataModel.getTitle());


        StrainType.setText(strainDataModel.getType_title());
        type_image.setImageResource(R.drawable.ic_hyb);
        if (strainDataModel.getAlphabetic_keyword().equalsIgnoreCase("i")) {
            type_image.setImageResource(R.drawable.ic_ind);
        } else if (strainDataModel.getAlphabetic_keyword().equalsIgnoreCase("s")) {
            type_image.setImageResource(R.drawable.ic_sti);
        } else if (strainDataModel.getAlphabetic_keyword().equalsIgnoreCase("h")) {
            type_image.setImageResource(R.drawable.ic_hyb);
        }
        Rating = findViewById(R.id.rating);
        Rating.setText(D_FORMAT_ONE.format(strainDataModel.getRating()) + "");


        Total_reviews = findViewById(R.id.total_reviews);
        Total_reviews.setText(strainDataModel.getReviews());


//        Like_Count.setText(strainDataModel.getGet_likes_count() + "");


//        Dislike_count.setText(strainDataModel.getGet_dislikes_count() + "");


        Rating_image.setImageResource(Strain_Rating(strainDataModel.getRating()));


//        if (strainDataModel.getCurrent_user_like() == 0) {
//            Like_Icon.setImageResource(R.drawable.ic_like_icon);
//            Like_Icon.setColorFilter(0);
//            Like_Count.setTextColor(Color.parseColor("#FFFFFF"));
//        } else {
//            Like_Icon.setImageResource(R.drawable.ic_like_icon_liked);
//            Like_Icon.setColorFilter(Color.parseColor("#FEC14A"), PorterDuff.Mode.SRC_IN);
//            Like_Count.setTextColor(Color.parseColor("#6CB835"));
//        }
//
//        if (strainDataModel.getCurrent_user_dis_like() == 0) {
//            Dislike_icon.setImageResource(R.drawable.ic_dislike_icon);
//            Dislike_icon.setColorFilter(0);
//            Dislike_count.setTextColor(Color.parseColor("#FFFFFF"));
//        } else {
//            Dislike_icon.setImageResource(R.drawable.ic_dislike_icon_disliked);
//            Dislike_icon.setColorFilter(Color.parseColor("#FEC14A"), PorterDuff.Mode.SRC_IN);
//            Dislike_count.setTextColor(Color.parseColor("#6CB835"));
//        }

        if (strainDataModel.getFavorite() == 1) {
            isFavorite = true;
            Favorite_it.setImageResource(R.drawable.ic_favorite_white);
        } else {
            isFavorite = false;
            Favorite_it.setImageResource(R.drawable.ic_favorite_border_white);
        }


    }

    public void MakeSlider() {
        if (!this.isBackPressed) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (strainDataModel.getImages().size() > 0) {
                        if (active_count == strainDataModel.getImages().size() - 1) {
                            active_count = 0;
                        } else {
                            active_count++;
                        }
                        if (!isBackPressed) {
                            try {
//                                MakeSlider();
                                Photo_Powered_By.setText(strainDataModel.getImages().get(active_count).getName() + "");
                                Photo_Powered_By.setTextColor(getUserRatingColor(strainDataModel.getImages().get(active_count).getUser_rating()));
                                Photo_Date.setText(DateConverter.getCustomDateString(strainDataModel.getImages().get(active_count).getUpdated_at()));
//                                LoadImage(strainDataModel.getImages().get(active_count).getImage_path());
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }, 5000);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                isBackPressed = true;
                finish();
                break;
            case R.id.home_btn:
                GoToHome(StrainDetailsActivity.this, true);
                finish();
                break;
            case R.id.share_btn:
                JSONObject object = new JSONObject();
                try {
                    object.put("id", strainDataModel.getId());
                    object.put("type", "Strain");
                    object.put("content", strainDataModel.getTitle());
                    object.put("url", URL.sharedBaseUrl + "/strain-details/" + strainDataModel.getId());
                    object.put("msg", "Check out Healing Budz for your smartphone: " + URL.sharedBaseUrl + "/strain-details/" + strainDataModel.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareHBContent(StrainDetailsActivity.this, object);
//                SaveDiscussionAlertDialog saveDiscussionAlertDialog = SaveDiscussionAlertDialog.newInstance("SAVED STRAIN", "Strains are saved in the app menu under My Saves.", "Got it! Do not show again for Strains I save.", StrainDetailsActivity.this);
//                saveDiscussionAlertDialog.show(this.getSupportFragmentManager(), "dialog");
                break;
            case R.id.favorite_it:

                try {
                    JSONObject jsonObject = new JSONObject();
                    if (isFavorite) {
                        jsonObject.put("is_favorit", 0);
                        strainDataModel.setFavorite(0);
                        isFavorite = false;
                        Favorite_it.setImageResource(R.drawable.ic_favorite_border_white);
                    } else {
                        if (!SharedPrefrences.getBool("IS_STRAIN_MY_SAVE_Dialog_Shown", this)) {
                            SaveDiscussionAlertDialog saveDiscussionAlertDialog = SaveDiscussionAlertDialog.newInstance("SAVED STRAIN", "Strains are saved in the app menu under My Saves.", "Got it! Do not show again for Strains I save.", StrainDetailsActivity.this);
                            saveDiscussionAlertDialog.show(getSupportFragmentManager(), "dialog");
                        }
                        jsonObject.put("is_favorit", 1);
                        strainDataModel.setFavorite(1);
                        isFavorite = true;
                        Favorite_it.setImageResource(R.drawable.ic_favorite_white);
                    }
                    jsonObject.put("strain_id", strainDataModel.getId());
                    new VollyAPICall(StrainDetailsActivity.this, false, URL.save_favorit_strain, jsonObject, user.getSession_key(), Request.Method.POST, StrainDetailsActivity.this, save_favorit_strain);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.back_btn_eddit:
                Menu_all_edits_by_user.setVisibility(View.GONE);
                line_botn.setVisibility(VISIBLE);
                tabs.setVisibility(VISIBLE);
                Menu_Main_contentHIde.setVisibility(VISIBLE);

                Main_content_stain_Deail_Activity.setVisibility(VISIBLE);
                Menu_Main_content.setVisibility(VISIBLE);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_layout, getTabTwoFragment());
                transaction.commitAllowingStateLoss();
                break;
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
    public void onOkClicked(SaveDiscussionAlertDialog dialog) {
        SharedPrefrences.setBool("IS_STRAIN_MY_SAVE_Dialog_Shown", true, this);
        dialog.dismiss();
    }

    @Override
    public void onCancelClicked(SaveDiscussionAlertDialog dialog) {
        dialog.dismiss();
    }

    public Fragment getTabOneFragment() {
        return strainOverViewTabFragment = new StrainOverViewTabFragment(this, StrainDetailsActivity.this);
    }

    StrainOverViewTabFragment strainOverViewTabFragment;

    public Fragment getTabTwoFragment() {
        return new StrainDetailsTabFragment(this);

    }

    public Fragment getTabThreeFragment() {
        return new StrainLocateThisBudTabFragment(StrainDetailsActivity.this);
    }

    @Override
    public void ShowDetails() {
        Tab_One.setBackgroundColor(Color.parseColor("#5c5c5c"));
        Tab_two.setBackgroundColor(Color.parseColor("#f4c42f"));
        Tab_three.setBackgroundColor(Color.parseColor("#5c5c5c"));

        Tab_two.setTextColor(Color.parseColor("#FFFFFF"));
        Tab_three.setTextColor(Color.parseColor("#c4c4c4"));
        Tab_One.setTextColor(Color.parseColor("#c4c4c4"));

        if (!getTabOneFragment().isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_layout, getTabTwoFragment());
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void viewAllEditsButtonClick(int position, final UserStrainDetailsDataModel userStrainDetailsDataModel) {
        Menu_all_edits_by_user.setVisibility(VISIBLE);
        line_botn.setVisibility(View.GONE);
//        Main_content_stain_Deail_Activity.setVisibility(View.GONE);
        tabs.setVisibility(View.GONE);
        Menu_Main_contentHIde.setVisibility(View.GONE);
        Menu_Main_content.setVisibility(View.GONE);
        strainDetailsActivity_scrollView.fullScroll(View.FOCUS_UP);
        Edit_By_like_Count.setText(userStrainDetailsDataModel.getGet_likes_count() + "");
        Edit_By_User_Name.setText(userStrainDetailsDataModel.getUser_name());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date mDate = sdf.parse(userStrainDetailsDataModel.getCreated_at());
            long timeInMilliseconds = mDate.getTime();
//            String agoo = getTimeAgo(timeInMilliseconds);
            Edit_by_Date.setText(DateConverter.getPrettyTime(userStrainDetailsDataModel.getCreated_at()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (userStrainDetailsDataModel.getGet_user_like_count() == 1) {
            Edit_By_like_Thumb.setImageResource(R.drawable.ic_thumb_licked);
            Edit_By_like_Thumb.setColorFilter(Color.parseColor("#f4c42f"), PorterDuff.Mode.SRC_IN);
        } else {
            Edit_By_like_Thumb.setImageResource(R.drawable.ic_thumb_unliked);
            Edit_By_like_Thumb.setColorFilter(0);
        }
        pressed_all_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                if (userStrainDetailsDataModel.getUser_id() != Splash.user.getUser_id()) {
                    if (userStrainDetailsDataModel.getGet_user_like_count() == 1) {
//                        Like(userStrainDetailsDataModel.getId(), 0);
                        userStrainDetailsDataModel.setGet_user_like_count(0);
                        Edit_By_like_Thumb.setImageResource(R.drawable.ic_thumb_unliked);
                        Edit_By_like_Thumb.setColorFilter(0);
                        userStrainDetailsDataModel.setGet_likes_count(userStrainDetailsDataModel.getGet_likes_count() - 1);
                        try {
                            jsonObject.put("user_strain_id", userStrainDetailsDataModel.getId());
                            jsonObject.put("is_like", 0);
                            jsonObject.put("strain_id", userStrainDetailsDataModel.getStrain_id());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
//                        Like(userStrainDetailsDataModel.getId(), 1);
                        userStrainDetailsDataModel.setGet_user_like_count(1);
                        Edit_By_like_Thumb.setImageResource(R.drawable.ic_thumb_licked);
                        Edit_By_like_Thumb.setColorFilter(Color.parseColor("#f4c42f"), PorterDuff.Mode.SRC_IN);
                        userStrainDetailsDataModel.setGet_likes_count(userStrainDetailsDataModel.getGet_likes_count() + 1);
//                    holder.Reward_image.setVisibility(View.VISIBLE);
//                        setOtherLikeFalse(position, 0);
                        try {
                            jsonObject.put("user_strain_id", userStrainDetailsDataModel.getId());
                            jsonObject.put("is_like", 1);
                            jsonObject.put("strain_id", userStrainDetailsDataModel.getStrain_id());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Edit_By_like_Count.setText(userStrainDetailsDataModel.getGet_likes_count() + "");
                    new VollyAPICall(v.getContext(), false, URL.save_user_strain_like, jsonObject, user.getSession_key(), Request.Method.POST, StrainDetailsActivity.this, APIActions.ApiActions.testAPI);
                } else {
                    CustomeToast.ShowCustomToast(getContext(), "You can't like your own info!", Gravity.TOP);
                }
            }
        });

    }

    @Override
    public void OnSnedClicked(JSONObject data, int position) {
        StrainDetailsActivity.isDataUpdateFromImageActivity = true;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("strain_image_id", strainDataModel.getImages().get(position).getId());
            jsonObject.put("is_flagged", 1);
            jsonObject.put("reason", data.getString("reason"));
            strainDataModel.getImages().get(position).setIs_current_user_flaged(true);
            new VollyAPICall(StrainDetailsActivity.this, true, URL.save_user_image_strain_flag, jsonObject, user.getSession_key(), Request.Method.POST, StrainDetailsActivity.this, APIActions.ApiActions.testAPI);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("strain_id", strainDataModel.getId());
//            strainDataModel.setCurrent_user_flag(1);
//            jsonObject.put("is_flaged", 1);
//            jsonObject.put("reason", data.getString("reason"));
//            Flag_Img.setImageResource(R.drawable.ic_flag_strain);
//            new VollyAPICall(StrainDetailsActivity.this, false, URL.strain_flaged, jsonObject, user.getSession_key(), Request.Method.POST, StrainDetailsActivity.this, APIActions.ApiActions.testAPI);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == APIActions.ApiActions.strain_flaged) {
        } else if (apiActions == APIActions.ApiActions.upload_strain_image) {
            CustomeToast.ShowCustomToast(this, "Thanks bud! Your image has been submitted for approval.", Gravity.TOP);
//            StrainDataModel.Images img = new StrainDataModel.Images();
//            try {
//                JSONObject image_object = new JSONObject(response).getJSONObject("successData");
//                img.setId(image_object.getInt("id"));
//                img.setStrain_id(image_object.getInt("strain_id"));
//                img.setUser_id(image_object.getInt("user_id"));
//                img.setImage_path(image_object.getString("image_path"));
//                img.setIs_approved(0);
//                img.setIs_main(0);
//                img.setName(user.getFirst_name());
//                img.setCreated_at(image_object.getString("created_at"));
//                img.setUpdated_at(image_object.getString("updated_at"));
//                ArrayList<StrainDataModel.Images> images = strainDataModel.getImages();
//                images.add(img);
//                strainDataModel.setImages(images);
//                setPager();
////                bannerSlider.nex
//                Slider_img.setVisibility(View.GONE);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        } else if (apiActions == save_favorit_strain) {

        } else if (apiActions == get_strains) {
            try {
                JSONObject strain_object = new JSONObject(response).getJSONObject("successData");
                StrainDataModel strainDataModel = new StrainDataModel();
                strainDataModel.setMathces(0);
                strainDataModel.setId(strain_object.getInt("id"));
                strainDataModel.setType_id(strain_object.getInt("type_id"));
                strainDataModel.setTitle(strain_object.getString("title"));
                strainDataModel.setOverview(strain_object.getString("overview"));
                strainDataModel.setApproved(strain_object.getInt("approved"));
                strainDataModel.setCreated_at(strain_object.getString("created_at"));
                strainDataModel.setUpdated_at(strain_object.getString("updated_at"));
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
//                    img.setName(image_object.getJSONObject("get_user").getString("first_name"));
//                    img.setUser_rating(image_object.getJSONObject("get_user").getInt("points"));

                    img.setLike_count(image_object.getJSONArray("like_count").length());
                    img.setDis_like_count(image_object.getJSONArray("dis_like_count").length());
                    if (image_object.isNull("liked")) {
                        img.setIs_current_user_liked(false);
                    } else {
                        img.setIs_current_user_liked(true);
                    }

                    if (image_object.isNull("disliked")) {
                        img.setIs_current_user_dislike(false);
                    } else {
                        img.setIs_current_user_dislike(true);
                    }

                    if (image_object.isNull("flagged")) {
                        img.setIs_current_user_flaged(false);
                    } else {
                        img.setIs_current_user_flaged(true);
                    }

                    images.add(img);
                }

                //add banner using resource drawable


                strainDataModel.setImages(images);
                if (strain_object.optJSONObject("rating_sum") != null) {
//                    strainDataModel.setRating_sum(Double.parseDouble(strain_object.getJSONObject("rating_sum").getDouble("total") + ""));
//                    strainDataModel.setRating(Double.parseDouble(strain_object.getJSONObject("rating_sum").getDouble("total") + ""));
                    strainDataModel.setRating_sum(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").getDouble("total"))));
                    strainDataModel.setRating(Double.valueOf(D_FORMAT_ONE.format(strain_object.getJSONObject("rating_sum").getDouble("total"))));

                }
                strainDataModel.setAlphabetic_keyword(String.valueOf(strainDataModel.getType_title().charAt(0)));
                strainDataModel.setReviews(strain_object.getInt("get_review_count") + " Reviews");
                StrainDetailsActivity.strainDataModel = strainDataModel;
                SetData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        swipe_strain.setRefreshing(false);
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(StrainDetailsActivity.this, object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            Log.d("paths", data.getExtras().getString("file_path_arg"));
            Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
            bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
            UploadImage(data.getExtras().getString("file_path_arg"));
        }
    }

    public void UploadImage(String image) {
        JSONArray parameter = new JSONArray();
        parameter.put("strain_id");
        JSONArray values = new JSONArray();
        values.put(strainDataModel.getId());
        new UploadFileWithProgress(this, true, URL.upload_strain_image, image, "image", values, parameter, null, StrainDetailsActivity.this, APIActions.ApiActions.upload_strain_image).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDataUpdateFromImageActivity) {
            isDataUpdateFromImageActivity = false;
            SetData();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        setValutop(position);
    }

    int positionsImage = 0;

    public void setValutop(int position) {
        if (mDemoSlider.getVisibility() == VISIBLE) {
            positionsImage = mDemoSlider.getCurrentPosition();
            if (strainDataModel.getImages().size() == positionsImage)
                positionsImage = positionsImage - 1;
        } else {
            positionsImage = 0;
        }
        if (positionsImage < strainDataModel.getImages().size()) {
            if (!strainDataModel.getImages().get(positionsImage).isIs_current_user_liked()) {
                Like_Icon.setImageResource(R.drawable.ic_like_icon);
                Like_Icon.setColorFilter(0);
                Like_Count.setTextColor(Color.parseColor("#FFFFFF"));
                Like_Count.setText(strainDataModel.getImages().get(positionsImage).getLike_count() + "");
            } else {
                Like_Icon.setImageResource(R.drawable.ic_like_icon_liked);
                Like_Icon.setColorFilter(Color.parseColor("#FEC14A"), PorterDuff.Mode.SRC_IN);
                Like_Count.setTextColor(Color.parseColor("#FEC14A"));
                Like_Count.setText(strainDataModel.getImages().get(positionsImage).getLike_count() + "");
            }
            if (strainDataModel.getImages().get(positionsImage).isIs_current_user_flaged()) {
                Flag_Img.setImageResource(R.drawable.ic_flag_strain);
            } else {
                Flag_Img.setImageResource(R.drawable.ic_flag_white);
            }
            if (!strainDataModel.getImages().get(positionsImage).isIs_current_user_dislike()) {
                Dislike_icon.setImageResource(R.drawable.ic_dislike_icon);
                Dislike_icon.setColorFilter(0);
                Dislike_count.setTextColor(Color.parseColor("#FFFFFF"));
                Dislike_count.setText(strainDataModel.getImages().get(positionsImage).getDis_like_count() + "");
            } else {
                Dislike_icon.setImageResource(R.drawable.ic_dislike_icon_disliked);
                Dislike_icon.setColorFilter(Color.parseColor("#FEC14A"), PorterDuff.Mode.SRC_IN);
                Dislike_count.setTextColor(Color.parseColor("#FEC14A"));
                Dislike_count.setText(strainDataModel.getImages().get(positionsImage).getDis_like_count() + "");
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
        setValutop(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Bundle bundle = slider.getBundle();
        if (bundle.containsKey("id")) {
            IntentFunction.GoToProfile(this, bundle.getInt("id"));
        }
//        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void SetTopDataCall() {
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(StrainDetailsActivity.this, true, URL.get_strain + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainDetailsActivity.this, get_strains);
    }

    @Override
    public void setFlaggedCall(boolean isFlag) {

    }
}
