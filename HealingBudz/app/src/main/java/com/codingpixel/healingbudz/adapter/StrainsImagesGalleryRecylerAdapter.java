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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.StrainDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.splash.Splash;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;


public class StrainsImagesGalleryRecylerAdapter extends RecyclerView.Adapter<StrainsImagesGalleryRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<StrainDataModel.Images> mData = new ArrayList<>();
    private StrainsImagesGalleryRecylerAdapter.ItemClickListener mClickListener;

    public StrainsImagesGalleryRecylerAdapter(Context context, ArrayList<StrainDataModel.Images> images) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = images;
    }

    // inflates the cell layout from xml when needed
    @Override
    public StrainsImagesGalleryRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.budz_map_images_recyler_view_item, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 3;
        view.setLayoutParams(lp);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final StrainsImagesGalleryRecylerAdapter.ViewHolder holder, final int position) {
        holder.loading_spinner_rs.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(images_baseurl + mData.get(position).getImage_path())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.noimage)
                .placeholder(R.drawable.image_plaecholder_bg)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.loading_spinner_rs.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.loading_spinner_rs.setVisibility(View.INVISIBLE);
//                        holder.img.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(holder.img);
        if (mData.get(position).getUser_id() == Splash.user.getUser_id()){
            holder.delete_layout.setVisibility(View.VISIBLE);
        }else {
            holder.delete_layout.setVisibility(View.GONE);
        }
        if (mData.get(position).getIs_approved() == 0){
            holder.img_place.setVisibility(View.VISIBLE);
        }else {
            holder.img_place.setVisibility(View.GONE);
        }
        holder.delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null)
                    mClickListener.onDeleteClick(v, position);
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
        ImageView img;
        ImageView img_place;
        LinearLayout delete_layout;
        public ProgressBar loading_spinner_rs;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            delete_layout = itemView.findViewById(R.id.delete_layout);
            img_place = itemView.findViewById(R.id.img_place);
            loading_spinner_rs = itemView.findViewById(R.id.loading_spinner_rs);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition(), img.getDrawable());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(StrainsImagesGalleryRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, Drawable drawable);
        void onDeleteClick(View view, int position);
    }
}