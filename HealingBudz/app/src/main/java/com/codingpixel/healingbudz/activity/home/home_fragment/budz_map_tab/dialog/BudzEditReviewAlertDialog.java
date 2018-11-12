package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.BudzMapReviews;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialogVideoProcessing;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotationVideo;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingChatViewActivity.getVideoThumbnail;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class BudzEditReviewAlertDialog extends BaseDialogFragment<BudzEditReviewAlertDialog.OnDialogFragmentClickListener> {

    static OnDialogFragmentClickListener Listener;
    BudzMapReviews review;
    private SimpleRatingBar rating_bar;
    //    private RatingBar rating_bar;
    private String Attatchment_file_path = "";
    private String Attatchment_file_type = "";
    private LinearLayout Comment_image_layout;
    private ProgressDialogVideoProcessing video_processing;
    Button submit_commnet, upload_comment_image;
    private TextView attachment_name;
    private View Attach_image_line;
    ImageView cross_btn, attatchment_cross, attachement_img;
    TextView comment_one_comment_text, question_detail_character_counter;
    int delete_attachment = 0;


    public interface OnDialogFragmentClickListener {
        public void onSubmitReview(BudzEditReviewAlertDialog dialog, JSONObject jsonObject);

        public void onCross(BudzEditReviewAlertDialog dialog);
    }

    public static BudzEditReviewAlertDialog newInstance(OnDialogFragmentClickListener listner, boolean isSubscribe, BudzMapReviews review) {
        BudzEditReviewAlertDialog frag = new BudzEditReviewAlertDialog();
        Bundle args = new Bundle();
        args.putBoolean("isSubscribe", isSubscribe);
        args.putParcelable("review", review);
        frag.setArguments(args);
        Listener = listner;
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.budz_edit_alert, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        review = getArguments().getParcelable("review");


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
//        Bitmap map=takeScreenShot(getActivity());
//        Bitmap fast=fastblur(map, 10);
//        final Drawable draw=new BitmapDrawable(getResources(),fast);
//        dialog.getWindow().setBackgroundDrawable(draw);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
//        int top_y = getResources().getDisplayMetrics().heightPixels / 5;
//        wmlp.y = top_y;   //y position
        Button Learn_more = main_dialog.findViewById(R.id.learn_more_btn);
        final ImageView premium_tag = main_dialog.findViewById(R.id.premium_tag);
        attachment_name = main_dialog.findViewById(R.id.attachment_name);
        question_detail_character_counter = main_dialog.findViewById(R.id.question_detail_character_counter);
        comment_one_comment_text = main_dialog.findViewById(R.id.commment_text);
        Comment_image_layout = main_dialog.findViewById(R.id.comment_image_layout);
        cross_btn = main_dialog.findViewById(R.id.cross_btn);
        attatchment_cross = main_dialog.findViewById(R.id.attatchment_cross);
        attachement_img = main_dialog.findViewById(R.id.attachement_img);
        submit_commnet = main_dialog.findViewById(R.id.submit_commnet);
        upload_comment_image = main_dialog.findViewById(R.id.upload_comment_image);
        submit_commnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                if (comment_one_comment_text.getText().toString().trim().length() > 0 && rating_bar.getRating() >= 1) {
                    try {

                        if (Attatchment_file_path.length() > 4) {
                            JSONArray parameter = new JSONArray();
                            parameter.put("sub_user_id");
                            parameter.put("rating");
                            parameter.put("review");
                            parameter.put("business_review_id");
                            JSONArray values = new JSONArray();
                            values.put(budz_map_item_clickerd_dataModel.getId());
                            values.put(rating_bar.getRating() < 1 ? 1 : rating_bar.getRating());
                            values.put(comment_one_comment_text.getText().toString());
                            values.put(review.getId());
                            jsonObject.put("param", parameter);
                            jsonObject.put("value", values);
                            jsonObject.put("isVideo", true);
                            jsonObject.put("type", Attatchment_file_type);
                            jsonObject.put("path", Attatchment_file_path);
//                            Refressh_Layout.setVisibility(View.VISIBLE);

                        } else {
                            jsonObject.put("sub_user_id", budz_map_item_clickerd_dataModel.getId());
                            jsonObject.put("rating", rating_bar.getRating() < 1 ? 1 : rating_bar.getRating());
                            if (delete_attachment == 1) {
                                jsonObject.put("delete_attachment", delete_attachment);
                            }
                            jsonObject.put("business_review_id", review.getId());
                            jsonObject.put("review", comment_one_comment_text.getText().toString());
                            jsonObject.put("isVideo", false);
                        }
                        Listener.onSubmitReview(BudzEditReviewAlertDialog.this, jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (comment_one_comment_text.getText().toString().trim().length() == 0) {
                        CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Comment Field Empty!", Gravity.TOP);
                    }
                    if (rating_bar.getRating() < 1) {
                        CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Rating is mandatory!", Gravity.TOP);
                    }

                }
            }
        });
        Attach_image_line = main_dialog.findViewById(R.id.attatch_img_line);
        rating_bar = main_dialog.findViewById(R.id.go_rating);
        rating_bar.setRating((float) (review.getRating() < 1 ? 1 : review.getRating()));
        comment_one_comment_text.addTextChangedListener(new TextWatcher() {
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
        MakeKeywordClickableText(comment_one_comment_text.getContext(), review.getReview(), comment_one_comment_text);
        cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        attatchment_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Attatchment_file_path = "";
                Attatchment_file_type = "";
                delete_attachment = 1;
                Comment_image_layout.setVisibility(View.GONE);
                Attach_image_line.setVisibility(View.GONE);
            }
        });
        if (review.getAttatchment_type() != null) {
            if (review.getAttatchment_type().equalsIgnoreCase("image")) {
                Comment_image_layout.setVisibility(View.VISIBLE);
                Attach_image_line.setVisibility(View.VISIBLE);
                SetImageBackground(attachement_img, review.getAttatchment_path());
                attachment_name.setText("image");
            } else if (review.getAttatchment_type().equalsIgnoreCase("video")) {
                Comment_image_layout.setVisibility(View.VISIBLE);
                Attach_image_line.setVisibility(View.VISIBLE);
                SetImageBackground(attachement_img, review.getAttatchment_poster());
                attachment_name.setText("video");
            } else {
                Comment_image_layout.setVisibility(View.GONE);
                Attach_image_line.setVisibility(View.GONE);
            }
        } else {
            Comment_image_layout.setVisibility(View.GONE);
            Attach_image_line.setVisibility(View.GONE);
        }


        upload_comment_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", true);
                startActivityForResult(intent, 1200);
            }
        });


        question_detail_character_counter.setText("Max. " + comment_one_comment_text.getText().toString().trim().length() + "/500 Characters");
        dialog.setView(main_dialog);
        HideKeyboard((AppCompatActivity) main_dialog.getContext());
        return dialog;
    }

    public void SetImageBackground(final ImageView imageView, String Path) {

        Glide.with(imageView.getContext())
                .load(images_baseurl + Path)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.image_plaecholder_bg)
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
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        Log.d("ready", model);
//                        try {
//                            Drawable d = resource;
//                            Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
//                            bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
//                            int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
//                            Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
//                            BitmapDrawable drawable = new BitmapDrawable(imageView.getContext().getResources(), bitmap);
//                            imageView.setImageDrawable(null);
//                            imageView.setBackground(drawable);
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
//                        return false;
//                    }
//                })
                .into(400, 400);

    }

    public void SetImageDrawable(final ImageView imageView, String Path) {


        Glide.with(imageView.getContext())
                .load(images_baseurl + Path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.image_plaecholder_bg)
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
                }).into(400, 400);


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
                    video_processing.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "pd");
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            new BudzEditReviewAlertDialog.VideoProcessing().execute(filePath);
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
                                attachement_img.setBackground(new BitmapDrawable(getContext().getResources(), video_thumbnil));
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
                Drawable drawable = new BitmapDrawable(getContext().getResources(), bitmap_plc);
                attachement_img.setBackground(new BitmapDrawable(getContext().getResources(), video_thumbnil));
            }
        }
    }

    public Drawable GetRounderDarbable(Bitmap bitmapOrg) {
        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
        return new BitmapDrawable(getContext().getResources(), bitmap);
    }
}