package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.FileUtils;
import com.codingpixel.healingbudz.Utilities.PermissionHandler;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.adapter.BudzMapImagesGalleryRecylerAdapter;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_file_with_progress.UploadFileWithProgress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity.GetRAtingImg;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity.main_scroll_view;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class BudzMapDetailsImagesActivity extends AppCompatActivity implements View.OnClickListener, BudzMapImagesGalleryRecylerAdapter.ItemClickListener, APIResponseListner {
    ImageView Back, Home;
    RecyclerView Images_Recyler_view;
    ImageView Image_Complete_View;
    boolean isImagePreview = false;
    LinearLayout Grid_layout;
    int slide_start_point = 0;
    ImageView Upload_image_button;
    ImageView Profile_img, Rating_img;
    TextView Main_Heading_title, Review_text;
    LinearLayout Delivery_layout, delivery_layout, Organic_Layout, ReviewLayout, image_showing_view;
    BudzMapImagesGalleryRecylerAdapter adapter;
    ViewPager mViewPager;
    ImagePaggerAdapter imagePaggerAdapter;

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
        setContentView(R.layout.activity_budz_map_details_images);
        ChangeStatusBarColor(BudzMapDetailsImagesActivity.this, "#171717");
        Back = (ImageView) findViewById(R.id.back_btn);
        Home = (ImageView) findViewById(R.id.home_btn);
        Back.setOnClickListener(this);
        Home.setOnClickListener(this);
        Images_Recyler_view = (RecyclerView) findViewById(R.id.images_gallery_recyler_view);
        Images_Recyler_view.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new BudzMapImagesGalleryRecylerAdapter(this, budz_map_item_clickerd_dataModel.getImages());
        adapter.setClickListener(this);
        Images_Recyler_view.setAdapter(adapter);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        imagePaggerAdapter = new ImagePaggerAdapter(this, budz_map_item_clickerd_dataModel.getImages());
        mViewPager.setAdapter(imagePaggerAdapter);
        Grid_layout = (LinearLayout) findViewById(R.id.grid_layout);
        Image_Complete_View = (ImageView) findViewById(R.id.image_showing_view_image);
        image_showing_view = (LinearLayout) findViewById(R.id.image_showing_view);
        Image_Complete_View.setVisibility(View.GONE);
        Upload_image_button = (ImageView) findViewById(R.id.upload_image_button);
        if (budz_map_item_clickerd_dataModel.getUser_id() == user.getUser_id()) {
            Upload_image_button.setVisibility(View.VISIBLE);
        } else {
            Upload_image_button.setVisibility(View.INVISIBLE);
        }
        Upload_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (budz_map_item_clickerd_dataModel.getUser_id() == user.getUser_id()) {
                    Intent intent = new Intent(BudzMapDetailsImagesActivity.this, HBCameraActivity.class);
                    intent.putExtra("isVideoCaptureAble", false);
                    startActivityForResult(intent, 1200);
                }
            }
        });
        setData();
    }

    public void setData() {
        ReviewLayout = (LinearLayout) findViewById(R.id.review_layout);
        ReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_scroll_view.smoothScrollTo(0, 4100);
                finish();
            }
        });
        Profile_img = (ImageView) findViewById(R.id.profile_image);
        Rating_img = (ImageView) findViewById(R.id.rating_img);
        Main_Heading_title = (TextView) findViewById(R.id.main_heading_title);
        Review_text = (TextView) findViewById(R.id.review_text);
        Delivery_layout = (LinearLayout) findViewById(R.id.deliver_layout);
        delivery_layout = (LinearLayout) findViewById(R.id.delivery_layout);
        Organic_Layout = (LinearLayout) findViewById(R.id.organic_layout);
        Glide.with(BudzMapDetailsImagesActivity.this)
                .load(images_baseurl + budz_map_item_clickerd_dataModel.getLogo())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_budz_adn)
                .error(R.drawable.ic_budz_adn)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Profile_img.setImageBitmap(resource);
                        return false;
                    }
                })
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
////                        Profile_img.setImageDrawable(resource);
//                        return false;
//                    }
//                })
                .into(Profile_img);

        Main_Heading_title.setText(budz_map_item_clickerd_dataModel.getTitle());
