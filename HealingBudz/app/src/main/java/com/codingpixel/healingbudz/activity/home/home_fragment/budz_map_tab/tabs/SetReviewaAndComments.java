package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.codingpixel.healingbudz.DataModel.BudzMapReviews;
import com.codingpixel.healingbudz.DataModel.ReportQuestionListDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapCommentFullView;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzEditReviewAlertDialog;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzReplyReviewAlertDialog;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialogVideoProcessing;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.GetActivityForResultResponse;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_file_with_progress.UploadFileWithProgress;
import com.codingpixel.healingbudz.static_function.ShareIntent;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotationVideo;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity.budz_map_report;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity.main_scroll_view;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.DiscussQuestionFragment.isNewScreen;
import static com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingChatViewActivity.getVideoThumbnail;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_budz_review;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_budz_review_flag;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.delete_budz_review;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.testAPI;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.sharedBaseUrl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;

public class SetReviewaAndComments implements GetActivityForResultResponse, APIResponseListner, ReportSendButtonLstner, BudzEditReviewAlertDialog.OnDialogFragmentClickListener, BudzReplyReviewAlertDialog.OnDialogFragmentClickListener {
    int rating = 1;
    Fragment fragmentVar;
    LinearLayout Full_comments;
    ImageView comment_one_user_img, profile_img_topi, profile_img_topi_2, ic_edit_one, ic_edit_two, ic_delete_one, ic_delete_two, comment_one_rating_img, comment_one_media_type_icon, Comment_one_img, comment_two_rating_img,
            comment_two_media_type_icon, Comment_two_img, comment_two_user_img, comment_one_report_icon, comment_two_report_icon;
    TextView comment_one_user_name, comment_one_comment_text, comment_one_date, comment_one_rating_count, comment_two_user_name,
            comment_two_comment_text, comment_two_date, comment_two_rating_count, review_count, comment_one_report_text, comment_two_report_text, question_detail_character_counter;
    LinearLayout comment_one_share, comment_two_share, first_review_layout, second_review_layout, report_one, report_two, comment_one_reply, comment_two_reply;
    RelativeLayout First_Attach, Second_Attach, edit_one, edit_two;
    Context MContext;
    ArrayList<BudzMapReviews> reviews;
    View reply_layout_one, reply_layout_two;
    int userCount = 0;
    RelativeLayout edit_reply_two, edit_reply;
    ImageView ic_edit_reply, ic_edit_reply_two, ic_delete_reply, ic_delete_reply_two;
    private LinearLayout Comment_Review_Layout;
    private LinearLayout Add_Comment_Layout, comment_section;
    private SimpleRatingBar rating_bar;
    private Button Submit_comment;
    private String Attatchment_file_path = "";
    private String Attatchment_file_type = "";
    private LinearLayout Comment_image_layout;
    private TextView attachment_name;
    private ImageView attachement_img, attatchment_cross, comment_two_like, comment_one_like;
    private Button upload_comment_image;
    private RelativeLayout Attach_image_line;
    private ProgressDialogVideoProcessing video_processing;
    private EditText commment_text;
    private LinearLayout Report_it_one, Report_it_two;
    private TextView reply, reply_two, reply_date, reply_date_two, first_count, second_count;
    int compareID = 0;

