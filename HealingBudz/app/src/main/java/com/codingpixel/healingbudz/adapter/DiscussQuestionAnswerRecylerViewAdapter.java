package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.DataModel.QuestionAnswersDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.SelectableRoundedImageView;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.QAAddSubFragmentListner;
import com.codingpixel.healingbudz.interfaces.ReportButtonClickListner;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.static_function.IntentFunction;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static android.view.View.GONE;
import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.Utilities.DateConverter.convertDate;
import static com.codingpixel.healingbudz.Utilities.UserNameTextColorWRTRating.getUserRatingColor;
import static com.codingpixel.healingbudz.Utilities.UserNameTextColorWRTRating.getUserRatingImage;
import static com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.DiscussQuestionFragment.isNewScreen;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

public class DiscussQuestionAnswerRecylerViewAdapter extends RecyclerView.Adapter<DiscussQuestionAnswerRecylerViewAdapter.ViewHolder> implements APIResponseListner {
    private LayoutInflater mInflater;
    ArrayList<QuestionAnswersDataModel> mData = new ArrayList<>();
    private DiscussQuestionAnswerRecylerViewAdapter.ItemClickListener mClickListener;
    Context context;
    QAAddSubFragmentListner qaAddSubFragmentListner;
    ReportButtonClickListner reportButtonClickListner;
    HomeQAfragmentDataModel dataModel;
    FragmentManager fragmentManager;

