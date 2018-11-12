package com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.DataModel.QuestionAnswersDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.customeUI.CircularImageView;
import com.codingpixel.healingbudz.customeUI.SelectableRoundedImageView;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.fragment_back_button_listner.BackButtonClickListner;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static android.view.View.GONE;
import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.Utilities.DateConverter.convertDate_with_month_name;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_my_question;
import static com.codingpixel.healingbudz.network.model.URL.get_question;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

public class QAUserNotifyScreenActivity extends AppCompatActivity implements BackButtonClickListner, APIResponseListner {
    TextView answer_text, question_date, question_detail_text, question, user_name_title;
    CircularImageView profile_image;
    ImageView edit_question, profile_img_topi;
    ImageView left_menu_btn_img;
    public static HomeQAfragmentDataModel dataModel;

    LinearLayout attachment_view;
    RelativeLayout attachment_three, attachment_two, attachment_one;
    SelectableRoundedImageView attachment_three_image, attachment_two_image, attachment_one_image;
    ImageView attachment_three_video, attachment_two_video, attachment_one_video;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
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
        setContentView(R.layout.activity_qauser_notify_screen);

        ChangeStatusBarColor(QAUserNotifyScreenActivity.this, "#171717");
        answer_text = (TextView) findViewById(R.id.answer_text);
        edit_question = (ImageView) findViewById(R.id.edit_question);
        edit_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditQuestionFragment editQuestionFragment = new EditQuestionFragment(dataModel);
                editQuestionFragment.setOnClickListener(QAUserNotifyScreenActivity.this);
                FragmentManager manager = QAUserNotifyScreenActivity.this.getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.fade_out);
                transaction.add(R.id.main_fragment, editQuestionFragment);
                transaction.commitAllowingStateLoss();
            }
        });
        answer_text.setText(dataModel.getUser_notify() + " Budz have been notified. \n You should received an answer in a few minutes.");
        question_date = (TextView) findViewById(R.id.question_date);
        question_detail_text = (TextView) findViewById(R.id.question_detail_text);
        question = (TextView) findViewById(R.id.question);
        user_name_title = (TextView) findViewById(R.id.user_name_title);
        profile_image = (CircularImageView) findViewById(R.id.profile_image);
        profile_img_topi = (ImageView) findViewById(R.id.profile_img_topi);
        left_menu_btn_img = (ImageView) findViewById(R.id.left_menu_btn_img);
        left_menu_btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QAUserNotifyScreenActivity.this.onBackPressed();
            }
        });
        user_name_title.setText(dataModel.getuser_name());
