package com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity;
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomEditText;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.LoadingDots;
import com.codingpixel.healingbudz.customeUI.ProgressDialogVideoProcessing;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.fragment_back_button_listner.BackButtonClickListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;
import com.codingpixel.healingbudz.network.upload_image.UploadVideoAPIcall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotationVideo;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.network.model.URL.add_answer;
import static com.codingpixel.healingbudz.network.model.URL.add_image;
import static com.codingpixel.healingbudz.network.model.URL.add_video;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class AnswerFragment extends Fragment implements View.OnClickListener, BackButtonClickListner, APIResponseListner {
    com.codingpixel.healingbudz.customeUI.SelectableRoundedImageView attachment_one_img, attachment_two_img, attachment_three_img;
    ImageView attachment_one_video, attachment_two_video, attachment_three_video;
    ImageView attachment_one_cross, attachment_two_cross, attachment_three_cross;
    RelativeLayout attachment_one, attachment_two, attachment_three;
    Button Add_Media;

    TextView Lodaing_Dots_text;

    public Button Close_Fragment;


    public HomeQAfragmentDataModel dataModel;
    TextView Character_counter, Question_Details, Question;
    CustomEditText Answer_text_field;

    View extra_view;
    Button Answer_Your_BUD;
    int attachment_count = 0;
    LoadingDots Lodaing_Dots;
    JSONObject jsonObject = new JSONObject();
    LinearLayout Dots_layout;
    boolean isUploadinginprogress = false;
    JSONArray attach_one_Array = new JSONArray();
    JSONArray attach_two_Array = new JSONArray();
    JSONArray attach_three_Array = new JSONArray();
    boolean isImageClick = false;
    boolean[] AttachmentAdded = new boolean[3];
    private ProgressDialogVideoProcessing video_processing;
    ScrollView scroll_view;

    public boolean isFromMainTabScreen = false;

    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_answer, container, false);
//        InitFragmentBackbtnListner(getActivity() , this);
        Dots_layout = view.findViewById(R.id.dots_layout);
        Lodaing_Dots_text = view.findViewById(R.id.lodaing_dots_text);
        Lodaing_Dots = view.findViewById(R.id.loading_dots);
        Character_counter = view.findViewById(R.id.character_counter);
        Answer_text_field = view.findViewById(R.id.answer_text_field);
        Answer_text_field.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent event) {

                if (view.getId() == R.id.answer_text_field) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
        Add_Media = view.findViewById(R.id.add_attachment);
        scroll_view = view.findViewById(R.id.scroll_view);
        extra_view = view.findViewById(R.id.extra_view);
        Question = view.findViewById(R.id.question);
        Question_Details = view.findViewById(R.id.question_details);
        Add_Media.setOnClickListener(this);
        Answer_Your_BUD = view.findViewById(R.id.answer_your_bud);
        Answer_Your_BUD.setOnClickListener(this);
        attachment_count = 0;
        AttachmentAdded[0] = false;
        AttachmentAdded[1] = false;
        AttachmentAdded[2] = false;
        InitAttachments(view);

        Answer_text_field.setKeyImeChangeListener(new CustomEditText.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                Answer_text_field.clearFocus();
                scroll_view.clearChildFocus(Answer_text_field);
//
//                Question.requestFocus();


            }
        });
        scroll_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });
        Answer_text_field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    extra_view.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            scroll_view.smoothScrollTo(0, Answer_Your_BUD.getTop());
