package com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomEditText;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.LoadingDots;
import com.codingpixel.healingbudz.customeUI.ProgressDialogVideoProcessing;
import com.codingpixel.healingbudz.customeUI.SelectableRoundedImageView;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.fragment_back_button_listner.BackButtonClickListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;
import com.codingpixel.healingbudz.network.upload_image.UploadVideoAPIcall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotationVideo;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.network.model.URL.add_image;
import static com.codingpixel.healingbudz.network.model.URL.add_question;
import static com.codingpixel.healingbudz.network.model.URL.add_video;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;


public class EditQuestionFragment extends Fragment implements BackButtonClickListner, APIResponseListner, View.OnClickListener {
    CustomEditText Question, Question_detail;
    TextView Question_counter, Question_counter_detail;
    Button Update_Question, Close;
    HomeQAfragmentDataModel dataModel;
    View Scroll_adjust;
    ScrollView scroll_ask_question;
    private BackButtonClickListner backButtonClickListner;


    ///
    Button add_attachment;
    RelativeLayout attachment_three, attachment_two, attachment_one;
    SelectableRoundedImageView attachment_three_image, attachment_two_image, attachment_one_image;
    ImageView attachment_three_video, attachment_two_video, attachment_one_video, attachment_three_cross, attachment_two_cross, attachment_one_cross;
    //
    int attachment_count = 0;
    TextView Lodaing_Dots_text;
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

