package com.codingpixel.healingbudz.adapter;

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

import com.codingpixel.healingbudz.DataModel.BudzMapDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;


public class UserProfileBudzMapRecylerAdapter extends RecyclerView.Adapter<UserProfileBudzMapRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<BudzMapDataModel> mData = new ArrayList<>();
    private UserProfileBudzMapRecylerAdapter.ItemClickListener mClickListener;

    public UserProfileBudzMapRecylerAdapter(Context context, ArrayList<BudzMapDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    public UserProfileBudzMapRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.user_profile_budz_map_recyler_view_item_layout, parent, false);
        UserProfileBudzMapRecylerAdapter.ViewHolder viewHolder = new UserProfileBudzMapRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(UserProfileBudzMapRecylerAdapter.ViewHolder holder, final int position) {
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

        if (position > 2) {
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
        holder.Main_Icon.setImageResource(data.getIcon());
        switch (data.getType()) {
            case 0:
                holder.type.setText("Dispensary");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_dispancry_icon);
                break;
            case 1:
                holder.type.setText("Medical");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_medical_icon);
                break;
            case 2:
                holder.type.setText("Cannabites");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_cannabites_icon);
                break;
            case 3:
                holder.type.setText("Entertainment");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_entertainment_icon);
                break;
            case 4:
                holder.type.setText("Events");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_events_icon);
                break;
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
        TextView type;

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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(UserProfileBudzMapRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}