    public DiscussQuestionAnswerRecylerViewAdapter(Context context, FragmentManager fragmentManager, ArrayList<QuestionAnswersDataModel> data, QAAddSubFragmentListner qaAddSubFragmentListner, ReportButtonClickListner reportButtonClickListner, HomeQAfragmentDataModel dataModel) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.qaAddSubFragmentListner = qaAddSubFragmentListner;
        this.reportButtonClickListner = reportButtonClickListner;
        mData.clear();
        this.mData = data;
        this.dataModel = dataModel;
    }

    //ic_qa_flag
    //ic_edit_dialog
    // inflates the cell layout from xml when needed
    @Override
    public DiscussQuestionAnswerRecylerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.discuss_question_recyler_item, parent, false);
        DiscussQuestionAnswerRecylerViewAdapter.ViewHolder viewHolder = new DiscussQuestionAnswerRecylerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final DiscussQuestionAnswerRecylerViewAdapter.ViewHolder holder, final int position) {
//        if (position != 0 && position % 10 == 0) {
////            holder.main_layout_whole.setVisibility(View.GONE);
//            holder.adView.setVisibility(View.VISIBLE);
//        } else {
////            holder.main_layout_whole.setVisibility(View.VISIBLE);
//            holder.adView.setVisibility(View.GONE);
//        }
        holder.adView.setVisibility(View.GONE);
        if (mData.get(position).getUser_id() == user.getUser_id()) {
            holder.plus_icon.setVisibility(GONE);
        } else {

            holder.plus_icon.setVisibility(View.VISIBLE);
            if (mData.get(position).isFollow()) {
                holder.plus_icon.setImageDrawable(holder.plus_icon.getContext().getResources().getDrawable(R.drawable.ic_cross_image));
            } else {
                holder.plus_icon.setImageDrawable(holder.plus_icon.getContext().getResources().getDrawable(R.drawable.ic_profile_add));
            }
        }
        holder.reply_layout.setVisibility(GONE);
        if (mData.get(position).getAttachments() != null) {
            if (mData.get(position).getAttachments().size() > 0) {
                holder.reply_layout.setVisibility(View.VISIBLE);
                final ArrayList<QuestionAnswersDataModel.Attachment> attachments = mData.get(position).getAttachments();
                for (int x = 0; x < attachments.size(); x++) {
                    final QuestionAnswersDataModel.Attachment attachment = attachments.get(x);
                    ImageView attachment_img = null;
                    if (x == 2) {
                        holder.attachment_one.setVisibility(View.VISIBLE);
                        holder.attachment_one.setTag(attachment.getUpload_path());
                        attachment_img = holder.attachment_one_img;
                    } else if (x == 1) {
                        holder.attachment_one.setVisibility(GONE);
                        holder.attachment_two.setVisibility(View.VISIBLE);
                        holder.attachment_two.setTag(attachment.getUpload_path());
                        attachment_img = holder.attachment_two_img;
                    } else if (x == 0) {
                        holder.attachment_two.setVisibility(GONE);
                        holder.attachment_one.setVisibility(GONE);
                        holder.attachment_three.setVisibility(View.VISIBLE);
                        holder.attachment_three.setTag(attachment.getUpload_path());
                        attachment_img = holder.attachment_three_img;
                    }
                    final ImageView finalAttachment_img = attachment_img;
                    String path = "";
                    if (attachment.getPoster() != null && attachment.getPoster().length() > 7) {
                        path = images_baseurl + attachment.getPoster();
                        // enable video icon
                        if (x == 2) {
                            holder.attachment_one_video.setVisibility(View.VISIBLE);
                            holder.attachment_one_video.setImageResource(R.drawable.ic_video_play_icon);
                        } else if (x == 1) {

                            holder.attachment_one_video.setVisibility(GONE);
                            holder.attachment_two_video.setVisibility(View.VISIBLE);
                            holder.attachment_two_video.setImageResource(R.drawable.ic_video_play_icon);
                        } else if (x == 0) {
                            holder.attachment_two_video.setVisibility(GONE);
                            holder.attachment_one_video.setVisibility(GONE);
                            holder.attachment_three_video.setImageResource(R.drawable.ic_video_play_icon);
                            holder.attachment_three_video.setVisibility(View.VISIBLE);
                        }
                    } else {
                        path = images_baseurl + attachment.getUpload_path();
                        if (x == 2) {
                            holder.attachment_one_video.setVisibility(View.VISIBLE);
                            holder.attachment_one_video.setImageResource(R.drawable.ic_gallery_icon);
                            holder.attachment_one_video.setVisibility(GONE);
                        } else if (x == 1) {
                            holder.attachment_one_video.setVisibility(GONE);
                            holder.attachment_two_video.setVisibility(View.VISIBLE);
                            holder.attachment_two_video.setImageResource(R.drawable.ic_gallery_icon);
                            holder.attachment_two_video.setVisibility(GONE);
                        } else if (x == 0) {
                            holder.attachment_two_video.setVisibility(GONE);
                            holder.attachment_one_video.setVisibility(GONE);
                            holder.attachment_three_video.setImageResource(R.drawable.ic_gallery_icon);
                            holder.attachment_three_video.setVisibility(View.VISIBLE);
                            holder.attachment_three_video.setVisibility(GONE);
                        }
                    }

                    holder.attachment_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String path = holder.attachment_one.getTag().toString();
                            boolean isVideo = false;
                            Intent intent = new Intent(context, MediPreview.class);
                            if (path.contains("mp4")) {
                                isVideo = true;
                                path = URL.videos_baseurl + path;
                            } else {
                                intent.putExtra("Attachment_array", (Serializable) attachments);
                                isVideo = false;
                                path = URL.images_baseurl + path;
                            }

                            intent.putExtra("display", holder.attachment_one.getTag().toString());
                            intent.putExtra("path", path);
                            intent.putExtra("isvideo", isVideo);
                            context.startActivity(intent);
                        }
                    });
                    holder.attachment_two.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String path = holder.attachment_two.getTag().toString();
                            boolean isVideo = false;
                            Intent intent = new Intent(context, MediPreview.class);
                            if (path.contains("mp4")) {
                                isVideo = true;
                                path = URL.videos_baseurl + path;
                            } else {
                                intent.putExtra("Attachment_array", (Serializable) attachments);
                                isVideo = false;
                                path = URL.images_baseurl + path;
                            }
                            intent.putExtra("display", holder.attachment_two.getTag().toString());
                            intent.putExtra("path", path);
                            intent.putExtra("isvideo", isVideo);
                            context.startActivity(intent);
                        }
                    });

                    holder.attachment_three.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String path = holder.attachment_three.getTag().toString();
                            boolean isVideo = false;
                            Intent intent = new Intent(context, MediPreview.class);
                            if (path.contains("mp4")) {
                                isVideo = true;
                                path = URL.videos_baseurl + path;
                            } else {
                                intent.putExtra("Attachment_array", (Serializable) attachments);
//                                intent.putExtra("Pos_Tap",x);
                                isVideo = false;
                                path = URL.images_baseurl + path;
                            }
                            intent.putExtra("display", holder.attachment_three.getTag().toString());
                            intent.putExtra("path", path);
                            intent.putExtra("isvideo", isVideo);
                            context.startActivity(intent);
                        }
                    });
                    Glide.with(context)
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
                                        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                                        finalAttachment_img.setImageDrawable(null);
                                        finalAttachment_img.setBackground(null);
                                        finalAttachment_img.setImageDrawable(drawable);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    return false;
                                }
                            })
