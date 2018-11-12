package com.codingpixel.healingbudz.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
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
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.SelectableRoundedImageView;
import com.codingpixel.healingbudz.custome_dialog.save_discussion.SaveDiscussionAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.DetectMenuBarHideListner;
import com.codingpixel.healingbudz.interfaces.QAAddSubFragmentListner;
import com.codingpixel.healingbudz.interfaces.ReportButtonClickListner;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static android.view.View.GONE;
import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.network.model.URL.add_question_like;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareHBContent;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

public class QAHomeFragmentRecylerAdapter extends RecyclerView.Adapter<QAHomeFragmentRecylerAdapter.ViewHolder> implements APIResponseListner, SaveDiscussionAlertDialog.OnDialogFragmentClickListener {
    private LayoutInflater mInflater;
    ArrayList<HomeQAfragmentDataModel> mData = new ArrayList<>();
    private QAHomeFragmentRecylerAdapter.ItemClickListener mClickListener;
    private DetectMenuBarHideListner detectMenuBarHideListner;
    private QAAddSubFragmentListner qaAddSubFragmentListner;
    private ReportButtonClickListner reportButtonClickListner;
    Activity context;

    public QAHomeFragmentRecylerAdapter(Activity context, DetectMenuBarHideListner detectMenuBarHideListner, ReportButtonClickListner reportButtonClickListner, QAAddSubFragmentListner addSubFragmentListner, ArrayList<HomeQAfragmentDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.detectMenuBarHideListner = detectMenuBarHideListner;
        this.qaAddSubFragmentListner = addSubFragmentListner;
        this.reportButtonClickListner = reportButtonClickListner;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public QAHomeFragmentRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.qa_recyler_view_item, parent, false);
        QAHomeFragmentRecylerAdapter.ViewHolder viewHolder = new QAHomeFragmentRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//        if (position != 0 && position % 10 == 0) {
////            holder.main_q_a_view.setVisibility(View.GONE);
//            holder.adView.setVisibility(View.VISIBLE);
//        } else {
////            holder.main_q_a_view.setVisibility(View.VISIBLE);
//            holder.adView.setVisibility(View.GONE);
//        }
        holder.adView.setVisibility(View.GONE);
        if (mData.get(position).getAnswerCount() == 0) {
            holder.first_bud_button.setVisibility(View.VISIBLE);
            holder.answer_content.setVisibility(View.GONE);
            if (mData.get(position).getUser_id() == user.getUser_id()) {
                holder.first_bud_button.setVisibility(View.GONE);
            }
        } else {
            holder.first_bud_button.setVisibility(View.GONE);
            holder.answer_content.setVisibility(View.VISIBLE);
            holder.Answer_count.setText(mData.get(position).getAnswerCount() + "");
            if (mData.get(position).getAnswerCount() > 1) {
                holder.text_ans_count.setText("ANSWERS");
            } else {
                holder.text_ans_count.setText("ANSWER");
            }
            holder.answer_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    qaAddSubFragmentListner.AddDiscussAnswerFragment(mData.get(position));
                    if (mData.get(position).getUser_id() == user.getUser_id() && mData.get(position).getShow_ads() == 1 && mData.get(position).getAnswerCount() >= 1) {
                        mData.get(position).setShow_ads(0);
                        notifyItemChanged(position);
                    }
                }
            });
        }
