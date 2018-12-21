package com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.tabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.ReportQuestionListDataModel;
import com.codingpixel.healingbudz.DataModel.StrainOverViewDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.Utilities.ProgressBarAnimation;
import com.codingpixel.healingbudz.activity.Registration.AddExpertiseQuestionActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainCommentFullView;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.dialog.StrainEditReviewAlertDialog;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.ExpertAreaRecylerAdapter;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.customeUI.ProgressDialogVideoProcessing;
import com.codingpixel.healingbudz.customeUI.SimpleToolTip.SimpleTooltip;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.customeUI.multiselect.MultiSelectDialog;
import com.codingpixel.healingbudz.customeUI.multiselect.MultiSelectModel;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.interfaces.ShowMoreStrainDetails;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_file_with_progress.UploadFileWithProgress;
import com.codingpixel.healingbudz.static_function.ShareIntent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.Utilities.StrainRatingImage.Strain_Rating;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotationVideo;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity.comment_top_int_y;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity.strainDataModel;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity.strainDetailsActivity_scrollView;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity.strain_report;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.tabs.StrainDetailsTabFragment.strainDiscription;
import static com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.DiscussQuestionFragment.isNewScreen;
import static com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingChatViewActivity.getVideoThumbnail;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.flag_strain_review;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_strain_detail;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.save_survey_answer;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.testAPI;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import com.codingpixel.healingbudz.customeUI.CustomAutoCompleteTextView;

public class StrainOverViewTabFragment extends Fragment implements APIResponseListner, ReportSendButtonLstner, StrainEditReviewAlertDialog.OnDialogFragmentClickListener {
    String survay_question_one = "";
    String survay_question_two = "";
    String survay_question_three = "";
    String survay_question_four = "";
    String survay_question_five = "";
    String survay_question_six = "";
    Button dialouge_box;
    int yes_no = 0;
    TextView all_value_text;
    String yes_for_list = "";
    int dailouge_value_1, dailouge_value_2, dailouge_value_3, dailouge_value_4, dailouge_value_5, dailouge_value_6;

    ArrayList<String> all_array_list = new ArrayList<String>();

    TextView short_description;
    TextView Top_Three_strain;
    ImageView Comment_img, Comment_img2;
    ShowMoreStrainDetails showMoreStrainDetails;
    ImageView Strain_description;
    StrainOverViewDataModel strainOverViewDataModel = new StrainOverViewDataModel();
    ProgressDialogVideoProcessing video_processing;
    LinearLayout Description_Layout;
    LinearLayout Main_Layout, Refressh_Layout;


    LinearLayout Comment_Review_Layout;
    LinearLayout Add_Comment_Layout;
    Button Submit_comment;
    int rating = 5;
    String Attatchment_file_path = "";
    String Attatchment_file_type = "";
    LinearLayout Comment_image_layout;
    TextView attachment_name;
    ImageView attachement_img, attatchment_cross, Strain_rating_one, Strain_rating_two, Strain_rating_three, Strain_rating_four, Strain_rating_five;
    Button upload_comment_image;
    View Attach_image_line;

    EditText commment_text;
    ImageView comment_two_like, comment_one_like, ic_edit_one, ic_edit_two, comment_one_user_img, ic_delete_one, ic_delete_two, comment_one_rating_img, comment_one_media_type_icon, Comment_one_img, comment_two_rating_img,
            comment_two_media_type_icon, Comment_two_img, profile_img_topi_2, profile_img_topi, comment_two_user_img, comment_one_report_icon, comment_two_report_icon;
    TextView comment_one_user_name, comment_one_comment_text, comment_one_date, comment_one_rating_count, comment_two_user_name,
            comment_two_comment_text, comment_two_date, comment_two_rating_count, review_count, comment_one_report_text, comment_two_report_text;

    LinearLayout comment_one_share, comment_two_share, first_review_layout, second_review_layout;
    LinearLayout Report_it_one, Report_it_two, report_one, report_two;

    RelativeLayout edit_one, edit_two;
//

    private static View view;
    LinearLayout Medical_Conditions;
    ProgressBar Medical_conditions_one_progress, Medical_conditions_two_progress, Medical_conditions_three_progress;
    ProgressBar Disease_one_progress, Disease_two_progress, Disease_three_progress;
    TextView Medical_conditions_one_text, Medical_conditions_two_text, Medical_conditions_three_text;
    TextView Disease_one_text, Disease_two_text, Disease_three_text;


    ProgressBar Mode_one_progress, Mode_two_progress, Mode_three_progress;
    TextView Mode_one_text, Mode_two_text, Mode_three_text;

    ProgressBar Negative_effect_one_progress, Negative_effect_two_progress, Negative_effect_three_progress;
    TextView Negative_effect_one_text, Negative_effect_two_text, Negative_effect_three_text;


    ProgressBar Flavour_one_progress, Flavour_two_progress, Flavour_three_progress;
    TextView Flavour_one_text, Flavour_two_text, Flavour_three_text, full_dec;
    Button Start_survay_button;

    RelativeLayout Second_Attach, First_Attach;
    Context context;
    LinearLayout Survay_layout, comment_above, comment_below;
    TextView No_survay_found, first_count, second_count, question_detail_character_counter;
    ImageView img_pending;
    View for_no_survay;
    RelativeLayout mu1, dp1, ms1, ne1, fp1;
    RelativeLayout mu2, dp2, ms2, ne2, fp2;
    RelativeLayout mu3, dp3, ms3, ne3, fp3;
    public static ArrayList<StrainOverViewDataModel.Reviews> commentArray = new ArrayList<>();
    private boolean isReviewAdd = false;
    public static boolean goneToReview = false;

    @SuppressLint("ValidFragment")
    public StrainOverViewTabFragment(ShowMoreStrainDetails showMoreStrainDetails, Context context) {
        this.showMoreStrainDetails = showMoreStrainDetails;
        this.context = context;
    }

    public StrainOverViewTabFragment() {
        this.context = getActivity();
    }

