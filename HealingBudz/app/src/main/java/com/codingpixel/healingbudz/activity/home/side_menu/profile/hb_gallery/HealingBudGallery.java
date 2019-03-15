package com.codingpixel.healingbudz.activity.home.side_menu.profile.hb_gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.HealingBudGalleryModel;
import com.codingpixel.healingbudz.DataModel.ReportQuestionListDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.ReportItView.Report;
import com.codingpixel.healingbudz.Utilities.FileUtils;
import com.codingpixel.healingbudz.Utilities.PermissionHandler;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.HealingBudGalleryRecylerAdapter;
import com.codingpixel.healingbudz.adapter.StrainCommentFullViewAdapter;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;

import tcking.github.com.giraffeplayer.GiraffePlayer;

import static android.view.View.GONE;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainCommentFullView.strain_report_full_screen;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_media_cover;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.delete_hb;
import static com.codingpixel.healingbudz.network.model.URL.delete_hb_gallery;
import static com.codingpixel.healingbudz.network.model.URL.hb_gallery;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

@SuppressLint("Registered")
public class HealingBudGallery extends AppCompatActivity implements View.OnClickListener, HealingBudGalleryRecylerAdapter.ItemClickListener, APIResponseListner, ViewPager.OnPageChangeListener, ReportSendButtonLstner {
    ImageView Back, Home, download_btn, upload_btn;
    RecyclerView Images_Recyler_view;
    public Report galleryReport;
    LinearLayout Grid_layout;
    int UserID = 47;
    TextView No_Record_found;
    HealingBudGalleryRecylerAdapter adapter;
    LinearLayout image_showing_view;
    ViewPager mViewPager;
    boolean isImagePreview = false;
    int slide_start_point = 0;
    ImageView Image_Complete_View;
    ImagePaggerAdapter imagePaggerAdapter;
    TextView q_a_menu_title;
    ArrayList<HealingBudGalleryModel> images_paths = new ArrayList<>();
    LinearLayout Refresh;
    RelativeLayout Main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healing_bud_gallery);

        UserID = getIntent().getExtras().getInt("user_id", 0);

        ChangeStatusBarColor(HealingBudGallery.this, "#171717");
        Back = (ImageView) findViewById(R.id.back_btn);
        download_btn = (ImageView) findViewById(R.id.download_btn);
        upload_btn = (ImageView) findViewById(R.id.upload_btn);
        if (Splash.user.getUser_id() == UserID)
            upload_btn.setVisibility(View.VISIBLE);
        else upload_btn.setVisibility(View.GONE);
        Home = (ImageView) findViewById(R.id.home_btn);
        q_a_menu_title = (TextView) findViewById(R.id.q_a_menu_title);
//        if (UserID == user.getUser_id()) {
////            q_a_menu_title.setText("");
//        } else {
        q_a_menu_title.setText(MessageFormat.format("{0}''s Gallery", getIntent().getExtras().getString("name")));
