package com.codingpixel.healingbudz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.BudzMapDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;


public class BudzMapRecylerAdapterForProfile extends RecyclerView.Adapter<BudzMapRecylerAdapterForProfile.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<BudzMapDataModel> mData = new ArrayList<>();
    private HomeSearchListRecylerAdapter.ItemClickListener mClickListener;
    Context context;
    public BudzMapRecylerAdapterForProfile(Context context, ArrayList<BudzMapDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public BudzMapRecylerAdapterForProfile.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.budz_map_recyler_view_item_layout, parent, false);
        BudzMapRecylerAdapterForProfile.ViewHolder viewHolder = new BudzMapRecylerAdapterForProfile.ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final BudzMapRecylerAdapterForProfile.ViewHolder holder, final int position) {
        BudzMapDataModel data = mData.get(position);
        if (data.isFeatureBushiness()) {
            holder.main_layout.setBackgroundColor(Color.parseColor("#000000"));
            holder.Featured_business_layout.setVisibility(View.VISIBLE);
//            holder.rating_imgs.setImageResource(R.drawable.ic_rating_img);
        } else {
            holder.main_layout.setBackgroundColor(Color.parseColor("#232323"));
            holder.Featured_business_layout.setVisibility(View.GONE);
//            holder.rating_imgs.setImageResource(R.drawable.ic_rating_img_2);
        }

        if (!mData.get(position).isFeatureBushiness()) {
            holder.doted_line.setImageDrawable(null);
            holder.doted_line.setBackgroundColor(Color.parseColor("#3c3c3c"));
        } else {
            holder.doted_line.setImageResource(R.drawable.doted_line);
            holder.doted_line.setBackgroundColor(Color.parseColor("#00000000"));
        }

        if (data.isOrganic()) {
            holder.Organic.setVisibility(View.VISIBLE);
        } else {
            holder.Organic.setVisibility(View.GONE);
        }

        if (data.isDelivery()) {
            holder.Delivery.setVisibility(View.VISIBLE);
        } else {
            holder.Delivery.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(images_baseurl + mData.get(position).getLogo())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_profile_blue)
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        Log.d("ready", e.getMessage());
//                        return false;
//                    }
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        Log.d("ready", model);
//                        holder.Main_Icon.setImageDrawable(resource);
//                        return false;
//                    }
//                })
                .into( holder.Main_Icon);
        switch (data.getBusiness_type_id()) {
            case 1:
                holder.type.setText("Dispensary");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_dispancry_icon);
                break;
            case 2:
                holder.type.setText("Medical");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_medical_icon);
                break;
            case 3:
                holder.type.setText("Cannabites");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_cannabites_icon);
                break;
            case 4:
                holder.type.setText("Entertainment");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_entertainment_icon);
                break;
            case 5:
                holder.type.setText("Events");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_events_icon);
                break;
        }


        holder.Heading_Title.setText(data.getTitle());
        if(mData.get(position).getDistance() != null){
            if(mData.get(position).getDistance().length() > 4){
                @SuppressLint("DefaultLocale") String Distance = String.format("%.01f", Double.parseDouble(mData.get(position).getDistance() + ""));
                holder.distance.setText(Distance + " mi");
            }else {
                holder.distance.setText("0.0" + " mi");
            }
        }else {
            holder.distance.setText("0.0" + " mi");
        }

        holder.reviewss.setText(data.getReviews() + " Reviews");

        if (data.getRating() > 0) {
            if (data.getRating() == 1) {
                holder.rating_img.setImageResource(R.drawable.rating_one);
            } else if (data.getRating() == 2) {
                holder.rating_img.setImageResource(R.drawable.rating_two);
            } else if (data.getRating() == 3) {
                holder.rating_img.setImageResource(R.drawable.rating_three);
            } else if (data.getRating() == 4) {
                holder.rating_img.setImageResource(R.drawable.rating_four);
            } else if (data.getRating() == 5) {
                holder.rating_img.setImageResource(R.drawable.rating_five);
            }
        } else {
            holder.rating_img.setImageResource(R.drawable.rating_zero);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return this.mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout main_layout;
        ImageView doted_line, Main_Icon;
        ImageView rating_imgs;
        LinearLayout Featured_business_layout;
        ImageView Organic, Delivery;
        ImageView BudCategory_small_icon;
        ImageView rating_img;
        TextView type, Heading_Title, distance, reviewss;

        public ViewHolder(View itemView) {
            super(itemView);
            main_layout = itemView.findViewById(R.id.main_layout);
            doted_line = itemView.findViewById(R.id.top_line);
//            rating_imgs = itemView.findViewById(R.id.rating_img);
            Featured_business_layout = itemView.findViewById(R.id.featured_business_layout);
            Organic = itemView.findViewById(R.id.organic_img);
            Delivery = itemView.findViewById(R.id.delivery_img);
            BudCategory_small_icon = itemView.findViewById(R.id.bud_map_catagory_img);
            type = itemView.findViewById(R.id.type);
            Main_Icon = itemView.findViewById(R.id.icon_img);
            Heading_Title = itemView.findViewById(R.id.heading_title);
            distance = itemView.findViewById(R.id.distance);
            reviewss = itemView.findViewById(R.id.reviewss);
            rating_img = itemView.findViewById(R.id.rating_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onGlobalSearchItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(HomeSearchListRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}