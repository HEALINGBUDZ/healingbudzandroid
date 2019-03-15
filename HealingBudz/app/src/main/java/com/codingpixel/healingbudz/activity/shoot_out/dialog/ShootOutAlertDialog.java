package com.codingpixel.healingbudz.activity.shoot_out.dialog;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.codingpixel.healingbudz.DataModel.ShootOutDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity.strainDataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareHBContent;

public class ShootOutAlertDialog extends BaseDialogFragment<ShootOutAlertDialog.OnDialogFragmentClickListener> implements APIResponseListner {
    boolean isLike = false;
    boolean isShare = false;
    TextView Received_From_Name, title;
    ImageView User_Image;
    ImageView Image_Shout, image_location_pin;
    RelativeLayout Image_Location;
    TextView Times_Ago, message, validity, distance;
    static OnDialogFragmentClickListener Listener;
    static ShootOutDataModel dataModel;

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("respisne", response);
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("respisne", response);
    }

    public interface OnDialogFragmentClickListener {
        public void onViewSpecialBtnClink(ShootOutAlertDialog dialog, ShootOutDataModel dataModel);
    }

    public static ShootOutAlertDialog newInstance(OnDialogFragmentClickListener listner, ShootOutDataModel shout_dataModel) {
        ShootOutAlertDialog frag = new ShootOutAlertDialog();
        dataModel = shout_dataModel;
        Bundle args = new Bundle();
        frag.setArguments(args);
        Listener = listner;
        return frag;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.shoot_out_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
//        int top_y = getResources().getDisplayMetrics().heightPixels / 5;
//        wmlp.y = top_y;   //y position
        Button Learn_more = main_dialog.findViewById(R.id.learn_more_btn);
        final ImageView premium_tag = main_dialog.findViewById(R.id.premium_tag);
        title = main_dialog.findViewById(R.id.title);

        ImageView Cross_btn = main_dialog.findViewById(R.id.cross_btn);
        Cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button view_Special = main_dialog.findViewById(R.id.view_special_btn);
        view_Special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener.onViewSpecialBtnClink(ShootOutAlertDialog.this, dataModel);
            }
        });

        final TextView share_text = main_dialog.findViewById(R.id.share_text);
        final TextView like_text = main_dialog.findViewById(R.id.like_text);

        if (dataModel.getUser_id() == Splash.user.getUser_id()) {
            title.setText("You have created Shout Out");
        }
        final ImageView Like_Img = main_dialog.findViewById(R.id.like_img);
        final ImageView Share_Img = main_dialog.findViewById(R.id.share_img);

        LinearLayout Like_layout = main_dialog.findViewById(R.id.like_layout);
        if (dataModel.getUserlike_count() == 0) {
            Like_Img.setImageResource(R.drawable.ic_unlike_icon_gray);
        } else {
            Like_Img.setImageResource(R.drawable.shoot_out_like);
        }

        Share_Img.setImageResource(R.drawable.ic_share_black_24dp);
//        if (SharedPrefrences.getInt("shoot_share_time", getContext()) == 0) {
//            Share_Img.setImageResource(R.drawable.ic_share_black_24dp);
//        } else {
//            Share_Img.setImageResource(R.drawable.ic_share_shoot_out_putpole);
//        }

        like_text.setText(dataModel.getLikes_count() + " Likes");
        Like_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("shout_out_id", dataModel.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dataModel.getUserlike_count() == 1) {
                    dataModel.setLikes_count(dataModel.getLikes_count() - 1);
                    like_text.setText(dataModel.getLikes_count() + " Likes");
                    Like_Img.setImageResource(R.drawable.ic_unlike_icon_gray);
                    dataModel.setUserlike_count(0);
                    try {
                        jsonObject.put("is_liked", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    dataModel.setLikes_count(dataModel.getLikes_count() + 1);
                    like_text.setText(dataModel.getLikes_count() + " Likes");
                    dataModel.setUserlike_count(1);
                    Like_Img.setImageResource(R.drawable.shoot_out_like);
                    Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.single_shaking);
                    Like_Img.startAnimation(startAnimation);
                    try {
                        jsonObject.put("is_liked", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                new VollyAPICall(getContext(), false, URL.like_shout_out, jsonObject, user.getSession_key(), Request.Method.POST, ShootOutAlertDialog.this, APIActions.ApiActions.testAPI);

            }
        });

//        share_text.setText("Shared " + SharedPrefrences.getInt("shoot_share_time", getContext()) + " times");
        share_text.setText("Shared");
        LinearLayout Share_layout = main_dialog.findViewById(R.id.share_layout);
        Share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (isShare) {
//                    SharedPrefrences.setInt("shoot_share_time", SharedPrefrences.getInt("shoot_share_time", getContext()) - 1, getContext());
//                    share_text.setText("Shared " + SharedPrefrences.getInt("shoot_share_time", getContext()) + " times");
//                    isShare = false;
//                    Share_Img.setImageResource(R.drawable.ic_share_black_24dp);
//                } else {
//                    SharedPrefrences.setInt("shoot_share_time", SharedPrefrences.getInt("shoot_share_time", getContext()) + 1, getContext());
//                    share_text.setText("Shared " + SharedPrefrences.getInt("shoot_share_time", getContext()) + " times");
//                    isShare = true;
//                    Share_Img.setImageResource(R.drawable.ic_share_shoot_out_putpole);
//
//                    JSONObject object = new JSONObject();
//                    try {
//                        object.put("msg", "Hey Bud! Check out the Healing Budz app for your smart phone.” Download it today from http://139.162.37.73/healingbudz/shout-outs");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    ShareHBContent(getActivity(), object);
//                }

                JSONObject object = new JSONObject();
                try {
                    object.put("id", dataModel.getId());
                    object.put("type", "Budz Special");
                    object.put("content", dataModel.getTitle());
                    object.put("url", URL.sharedBaseUrl + "/get-shoutout/" + dataModel.getId());
                    object.put("msg", "Check out Healing Budz for your smartphone: " + URL.sharedBaseUrl + "/get-shoutout/" + dataModel.getId());
                    //http://139.162.37.73/healingbudz/get-shoutout/383
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareHBContent(getActivity(), object);

            }
        });

        Received_From_Name = main_dialog.findViewById(R.id.received_from);
