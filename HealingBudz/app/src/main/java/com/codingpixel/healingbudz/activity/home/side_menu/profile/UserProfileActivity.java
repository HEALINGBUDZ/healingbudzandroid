package com.codingpixel.healingbudz.activity.home.side_menu.profile;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.DataModel.MessagesDataModel;
import com.codingpixel.healingbudz.DataModel.UserProfileQATabDataModel;
import com.codingpixel.healingbudz.DataModel.UserProfileQATabExpandAbleData;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.ReportItView.WallTopDropDowns;
import com.codingpixel.healingbudz.Utilities.GetUserLatLng;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.Registration.AddExpertiseQuestionActivity;
import com.codingpixel.healingbudz.activity.age_verification.AgeVerification;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.wall_feeds.WallFeedsMainFragment;
import com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagesActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingChatViewActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.my_answers.MyAnswersActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.budz_map_tab.BudzMapFragment;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.dialog.EditUserProfileUploadPhotoAlertDialog;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.dialog.UserFollowFollwingAlertDialog;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.groups_tab.GroupFragment;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.hb_gallery.HealingBudGallery;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.journal_tab.JournalFragment;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.qa_tab.QAFragment;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.review_tab.ReviewFragment;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.strain_tab.StrainFragment;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.BudzMapHomeRecylAdapter;
import com.codingpixel.healingbudz.adapter.ExpertAreaRecylerAdapter;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.UserFollowCallBack;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.google.gson.Gson;
import com.onesignal.OneSignal;
import com.onesignal.shortcutbadger.ShortcutBadger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.codingpixel.healingbudz.Utilities.Constants.D_FORMAT_ONE;
import static com.codingpixel.healingbudz.Utilities.SetUserValuesInSP.delete_UserValues;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.activity.home.HomeActivity.Profile_Img;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel_abc;
import static com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingChatViewActivity.chat_message_data_modal;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.follow_unfollow_user;
import static com.codingpixel.healingbudz.network.model.URL.delete_answer;
import static com.codingpixel.healingbudz.network.model.URL.follow_user;
import static com.codingpixel.healingbudz.network.model.URL.get_user_profile;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.logout;
import static com.codingpixel.healingbudz.network.model.URL.un_follow;
import static com.codingpixel.healingbudz.network.model.URL.update_image;
import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.ClearSharedPrefrences;
import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.setBool;
import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.setString;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;

//import com.google.android.gms.ads.MobileAds;

public class UserProfileActivity extends AppCompatActivity implements APIResponseListner, BudzMapHomeRecylAdapter.ItemClickListener, UserFollowFollwingAlertDialog.OnDialogFragmentClickListener, UserFollowCallBack, UserLocationListner, EditUserProfileUploadPhotoAlertDialog.OnDialogFragmentClickListener {
    RecyclerView my_listing_recyler_vewi;
    NestedScrollView scrollView;
    RelativeLayout InfoDialog, EditUserParentRelative;
    BudzMapHomeRecylAdapter my_listin_recyler_adapter;
    ImageView InfoButton;
    LinearLayout Healing_Bud_Gallery;
    boolean isUpdate = false;
    TextView my_listing_no_record, Follow_btn_text;
    ArrayList<BudzMapHomeDataModel> dataModels = new ArrayList<>();
    ImageView Back, Home, User_Rating_image, Follow_btn_icon;
    public static int USER_ID;
    public static boolean isGoneToOther = false;
    public static UserProfileDataModel profiledataModel = new UserProfileDataModel();
    public static UserProfileDataModel profiledataModelLoggedUser = new UserProfileDataModel();
    LinearLayout Follow_Button;
    ImageView Cover_Photo, Profile_Image, profile_img_topi;
    RecyclerView Medical_experties_rc, Strain_experties_rc;
    ExpertAreaRecylerAdapter question_two_adapter;
    ExpertAreaRecylerAdapter question_one_adapter;
    TextView UserName, Follower_count, Following_count, User_Bio, Medical_experties, Strain_experties,
            Rating_dialog_title, User_Rating, User_Rating_title;
    View User_Rating_seprator;
    ImageView Edit_Profile;
    LinearLayout User_Message_Layout, Follow_layout, Unfollow_layout;
    LinearLayout User_Logout_Layout;
    boolean isInfoDialogOpen = false;
    QAFragment qaFragment;
    RelativeLayout block_user;
    GroupFragment groupFragment;
    StrainFragment strainFragment;
    JournalFragment journalFragment = new JournalFragment();
    BudzMapFragment budzMapFragment;
    ReviewFragment reviewFragment;
    WallFeedsMainFragment wallFeedsMainFragment;
    WallTopDropDowns topDropDowns;
    TextView text_gallery, text_msg;


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
        setContentView(R.layout.activity_user_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        USER_ID = getIntent().getExtras().getInt("user_id");
//        MobileAds.initialize(this, Constants.add_init);
        groupFragment = new GroupFragment(USER_ID);
        strainFragment = new StrainFragment(USER_ID);
        budzMapFragment = new BudzMapFragment(USER_ID);
        reviewFragment = new ReviewFragment(USER_ID);

        topDropDowns = new WallTopDropDowns(UserProfileActivity.this);
        RelativeLayout layout = findViewById(R.id.activity_user_profile_parent_view);
        layout.addView(topDropDowns.getView());

        wallFeedsMainFragment = new WallFeedsMainFragment(USER_ID, true, topDropDowns, UserProfileActivity.this);
        Bundle bundle = new Bundle();
        bundle.putInt("USER_ID", USER_ID);
        wallFeedsMainFragment.setArguments(bundle);
        Healing_Bud_Gallery = findViewById(R.id.healing_bud_gallery);
        block_user = findViewById(R.id.block_user);
        my_listing_recyler_vewi = findViewById(R.id.my_listin_recyler_view);
        Follow_btn_text = findViewById(R.id.follow_btn_text);
        text_gallery = findViewById(R.id.text_gallery);
        text_msg = findViewById(R.id.text_msg);
        Follow_btn_icon = findViewById(R.id.follow_btn_icon);
        RecyclerView.LayoutManager layoutManager_home_saerch = new LinearLayoutManager(UserProfileActivity.this);
        my_listing_recyler_vewi.setLayoutManager(layoutManager_home_saerch);
        scrollView = findViewById(R.id.scroll_view);
        Follow_layout = findViewById(R.id.follow_layout);
        Unfollow_layout = findViewById(R.id.unfollow_layout);
        Follow_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (USER_ID == user.getUser_id()) {
                    if (profiledataModel.getFollowers_count() > 0) {

                        UserFollowFollwingAlertDialog followerJournalAlertDialog = UserFollowFollwingAlertDialog.newInstance(UserProfileActivity.this, USER_ID + "", false);
                        followerJournalAlertDialog.show(getSupportFragmentManager(), "dialog");
                    }
                }


            }
        });
        Unfollow_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (USER_ID == user.getUser_id()) {
                    if (profiledataModel.getFollowings_count() > 0) {
                        UserFollowFollwingAlertDialog followerJournalAlertDialog = UserFollowFollwingAlertDialog.newInstance(UserProfileActivity.this, USER_ID + "", true);
                        followerJournalAlertDialog.show(getSupportFragmentManager(), "dialog");
                    }
                }
            }
        });
        scrollView.fullScroll(View.FOCUS_UP);

        Healing_Bud_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                Intent intent = new Intent(UserProfileActivity.this, HealingBudGallery.class);
                intent.putExtra("user_id", USER_ID);
                intent.putExtra("name", UserName.getText().toString().trim());
                startActivity(intent);
            }
        });
        Back = findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                if (SharedPrefrences.getBool("isFromSignup", UserProfileActivity.this)) {
                    setBool("isFromSignup", false, UserProfileActivity.this);
                    GoToHome(UserProfileActivity.this, true);
                    finish();
                } else {
                    finish();
                }

            }
        });

        Home = findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                GoToHome(UserProfileActivity.this, true);
                finish();
            }
        });

        InitTabs();
        InfoButton = findViewById(R.id.info_button);
        InfoDialog = findViewById(R.id.user_profile_info_dialog);

        InfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInfoDialogOpen) {
                    isInfoDialogOpen = true;
                    InfoDialog.setVisibility(View.VISIBLE);
                    Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                    InfoDialog.startAnimation(startAnimation);
                } else {
                    isInfoDialogOpen = false;
                    InfoDialog.setVisibility(View.GONE);
                    Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                    InfoDialog.startAnimation(startAnimation);
                }
            }
        });

        User_Message_Layout = findViewById(R.id.user_messages_layout);
        User_Message_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                if (USER_ID == user.getUser_id()) {
                    GoTo(UserProfileActivity.this, MessagesActivity.class);
                } else {
                    MessagesDataModel messagesDataModel = new MessagesDataModel();
                    messagesDataModel.setId(USER_ID);
                    messagesDataModel.setSender_id(USER_ID);
                    messagesDataModel.setReceiver_id(user.getUser_id());
                    messagesDataModel.setLast_message_id(USER_ID);
                    messagesDataModel.setSender_deleted(0);
                    messagesDataModel.setReceiver_deleted(0);
                    messagesDataModel.setCreated_at(profiledataModel.getCreated_at());
                    messagesDataModel.setUpdated_at(profiledataModel.getCreated_at());
                    messagesDataModel.setMessages_count(0);
                    messagesDataModel.setSender_first_name(profiledataModel.getFirst_name());
                    messagesDataModel.setSender_image_path(profiledataModel.getImage_path());
                    messagesDataModel.setSender_avatar(profiledataModel.getAvatar());
                    messagesDataModel.setReceiver_first_name(profiledataModel.getFirst_name());
                    messagesDataModel.setReceiver_image_path(profiledataModel.getImage_path());
                    messagesDataModel.setReceiver_avatar(profiledataModel.getImage_path());
                    chat_message_data_modal = messagesDataModel;
                    GoTo(UserProfileActivity.this, MessagingChatViewActivity.class);
                }
            }
        });

        User_Logout_Layout = findViewById(R.id.user_logout_layout);
        User_Logout_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logout
                OneSignal.clearOneSignalNotifications();
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancelAll();
                ShortcutBadger.removeCount(UserProfileActivity.this);
                JSONObject object = new JSONObject();
                try {
                    String android_id = Settings.Secure.getString(UserProfileActivity.this.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    object.put("device_id", android_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                budz_map_item_clickerd_dataModel = null;
                budz_map_item_clickerd_dataModel_abc = null;
                new VollyAPICall(UserProfileActivity.this, false, logout, object, user.getSession_key(), Request.Method.POST, UserProfileActivity.this, APIActions.ApiActions.logout);
                InfoDialogDisappear();
                ClearSharedPrefrences(UserProfileActivity.this);
                final ProgressDialog pd = ProgressDialog.newInstance();
                pd.show(UserProfileActivity.this.getSupportFragmentManager(), "pd");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Collection<String> keys = new ArrayList<>();
                        keys.add("user_id");
                        keys.add("device_type");
                        OneSignal.deleteTags(keys);
                        setString("user_email", "", UserProfileActivity.this);
                        setString("user_password", "", UserProfileActivity.this);
                        setBool("is_user_login", false, UserProfileActivity.this);

                        delete_UserValues(UserProfileActivity.this);
                        SharedPrefrences.setBool("first_launch_overview_screen", false, UserProfileActivity.this);
                        Intent i = new Intent(UserProfileActivity.this, AgeVerification.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                }, 1000);

            }
        });
        Edit_Profile = findViewById(R.id.edit_profile);
        Edit_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                isUpdate = true;

                GoTo(UserProfileActivity.this, EditUserProfileActivity.class);
            }
        });
        EditUserParentRelative = findViewById(R.id.edit_user_parent_relative);
        EditUserParentRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
            }
        });
        Init();
        MakeRealView(true);