    @SuppressLint("ClickableViewAccessibility")
    public void InitData(final Context context, final Fragment fragment, View view, final ArrayList<BudzMapReviews> review, int userCount) {
        //REviews
        MContext = context;
        if (budz_map_item_clickerd_dataModel != null) {
            compareID = budz_map_item_clickerd_dataModel.getUser_id();
        }
        fragmentVar = fragment;
        InitView(view);
        this.userCount = userCount;
        this.reviews = review;


        //comments
        BudzMapHomeFragment.getActivityForResultResponse = SetReviewaAndComments.this;
        comment_section = view.findViewById(R.id.comment_section);
        comment_two_like = view.findViewById(R.id.comment_two_like);
        first_count = view.findViewById(R.id.first_count);
        second_count = view.findViewById(R.id.second_count);
        comment_one_like = view.findViewById(R.id.comment_one_like);
        reply = view.findViewById(R.id.reply);
        reply_two = view.findViewById(R.id.reply_two);
        ic_edit_reply = view.findViewById(R.id.ic_edit_reply);
        ic_edit_reply_two = view.findViewById(R.id.ic_edit_reply_two);
        ic_delete_reply = view.findViewById(R.id.ic_delete_reply);
        ic_delete_reply_two = view.findViewById(R.id.ic_delete_reply_two);
        ic_delete_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(MContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this reply?")
                        .setConfirmText("Yes, delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                try {
                                    new VollyAPICall(MContext
                                            , true
                                            , URL.delete_budz_review_reply
                                            , new JSONObject().put("review_reply_id", review.get(0).getReply().getId())
                                            , user.getSession_key()
                                            , Request.Method.POST
                                            , SetReviewaAndComments.this, APIActions.ApiActions.delete_budz_review);
                                    reply_layout_one.setVisibility(View.GONE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
        });
        ic_delete_reply_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(MContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this reply?")
                        .setConfirmText("Yes, delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                try {
                                    new VollyAPICall(MContext
                                            , true
                                            , URL.delete_budz_review_reply
                                            , new JSONObject().put("review_reply_id", review.get(1).getReply().getId())
                                            , user.getSession_key()
                                            , Request.Method.POST
                                            , SetReviewaAndComments.this, APIActions.ApiActions.delete_budz_review);
                                    reply_layout_two.setVisibility(View.GONE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
        });
        edit_reply_two = view.findViewById(R.id.edit_reply_two);
        edit_reply = view.findViewById(R.id.edit_reply);
        reply_date = view.findViewById(R.id.reply_date);
        reply_date_two = view.findViewById(R.id.reply_date_two);
        reply_layout_one = view.findViewById(R.id.reply_layout_one);
        reply_layout_two = view.findViewById(R.id.reply_layout_two);
        Add_Comment_Layout = view.findViewById(R.id.add_comment);
        comment_one_reply = view.findViewById(R.id.comment_one_reply);
        comment_two_reply = view.findViewById(R.id.comment_two_reply);
        comment_one_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BudzReplyReviewAlertDialog budzReplyReviewAlertDialog = BudzReplyReviewAlertDialog.newInstance(SetReviewaAndComments.this, false, reviews.get(0));
                budzReplyReviewAlertDialog.show(((AppCompatActivity) MContext).getSupportFragmentManager(), "pd");
            }
        });
        comment_two_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BudzReplyReviewAlertDialog budzReplyReviewAlertDialog = BudzReplyReviewAlertDialog.newInstance(SetReviewaAndComments.this, false, reviews.get(1));
                budzReplyReviewAlertDialog.show(((AppCompatActivity) MContext).getSupportFragmentManager(), "pd");
            }
        });
        question_detail_character_counter = view.findViewById(R.id.question_detail_character_counter);
        edit_one = view.findViewById(R.id.edit_one);
        edit_two = view.findViewById(R.id.edit_two);
        report_one = view.findViewById(R.id.report_one);
        report_two = view.findViewById(R.id.report_two);
        ic_edit_one = view.findViewById(R.id.ic_edit_one);
        ic_edit_two = view.findViewById(R.id.ic_edit_two);
        ic_delete_one = view.findViewById(R.id.ic_delete_one);
        ic_delete_two = view.findViewById(R.id.ic_delete_two);
        Report_it_one = view.findViewById(R.id.repote_it_item_one);
        Report_it_two = view.findViewById(R.id.repote_it_item_two);
        Report_it_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getUser_id() != reviews.get(0).getUser_id()) {
                    if (reviews.get(0).getIs_user_flaged_count() == 0) {
                        ArrayList<ReportQuestionListDataModel> dataModels = new ArrayList<>();
                        dataModels.add(new ReportQuestionListDataModel("Nudity or sexual content", false));
                        dataModels.add(new ReportQuestionListDataModel("Harassment or hate speech", false));
                        dataModels.add(new ReportQuestionListDataModel("Threatening, violent, or concerning", false));
                        dataModels.add(new ReportQuestionListDataModel("Spam", false));
                        dataModels.add(new ReportQuestionListDataModel("Offensive", false));
                        dataModels.add(new ReportQuestionListDataModel("Unrelated", false));
                        if (budz_map_report.isSlide()) {
                            budz_map_report.SlideUp();
                        } else {
                            budz_map_report.SlideDown(0, dataModels, SetReviewaAndComments.this, "budz");
                        }
                    } else {
                        CustomeToast.ShowCustomToast(context, "You already report this review.", Gravity.TOP);
                    }
                } else {
//                    BudzEditReviewAlertDialog
//                            .newInstance(SetReviewaAndComments.this, false, reviews.get(0))
//                            .show(((AppCompatActivity) MContext).getSupportFragmentManager(), "pd");

//                    CustomeToast.ShowCustomToast(context, "You can't report your own review.", Gravity.TOP);
                }
            }
        });
        ic_edit_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudzReplyReviewAlertDialog budzReplyReviewAlertDialog = BudzReplyReviewAlertDialog.newInstance(SetReviewaAndComments.this, false, reviews.get(0), true);
                budzReplyReviewAlertDialog.show(((AppCompatActivity) MContext).getSupportFragmentManager(), "pd");

            }
        });
        ic_edit_reply_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudzReplyReviewAlertDialog budzReplyReviewAlertDialog = BudzReplyReviewAlertDialog.newInstance(SetReviewaAndComments.this, false, reviews.get(1), true);
                budzReplyReviewAlertDialog.show(((AppCompatActivity) MContext).getSupportFragmentManager(), "pd");

            }
        });
        ic_edit_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudzEditReviewAlertDialog
                        .newInstance(SetReviewaAndComments.this, false, reviews.get(0))
                        .show(((AppCompatActivity) MContext).getSupportFragmentManager(), "pd");

            }
        });
        ic_edit_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudzEditReviewAlertDialog
                        .newInstance(SetReviewaAndComments.this, false, reviews.get(1))
                        .show(((AppCompatActivity) MContext).getSupportFragmentManager(), "pd");
            }
        });
        ic_delete_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(MContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this review?")
                        .setConfirmText("Yes, delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                try {
                                    new VollyAPICall(MContext
                                            , true
                                            , URL.delete_budz_review
                                            , new JSONObject().put("review_id", review.get(0).getId())
                                            , user.getSession_key()
                                            , Request.Method.POST
                                            , SetReviewaAndComments.this, APIActions.ApiActions.delete_budz_review);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
        });
        ic_delete_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(MContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this review?")
                        .setConfirmText("Yes, delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                try {
                                    new VollyAPICall(MContext
                                            , true
                                            , URL.delete_budz_review
                                            , new JSONObject().put("review_id", review.get(1).getId())
                                            , user.getSession_key()
                                            , Request.Method.POST
                                            , SetReviewaAndComments.this, APIActions.ApiActions.delete_budz_review);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
        });
        Report_it_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getUser_id() != reviews.get(1).getUser_id()) {
                    if (reviews.get(1).getIs_user_flaged_count() == 0) {
                        ArrayList<ReportQuestionListDataModel> dataModels = new ArrayList<>();
                        dataModels.add(new ReportQuestionListDataModel("Nudity or sexual content", false));
                        dataModels.add(new ReportQuestionListDataModel("Harassment or hate speech", false));
                        dataModels.add(new ReportQuestionListDataModel("Threatening, violent, or concerning", false));
                        dataModels.add(new ReportQuestionListDataModel("Spam", false));
                        dataModels.add(new ReportQuestionListDataModel("Offensive", false));
                        dataModels.add(new ReportQuestionListDataModel("Unrelated", false));
                        if (budz_map_report.isSlide()) {
                            budz_map_report.SlideUp();
                        } else {
                            budz_map_report.SlideDown(1, dataModels, SetReviewaAndComments.this, "budz");
                        }
                    } else {
                        CustomeToast.ShowCustomToast(context, "You already report this review.", Gravity.TOP);
                    }
                } else {
//                    BudzEditReviewAlertDialog
//                            .newInstance(SetReviewaAndComments.this, false, reviews.get(0))
//                            .show(((AppCompatActivity) MContext).getSupportFragmentManager(), "pd");

//                    CustomeToast.ShowCustomToast(context, "You can't report your own review.", Gravity.TOP);
                }
            }
        });
        Comment_image_layout = view.findViewById(R.id.comment_image_layout);
        Comment_image_layout.setVisibility(View.GONE);
        commment_text = view.findViewById(R.id.commment_text);
        commment_text.clearFocus();
