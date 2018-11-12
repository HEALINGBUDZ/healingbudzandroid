package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.User;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;

public class UserRecylerAdapter extends RecyclerView.Adapter<UserRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<User> mData;
    private UserRecylerAdapter.ItemClickListener mClickListener;

    public UserRecylerAdapter(Context context, ArrayList<User> main_data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = main_data;
    }

    // inflates the cell layout from xml when needed
    @Override
    public UserRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.user_recycle_item, parent, false);
        UserRecylerAdapter.ViewHolder viewHolder = new UserRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String icon_path = "", Name = "";
        if (mData.get(position).getImage_path() != null && mData.get(position).getImage_path().length() > 8) {
            if (mData.get(position).getImage_path().contains("facebook.com") || mData.get(position).getImage_path().contains("https") || mData.get(position).getImage_path().contains("https") || mData.get(position).getImage_path().contains("google.com") || mData.get(position).getImage_path().contains("googleusercontent.com"))
                icon_path = mData.get(position).getImage_path();
            else {
                icon_path = images_baseurl + mData.get(position).getImage_path();
            }
        } else {
            icon_path = images_baseurl + mData.get(position).getAvatar();
        }
        Name = mData.get(position).getFirst_name() + " " + mData.get(position).getLast_name();
        holder.user_name.setText(Name);
        Glide.with(context)
                .load(icon_path)
                .asBitmap()
                .placeholder(R.drawable.ic_user_person)
                .error(R.drawable.ic_user_person)
                .into(holder.user_image);
        if (mData.get(position).getSpecial_icon().length() > 6) {
            holder.profile_img_topi.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(images_baseurl + mData.get(position).getSpecial_icon())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.topi_ic).error(R.drawable.topi_ic)
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
        holder.follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) mClickListener.onItemEditClick(v, position);
            }
        });

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView user_name;
        RelativeLayout follow_btn;
        ImageView user_image, profile_img_topi;

        public ViewHolder(View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            follow_btn = itemView.findViewById(R.id.follow_btn);
            user_image = itemView.findViewById(R.id.user_image);
            profile_img_topi = itemView.findViewById(R.id.profile_img_topi);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(UserRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onItemDeleteClick(View view, int position);

        void onItemEditClick(View view, int position);
    }
}