//        if (mData.get(position).getUser_id() == user.getUser_id()) {
//            holder.plus_icon.setVisibility(View.GONE);
//        }else{
//            holder.plus_icon.setVisibility(View.VISIBLE);
//        }

        if (mData.get(position).getGet_user_likes_count() == 1) {
            holder.Favorite_btn.setImageResource(R.drawable.ic_favorite_blue);
        } else {
            holder.Favorite_btn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }


        if (mData.get(position).getGet_user_flag_count() == 1) {
            holder.Report_it.setImageResource(R.drawable.ic_flag_blue);
        } else {
            holder.Report_it.setImageResource(R.drawable.ic_flag_gray);
        }
        holder.Share_Content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                try {
//                    object.put("msg", "Q: " + mData.get(position).getQuestion());
                    object.put("id", mData.get(position).getId());
                    object.put("type", "Question");
                    object.put("content",  mData.get(position).getQuestion());
                    object.put("url",  URL.sharedBaseUrl + "/get-question-answers/" +  mData.get(position).getId());
                    object.put("msg", "Check out Healing Budz for your smartphone: " + URL.sharedBaseUrl + "/get-question-answers/" + mData.get(position).getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareHBContent(context, object);
            }
        });
        if (mData.get(position).getIsAnswered() > 0) {
            holder.isAnswered.setImageResource(R.drawable.ic_answered);
        } else {
            holder.isAnswered.setImageResource(R.drawable.ic_non_answer);
        }
        holder.Favorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject object = new JSONObject();
                if (mData.get(position).getGet_user_likes_count() == 1) {
                    mData.get(position).setGet_user_likes_count(0);
                    holder.Favorite_btn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    try {
                        object.put("question_id", mData.get(position).getId());
                        object.put("is_like", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (!SharedPrefrences.getBool("IS_QA_My_SAVE_Dialog_Shown", context)) {
                        SaveDiscussionAlertDialog saveDiscussionAlertDialog = SaveDiscussionAlertDialog.newInstance("SAVED DISCUSSION", "Q’s & A’s are saved in the app menu under My Saves", "Got it! Do not show again for Q’s & A’s I save", QAHomeFragmentRecylerAdapter.this);
                        saveDiscussionAlertDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                    }
                    mData.get(position).setGet_user_likes_count(1);
                    holder.Favorite_btn.setImageResource(R.drawable.ic_favorite_blue);
                    try {
                        object.put("question_id", mData.get(position).getId());
                        object.put("is_like", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                new VollyAPICall(context, false, add_question_like, object, user.getSession_key(), Request.Method.POST, QAHomeFragmentRecylerAdapter.this, APIActions.ApiActions.add_answer);
            }
        });


        MakeKeywordClickableText(context, mData.get(position).getQuestion(), holder.Question);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.Question.setText(Html.fromHtml(mData.get(position).getQuestion(), Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            holder.Question.setText(Html.fromHtml(mData.get(position).getQuestion()));
//        }

        holder.Title_description.setText(mData.get(position).getuser_name_dscription());
        holder.Main_Title.setText(mData.get(position).getuser_name());
//        holder.Main_Title.setTextColor(Color.parseColor(Utility.getBudColor(mData.get(position).getUser_points())));


        holder.first_bud_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               detectMenuBarHideListner.HideMenuBar();
                if (mData.get(position).getUser_id() != user.getUser_id()) {
                    qaAddSubFragmentListner.AddFirstAnswerBudFragment(mData.get(position));
                }
            }
        });

        holder.Discuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                qaAddSubFragmentListner.AddDiscussAnswerFragment(mData.get(position));
                if (mData.get(position).getUser_id() == user.getUser_id() && mData.get(position).getShow_ads() == 1 && mData.get(position).getAnswerCount() >= 1) {
                    mData.get(position).setShow_ads(0);
                    notifyItemChanged(position);
                }
            }
        });

        holder.Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.Question.isClickable() && holder.Question.isEnabled()) {
                    qaAddSubFragmentListner.AddDiscussAnswerFragment(mData.get(position));
                    if (mData.get(position).getUser_id() == user.getUser_id() && mData.get(position).getShow_ads() == 1 && mData.get(position).getAnswerCount() >= 1) {
                        mData.get(position).setShow_ads(0);
                        notifyItemChanged(position);
                    }
                }
            }
        });

        holder.Report_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();

                if (mData.get(position).getGet_user_flag_count() == 1) {
                    CustomeToast.ShowCustomToast(context, "Question is already reported!", Gravity.TOP);
//                    mData.get(position).setGet_user_flag_count(0);
//                    holder.Report_it.setImageResource(R.drawable.ic_qa_flag);
//                    try {
//                        object.put("question_id", mData.get(position).getId());
//                        object.put("is_flag", 0);
//                        object.put("reason", "");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    new VollyAPICall(context, false, add_question_flag, object, user.getSession_key(), Request.Method.POST, QAHomeFragmentRecylerAdapter.this, APIActions.ApiActions.add_answer);
                } else {
                    if (mData.get(position).getUser_id() != user.getUser_id()) {
                        reportButtonClickListner.onReportClick(position);
                    } else {
                        CustomeToast.ShowCustomToast(context, "You can't report your own question!", Gravity.TOP);
                    }
                }

            }
        });
        String pathImage = "";
        if (mData.get(position).getUser_photo().contains("facebook.com") || mData.get(position).getUser_photo().contains("google.com")
                || mData.get(position).getUser_photo().contains("googleusercontent.com")|| mData.get(position).getUser_photo().contains("https")|| mData.get(position).getUser_photo().contains("http")) {
            pathImage = mData.get(position).getUser_photo();
        } else {
            pathImage = images_baseurl + mData.get(position).getUser_photo();
        }