//        commment_text.setInputType(InputType.TYPE_NULL);
        commment_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                question_detail_character_counter.setText("Max. " + s.length() + "/500 Characters");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        commment_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                commment_text.setInputType(InputType.T);
                commment_text.requestFocus();
                return false;
            }
        });
        commment_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        commment_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(commment_text.length() > 0)) {
                    commment_text.setText(commment_text.getText().toString());
                }
            }
        });
        attachment_name = view.findViewById(R.id.attachment_name);
        attachement_img = view.findViewById(R.id.attachement_img);
        attatchment_cross = view.findViewById(R.id.attatchment_cross);
        upload_comment_image = view.findViewById(R.id.upload_comment_image);
        Attach_image_line = view.findViewById(R.id.attatch_img_line);
        Attach_image_line.setVisibility(View.GONE);
        upload_comment_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", true);
                fragment.startActivityForResult(intent, 1200);
            }
        });

        attatchment_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Attatchment_file_path = "";
                Attatchment_file_type = "";
                Comment_image_layout.setVisibility(View.GONE);
                Attach_image_line.setVisibility(View.GONE);
            }
        });

        rating_bar = view.findViewById(R.id.go_rating);
        rating_bar.setRating(5);// < 1 ? 1: rating_bar.getRating()
        rating_bar.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
//                if (rating < 1) {
//                    simpleRatingBar.setRating(1);
//                    rating_bar.setRating(1);
//                }
            }
        });
        Submit_comment = view.findViewById(R.id.submit_commnet);
        Submit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commment_text.getText().toString().trim().length() > 0) {
                    if (Attatchment_file_path.length() > 4) {
                        try {
                            if (rating_bar.getRating() >= 1) {
                                JSONArray parameter = new JSONArray();
                                parameter.put("sub_user_id");
                                parameter.put("review");
                                parameter.put("rating");
                                JSONArray values = new JSONArray();
                                values.put(budz_map_item_clickerd_dataModel.getId());
                                values.put(commment_text.getText().toString());
                                values.put(rating_bar.getRating() < 1 ? 1 : rating_bar.getRating());
                                new UploadFileWithProgress(MContext, true, URL.add_budz_review, Attatchment_file_path, Attatchment_file_type, values, parameter, null, SetReviewaAndComments.this, add_budz_review).execute();
                            } else {
                                CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Rating is mandatory!", Gravity.TOP);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        JSONObject jsonObject = new JSONObject();

                        try {
                            if (rating_bar.getRating() >= 1) {
                                jsonObject.put("sub_user_id", budz_map_item_clickerd_dataModel.getId());
                                jsonObject.put("rating", rating_bar.getRating() < 1 ? 1 : rating_bar.getRating());
                                jsonObject.put("review", commment_text.getText().toString());
                                new VollyAPICall(MContext, true, URL.add_budz_review, jsonObject, user.getSession_key(), Request.Method.POST, SetReviewaAndComments.this, add_budz_review);
                            } else {
                                CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Rating is mandatory!", Gravity.TOP);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (commment_text.getText().toString().trim().length() == 0) {
                        CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Comment Field Empty!", Gravity.TOP);
                    }

                }
            }
        });

        Full_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reviews.size() > 2) {
                    if (fragmentVar instanceof DetailsBusinessInfoTabFragment) {
                        DetailsBusinessInfoTabFragment.isFresh = true;
                    } else if (fragmentVar instanceof DetailsBusinessInfoMedicalTabFragment) {
                        DetailsBusinessInfoMedicalTabFragment.isFresh = true;
                    } else if (fragmentVar instanceof DetailsBusinessInfoEventTabFragment) {
                        DetailsBusinessInfoEventTabFragment.isFresh = true;
                    } else if (fragmentVar instanceof DetailsBusinessInfoCannabitesTabFragment) {
                        DetailsBusinessInfoCannabitesTabFragment.isFresh = true;
                    }
                    BudzMapCommentFullView.reviews = reviews;
                    GoTo(context, BudzMapCommentFullView.class);
                } else if (reviews.size() == 0) {
                    CustomeToast.ShowCustomToast(context, "No review added!", Gravity.TOP);
                }
            }
        });
        if (budz_map_item_clickerd_dataModel != null) {
            if (compareID == user.getUser_id()) {
                comment_one_reply.setVisibility(View.VISIBLE);
                comment_two_reply.setVisibility(View.VISIBLE);
                comment_section.setVisibility(View.GONE);
                Add_Comment_Layout.setVisibility(View.GONE);
            } else {
                comment_one_reply.setVisibility(View.INVISIBLE);
                comment_two_reply.setVisibility(View.INVISIBLE);
                if (this.userCount == 0) {
                    comment_section.setVisibility(View.VISIBLE);
                    Add_Comment_Layout.setVisibility(View.VISIBLE);
                } else {
                    comment_section.setVisibility(View.GONE);
                    Add_Comment_Layout.setVisibility(View.GONE);
                }
            }

//            }
        } else {
            if (this.userCount == 0) {
                comment_section.setVisibility(View.VISIBLE);
                Add_Comment_Layout.setVisibility(View.VISIBLE);
            } else {
                comment_section.setVisibility(View.GONE);
                Add_Comment_Layout.setVisibility(View.GONE);
            }
        }
        Add_Comment_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        main_scroll_view.fullScroll(View.FOCUS_DOWN);
                    }
                }, 50);
