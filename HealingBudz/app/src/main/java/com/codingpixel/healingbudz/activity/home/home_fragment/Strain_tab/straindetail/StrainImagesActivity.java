package com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.ReportQuestionListDataModel;
import com.codingpixel.healingbudz.DataModel.StrainDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.ReportItView.Report;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.Utilities.FileUtils;
import com.codingpixel.healingbudz.Utilities.PermissionHandler;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.adapter.StrainsImagesGalleryRecylerAdapter;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_file_with_progress.UploadFileWithProgress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.codingpixel.healingbudz.Utilities.Constants.D_FORMAT_ONE;
import static com.codingpixel.healingbudz.Utilities.StrainRatingImage.Strain_Rating;
import static com.codingpixel.healingbudz.Utilities.UserNameTextColorWRTRating.getUserRatingColor;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity.strainDataModel;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity.strainDetailsActivity_scrollView;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_strains;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.upload_strain_image;
import static com.codingpixel.healingbudz.network.model.URL.delete_strain_image;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

public class StrainImagesActivity extends AppCompatActivity implements View.OnClickListener, StrainsImagesGalleryRecylerAdapter.ItemClickListener, APIResponseListner, ReportSendButtonLstner {
    ImageView Back, Home, Rating_image;
    RecyclerView Images_Recyler_view;
    LinearLayout Image_Complete_View;
    boolean isImagePreview = false;
    LinearLayout Grid_layout;
    LinearLayout Review_Layout;
    ImageView Complete_image;
    int slide_start_point = 0;
    RelativeLayout Main_Layout;
    public Report strain_report;
    ImageView Upload_image_button;
    StrainsImagesGalleryRecylerAdapter adapter;
    ViewPager mViewPager;
    ImagePaggerAdapter imagePaggerAdapter;
    SwipeRefreshLayout swipe_layout;
    TextView StainTitle, StrainType, Rating, Total_reviews;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.strain_images_activity);
        Main_Layout = (RelativeLayout) findViewById(R.id.main_layout);
        strain_report = new Report(this, this, "#f4c42f", "strain");
        Main_Layout.addView(strain_report.getView());
        strain_report.InitSlide();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        swipe_layout = findViewById(R.id.swipe_layout);

        ChangeStatusBarColor(StrainImagesActivity.this, "#171717");
        Back = (ImageView) findViewById(R.id.back_btn);
        Home = (ImageView) findViewById(R.id.home_btn);
        Back.setOnClickListener(this);
        Home.setOnClickListener(this);
        Images_Recyler_view = (RecyclerView) findViewById(R.id.images_gallery_recyler_view);
        Images_Recyler_view.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new StrainsImagesGalleryRecylerAdapter(this, strainDataModel.getImages());
        adapter.setClickListener(this);
        Images_Recyler_view.setAdapter(adapter);
        imagePaggerAdapter = new ImagePaggerAdapter(this, strainDataModel.getImages());
        mViewPager.setAdapter(imagePaggerAdapter);
        Grid_layout = (LinearLayout) findViewById(R.id.grid_layout);
        Image_Complete_View = (LinearLayout) findViewById(R.id.image_showing_view);
        Complete_image = (ImageView) findViewById(R.id.complete_img);
        Review_Layout = (LinearLayout) findViewById(R.id.review_layout);
        Review_Layout.setOnClickListener(this);
        SetData();
        Upload_image_button = (ImageView) findViewById(R.id.upload_image_button);
        Upload_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StrainImagesActivity.this, HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", false);
                startActivityForResult(intent, 1200);
            }
        });
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                JSONObject jsonObject = new JSONObject();
                new VollyAPICall(StrainImagesActivity.this, true, URL.get_strain + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainImagesActivity.this, get_strains);
            }
        });
    }

    public void ShowImage(final int position) {
        Glide.with(StrainImagesActivity.this)
                .load(images_baseurl + strainDataModel.getImages().get(position).getImage_path())
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
//                        Complete_image.setBackground(resource);
                        return false;
                    }
                }).into(Complete_image);
    }

    public void SetData() {
        swipe_layout.setRefreshing(false);
        StainTitle = (TextView) findViewById(R.id.main_heading_title);
        StainTitle.setText(strainDataModel.getTitle());
        StainTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StrainType = (TextView) findViewById(R.id.strain_type);
        StrainType.setText(strainDataModel.getType_title());

        Rating = (TextView) findViewById(R.id.rating);
        Rating.setText(strainDataModel.getRating() + "");


        Total_reviews = (TextView) findViewById(R.id.total_reviews);
        Total_reviews.setText(strainDataModel.getReviews());

        Rating_image = (ImageView) findViewById(R.id.rating_image);
        Rating_image.setImageResource(Strain_Rating(strainDataModel.getRating()));
        adapter = new StrainsImagesGalleryRecylerAdapter(this, strainDataModel.getImages());
        adapter.setClickListener(this);
        Images_Recyler_view.setAdapter(adapter);
        imagePaggerAdapter = new ImagePaggerAdapter(this, strainDataModel.getImages());
        mViewPager.setAdapter(imagePaggerAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                if (isImagePreview) {
                    isImagePreview = false;
                    Image_Complete_View.setVisibility(View.GONE);
                    Grid_layout.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }

                break;
            case R.id.home_btn:
                GoToHome(StrainImagesActivity.this, true);
                finish();
                break;

            case R.id.review_layout:
                strainDetailsActivity_scrollView.smoothScrollTo(0, 4100);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isImagePreview) {
            isImagePreview = false;
            Image_Complete_View.setVisibility(View.GONE);
            Grid_layout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onItemClick(View view, int position, Drawable drawable) {
        isImagePreview = true;
        slide_start_point = position;
        ShowImage(position);
        imagePaggerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(position);
        Image_Complete_View.setVisibility(View.VISIBLE);
        Grid_layout.setVisibility(View.GONE);
    }

    @Override
    public void onDeleteClick(View view, final int position) {
        new SweetAlertDialog(StrainImagesActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to delete this strain image?")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        JSONObject jsonObject = new JSONObject();
                        new VollyAPICall(StrainImagesActivity.this, true, delete_strain_image + strainDataModel.getImages().get(position).getId(), jsonObject, user.getSession_key(), Request.Method.GET, new APIResponseListner() {
                            @Override
                            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                                JSONObject jsonObject = new JSONObject();
                                new VollyAPICall(StrainImagesActivity.this, true, URL.get_strain + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainImagesActivity.this, get_strains);
                            }

                            @Override
                            public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                JSONObject jsonObject = new JSONObject();
                                new VollyAPICall(StrainImagesActivity.this, true, URL.get_strain + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainImagesActivity.this, get_strains);
                            }
                        }, APIActions.ApiActions.delete_answer);

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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            Log.d("paths", data.getExtras().getString("file_path_arg"));
            Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
            bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
//            bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
            Drawable drawable = new BitmapDrawable(getResources(), bitmapOrg);
            if (isImagePreview) {
                Complete_image.setBackground(drawable);
                Date newDate = new Date();
                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String date = spf.format(newDate);
            }
            UploadImage(data.getExtras().getString("file_path_arg"));
        }
    }

    public void UploadImage(String image) {
        JSONArray parameter = new JSONArray();
        parameter.put("strain_id");
        JSONArray values = new JSONArray();
        values.put(strainDataModel.getId());
        new UploadFileWithProgress(this, true, URL.upload_strain_image, image, "image", values, parameter, null, StrainImagesActivity.this, upload_strain_image).execute();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        imagePaggerAdapter.notifyDataSetChanged();
        Log.d("response", response);
        if (apiActions == upload_strain_image) {
            CustomeToast.ShowCustomToast(StrainImagesActivity.this, "Thanks bud! Your image has been submitted for approval.", Gravity.TOP);
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
//                adapter.notifyDataSetChanged();
//                if (isImagePreview) {
//                    slide_start_point = strainDataModel.getImages().size() - 1;
//                    ShowImage(strainDataModel.getImages().size() - 1);
//                    imagePaggerAdapter.notifyDataSetChanged();
//                    mViewPager.setCurrentItem(strainDataModel.getImages().size() - 1);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
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
        swipe_layout.setRefreshing(false);
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(StrainImagesActivity.this, object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            new VollyAPICall(StrainImagesActivity.this, true, URL.save_user_image_strain_flag, jsonObject, user.getSession_key(), Request.Method.POST, StrainImagesActivity.this, APIActions.ApiActions.testAPI);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class ImagePaggerAdapter extends PagerAdapter {
        TextView Photo_Powered_By, Photo_Date, Like_Count;
        LinearLayout Like_btn, Dislike_btn, Flag_Strain;
        ImageView download_image, Flag_Img, Like_Icon, Dislike_icon, Back_Img, Next_Img;
        ;
        TextView Image_Counter, Dislike_count;
        com.codingpixel.healingbudz.customeUI.TouchImageView imageView;
        Context mContext;
        ArrayList<StrainDataModel.Images> mData = new ArrayList<>();
        LayoutInflater mLayoutInflater;

        public ImagePaggerAdapter(Context context, ArrayList<StrainDataModel.Images> mData) {
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
            View itemView = mLayoutInflater.inflate(R.layout.pager_layout, container, false);
            Init(itemView, position);
            Image_Counter.setText(position + 1 + "/" + mData.size());
            imageView = itemView.findViewById(R.id.img);
            Photo_Powered_By.setText(mData.get(position).getName() + "");
            Photo_Powered_By.setTextColor(getUserRatingColor(strainDataModel.getImages().get(position).getUser_rating()));
            Photo_Date.setText(DateConverter.getCustomDateString(mData.get(position).getUpdated_at()));
            Glide.with(mContext)
                    .load(images_baseurl + mData.get(position).getImage_path())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.image_plaecholder_bg)
                    .error(R.drawable.noimage)
                    .fitCenter()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("ready", model);
//                            imageView.setImageDrawable(resource);
//                            Complete_image.setImageDrawable(resource);

                            return false;
                        }
                    }).into(imageView);
            /*  new SweetAlertDialog(v.getContext(), SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("Download")
                            .setContentText("Save image locally.")
                            .setConfirmText("Okay")
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                    pDialog.setTitleText("Downloading..");
                                    pDialog.setCancelable(false);
                                    pDialog.show();
                                    int downloadId = PRDownloader.download(images_baseurl + mData.get(position).getImage_path(), FileUtils.getRootDirPath(mContext), mData.get(position).getImage_path().replace("/", "_"))
                                            .build()
                                            .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                                @Override
                                                public void onStartOrResume() {

                                                }
                                            })
                                            .setOnPauseListener(new OnPauseListener() {
                                                @Override
                                                public void onPause() {

                                                }
                                            })
                                            .setOnCancelListener(new OnCancelListener() {
                                                @Override
                                                public void onCancel() {

                                                }
                                            })
                                            .setOnProgressListener(new OnProgressListener() {
                                                @Override
                                                public void onProgress(Progress progress) {
                                                    pDialog.getProgressHelper().setProgress((float) (progress.currentBytes / progress.totalBytes));
                                                }
                                            })
                                            .start(new OnDownloadListener() {
                                                @Override
                                                public void onDownloadComplete() {
                                                    pDialog.dismissWithAnimation();
                                                }

                                                @Override
                                                public void onError(Error error) {
                                                    pDialog.dismissWithAnimation();
                                                }

                                            });
                                }
                            }).show();*/
            Photo_Powered_By.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int USerID = mData.get(position).getUser_id();
                    if (USerID != -1) {
                        GoToProfile(StrainImagesActivity.this, USerID);
                    }
                }
            });


            Flag_Strain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (strainDataModel.getImages().get(position).isIs_current_user_flaged()) {
//                        strainDataModel.getImages().get(position).setIs_current_user_flaged(false);
//                       JSONObject jsonObject = new JSONObject();
//                        try {
//                            jsonObject.put("strain_image_id", strainDataModel.getImages().get(position).getId());
//                            jsonObject.put("is_flagged", 0);
//                            jsonObject.put("reason", "");
//                            strainDataModel.getImages().get(position).setIs_current_user_flaged(false);
//                            new VollyAPICall(StrainImagesActivity.this, true, URL.save_user_image_strain_flag, jsonObject, user.getSession_key(), Request.Method.POST, StrainImagesActivity.this, APIActions.ApiActions.testAPI);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        CustomeToast.ShowCustomToast(StrainImagesActivity.this, "you already reported this photo!", Gravity.TOP);
                    } else {
                        ArrayList<ReportQuestionListDataModel> dataModels = new ArrayList<>();
                        dataModels.add(new ReportQuestionListDataModel("Nudity or sexual content", false));
                        dataModels.add(new ReportQuestionListDataModel("Harassment or hate speech", false));
                        dataModels.add(new ReportQuestionListDataModel("Threatening, violent, or concerning", false));
                        dataModels.add(new ReportQuestionListDataModel("Spam", false));
                        dataModels.add(new ReportQuestionListDataModel("Offensive", false));
                        dataModels.add(new ReportQuestionListDataModel("Unrelated", false));
                        strain_report.SlideDown(position, dataModels, StrainImagesActivity.this, "strain");
                    }
                }
            });

            Like_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StrainDetailsActivity.isDataUpdateFromImageActivity = true;
                    try {
                        JSONObject jsonObject = new JSONObject();
                        if (mData.get(position).isIs_current_user_liked()) {
                            mData.get(position).setIs_current_user_liked(false);
                            Like_Icon.setImageResource(R.drawable.ic_like_icon);
                            Like_Icon.setColorFilter(0);
                            jsonObject.put("is_like", 0);
                            Like_Count.setTextColor(Color.parseColor("#FFFFFF"));
                            mData.get(position).setLike_count(mData.get(position).getLike_count() - 1);

                        } else {
                            Like_Icon.setImageResource(R.drawable.ic_like_icon_liked);
                            Like_Icon.setColorFilter(Color.parseColor("#FEC14A"), PorterDuff.Mode.SRC_IN);
                            mData.get(position).setIs_current_user_liked(true);
                            jsonObject.put("is_like", 1);
                            Like_Count.setTextColor(Color.parseColor("#FEC14A"));
                            mData.get(position).setLike_count(mData.get(position).getLike_count() + 1);
                        }

                        if (mData.get(position).isIs_current_user_dislike()) {
                            mData.get(position).setIs_current_user_dislike(false);
                            Dislike_icon.setImageResource(R.drawable.ic_dislike_icon);
                            mData.get(position).setDis_like_count(mData.get(position).getDis_like_count() - 1);
                            Dislike_count.setTextColor(Color.parseColor("#FFFFFF"));
                            Dislike_count.setText(mData.get(position).getDis_like_count() + "");
                        }
                        Like_Count.setText(mData.get(position).getLike_count() + "");
                        jsonObject.put("strain_image_id", mData.get(position).getId());
                        new VollyAPICall(StrainImagesActivity.this, true, URL.save_user_image_strain_like, jsonObject, user.getSession_key(), Request.Method.POST, StrainImagesActivity.this, APIActions.ApiActions.testAPI);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            Dislike_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StrainDetailsActivity.isDataUpdateFromImageActivity = true;
                    try {
                        JSONObject jsonObject = new JSONObject();
                        if (mData.get(position).isIs_current_user_dislike()) {
                            mData.get(position).setIs_current_user_dislike(false);
                            Dislike_icon.setImageResource(R.drawable.ic_dislike_icon);
                            jsonObject.put("is_disliked", 0);
                            Dislike_count.setTextColor(Color.parseColor("#FFFFFF"));
                            Dislike_icon.setColorFilter(0);
                            mData.get(position).setDis_like_count(mData.get(position).getDis_like_count() - 1);

                        } else {
                            Dislike_icon.setImageResource(R.drawable.ic_dislike_icon_disliked);
                            Dislike_icon.setColorFilter(Color.parseColor("#FEC14A"), PorterDuff.Mode.SRC_IN);
                            mData.get(position).setIs_current_user_dislike(true);
                            jsonObject.put("is_disliked", 1);
                            Dislike_count.setTextColor(Color.parseColor("#FEC14A"));
                            mData.get(position).setDis_like_count(mData.get(position).getDis_like_count() + 1);
                        }

                        if (mData.get(position).isIs_current_user_liked()) {
                            mData.get(position).setIs_current_user_liked(false);
                            Like_Icon.setImageResource(R.drawable.ic_like_icon);
                            mData.get(position).setLike_count(mData.get(position).getLike_count() - 1);
                            Like_Count.setTextColor(Color.parseColor("#FFFFFF"));
                            Like_Count.setText(mData.get(position).getLike_count() + "");
                        }
                        Dislike_count.setText(mData.get(position).getDis_like_count() + "");
                        jsonObject.put("strain_image_id", mData.get(position).getId());
                        notifyDataSetChanged();
                        new VollyAPICall(StrainImagesActivity.this, true, URL.save_user_image_strain_dislike, jsonObject, user.getSession_key(), Request.Method.POST, StrainImagesActivity.this, APIActions.ApiActions.testAPI);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            Next_Img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((mViewPager.getCurrentItem() + 1) < mData.size()) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    } else {
                        mViewPager.setCurrentItem(0);
                    }
                }
            });

            Back_Img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((mViewPager.getCurrentItem() - 1) > 0) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                    } else {
                        mViewPager.setCurrentItem(mData.size() - 1);
                    }
                }
            });
            if (mData.size() > 1) {
                Next_Img.setVisibility(View.VISIBLE);
                Back_Img.setVisibility(View.VISIBLE);
            } else {
                Next_Img.setVisibility(View.GONE);
                Back_Img.setVisibility(View.GONE);
            }

            if (mData.get(position).isIs_current_user_flaged()) {
                Flag_Img.setImageResource(R.drawable.ic_flag_strain);
            } else {
                Flag_Img.setImageResource(R.drawable.ic_flag_white);
            }

            Like_Count.setText(mData.get(position).getLike_count() + "");

            Dislike_count.setText(mData.get(position).getDis_like_count() + "");

            if (!mData.get(position).isIs_current_user_liked()) {
                Like_Icon.setImageResource(R.drawable.ic_like_icon);
                Like_Count.setTextColor(Color.parseColor("#FFFFFF"));
                Like_Icon.setColorFilter(0);
            } else {
                Like_Icon.setImageResource(R.drawable.ic_like_icon_liked);
                Like_Icon.setColorFilter(Color.parseColor("#FEC14A"), PorterDuff.Mode.SRC_IN);
                Like_Count.setTextColor(Color.parseColor("#FEC14A"));
            }

            if (!mData.get(position).isIs_current_user_dislike()) {
                Dislike_icon.setImageResource(R.drawable.ic_dislike_icon);
                Dislike_icon.setColorFilter(0);
                Dislike_count.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                Dislike_icon.setImageResource(R.drawable.ic_dislike_icon_disliked);
                Dislike_icon.setColorFilter(Color.parseColor("#FEC14A"), PorterDuff.Mode.SRC_IN);
                Dislike_count.setTextColor(Color.parseColor("#FEC14A"));
            }

            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        public void Init(View view, final int position) {
            Image_Counter = view.findViewById(R.id.image_counter);
            Image_Counter.setText(1 + "/" + strainDataModel.getImages().size());
            Photo_Powered_By = view.findViewById(R.id.photo_powered_by);
            download_image = view.findViewById(R.id.download_image);
            Photo_Date = view.findViewById(R.id.photo_date);
            Like_btn = view.findViewById(R.id.like_strain);
            Dislike_btn = view.findViewById(R.id.dislike_strain);
            Flag_Img = view.findViewById(R.id.flag_img);
            Flag_Strain = view.findViewById(R.id.flag_strain);
            Flag_Img = view.findViewById(R.id.flag_img);
            Like_Count = view.findViewById(R.id.like_count);
            Dislike_count = view.findViewById(R.id.dislike_count);
            Like_Icon = view.findViewById(R.id.like_icon);
            Dislike_icon = view.findViewById(R.id.dislike_icon);
            Flag_Img = view.findViewById(R.id.flag_img);
            Back_Img = view.findViewById(R.id.back_img);
            Next_Img = view.findViewById(R.id.next_img);
            download_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    PermissionHandler.checkPermission(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, v.getContext(), new PermissionHandler.CheckPermissionResponse() {
                        @Override
                        public void permissionGranted() {
                            FileUtils.StartDownload(images_baseurl + mData.get(position).getImage_path(), mData.get(position).getImage_path(), v.getContext());
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
        }
    }
}