//        }
        Back.setOnClickListener(this);
        Home.setOnClickListener(this);
        image_showing_view = (LinearLayout) findViewById(R.id.image_showing_view);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        imagePaggerAdapter = new ImagePaggerAdapter(this, images_paths);
        Refresh = (LinearLayout) findViewById(R.id.refresh);
        Refresh.setVisibility(View.VISIBLE);
        mViewPager.setAdapter(imagePaggerAdapter);
        Image_Complete_View = (ImageView) findViewById(R.id.complete_img);
        Image_Complete_View.setVisibility(GONE);
        mViewPager.addOnPageChangeListener(this);
        Images_Recyler_view = (RecyclerView) findViewById(R.id.images_gallery_recyler_view);
        Images_Recyler_view.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new HealingBudGalleryRecylerAdapter(this, images_paths, UserID);
        adapter.setClickListener(this);
        Images_Recyler_view.setAdapter(adapter);
        No_Record_found = (TextView) findViewById(R.id.no_recorcd_found);
        Grid_layout = (LinearLayout) findViewById(R.id.grid_layout);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealingBudGallery.this, HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", false);
                startActivityForResult(intent, 1200);
            }
        });
        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHandler.checkPermission(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, v.getContext(), new PermissionHandler.CheckPermissionResponse() {
                    @Override
                    public void permissionGranted() {
                        FileUtils.StartDownload(urlPath, fileName, HealingBudGallery.this);
                    }

                    @Override
                    public void showNeededPermissionDialog() {

                    }

                    @Override
                    public void requestPermission() {

                    }
                });

            }
        });
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(HealingBudGallery.this, false, hb_gallery + "/" + UserID, jsonObject, user.getSession_key(), Request.Method.GET, HealingBudGallery.this, APIActions.ApiActions.hb_gallery);
        Main_layout = (RelativeLayout) findViewById(R.id.main_cntnt_strain);
        galleryReport = new Report(this, this, "#7CC245", "gallery");
        Main_layout.addView(galleryReport.getView());
        galleryReport.InitSlide();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (images_paths.get(position).getType().equalsIgnoreCase("image")) {
            download_btn.setVisibility(View.GONE);
            upload_btn.setVisibility(View.GONE);
            urlPath = URL.images_baseurl + images_paths.get(position).getPath();
            fileName = images_paths.get(position).getPath();
        } else {
            download_btn.setVisibility(View.GONE);
            if (Splash.user.getUser_id() == UserID)
                upload_btn.setVisibility(View.VISIBLE);
            else upload_btn.setVisibility(View.GONE);
            urlPath = "";
            fileName = "";
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (images_paths.get(position).getType().equalsIgnoreCase("image")) {
            download_btn.setVisibility(View.GONE);
            upload_btn.setVisibility(View.GONE);
            urlPath = URL.images_baseurl + images_paths.get(position).getPath();
            fileName = images_paths.get(position).getPath();
        } else {
            download_btn.setVisibility(View.GONE);
            if (Splash.user.getUser_id() == UserID)
                upload_btn.setVisibility(View.VISIBLE);
            else upload_btn.setVisibility(View.GONE);
            urlPath = "";
            fileName = "";
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    String urlPath = "", fileName = "";

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (imagePaggerAdapter.player != null && imagePaggerAdapter.player.onBackPressed()) {
            Back.performClick();
        } else {
            Back.performClick();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                if (isImagePreview) {
                    download_btn.setVisibility(View.GONE);
                    if (Splash.user.getUser_id() == UserID)
                        upload_btn.setVisibility(View.VISIBLE);
                    else upload_btn.setVisibility(View.GONE);
                    isImagePreview = false;
                    Image_Complete_View.setVisibility(GONE);
                    Grid_layout.setVisibility(View.VISIBLE);
                    Images_Recyler_view.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }
                break;
            case R.id.home_btn:
                GoToHome(HealingBudGallery.this, true);
                finish();
                break;
        }
    }

    public void ShowImage(final int position) {
        Glide.with(HealingBudGallery.this)
                .load(images_baseurl + images_paths.get(position).getPath())
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
                        Image_Complete_View.setBackground(resource);
                        return false;
                    }
                }).into(1080, 1080);
    }

    @Override
    public void onItemClick(View view, int position) {
        download_btn.setVisibility(View.GONE);
        upload_btn.setVisibility(View.GONE);
        if (images_paths.get(position).getType().equalsIgnoreCase("image")) {
            isImagePreview = true;
            slide_start_point = position;
            ShowImage(position);
//            imagePaggerAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(position);
            image_showing_view.setVisibility(View.VISIBLE);
            Grid_layout.setVisibility(GONE);
//            Intent intent = new Intent(getContext(), MediPreview.class);
//            intent.putExtra("path", images_baseurl + images_paths.get(position).getPath());
//            intent.putExtra("isvideo", false);
//            getContext().startActivity(intent);
        } else {
            isImagePreview = true;
            slide_start_point = position;
//            imagePaggerAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(position);
            image_showing_view.setVisibility(View.VISIBLE);
            Grid_layout.setVisibility(GONE);
//            Intent intent = new Intent(getContext(), MediPreview.class);
//            intent.putExtra("path", videos_baseurl + images_paths.get(position).getPath());
//            intent.putExtra("isvideo", true);
//            getContext().startActivity(intent);
        }
    }

    @Override
    public void onItemDelete(View view, final int position) {
        new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to delete this image?")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        JSONObject jsonObject = new JSONObject();
                        new VollyAPICall(HealingBudGallery.this, false, delete_hb_gallery + "/" + images_paths.get(position).getId(), jsonObject, user.getSession_key(), Request.Method.GET, HealingBudGallery.this, delete_hb);
                        images_paths.remove(position);
                        imagePaggerAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
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
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Refresh.setVisibility(GONE);
        if (apiActions == delete_hb) {
            CustomeToast.ShowCustomToast(getContext(), "Deleted Successfully!", Gravity.TOP);
        } else if (apiActions == add_media_cover) {
            pd.dismiss();
            HealingBudGalleryModel model = new HealingBudGalleryModel();
            JSONObject object = null;
            try {
                object = new JSONObject(response);
                model.setId(object.getInt("id"));
                model.setA_type(object.getString("type"));
                model.setPath(object.getString("path"));
                model.setType(object.getString("type"));
                model.setUser_id(object.getInt("user_id"));
                model.setFlag_count(object.optInt("flaged_count"));
                model.setPoster("");
                model.setCreated_at(object.getString("created_at"));
                model.setV_pk(String.valueOf(object.getInt("id")) + "_hbgallery");
                images_paths.add(model);
                adapter.notifyDataSetChanged();
                imagePaggerAdapter.notifyDataSetChanged();
                if (isImagePreview) {
                    slide_start_point = images_paths.size() - 1;
                    ShowImage(images_paths.size() - 1);
//                        imagePaggerAdapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(images_paths.size() - 1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("successData");
                if (jsonArray.length() > 0) {
                    No_Record_found.setVisibility(GONE);
                    Images_Recyler_view.setVisibility(View.VISIBLE);
                    images_paths.clear();
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject object = jsonArray.getJSONObject(x);
                        HealingBudGalleryModel model = new HealingBudGalleryModel();
                        model.setId(object.getInt("id"));
//                        model.setA_type(object.getString("a_type"));
                        model.setPath(object.getString("path"));
                        model.setType(object.getString("type"));
                        model.setUser_id(object.getInt("user_id"));
                        model.setPoster(object.getString("poster"));
                        model.setFlag_count(object.optInt("flaged_count"));
                        model.setCreated_at(object.getString("created_at"));
//                        model.setV_pk(object.getString("v_pk"));
                        images_paths.add(model);
                    }
                    adapter.notifyDataSetChanged();
                    imagePaggerAdapter.notifyDataSetChanged();

                    if (isImagePreview) {
                        slide_start_point = images_paths.size() - 1;
                        ShowImage(images_paths.size() - 1);
//                        imagePaggerAdapter.notifyDataSetChanged();
                        mViewPager.setCurrentItem(images_paths.size() - 1);
                    }
                } else {
                    No_Record_found.setVisibility(View.VISIBLE);
                    Images_Recyler_view.setVisibility(GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Refresh.setVisibility(GONE);
        if (apiActions == delete_hb) {
            CustomeToast.ShowCustomToast(getContext(), "Could not delete server error!", Gravity.TOP);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (imagePaggerAdapter.player != null) {
            imagePaggerAdapter.player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (imagePaggerAdapter.player != null) {
            imagePaggerAdapter.player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imagePaggerAdapter.player != null) {
            imagePaggerAdapter.player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (imagePaggerAdapter.player != null) {
            imagePaggerAdapter.player.onConfigurationChanged(newConfig);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            Log.d("paths", data.getExtras().getString("file_path_arg"));
            Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
            if (bitmapOrg != null) {
                bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
                Drawable drawable = new BitmapDrawable(getResources(), bitmapOrg);
                UploadImageCover(data.getExtras().getString("file_path_arg"));
//                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 1400, 1400, false);

            }

        }
    }

    ProgressDialog pd;

    public void UploadImageCover(String drawable) {
        pd = ProgressDialog.newInstance();
        pd.show(getSupportFragmentManager(), "hey_pd");
        new UploadImageAPIcall(this, URL.add_hb_media, drawable, user.getSession_key(), this, add_media_cover);
    }

    @Override
    public void OnSnedClicked(JSONObject data, final int position) {
        try {
            new VollyAPICall(this, true, URL.block_hb_gallery, new JSONObject().put("image_id", images_paths.get(position).getId()).put("reason", data.getString("reason"))
                    , Splash.user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                @Override
                public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                    images_paths.get(position).setFlag_count(1);
                    adapter.notifyDataSetChanged();
                    imagePaggerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onRequestError(String response, APIActions.ApiActions apiActions) {

                }
            }, APIActions.ApiActions.testAPI);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ImagePaggerAdapter extends PagerAdapter {

        ImageView Back_Img, Next_Img, download_image, play_vid;
        TextView Image_Counter;
        Activity mContext;
        ArrayList<HealingBudGalleryModel> mData = new ArrayList<>();
        LayoutInflater mLayoutInflater;
        ProgressBar Loading_spiner;
        com.codingpixel.healingbudz.customeUI.TouchImageView imageView;
        public GiraffePlayer player;

        public ImagePaggerAdapter(Activity context, ArrayList<HealingBudGalleryModel> mData) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mData = mData;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final View itemView = mLayoutInflater.inflate(R.layout.pager_layout_user_main_gallery, container, false);
            imageView = itemView.findViewById(R.id.img);
            ImageView download_image_user = itemView.findViewById(R.id.download_image_user);
            LinearLayout linn = itemView.findViewById(R.id.linn);
            ImageView flag_image_user = itemView.findViewById(R.id.flag_image_user);
            RelativeLayout flag_view = itemView.findViewById(R.id.flag_view);
            play_vid = itemView.findViewById(R.id.play_vid);
            download_image_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PermissionHandler.checkPermission(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, v.getContext(), new PermissionHandler.CheckPermissionResponse() {
                        @Override
                        public void permissionGranted() {
                            String pth = URL.images_baseurl + images_paths.get(position).getPath();
                            FileUtils.StartDownload(pth, images_paths.get(position).getPath(), HealingBudGallery.this);
                        }

                        @Override
                        public void showNeededPermissionDialog() {

                        }

                        @Override
                        public void requestPermission() {

                        }
                    });
                }
            });
            if (UserID == Splash.user.getUser_id()) {
                flag_image_user.setVisibility(GONE);
                linn.setWeightSum(1.0F);
                if (mData.get(position).getType().equalsIgnoreCase("image")){
                    download_image_user.setVisibility(View.VISIBLE);
                } else {
                    download_image_user.setVisibility(View.GONE);
                }
            } else {
                linn.setWeightSum(2.0F);
                flag_image_user.setVisibility(View.VISIBLE);
                if (mData.get(position).getType().equalsIgnoreCase("image")){
                    linn.setWeightSum(2.0F);
                    download_image_user.setVisibility(View.VISIBLE);
                } else {
                    linn.setWeightSum(1.0F);
                    download_image_user.setVisibility(View.GONE);
                }
            }
            if (mData.get(position).getFlag_count() == 0) {
                flag_image_user.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                flag_image_user.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.post_description_links_color), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            flag_image_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<ReportQuestionListDataModel> dataModels = new ArrayList<>();
                    dataModels.add(new ReportQuestionListDataModel("Nudity or sexual content", false));
                    dataModels.add(new ReportQuestionListDataModel("Harassment or hate speech", false));
                    dataModels.add(new ReportQuestionListDataModel("Threatening, violent, or concerning", false));
                    dataModels.add(new ReportQuestionListDataModel("Spam", false));
                    dataModels.add(new ReportQuestionListDataModel("Offensive", false));
                    dataModels.add(new ReportQuestionListDataModel("Unrelated", false));
                    if (mData.get(position).getFlag_count() == 0) {
                        if (galleryReport.isSlide()) {
                            galleryReport.SlideUp();
                        } else {
                            galleryReport.SlideDown(position, dataModels, HealingBudGallery.this, "gallery");
                        }
                    } else {
                        if (mData.get(position).getType().equalsIgnoreCase("image")){
                            CustomeToast.ShowCustomToast(itemView.getContext(), "You already flagged this image!", Gravity.TOP);
                        } else {
                            CustomeToast.ShowCustomToast(itemView.getContext(), "You already flagged this video!", Gravity.TOP);
                        }

                    }

                }
            });
            download_image = itemView.findViewById(R.id.download_image);
            Loading_spiner = (ProgressBar) itemView.findViewById(R.id.loading_spinner);
            Loading_spiner.setVisibility(View.GONE);
            final RelativeLayout rl = itemView.findViewById(R.id.app_video_box);
            imagePaggerAdapter.player = new GiraffePlayer((Activity) itemView.getContext(), itemView);
            imagePaggerAdapter.player.hide(true);
            rl.setVisibility(GONE);
            if (mData.get(position).getType().equalsIgnoreCase("image")) {
                download_btn.setVisibility(View.GONE);
                download_image_user.setVisibility(View.VISIBLE);
                play_vid.setVisibility(GONE);
//                urlPath = images_baseurl + mData.get(position).getPath();
//                fileName = mData.get(position).getPath();
                download_image.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Loading_spiner.setVisibility(View.VISIBLE);
                imagePaggerAdapter.player.hide(true);
                download_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
//                        urlPath = images_baseurl + mData.get(position).getPath();
//                        fileName = mData.get(position).getPath();
                        PermissionHandler.checkPermission(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, v.getContext(), new PermissionHandler.CheckPermissionResponse() {
                            @Override
                            public void permissionGranted() {
                                FileUtils.StartDownload(images_baseurl + mData.get(position).getPath(), mData.get(position).getPath(), v.getContext());
                            }

                            @Override
                            public void showNeededPermissionDialog() {

                            }

                            @Override
                            public void requestPermission() {

                            }
                        });

                    }
                });
                rl.setVisibility(GONE);
//                vid.setVisibility(View.GONE);
                Glide.with(HealingBudGallery.this)
                        .load(images_baseurl + mData.get(position).getPath())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.image_plaecholder_bl)
                        .error(R.drawable.noimage)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                Loading_spiner.setVisibility(GONE);
                                rl.setVisibility(GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Log.d("ready", model);
                                imageView.setImageDrawable(resource);
                                Loading_spiner.setVisibility(GONE);
                                rl.setVisibility(GONE);
                                return false;
                            }
                        }).into(imageView);
