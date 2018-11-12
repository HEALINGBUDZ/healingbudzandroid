package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;


public class BudzMapImagesGalleryRecylerAdapter extends RecyclerView.Adapter<BudzMapImagesGalleryRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<BudzMapHomeDataModel.Images> mData;
    private BudzMapImagesGalleryRecylerAdapter.ItemClickListener mClickListener;

    public BudzMapImagesGalleryRecylerAdapter(Context context, ArrayList<BudzMapHomeDataModel.Images> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    public BudzMapImagesGalleryRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.budz_map_images_recyler_view_item, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 3;
        view.setLayoutParams(lp);
        BudzMapImagesGalleryRecylerAdapter.ViewHolder viewHolder = new BudzMapImagesGalleryRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final BudzMapImagesGalleryRecylerAdapter.ViewHolder holder, final int position) {
        holder.pg.setVisibility(View.VISIBLE);
        if (mData.get(position).isLocal()) {
            Glide.with(context)
                    .load(mData.get(position).getPathLocal())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.image_plaecholder_bg)
                    .error(R.drawable.noimage)
                    .centerCrop()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.pg.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            holder.img.setImageDrawable(resource);
                            holder.pg.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.img);
        } else {
            Glide.with(context)
                    .load(images_baseurl + mData.get(position).getImage_path())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.image_plaecholder_bg)
                    .error(R.drawable.noimage)
                    .centerCrop()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.pg.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            holder.img.setImageDrawable(resource);
                            holder.pg.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.img);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        ProgressBar pg;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            pg = itemView.findViewById(R.id.loading_spinner_rs);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition(), img.getDrawable());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(BudzMapImagesGalleryRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, Drawable drawable);
    }
}