    public EditQuestionFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public EditQuestionFragment(HomeQAfragmentDataModel dataModel) {
        this.dataModel = dataModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_question_layout, container, false);
//        InitFragmentBackbtnListner(getActivity() , this);
        Init(view);
        InitListenerForAttachment(view);
        setEdits(view);
        return view;
    }

    private void setEdits(View view) {
        if (dataModel.getAttachments() != null && dataModel.getAttachments().size() > 0) {
            attachment_count = dataModel.getAttachments().size();
            for (int i = 0; i < dataModel.getAttachments().size(); i++) {
                JSONObject file_object = new JSONObject();
                try {

                    if (dataModel.getAttachments().get(i).getPoster().length() > 4) {
                        file_object.put("media_type", dataModel.getAttachments().get(i).getMedia_type());
                    } else {
                        file_object.put("media_type", dataModel.getAttachments().get(i).getMedia_type());
                    }
                    file_object.put("path", dataModel.getAttachments().get(i).getUpload_path());
                    file_object.put("poster", dataModel.getAttachments().get(i).getPoster());
                    if (i == 0) {
                        String path = "";
                        if (dataModel.getAttachments().get(i).getPoster().length() > 4) {
                            attachment_one_video.setImageResource(R.drawable.ic_video_play_icon);
                            attachment_one_video.setVisibility(View.VISIBLE);
                            path = dataModel.getAttachments().get(i).getPoster();
                        } else {
                            path = dataModel.getAttachments().get(i).getUpload_path();
                            attachment_one_video.setImageResource(R.drawable.ic_gallery_icon);
                            attachment_one_video.setVisibility(View.GONE);
                        }
                        //.setImageResource(R.drawable.ic_gallery_icon);
                        //.setImageResource(R.drawable.ic_video_play_icon);
                        AttachmentAdded[0] = true;
                        attachment_one.setVisibility(View.VISIBLE);

                        attach_one_Array.put(file_object);
                        Glide.with(attachment_one_image.getContext())
                                .load(images_baseurl + path)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .placeholder(R.drawable.image_bg_blur)
                                .error(R.drawable.noimage)
                                .listener(new RequestListener<String, Bitmap>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        attachment_one_image.setImageDrawable(null);
                                        try {
//                                            Drawable d = resource;
                                            Bitmap bitmapOrg = resource;
                                            bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                            int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                                            Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                            BitmapDrawable drawable = new BitmapDrawable(attachment_one_image.getContext().getResources(), bitmap);
                                            attachment_one_image.setImageDrawable(null);
                                            attachment_one_image.setBackground(null);
                                            attachment_one_image.setImageDrawable(drawable);
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        return false;
                                    }
                                })

                                .into(attachment_one_image);
                    } else if (i == 1) {
                        String path = "";
                        attachment_two.setVisibility(View.VISIBLE);
                        attachment_two_video.setVisibility(View.VISIBLE);
                        //.setImageResource(R.drawable.ic_gallery_icon);
                        //.setImageResource(R.drawable.ic_video_play_icon);
                        if (dataModel.getAttachments().get(i).getPoster().length() > 4) {
                            path = dataModel.getAttachments().get(i).getPoster();
                            attachment_two_video.setImageResource(R.drawable.ic_video_play_icon);
                        } else {
                            attachment_two_video.setImageResource(R.drawable.ic_gallery_icon);
                            attachment_two_video.setVisibility(View.GONE);
                            path = dataModel.getAttachments().get(i).getUpload_path();
                        }
                        AttachmentAdded[1] = true;
                        attach_two_Array.put(file_object);
                        Glide.with(attachment_two_image.getContext())
                                .load(images_baseurl + path)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .placeholder(R.drawable.image_bg_blur)
                                .error(R.drawable.noimage)
                                .listener(new RequestListener<String, Bitmap>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        attachment_two_image.setImageDrawable(null);
                                        try {

                                            Bitmap bitmapOrg = resource;
                                            bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                            int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                                            Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                            BitmapDrawable drawable = new BitmapDrawable(attachment_two_image.getContext().getResources(), bitmap);
                                            attachment_two_image.setImageDrawable(null);
                                            attachment_two_image.setBackground(null);
                                            attachment_two_image.setImageDrawable(drawable);
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        return false;
                                    }
                                })
                                .into(attachment_two_image);
                    } else if (i == 2) {
                        String path = "";
                        attachment_three.setVisibility(View.VISIBLE);
                        attachment_three_video.setVisibility(View.VISIBLE);
                        //.setImageResource(R.drawable.ic_gallery_icon);
                        //.setImageResource(R.drawable.ic_video_play_icon);
                        if (dataModel.getAttachments().get(i).getPoster().length() > 4) {
                            path = dataModel.getAttachments().get(i).getPoster();
                            attachment_three_video.setImageResource(R.drawable.ic_video_play_icon);
                        } else {
                            path = dataModel.getAttachments().get(i).getUpload_path();
                            attachment_three_video.setImageResource(R.drawable.ic_gallery_icon);
                            attachment_three_video.setVisibility(View.GONE);
                        }
                        AttachmentAdded[2] = true;
                        attach_three_Array.put(file_object);
                        Glide.with(attachment_three_image.getContext())
                                .load(images_baseurl + path)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .placeholder(R.drawable.image_bg_blur)
                                .error(R.drawable.noimage)
                                .listener(new RequestListener<String, Bitmap>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        attachment_three_image.setImageDrawable(null);
                                        try {

                                            Bitmap bitmapOrg = resource;
                                            bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                            int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                                            Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                            BitmapDrawable drawable = new BitmapDrawable(attachment_three_image.getContext().getResources(), bitmap);
                                            attachment_three_image.setImageDrawable(null);
                                            attachment_three_image.setBackground(null);
                                            attachment_three_image.setImageDrawable(drawable);
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        return false;
                                    }
                                })
                                .into(attachment_three_image);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void InitListenerForAttachment(View view) {
        attachment_one_cross.setOnClickListener(this);
        attachment_two_cross.setOnClickListener(this);
        attachment_three_cross.setOnClickListener(this);
        add_attachment.setOnClickListener(this);
    }

    public void Init(View view) {
        //3
        add_attachment = view.findViewById(R.id.add_attachment);
        attachment_three = view.findViewById(R.id.attachment_three);
        attachment_three_image = view.findViewById(R.id.attachment_three_image);
        attachment_three_video = view.findViewById(R.id.attachment_three_video);
        attachment_three_cross = view.findViewById(R.id.attachment_three_cross);
        //2
        attachment_two = view.findViewById(R.id.attachment_two);
        attachment_two_image = view.findViewById(R.id.attachment_two_image);
        attachment_two_video = view.findViewById(R.id.attachment_two_video);
        attachment_two_cross = view.findViewById(R.id.attachment_two_cross);
        //1
        attachment_one = view.findViewById(R.id.attachment_one);
        attachment_one_image = view.findViewById(R.id.attachment_one_image);
        attachment_one_video = view.findViewById(R.id.attachment_one_video);
        attachment_one_cross = view.findViewById(R.id.attachment_one_cross);
        //
        Dots_layout = view.findViewById(R.id.dots_layout);
        Lodaing_Dots_text = view.findViewById(R.id.lodaing_dots_text);
        Lodaing_Dots = view.findViewById(R.id.loading_dots);
        attachment_one_image.setCornerRadiusDP(3f);
        attachment_two_image.setCornerRadiusDP(3f);
        attachment_three_image.setCornerRadiusDP(3f);
        attachment_count = 0;
        AttachmentAdded[0] = false;
        AttachmentAdded[1] = false;
        AttachmentAdded[2] = false;
        attachment_one.setVisibility(View.GONE);
        attachment_two.setVisibility(View.GONE);
        attachment_three.setVisibility(View.GONE);
        attachment_one_image.setImageDrawable(null);
        attachment_two_image.setImageDrawable(null);
        attachment_three_image.setImageDrawable(null);
        attachment_count = 0;
        //
        Scroll_adjust = view.findViewById(R.id.scrool_adjust);
        scroll_ask_question = view.findViewById(R.id.scroll_ask_question);
        Question = view.findViewById(R.id.question_text_field);
        Question_detail = view.findViewById(R.id.question_detail_textfiled);
        Question_detail.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Question_counter = view.findViewById(R.id.question_character_counter);
        Question_counter_detail = view.findViewById(R.id.question_detail_character_counter);
        Update_Question = view.findViewById(R.id.ask_your_budz);
        Close = view.findViewById(R.id.close);
        Question.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Scroll_adjust.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            scroll_ask_question.scrollTo(0, 0);
                            scroll_ask_question.fullScroll(View.FOCUS_UP);
                        }
                    }, 100);
                }
            }
        });

        Question_detail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, boolean b) {
                if (b) {
                    Scroll_adjust.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            scroll_ask_question.scrollTo(0, 2000);
                            scroll_ask_question.fullScroll(View.FOCUS_DOWN);
                        }
                    }, 100);
                } else {
                    Scroll_adjust.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            scroll_ask_question.scrollTo(0, 0);
                            scroll_ask_question.fullScroll(View.FOCUS_UP);
                        }
                    }, 100);
                }
            }
        });
        Question_detail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Scroll_adjust.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            scroll_ask_question.scrollTo(0, 0);
                            scroll_ask_question.fullScroll(View.FOCUS_UP);
                        }
                    }, 100);
                }
                return false;
            }
        });

        Question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Question_counter.setText((charSequence.toString().trim().length()) + "/150 characters");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Question_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Question_counter_detail.setText((charSequence.length()) + "/300 characters");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) view.getContext());
                attachment_count = 0;
                AttachmentAdded[0] = false;
                AttachmentAdded[1] = false;
                AttachmentAdded[2] = false;
                attachment_one.setVisibility(View.GONE);
                attachment_two.setVisibility(View.GONE);
                attachment_three.setVisibility(View.GONE);
                attachment_one_image.setImageDrawable(null);
                attachment_two_image.setImageDrawable(null);
                attachment_three_image.setImageDrawable(null);
                attachment_count = 0;
                AttachmentAdded[0] = false;
                AttachmentAdded[1] = false;
                AttachmentAdded[2] = false;
                attachment_one.setVisibility(View.GONE);
                attachment_two.setVisibility(View.GONE);
                attachment_three.setVisibility(View.GONE);
                attachment_one_image.setImageDrawable(null);
                attachment_two_image.setImageDrawable(null);
                attachment_three_image.setImageDrawable(null);
                attach_one_Array = new JSONArray();
                attachment_count = 0;
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                transaction.remove(EditQuestionFragment.this);
                transaction.commitAllowingStateLoss();
                if (backButtonClickListner != null) {
//                    backButtonClickListner.onBackButtonClick();
                }
            }
        });
        Update_Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HideKeyboard(getActivity());
                        if (Question.getText().toString().trim().length() == 0 && Question_detail.getText().toString().trim().length() == 0) {
                            CustomeToast.ShowCustomToast(getContext(), "Required fields are Empty!", Gravity.TOP);
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("question", Question.getText().toString());
                                jsonObject.put("description", Question_detail.getText().toString());
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
                                if (QAUserNotifyScreenActivity.dataModel != null) {
                                    QAUserNotifyScreenActivity.dataModel.setQuestion(Question.getText().toString());
                                    QAUserNotifyScreenActivity.dataModel.setQuestion_description(Question_detail.getText().toString());
                                    if (backButtonClickListner != null) {
                                        backButtonClickListner.onBackButtonClick();
                                    }
                                }
                                new VollyAPICall(getContext(), true, add_question, jsonObject, user.getSession_key(), Request.Method.POST, EditQuestionFragment.this, APIActions.ApiActions.ask_question);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
        });


        Question_detail.setKeyImeChangeListener(new CustomEditText.KeyImeChange() {

            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                Scroll_adjust.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        scroll_ask_question.scrollTo(0, 0);
                        scroll_ask_question.fullScroll(View.FOCUS_UP);
                    }
                }, 100);
                // All keypresses with the keyboard open will come through here!
                // You could also bubble up the true/false if you wanted
                // to disable propagation.
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Question.setText((Html.fromHtml(Html.escapeHtml(Jsoup.parse(dataModel.getQuestion()).text()), Html.FROM_HTML_MODE_COMPACT)));
        } else {
            Question.setText(Html.fromHtml(Html.escapeHtml(Jsoup.parse(dataModel.getQuestion()).text())));
        }
