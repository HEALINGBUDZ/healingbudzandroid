package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import com.codingpixel.healingbudz.DataModel.HealingBudGalleryModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.model.APIResponseListner;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.delete_hb;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;


public class HealingBudGalleryRecylerAdapter extends RecyclerView.Adapter<HealingBudGalleryRecylerAdapter.ViewHolder> implements APIResponseListner {
    private LayoutInflater mInflater;
    Context context;
    private ArrayList<HealingBudGalleryModel> images = new ArrayList<>();
    private HealingBudGalleryRecylerAdapter.ItemClickListener mClickListener;
    int userId = 0;

    public HealingBudGalleryRecylerAdapter(Context context, ArrayList<HealingBudGalleryModel> images) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.images = images;
    }

    public HealingBudGalleryRecylerAdapter(Context context, ArrayList<HealingBudGalleryModel> images, int User_id) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.images = images;
        userId = User_id;
    }

    // inflates the cell layout from xml when needed
    @Override
    public HealingBudGalleryRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.budz_map_images_recyler_view_item, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 4;
        view.setLayoutParams(lp);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final HealingBudGalleryRecylerAdapter.ViewHolder holder, final int position) {
        String Path = "";
        if (userId == user.getUser_id()) {
            holder.delete_layout.setVisibility(View.VISIBLE);
        } else {
            holder.delete_layout.setVisibility(View.GONE);
        }

        holder.delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                images.get(position).getV_pk()
                if (mClickListener != null) mClickListener.onItemDelete(v, position);
//                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("Are you sure?")
//                        .setContentText("You want to delete this image?")
//                        .setConfirmText("Yes, delete it!")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                sweetAlertDialog.dismissWithAnimation();
//                                JSONObject jsonObject = new JSONObject();
//                                new VollyAPICall(context, false, delete_hb_gallery + "/" + images.get(position).getV_pk(), jsonObject, user.getSession_key(), Request.Method.GET, HealingBudGalleryRecylerAdapter.this, delete_hb);
//                                images.remove(position);
//                                notifyDataSetChanged();
//                            }
//                        })
//                        .setCancelText("Close!")
//                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                sweetAlertDialog.dismissWithAnimation();
//                            }
//                        })
//                        .show();
            }
        });
        if (images.get(position).getType().equalsIgnoreCase("image")) {
            holder.Video_icon.setVisibility(View.GONE);
            holder.loading_spinner_rs.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(images_baseurl + images.get(position).getPath())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.image_plaecholder_bl)
                    .error(R.drawable.noimage)
                    .centerCrop()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.loading_spinner_rs.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.loading_spinner_rs.setVisibility(View.INVISIBLE);
//                            holder.imageView.setImageDrawable(resource);
                            return false;
                        }
                    }).into(holder.imageView);
        } else {
            holder.Video_icon.setVisibility(View.VISIBLE);
            holder.loading_spinner_rs.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(images_baseurl + images.get(position).getPoster())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_play_attachment)
                    .error(R.drawable.noimage)
                    .centerCrop()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.loading_spinner_rs.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.loading_spinner_rs.setVisibility(View.INVISIBLE);
//                            holder.imageView.setImageDrawable(resource);
                            return false;
                        }
                    }).into(holder.imageView);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return images.size();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == delete_hb) {
            CustomeToast.ShowCustomToast(getContext(), "Deleted Successfully!", Gravity.TOP);
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        if (apiActions == delete_hb) {
            CustomeToast.ShowCustomToast(getContext(), "Could not delete server error!", Gravity.TOP);
        }
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        ImageView Video_icon;
        ProgressBar loading_spinner_rs;
        LinearLayout delete_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            loading_spinner_rs = itemView.findViewById(R.id.loading_spinner_rs);
            delete_layout = itemView.findViewById(R.id.delete_layout);
            Video_icon = itemView.findViewById(R.id.attachment_one_video);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(HealingBudGalleryRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onItemDelete(View view, int position);
    }
}