//        MakeKeywordClickableText(Main_Heading_title.getContext(), budz_map_item_clickerd_dataModel.getTitle(), Main_Heading_title);
        Rating_img.setImageResource(GetRAtingImg(budz_map_item_clickerd_dataModel.getRating_sum()));

        Review_text.setText(MessageFormat.format("{0} Reviews", budz_map_item_clickerd_dataModel.getReviews().size()));

        if (budz_map_item_clickerd_dataModel.getIs_organic() == 0 && budz_map_item_clickerd_dataModel.getIs_delivery() == 0) {
            Delivery_layout.setVisibility(View.INVISIBLE);
        }

        delivery_layout = (LinearLayout) findViewById(R.id.delivery_layout);
        Organic_Layout = (LinearLayout) findViewById(R.id.organic_layout);
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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                if (isImagePreview) {
                    isImagePreview = false;
                    Image_Complete_View.setVisibility(View.GONE);
                    image_showing_view.setVisibility(View.GONE);
                    Grid_layout.setVisibility(View.VISIBLE);
                } else {
                    finish();
                    main_scroll_view.fullScroll(View.FOCUS_UP);
                }
                break;
            case R.id.home_btn:
                GoToHome(BudzMapDetailsImagesActivity.this, true);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isImagePreview) {
            isImagePreview = false;
            Image_Complete_View.setVisibility(View.GONE);
            image_showing_view.setVisibility(View.GONE);
            Grid_layout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(View view, int position, Drawable drawable) {
        isImagePreview = true;
        ShowImage(position);
//        Image_Complete_View.setVisibility(View.VISIBLE);

        Grid_layout.setVisibility(View.GONE);
        isImagePreview = true;
        slide_start_point = position;
        ShowImage(position);
        imagePaggerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(position);
        Image_Complete_View.setVisibility(View.GONE);
        image_showing_view.setVisibility(View.VISIBLE);
        Grid_layout.setVisibility(View.GONE);
    }

    public void ShowImage(final int position) {
        Glide.with(BudzMapDetailsImagesActivity.this)
                .load(images_baseurl + budz_map_item_clickerd_dataModel.getImages().get(position).getImage_path())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.image_placholder_budz)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        Image_Complete_View.setImageDrawable(resource);
                        return false;
                    }
                }).into(Image_Complete_View);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
            bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
