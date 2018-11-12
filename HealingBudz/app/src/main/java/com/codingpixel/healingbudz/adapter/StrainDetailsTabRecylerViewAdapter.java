package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.UserStrainDetailsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ViewAllDetailsAddedByUserButtonListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.static_function.IntentFunction;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

public class StrainDetailsTabRecylerViewAdapter extends RecyclerView.Adapter<StrainDetailsTabRecylerViewAdapter.ViewHolder> implements APIResponseListner {
    private LayoutInflater mInflater;
    private StrainDetailsTabRecylerViewAdapter.ItemClickListener mClickListener;
    ViewAllDetailsAddedByUserButtonListner listner;
    Context context;
    ArrayList<UserStrainDetailsDataModel> mData;

    public StrainDetailsTabRecylerViewAdapter(Context context, ViewAllDetailsAddedByUserButtonListner viewAllDetailsAddedByUserButtonListner, ArrayList<UserStrainDetailsDataModel> userStrainDetailsDataModels) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listner = viewAllDetailsAddedByUserButtonListner;
        this.mData = userStrainDetailsDataModels;
    }

    // inflates the cell layout from xml when needed
    @Override
    public StrainDetailsTabRecylerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.strain_detail_tab_recyler_item, parent, false);
        StrainDetailsTabRecylerViewAdapter.ViewHolder viewHolder = new StrainDetailsTabRecylerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final StrainDetailsTabRecylerViewAdapter.ViewHolder holder, final int position) {
//        if (position != 0 && position % 10 == 0) {
////            holder.main_layout_whole.setVisibility(View.GONE);
//            holder.adView.setVisibility(View.VISIBLE);
//        } else {
////            holder.main_layout_whole.setVisibility(View.VISIBLE);
//            holder.adView.setVisibility(View.GONE);
//        }
        holder.adView.setVisibility(View.GONE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date mDate = sdf.parse(mData.get(position).getCreated_at());

            String text = "updated <b><font color=#f4c42f>Description</font> </b><i>  " + DateConverter.getPrettyTime(mData.get(position).getCreated_at()) + "</i>";
            holder.Updated_by_text.setText(Html.fromHtml(text));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MakeKeywordClickableText(holder.Details_Text.getContext(), mData.get(position).getDescription(), holder.Details_Text);
        holder.Like_Count.setText(mData.get(position).getGet_likes_count() + "");
        holder.User_Name.setText(mData.get(position).getUser_name());
//        holder.User_Name.setTextColor(Color.parseColor(Utility.getBudColor(mData.get(position).getUser_point())));
        holder.AllEditsByUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.viewAllEditsButtonClick(position, mData.get(position));
            }
        });
        String pathImage = "";
        if (mData.get(position).getUser_image_path().contains("facebook.com") || mData.get(position).getUser_image_path().contains("http") ||mData.get(position).getUser_image_path().contains("https") ||mData.get(position).getUser_image_path().contains("google.com") || mData.get(position).getUser_image_path().contains("googleusercontent.com")) {
            pathImage = mData.get(position).getUser_image_path();
        } else {
            pathImage = images_baseurl + mData.get(position).getUser_image_path();
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
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.profile_img_topi.setImageDrawable(resource);
                            return false;
                        }
                    }).into(holder.profile_img_topi);
        } else {
            holder.profile_img_topi.setVisibility(View.GONE);

        }
        Glide.with(context)
                .load(pathImage)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.image_plaecholder_bg).error(R.drawable.noimage)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.User_Image.setImageDrawable(resource);
                        return false;
                    }
                }).into(holder.User_Image);
        holder.User_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFunction.GoToProfile(v.getContext(), mData.get(position).getUser_id());
            }
        });

        if (mData.get(position).getGet_user_like_count() == 1) {
            holder.LikeThumb.setImageResource(R.drawable.ic_thumb_licked);
            holder.LikeThumb.setColorFilter(Color.parseColor("#f4c42f"), PorterDuff.Mode.SRC_IN);
            holder.got_ur_vote.setVisibility(View.VISIBLE);
//            holder.Reward_image.setVisibility(View.VISIBLE);

        } else {
            holder.LikeThumb.setImageResource(R.drawable.ic_thumb_unliked);
            holder.LikeThumb.setColorFilter(0);
            holder.got_ur_vote.setVisibility(View.GONE);
            holder.Reward_image.setVisibility(View.GONE);
        }

        holder.LikeThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                if (mData.get(position).getUser_id() != Splash.user.getUser_id()) {
                    if (mData.get(position).getGet_user_like_count() == 1) {
                        Like(mData.get(position).getId(), 0);
                        mData.get(position).setGet_user_like_count(0);
                        holder.LikeThumb.setImageResource(R.drawable.ic_thumb_unliked);
                        holder.LikeThumb.setColorFilter(0);
                        mData.get(position).setGet_likes_count(mData.get(position).getGet_likes_count() - 1);
                        holder.got_ur_vote.setVisibility(View.GONE);
                        holder.Reward_image.setVisibility(View.GONE);
                        try {
                            jsonObject.put("user_strain_id", mData.get(position).getId());
                            jsonObject.put("is_like", 0);
                            jsonObject.put("strain_id", mData.get(position).getStrain_id());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Like(mData.get(position).getId(), 1);
                        mData.get(position).setGet_user_like_count(1);
                        holder.LikeThumb.setImageResource(R.drawable.ic_thumb_licked);
                        holder.LikeThumb.setColorFilter(Color.parseColor("#f4c42f"), PorterDuff.Mode.SRC_IN);
                        mData.get(position).setGet_likes_count(mData.get(position).getGet_likes_count() + 1);
                        holder.got_ur_vote.setVisibility(View.VISIBLE);
//                    holder.Reward_image.setVisibility(View.VISIBLE);
                        setOtherLikeFalse(position, 0);
                        try {
                            jsonObject.put("user_strain_id", mData.get(position).getId());
                            jsonObject.put("is_like", 1);
                            jsonObject.put("strain_id", mData.get(position).getStrain_id());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    holder.Like_Count.setText(mData.get(position).getGet_likes_count() + "");
                    new VollyAPICall(context, false, URL.save_user_strain_like, jsonObject, user.getSession_key(), Request.Method.POST, StrainDetailsTabRecylerViewAdapter.this, APIActions.ApiActions.testAPI);
                } else {
                    CustomeToast.ShowCustomToast(getContext(), "You can't like your own info!", Gravity.TOP);
                }
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) mClickListener.onItemClick(v, position);
            }
        });
    }
