package com.codingpixel.healingbudz.activity.home.side_menu.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.ScreenshotUtils;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.dialog.EditUserProfileUploadPhotoAlertDialog;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.activity.home.side_menu.profile.EditUserProfileActivity.drawableCover;
import static com.codingpixel.healingbudz.activity.home.side_menu.profile.EditUserProfileActivity.isCoverSet;
import static com.codingpixel.healingbudz.activity.home.side_menu.profile.UserProfileActivity.profiledataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.update_cover;
import static com.codingpixel.healingbudz.network.model.URL.update_cover_orignal_image;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class EditUserProfileUploadCoverPhotoActivity extends AppCompatActivity implements EditUserProfileUploadPhotoAlertDialog.OnDialogFragmentClickListener, APIResponseListner, View.OnTouchListener {
    private static final String TAG = "MAinCat.cds";
    ImageView Back;
    Button Save;
    PhotoViewAttacher mAttacher;
    ImageView Cover_Photo, Profile_Image, profile_img_topi;
    TextView User_Name;
    ProgressDialog pd;
    Drawable main_drawable;
    LinearLayout buder_infoo;
    RelativeLayout prfl_img;

    private static final float MIN_ZOOM = .5f;
    private static final float MAX_ZOOM = 2.0f;
    private float[] values;
    private float scale;
    TextView upload;
    private float newScale;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    private ImageView reticle;
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    RelativeLayout top_view;
    String savedItemClicked;

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
        setContentView(R.layout.activity_edit_user_profile_upload_cover_photo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
//        EditUserProfileUploadPhotoAlertDialog shootOutAlertDialog = EditUserProfileUploadPhotoAlertDialog.newInstance(EditUserProfileUploadCoverPhotoActivity.this, true);
//        shootOutAlertDialog.show(getSupportFragmentManager(), "dialog");
        Back = (ImageView) findViewById(R.id.back_btn);
        top_view = (RelativeLayout) findViewById(R.id.top_view);
        buder_infoo = findViewById(R.id.buder_infoo);
        prfl_img = findViewById(R.id.prfl_img);
        upload = findViewById(R.id.upload);
        upload.setClickable(false);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserProfileUploadCoverPhotoActivity.this, HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", false);
                startActivityForResult(intent, 1200);
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Save = (Button) findViewById(R.id.save_btn);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cover_Photo.setDrawingCacheEnabled(true);
                buder_infoo.setVisibility(View.GONE);
                prfl_img.setVisibility(View.GONE);
//                Cover_Photo.setImageBitmap(Constants.takeScreenShotView(mAttacher.getImageView(),mAttacher.getVisibleRectangleBitmap()));//; ;
//                Cover_Photo.setImageBitmap(Constants.SavePixels(top_view.getScrollX(),top_view.getScrollY(), Utility.getDeviceWidth(view.getContext()),290));//; ;
                UploadImageFull(Cover_Photo.getDrawable());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                        Bitmap bitmap = ScreenshotUtils.getScreenShot(top_view);
//                        Cover_Photo.setImageBitmap(bitmap);//; ;
//                        Cover_Photo.setDrawingCacheEnabled(true);
//
////                mAttacher = new PhotoViewAttacher(Cover_Photo);
////                mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
////                mAttacher.getVisibleRectangleBitmap();
////                mAttacher.update();
//
//                        UploadImage(Cover_Photo.getDrawable());
//                        buder_infoo.setVisibility(View.VISIBLE);
//                        prfl_img.setVisibility(View.VISIBLE);
                    }
                }, 10);

            }
        });
        Cover_Photo = (ImageView) findViewById(R.id.cover_photo);
        Cover_Photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (profiledataModel.getCover() != null && profiledataModel.getCover().length() > 0) {
            Glide.with(this)
                    .load(images_baseurl + profiledataModel.getCover())
                    .asBitmap()


//                .listener(new RequestListener<String, Bitmap>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//
//                        Cover_Photo.setImageBitmap(resource);
//                        mAttacher = new PhotoViewAttacher(Cover_Photo);
//                        mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                        mAttacher.getVisibleRectangleBitmap();
//                        mAttacher.update();
//                        return false;
//                    }
//                })
//                .into(Cover_Photo);
                    .into(new SimpleTarget<Bitmap>(1080, 1080) {
                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            upload.setClickable(true);
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            Drawable main_drawable = new BitmapDrawable(getResources(), resource);
                            upload.setClickable(true);
                            Cover_Photo.setImageDrawable(main_drawable);
                            mAttacher = new PhotoViewAttacher(Cover_Photo);
                            mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            mAttacher.getVisibleRectangleBitmap();
                            mAttacher.update();
                        }
                    });
        } else {
            upload.setClickable(true);
        }
        Profile_Image = (ImageView) findViewById(R.id.profile_photo);
        profile_img_topi = (ImageView) findViewById(R.id.profile_img_topi);
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
        User_Name = (TextView) findViewById(R.id.user_name);

        String Name = "";
        if (profiledataModel.getLast_name() != null && !profiledataModel.getLast_name().equalsIgnoreCase("null")) {
            Name = profiledataModel.getFirst_name() + " " + profiledataModel.getLast_name();
        } else {
            Name = profiledataModel.getFirst_name();
        }
        User_Name.setText(Name);
        User_Name.setTextColor(Color.parseColor(Utility.getBudColor(Splash.user.getPoints())));
    }

    public void SetPhot(String path, final ImageView imageView, int Placeholder, final boolean isBackground) {
        String imag_url = images_baseurl;
        if (user.getImage_path().contains("http")) {
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
                    }).into(1080, 1080);
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

    public void InitImage(ImageView v) {
        reticle = v;
        v.setOnTouchListener(this);
        mid = new PointF();
        start = new PointF();
        matrix = new Matrix();
        savedMatrix = new Matrix();
        values = new float[9];

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        ImageView view = (ImageView) v;
        dumpEvent(event);

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    Log.d("ss---pointx", (event.getX() - start.x) + "");
                    Log.d("sss--pointy", (event.getY() - start.y) + "");

                    Log.d("ss---startX", (start.x) + "");
                    Log.d("sss--StartY", (start.y) + "");

                    Log.d("ss---eventX", (event.getX() + ""));
                    Log.d("sss--evnetY", (event.getY() + ""));
                    float x = event.getX() - start.x;
                    float y = (event.getY() - start.y);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                }