//                Loading_spiner.setVisibility(GONE);

            } else {

                download_image_user.setVisibility(GONE);
                imagePaggerAdapter.player.hide(true);
                play_vid.setVisibility(View.VISIBLE);
                play_vid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = "";
                        path = URL.videos_baseurl + mData.get(position).getPath();
                        Intent intent = new Intent(v.getContext(), MediPreview.class);
                        intent.putExtra("path", path);
                        intent.putExtra("isvideo", true);
                        v.getContext().startActivity(intent);
                    }
                });
                download_btn.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Loading_spiner.setVisibility(View.VISIBLE);
                Glide.with(HealingBudGallery.this)
                        .load(images_baseurl + mData.get(position).getPoster())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.image_plaecholder_bl)
                        .error(R.drawable.noimage)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                Loading_spiner.setVisibility(GONE);
                                rl.setVisibility(GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Log.d("ready", model);
                                imageView.setImageDrawable(resource);
                                Loading_spiner.setVisibility(GONE);
                                rl.setVisibility(GONE);
                                return false;
                            }
                        }).into(imageView);
//                urlPath = "";
//                fileName = "";
                download_image.setVisibility(View.GONE);
                rl.setVisibility(GONE);
                imagePaggerAdapter.player.hide(false);


//                imagePaggerAdapter.player.play(videos_baseurl + mData.get(position).getPath());
//
////                imagePaggerAdapter.player.
//                imagePaggerAdapter.player.onComplete(new Runnable() {
//                    @Override
//                    public void run() {
//                        Loading_spiner.setVisibility(View.GONE);
//                        imagePaggerAdapter.player.pause();
//                        //callback when video is finish
//                    }
//                }).onInfo(new GiraffePlayer.OnInfoListener() {
//                    @Override
//                    public void onInfo(int what, int extra) {
//                        switch (what) {
//                            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
//                                Loading_spiner.setVisibility(View.GONE);
//                                //do something when buffering start
//                                break;
//                            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
//                                Loading_spiner.setVisibility(View.GONE);
//                                //do something when buffering end
//                                break;
//                            case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
//                                Loading_spiner.setVisibility(View.GONE);
//                                //download speed
//                                Log.d("speed", Formatter.formatFileSize(getApplicationContext(), extra) + "/s");
//                                break;
//                            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//                                Loading_spiner.setVisibility(View.GONE);
//                                //do something when video rendering
//                                break;
//                        }
//                    }
//                }).onError(new GiraffePlayer.OnErrorListener() {
//                    @Override
//                    public void onError(int what, int extra) {
//                        Loading_spiner.setVisibility(View.GONE);
//                    }
//                });
//                Loading_spiner.setVisibility(View.GONE);
            }

            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }

}