//                            scroll_view.fullScroll(View.FOCUS_DOWN);
                        }
                    }, 100);
                } else {
                    extra_view.setVisibility(View.GONE);
//                    new Handler().postDelayed(new Runnable() {
//                        public void run() {
//                            scroll_view.smoothScrollTo(0, 0);
//                            scroll_view.fullScroll(View.FOCUS_UP);
//                        }
//                    }, 100);


//                    extra_view.setVisibility(View.GONE);
//                    new Handler().postDelayed(new Runnable() {
//                        public void run() {
//                            scroll_view.scrollTo(0, 0);
//                            scroll_view.fullScroll(View.FOCUS_UP);
//                            Answer_text_field.clearFocus();
//                        }
//                    }, 100);
//                    Answer_text_field.clearFocus();
                }
            }
        });
        Answer_text_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Character_counter.setText(charSequence.length() + "/2500 characters");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        MakeKeywordClickableText(getContext(), dataModel.getQuestion(), Question);
        MakeKeywordClickableText(getContext(), dataModel.getQuestion_description(), Question_Details);
        Answer_text_field.setText("");
        Close_Fragment = view.findViewById(R.id.close);
        Close_Fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Answer_text_field.setText("");
                        AttachmentAdded[0] = false;
                        AttachmentAdded[1] = false;
                        AttachmentAdded[2] = false;
                        attachment_one.setVisibility(View.GONE);
                        attachment_two.setVisibility(View.GONE);
                        attachment_three.setVisibility(View.GONE);
                        attachment_one_img.setImageDrawable(null);
                        attachment_two_img.setImageDrawable(null);
                        attachment_three_img.setImageDrawable(null);
                        attach_one_Array = new JSONArray();
                        attachment_count = 0;
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                        transaction.remove(AnswerFragment.this);
                        transaction.commitAllowingStateLoss();
                    }
                });
            }
        });

        return view;
    }

    public void InitAttachments(View view) {
        attachment_one = view.findViewById(R.id.attachment_one);
        attachment_two = view.findViewById(R.id.attachment_two);
        attachment_three = view.findViewById(R.id.attachment_three);

        attachment_one_img = view.findViewById(R.id.attachment_one_image);
        attachment_one_img.setCornerRadiusDP(3f);
        attachment_two_img = view.findViewById(R.id.attachment_two_image);
        attachment_two_img.setCornerRadiusDP(3f);
        attachment_three_img = view.findViewById(R.id.attachment_three_image);
        attachment_three_img.setCornerRadiusDP(3f);

        attachment_one_video = view.findViewById(R.id.attachment_one_video);
        attachment_two_video = view.findViewById(R.id.attachment_two_video);
        attachment_three_video = view.findViewById(R.id.attachment_three_video);

        attachment_one_cross = view.findViewById(R.id.attachment_one_cross);
        attachment_one_cross.setOnClickListener(this);
        attachment_two_cross = view.findViewById(R.id.attachment_two_cross);
        attachment_two_cross.setOnClickListener(this);
        attachment_three_cross = view.findViewById(R.id.attachment_three_cross);
        attachment_three_cross.setOnClickListener(this);
        attachment_count = 0;
        Answer_text_field.setText("");
        AttachmentAdded[0] = false;
        AttachmentAdded[1] = false;
        AttachmentAdded[2] = false;
        attachment_one.setVisibility(View.GONE);
        attachment_two.setVisibility(View.GONE);
        attachment_three.setVisibility(View.GONE);
        attachment_one_img.setImageDrawable(null);
        attachment_two_img.setImageDrawable(null);
        attachment_three_img.setImageDrawable(null);
        attachment_count = 0;
        AttachmentAdded[0] = false;
        AttachmentAdded[1] = false;
        AttachmentAdded[2] = false;
        attachment_one.setVisibility(View.GONE);
        attachment_two.setVisibility(View.GONE);
        attachment_three.setVisibility(View.GONE);
        attachment_one_img.setImageDrawable(null);
        attachment_two_img.setImageDrawable(null);
        attachment_three_img.setImageDrawable(null);
        attach_one_Array = new JSONArray();
        attachment_count = 0;

    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (!isImageClick) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Answer_text_field.setText("");
//                    attachment_count = 0;
//                    AttachmentAdded[0] = false;
//                    AttachmentAdded[1] = false;
//                    AttachmentAdded[2] = false;
//                    attachment_one.setVisibility(View.GONE);
//                    attachment_two.setVisibility(View.GONE);
//                    attachment_three.setVisibility(View.GONE);
//                    attachment_one_img.setImageDrawable(null);
//                    attachment_two_img.setImageDrawable(null);
//                    attachment_three_img.setImageDrawable(null);
//                    FragmentManager manager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
////                    transaction.remove(AnswerFragment.this);
////                    transaction.commitAllowingStateLoss();
//                }
//            });
//        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_attachment:
                if (!isUploadinginprogress) {
                    isImageClick = true;
                    if (Build.VERSION.SDK_INT > 15) {
                        final String[] permissions = {
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE};
                        final List<String> permissionsToRequest = new ArrayList<>();
                        for (String permission : permissions) {
                            if (checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                                permissionsToRequest.add(permission);
                            }
                        }
                        if (!permissionsToRequest.isEmpty()) {
                            requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 1200);
//                            ActivityCompat.requestPermissions(getActivity(), permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 12);
                        } else {
                            if (attachment_count < 3) {

                                Intent intent = new Intent(getContext(), HBCameraActivity.class);
                                intent.putExtra("isVideoCaptureAble", true);
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
                            intent.putExtra("isVideoCaptureAble", true);
                            startActivityForResult(intent, 1200);
                        } else {
                            Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shaking);
                            attachment_one.startAnimation(startAnimation);
                            attachment_two.startAnimation(startAnimation);
                            attachment_three.startAnimation(startAnimation);
                        }
                    }
                }
                break;
            case R.id.attachment_one_cross:
                Dots_layout.setVisibility(View.GONE);
                isUploadinginprogress = false;
                Answer_Your_BUD.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
                Answer_Your_BUD.setClickable(true);
                Answer_Your_BUD.setEnabled(true);

                attachment_one.setVisibility(View.GONE);
                AttachmentAdded[0] = false;
                attachment_count--;
                attach_one_Array.remove(0);
                break;
            case R.id.attachment_two_cross:
                Dots_layout.setVisibility(View.GONE);
                isUploadinginprogress = false;
                Answer_Your_BUD.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
                Answer_Your_BUD.setClickable(true);
                Answer_Your_BUD.setEnabled(true);

                attachment_two.setVisibility(View.GONE);
                AttachmentAdded[1] = false;
                attachment_count--;
                attach_two_Array.remove(0);
                break;
            case R.id.attachment_three_cross:
                Dots_layout.setVisibility(View.GONE);
                isUploadinginprogress = false;
                Answer_Your_BUD.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
                Answer_Your_BUD.setClickable(true);
                Answer_Your_BUD.setEnabled(true);

                attachment_three.setVisibility(View.GONE);
                AttachmentAdded[2] = false;
                attachment_count--;
                attach_three_Array.remove(0);
                break;

            case R.id.answer_your_bud:
                HideKeyboard((Activity) view.getContext());
                if (Answer_text_field.getText().length() > 0 && !isUploadinginprogress) {
                    try {
                        jsonObject.put("answer", Answer_text_field.getText().toString());
                        jsonObject.put("question_id", dataModel.getId());
                        JSONArray files_array = new JSONArray();
                        if (AttachmentAdded[0]) {
                            files_array.put(attach_one_Array.get(0));
                        }
                        if (AttachmentAdded[1]) {
                            files_array.put(attach_two_Array.get(0));
                        }

                        if (AttachmentAdded[2]) {
                            files_array.put(attach_three_Array.get(0));
                        }
                        jsonObject.put("file", files_array);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new VollyAPICall(getContext(), true, add_answer, jsonObject, user.getSession_key(), Request.Method.POST, AnswerFragment.this, APIActions.ApiActions.add_answer);

                } else {
                    CustomeToast.ShowCustomToast(getActivity(), "Required fields are Empty!", Gravity.TOP);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            attachment_count = 0;
//                            FragmentManager manager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction transaction = manager.beginTransaction();
//                            transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
//                            transaction.remove(AnswerFragment.this);
//                            transaction.commitAllowingStateLoss();
//                        }
//                    });
                }
                break;
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
            if (data.getExtras().getBoolean("isVideo")) {
                video_processing = ProgressDialogVideoProcessing.newInstance();
                video_processing.show(AnswerFragment.this.getFragmentManager(), "pd");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Dots_layout.setVisibility(View.VISIBLE);
                        Lodaing_Dots_text.setText("Video Processing...");
                        String filePath = data.getExtras().getString("file_path_arg");
                        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.test_img);
                        Bitmap bitmapOrg = ((BitmapDrawable) d).getBitmap();
                        bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                        Drawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
                        if (!AttachmentAdded[0]) {
                            attachment_one_video.setVisibility(View.VISIBLE);
                            attachment_one.setVisibility(View.VISIBLE);
                            attachment_one_img.setImageDrawable(drawable);
                            new TestAsync(attachment_one_img).execute(filePath);
                            AttachmentAdded[attachment_count] = true;
                            attachment_count++;
                        } else if (!AttachmentAdded[1]) {
                            attachment_two_video.setVisibility(View.VISIBLE);
                            attachment_two.setVisibility(View.VISIBLE);
                            attachment_two_img.setImageDrawable(drawable);
                            new TestAsync(attachment_two_img).execute(filePath);
                            AttachmentAdded[attachment_count] = true;
                            attachment_count++;
                        } else if (!AttachmentAdded[2]) {
                            attachment_three_video.setVisibility(View.VISIBLE);
                            attachment_three.setVisibility(View.VISIBLE);
                            attachment_three_img.setImageDrawable(drawable);
                            new TestAsync(attachment_three_img).execute(filePath);
                            AttachmentAdded[attachment_count] = true;
                            attachment_count++;
                        }
                        isImageClick = false;
                    }
                }, 250);
            } else {
                Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
                bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                if (!AttachmentAdded[0]) {
                    attachment_one.setVisibility(View.VISIBLE);
                    attachment_one_video.setVisibility(View.GONE);
                    attachment_one_img.setImageDrawable(drawable);
                    AttachmentAdded[attachment_count] = true;
                    attachment_count++;
                } else if (!AttachmentAdded[1]) {
                    attachment_two.setVisibility(View.VISIBLE);
                    attachment_two_img.setImageDrawable(drawable);
                    attachment_two_video.setVisibility(View.GONE);
                    AttachmentAdded[attachment_count] = true;
                    attachment_count++;
                } else if (!AttachmentAdded[2]) {
                    attachment_three.setVisibility(View.VISIBLE);
                    attachment_three_video.setVisibility(View.GONE);
                    attachment_three_img.setImageDrawable(drawable);
                    AttachmentAdded[attachment_count] = true;
                    attachment_count++;
                }
                Bitmap bitmapOrgAbc = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
                bitmapOrgAbc = checkRotation(bitmapOrgAbc, data.getExtras().getString("file_path_arg"));
                Drawable main_drawable = new BitmapDrawable(getResources(), bitmapOrgAbc);
                UploadImage(data.getExtras().getString("file_path_arg"));
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
            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            if (!AttachmentAdded[0]) {
                attachment_one.setVisibility(View.VISIBLE);
                attachment_one_video.setVisibility(View.GONE);
                attachment_one_img.setImageDrawable(drawable);
                AttachmentAdded[attachment_count] = true;
                attachment_count++;
            } else if (!AttachmentAdded[1]) {
                attachment_two.setVisibility(View.VISIBLE);
                attachment_two_video.setVisibility(View.GONE);
                attachment_two_img.setImageDrawable(drawable);
                AttachmentAdded[attachment_count] = true;
                attachment_count++;
            } else if (!AttachmentAdded[2]) {
                attachment_three.setVisibility(View.VISIBLE);
                attachment_three_video.setVisibility(View.GONE);
                attachment_three_img.setImageDrawable(drawable);
                AttachmentAdded[attachment_count] = true;
                attachment_count++;
            }
            isImageClick = false;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        attachment_count = 0;
        AttachmentAdded[0] = false;
        AttachmentAdded[1] = false;
        AttachmentAdded[2] = false;

        Answer_text_field.setText("");
        AttachmentAdded[0] = false;
        AttachmentAdded[1] = false;
        AttachmentAdded[2] = false;
        attachment_one.setVisibility(View.GONE);
        attachment_two.setVisibility(View.GONE);
        attachment_three.setVisibility(View.GONE);
        attachment_one_img.setImageDrawable(null);
        attachment_two_img.setImageDrawable(null);
        attachment_three_img.setImageDrawable(null);
    }

    @Override
    public void onBackButtonClick() {

    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("respnose", response);
//        HideKeyboard(getActivity());
        if (apiActions == APIActions.ApiActions.add_answer) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    attachment_count = 0;
                    Answer_text_field.setText("");
                    AttachmentAdded[0] = false;
                    AttachmentAdded[1] = false;
                    AttachmentAdded[2] = false;
                    attachment_one.setVisibility(View.GONE);
                    attachment_two.setVisibility(View.GONE);
                    attachment_three.setVisibility(View.GONE);
                    attachment_one_img.setImageDrawable(null);
                    attachment_two_img.setImageDrawable(null);
                    attachment_three_img.setImageDrawable(null);
                    attachment_count = 0;
                    AttachmentAdded[0] = false;
                    AttachmentAdded[1] = false;
                    AttachmentAdded[2] = false;
                    attachment_one.setVisibility(View.GONE);
                    attachment_two.setVisibility(View.GONE);
                    attachment_three.setVisibility(View.GONE);
                    attachment_one_img.setImageDrawable(null);
                    attachment_two_img.setImageDrawable(null);
                    attachment_three_img.setImageDrawable(null);
                    attach_one_Array = new JSONArray();
                    attachment_count = 0;
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                    if (isFromMainTabScreen) {
                        isFromMainTabScreen = false;
                        QA_HomeTabFragment.isAnsswred = true;
                        QA_HomeTabFragment.dataModel = dataModel;
                    }
                    transaction.remove(AnswerFragment.this);
                    transaction.commitAllowingStateLoss();
                    DiscussQuestionFragment.RefreshData();
                    QA_HomeTabFragment.ReloadData();
                    CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Submit Successfully!", Gravity.TOP);
                }
            });
        } else if (apiActions == APIActions.ApiActions.add_media) {
            Dots_layout.setVisibility(View.GONE);
            isUploadinginprogress = false;
            Answer_Your_BUD.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
            Answer_Your_BUD.setClickable(true);
            Answer_Your_BUD.setEnabled(true);
            JSONObject file_object = new JSONObject();
            try {
                JSONObject response_obg = new JSONObject(response);
                if (response_obg.getString("poster").length() > 4) {
                    file_object.put("media_type", "video");
                } else {
                    file_object.put("media_type", "image");
                }
                file_object.put("path", response_obg.getString("path"));
                file_object.put("poster", response_obg.getString("poster"));
                if (attachment_count == 1) {
                    attach_one_Array.put(file_object);
                } else if (attachment_count == 2) {
                    attach_two_Array.put(file_object);
                } else if (attachment_count == 3) {
                    attach_three_Array.put(file_object);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("respnose", response);
        Dots_layout.setVisibility(View.GONE);
        isUploadinginprogress = false;
        Answer_Your_BUD.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void UploadImage(String drawable) {
        isUploadinginprogress = false;
        Answer_Your_BUD.setBackgroundResource(R.drawable.q_a_menu_ask_q_button_inprogress);
        Answer_Your_BUD.setClickable(false);
        Answer_Your_BUD.setEnabled(false);
        new UploadImageAPIcall(getContext(), add_image, drawable, user.getSession_key(), AnswerFragment.this, APIActions.ApiActions.add_media);
    }

    public void UploadVideo(String path) {
        isUploadinginprogress = false;
        Answer_Your_BUD.setBackgroundResource(R.drawable.q_a_menu_ask_q_button_inprogress);
        Answer_Your_BUD.setClickable(false);
        Answer_Your_BUD.setEnabled(false);
        new UploadVideoAPIcall(HealingBudApplication.getContext(), add_video, path, user.getSession_key(), AnswerFragment.this, APIActions.ApiActions.add_media);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            if (checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, 7677);
            } else if (requestCode == 1200) {
                if (attachment_count < 3) {

                    Intent intent = new Intent(getContext(), HBCameraActivity.class);
                    intent.putExtra("isVideoCaptureAble", true);
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

    @SuppressLint("NewApi")
    public static Bitmap retriveVideoFrameFromVideo(String p_videoPath)
            throws Throwable {
        Bitmap m_bitmap = null;
        MediaMetadataRetriever m_mediaMetadataRetriever = null;
        try {
            m_mediaMetadataRetriever = new MediaMetadataRetriever();
            m_mediaMetadataRetriever.setDataSource(p_videoPath);
            m_bitmap = m_mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception m_e) {
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String p_videoPath)"
                            + m_e.getMessage());
        } finally {
            if (m_mediaMetadataRetriever != null) {
                m_mediaMetadataRetriever.release();
            }
        }
        return m_bitmap;
    }

    private class TestAsync extends AsyncTask<String, String, Bitmap> {

        String path = "";
        ImageView activity;

        TestAsync(ImageView imageView) {
            this.activity = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap video_thumbnil = null;
            try {
                path = params[0];
                video_thumbnil = GroupsChatViewActivity.getVideoThumbnail(params[0]);
                Log.d("img", video_thumbnil.toString());
                onProgressUpdate();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return video_thumbnil;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            bitmap = checkRotationVideo(bitmap, path);
            Bitmap video_thumbnil = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
            int corner_radious_plc = (video_thumbnil.getWidth() * 10) / 100;
            Bitmap bitmap_plc = getRoundedCornerBitmap(video_thumbnil, corner_radious_plc);
            Drawable drawable = new BitmapDrawable(HealingBudApplication.getContext().getResources(), bitmap_plc);
            this.activity.setImageDrawable(drawable);
            video_processing.dismiss();
            Lodaing_Dots_text.setText("Uploading Video...");
            UploadVideo(path);

        }
    }
}