//                } else if (mode == ZOOM) {
//                    float newDist = spacing(event);
//                    Log.d(TAG, "newDist=" + newDist);
//                    if (newDist > 10f) {
//                        matrix.set(savedMatrix);
//                        float scale = newDist / oldDist;
//                        matrix.postScale(scale, scale, mid.x, mid.y);
//                    }
//                }
                break;
        }
        Log.d("matrix_width", getWidthFromMatrix(matrix, Cover_Photo) + "");
        Log.d("matrix_height", getHeightFromMatrix(matrix, Cover_Photo) + "");

        view.setImageMatrix(matrix);
        return true;
    }

    private float getWidthFromMatrix(Matrix matrix, ImageView imageview) {

        float[] values = new float[9];
        matrix.getValues(values);

        float width = values[0] * imageview.getWidth();

        return width;
    }

    private float getHeightFromMatrix(Matrix matrix, ImageView imageview) {

        float[] values = new float[9];
        matrix.getValues(values);

        float height = values[4] * imageview.getHeight();

        return height;
    }


    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        Log.d(TAG, sb.toString());
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    public void onSaveButtonClick(EditUserProfileUploadPhotoAlertDialog dialog, Drawable drawable) {
        dialog.dismiss();
    }

    @Override
    public void onSaveButtonClick(EditUserProfileUploadPhotoAlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onTopiChanged(Drawable drawable) {

    }

    @Override
    public void onCrossButtonClick(EditUserProfileUploadPhotoAlertDialog dialog) {
        dialog.dismiss();
        finish();
    }

    @Override
    public void onUploadButtonClick(EditUserProfileUploadPhotoAlertDialog dialog) {
        dialog.dismiss();
        Intent intent = new Intent(EditUserProfileUploadCoverPhotoActivity.this, HBCameraActivity.class);
        intent.putExtra("isVideoCaptureAble", false);
        startActivityForResult(intent, 1200);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            buder_infoo.setVisibility(View.GONE);
            prfl_img.setVisibility(View.GONE);
            Log.d("paths", data.getExtras().getString("file_path_arg"));
            Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
            if (bitmapOrg != null) {
                bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
//                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 1400, 1400, false);
                main_drawable = new BitmapDrawable(getResources(), bitmapOrg);
                Cover_Photo.setImageDrawable(main_drawable);
                Cover_Photo.setBackground(null);
                mAttacher = new PhotoViewAttacher(Cover_Photo);
                mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mAttacher.getVisibleRectangleBitmap();
                mAttacher.update();
                Glide.with(this)
                        .load(data.getExtras().getString("file_path_arg"))
                        .asBitmap()
//                .listener(new RequestListener<String, Bitmap>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//
//                        Cover_Photo.setImageBitmap(resource);
//                        mAttacher = new PhotoViewAttacher(Cover_Photo);
//                        mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                        mAttacher.getVisibleRectangleBitmap();
//                        mAttacher.update();
//                        return false;
//                    }
//                })
//                .into(Cover_Photo);
                        .into(new SimpleTarget<Bitmap>(1080, 1080) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                Drawable main_drawable = new BitmapDrawable(getResources(), resource);
                                Cover_Photo.setImageDrawable(main_drawable);
                                mAttacher = new PhotoViewAttacher(Cover_Photo);
                                mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                mAttacher.getVisibleRectangleBitmap();
                                mAttacher.update();
                            }
                        });
            }

        }
    }

    public void UploadImage(Drawable drawable) {
//        pd = ProgressDialog.newInstance();
//        pd.show(((FragmentActivity) this).getSupportFragmentManager(), "pd");
        new UploadImageAPIcall(EditUserProfileUploadCoverPhotoActivity.this, update_cover, drawable, user.getSession_key(), EditUserProfileUploadCoverPhotoActivity.this, APIActions.ApiActions.add_media);
    }

    public void UploadImageFull(Drawable drawable) {
        pd = ProgressDialog.newInstance();
        pd.show(((FragmentActivity) this).getSupportFragmentManager(), "pd");
        new UploadImageAPIcall(EditUserProfileUploadCoverPhotoActivity.this, update_cover_orignal_image, drawable, user.getSession_key(), EditUserProfileUploadCoverPhotoActivity.this, APIActions.ApiActions.add_media_full);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == APIActions.ApiActions.add_media_full) {
            Bitmap bitmap = ScreenshotUtils.getScreenShot(top_view);
            Cover_Photo.setImageBitmap(bitmap);//; ;
            Cover_Photo.setDrawingCacheEnabled(true);
//                mAttacher = new PhotoViewAttacher(Cover_Photo);
//                mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                mAttacher.getVisibleRectangleBitmap();
//                mAttacher.update();
            UploadImage(Cover_Photo.getDrawable());
            buder_infoo.setVisibility(View.VISIBLE);
            prfl_img.setVisibility(View.VISIBLE);
        } else {
            try {
                pd.dismiss();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

            profiledataModel.setCover(response);
            isCoverSet = true;
            drawableCover = Cover_Photo.getDrawable();
            finish();
        }

    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }

}