//        user_name_title.setTextColor(Color.parseColor(Utility.getBudColor(dataModel.getUser_points())));
//        UserName_Location.setText(dataModel.getuser_name() + " from " + str[0]);
        question_date.setText(convertDate_with_month_name(dataModel.getCreated_at()));
        question.setText(dataModel.getQuestion());
        MakeKeywordClickableText(QAUserNotifyScreenActivity.this, dataModel.getQuestion_description(), question_detail_text);
        MakeKeywordClickableText(QAUserNotifyScreenActivity.this, dataModel.getQuestion(), question);
        if (dataModel.getSpecial_icon().length() > 6) {
            profile_img_topi.setVisibility(View.VISIBLE);
            Glide.with(this).load(URL.images_baseurl + dataModel.getSpecial_icon()).error(R.drawable.noimage)
                    .placeholder(R.drawable.topi_ic).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                    profile_img_topi.setImageDrawable(resource);
                    return false;
                }
            }).into(profile_img_topi);

        } else {
            profile_img_topi.setVisibility(View.GONE);
        }
        Glide.with(this).load(URL.images_baseurl + dataModel.getUser_photo()).error(R.drawable.noimage)
                .placeholder(R.drawable.ic_user_profile).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                profile_image.setImageDrawable(resource);
                return false;
            }
        }).into(profile_image);
        attachment_three = findViewById(R.id.attachment_three);
        attachment_two = findViewById(R.id.attachment_two);
        attachment_one = findViewById(R.id.attachment_one);
        attachment_three_image = findViewById(R.id.attachment_three_image);
        attachment_two_image = findViewById(R.id.attachment_two_image);
        attachment_one_image = findViewById(R.id.attachment_one_image);
        attachment_three_video = findViewById(R.id.attachment_three_video);
        attachment_two_video = findViewById(R.id.attachment_two_video);
        attachment_one_video = findViewById(R.id.attachment_one_video);
        attachment_one_image.setCornerRadiusDP(3f);
        attachment_two_image.setCornerRadiusDP(3f);
        attachment_three_image.setCornerRadiusDP(3f);
        attachment_view = findViewById(R.id.attachment_view);
        setData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(this, true, get_question + "/" + dataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, this, APIActions.ApiActions.get_my_question);
    }

    public void setData() {
        if (dataModel.getAttachments() != null) {
            if (dataModel.getAttachments().size() > 0) {
                attachment_view.setVisibility(View.VISIBLE);
                final ArrayList<QuestionAnswersDataModel.Attachment> attachments = dataModel.getAttachments();
                for (int x = 0; x < attachments.size(); x++) {
                    final QuestionAnswersDataModel.Attachment attachment = attachments.get(x);
                    ImageView attachment_img = null;
                    if (x == 2) {
                        attachment_one.setVisibility(View.VISIBLE);
                        attachment_one.setTag(attachment.getUpload_path());
                        attachment_img = attachment_one_image;
                    } else if (x == 1) {
                        attachment_one.setVisibility(GONE);
                        attachment_two.setVisibility(View.VISIBLE);
                        attachment_two.setTag(attachment.getUpload_path());
                        attachment_img = attachment_two_image;
                    } else if (x == 0) {
                        attachment_two.setVisibility(GONE);
                        attachment_one.setVisibility(GONE);
                        attachment_three.setVisibility(View.VISIBLE);
                        attachment_three.setTag(attachment.getUpload_path());
                        attachment_img = attachment_three_image;
                    }
                    final ImageView finalAttachment_img = attachment_img;
                    String path = "";
                    if (attachment.getPoster() != null && attachment.getPoster().length() > 7) {
                        path = images_baseurl + attachment.getPoster();
                        // enable video icon
                        if (x == 2) {
                            attachment_one_video.setVisibility(View.VISIBLE);
                            attachment_one_video.setImageResource(R.drawable.ic_video_play_icon);
                        } else if (x == 1) {

                            attachment_one_video.setVisibility(GONE);
                            attachment_two_video.setVisibility(View.VISIBLE);
                            attachment_two_video.setImageResource(R.drawable.ic_video_play_icon);
                        } else if (x == 0) {
                            attachment_two_video.setVisibility(GONE);
                            attachment_one_video.setVisibility(GONE);
                            attachment_three_video.setImageResource(R.drawable.ic_video_play_icon);
                            attachment_three_video.setVisibility(View.VISIBLE);
                        }
                    } else {
                        path = images_baseurl + attachment.getUpload_path();
                        if (x == 2) {
                            attachment_one_video.setVisibility(View.VISIBLE);
                            attachment_one_video.setImageResource(R.drawable.ic_gallery_icon);
                            attachment_one_video.setVisibility(GONE);
                        } else if (x == 1) {
                            attachment_one_video.setVisibility(GONE);
                            attachment_two_video.setVisibility(View.VISIBLE);
                            attachment_two_video.setImageResource(R.drawable.ic_gallery_icon);
                            attachment_two_video.setVisibility(GONE);
                        } else if (x == 0) {
                            attachment_two_video.setVisibility(GONE);
                            attachment_one_video.setVisibility(GONE);
                            attachment_three_video.setImageResource(R.drawable.ic_gallery_icon);
                            attachment_three_video.setVisibility(View.VISIBLE);
                            attachment_three_video.setVisibility(GONE);
                        }
                    }

                    attachment_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String path = attachment_one.getTag().toString();
                            boolean isVideo = false;
                            Intent intent = new Intent(QAUserNotifyScreenActivity.this, MediPreview.class);
                            if (path.contains("mp4")) {
                                isVideo = true;
                                path = URL.videos_baseurl + path;
                            } else {
                                intent.putExtra("Attachment_array", (Serializable) attachments);
                                isVideo = false;
                                path = URL.images_baseurl + path;
                            }

                            intent.putExtra("display", attachment_one.getTag().toString());
                            intent.putExtra("path", path);
                            intent.putExtra("isvideo", isVideo);
                            startActivity(intent);
                        }
                    });
                    attachment_two.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String path = attachment_two.getTag().toString();
                            boolean isVideo = false;
                            Intent intent = new Intent(QAUserNotifyScreenActivity.this, MediPreview.class);
                            if (path.contains("mp4")) {
                                isVideo = true;
                                path = URL.videos_baseurl + path;
                            } else {
                                intent.putExtra("Attachment_array", (Serializable) attachments);
                                isVideo = false;
                                path = URL.images_baseurl + path;
                            }
                            intent.putExtra("display", attachment_two.getTag().toString());
                            intent.putExtra("path", path);
                            intent.putExtra("isvideo", isVideo);
                            QAUserNotifyScreenActivity.this.startActivity(intent);
                        }
                    });

                    attachment_three.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String path = attachment_three.getTag().toString();
                            boolean isVideo = false;
                            Intent intent = new Intent(QAUserNotifyScreenActivity.this, MediPreview.class);
                            if (path.contains("mp4")) {
                                isVideo = true;
                                path = URL.videos_baseurl + path;
                            } else {
                                intent.putExtra("Attachment_array", (Serializable) attachments);
//                                intent.putExtra("Pos_Tap",x);
                                isVideo = false;
                                path = URL.images_baseurl + path;
                            }
                            intent.putExtra("display", attachment_three.getTag().toString());
                            intent.putExtra("path", path);
                            intent.putExtra("isvideo", isVideo);
                            startActivity(intent);
                        }
                    });
                    Glide.with(attachment_three.getContext())
                            .load(path)
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
                                    finalAttachment_img.setImageDrawable(null);
                                    try {
//                                        Drawable d = resource;
                                        Bitmap bitmapOrg = resource;
                                        bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                                        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                        BitmapDrawable drawable = new BitmapDrawable(QAUserNotifyScreenActivity.this.getResources(), bitmap);
                                        finalAttachment_img.setImageDrawable(null);
                                        finalAttachment_img.setBackground(null);
                                        finalAttachment_img.setImageDrawable(drawable);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    return false;
                                }
                            })

                            .into(finalAttachment_img);
                }
            } else {
                attachment_view.setVisibility(GONE);
            }
        } else {
            attachment_view.setVisibility(GONE);
        }
        question_date.setText(convertDate_with_month_name(dataModel.getCreated_at()));
        question.setText(dataModel.getQuestion());
        user_name_title.setText(dataModel.getuser_name());
