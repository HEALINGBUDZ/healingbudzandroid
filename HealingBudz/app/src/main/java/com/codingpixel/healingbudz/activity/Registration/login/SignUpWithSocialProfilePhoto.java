package com.codingpixel.healingbudz.activity.Registration.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.Registration.AddYourExpertAreasActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.ProfilePhotoAvatreRecylerAdapter;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.activity.Registration.SignUpwithEmailFirstStep.isSpecialBud;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.network.model.URL.get_defaults;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.update_image;
import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class SignUpWithSocialProfilePhoto extends AppCompatActivity implements ProfilePhotoAvatreRecylerAdapter.ItemClickListener, APIResponseListner {
    ImageView Next_Step;
    Button Upload_profile_img;
    TextView Hello_User;
    ImageView Back;
    public static String demo_question;
    ArrayList<String> avater_data = new ArrayList<>();
    ArrayList<String> avater_data_topi = new ArrayList<>();
    ProgressBar preogress_bar, preogress_bar_topi;
    RecyclerView Avatr_Recyler_View, Avatr_Recyler_View_topi;
    ImageView Profile_Img;
    ImageView profile_img_topi;
    LinearLayout topi;
    public static Drawable user_image_drawable;
    public static Drawable user_image_drawable_topi;
    String selected_avatar_name = "/icons/1.png";
    String selected_avatar_name_topi = "/icons/1.png";
    ProfilePhotoAvatreRecylerAdapter avatreRecylerAdapter;
    ProfilePhotoAvatreRecylerAdapter avatreRecylerAdapterTopi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_withemail_profile_photo_layout);
        FullScreen(SignUpWithSocialProfilePhoto.this);
        HideKeyboard(SignUpWithSocialProfilePhoto.this);
        Init();
    }

    Drawable drawable;

    public void Init() {
        isSpecialBud = false;
        Back = (ImageView) findViewById(R.id.back_btn);
        topi = findViewById(R.id.topi);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Profile_Img = (ImageView) findViewById(R.id.profile_img);
        Profile_Img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        profile_img_topi = (ImageView) findViewById(R.id.profile_img_topi);
        preogress_bar = (ProgressBar) findViewById(R.id.loading_spinner);
        preogress_bar_topi = (ProgressBar) findViewById(R.id.loading_spinner_topi);
        Hello_User = (TextView) findViewById(R.id.hello_user);
        Hello_User.setText("Hello, " + Splash.user.getFirst_name());
        Next_Step = (ImageView) findViewById(R.id.next_step);
        Next_Step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UploadImageAPIcall(SignUpWithSocialProfilePhoto.this, update_image, drawable, Splash.user.getSession_key(), SignUpWithSocialProfilePhoto.this, APIActions.ApiActions.upload_profile_img);
                //                    new VollyAPICall(SignUpWithSocialProfilePhoto.this, true, register, signup_data, null, Request.Method.POST, SignUpWithSocialProfilePhoto.this, APIActions.ApiActions.register);
            }
        });

        Avatr_Recyler_View = (RecyclerView) findViewById(R.id.avatar_recyler_view);
        Avatr_Recyler_View.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        avatreRecylerAdapter = new ProfilePhotoAvatreRecylerAdapter(this, avater_data, false);
        avatreRecylerAdapter.setClickListener(this);
        Avatr_Recyler_View.setAdapter(avatreRecylerAdapter);
        Avatr_Recyler_View_topi = (RecyclerView) findViewById(R.id.avatar_recyler_view_topi);
        Avatr_Recyler_View_topi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        avatreRecylerAdapterTopi = new ProfilePhotoAvatreRecylerAdapter(this, avater_data_topi, true);
        avatreRecylerAdapterTopi.setClickListener(this);
        Avatr_Recyler_View_topi.setAdapter(avatreRecylerAdapterTopi);
        JSONObject object = new JSONObject();
        new VollyAPICall(SignUpWithSocialProfilePhoto.this, false, get_defaults + "/" + Splash.user.getEmail(), object, Splash.user.getSession_key(), Request.Method.GET, SignUpWithSocialProfilePhoto.this, APIActions.ApiActions.get_defaults);
        Upload_profile_img = (Button) findViewById(R.id.upload_profile_img);
        Upload_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > 15) {
                    final String[] permissions = {
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE};
                    final List<String> permissionsToRequest = new ArrayList<>();
                    for (String permission : permissions) {
                        if (ActivityCompat.checkSelfPermission(SignUpWithSocialProfilePhoto.this, permission) != PackageManager.PERMISSION_GRANTED) {
                            permissionsToRequest.add(permission);
                        }
                    }
                    if (!permissionsToRequest.isEmpty()) {
                        ActivityCompat.requestPermissions(SignUpWithSocialProfilePhoto.this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 12);
                    } else {

                        Intent intent = new Intent(SignUpWithSocialProfilePhoto.this, HBCameraActivity.class);
                        intent.putExtra("isVideoCaptureAble", false);
                        startActivityForResult(intent, 1200);
                    }
                } else {
                    Intent intent = new Intent(SignUpWithSocialProfilePhoto.this, HBCameraActivity.class);
                    intent.putExtra("isVideoCaptureAble", false);
                    startActivityForResult(intent, 1200);
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, Drawable drawable, int position) {
        Profile_Img.setImageDrawable(drawable);
        this.drawable = new BitmapDrawable(getResources(), drawableToBitmap(drawable));
        selected_avatar_name = avater_data.get(position);
    }

    @Override
    public void onTopiClicked(View view, Drawable drawable, int position) {
        profile_img_topi.setImageDrawable(drawable);
        selected_avatar_name_topi = avater_data_topi.get(position);
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

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == APIActions.ApiActions.upload_profile_img) {
            Log.d("respoonse", response);
            JSONObject signup_data = new JSONObject();
            try {

                signup_data.put("avatar", selected_avatar_name);
                new VollyAPICall(SignUpWithSocialProfilePhoto.this, false, URL.update_avatar, signup_data, user.getSession_key(), Request.Method.POST, SignUpWithSocialProfilePhoto.this, APIActions.ApiActions.register);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (apiActions == APIActions.ApiActions.register) {
            Log.d("respoonse", response);

            new VollyAPICall(this,
                    false
                    , URL.get_keywords
                    , new JSONObject()
                    , user.getSession_key()
                    , Request.Method.GET
                    , new APIResponseListner() {
                @Override
                public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                    try {
                        if (new JSONObject(response).getString("status").equalsIgnoreCase("success")) {
                            JSONArray jsonObject = new JSONObject(response).getJSONArray("successData");
                            Splash.keywordList = new ArrayList<>();
                            for (int i = 0; i < jsonObject.length(); i++) {
                                JSONObject object = jsonObject.getJSONObject(i);
                                Splash.keywordList.add(object.getString("title"));
                            }

                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void onRequestError(String response, APIActions.ApiActions apiActions) {

                }
            }
                    , APIActions.ApiActions.key_words);
            Intent i = new Intent(SignUpWithSocialProfilePhoto.this, AddYourExpertAreasActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();

        } else {
            try {
                JSONObject object = new JSONObject(response);
                JSONArray avatr_list = object.getJSONObject("successData").getJSONArray("icons");
                JSONArray avatr_list_topi = object.getJSONObject("successData").getJSONArray("specials_icons");
                avater_data.clear();
                avater_data_topi.clear();
                for (int x = 0; x < avatr_list.length(); x++) {
                    JSONObject jsonObject = avatr_list.getJSONObject(x);
                    avater_data.add(jsonObject.getString("name"));
                }
                for (int x = 0; x < avatr_list_topi.length(); x++) {
                    JSONObject jsonObject = avatr_list_topi.getJSONObject(x);
                    avater_data_topi.add(jsonObject.getString("name"));
                }
                preogress_bar.setVisibility(View.GONE);
                Avatr_Recyler_View.setVisibility(View.VISIBLE);
                avatreRecylerAdapterTopi.notifyDataSetChanged();
                if (isSpecialBud) {
                    topi.setVisibility(View.VISIBLE);
                    profile_img_topi.setVisibility(View.VISIBLE);
                    preogress_bar_topi.setVisibility(View.GONE);
                    Avatr_Recyler_View_topi.setVisibility(View.VISIBLE);
                    avatreRecylerAdapterTopi.notifyDataSetChanged();
                }
                if (avater_data.size() > 0) {
//                    selected_avatar_name = avater_data.get(0);
                    Glide.with(this)
                            .load(images_baseurl + avater_data.get(0))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(R.drawable.ic_user_profile_green)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    Profile_Img.setImageDrawable(resource);
                                    return false;
                                }
                            })
                            .into(480, 480);
                }
                if (avater_data_topi.size() > 0) {
                    selected_avatar_name_topi = avater_data_topi.get(0);
                    Glide.with(this)
                            .load(images_baseurl + avater_data_topi.get(0))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(R.drawable.ic_user_profile_green)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    profile_img_topi.setImageDrawable(resource);
                                    return false;
                                }
                            })
                            .into(480, 480);
                }
                demo_question = object.getJSONObject("successData").getJSONObject("question").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Log.d("respinse", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getApplicationContext(), object.getString("errorMessage"), Gravity.TOP);
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
//            bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
            int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
            Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            Profile_Img.setImageBitmap(bitmapOrg);
            Profile_Img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            this.drawable = drawable;
//            new UploadImageAPIcall(SignUpWithSocialProfilePhoto.this, update_image, drawable, Splash.user.getSession_key(), SignUpWithSocialProfilePhoto.this, APIActions.ApiActions.upload_profile_img);
        }
    }

}
