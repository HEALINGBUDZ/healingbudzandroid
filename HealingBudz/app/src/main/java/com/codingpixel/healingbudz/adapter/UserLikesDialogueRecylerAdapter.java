package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.Like;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class UserLikesDialogueRecylerAdapter extends RecyclerView.Adapter<UserLikesDialogueRecylerAdapter.ViewHolder> implements APIResponseListner {
    private LayoutInflater mInflater;
    Context context;
    List<Like> mData = new ArrayList<>();
    private UserLikesDialogueRecylerAdapter.ItemClickListener mClickListener;

    public UserLikesDialogueRecylerAdapter(Context context, List<Like> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public UserLikesDialogueRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.likes_view_item, parent, false);
//        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
//        lp.height = parent.getMeasuredHeight() / 6;
//        view.setLayoutParams(lp);
        UserLikesDialogueRecylerAdapter.ViewHolder viewHolder = new UserLikesDialogueRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mData.get(position).getUser() != null && mData.get(position).getUser().getFirstName() != null) {
            holder.Name.setText(mData.get(position).getUser().getFirstName());
            holder.Name.setTextColor(Color.parseColor(Utility.getBudColor(mData.get(position).getUser().getPoints())));
            if (mData.get(position).getUser().getSpecial_icon().length() > 6) {
                holder.profile_img_topi.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(mData.get(position).getUser().getSpecial_icon())
                        .placeholder(R.drawable.ic_user_person)
                        .centerCrop()
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
                        }).into(holder.profile_img_topi);
            } else {
                holder.profile_img_topi.setVisibility(View.GONE);
            }

            if (mData.get(position).getUser().getImagePath().length() > 8) {
                Glide.with(context)
                        .load(mData.get(position).getUser().getImagePath())
                        .placeholder(R.drawable.ic_user_person)
                        .centerCrop()
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Log.d("ready", model);
                                holder.img_user.setImageDrawable(resource);
                                return false;
                            }
                        }).into(420, 420);
            } else {
                Glide.with(context)
                        .load(mData.get(position).getUser().getAvatar())
                        .placeholder(R.drawable.ic_user_person)
                        .centerCrop()
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Log.d("ready", model);
                                holder.img_user.setImageDrawable(resource);
                                return false;
                            }
                        }).into(420, 420);

            }
        } else {
            holder.Name.setText(Splash.user.getFirst_name());
            holder.Name.setTextColor(Color.parseColor(Utility.getBudColor(Splash.user.getPoints())));
            if (Splash.user.getSpecial_icon().length() > 6) {
                holder.profile_img_topi.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(URL.images_baseurl + Splash.user.getSpecial_icon())
                        .placeholder(R.drawable.ic_user_person)
                        .centerCrop()
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
                        }).into(holder.profile_img_topi);
            } else {
                holder.profile_img_topi.setVisibility(View.GONE);
            }

            if (Splash.user.getImage_path().length() > 8) {
                Glide.with(context)
                        .load(URL.images_baseurl + Splash.user.getImage_path())
                        .placeholder(R.drawable.ic_user_person)
                        .centerCrop()
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Log.d("ready", model);
                                holder.img_user.setImageDrawable(resource);
                                return false;
                            }
                        }).into(420, 420);
            } else {
                Glide.with(context)
                        .load(URL.images_baseurl + Splash.user.getAvatar())
                        .placeholder(R.drawable.ic_user_person)
                        .centerCrop()
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Log.d("ready", model);
                                holder.img_user.setImageDrawable(resource);
                                return false;
                            }
                        }).into(420, 420);

            }
        }
    }

    public void SetImageDrawable(final ImageView imageView, String Path) {
//        Glide.with(context)
//                .load(images_baseurl + Path)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .placeholder(R.drawable.image_plaecholder_bg)
//                .centerCrop()
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        Log.d("ready", model);
//                        imageView.setImageDrawable(resource);
//                        return false;
//                    }
//                }).into(imageView);
    }

    // total number of cells
    @Override
    public int getItemCount() {

        return mData.size();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("respisne", response);
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("respisne", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Name, Follow_text;
        LinearLayout Follow_btn;
        LinearLayout F_btn;
        ImageView Follow_icon, img_user, profile_img_topi;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            Name = itemView.findViewById(R.id.name);
            img_user = itemView.findViewById(R.id.img_user);
            profile_img_topi = itemView.findViewById(R.id.profile_img_topi);
//            Follow_text = itemView.findViewById(R.id.follow_text);
//            Follow_btn = itemView.findViewById(R.id.follow_btn);
//            F_btn = itemView.findViewById(R.id.f_btn);
//            Follow_icon = itemView.findViewById(R.id.followe_icon);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(UserLikesDialogueRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}