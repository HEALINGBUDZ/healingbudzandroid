package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.Utils;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.BusinessInfoTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.BusinessInfoTabFragmentEdit;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.EventPricesANDTicketesTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.ProductServicesTabFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs.SpecialInfoTabFragment;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.FragmentToActivityComm;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import io.togoto.imagezoomcrop.cropoverlay.CropOverlayView;
import io.togoto.imagezoomcrop.photoview.IGetImageBounds;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity.main_scroll_view;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel_abc;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_media_cropped;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class AddNewBudzMapActivity extends AppCompatActivity implements View.OnClickListener, APIResponseListner, FragmentToActivityComm {
    ImageView Check_box_organic, Check_box_deleivry;
    boolean isOrganic = false;
    boolean isDelivery = false;
    public static ScrollView add_main_scroll_view;
    Button Tab_One, Tab_two, Tab_three;
    ImageView Back, Home;
    EditText Title;
    Button Add_Logo;
    CropOverlayView mCropOverlayView;
    RelativeLayout toper_view;
    RelativeLayout Banner;
    ImageView LogoImage;
    Button resize, resize_cancel;
    ImageView Add_Banner;
    boolean isSubcriber_user = false;
    io.togoto.imagezoomcrop.photoview.PhotoView iv_photo;
    String Subcribed_user_id = "";
    boolean isLogo = false;
    public static JSONObject add_new_bud_jason_Data = new JSONObject();
    BusinessInfoTabFragment businessInfoTabFragment = new BusinessInfoTabFragment();
    BusinessInfoTabFragmentEdit businessInfoTabFragmentEdit = new BusinessInfoTabFragmentEdit();
    private ImageView Slider_img;
    LinearLayout opt_value;
    ProgressDialog progressDialog;
    LinearLayout other_tabs;
    private Uri mImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_budz_map);
        mContentResolver = getContentResolver();
        Title = (EditText) findViewById(R.id.Namea);
        opt_value = findViewById(R.id.opt_value);
        resize = findViewById(R.id.resize);
        LogoImage = (ImageView) findViewById(R.id.logo_img);
        Banner = (RelativeLayout) findViewById(R.id.top_header);
        resize_cancel = findViewById(R.id.resize_cancel);
        toper_view = findViewById(R.id.toper_view);
        mCropOverlayView = (CropOverlayView) findViewById(R.id.crop_overlay);
        iv_photo = findViewById(R.id.iv_photo);
        other_tabs = findViewById(R.id.other_tabs);
        Title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {
                    add_new_bud_jason_Data.put("title", Title.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        HideKeyboard(AddNewBudzMapActivity.this);
        isSubcriber_user = getIntent().getExtras().getBoolean("isSubcribed");
        Subcribed_user_id = getIntent().getExtras().getString("sub_user_id");
        add_main_scroll_view = (ScrollView) findViewById(R.id.main_scroll);
//        ChangeStatusBarColor(AddNewBudzMapActivity.this, "#0a0a0a");
        Check_box_organic = (ImageView) findViewById(R.id.check_box_one);
        Slider_img = (ImageView) findViewById(R.id.slider_img);
        iv_photo.setImageBoundsListener(new IGetImageBounds() {
            @Override
            public Rect getImageBounds() {
                return mCropOverlayView.getImageBounds();
            }
        });
        try {
            add_new_bud_jason_Data.put("is_organic", 0);
            add_new_bud_jason_Data.put("is_delivery", 0);
            if (isSubcriber_user) {
                add_new_bud_jason_Data.put("sub_user_id", Subcribed_user_id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Check_box_organic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOrganic) {
                    isOrganic = false;
                    Check_box_organic.setImageResource(R.drawable.ic_budmap_check_box_uncheckd);
                    try {
                        add_new_bud_jason_Data.put("is_organic", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    isOrganic = true;
                    Check_box_organic.setImageResource(R.drawable.ic_budmap_check_box_checked);
                    try {
                        add_new_bud_jason_Data.put("is_organic", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Check_box_deleivry = (ImageView) findViewById(R.id.check_box_two);
        Check_box_deleivry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDelivery) {
                    isDelivery = false;
                    Check_box_deleivry.setImageResource(R.drawable.ic_budmap_check_box_uncheckd);
                    try {
                        add_new_bud_jason_Data.put("is_delivery", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    isDelivery = true;
                    Check_box_deleivry.setImageResource(R.drawable.ic_budmap_check_box_checked);
                    try {
                        add_new_bud_jason_Data.put("is_delivery", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        if (budz_map_item_clickerd_dataModel_abc != null) {
            if (budz_map_item_clickerd_dataModel.getIs_featured() == 1) {
                other_tabs.setVisibility(View.VISIBLE);
            } else {
                other_tabs.setVisibility(View.VISIBLE);
            }
            if (!businessInfoTabFragmentEdit.isVisible()) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                transaction.add(R.id.fragment_layout, businessInfoTabFragmentEdit);
                transaction.commitAllowingStateLoss();
                SetTopData();

            }
        } else {
            budz_map_item_clickerd_dataModel_abc = null;
            budz_map_item_clickerd_dataModel = null;
            if (!businessInfoTabFragmentEdit.isVisible()) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                transaction.add(R.id.fragment_layout, businessInfoTabFragmentEdit);
                transaction.commitAllowingStateLoss();

            }
        }
        Tab_One = (Button) findViewById(R.id.tab_one);
//        Tab_One.setClickable(false);
//        Tab_One.setEnabled(false);

        Tab_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tab_One.setBackgroundColor(Color.parseColor("#932a88"));
                Tab_two.setBackgroundColor(Color.parseColor("#5c5c5c"));
                Tab_three.setBackgroundColor(Color.parseColor("#5c5c5c"));
                Tab_One.setTextColor(Color.parseColor("#e9d4e7"));
                Tab_three.setTextColor(Color.parseColor("#b0b0b0"));
                Tab_two.setTextColor(Color.parseColor("#b0b0b0"));
                if (budz_map_item_clickerd_dataModel_abc != null && budz_map_item_clickerd_dataModel != null) {
                    if (!businessInfoTabFragmentEdit.isVisible()) {
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                        transaction.replace(R.id.fragment_layout, businessInfoTabFragmentEdit);
                        transaction.commitAllowingStateLoss();
                        SetTopData();
                    }
                } else {
                    if (!businessInfoTabFragmentEdit.isVisible()) {
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                        transaction.replace(R.id.fragment_layout, businessInfoTabFragmentEdit);
                        transaction.commitAllowingStateLoss();
                    }
                }
            }
        });


        Tab_two = (Button) findViewById(R.id.tab_two);
//        Tab_two.setClickable(false);
//        Tab_two.setEnabled(false);
        Tab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager manager = getSupportFragmentManager();
                makeUpgrade(2);

//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
//                transaction.replace(R.id.fragment_layout, new ProductServicesTabFragment("0"));
//                transaction.commitAllowingStateLoss();


            }
        });
        Tab_three = (Button) findViewById(R.id.tab_three);
//        Tab_three.setClickable(false);
//        Tab_three.setEnabled(false);
        Tab_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeUpgrade(3);
//                FragmentManager manager = getSupportFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
//                transaction.replace(R.id.fragment_layout, new SpecialInfoTabFragment());
//                transaction.commitAllowingStateLoss();
//

            }
        });

        Back = (ImageView) findViewById(R.id.back_btn);
        Home = (ImageView) findViewById(R.id.home_btn);

        Back.setOnClickListener(this);
        Home.setOnClickListener(this);


        Add_Logo = (Button) findViewById(R.id.add_logo);
        Add_Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLogo = true;
                Intent intent = new Intent(view.getContext(), HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", false);
                startActivityForResult(intent, 1200);
            }
        });

        Add_Banner = (ImageView) findViewById(R.id.add_banner_btn);
        Add_Banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLogo = false;
                Intent intent = new Intent(view.getContext(), HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", false);
                startActivityForResult(intent, 1200);
            }
        });

        if (budz_map_item_clickerd_dataModel != null) {
            if (String.valueOf(budz_map_item_clickerd_dataModel.getBusiness_type_id()).equalsIgnoreCase("3")) {
                Tab_two.setText("Menu");
            } else if (String.valueOf(budz_map_item_clickerd_dataModel.getBusiness_type_id()).equalsIgnoreCase("2")
                    || String.valueOf(budz_map_item_clickerd_dataModel.getBusiness_type_id()).equalsIgnoreCase("6")
                    || String.valueOf(budz_map_item_clickerd_dataModel.getBusiness_type_id()).equalsIgnoreCase("7")) {
                Tab_two.setText("Products/Services");
            } else if (String.valueOf(budz_map_item_clickerd_dataModel.getBusiness_type_id()).equalsIgnoreCase("5")) {
                Tab_two.setText("Price/Tickets");
                Tab_One.setText("Event Details");
            } else {
                Tab_two.setText("Products/Services");
            }

        }
        resize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOutput();
            }
        });
        resize_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveOutput();
                toper_view.setVisibility(View.VISIBLE);
                resize.setVisibility(View.GONE);
                resize_cancel.setVisibility(View.GONE);
                if (cropPre.length() > 0) {
//                    pathFullPre = "";
                    pathFullNex = "";
                    cropNex = "";
                    Glide.with(AddNewBudzMapActivity.this)
                            .load(images_baseurl + cropPre)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(R.drawable.image_plaecholder_bg)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    iv_photo.setImageDrawable(resource);
                                    return false;
                                }
                            }).into(480, 480);
                    try {
                        add_new_bud_jason_Data.put("banner", cropPre);
                        add_new_bud_jason_Data.put("banner_full", pathFullPre);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    cropPre = "";
                } else {
                    pathFullPre = "";
                    pathFullNex = "";
                    cropNex = "";
                    cropPre = "";
                    iv_photo.setImageDrawable(null);
                    try {
                        add_new_bud_jason_Data.put("banner", "");
                        add_new_bud_jason_Data.put("banner_full", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        /////

    }

    String pathFullPre = "", pathFullNex = "";
    String cropPre = "", cropNex = "";

    private void init() {
        Bitmap bitmap = getBitmap(mImageUri);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

        float minScale = iv_photo.setMinimumScaleToFit(drawable);
        iv_photo.setMaximumScale(minScale * 3);
        iv_photo.setMediumScale(minScale * 2);
        iv_photo.setScale(minScale);
        iv_photo.setImageDrawable(drawable);

//        //Initialize the MoveResize text
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mMoveResizeText.getLayoutParams();
//        lp.setMargins(0, Math.round(Edge.BOTTOM.getCoordinate()) + 20, 0, 0);
//        mMoveResizeText.setLayoutParams(lp);
    }

    private ContentResolver mContentResolver;

    private final int IMAGE_MAX_SIZE = 480;

    private Bitmap getBitmap(Uri uri) {
        InputStream in = null;
        Bitmap returnedBitmap = null;
        try {
            in = mContentResolver.openInputStream(uri);
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();
            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = mContentResolver.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in, null, o2);
            in.close();

            //First check
            ExifInterface ei = new ExifInterface(uri.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    returnedBitmap = rotateImage(bitmap, 90);
                    //Free up the memory
                    bitmap.recycle();
                    bitmap = null;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    returnedBitmap = rotateImage(bitmap, 180);
                    //Free up the memory
                    bitmap.recycle();
                    bitmap = null;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    returnedBitmap = rotateImage(bitmap, 270);
                    //Free up the memory
                    bitmap.recycle();
                    bitmap = null;
                    break;
                default:
                    returnedBitmap = bitmap;
            }
            return returnedBitmap;
        } catch (FileNotFoundException e) {
            Log.e("Hi", "getBitmap: ", e);
        } catch (IOException e) {
            Log.e("Hi", "getBitmap: ", e);
        }
        return null;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public Fragment getTabTwoFragment() {
        if (String.valueOf(budz_map_item_clickerd_dataModel.getBusiness_type_id()).equalsIgnoreCase("5")) {
            return new EventPricesANDTicketesTabFragment("-1");
        } else {
            return new ProductServicesTabFragment("1");
        }
    }

    public Fragment getTabThreeFragment() {
        return new SpecialInfoTabFragment();
    }

    public void makeUpgrade(int which) {
        if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getBusiness_type_id() != 9 && budz_map_item_clickerd_dataModel.getIs_featured() == 0 && budz_map_item_clickerd_dataModel.getUser_id() == user.getUser_id()) {
            Intent intent = new Intent(this, BudzMapPaidViewActivity.class);
            intent.putExtra("isUpdate", true);
            intent.putExtra("budz_id", budz_map_item_clickerd_dataModel.getId());
            startActivityForResult(intent, Constants.UPGRADED);
        } else {
            if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getIs_featured() == 1) {
                if (which == 2) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                    transaction.replace(R.id.fragment_layout, getTabTwoFragment());
                    transaction.commitAllowingStateLoss();
                    getTabTwoFragment();
                    Tab_One.setBackgroundColor(Color.parseColor("#5c5c5c"));
                    Tab_two.setBackgroundColor(Color.parseColor("#932a88"));
                    Tab_three.setBackgroundColor(Color.parseColor("#5c5c5c"));

                    Tab_two.setTextColor(Color.parseColor("#e9d4e7"));
                    Tab_three.setTextColor(Color.parseColor("#b0b0b0"));
                    Tab_One.setTextColor(Color.parseColor("#b0b0b0"));
                } else if (which == 3) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                    transaction.replace(R.id.fragment_layout, getTabThreeFragment());
                    transaction.commitAllowingStateLoss();

                    Tab_One.setBackgroundColor(Color.parseColor("#5c5c5c"));
                    Tab_two.setBackgroundColor(Color.parseColor("#5c5c5c"));
                    Tab_three.setBackgroundColor(Color.parseColor("#932a88"));
                    Tab_three.setTextColor(Color.parseColor("#e9d4e7"));
                    Tab_two.setTextColor(Color.parseColor("#b0b0b0"));
                    Tab_One.setTextColor(Color.parseColor("#b0b0b0"));
                }
//                CustomeToast.ShowCustomToast(this, "Budz Ad is already featured!", Gravity.TOP);
            } else {
                if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getBusiness_type_id() != 9) {
                    Intent intent = new Intent(this, BudzMapPaidViewActivity.class);
                    intent.putExtra("isMakeNewUpdate", true);
                    intent.putExtra("budz_id", budz_map_item_clickerd_dataModel.getId());
                    startActivity(intent);
                } else if (budz_map_item_clickerd_dataModel == null) {
                    if (buzzType == 9) {
                        CustomeToast.ShowCustomToast(this, "You can't upgrade Other Type Business!", Gravity.TOP);
                    } else {
                        Intent intent = new Intent(this, BudzMapPaidViewActivity.class);
                        intent.putExtra("isMakeNewUpdate", true);
                        startActivity(intent);
                    }

                } else {
                    CustomeToast.ShowCustomToast(this, "You can't upgrade Other Type Business!", Gravity.TOP);
                }


            }
        }
    }

    public static boolean isNewMaker = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (budz_map_item_clickerd_dataModel != null) {
            if (budz_map_item_clickerd_dataModel.getIs_featured() == 1) {
                other_tabs.setVisibility(View.VISIBLE);

            } else {
                other_tabs.setVisibility(View.VISIBLE);
            }
        }
        if (isNewMaker) {
            isNewMaker = false;
            new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("Congratulations!")
                    .setContentText("Your business is now paid.")
                    .setConfirmText("Okay!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            if (budz_map_item_clickerd_dataModel != null) {
                                other_tabs.setVisibility(View.VISIBLE);
                            } else {
                                other_tabs.setVisibility(View.GONE);
                            }
                        }
                    }).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                add_new_bud_jason_Data = new JSONObject();
                if (SharedPrefrences.getBool("isFromProfile", AddNewBudzMapActivity.this)) {
                    GoToHome(AddNewBudzMapActivity.this, true);
                    SharedPrefrences.setBool("isFromProfile", false, AddNewBudzMapActivity.this);
                    finish();
                } else {
                    finish();
                    if (main_scroll_view != null) {
                        main_scroll_view.fullScroll(View.FOCUS_UP);
                    }
                }
                break;
            case R.id.home_btn:
                GoToHome(AddNewBudzMapActivity.this, true);
                finish();
                break;
        }
    }

    int buzzType = 1;

    @Override
    public void onBackPressed() {
        if (SharedPrefrences.getBool("isFromProfile", AddNewBudzMapActivity.this)) {
            GoToHome(AddNewBudzMapActivity.this, true);
            SharedPrefrences.setBool("isFromProfile", false, AddNewBudzMapActivity.this);
            super.onBackPressed();
        } else {
            super.onBackPressed();
            if (main_scroll_view != null) {
                main_scroll_view.fullScroll(View.FOCUS_UP);
            }
        }

    }

    private void saveOutput() {
        Bitmap croppedImage = iv_photo.getCroppedImage();
        progressDialog = new ProgressDialog(this);
        progressDialog.Show();
        new UploadImageAPIcall(AddNewBudzMapActivity.this, URL.add_banner, new BitmapDrawable(getResources(), croppedImage), user.getSession_key(), AddNewBudzMapActivity.this, add_media_cropped);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            Log.d("paths", data.getExtras().getString("file_path_arg"));
            Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
            bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));

