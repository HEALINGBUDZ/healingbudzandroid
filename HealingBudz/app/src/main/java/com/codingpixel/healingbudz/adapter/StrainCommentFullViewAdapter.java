package com.codingpixel.healingbudz.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.ReportQuestionListDataModel;
import com.codingpixel.healingbudz.DataModel.StrainOverViewDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.dialog.StrainEditReviewAlertDialog;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_file_with_progress.UploadFileWithProgress;
import com.codingpixel.healingbudz.static_function.ShareIntent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.Utilities.StrainRatingImage.Strain_Rating;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainCommentFullView.strain_report_full_screen;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity.strainDataModel;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.tabs.StrainOverViewTabFragment.goneToReview;
import static com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.DiscussQuestionFragment.isNewScreen;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.delete_strain_review;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.flag_strain_review;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.testAPI;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

/**
 * Created by macmini on 20/12/2017.
 */

public class StrainCommentFullViewAdapter extends RecyclerView.Adapter<StrainCommentFullViewAdapter.ViewHolder> implements APIResponseListner, ReportSendButtonLstner, StrainEditReviewAlertDialog.OnDialogFragmentClickListener {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<StrainOverViewDataModel.Reviews> mData = new ArrayList<>();
    private StrainCommentFullViewAdapter.ItemClickListener mClickListener;

    public StrainCommentFullViewAdapter(Context context, ArrayList<StrainOverViewDataModel.Reviews> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = mData;
    }

