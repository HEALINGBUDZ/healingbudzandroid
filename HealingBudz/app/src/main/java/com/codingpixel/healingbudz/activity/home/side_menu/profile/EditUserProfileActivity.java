package com.codingpixel.healingbudz.activity.home.side_menu.profile;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.ExpertiesUpdate;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.Registration.AddExpertiseQuestionActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.dialog.EditUserProfileUpdatePersonalInfoAlertDialog;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.dialog.EditUserProfileUploadPhotoAlertDialog;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.dialog.UserFollowFollwingAlertDialog;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.hb_gallery.HealingBudGallery;
import com.codingpixel.healingbudz.adapter.ExpertAreaRecylerAdapter;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.activity.home.HomeActivity.Profile_Img;
import static com.codingpixel.healingbudz.activity.home.HomeActivity.Profile_Name;
import static com.codingpixel.healingbudz.activity.home.side_menu.profile.UserProfileActivity.USER_ID;
import static com.codingpixel.healingbudz.activity.home.side_menu.profile.UserProfileActivity.profiledataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.network.model.URL.get_user_profile;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.update_cover;
import static com.codingpixel.healingbudz.network.model.URL.update_image;
import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.setString;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class EditUserProfileActivity extends AppCompatActivity implements EditUserProfileUploadPhotoAlertDialog.OnDialogFragmentClickListener, EditUserProfileUpdatePersonalInfoAlertDialog.OnDialogFragmentClickListener, APIResponseListner, UserFollowFollwingAlertDialog.OnDialogFragmentClickListener {
    NestedScrollView scrollView;
    RelativeLayout InfoDialog, EditUserParentRelative;
    ImageView InfoButton;
    ImageView Cover_Photo, Profile_Image, profile_img_topi, User_Rating_image;
    ImageView Back, Home;
    LinearLayout Healing_Bud_Gallery, Follow_layout, Unfollow_layout;
    ;
    ImageView Upload_Photo;
    boolean isUpdate = false;
    boolean isInfoDialogOpen = false;
    LinearLayout Upload_Cover_Photo;
    LinearLayout Edit_Bud_info;
    LinearLayout Edit_Expert_Area;
    LinearLayout Update_Name;
    RelativeLayout Email_Edit;
    RelativeLayout Edit_Password;
    public static Drawable drawableCover;
    public static boolean isCoverSet = false;
    RelativeLayout Edit_Zipcode;
    TextView UserName, Follower_count, Following_count, User_Bio, Medical_experties, Strain_experties,
            Rating_dialog_title, User_Rating, User_Rating_title, Email, Password, Zipcode;
    View User_Rating_seprator;
    RecyclerView Medical_experties_rc, Strain_experties_rc;
    ExpertAreaRecylerAdapter question_two_adapter;
    ExpertAreaRecylerAdapter question_one_adapter;
    public static List<AddExpertiseQuestionActivity.DataModelEntryExpert> list;
    public static List<AddExpertiseQuestionActivity.DataModelEntryExpert> list2;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (!event.getNotify()) {
            question_two_adapter = new ExpertAreaRecylerAdapter(this, list, 2, true);
            Strain_experties_rc.setAdapter(question_two_adapter);
            question_one_adapter = new ExpertAreaRecylerAdapter(this, list2, 1, true);
            Medical_experties_rc.setAdapter(question_one_adapter);
//
//            if (!this.isDestroyed()) {
//                finish();
//            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExpertiesUpdate(ExpertiesUpdate event) {
        question_two_adapter = new ExpertAreaRecylerAdapter(this, event.getList(), 2, true);
        Strain_experties_rc.setAdapter(question_two_adapter);
        question_one_adapter = new ExpertAreaRecylerAdapter(this, event.getList2(), 1, true);
        Medical_experties_rc.setAdapter(question_one_adapter);
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
        setContentView(R.layout.activity_edit_user_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
//        ArrayList<BudzMapDataModel> dataModels = budz_map_test_data();
//        ArrayList<BudzMapDataModel> data = new ArrayList<>();
//        for (int x = 0; x < 2; x++) {
//            data.add(dataModels.get(x));
//        }
        Follow_layout = (LinearLayout) findViewById(R.id.follow_layout);
        Unfollow_layout = (LinearLayout) findViewById(R.id.unfollow_layout);
        Follow_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profiledataModel.getFollowers_count() > 0) {
                    UserFollowFollwingAlertDialog followerJournalAlertDialog = UserFollowFollwingAlertDialog.newInstance(EditUserProfileActivity.this, USER_ID + "", false);
                    followerJournalAlertDialog.show(getSupportFragmentManager(), "dialog");
                }

            }
        });
        Unfollow_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profiledataModel.getFollowings_count() > 0) {
                    UserFollowFollwingAlertDialog followerJournalAlertDialog = UserFollowFollwingAlertDialog.newInstance(EditUserProfileActivity.this, USER_ID + "", true);
                    followerJournalAlertDialog.show(getSupportFragmentManager(), "dialog");
                }
            }
        });
        scrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        scrollView.fullScroll(View.FOCUS_UP);
        InfoButton = (ImageView) findViewById(R.id.info_button);
        InfoDialog = (RelativeLayout) findViewById(R.id.user_profile_info_dialog);


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
        Healing_Bud_Gallery = (LinearLayout) findViewById(R.id.healing_bud_gallery);
        Healing_Bud_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                Intent intent = new Intent(EditUserProfileActivity.this, HealingBudGallery.class);
                intent.putExtra("user_id", user.getUser_id());
                intent.putExtra("name", UserName.getText().toString().trim());
                startActivity(intent);
            }
        });
        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                finish();
            }
        });

        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                GoToHome(EditUserProfileActivity.this, true);
                finish();
            }
        });


        Upload_Photo = (ImageView) findViewById(R.id.upload_photo_edit);
        Upload_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                if (profiledataModel.getSpecial_icon().length() > 6) {
                    EditUserProfileUploadPhotoAlertDialog shootOutAlertDialog = EditUserProfileUploadPhotoAlertDialog.newInstance(EditUserProfileActivity.this, false, true, Profile_Image.getDrawable(), profile_img_topi.getDrawable());
                    shootOutAlertDialog.show(getSupportFragmentManager(), "dialog");
                } else {
                    EditUserProfileUploadPhotoAlertDialog shootOutAlertDialog = EditUserProfileUploadPhotoAlertDialog.newInstance(EditUserProfileActivity.this, false, Profile_Image.getDrawable());
                    shootOutAlertDialog.show(getSupportFragmentManager(), "dialog");
                }

            }
        });


        Edit_Bud_info = (LinearLayout) findViewById(R.id.edit_bud_info);
        Edit_Bud_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                JSONObject object = new JSONObject();
                try {
                    if (!profiledataModel.getBio().equalsIgnoreCase("null") && profiledataModel.getBio() != null) {
                        object.put("bio", User_Bio.getText().toString());
                    } else {
                        object.put("bio", "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                EditUserProfileUpdatePersonalInfoAlertDialog shootOutAlertDialog = EditUserProfileUpdatePersonalInfoAlertDialog.newInstance(EditUserProfileActivity.this, "BIO", object);
                shootOutAlertDialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        Update_Name = (LinearLayout) findViewById(R.id.update_name);
        Update_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                JSONObject object = new JSONObject();
                String Name = "";
                if (profiledataModel.getLast_name() != null && !profiledataModel.getLast_name().equalsIgnoreCase("null")) {
                    Name = profiledataModel.getFirst_name() + " " + profiledataModel.getLast_name();
                } else {
                    Name = profiledataModel.getFirst_name();
                }
                try {
                    object.put("name", Name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                EditUserProfileUpdatePersonalInfoAlertDialog shootOutAlertDialog = EditUserProfileUpdatePersonalInfoAlertDialog.newInstance(EditUserProfileActivity.this, "NAME", object);
                shootOutAlertDialog.show(getSupportFragmentManager(), "dialog");
            }
        });


        Email_Edit = (RelativeLayout) findViewById(R.id.email_edit);
        Email_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                JSONObject object = new JSONObject();
                try {
                    if (!profiledataModel.getEmail().equalsIgnoreCase("null") && profiledataModel.getEmail() != null) {
                        object.put("email", Email.getText().toString());
                    } else {
                        object.put("email", "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                EditUserProfileUpdatePersonalInfoAlertDialog shootOutAlertDialog = EditUserProfileUpdatePersonalInfoAlertDialog.newInstance(EditUserProfileActivity.this, "Email", object);
                shootOutAlertDialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        Edit_Password = (RelativeLayout) findViewById(R.id.edit_password);
        Edit_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                JSONObject object = new JSONObject();
                try {
                    object.put("password", "*********");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                EditUserProfileUpdatePersonalInfoAlertDialog shootOutAlertDialog = EditUserProfileUpdatePersonalInfoAlertDialog.newInstance(EditUserProfileActivity.this, "Password", object);
                shootOutAlertDialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        Edit_Zipcode = (RelativeLayout) findViewById(R.id.edit_zipcode);
        Edit_Zipcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                JSONObject object = new JSONObject();
                try {
                    if (!profiledataModel.getZip_code().equalsIgnoreCase("null") && profiledataModel.getZip_code() != null) {
                        object.put("zipcode", Zipcode.getText().toString());
                    } else {
                        object.put("zipcode", "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                EditUserProfileUpdatePersonalInfoAlertDialog shootOutAlertDialog = EditUserProfileUpdatePersonalInfoAlertDialog.newInstance(EditUserProfileActivity.this, "Zipcode", object);
                shootOutAlertDialog.show(getSupportFragmentManager(), "dialog");
            }
        });


        Upload_Cover_Photo = (LinearLayout) findViewById(R.id.upload_cover_photo);
        Upload_Cover_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
//                Intent intent = new Intent(EditUserProfileActivity.this, HBCameraActivity.class);
//                intent.putExtra("isVideoCaptureAble", false);
//                startActivityForResult(intent, 1202);
                isUpdate = true;
                GoTo(EditUserProfileActivity.this, EditUserProfileUploadCoverPhotoActivity.class);
            }
        });

        Edit_Expert_Area = (LinearLayout) findViewById(R.id.edit_expert_aria);
        Edit_Expert_Area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
                isUpdate = true;
                Intent intent = new Intent(EditUserProfileActivity.this, AddExpertiseQuestionActivity.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("expertArray", (Serializable) profiledataModel.getExpertiesDataModels());
                intent.putExtra("medical_expertiex", profiledataModel.getMedical_expertiex());
                intent.putExtra("strain_expertiex", profiledataModel.getStrain_Experties());
                intent.putExtra("strain_expertiex_count", profiledataModel.getStrain_Experties_count());
                intent.putExtra("medical_expertiex_count", profiledataModel.getMedical_expertiex_count());
                intent.putExtra("medical_expertiex_ids", profiledataModel.getMedical_expertiex_ids());
                intent.putExtra("strain_expertiex_ids", profiledataModel.getStrain_Experties_ids());
                startActivity(intent);

            }
        });
        EditUserParentRelative = (RelativeLayout) findViewById(R.id.edit_user_parent_relative);
        EditUserParentRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialogDisappear();
            }
        });
        Init();
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
//                Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
//                InfoDialog.startAnimation(startAnimation);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCoverSet) {
            isCoverSet = false;
            Cover_Photo.setImageDrawable(drawableCover);

        }
        if (isUpdate) {
            isUpdate = false;
            SharedPrefrences.getString("lat_cur", this);
            SharedPrefrences.getString("lng_cur", this);
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(this
                    , true
                    , get_user_profile + "/" + USER_ID + "?lat=" + SharedPrefrences.getString("lat_cur", this) + "&lng=" + SharedPrefrences.getString("lng_cur", this)
                    , jsonObject, user.getSession_key(), Request.Method.GET, this, APIActions.ApiActions.get_user_profile);


        }


    }

    public void Init() {

        Cover_Photo = (ImageView) findViewById(R.id.cover_photo);
        Profile_Image = (ImageView) findViewById(R.id.profile_image);
        profile_img_topi = (ImageView) findViewById(R.id.profile_img_topi);
        Medical_experties_rc = (RecyclerView) findViewById(R.id.medial_experties_rc);
        Medical_experties_rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Strain_experties = (TextView) findViewById(R.id.setrain_experties);
        Strain_experties_rc = (RecyclerView) findViewById(R.id.setrain_experties_rc);
        Strain_experties_rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        UserName = (TextView) findViewById(R.id.user_name);
        Following_count = (TextView) findViewById(R.id.following_count);
        Follower_count = (TextView) findViewById(R.id.follower_count);
        User_Bio = (TextView) findViewById(R.id.user_bio);
        Medical_experties = (TextView) findViewById(R.id.medial_experties);
        Rating_dialog_title = (TextView) findViewById(R.id.rating_title);
        User_Rating_image = (ImageView) findViewById(R.id.user_rating_image);
        User_Rating = (TextView) findViewById(R.id.user_rating);
        User_Rating_seprator = findViewById(R.id.user_rating_seprator);
        User_Rating_title = (TextView) findViewById(R.id.user_rating_title);
        Password = (TextView) findViewById(R.id.password);
        Email = (TextView) findViewById(R.id.email);
        Zipcode = (TextView) findViewById(R.id.zip_code);
        Profile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDialogDisappear();
                if (profiledataModel.getSpecial_icon().length() > 6) {
                    EditUserProfileUploadPhotoAlertDialog shootOutAlertDialog = EditUserProfileUploadPhotoAlertDialog.newInstance(EditUserProfileActivity.this, false, true, Profile_Image.getDrawable(), profile_img_topi.getDrawable());
                    shootOutAlertDialog.show(getSupportFragmentManager(), "dialog");
                } else {
                    EditUserProfileUploadPhotoAlertDialog shootOutAlertDialog = EditUserProfileUploadPhotoAlertDialog.newInstance(EditUserProfileActivity.this, false, Profile_Image.getDrawable());
                    shootOutAlertDialog.show(getSupportFragmentManager(), "dialog");
                }
            }
        });
        SetProfileData();
    }

    public void SetProfileData() {
        SetPhot(profiledataModel.getCover(), Cover_Photo, R.drawable.home_bg, true);
        if (profiledataModel.getImage_path() != null && !profiledataModel.getImage_path().equalsIgnoreCase("null") && profiledataModel.getImage_path().length() > 6) {
            SetPhot(profiledataModel.getImage_path(), Profile_Image, R.drawable.ic_user_profile, false);
        } else {
            SetPhot(profiledataModel.getAvatar(), Profile_Image, R.drawable.ic_user_profile, false);
        }
        if (profiledataModel.getSpecial_icon().length() > 6) {
            profile_img_topi.setVisibility(View.VISIBLE);
            SetPhot(profiledataModel.getSpecial_icon(), profile_img_topi, R.drawable.topi_ic, false);
        } else {
            profile_img_topi.setVisibility(View.GONE);

//            SetPhot(profiledataModel.getAvatar(), Profile_Image, R.drawable.ic_user_profile, false);
        }
        String Name = "";
        if (profiledataModel.getLast_name() != null && !profiledataModel.getLast_name().equalsIgnoreCase("null")) {
            Name = profiledataModel.getFirst_name() + " " + profiledataModel.getLast_name();
        } else {
            Name = profiledataModel.getFirst_name();
        }
        UserName.setText(Name);
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
        Follower_count.setText(profiledataModel.getFollowers_count() + "");
        Following_count.setText(profiledataModel.getFollowings_count() + "");
        if (profiledataModel.getBio() != null && !profiledataModel.getBio().equalsIgnoreCase("null")) {
            User_Bio.setText(profiledataModel.getBio() + "");
        } else {
            User_Bio.setText("No biography available.");
        }
        if (profiledataModel.getMedical_expertiex().length() > 2) {
            Medical_experties.setText(profiledataModel.getMedical_expertiex());
            Medical_experties_rc.setVisibility(View.VISIBLE);
            Medical_experties.setVisibility(View.GONE);
        } else {
            Medical_experties_rc.setVisibility(View.GONE);
            Medical_experties.setVisibility(View.VISIBLE);
            Medical_experties.setText("None listed.");
        }
        if (profiledataModel.getStrain_Experties().length() > 2) {
            Strain_experties.setText(profiledataModel.getStrain_Experties());
            Strain_experties_rc.setVisibility(View.VISIBLE);
            Strain_experties.setVisibility(View.GONE);
        } else {
            Strain_experties_rc.setVisibility(View.GONE);
            Strain_experties.setVisibility(View.VISIBLE);
            Strain_experties.setText("None listed.");
        }
        setRatingEffect(profiledataModel.getUser_rating());
        Email.setText(profiledataModel.getEmail());
        Password.setText("*********");
        Zipcode.setText(profiledataModel.getZip_code());
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
        if (path.contains("/data/user/")) {
            imag_url = path;
        } else if (user.getImage_path().contains("http")) {
            imag_url = path;
        } else {
            imag_url = imag_url + path;
        }
        if (isBackground) {
            Glide.with(this)
                    .load(imag_url)
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
                    .placeholder(Placeholder)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.d("ready", e.getMessage());
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
        Intent intent = new Intent(EditUserProfileActivity.this, HBCameraActivity.class);
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
            Log.d("paths", data.getExtras().getString("file_path_arg"));
            Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
            if (bitmapOrg != null) {
                bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
//                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 1400, 1400, false);
                Drawable drawable = new BitmapDrawable(getResources(), bitmapOrg);
                Cover_Photo.setImageDrawable(drawable);
                Cover_Photo.setBackground(null);
                Cover_Photo.setImageDrawable(new BitmapDrawable(getResources(), bitmapOrg));
                Cover_Photo.setDrawingCacheEnabled(true);
                SetPhot(data.getExtras().getString("file_path_arg"), Cover_Photo, R.drawable.home_bg, true);
//                Bitmap bitmap = Bitmap.createBitmap(drawableToBitmap(drawable));
//                Bitmap bitmap = mAttacher.getVisibleRectangleBitmap();
                UploadImageCover(new BitmapDrawable(getResources(), bitmapOrg));
            }

        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    ProgressDialog pd;

    public void UploadImageCover(Drawable drawable) {
        pd = ProgressDialog.newInstance();
        pd.show(((FragmentActivity) this).getSupportFragmentManager(), "pd");
        new UploadImageAPIcall(this, update_cover, drawable, user.getSession_key(), this, APIActions.ApiActions.add_media_cover);
    }

    @Override
    public void onSaveButtonClick(EditUserProfileUpdatePersonalInfoAlertDialog dialog, JSONObject data) {
        dialog.dismiss();
        try {
            if (data.getString("type").equalsIgnoreCase("BIO")) {
                user.setBio(data.getString("bio"));
                profiledataModel.setBio(data.getString("bio"));
            } else if (data.getString("type").equalsIgnoreCase("Email")) {
                setString("user_email", data.getString("email"), EditUserProfileActivity.this);
                user.setEmail(data.getString("email"));
                profiledataModel.setEmail(data.getString("email"));
                user.setEmail(data.getString("email"));
            } else if (data.getString("type").equalsIgnoreCase("Password")) {
                // password
                setString("user_password", data.getString("email"), EditUserProfileActivity.this);
            } else if (data.getString("type").equalsIgnoreCase("Zipcode")) {
                user.setZip_code(data.getString("zipcode"));
                profiledataModel.setZip_code(data.getString("zipcode"));

            } else if (data.getString("type").equalsIgnoreCase("name")) {
                user.setFirst_name(data.getString("name"));
                profiledataModel.setFirst_name(data.getString("name"));
                Profile_Name.setText(data.getString("name"));
            }
            SetProfileData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCrossButtonClick(EditUserProfileUpdatePersonalInfoAlertDialog dialog) {
        dialog.dismiss();
    }

    public void UploadImage(Drawable drawable) {
        new UploadImageAPIcall(EditUserProfileActivity.this, update_image, drawable, user.getSession_key(), EditUserProfileActivity.this, APIActions.ApiActions.add_media);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == APIActions.ApiActions.add_media_cover) {
            pd.dismiss();
        } else if (apiActions == APIActions.ApiActions.get_user_profile) {

            JSONObject json_object;
            try {
                json_object = new JSONObject(response).getJSONObject("successData").getJSONArray("user_data").getJSONObject(0);
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
                            ;
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
                profiledataModel.setCover(json_object.getString("cover"));
                profiledataModel.setExpertiesDataModels(expertiesDataModels);
                profiledataModel.setMedical_expertiex(medical_experties);
                profiledataModel.setMedical_expertiex_count(medical_experties_count);
                profiledataModel.setMedical_expertiex_ids(medical_experties_ids);
                profiledataModel.setStrain_Experties(Strain_Experties);
                profiledataModel.setStrain_Experties_count(Strain_Experties_count);
                profiledataModel.setStrain_Experties_ids(Strain_Experties_ids);
                SetProfileData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            profiledataModel.setImage_path(response);
        }

    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        pd.dismiss();
    }

    @Override
    public void onFollowFollwoingCrossBtnClink(UserFollowFollwingAlertDialog dialog) {
        dialog.dismiss();
        Follower_count.setText(profiledataModel.getFollowers_count() + "");
        Following_count.setText(profiledataModel.getFollowings_count() + "");
    }
}