//                            .listener(new RequestListener<String, GlideDrawable>() {
//                                @Override
//                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
////                                    Log.d("ready", e.getMessage());
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
////                                    Log.d("ready", model);
//                                    finalAttachment_img.setImageDrawable(null);
//                                    try {
//                                        Drawable d = resource;
//                                        Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
//                                        bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
//                                        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
//                                        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
//                                        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
//                                        finalAttachment_img.setImageDrawable(null);
//                                        finalAttachment_img.setBackground(null);
//                                        finalAttachment_img.setImageDrawable(drawable);
//                                    } catch (Exception ex) {
//                                        ex.printStackTrace();
//                                    }
//                                    return true;
//                                }
//                            })
                            .into(finalAttachment_img);
                }
            } else {
                holder.reply_layout.setVisibility(GONE);
            }
        } else {
            holder.reply_layout.setVisibility(GONE);
        }
//ic_qa_flag
        //ic_edit_dialog
        if (mData.get(position).getUser_id() != user.getUser_id()) {
            holder.Report_answer.setImageResource(R.drawable.ic_qa_flag);
            if (mData.get(position).getFlag_by_user_count() == 1) {
                holder.Report_answer.setImageResource(R.drawable.ic_flag_blue);
            } else {
                holder.Report_answer.setImageResource(R.drawable.ic_flag_gray);
            }
            holder.Report_answer.setPadding(Utility.convertDpToPixel(7F, context)
                    , Utility.convertDpToPixel(7F, context)
                    , Utility.convertDpToPixel(7F, context)
                    , Utility.convertDpToPixel(7F, context));
        } else {
            holder.Report_answer.setPadding((int) Utility.convertDpToPixel(10F, context)
                    , (int) Utility.convertDpToPixel(10F, context)
                    , (int) Utility.convertDpToPixel(10F, context)
                    , (int) Utility.convertDpToPixel(10F, context));
            holder.Report_answer.setImageResource(R.drawable.ic_edit_dialog);
        }
        holder.Report_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData.get(position).getUser_id() != user.getUser_id()) {
                    if (!mData.get(position).isReportButtonOpen()) {
                        if (mData.get(position).getFlag_by_user_count() == 0) {

//                        holder.Report_answer_dialog.setVisibility(View.VISIBLE);
//                        Animation startAnimation = AnimationUtils.loadAnimation(context, R.anim.attatch_fragment_right_to_left);
//                        holder.Report_answer_dialog.startAnimation(startAnimation);
                            reportButtonClickListner.onReportClick(position);
                        } else {
                            CustomeToast.ShowCustomToast(context, "Answer is already reported!", Gravity.TOP);
                        }

                        mData.get(position).setReportButtonOpen(true);
                    } else {
                        mData.get(position).setReportButtonOpen(false);
                        holder.Report_answer_dialog.setVisibility(GONE);
                        if (mData.get(position).getFlag_by_user_count() == 0) {

//                        holder.Report_answer_dialog.setVisibility(View.VISIBLE);
//                        Animation startAnimation = AnimationUtils.loadAnimation(context, R.anim.attatch_fragment_right_to_left);
//                        holder.Report_answer_dialog.startAnimation(startAnimation);
                            reportButtonClickListner.onReportClick(position);
                        } else {
                            CustomeToast.ShowCustomToast(context, "Answer is already reported!", Gravity.TOP);
                        }
                    }
                } else {
                    qaAddSubFragmentListner.EditReplyAnswerFragment(dataModel, mData.get(position));
//                    CustomeToast.ShowCustomToast(context, "You can't report your own answer!", Gravity.TOP);
                }
            }
        });


        holder.Report_answer_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportButtonClickListner.onReportClick(position);
                holder.Report_answer_dialog.setVisibility(GONE);
            }
        });


        if (mData.get(position).getAnswer_user_like_count() == 1) {
            holder.Like_answer_btn.setImageResource(R.drawable.ic_thumb_licked);
        } else {
            holder.Like_answer_btn.setImageResource(R.drawable.ic_thumb_unliked);
        }

