package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;

public class ProfilePhotoAvatreRecylerAdapter extends RecyclerView.Adapter<ProfilePhotoAvatreRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<String> mData = new ArrayList<>();
    private ProfilePhotoAvatreRecylerAdapter.ItemClickListener mClickListener;
    Context context;
    Boolean isTopi;

    public ProfilePhotoAvatreRecylerAdapter(Context context, ArrayList<String> data, boolean isTopi) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.isTopi = isTopi;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ProfilePhotoAvatreRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.photo_avater_recyler_view_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ProfilePhotoAvatreRecylerAdapter.ViewHolder holder, final int position) {
        //set image here
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(holder.itemView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);// = 5f;
        circularProgressDrawable.setCenterRadius(30f);// = 30f;
        circularProgressDrawable.start();
        Glide.with(context)
                .load(images_baseurl + mData.get(position))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(circularProgressDrawable)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.Avatr_Img.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(holder.Avatr_Img);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return this.mData.size();
    }

//    public void perFormedFirstClick(int position) {
//        mClickListener.onItemClick(view, Avatr_Img.getDrawable(), position);
//    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        com.codingpixel.healingbudz.customeUI.CircularImageView Avatr_Img;

        public ViewHolder(View itemView) {
            super(itemView);
            Avatr_Img = itemView.findViewById(R.id.avtr_img);
            Avatr_Img.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (!isTopi) {
                if (mClickListener != null)
                    mClickListener.onItemClick(view, Avatr_Img.getDrawable(), getAdapterPosition());
            } else {
                if (mClickListener != null)
                    mClickListener.onTopiClicked(view, Avatr_Img.getDrawable(), getAdapterPosition());
            }
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ProfilePhotoAvatreRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, Drawable drawable, int position);

        void onTopiClicked(View view, Drawable drawable, int position);
    }
}