//            int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
//            Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
            Drawable drawable = new BitmapDrawable(getResources(), bitmapOrg);
            if (isLogo) {
                progressDialog = new ProgressDialog(this);
                progressDialog.Show();
//                new UploadImageAPIcall(AddNewBudzMapActivity.this, URL.add_logo, drawable, user.getSession_key(), AddNewBudzMapActivity.this, APIActions.ApiActions.add_media);
                new UploadImageAPIcall(AddNewBudzMapActivity.this, URL.add_logo, data.getExtras().getString("file_path_arg"), user.getSession_key(), AddNewBudzMapActivity.this, APIActions.ApiActions.add_media);
                LogoImage.setImageDrawable(new BitmapDrawable(getResources(), bitmapOrg));
            } else {
                progressDialog = new ProgressDialog(this);
                progressDialog.Show();
//                new UploadImageAPIcall(AddNewBudzMapActivity.this, URL.add_banner, drawable, user.getSession_key(), AddNewBudzMapActivity.this, APIActions.ApiActions.add_media);
                new UploadImageAPIcall(AddNewBudzMapActivity.this, URL.add_banner, data.getExtras().getString("file_path_arg"), user.getSession_key(), AddNewBudzMapActivity.this, APIActions.ApiActions.add_media);
                Slider_img.setImageDrawable(new BitmapDrawable(getResources(), bitmapOrg));
                mImageUri = Utils.getImageUri(data.getExtras().getString("file_path_arg"));
                init();
//                iv_photo.setImageDrawable(new BitmapDrawable(getResources(), bitmapOrg));
            }
        } else if (resultCode == Constants.UPGRADED) {
            if (budz_map_item_clickerd_dataModel != null) {
                budz_map_item_clickerd_dataModel.setIs_featured(1);
                new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Congratulations!")
                        .setContentText("Your business is now paid.")
                        .setConfirmText("Okay!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                if (budz_map_item_clickerd_dataModel != null) {
                                    if (budz_map_item_clickerd_dataModel.getIs_featured() == 1) {
                                        other_tabs.setVisibility(View.VISIBLE);
                                    } else {
                                        other_tabs.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }).show();
            }
            if (budz_map_item_clickerd_dataModel_abc != null)
                budz_map_item_clickerd_dataModel_abc.setIs_featured(1);

        }
    }


    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        progressDialog.Dismis();
        if (apiActions == add_media_cropped) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                add_new_bud_jason_Data.put("banner", jsonObject.getString("url"));
                if (cropPre.length() > 0 && cropNex.length() > 0) {
                    cropPre = cropNex;
                    cropNex = jsonObject.getString("url");
                } else if (cropPre.length() > 0) {
                    cropNex = jsonObject.getString("url");
                } else {
                    cropPre = jsonObject.getString("url");
                }
                toper_view.setVisibility(View.VISIBLE);
                resize.setVisibility(View.GONE);
                resize_cancel.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (isLogo) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    add_new_bud_jason_Data.put("logo", jsonObject.getString("url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    toper_view.setVisibility(View.GONE);
                    resize.setVisibility(View.VISIBLE);
                    resize_cancel.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    add_new_bud_jason_Data.put("banner_full", jsonObject.getString("url"));
                    add_new_bud_jason_Data.put("banner", jsonObject.getString("url"));
                    if (pathFullPre.length() > 0 && pathFullNex.length() > 0) {
                        pathFullPre = pathFullNex;
                        pathFullNex = jsonObject.getString("url");
                    } else if (pathFullPre.length() > 0) {
                        pathFullNex = jsonObject.getString("url");
                    } else {
                        pathFullPre = jsonObject.getString("url");
                    }
                    init();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        progressDialog.Dismis();
    }

    public void SetTopData() {
//        if (!getTabOneFragment().isVisible()) {
//            FragmentManager manager = getSupportFragmentManager();
//            FragmentTransaction transaction = manager.beginTransaction();
//            transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
//            transaction.add(R.id.fragment_layout, getTabOneFragment());
//            transaction.commitAllowingStateLoss();
//        }
        if (budz_map_item_clickerd_dataModel != null) {
            if (budz_map_item_clickerd_dataModel.getBanner_full() != null && budz_map_item_clickerd_dataModel.getBanner_full().length() > 4) {
                pathFullPre = budz_map_item_clickerd_dataModel.getBanner_full();
            }
            if (budz_map_item_clickerd_dataModel.getBanner() != null && budz_map_item_clickerd_dataModel.getBanner().length() > 4) {
                cropPre = budz_map_item_clickerd_dataModel.getBanner();
            }
            try {
                add_new_bud_jason_Data.put("banner", cropPre);
                add_new_bud_jason_Data.put("banner_full", pathFullPre);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getBanner() != null && budz_map_item_clickerd_dataModel.getBanner().length() > 6) {
            Glide.with(AddNewBudzMapActivity.this)
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
                            iv_photo.setImageDrawable(resource);
                            return false;
                        }
                    }).into(480, 480);

        } else if (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getBanner() != null && budz_map_item_clickerd_dataModel.getBanner().length() > 6) {
            Glide.with(AddNewBudzMapActivity.this)
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
                            iv_photo.setImageDrawable(resource);
                            return false;
                        }
                    }).into(480, 480);
        }
        if (budz_map_item_clickerd_dataModel != null) {
            Glide.with(AddNewBudzMapActivity.this)
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
                            LogoImage.setImageDrawable(resource);
                            return false;
                        }
                    }).into(480, 480);


            MakeKeywordClickableText(Title.getContext(), budz_map_item_clickerd_dataModel.getTitle(), Title);
            if (budz_map_item_clickerd_dataModel.getIs_organic() == 0) {
                isOrganic = false;
                Check_box_organic.setImageResource(R.drawable.ic_budmap_check_box_uncheckd);
                try {
                    add_new_bud_jason_Data.put("is_organic", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                isOrganic = true;
                Check_box_organic.setImageResource(R.drawable.ic_budmap_check_box_checked);
                try {
                    add_new_bud_jason_Data.put("is_organic", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (budz_map_item_clickerd_dataModel.getIs_delivery() == 0) {

                isDelivery = false;
                Check_box_deleivry.setImageResource(R.drawable.ic_budmap_check_box_uncheckd);
                try {
                    add_new_bud_jason_Data.put("is_delivery", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                isDelivery = true;
                Check_box_deleivry.setImageResource(R.drawable.ic_budmap_check_box_checked);
                try {
                    add_new_bud_jason_Data.put("is_delivery", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

//        Title.setText(budz_map_item_clickerd_dataModel.getTitle());


    }

    @Override
    public boolean checkValue() {
        return Title != null && Title.getText().length() > 0;
    }

    @Override
    public void hideOptVale(boolean isHide, boolean hideCover) {
        if (isHide) {
            opt_value.setVisibility(View.INVISIBLE);
        } else {
            opt_value.setVisibility(View.VISIBLE);
        }
        if (hideCover) {
            buzzType = 9;
            iv_photo.setVisibility(View.INVISIBLE);
            Add_Banner.setVisibility(View.INVISIBLE);
        } else {
            buzzType = 1;
            iv_photo.setVisibility(View.VISIBLE);
            Add_Banner.setVisibility(View.VISIBLE);
        }
    }
}
