package com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import com.codingpixel.healingbudz.DataModel.JournalDetailsExpandAbleData;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity;
import com.codingpixel.healingbudz.adapter.AddNewJournalKeywordRecylerAdapter;
import com.codingpixel.healingbudz.adapter.AddNewJournalSelectTagRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.Utilities.DateConverter.convertDate_with_complete_details;
import static com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.JournalDetailsActivity.journalFragmentDataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.adapter.JournalItemHomeFragmentRecylerAdapter.GetEmojis;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.delete_journal_event;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.sharedBaseUrl;
import static com.codingpixel.healingbudz.network.model.URL.videos_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareHBContent;

public class JournalListChildDetailsActivity extends AppCompatActivity implements APIResponseListner {
    RecyclerView Strains_RecylerView, Select_Tag_recylerview;
    AddNewJournalKeywordRecylerAdapter addNewJournalKeywordRecylerAdapter;
    private static final List<String> data = new ArrayList<String>();
    ImageView Back, Home;
    LinearLayout Video_btn;
    public static boolean isActive = true;
    ImageView Slider_image, Indicator_one, Indicator_two, Indicator_three;
    public static JournalDetailsExpandAbleData journalDetailsExpandAbleData;
    ImageView Edit_journal_details;
    Button More_about_this_Strain;
    TextView video_count;
    LinearLayout Like_btn, Dislike_btn;
    ImageView Feeling_image;
    RelativeLayout Delete_event_dialog;
    Handler handler;
    ImageView Share_it;
    ImageView Delete_event_dialog_dots;
    TextView Feeling_Text, More_about_this_starain_text;
    ImageView dislike_icon, like_icon;
    TextView day_count, day_name, mnth_year_text, time_text, journal_name, dislike_count, like_count, disscription,
            First_Day_of_treatment, No_Tags_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActive = true;
        handler = new Handler();
        setContentView(R.layout.activity_journal_list_child_details);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        data.clear();
        data.add(journalDetailsExpandAbleData.getStrain_title());
        addNewJournalKeywordRecylerAdapter = new AddNewJournalKeywordRecylerAdapter(JournalListChildDetailsActivity.this, data, false);
        Strains_RecylerView = (RecyclerView) findViewById(R.id.strain_recyler_view);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(JournalListChildDetailsActivity.this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        Strains_RecylerView.setLayoutManager(layoutManager);
        Strains_RecylerView.setAdapter(addNewJournalKeywordRecylerAdapter);

        AddNewJournalSelectTagRecylerAdapter addNewJournalSelectTagRecylerAdapter = new AddNewJournalSelectTagRecylerAdapter(JournalListChildDetailsActivity.this, journalDetailsExpandAbleData.getTags());
        Select_Tag_recylerview = (RecyclerView) findViewById(R.id.tags_recyler_view);
        FlexboxLayoutManager layoutManager_1 = new FlexboxLayoutManager(JournalListChildDetailsActivity.this);
        layoutManager_1.setFlexDirection(FlexDirection.ROW);
        layoutManager_1.setJustifyContent(JustifyContent.FLEX_START);
        Select_Tag_recylerview.setLayoutManager(layoutManager_1);
        Select_Tag_recylerview.setAdapter(addNewJournalSelectTagRecylerAdapter);

        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(JournalListChildDetailsActivity.this, true);
                finish();
            }
        });


        Edit_journal_details = (ImageView) findViewById(R.id.edit);
        Edit_journal_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoTo(JournalListChildDetailsActivity.this, AddNewJournalEvent.class);
            }
        });

        More_about_this_Strain = (Button) findViewById(R.id.more_abouth_strain);
        More_about_this_starain_text = (TextView) findViewById(R.id.more_about_this_starain);
        More_about_this_Strain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent strain_intetn = new Intent(JournalListChildDetailsActivity.this, StrainDetailsActivity.class);
                strain_intetn.putExtra("strain_id", journalDetailsExpandAbleData.getStrain_id());
                startActivity(strain_intetn);
            }
        });

        if (journalDetailsExpandAbleData.getStrain_id() == 0 || journalDetailsExpandAbleData.getStrain_title().length() == 0) {
            More_about_this_Strain.setVisibility(View.GONE);
        } else {
            More_about_this_Strain.setVisibility(View.VISIBLE);
        }

        Slider_image = (ImageView) findViewById(R.id.slider_img);
        Indicator_one = (ImageView) findViewById(R.id.img_indicator_one);
        Indicator_two = (ImageView) findViewById(R.id.img_indicator_two);
        Indicator_three = (ImageView) findViewById(R.id.img_indicator_three);
        video_count = (TextView) findViewById(R.id.video_count);
        day_count = (TextView) findViewById(R.id.day_count);
        day_name = (TextView) findViewById(R.id.day_name);
        mnth_year_text = (TextView) findViewById(R.id.mnth_year_text);
        time_text = (TextView) findViewById(R.id.time_text);

        Feeling_image = (ImageView) findViewById(R.id.feeling_image);
        Feeling_Text = (TextView) findViewById(R.id.feeling_text);
        journal_name = (TextView) findViewById(R.id.journal_name);
        dislike_count = (TextView) findViewById(R.id.dislike_count);
        like_count = (TextView) findViewById(R.id.like_count);
        disscription = (TextView) findViewById(R.id.disscription);
        First_Day_of_treatment = (TextView) findViewById(R.id.first_day_of_treatment);
        No_Tags_found = (TextView) findViewById(R.id.tags_title);
        like_icon = (ImageView) findViewById(R.id.ic_like_icon);
        dislike_icon = (ImageView) findViewById(R.id.ic_dislike_icon);

        Delete_event_dialog = (RelativeLayout) findViewById(R.id.delete_event_dialog);
        Delete_event_dialog_dots = (ImageView) findViewById(R.id.delete_event_dialog_dots);
        Delete_event_dialog_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delete_event_dialog.setVisibility(View.VISIBLE);
                Animation startAnimation = AnimationUtils.loadAnimation(JournalListChildDetailsActivity.this, R.anim.attatch_fragment_right_to_left);
                Delete_event_dialog.startAnimation(startAnimation);
            }
        });

        if (journalFragmentDataModel.getUser_id() != user.getUser_id()) {
            Delete_event_dialog_dots.setVisibility(View.GONE);
        }
        Share_it = (ImageView) findViewById(R.id.share_btn);
        Share_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                try {
                    object.put("msg", "Check out Healing Budz for your smartphone: " + sharedBaseUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareHBContent(JournalListChildDetailsActivity.this, object);
            }
        });

        Delete_event_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delete_event_dialog.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("journal_event_id", journalDetailsExpandAbleData.getId());
                    new VollyAPICall(JournalListChildDetailsActivity.this, true, URL.delete_journal_event, jsonObject, user.getSession_key(), Request.Method.POST, JournalListChildDetailsActivity.this, delete_journal_event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        Like_btn = (LinearLayout) findViewById(R.id.like_btn);
        Like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    if (journalDetailsExpandAbleData.isCurrentUserLike()) {
                        journalDetailsExpandAbleData.setCurrentUserLike(false);
                        like_icon.setImageResource(R.drawable.ic_like_icon);
                        jsonObject.put("is_like", 0);
                        like_count.setTextColor(Color.parseColor("#FFFFFF"));
                        journalDetailsExpandAbleData.setGet_likes_count(journalDetailsExpandAbleData.getGet_likes_count() - 1);

                    } else {
                        like_icon.setImageResource(R.drawable.ic_like_icon_liked);
                        journalDetailsExpandAbleData.setCurrentUserLike(true);
                        jsonObject.put("is_like", 1);
                        like_count.setTextColor(Color.parseColor("#6BB635"));
                        journalDetailsExpandAbleData.setGet_likes_count(journalDetailsExpandAbleData.getGet_likes_count() + 1);

                    }

                    if (journalDetailsExpandAbleData.isCurrentUserDisLike()) {
                        journalDetailsExpandAbleData.setCurrentUserDisLike(false);
                        dislike_icon.setImageResource(R.drawable.ic_dislike_icon);
                        journalDetailsExpandAbleData.setGet_dis_likes_count(journalDetailsExpandAbleData.getGet_dis_likes_count() - 1);
                        dislike_count.setTextColor(Color.parseColor("#FFFFFF"));
                        dislike_count.setText(journalDetailsExpandAbleData.getGet_dis_likes_count() + "");
                    }
                    like_count.setText(journalDetailsExpandAbleData.getGet_likes_count() + "");
                    jsonObject.put("is_dislike", 0);
                    jsonObject.put("journal_event_id", journalDetailsExpandAbleData.getId());

                    new VollyAPICall(JournalListChildDetailsActivity.this, false, URL.journal_like_or_dislike, jsonObject, user.getSession_key(), Request.Method.POST, JournalListChildDetailsActivity.this, APIActions.ApiActions.testAPI);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Dislike_btn = (LinearLayout) findViewById(R.id.dislike_btn);
        Dislike_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    if (journalDetailsExpandAbleData.isCurrentUserDisLike()) {
                        journalDetailsExpandAbleData.setCurrentUserDisLike(false);
                        dislike_icon.setImageResource(R.drawable.ic_dislike_icon);
                        jsonObject.put("is_dislike", 0);
                        dislike_count.setTextColor(Color.parseColor("#FFFFFF"));
                        journalDetailsExpandAbleData.setGet_dis_likes_count(journalDetailsExpandAbleData.getGet_dis_likes_count() - 1);

                    } else {
                        dislike_icon.setImageResource(R.drawable.ic_dislike_icon_disliked);
                        journalDetailsExpandAbleData.setCurrentUserDisLike(true);
                        jsonObject.put("is_dislike", 1);
                        dislike_count.setTextColor(Color.parseColor("#6BB635"));
                        journalDetailsExpandAbleData.setGet_dis_likes_count(journalDetailsExpandAbleData.getGet_dis_likes_count() + 1);

                    }

                    if (journalDetailsExpandAbleData.isCurrentUserLike()) {
                        journalDetailsExpandAbleData.setCurrentUserLike(false);
                        like_icon.setImageResource(R.drawable.ic_like_icon);
                        journalDetailsExpandAbleData.setGet_likes_count(journalDetailsExpandAbleData.getGet_likes_count() - 1);
                        like_count.setTextColor(Color.parseColor("#FFFFFF"));
                        like_count.setText(journalDetailsExpandAbleData.getGet_likes_count() + "");
                    }


                    dislike_count.setText(journalDetailsExpandAbleData.getGet_dis_likes_count() + "");
                    jsonObject.put("is_like", 0);
                    jsonObject.put("journal_event_id", journalDetailsExpandAbleData.getId());

                    new VollyAPICall(JournalListChildDetailsActivity.this, false, URL.journal_like_or_dislike, jsonObject, user.getSession_key(), Request.Method.POST, JournalListChildDetailsActivity.this, APIActions.ApiActions.testAPI);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Video_btn = (LinearLayout) findViewById(R.id.video_button);
        Video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (journalDetailsExpandAbleData.getGet_video_attachments().size() > 0) {
                    if (journalDetailsExpandAbleData.getGet_video_attachments().size() == 1) {
                        Intent intent = new Intent(JournalListChildDetailsActivity.this, MediPreview.class);
                        intent.putExtra("path", videos_baseurl + journalDetailsExpandAbleData.getGet_video_attachments().get(0).getPath());
                        intent.putExtra("isvideo", true);
                        JournalListChildDetailsActivity.this.startActivity(intent);
                    } else {
                        JournalEventVideoGallery.videos_path = journalDetailsExpandAbleData.getGet_video_attachments();
                        GoTo(JournalListChildDetailsActivity.this, JournalEventVideoGallery.class);
                    }
                }
            }
        });
        SetData();
    }

    public void SetData() {
        if (journalDetailsExpandAbleData.getGet_image_attachments().size() > 0) {
            if (journalDetailsExpandAbleData.getGet_image_attachments().size() == 1) {
                Indicator_one.setVisibility(View.VISIBLE);
                Indicator_two.setVisibility(View.GONE);
                Indicator_three.setVisibility(View.GONE);
                Indicator_one.setImageResource(R.drawable.enter_pin_circle);
                Indicator_two.setImageResource(R.drawable.pin_password_button_bg);
                Indicator_three.setImageResource(R.drawable.pin_password_button_bg);
                Glide.with(this)
                        .load(images_baseurl + journalDetailsExpandAbleData.getGet_image_attachments().get(0))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.ic_user_profile)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Slider_image.setBackground(resource);
                                return false;
                            }
                        })
                        .into(800, 800);
            } else if (journalDetailsExpandAbleData.getGet_image_attachments().size() == 2) {
                Indicator_one.setVisibility(View.VISIBLE);
                Indicator_two.setVisibility(View.VISIBLE);
                Indicator_three.setVisibility(View.GONE);
                SliderImages(0);
            } else if (journalDetailsExpandAbleData.getGet_image_attachments().size() == 3) {
                Indicator_one.setVisibility(View.VISIBLE);
                Indicator_two.setVisibility(View.VISIBLE);
                Indicator_three.setVisibility(View.VISIBLE);
                SliderImages(0);
            }
        } else {
            if (journalDetailsExpandAbleData.getGet_video_attachments().size() == 1) {
                Indicator_one.setVisibility(View.VISIBLE);
                Indicator_two.setVisibility(View.GONE);
                Indicator_three.setVisibility(View.GONE);
                Indicator_one.setImageResource(R.drawable.enter_pin_circle);
                Indicator_two.setImageResource(R.drawable.pin_password_button_bg);
                Indicator_three.setImageResource(R.drawable.pin_password_button_bg);
                Glide.with(this)
                        .load(images_baseurl + journalDetailsExpandAbleData.getGet_video_attachments().get(0).getPoster())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.ic_user_profile)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Slider_image.setBackground(resource);
                                return false;
                            }
                        })
                        .into(800, 800);
            }
        }

        video_count.setText(journalDetailsExpandAbleData.getGet_video_attachments().size() + "");
        String date = convertDate_with_complete_details(journalDetailsExpandAbleData.getDate());
        Log.d("date", date);
        String[] date_splt = date.split(",");
        day_name.setText(date_splt[0]);
        day_count.setText(date_splt[1]);
        mnth_year_text.setText(date_splt[2] + ", " + date_splt[3]);
        time_text.setText(date_splt[4]);