//        JSONObject jsonObject = new JSONObject();
//        new VollyAPICall(UserProfileActivity.this
//                , false
//                , get_user_profile + "/" + USER_ID + "?lat=" + SharedPrefrences.getString("lat_cur", this) + "&lng=" + SharedPrefrences.getString("lng_cur", this)
//                , jsonObject, user.getSession_key(), Request.Method.GET, UserProfileActivity.this, APIActions.ApiActions.get_user_profile);

        Follow_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                String url = "";
                if (profiledataModel.getIs_following_count() == 1) {
                    Follow_btn_icon.setImageResource(R.drawable.ic_profile_add);
                    Follow_btn_text.setText("Follow");
                    profiledataModel.setIs_following_count(0);
                    profiledataModel.setFollowers_count(profiledataModel.getFollowers_count() - 1);
                    Follower_count.setText(profiledataModel.getFollowers_count() + "");
                    url = un_follow;

                } else {
                    Follow_btn_icon.setImageResource(R.drawable.ic_cross_image);
                    Follow_btn_text.setText("Unfollow");
                    profiledataModel.setIs_following_count(1);
                    profiledataModel.setFollowers_count(profiledataModel.getFollowers_count() + 1);
                    Follower_count.setText(profiledataModel.getFollowers_count() + "");
                    url = follow_user;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("followed_id", USER_ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new VollyAPICall(UserProfileActivity.this, false, url, jsonObject, user.getSession_key(), Request.Method.POST, UserProfileActivity.this, follow_unfollow_user);
            }
        });
        new GetUserLatLng().getUserLocation(this, this);
    }

    Double user_latitude;
    Double user_longitude;

    @Override
    public void onLocationSuccess(Location location) {

        user_latitude = location.getLatitude();
        user_longitude = location.getLongitude();
        SharedPrefrences.setString("lat_cur", String.valueOf(user_latitude), this);
        SharedPrefrences.setString("lng_cur", String.valueOf(user_longitude), this);
        new VollyAPICall(UserProfileActivity.this
                , false
                , get_user_profile + "/" + USER_ID + "?lat=" + SharedPrefrences.getString("lat_cur", this, "0.0") + "&lng=" + SharedPrefrences.getString("lng_cur", this, "0.0")
                , new JSONObject(), user.getSession_key(), Request.Method.GET, UserProfileActivity.this, APIActions.ApiActions.get_user_profile);
    }

    @Override
    public void onLocationFailed(String Error) {
        new VollyAPICall(UserProfileActivity.this
                , false
                , get_user_profile + "/" + USER_ID + "?lat=" + "0.0" + "&lng=" + "0.0"
                , new JSONObject(), user.getSession_key(), Request.Method.GET, UserProfileActivity.this, APIActions.ApiActions.get_user_profile);
    }

    private void InfoDialogDisappear() {
        if (InfoDialog.getVisibility() == View.VISIBLE) {
            if (!isInfoDialogOpen) {
                isInfoDialogOpen = true;
                InfoDialog.setVisibility(View.VISIBLE);
                Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                InfoDialog.startAnimation(startAnimation);
            } else {
                isInfoDialogOpen = false;
                InfoDialog.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                InfoDialog.startAnimation(startAnimation);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isUpdate) {
            isUpdate = false;
            SharedPrefrences.getString("lat_cur", this);
            SharedPrefrences.getString("lng_cur", this);
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(UserProfileActivity.this
                    , true
                    , get_user_profile + "/" + USER_ID + "?lat=" + SharedPrefrences.getString("lat_cur", this, "0.0") + "&lng=" + SharedPrefrences.getString("lng_cur", this, "0.0")
                    , jsonObject, user.getSession_key(), Request.Method.GET, UserProfileActivity.this, APIActions.ApiActions.get_user_profile);
//            SetProfileData(profiledataModel);
        }
//        if(isGoneToOther){
//            isGoneToOther = false;
//            SetProfileData(profiledataModelLoggedUser);
//        }
    }

    public void Init() {
        Follow_Button = findViewById(R.id.follow_button);

        Cover_Photo = findViewById(R.id.cover_photo);
        Profile_Image = findViewById(R.id.profile_image);
        profile_img_topi = findViewById(R.id.profile_img_topi);
        UserName = findViewById(R.id.user_name);
        Following_count = findViewById(R.id.following_count);
        Follower_count = findViewById(R.id.follower_count);
        User_Bio = findViewById(R.id.user_bio);
        Medical_experties = findViewById(R.id.medial_experties);
        Medical_experties_rc = findViewById(R.id.medial_experties_rc);
        Medical_experties_rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Strain_experties = findViewById(R.id.setrain_experties);
        Strain_experties_rc = findViewById(R.id.setrain_experties_rc);
        Strain_experties_rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Rating_dialog_title = findViewById(R.id.rating_title);
        User_Rating_image = findViewById(R.id.user_rating_image);
        User_Rating = findViewById(R.id.user_rating);
        User_Rating_seprator = findViewById(R.id.user_rating_seprator);
        User_Rating_title = findViewById(R.id.user_rating_title);
        my_listing_no_record = findViewById(R.id.my_listing_no_record_found);
        if (USER_ID == user.getUser_id()) {
            User_Message_Layout.setVisibility(View.GONE);
            User_Logout_Layout.setVisibility(View.VISIBLE);
            Follow_Button.setVisibility(View.GONE);
            Edit_Profile.setVisibility(View.VISIBLE);
            text_gallery.setText("Edit My HB Gallery");
            text_msg.setText("Messages");
            Profile_Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (profiledataModel.getSpecial_icon().length() > 6) {
//                        EditUserProfileUploadPhotoAlertDialog shootOutAlertDialog = EditUserProfileUploadPhotoAlertDialog.newInstance(UserProfileActivity.this, false, true, Profile_Image.getDrawable(), profile_img_topi.getDrawable());
//                        shootOutAlertDialog.show(getSupportFragmentManager(), "dialog");
//                    } else {
//                        EditUserProfileUploadPhotoAlertDialog shootOutAlertDialog = EditUserProfileUploadPhotoAlertDialog.newInstance(UserProfileActivity.this, false, Profile_Image.getDrawable());
//                        shootOutAlertDialog.show(getSupportFragmentManager(), "dialog");
//                    }
                }
            });
        } else {
            text_gallery.setText("My HB Gallery");
            text_msg.setText("Message");
            User_Message_Layout.setVisibility(View.VISIBLE);
            User_Logout_Layout.setVisibility(View.GONE);
            Follow_Button.setVisibility(View.VISIBLE);
            Edit_Profile.setVisibility(View.INVISIBLE);
        }
        block_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new VollyAPICall(v.getContext(),
                            true
                            , URL.block_user,
                            new JSONObject().put("user_id", USER_ID)
                            , Splash.user.getSession_key(),
                            Request.Method.POST
                            , new APIResponseListner() {
                        @Override
                        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                            new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Blocked")
                                    .setContentText("User blocked successfully!")
                                    .setConfirmText("Okay!")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                        }
                                    })
                                    .show();
                        }

                        @Override
                        public void onRequestError(String response, APIActions.ApiActions apiActions) {
                            Log.d("Response", response);
                            try {
                                JSONObject object = new JSONObject(response);
                                CustomeToast.ShowCustomToast(UserProfileActivity.this, object.optString("errorMessage"), Gravity.TOP);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, APIActions.ApiActions.testAPI);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void TabOneClick() {
        if (!qaFragment.isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.tab_fragments, qaFragment, "1");
            transaction.commitAllowingStateLoss();
        }
    }

    public void TabWallClick() {
        try {
            if (profiledataModel.getIs_following_count() > 0) {
                if (wallFeedsMainFragment != null && !wallFeedsMainFragment.isVisible()) {
//        if (!qaFragment.isVisible()) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.tab_fragments, wallFeedsMainFragment, "1");
                    transaction.commitAllowingStateLoss();
//        }
                }
            } else {
                if (user.getUser_id() == USER_ID) {
                    if (wallFeedsMainFragment != null && !wallFeedsMainFragment.isVisible()) {
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.tab_fragments, wallFeedsMainFragment, "1");
                        transaction.commit();
                    }
                } else {
                    if (wallFeedsMainFragment != null && !wallFeedsMainFragment.isVisible()) {
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.tab_fragments, wallFeedsMainFragment, "1");
                        transaction.commit();
                    }
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();

        }
    }

    public void TabTwoClick() {
        if (!groupFragment.isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.tab_fragments, groupFragment, "1");
            transaction.commitAllowingStateLoss();
        }

    }

    public void TabThreeClick() {
        if (!strainFragment.isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.tab_fragments, strainFragment, "1");
            transaction.commitAllowingStateLoss();
        }
    }

    public void TabFourClick() {
        if (!journalFragment.isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.tab_fragments, journalFragment, "1");
            transaction.commitAllowingStateLoss();
        }

    }

    public void TabFiveClick() {
        if (!budzMapFragment.isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.tab_fragments, budzMapFragment, "1");
            transaction.commitAllowingStateLoss();
        }
    }

    public void TabSixClick() {
        if (!reviewFragment.isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.tab_fragments, reviewFragment, "1");
            transaction.commitAllowingStateLoss();
        }
    }

    public void InitTabs() {
        final ImageView Tab_One, Tab_Two, Tab_Three, Tab_Four, Tab_Five, Tab_Six, Tab_Wall;
        Tab_One = findViewById(R.id.tab_one);
        Tab_Wall = findViewById(R.id.tab_wall);
        Tab_Two = findViewById(R.id.tab_two);
        Tab_Three = findViewById(R.id.tab_three);
        Tab_Four = findViewById(R.id.tab_four);
        Tab_Five = findViewById(R.id.tab_five);
        Tab_Six = findViewById(R.id.tab_six);
//budz_feeds
        //#6BB735
        Tab_Wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDialogDisappear();
                Tab_One.setImageResource(R.drawable.ic_tab_qa);
                Tab_One.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Wall.setImageResource(R.drawable.budz_feeds_white);
                Tab_Wall.setBackgroundColor(Color.parseColor("#6BB735"));
                Tab_Two.setImageResource(R.drawable.ic_tab_group);
                Tab_Two.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Three.setImageResource(R.drawable.ic_tab_strain);
                Tab_Three.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Four.setImageResource(R.drawable.ic_tab_journal);
                Tab_Four.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Five.setImageResource(R.drawable.budzmapnewic);
                Tab_Five.setColorFilter(0);
                Tab_Five.setPadding(Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this));
                Tab_Five.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Six.setImageResource(R.drawable.ic_strain_rating_one_white);
                Tab_Six.setBackgroundColor(Color.parseColor("#00000000"));
                TabWallClick();
            }
        });
        Tab_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                Tab_One.setImageResource(R.drawable.ic_qa_icon_white);
                Tab_One.setBackgroundColor(Color.parseColor("#0081c9"));
                Tab_Wall.setImageResource(R.drawable.budz_feeds);
                Tab_Wall.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Two.setImageResource(R.drawable.ic_tab_group);
                Tab_Two.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Three.setImageResource(R.drawable.ic_tab_strain);
                Tab_Three.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Four.setImageResource(R.drawable.ic_tab_journal);
                Tab_Four.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Five.setImageResource(R.drawable.budzmapnewic);
                Tab_Five.setColorFilter(0);
                Tab_Five.setPadding(Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this));
                Tab_Five.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Six.setImageResource(R.drawable.ic_strain_rating_one_white);
                Tab_Six.setBackgroundColor(Color.parseColor("#00000000"));

                TabOneClick();

            }
        });

        Tab_Two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                Tab_Two.setImageResource(R.drawable.ic_group_icon_white);
                Tab_Two.setBackgroundColor(Color.parseColor("#f79125"));
                Tab_Wall.setImageResource(R.drawable.budz_feeds);
                Tab_Wall.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_One.setImageResource(R.drawable.ic_tab_qa);
                Tab_One.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Three.setImageResource(R.drawable.ic_tab_strain);
                Tab_Three.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Four.setImageResource(R.drawable.ic_tab_journal);
                Tab_Four.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Five.setImageResource(R.drawable.budzmapnewic);
                Tab_Five.setColorFilter(0);
                Tab_Five.setPadding(Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this));
                Tab_Five.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Six.setImageResource(R.drawable.ic_strain_rating_one_white);
                Tab_Six.setBackgroundColor(Color.parseColor("#00000000"));

                TabTwoClick();
            }
        });

        Tab_Three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                Tab_Three.setImageResource(R.drawable.ic_strain_icon_white);
                Tab_Three.setBackgroundColor(Color.parseColor("#f4c42f"));
                Tab_Wall.setImageResource(R.drawable.budz_feeds);
                Tab_Wall.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Two.setImageResource(R.drawable.ic_tab_group);
                Tab_Two.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_One.setImageResource(R.drawable.ic_tab_qa);
                Tab_One.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Four.setImageResource(R.drawable.ic_tab_journal);
                Tab_Four.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Five.setImageResource(R.drawable.budzmapnewic);
                Tab_Five.setColorFilter(0);
                Tab_Five.setPadding(Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this));
                Tab_Five.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Six.setImageResource(R.drawable.ic_strain_rating_one_white);
                Tab_Six.setBackgroundColor(Color.parseColor("#00000000"));
                TabThreeClick();
            }
        });

        Tab_Four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                Tab_Four.setImageResource(R.drawable.ic_journal_white_icon);
                Tab_Four.setBackgroundColor(Color.parseColor("#7cc044"));
                Tab_Wall.setImageResource(R.drawable.budz_feeds);
                Tab_Wall.setBackgroundColor(Color.parseColor("#00000000"));

                Tab_Two.setImageResource(R.drawable.ic_tab_group);
                Tab_Two.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_One.setImageResource(R.drawable.ic_tab_qa);
                Tab_One.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Three.setImageResource(R.drawable.ic_tab_strain);
                Tab_Three.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Five.setImageResource(R.drawable.budzmapnewic);
                Tab_Five.setColorFilter(0);
                Tab_Five.setPadding(Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this));
                Tab_Five.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Six.setImageResource(R.drawable.ic_strain_rating_one_white);
                Tab_Six.setBackgroundColor(Color.parseColor("#00000000"));

                TabFourClick();
            }
        });


        Tab_Five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                Tab_Five.setImageResource(R.drawable.budzmapnewic);
                Tab_Five.setColorFilter(ContextCompat.getColor(Tab_Five.getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                Tab_Five.setPadding(Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this));
                Tab_Five.setBackgroundColor(Color.parseColor("#932b88"));
                Tab_Wall.setImageResource(R.drawable.budz_feeds);
                Tab_Wall.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Two.setImageResource(R.drawable.ic_tab_group);
                Tab_Two.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_One.setImageResource(R.drawable.ic_tab_qa);
                Tab_One.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Three.setImageResource(R.drawable.ic_tab_strain);
                Tab_Three.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Four.setImageResource(R.drawable.ic_tab_journal);
                Tab_Four.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Six.setImageResource(R.drawable.ic_strain_rating_one_white);
                Tab_Six.setBackgroundColor(Color.parseColor("#00000000"));

                TabFiveClick();
            }
        });


        Tab_Six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                Tab_Six.setImageResource(R.drawable.ic_review_icon);
                Tab_Six.setBackgroundColor(Color.parseColor("#7cc044"));
                Tab_Two.setImageResource(R.drawable.ic_tab_group);
                Tab_Two.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_One.setImageResource(R.drawable.ic_tab_qa);
                Tab_One.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Three.setImageResource(R.drawable.ic_tab_strain);
                Tab_Three.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Four.setImageResource(R.drawable.ic_tab_journal);
                Tab_Wall.setImageResource(R.drawable.budz_feeds);
                Tab_Wall.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Four.setBackgroundColor(Color.parseColor("#00000000"));
                Tab_Five.setImageResource(R.drawable.budzmapnewic);
                Tab_Five.setColorFilter(0);
                Tab_Five.setPadding(Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this)
                        , Utility.convertDpToPixel(10, UserProfileActivity.this));
                Tab_Five.setBackgroundColor(Color.parseColor("#00000000"));
                TabSixClick();
            }
        });
    }

    @Override
    public void onBackPressed() {
        InfoDialogDisappear();
        if (SharedPrefrences.getBool("isFromSignup", UserProfileActivity.this)) {
            setBool("isFromSignup", false, UserProfileActivity.this);
            GoToHome(UserProfileActivity.this, true);
            finish();
        } else {
            finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("Response", response);
        if (apiActions == APIActions.ApiActions.get_user_profile) {

            JSONObject json_object;
            try {
                json_object = new JSONObject(response).getJSONObject("successData").getJSONArray("user_data").getJSONObject(0);
                profiledataModel.setFirst_name(json_object.getString("first_name"));
                profiledataModel.setLast_name(json_object.getString("last_name"));
                profiledataModel.setEmail(json_object.getString("email"));
                profiledataModel.setSpecial_icon(json_object.getString("special_icon"));
                profiledataModel.setZip_code(json_object.getString("zip_code"));
                profiledataModel.setImage_path(json_object.getString("image_path"));
                profiledataModel.setUser_type(json_object.getString("user_type"));
                profiledataModel.setAvatar(json_object.getString("avatar"));
                profiledataModel.setCover(json_object.getString("cover"));
                profiledataModel.setBio(json_object.getString("bio"));
                profiledataModel.setLocation(json_object.getString("location"));
                profiledataModel.setCreated_at(json_object.getString("created_at"));
                profiledataModel.setUpdated_at(json_object.getString("updated_at"));
                profiledataModel.setFollowers_count(json_object.getInt("followers_count"));
                profiledataModel.setFollowings_count(json_object.getInt("followings_count"));
                profiledataModel.setIs_following_count(json_object.getInt("is_following_count"));
                profiledataModel.setUser_rating(json_object.getInt("points"));
                String medical_experties = "", Strain_Experties = "";
                String medical_experties_ids = "", Strain_Experties_ids = "";
                int medical_experties_count = 0, Strain_Experties_count = 0;
                JSONArray expeties = json_object.getJSONArray("get_expertise");
                List<AddExpertiseQuestionActivity.ExpertiesDataModel> expertiesDataModels = Arrays.asList(new Gson().fromJson(expeties.toString(), AddExpertiseQuestionActivity.ExpertiesDataModel[].class));
                for (int x = 0; x < expertiesDataModels.size(); x++) {
                    AddExpertiseQuestionActivity.ExpertiesDataModel object = expertiesDataModels.get(x);
                    if (object.getMedical_id() != null) {

                        if (medical_experties_ids.equalsIgnoreCase("")) {
                            medical_experties_ids = medical_experties_ids + object.getMedical_id();
                        } else {
                            medical_experties_ids = medical_experties_ids + "," + object.getMedical_id();
                        }
                        if (medical_experties.length() == 0) {
                            medical_experties_count = 1;
                            medical_experties = medical_experties + object.getMedical().getTitle();

                        } else {
                            medical_experties_count = medical_experties_count + 1;
                            medical_experties = medical_experties + "\n" + object.getMedical().getTitle();
                        }


                    } else {
                        if (Strain_Experties_ids.equalsIgnoreCase("")) {
                            Strain_Experties_ids = Strain_Experties_ids + object.getStrain_id();
                        } else {
                            Strain_Experties_ids = Strain_Experties_ids + "," + object.getStrain_id();
                        }
                        if (Strain_Experties.length() == 0) {
                            Strain_Experties_count = 1;
                            Strain_Experties = Strain_Experties + object.getStrain().getTitle();
                        } else {
                            Strain_Experties_count = Strain_Experties_count + 1;
                            Strain_Experties = Strain_Experties + "\n" + object.getStrain().getTitle();
                        }
                    }
                }
                profiledataModel.setExpertiesDataModels(expertiesDataModels);
                profiledataModel.setMedical_expertiex(medical_experties);
                profiledataModel.setMedical_expertiex_count(medical_experties_count);
                profiledataModel.setMedical_expertiex_ids(medical_experties_ids);
                profiledataModel.setStrain_Experties(Strain_Experties);
                profiledataModel.setStrain_Experties_count(Strain_Experties_count);
                profiledataModel.setStrain_Experties_ids(Strain_Experties_ids);
                ArrayList<BudzMapHomeDataModel> my_lisitn_data = new ArrayList<>();
                JSONArray my_listing_array = new JSONObject(response).getJSONObject("successData").getJSONArray("subuser");
                for (int y = 0; y < my_listing_array.length(); y++) {
                    JSONObject object = my_listing_array.getJSONObject(y);
                    BudzMapHomeDataModel dataModel = new BudzMapHomeDataModel();
                    if (object.isNull("business_type_id") || object.isNull("lat") || object.isNull("lng")) {

                    } else {
                        ///// My Listing
                        dataModel.setId(object.getInt("id"));
                        dataModel.setUser_id(object.getInt("user_id"));
                        dataModel.setBusiness_type_id(object.getInt("business_type_id"));
                        dataModel.setTitle(object.getString("title"));
                        dataModel.setLogo(object.getString("logo"));
                        dataModel.setIs_organic(object.getInt("is_organic"));
                        dataModel.setIs_delivery(object.getInt("is_delivery"));
                        dataModel.setDescription(object.getString("description"));
                        dataModel.setLocation(object.getString("location"));
                        dataModel.setOthers_image(object.optString("others_image"));
                        if (!object.isNull("lat") && !object.isNull("lng")) {
                            dataModel.setLat(object.getDouble("lat"));
                            dataModel.setLng(object.getDouble("lng"));
                        } else {
                            dataModel.setLat(0.0);
                            dataModel.setLng(0.0);
                        }

                        if (object.getString("stripe_id").length() > 1 && !object.getString("stripe_id").equalsIgnoreCase("null")) {
                            dataModel.setIs_featured(1);
                        } else {
                            dataModel.setIs_featured(0);
                        }
                        ArrayList<BudzMapHomeDataModel.Reviews> reviews = new ArrayList<>();
                        JSONArray reviews_Array = object.getJSONArray("review");
                        for (int xxy = 0; xxy < reviews_Array.length(); xxy++) {
                            BudzMapHomeDataModel.Reviews reviews_model = new BudzMapHomeDataModel.Reviews();
                            JSONObject review_object = reviews_Array.getJSONObject(xxy);
                            reviews_model.setId(review_object.getInt("id"));
                            reviews_model.setSub_user_id(review_object.getInt("sub_user_id"));
                            reviews_model.setReviewed_by(review_object.getInt("reviewed_by"));
                            reviews_model.setText(review_object.getString("text"));
                            reviews_model.setCreated_at(review_object.getString("created_at"));
                            reviews.add(reviews_model);
                        }
                        dataModel.setReviews(reviews);
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
                        if (!object.isNull("distance")) {
                            dataModel.setDistance(object.optDouble("distance"));
                        }
                        if (!object.isNull("get_user_save_count")) {
                            dataModel.setGet_user_save_count(object.getInt("get_user_save_count"));
                        }

                        if (!object.isNull("rating_sum")) {
                            dataModel.setRating_sum(Double.valueOf(D_FORMAT_ONE.format(object.optJSONObject("rating_sum").getDouble("total"))));
                        }
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
                        ////
                        dataModel.setImages(new ArrayList<BudzMapHomeDataModel.Images>());
                        my_lisitn_data.add(dataModel);
                    }
                }
                profiledataModel.setMy_listing_data_models(my_lisitn_data);
                //main array
                List<UserProfileQATabDataModel> userProfileQATabDataModels = new ArrayList<>();
                //quistions array
                List<UserProfileQATabExpandAbleData> questions = new ArrayList<>();
                JSONArray questions_array = json_object.getJSONArray("get_question");
                for (int q = 0; q < questions_array.length(); q++) {
                    JSONObject object = questions_array.getJSONObject(q);
                    UserProfileQATabExpandAbleData userProfileQATabExpandAbleData = new UserProfileQATabExpandAbleData();
                    userProfileQATabExpandAbleData.setHeading("Q. " + object.getString("question"));
                    JSONArray answer_count_array = object.getJSONArray("answers_sum");
                    if (answer_count_array.length() > 0) {
                        userProfileQATabExpandAbleData.setDiscription(answer_count_array.getJSONObject(0).getString("total") + " ANSWERS");
                    } else {
                        userProfileQATabExpandAbleData.setDiscription("0 ANSWERS");
                    }
                    userProfileQATabExpandAbleData.setId(object.getInt("id"));
                    userProfileQATabExpandAbleData.setUser_id(object.getInt("user_id"));
                    userProfileQATabExpandAbleData.setCreated_data(object.getString("created_at"));
                    userProfileQATabExpandAbleData.setShow_ads(object.getInt("show_ads"));
                    questions.add(userProfileQATabExpandAbleData);
                }
                if (questions.size() > 0)
                    userProfileQATabDataModels.add(new UserProfileQATabDataModel("My Q’s", questions));

// answers
                List<UserProfileQATabExpandAbleData> answers = new ArrayList<>();
                JSONArray answers_array = json_object.getJSONArray("get_answers");
                for (int q = 0; q < answers_array.length(); q++) {
                    JSONObject object = answers_array.getJSONObject(q);

                    UserProfileQATabExpandAbleData userProfileQATabExpandAbleData = new UserProfileQATabExpandAbleData();
                    userProfileQATabExpandAbleData.setHeading("A. " + object.getString("answer"));
                    userProfileQATabExpandAbleData.setId(object.getInt("id"));
                    JSONObject question_Details = object.getJSONObject("get_question");
                    if (question_Details != null) {
                        userProfileQATabExpandAbleData.setDiscription("Q. " + question_Details.getString("question"));
                        userProfileQATabExpandAbleData.setId(question_Details.getInt("id"));
                    }
                    userProfileQATabExpandAbleData.setUser_id(object.getInt("user_id"));
                    userProfileQATabExpandAbleData.setCreated_data(object.getString("created_at"));
                    answers.add(userProfileQATabExpandAbleData);
                }
                if (answers.size() > 0)
                    userProfileQATabDataModels.add(new UserProfileQATabDataModel("My A’s", answers));
                profiledataModel.setQuestion_answers(userProfileQATabDataModels);
                if (USER_ID == user.getUser_id()) {

                    try {
                        profiledataModelLoggedUser = (UserProfileDataModel) profiledataModel.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
                if (!UserProfileActivity.this.isDestroyed()) {
                    qaFragment = new QAFragment(profiledataModel.getQuestion_answers());
//                    FragmentManager manager = getSupportFragmentManager();
//                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.add(R.id.tab_fragments, qaFragment, "1");
//                    transaction.commitAllowingStateLoss();
                    TabWallClick();
                    SetProfileData(profiledataModel);
                }
                MakeRealView(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == follow_unfollow_user) {
            TabWallClick();
            Log.d("response", response);
        } else if (apiActions == APIActions.ApiActions.logout) {
            Log.d("response", response);
        }

    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("Response", response);
        try {
            JSONObject object = new JSONObject(response);
            if (object.optString("errorMessage").equalsIgnoreCase("User is blocked")) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Blocked")
                        .setContentText(object.optString("errorMessage"))
                        .setConfirmText("Okay!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        })
                        .show();
            } else {
                CustomeToast.ShowCustomToast(getContext(), object.optString("errorMessage"), Gravity.TOP);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void SetProfileData(UserProfileDataModel model) {
        if (profiledataModel.getIs_following_count() == 1) {
            Follow_btn_icon.setImageResource(R.drawable.ic_cross_image);
            Follow_btn_text.setText("Unfollow");
        } else {
            Follow_btn_icon.setImageResource(R.drawable.ic_profile_add);
            Follow_btn_text.setText("Follow");
        }

        SetPhot(model.getCover(), Cover_Photo, R.drawable.home_bg, true);

        if (model.getImage_path() != null && !model.getImage_path().equalsIgnoreCase("null") && model.getImage_path().length() > 6) {
            SetPhot(model.getImage_path(), Profile_Image, R.drawable.ic_user_profile, false);
        } else {
            SetPhot(model.getAvatar(), Profile_Image, R.drawable.ic_user_profile, false);
        }
        if (model.getSpecial_icon().length() > 5) {
            //profile_img_topi
            profile_img_topi.setVisibility(View.VISIBLE);
            SetPhot(model.getSpecial_icon(), profile_img_topi, R.drawable.ic_user_profile, false);

        } else {
            profile_img_topi.setVisibility(View.GONE);

        }


        String Name = "";
        if (model.getLast_name() != null && !model.getLast_name().equalsIgnoreCase("null")) {
            Name = model.getFirst_name() + " " + model.getLast_name();
        } else {
            Name = model.getFirst_name();
        }
        UserName.setText(Name);
        Follower_count.setText(model.getFollowers_count() + "");
        Following_count.setText(model.getFollowings_count() + "");
        List<AddExpertiseQuestionActivity.DataModelEntryExpert> mData2 = new ArrayList<>();
        List<AddExpertiseQuestionActivity.DataModelEntryExpert> mData1 = new ArrayList<>();
        for (int i = 0; i < profiledataModel.getExpertiesDataModels().size(); i++) {
            if (profiledataModel.getExpertiesDataModels().get(i).getMedical_id() != null) {
                if (profiledataModel.getExpertiesDataModels().get(i).isApproved == 0 && profiledataModel.getExpertiesDataModels().get(i).getMedical().isApproved.equalsIgnoreCase("0"))
                    mData1.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(profiledataModel.getExpertiesDataModels().get(i).getMedical().getTitle(), true));
                else {
                    mData1.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(profiledataModel.getExpertiesDataModels().get(i).getMedical().getTitle(), false));
                }
            } else {
                if (profiledataModel.getExpertiesDataModels().get(i).isApproved == 0 && profiledataModel.getExpertiesDataModels().get(i).getStrain().isApproved.equalsIgnoreCase("0"))
                    mData2.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(profiledataModel.getExpertiesDataModels().get(i).getStrain().getTitle(), true));
                else {
                    mData2.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(profiledataModel.getExpertiesDataModels().get(i).getStrain().getTitle(), false));
                }
            }
        }
        question_two_adapter = new ExpertAreaRecylerAdapter(this, mData2, 2, true);
        Strain_experties_rc.setAdapter(question_two_adapter);
        question_one_adapter = new ExpertAreaRecylerAdapter(this, mData1, 1, true);
        Medical_experties_rc.setAdapter(question_one_adapter);
        if (model.getBio() != null && !model.getBio().equalsIgnoreCase("null")) {
            User_Bio.setText(model.getBio() + "");
        } else {
            User_Bio.setText("No biography available.");
        }
        if (model.getMedical_expertiex().length() > 0) {
            Medical_experties.setText(model.getMedical_expertiex());
            Medical_experties_rc.setVisibility(View.VISIBLE);
            Medical_experties.setVisibility(View.GONE);
        } else {
            Medical_experties_rc.setVisibility(View.GONE);
            Medical_experties.setVisibility(View.VISIBLE);
            Medical_experties.setText("None listed.");
        }
        if (model.getStrain_Experties().length() > 0) {
            Strain_experties.setText(model.getStrain_Experties());
            Strain_experties_rc.setVisibility(View.VISIBLE);
            Strain_experties.setVisibility(View.GONE);
        } else {
            Strain_experties_rc.setVisibility(View.GONE);
            Strain_experties.setVisibility(View.VISIBLE);
            Strain_experties.setText("None listed.");
        }


        setRatingEffect(model.getUser_rating());

        if (model.getMy_listing_data_models().size() > 2) {
            dataModels.add(model.getMy_listing_data_models().get(0));
            dataModels.add(model.getMy_listing_data_models().get(1));
        } else {
            dataModels = model.getMy_listing_data_models();
        }
        if (dataModels.size() > 0) {
            my_listing_no_record.setVisibility(View.GONE);
//            my_listin_recyler_adapter = new BudzMapRecylerAdapterForProfile(UserProfileActivity.this, dataModels);
            if (dataModels.size() > 2) {
                for (int i = 2; i < dataModels.size(); i++) {
                    dataModels.remove(i);
                }
                my_listin_recyler_adapter = new BudzMapHomeRecylAdapter(UserProfileActivity.this, dataModels);
            } else {
                my_listin_recyler_adapter = new BudzMapHomeRecylAdapter(UserProfileActivity.this, dataModels);
            }
            my_listing_recyler_vewi.setAdapter(my_listin_recyler_adapter);
            my_listin_recyler_adapter.setClickListener(this);
            my_listin_recyler_adapter.notifyDataSetChanged();
        } else {
            my_listing_no_record.setVisibility(View.VISIBLE);
        }
    }

    public void setRatingEffect(int rating) {
        User_Rating.setText(rating + "");
        if (rating >= 0 && rating < 100) {
            UserName.setTextColor(Color.parseColor("#dedede"));
            User_Rating_title.setTextColor(Color.parseColor("#dedede"));
            User_Rating.setTextColor(Color.parseColor("#dedede"));
            Rating_dialog_title.setTextColor(Color.parseColor("#dedede"));
            User_Rating_seprator.setBackgroundColor(Color.parseColor("#dedede"));
            User_Rating_image.setImageResource(R.drawable.ic_user_profile_rating_99);

            User_Rating_title.setText("0-99");
            Rating_dialog_title.setText("0-99");
        } else if (rating >= 100 && rating < 200) {
            UserName.setTextColor(Color.parseColor("#73ae44"));
            User_Rating_title.setTextColor(Color.parseColor("#73ae44"));
            User_Rating.setTextColor(Color.parseColor("#73ae44"));
            Rating_dialog_title.setTextColor(Color.parseColor("#73ae44"));
            User_Rating_seprator.setBackgroundColor(Color.parseColor("#73ae44"));
            User_Rating_image.setImageResource(R.drawable.ic_user_profile_rating_199);

            User_Rating_title.setText("Seedling");
            Rating_dialog_title.setText("Seedling");
        } else if (rating >= 200 && rating < 300) {
            UserName.setTextColor(Color.parseColor("#f3c330"));
            User_Rating_title.setTextColor(Color.parseColor("#f3c330"));
            User_Rating.setTextColor(Color.parseColor("#f3c330"));
            Rating_dialog_title.setTextColor(Color.parseColor("#f3c330"));
            User_Rating_seprator.setBackgroundColor(Color.parseColor("#f3c330"));
            User_Rating_image.setImageResource(R.drawable.ic_user_profile_rating_299);
            User_Rating_title.setText("Young Bud");
            Rating_dialog_title.setText("Young Bud");
        } else if (rating >= 300 && rating < 400) {
            UserName.setTextColor(Color.parseColor("#df910b"));
            User_Rating_title.setTextColor(Color.parseColor("#df910b"));
            User_Rating.setTextColor(Color.parseColor("#df910b"));
            Rating_dialog_title.setTextColor(Color.parseColor("#df910b"));
            User_Rating_seprator.setBackgroundColor(Color.parseColor("#df910b"));
            User_Rating_image.setImageResource(R.drawable.ic_user_profile_rating_399);


            User_Rating_title.setText("Blooming Bud");
            Rating_dialog_title.setText("Blooming Bud");
        } else if (rating >= 400) {
            UserName.setTextColor(Color.parseColor("#cb6acc"));
            User_Rating_title.setTextColor(Color.parseColor("#cb6acc"));
            User_Rating.setTextColor(Color.parseColor("#cb6acc"));
            Rating_dialog_title.setTextColor(Color.parseColor("#cb6acc"));
            User_Rating_seprator.setBackgroundColor(Color.parseColor("#cb6acc"));
            User_Rating_image.setImageResource(R.drawable.ic_user_profile_rating_400_plus);

            User_Rating_title.setText("Best Bud");
            Rating_dialog_title.setText("Best Bud");
        }
    }

    public void SetPhot(String path, final ImageView imageView, int Placeholder, final boolean isBackground) {
        String imag_url = images_baseurl;
        if (path.contains("http")) {
            imag_url = path;
        } else {
            imag_url = imag_url + path;
        }
        if (isBackground) {
            Glide.with(this)
                    .load(imag_url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            Log.d("ready", e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("ready", model);
//                            imageView.setImageDrawable(null);
                            imageView.setImageDrawable(resource);
                            return false;
                        }
                    }).into(imageView);
        } else {
            Glide.with(this)
                    .load(imag_url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_user_profile)
                    .error(R.drawable.noimage)
                    .centerCrop()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            Log.d("ready", e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("ready", model);
                            imageView.setImageDrawable(resource);
                            return false;
                        }
                    }).into(imageView);
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        budz_map_item_clickerd_dataModel = dataModels.get(position);
        budz_map_item_clickerd_dataModel_abc = dataModels.get(position);
        Intent i = new Intent(UserProfileActivity.this, BudzMapDetailsActivity.class);
        i.putExtra("budzmap_id", dataModels.get(position).getId());
        startActivity(i);
//        i.putExtra("budz_type", dataModels.get(position).getBusiness_type_id() + "");
//        startActivity(i);
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

    @Override
    public void onFollowFollwoingCrossBtnClink(UserFollowFollwingAlertDialog dialog) {
        dialog.dismiss();
        Follower_count.setText(profiledataModel.getFollowers_count() + "");
        Following_count.setText(profiledataModel.getFollowings_count() + "");

    }

    @Override
    public void onUserFollow(int userID) {
        //todo: unfollow user change layout
        Follow_btn_icon.setImageResource(R.drawable.ic_cross_image);
        Follow_btn_text.setText("Unfollow");
        profiledataModel.setIs_following_count(1);
        profiledataModel.setFollowers_count(profiledataModel.getFollowers_count() + 1);
        Follower_count.setText(profiledataModel.getFollowers_count() + "");
        TabWallClick();
    }

    @Override
    public void onUserUnfollow(int userID) {
        Follow_btn_icon.setImageResource(R.drawable.ic_profile_add);
        Follow_btn_text.setText("Follow");
        profiledataModel.setIs_following_count(0);
        profiledataModel.setFollowers_count(profiledataModel.getFollowers_count() - 1);
        Follower_count.setText(profiledataModel.getFollowers_count() + "");
        TabWallClick();
        //todo: follow user change layout
    }

    public void onBackDataCalled() {
        try {
            profiledataModel = (UserProfileDataModel) profiledataModelLoggedUser.clone();
            USER_ID = user.getUser_id();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        SetProfileData(profiledataModel);
    }

    @Override
    public void onSaveButtonClick(EditUserProfileUploadPhotoAlertDialog dialog, Drawable drawable) {
        dialog.dismiss();
        Profile_Image.setImageDrawable(drawable);
    }

    @Override
    public void onSaveButtonClick(EditUserProfileUploadPhotoAlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onCrossButtonClick(EditUserProfileUploadPhotoAlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onTopiChanged(Drawable drawable) {
        profile_img_topi.setVisibility(View.VISIBLE);
        profile_img_topi.setImageDrawable(drawable);
    }

    @Override
    public void onUploadButtonClick(EditUserProfileUploadPhotoAlertDialog dialog) {
        dialog.dismiss();
        InfoDialogDisappear();
        Intent intent = new Intent(this, HBCameraActivity.class);
        intent.putExtra("isVideoCaptureAble", false);
        startActivityForResult(intent, 1200);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            Log.d("paths", data.getExtras().getString("file_path_arg"));
            Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
            bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
//            bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
            int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
            Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            Profile_Image.setImageDrawable(drawable);
            Drawable main_drawable = new BitmapDrawable(getResources(), bitmapOrg);
            Profile_Img.setImageDrawable(main_drawable);
            UploadImage(main_drawable);
        } else if (resultCode == RESULT_OK && requestCode == 1202) {
//            Log.d("paths", data.getExtras().getString("file_path_arg"));
//            Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
//            if (bitmapOrg != null) {
//                bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
////                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 1400, 1400, false);
//                Drawable drawable = new BitmapDrawable(getResources(), bitmapOrg);
//                Cover_Photo.setImageDrawable(drawable);
//                Cover_Photo.setBackground(null);
//                Cover_Photo.setImageDrawable(new BitmapDrawable(getResources(), bitmapOrg));
//                Cover_Photo.setDrawingCacheEnabled(true);
//                SetPhot(data.getExtras().getString("file_path_arg"), Cover_Photo, R.drawable.home_bg, true);
////                Bitmap bitmap = Bitmap.createBitmap(drawableToBitmap(drawable));
////                Bitmap bitmap = mAttacher.getVisibleRectangleBitmap();
//                UploadImageCover(new BitmapDrawable(getResources(), bitmapOrg));
//            }

        }
    }

    public void UploadImage(Drawable drawable) {
        new UploadImageAPIcall(this, update_image, drawable, user.getSession_key(), new APIResponseListner() {
            @Override
            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                profiledataModel.setImage_path(response);
            }

            @Override
            public void onRequestError(String response, APIActions.ApiActions apiActions) {
                Log.d("response", response);
            }
        }, APIActions.ApiActions.add_media);
    }


    public static class UserProfileDataModel implements Cloneable, Serializable {
        List<AddExpertiseQuestionActivity.ExpertiesDataModel> expertiesDataModels;

        public List<AddExpertiseQuestionActivity.ExpertiesDataModel> getExpertiesDataModels() {
            if (expertiesDataModels == null)
                expertiesDataModels = new ArrayList<>();
            return expertiesDataModels;
        }

        public void setExpertiesDataModels(List<AddExpertiseQuestionActivity.ExpertiesDataModel> expertiesDataModels) {
            this.expertiesDataModels = expertiesDataModels;
        }

        String image_path;
        String first_name;
        String last_name;
        String email;
        String zip_code;
        String user_type;
        String avatar;
        String special_icon;
        String cover;
        String bio;
        String location;
        String created_at;
        String updated_at;
        String medical_expertiex;
        String medical_expertiex_ids;
        int medical_expertiex_count;
        String Strain_Experties;
        String Strain_Experties_ids;
        int Strain_Experties_count;
        int followers_count;
        int followings_count;
        int user_rating;

        public String getSpecial_icon() {
            if (special_icon != null)
                return special_icon;
            else {
                return "";
            }
        }

        public void setSpecial_icon(String special_icon) {
            this.special_icon = special_icon;
        }

        List<UserProfileQATabDataModel> question_answers = new ArrayList<>();

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public List<UserProfileQATabDataModel> getQuestion_answers() {
            return question_answers;
        }

        public void setQuestion_answers(List<UserProfileQATabDataModel> question_answers) {
            this.question_answers = question_answers;
        }

        public String getMedical_expertiex_ids() {
            return medical_expertiex_ids;
        }

        public void setMedical_expertiex_ids(String medical_expertiex_ids) {
            this.medical_expertiex_ids = medical_expertiex_ids;
        }

        public String getStrain_Experties_ids() {
            return Strain_Experties_ids;
        }

        public void setStrain_Experties_ids(String strain_Experties_ids) {
            Strain_Experties_ids = strain_Experties_ids;
        }

        ArrayList<BudzMapHomeDataModel> my_listing_data_models;


        public int getUser_rating() {
            return user_rating;
        }

        public ArrayList<BudzMapHomeDataModel> getMy_listing_data_models() {
            return my_listing_data_models;
        }

        public int getMedical_expertiex_count() {
            return medical_expertiex_count;
        }

        public void setMedical_expertiex_count(int medical_expertiex_count) {
            this.medical_expertiex_count = medical_expertiex_count;
        }

        public int getStrain_Experties_count() {
            return Strain_Experties_count;
        }

        public void setStrain_Experties_count(int strain_Experties_count) {
            Strain_Experties_count = strain_Experties_count;
        }

        public void setMy_listing_data_models(ArrayList<BudzMapHomeDataModel> my_listing_data_models) {
            this.my_listing_data_models = my_listing_data_models;
        }

        public void setUser_rating(int user_rating) {
            this.user_rating = user_rating;
        }

        public String getMedical_expertiex() {
            return medical_expertiex;
        }

        public void setMedical_expertiex(String medical_expertiex) {
            this.medical_expertiex = medical_expertiex;
        }

        public String getStrain_Experties() {
            return Strain_Experties;
        }

        public void setStrain_Experties(String strain_Experties) {
            Strain_Experties = strain_Experties;
        }

        int is_following_count;

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getZip_code() {
            return zip_code;
        }

        public void setZip_code(String zip_code) {
            this.zip_code = zip_code;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getFollowers_count() {
            return followers_count;
        }

        public void setFollowers_count(int followers_count) {
            this.followers_count = followers_count;
        }

        public int getFollowings_count() {
            return followings_count;
        }

        public void setFollowings_count(int followings_count) {
            this.followings_count = followings_count;
        }

        public int getIs_following_count() {
            return is_following_count;
        }

        public void setIs_following_count(int is_following_count) {
            this.is_following_count = is_following_count;
        }
    }


    public void MakeRealView(boolean isAnimate) {
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking);
        LinearLayout Hide_Layout_One = findViewById(R.id.hide_layout_one);
        LinearLayout buder_infoo = findViewById(R.id.buder_infoo);
        LinearLayout following_follwoer_layout = findViewById(R.id.following_follwoer_layout);
        LinearLayout experties_detail = findViewById(R.id.experties_detail);
        LinearLayout tabs_bottom = findViewById(R.id.tabs_bottom);
        RelativeLayout prfl_img = findViewById(R.id.prfl_img);
        RelativeLayout menu = findViewById(R.id.menu);
        LinearLayout about_bud = findViewById(R.id.about_bud);
        LinearLayout main_layout_bot = findViewById(R.id.main_layout_bot);
        RelativeLayout my_listingg = findViewById(R.id.my_listingg);
        LinearLayout my_activity = findViewById(R.id.my_activity);
        LinearLayout expert_area_header = findViewById(R.id.expert_area_header);
        TextView About_bud_Text = findViewById(R.id.about_bud_text);
        ImageView blur_image_view = findViewById(R.id.blur_image_view);

        if (isAnimate) {
            about_bud.startAnimation(startAnimation);
            expert_area_header.startAnimation(startAnimation);
            my_listingg.startAnimation(startAnimation);
            my_activity.startAnimation(startAnimation);

        } else {
            if (USER_ID == user.getUser_id()) {
                block_user.setVisibility(View.GONE);
            } else {
                block_user.setVisibility(View.VISIBLE);
            }
            blur_image_view.setVisibility(View.VISIBLE);
            about_bud.clearAnimation();
            my_listingg.clearAnimation();
            expert_area_header.clearAnimation();
            experties_detail.clearAnimation();
            main_layout_bot.clearAnimation();
            my_activity.clearAnimation();

            Hide_Layout_One.setVisibility(View.VISIBLE);
            buder_infoo.setVisibility(View.VISIBLE);
            following_follwoer_layout.setVisibility(View.VISIBLE);

            experties_detail.setVisibility(View.VISIBLE);

            tabs_bottom.setVisibility(View.VISIBLE);

            prfl_img.setVisibility(View.VISIBLE);


            about_bud.setBackgroundColor(Color.parseColor("#4d4d4d"));

            main_layout_bot.setBackgroundColor(Color.parseColor("#232323"));


            my_listingg.setBackgroundColor(Color.parseColor("#932a88"));


            my_activity.setBackgroundColor(Color.parseColor("#4d4d4d"));


            expert_area_header.setBackgroundColor(Color.parseColor("#4d4d4d"));

            About_bud_Text.setVisibility(View.VISIBLE);


            TextView listing_text = findViewById(R.id.listing_text);
            listing_text.setVisibility(View.VISIBLE);
            TextView listing_text_see = findViewById(R.id.listing_text_see);
//            if (user.getUser_id() != USER_ID) {
//                listing_text_see.setVisibility(View.INVISIBLE);
//            } else {
            if (profiledataModel.getMy_listing_data_models() != null && profiledataModel.getMy_listing_data_models().size() > 1) {
                listing_text_see.setVisibility(View.VISIBLE);
            }
//            }
            listing_text_see.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserProfileDataModel model = profiledataModel;

                    dataModels = model.getMy_listing_data_models();

                    if (dataModels.size() > 0) {
                        my_listing_no_record.setVisibility(View.GONE);//            my_listin_recyler_adapter = new BudzMapRecylerAdapterForProfile(UserProfileActivity.this, dataModels);

                        my_listin_recyler_adapter = new BudzMapHomeRecylAdapter(UserProfileActivity.this, dataModels);
                        my_listing_recyler_vewi.setAdapter(my_listin_recyler_adapter);
                        my_listin_recyler_adapter.setClickListener(UserProfileActivity.this);
                        my_listin_recyler_adapter.notifyDataSetChanged();
                    } else {
                        my_listing_no_record.setVisibility(View.VISIBLE);
                    }
                    //                    Intent activity_mybudzmap = new Intent(UserProfileActivity.this, MyBudzMapActivity.class);
//                    startActivity(activity_mybudzmap);
                }
            });


            TextView my_activity_text = findViewById(R.id.my_activity_text);
            my_activity_text.setVisibility(View.VISIBLE);


//            my_listing_no_record.setVisibility(View.VISIBLE);

            TextView expert_text = findViewById(R.id.expert_text);
            expert_text.setVisibility(View.VISIBLE);

            User_Bio.setVisibility(View.VISIBLE);

            scrollView.setBackgroundColor(Color.parseColor("#232323"));

        }

    }
}
