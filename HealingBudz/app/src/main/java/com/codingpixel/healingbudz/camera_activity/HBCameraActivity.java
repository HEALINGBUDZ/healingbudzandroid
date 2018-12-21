package com.codingpixel.healingbudz.camera_activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.FileUtils;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.Frame;
import com.otaliastudios.cameraview.FrameProcessor;
import com.otaliastudios.cameraview.SessionType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;

public class HBCameraActivity extends AppCompatActivity implements ControlView.Callback {
    boolean isPhotoBtnClick = true;
    boolean isVideoBtnClick = false;
    @BindView(R.id.controls)
    ViewGroup controlPanel;
    File file;
    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final int REQUEST_PREVIEW_CODE = 1001;
    public static final int ACTION_CONFIRM = 900;
    public static final int ACTION_RETAKE = 901;
    public static final int ACTION_CANCEL = 902;
    private final static String MEDIA_ACTION_ARG = "media_action_arg";
    private final static String FILE_PATH_ARG = "file_path_arg";
    private final static String RESPONSE_CODE_ARG = "response_code_arg";
    private final static String VIDEO_POSITION_ARG = "current_video_position";
    private final static String VIDEO_IS_PLAYED_ARG = "is_played";
    private final static String MIME_TYPE_VIDEO = "video";
    private final static String MIME_TYPE_IMAGE = "image";
    public static final String FRAGMENT_TAG = "camera";
    private String previewFilePath;
    String video_path = "";
    boolean is_video_process = false;
    boolean isVideoCaptureAble = true;

    //Muzammil android branch
    private int type = 0;
    //Muzammil android branch
    @BindView(R.id.camera_view)
    com.otaliastudios.cameraview.CameraView camera;
    @BindView(R.id.flash_switch_view)
    ImageView flashSwitchView;
    @BindView(R.id.front_back_camera_switcher)
    ImageView cameraSwitchView;
    @BindView(R.id.record_button)
    Button recordButton;

    RelativeLayout video_button_layout;

    @BindView(R.id.video_duration_text)
    TextView recordDurationText;
    @BindView(R.id.text_top)
    TextView text_top;
    CountDownTimer countDownTimer;

    public static File getSavePath() {
        File path;
        if (hasSDCard()) { // SD card
            path = new File(getSDCardPath() + "/HB_Camera/");
            path.mkdir();
        } else {
            path = Environment.getDataDirectory();
        }
        return path;
    }

    public static String getCacheFilename(String name) {
        File f = getSavePath();
        return f.getAbsolutePath() + "/" + name;
    }