    //Our metrics are calculated from experiences submitted by the users of the Healing Budz community - not a laboratory.
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        goneToReview = false;
        View view = inflater.inflate(R.layout.strain_overview_tab_layout, container, false);
        full_dec = view.findViewById(R.id.full_dec);
        full_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreStrainDetails.ShowDetails();
            }
        });
        HideKeyboard(getActivity());
        CommentView(view);
        InitView(view);
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(getContext(), false, URL.get_strain_detail + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainOverViewTabFragment.this, get_strain_detail);
        this.view = view;
        return view;
    }

    private void CommentView(View view) {
        Top_Three_strain = view.findViewById(R.id.top_3_strain_text);
        first_count = view.findViewById(R.id.first_count);
        second_count = view.findViewById(R.id.second_count);
        question_detail_character_counter = view.findViewById(R.id.question_detail_character_counter);
        comment_above = view.findViewById(R.id.comment_above);
        comment_below = view.findViewById(R.id.comment_below);
        First_Attach = view.findViewById(R.id.first_attttach);
        Second_Attach = view.findViewById(R.id.second_attttach);
        short_description = view.findViewById(R.id.shot_description);
        Strain_description = view.findViewById(R.id.strain_description);
        Description_Layout = view.findViewById(R.id.description_layout);
        Strain_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("bottom", Description_Layout.getTop() + "");
//                strainDetailsActivity_scrollView.smoothScrollTo(0, Description_Layout.getTop());
                StrainDetailsTabFragment.ShowToolTipDialog(view, Gravity.TOP, R.layout.dialogue_layout_over, "Our metrics are calculated from experiences submitted by the users of the Healing Budz community - not a laboratory.");
            }
        });
        Comment_Review_Layout = view.findViewById(R.id.comment_layout_review);
        comment_top_int_y = Comment_Review_Layout.getTop();

        Submit_comment = view.findViewById(R.id.submit_commnet);
        Add_Comment_Layout = view.findViewById(R.id.add_new_reviews);
        Add_Comment_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strainDetailsActivity_scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        Report_it_one = view.findViewById(R.id.repote_it_item_one);
        Report_it_two = view.findViewById(R.id.repote_it_item_two);
        Report_it_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (user.getUser_id() != strainOverViewDataModel.getReviews().get(0).getUser_id()) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("strain_id", strainOverViewDataModel.getReviews().get(0).getStrain_id());
                        jsonObject.put("strain_review_id", strainOverViewDataModel.getReviews().get(0).getId());
                        if (strainOverViewDataModel.getReviews().get(0).getIs_user_flaged_count() == 1) {
//                        comment_one_report_icon.setImageResource(R.drawable.ic_flag_white);
//                        comment_one_report_text.setTextColor(Color.parseColor("#787878"));
//                        strainOverViewDataModel.getReviews().get(0).setIs_user_flaged_count(0);
//                        jsonObject.put("is_flaged", 0);
//                        jsonObject.put("reason", "");
//                        new VollyAPICall(getContext(), false, URL.flag_strain_review, jsonObject, user.getSession_key(), Request.Method.POST, StrainOverViewTabFragment.this, flag_strain_review);
                            CustomeToast.ShowCustomToast(getView().getContext(), "you already reported this review!", Gravity.TOP);
                        } else {
                            ArrayList<ReportQuestionListDataModel> dataModels = new ArrayList<>();
                            dataModels.add(new ReportQuestionListDataModel("Nudity or sexual content", false));
                            dataModels.add(new ReportQuestionListDataModel("Harassment or hate speech", false));
                            dataModels.add(new ReportQuestionListDataModel("Threatening, violent, or concerning", false));
                            dataModels.add(new ReportQuestionListDataModel("Spam", false));
                            dataModels.add(new ReportQuestionListDataModel("Offensive", false));
                            dataModels.add(new ReportQuestionListDataModel("Unrelated", false));
                            if (strain_report.isSlide()) {
                                strain_report.SlideUp();
                            } else {
                                strain_report.SlideDown(0, dataModels, StrainOverViewTabFragment.this, "strain");
                            }
                        }
                    } else {
//                        CustomeToast.ShowCustomToast(context, "You can't flag your own review.", Gravity.TOP);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                strain_report.SlideDown(0);
            }
        });
        Report_it_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (user.getUser_id() != strainOverViewDataModel.getReviews().get(1).getUser_id()) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("strain_id", strainOverViewDataModel.getReviews().get(1).getStrain_id());
                        jsonObject.put("strain_review_id", strainOverViewDataModel.getReviews().get(1).getId());
                        if (strainOverViewDataModel.getReviews().get(1).getIs_user_flaged_count() == 1) {
                            CustomeToast.ShowCustomToast(getView().getContext(), "you already reported this review!", Gravity.TOP);
//                        comment_two_report_icon.setImageResource(R.drawable.ic_flag_white);
//                        comment_two_report_text.setTextColor(Color.parseColor("#787878"));
//                        strainOverViewDataModel.getReviews().get(1).setIs_user_flaged_count(0);
//                        jsonObject.put("is_flaged", 0);
//                        jsonObject.put("reason", "");
//                        new VollyAPICall(getContext(), false, URL.flag_strain_review, jsonObject, user.getSession_key(), Request.Method.POST, StrainOverViewTabFragment.this, flag_strain_review);
                        } else {
                            ArrayList<ReportQuestionListDataModel> dataModels = new ArrayList<>();
                            dataModels.add(new ReportQuestionListDataModel("Nudity or sexual content", false));
                            dataModels.add(new ReportQuestionListDataModel("Harassment or hate speech", false));
                            dataModels.add(new ReportQuestionListDataModel("Threatening, violent, or concerning", false));
                            dataModels.add(new ReportQuestionListDataModel("Spam", false));
                            dataModels.add(new ReportQuestionListDataModel("Offensive", false));
                            dataModels.add(new ReportQuestionListDataModel("Unrelated", false));
                            if (strain_report.isSlide()) {
                                strain_report.SlideUp();
                            } else {
                                strain_report.SlideDown(1, dataModels, StrainOverViewTabFragment.this, "strain");
                            }
                        }
                    } else {
//                        CustomeToast.ShowCustomToast(context, "You can't flag your own review.", Gravity.TOP);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Main_Layout = view.findViewById(R.id.main_layout);
        Main_Layout.setVisibility(GONE);
        Refressh_Layout = view.findViewById(R.id.refresh);
        Refressh_Layout.setVisibility(View.VISIBLE);
        Comment_image_layout = view.findViewById(R.id.comment_image_layout);
        Comment_image_layout.setVisibility(GONE);
        commment_text = view.findViewById(R.id.commment_text);
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
        attachment_name = view.findViewById(R.id.attachment_name);
        attachement_img = view.findViewById(R.id.attachement_img);
        attatchment_cross = view.findViewById(R.id.attatchment_cross);
        upload_comment_image = view.findViewById(R.id.upload_comment_image);
        Attach_image_line = view.findViewById(R.id.attatch_img_line);
        Attach_image_line.setVisibility(GONE);
        upload_comment_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", true);
                startActivityForResult(intent, 1200);
            }
        });

        attatchment_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Attatchment_file_path = "";
                Attatchment_file_type = "";
                Comment_image_layout.setVisibility(GONE);
                Attach_image_line.setVisibility(GONE);
            }
        });
        Strain_rating_one = view.findViewById(R.id.str_rating_one);
        Strain_rating_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Strain_rating_one.setBackgroundColor(Color.parseColor("#353434"));
                Strain_rating_two.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_three.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_four.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_five.setBackgroundColor(Color.parseColor("#00000000"));
                rating = 1;
            }
        });

        Strain_rating_two = view.findViewById(R.id.str_rating_two);
        Strain_rating_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Strain_rating_two.setBackgroundColor(Color.parseColor("#353434"));
                Strain_rating_one.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_three.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_four.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_five.setBackgroundColor(Color.parseColor("#00000000"));
                rating = 2;
            }
        });
        Strain_rating_three = view.findViewById(R.id.str_rating_three);
        Strain_rating_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Strain_rating_three.setBackgroundColor(Color.parseColor("#353434"));
                Strain_rating_one.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_two.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_four.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_five.setBackgroundColor(Color.parseColor("#00000000"));
                rating = 3;
            }
        });
        Strain_rating_four = view.findViewById(R.id.str_rating_four);
        Strain_rating_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Strain_rating_four.setBackgroundColor(Color.parseColor("#353434"));
                Strain_rating_one.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_two.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_three.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_five.setBackgroundColor(Color.parseColor("#00000000"));
                rating = 4;
            }
        });
        Strain_rating_five = view.findViewById(R.id.str_rating_five);
        Strain_rating_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Strain_rating_five.setBackgroundColor(Color.parseColor("#353434"));
                Strain_rating_one.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_two.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_three.setBackgroundColor(Color.parseColor("#00000000"));
                Strain_rating_four.setBackgroundColor(Color.parseColor("#00000000"));
                rating = 5;
            }
        });
        Strain_rating_five.performClick();
        Submit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commment_text.getText().toString().trim().length() > 0) {
                    isReviewAdd = true;
                    if (Attatchment_file_path.length() > 4) {
                        JSONArray parameter = new JSONArray();
                        parameter.put("strain_id");
//                        parameter.put("rating");
                        parameter.put("rating");
                        parameter.put("review");
                        JSONArray values = new JSONArray();
                        values.put(strainDataModel.getId());
                        values.put(rating);
                        values.put(commment_text.getText().toString());
                        Refressh_Layout.setVisibility(View.VISIBLE);
                        new UploadFileWithProgress(getContext(), true, URL.add_strain_review, Attatchment_file_path, Attatchment_file_type, values, parameter, null, StrainOverViewTabFragment.this, APIActions.ApiActions.add_strain_review).execute();
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("strain_id", strainDataModel.getId());
                            jsonObject.put("rating", rating);
                            jsonObject.put("review", commment_text.getText().toString());
                            new VollyAPICall(getContext(), true, URL.add_strain_review, jsonObject, user.getSession_key(), Request.Method.POST, StrainOverViewTabFragment.this, APIActions.ApiActions.add_strain_review);
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

        commment_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
//                            strainDetailsActivity_scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    }, 100);
                }
            }
        });
    }

    public void SetShortDiscription(String discription) {
//
//        String myString = discription + " [ See Full Strain Details ]";
//        int i1 = myString.indexOf("[");
//        int i2 = myString.indexOf("]");
//        short_description.setMovementMethod(LinkMovementMethod.getInstance());
        if (discription.trim().length() > 0) {
            MakeKeywordClickableText(short_description.getContext(), discription, short_description);
        } else {
            MakeKeywordClickableText(short_description.getContext(), "No description added.", short_description);
        }

//        MakeKeywordClickableText(short_description.getContext(), discription, short_description);

//        short_description.setText(myString, TextView.BufferType.SPANNABLE);
//
//        Spannable mySpannable = (Spannable) short_description.getText();
//        ClickableSpan myClickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                showMoreStrainDetails.ShowDetails();
//            }
//
//            @Override
//            public void updateDrawState(TextPaint tp) {
//                tp.bgColor = Color.TRANSPARENT;
//                tp.linkColor = Color.parseColor("#f4c42f");
//                tp.setColor(Color.parseColor("#f4c42f"));
////                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
////                tp.setTypeface(boldTypeface);
//                tp.setUnderlineText(false);
//            }
//        };
//        short_description.setHighlightColor(Color.parseColor("#f0c12f"));
//        mySpannable.setSpan(new StyleSpan(Typeface.BOLD), i1, i2 + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        mySpannable.setSpan(myClickableSpan, i1, i2 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    video_processing.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "pd");
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
                                attachement_img.setBackground(new BitmapDrawable(getResources(), video_thumbnil));
                            } else {
                                Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.test_img);
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

    public Drawable GetRounderDarbable(Bitmap bitmapOrg) {
        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
        return new BitmapDrawable(getContext().getResources(), bitmap);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == APIActions.ApiActions.add_strain_review) {
            CustomeToast.ShowCustomToast(getContext(), "Review Added Successfully!", Gravity.TOP);
            Attatchment_file_path = "";
            Attatchment_file_type = "";
            Comment_image_layout.setVisibility(GONE);
            Attach_image_line.setVisibility(GONE);
            commment_text.setText("");
            Strain_rating_five.setBackgroundColor(Color.parseColor("#00000000"));
            Strain_rating_one.setBackgroundColor(Color.parseColor("#00000000"));
            Strain_rating_two.setBackgroundColor(Color.parseColor("#00000000"));
            Strain_rating_three.setBackgroundColor(Color.parseColor("#00000000"));
            Strain_rating_four.setBackgroundColor(Color.parseColor("#00000000"));
            rating = 0;

            ((StrainDetailsActivity) view.getContext()).SetTopDataCall();

            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(getContext(), true, URL.get_strain_detail + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainOverViewTabFragment.this, get_strain_detail);
        } else if (apiActions == APIActions.ApiActions.delete_strain_review) {
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(getContext(), true, URL.get_strain_detail + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainOverViewTabFragment.this, get_strain_detail);
            ((StrainDetailsActivity) view.getContext()).SetTopDataCall();
        } else if (apiActions == get_strain_detail) {
            Main_Layout.setVisibility(View.VISIBLE);
            Refressh_Layout.setVisibility(GONE);
            try {
                Log.d("response", response);
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(response);

                // @Strain Detail
                strainOverViewDataModel = new StrainOverViewDataModel();
                strainOverViewDataModel.setGet_user_review_count(jsonObject.getJSONObject("successData").getJSONObject("strain").getInt("get_user_review_count"));
                strainOverViewDataModel.setSurvey_budz_count(jsonObject.getJSONObject("successData").getInt("survey_budz_count"));
                strainOverViewDataModel.setGet_user_servy_cout(jsonObject.getJSONObject("successData").getJSONObject("strain").getInt("get_strain_survey_user_count"));
                if (strainOverViewDataModel.getGet_user_review_count() == 0) {
                    comment_above.setVisibility(View.VISIBLE);
                    comment_below.setVisibility(View.VISIBLE);
                } else {
                    comment_above.setVisibility(GONE);
                    comment_below.setVisibility(GONE);
                }
                strainOverViewDataModel.setStrain_short_Discription(jsonObject.getJSONObject("successData").getJSONObject("strain").getString("overview"));
                if (!jsonObject.getJSONObject("successData").isNull("top_strain")) {
                    if (jsonObject.getJSONObject("successData").getJSONObject("top_strain").getInt("get_likes_count") > 4) {
                        strainOverViewDataModel.setStrain_short_Discription(jsonObject.getJSONObject("successData").getJSONObject("top_strain").getString("description"));
                    }
                }
                final ArrayList<StrainOverViewDataModel.Reviews> reviews = new ArrayList<>();
                JSONArray reviews_array = jsonObject.getJSONObject("successData").getJSONObject("strain").getJSONArray("get_review");
                for (int x = 0; x < reviews_array.length(); x++) {
                    JSONObject review_object = reviews_array.getJSONObject(x);
                    StrainOverViewDataModel.Reviews review = new StrainOverViewDataModel.Reviews();
                    review.setId(review_object.getInt("id"));
                    review.setIs_user_flaged_count(review_object.getInt("is_user_flaged_count"));
                    review.setStrain_id(review_object.getInt("strain_id"));
                    review.setReviewed_by(review_object.getInt("reviewed_by"));
                    review.setReview(review_object.getString("review"));
                    review.setCreated_at(review_object.getString("created_at"));
                    review.setUpdated_at(review_object.getString("updated_at"));
                    review.setUpdated_at(review_object.getString("updated_at"));
                    review.setUser_id(review_object.getJSONObject("get_user").getInt("id"));
                    review.setUser_first_name(review_object.getJSONObject("get_user").getString("first_name"));
                    review.setUser_avatar(review_object.getJSONObject("get_user").getString("avatar"));
                    review.setUser_image_path(review_object.getJSONObject("get_user").getString("image_path"));
                    review.setUser_point(review_object.getJSONObject("get_user").getInt("points"));
                    review.setSpecial_icon(review_object.getJSONObject("get_user").getString("special_icon"));
                    review.setTotal_likes(review_object.optInt("likes_count"));
                    if (review_object.getInt("is_reviewed_count") == 0) {
                        review.setLiked(false);
                    } else {
                        review.setLiked(true);
                    }
                    if (!review_object.isNull("rating")) {
                        review.setRating(review_object.getJSONObject("rating").getDouble("rating"));
                    } else {
                        review.setRating(0);
                    }
                    if (!review_object.isNull("attachment")) {
                        review.setAttatchment_type(review_object.getJSONObject("attachment").getString("type"));
                        review.setAttatchment_poster(review_object.getJSONObject("attachment").optString("poster"));
                        review.setAttatchment_path(review_object.getJSONObject("attachment").getString("attachment"));
                    }
                    reviews.add(review);
                }
                strainOverViewDataModel.setReviews(reviews);

                //@Medical Conditions
                ArrayList<StrainOverViewDataModel.SurvayDataModal> medical_condistions = new ArrayList<>();
                if (!jsonObject.getJSONObject("successData").isNull("madical_conditions")) {
                    JSONArray medical_condition_array = jsonObject.getJSONObject("successData").getJSONArray("madical_conditions");
                    for (int x = 0; x < medical_condition_array.length(); x++) {
                        JSONObject m_jsonobject = medical_condition_array.getJSONObject(x);
                        StrainOverViewDataModel.SurvayDataModal madicalConditions = new StrainOverViewDataModel.SurvayDataModal();
                        madicalConditions.setName(m_jsonobject.getString("name"));
                        madicalConditions.setResult(m_jsonobject.getInt("result"));
                        medical_condistions.add(madicalConditions);
                    }
                }
                strainOverViewDataModel.setMadicalConditions(medical_condistions);

                ArrayList<StrainOverViewDataModel.SurvayDataModal> disease_prevention = new ArrayList<>();
                if (!jsonObject.getJSONObject("successData").isNull("preventions")) {
                    JSONArray medical_condition_array = jsonObject.getJSONObject("successData").getJSONArray("preventions");
                    for (int x = 0; x < medical_condition_array.length(); x++) {
                        JSONObject m_jsonobject = medical_condition_array.getJSONObject(x);
                        StrainOverViewDataModel.SurvayDataModal madicalConditions = new StrainOverViewDataModel.SurvayDataModal();
                        madicalConditions.setName(m_jsonobject.getString("name"));
                        madicalConditions.setResult(m_jsonobject.getInt("result"));
                        disease_prevention.add(madicalConditions);
                    }
                }
                strainOverViewDataModel.setDiseace_Prevention(disease_prevention);

                ArrayList<StrainOverViewDataModel.SurvayDataModal> modes_sensation = new ArrayList<>();
                if (!jsonObject.getJSONObject("successData").isNull("sensations")) {
                    JSONArray medical_condition_array = jsonObject.getJSONObject("successData").getJSONArray("sensations");
                    for (int x = 0; x < medical_condition_array.length(); x++) {
                        JSONObject m_jsonobject = medical_condition_array.getJSONObject(x);
                        StrainOverViewDataModel.SurvayDataModal madicalConditions = new StrainOverViewDataModel.SurvayDataModal();
                        madicalConditions.setName(m_jsonobject.getString("name"));
                        madicalConditions.setResult(m_jsonobject.getInt("result"));
                        modes_sensation.add(madicalConditions);
                    }
                }
                strainOverViewDataModel.setModes_Sensation(modes_sensation);


                ArrayList<StrainOverViewDataModel.SurvayDataModal> negative_Effect = new ArrayList<>();
                if (!jsonObject.getJSONObject("successData").isNull("negative_effects")) {
                    JSONArray medical_condition_array = jsonObject.getJSONObject("successData").getJSONArray("negative_effects");
                    for (int x = 0; x < medical_condition_array.length(); x++) {
                        JSONObject m_jsonobject = medical_condition_array.getJSONObject(x);
                        StrainOverViewDataModel.SurvayDataModal madicalConditions = new StrainOverViewDataModel.SurvayDataModal();
                        madicalConditions.setName(m_jsonobject.getString("name"));
                        madicalConditions.setResult(m_jsonobject.getInt("result"));
                        negative_Effect.add(madicalConditions);
                    }
                }
                strainOverViewDataModel.setNegative_Effect(negative_Effect);

                ArrayList<StrainOverViewDataModel.SurvayDataModal> flavour_profile = new ArrayList<>();
                if (!jsonObject.getJSONObject("successData").isNull("survey_flavors")) {
                    JSONArray medical_condition_array = jsonObject.getJSONObject("successData").getJSONArray("survey_flavors");
                    for (int x = 0; x < medical_condition_array.length(); x++) {
                        JSONObject m_jsonobject = medical_condition_array.getJSONObject(x);
                        StrainOverViewDataModel.SurvayDataModal madicalConditions = new StrainOverViewDataModel.SurvayDataModal();
                        madicalConditions.setName(m_jsonobject.getString("name"));
                        madicalConditions.setResult(m_jsonobject.getInt("result"));
                        flavour_profile.add(madicalConditions);
                    }
                }
                strainOverViewDataModel.setFlavour_Profiles(flavour_profile);


                //@survay Answersss
                ArrayList<StrainOverViewDataModel.MedicalConditionAnswers> medicalConditionAnswers = new ArrayList<>();
                JSONArray medical_condition_answers_array = jsonObject.getJSONObject("successData").getJSONArray("madical_condition_answers");
                for (int x = 0; x < medical_condition_answers_array.length(); x++) {
                    JSONObject m_jsonobject = medical_condition_answers_array.getJSONObject(x);
                    StrainOverViewDataModel.MedicalConditionAnswers medicalConditio_answers_modal = new StrainOverViewDataModel.MedicalConditionAnswers();
                    medicalConditio_answers_modal.setId(m_jsonobject.getInt("id"));
                    medicalConditio_answers_modal.setM_condition(m_jsonobject.getString("m_condition"));
                    medicalConditio_answers_modal.setIs_approved(m_jsonobject.optInt("is_approved"));
                    medicalConditionAnswers.add(medicalConditio_answers_modal);
                }
                strainOverViewDataModel.setMedicalConditionAnswers(medicalConditionAnswers);


                ArrayList<StrainOverViewDataModel.ModesAndSensationsAnswers> modesAndSensationsAnswers = new ArrayList<>();
                JSONArray modes_sensation_answers_array = jsonObject.getJSONObject("successData").getJSONArray("sensation_answers");
                for (int x = 0; x < modes_sensation_answers_array.length(); x++) {
                    JSONObject m_jsonobject = modes_sensation_answers_array.getJSONObject(x);
                    StrainOverViewDataModel.ModesAndSensationsAnswers modesAndSensationsAnswers1 = new StrainOverViewDataModel.ModesAndSensationsAnswers();
                    modesAndSensationsAnswers1.setId(m_jsonobject.getInt("id"));
                    modesAndSensationsAnswers1.setSensation(m_jsonobject.getString("sensation"));
                    modesAndSensationsAnswers1.setIs_approved(m_jsonobject.optInt("is_approved"));
                    modesAndSensationsAnswers.add(modesAndSensationsAnswers1);
                }
                strainOverViewDataModel.setModesAndSensationsAnswers(modesAndSensationsAnswers);


                ArrayList<StrainOverViewDataModel.NegativeEffectAnswers> negativeEffectAnswers = new ArrayList<>();
                JSONArray negative_effect_answers_array = jsonObject.getJSONObject("successData").getJSONArray("negative_effect_answers");
                for (int x = 0; x < negative_effect_answers_array.length(); x++) {
                    JSONObject m_jsonobject = negative_effect_answers_array.getJSONObject(x);
                    StrainOverViewDataModel.NegativeEffectAnswers negativeEffectAnswers1 = new StrainOverViewDataModel.NegativeEffectAnswers();
                    negativeEffectAnswers1.setId(m_jsonobject.getInt("id"));
                    negativeEffectAnswers1.setEffect(m_jsonobject.getString("effect"));
                    negativeEffectAnswers1.setIs_approved(m_jsonobject.optInt("is_approved"));
                    negativeEffectAnswers.add(negativeEffectAnswers1);
                }
                strainOverViewDataModel.setNegativeEffectAnswers(negativeEffectAnswers);


                ArrayList<StrainOverViewDataModel.DiseasePreventionAnswers> diseasePreventionAnswers = new ArrayList<>();
                JSONArray diseasePrevention_answers_array = jsonObject.getJSONObject("successData").getJSONArray("prevention_answers");
                for (int x = 0; x < diseasePrevention_answers_array.length(); x++) {
                    JSONObject m_jsonobject = diseasePrevention_answers_array.getJSONObject(x);
                    StrainOverViewDataModel.DiseasePreventionAnswers diseasePreventionAnswers1 = new StrainOverViewDataModel.DiseasePreventionAnswers();
                    diseasePreventionAnswers1.setId(m_jsonobject.getInt("id"));
                    diseasePreventionAnswers1.setPrevention(m_jsonobject.getString("prevention"));
                    diseasePreventionAnswers1.setIs_approved(m_jsonobject.optInt("is_approved"));
                    diseasePreventionAnswers.add(diseasePreventionAnswers1);
                }
                strainOverViewDataModel.setDiseasePreventionAnswers(diseasePreventionAnswers);


                ArrayList<StrainOverViewDataModel.SurvayFlavourAnswers> survayFlavourAnswers = new ArrayList<>();
                JSONArray survayFlavour_answers_array = jsonObject.getJSONObject("successData").getJSONArray("survey_flavor_answers");
                for (int x = 0; x < survayFlavour_answers_array.length(); x++) {
                    JSONObject m_jsonobject = survayFlavour_answers_array.getJSONObject(x);
                    StrainOverViewDataModel.SurvayFlavourAnswers survayFlavourAnswers1 = new StrainOverViewDataModel.SurvayFlavourAnswers();
                    survayFlavourAnswers1.setId(m_jsonobject.getInt("id"));
                    survayFlavourAnswers1.setFlavor(m_jsonobject.getString("flavor"));
                    survayFlavourAnswers1.setIs_approved(m_jsonobject.optInt("is_approved"));
                    survayFlavourAnswers.add(survayFlavourAnswers1);
                }
                strainOverViewDataModel.setSurvayFlavourAnswers(survayFlavourAnswers);


                SetData();
                SetTopTwoComments(reviews.size());
                if (isReviewAdd) {
                    isReviewAdd = false;
                    CommentView(view);
                    InitView(view);
                    Main_Layout.setVisibility(View.VISIBLE);
                    Refressh_Layout.setVisibility(GONE);
                    SetData();
                    SetTopTwoComments(reviews.size());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == flag_strain_review) {
            Log.d("reaposne", response);
        } else if (apiActions == save_survey_answer) {
            CustomeToast.ShowCustomToast(getContext(), "Survey submitted successfully!", Gravity.TOP);
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(getContext(), false, URL.get_strain_detail + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainOverViewTabFragment.this, get_strain_detail);
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == save_survey_answer) {
            CustomeToast.ShowCustomToast(getContext(), "Survey not submitted successfully!", Gravity.TOP);
            Main_Layout.setVisibility(View.VISIBLE);
            Refressh_Layout.setVisibility(GONE);
        } else if (apiActions == get_strain_detail) {
            Main_Layout.setVisibility(View.VISIBLE);
            Refressh_Layout.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(response);
                CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                JSONObject object = new JSONObject(response);
                CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void SetProgressWithAnim(ProgressBar progress, float from, float to) {
        ProgressBarAnimation anim = new ProgressBarAnimation(progress, from, to);
        anim.setDuration(1000);
        progress.startAnimation(anim);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (goneToReview) {
            goneToReview = false;
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(getContext(), true, URL.get_strain_detail + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainOverViewTabFragment.this, get_strain_detail);
            ((StrainDetailsActivity) view.getContext()).SetTopDataCall();
        }
    }

    public void SetData() {

        Top_Three_strain.setText(Html.fromHtml("Based on <b><font color=#f0c12f>" + strainOverViewDataModel.getSurvey_budz_count() + " </font></b> Budz<br>Surveys Submitted"));
        //TODO WORK HERE
        if (strainOverViewDataModel.getGet_user_servy_cout() == 0) {
            Start_survay_button.setVisibility(View.VISIBLE);
            Description_Layout.setVisibility(View.VISIBLE);
        } else {
            Description_Layout.setVisibility(GONE);
            Start_survay_button.setVisibility(GONE);
        }
        strainDiscription = strainOverViewDataModel.getStrain_short_Discription();
        if (strainOverViewDataModel.getStrain_short_Discription().length() >= 100) {
            MakeKeywordClickableText(short_description.getContext(), strainOverViewDataModel.getStrain_short_Discription(), short_description);
            if (short_description.getText().length() >= 100) {
                full_dec.setVisibility(View.VISIBLE);
                SetShortDiscription(strainOverViewDataModel.getStrain_short_Discription().substring(0, 99));
            } else {
                full_dec.setVisibility(GONE);
            }


        } else {
            full_dec.setVisibility(GONE);
            if (strainOverViewDataModel.getStrain_short_Discription().trim().length() > 0) {
                MakeKeywordClickableText(short_description.getContext(), strainOverViewDataModel.getStrain_short_Discription(), short_description);
            } else {
                MakeKeywordClickableText(short_description.getContext(), "No description added.", short_description);
            }

//            short_description.setText(strainOverViewDataModel.getStrain_short_Discription());
        }

        if (strainOverViewDataModel.getMadicalConditions().size() == 0) {
            No_survay_found.setVisibility(View.VISIBLE);
            img_pending.setVisibility(View.VISIBLE);
            for_no_survay.setVisibility(View.VISIBLE);
            Survay_layout.setVisibility(GONE);
        } else {
            No_survay_found.setVisibility(GONE);
            img_pending.setVisibility(View.GONE);
            for_no_survay.setVisibility(View.GONE);
            Survay_layout.setVisibility(View.VISIBLE);
        }

        // 1
        if (strainOverViewDataModel.getMadicalConditions().size() > 0) {
            Description_Layout.setVisibility(GONE);
            if (strainOverViewDataModel.getMadicalConditions().size() == 1) {
                SetProgressWithAnim(Medical_conditions_one_progress, 0, strainOverViewDataModel.getMadicalConditions().get(0).getResult());
                Medical_conditions_one_text.setText(strainOverViewDataModel.getMadicalConditions().get(0).getName());
                boolean isFirst_set = false;
                for (int x = 0; x < strainOverViewDataModel.getMedicalConditionAnswers().size(); x++) {
                    if (!strainOverViewDataModel.getMedicalConditionAnswers().get(x).getM_condition().equalsIgnoreCase(strainOverViewDataModel.getMadicalConditions().get(0).getName())) {
                        if (isFirst_set) {
                            Medical_conditions_three_progress.setProgress(0);
                            Medical_conditions_three_text.setText(strainOverViewDataModel.getMedicalConditionAnswers().get(x).getM_condition());
                            break;
                        } else {
                            isFirst_set = true;
                            Medical_conditions_two_progress.setProgress(0);
                            Medical_conditions_two_text.setText(strainOverViewDataModel.getMedicalConditionAnswers().get(x).getM_condition());
                        }
                    }
                }
                mu2.setVisibility(GONE);
                mu3.setVisibility(GONE);
            } else if (strainOverViewDataModel.getMadicalConditions().size() == 2) {
                SetProgressWithAnim(Medical_conditions_one_progress,
                        0,
                        strainOverViewDataModel.getMadicalConditions().get(0).getResult());
                Medical_conditions_one_text.setText(strainOverViewDataModel.getMadicalConditions().get(0).getName());

                SetProgressWithAnim(Medical_conditions_two_progress,
                        0,
                        strainOverViewDataModel.getMadicalConditions().get(1).getResult());
                Medical_conditions_two_text.setText(strainOverViewDataModel.getMadicalConditions().get(1).getName());
                for (int x = 0; x < strainOverViewDataModel.getMedicalConditionAnswers().size(); x++) {
                    if (!strainOverViewDataModel.getMedicalConditionAnswers().get(x).getM_condition().equalsIgnoreCase(strainOverViewDataModel.getMadicalConditions().get(0).getName())) {
                        Medical_conditions_three_progress.setProgress(0);
                        Medical_conditions_three_text.setText(strainOverViewDataModel.getMedicalConditionAnswers().get(x).getM_condition());
                        break;
                    }
                }
                mu3.setVisibility(GONE);
            } else {
                SetProgressWithAnim(Medical_conditions_one_progress,
                        0,
                        strainOverViewDataModel.getMadicalConditions().get(0).getResult());
                SetProgressWithAnim(Medical_conditions_two_progress,
                        0,
                        strainOverViewDataModel.getMadicalConditions().get(1).getResult());
                SetProgressWithAnim(Medical_conditions_three_progress,
                        0,
                        strainOverViewDataModel.getMadicalConditions().get(2).getResult());
                Medical_conditions_one_text.setText(strainOverViewDataModel.getMadicalConditions().get(0).getName());
                Medical_conditions_two_text.setText(strainOverViewDataModel.getMadicalConditions().get(1).getName());
                Medical_conditions_three_text.setText(strainOverViewDataModel.getMadicalConditions().get(2).getName());
            }
        } else {
            mu2.setVisibility(GONE);
            mu1.setVisibility(GONE);
            mu3.setVisibility(GONE);
            if (strainOverViewDataModel.getMedicalConditionAnswers().size() > 0) {
                try {
                    Medical_conditions_one_progress.setProgress(0);
                    Medical_conditions_one_text.setText(strainOverViewDataModel.getMedicalConditionAnswers().get(0).getM_condition());
                    Medical_conditions_two_progress.setProgress(0);
                    Medical_conditions_two_text.setText(strainOverViewDataModel.getMedicalConditionAnswers().get(1).getM_condition());
                    Medical_conditions_three_progress.setProgress(0);
                    Medical_conditions_three_text.setText(strainOverViewDataModel.getMedicalConditionAnswers().get(2).getM_condition());
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            } else {
                Medical_conditions_one_progress.setProgress(0);
                Medical_conditions_one_text.setText("");
                Medical_conditions_two_progress.setProgress(0);
                Medical_conditions_two_text.setText("");
                Medical_conditions_three_progress.setProgress(0);
                Medical_conditions_three_text.setText("");
            }
        }


        // 2
        if (strainOverViewDataModel.getDiseace_Prevention().size() > 0) {
            if (strainOverViewDataModel.getDiseace_Prevention().size() == 1) {
                SetProgressWithAnim(Disease_one_progress,
                        0,
                        strainOverViewDataModel.getDiseace_Prevention().get(0).getResult());
                Disease_one_text.setText(strainOverViewDataModel.getDiseace_Prevention().get(0).getName());
                boolean isFirst_set = false;
                for (int x = 0; x < strainOverViewDataModel.getDiseasePreventionAnswers().size(); x++) {
                    if (!strainOverViewDataModel.getDiseasePreventionAnswers().get(x).getPrevention().equalsIgnoreCase(strainOverViewDataModel.getDiseace_Prevention().get(0).getName())) {
                        if (isFirst_set) {
                            Disease_three_progress.setProgress(0);
                            Disease_three_text.setText(strainOverViewDataModel.getDiseasePreventionAnswers().get(x).getPrevention());
                            break;
                        } else {
                            isFirst_set = true;
                            Disease_two_progress.setProgress(0);
                            Disease_two_text.setText(strainOverViewDataModel.getDiseasePreventionAnswers().get(x).getPrevention());
                        }
                    }
                }
                dp2.setVisibility(GONE);
                dp3.setVisibility(GONE);
            } else if (strainOverViewDataModel.getDiseace_Prevention().size() == 2) {
                SetProgressWithAnim(Disease_one_progress,
                        0,
                        strainOverViewDataModel.getDiseace_Prevention().get(0).getResult());

                Disease_one_text.setText(strainOverViewDataModel.getDiseace_Prevention().get(0).getName());
                SetProgressWithAnim(Disease_two_progress,
                        0,
                        strainOverViewDataModel.getDiseace_Prevention().get(1).getResult());
                Disease_two_text.setText(strainOverViewDataModel.getDiseace_Prevention().get(1).getName());
                for (int x = 0; x < strainOverViewDataModel.getDiseasePreventionAnswers().size(); x++) {
                    if (!strainOverViewDataModel.getDiseasePreventionAnswers().get(x).getPrevention().equalsIgnoreCase(strainOverViewDataModel.getDiseace_Prevention().get(0).getName())) {
                        Disease_three_progress.setProgress(0);
                        Disease_three_text.setText(strainOverViewDataModel.getDiseasePreventionAnswers().get(x).getPrevention());
                        break;
                    }
                }
                dp3.setVisibility(GONE);
            } else {

                SetProgressWithAnim(Disease_one_progress,
                        0,
                        strainOverViewDataModel.getDiseace_Prevention().get(0).getResult());
                SetProgressWithAnim(Disease_two_progress,
                        0,
                        strainOverViewDataModel.getDiseace_Prevention().get(1).getResult());
                SetProgressWithAnim(Disease_three_progress,
                        0,
                        strainOverViewDataModel.getDiseace_Prevention().get(2).getResult());
                Disease_one_text.setText(strainOverViewDataModel.getDiseace_Prevention().get(0).getName());
                Disease_two_text.setText(strainOverViewDataModel.getDiseace_Prevention().get(1).getName());
                Disease_three_text.setText(strainOverViewDataModel.getDiseace_Prevention().get(2).getName());
            }
        } else {
            dp1.setVisibility(GONE);
            dp2.setVisibility(GONE);
            dp3.setVisibility(GONE);
            if (strainOverViewDataModel.getDiseasePreventionAnswers().size() > 0) {
                try {
                    Disease_one_progress.setProgress(0);
                    Disease_one_text.setText(strainOverViewDataModel.getDiseasePreventionAnswers().get(0).getPrevention());
                    Disease_two_progress.setProgress(0);
                    Disease_two_text.setText(strainOverViewDataModel.getDiseasePreventionAnswers().get(1).getPrevention());
                    Disease_three_progress.setProgress(0);
                    Disease_three_text.setText(strainOverViewDataModel.getDiseasePreventionAnswers().get(2).getPrevention());
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            } else {
                Disease_one_progress.setProgress(0);
                Disease_one_text.setText("");
                Disease_two_progress.setProgress(0);
                Disease_two_text.setText("");
                Disease_three_progress.setProgress(0);
                Disease_three_text.setText("");
            }
        }


        // 3
        if (strainOverViewDataModel.getModes_Sensation().size() > 0) {
            if (strainOverViewDataModel.getModes_Sensation().size() == 1) {
                SetProgressWithAnim(Mode_one_progress,
                        0,
                        strainOverViewDataModel.getModes_Sensation().get(0).getResult());
                Mode_one_text.setText(strainOverViewDataModel.getModes_Sensation().get(0).getName());
                boolean isFirst_set = false;
                for (int x = 0; x < strainOverViewDataModel.getModesAndSensationsAnswers().size(); x++) {
                    if (!strainOverViewDataModel.getModesAndSensationsAnswers().get(x).getSensation().equalsIgnoreCase(strainOverViewDataModel.getModes_Sensation().get(0).getName())) {
                        if (isFirst_set) {
                            Mode_three_progress.setProgress(0);
                            Mode_three_text.setText(strainOverViewDataModel.getModesAndSensationsAnswers().get(x).getSensation());
                            break;
                        } else {
                            isFirst_set = true;
                            Mode_two_progress.setProgress(0);
                            Mode_two_text.setText(strainOverViewDataModel.getModesAndSensationsAnswers().get(x).getSensation());
                        }
                    }
                }
                ms2.setVisibility(GONE);
                ms3.setVisibility(GONE);
            } else if (strainOverViewDataModel.getModes_Sensation().size() == 2) {
                SetProgressWithAnim(Mode_one_progress,
                        0,
                        strainOverViewDataModel.getModes_Sensation().get(0).getResult());

                Mode_one_text.setText(strainOverViewDataModel.getModes_Sensation().get(0).getName());
                SetProgressWithAnim(Mode_two_progress,
                        0,
                        strainOverViewDataModel.getModes_Sensation().get(1).getResult());
                Mode_two_text.setText(strainOverViewDataModel.getModes_Sensation().get(1).getName());
                for (int x = 0; x < strainOverViewDataModel.getModesAndSensationsAnswers().size(); x++) {
                    if (!strainOverViewDataModel.getModesAndSensationsAnswers().get(x).getSensation().equalsIgnoreCase(strainOverViewDataModel.getModes_Sensation().get(0).getName())) {
                        Mode_three_progress.setProgress(0);
                        Mode_three_text.setText(strainOverViewDataModel.getModesAndSensationsAnswers().get(x).getSensation());
                        break;
                    }
                }
//                mu2.setVisibility(GONE);
                ms3.setVisibility(GONE);
            } else {
                SetProgressWithAnim(Mode_one_progress,
                        0,
                        strainOverViewDataModel.getModes_Sensation().get(0).getResult());
                SetProgressWithAnim(Mode_two_progress,
                        0,
                        strainOverViewDataModel.getModes_Sensation().get(1).getResult());
                SetProgressWithAnim(Mode_three_progress,
                        0,
                        strainOverViewDataModel.getModes_Sensation().get(2).getResult());
                Mode_one_text.setText(strainOverViewDataModel.getModes_Sensation().get(0).getName());
                Mode_two_text.setText(strainOverViewDataModel.getModes_Sensation().get(1).getName());
                Mode_three_text.setText(strainOverViewDataModel.getModes_Sensation().get(2).getName());
            }
        } else {
            ms1.setVisibility(GONE);
            ms2.setVisibility(GONE);
            ms3.setVisibility(GONE);
            if (strainOverViewDataModel.getModesAndSensationsAnswers().size() > 0) {
                try {
                    Mode_one_progress.setProgress(0);
                    Mode_one_text.setText(strainOverViewDataModel.getModesAndSensationsAnswers().get(0).getSensation());
                    Mode_two_progress.setProgress(0);
                    Mode_two_text.setText(strainOverViewDataModel.getModesAndSensationsAnswers().get(1).getSensation());
                    Mode_three_progress.setProgress(0);
                    Mode_three_text.setText(strainOverViewDataModel.getModesAndSensationsAnswers().get(2).getSensation());
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            } else {
                Mode_one_progress.setProgress(0);
                Mode_one_text.setText("");
                Mode_two_progress.setProgress(0);
                Mode_two_text.setText("");
                Mode_three_progress.setProgress(0);
                Mode_three_text.setText("");
            }
        }


        // 4
        if (strainOverViewDataModel.getNegative_Effect().size() > 0) {
            if (strainOverViewDataModel.getNegative_Effect().size() == 1) {
                SetProgressWithAnim(Negative_effect_one_progress,
                        0,
                        strainOverViewDataModel.getNegative_Effect().get(0).getResult());
                Negative_effect_one_text.setText(strainOverViewDataModel.getNegative_Effect().get(0).getName());
                boolean isFirst_set = false;
                for (int x = 0; x < strainOverViewDataModel.getNegativeEffectAnswers().size(); x++) {
                    if (!strainOverViewDataModel.getNegativeEffectAnswers().get(x).getEffect().equalsIgnoreCase(strainOverViewDataModel.getNegative_Effect().get(0).getName())) {
                        if (isFirst_set) {
                            Negative_effect_three_progress.setProgress(0);
                            Negative_effect_three_text.setText(strainOverViewDataModel.getNegativeEffectAnswers().get(x).getEffect());
                            break;
                        } else {
                            isFirst_set = true;
                            Negative_effect_two_progress.setProgress(0);
                            Negative_effect_two_text.setText(strainOverViewDataModel.getNegativeEffectAnswers().get(x).getEffect());
                        }
                    }
                }
                ne2.setVisibility(GONE);
                ne3.setVisibility(GONE);
            } else if (strainOverViewDataModel.getNegative_Effect().size() == 2) {
                SetProgressWithAnim(Negative_effect_one_progress,
                        0,
                        strainOverViewDataModel.getNegative_Effect().get(0).getResult());
                Negative_effect_one_text.setText(strainOverViewDataModel.getNegative_Effect().get(0).getName());

                SetProgressWithAnim(Negative_effect_two_progress,
                        0,
                        strainOverViewDataModel.getNegative_Effect().get(1).getResult());
                Negative_effect_two_text.setText(strainOverViewDataModel.getNegative_Effect().get(1).getName());
                for (int x = 0; x < strainOverViewDataModel.getNegativeEffectAnswers().size(); x++) {
                    if (!strainOverViewDataModel.getNegativeEffectAnswers().get(x).getEffect().equalsIgnoreCase(strainOverViewDataModel.getNegative_Effect().get(0).getName())) {
                        Negative_effect_three_progress.setProgress(0);
                        Negative_effect_three_text.setText(strainOverViewDataModel.getNegativeEffectAnswers().get(x).getEffect());
                        break;
                    }
                }
//                ne2.setVisibility(GONE);
                ne3.setVisibility(GONE);
            } else {
                SetProgressWithAnim(Negative_effect_one_progress,
                        0,
                        strainOverViewDataModel.getNegative_Effect().get(0).getResult());
                SetProgressWithAnim(Negative_effect_two_progress,
                        0,
                        strainOverViewDataModel.getNegative_Effect().get(1).getResult());
                SetProgressWithAnim(Negative_effect_three_progress,
                        0,
                        strainOverViewDataModel.getNegative_Effect().get(2).getResult());
                Negative_effect_one_text.setText(strainOverViewDataModel.getNegative_Effect().get(0).getName());
                Negative_effect_two_text.setText(strainOverViewDataModel.getNegative_Effect().get(1).getName());
                Negative_effect_three_text.setText(strainOverViewDataModel.getNegative_Effect().get(2).getName());
            }
        } else {
            ne1.setVisibility(GONE);
            ne2.setVisibility(GONE);
            ne3.setVisibility(GONE);
            if (strainOverViewDataModel.getNegativeEffectAnswers().size() > 0) {
                try {
                    Negative_effect_one_progress.setProgress(0);
                    Negative_effect_one_text.setText(strainOverViewDataModel.getNegativeEffectAnswers().get(0).getEffect());
                    Negative_effect_two_progress.setProgress(0);
                    Negative_effect_two_text.setText(strainOverViewDataModel.getNegativeEffectAnswers().get(1).getEffect());
                    Negative_effect_three_progress.setProgress(0);
                    Negative_effect_three_text.setText(strainOverViewDataModel.getNegativeEffectAnswers().get(2).getEffect());
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            } else {
                Negative_effect_one_progress.setProgress(0);
                Negative_effect_one_text.setText("");
                Negative_effect_two_progress.setProgress(0);
                Negative_effect_two_text.setText("");
                Negative_effect_three_progress.setProgress(0);
                Negative_effect_three_text.setText("");
            }
        }

// 5
        if (strainOverViewDataModel.getFlavour_Profiles().size() > 0) {
            if (strainOverViewDataModel.getFlavour_Profiles().size() == 1) {
                SetProgressWithAnim(Flavour_one_progress,
                        0,
                        strainOverViewDataModel.getFlavour_Profiles().get(0).getResult());
                Flavour_one_text.setText(strainOverViewDataModel.getFlavour_Profiles().get(0).getName());
                boolean isFirst_set = false;
                for (int x = 0; x < strainOverViewDataModel.getSurvayFlavourAnswers().size(); x++) {
                    if (!strainOverViewDataModel.getSurvayFlavourAnswers().get(x).getFlavor().equalsIgnoreCase(strainOverViewDataModel.getFlavour_Profiles().get(0).getName())) {
                        if (isFirst_set) {
                            Flavour_three_progress.setProgress(0);
                            Flavour_three_text.setText(strainOverViewDataModel.getSurvayFlavourAnswers().get(x).getFlavor());
                            break;
                        } else {
                            isFirst_set = true;
                            Flavour_two_progress.setProgress(0);
                            Flavour_two_text.setText(strainOverViewDataModel.getSurvayFlavourAnswers().get(x).getFlavor());
                        }
                    }
                }
                fp2.setVisibility(GONE);
                fp3.setVisibility(GONE);
            } else if (strainOverViewDataModel.getFlavour_Profiles().size() == 2) {
                SetProgressWithAnim(Flavour_one_progress,
                        0,
                        strainOverViewDataModel.getFlavour_Profiles().get(0).getResult());

                Flavour_one_text.setText(strainOverViewDataModel.getFlavour_Profiles().get(0).getName());

                SetProgressWithAnim(Flavour_two_progress,
                        0,
                        strainOverViewDataModel.getFlavour_Profiles().get(1).getResult());
                Flavour_two_text.setText(strainOverViewDataModel.getFlavour_Profiles().get(1).getName());
                for (int x = 0; x < strainOverViewDataModel.getSurvayFlavourAnswers().size(); x++) {
                    if (!strainOverViewDataModel.getSurvayFlavourAnswers().get(x).getFlavor().equalsIgnoreCase(strainOverViewDataModel.getFlavour_Profiles().get(0).getName())) {
                        Flavour_three_progress.setProgress(0);
                        Flavour_three_text.setText(strainOverViewDataModel.getSurvayFlavourAnswers().get(x).getFlavor());
                        break;
                    }
                }
//                fp2.setVisibility(GONE);
                fp3.setVisibility(GONE);
            } else {
                SetProgressWithAnim(Flavour_one_progress,
                        0,
                        strainOverViewDataModel.getFlavour_Profiles().get(0).getResult());
                SetProgressWithAnim(Flavour_two_progress,
                        0,
                        strainOverViewDataModel.getFlavour_Profiles().get(1).getResult());
                SetProgressWithAnim(Flavour_three_progress,
                        0,
                        strainOverViewDataModel.getFlavour_Profiles().get(2).getResult());
                Flavour_one_text.setText(strainOverViewDataModel.getFlavour_Profiles().get(0).getName());
                Flavour_two_text.setText(strainOverViewDataModel.getFlavour_Profiles().get(1).getName());
                Flavour_three_text.setText(strainOverViewDataModel.getFlavour_Profiles().get(2).getName());
            }
        } else {
            fp1.setVisibility(GONE);
            fp2.setVisibility(GONE);
            fp3.setVisibility(GONE);
            if (strainOverViewDataModel.getSurvayFlavourAnswers().size() > 0) {
                try {
                    Flavour_one_progress.setProgress(0);
                    Flavour_one_text.setText(strainOverViewDataModel.getSurvayFlavourAnswers().get(0).getFlavor());
                    Flavour_two_progress.setProgress(0);
                    Flavour_two_text.setText(strainOverViewDataModel.getSurvayFlavourAnswers().get(1).getFlavor());
                    Flavour_three_progress.setProgress(0);
                    Flavour_three_text.setText(strainOverViewDataModel.getSurvayFlavourAnswers().get(2).getFlavor());
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            } else {
                Flavour_one_progress.setProgress(0);
                Flavour_one_text.setText("");
                Flavour_two_progress.setProgress(0);
                Flavour_two_text.setText("");
                Flavour_three_progress.setProgress(0);
                Flavour_three_text.setText("");
            }
        }
    }

    @Override
    public void OnSnedClicked(JSONObject data, int position) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("strain_id", strainOverViewDataModel.getReviews().get(position).getStrain_id());
            jsonObject.put("strain_review_id", strainOverViewDataModel.getReviews().get(position).getId());
            if (position == 0) {
                comment_one_report_icon.setImageResource(R.drawable.ic_flag_strain);
                comment_one_report_text.setTextColor(Color.parseColor("#f4c42f"));
            } else {
                comment_two_report_icon.setImageResource(R.drawable.ic_flag_strain);
                comment_two_report_text.setTextColor(Color.parseColor("#f4c42f"));
            }
            strainOverViewDataModel.getReviews().get(position).setIs_user_flaged_count(1);
            jsonObject.put("is_flaged", 1);
            jsonObject.put("reason", data.getString("reason"));
            new VollyAPICall(getContext(), false, URL.flag_strain_review, jsonObject, user.getSession_key(), Request.Method.POST, StrainOverViewTabFragment.this, flag_strain_review);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onSubmitReview(StrainEditReviewAlertDialog dialog, JSONObject jsonObject) {
        dialog.dismiss();
//        JSONObject jsonObject = new JSONObject();
        try {
            if (jsonObject.getBoolean("isVideo")) {
                JSONArray parameter = new JSONArray();
                JSONArray values = new JSONArray();
                parameter = jsonObject.getJSONArray("param");
                values = jsonObject.getJSONArray("value");
                Attatchment_file_path = jsonObject.getString("path");
                Attatchment_file_type = jsonObject.getString("type");
                Refressh_Layout.setVisibility(View.VISIBLE);
                new UploadFileWithProgress(getContext(), true, URL.add_strain_review, Attatchment_file_path, Attatchment_file_type, values, parameter, null, StrainOverViewTabFragment.this, APIActions.ApiActions.add_strain_review).execute();
            } else {
                new VollyAPICall(getContext(), true, URL.add_strain_review, jsonObject, user.getSession_key(), Request.Method.POST, StrainOverViewTabFragment.this, APIActions.ApiActions.add_strain_review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onCross(StrainEditReviewAlertDialog dialog) {

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
                Drawable drawable = new BitmapDrawable(getResources(), bitmap_plc);
                attachement_img.setBackground(new BitmapDrawable(getResources(), video_thumbnil));
            }
        }
    }

    public void InitView(View view) {
//        ImageView share_1, share_2;
//        share_1 = view.findViewById(R.id.share_one);
//        share_2 = view.findViewById(R.id.share_two);
//        share_1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                JSONObject object = new JSONObject();
//                try {
//                    object.put("msg", "Hey Bud! Check out the Healing Budz app for your smart phone.” Download it today from http://139.162.37.73/healingbudz/");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                ShareIntent.ShareHBContent(view.getContext(), object);
//            }
//        });
//        share_2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                JSONObject object = new JSONObject();
//                try {
//                    object.put("msg", "Hey Bud! Check out the Healing Budz app for your smart phone.” Download it today from http://139.162.37.73/healingbudz/");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                ShareIntent.ShareHBContent(view.getContext(), object);
//            }
//        });
        mu1 = view.findViewById(R.id.mu1);
        mu2 = view.findViewById(R.id.mu2);
        mu3 = view.findViewById(R.id.mu3);
        dp1 = view.findViewById(R.id.dp1);
        dp2 = view.findViewById(R.id.dp2);
        dp3 = view.findViewById(R.id.dp3);
        ms1 = view.findViewById(R.id.ms1);
        ms2 = view.findViewById(R.id.ms2);
        ms3 = view.findViewById(R.id.ms3);
        ne1 = view.findViewById(R.id.ne1);
        ne2 = view.findViewById(R.id.ne2);
        ne3 = view.findViewById(R.id.ne3);
        fp1 = view.findViewById(R.id.fp1);
        fp2 = view.findViewById(R.id.fp2);
        fp3 = view.findViewById(R.id.fp3);
        comment_one_user_img = view.findViewById(R.id.comment_one_user_img);
        profile_img_topi = view.findViewById(R.id.profile_img_topi);
        comment_one_user_name = view.findViewById(R.id.comment_one_user_name);
        comment_one_comment_text = view.findViewById(R.id.comment_one_comment_text);
        comment_one_date = view.findViewById(R.id.comment_one_date);
        Comment_one_img = view.findViewById(R.id.comment_attachment_one);
        comment_one_report_icon = view.findViewById(R.id.comment_one_report_icon);
        comment_one_report_text = view.findViewById(R.id.comment_one_report_text);
        comment_one_rating_count = view.findViewById(R.id.comment_one_rating_count);
        comment_one_rating_img = view.findViewById(R.id.comment_one_rating_img);
        comment_one_media_type_icon = view.findViewById(R.id.comment_one_video_icon);
        comment_one_share = view.findViewById(R.id.comment_one_share);


        profile_img_topi_2 = view.findViewById(R.id.profile_img_topi_2);
        comment_two_user_img = view.findViewById(R.id.comment_two_user_img);
        comment_two_user_name = view.findViewById(R.id.comment_two_user_name);
        comment_two_comment_text = view.findViewById(R.id.comment_two_comment_text);
        comment_two_date = view.findViewById(R.id.comment_two_date);
        comment_two_rating_count = view.findViewById(R.id.comment_two_rating_count);
        comment_two_rating_img = view.findViewById(R.id.comment_two_rating_img);
        comment_two_media_type_icon = view.findViewById(R.id.comment_two_video_icon);
        comment_two_share = view.findViewById(R.id.comment_two_share);
        comment_two_report_icon = view.findViewById(R.id.comment_two_report_icon);
        comment_two_report_text = view.findViewById(R.id.comment_two_report_text);
        Comment_two_img = view.findViewById(R.id.comment_attachment_two);


        edit_one = view.findViewById(R.id.edit_one);
        edit_two = view.findViewById(R.id.edit_two);

        report_one = view.findViewById(R.id.report_one);
        report_two = view.findViewById(R.id.report_two);
        ic_edit_one = view.findViewById(R.id.ic_edit_one);
        comment_two_like = view.findViewById(R.id.comment_two_like);
        comment_one_like = view.findViewById(R.id.comment_one_like);
        ic_edit_two = view.findViewById(R.id.ic_edit_two);
        ic_delete_one = view.findViewById(R.id.ic_delete_one);
        ic_delete_two = view.findViewById(R.id.ic_delete_two);


        comment_one_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                try {
                    object.put("id", strainOverViewDataModel.getReviews().get(0).getId());
                    object.put("type", "Strain");
                    object.put("content", strainDataModel.getTitle());
                    object.put("url", URL.sharedBaseUrl + "/strain-details/" + strainOverViewDataModel.getReviews().get(0).getStrain_id());
                    object.put("msg", "Check out Healing Budz for your smartphone: " + URL.sharedBaseUrl + "/strain_details/" + strainOverViewDataModel.getReviews().get(0).getStrain_id());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareIntent.ShareHBContent((Activity) view.getContext(), object);
            }
        });

        Survay_layout = view.findViewById(R.id.survay_layoutt);
        No_survay_found = view.findViewById(R.id.no_survay);
        img_pending = view.findViewById(R.id.img_pending);
        for_no_survay = view.findViewById(R.id.for_no_survay);


        comment_two_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                try {
                    object.put("id", strainOverViewDataModel.getReviews().get(1).getId());
                    object.put("type", "Strain");
                    object.put("content", strainDataModel.getTitle());
                    object.put("url", URL.sharedBaseUrl + "/strain-details/" + strainOverViewDataModel.getReviews().get(0).getStrain_id());
                    object.put("msg", "Check out Healing Budz for your smartphone: " + URL.sharedBaseUrl + "/strain_details/" + strainOverViewDataModel.getReviews().get(1).getStrain_id());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareIntent.ShareHBContent((Activity) view.getContext(), object);
            }
        });

        review_count = view.findViewById(R.id.review_count);
        first_review_layout = view.findViewById(R.id.first_review_layout);
        second_review_layout = view.findViewById(R.id.second_review_layout);


        Medical_Conditions = view.findViewById(R.id.medical_conditions);

        Medical_conditions_one_progress = view.findViewById(R.id.medical_conditions_one_progress);
        Medical_conditions_two_progress = view.findViewById(R.id.medical_conditions_two_progress);
        Medical_conditions_three_progress = view.findViewById(R.id.medical_conditions_three_progress);

        Medical_conditions_one_text = view.findViewById(R.id.medical_conditions_one_text);
        Medical_conditions_two_text = view.findViewById(R.id.medical_conditions_two_text);
        Medical_conditions_three_text = view.findViewById(R.id.medical_conditions_three_text);


        Disease_one_progress = view.findViewById(R.id.disease_one_progress);
        Disease_two_progress = view.findViewById(R.id.disease_two_progress);
        Disease_three_progress = view.findViewById(R.id.disease_three_progress);

        Disease_one_text = view.findViewById(R.id.disease_one_text);
        Disease_two_text = view.findViewById(R.id.disease_two_text);
        Disease_three_text = view.findViewById(R.id.disease_three_text);


        Mode_one_progress = view.findViewById(R.id.mode_one_progress);
        Mode_two_progress = view.findViewById(R.id.mode_two_progress);
        Mode_three_progress = view.findViewById(R.id.mode_three_progress);

        Mode_one_text = view.findViewById(R.id.mode_one_text);
        Mode_two_text = view.findViewById(R.id.mode_two_text);
        Mode_three_text = view.findViewById(R.id.mode_three_text);


        Negative_effect_one_progress = view.findViewById(R.id.negative_effect_one_progress);
        Negative_effect_two_progress = view.findViewById(R.id.negative_effect_two_progress);
        Negative_effect_three_progress = view.findViewById(R.id.negative_effect_three_progress);

        Negative_effect_one_text = view.findViewById(R.id.negative_effect_one_text);
        Negative_effect_two_text = view.findViewById(R.id.negative_effect_two_text);
        Negative_effect_three_text = view.findViewById(R.id.negative_effect_three_text);


        Flavour_one_progress = view.findViewById(R.id.flavour_one_progress);
        Flavour_two_progress = view.findViewById(R.id.flavour_two_progress);
        Flavour_three_progress = view.findViewById(R.id.flavour_three_progress);

        Flavour_one_text = view.findViewById(R.id.flavour_one_text);
        Flavour_two_text = view.findViewById(R.id.flavour_two_text);
        Flavour_three_text = view.findViewById(R.id.flavour_three_text);


        Start_survay_button = view.findViewById(R.id.start_survey_btn);
        Start_survay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SurvayQuestionOneDialog();
            }
        });

    }


    public void SetTopTwoComments(int cont) {
        if (cont == 0) {
            review_count.setText("");
            review_count.setText(0 + " Reviews");
            first_review_layout.setVisibility(GONE);
            second_review_layout.setVisibility(GONE);
        } else if (cont == 1) {
            review_count.setText(1 + " Reviews");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    first_review_layout.setVisibility(View.VISIBLE);
                    second_review_layout.setVisibility(GONE);
                    FirstReview();
                }
            }, 100);
        } else if (cont >= 2) {
            review_count.setText(strainOverViewDataModel.getReviews().size() + " Reviews");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    first_review_layout.setVisibility(View.VISIBLE);
                    second_review_layout.setVisibility(View.VISIBLE);
                    FirstReview();
                    SecondReview();
                }
            }, 100);
            review_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentArray = strainOverViewDataModel.getReviews();
                    goneToReview = true;
                    GoTo(getActivity(), StrainCommentFullView.class);
                }
            });
        }
    }

    public void FirstReview() {
        final StrainOverViewDataModel.Reviews first_review = strainOverViewDataModel.getReviews().get(0);
        first_count.setText(first_review.getTotal_likes() + "");
        if (first_review.getUser_id() == user.getUser_id()) {
            report_one.setVisibility(GONE);
            edit_one.setVisibility(View.VISIBLE);
        } else {
            report_one.setVisibility(View.VISIBLE);
            edit_one.setVisibility(GONE);
        }
        if (first_review.isLiked()) {

            comment_one_like.setColorFilter(Color.parseColor("#f4c42f"), PorterDuff.Mode.SRC_IN);
            first_count.setTextColor(Color.parseColor("#f4c42f"));
        } else {
            first_count.setTextColor(Color.parseColor("#FFFFFF"));
            comment_one_like.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        }
        comment_one_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = "0";
                if (first_review.isLiked()) {
                    val = "0";
                    first_review.setLiked(false);
                    strainOverViewDataModel.getReviews().get(0).setLiked(false);
                    comment_one_like.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                } else {
                    val = "1";
                    first_review.setLiked(true);
                    strainOverViewDataModel.getReviews().get(0).setLiked(true);
                    comment_one_like.setColorFilter(Color.parseColor("#f4c42f"), PorterDuff.Mode.SRC_IN);
                }
                try {
                    new VollyAPICall(v.getContext()
                            , true
                            , URL.add_strain_review_like
                            , new JSONObject().put("review_id", first_review.getId()).put("strain_id", first_review.getStrain_id()).put("like_val", val)
                            , Splash.user.getSession_key()
                            , Request.Method.POST
                            , new APIResponseListner() {
                        @Override
                        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                            ((StrainDetailsActivity) view.getContext()).SetTopDataCall();

                            JSONObject jsonObject = new JSONObject();
                            new VollyAPICall(getContext(), true, URL.get_strain_detail + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainOverViewTabFragment.this, get_strain_detail);
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
        comment_one_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNewScreen = true;
                GoToProfile(comment_one_user_name.getContext(), first_review.getUser_id());
            }
        });

        comment_one_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNewScreen = true;
                GoToProfile(comment_one_user_img.getContext(), first_review.getUser_id());
            }
        });
        comment_one_user_name.setText(first_review.getUser_first_name());