//        if(mData.get(position).)
        if (mData.get(position).getSpecial_icon().length() > 5) {
            holder.profile_img_topi.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(images_baseurl + mData.get(position).getSpecial_icon())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.topi_ic)
                    .error(R.drawable.noimage).error(R.drawable.noimage)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
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
        if (mData.get(position).getAnswerCount() > 19) {
            holder.hottest.setVisibility(View.VISIBLE);
        } else {
            holder.hottest.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qaAddSubFragmentListner.AddDiscussAnswerFragment(mData.get(position));
            }
        });
        Glide.with(context)
                .load(pathImage)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_user_profile)
                .error(R.drawable.noimage).error(R.drawable.noimage)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d("ready", model);
                        holder.User_Img.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(holder.User_Img);
        //Images Display Attachment
        holder.attachment_view.setVisibility(GONE);
        if (mData.get(position).getAttachments() != null){
            if (mData.get(position).getAttachments().size() > 0) {
                holder.attachment_view.setVisibility(View.VISIBLE);
                final ArrayList<QuestionAnswersDataModel.Attachment> attachments = mData.get(position).getAttachments();
                for (int x = 0; x < attachments.size(); x++) {
                    final QuestionAnswersDataModel.Attachment attachment = attachments.get(x);
                    ImageView attachment_img = null;
                    if (x == 2) {
                        holder.attachment_one.setVisibility(View.VISIBLE);
                        holder.attachment_one.setTag(attachment.getUpload_path());
                        attachment_img = holder.attachment_one_image;
                    } else if (x == 1) {
                        holder.attachment_one.setVisibility(GONE);
                        holder.attachment_two.setVisibility(View.VISIBLE);
                        holder.attachment_two.setTag(attachment.getUpload_path());
                        attachment_img = holder.attachment_two_image;
                    } else if (x == 0) {
                        holder.attachment_two.setVisibility(GONE);
                        holder.attachment_one.setVisibility(GONE);
                        holder.attachment_three.setVisibility(View.VISIBLE);
                        holder.attachment_three.setTag(attachment.getUpload_path());
                        attachment_img = holder.attachment_three_image;
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
                holder.attachment_view.setVisibility(GONE);
            }
        } else {
            holder.attachment_view.setVisibility(GONE);
        }
    }
//    }

    // total number of cells
    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response,", response);
        notifyDataSetChanged();
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response,", response);
        notifyDataSetChanged();
    }

    @Override
    public void onOkClicked(SaveDiscussionAlertDialog dialog) {
        SharedPrefrences.setBool("IS_QA_My_SAVE_Dialog_Shown", true, context);
        JSONObject object = new JSONObject();
        try {
            object.put("msg", "Hey Bud! Check out the Healing Budz app for your smart phone.” Download it today from http://139.162.37.73/healingbudz/");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        ShareHBContent(getActivity(), object);
        dialog.dismiss();
    }

    @Override
    public void onCancelClicked(SaveDiscussionAlertDialog dialog) {
        JSONObject object = new JSONObject();
        try {
            object.put("msg", "Hey Bud! Check out the Healing Budz app for your smart phone.” Download it today from http://139.162.37.73/healingbudz/");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        ShareHBContent(getActivity(), object);
        dialog.dismiss();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        Button first_bud_button;
        LinearLayout answer_content;
        ImageView Favorite_btn, Report_it;
        Button Discuss;
        AdView adView;
        ImageView User_Img, isAnswered, profile_img_topi, Share_Content, plus_icon, hottest;
        LinearLayout Profile_View, main_q_a_view;
        TextView Main_Title, Title_description, Answer_count, Question, text_ans_count;
        LinearLayout attachment_view;
        RelativeLayout attachment_three,attachment_two,attachment_one;
        SelectableRoundedImageView attachment_three_image,attachment_two_image,attachment_one_image;
        ImageView attachment_three_video,attachment_two_video,attachment_one_video;

        public ViewHolder(View itemView) {
            super(itemView);
            attachment_three = itemView.findViewById(R.id.attachment_three);
            attachment_two = itemView.findViewById(R.id.attachment_two);
            attachment_one = itemView.findViewById(R.id.attachment_one);
            attachment_three_image = itemView.findViewById(R.id.attachment_three_image);
            attachment_two_image = itemView.findViewById(R.id.attachment_two_image);
            attachment_one_image = itemView.findViewById(R.id.attachment_one_image);
            attachment_three_video = itemView.findViewById(R.id.attachment_three_video);
            attachment_two_video = itemView.findViewById(R.id.attachment_two_video);
            attachment_one_video = itemView.findViewById(R.id.attachment_one_video);
            attachment_one_image.setCornerRadiusDP(3f);
            attachment_two_image.setCornerRadiusDP(3f);
            attachment_three_image.setCornerRadiusDP(3f);

            text_ans_count = itemView.findViewById(R.id.text_ans_count);
            attachment_view = itemView.findViewById(R.id.attachment_view);
            hottest = itemView.findViewById(R.id.hottest);
            isAnswered = itemView.findViewById(R.id.is_answered);
            adView = itemView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(itemView.getContext().getString(R.string.ad_mob_id_did)).build();
            adView.loadAd(adRequest);


            main_q_a_view = itemView.findViewById(R.id.main_q_a_view);
            plus_icon = itemView.findViewById(R.id.plus_icon);
            first_bud_button = itemView.findViewById(R.id.first_bid_button);
            answer_content = itemView.findViewById(R.id.bid_first_content);
            Favorite_btn = itemView.findViewById(R.id.favorite_btn);
            Main_Title = itemView.findViewById(R.id.main_title);
            Title_description = itemView.findViewById(R.id.main_title_description);
            Answer_count = itemView.findViewById(R.id.ans_count);
            Question = itemView.findViewById(R.id.question);
            Discuss = itemView.findViewById(R.id.qa_fragment_list_discuss_button);
            Report_it = itemView.findViewById(R.id.report_it);
            Profile_View = itemView.findViewById(R.id.profile_view);
            User_Img = itemView.findViewById(R.id.user_img);
            profile_img_topi = itemView.findViewById(R.id.profile_img_topi);
            Share_Content = itemView.findViewById(R.id.share_qa_content);
//            Profile_View.setOnClickListener(this);
            Main_Title.setOnClickListener(this);
            User_Img.setOnClickListener(this);

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

    public void NotifyDataChange(ArrayList<HomeQAfragmentDataModel> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    // allows clicks events to be caught
    public void setClickListener(QAHomeFragmentRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<HomeQAfragmentDataModel> list) {
        mData.addAll(list);
        notifyDataSetChanged();
    }
}