    public static void saveToFile(String filename, Bitmap bmp) {
        try {
            FileOutputStream out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }

    public static boolean hasSDCard() { // SD????????
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    public static String getSDCardPath() {
        File path = Environment.getExternalStorageDirectory();
        return path.getAbsolutePath();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healing_bud_camera_demo);
        ButterKnife.bind(this);

        if (getIntent().getExtras() == null) {
            HBCameraActivity.this.finish();
            return;
        }
        video_button_layout = (RelativeLayout) findViewById(R.id.video_button);
        FullScreen(this);
        isVideoCaptureAble = getIntent().getExtras().getBoolean("isVideoCaptureAble");
        if (isVideoCaptureAble) {
            video_button_layout.setVisibility(View.VISIBLE);
        } else {
            video_button_layout.setVisibility(View.GONE);
        }

        //Muzammil android branch
        type = getIntent().getExtras().getInt(Constants.CAMERA_ONLY_VIDEO, 0);

        //  type 1 => photo only : type 2 => video only
        if (type == 2) {
            findViewById(R.id.bottom_tabbb).setVisibility(View.GONE);
            take_video_btn.performClick();
            //OnTakeVideoClick();
        } else if (type == 1) {
            findViewById(R.id.bottom_tabbb).setVisibility(View.VISIBLE);
            video_button_layout.setVisibility(View.GONE);
            take_photo_btn.performClick();
        }
        //Muzammil android branch
//

        if (Build.VERSION.SDK_INT > 21) {
            final String[] permissions = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};

            final List<String> permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
            } else {
                newCamera();
//                addCamera();
            }
            newCamera();
        } else {
//            addCamera();
        }
    }

    ProgressDialog progressDialog;

    @OnClick(R.id.cross)
    void CrossActivity() {
        finish();
    }

    private class SavePhotoTask extends AsyncTask<byte[], String, String> {
        String path = "";


        public SavePhotoTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.Show();

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.Dismis();
            super.onPostExecute(s);
            try {
                File abc = new File(path);
                abc = new Compressor(HBCameraActivity.this).setQuality(75).compressToFile(abc);
                double bytes = abc.length();
                double kilobytes = (bytes / 1024);
                double megabytes = (kilobytes / 1024);
                double gigabytes = (megabytes / 1024);
                double terabytes = (gigabytes / 1024);
                double petabytes = (terabytes / 1024);
                double exabytes = (petabytes / 1024);
                double zettabytes = (exabytes / 1024);
                double yottabytes = (zettabytes / 1024);
//                if (megabytes > 1.0) {
//                    abc = new Compressor(HBCameraActivity.this).setQuality(75).compressToFile(abc);
//                    Intent resultIntent = new Intent();
//                    resultIntent.putExtra("isVideo", false).
//                            putExtra("camera_video", false).
//                            putExtra(RESPONSE_CODE_ARG, ACTION_CONFIRM).
//                            putExtra(FILE_PATH_ARG, abc.getAbsolutePath());
//                    setResult(RESULT_OK, resultIntent);
//                    finish();
//                } else {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("isVideo", false).
                        putExtra("camera_video", false).
                        putExtra(RESPONSE_CODE_ARG, ACTION_CONFIRM).
                        putExtra(FILE_PATH_ARG, abc.getAbsolutePath());
                setResult(RESULT_OK, resultIntent);
                finish();
//                }

            } catch (IOException e) {
                e.printStackTrace();
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("isVideo", false).
//                        putExtra("camera_video", false).
//                        putExtra(RESPONSE_CODE_ARG, ACTION_CONFIRM).
//                        putExtra(FILE_PATH_ARG, abc.getAbsolutePath());
//                setResult(RESULT_OK, resultIntent);
                finish();
            }
//            pd.dismiss();

        }

        @Override
        protected String doInBackground(byte[]... jpeg) {
            File photo = new File(getCacheFilename("/IMG_" + Utility.getImageTimeStamp() + ".jpg"));
            path = photo.getAbsolutePath();

            if (photo.exists()) {
                photo.delete();
            }

            try {
                FileOutputStream fos = new FileOutputStream(photo.getPath());

                fos.write(jpeg[0]);
                fos.close();
            } catch (java.io.IOException e) {
                Log.e("PictureDemo", "Exception in photoCallback", e);
            }

            return (null);
        }

    }

    //    new SavePhotoTask().execute(jpeg);
    public void newCamera() {
        camera.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {

            }

        });

        camera.addCameraListener(new CameraListener() {
            @Override
            public void onCameraOpened(CameraOptions options) {
                super.onCameraOpened(options);
                onOpened();
            }

            @Override
            public void onCameraClosed() {
                super.onCameraClosed();
            }

            @Override
            public void onCameraError(@NonNull CameraException exception) {
                super.onCameraError(exception);
            }

            @Override
            public void onPictureTaken(byte[] jpeg) {
                super.onPictureTaken(jpeg);
                progressDialog = new ProgressDialog(HBCameraActivity.this);
//                final ProgressDialog pd = ProgressDialog.newInstance();
//                pd.show(HBCameraActivity.this.getSupportFragmentManager(), "pd");
//                CameraUtils.decodeBitmap(jpeg, new CameraUtils.BitmapCallback() {
//                    @Override
//                    public void onBitmapReady(Bitmap bitmap) {
//                        String pathNameFile;
//                        pathNameFile = getCacheFilename("/IMG_" + Utility.getImageTimeStamp() + ".jpg");
//                        saveToFile(pathNameFile, bitmap);
//                        pd.dismiss();
//                        Intent resultIntent = new Intent();
//                        resultIntent.putExtra("isVideo", false).
//                                putExtra("camera_video", false).
//                                putExtra(RESPONSE_CODE_ARG, ACTION_CONFIRM).
//                                putExtra(FILE_PATH_ARG, pathNameFile);
//
//                        setResult(RESULT_OK, resultIntent);
//                        finish();
//                    }
//                });
                new SavePhotoTask().execute(jpeg);
            }

            @Override
            public void onVideoTaken(File video) {
                super.onVideoTaken(video);
                Intent resultIntent = new Intent();
                String path = FileUtils.getPath(HBCameraActivity.this, Uri.fromFile(video));
                countDownTimer.cancel();
                recordDurationText.setText("");
                take_video_btn.performClick();
                double bytes = video.length();
                double kilobytes = (bytes / 1024);
                double megabytes = (kilobytes / 1024);
                double gigabytes = (megabytes / 1024);
                double terabytes = (gigabytes / 1024);
                double petabytes = (terabytes / 1024);
                double exabytes = (petabytes / 1024);
                double zettabytes = (exabytes / 1024);
                double yottabytes = (zettabytes / 1024);
                if (megabytes > 25D) {
                    new SweetAlertDialog(HBCameraActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Size Exceed!")
                            .setContentText("Selected file is exceed from 25 MB.")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();

                                }
                            }).show();
                } else {
                    resultIntent.putExtra("isVideo", true)
                            .putExtra("camera_video", false)
                            .putExtra(RESPONSE_CODE_ARG, ACTION_CONFIRM)
                            .putExtra(FILE_PATH_ARG, video.getAbsolutePath());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }

            @Override
            public void onOrientationChanged(int orientation) {
                super.onOrientationChanged(orientation);
            }

            @Override
            public void onFocusStart(PointF point) {
                super.onFocusStart(point);
            }

            @Override
            public void onFocusEnd(boolean successful, PointF point) {
                super.onFocusEnd(successful, point);
            }

            @Override
            public void onZoomChanged(float newValue, float[] bounds, PointF[] fingers) {
                super.onZoomChanged(newValue, bounds, fingers);
            }

            @Override
            public void onExposureCorrectionChanged(float newValue, float[] bounds, PointF[] fingers) {
                super.onExposureCorrectionChanged(newValue, bounds, fingers);
            }
        });

        ViewGroup group = (ViewGroup) controlPanel.getChildAt(0);
        Control[] controls = Control.values();
        for (Control control : controls) {
            ControlView view = new ControlView(this, control, this);
            group.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        controlPanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
                b.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    private void onOpened() {
        ViewGroup group = (ViewGroup) controlPanel.getChildAt(0);
        for (int i = 0; i < group.getChildCount(); i++) {
            ControlView view = (ControlView) group.getChildAt(i);
            view.onCameraOpened(camera);
        }

    }

    @BindView(R.id.one_indicator)
    View indicator_one;

    @BindView(R.id.two_indicator)
    View indicator_two;

    @BindView(R.id.three_indicator)
    View indicator_three;


    @BindView(R.id.gallery_img)
    Button gallery_btn;
    boolean isGalleryClick = false;

    @OnClick(R.id.gallery_img)
    public void OnGalleryClick() {
//        indicator_one.setVisibility(View.VISIBLE);
//        indicator_two.setVisibility(View.GONE);
//        indicator_three.setVisibility(View.GONE);
//        gallery_btn.setTextColor(Color.parseColor("#282828"));
//        take_photo_btn.setTextColor(Color.parseColor("#9a9a9a"));
//        take_video_btn.setTextColor(Color.parseColor("#9a9a9a"));
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (isVideoCaptureAble) {
                intent.setType("image/* video/*");
            } else {
                intent.setType("image/*");
            }
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 7678);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            if (isVideoCaptureAble) {
                intent.setType("image/* video/*");
            } else {
                intent.setType("image/*");
            }
            if (isVideoCaptureAble) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
            } else {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
            }

            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 7678);
        }