//        if (mData.get(position).getFlag_by_user_count() == 1) {
//            holder.Report_answer.setImageResource(R.drawable.ic_flag_blue);
//        } else {
//            holder.Report_answer.setImageResource(R.drawable.ic_flag_gray);
//        }
//b3b2b2
        holder.Like_answer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                if (mData.get(position).getAnswer_user_like_count() == 0) {
                    holder.Like_Answer_Count.setText(mData.get(position).getAnswer_like_count() + 1 + "");
                    holder.Like_answer_btn.setImageResource(R.drawable.ic_thumb_licked);
                    mData.get(position).setAnswer_user_like_count(1);
                    mData.get(position).setAnswer_like_count(mData.get(position).getAnswer_like_count() + 1);
                    try {
                        object.put("is_like", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.Like_Answer_Count.setText(mData.get(position).getAnswer_like_count() - 1 + "");
                    holder.Like_answer_btn.setImageResource(R.drawable.ic_thumb_unliked);
                    mData.get(position).setAnswer_user_like_count(0);
                    mData.get(position).setAnswer_like_count(mData.get(position).getAnswer_like_count() - 1);
                    try {
                        object.put("is_like", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    object.put("answer_id", mData.get(position).getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new VollyAPICall(context, false, URL.add_answer_like, object, user.getSession_key(), Request.Method.POST, DiscussQuestionAnswerRecylerViewAdapter.this, APIActions.ApiActions.answer_like);
            }
        });

        holder.Reply_Answer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                qaAddSubFragmentListner.AddReplyAnswerFragment(dataModel);
            }
        });

        String path = "";
        if (mData.get(position).getUser_image_path().length() > 8) {
            if (mData.get(position).getUser_image_path().contains("facebook.com") || mData.get(position).getUser_image_path().contains("https") || mData.get(position).getUser_image_path().contains("http") || mData.get(position).getUser_image_path().contains("google.com") || mData.get(position).getUser_image_path().contains("googleusercontent.com")) {
                path = mData.get(position).getUser_image_path();
            } else {
                path = images_baseurl + mData.get(position).getUser_image_path();
            }
//            path = images_baseurl + mData.get(position).getUser_image_path();
        } else {
            path = images_baseurl + mData.get(position).getUser_avatar();
        }
        if (mData.get(position).getSpecial_icon().length() > 5) {

            holder.profile_img_topi.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(images_baseurl + mData.get(position).getSpecial_icon())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.topi_ic).error(R.drawable.noimage)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        Log.d("ready", e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("ready", model);
                            holder.profile_img_topi.setImageDrawable(resource);
                            return false;
                        }
                    })
                    .into(holder.profile_img_topi);
        } else {
            holder.profile_img_topi.setVisibility(View.GONE);
        }
        Glide.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_user_profile).error(R.drawable.noimage)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        Log.d("ready", e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d("ready", model);
                        holder.User_Image_Profile.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(holder.User_Image_Profile);

        MakeKeywordClickableText(holder.Answer_detail.getContext(), mData.get(position).getAnswer(), holder.Answer_detail);
        holder.Answer_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.Answer_detail.isClickable() && holder.Answer_detail.isEnabled()) {
                    if (mClickListener != null)
                        mClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });
        holder.Answer_User_Name.setText(mData.get(position).getUser_first_name());
        holder.answer_user_points.setText(mData.get(position).getUser_points() + "");
        holder.Answer_User_Name.setTextColor(getUserRatingColor(mData.get(position).getUser_points()));
        holder.answer_user_points.setTextColor(getUserRatingColor(mData.get(position).getUser_points()));
        holder.answer_user_points_image.setImageResource(getUserRatingImage(mData.get(position).getUser_points()));

        holder.Answer_Date.setText(convertDate(mData.get(position).getCreated_at()));
        if (mData.get(position).getCreated_at().equalsIgnoreCase(mData.get(position).getUpdated_at())) {
            holder.answer_date_edit.setVisibility(View.VISIBLE);
//            holder.edit_screen.setVisibility(GONE);
        } else {
//            holder.edit_screen.setVisibility(View.VISIBLE);
            holder.answer_date_edit.setVisibility(View.VISIBLE);
            if (mData.get(position).getUser_id() == user.getUser_id()) {
                holder.answer_date_edit.setVisibility(View.VISIBLE);
            } else {
                holder.answer_date_edit.setVisibility(View.VISIBLE);
            }
        }
        if (mData.get(position).isIs_edit_count()) {
            holder.edit_screen.setVisibility(View.VISIBLE);
        } else {
            holder.edit_screen.setVisibility(View.GONE);
        }
        holder.answer_date_edit.setVisibility(View.VISIBLE);
        holder.edit_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TOODO FOR EDIT SCREEN
                IntentFunction.GoToEditHistory(v.getContext(), mData.get(position).getId());
            }
        });
        holder.answer_date_edit.setText(convertDate(mData.get(position).getCreated_at()));
        holder.Like_Answer_Count.setText(mData.get(position).getAnswer_like_count() + "");

        holder.Profile_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNewScreen = true;
                GoToProfile(context, mData.get(position).getUser_id());
            }
        });

        holder.Profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNewScreen = true;
                GoToProfile(context, mData.get(position).getUser_id());
            }
        });

        holder.Main_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.Report_answer_dialog.setVisibility(GONE);
            }
        });