//        Received_From_Name.setAutoLinkMask(0);
        Received_From_Name.setText(dataModel.getReceived_From());

        User_Image = main_dialog.findViewById(R.id.user_img);
        final ImageView special_pic = main_dialog.findViewById(R.id.special_pic);
        if (dataModel.getUser_Image_Path().length() > 0) {
            String path = "";

            if (dataModel.getUser_Image_Path().contains("facebook.com") ||dataModel.getUser_Image_Path().contains("http") ||dataModel.getUser_Image_Path().contains("https") || dataModel.getUser_Image_Path().contains("google.com") || dataModel.getUser_Image_Path().contains("googleusercontent.com"))
                path = dataModel.getUser_Image_Path();
            else {
                path = images_baseurl + dataModel.getUser_Image_Path();
            }
            Glide.with(getContext())
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_budz_adn).error(R.drawable.ic_budz_adn)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if (ShootOutAlertDialog.this.isVisible()) {
                                User_Image.setImageDrawable(resource);
                            }
                            return false;
                        }
                    })
                    .into(User_Image);
        } else {
            User_Image.setImageDrawable(User_Image.getContext().getResources().getDrawable(R.drawable.ic_budz_adn));
        }
        if (dataModel.getSpecial_icon().length() > 6) {
            special_pic.setVisibility(View.VISIBLE);
            String path = images_baseurl + dataModel.getSpecial_icon();
            Glide.with(getContext())
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.topi_ic).error(R.drawable.noimage)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if (ShootOutAlertDialog.this.isVisible()) {
                                special_pic.setImageDrawable(resource);
                            }
                            return false;
                        }
                    })
                    .into(special_pic);
        } else {
            special_pic.setVisibility(View.GONE);
        }

        Times_Ago = main_dialog.findViewById(R.id.time_agoo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date mDate = sdf.parse(dataModel.getUpdated_at());
            long timeInMilliseconds = mDate.getTime();
//            String agoo = getTimeAgo(timeInMilliseconds);
            Times_Ago.setText(DateConverter.getPrettyTime(dataModel.getUpdated_at()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        validity = main_dialog.findViewById(R.id.validity);
        if (DateConverter.convertDateForShooutOut(dataModel.getValidity_date()).contains("Expired")) {
            validity.setAllCaps(true);
            validity.setText("Expired");
            validity.setTextColor(Color.parseColor("#c24a68"));

        } else if (DateConverter.convertDateForShooutOut(dataModel.getValidity_date()).contains("Expire Soon!")) {
//            String text = "<b ><font color=#c24a68>Expire Soon! </font> </b>" + DateConverter.convertDateShootOut(dataModel.getValidity_date());
            String text = "Expire Soon!" + DateConverter.convertDateShootOut(dataModel.getValidity_date());
            validity.setAllCaps(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                validity.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
            } else {
                validity.setText(Html.fromHtml(text));
            }
        } else {
            validity.setText("Valid until : " + DateConverter.convertDateShootOut(dataModel.getValidity_date()));
        }
//        validity.setText("Valid until : " + DateConverter.convertDateForShooutOut(dataModel.getValidity_date()));
        message = main_dialog.findViewById(R.id.message);
//        message.setAutoLinkMask(0);
        message.setText(dataModel.getMessage());
        distance = main_dialog.findViewById(R.id.distance);
        distance.setText(String.format("%.2f", Double.parseDouble(dataModel.getDistance())) + " Miles away");

        Image_Location = main_dialog.findViewById(R.id.image_location);
        image_location_pin = main_dialog.findViewById(R.id.image_location_pin);
        if (dataModel.getPublic_location().length() > 3) {
            Image_Location.setVisibility(View.VISIBLE);
            String url = "http://maps.google.com/maps/api/staticmap?center=" + dataModel.getLat() + "," + dataModel.getLng() + "&zoom=19&size=1080x200&sensor=false";//&markers=color:green
            Glide.with(getContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Image_Location.setBackground(resource);
                            image_location_pin.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }).into(1080, 1080);

        } else {
            Image_Location.setVisibility(View.GONE);
            Image_Location.setBackground(getContext().getDrawable(R.drawable.map_border_shoot_out));
        }
//        13114
        Image_Shout = main_dialog.findViewById(R.id.image_shout);
        if (dataModel.getImage().length() > 8) {
            Image_Shout.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(images_baseurl + dataModel.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_profile_blue)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("ready", model);
                            Image_Shout.setBackground(resource);
                            return false;
                        }
                    })
                    .into(1080, 1080);
        } else {
            Image_Shout.setVisibility(View.GONE);
        }
        dialog.setView(main_dialog);
        return dialog;
    }
}