//                main_scroll_view.smoothScrollTo();
            }
        });
        if (reviews != null) {
            SetTopTwoComments(reviews.size(), reviews);
        }
    }

    public Drawable GetRounderDarbable(Bitmap bitmapOrg) {
        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
        return new BitmapDrawable(MContext.getResources(), bitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        BudzMapDetailsActivity.isBackFromCameraActivity = true;
        if (resultCode == RESULT_OK && requestCode == 1200) {
            if (data.getExtras().getBoolean("isVideo")) {
                Attatchment_file_path = data.getExtras().getString("file_path_arg");
                Attatchment_file_type = "video";
                Comment_image_layout.setVisibility(View.VISIBLE);
                Attach_image_line.setVisibility(View.VISIBLE);
                attachment_name.setText(new File(data.getExtras().getString("file_path_arg")).getName());
                if (data.getExtras().getBoolean("camera_video")) {
                    final String filePath = data.getExtras().getString("file_path_arg");
                    video_processing = ProgressDialogVideoProcessing.newInstance();
                    video_processing.show(((FragmentActivity) MContext).getSupportFragmentManager(), "pd");
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            new VideoProcessing().execute(filePath);
                        }
                    }, 200);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            String filePath = data.getExtras().getString("file_path_arg");
                            Bitmap video_thumbnil = getVideoThumbnail(filePath);
                            video_thumbnil = checkRotationVideo(video_thumbnil, filePath);
                            if (video_thumbnil != null) {
                                video_thumbnil = Bitmap.createScaledBitmap(video_thumbnil, 300, 300, false);
                                int corner_radious = (video_thumbnil.getWidth() * 10) / 100;
                                Bitmap bitmap = getRoundedCornerBitmap(video_thumbnil, corner_radious);
                                attachement_img.setBackground(new BitmapDrawable(MContext.getResources(), video_thumbnil));
                            } else {
                                Drawable d = ContextCompat.getDrawable(MContext, R.drawable.test_img);
                                Bitmap bitmapOrg = ((BitmapDrawable) d).getBitmap();
                                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                int corner_radious = (bitmapOrg.getWidth() * 6) / 100;
                                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                attachement_img.setBackground(d);
                            }
                        }
                    }, 10);
                }
            } else {
                Attatchment_file_path = data.getExtras().getString("file_path_arg");
                Attatchment_file_type = "image";
                Comment_image_layout.setVisibility(View.VISIBLE);
                Attach_image_line.setVisibility(View.VISIBLE);
                Log.d("paths", data.getExtras().getString("file_path_arg"));
                Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
                bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                attachement_img.setBackground(GetRounderDarbable(bitmapOrg));
                attachment_name.setText(new File(data.getExtras().getString("file_path_arg")).getName());
            }
        }

    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("Response", response);
        if (apiActions == add_budz_review || apiActions == APIActions.ApiActions.add_budz_review_reply) {
            if (fragmentVar instanceof DetailsBusinessInfoTabFragment) {
                DetailsBusinessInfoTabFragment.isFresh = true;
            } else if (fragmentVar instanceof DetailsBusinessInfoMedicalTabFragment) {
                DetailsBusinessInfoMedicalTabFragment.isFresh = true;
            } else if (fragmentVar instanceof DetailsBusinessInfoEventTabFragment) {
                DetailsBusinessInfoEventTabFragment.isFresh = true;
            } else if (fragmentVar instanceof DetailsBusinessInfoCannabitesTabFragment) {
                DetailsBusinessInfoCannabitesTabFragment.isFresh = true;
            }
            fragmentVar.onResume();
            if (apiActions == APIActions.ApiActions.add_budz_review_reply) {
                CustomeToast.ShowCustomToast(MContext, "Replied Successfully!", Gravity.TOP);
            } else {
                CustomeToast.ShowCustomToast(MContext, "Review Added Successfully!", Gravity.TOP);
            }
            Attatchment_file_path = "";
            Attatchment_file_type = "";
            Comment_image_layout.setVisibility(View.GONE);
            Attach_image_line.setVisibility(View.GONE);
            commment_text.setText("");
            rating_bar.setRating(1);
            rating = 1;
            BudzMapHomeFragment.RefreshData();
        } else if (apiActions == delete_budz_review ||
                apiActions == add_budz_review) {
            if (fragmentVar instanceof DetailsBusinessInfoTabFragment) {
                DetailsBusinessInfoTabFragment.isFresh = true;
            } else if (fragmentVar instanceof DetailsBusinessInfoMedicalTabFragment) {
                DetailsBusinessInfoMedicalTabFragment.isFresh = true;
            } else if (fragmentVar instanceof DetailsBusinessInfoEventTabFragment) {
                DetailsBusinessInfoEventTabFragment.isFresh = true;
            } else if (fragmentVar instanceof DetailsBusinessInfoCannabitesTabFragment) {
                DetailsBusinessInfoCannabitesTabFragment.isFresh = true;
            }
            fragmentVar.onResume();
        } else if (apiActions == APIActions.ApiActions.liked_count_done) {
            if (fragmentVar instanceof DetailsBusinessInfoTabFragment) {
                DetailsBusinessInfoTabFragment.isFresh = true;
            } else if (fragmentVar instanceof DetailsBusinessInfoMedicalTabFragment) {
                DetailsBusinessInfoMedicalTabFragment.isFresh = true;
            } else if (fragmentVar instanceof DetailsBusinessInfoEventTabFragment) {
                DetailsBusinessInfoEventTabFragment.isFresh = true;
            } else if (fragmentVar instanceof DetailsBusinessInfoCannabitesTabFragment) {
                DetailsBusinessInfoCannabitesTabFragment.isFresh = true;
            }
            fragmentVar.onResume();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("Response", response);

    }

    @Override
    public void OnSnedClicked(JSONObject data, int position) {
        if (position == 0) {
            comment_one_report_icon.setImageResource(R.drawable.ic_flag_budz);
            comment_one_report_text.setTextColor(Color.parseColor("#932a88"));
        } else {
            comment_two_report_icon.setImageResource(R.drawable.ic_flag_budz);
            comment_two_report_text.setTextColor(Color.parseColor("#932a88"));
        }
        reviews.get(position).setIs_user_flaged_count(1);
        JSONObject object = new JSONObject();
        try {
            object.put("review_id", reviews.get(position).getId());
            object.put("reason", data.getString("reason"));
            new VollyAPICall(MContext, false, URL.add_budz_review_flag, object, user.getSession_key(), Request.Method.POST, SetReviewaAndComments.this, add_budz_review_flag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSubmitReview(BudzEditReviewAlertDialog dialog, JSONObject jsonObject) {
        dialog.dismiss();
        try {
            if (jsonObject.getBoolean("isVideo")) {
                JSONArray parameter = new JSONArray();
                JSONArray values = new JSONArray();
                parameter = jsonObject.getJSONArray("param");
                values = jsonObject.getJSONArray("value");
                String Attatchment_file_path = jsonObject.getString("path");
                String Attatchment_file_type = jsonObject.getString("type");
//                Refressh_Layout.setVisibility(View.VISIBLE);
                new UploadFileWithProgress(MContext, true, URL.add_budz_review, Attatchment_file_path, Attatchment_file_type, values, parameter, null, SetReviewaAndComments.this, add_budz_review).execute();
            } else {
                new VollyAPICall(MContext, true, URL.add_budz_review, jsonObject, user.getSession_key(), Request.Method.POST, SetReviewaAndComments.this, add_budz_review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCross(BudzEditReviewAlertDialog dialog) {

    }

    @Override
    public void onSubmitReplyReview(BudzReplyReviewAlertDialog dialog, JSONObject jsonObject) {

        dialog.dismiss();
        new VollyAPICall(MContext, true, URL.add_budz_review_reply, jsonObject, user.getSession_key(), Request.Method.POST, SetReviewaAndComments.this, APIActions.ApiActions.add_budz_review_reply);


    }

    @Override
    public void onCross(BudzReplyReviewAlertDialog dialog) {

    }

    public void InitView(View view) {
        comment_one_user_img = view.findViewById(R.id.comment_one_user_img);
        profile_img_topi = view.findViewById(R.id.profile_img_topi);
        comment_one_user_name = view.findViewById(R.id.comment_one_user_name);
        comment_one_comment_text = view.findViewById(R.id.comment_one_comment_text);
        comment_one_date = view.findViewById(R.id.comment_one_date);
        comment_one_rating_count = view.findViewById(R.id.comment_one_rating_count);
        comment_one_rating_img = view.findViewById(R.id.comment_one_rating_img);
        comment_one_media_type_icon = view.findViewById(R.id.comment_one_video_icon);

        comment_one_share = view.findViewById(R.id.comment_one_share);
        comment_one_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                try {
                    object.put("id", reviews.get(0).getId());
                    object.put("type", "Budz Reviews");
                    object.put("content", budz_map_item_clickerd_dataModel.getTitle());
                    object.put("url", sharedBaseUrl + "/get-budz?business_id=" + budz_map_item_clickerd_dataModel.getId() + "&business_type_id=" + budz_map_item_clickerd_dataModel.getBusiness_type_id());
                    object.put("BudzCome", "");
                    object.put("msg", "Check out Healing Budz for your smartphone: " + sharedBaseUrl + "/get-budz?business_id=" + budz_map_item_clickerd_dataModel.getId() + "&business_type_id=" + budz_map_item_clickerd_dataModel.getBusiness_type_id());//"/get-budz-review/" + reviews.get(0).getId() + "/" + budz_map_item_clickerd_dataModel.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareIntent.ShareHBContent((Activity) view.getContext(), object);
            }
        });

        Comment_one_img = view.findViewById(R.id.comment_attachment_one);
        comment_one_report_icon = view.findViewById(R.id.comment_one_report_icon);
        comment_one_report_text = view.findViewById(R.id.comment_one_report_text);
        First_Attach = view.findViewById(R.id.first_attttach);
        comment_two_user_img = view.findViewById(R.id.comment_two_user_img);
        profile_img_topi_2 = view.findViewById(R.id.profile_img_topi_2);
        comment_two_user_name = view.findViewById(R.id.comment_two_user_name);
        comment_two_comment_text = view.findViewById(R.id.comment_two_comment_text);
        comment_two_date = view.findViewById(R.id.comment_two_date);
        comment_two_rating_count = view.findViewById(R.id.comment_two_rating_count);
        comment_two_rating_img = view.findViewById(R.id.comment_two_rating_img);
        comment_two_media_type_icon = view.findViewById(R.id.comment_two_video_icon);
        comment_two_share = view.findViewById(R.id.comment_two_share);
        comment_two_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                try {
                    object.put("id", reviews.get(1).getId());
                    object.put("type", "Budz Reviews");
                    object.put("content", budz_map_item_clickerd_dataModel.getTitle());
                    object.put("url", sharedBaseUrl + "/get-budz?business_id=" + budz_map_item_clickerd_dataModel.getId() + "&business_type_id=" + budz_map_item_clickerd_dataModel.getBusiness_type_id());
                    object.put("BudzCome", "");
                    object.put("msg", "Check out Healing Budz for your smartphone: " + sharedBaseUrl + "/get-budz?business_id=" + budz_map_item_clickerd_dataModel.getId() + "&business_type_id=" + budz_map_item_clickerd_dataModel.getBusiness_type_id());//"/get-budz-review/" + reviews.get(0).getId() + "/" + budz_map_item_clickerd_dataModel.getId());// "/get-budz-review/" + reviews.get(1).getId() + "/" + budz_map_item_clickerd_dataModel.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareIntent.ShareHBContent((Activity) view.getContext(), object);
            }
        });
        Comment_two_img = view.findViewById(R.id.comment_attachment_two);
        review_count = view.findViewById(R.id.review_count);
        first_review_layout = view.findViewById(R.id.first_review_layout);
        second_review_layout = view.findViewById(R.id.second_review_layout);
        comment_two_report_icon = view.findViewById(R.id.comment_two_report_icon);
        comment_two_report_text = view.findViewById(R.id.comment_two_report_text);
        Second_Attach = view.findViewById(R.id.second_attttach);
        Full_comments = view.findViewById(R.id.open_full_cooment);


    }

    public void SetTopTwoComments(int cont, ArrayList<BudzMapReviews> reviews) {
        if (cont == 0) {
            review_count.setText(0 + " Reviews");
            first_review_layout.setVisibility(View.GONE);
            second_review_layout.setVisibility(View.GONE);
        } else if (cont == 1) {
            first_review_layout.setVisibility(View.VISIBLE);
            second_review_layout.setVisibility(View.GONE);
            FirstReview(reviews);
        } else if (cont >= 2) {
            first_review_layout.setVisibility(View.VISIBLE);
            second_review_layout.setVisibility(View.VISIBLE);
            FirstReview(reviews);
            SecondReview(reviews);
        }
    }

    public void FirstReview(final ArrayList<BudzMapReviews> reviews) {
        final BudzMapReviews first_review = reviews.get(0);
        if (first_review.isLiked()) {
            comment_one_like.setColorFilter(Color.parseColor("#932a88"), PorterDuff.Mode.SRC_IN);
            first_count.setTextColor(Color.parseColor("#932a88"));
        } else {
            first_count.setTextColor(Color.parseColor("#FFFFFF"));
            comment_one_like.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        }

        first_count.setText(MessageFormat.format("{0}", first_review.getTotal_review()));
        comment_one_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = "0";
                if (first_review.isLiked()) {
                    val = "0";
                    first_review.setLiked(false);
                    reviews.get(0).setLiked(false);
                    comment_one_like.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                } else {
                    val = "1";
                    first_review.setLiked(true);
                    reviews.get(0).setLiked(true);
                    comment_one_like.setColorFilter(Color.parseColor("#932a88"), PorterDuff.Mode.SRC_IN);
                }
                try {
                    new VollyAPICall(v.getContext()
                            , true
                            , URL.add_budz_review_like
                            , new JSONObject().put("review_id", first_review.getId()).put("budz_id", budz_map_item_clickerd_dataModel.getId()).put("like_val", val)
                            , Splash.user.getSession_key()
                            , Request.Method.POST
                            , new APIResponseListner() {
                        @Override
                        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                            if (fragmentVar instanceof DetailsBusinessInfoTabFragment) {
                                DetailsBusinessInfoTabFragment.isFresh = true;
                            } else if (fragmentVar instanceof DetailsBusinessInfoMedicalTabFragment) {
                                DetailsBusinessInfoMedicalTabFragment.isFresh = true;
                            } else if (fragmentVar instanceof DetailsBusinessInfoEventTabFragment) {
                                DetailsBusinessInfoEventTabFragment.isFresh = true;
                            } else if (fragmentVar instanceof DetailsBusinessInfoCannabitesTabFragment) {
                                DetailsBusinessInfoCannabitesTabFragment.isFresh = true;
                            }
                            fragmentVar.onResume();
                        }

                        @Override
                        public void onRequestError(String response, APIActions.ApiActions apiActions) {

                        }
                    }, testAPI);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        if (first_review.getUser_id() == user.getUser_id()) {
            report_one.setVisibility(View.GONE);
            edit_one.setVisibility(View.VISIBLE);
            if (compareID == user.getUser_id()) {
                comment_one_reply.setVisibility(View.VISIBLE);

//                comment_two_reply.setVisibility(View.GONE);
            } else {
//                comment_one_reply.setVisibility(View.VISIBLE);
//                comment_two_reply.setVisibility(View.VISIBLE);
            }
            if (first_review.getReply() != null) {
                comment_one_reply.setVisibility(View.INVISIBLE);
                if (first_review.getReply() != null) {
                    comment_one_reply.setVisibility(View.INVISIBLE);
                    reply_layout_one.setVisibility(View.VISIBLE);
//                    reply.setText(first_review.getReply().getReply());
                    MakeKeywordClickableText(reply.getContext(), first_review.getReply().getReply(), reply);
                    reply_date.setText(DateConverter.getPrettyTime(first_review.getReply().getCreatedAt()));
                    if (compareID == user.getUser_id() && first_review.getReply().getUserId() == user.getUser_id()) {
                        edit_reply.setVisibility(View.VISIBLE);
                    } else {
                        edit_reply.setVisibility(View.GONE);
                    }
                } else {
                    reply_layout_one.setVisibility(View.GONE);
                    comment_one_reply.setVisibility(View.VISIBLE);
                }
            } else {
                reply_layout_one.setVisibility(View.GONE);
                if (first_review.getUser_id() == user.getUser_id()) {
                    if (compareID == user.getUser_id()) {
                        comment_one_reply.setVisibility(View.VISIBLE);
                    }
                }
            }
//            report_one.setVisibility(View.VISIBLE);
//            edit_one.setVisibility(View.GONE);
        } else {
            if (first_review.getReply() != null) {
                comment_one_reply.setVisibility(View.INVISIBLE);
                if (first_review.getReply() != null) {
                    comment_one_reply.setVisibility(View.INVISIBLE);
                    reply_layout_one.setVisibility(View.VISIBLE);
//                    reply.setText(first_review.getReply().getReply());
                    MakeKeywordClickableText(reply.getContext(), first_review.getReply().getReply(), reply);
                    reply_date.setText(DateConverter.getPrettyTime(first_review.getReply().getCreatedAt()));
                    if (compareID == user.getUser_id() && first_review.getReply().getUserId() == user.getUser_id()) {
                        edit_reply.setVisibility(View.VISIBLE);
                    } else {
                        edit_reply.setVisibility(View.GONE);
                    }
                } else {
                    reply_layout_one.setVisibility(View.GONE);
                    comment_one_reply.setVisibility(View.VISIBLE);
                }
            } else {
                reply_layout_one.setVisibility(View.GONE);
                if (first_review.getUser_id() == user.getUser_id()) {
                    if (compareID == user.getUser_id()) {
                        comment_one_reply.setVisibility(View.VISIBLE);
                    }
                }
            }
            report_one.setVisibility(View.VISIBLE);
            edit_one.setVisibility(View.GONE);
        }

        comment_one_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNewScreen = true;
                GoToProfile(comment_one_user_img.getContext(), first_review.getUser_id());
            }
        });

        comment_one_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNewScreen = true;
                GoToProfile(comment_one_user_name.getContext(), first_review.getUser_id());
            }
        });
        comment_one_user_name.setText(first_review.getUser_first_name());