//        if (position == 0) {
//            holder.Main_Layout.setBackgroundResource(R.drawable.qa_question_answers_first_anser_bg);
//        } else {
        holder.Main_Layout.setBackgroundResource(R.drawable.qa_question_list_item_bg);
//        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) mClickListener.onItemClick(v, position);
            }
        });

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        TextView Answer_detail, answer_date_edit, Answer_User_Name, answer_user_points, Answer_Date, Like_Answer_Count;
        ImageView Report_answer, Like_answer_btn;
        LinearLayout Report_answer_dialog, edit_screen;
        Button Reply_Answer_btn;
        LinearLayout Profile_Name;
        RelativeLayout Profile_img;
        ImageView User_Image_Profile, profile_img_topi;
        LinearLayout Main_Layout, reply_layout;
        SelectableRoundedImageView attachment_one_img, attachment_two_img, attachment_three_img;
        ImageView answer_user_points_image, plus_icon;
        ImageView attachment_one_video, attachment_two_video, attachment_three_video;
        RelativeLayout attachment_one, attachment_two, attachment_three;
        public LinearLayout main_layout_whole;
        AdView adView;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            plus_icon = itemView.findViewById(R.id.plus_icon);
            reply_layout = itemView.findViewById(R.id.reply_layout);
            answer_date_edit = itemView.findViewById(R.id.answer_date_edit);
            edit_screen = itemView.findViewById(R.id.edit_screen);

            attachment_one_img = itemView.findViewById(R.id.attachment_one_image);
            attachment_one_img.setCornerRadiusDP(3f);
            attachment_two_img = itemView.findViewById(R.id.attachment_two_image);
            attachment_two_img.setCornerRadiusDP(3f);
            attachment_three_img = itemView.findViewById(R.id.attachment_three_image);
            attachment_three_img.setCornerRadiusDP(3f);
            main_layout_whole = itemView.findViewById(R.id.main_layout_whole);
            adView = itemView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(itemView.getContext().getString(R.string.ad_mob_id_did)).build();
            adView.loadAd(adRequest);
            attachment_one = itemView.findViewById(R.id.attachment_one);
            attachment_two = itemView.findViewById(R.id.attachment_two);
            attachment_three = itemView.findViewById(R.id.attachment_three);

            attachment_one_video = itemView.findViewById(R.id.attachment_one_video);
            attachment_two_video = itemView.findViewById(R.id.attachment_two_video);
            attachment_three_video = itemView.findViewById(R.id.attachment_three_video);
            answer_user_points = itemView.findViewById(R.id.answer_user_points);
            answer_user_points_image = itemView.findViewById(R.id.answer_user_points_image);


            Report_answer_dialog = itemView.findViewById(R.id.report_answer_dialog);
            Report_answer = itemView.findViewById(R.id.report_answer);
            Answer_detail = itemView.findViewById(R.id.answer_detail);
            Like_answer_btn = itemView.findViewById(R.id.like_answer_btn);
            Reply_Answer_btn = itemView.findViewById(R.id.reply_answer);
            User_Image_Profile = itemView.findViewById(R.id.user_profile_img);
            profile_img_topi = itemView.findViewById(R.id.profile_img_topi);
            Answer_User_Name = itemView.findViewById(R.id.answer_user_name);
            Answer_Date = itemView.findViewById(R.id.answer_date);
            Like_Answer_Count = itemView.findViewById(R.id.like_answer_count);
            Profile_img = itemView.findViewById(R.id.profile_image);
            Profile_Name = itemView.findViewById(R.id.profile_name);
            Main_Layout = itemView.findViewById(R.id.main_layout);
//            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    // allows clicks events to be caught
    public void setClickListener(DiscussQuestionAnswerRecylerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}