//    }

    void setOtherLikeFalse(int position, int like) {
        for (int i = 0; i < mData.size(); i++) {
            if (i != position) {
                if (mData.get(i).getGet_user_like_count() == 1) {
                    mData.get(i).setGet_likes_count(mData.get(i).getGet_likes_count() - 1);
                }
                mData.get(i).setGet_user_like_count(0);
            }
        }
        notifyDataSetChanged();
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
        TextView Updated_by_text;
        LinearLayout got_ur_vote;
        TextView AllEditsByUser;
        TextView Details_Text;
        ImageView Reward_image, User_Image, profile_img_topi, LikeThumb;
        TextView Like_Count, User_Name;
        public LinearLayout main_layout_whole;
        AdView adView;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            Updated_by_text = itemView.findViewById(R.id.updated_by);
            got_ur_vote = itemView.findViewById(R.id.got_ur_vote);
            Reward_image = itemView.findViewById(R.id.reward_like);
            Details_Text = itemView.findViewById(R.id.detail_text);
            AllEditsByUser = itemView.findViewById(R.id.all_edits);
            Like_Count = itemView.findViewById(R.id.like_count);
            User_Name = itemView.findViewById(R.id.user_name);
            User_Image = itemView.findViewById(R.id.user_img);
            profile_img_topi = itemView.findViewById(R.id.profile_img_topi);
            LikeThumb = itemView.findViewById(R.id.like_thumb);
            main_layout_whole = itemView.findViewById(R.id.main_layout_whole);
            adView = itemView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(itemView.getContext().getString(R.string.ad_mob_id_did)).build();
            adView.loadAd(adRequest);
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
    public void setClickListener(StrainDetailsTabRecylerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private void Like(int id, int islike) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("user_strain_id", id);
//            jsonObject.put("is_like", islike);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        new VollyAPICall(context, false, URL.save_user_strain_like, jsonObject, user.getSession_key(), Request.Method.POST, StrainDetailsTabRecylerViewAdapter.this, APIActions.ApiActions.testAPI);
    }
}