//        comment_one_user_name.setTextColor(Color.parseColor(Utility.getBudColor(first_review.getUser_point())));
        if (first_review.getUser_image_path() != null && first_review.getUser_image_path().length() > 8) {
            SetImageDrawable(comment_one_user_img, first_review.getUser_image_path());
        } else {
            SetImageDrawable(comment_one_user_img, first_review.getUser_avatar());
        }
        if (first_review.getSpecial_icon() != null && first_review.getSpecial_icon().length() > 8) {
            profile_img_topi.setVisibility(View.VISIBLE);
            SetImageDrawable(profile_img_topi, first_review.getSpecial_icon());
        } else {
            profile_img_topi.setVisibility(View.GONE);
//            SetImageDrawable(comment_one_user_img, first_review.getUser_avatar());
        }
//        comment_one_comment_text.setText(first_review.getReview());
        MakeKeywordClickableText(comment_one_comment_text.getContext(), first_review.getReview(), comment_one_comment_text);
        comment_one_date.setText(DateConverter.getCustomDateString(first_review.getCreated_at()));
        comment_one_rating_count.setText(first_review.getRating() + "");
        if (first_review.getRating() > 0) {
            comment_one_rating_img.setImageResource(R.drawable.star_fill);
        } else {
            comment_one_rating_img.setImageResource(R.drawable.star_empty);
        }
        if (first_review.getAttatchment_type() != null) {
            if (first_review.getAttatchment_type().equalsIgnoreCase("image")) {
                comment_one_media_type_icon.setVisibility(View.GONE);
                First_Attach.setVisibility(View.VISIBLE);
                Comment_one_img.setVisibility(View.VISIBLE);
                comment_one_media_type_icon.setImageResource(R.drawable.ic_gallery_icon);
                SetImageBackground(Comment_one_img, first_review.getAttatchment_path());
                Comment_one_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = "";
                        path = URL.images_baseurl + first_review.getAttatchment_path();
                        Intent intent = new Intent(MContext, MediPreview.class);
                        intent.putExtra("path", path);
                        intent.putExtra("isvideo", false);
                        MContext.startActivity(intent);
                    }
                });
            } else if (first_review.getAttatchment_type().equalsIgnoreCase("video")) {
                comment_one_media_type_icon.setVisibility(View.VISIBLE);
                Comment_one_img.setVisibility(View.VISIBLE);
                First_Attach.setVisibility(View.VISIBLE);
                comment_one_media_type_icon.setImageResource(R.drawable.ic_video_play_icon);
                SetImageBackground(Comment_one_img, first_review.getAttatchment_poster());
                Comment_one_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = "";
                        path = URL.videos_baseurl + first_review.getAttatchment_path();
                        Intent intent = new Intent(MContext, MediPreview.class);
                        intent.putExtra("path", path);
                        intent.putExtra("isvideo", true);
                        MContext.startActivity(intent);
                    }
                });
            } else {
                comment_one_media_type_icon.setVisibility(View.GONE);
                Comment_one_img.setVisibility(View.GONE);
                First_Attach.setVisibility(View.GONE);
            }
        } else {
            comment_one_media_type_icon.setVisibility(View.GONE);
            Comment_one_img.setVisibility(View.GONE);
            First_Attach.setVisibility(View.GONE);
        }

        review_count.setText(reviews.size() + " Reviews");

        if (first_review.getIs_user_flaged_count() == 1) {
            comment_one_report_icon.setImageResource(R.drawable.ic_flag_budz);
            comment_one_report_text.setTextColor(Color.parseColor("#932a88"));

        } else {
            comment_one_report_icon.setImageResource(R.drawable.ic_flag_white);
            comment_one_report_text.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public void SecondReview(final ArrayList<BudzMapReviews> reviews) {
        final BudzMapReviews second_review = reviews.get(1);
        if (second_review.isLiked()) {
            comment_two_like.setColorFilter(Color.parseColor("#932a88"), PorterDuff.Mode.SRC_IN);
            second_count.setTextColor(Color.parseColor("#932a88"));
        } else {
            second_count.setTextColor(Color.parseColor("#FFFFFF"));
            comment_two_like.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        }
        second_count.setText(MessageFormat.format("{0}", second_review.getTotal_review()));
        comment_two_user_name.setText("TEST");
        comment_two_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNewScreen = true;
                GoToProfile(comment_two_user_name.getContext(), second_review.getUser_id());
            }
        });
        comment_two_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = "0";
                if (second_review.isLiked()) {
                    val = "0";
                    second_review.setLiked(false);
                    reviews.get(1).setLiked(false);
                    comment_two_like.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                } else {
                    val = "1";
                    second_review.setLiked(true);
                    reviews.get(1).setLiked(true);
                    comment_two_like.setColorFilter(Color.parseColor("#932a88"), PorterDuff.Mode.SRC_IN);
                }
                try {
                    new VollyAPICall(v.getContext()
                            , true
                            , URL.add_budz_review_like
                            , new JSONObject().put("review_id", second_review.getId()).put("budz_id", budz_map_item_clickerd_dataModel.getId()).put("like_val", val)
                            , Splash.user.getSession_key()
                            , Request.Method.POST
                            , new APIResponseListner() {
                        @Override
                        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                            if (fragmentVar instanceof DetailsBusinessInfoTabFragment) {
                                DetailsBusinessInfoTabFragment.isFresh = true;
                            } else if (fragmentVar instanceof DetailsBusinessInfoMedicalTabFragment) {
                                DetailsBusinessInfoMedicalTabFragment.isFresh = true;
                            } else if (fragmentVar instanceof DetailsBusinessInfoEventTabFragment) {
                                DetailsBusinessInfoEventTabFragment.isFresh = true;
                            } else if (fragmentVar instanceof DetailsBusinessInfoCannabitesTabFragment) {
                                DetailsBusinessInfoCannabitesTabFragment.isFresh = true;
                            }
                            fragmentVar.onResume();
                        }

                        @Override
                        public void onRequestError(String response, APIActions.ApiActions apiActions) {

                        }
                    }, testAPI);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        comment_two_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNewScreen = true;
                GoToProfile(comment_two_user_img.getContext(), second_review.getUser_id());
            }
        });
        if (second_review.getUser_id() == user.getUser_id()) {
            report_two.setVisibility(View.GONE);
            edit_two.setVisibility(View.VISIBLE);
            if (compareID == user.getUser_id()) {
//                comment_one_reply.setVisibility(View.GONE);
                comment_two_reply.setVisibility(View.VISIBLE);

            } else {

//                comment_one_reply.setVisibility(View.VISIBLE);
//                comment_two_reply.setVisibility(View.VISIBLE);
            }
            if (second_review.getReply() != null) {
                comment_two_reply.setVisibility(View.INVISIBLE);
                if (second_review.getReply() != null) {
                    comment_two_reply.setVisibility(View.INVISIBLE);
                    reply_layout_two.setVisibility(View.VISIBLE);
//                    reply_two.setText(second_review.getReply().getReply());
                    MakeKeywordClickableText(reply_two.getContext(), second_review.getReply().getReply(), reply_two);
                    reply_date_two.setText(DateConverter.getPrettyTime(second_review.getReply().getCreatedAt()));
                    if (compareID == user.getUser_id() && second_review.getReply().getUserId() == user.getUser_id()) {
                        edit_reply_two.setVisibility(View.VISIBLE);
                    } else {
                        edit_reply_two.setVisibility(View.GONE);
                    }
                } else {
                    reply_layout_two.setVisibility(View.GONE);
                    comment_two_reply.setVisibility(View.VISIBLE);
                }
            } else {
                reply_layout_two.setVisibility(View.GONE);
                if (second_review.getUser_id() == user.getUser_id()) {
                    if (compareID == user.getUser_id()) {
                        comment_two_reply.setVisibility(View.VISIBLE);
                    }
                }

            }
//            report_two.setVisibility(View.VISIBLE);
//            edit_two.setVisibility(View.GONE);
        } else {
            if (second_review.getReply() != null) {
                comment_two_reply.setVisibility(View.INVISIBLE);
                if (second_review.getReply() != null) {
                    comment_two_reply.setVisibility(View.INVISIBLE);
                    reply_layout_two.setVisibility(View.VISIBLE);
//                    reply_two.setText(second_review.getReply().getReply());
                    MakeKeywordClickableText(reply_two.getContext(), second_review.getReply().getReply(), reply_two);
                    reply_date_two.setText(DateConverter.getPrettyTime(second_review.getReply().getCreatedAt()));
                    if (compareID == user.getUser_id() && second_review.getReply().getUserId() == user.getUser_id()) {
                        edit_reply_two.setVisibility(View.VISIBLE);
                    } else {
                        edit_reply_two.setVisibility(View.GONE);
                    }
                } else {
                    reply_layout_two.setVisibility(View.GONE);
                    comment_two_reply.setVisibility(View.VISIBLE);
                }
            } else {
                reply_layout_two.setVisibility(View.GONE);
                if (second_review.getUser_id() == user.getUser_id()) {
                    if (compareID == user.getUser_id()) {
                        comment_two_reply.setVisibility(View.VISIBLE);
                    }
                }

            }
            report_two.setVisibility(View.VISIBLE);
            edit_two.setVisibility(View.GONE);
        }

        comment_two_user_name.setText(second_review.getUser_first_name());
        comment_two_user_name.setTextColor(Color.parseColor(Utility.getBudColor(second_review.getUser_point())));
        if (second_review.getUser_image_path() != null && second_review.getUser_image_path().length() > 8) {
            SetImageDrawable(comment_two_user_img, second_review.getUser_image_path());
        } else {
            SetImageDrawable(comment_two_user_img, second_review.getUser_avatar());
        }
        if (second_review.getSpecial_icon() != null && second_review.getSpecial_icon().length() > 8) {
            profile_img_topi_2.setVisibility(View.VISIBLE);
            SetImageDrawable(profile_img_topi_2, second_review.getSpecial_icon());
        } else {
            profile_img_topi_2.setVisibility(View.GONE);
//            SetImageDrawable(comment_one_user_img, first_review.getUser_avatar());
        }