//        user_name_title.setTextColor(Color.parseColor(Utility.getBudColor(dataModel.getUser_points())));
//        UserName_Location.setText(dataModel.getuser_name() + " from " + str[0]);
        question_date.setText(convertDate_with_month_name(dataModel.getCreated_at()));
        question.setText(dataModel.getQuestion());
        MakeKeywordClickableText(QAUserNotifyScreenActivity.this, dataModel.getQuestion_description(), question_detail_text);
        MakeKeywordClickableText(QAUserNotifyScreenActivity.this, dataModel.getQuestion(), question);
        if (dataModel.getSpecial_icon().length() > 6) {
            profile_img_topi.setVisibility(View.VISIBLE);
            Glide.with(this).load(URL.images_baseurl + dataModel.getSpecial_icon()).error(R.drawable.noimage)
                    .placeholder(R.drawable.topi_ic).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                    profile_img_topi.setImageDrawable(resource);
                    return false;
                }
            }).into(profile_img_topi);

        } else {
            profile_img_topi.setVisibility(View.GONE);
        }
        Glide.with(this).load(URL.images_baseurl + dataModel.getUser_photo()).error(R.drawable.noimage)
                .placeholder(R.drawable.ic_user_profile).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                profile_image.setImageDrawable(resource);
                return false;
            }
        }).into(profile_image);
    }

    @Override
    public void onBackButtonClick() {
        question_date.setText(convertDate_with_month_name(dataModel.getCreated_at()));
        question.setText(dataModel.getQuestion());
        MakeKeywordClickableText(QAUserNotifyScreenActivity.this, dataModel.getQuestion_description(), question_detail_text);
        MakeKeywordClickableText(QAUserNotifyScreenActivity.this, dataModel.getQuestion(), question);
        onResume();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == get_my_question) {

            try {
                JSONObject object = new JSONObject(response);
                JSONArray question_array = object.getJSONArray("successData");
                for (int x = 0; x < question_array.length(); x++) {
                    JSONObject quesiton_object = question_array.getJSONObject(x);
                    HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                    JSONObject user_object = quesiton_object.getJSONObject("get_user");
                    model.setUser_name(user_object.getString("first_name"));
                    model.setUser_name_dscription("asks...");
                    String user_photo = user_object.optString("image_path");
                    if (!user_photo.equalsIgnoreCase("null") && user_photo.length() > 6) {
                        model.setUser_photo(user_photo);
                        model.setAvatar(user_object.optString("avatar"));
                    } else {
                        model.setUser_photo(user_object.optString("avatar"));
                        model.setAvatar(user_object.optString("avatar"));
                    }
                    model.setUser_points(user_object.getInt("points"));
                    model.setUser_location(user_object.optString("location"));
                    model.setQuestion(quesiton_object.getString("question"));
                    model.setSpecial_icon(user_object.optString("special_icon"));
                    model.setShow_ads(quesiton_object.getInt("show_ads"));
                    model.setId(quesiton_object.getInt("id"));
                    model.setUser_id(quesiton_object.getInt("user_id"));
                    model.setCreated_at(quesiton_object.getString("created_at"));
                    model.setUpdated_at(quesiton_object.getString("updated_at"));
                    model.setQuestion_description(quesiton_object.getString("description"));
                    model.setAnswerCount(quesiton_object.getInt("get_answers_count"));
                    model.setGet_user_flag_count(quesiton_object.getInt("get_user_flag_count"));
                    model.setGet_user_likes_count(quesiton_object.getInt("get_user_likes_count"));
                    JSONArray attachment_Array = quesiton_object.getJSONArray("attachments");
                    ArrayList<QuestionAnswersDataModel.Attachment> attachments = new ArrayList<>();
                    if (attachment_Array.length() > 0) {
                        for (int y = 0; y < attachment_Array.length(); y++) {
                            JSONObject jsn_obj = attachment_Array.getJSONObject(y);
                            QuestionAnswersDataModel.Attachment attachment = new QuestionAnswersDataModel.Attachment();
                            attachment.setId(jsn_obj.getInt("id"));
                            attachment.setAnswer_id(jsn_obj.getInt("question_id"));
                            attachment.setMedia_type(jsn_obj.getString("media_type"));
                            attachment.setUpload_path(jsn_obj.getString("upload_path"));
                            attachment.setPoster(jsn_obj.optString("poster"));
                            attachments.add(attachment);
                        }

                    }
                    model.setAttachments(attachments);
                    dataModel = model;
                    if (model.getShow_ads() == 1 && model.getUser_id() == user.getUser_id()) {
                        JSONObject objecta = new JSONObject();
                        try {
                            objecta.put("show_ads", 0);
                            objecta.put("question_id", dataModel.getId());
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                    setData();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {

    }
}