    @Override
    public StrainCommentFullViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.strain_comment_itme_view, parent, false);
        view.setOnTouchListener(null);
        StrainCommentFullViewAdapter.ViewHolder viewHolder = new StrainCommentFullViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StrainCommentFullViewAdapter.ViewHolder holder, final int position) {
//        if (position != 0 && position % 10 == 0) {
////            holder.first_review_layout.setVisibility(View.GONE);
//            holder.adView.setVisibility(View.VISIBLE);
//        } else {
////            holder.first_review_layout.setVisibility(View.VISIBLE);
//            holder.adView.setVisibility(View.GONE);
//        }

        holder.adView.setVisibility(View.GONE);
        final StrainOverViewDataModel.Reviews first_review = mData.get(position);
        MakeKeywordClickableText(holder.comment_one_comment_text.getContext(), first_review.getReview(), holder.comment_one_comment_text);
        holder.first_count.setText(first_review.getTotal_likes() + "");
        holder.comment_one_user_name.setText(first_review.getUser_first_name());
//        holder.comment_one_user_name.setTextColor(Color.parseColor(Utility.getBudColor(first_review.getUser_point())));
        if (first_review.getUser_image_path().length() > 8) {
            SetImageDrawable(holder.comment_one_user_img, first_review.getUser_image_path());
        } else {
            SetImageDrawable(holder.comment_one_user_img, first_review.getUser_avatar());
//            comment_one_user_img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_user_profile));
        }
//        SetImageDrawable(holder.comment_one_user_img, first_review.getUser_image_path());
//        holder.comment_one_comment_text.setText(first_review.getReview());
        holder.comment_one_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNewScreen = true;
                GoToProfile(context, first_review.getUser_id());
            }
        });

        holder.comment_one_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNewScreen = true;
                GoToProfile(context, mData.get(position).getUser_id());
            }
        });
        holder.comment_one_date.setText(DateConverter.getCustomDateString(first_review.getUpdated_at()));
        holder.comment_one_rating_count.setText(first_review.getRating() + "");
        holder.comment_one_rating_img.setImageResource(Strain_Rating(first_review.getRating()));
        if (first_review.isLiked()) {
            holder.comment_one_like.setColorFilter(Color.parseColor("#f4c42f"), PorterDuff.Mode.SRC_IN);
        } else {
            holder.comment_one_like.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        }
        holder.comment_one_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String val = "0";
                if (first_review.isLiked()) {
                    val = "0";
                    mData.get(position).setLiked(false);
                    holder.comment_one_like.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                } else {
                    val = "1";
                    mData.get(position).setLiked(true);
                    holder.comment_one_like.setColorFilter(Color.parseColor("#f4c42f"), PorterDuff.Mode.SRC_IN);
                }
                try {
                    new VollyAPICall(v.getContext()
                            , true
                            , URL.add_strain_review_like
                            , new JSONObject().put("review_id", mData.get(position).getId()).put("strain_id", mData.get(position).getStrain_id()).put("like_val", val)
                            , Splash.user.getSession_key()
                            , Request.Method.POST
                            , new APIResponseListner() {
                        @Override
                        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                            if (mClickListener != null) mClickListener.onItemClick(v, position);
                            notifyItemChanged(position);
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
        if (first_review.getAttatchment_type() != null) {
            if (first_review.getAttatchment_type().equalsIgnoreCase("image")) {
                holder.comment_one_media_type_icon.setVisibility(View.GONE);
                holder.Comment_one_img.setVisibility(View.VISIBLE);
                holder.comment_one_media_type_icon.setImageResource(R.drawable.ic_gallery_icon);
                holder.First_Attach.setVisibility(View.VISIBLE);
                SetImageBackground(holder.Comment_one_img, first_review.getAttatchment_path());
                holder.Comment_one_img.setOnClickListener(new View.OnClickListener() {
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
                holder.comment_one_media_type_icon.setVisibility(View.VISIBLE);
                holder.Comment_one_img.setVisibility(View.VISIBLE);
                holder.comment_one_media_type_icon.setImageResource(R.drawable.ic_video_play_icon);
                holder.First_Attach.setVisibility(View.VISIBLE);
                SetImageBackground(holder.Comment_one_img, first_review.getAttatchment_poster());
                holder.Comment_one_img.setOnClickListener(new View.OnClickListener() {
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
                holder.comment_one_media_type_icon.setVisibility(View.GONE);
                holder.Comment_one_img.setVisibility(View.GONE);
                holder.First_Attach.setVisibility(View.GONE);
            }
        } else {
            holder.comment_one_media_type_icon.setVisibility(View.GONE);
            holder.Comment_one_img.setVisibility(View.GONE);
            holder.First_Attach.setVisibility(View.GONE);
        }

//        holder.review_count.setText(mData.size() + " Reviews");

        if (first_review.getIs_user_flaged_count() == 1) {
            holder.comment_one_report_icon.setImageResource(R.drawable.ic_flag_strain);
            holder.comment_one_report_text.setTextColor(Color.parseColor("#f4c42f"));

        } else {
            holder.comment_one_report_icon.setImageResource(R.drawable.ic_flag_white);
            holder.comment_one_report_text.setTextColor(Color.parseColor("#caccca"));
        }
        holder.Report_it_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (user.getUser_id() != mData.get(position).getUser_id()) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("strain_id", mData.get(position).getStrain_id());
                        jsonObject.put("strain_review_id", mData.get(position).getId());
                        if (mData.get(position).getIs_user_flaged_count() == 1) {
                            CustomeToast.ShowCustomToast(context, "you already reported this review!", Gravity.TOP);
//                        holder.comment_one_report_icon.setImageResource(R.drawable.ic_flag_white);
//                        holder.comment_one_report_text.setTextColor(Color.parseColor("#787878"));
//                        mData.get(position).setIs_user_flaged_count(0);
//                        jsonObject.put("is_flaged", 0);
//                        jsonObject.put("reason", "");
//                        new VollyAPICall(context, false, URL.flag_strain_review, jsonObject, user.getSession_key(), Request.Method.POST, StrainCommentFullViewAdapter.this, flag_strain_review);
                        } else {

                            ArrayList<ReportQuestionListDataModel> dataModels = new ArrayList<>();
                            dataModels.add(new ReportQuestionListDataModel("Nudity or sexual content", false));
                            dataModels.add(new ReportQuestionListDataModel("Harassment or hate speech", false));
                            dataModels.add(new ReportQuestionListDataModel("Threatening, violent, or concerning", false));
                            dataModels.add(new ReportQuestionListDataModel("Spam", false));
                            dataModels.add(new ReportQuestionListDataModel("Offensive", false));
                            dataModels.add(new ReportQuestionListDataModel("Unrelated", false));
                            if (strain_report_full_screen.isSlide()) {
                                strain_report_full_screen.SlideUp();
                            } else {
                                strain_report_full_screen.SlideDown(position, dataModels, StrainCommentFullViewAdapter.this, "strain");
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
        if (first_review.getUser_id() == user.getUser_id()) {
            holder.report_two.setVisibility(View.GONE);
            holder.edit_two.setVisibility(View.VISIBLE);
        } else {
            holder.report_two.setVisibility(View.VISIBLE);
            holder.edit_two.setVisibility(View.GONE);
        }
        holder.comment_one_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                try {
                    object.put("id", mData.get(position).getId());
                    object.put("type", "Strain");
                    object.put("content", strainDataModel.getTitle());
                    object.put("url", URL.sharedBaseUrl + "/strain-details/" + strainDataModel.getId());
                    object.put("msg", "Check out Healing Budz for your smartphone: " + URL.sharedBaseUrl + "/strain-details/" + mData.get(position).getStrain_id());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareIntent.ShareHBContent((Activity) view.getContext(), object);
            }
        });
        holder.ic_edit_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrainEditReviewAlertDialog
                        .newInstance(StrainCommentFullViewAdapter.this, false, first_review)
                        .show(((AppCompatActivity) holder.ic_edit_two.getContext()).getSupportFragmentManager(), "pd");
            }
        });

        holder.ic_delete_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(holder.ic_edit_two.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this review?")
                        .setConfirmText("Yes, delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                mData.remove(position);
                                try {
                                    new VollyAPICall(holder.ic_delete_two.getContext()
                                            , true
                                            , URL.delete_strain_review
                                            , new JSONObject().put("strain_review_id", first_review.getId())
                                            , user.getSession_key()
                                            , Request.Method.POST
                                            , StrainCommentFullViewAdapter.this, delete_strain_review);
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
        if (first_review.getSpecial_icon().length() > 8) {
            holder.profile_img_topi.setVisibility(View.VISIBLE);
            SetImageDrawable(holder.profile_img_topi, first_review.getSpecial_icon());
        } else {
            holder.profile_img_topi.setVisibility(View.GONE);
//            comment_one_user_img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_user_profile));
        }
//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mClickListener != null) mClickListener.onItemClick(v, position);
//            }
//        });
    }
//}

    public void SetImageDrawable(final ImageView imageView, String Path) {
        if (Path.contains("facebook.com") || Path.contains("https") || Path.contains("http") || Path.contains("google.com") || Path.contains("googleusercontent.com")) {
            Path = Path;
        } else {
            Path = images_baseurl + Path;
        }
        Glide.with(context)
                .load(Path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.image_plaecholder_bg).error(R.drawable.noimage)
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        Log.d("ready", model);
////                        imageView.setImageDrawable(resource);
////                        imageView.setBackground(null);
//                        return false;
//                    }
//                })
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setClickListener(ItemClickListener strainCommentFullView) {
        this.mClickListener = strainCommentFullView;
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("onRequestSuccess: ", response);
        goneToReview = true;
        if (apiActions == delete_strain_review) {
            notifyDataSetChanged();
        } else if (apiActions == APIActions.ApiActions.add_strain_review) {
            notifyDataSetChanged();
            mClickListener.onItemClick(null, 0);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("onRequestError: ", response);
        notifyDataSetChanged();
    }

    @Override
    public void OnSnedClicked(JSONObject data, int position) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("strain_id", mData.get(position).getStrain_id());
            jsonObject.put("strain_review_id", mData.get(position).getId());
            mData.get(position).setIs_user_flaged_count(1);
            jsonObject.put("is_flaged", 1);
            jsonObject.put("reason", data.getString("reason"));
            notifyItemChanged(position);
            new VollyAPICall(context, false, URL.flag_strain_review, jsonObject, user.getSession_key(), Request.Method.POST, StrainCommentFullViewAdapter.this, flag_strain_review);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSubmitReview(StrainEditReviewAlertDialog dialog, JSONObject jsonObject) {
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
                new UploadFileWithProgress(context, true, URL.add_strain_review, Attatchment_file_path, Attatchment_file_type, values, parameter, null, StrainCommentFullViewAdapter.this, APIActions.ApiActions.add_strain_review).execute();
            } else {
                new VollyAPICall(context, true, URL.add_strain_review, jsonObject, user.getSession_key(), Request.Method.POST, StrainCommentFullViewAdapter.this, APIActions.ApiActions.add_strain_review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCross(StrainEditReviewAlertDialog dialog) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements APIResponseListner {
        public EditText commment_text;
        public RelativeLayout Second_Attach, First_Attach;
        public ImageView comment_one_like, profile_img_topi, ic_delete_two, ic_edit_two, comment_one_user_img, comment_one_rating_img, comment_one_media_type_icon, Comment_one_img, comment_two_rating_img,
                comment_two_media_type_icon, Comment_two_img, comment_two_user_img, comment_one_report_icon, comment_two_report_icon;
        public TextView comment_one_user_name, comment_one_comment_text, comment_one_date, comment_one_rating_count, comment_two_user_name,
                comment_two_comment_text, comment_two_date, comment_two_rating_count, review_count, comment_one_report_text, comment_two_report_text;

        LinearLayout comment_one_share, comment_two_share, first_review_layout, second_review_layout;
        LinearLayout Report_it_one, Report_it_two;

        AdView adView;
        TextView first_count;
        View view;
        LinearLayout report_two;
        RelativeLayout edit_two;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            edit_two = view.findViewById(R.id.edit_two);
            profile_img_topi = view.findViewById(R.id.profile_img_topi);
            comment_one_like = view.findViewById(R.id.comment_one_like);
            first_count = view.findViewById(R.id.first_count);

            report_two = view.findViewById(R.id.report_two);

            ic_edit_two = view.findViewById(R.id.ic_edit_two);

            ic_delete_two = view.findViewById(R.id.ic_delete_two);
            First_Attach = view.findViewById(R.id.first_attttach);
            Report_it_one = view.findViewById(R.id.repote_it_item_one);
            comment_one_user_img = view.findViewById(R.id.comment_one_user_img);
            comment_one_user_name = view.findViewById(R.id.comment_one_user_name);
            comment_one_comment_text = view.findViewById(R.id.comment_one_comment_text);
            comment_one_date = view.findViewById(R.id.comment_one_date);

            comment_one_rating_count = view.findViewById(R.id.comment_one_rating_count);
            comment_one_rating_img = view.findViewById(R.id.comment_one_rating_img);
            comment_one_media_type_icon = view.findViewById(R.id.comment_one_video_icon);
            comment_one_share = view.findViewById(R.id.comment_one_share);
            Comment_one_img = view.findViewById(R.id.comment_attachment_one);
            comment_one_report_icon = view.findViewById(R.id.comment_one_report_icon);
            comment_one_report_text = view.findViewById(R.id.comment_one_report_text);
            comment_one_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("id", mData.get(getAdapterPosition()).getId());
                        object.put("type", "Strain");
                        object.put("content", strainDataModel.getTitle());
                        object.put("url", URL.sharedBaseUrl + "/strain-details/" + strainDataModel.getId());
                        object.put("msg", "Check out Healing Budz for your smartphone: " + URL.sharedBaseUrl + "/strain-details/" + mData.get(getAdapterPosition()).getStrain_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ShareIntent.ShareHBContent((Activity) view.getContext(), object);
                }
            });

            first_review_layout = view.findViewById(R.id.first_review_layout);
            adView = itemView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(view.getContext().getString(R.string.ad_mob_id_did)).build();
            adView.loadAd(adRequest);


        }

        @Override
        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
            Log.d("onRequestSuccess: ", response);
            notifyDataSetChanged();
        }

        @Override
        public void onRequestError(String response, APIActions.ApiActions apiActions) {
            Log.d("onRequestError: ", response);
            notifyDataSetChanged();
        }
    }

    public void SetImageBackground(final ImageView imageView, String Path) {
        if (context != null) {
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
//                                BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
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

    public interface ItemClickListener {
        void onItemClick(View view, int position);

//        void onDeleteClick(View view, int position);
    }
}
