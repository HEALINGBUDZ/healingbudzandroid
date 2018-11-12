package com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.fragment_back_button_listner.BackButtonClickListner;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;

public class ReplyQuestionFragment extends Fragment implements View.OnClickListener, BackButtonClickListner {
    ImageView attachment_one_img, attachment_two_img, attachment_three_img;
    ImageView attachment_one_video, attachment_two_video, attachment_three_video;
    ImageView attachment_one_cross, attachment_two_cross, attachment_three_cross;
    RelativeLayout attachment_one, attachment_two, attachment_three;
    Button Add_Media;
    int attachment_count = 0;
    Button Answer_Your_BUD;
    boolean isImageClick = false ;
    boolean[] AttachmentAdded = new boolean[3];
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reply_question_fragment_layout, container, false);
//        InitFragmentBackbtnListner(getActivity() , this);
        Init(view);
        return view;
    }

    public void Init(View view){
        Answer_Your_BUD = view.findViewById(R.id.answer_your_bud);
        Answer_Your_BUD.setOnClickListener(this);
        AttachmentAdded[0] = true;
        AttachmentAdded[1] = true;
        AttachmentAdded[2] = true;
        Add_Media = view.findViewById(R.id.add_attachment);
        Add_Media.setOnClickListener(this);

        InitAttachments(view);
        Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.test_img);
        Bitmap bitmapOrg = ((BitmapDrawable)drawable).getBitmap();
        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
        Drawable d = new BitmapDrawable(getContext().getResources(), bitmap);
        attachment_one_img.setBackground(d);
        attachment_two_img.setBackground(d);
        attachment_three_img.setBackground(d);
        attachment_count = 3;
    }

    public void InitAttachments(View view) {
        attachment_one = view.findViewById(R.id.attachment_one);
        attachment_two = view.findViewById(R.id.attachment_two);
        attachment_three = view.findViewById(R.id.attachment_three);

        attachment_one_img = view.findViewById(R.id.attachment_one_image);
        attachment_two_img = view.findViewById(R.id.attachment_two_image);
        attachment_three_img = view.findViewById(R.id.attachment_three_image);

        attachment_one_video = view.findViewById(R.id.attachment_one_video);
        attachment_two_video = view.findViewById(R.id.attachment_two_video);
        attachment_three_video = view.findViewById(R.id.attachment_three_video);

        attachment_one_cross = view.findViewById(R.id.attachment_one_cross);
        attachment_one_cross.setOnClickListener(this);
        attachment_two_cross = view.findViewById(R.id.attachment_two_cross);
        attachment_two_cross.setOnClickListener(this);
        attachment_three_cross = view.findViewById(R.id.attachment_three_cross);
        attachment_three_cross.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_attachment:
                isImageClick = true ;

                if (Build.VERSION.SDK_INT > 15) {
                    final String[] permissions = {
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE};
                    final List<String> permissionsToRequest = new ArrayList<>();
                    for (String permission : permissions) {
                        if (ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                            permissionsToRequest.add(permission);
                        }
                    }
                    if (!permissionsToRequest.isEmpty()) {
                        requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 12);
//                        ActivityCompat.requestPermissions(getActivity(), permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 12);
                    }else {
                        if (attachment_count < 3) {
                            Intent intent = new Intent(getContext(), HBCameraActivity.class);
                            intent.putExtra("isVideoCaptureAble" , true);
                            startActivityForResult(intent, 1200);
                        } else {
                            Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shaking);
                            attachment_one.startAnimation(startAnimation);
                            attachment_two.startAnimation(startAnimation);
                            attachment_three.startAnimation(startAnimation);
                        }
                    }
                } else {
                    if (attachment_count < 3) {
                        Intent intent = new Intent(getContext(), HBCameraActivity.class);
                        intent.putExtra("isVideoCaptureAble" , true);
                        startActivityForResult(intent, 1200);
                    } else {
                        Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shaking);
                        attachment_one.startAnimation(startAnimation);
                        attachment_two.startAnimation(startAnimation);
                        attachment_three.startAnimation(startAnimation);
                    }
                }
                break;
            case R.id.attachment_one_cross:
                attachment_one.setVisibility(View.GONE);
                AttachmentAdded[0] = false;
                attachment_count--;
                break;
            case R.id.attachment_two_cross:
                attachment_two.setVisibility(View.GONE);
                AttachmentAdded[1] = false;
                attachment_count--;
                break;
            case R.id.attachment_three_cross:
                attachment_three.setVisibility(View.GONE);
                AttachmentAdded[2] = false;
                attachment_count--;
                break;
            case R.id.answer_your_bud:


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        attachment_count = 0 ;
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                        transaction.remove(ReplyQuestionFragment.this);
                        transaction.commitAllowingStateLoss();
                    }
                });
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, 7677);
            } else if (requestCode == 12) {
                if (attachment_count < 3) {
                    Intent intent = new Intent(getContext(), HBCameraActivity.class);
                    intent.putExtra("isVideoCaptureAble" , true);
                    startActivityForResult(intent, 1200);
                } else {
                    Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shaking);
                    attachment_one.startAnimation(startAnimation);
                    attachment_two.startAnimation(startAnimation);
                    attachment_three.startAnimation(startAnimation);
                }
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, 7677);
            } else if (requestCode == 1001) {
//                getCameraCall();
            } else if (requestCode == 1002) {
//                settingFregment.setCamera();


            }
        }
    }

    public void SetPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            Log.d("paths", data.getExtras().getString("file_path_arg"));
            if(data.getExtras().getBoolean("isVideo")){
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        String filePath = data.getExtras().getString("file_path_arg");
                        Bitmap video_thumbnil = ThumbnailUtils.createVideoThumbnail(data.getExtras().getString("file_path_arg"), MediaStore.Video.Thumbnails.MINI_KIND);
                        Drawable drawable = null;
                        if(video_thumbnil != null){
                            video_thumbnil =   Bitmap.createScaledBitmap(video_thumbnil, 300, 300, false);
                            int corner_radious = (video_thumbnil.getWidth() * 10) / 100;
                            Bitmap bitmap = getRoundedCornerBitmap(video_thumbnil, corner_radious);
                            drawable = new BitmapDrawable(getResources(), bitmap);
                        }else {
                            Drawable d = ContextCompat.getDrawable(getContext(),R.drawable.test_img);
                            Bitmap bitmapOrg = ((BitmapDrawable)d).getBitmap();
                            bitmapOrg =   Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                            int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                            Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                            drawable = new BitmapDrawable(getContext().getResources(), bitmap);
                        }

                        if (!AttachmentAdded[0]) {
                            attachment_one_video.setVisibility(View.VISIBLE);
                            attachment_one.setVisibility(View.VISIBLE);
                            attachment_one_img.setBackground(drawable);
                            AttachmentAdded[attachment_count] = true;
                            attachment_count++;
                        } else if (!AttachmentAdded[1]) {
                            attachment_two_video.setVisibility(View.VISIBLE);
                            attachment_two.setVisibility(View.VISIBLE);
                            attachment_two_img.setBackground(drawable);
                            AttachmentAdded[attachment_count] = true;
                            attachment_count++;
                        } else if (!AttachmentAdded[2]) {
                            attachment_three_video.setVisibility(View.VISIBLE);
                            attachment_three.setVisibility(View.VISIBLE);
                            attachment_three_img.setBackground(drawable);
                            AttachmentAdded[attachment_count] = true;
                            attachment_count++;
                        }
                        isImageClick = false;
                    }
                }, 150);
            }else {
                Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
                bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
                bitmapOrg =   Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                if (!AttachmentAdded[0]) {
                    attachment_one.setVisibility(View.VISIBLE);
                    attachment_one_img.setBackground(drawable);
                    AttachmentAdded[attachment_count] = true;
                    attachment_count++;
                } else if (!AttachmentAdded[1]) {
                    attachment_two.setVisibility(View.VISIBLE);
                    attachment_two_img.setBackground(drawable);
                    AttachmentAdded[attachment_count] = true;
                    attachment_count++;
                } else if (!AttachmentAdded[2]) {
                    attachment_three.setVisibility(View.VISIBLE);
                    attachment_three_img.setBackground(drawable);
                    AttachmentAdded[attachment_count] = true;
                    attachment_count++;
                }
                isImageClick = false;
            }
        } else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn;
            filePathColumn = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmapOrg = BitmapFactory.decodeFile(picturePath);
            int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
            Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);

            if (!AttachmentAdded[0]) {
                attachment_one.setVisibility(View.VISIBLE);
                attachment_one_img.setBackground(drawable);
                AttachmentAdded[attachment_count] = true;
                attachment_count++;
            } else if (!AttachmentAdded[1]) {
                attachment_two.setVisibility(View.VISIBLE);
                attachment_two_img.setBackground(drawable);
                AttachmentAdded[attachment_count] = true;
                attachment_count++;
            } else if (!AttachmentAdded[2]) {
                attachment_three.setVisibility(View.VISIBLE);
                attachment_three_img.setBackground(drawable);
                AttachmentAdded[attachment_count] = true;
                attachment_count++;
            }
            isImageClick = false;

        }
    }


    @Override
    public void onBackButtonClick() {

    }
}