//        Feeling_Text.setText(journalDetailsExpandAbleData.getFeeling().replace(":", ""));
        MakeKeywordClickableText(Feeling_Text.getContext(), journalDetailsExpandAbleData.getFeeling().replace(":", ""), Feeling_Text);
        Feeling_image.setImageResource(GetEmojis(journalDetailsExpandAbleData.getFeeling()));

        like_count.setText(journalDetailsExpandAbleData.getGet_likes_count() + "");
        dislike_count.setText(journalDetailsExpandAbleData.getGet_dis_likes_count() + "");

        journal_name.setText(journalFragmentDataModel.getTitle());
        First_Day_of_treatment.setText(journalDetailsExpandAbleData.getTitle());

//        disscription.setText(journalDetailsExpandAbleData.getDescription());
        MakeKeywordClickableText(disscription.getContext(), journalDetailsExpandAbleData.getDescription(), disscription);

        if (journalDetailsExpandAbleData.getTags().size() == 0) {
            No_Tags_found.setVisibility(View.VISIBLE);
        } else {
            No_Tags_found.setVisibility(View.GONE);
        }

        if (journalDetailsExpandAbleData.isCurrentUserLike()) {
            like_icon.setImageResource(R.drawable.ic_like_icon_liked);
            like_count.setTextColor(Color.parseColor("#6BB635"));
        } else {
            like_icon.setImageResource(R.drawable.ic_like_icon);
            like_count.setTextColor(Color.parseColor("#FFFFFF"));
        }

        if (journalDetailsExpandAbleData.isCurrentUserDisLike()) {
            dislike_icon.setImageResource(R.drawable.ic_dislike_icon_disliked);
            dislike_count.setTextColor(Color.parseColor("#6BB635"));
        } else {
            dislike_icon.setImageResource(R.drawable.ic_dislike_icon);
            dislike_count.setTextColor(Color.parseColor("#FFFFFF"));
        }


        More_about_this_starain_text.setText(journalDetailsExpandAbleData.getStarin_detail());
    }


    public void SliderImages(final int count) {
        if (isActive) {
            Glide.with(JournalListChildDetailsActivity.this)
                    .load(images_baseurl + journalDetailsExpandAbleData.getGet_image_attachments().get(count))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_user_profile)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Slider_image.setBackground(resource);
                            if (count == 0) {
                                Indicator_one.setImageResource(R.drawable.enter_pin_circle);
                                Indicator_two.setImageResource(R.drawable.pin_password_button_bg);
                                Indicator_three.setImageResource(R.drawable.pin_password_button_bg);
                            } else if (count == 1) {
                                Indicator_two.setImageResource(R.drawable.enter_pin_circle);
                                Indicator_one.setImageResource(R.drawable.pin_password_button_bg);
                                Indicator_three.setImageResource(R.drawable.pin_password_button_bg);
                            } else if (count == 2) {
                                Indicator_three.setImageResource(R.drawable.enter_pin_circle);
                                Indicator_two.setImageResource(R.drawable.pin_password_button_bg);
                                Indicator_one.setImageResource(R.drawable.pin_password_button_bg);
                            }
                            return false;
                        }
                    })
                    .into(800, 800);
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (isActive) {
                        if (count == journalDetailsExpandAbleData.getGet_image_attachments().size() - 1) {
                            SliderImages(0);
                        } else {
                            SliderImages(count + 1);
                        }
                    }
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        handler = new Handler();
        if (journalDetailsExpandAbleData.getGet_image_attachments().size() > 0) {
            SliderImages(0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        handler.removeCallbacksAndMessages(null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActive = false;
        handler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == delete_journal_event) {
            JournalDetailsActivity.isRefreshable = true;
            finish();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(JournalListChildDetailsActivity.this, object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (apiActions == delete_journal_event) {
            JournalDetailsActivity.isRefreshable = true;
            finish();
        }
//        JournalDetailsActivity.isRefreshable = true;
//        finish();
    }
}