//        Intent intent = new Intent();
//
//        if (isVideoCaptureAble) {
//            intent.setType("image/* video/*");
//        } else {
//            intent.setType("image/*");
//        }
//        intent.setAction(Intent.ACTION_PICK);
//        //Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 7676);
    }

    @BindView(R.id.take_video)
    Button take_video_btn;

    @OnClick(R.id.take_video)
    public void OnTakeVideoClick() {
        if (!isVideoBtnClick) {
            isGalleryClick = false;
            isPhotoBtnClick = false;
            isVideoBtnClick = true;
            indicator_one.setVisibility(View.GONE);
            indicator_two.setVisibility(View.GONE);
            indicator_three.setVisibility(View.VISIBLE);
            cameraSwitchView.setVisibility(View.VISIBLE);
            gallery_btn.setTextColor(Color.parseColor("#9a9a9a"));
            take_photo_btn.setTextColor(Color.parseColor("#9a9a9a"));
            take_video_btn.setTextColor(Color.parseColor("#282828"));
            flashSwitchView.setVisibility(View.GONE);
            recordButton.setBackgroundResource(R.drawable.ic_start_recording);
            recordDurationText.setVisibility(View.VISIBLE);
            camera.setSessionType(SessionType.VIDEO);
            text_top.setText("Video");
//            final CameraFragmentApi cameraFragment = getCameraFragment();
//            if (cameraFragment != null) {
//                cameraFragment.switchActionPhotoVideo();
//            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.destroy();
        if (progressDialog != null) {
            if (progressDialog.isVisible()) {
                progressDialog.Dismis();
                progressDialog = null;
            }
        }
    }

    @BindView(R.id.take_photo)
    Button take_photo_btn;

    @OnClick(R.id.take_photo)
    public void OnTakePhotoClick() {
        if (!isPhotoBtnClick) {
            isGalleryClick = false;
            isVideoBtnClick = false;
            isPhotoBtnClick = true;
            camera.setSessionType(SessionType.PICTURE);
            indicator_one.setVisibility(View.GONE);
            indicator_two.setVisibility(View.VISIBLE);
            indicator_three.setVisibility(View.GONE);
            flashSwitchView.setVisibility(View.VISIBLE);
            recordButton.setBackgroundResource(R.drawable.camera_capture);
            gallery_btn.setTextColor(Color.parseColor("#9a9a9a"));
            take_photo_btn.setTextColor(Color.parseColor("#282828"));
            take_video_btn.setTextColor(Color.parseColor("#9a9a9a"));
            recordDurationText.setVisibility(View.GONE);
            text_top.setText("Photo");
//            final CameraFragmentApi cameraFragment = getCameraFragment();
//            if (cameraFragment != null) {
//                cameraFragment.switchActionPhotoVideo();
//            }
        }
    }

    @OnClick(R.id.flash_switch_view)
    public void onFlashSwitcClicked() {
//        .toggleFacing();
        if (camera.getFlash() == Flash.ON) {
            camera.setFlash(Flash.OFF);
            flashSwitchView.setImageResource(R.drawable.ic_camera_flash_off);
        } else if (camera.getFlash() == Flash.OFF) {

            camera.setFlash(Flash.AUTO);
            flashSwitchView.setImageResource(R.drawable.ic_flash_camera_auto);
        } else {
            flashSwitchView.setImageResource(R.drawable.ic_camera_flash);
            camera.setFlash(Flash.ON);
        }


//        final CameraFragmentApi cameraFragment = getCameraFragment();
//        if (cameraFragment != null) {
//            cameraFragment.toggleFlashMode();
//        }
    }

    @OnClick(R.id.front_back_camera_switcher)
    public void onSwitchCameraClicked() {
        camera.toggleFacing();
//        final CameraFragmentApi cameraFragment = getCameraFragment();
//        if (cameraFragment != null) {
//            cameraFragment.switchCameraTypeFrontBack();
//        }
    }

    @OnClick(R.id.record_button)
    public void onRecordButtonClicked() {
        FinializeVideoOrImage();
    }

    public void FinializeVideoOrImage() {
        if (isPhotoBtnClick) {
//            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +);
            camera.capturePicture();
//        camera.startR
        } else if (isVideoBtnClick) {
            if (camera.isCapturingVideo()) {
                take_photo_btn.setEnabled(true);
                take_video_btn.setEnabled(true);
                gallery_btn.setEnabled(true);
                take_video_btn.setClickable(true);
                take_video_btn.setClickable(true);
                gallery_btn.setClickable(true);

                camera.stopCapturingVideo();
            } else {
                if (hasSDCard()) { // SD card
                    file = new File(getSDCardPath() + "/HB_CAMERA/");
                    file.mkdir();
                    file = new File(file.getAbsolutePath() + "VID_" + Utility.getImageTimeStamp() + ".mp4");
                } else {
                    file = Environment.getDataDirectory();
                }
                cameraSwitchView.setVisibility(View.GONE);
                camera.setVideoMaxDuration(20000);
                camera.startCapturingVideo(file);
                take_photo_btn.setEnabled(false);
                take_video_btn.setEnabled(false);
                gallery_btn.setEnabled(false);
                take_video_btn.setClickable(false);
                take_video_btn.setClickable(false);
                gallery_btn.setClickable(false);
                recordButton.setBackgroundResource(R.drawable.ic_stop_recording);


                countDownTimer = new CountDownTimer(21000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        long millis = millisUntilFinished;
                        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                        recordDurationText.setText(hms);
                    }

                    public void onFinish() {
                        recordDurationText.setText("00:00");
                    }
                }.start();
            }
        }

    }

    private void message(String content, boolean important) {
        int length = important ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        CustomeToast.ShowCustomToast(this, content, Gravity.TOP);
//        Toast.makeText(this, content, length).show();
    }

    private void edit() {
        BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
        b.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.settings_view)
    public void onSettingsClicked() {
        edit();
//        camera.st
//        final CameraFragmentApi cameraFragment = getCameraFragment();
//        if (cameraFragment != null) {
//            cameraFragment.openSettingDialog();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                finish();
                return;
            }