//        SetImageDrawable(comment_two_user_img, reviews.get(1).getUser_image_path());
//        comment_two_comment_text.setText(second_review.getReview());
        MakeKeywordClickableText(comment_two_comment_text.getContext(), second_review.getReview(), comment_two_comment_text);
        comment_two_date.setText(DateConverter.getCustomDateString(second_review.getCreated_at()));
        comment_two_rating_count.setText(second_review.getRating() + "");
        if (second_review.getRating() > 0) {
            comment_two_rating_img.setImageResource(R.drawable.star_fill);
        } else {
            comment_two_rating_img.setImageResource(R.drawable.star_empty);
        }

        if (second_review.getAttatchment_type() != null) {
            if (second_review.getAttatchment_type().equalsIgnoreCase("image")) {
                comment_two_media_type_icon.setVisibility(View.GONE);
                Comment_two_img.setVisibility(View.VISIBLE);
                Second_Attach.setVisibility(View.VISIBLE);
                comment_two_media_type_icon.setImageResource(R.drawable.ic_gallery_icon);
                SetImageBackground(Comment_two_img, second_review.getAttatchment_path());
                Comment_two_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = "";
                        path = URL.images_baseurl + second_review.getAttatchment_path();
                        Intent intent = new Intent(MContext, MediPreview.class);
                        intent.putExtra("path", path);
                        intent.putExtra("isvideo", false);
                        MContext.startActivity(intent);
                    }
                });
            } else if (second_review.getAttatchment_type().equalsIgnoreCase("video")) {
                comment_two_media_type_icon.setVisibility(View.VISIBLE);
                Comment_two_img.setVisibility(View.VISIBLE);
                Second_Attach.setVisibility(View.VISIBLE);
                comment_two_media_type_icon.setImageResource(R.drawable.ic_video_play_icon);
                SetImageBackground(Comment_two_img, second_review.getAttatchment_poster());
                Comment_two_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = "";
                        path = URL.videos_baseurl + second_review.getAttatchment_path();
                        Intent intent = new Intent(MContext, MediPreview.class);
                        intent.putExtra("path", path);
                        intent.putExtra("isvideo", true);
                        MContext.startActivity(intent);
                    }
                });
            } else {
                comment_two_media_type_icon.setVisibility(View.GONE);
                Comment_two_img.setVisibility(View.GONE);
                Second_Attach.setVisibility(View.GONE);
            }
        } else {
            Second_Attach.setVisibility(View.GONE);
            comment_two_media_type_icon.setVisibility(View.GONE);
            Comment_two_img.setVisibility(View.GONE);
        }
        if (second_review.getIs_user_flaged_count() == 1) {
            comment_two_report_icon.setImageResource(R.drawable.ic_flag_budz);
            comment_two_report_text.setTextColor(Color.parseColor("#932a88"));

        } else {
            comment_two_report_icon.setImageResource(R.drawable.ic_flag_white);
            comment_two_report_text.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public void SetImageBackground(final ImageView imageView, String Path) {
        if (MContext != null) {
            Glide.with(MContext)
                    .load(images_baseurl + Path)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.image_plaecholder_bg).error(R.drawable.noimage)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            try {

                                Bitmap bitmapOrg = resource;
                                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                BitmapDrawable drawable = new BitmapDrawable(imageView.getContext().getResources(), bitmap);
                                imageView.setImageDrawable(null);
                                imageView.setBackground(drawable);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return false;
                        }
                    })
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            Log.d("ready", model);
//                            try {
//                                Drawable d = resource;
//                                Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
//                                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
//                                int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
//                                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
//                                BitmapDrawable drawable = new BitmapDrawable(MContext.getResources(), bitmap);
//                                imageView.setImageDrawable(null);
//                                imageView.setBackground(drawable);
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//                            return false;
//                        }
//                    })
                    .into(400, 400);
        }
    }

    public void SetImageDrawable(final ImageView imageView, String Path) {
        if (MContext != null) {
            if (Path.contains("facebook.com") || Path.contains("https") || Path.contains("http") || Path.contains("google.com") || Path.contains("googleusercontent.com")) {
                Path = Path;
            } else {
                Path = images_baseurl + Path;
            }
            Glide.with(MContext)
                    .load(Path)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.image_plaecholder_bg).error(R.drawable.noimage)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("ready", model);
                            imageView.setImageDrawable(resource);
                            imageView.setBackground(null);
                            return false;
                        }
                    }).into(imageView);

        }

    }

    private class VideoProcessing extends AsyncTask<String, Integer, Bitmap> {
        String path = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap video_thumbnil = null;
            try {
                path = params[0];
                video_thumbnil = getVideoThumbnail(path);
                Log.d("img", video_thumbnil.toString());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return video_thumbnil;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (null != bitmap) {
                video_processing.dismiss();
                bitmap = checkRotationVideo(bitmap, path);
                Bitmap video_thumbnil = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                int corner_radious_plc = (video_thumbnil.getWidth() * 10) / 100;
                Bitmap bitmap_plc = getRoundedCornerBitmap(video_thumbnil, corner_radious_plc);
                Drawable drawable = new BitmapDrawable(MContext.getResources(), bitmap_plc);
                attachement_img.setBackground(new BitmapDrawable(MContext.getResources(), video_thumbnil));
            }
        }

    }


}