//        comment_one_user_name.setTextColor(Color.parseColor(Utility.getBudColor(first_review.getUser_point())));
        if (first_review.getUser_image_path().length() > 8) {
            SetImageDrawable(comment_one_user_img, first_review.getUser_image_path());
        } else {
            SetImageDrawable(comment_one_user_img, first_review.getUser_avatar());
//            comment_one_user_img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_user_profile));
        }
        if (first_review.getSpecial_icon().length() > 8) {
            profile_img_topi.setVisibility(View.VISIBLE);
            SetImageDrawable(profile_img_topi, first_review.getSpecial_icon());
        } else {
            profile_img_topi.setVisibility(GONE);
//            comment_one_user_img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_user_profile));
        }
        MakeKeywordClickableText(comment_one_comment_text.getContext(), first_review.getReview(), comment_one_comment_text);
        comment_one_date.setText(DateConverter.getCustomDateString(first_review.getUpdated_at()));
        comment_one_rating_count.setText(first_review.getRating() + "");
        comment_one_rating_img.setImageResource(Strain_Rating(first_review.getRating()));
        if (first_review.getAttatchment_type() != null) {
            if (first_review.getAttatchment_type().equalsIgnoreCase("image")) {
                First_Attach.setVisibility(View.VISIBLE);
                comment_one_media_type_icon.setVisibility(View.GONE);
                Comment_one_img.setVisibility(View.VISIBLE);
                comment_one_media_type_icon.setImageResource(R.drawable.ic_gallery_icon);
                SetImageBackground(Comment_one_img, first_review.getAttatchment_path());
                Comment_one_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = "";
                        path = URL.images_baseurl + first_review.getAttatchment_path();
                        Intent intent = new Intent(context, MediPreview.class);
                        intent.putExtra("path", path);
                        intent.putExtra("isvideo", false);
                        context.startActivity(intent);
                    }
                });
            } else if (first_review.getAttatchment_type().equalsIgnoreCase("video")) {
                First_Attach.setVisibility(View.VISIBLE);
                comment_one_media_type_icon.setVisibility(View.VISIBLE);
                Comment_one_img.setVisibility(View.VISIBLE);
                comment_one_media_type_icon.setImageResource(R.drawable.ic_video_play_icon);
                SetImageBackground(Comment_one_img, first_review.getAttatchment_poster());
                Comment_one_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = "";
                        path = URL.videos_baseurl + first_review.getAttatchment_path();
                        Intent intent = new Intent(context, MediPreview.class);
                        intent.putExtra("path", path);
                        intent.putExtra("isvideo", true);
                        context.startActivity(intent);
                    }
                });
            } else {
                comment_one_media_type_icon.setVisibility(GONE);
                Comment_one_img.setVisibility(GONE);
                First_Attach.setVisibility(GONE);
            }
        } else {
            comment_one_media_type_icon.setVisibility(GONE);
            Comment_one_img.setVisibility(GONE);
            First_Attach.setVisibility(GONE);
        }

        review_count.setText("");
        review_count.setText(strainOverViewDataModel.getReviews().size() + " Reviews");

        if (first_review.getIs_user_flaged_count() == 1) {
            comment_one_report_icon.setImageResource(R.drawable.ic_flag_strain);
            comment_one_report_text.setTextColor(Color.parseColor("#f4c42f"));

        } else {
            comment_one_report_icon.setImageResource(R.drawable.ic_flag_white);
            comment_one_report_text.setTextColor(Color.parseColor("#caccca"));
        }
        ic_edit_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrainEditReviewAlertDialog
                        .newInstance(StrainOverViewTabFragment.this, false, first_review)
                        .show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "pd");

            }
        });
        ic_delete_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this review?")
                        .setConfirmText("Yes, delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                try {
                                    new VollyAPICall(getContext()
                                            , true
                                            , URL.delete_strain_review
                                            , new JSONObject().put("strain_review_id", first_review.getId())
                                            , user.getSession_key()
                                            , Request.Method.POST
                                            , StrainOverViewTabFragment.this, APIActions.ApiActions.delete_strain_review);
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
    }

    public void SecondReview() {
        final StrainOverViewDataModel.Reviews second_review = strainOverViewDataModel.getReviews().get(1);
        second_count.setText(second_review.getTotal_likes() + "");
        if (second_review.getUser_id() == user.getUser_id()) {
            report_two.setVisibility(GONE);
            edit_two.setVisibility(View.VISIBLE);
        } else {
            report_two.setVisibility(View.VISIBLE);
            edit_two.setVisibility(GONE);
        }
        comment_two_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNewScreen = true;
                GoToProfile(comment_two_user_name.getContext(), second_review.getUser_id());
            }
        });
        if (second_review.isLiked()) {
            comment_two_like.setColorFilter(Color.parseColor("#f4c42f"), PorterDuff.Mode.SRC_IN);
            second_count.setTextColor(Color.parseColor("#f4c42f"));
        } else {
            second_count.setTextColor(Color.parseColor("#FFFFFF"));
            comment_two_like.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        }
        comment_two_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = "0";
                if (second_review.isLiked()) {
                    val = "0";
                    second_review.setLiked(false);
                    strainOverViewDataModel.getReviews().get(1).setLiked(false);
                    comment_two_like.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                } else {
                    val = "1";
                    second_review.setLiked(true);
                    strainOverViewDataModel.getReviews().get(1).setLiked(true);
                    comment_two_like.setColorFilter(Color.parseColor("#f4c42f"), PorterDuff.Mode.SRC_IN);
                }
                try {
                    new VollyAPICall(v.getContext()
                            , true
                            , URL.add_strain_review_like
                            , new JSONObject().put("review_id", second_review.getId()).put("strain_id", second_review.getStrain_id()).put("like_val", val)
                            , Splash.user.getSession_key()
                            , Request.Method.POST
                            , new APIResponseListner() {
                        @Override
                        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                            ((StrainDetailsActivity) view.getContext()).SetTopDataCall();

                            JSONObject jsonObject = new JSONObject();
                            new VollyAPICall(getContext(), true, URL.get_strain_detail + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainOverViewTabFragment.this, get_strain_detail);
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
        ic_edit_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrainEditReviewAlertDialog
                        .newInstance(StrainOverViewTabFragment.this, false, second_review)
                        .show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "pd");
            }
        });

        ic_delete_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this review?")
                        .setConfirmText("Yes, delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                try {
                                    new VollyAPICall(getContext()
                                            , true
                                            , URL.delete_strain_review
                                            , new JSONObject().put("strain_review_id", second_review.getId())
                                            , user.getSession_key()
                                            , Request.Method.POST
                                            , StrainOverViewTabFragment.this, APIActions.ApiActions.delete_strain_review);
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
        comment_two_user_name.setText(second_review.getUser_first_name());
//        comment_two_user_name.setTextColor(Color.parseColor(Utility.getBudColor(second_review.getUser_point())));
        if (second_review.getUser_image_path().length() > 8) {
            SetImageDrawable(comment_two_user_img, second_review.getUser_image_path());
        } else {
            SetImageDrawable(comment_two_user_img, second_review.getUser_avatar());
//            comment_two_user_img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_user_profile));
        }
        if (second_review.getSpecial_icon().length() > 8) {
            profile_img_topi_2.setVisibility(View.VISIBLE);
            SetImageDrawable(profile_img_topi_2, second_review.getSpecial_icon());
        } else {
            profile_img_topi_2.setVisibility(GONE);
//            comment_one_user_img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_user_profile));
        }
        MakeKeywordClickableText(comment_two_comment_text.getContext(), second_review.getReview(), comment_two_comment_text);
        comment_two_date.setText(DateConverter.getCustomDateString(second_review.getUpdated_at()));
        comment_two_rating_count.setText(second_review.getRating() + "");
        comment_two_rating_img.setImageResource(Strain_Rating(second_review.getRating()));
        if (second_review.getAttatchment_type() != null) {
            if (second_review.getAttatchment_type().equalsIgnoreCase("image")) {
                comment_two_media_type_icon.setVisibility(View.GONE);
                Second_Attach.setVisibility(View.VISIBLE);
                Comment_two_img.setVisibility(View.VISIBLE);
                comment_two_media_type_icon.setImageResource(R.drawable.ic_gallery_icon);
                SetImageBackground(Comment_two_img, second_review.getAttatchment_path());
                Comment_two_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = "";
                        path = URL.images_baseurl + second_review.getAttatchment_path();
                        Intent intent = new Intent(context, MediPreview.class);
                        intent.putExtra("path", path);
                        intent.putExtra("isvideo", false);
                        context.startActivity(intent);
                    }
                });
            } else if (second_review.getAttatchment_type().equalsIgnoreCase("video")) {
                comment_two_media_type_icon.setVisibility(View.VISIBLE);
                Second_Attach.setVisibility(View.VISIBLE);
                Comment_two_img.setVisibility(View.VISIBLE);
                comment_two_media_type_icon.setImageResource(R.drawable.ic_video_play_icon);
                SetImageBackground(Comment_two_img, second_review.getAttatchment_poster());
                Comment_two_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = "";
                        path = URL.videos_baseurl + second_review.getAttatchment_path();
                        Intent intent = new Intent(context, MediPreview.class);
                        intent.putExtra("path", path);
                        intent.putExtra("isvideo", true);
                        context.startActivity(intent);
                    }
                });
            } else {
                comment_two_media_type_icon.setVisibility(GONE);
                Comment_two_img.setVisibility(GONE);
                Second_Attach.setVisibility(GONE);
            }
        } else {
            Second_Attach.setVisibility(GONE);
            comment_two_media_type_icon.setVisibility(GONE);
            Comment_two_img.setVisibility(GONE);
        }
        if (second_review.getIs_user_flaged_count() == 1) {
            comment_two_report_icon.setImageResource(R.drawable.ic_flag_strain);
            comment_two_report_text.setTextColor(Color.parseColor("#f4c42f"));

        } else {
            comment_two_report_icon.setImageResource(R.drawable.ic_flag_white);
            comment_two_report_text.setTextColor(Color.parseColor("#caccca"));
        }
    }

    public void SetImageBackground(final ImageView imageView, String Path) {
        if (context != null) {
            try {
                Glide.with(context)
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
                                    BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
                                    imageView.setImageDrawable(null);
                                    imageView.setBackground(drawable);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                return false;
                            }
                        })
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                Log.d("ready", model);
//                                try {
//                                    Drawable d = resource;
//                                    Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
//                                    bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
//                                    int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
//                                    Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
//                                    BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
//                                    imageView.setImageDrawable(null);
//                                    imageView.setBackground(drawable);
//                                } catch (Exception ex) {
//                                    ex.printStackTrace();
//                                }
//                                return false;
//                            }
                        .into(400, 400);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public void SetImageDrawable(final ImageView imageView, String Path) {
        if (getActivity() != null) {
            if (!getActivity().isFinishing()) {
                if (Path.contains("facebook.com") || Path.contains("https") || Path.contains("http") || Path.contains("google.com") || Path.contains("googleusercontent.com")) {
                    Path = Path;
                } else {
                    Path = images_baseurl + Path;
                }
                Glide.with(context)
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
    }


    public void SurvayQuestionOneDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View mView = factory.inflate(R.layout.survay_question_dialog_one, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
        ImageView cross_button = mView.findViewById(R.id.cross_button);
        Button start_survey = mView.findViewById(R.id.start_survey);
        start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) view.getContext());
                dialog.dismiss();
                dialouge_1();

            }
        });
        cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) view.getContext());
                dialog.dismiss();
                CallDummyDialogue();
            }
        });
    }

    static int count, pos;
    AlertDialog.Builder mBuilderFirst, mBuilderSecond, mBuilderThree, mBuilderFour, mBuilderFive, mBuilderSix;
    AlertDialog dialogFirst, dialogSecond, dialogThree, dialogFour, dialogFive, dialogSix;
    ArrayList<AddExpertiseQuestionActivity.DataModelEntryExpert> firstList = new ArrayList<>(),
            secList = new ArrayList<>(), threeList = new ArrayList<>(), fiveList = new ArrayList<>(), sixList = new ArrayList<>();
    ExpertAreaRecylerAdapter firstAdapter, secAdapter, threeAdapter, fiveAdapter, sixAdapter;

    public void dialouge_1() {
        all_array_list.add("Question_1");
        mBuilderFirst = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View mView = factory.inflate(R.layout.survay_dialog_question_one, null);
        mBuilderFirst.setView(mView);
        dialogFirst = mBuilderFirst.create();
        dialogFirst.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFirst.setCanceledOnTouchOutside(false);
        dialogFirst.setCancelable(false);
        dialogFirst.setCanceledOnTouchOutside(false);
        dialogFirst.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogFirst.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogFirst.show();

        ImageView cross_button = (ImageView) mView.findViewById(R.id.cross_button);
        final AutoCompleteTextView NameOREmail;
        final RelativeLayout selected_layout, _2_rlt, _3_rlt, _1_rtl;
        final ImageView selected_2_img, selected_1_img, selected_3_img;
        final LinearLayout layout_save_continue, auto_complete_textview_textview;
        final LinearLayout add_upto_3_layout;
        final TextView selected_item_1;
        final TextView selected_item_2;
        final TextView selected_item_3;
        final Button save_continue_button;
        final Button add_upto_3_button;
        final TextView add_upto_3;
        final View view_line;
        final View v = mView.findViewById(R.id.no_suggest_layout);
        final List<String> StrainKeywords = new ArrayList<String>();
        final RecyclerView first_rv = mView.findViewById(R.id.first_rv);
        final TextView or_view = mView.findViewById(R.id.or_view);
//        setListenerForSuggestion(v);
        first_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        selected_item_1 = (TextView) mView.findViewById(R.id.selected_first_item);
        selected_item_2 = (TextView) mView.findViewById(R.id.selected_2_item);
        selected_item_3 = (TextView) mView.findViewById(R.id.selected_3_item);
        selected_1_img = mView.findViewById(R.id.selected_1_img);
        selected_2_img = mView.findViewById(R.id.selected_2_img);
        selected_3_img = mView.findViewById(R.id.selected_3_img);
        selected_1_img.setOnClickListener(new OneViewClick());
        selected_2_img.setOnClickListener(new OneViewClick());
        selected_3_img.setOnClickListener(new OneViewClick());
        _2_rlt = mView.findViewById(R.id._2_rlt);
        _3_rlt = mView.findViewById(R.id._3_rlt);
        _1_rtl = mView.findViewById(R.id._1_rtl);
        selected_layout = (RelativeLayout) mView.findViewById(R.id.selected_layout);
        save_continue_button = (Button) mView.findViewById(R.id.save_continue);
        layout_save_continue = (LinearLayout) mView.findViewById(R.id.save_continue_layout);
        add_upto_3_layout = (LinearLayout) mView.findViewById(R.id.add_upto_3_layout);
        auto_complete_textview_textview = (LinearLayout) mView.findViewById(R.id.auto_complete_textview_textview);
        NameOREmail = mView.findViewById(R.id.name_or_email);
        add_upto_3 = (TextView) mView.findViewById(R.id.add_upto_3);
        view_line = (View) mView.findViewById(R.id.view_line);
        NameOREmail.setThreshold(1);

        for (int x = 0; x < strainOverViewDataModel.getMedicalConditionAnswers().size(); x++) {
            StrainKeywords.add(strainOverViewDataModel.getMedicalConditionAnswers().get(x).getM_condition());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.simple_text_view, StrainKeywords);
        NameOREmail.setAdapter(adapter);
        firstAdapter = new ExpertAreaRecylerAdapter(getContext(), firstList, 2);
        first_rv.setAdapter(firstAdapter);
        first_rv.setVisibility(View.VISIBLE);
        firstAdapter.setClickListener(new ExpertAreaRecylerAdapter.ItemClickListenerKey() {
            @Override
            public void onItemClickKey(View view, int position) {

            }

            @Override
            public void onQuestionOneCross(String text) {
                //TODO REMOVE EDIT
                if (firstAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);
                    add_upto_3.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    add_upto_3_layout.setVisibility(View.VISIBLE);
                    auto_complete_textview_textview.setVisibility(GONE);


                }
                if (!firstAdapter.isSuggestItem(text)) {
                    StrainKeywords.add(text);
                    adapter.add(text);
                    adapter.notifyDataSetChanged();
                }
                if (firstAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);

                } else {
                    if (firstAdapter.getItemCount() == 0) {
                        or_view.setVisibility(GONE);
                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onQuestionTwoCross(String text) {
                //TODO REMOVE EDIT
                if (firstAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);
                    add_upto_3.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    add_upto_3_layout.setVisibility(View.VISIBLE);
                    auto_complete_textview_textview.setVisibility(GONE);
                }
                if (!firstAdapter.isSuggestItem(text)) {
                    StrainKeywords.add(text);
                    adapter.add(text);
                    adapter.notifyDataSetChanged();
                }
                if (firstAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);
                } else {
                    if (firstAdapter.getItemCount() == 0) {
                        or_view.setVisibility(GONE);
                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        for (AddExpertiseQuestionActivity.DataModelEntryExpert item : firstList) {
            for (int i = 0; i < StrainKeywords.size(); i++) {
                if (item.getTitle().equalsIgnoreCase(StrainKeywords.get(i))) {
                    StrainKeywords.remove(i);
                }
            }
        }
        LinearLayout listen = v.findViewById(R.id.suggest_add);
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstAdapter.isContainItem(NameOREmail.getText().toString().trim())) {
                    CustomeToast.ShowCustomToast(view.getContext(), "This Suggestion Already Added!", Gravity.TOP);
                } else if (NameOREmail.getText().toString().trim().length() == 0) {
                    CustomeToast.ShowCustomToast(view.getContext(), "Field is empty!", Gravity.TOP);
                } else {
                    try {
                        new VollyAPICall(view.getContext()
                                , true
                                , URL.suggest_medical_condition
                                , new JSONObject().put("medical_condition", NameOREmail.getText().toString().trim())
                                , user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                            @Override
                            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {

                                all_array_list.add(NameOREmail.getText().toString().trim() + "\n");
                                firstList.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(NameOREmail.getText().toString().trim(), true));
                                firstAdapter.notifyDataSetChanged();
                                NameOREmail.setText("");
                                if (firstAdapter.getItemCount() > 0) {
                                    layout_save_continue.setVisibility(View.VISIBLE);
                                    add_upto_3.setVisibility(View.VISIBLE);
                                    view_line.setVisibility(View.VISIBLE);
                                    add_upto_3_layout.setVisibility(View.VISIBLE);
                                    auto_complete_textview_textview.setVisibility(GONE);
                                }
                                if (firstAdapter.getItemCount() == 3) {
                                    selected_layout.setVisibility(GONE);
                                    layout_save_continue.setVisibility(View.VISIBLE);
                                } else {
                                    if (firstAdapter.getItemCount() == 0) {
                                        or_view.setVisibility(GONE);
                                        layout_save_continue.setVisibility(GONE);
                                    }
                                    selected_layout.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, APIActions.ApiActions.get_suggestion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        NameOREmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (NameOREmail.getText().length() == 0) {
                        v.setVisibility(GONE);
                    }
                } else {
                    v.setVisibility(GONE);
                }
            }
        });
        NameOREmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (NameOREmail.isPopupShowing()) {
                                v.setVisibility(GONE);
                            } else {

                                v.setVisibility(GONE);
                            }
                        }
                    }, 10);

                } else {
                    v.setVisibility(GONE);
                }
            }
        });
        NameOREmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setVisibility(GONE);
                    }
                }, 10);
                all_array_list.add(adapterView.getItemAtPosition(i).toString() + "\n");
                firstList.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(adapterView.getItemAtPosition(i).toString().trim(), false));
                firstAdapter.notifyDataSetChanged();
                StrainKeywords.remove(adapterView.getItemAtPosition(i));
                adapter.remove((String) adapterView.getItemAtPosition(i));
                adapter.notifyDataSetChanged();
                NameOREmail.setText("");
                if (firstAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);
                    add_upto_3.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    add_upto_3_layout.setVisibility(View.VISIBLE);
                    auto_complete_textview_textview.setVisibility(GONE);

                }
                if (firstAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);
                } else {
                    if (firstAdapter.getItemCount() == 0) {
                        or_view.setVisibility(GONE);
                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }


            }
        });
        add_upto_3_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auto_complete_textview_textview.setVisibility(View.VISIBLE);
                add_upto_3_layout.setVisibility(GONE);
            }
        });
        if (firstAdapter.getItemCount() > 0) {
            layout_save_continue.setVisibility(View.VISIBLE);
            add_upto_3.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.VISIBLE);
            add_upto_3_layout.setVisibility(View.VISIBLE);
            auto_complete_textview_textview.setVisibility(GONE);
        }
        if (firstAdapter.getItemCount() == 3) {
            selected_layout.setVisibility(GONE);
            layout_save_continue.setVisibility(View.VISIBLE);
        } else {
            if (firstAdapter.getItemCount() == 0) {
                or_view.setVisibility(GONE);
                layout_save_continue.setVisibility(GONE);
            }
            selected_layout.setVisibility(View.VISIBLE);
        }
        cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) v.getContext());
                CallDummyDialogue();
                survay_question_one = "";
                survay_question_two = "";
                survay_question_three = "";
                survay_question_four = "";
                survay_question_five = "";
                survay_question_six = "";
                firstList.clear();
                secList.clear();
                threeList.clear();
                firstList.clear();
                sixList.clear();
                yes_for_list = "";
                yes_no = 0;
                dialogFirst.dismiss();


            }
        });
        save_continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) v.getContext());
                if (firstAdapter.getItemCount() > 0) {
                    HideKeyboard((Activity) v.getContext());
                    dialogFirst.dismiss();
                    dialouge_2();
                } else {
                    CustomeToast.ShowCustomToast(getContext(), "At least one condition is required!", Gravity.TOP);
                }
            }
        });
    }

    private void dialouge_2() {
        all_array_list.add("Question_2");
        mBuilderSecond = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View mView = factory.inflate(R.layout.survay_dialog_question_two, null);
        mBuilderSecond.setView(mView);
        dialogSecond = mBuilderSecond.create();
        dialogSecond.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSecond.setCanceledOnTouchOutside(false);
        dialogSecond.setCancelable(false);
        dialogSecond.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSecond.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogSecond.show();
        ImageView cross_button = (ImageView) mView.findViewById(R.id.cross_button);
        ImageView back_button = (ImageView) mView.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard((Activity) v.getContext());
                dialogSecond.dismiss();
                dialouge_1();
            }
        });
        final AutoCompleteTextView NameOREmail;
        final RelativeLayout selected_layout, _2_rlt, _3_rlt, _1_rtl;
        final ImageView selected_2_img, selected_1_img, selected_3_img;
        final LinearLayout layout_save_continue, auto_complete_textview_textview;
        final LinearLayout add_upto_3_layout;
        final TextView selected_item_1;
        final TextView selected_item_2;
        final TextView selected_item_3;
        final Button save_continue_button;
        final Button add_upto_3_button;
        final TextView add_upto_3;
        final View view_line;
        final View v = mView.findViewById(R.id.no_suggest_layout);
        final RecyclerView first_rv = mView.findViewById(R.id.sec_rv);
        final TextView or_view = mView.findViewById(R.id.or_view);
        first_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        setListenerForSuggestion(v);
        selected_item_1 = (TextView) mView.findViewById(R.id.selected_first_item);
        selected_item_2 = (TextView) mView.findViewById(R.id.selected_2_item);
        selected_item_3 = (TextView) mView.findViewById(R.id.selected_3_item);
        selected_1_img = mView.findViewById(R.id.selected_1_img);
        selected_2_img = mView.findViewById(R.id.selected_2_img);
        selected_3_img = mView.findViewById(R.id.selected_3_img);
        selected_1_img.setOnClickListener(new OneViewClick());
        selected_2_img.setOnClickListener(new OneViewClick());
        selected_3_img.setOnClickListener(new OneViewClick());
        _2_rlt = mView.findViewById(R.id._2_rlt);
        _3_rlt = mView.findViewById(R.id._3_rlt);
        _1_rtl = mView.findViewById(R.id._1_rtl);
        selected_layout = (RelativeLayout) mView.findViewById(R.id.selected_layout);
        save_continue_button = (Button) mView.findViewById(R.id.save_continue);
        layout_save_continue = (LinearLayout) mView.findViewById(R.id.save_continue_layout);
        add_upto_3_layout = (LinearLayout) mView.findViewById(R.id.add_upto_3_layout);
        auto_complete_textview_textview = (LinearLayout) mView.findViewById(R.id.auto_complete_textview_textview);
        NameOREmail = mView.findViewById(R.id.name_or_email);
        add_upto_3 = (TextView) mView.findViewById(R.id.add_upto_3);
        view_line = (View) mView.findViewById(R.id.view_line);
        NameOREmail.setThreshold(1);
        final List<String> StrainKeywords = new ArrayList<String>();
        for (int x = 0; x < strainOverViewDataModel.getModesAndSensationsAnswers().size(); x++) {
            StrainKeywords.add(strainOverViewDataModel.getModesAndSensationsAnswers().get(x).getSensation());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.simple_text_view, StrainKeywords);
        NameOREmail.setAdapter(adapter);
        secAdapter = new ExpertAreaRecylerAdapter(getContext(), secList, 2);
        first_rv.setAdapter(secAdapter);
        first_rv.setVisibility(View.VISIBLE);
        secAdapter.setClickListener(new ExpertAreaRecylerAdapter.ItemClickListenerKey() {
            @Override
            public void onItemClickKey(View view, int position) {

            }

            @Override
            public void onQuestionOneCross(String text) {
                //TODO REMOVE EDIT
                if (secAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);
                    add_upto_3.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    add_upto_3_layout.setVisibility(View.VISIBLE);
                    auto_complete_textview_textview.setVisibility(GONE);
                }
                if (!secAdapter.isSuggestItem(text)) {
                    StrainKeywords.add(text);
                    adapter.add(text);
                    adapter.notifyDataSetChanged();
                }
                if (secAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);
                } else {
                    if (secAdapter.getItemCount() == 0) {
                        or_view.setVisibility(GONE);
                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onQuestionTwoCross(String text) {
                //TODO REMOVE EDIT
                if (secAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);
                    add_upto_3.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    add_upto_3_layout.setVisibility(View.VISIBLE);
                    auto_complete_textview_textview.setVisibility(GONE);
                }
                if (!secAdapter.isSuggestItem(text)) {
                    StrainKeywords.add(text);
                    adapter.add(text);
                    adapter.notifyDataSetChanged();
                }
                if (secAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);
                } else {
                    if (secAdapter.getItemCount() == 0) {
                        or_view.setVisibility(GONE);
                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        LinearLayout listen = v.findViewById(R.id.suggest_add);
        for (AddExpertiseQuestionActivity.DataModelEntryExpert item : secList) {
            for (int i = 0; i < StrainKeywords.size(); i++) {
                if (item.getTitle().equalsIgnoreCase(StrainKeywords.get(i))) {
                    StrainKeywords.remove(i);
                }
            }
        }
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (secAdapter.isContainItem(NameOREmail.getText().toString().trim())) {
                    CustomeToast.ShowCustomToast(view.getContext(), "This Suggestion Already Added!", Gravity.TOP);
                } else if (NameOREmail.getText().toString().trim().length() == 0) {
                    CustomeToast.ShowCustomToast(view.getContext(), "Field is empty!", Gravity.TOP);
                } else {
                    try {
                        new VollyAPICall(view.getContext()
                                , true
                                , URL.suggest_sensation
                                , new JSONObject().put("sensation", NameOREmail.getText().toString().trim())
                                , user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                            @Override
                            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                                all_array_list.add(NameOREmail.getText().toString().trim() + "\n");
                                secList.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(NameOREmail.getText().toString().trim(), true));
                                secAdapter.notifyDataSetChanged();
                                NameOREmail.setText("");
                                if (secAdapter.getItemCount() > 0) {
                                    layout_save_continue.setVisibility(View.VISIBLE);
                                    add_upto_3.setVisibility(View.VISIBLE);
                                    view_line.setVisibility(View.VISIBLE);
                                    add_upto_3_layout.setVisibility(View.VISIBLE);
                                    auto_complete_textview_textview.setVisibility(GONE);
                                }
                                if (secAdapter.getItemCount() == 3) {
                                    selected_layout.setVisibility(GONE);
                                    layout_save_continue.setVisibility(View.VISIBLE);
                                } else {
                                    if (secAdapter.getItemCount() == 0) {
                                        or_view.setVisibility(GONE);
                                        layout_save_continue.setVisibility(GONE);
                                    }
                                    selected_layout.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, APIActions.ApiActions.get_suggestion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        count = 0;
        NameOREmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (NameOREmail.getText().length() == 0) {
                        v.setVisibility(GONE);
                    }
                } else {
                    v.setVisibility(GONE);
                }
            }
        });
        NameOREmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (NameOREmail.isPopupShowing()) {
                                v.setVisibility(GONE);
                            } else {
                                v.setVisibility(GONE);
                            }
                        }
                    }, 10);
                } else {
                    v.setVisibility(GONE);
                }
            }
        });
        add_upto_3_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auto_complete_textview_textview.setVisibility(View.VISIBLE);
                add_upto_3_layout.setVisibility(GONE);
            }
        });
        NameOREmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int counter = 0;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setVisibility(GONE);
                    }
                }, 10);
                all_array_list.add(adapterView.getItemAtPosition(i).toString() + "\n");
                secList.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(adapterView.getItemAtPosition(i).toString().trim(), false));
                secAdapter.notifyDataSetChanged();
                StrainKeywords.remove(adapterView.getItemAtPosition(i));
                adapter.remove((String) adapterView.getItemAtPosition(i));
                adapter.notifyDataSetChanged();
                NameOREmail.setText("");
                if (secAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);
                    add_upto_3.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    add_upto_3_layout.setVisibility(View.VISIBLE);
                    auto_complete_textview_textview.setVisibility(GONE);
                }
                if (secAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);
                } else {
                    if (secAdapter.getItemCount() == 0) {
                        or_view.setVisibility(GONE);
                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        if (secAdapter.getItemCount() > 0) {
            layout_save_continue.setVisibility(View.VISIBLE);
            add_upto_3.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.VISIBLE);
            add_upto_3_layout.setVisibility(View.VISIBLE);
            auto_complete_textview_textview.setVisibility(GONE);
        }
        if (secAdapter.getItemCount() == 3) {
            selected_layout.setVisibility(GONE);
            layout_save_continue.setVisibility(View.VISIBLE);
        } else {
            if (secAdapter.getItemCount() == 0) {
                or_view.setVisibility(GONE);
                layout_save_continue.setVisibility(GONE);
            }
            selected_layout.setVisibility(View.VISIBLE);
        }
        cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) v.getContext());
                CallDummyDialogue();
                survay_question_one = "";
                survay_question_two = "";
                survay_question_three = "";
                survay_question_four = "";
                survay_question_five = "";
                survay_question_six = "";
                firstList.clear();
                secList.clear();
                threeList.clear();
                firstList.clear();
                sixList.clear();
                yes_for_list = "";
                yes_no = 0;
                dialogSecond.dismiss();

            }
        });
        save_continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) v.getContext());
                if (secAdapter.getItemCount() > 0) {
                    dialogSecond.dismiss();
                    dialouge_3();

                } else {
                    CustomeToast.ShowCustomToast(getContext(), "At least one condition is required!", Gravity.TOP);
                }

            }
        });
    }

    private void dialouge_3() {
        all_array_list.add("Question_3");
        mBuilderThree = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View mView = factory.inflate(R.layout.survay_dialog_question_three, null);
        mBuilderThree.setView(mView);
        dialogThree = mBuilderThree.create();
        dialogThree.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogThree.setCanceledOnTouchOutside(false);
        dialogThree.setCancelable(false);
        dialogThree.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogThree.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialogThree.show();
        ImageView cross_button = (ImageView) mView.findViewById(R.id.cross_button);
        ImageView back_button = (ImageView) mView.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard((Activity) v.getContext());
                dialogThree.dismiss();
                dialouge_2();

            }
        });
        final AutoCompleteTextView NameOREmail;
        final RelativeLayout selected_layout, _2_rlt, _3_rlt, _1_rtl;
        final ImageView selected_2_img, selected_1_img, selected_3_img;
        final LinearLayout layout_save_continue, auto_complete_textview_textview;
        final LinearLayout add_upto_3_layout;
        final TextView selected_item_1;
        final TextView selected_item_2;
        final TextView selected_item_3;
        final Button save_continue_button;
        final Button add_upto_3_button;
        final TextView add_upto_3;
        final View view_line;
        final View v = mView.findViewById(R.id.no_suggest_layout);
        final TextView or_view = mView.findViewById(R.id.or_view);
        final RecyclerView first_rv = mView.findViewById(R.id.three_rv);
        first_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        setListenerForSuggestion(v);
        selected_item_1 = (TextView) mView.findViewById(R.id.selected_first_item);
        selected_item_2 = (TextView) mView.findViewById(R.id.selected_2_item);
        selected_item_3 = (TextView) mView.findViewById(R.id.selected_3_item);
        selected_1_img = mView.findViewById(R.id.selected_1_img);
        selected_2_img = mView.findViewById(R.id.selected_2_img);
        selected_3_img = mView.findViewById(R.id.selected_3_img);
        selected_1_img.setOnClickListener(new OneViewClick());
        selected_2_img.setOnClickListener(new OneViewClick());
        selected_3_img.setOnClickListener(new OneViewClick());
        _2_rlt = mView.findViewById(R.id._2_rlt);
        _3_rlt = mView.findViewById(R.id._3_rlt);
        _1_rtl = mView.findViewById(R.id._1_rtl);
        selected_layout = (RelativeLayout) mView.findViewById(R.id.selected_layout);
        save_continue_button = (Button) mView.findViewById(R.id.save_continue);
        layout_save_continue = (LinearLayout) mView.findViewById(R.id.save_continue_layout);
        add_upto_3_layout = (LinearLayout) mView.findViewById(R.id.add_upto_3_layout);
        auto_complete_textview_textview = (LinearLayout) mView.findViewById(R.id.auto_complete_textview_textview);
        NameOREmail = mView.findViewById(R.id.name_or_email);
        add_upto_3 = (TextView) mView.findViewById(R.id.add_upto_3);
        view_line = (View) mView.findViewById(R.id.view_line);
        NameOREmail.setThreshold(1);
        final List<String> StrainKeywords = new ArrayList<String>();
        for (int x = 0; x < strainOverViewDataModel.getNegativeEffectAnswers().size(); x++) {
            StrainKeywords.add(strainOverViewDataModel.getNegativeEffectAnswers().get(x).getEffect());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.simple_text_view, StrainKeywords);
        NameOREmail.setAdapter(adapter);
        threeAdapter = new ExpertAreaRecylerAdapter(getContext(), threeList, 2);
        first_rv.setAdapter(threeAdapter);
        first_rv.setVisibility(View.VISIBLE);
        threeAdapter.setClickListener(new ExpertAreaRecylerAdapter.ItemClickListenerKey() {
            @Override
            public void onItemClickKey(View view, int position) {

            }

            @Override
            public void onQuestionOneCross(String text) {
                //TODO REMOVE EDIT
                if (threeAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);
                    add_upto_3.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    add_upto_3_layout.setVisibility(View.VISIBLE);
                    auto_complete_textview_textview.setVisibility(GONE);
                }
                if (!threeAdapter.isSuggestItem(text)) {
                    StrainKeywords.add(text);
                    adapter.add(text);
                    adapter.notifyDataSetChanged();
                }
                if (threeAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);
                } else {
                    if (threeAdapter.getItemCount() == 0) {
                        or_view.setVisibility(GONE);
                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onQuestionTwoCross(String text) {
                //TODO REMOVE EDIT
                if (threeAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);
                    add_upto_3.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    add_upto_3_layout.setVisibility(View.VISIBLE);
                    auto_complete_textview_textview.setVisibility(GONE);
                }
                if (!threeAdapter.isSuggestItem(text)) {
                    StrainKeywords.add(text);
                    adapter.add(text);
                    adapter.notifyDataSetChanged();
                }
                if (threeAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);
                } else {
                    if (threeAdapter.getItemCount() == 0) {
                        or_view.setVisibility(GONE);
                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        LinearLayout listen = v.findViewById(R.id.suggest_add);
        for (AddExpertiseQuestionActivity.DataModelEntryExpert item : threeList) {
            for (int i = 0; i < StrainKeywords.size(); i++) {
                if (item.getTitle().equalsIgnoreCase(StrainKeywords.get(i))) {
                    StrainKeywords.remove(i);
                }
            }
        }
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (threeAdapter.isContainItem(NameOREmail.getText().toString().trim())) {
                    CustomeToast.ShowCustomToast(view.getContext(), "This Suggestion Already Added!", Gravity.TOP);
                } else if (NameOREmail.getText().toString().trim().length() == 0) {
                    CustomeToast.ShowCustomToast(view.getContext(), "Field is empty!", Gravity.TOP);
                } else {
                    try {
                        new VollyAPICall(view.getContext()
                                , true
                                , URL.suggest_negative_effect
                                , new JSONObject().put("negative_effect", NameOREmail.getText().toString().trim())
                                , user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                            @Override
                            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                                all_array_list.add(NameOREmail.getText().toString().trim() + "\n");
                                threeList.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(NameOREmail.getText().toString().trim(), true));
                                threeAdapter.notifyDataSetChanged();
                                NameOREmail.setText("");
                                if (threeAdapter.getItemCount() > 0) {
                                    layout_save_continue.setVisibility(View.VISIBLE);
                                    add_upto_3.setVisibility(View.VISIBLE);
                                    view_line.setVisibility(View.VISIBLE);
                                    add_upto_3_layout.setVisibility(View.VISIBLE);
                                    auto_complete_textview_textview.setVisibility(GONE);
                                }
                                if (threeAdapter.getItemCount() == 3) {
                                    selected_layout.setVisibility(GONE);
                                    layout_save_continue.setVisibility(View.VISIBLE);
                                } else {
                                    if (threeAdapter.getItemCount() == 0) {
                                        or_view.setVisibility(GONE);
                                        layout_save_continue.setVisibility(GONE);
                                    }
                                    selected_layout.setVisibility(View.VISIBLE);
                                }

                            }

                            @Override
                            public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, APIActions.ApiActions.get_suggestion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        NameOREmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (NameOREmail.getText().length() == 0) {
                        v.setVisibility(GONE);
                    }
                } else {
                    v.setVisibility(GONE);
                }
            }
        });
        NameOREmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (NameOREmail.isPopupShowing()) {
                                v.setVisibility(GONE);
                            } else {
                                v.setVisibility(GONE);
                            }
                        }
                    }, 10);
                } else {
                    v.setVisibility(GONE);
                }
            }
        });
        add_upto_3_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auto_complete_textview_textview.setVisibility(View.VISIBLE);
                add_upto_3_layout.setVisibility(GONE);
            }
        });
        NameOREmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int counter = 0;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setVisibility(GONE);
                    }
                }, 10);
                all_array_list.add(adapterView.getItemAtPosition(i).toString() + "\n");
                threeList.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(adapterView.getItemAtPosition(i).toString().trim(), false));
                threeAdapter.notifyDataSetChanged();
                StrainKeywords.remove(adapterView.getItemAtPosition(i));
                adapter.remove((String) adapterView.getItemAtPosition(i));
                adapter.notifyDataSetChanged();
                NameOREmail.setText("");
                if (threeAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);
                    add_upto_3.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    add_upto_3_layout.setVisibility(View.VISIBLE);
                    auto_complete_textview_textview.setVisibility(GONE);
                }
                if (threeAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);
                } else {
                    if (threeAdapter.getItemCount() == 0) {
                        or_view.setVisibility(GONE);
                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }


            }
        });
        if (threeAdapter.getItemCount() > 0) {
            layout_save_continue.setVisibility(View.VISIBLE);
            add_upto_3.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.VISIBLE);
            add_upto_3_layout.setVisibility(View.VISIBLE);
            auto_complete_textview_textview.setVisibility(GONE);
        }
        if (threeAdapter.getItemCount() == 3) {
            selected_layout.setVisibility(GONE);
            layout_save_continue.setVisibility(View.VISIBLE);
        } else {
            if (threeAdapter.getItemCount() == 0) {
                or_view.setVisibility(GONE);
                layout_save_continue.setVisibility(GONE);
            }
            selected_layout.setVisibility(View.VISIBLE);
        }
        cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) v.getContext());
                CallDummyDialogue();
                survay_question_one = "";
                survay_question_two = "";
                survay_question_three = "";
                survay_question_four = "";
                survay_question_five = "";
                survay_question_six = "";
                firstList.clear();
                secList.clear();
                threeList.clear();
                firstList.clear();
                sixList.clear();
                yes_for_list = "";
                yes_no = 0;
                dialogThree.dismiss();

            }
        });
        save_continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) v.getContext());
                if (threeAdapter.getItemCount() > 0) {
                    dialouge_4();
                    HideKeyboard((Activity) v.getContext());
                    dialogThree.dismiss();
                } else {
                    CustomeToast.ShowCustomToast(getContext(), "At least one condition is required!", Gravity.TOP);
                }
            }
        });
    }

    private void dialouge_4() {
        all_array_list.add("Question_4");
        mBuilderFour = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View mView = factory.inflate(R.layout.survay_dialog_question_four, null);
        mBuilderFour.setView(mView);
        dialogFour = mBuilderFour.create();
        dialogFour.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFour.setCanceledOnTouchOutside(false);
        dialogFour.setCanceledOnTouchOutside(false);
        dialogFour.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogFour.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogFour.show();
        ImageView cross_button = (ImageView) mView.findViewById(R.id.cross_button);
        ImageView back_button = (ImageView) mView.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard((Activity) view.getContext());
                dialogFour.dismiss();
                dialouge_3();

            }
        });
        final Button save_continue_button, yes, no;
        yes = mView.findViewById(R.id.yes);
        no = mView.findViewById(R.id.no);
        save_continue_button = mView.findViewById(R.id.save_continue);
        cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) view.getContext());
                CallDummyDialogue();
                survay_question_one = "";
                survay_question_two = "";
                survay_question_three = "";
                survay_question_four = "";
                survay_question_five = "";
                survay_question_six = "";
                firstList.clear();
                secList.clear();
                threeList.clear();
                firstList.clear();
                sixList.clear();
                yes_for_list = "";
                yes_no = 0;
                dialogFour.dismiss();

            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yes.setTextColor(Color.parseColor("#7CC244"));
                no.setTextColor(Color.parseColor("#000000"));
                yes_no = 1;
                HideKeyboard((Activity) view.getContext());
                yes_for_list = "YES";
                isNoSelect = 0;

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) view.getContext());
                no.setTextColor(Color.parseColor("#7CC244"));
                yes.setTextColor(Color.parseColor("#000000"));
                yes_no = 1;
                yes_for_list = "NO";
                isNoSelect = 1;

            }

        });
        if (yes_no == 1) {
            if (yes_for_list.equalsIgnoreCase("NO")) {
                HideKeyboard((Activity) view.getContext());
                no.setTextColor(Color.parseColor("#7CC244"));
                yes.setTextColor(Color.parseColor("#000000"));
                yes_no = 1;
                isNoSelect = 1;

            } else if (yes_for_list.equalsIgnoreCase("YES")) {
                HideKeyboard((Activity) view.getContext());
                yes.setTextColor(Color.parseColor("#7CC244"));
                no.setTextColor(Color.parseColor("#000000"));
                yes_no = 1;
                isNoSelect = 0;
            }
        }
        save_continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) view.getContext());
                all_array_list.add(yes_for_list + "\n");
                if (yes_no == 1) {
                    if (isNoSelect == 0){
                        dialouge_5();
                    }else {
                        dialouge_6();
                    }

                    HideKeyboard((Activity) view.getContext());
                    dialogFour.dismiss();
                } else {
                    CustomeToast.ShowCustomToast(getContext(), "Please choose an option!", Gravity.TOP);
                }

            }
        });
    }
    int isNoSelect = 0;

    private void dialouge_5() {
        all_array_list.add("Question_5");
        mBuilderFive = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View mView = factory.inflate(R.layout.survay_dialog_question_five, null);
        mBuilderFive.setView(mView);
        dialogFive = mBuilderFive.create();
        dialogFive.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFive.setCanceledOnTouchOutside(false);
        dialogFive.setCanceledOnTouchOutside(false);
        dialogFive.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogFive.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogFive.show();
        ImageView cross_button = (ImageView) mView.findViewById(R.id.cross_button);
        ImageView back_button = (ImageView) mView.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard((Activity) v.getContext());
                dialogFive.dismiss();
                dialouge_4();

            }
        });
        final AutoCompleteTextView NameOREmail;
        final RelativeLayout selected_layout, _2_rlt, _3_rlt, _1_rtl;
        final ImageView selected_2_img, selected_1_img, selected_3_img;
        final LinearLayout layout_save_continue, auto_complete_textview_textview;
        final LinearLayout add_upto_3_layout;
        final TextView selected_item_1;
        final TextView selected_item_2;
        final TextView selected_item_3;
        final Button save_continue_button;
        final Button add_upto_3_button;
        final TextView add_upto_3;
        final View view_line;
        final View v = mView.findViewById(R.id.no_suggest_layout);
        final TextView or_view = mView.findViewById(R.id.or_view);
        final RecyclerView first_rv = mView.findViewById(R.id.five_rv);
        first_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        setListenerForSuggestion(v);

        selected_item_1 = (TextView) mView.findViewById(R.id.selected_first_item);
        selected_item_2 = (TextView) mView.findViewById(R.id.selected_2_item);
        selected_item_3 = (TextView) mView.findViewById(R.id.selected_3_item);
        selected_1_img = mView.findViewById(R.id.selected_1_img);
        selected_2_img = mView.findViewById(R.id.selected_2_img);
        selected_3_img = mView.findViewById(R.id.selected_3_img);
        selected_1_img.setOnClickListener(new OneViewClick());
        selected_2_img.setOnClickListener(new OneViewClick());
        selected_3_img.setOnClickListener(new OneViewClick());
        _2_rlt = mView.findViewById(R.id._2_rlt);
        _3_rlt = mView.findViewById(R.id._3_rlt);
        _1_rtl = mView.findViewById(R.id._1_rtl);
        selected_layout = (RelativeLayout) mView.findViewById(R.id.selected_layout);
        save_continue_button = (Button) mView.findViewById(R.id.save_continue);
        layout_save_continue = (LinearLayout) mView.findViewById(R.id.save_continue_layout);
        add_upto_3_layout = (LinearLayout) mView.findViewById(R.id.add_upto_3_layout);
        auto_complete_textview_textview = (LinearLayout) mView.findViewById(R.id.auto_complete_textview_textview);
        NameOREmail = mView.findViewById(R.id.name_or_email);
        add_upto_3 = (TextView) mView.findViewById(R.id.add_upto_3);
        view_line = (View) mView.findViewById(R.id.view_line);
        NameOREmail.setThreshold(1);
        final List<String> StrainKeywords = new ArrayList<String>();
        for (int x = 0; x < strainOverViewDataModel.getDiseasePreventionAnswers().size(); x++) {
            StrainKeywords.add(strainOverViewDataModel.getDiseasePreventionAnswers().get(x).getPrevention());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.simple_text_view, StrainKeywords);
        NameOREmail.setAdapter(adapter);
        fiveAdapter = new ExpertAreaRecylerAdapter(getContext(), fiveList, 2);
        first_rv.setAdapter(fiveAdapter);
        first_rv.setVisibility(View.VISIBLE);
        fiveAdapter.setClickListener(new ExpertAreaRecylerAdapter.ItemClickListenerKey() {
            @Override
            public void onItemClickKey(View view, int position) {

            }

            @Override
            public void onQuestionOneCross(String text) {
                //TODO REMOVE EDIT
                if (fiveAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);
                    add_upto_3.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    add_upto_3_layout.setVisibility(View.VISIBLE);
                    auto_complete_textview_textview.setVisibility(GONE);
                }
                if (!fiveAdapter.isSuggestItem(text)) {
                    StrainKeywords.add(text);
                    adapter.add(text);
                    adapter.notifyDataSetChanged();
                }
                if (fiveAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);
                } else {
                    if (fiveAdapter.getItemCount() == 0) {
                        or_view.setVisibility(GONE);
                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onQuestionTwoCross(String text) {
                //TODO REMOVE EDIT
                if (fiveAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);
                    add_upto_3.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    add_upto_3_layout.setVisibility(View.VISIBLE);
                    auto_complete_textview_textview.setVisibility(GONE);
                }
                if (!fiveAdapter.isSuggestItem(text)) {
                    StrainKeywords.add(text);
                    adapter.add(text);
                    adapter.notifyDataSetChanged();
                }
                if (fiveAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);
                } else {
                    if (fiveAdapter.getItemCount() == 0) {
                        or_view.setVisibility(GONE);
                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        LinearLayout listen = v.findViewById(R.id.suggest_add);
        for (AddExpertiseQuestionActivity.DataModelEntryExpert item : fiveList) {
            for (int i = 0; i < StrainKeywords.size(); i++) {
                if (item.getTitle().equalsIgnoreCase(StrainKeywords.get(i))) {
                    StrainKeywords.remove(i);
                }
            }
        }
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fiveAdapter.isContainItem(NameOREmail.getText().toString().trim())) {
                    CustomeToast.ShowCustomToast(view.getContext(), "This Suggestion Already Added!", Gravity.TOP);
                } else if (NameOREmail.getText().toString().trim().length() == 0) {
                    CustomeToast.ShowCustomToast(view.getContext(), "Field is empty!", Gravity.TOP);
                } else {
                    try {
                        new VollyAPICall(view.getContext()
                                , true
                                , URL.suggest_disease_prevention
                                , new JSONObject().put("prevention", NameOREmail.getText().toString().trim())
                                , user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                            @Override
                            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {

                                all_array_list.add(NameOREmail.getText().toString().trim() + "\n");
                                fiveList.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(NameOREmail.getText().toString().trim(), true));
                                fiveAdapter.notifyDataSetChanged();
                                if (survay_question_four.equalsIgnoreCase("")) {
                                    survay_question_four = survay_question_four + NameOREmail.getText().toString().trim();
                                } else {
                                    survay_question_four = survay_question_four + "," + NameOREmail.getText().toString().trim();
                                }
                                NameOREmail.setText("");
                                if (fiveAdapter.getItemCount() > 0) {
                                    layout_save_continue.setVisibility(View.VISIBLE);
                                    add_upto_3.setVisibility(View.VISIBLE);
                                    view_line.setVisibility(View.VISIBLE);
                                    add_upto_3_layout.setVisibility(View.VISIBLE);
                                    auto_complete_textview_textview.setVisibility(GONE);
                                }
                                if (fiveAdapter.getItemCount() == 3) {
                                    selected_layout.setVisibility(GONE);
                                    layout_save_continue.setVisibility(View.VISIBLE);
                                } else {
                                    if (fiveAdapter.getItemCount() == 0) {
                                        or_view.setVisibility(GONE);
                                        layout_save_continue.setVisibility(GONE);
                                    }
                                    selected_layout.setVisibility(View.VISIBLE);
                                }

                            }

                            @Override
                            public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, APIActions.ApiActions.get_suggestion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        add_upto_3_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auto_complete_textview_textview.setVisibility(View.VISIBLE);
                add_upto_3_layout.setVisibility(GONE);
            }
        });
        NameOREmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (NameOREmail.getText().length() == 0) {
                        v.setVisibility(GONE);
                    }
                } else {
                    v.setVisibility(GONE);
                }
            }
        });
        NameOREmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (NameOREmail.isPopupShowing()) {
                                v.setVisibility(GONE);
                            } else {
                                v.setVisibility(GONE);
                            }
                        }
                    }, 10);
                } else {
                    v.setVisibility(GONE);
                }
            }
        });
        NameOREmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int counter = 0;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setVisibility(GONE);
                    }
                }, 10);
                all_array_list.add(adapterView.getItemAtPosition(i).toString() + "\n");
                fiveList.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(adapterView.getItemAtPosition(i).toString().trim(), false));
                fiveAdapter.notifyDataSetChanged();
                if (survay_question_four.equalsIgnoreCase("")) {
                    survay_question_four = survay_question_four + adapterView.getItemAtPosition(i).toString().trim();
                } else {
                    survay_question_four = survay_question_four + "," + adapterView.getItemAtPosition(i).toString().trim();
                }
                StrainKeywords.remove(adapterView.getItemAtPosition(i));
                adapter.remove((String) adapterView.getItemAtPosition(i));
                adapter.notifyDataSetChanged();
                NameOREmail.setText("");
                if (fiveAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);
                    add_upto_3.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    add_upto_3_layout.setVisibility(View.VISIBLE);
                    auto_complete_textview_textview.setVisibility(GONE);
                }
                if (fiveAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);
                } else {
                    if (fiveAdapter.getItemCount() == 0) {
                        or_view.setVisibility(GONE);
                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }

            }
        });
        if (fiveAdapter.getItemCount() > 0) {
            layout_save_continue.setVisibility(View.VISIBLE);
            add_upto_3.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.VISIBLE);
            add_upto_3_layout.setVisibility(View.VISIBLE);
            auto_complete_textview_textview.setVisibility(GONE);
        }
        if (fiveAdapter.getItemCount() == 3) {
            selected_layout.setVisibility(GONE);
            layout_save_continue.setVisibility(View.VISIBLE);
        } else {
            if (fiveAdapter.getItemCount() == 0) {
                or_view.setVisibility(GONE);
                layout_save_continue.setVisibility(GONE);
            }
            selected_layout.setVisibility(View.VISIBLE);
        }

        cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) v.getContext());
                CallDummyDialogue();
                survay_question_one = "";
                survay_question_two = "";
                survay_question_three = "";
                survay_question_four = "";
                survay_question_five = "";
                survay_question_six = "";
                firstList.clear();
                secList.clear();
                threeList.clear();
                firstList.clear();
                sixList.clear();
                yes_for_list = "";
                yes_no = 0;
                dialogFive.dismiss();


            }
        });
        save_continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fiveAdapter.getItemCount() > 0) {
                    dialogFive.dismiss();
                    dialouge_6();
                    HideKeyboard((Activity) v.getContext());
                } else {
                    CustomeToast.ShowCustomToast(getContext(), "At least one condition is required!", Gravity.TOP);
                }
            }
        });
    }

    private void dialouge_6() {
        all_array_list.add("Question_6");
        mBuilderSix = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View mView = factory.inflate(R.layout.survay_dialog_question_six, null);
        mBuilderSix.setView(mView);
        dialogSix = mBuilderSix.create();
        dialogSix.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSix.setCanceledOnTouchOutside(false);
        dialogSix.setCanceledOnTouchOutside(false);
        dialogSix.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSix.setCancelable(false);
        dialogSix.setCanceledOnTouchOutside(false);
        dialogSix.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSix.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogSix.show();
        ImageView cross_button = (ImageView) mView.findViewById(R.id.cross_button);
        ImageView back_button = (ImageView) mView.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard((Activity) view.getContext());
                dialogSix.dismiss();
                if (isNoSelect == 0){
                    dialouge_5();
                }else {
                    dialouge_4();
                }
            }
        });
        final AutoCompleteTextView NameOREmail;
        final LinearLayout selected_layout;
        final LinearLayout layout_save_continue;
        final TextView selected_item_1;
        final Button save_continue_button;
        final RecyclerView first_rv = mView.findViewById(R.id.six_rv);
        first_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        selected_item_1 = (TextView) mView.findViewById(R.id.selected_first_item);
        selected_layout = mView.findViewById(R.id.selected_layout);
        save_continue_button = (Button) mView.findViewById(R.id.save_continue);
        layout_save_continue = (LinearLayout) mView.findViewById(R.id.save_continue_layout);
        cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) view.getContext());
                CallDummyDialogue();
                survay_question_one = "";
                survay_question_two = "";
                survay_question_three = "";
                survay_question_four = "";
                survay_question_five = "";
                survay_question_six = "";
                firstList.clear();
                secList.clear();
                threeList.clear();
                firstList.clear();
                sixList.clear();
                yes_for_list = "";
                yes_no = 0;
                dialogSix.dismiss();
            }
        });
        save_continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sixAdapter.getItemCount() > 0) {
                    dialogSix.dismiss();
                    dialoge_close();
                    HideKeyboard((Activity) view.getContext());
                } else {
                    CustomeToast.ShowCustomToast(getContext(), "At least select one condition!", Gravity.TOP);
                }
            }
        });
        LinearLayout add_another_layout;
        final List<String> listFlavour = new ArrayList<>();
        final ArrayList<MultiSelectModel> listOfCountries = new ArrayList<>();


        for (int x = 0; x < strainOverViewDataModel.getSurvayFlavourAnswers().size(); x++) {
            listFlavour.add(strainOverViewDataModel.getSurvayFlavourAnswers().get(x).getFlavor());
            listOfCountries.add(new MultiSelectModel(x, strainOverViewDataModel.getSurvayFlavourAnswers().get(x).getFlavor()));
        }

        final String[] listItems = new String[listFlavour.size()];
        final ArrayList<Integer> items = new ArrayList<>();
        final boolean[] checkedItems;
        checkedItems = new boolean[listItems.length];
        final ArrayList<Integer> mUserItems = new ArrayList<>();
        add_another_layout = (LinearLayout) mView.findViewById(R.id.add_another_layout);
        for (int x = 0; x < listFlavour.size(); x++) {
            listItems[x] = listFlavour.get(x);
            checkedItems[x] = false;
        }
        sixAdapter = new ExpertAreaRecylerAdapter(getContext(), sixList, 2);
        first_rv.setAdapter(sixAdapter);
        first_rv.setVisibility(View.VISIBLE);
        for (int i = 0; i < sixList.size(); i++) {
            for (int x = 0; x < listItems.length; x++) {
                if (listItems[x].equalsIgnoreCase(sixList.get(i).getTitle())) {
                    checkedItems[x] = true;
                }
            }
        }
        sixAdapter.setClickListener(new ExpertAreaRecylerAdapter.ItemClickListenerKey() {
            @Override
            public void onItemClickKey(View view, int position) {

            }

            @Override
            public void onQuestionOneCross(String text) {
                //TODO REMOVE EDIT
                if (sixAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);

                }
                for (int x = 0; x < listItems.length; x++) {
                    if (listItems[x].equalsIgnoreCase(text)) {

                        checkedItems[x] = false;
                    }
                }
                if (sixAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);
                } else {
                    if (sixAdapter.getItemCount() == 0) {

                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onQuestionTwoCross(String text) {
                //TODO REMOVE EDIT
                if (sixAdapter.getItemCount() > 0) {
                    layout_save_continue.setVisibility(View.VISIBLE);

                }
                for (int jk = 0; jk < listOfCountries.size(); jk++) {
                    if (listOfCountries.get(jk).getName().equalsIgnoreCase(text))
//                        if()
                        for (int j = 0; j < items.size(); j++) {
                            if (items.get(j).compareTo(listOfCountries.get(jk).getId()) == 0) {
                                items.remove(j);//e(j);
                            }
                        }
                }
                for (int x = 0; x < listItems.length; x++) {
                    if (listItems[x].equalsIgnoreCase(text)) {

                        checkedItems[x] = false;
                    }
                }
                if (sixAdapter.getItemCount() == 3) {
                    selected_layout.setVisibility(GONE);
                    layout_save_continue.setVisibility(View.VISIBLE);
                } else {
                    if (sixAdapter.getItemCount() == 0) {

                        layout_save_continue.setVisibility(GONE);
                    }
                    selected_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        add_another_layout.setOnClickListener(new View.OnClickListener() {

            int global_counter = 0;
            int global_counter_2 = 0;
            final int check_counter = 3;
            String item = "";

            @Override
            public void onClick(View view) {
                MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                        .title("Select Flavor:") //setting title for dialog
                        .titleSize(25)
                        .positiveText("Done")

                        .negativeText("Cancel")
                        .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                        .setMaxSelectionLimit(3) //you can set maximum checkbox selection limit (Optional)
                        .preSelectIDsList(items) //List of ids that you need to be selected
                        .multiSelectList(listOfCountries) // the multi select model list with ids and name
                        .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                            @Override
                            public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                                //will return list of selected IDS
                                sixList.clear();
                                items.clear();
                                for (int i = 0; i < selectedNames.size(); i++) {

                                    sixList.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(selectedNames.get(i), false, i));
                                    sixAdapter.notifyDataSetChanged();

                                }
                                for (int jk = 0; jk < listOfCountries.size(); jk++) {
                                    for (int i = 0; i < selectedNames.size(); i++) {
                                        if (listOfCountries.get(jk).getName().equalsIgnoreCase(selectedNames.get(i)))
                                            items.add(Integer.parseInt(String.valueOf(listOfCountries.get(jk).getId())));

                                    }

                                }
                                //items.add(i);
                                if (sixAdapter.getItemCount() > 0) {
                                    layout_save_continue.setVisibility(View.VISIBLE);
                                }
                                if (sixAdapter.getItemCount() == 3) {
                                    selected_layout.setVisibility(GONE);
                                }

                            }

                            @Override
                            public void onCancel() {

                            }


                        });
                multiSelectDialog.show(getFragmentManager(), "multiSelectDialog");
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
//                mBuilder.setTitle("Select Flavour:");
//                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//                        if (isChecked) {
//                            sixList.add(new AddExpertiseQuestionActivity.DataModelEntryExpert(listItems[position], false, position));
//                            sixAdapter.notifyDataSetChanged();
//                            checkedItems[position] = true;
//                            dialogInterface.dismiss();
//                            layout_save_continue.setVisibility(View.VISIBLE);
//                            global_counter = 0;
//                        } else {
//                            checkedItems[position] = true;
//                            dialogInterface.dismiss();
//                            CustomeToast.ShowCustomToast(getContext(), "Already Added!", Gravity.TOP);
//                        }
//                        if (global_counter_2 == 3 || sixAdapter.getItemCount() == 3) {
//                            selected_layout.setVisibility(GONE);
//                        }
//                    }
//                });
//
//                mBuilder.setCancelable(false);
//                mBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                AlertDialog mDialog = mBuilder.create();
//                mDialog.show();
            }
        });
        if (sixAdapter.getItemCount() == 3) {
            layout_save_continue.setVisibility(View.VISIBLE);
            selected_layout.setVisibility(GONE);
        }
        if (sixAdapter.getItemCount() > 0 && sixAdapter.getItemCount() < 3) {
            selected_layout.setVisibility(View.VISIBLE);
            layout_save_continue.setVisibility(View.VISIBLE);
        }
    }

    private void dialoge_close() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View mView = factory.inflate(R.layout.survay_dialog_close, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ImageView cross_button = (ImageView) mView.findViewById(R.id.cross_button);
        ImageView back_button = (ImageView) mView.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard((Activity) view.getContext());
                dialouge_6();

                dialog.dismiss();
            }
        });
        back_button.setVisibility(GONE);
        Button start_survey = (Button) mView.findViewById(R.id.close);
        start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) view.getContext());
                dialog.dismiss();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("strain_id", strainDataModel.getId());
                    jsonObject.put("q1_answer", commaSeprate(firstList));
                    jsonObject.put("q2_answer", commaSeprate(secList));
                    jsonObject.put("q3_answer", commaSeprate(threeList));
                    jsonObject.put("q4_answer", yes_for_list);
                    jsonObject.put("q5_answer", commaSeprate(fiveList));
                    jsonObject.put("q6_answer", commaSeprate(sixList));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Refressh_Layout.setVisibility(View.VISIBLE);
                Description_Layout.setVisibility(GONE);
                Start_survay_button.setVisibility(GONE);
                new VollyAPICall(getContext(), false, URL.save_survey_answer, jsonObject, user.getSession_key(), Request.Method.POST, StrainOverViewTabFragment.this, save_survey_answer);
                survay_question_one = "";
                survay_question_two = "";
                survay_question_three = "";
                survay_question_four = "";
                survay_question_five = "";
                survay_question_six = "";
                firstList.clear();
                secList.clear();
                threeList.clear();
                firstList.clear();
                sixList.clear();
                yes_for_list = "";
                yes_no = 0;
            }
        });
        cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallDummyDialogue();
                survay_question_one = "";
                survay_question_two = "";
                survay_question_three = "";
                survay_question_four = "";
                survay_question_five = "";
                survay_question_six = "";
                firstList.clear();
                secList.clear();
                threeList.clear();
                firstList.clear();
                sixList.clear();
                yes_for_list = "";
                yes_no = 0;
                HideKeyboard((Activity) view.getContext());
                dialog.dismiss();
            }
        });
    }

    private String commaSeprate(List<AddExpertiseQuestionActivity.DataModelEntryExpert> list) {
        StringBuilder comaSuprateString = new StringBuilder("");
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                comaSuprateString.append(list.get(i).getTitle()).append(",");
            } else if (i == (list.size() - 1)) {
                comaSuprateString.append(list.get(i).getTitle());
            } else {
                comaSuprateString.append(list.get(i).getTitle()).append(",");
            }
        }
        return comaSuprateString.toString();
    }

    private class OneViewClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            view.setFocusable(false);
            view.setScrollContainer(false);
            ShowToolTipDialog(view, 0);
        }

    }

    public void ShowToolTipDialog(final View v, final int position) {
        v.setFocusable(false);
        v.setScrollContainer(false);
        final SimpleTooltip tooltip = new SimpleTooltip.Builder(v.getContext())
                .anchorView(v)
                .focusable(false)
                .arrowDrawable(null)
                .showArrow(false)
                .text("Sample Text")
                .dismissOnOutsideTouch(true)
                .dismissOnInsideTouch(true)
                .modal(true)
                .gravity(Gravity.START)
                .animated(true)
                .contentView(R.layout.strain_survay_sugggestion_alert)
                .focusable(false)
                .overlayMatchParent(true)
                .build();

        tooltip.show();
    }

    public void CallDummyDialogue() {
        final ProgressDialog progressDialog = ProgressDialog.newInstance();
        progressDialog.show(getFragmentManager(), "Test");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 2000);
    }

}