//        String text = Question.getText().toString().length();
        Question_counter.setText(Question.getText().toString().trim().length() + "/150 characters");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Question_detail.setText((Html.fromHtml(Html.escapeHtml(Jsoup.parse(dataModel.getQuestion_description()).text()), Html.FROM_HTML_MODE_COMPACT)));
        } else {
            Question_detail.setText(Html.fromHtml(Html.escapeHtml(Jsoup.parse(dataModel.getQuestion_description()).text())));
        }

        Question_counter_detail.setText((Question_detail.getText().toString().trim().length()) + "/300 characters");
    }

    @Override
    public void onBackButtonClick() {

    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == APIActions.ApiActions.add_media) {
            Dots_layout.setVisibility(View.GONE);
            isUploadinginprogress = false;
            add_attachment.setClickable(true);
            add_attachment.setEnabled(true);
            add_attachment.setOnClickListener(this);
            Update_Question.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
            Update_Question.setClickable(true);
            Update_Question.setEnabled(true);
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
                    if (attach_one_Array.length() == 0) {
                        attach_one_Array.put(file_object);
                    } else
                        attach_two_Array.put(file_object);
                } else if (attachment_count == 3) {
                    if (attach_two_Array.length() == 0) {
                        attach_two_Array.put(file_object);
                    } else
                        attach_three_Array.put(file_object);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            attachment_count = 0;
            AttachmentAdded[0] = false;
            AttachmentAdded[1] = false;
            AttachmentAdded[2] = false;
            attachment_one.setVisibility(View.GONE);
            attachment_two.setVisibility(View.GONE);
            attachment_three.setVisibility(View.GONE);
            attachment_one_image.setImageDrawable(null);
            attachment_two_image.setImageDrawable(null);
            attachment_three_image.setImageDrawable(null);
            attachment_count = 0;
            AttachmentAdded[0] = false;
            AttachmentAdded[1] = false;
            AttachmentAdded[2] = false;
            attachment_one.setVisibility(View.GONE);
            attachment_two.setVisibility(View.GONE);
            attachment_three.setVisibility(View.GONE);
            attachment_one_image.setImageDrawable(null);
            attachment_two_image.setImageDrawable(null);
            attachment_three_image.setImageDrawable(null);
            attach_one_Array = new JSONArray();
            attachment_count = 0;
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
            transaction.remove(EditQuestionFragment.this);
            transaction.commitAllowingStateLoss();
            CustomeToast.ShowCustomToast(getContext(), "Question updated successfully!", Gravity.TOP);
            if (backButtonClickListner != null) {

                backButtonClickListner.onBackButtonClick();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Dots_layout.setVisibility(View.GONE);
        isUploadinginprogress = false;
        Update_Question.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
        add_attachment.setClickable(true);
        add_attachment.setEnabled(true);
        add_attachment.setOnClickListener(this);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setOnClickListener(BackButtonClickListner backButtonClickListner) {
        this.backButtonClickListner = backButtonClickListner;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        attachment_count = 0;
        AttachmentAdded[0] = false;
        AttachmentAdded[1] = false;
        AttachmentAdded[2] = false;
        attachment_one.setVisibility(View.GONE);
        attachment_two.setVisibility(View.GONE);
        attachment_three.setVisibility(View.GONE);
        attachment_one_image.setImageDrawable(null);
        attachment_two_image.setImageDrawable(null);
        attachment_three_image.setImageDrawable(null);
        attachment_count = 0;
        AttachmentAdded[0] = false;
        AttachmentAdded[1] = false;
        AttachmentAdded[2] = false;
        attachment_one.setVisibility(View.GONE);
        attachment_two.setVisibility(View.GONE);
        attachment_three.setVisibility(View.GONE);
        attachment_one_image.setImageDrawable(null);
        attachment_two_image.setImageDrawable(null);
        attachment_three_image.setImageDrawable(null);
        attach_one_Array = new JSONArray();
        attachment_count = 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            Log.d("paths", data.getExtras().getString("file_path_arg"));
            video_processing = ProgressDialogVideoProcessing.newInstance();
            video_processing.show(this.getFragmentManager(), "pd");
            if (data.getExtras().getBoolean("isVideo")) {
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
                            Glide.with(attachment_one_image.getContext())
                                    .load(filePath)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .placeholder(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"))))
                                    .error(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"))))
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                    Log.d("ready", e.getMessage());
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            Log.d("ready", model);
                                            attachment_one_image.setImageDrawable(null);
                                            try {
                                                Drawable d = resource;
                                                Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
                                                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                                int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                                                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                                BitmapDrawable drawable = new BitmapDrawable(attachment_one_image.getContext().getResources(), bitmap);
                                                attachment_one_image.setImageDrawable(null);
                                                attachment_one_image.setBackground(null);
                                                attachment_one_image.setImageDrawable(drawable);
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                            return true;
                                        }
                                    })
                                    .into(430,430);
                            attachment_one_video.setVisibility(View.VISIBLE);
                            attachment_one_video.setImageResource(R.drawable.ic_video_play_icon);
                            attachment_one.setVisibility(View.VISIBLE);
                            attachment_one_image.setImageDrawable(drawable);
                            new EditQuestionFragment.TestAsync(attachment_one_image).execute(filePath);
                            AttachmentAdded[0] = true;
                            attachment_count++;
                        } else if (!AttachmentAdded[1]) {
                            Glide.with(attachment_two_image.getContext())
                                    .load(filePath)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .placeholder(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"))))
                                    .error(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"))))
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                    Log.d("ready", e.getMessage());
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            Log.d("ready", model);
                                            attachment_two_image.setImageDrawable(null);
                                            try {
                                                Drawable d = resource;
                                                Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
                                                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                                int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                                                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                                BitmapDrawable drawable = new BitmapDrawable(attachment_two_image.getContext().getResources(), bitmap);
                                                attachment_two_image.setImageDrawable(null);
                                                attachment_two_image.setBackground(null);
                                                attachment_two_image.setImageDrawable(drawable);
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                            return true;
                                        }
                                    })
                                    .into(430,430);
                            attachment_two_video.setVisibility(View.VISIBLE);
                            attachment_two_video.setImageResource(R.drawable.ic_video_play_icon);
                            attachment_two.setVisibility(View.VISIBLE);
                            attachment_two_image.setImageDrawable(drawable);
                            new EditQuestionFragment.TestAsync(attachment_two_image).execute(filePath);
                            AttachmentAdded[1] = true;
                            attachment_count++;
                        } else if (!AttachmentAdded[2]) {
                            Glide.with(attachment_three_image.getContext())
                                    .load(filePath)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .placeholder(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"))))
                                    .error(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"))))
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                    Log.d("ready", e.getMessage());
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            Log.d("ready", model);
                                            attachment_three_image.setImageDrawable(null);
                                            try {
                                                Drawable d = resource;
                                                Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
                                                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                                int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                                                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                                BitmapDrawable drawable = new BitmapDrawable(attachment_three_image.getContext().getResources(), bitmap);
                                                attachment_three_image.setImageDrawable(null);
                                                attachment_three_image.setBackground(null);
                                                attachment_three_image.setImageDrawable(drawable);
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                            return true;
                                        }
                                    })
                                    .into(430,430);
                            attachment_three_video.setVisibility(View.VISIBLE);
                            attachment_three_video.setImageResource(R.drawable.ic_video_play_icon);
                            attachment_three.setVisibility(View.VISIBLE);
                            attachment_three_image.setImageDrawable(drawable);
                            new EditQuestionFragment.TestAsync(attachment_three_image).execute(filePath);
                            AttachmentAdded[2] = true;
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
                    attachment_one_image.setImageDrawable(drawable);
                    AttachmentAdded[0] = true;
                    attachment_one_video.setVisibility(View.GONE);
                    attachment_one_video.setImageResource(R.drawable.ic_gallery_icon);
                    attachment_count++;
                    Glide.with(attachment_one_image.getContext())
                            .load(data.getExtras().getString("file_path_arg"))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"))))
                            .error(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"))))
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                    Log.d("ready", e.getMessage());
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    Log.d("ready", model);
                                    attachment_one_image.setImageDrawable(null);
                                    try {
                                        Drawable d = resource;
                                        Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
                                        bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                                        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                        BitmapDrawable drawable = new BitmapDrawable(attachment_one_image.getContext().getResources(), bitmap);
                                        attachment_one_image.setImageDrawable(null);
                                        attachment_one_image.setBackground(null);
                                        attachment_one_image.setImageDrawable(drawable);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    return true;
                                }
                            })
                            .into(430,430);
                } else if (!AttachmentAdded[1]) {

                    Glide.with(attachment_two_image.getContext())
                            .load(data.getExtras().getString("file_path_arg"))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"))))
                            .error(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"))))
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                    Log.d("ready", e.getMessage());
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    Log.d("ready", model);
                                    attachment_two_image.setImageDrawable(null);
                                    try {
                                        Drawable d = resource;
                                        Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
                                        bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                                        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                        BitmapDrawable drawable = new BitmapDrawable(attachment_two_image.getContext().getResources(), bitmap);
                                        attachment_two_image.setImageDrawable(null);
                                        attachment_two_image.setBackground(null);
                                        attachment_two_image.setImageDrawable(drawable);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    return true;
                                }
                            })
                            .into(430,430);
                    attachment_two.setVisibility(View.VISIBLE);
                    attachment_two_image.setImageDrawable(drawable);
                    AttachmentAdded[1] = true;
                    attachment_two_video.setVisibility(View.GONE);
                    attachment_two_video.setImageResource(R.drawable.ic_gallery_icon);
                    attachment_count++;
                } else if (!AttachmentAdded[2]) {

                    Glide.with(attachment_three_image.getContext())
                            .load(data.getExtras().getString("file_path_arg"))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"))))
                            .error(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"))))
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                    Log.d("ready", e.getMessage());
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    Log.d("ready", model);
                                    attachment_three_image.setImageDrawable(null);
                                    try {
                                        Drawable d = resource;
                                        Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
                                        bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                                        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                        BitmapDrawable drawable = new BitmapDrawable(attachment_three_image.getContext().getResources(), bitmap);
                                        attachment_three_image.setImageDrawable(null);
                                        attachment_three_image.setBackground(null);
                                        attachment_three_image.setImageDrawable(drawable);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    return true;
                                }
                            })
                            .into(430,430);
                    attachment_three.setVisibility(View.VISIBLE);
                    attachment_three_image.setImageDrawable(drawable);
                    AttachmentAdded[2] = true;
                    attachment_three_video.setVisibility(View.GONE);
                    attachment_three_video.setImageResource(R.drawable.ic_gallery_icon);
                    attachment_count++;
                }
                Drawable main_drawable = new BitmapDrawable(getResources(), bitmapOrg);
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
                attachment_one_image.setImageDrawable(drawable);
                AttachmentAdded[0] = true;
                attachment_count++;
            } else if (!AttachmentAdded[1]) {
                attachment_two.setVisibility(View.VISIBLE);
                attachment_two_image.setImageDrawable(drawable);
                AttachmentAdded[1] = true;
                attachment_count++;
            } else if (!AttachmentAdded[2]) {
                attachment_three.setVisibility(View.VISIBLE);
                attachment_three_image.setImageDrawable(drawable);
                AttachmentAdded[2] = true;
                attachment_count++;
            }
            isImageClick = false;

        }
    }


    public void UploadImage(String drawable) {
        isUploadinginprogress = false;
        Update_Question.setBackgroundResource(R.drawable.q_a_menu_ask_q_button_inprogress);
        Update_Question.setClickable(false);
        Update_Question.setEnabled(false);
        add_attachment.setClickable(false);
        add_attachment.setEnabled(false);
        add_attachment.setOnClickListener(null);
        new UploadImageAPIcall(getContext(), add_image, drawable, user.getSession_key(), this, APIActions.ApiActions.add_media);
    }

    public void UploadVideo(String path) {
        isUploadinginprogress = false;
        Update_Question.setBackgroundResource(R.drawable.q_a_menu_ask_q_button_inprogress);
        Update_Question.setClickable(false);
        Update_Question.setEnabled(false);
        add_attachment.setClickable(false);
        add_attachment.setEnabled(false);
        add_attachment.setOnClickListener(null);
        new UploadVideoAPIcall(HealingBudApplication.getContext(), add_video, path, user.getSession_key(), this, APIActions.ApiActions.add_media);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                Update_Question.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
                Update_Question.setClickable(true);
                Update_Question.setEnabled(true);

                attachment_one.setVisibility(View.GONE);
                AttachmentAdded[0] = false;
                attachment_count--;
                attach_one_Array.remove(0);
                break;
            case R.id.attachment_two_cross:
                Dots_layout.setVisibility(View.GONE);
                isUploadinginprogress = false;
                Update_Question.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
                Update_Question.setClickable(true);
                Update_Question.setEnabled(true);

                attachment_two.setVisibility(View.GONE);
                AttachmentAdded[1] = false;
                attachment_count--;
                attach_two_Array.remove(0);
                break;
            case R.id.attachment_three_cross:
                Dots_layout.setVisibility(View.GONE);
                isUploadinginprogress = false;
                Update_Question.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
                Update_Question.setClickable(true);
                Update_Question.setEnabled(true);

                attachment_three.setVisibility(View.GONE);
                AttachmentAdded[2] = false;
                attachment_count--;
                attach_three_Array.remove(0);
                break;
        }
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