//            addCamera();
            newCamera();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 7678) {
                if (data != null) {
                    Uri selectedUri = data.getData();
                    String path = FileUtils.getPath(this, selectedUri);

                    File file = new File(path);

                    if (file.exists()) {

                        double bytes = file.length();
                        double kilobytes = (bytes / 1024);
                        double megabytes = (kilobytes / 1024);
                        double gigabytes = (megabytes / 1024);
                        double terabytes = (gigabytes / 1024);
                        double petabytes = (terabytes / 1024);
                        double exabytes = (petabytes / 1024);
                        double zettabytes = (exabytes / 1024);
                        double yottabytes = (zettabytes / 1024);
                        if (megabytes > 25D) {
                            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Size Exceed!")
                                    .setContentText("Selected file is exceed from 25 MB.")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();

                                        }
                                    }).show();
                        } else {
                            String[] columns = {MediaStore.Images.Media.DATA,
                                    MediaStore.Images.Media.MIME_TYPE};


                            Cursor cursor = getContentResolver().query(selectedUri, columns, null, null, null);
                            cursor.moveToFirst();
                            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            String result = cursor.getString(idx);
                            int pathColumnIndex = cursor.getColumnIndex(columns[0]);
                            int mimeTypeColumnIndex = cursor.getColumnIndex(columns[1]);

                            String contentPath = cursor.getString(pathColumnIndex);

                            String mimeType = cursor.getString(mimeTypeColumnIndex);
                            cursor.close();
                            InputStream inputStream = null;
                            try {
                                inputStream = getBaseContext().getContentResolver().openInputStream(selectedUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap bm = BitmapFactory.decodeStream(inputStream);

//                    String pp =
                            Intent resultIntent = new Intent();
                            if (mimeType.startsWith("image")) {
                                resultIntent.putExtra("isVideo", false).putExtra("camera_video", false).putExtra(RESPONSE_CODE_ARG, ACTION_CONFIRM).putExtra(FILE_PATH_ARG, path);
                            } else if (mimeType.startsWith("video")) {
                                resultIntent.putExtra("isVideo", true).putExtra("camera_video", false).putExtra(RESPONSE_CODE_ARG, ACTION_CONFIRM).putExtra(FILE_PATH_ARG, path);
                            }
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }

                    } else {
                        System.out.println("File does not exists!");
                    }

                }

            } else {
                Uri selectedImage = data.getData();
                String[] filePathColumn;
                filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Intent resultIntent = new Intent();
                if (selectedImage.toString().contains("video")) {
                    resultIntent.putExtra("isVideo", true).putExtra("camera_video", false).putExtra(RESPONSE_CODE_ARG, ACTION_CONFIRM).putExtra(FILE_PATH_ARG, picturePath);
                } else {
                    resultIntent.putExtra("isVideo", false).putExtra("camera_video", false).putExtra(RESPONSE_CODE_ARG, ACTION_CONFIRM).putExtra(FILE_PATH_ARG, picturePath);
                }
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }
    }

    @Override
    public boolean onValueChanged(Control control, Object value, String name) {
        if (!camera.isHardwareAccelerated() && (control == Control.WIDTH || control == Control.HEIGHT)) {
            if ((Integer) value > 0) {
                message("This device does not support hardware acceleration. " +
                        "In this case you can not change width or height. " +
                        "The view will act as WRAP_CONTENT by default.", true);
                return false;
            }
        }
        control.applyValue(camera, value);
        BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
        b.setState(BottomSheetBehavior.STATE_HIDDEN);
        message("Changed " + control.getName() + " to " + name, false);
        return true;
    }

    @Override
    public void onBackPressed() {
        BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
        if (b.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            b.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }
        super.onBackPressed();
    }
}