//            BudzMapHomeDataModel.Images img = new BudzMapHomeDataModel.Images();
//            img.setPathLocal(data.getExtras().getString("file_path_arg"));
//            img.setLocal(false);
//            budz_map_item_clickerd_dataModel.getImages().add(img);
//            slide_start_point = budz_map_item_clickerd_dataModel.getImages().size() - 1;
//            ShowImage(budz_map_item_clickerd_dataModel.getImages().size() - 1);
//            imagePaggerAdapter.notifyDataSetChanged();
//            adapter.notifyDataSetChanged();
//            mViewPager.setCurrentItem(budz_map_item_clickerd_dataModel.getImages().size() - 1);
//            bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
//            Drawable drawable = new BitmapDrawable(getResources(), bitmapOrg);
            UploadImage(data.getExtras().getString("file_path_arg"));
        }
    }

    public void UploadImage(String image) {
        JSONArray parameter = new JSONArray();
        parameter.put("id");
        JSONArray values = new JSONArray();
        values.put(budz_map_item_clickerd_dataModel.getId());
        new UploadFileWithProgress(this, true, URL.add_budz_image, image, "image", values, parameter, null, BudzMapDetailsImagesActivity.this, APIActions.ApiActions.upload_strain_image).execute();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        BudzMapHomeDataModel.Images img = new BudzMapHomeDataModel.Images();
        try {
            JSONObject image_object = new JSONObject(response).getJSONObject("successData");
            img.setId(image_object.getInt("id"));
            img.setUser_id(image_object.getInt("user_id"));
            img.setImage_path(image_object.getString("image"));
            img.setIs_approved(0);
            img.setIs_main(0);
            img.setCreated_at(image_object.getString("created_at"));
            img.setUpdated_at(image_object.getString("updated_at"));
            List<BudzMapHomeDataModel.Images> images = budz_map_item_clickerd_dataModel.getImages();
            images.add(img);
            budz_map_item_clickerd_dataModel.setImages((ArrayList<BudzMapHomeDataModel.Images>) images);
            adapter.notifyDataSetChanged();
            CustomeToast.ShowCustomToast(BudzMapDetailsImagesActivity.this, "Upload Successfully!", Gravity.TOP);
            if (isImagePreview) {
                slide_start_point = budz_map_item_clickerd_dataModel.getImages().size() - 1;
                ShowImage(budz_map_item_clickerd_dataModel.getImages().size() - 1);
                imagePaggerAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(budz_map_item_clickerd_dataModel.getImages().size() - 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(BudzMapDetailsImagesActivity.this, object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ImagePaggerAdapter extends PagerAdapter {
        TextView Photo_Powered_By, Photo_Date, Like_Count;
        LinearLayout Like_btn, Dislike_btn, Flag_Strain;
        ImageView Flag_Img, Like_Icon, Dislike_icon, Back_Img, Next_Img, download_image;
        ;
        TextView Image_Counter, Dislike_count;
        Context mContext;
        ArrayList<BudzMapHomeDataModel.Images> mData = new ArrayList<>();
        LayoutInflater mLayoutInflater;

        public ImagePaggerAdapter(Context context, ArrayList<BudzMapHomeDataModel.Images> mData) {
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
            View itemView = mLayoutInflater.inflate(R.layout.pager_layout_budz, container, false);
            Init(itemView);
            Image_Counter.setText(position + 1 + "/" + mData.size());
            final com.codingpixel.healingbudz.customeUI.TouchImageView imageView = itemView.findViewById(R.id.img);
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
            if (mData.get(position).isLocal()) {
                Glide.with(BudzMapDetailsImagesActivity.this)
                        .load(mData.get(position).getPathLocal())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.image_placholder_budz)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Log.d("ready", model);
//                                imageView.setImageDrawable(resource);
//                            Complete_image.setBackground(resource);
//                            Photo_Powered_By.setText(mData.get(position).getName() + "");
//                            Photo_Powered_By.setTextColor(getUserRatingColor(budz_map_item_clickerd_dataModel.getImages().get(position).getUser_rating()));
//                            Photo_Date.setText(DateConverter.getCustomDateString(mData.get(position).getUpdated_at()));
                                return false;
                            }
                        }).into(imageView);
            } else {
                Glide.with(BudzMapDetailsImagesActivity.this)
                        .load(images_baseurl + mData.get(position).getImage_path())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.image_placholder_budz)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Log.d("ready", model);
//                                imageView.setImageDrawable(resource);
//                            Complete_image.setBackground(resource);
//                            Photo_Powered_By.setText(mData.get(position).getName() + "");
//                            Photo_Powered_By.setTextColor(getUserRatingColor(budz_map_item_clickerd_dataModel.getImages().get(position).getUser_rating()));
//                            Photo_Date.setText(DateConverter.getCustomDateString(mData.get(position).getUpdated_at()));
                                return false;
                            }
                        }).into(imageView);
            }

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
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        public void Init(View view) {
            Image_Counter = view.findViewById(R.id.image_counter);
            download_image = view.findViewById(R.id.download_image);
            Image_Counter.setText(1 + "/" + budz_map_item_clickerd_dataModel.getImages().size());
            Photo_Powered_By = view.findViewById(R.id.photo_powered_by);
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

        }
    }
}
